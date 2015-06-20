//package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  KnockSensorApp.java  
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
import com.pi4j.component.button.*;
import com.pi4j.component.button.impl.*;
import com.pi4j.component.*;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
import com.pi4j.io.gpio.RaspiPin;


public class ButtonExample {

  public static void main(String[] args) throws InterruptedException {

    final GpioPinDigitalInput buttonPin = GpioFactory.getInstance().provisionDigitalInputPin(RaspiPin.GPIO_00);
    //final GpioPinDigitalOutput ledPin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);

    GpioButtonComponent button = new GpioButtonComponent(buttonPin);

    button.addListener(new ButtonPressedListener(){
       
       public void onButtonPressed(ButtonEvent event){	
	     if(event.isPressed()){
		   System.out.println("Button Pressed.");		
	     }	
       }
    });
/*
    button.addListener(new ButtonHoldListener(){
       
       public void onButtonHold(ButtonEvent event){	
	     if(!event.isPressed() && !event.isReleased() ){
		   System.out.println("Button Hold.");		
	     }	
       }
    });

    button.addListener(new ButtonReleasedListener(){
       
       public void onButtonReleased(ButtonEvent event){	
	     if(event.isReleased()){
		   System.out.println("Button Released.");		
	     }	
       }
    });
*/
    System.out.println("Button activated.");
    while(true);
  }
}