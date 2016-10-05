/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.operation;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetServiceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetService;

/**
 * Operation get service on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetServiceOperation implements GetService {
	/** The bean name to use. */
	public static final String BEAN_NAME = "com.qpark.eip.core.model.analysis.operationProviderDomainDocGetService";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetServiceOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetServiceRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetServiceResponseType}.
	 */
	@Override
	public final JAXBElement<GetServiceResponseType> invoke(
			final JAXBElement<GetServiceRequestType> message) {
		this.logger.debug("+getService");
		GetServiceRequestType request = message.getValue();
		GetServiceResponseType response = this.of
				.createGetServiceResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.setService(this.dao.getServiceByServiceId(modelVersion,
					request.getServiceId()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getService duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getService #{}",
					response.getService() != null ? 1 : 0);
		}
		return this.of.createGetServiceResponse(response);
	}
}
