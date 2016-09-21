package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetServiceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceResponseType;

/**
 * Gateway to call operation get service on service <code>domain.doc</code>.
 * 
 * @author bhausen
 */
public interface GetService {
	JAXBElement<GetServiceRequestType> invoke(
			JAXBElement<GetServiceResponseType> request);
}
