/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.service.iss.tech.support;

import javax.xml.bind.JAXBElement;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.DateUtil;
import com.samples.platform.service.iss.tech.support.msg.GetSoapFaultRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetSoapFaultResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;

/**
 * Operation get soap fault on service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetSoapFault")
public class GetSoapFaultOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetSoapFaultOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetSoapFaultRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetSoapFaultResponseType}.
	 */
	// @InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetSoapFaultResponseType> getSoapFault(
			final JAXBElement<GetSoapFaultRequestType> message) {
		this.logger.debug("+getSoapFault");
		GetSoapFaultResponseType response = this.of
				.createGetSoapFaultResponseType();
		long start = System.currentTimeMillis();
		try {
			throw new RuntimeException(
					"This RuntimeException need to last in a SOAP fault.");
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
			if ("a".equals("a")) {
				throw e;
			}
		} finally {
			this.logger.debug(" getSoapFault duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getSoapFault #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetSoapFaultResponse(response);
	}
}
