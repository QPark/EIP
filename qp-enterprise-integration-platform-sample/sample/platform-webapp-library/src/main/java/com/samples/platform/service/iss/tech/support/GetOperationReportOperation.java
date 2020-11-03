/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.service.iss.tech.support;

import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.DateUtil;
import com.samples.platform.model.iss.tech.support.OperationReportType;
import com.samples.platform.service.iss.tech.support.msg.GetOperationReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetOperationReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.samples.platform.util.OperationLogProvider;

/**
 * Operation get operation report on service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetOperationReport")
public class GetOperationReportOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetOperationReportOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link OperationLogProvider}. */
	@Autowired
	private OperationLogProvider operationLogProvider;

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetOperationReportRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetOperationReportResponseType}.
	 */
	// @InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetOperationReportResponseType> getOperationReport(
			final JAXBElement<GetOperationReportRequestType> message) {
		this.logger.debug("+getOperationReport");
		GetOperationReportResponseType response = this.of
				.createGetOperationReportResponseType();
		long start = System.currentTimeMillis();
		try {
			List<OperationReportType> operationReports = this.operationLogProvider
					.getOperationReport(new Date());
			response.getReport().addAll(operationReports);
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getOperationReport duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getOperationReport #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetOperationReportResponse(response);
	}
}
