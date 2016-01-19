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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.samples.platform.core.flow.FlowContextImpl;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlow;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowRequestType;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get system user report on service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetSystemUserReport")
public class GetSystemUserReportOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetSystemUserReportOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link SystemUserReportFlow}. */
	@Autowired
	private SystemUserReportFlow flow;
	/**
	 * The {@link ApplicationPlaceholderConfigurer} containing the configuration
	 * properties.
	 */
	@Autowired
	private ApplicationPlaceholderConfigurer properties;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetSystemUserReportRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetSystemUserReportResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetSystemUserReportResponseType> getSystemUserReport(
			final JAXBElement<GetSystemUserReportRequestType> message) {
		this.logger.debug("+getSystemUserReport");
		GetSystemUserReportResponseType response = this.of
				.createGetSystemUserReportResponseType();
		long start = System.currentTimeMillis();
		try {
			FlowContextImpl flowContext = new FlowContextImpl();
			flowContext.setRequesterOperationName("GetSystemUserReport");
			flowContext.setRequesterServiceName("iss.tech.support");
			flowContext.setRequesterServiceVersion(this.properties
					.getProperty("eip.service.version", "unknown version"));
			flowContext.setRequesterUserName(message.getValue().getUserName());
			SystemUserReportFlowRequestType in = new SystemUserReportFlowRequestType();
			in.setIn(message.getValue());
			SystemUserReportFlowResponseType out = this.flow.invokeFlow(in,
					flowContext);
			if (out != null) {
				response = out.getOut();
			}
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
			// response.getFailure().add(
			// FailureHandler.handleException(e, "E_ALL_NOT_KNOWN_ERROR",
			// this.logger);
		} finally {
			this.logger.debug(" getSystemUserReport duration {}",
					DateUtil.getDuration(start));
			this.logger.debug("-getSystemUserReport #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetSystemUserReportResponse(response);
	}
}
