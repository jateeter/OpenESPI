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

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.energyos.espi.datacustodian.common.ApplicationInformation;
import org.energyos.espi.datacustodian.common.Authorization;
import org.energyos.espi.datacustodian.common.ElectricPowerQualitySummary;
import org.energyos.espi.datacustodian.common.ElectricPowerUsageSummary;
import org.energyos.espi.datacustodian.common.IntervalBlock;
import org.energyos.espi.datacustodian.common.MeterReading;
import org.energyos.espi.datacustodian.common.ReadingType;
import org.energyos.espi.datacustodian.common.Subscription;
import org.energyos.espi.datacustodian.common.TimeConfiguration;
import org.energyos.espi.datacustodian.common.UsagePoint;
import org.hibernate.mapping.List;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;


/**
 * This is a root class to provide common naming attributes for all classes needing naming attributes
 * 
 * <p>Java class for IdentifiedObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IdentifiedObject">
 *   &lt;complexContent>
 *     &lt;extension base="{http://naesb.org/espi}Resource">
 *       &lt;sequence>
 *         &lt;element name="mRID" type="{http://naesb.org/espi}UUIDType" minOccurs="0"/>
 *         &lt;element name="description" type="{http://naesb.org/espi}String32" minOccurs="0"/>
 *       &lt;/sequence>
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
@XmlType(name = "IdentifiedObject", propOrder = {
    "uuid",
    // "mrid",
    "description"
})
@XmlSeeAlso({
    MeterReading.class,
    UsagePoint.class,
    ElectricPowerUsageSummary.class,
    TimeConfiguration.class,
    ApplicationInformation.class,
    Authorization.class,
    Subscription.class,
    ElectricPowerQualitySummary.class,
    IntervalBlock.class,
    ReadingQuality.class
})

public class IdentifiedObject {
	@XmlElement (name="uuid")
    private String uuid;
	@XmlElement (name="description")
    private String description;
   
    public IdentifiedObject () {
    	uuid = UUID.randomUUID().toString();
        description = "Description: " + uuid.toString();
    }

    // constructor for use whe we are given the uuid from an import
    //
    public IdentifiedObject (String aUuid) {
        uuid = aUuid;
	    description = "Description: " + aUuid;
    }

	public static IdentifiedObject findIdentifiedObject(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
