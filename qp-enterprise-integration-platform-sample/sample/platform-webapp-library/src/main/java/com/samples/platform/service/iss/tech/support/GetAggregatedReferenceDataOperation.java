/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
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
import com.samples.platform.service.iss.tech.support.msg.GetAggregatedReferenceDataRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetAggregatedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get aggregated reference data on service
 * <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetAggregatedReferenceData")
public class GetAggregatedReferenceDataOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetAggregatedReferenceDataOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link com.qpark.eip.service.common.msg.ObjectFactory}. */
	private final com.qpark.eip.service.common.msg.ObjectFactory commonOf = new com.qpark.eip.service.common.msg.ObjectFactory();
	/** The {@link GetReferenceData} gateway to show the aggregated response. */
	@Autowired
	@Qualifier("eipCallerComSamplesPlatformCommonAggregatGetReferenceDataGateway")
	private GetReferenceData getAggregatedReferenceData;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetAggregatedReferenceDataRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetAggregatedReferenceDataResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetAggregatedReferenceDataResponseType> getAggregatedReferenceData(
			final JAXBElement<GetAggregatedReferenceDataRequestType> message) {
		this.logger.debug("+getAggregatedReferenceData");
		GetAggregatedReferenceDataRequestType request = message.getValue();
		GetAggregatedReferenceDataResponseType response = this.of
				.createGetAggregatedReferenceDataResponseType();
		long start = System.currentTimeMillis();
		try {
			GetReferenceDataRequestType getReferenceDataRequestType = this.commonOf
					.createGetReferenceDataRequestType();
			getReferenceDataRequestType.setCriteria(request.getCriteria());
			JAXBElement<GetReferenceDataResponseType> aggregateResponse = this.getAggregatedReferenceData
					.invoke(this.commonOf.createGetReferenceDataRequest(
							getReferenceDataRequestType));
			if (aggregateResponse != null
					&& aggregateResponse.getValue() != null) {
				response.getFailure()
						.addAll(aggregateResponse.getValue().getFailure());
				response.getReferenceData().addAll(
						aggregateResponse.getValue().getReferenceData());
			} else {
				throw new IllegalStateException("Aggregated response empty!");
			}
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getAggregatedReferenceData duration {}",
					DateUtil.getDuration(start));
			this.logger.debug("-getAggregatedReferenceData #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetAggregatedReferenceDataResponse(response);
	}
}
