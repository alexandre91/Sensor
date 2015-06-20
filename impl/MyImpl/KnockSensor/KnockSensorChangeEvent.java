//package com.pi4j.component.sensor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  KnockSensorChangeEvent.java  
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



import java.util.Date;
import java.util.EventObject;

public class KnockSensorChangeEvent extends EventObject {

    private static final long serialVersionUID = 2401326354080048006L;
    protected final Date timestamp;
    protected final boolean knock;

    public KnockSensorChangeEvent(KnockSensor sensor, boolean knock, Date timestamp) {
        super(sensor);
        this.knock = knock;
        this.timestamp = timestamp;        
    }

    public KnockSensorChangeEvent(KnockSensor sensor, boolean knock) {
        super(sensor);
        this.knock = knock;
        this.timestamp = new Date();        
    }
    
    public KnockSensor getSensor() {
        return (KnockSensor)getSource();
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public boolean isKnockDetected() {
        return knock;
    }
    
}
