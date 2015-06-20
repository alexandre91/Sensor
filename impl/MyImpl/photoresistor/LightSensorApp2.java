//package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  LightSensorApp2.java  
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
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioController;

public class LightSensorApp2 {

  private static int limit = 100;

  public static void main(String[] args) throws InterruptedException {    

//ADC Pins
    final Pin ADC_CS = RaspiPin.GPIO_00;
    final Pin ADC_CLK = RaspiPin.GPIO_01;
    final Pin ADC_DIO = RaspiPin.GPIO_02;

//Create ADC Gpio Provider
    ADC0832GpioProvider2 ADC0832Provider = new ADC0832GpioProvider2(ADC_CS, ADC_CLK, ADC_DIO);

//LED Pin
    final GpioPinDigitalOutput ledPin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.HIGH);

// Create sensor
    AnalogSensorComponent lightSensor = new AnalogSensorComponent(ADC0832Provider.createAnalogInputPin(1, "Light sensor pin")); //channel (analog pin) parameter

// Create and Add Sensor Listener
    lightSensor.addListener(new AnalogSensorListener(){   
       double illum; 
       public void onValueChange(AnalogSensorValueChangeEvent event){   // handle event function
	   illum = (255 - event.getNewValue());
	   System.out.println("Current illumination :" + illum);
	  
           if(illum > limit){
		ledPin.setState(PinState.HIGH);
		//System.out.println("Luminosity higher then " + limit + " lumens ");		
	   }	
	   else
	   {
		ledPin.setState(PinState.LOW);
	   }
       }
    });
    System.out.println("Light sensor activated.");

// Create sensor
    TemperatureSensorComponent temperatureSensor = new TemperatureSensorComponent(ADC0832Provider.createAnalogInputPin(0, "Temperature sensor pin")); //channel (analog pin) parameter

// Create and Add Sensor Listener
    temperatureSensor.addListener(new TemperatureListener(){   
       public void onTemperatureChange(TemperatureChangeEvent event){   // handle event function
           System.out.println("Current temperature: " + event.getTemperatureSensor().getTemperature(TemperatureScale.CELSIUS));	
       }
    });
    System.out.println("Temperature sensor activated.");
    
    for(;;){
	Thread.sleep(5000);
    }
  }
}