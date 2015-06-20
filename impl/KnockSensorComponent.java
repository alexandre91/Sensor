//package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  KnockSensorComponent.java  
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

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class KnockSensorComponent extends KnockSensorBase {
    
    // internal class members
    private GpioPinDigitalInput pin = null;
    private PinState knockDetectedState = PinState.HIGH;
    private final KnockSensor sensor = this;
    
    // create internal pin listener
    public GpioPinListenerDigital pinListener = new GpioPinListenerDigital() {

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {            
            // notify any knock sensor change listeners
            notifyListeners(new KnockSensorChangeEvent(sensor, (event.getState() == knockDetectedState)));                
        }
    };
    
    /**
     * using this constructor requires that the consumer 
     *  define the KNOCK DETECTED pin state 
     *  
     * @param pin GPIO digital input pin
     * @param knockDetectedState pin state to set when SENSOR DETECTS KNOCK
     */
    public KnockSensorComponent(GpioPinDigitalInput pin, PinState knockDetectedState) {
        this.pin = pin;
        this.knockDetectedState = knockDetectedState;
        
        // add pin listener
        this.pin.addListener(pinListener);
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is KNOCK DETECTED
     *  
     * @param pin GPIO digital input pin
     */
    public KnockSensorComponent(GpioPinDigitalInput pin) {
        this.pin = pin;
        
        // add pin listener
        this.pin.addListener(pinListener); 
    }

    /**
     * Return 'true' if knock is currently detected   
     * based on the GPIO digital input pin state.
     *  
     * @return knock detected status 
     */
    @Override
    public boolean isKnockDetected() {
	return pin.isState(knockDetectedState);
    }
}
