/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider.impl;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.samples.domain.serviceprovider.AppSecurityContextHandler;
import com.samples.domain.serviceprovider.OperationProviderIssTechSupport;
import com.samples.platform.service.iss.tech.support.msg.AppOperationEventRequestType;
import com.samples.platform.service.iss.tech.support.msg.AppOperationEventResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetAggregatedReferenceDataRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetAggregatedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetFailureMessageRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetFailureMessageResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetFlowReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetFlowReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetOperationReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetOperationReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetSoapFaultRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetSoapFaultResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.samples.platform.service.iss.tech.support.msg.gateway.AppOperationEvent;
import com.samples.platform.service.iss.tech.support.msg.gateway.GetAggregatedReferenceData;
import com.samples.platform.service.iss.tech.support.msg.gateway.GetFailureMessage;
import com.samples.platform.service.iss.tech.support.msg.gateway.GetFlowReport;
import com.samples.platform.service.iss.tech.support.msg.gateway.GetForwardedReferenceData;
import com.samples.platform.service.iss.tech.support.msg.gateway.GetOperationReport;
import com.samples.platform.service.iss.tech.support.msg.gateway.GetSoapFault;
import com.samples.platform.service.iss.tech.support.msg.gateway.GetSystemUserReport;

/**
 * Operation provider of service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component
public class OperationProviderIssTechSupportImpl
		implements OperationProviderIssTechSupport {
	/** The {@link Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(OperationProviderIssTechSupportImpl.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** Authenticated? */
	@Autowired
	private AppSecurityContextHandler setSecurityContextAuth;
	/** Gateway to app operation event. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportAppOperationEventGateway")
	private AppOperationEvent appOperationEvent;
	/** Gateway to get aggregated reference data. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportGetAggregatedReferenceDataGateway")
	private GetAggregatedReferenceData getAggregatedReferenceData;
	/** Gateway to get failure message. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportGetFailureMessageGateway")
	private GetFailureMessage getFailureMessage;
	/** Gateway to get flow report. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportGetFlowReportGateway")
	private GetFlowReport getFlowReport;
	/** Gateway to get forwarded reference data. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportGetForwardedReferenceDataGateway")
	private GetForwardedReferenceData getForwardedReferenceData;
	/** Gateway to get operation report. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportGetOperationReportGateway")
	private GetOperationReport getOperationReport;
	/** Gateway to get soap fault. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportGetSoapFaultGateway")
	private GetSoapFault getSoapFault;
	/** Gateway to get system user report. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusIssTechSupportGetSystemUserReportGateway")
	private GetSystemUserReport getSystemUserReport;

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

	/** Initalize the request. */
	private void requestInit() {
		if (this.setSecurityContextAuth != null) {
			this.setSecurityContextAuth.setAppSecurityContextAuthentication();
		}
	}

	/** Finalize the request. */
	private void requestFinalization() {
	}
	/// ** Get the size of the response value. */
	// private String responseValueSize(Object value) {
	// String s = "null";
	// if (value != null) {
	// if (Collection.class.isInstance(value)) {
	// s = String.valueOf(((Collection<?>) value).size());
	// } else if (value.getClass().isArray()) {
	// s = String.valueOf(((Object[]) value).length);
	// } else {
	// s = "1";
	// }
	// }
	// return s;
	// }

	/**
	 * @param request
	 *                    the {@link AppOperationEventRequestType}.
	 * @return the {@link AppOperationEventResponseType}.
	 */
	@Override
	public AppOperationEventResponseType appOperationEvent(
			final AppOperationEventRequestType request) {
		this.logger.debug("+appOperationEvent");
		AppOperationEventResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<AppOperationEventResponseType> response = this.appOperationEvent
					.invoke(this.of.createAppOperationEventRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" appOperationEvent duration {}",
					this.requestDuration(start));
			this.logger.debug("-appOperationEvent");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetAggregatedReferenceDataRequestType}.
	 * @return the {@link GetAggregatedReferenceDataResponseType}.
	 */
	@Override
	public GetAggregatedReferenceDataResponseType getAggregatedReferenceData(
			final GetAggregatedReferenceDataRequestType request) {
		this.logger.debug("+getAggregatedReferenceData");
		GetAggregatedReferenceDataResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetAggregatedReferenceDataResponseType> response = this.getAggregatedReferenceData
					.invoke(this.of
							.createGetAggregatedReferenceDataRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getAggregatedReferenceData duration {}",
					this.requestDuration(start));
			this.logger.debug("-getAggregatedReferenceData");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetFailureMessageRequestType}.
	 * @return the {@link GetFailureMessageResponseType}.
	 */
	@Override
	public GetFailureMessageResponseType getFailureMessage(
			final GetFailureMessageRequestType request) {
		this.logger.debug("+getFailureMessage");
		GetFailureMessageResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetFailureMessageResponseType> response = this.getFailureMessage
					.invoke(this.of.createGetFailureMessageRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getFailureMessage duration {}",
					this.requestDuration(start));
			this.logger.debug("-getFailureMessage");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetFlowReportRequestType}.
	 * @return the {@link GetFlowReportResponseType}.
	 */
	@Override
	public GetFlowReportResponseType getFlowReport(
			final GetFlowReportRequestType request) {
		this.logger.debug("+getFlowReport");
		GetFlowReportResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetFlowReportResponseType> response = this.getFlowReport
					.invoke(this.of.createGetFlowReportRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getFlowReport duration {}",
					this.requestDuration(start));
			this.logger.debug("-getFlowReport");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetForwardedReferenceDataRequestType}.
	 * @return the {@link GetForwardedReferenceDataResponseType}.
	 */
	@Override
	public GetForwardedReferenceDataResponseType getForwardedReferenceData(
			final GetForwardedReferenceDataRequestType request) {
		this.logger.debug("+getForwardedReferenceData");
		GetForwardedReferenceDataResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetForwardedReferenceDataResponseType> response = this.getForwardedReferenceData
					.invoke(this.of
							.createGetForwardedReferenceDataRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getForwardedReferenceData duration {}",
					this.requestDuration(start));
			this.logger.debug("-getForwardedReferenceData");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetOperationReportRequestType}.
	 * @return the {@link GetOperationReportResponseType}.
	 */
	@Override
	public GetOperationReportResponseType getOperationReport(
			final GetOperationReportRequestType request) {
		this.logger.debug("+getOperationReport");
		GetOperationReportResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetOperationReportResponseType> response = this.getOperationReport
					.invoke(this.of.createGetOperationReportRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getOperationReport duration {}",
					this.requestDuration(start));
			this.logger.debug("-getOperationReport");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetSoapFaultRequestType}.
	 * @return the {@link GetSoapFaultResponseType}.
	 */
	@Override
	public GetSoapFaultResponseType getSoapFault(
			final GetSoapFaultRequestType request) {
		this.logger.debug("+getSoapFault");
		GetSoapFaultResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetSoapFaultResponseType> response = this.getSoapFault
					.invoke(this.of.createGetSoapFaultRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getSoapFault duration {}",
					this.requestDuration(start));
			this.logger.debug("-getSoapFault");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetSystemUserReportRequestType}.
	 * @return the {@link GetSystemUserReportResponseType}.
	 */
	@Override
	public GetSystemUserReportResponseType getSystemUserReport(
			final GetSystemUserReportRequestType request) {
		this.logger.debug("+getSystemUserReport");
		GetSystemUserReportResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetSystemUserReportResponseType> response = this.getSystemUserReport
					.invoke(this.of.createGetSystemUserReportRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getSystemUserReport duration {}",
					this.requestDuration(start));
			this.logger.debug("-getSystemUserReport");
		}
		return value;
	}
}