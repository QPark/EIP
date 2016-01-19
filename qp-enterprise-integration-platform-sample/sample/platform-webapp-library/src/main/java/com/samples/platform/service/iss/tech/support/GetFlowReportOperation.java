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
import com.samples.platform.service.iss.tech.support.msg.GetFlowReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetFlowReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get flow report on service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetFlowReport")
public class GetFlowReportOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFlowReportOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetFlowReportRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetFlowReportResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetFlowReportResponseType> getFlowReport(
			final JAXBElement<GetFlowReportRequestType> message) {
		this.logger.debug("+getFlowReport");
		GetFlowReportResponseType response = this.of
				.createGetFlowReportResponseType();
		long start = System.currentTimeMillis();
		try {
			// TODO
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFlowReport duration {}",
					DateUtil.getDuration(start));
			this.logger.debug("-getFlowReport #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetFlowReportResponse(response);
	}
}
