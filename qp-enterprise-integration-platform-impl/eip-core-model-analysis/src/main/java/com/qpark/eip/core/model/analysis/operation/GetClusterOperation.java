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
import com.qpark.eip.service.domain.doc.msg.GetClusterRequestType;
import com.qpark.eip.service.domain.doc.msg.GetClusterResponseType;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetCluster;

/**
 * Operation get cluster on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetClusterOperation implements GetCluster {
	/** The bean name to use. */
	public static final String BEAN_NAME = "com.qpark.eip.cobeare.model.analysis.operationProviderDomainDocGetCluster";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetClusterOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetClusterRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetComplexTypeResponseType}
	 *         .
	 */
	@Override
	public JAXBElement<GetClusterResponseType> invoke(
			final JAXBElement<GetClusterRequestType> message) {
		this.logger.debug("+getCluster");
		GetClusterRequestType request = message.getValue();
		GetClusterResponseType response = this.of
				.createGetClusterResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.setCluster(this.dao.getClusterByTargetNamespace(
					modelVersion, request.getTargetNamespace()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getCluster duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getCluster #{}",
					response.getCluster() == null ? 0 : 1);
		}
		return this.of.createGetClusterResponse(response);
	}
}
