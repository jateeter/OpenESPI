/*******************************************************************************
 * Copyright (c) 2011, 2012, 2013 EnergyOS.Org
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

import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ReadingType")

public class ReadingType extends IdentifiedObject {

    @ManyToOne
    @XmlJavaTypeAdapter(AccumulationBehaviorAdapter.class)
    private AccumulationBehavior accumulationBehavior;

    @ManyToOne
    @XmlJavaTypeAdapter(CommodityAdapter.class)
    private Commodity commodity;

    @ManyToOne
    @XmlJavaTypeAdapter(DataQualifierAdapter.class)
    private DataQualifier dataQualifier;

    @ManyToOne
    @XmlJavaTypeAdapter(DirectionOfFlowAdapter.class)
    private DirectionOfFlow flowDirection;

    private Integer intervalLength;

    @ManyToOne
    @XmlJavaTypeAdapter(KindAdapter.class)
    private Kind kind;

    @ManyToOne
    @XmlJavaTypeAdapter(PhaseCodeAdapter.class)
    private PhaseCode phase;

    @ManyToOne
    @XmlJavaTypeAdapter(UnitMultiplierAdapter.class)
    private UnitMultiplier powerOfTenMultiplier;

    @ManyToOne
    @XmlJavaTypeAdapter(TimeAttributeAdapter.class)
    private TimeAttribute timeAttribute;

    @ManyToOne
    @XmlJavaTypeAdapter(UnitSymbolAdapter.class)
    private UnitSymbol uom;

    @ManyToOne
    @XmlJavaTypeAdapter(ConsumptionTierAdapter.class)
    private ConsumptionTier consumptionTier;

    @ManyToOne
    @XmlJavaTypeAdapter(CPPAdapter.class)
    private CPP cpp;

    @ManyToOne
    @XmlJavaTypeAdapter(CurrencyAdapter.class)
    private Currency currency;

    @ManyToOne
    @XmlElement
    private ReadingInterharmonic interharmonic;

    @ManyToOne
    @XmlJavaTypeAdapter(TimeAttributeAdapter.class)
    private TimeAttribute measuringPeriod;

    @ManyToOne
    @XmlJavaTypeAdapter(TOUAdapter.class)
    private TOU tou;

    @ManyToOne
    @XmlJavaTypeAdapter(DataQualifierAdapter.class)
    private DataQualifier aggregate;

    @ManyToOne
    @XmlElement
    private RationalNumber argument;


	public static ReadingType CopyReadingType(ReadingType obj) {
	    ReadingType result = new ReadingType();
    	result.accumulationBehavior = obj.accumulationBehavior;
    	result.aggregate = obj.aggregate;
   	    result.argument = (obj.argument != null) ? obj.argument.makePersistent(obj.argument) : obj.argument;
   	    /*
    	result.commodity = obj.commodity;
    	result.consumptionTier = obj.consumptionTier;
    	result.cpp = obj.cpp;
    	result.currency = obj.currency;
    	result.dataQualifier = obj.dataQualifier;
    	result.flowDirection = obj.flowDirection;
    	result.interharmonic = (obj.interharmonic != null) ? ReadingInterharmonic.makePersistent(obj.interharmonic) : obj.interharmonic;
    	result.intervalLength = obj.intervalLength;
    	result.kind = obj.kind;
    	result.measuringPeriod = obj.measuringPeriod;
    	result.phase = obj.phase;
    	result.powerOfTenMultiplier = obj.powerOfTenMultiplier;
    	result.timeAttribute = obj.timeAttribute;
    	result.tou = obj.tou;
    	result.uom = obj.uom;
    	*/
   	    return result;
	}

	public static ReadingType makePersistent (ReadingType obj) {
		ReadingType result = CopyReadingType(obj);
		result.persist();
        return result;
	}
}
