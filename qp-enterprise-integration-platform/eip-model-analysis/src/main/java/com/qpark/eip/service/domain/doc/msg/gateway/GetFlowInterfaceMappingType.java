package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeResponseType;

/**
 * Gateway to call operation get flow interface mapping type on service
 * <code>domain.doc</code>.
 * 
 * @author bhausen
 */
public interface GetFlowInterfaceMappingType {
	JAXBElement<GetFlowInterfaceMappingTypeRequestType> invoke(
			JAXBElement<GetFlowInterfaceMappingTypeResponseType> request);
}
