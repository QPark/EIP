package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetServiceIdRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdResponseType;

/**
 * Gateway to call operation get service id on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public interface GetServiceId {
	JAXBElement<GetServiceIdResponseType> invoke(
			JAXBElement<GetServiceIdRequestType> request);
}
