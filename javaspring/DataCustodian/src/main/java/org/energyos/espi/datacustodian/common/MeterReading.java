/*******************************************************************************
 * Copyright (c) 2011, 2012 EnergyOS.Org
 *
 * Licensed by EnergyOS.Org under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The EnergyOS.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at:
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 ******************************************************************************
*/



package org.energyos.espi.datacustodian.common;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;



/**
 * Set of values obtained from the meter.
 * 
 * <p>Java class for MeterReading complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MeterReading">
 *   &lt;complexContent>
 *     &lt;extension base="{http://naesb.org/espi}IdentifiedObject">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@RooJavaBean
@RooToString
@RooJpaActiveRecord

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "MeterReading")

public class MeterReading extends IdentifiedObject {

    @OneToOne
   // @XmlElement
    @XmlJavaTypeAdapter(UsagePointAdapter.class)
    private UsagePoint usagePoint;

    @OneToMany(cascade = CascadeType.ALL)
    //@XmlElement
    private Set<IntervalBlock> intervalBlocks = new HashSet<IntervalBlock>();
    
	public MeterReading() {
		// TODO Auto-generated constructor stub
	}

	public MeterReading (MeterReading obj) {
    	new MeterReading();
    	this.intervalBlocks = obj.intervalBlocks;
    	this.usagePoint = obj.usagePoint;
	}

	public static MeterReading makePersistent (MeterReading obj) {
		MeterReading result = new MeterReading(obj);
        result.persist();
		return result;    	    	
    }
}
