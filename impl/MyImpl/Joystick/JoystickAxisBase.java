//package com.pi4j.component.sensor;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Joystick2AxisBase.java  
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


public abstract class JoystickAxisBase extends ObserveableComponentBase implements JoystickAxis {

    private double centralAxisValue = 0;


    protected JoystickAxisBase(double centralValue){
	this.centralAxisValue = centralValue;
    }
        
    @Override
    public void addListener(JoystickAxisListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(JoystickAxisListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(JoystickAxisValueChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((JoystickAxisListener)listener).onAxisValueChange(event);
        }
    }

    @Override
    public void calibrate(double center_value){
	this.centralAxisValue = center_value;
    }

    @Override
    public double getCalibrateValue(){
	return this.centralAxisValue;
    }

    @Override
    public boolean isValue(double value)
    {
        return ((boolean)(getValue() == value));
    }

    @Override
    public boolean isValueInRange(double min, double max)
    {
        double value = getValue();
        return (value >= min && value <= max);
    }      
}
