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

import com.qpark.eip.service.common.msg.GetReferenceDataRequestType;
import com.qpark.eip.service.common.msg.GetReferenceDataResponseType;
import com.qpark.eip.service.common.msg.GetServiceStatusRequestType;
import com.qpark.eip.service.common.msg.GetServiceStatusResponseType;
import com.qpark.eip.service.common.msg.ObjectFactory;
import com.qpark.eip.service.common.msg.gateway.GetReferenceData;
import com.qpark.eip.service.common.msg.gateway.GetServiceStatus;
import com.samples.domain.serviceprovider.AppSecurityContextHandler;
import com.samples.domain.serviceprovider.OperationProviderCommon;

/**
 * Operation provider of service <code>common</code>.
 *
 * @author bhausen
 */
@Component
public class OperationProviderCommonImpl implements OperationProviderCommon {
	/** The {@link Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(OperationProviderCommonImpl.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** Authenticated? */
	@Autowired
	private AppSecurityContextHandler setSecurityContextAuth;
	/** Gateway to get reference data. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusCommonGetReferenceDataGateway")
	private GetReferenceData getReferenceData;
	/** Gateway to get service status. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusCommonGetServiceStatusGateway")
	private GetServiceStatus getServiceStatus;

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
	 *                    the {@link GetReferenceDataRequestType}.
	 * @return the {@link GetReferenceDataResponseType}.
	 */
	@Override
	public GetReferenceDataResponseType getReferenceData(
			final GetReferenceDataRequestType request) {
		this.logger.debug("+getReferenceData");
		GetReferenceDataResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetReferenceDataResponseType> response = this.getReferenceData
					.invoke(this.of.createGetReferenceDataRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getReferenceData duration {}",
					this.requestDuration(start));
			this.logger.debug("-getReferenceData");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetServiceStatusRequestType}.
	 * @return the {@link GetServiceStatusResponseType}.
	 */
	@Override
	public GetServiceStatusResponseType getServiceStatus(
			final GetServiceStatusRequestType request) {
		this.logger.debug("+getServiceStatus");
		GetServiceStatusResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetServiceStatusResponseType> response = this.getServiceStatus
					.invoke(this.of.createGetServiceStatusRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getServiceStatus duration {}",
					this.requestDuration(start));
			this.logger.debug("-getServiceStatus");
		}
		return value;
	}
}