package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetFlowRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowResponseType;

/**
 * Gateway to call operation get flow on service <code>domain.doc</code>.
 * 
 * @author bhausen
 */
public interface GetFlow {
	JAXBElement<GetFlowRequestType> invoke(
			JAXBElement<GetFlowResponseType> request);
}
