package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeResponseType;

/**
 * Gateway to call operation get field mapping type on service
 * <code>domain.doc</code>.
 *
 * @author bhausen
 */
public interface GetFieldMappingType {
	JAXBElement<GetFieldMappingTypeResponseType> invoke(
			JAXBElement<GetFieldMappingTypeRequestType> request);
}
