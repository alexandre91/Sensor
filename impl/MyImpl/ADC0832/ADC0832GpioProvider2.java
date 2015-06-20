//package com.pi4j.gpio.extension.adc;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ADC0832GpioProvider2.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinAnalog;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;
import java.util.EnumSet;
import java.lang.StackTraceElement;
import java.lang.Exception;

/**
 * 
 * <p>
 * This GPIO provider implements the MCP3008 SPI GPIO expansion board as native Pi4J GPIO pins. It is a 10-bit ADC providing 8 input
 * channels. More information about the board can be found here: http://ww1.microchip.com/downloads/en/DeviceDoc/21295d.pdf
 * </p>
 * 
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO pins that can be used for analog input pins. The
 * values returned are in the range 0-1023 (max 10 bit value).
 * </p>
 * 
 * @author pojd
 */
public class ADC0832GpioProvider2 extends GpioProviderBase {

    // Conversion Delay (in milliseconds)
    protected  static final int CONVERSION_DELAY = 32;

    // Range value of ADC0832 converter (8 bits)
    public static final int ADC0832_MAX_RANGE_VALUE = 256;

    public static final String NAME = "ADC0832GpioProvider2";
    public static final String DESCRIPTION = "ADC0832 GPIO Provider";
    public static final int INVALID_VALUE = -1;


    protected ADCMonitor monitor = null; //monitor thread read analog value from adc
    
//Analog Input pins
    private GpioPinAnalogInput CH0 = null;
    private GpioPinAnalogInput CH1 = null;
    protected GpioPinAnalogInput[] allChannelPins = {CH0, CH1}; 



    // this cache value is used to track last known pin values for raising event
    protected double[] cachedValue = { 0, 0}; // one value for each channel

    // this value defines the sleep time between value reads by the event monitoring thread
    protected int monitorInterval = 100;

    // the threshold used to determine if a significant value warrants an event to be raised
    protected double[] threshold = { 1, 1};

        private GpioPinDigitalOutput CS_PIN; 
    	private GpioPinDigitalOutput CLK_PIN;
	private GpioPinDigitalMultipurpose DIO_PIN;
    	private GpioPinDigitalInput DO_PIN;
    	private GpioPinDigitalOutput DI_PIN;






	public GpioPinAnalogInput createAnalogInputPin(int channel, String name) {

    	    GpioPinAnalogInput pin = GpioFactory.getInstance().provisionAnalogInputPin(this, new PinImpl(ADC0832GpioProvider2.NAME, channel, "ADC0832 CH" + channel, EnumSet.of(PinMode.ANALOG_INPUT)), name);

	    switch (channel){
	        case 0: 
		        this.CH0 = pin;
		        allChannelPins[channel] = this.CH0;
		        break;
	        case 1:
                        this.CH1 = pin;
		        allChannelPins[channel] = this.CH1;
		        break;
		default:
			System.out.println("Invalid ADC channel.");
			return null;
                }

            return pin;
	}


