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

import com.qpark.eip.service.domain.doc.msg.GetFlowRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowResponseType;

/**
 * Operation get flow on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
@Component("operationProviderDomainDocGetFlow")
public class GetFlowOperation {

	/**
	 * The {@link com.qpark.eip.core.model.analysis.operation.GetFlowOperation}
	 * .
	 */
	@Autowired
	@Qualifier(com.qpark.eip.core.model.analysis.operation.GetFlowOperation.BEAN_NAME)
	private com.qpark.eip.core.model.analysis.operation.GetFlowOperation operation;

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetFlowRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetFlowResponseType}.
	 */
	// @InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetFlowResponseType> getFlow(
			final JAXBElement<GetFlowRequestType> message) {
		return this.operation.invoke(message);
	}
}
