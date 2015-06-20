//package com.pi4j.component.sensor;

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

import com.pi4j.io.gpio.GpioPinAnalogInput;

public abstract class Joystick2AxisBase implements Joystick2Axis {

    protected JoystickAxis xAxis = null;
    protected JoystickAxis yAxis = null;
        
    @Override
    public void addListenerX(JoystickAxisListener... listener) {
        xAxis.addListener(listener);
    }

    @Override
    public synchronized void removeListenerX(JoystickAxisListener... listener) {
        xAxis.removeListener(listener);
    }

    @Override
    public void addListenerY(JoystickAxisListener... listener) {
        yAxis.addListener(listener);
    }

    @Override
    public synchronized void removeListenerY(JoystickAxisListener... listener) {
        yAxis.removeListener(listener);
    }

    @Override
    public void calibrateX(double center_value){
	xAxis.calibrate(center_value);
    }

    @Override
    public void calibrateY(double center_value){
	yAxis.calibrate(center_value);
    }

    @Override
    public boolean isXValue(double value){
	return (xAxis.getValue() == value);
    }

    @Override
    public boolean isYValue(double value){
	return (yAxis.getValue() == value);
    }
    
    @Override
    public boolean isFullUp(){
	return (xAxis.getValue() == 0);
    }
    
    @Override
    public boolean isFullDown(){
	return (xAxis.getValue() == 255);
    }
    
    @Override
    public boolean isFullRight(){
	return (yAxis.getValue() == 0);
    }
    
    @Override
    public boolean isFullLeft(){
	return (yAxis.getValue() == 255);	
    }
    @Override
    public boolean isMovingUp(){
	return (xAxis.getValue() < xAxis.getCalibrateValue());
    }

    @Override
    public boolean isMovingDown(){
	return (getXValue() > xAxis.getCalibrateValue());
    }

    @Override
    public boolean isMovingRight(){
	return (getYValue() < yAxis.getCalibrateValue());
    }

    @Override
    public boolean isMovingLeft(){
	return (getYValue() > yAxis.getCalibrateValue());
    } 

    @Override
    public double getXValue(){
	return xAxis.getValue();
    }

    @Override
    public double getYValue(){
	return yAxis.getValue();
    }
  
}
