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
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetFlowInterfaceMappingType;

/**
 * Operation get flow interface mapping type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetFlowInterfaceMappingTypeOperation
		implements GetFlowInterfaceMappingType {
	/** The bean name to use. */
	public static final String BEAN_NAME = "com.qpark.eip.core.model.analysis.operationProviderDomainDocGetFlowInterfaceMappingType";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFlowInterfaceMappingTypeOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetFlowInterfaceMappingTypeRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetFlowInterfaceMappingTypeResponseType}.
	 */
	@Override
	public final JAXBElement<GetFlowInterfaceMappingTypeResponseType> invoke(
			final JAXBElement<GetFlowInterfaceMappingTypeRequestType> message) {
		this.logger.debug("+getFlowInterfaceMappingType");
		GetFlowInterfaceMappingTypeRequestType request = message.getValue();
		GetFlowInterfaceMappingTypeResponseType response = this.of
				.createGetFlowInterfaceMappingTypeResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = request.getRevision();
			if (Objects.isNull(modelVersion)
					|| modelVersion.trim().length() == 0) {
				modelVersion = this.dao.getLastModelVersion();
			}
			response.getInterfaceType()
					.addAll(this.dao.getFlowInterfaceMappingTypes(modelVersion,
							request.getFlowId()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFlowInterfaceMappingType duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getFlowInterfaceMappingType #{}",
					response.getInterfaceType().size());
		}
		return this.of.createGetFlowInterfaceMappingTypeResponse(response);
	}
}
