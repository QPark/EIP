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
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetFieldMappingType;

/**
 * Operation get field mapping type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetFieldMappingTypeOperation implements GetFieldMappingType {
	/** The bean name to use. */
	public static final String BEAN_NAME = "com.qpark.eip.core.model.analysis.operationProviderDomainDocGetFieldMappingType";
	/** The {@link ExtendedDataProviderModelAnalysis}. */
	@Autowired
	private ExtendedDataProviderModelAnalysis dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFieldMappingTypeOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetFieldMappingTypeRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetFieldMappingTypeResponseType} .
	 */
	@Override
	public final JAXBElement<GetFieldMappingTypeResponseType> invoke(
			final JAXBElement<GetFieldMappingTypeRequestType> message) {
		this.logger.debug("+getFieldMappingType");
		GetFieldMappingTypeRequestType request = message.getValue();
		GetFieldMappingTypeResponseType response = this.of
				.createGetFieldMappingTypeResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = request.getRevision();
			if (Objects.isNull(modelVersion)
					|| modelVersion.trim().length() == 0) {
				modelVersion = this.dao.getLastModelVersion();
			}
			response.getFieldMappingType().addAll(this.dao
					.getFieldMappingTypesById(modelVersion, request.getId()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFieldMappingType duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getFieldMappingType #{}",
					response.getFieldMappingType().size());
		}
		return this.of.createGetFieldMappingTypeResponse(response);
	}
}
