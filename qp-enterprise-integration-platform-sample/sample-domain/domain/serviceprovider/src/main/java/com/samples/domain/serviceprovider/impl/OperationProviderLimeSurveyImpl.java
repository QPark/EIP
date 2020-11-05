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

import com.qpark.eip.service.lime.survey.msg.GetSessionKeyRequestType;
import com.qpark.eip.service.lime.survey.msg.GetSessionKeyResponseType;
import com.qpark.eip.service.lime.survey.msg.ListSurveysRequestType;
import com.qpark.eip.service.lime.survey.msg.ListSurveysResponseType;
import com.qpark.eip.service.lime.survey.msg.ObjectFactory;
import com.qpark.eip.service.lime.survey.msg.gateway.GetSessionKey;
import com.qpark.eip.service.lime.survey.msg.gateway.ListSurveys;
import com.samples.domain.serviceprovider.AppSecurityContextHandler;
import com.samples.domain.serviceprovider.OperationProviderLimeSurvey;

/**
 * Operation provider of service <code>lime.survey</code>.
 *
 * @author bhausen
 */
@Component
public class OperationProviderLimeSurveyImpl
		implements OperationProviderLimeSurvey {
	/** The {@link Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(OperationProviderLimeSurveyImpl.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** Authenticated? */
	@Autowired
	private AppSecurityContextHandler setSecurityContextAuth;
	/** Gateway to get session key. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusLimeSurveyGetSessionKeyGateway")
	private GetSessionKey getSessionKey;
	/** Gateway to list surveys. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusLimeSurveyListSurveysGateway")
	private ListSurveys listSurveys;

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
	 *                    the {@link GetSessionKeyRequestType}.
	 * @return the {@link GetSessionKeyResponseType}.
	 */
	@Override
	public GetSessionKeyResponseType getSessionKey(
			final GetSessionKeyRequestType request) {
		this.logger.debug("+getSessionKey");
		GetSessionKeyResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetSessionKeyResponseType> response = this.getSessionKey
					.invoke(this.of.createGetSessionKeyRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getSessionKey duration {}",
					this.requestDuration(start));
			this.logger.debug("-getSessionKey");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link ListSurveysRequestType}.
	 * @return the {@link ListSurveysResponseType}.
	 */
	@Override
	public ListSurveysResponseType listSurveys(
			final ListSurveysRequestType request) {
		this.logger.debug("+listSurveys");
		ListSurveysResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<ListSurveysResponseType> response = this.listSurveys
					.invoke(this.of.createListSurveysRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" listSurveys duration {}",
					this.requestDuration(start));
			this.logger.debug("-listSurveys");
		}
		return value;
	}
}