/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.operation;

import java.util.Objects;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetServiceId;

/**
 * Operation get service id on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetServiceIdOperation implements GetServiceId {
	/** The bean name to use. */
	public static final String BEAN_NAME = "com.qpark.eip.core.model.analysis.operationProviderDomainDocGetServiceId";
	/** The {@link ExtendedDataProviderModelAnalysis}. */
	@Autowired
	private ExtendedDataProviderModelAnalysis dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetServiceIdOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetServiceIdRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetServiceIdResponseType}.
	 */
	@Override
	public final JAXBElement<GetServiceIdResponseType> invoke(
			final JAXBElement<GetServiceIdRequestType> message) {
		this.logger.debug("+getServiceId");
		final GetServiceIdRequestType request = message.getValue();
		final GetServiceIdResponseType response = this.of
				.createGetServiceIdResponseType();
		final long start = System.currentTimeMillis();
		try {
			String modelVersion = request.getRevision();
			if (Objects.isNull(modelVersion)
					|| modelVersion.trim().length() == 0) {
				modelVersion = this.dao.getLastModelVersion();
			}
			response.getServiceId()
					.addAll(this.dao.getServiceIds(modelVersion));
		} catch (final Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getServiceId duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getServiceId #{}",
					response.getServiceId().size());
		}
		return this.of.createGetServiceIdResponse(response);
	}
}