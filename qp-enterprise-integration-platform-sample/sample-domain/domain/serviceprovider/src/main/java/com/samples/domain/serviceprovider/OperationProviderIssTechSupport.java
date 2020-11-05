/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider;

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

public interface OperationProviderIssTechSupport {

	/**
	 * @param request the {@link AppOperationEventRequestType}.
	 * @return the {@link AppOperationEventResponseType}.
	 */
	AppOperationEventResponseType appOperationEvent(AppOperationEventRequestType request);

	/**
	 * @param request the {@link GetAggregatedReferenceDataRequestType}.
	 * @return the {@link GetAggregatedReferenceDataResponseType}.
	 */
	GetAggregatedReferenceDataResponseType getAggregatedReferenceData(GetAggregatedReferenceDataRequestType request);

	/**
	 * @param request the {@link GetFailureMessageRequestType}.
	 * @return the {@link GetFailureMessageResponseType}.
	 */
	GetFailureMessageResponseType getFailureMessage(GetFailureMessageRequestType request);

	/**
	 * @param request the {@link GetFlowReportRequestType}.
	 * @return the {@link GetFlowReportResponseType}.
	 */
	GetFlowReportResponseType getFlowReport(GetFlowReportRequestType request);

	/**
	 * @param request the {@link GetForwardedReferenceDataRequestType}.
	 * @return the {@link GetForwardedReferenceDataResponseType}.
	 */
	GetForwardedReferenceDataResponseType getForwardedReferenceData(GetForwardedReferenceDataRequestType request);

	/**
	 * @param request the {@link GetOperationReportRequestType}.
	 * @return the {@link GetOperationReportResponseType}.
	 */
	GetOperationReportResponseType getOperationReport(GetOperationReportRequestType request);

	/**
	 * @param request the {@link GetSoapFaultRequestType}.
	 * @return the {@link GetSoapFaultResponseType}.
	 */
	GetSoapFaultResponseType getSoapFault(GetSoapFaultRequestType request);

	/**
	 * @param request the {@link GetSystemUserReportRequestType}.
	 * @return the {@link GetSystemUserReportResponseType}.
	 */
	GetSystemUserReportResponseType getSystemUserReport(GetSystemUserReportRequestType request);

}