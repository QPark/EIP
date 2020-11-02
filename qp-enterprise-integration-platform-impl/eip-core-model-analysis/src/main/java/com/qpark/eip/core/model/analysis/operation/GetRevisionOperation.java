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
import com.qpark.eip.service.domain.doc.msg.GetRevisionRequestType;
import com.qpark.eip.service.domain.doc.msg.GetRevisionResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetRevision;

/**
 * Operation get revision on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetRevisionOperation implements GetRevision {
	/** The bean name to use. */
	public static final String BEAN_NAME = "com.qpark.eip.core.model.analysis.operationProviderDomainDocGetRevision";
	/** The {@link ExtendedDataProviderModelAnalysis}. */
	@Autowired
	private ExtendedDataProviderModelAnalysis dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetRevisionOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetRevisionRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetRevisionResponseType}.
	 */
	@Override
	public final JAXBElement<GetRevisionResponseType> invoke(
			final JAXBElement<GetRevisionRequestType> message) {
		this.logger.debug("+getRevision");
		GetRevisionResponseType response = this.of
				.createGetRevisionResponseType();
		long start = System.currentTimeMillis();
		try {
			response.getRevision().addAll(this.dao.getRevisions());
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getRevision duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getRevision #{}",
					response.getRevision().size());
		}
		return this.of.createGetRevisionResponse(response);
	}
}