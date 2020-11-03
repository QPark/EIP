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

import com.qpark.eip.service.domain.doc.msg.GetElementTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeResponseType;

/**
 * Operation get element type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
@Component("operationProviderDomainDocGetElementType")
public class GetElementTypeOperation {
	/**
	 * The
	 * {@link com.qpark.eip.core.model.analysis.operation.GetElementTypeOperation}
	 * .
	 */
	@Autowired
	@Qualifier(com.qpark.eip.core.model.analysis.operation.GetElementTypeOperation.BEAN_NAME)
	private com.qpark.eip.core.model.analysis.operation.GetElementTypeOperation operation;

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetElementTypeRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetElementTypeResponseType}
	 *         .
	 */
	// @InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetElementTypeResponseType> getElementType(
			final JAXBElement<GetElementTypeRequestType> message) {
		return this.operation.invoke(message);
	}
}