	/**
	 * Create new instance of this ADC0832 provider.
	 * 
	 * @param spiChannel
	 *            spi channel the MCP3008 is connected to
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public ADC0832GpioProvider2(Pin CS, Pin CLK, Pin DIO) {
		//set this as GpioProvider
			

		//provision and set Pins
    		this.CS_PIN = GpioFactory.getInstance().provisionDigitalOutputPin(CS);
		this.CLK_PIN = GpioFactory.getInstance().provisionDigitalOutputPin(CLK);	
		this.DIO_PIN = GpioFactory.getInstance().provisionDigitalMultipurposePin(DIO, PinMode.DIGITAL_OUTPUT); // in case of same pin being used to input and output data to/from ADC
		this.DI_PIN = (GpioPinDigitalOutput)this.DIO_PIN;
		this.DO_PIN = (GpioPinDigitalInput)this.DIO_PIN;
		GpioFactory.setDefaultProvider(this);

		//initialize channel analog input pins
		this.CH0 = null;
		this.CH1 = null; //createAnalogInputPin(1, "ANALOG INPUT 1");

	        // start monitoring thread            
	        monitor = new ADC0832GpioProvider2.ADCMonitor();
	        monitor.start();		

	}

	public ADC0832GpioProvider2(Pin CS, Pin CLK, Pin DI, Pin DO) {
		// not implemented yet
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
    	public double getValue(Pin pin){
		return super.getValue(pin);
    	}


	public double getNewValue(Pin pin){
		// do not return, only let parent handle whether this pin is OK
		try {
		//Exception ex = new Exception();
		//StackTraceElement[] stack = ex.getStackTrace();
		//System.out.println("quem chamou:" + stack[1].getClassName() + "." + stack[1].getMethodName());
			return isInitiated() ? readAnalog((short)pin.getAddress()) : INVALID_VALUE;
		}
                catch (InterruptedException e) { 
                    e.printStackTrace();
                }  
		return INVALID_VALUE;

	}

	private short toCommand(short channel) {
		short command = (short) ((channel + 8) << 4);
		return command;
	}

	private boolean isInitiated() {
		return DO_PIN != null;
	}

	private int readAnalog(short channel) throws InterruptedException{
	    		
		int i;
		int dat1=0, dat2=0;
	
		CS_PIN.low(); //Chip Select setted low to start conversion process. Held low for the entire conversion.
	
		DI_PIN.setMode(PinMode.DIGITAL_OUTPUT); //set PinMode to Digital Output in case Multipurpose  digital pin
		
		// start bit
		DI_PIN.high(); Thread.sleep(2);

		// Channel Select (2 bits MUX Addressing) 1 0 to channel 0, 1 1 to channel 1

		//first clock generated
		CLK_PIN.high(); Thread.sleep(2); 	

		// On each rising edge of the clock the status of the data in (DI) line is clocked into de MUX address shift register.

		// bit 1
		CLK_PIN.low();
		DI_PIN.high(); Thread.sleep(2);
		CLK_PIN.high(); Thread.sleep(2);

		// bit 2
		CLK_PIN.low();	

		//0 to channel 0 , 1 to channel 1
		if (channel == 0){
			DI_PIN.low(); Thread.sleep(2);		
		}
		else if (channel == 1){
			DI_PIN.high(); Thread.sleep(2);
		}

		CLK_PIN.high();

		//clock pin low to start conversion
		CLK_PIN.low();		
		
		for(i=0;i<8;i++)
		{
			CLK_PIN.high();	Thread.sleep(2);
			CLK_PIN.low();    Thread.sleep(2);
	
			DO_PIN.setMode(PinMode.DIGITAL_INPUT);
			dat1=dat1<<1 | DO_PIN.getState().getValue();
		}
		
		for(i=0;i<8;i++)
		{
			dat2 = dat2 | (DO_PIN.getState().getValue()<<i);
			CLK_PIN.high();	Thread.sleep(2);
			CLK_PIN.low();    Thread.sleep(2);
		}
	
		CS_PIN.high();
		return(dat1==dat2) ? dat1 : 0;
	}



    /**
     * This class/thread is used to actively monitor for GPIO interrupts
     * 
     * @author Robert Savage
     * 
     */
    private class ADCMonitor extends Thread {
        
        //private GpioPinAnalogInput pin;
        private boolean shuttingDown = false;

        public ADCMonitor() { //parametro são os canais que devem ser monitorados
            //this.pin = pin;
        }

        public void shutdown() {
            shuttingDown = true;
        }

        public void run() {
            while (!shuttingDown) {
                try { 
                    
                    // determine if there is a pin value difference reading analog value of each channel
                    if(allChannelPins != null && allChannelPins.length > 0){
			int i = 0;
                        for (GpioPinAnalogInput gpioPin : allChannelPins) {
			    if (gpioPin != null) { //only try to get analog value if gpioPin (channel) != null
                                Pin pin = gpioPin.getPin();	
                            //try{                        
                                // get current cached value
                                double oldValue = cachedValue[pin.getAddress()];
                                
                                // get actual value from ADC chip
                                double newValue = getNewValue(pin); 
        
                                // check to see if the pin value exceeds the event threshold
                                if(Math.abs(oldValue - newValue) >= threshold[pin.getAddress()]){
        
                                    // cache new value (both in local event comparison cache variable and pin state cache)
                                    cachedValue[pin.getAddress()] = newValue;
                                    getPinCache(pin).setAnalogValue(newValue);
                                
                                    // only dispatch events for analog input pins
                                    if (getMode(pin) == PinMode.ANALOG_INPUT) {
                                        dispatchPinChangeEvent(pin.getAddress(), newValue);
                                    }
                                }
                                
                                // Wait for the conversion to complete
                                try{
                                    if (ADC0832GpioProvider2.CONVERSION_DELAY > 0){
                                        Thread.sleep(ADC0832GpioProvider2.CONVERSION_DELAY);
                                    }
                                }
                                catch (InterruptedException e) { 
                                    e.printStackTrace();
                                }            
			    } //end if                
                            //}
                            //catch(InterruptedException ex){
                                // I2C read error
                            //}
                        }
                    }

                    // ... lets take a short breather ...
                    Thread.currentThread();
                    Thread.sleep(monitorInterval);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void dispatchPinChangeEvent(int pinAddress, double value) {
            // iterate over the pin listeners map
            for (Pin pin : listeners.keySet()) {
                // dispatch this event to the listener
                // if a matching pin address is found
                if (pin.getAddress() == pinAddress) {
                    // dispatch this event to all listener handlers
                    for (PinListener listener : listeners.get(pin)) {
                        listener.handlePinEvent(new PinAnalogValueChangeEvent(this, pin, value));
                    }
                }
            }
        }
    }
}
