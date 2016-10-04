package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetDataTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetDataTypeResponseType;

/**
 * Gateway to call operation get data type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public interface GetDataType {
	JAXBElement<GetDataTypeResponseType> invoke(
			JAXBElement<GetDataTypeRequestType> request);
}
