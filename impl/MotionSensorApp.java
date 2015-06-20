//package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MotionSensorApp.java  
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
import com.pi4j.component.*;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
import com.pi4j.io.gpio.RaspiPin;


public class MotionSensorApp {
  private static int counter = 0;
	private static int counter2 = 0;

  public static void main(String[] args) throws InterruptedException {

    final GpioController gpio = GpioFactory.getInstance(); 
    final GpioPinDigitalInput motionSensorPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05);
    final GpioPinDigitalInput knockSensorPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03);
    final GpioPinDigitalOutput ledPin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);

    GpioMotionSensorComponent pir = new GpioMotionSensorComponent(motionSensorPin);

    pir.addListener(new MotionSensorListener(){
       public void onMotionStateChange(MotionSensorChangeEvent event){
	   if(event.isMotionDetected()){
		ledPin.setState(PinState.getInverseState(ledPin.getState()));
		System.out.println("Motion detected " + ++counter);		
	   }	
       }
    });

    GpioSensorComponent knockSensor = new GpioSensorComponent(knockSensorPin);

    knockSensor.addListener(new SensorListener(){
       
       public void onStateChange(SensorStateChangeEvent event){	
	     if(event.getNewState() == SensorState.CLOSED){
		   System.out.println("Knock detected " + ++counter2);		
	     }	
       }
    });
	System.out.println("Motion sensor activated.");
    System.out.println("Knock sensor activated.");
    while(true);
  }


}