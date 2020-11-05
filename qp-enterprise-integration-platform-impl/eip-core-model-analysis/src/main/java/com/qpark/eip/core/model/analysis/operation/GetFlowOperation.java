/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.service.domain.doc.msg.GetFlowRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetFlow;

/**
 * Operation get flow on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetFlowOperation implements GetFlow {
	/** The bean name to use. */
	public static final String BEAN_NAME = "com.qpark.eip.core.model.analysis.operationProviderDomainDocGetFlow";

	/**
	 * Translate the pattern do SQL <i>like</i>.
	 *
	 * @param namePattern
	 *                        the given name pattern.
	 * @return the translated pattern.
	 */
	private static Optional<String> translateNamePattern(
			final String namePattern) {
		Optional<String> value = Optional.empty();
		if (Objects.nonNull(namePattern)) {
			String s = namePattern.replace('*', '%');
			if (!s.endsWith("%")) {
				s = String.format("%s%s", s, "%");
			}
			if (!s.startsWith("%")) {
				s = String.format("%s%s", "%", s);
			}
			value = Optional.of(s);
		}
		return value;
	}

	/** The {@link ExtendedDataProviderModelAnalysis}. */
	@Autowired
	private ExtendedDataProviderModelAnalysis dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFlowOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *                    the {@link JAXBElement} containing a
	 *                    {@link GetFlowRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetFlowResponseType}.
	 */
	@Override
	public final JAXBElement<GetFlowResponseType> invoke(
			final JAXBElement<GetFlowRequestType> message) {
		this.logger.debug("+getFlow");
		final GetFlowRequestType request = message.getValue();
		final GetFlowResponseType response = this.of
				.createGetFlowResponseType();
		final long start = System.currentTimeMillis();
		try {
			final List<String> namePattern = new ArrayList<>();
			request.getNamePattern().stream()
					.forEach(s -> translateNamePattern(s).ifPresent(
							translated -> namePattern.add(translated)));
			if (!namePattern.isEmpty()) {
				String modelVersion = request.getRevision();
				if (Objects.isNull(modelVersion)
						|| modelVersion.trim().length() == 0) {
					modelVersion = this.dao.getLastModelVersion();
				}
				response.getFlow().addAll(this.dao
						.getFlowByNamePattern(modelVersion, namePattern));
			}
		} catch (final Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFlow duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getFlow #{}", response.getFlow().size());
		}
		return this.of.createGetFlowResponse(response);
	}
}
