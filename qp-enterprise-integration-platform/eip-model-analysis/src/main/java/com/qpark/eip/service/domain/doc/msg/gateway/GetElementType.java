package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetElementTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeResponseType;

/**
 * Gateway to call operation get element type on service <code>domain.doc</code>
 * .
 * 
 * @author bhausen
 */
public interface GetElementType {
	JAXBElement<GetElementTypeRequestType> invoke(
			JAXBElement<GetElementTypeResponseType> request);
}
