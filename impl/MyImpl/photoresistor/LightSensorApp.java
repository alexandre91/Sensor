//package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  LightSensorApp.java  
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
import com.pi4j.component.sensor.impl.*;
import com.pi4j.component.*;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioController;



public class LightSensorApp {
  private static final int LIMIT = 100;

  public static void main(String[] args) throws InterruptedException {

    //ADC Pins
    final Pin ADC_CS = RaspiPin.GPIO_00;
    final Pin ADC_CLK = RaspiPin.GPIO_01;
    final Pin ADC_DIO = RaspiPin.GPIO_02;

//Create ADC Gpio Provider
    ADC0832GpioProvider2 ADC0832Provider = new ADC0832GpioProvider2(ADC_CS, ADC_CLK, ADC_DIO);

    final GpioPinDigitalOutput ledPin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);


// Create sensor
    PhotoresistorSensorComponent photoresistor = new PhotoresistorSensorComponent(ADC0832Provider.createAnalogInputPin(1, "Light sensor pin")); //channel (analog pin) parameter

//Add Listener
    photoresistor.addListener(new LightSensorListener(){
       public void onLuminosityChange(LightChangeEvent event){
	double luminosity; // luminosity value
	luminosity = event.getLightSensor().getLux();
	System.out.println("Current illumination :" + luminosity + " lux");
	if(luminosity < LIMIT)
	    ledPin.setState(PinState.HIGH);			
	else
	    ledPin.setState(PinState.LOW);
       }
    });


    System.out.println("Light sensor activated.");

	Thread.sleep(600000);

/*
//pooling pattern
    double analogVal;
    while(true){
    	analogVal = gpio.getValue(ADC0832Provider.CH0);
    	illum = 210 - (int)analogVal;
    	System.out.println("Current illumination :" + illum);
	Thread.sleep(500);
    }
*/

  }
}