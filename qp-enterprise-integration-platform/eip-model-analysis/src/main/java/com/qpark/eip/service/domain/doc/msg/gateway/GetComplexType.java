package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetComplexTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeResponseType;

/**
 * Gateway to call operation get complex type on service <code>domain.doc</code>
 * .
 *
 * @author bhausen
 */
public interface GetComplexType {
	JAXBElement<GetComplexTypeResponseType> invoke(
			JAXBElement<GetComplexTypeRequestType> request);
}
