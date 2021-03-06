//package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  JoystickApp2.java  
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

import com.pi4j.component.sensor.*;
import com.pi4j.component.temperature.*;
//import com.pi4j.component.sensor.impl.*;
import com.pi4j.component.*;
import com.pi4j.temperature.TemperatureScale;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioController;

public class JoystickApp2 {


  public static void main(String[] args) throws InterruptedException {    

//ADC Pins
    final Pin ADC_CS = RaspiPin.GPIO_00;
    final Pin ADC_CLK = RaspiPin.GPIO_01;
    final Pin ADC_DIO = RaspiPin.GPIO_02;

//LED Pin
    final GpioPinDigitalOutput ledPin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);

//BUTTON Pin
    final GpioPinDigitalInput SW_PIN = GpioFactory.getInstance().provisionDigitalInputPin(RaspiPin.GPIO_05);


//Create ADC Gpio Provider
    ADC0832GpioProvider ADC0832Provider = new ADC0832GpioProvider(ADC_CS, ADC_CLK, ADC_DIO);

// Create sensor
    JoystickComponent joystick = new JoystickComponent(ADC0832Provider.createAnalogInputPin(0, "X Axis"), 
						       ADC0832Provider.createAnalogInputPin(1, "Y Axis")); //channel (analog pinX, analog pinY) parameter

// Create and Add Sensor Listener for X axys
    joystick.addListenerX(
        new JoystickAxisListener(){   
       		double xVal; 
       		public void onAxisValueChange(JoystickAxisValueChangeEvent event){   // handle event function
			if(joystick.isMovingUp()) {				
				System.out.println("Up");
				ledPin.setState(PinState.LOW);
			}
			else if(joystick.isMovingDown()) {
				System.out.println("Down");
				ledPin.setState(PinState.LOW);
			}
       		}	
    	});
// Create and Add Sensor Listener for Y axys
    joystick.addListenerY(
    	new JoystickAxisListener(){   
       		double yVal; 
       		public void onAxisValueChange(JoystickAxisValueChangeEvent event){   // handle event function
            		yVal = (event.getNewValue());
			if(yVal == 0){
				System.out.println("Right");	
			}
			if(yVal == 255){
				System.out.println("Left");
	    		}	  
       		}	  
     	});

//BUTTON
    GpioSensorComponent joystickButton = new GpioSensorComponent(SW_PIN);

     joystickButton.addListener(new SensorListener(){
       
       public void onStateChange(SensorStateChangeEvent event){	
	     if(event.getNewState() == SensorState.CLOSED){
		   System.out.println("Button is pressed!");		
	     }	
       }
    });

    System.out.println("Joystick Activated");
  }	
}