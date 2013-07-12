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


package org.energyos.espi.datacustodian.web;

import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.energyos.espi.datacustodian.common.ApplicationInformation;
import org.energyos.espi.datacustodian.common.DataCustodianApplicationStatus;
import org.energyos.espi.datacustodian.common.DataCustodianType;
import org.energyos.espi.datacustodian.common.ElectricPowerUsageSummary;
import org.energyos.espi.datacustodian.common.IdentifiedObject;
import org.energyos.espi.datacustodian.common.IntervalBlock;
import org.energyos.espi.datacustodian.common.IntervalReading;
import org.energyos.espi.datacustodian.common.MeterReading;
import org.energyos.espi.datacustodian.common.ReadingType;
import org.energyos.espi.datacustodian.common.ServiceCategory;
import org.energyos.espi.datacustodian.common.ServiceStatus;
import org.energyos.espi.datacustodian.common.TimeConfiguration;
import org.energyos.espi.datacustodian.common.UUIDType;
import org.energyos.espi.datacustodian.common.UsagePoint;
import org.energyos.espi.datacustodian.domain.DataCustodian;
import org.energyos.espi.datacustodian.domain.RetailCustomer;
import org.energyos.espi.datacustodian.domain.ThirdParty;

import org.energyos.espi.datacustodian.atom.*;
import org.energyos.espi.datacustodian.atom.Object;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.sun.tools.javac.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


@RequestMapping("/datacustodians")
@Controller
@RooWebScaffold(path = "datacustodians", formBackingObject = DataCustodian.class)
public class DataCustodianController {
                                                                                                                                                                                                                                               
