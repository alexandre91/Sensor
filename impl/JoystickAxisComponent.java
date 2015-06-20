//package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  JoystickComponent.java  
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

import com.pi4j.component.sensor.AnalogSensor;
import com.pi4j.component.sensor.AnalogSensorBase;
import com.pi4j.component.sensor.AnalogSensorValueChangeEvent;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;

public class JoystickAxisComponent extends JoystickAxisBase {
    
    // internal class members
    private GpioPinAnalogInput pin = null;
    private static int defaltCentralValue = 135;
    private final JoystickAxis sensor = this;
       
    // create internal pin listener for axis
    private GpioPinListenerAnalog pinListener = new GpioPinListenerAnalog() {

        @Override
        public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
            // notify any sensor change listeners
            notifyListeners(new JoystickAxisValueChangeEvent(sensor, getValue(), event.getValue()));
        }
    };
    
    /**
     * default constructor 
     *  
     * @param pin analog input pin
     */
    public JoystickAxisComponent(GpioPinAnalogInput pin) {
	super(JoystickAxisComponent.defaltCentralValue);
        this.pin = pin;
        
        // add pin listener
        this.pin.addListener(pinListener);
    }


    @Override
    public double getValue()
    {
        return pin.getValue();
    }

}
