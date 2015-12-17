/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.service.iss.tech.support;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.service.common.msg.GetReferenceDataRequestType;
import com.qpark.eip.service.common.msg.GetReferenceDataResponseType;
import com.qpark.eip.service.common.msg.gateway.GetReferenceData;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get forwarded reference data on service
 * <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetForwardedReferenceData")
public class GetForwardedReferenceDataOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetForwardedReferenceDataOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link com.qpark.eip.service.common.msg.ObjectFactory}. */
	private final com.qpark.eip.service.common.msg.ObjectFactory commonOf = new com.qpark.eip.service.common.msg.ObjectFactory();
	/** The {@link GetReferenceData} gateway to show the forward response. */
	@Autowired
	@Qualifier("eipCallerComSamplesPlatformCommonForwardGetReferenceDataGateway")
	private GetReferenceData getAggregatedReferenceData;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetForwardedReferenceDataRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetForwardedReferenceDataResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetForwardedReferenceDataResponseType> getForwardedReferenceData(
			final JAXBElement<GetForwardedReferenceDataRequestType> message) {
		this.logger.debug("+getForwardedReferenceData");
		GetForwardedReferenceDataRequestType request = message.getValue();
		GetForwardedReferenceDataResponseType response = this.of
				.createGetForwardedReferenceDataResponseType();
		long start = System.currentTimeMillis();
		try {
			GetReferenceDataRequestType getReferenceDataRequestType = this.commonOf
					.createGetReferenceDataRequestType();
			getReferenceDataRequestType.setCriteria(request.getCriteria());
			JAXBElement<GetReferenceDataResponseType> forwardResponse = this.getAggregatedReferenceData
					.invoke(this.commonOf.createGetReferenceDataRequest(
							getReferenceDataRequestType));
			if (forwardResponse != null && forwardResponse.getValue() != null) {
				response.getFailure()
						.addAll(forwardResponse.getValue().getFailure());
				response.getReferenceData()
						.addAll(forwardResponse.getValue().getReferenceData());
			} else {
				throw new IllegalStateException("Forward response empty!");
			}
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getForwardedReferenceData duration {}",
					DateUtil.getDuration(start));
			this.logger.debug("-getForwardedReferenceData #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetForwardedReferenceDataResponse(response);
	}
}