	   @SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.GET, value="/{id}/insertDownloadMyDataFile", params={"url"}, headers="Accept=application/atom+xml")
	    @ResponseBody
	    public String insertDownloadMyDataFile(@PathVariable("id") Long id, @RequestParam("url") String aUrl) {
		   // TODO: change return from String to HttpResponseEntity with proper content
	    	String xmlResult;
			URL aFeed;
			FeedType theFeed;
		    Unmarshaller unmarshaller;
	        JAXBContext context;
	        Marshaller m;
	        Writer w = null;
	        JAXBContext jc;
	        
	        MeterReading meterReading = null;
	        TimeConfiguration timeConfiguration;
	        HashSet <IntervalBlock> intervalBlocks = new HashSet <IntervalBlock> ();
	        ReadingType readingType = null;
	        ElectricPowerUsageSummary electricPowerUsageSummary;
	        UsagePoint usagePoint = null;
			
	        DataCustodian resource = DataCustodian.findDataCustodian(id);
	      
	        if (resource == null) {
	            // TODO establish the proper way to return the error streams                                                                                                                                                                                               
	            // return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);   
	            return "400: Resource Not Found";
	        } else {
	        	// first upload and unmarshal     	
	    		try {
					jc = JAXBContext.newInstance("org.energyos.espi.datacustodian.atom:org.energyos.espi.datacustodian.common");
					try {
						aFeed = new URL(aUrl);
						try {
							unmarshaller = jc.createUnmarshaller();
				    		try {
				    			theFeed = (FeedType) JAXBIntrospector.getValue(unmarshaller.unmarshal(aFeed));				    	 
				    	        // now instantiate the feed
				    	        // TODO: this is should be happening incrementally during the parse
				    	        
				    			java.util.List<java.lang.Object> theList = theFeed.getAuthorOrCategoryOrContributor();
				    	        Iterator it = theList.iterator();
				    	        while (it.hasNext()) {
				    	        	JAXBElement aThing = (JAXBElement) it.next();
				    	            System.out.println(aThing.toString() + aThing.getDeclaredType().toString());
				    	            if (aThing.getDeclaredType().equals(org.energyos.espi.datacustodian.atom.EntryType.class)) {
					    	            EntryType theValue = (EntryType) aThing.getValue();
					    	            java.util.List<java.lang.Object> theContent = theValue.getAuthorOrCategoryOrContent();
					    	            Iterator values = theContent.iterator();
					    	            while (values.hasNext()) {
					    	            	JAXBElement aValue = (JAXBElement) values.next();
						    	            System.out.println(".." + aValue.toString() + aValue.getDeclaredType().toString());
						    	            if (aValue.getDeclaredType().equals(org.energyos.espi.datacustodian.atom.ContentType.class)) {
							    	            System.out.println("..." + aValue.toString() + aValue.getDeclaredType().toString());
							    	            ContentType temp = (ContentType) aValue.getValue();
							    	            java.util.List<java.lang.Object> foo = temp.getContent();
							    	            Iterator<java.lang.Object> fooValues = foo.iterator();
							    	            while (fooValues.hasNext()) {
							    	            	java.lang.Object aFooValue = fooValues.next();
							    	            	if (aFooValue.getClass().equals(JAXBElement.class)) {
							    	            		JAXBElement temp2 = (JAXBElement) aFooValue;
							    	   
							    	            		if (temp2.getDeclaredType().getPackage().equals(Package.getPackage("org.energyos.espi.datacustodian.common"))) {                                        
							    	            			System.out.println("...." + temp2.toString() + ":" + temp2.getDeclaredType().toString());
							    	            			if (temp2.getDeclaredType().equals(MeterReading.class)) {
							    	            				meterReading = (MeterReading) temp2.getValue();
							    	            			}
							    	            			if (temp2.getDeclaredType().equals(UsagePoint.class)) {
							    	            				usagePoint = (UsagePoint) temp2.getValue();
							    	            			}
							    	            			if (temp2.getDeclaredType().equals(TimeConfiguration.class)) {
							    	            				timeConfiguration = (TimeConfiguration) temp2.getValue();
							    	            			}
							    	            			if (temp2.getDeclaredType().equals(IntervalBlock.class)) {
							    	            				intervalBlocks.add((IntervalBlock) temp2.getValue());
							    	            			}
							    	            			if (temp2.getDeclaredType().equals(ReadingType.class)) {
							    	            				readingType = (ReadingType) temp2.getValue();
							    	            			}
							    	            			if (temp2.getDeclaredType().equals(ElectricPowerUsageSummary.class)) {
							    	            				electricPowerUsageSummary = (ElectricPowerUsageSummary) temp2.getValue();
							    	            			}
							    	            		}
							    	            	}
							    	            	fooValues.remove();
							    	            }
						    	            }
							    	        values.remove();	
					    	            
					    	            }
					    	            }
				    	            
				    	            it.remove(); // avoids a ConcurrentModificationException	        	
				    	        }
				    	        //TODO now hook things together
    	            			//TODO Link to the Retail Customer through the Access Token
    	            			//TODO Add in Location and Address based on Retail Customer
    	            			//TODO Make the enums not persist new values, rather through an error if something unexpected comes in	
				    	        
				    	        //TODO check to see if we already have this usagePoint (based upon UUID)
				    	        UsagePoint realUsagePoint = new UsagePoint();
				    	        realUsagePoint.setDataCustodian(resource);
				    	        ServiceCategory serviceCategory = new ServiceCategory();
				    	        serviceCategory.setKind(usagePoint.getServiceCategory().getKind());
				    	        serviceCategory.persist();
				    	        realUsagePoint.setServiceCategory(serviceCategory);
				    	        realUsagePoint.setUuid(usagePoint.getUuid());
				    	        realUsagePoint.persist();
				    	        
				    	        //TODO check to see if we already have this meterReading (based upon UUID)
				    	        MeterReading realMeterReading = new MeterReading();
				    	        realMeterReading.setUsagePoint(realUsagePoint);
				    	        realMeterReading.setUuid(meterReading.getUuid());
				    	        realMeterReading.persist();
				    	        
				    	        realMeterReading.setIntervalBlocks(intervalBlocks);
                                ReadingType realReadingType = ReadingType.makePersistent(readingType);
				    	        //First Persist the Interval Readings and Interval Blocks
				    	        Iterator ibIterator = intervalBlocks.iterator();
				    	        while (ibIterator.hasNext()) {
				    	        	IntervalBlock ib = (IntervalBlock) ibIterator.next();
				    	            ib.setMeterReading(meterReading);
				    	         	ib.setReadingType(readingType);
					    	        ib.persist();
				    	         	Iterator iIterator = ib.getIntervalReading().iterator();
				    	         	while (iIterator.hasNext()) {
				    	         		IntervalReading ir = (IntervalReading) iIterator.next();
				    	         		ir.setUsagePoint(usagePoint);
				    	         		ir.setIntervalBlock(ib);
				    	         	    ir.persist();
				    	         		iIterator.remove();
				    	         }	
				    	         ibIterator.remove();
				    	        }

				    	        
				    	        
				    	        
				    		} catch (JAXBException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (JAXBException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
	    		}
	        }
            
	        return "200 - Ok";
	    }
		 
	@RequestMapping(method = RequestMethod.GET, value="/{id}/uploadmydata", params={"url"}, headers="Accept=application/atom+xml")
    @ResponseBody
    public String getDownloadMyData(@PathVariable("id") Long id, @RequestParam("url") String aUrl) {
	    // TODO: change return from String to HttpResponseEntity with proper content
    	String xmlResult;
		URL aFeed;
		FeedType theFeed;
	    Unmarshaller unmarshaller;
        JAXBContext context;
        Marshaller m;
        Writer w = null;
        JAXBContext jc;
		
        DataCustodian resource = DataCustodian.findDataCustodian(id);
      
        if (resource == null) {
            // TODO establish the proper way to return the error streams                                                                                                                                                                                               
            // return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);   
            return "400: Resource Not Found";
        } else {
        	// first upload and unmarshal     	
    		try {
				jc = JAXBContext.newInstance("org.energyos.espi.datacustodian.atom:org.energyos.espi.datacustodian.common");
				try {
					aFeed = new URL(aUrl);
					try {
						unmarshaller = jc.createUnmarshaller();
			    		try {
			    			theFeed = (FeedType) JAXBIntrospector.getValue(unmarshaller.unmarshal(aFeed));

				            try {
								context = JAXBContext.newInstance("org.energyos.espi.datacustodian.atom:org.energyos.espi.datacustodian.common");
								m = context.createMarshaller();
								m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
								m = context.createMarshaller();
							    w = new StringWriter();
								m.marshal(theFeed, w);
							    xmlResult = w.toString();
					            try {
					                w.close();
					            } catch (IOException e) {
					                e.printStackTrace();
					            }
					            HttpHeaders hdr = new HttpHeaders();
					            hdr.set("Content-Type", "application/atom+xml");
					            return xmlResult;

							} catch (JAXBException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			    		} catch (JAXBException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (JAXBException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
    		}
        }
        return null;
    }
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}", headers="Accept=application/atom+xml")
	    @ResponseBody
	    public String getResource(@PathVariable("id") Long id)  {
		   // TODO: change return from String to HttpResponseEntity with proper content
	        String xmlResult;
	        // get the resource                                                                                                                                                                                                                                                
	        DataCustodian resource = DataCustodian.findDataCustodian(id);
          
	        if (resource == null) {
	            // TODO establish the proper way to return the error streams                                                                                                                                                                                               
	            // return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);   
                return "400: Resource Not Found";
	        } else {
	            // marshal the resource                                                                                                                                                                                                                                       
	            JAXBContext context;
	            Marshaller m;
	            Writer w = null;

	            try {
	                context = JAXBContext.newInstance("org.energyos.espi.datacustodian.atom:org.energyos.espi.datacustodian.common");
	                m = context.createMarshaller();
	                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	                w = new StringWriter();
	                m.marshal(resource, w);
	            } catch (JAXBException e1) {
	                // TODO Auto-generated catch block                                                                                                                                                                                                                         
	                e1.printStackTrace();
	            }
	            xmlResult = w.toString();
	            try {
	                w.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            HttpHeaders hdr = new HttpHeaders();
	            hdr.set("Content-Type", "application/atom+xml");
	            return xmlResult;
	            // we may want a response entity for response encapsulation rather than just the raw string                                                                                                                                                                    
	            //                                                                                                                                                                                                                                                             
	            //      return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.OK);                                                                                                                                                                          
	        }
	 
	    }

	    
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DataCustodian dataCustodian, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, dataCustodian);
            return "datacustodians/create";
        }
        uiModel.asMap().clear();
        dataCustodian.persist();
        return "redirect:/datacustodians/" + encodeUrlPathSegment(dataCustodian.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new DataCustodian());
        return "datacustodians/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("datacustodian", DataCustodian.findDataCustodian(id));
        uiModel.addAttribute("itemId", id);
        return "datacustodians/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("datacustodians", DataCustodian.findDataCustodianEntries(firstResult, sizeNo));
            float nrOfPages = (float) DataCustodian.countDataCustodians() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("datacustodians", DataCustodian.findAllDataCustodians());
        }
        return "datacustodians/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DataCustodian dataCustodian, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, dataCustodian);
            return "datacustodians/update";
        }
        uiModel.asMap().clear();
        dataCustodian.merge();
        return "redirect:/datacustodians/" + encodeUrlPathSegment(dataCustodian.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, DataCustodian.findDataCustodian(id));
        return "datacustodians/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DataCustodian dataCustodian = DataCustodian.findDataCustodian(id);
        dataCustodian.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/datacustodians";
    }

	void populateEditForm(Model uiModel, DataCustodian dataCustodian) {
        uiModel.addAttribute("dataCustodian", dataCustodian);
        uiModel.addAttribute("applicationinformations", ApplicationInformation.findAllApplicationInformations());
        uiModel.addAttribute("datacustodianapplicationstatuses", DataCustodianApplicationStatus.findAllDataCustodianApplicationStatuses());
        uiModel.addAttribute("datacustodiantypes", DataCustodianType.findAllDataCustodianTypes());
        uiModel.addAttribute("servicestatuses", ServiceStatus.findAllServiceStatuses());
        uiModel.addAttribute("retailcustomers", RetailCustomer.findAllRetailCustomers());
        uiModel.addAttribute("thirdpartys", ThirdParty.findAllThirdPartys());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
