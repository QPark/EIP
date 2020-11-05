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
import com.qpark.eip.service.base.msg.FailureType;
import com.samples.platform.service.iss.tech.support.msg.GetFailureMessageRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetFailureMessageResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;

/**
 * Operation get failure message on service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetFailureMessage")
public class GetFailureMessageOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFailureMessageOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetFailureMessageRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetFailureMessageResponseType}.
	 */
	// @InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetFailureMessageResponseType> getFailureMessage(
			final JAXBElement<GetFailureMessageRequestType> message) {
		this.logger.debug("+getFailureMessage");
		GetFailureMessageResponseType response = this.of
				.createGetFailureMessageResponseType();
		long start = System.currentTimeMillis();
		try {
			FailureType ft = new FailureType();
			ft.setCode("FAILURE_TYPE_CODE");
			ft.setErrorDetails("Error details");
			ft.setSeverity("Severity INFO");
			ft.setUserMessage("This service operation provides a FailureType.");
			response.getFailure().add(ft);
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFailureMessage duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getFailureMessage #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetFailureMessageResponse(response);
	}
}
