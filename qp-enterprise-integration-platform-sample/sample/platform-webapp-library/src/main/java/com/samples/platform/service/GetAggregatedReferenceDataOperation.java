package com.samples.platform.service;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.service.common.msg.GetReferenceDataRequestType;
import com.qpark.eip.service.common.msg.GetReferenceDataResponseType;
import com.qpark.eip.service.common.msg.gateway.GetReferenceData;
import com.samples.platform.service.iss.tec.support.msg.GetAggregatedReferenceDataRequestType;
import com.samples.platform.service.iss.tec.support.msg.GetAggregatedReferenceDataResponseType;
import com.samples.platform.service.iss.tec.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get aggregated reference data on service
 * <code>iss.tec.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTecSupportGetAggregatedReferenceData")
public class GetAggregatedReferenceDataOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetAggregatedReferenceDataOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
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
					this.requestDuration(start));
			this.logger.debug("-getAggregatedReferenceData #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetAggregatedReferenceDataResponse(response);
	}

	/**
	 * @param start
	 * @return the duration in 000:00:00.000 format.
	 */
	private String requestDuration(final long start) {
		long millis = System.currentTimeMillis() - start;
		String hmss = String.format("%03d:%02d:%02d.%03d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS
						.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES
						.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				TimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS
						.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		return hmss;
	}
}
