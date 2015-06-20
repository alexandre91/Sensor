//package com.pi4j.component.sensor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  KnockSensorBase.java  
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

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;


public abstract class KnockSensorBase extends ObserveableComponentBase implements KnockSensor {
    
    protected Date lastKnockTimestamp = null;
    protected Date lastInactivityTimestamp = null;
    protected int knockCounter = 0;
    
    @Override
    public Date getLastKnockTimestamp() {
        return lastKnockTimestamp;
    }

    @Override
    public Date getLastInactivityTimestamp() {
        return lastInactivityTimestamp;
    }
    
    @Override
    public abstract boolean isKnockDetected();

    @Override
    public void addListener(KnockSensorListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(KnockSensorListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public int getKnockCounter() {
        return knockCounter;
    }

    @Override
    public void updateKnockCounter() {
        knockCounter++;
    }

    protected synchronized void notifyListeners(KnockSensorChangeEvent event) {
        // cache last detected timestamp
        if(event.isKnockDetected()){
            lastKnockTimestamp = event.timestamp;
	    this.updateKnockCounter();
	}
        else
            lastInactivityTimestamp = event.timestamp;
        
        // raise events to listeners
        for(ComponentListener listener : super.listeners) {
            ((KnockSensorListener)listener).onKnockStateChange(event);
        }
    }  
}
