/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider;

import com.qpark.eip.service.lime.survey.msg.GetSessionKeyRequestType;
import com.qpark.eip.service.lime.survey.msg.GetSessionKeyResponseType;
import com.qpark.eip.service.lime.survey.msg.ListSurveysRequestType;
import com.qpark.eip.service.lime.survey.msg.ListSurveysResponseType;

public interface OperationProviderLimeSurvey {

	/**
	 * @param request the {@link GetSessionKeyRequestType}.
	 * @return the {@link GetSessionKeyResponseType}.
	 */
	GetSessionKeyResponseType getSessionKey(GetSessionKeyRequestType request);

	/**
	 * @param request the {@link ListSurveysRequestType}.
	 * @return the {@link ListSurveysResponseType}.
	 */
	ListSurveysResponseType listSurveys(ListSurveysRequestType request);

}