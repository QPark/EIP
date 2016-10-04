package com.qpark.eip.service.domain.doc.msg.gateway;

import javax.xml.bind.JAXBElement;

import com.qpark.eip.service.domain.doc.msg.GetClusterRequestType;
import com.qpark.eip.service.domain.doc.msg.GetClusterResponseType;

/**
 * Gateway to call operation get cluster on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public interface GetCluster {
	JAXBElement<GetClusterResponseType> invoke(
			JAXBElement<GetClusterRequestType> request);

}
