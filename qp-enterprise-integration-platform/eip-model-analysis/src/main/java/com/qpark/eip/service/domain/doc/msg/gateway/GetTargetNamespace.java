package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceResponseType;

/**
 * Gateway to call operation get target namespace on service
 * <code>domain.doc</code>.
 * 
 * @author bhausen
 */
public interface GetTargetNamespace {
	JAXBElement<GetTargetNamespaceRequestType> invoke(
			JAXBElement<GetTargetNamespaceResponseType> request);
}
