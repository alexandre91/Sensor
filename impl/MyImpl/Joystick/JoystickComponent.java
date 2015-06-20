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

public class JoystickComponent extends Joystick2AxisBase {
    
    // internal class members

    private static int xDefaultCentralValue = 135;
    private static int yDefaultCentralValue = 135;

    private final Joystick2Axis sensor = this;     


    
/**
     * default constructor 
     *  
     * @param pinX analog input X axis
     * @param pinY analog input Y axis
     */
    public JoystickComponent(GpioPinAnalogInput pinX, GpioPinAnalogInput pinY) {    
        this(pinX, pinY, JoystickComponent.xDefaultCentralValue, JoystickComponent.yDefaultCentralValue);
    }

    /**
     * default constructor 
     *  
     * @param pinX analog input X axis
     * @param pinY analog input Y axis
     * @param XCenter central value of X axis
     * @param YCenter central value of Y axis
     */
    public JoystickComponent(GpioPinAnalogInput pinX, GpioPinAnalogInput pinY, int XCenter, int YCenter) {
        this.xAxis = new JoystickAxisComponent(pinX);
        this.yAxis = new JoystickAxisComponent(pinY);   
	this.xAxis.calibrate(XCenter);
	this.yAxis.calibrate(YCenter);
    }

    @Override
    public double getXValue()
    {
        return xAxis.getValue();
    }

    @Override
    public double getYValue()
    {
        return yAxis.getValue();
    }
}
