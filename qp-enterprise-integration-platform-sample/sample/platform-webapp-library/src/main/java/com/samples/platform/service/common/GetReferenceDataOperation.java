/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.service.common;

import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.model.common.ReferenceDataType;
import com.qpark.eip.service.common.msg.GetReferenceDataRequestType;
import com.qpark.eip.service.common.msg.GetReferenceDataResponseType;
import com.qpark.eip.service.common.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get reference data on service <code>common</code>.
 *
 * @author bhausen
 */
@Component("operationProviderCommonGetReferenceData")
public class GetReferenceDataOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetReferenceDataOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetReferenceDataRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetReferenceDataResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetReferenceDataResponseType> getReferenceData(
			final JAXBElement<GetReferenceDataRequestType> message) {
		this.logger.debug("+getReferenceData");
		GetReferenceDataResponseType response = this.of
				.createGetReferenceDataResponseType();
		try {
			ReferenceDataType referenceDataType = new ReferenceDataType();
			referenceDataType.setActive(true);
			referenceDataType.setCategory(new StringBuffer("Category ")
					.append(RandomStringUtils.random(15, 32, 127, true, false))
					.toString());
			referenceDataType.setDisplayValue(new StringBuffer("DisplayValue ")
					.append(RandomStringUtils.random(15, 32, 127, true, false))
					.toString());
			referenceDataType.setUUID(UUID.randomUUID().toString());
			response.getReferenceData().add(referenceDataType);
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug("-getReferenceData #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetReferenceDataResponse(response);
	}
}
