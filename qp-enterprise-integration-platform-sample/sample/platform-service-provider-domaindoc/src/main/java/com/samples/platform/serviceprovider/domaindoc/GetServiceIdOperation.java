/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.domaindoc;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.service.domain.doc.msg.GetServiceIdRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdResponseType;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get service id on service <code>domain.doc</code>.
 * 
 * @author bhausen
 */
@Component("operationProviderDomainDocGetServiceId")
public class GetServiceIdOperation {
	/**
	 * The
	 * {@link com.qpark.eip.core.model.analysis.operation.GetServiceIdOperation}
	 * .
	 */
	@Autowired
	@Qualifier(com.qpark.eip.core.model.analysis.operation.GetServiceIdOperation.BEAN_NAME)
	private com.qpark.eip.core.model.analysis.operation.GetServiceIdOperation operation;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetServiceIdRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetServiceIdResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetServiceIdResponseType> getServiceId(
			final JAXBElement<GetServiceIdRequestType> message) {
		return this.operation.invoke(message);
	}
}
