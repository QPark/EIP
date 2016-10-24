package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetRevisionRequestType;
import com.qpark.eip.service.domain.doc.msg.GetRevisionResponseType;

/**
 * Gateway to call operation get revision on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public interface GetRevision {
	JAXBElement<GetRevisionResponseType> invoke(
			JAXBElement<GetRevisionRequestType> request);
}
