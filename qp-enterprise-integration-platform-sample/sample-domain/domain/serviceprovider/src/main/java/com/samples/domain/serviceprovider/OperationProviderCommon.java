/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider;

import com.qpark.eip.service.common.msg.GetReferenceDataRequestType;
import com.qpark.eip.service.common.msg.GetReferenceDataResponseType;
import com.qpark.eip.service.common.msg.GetServiceStatusRequestType;
import com.qpark.eip.service.common.msg.GetServiceStatusResponseType;

public interface OperationProviderCommon {

	/**
	 * @param request the {@link GetReferenceDataRequestType}.
	 * @return the {@link GetReferenceDataResponseType}.
	 */
	GetReferenceDataResponseType getReferenceData(GetReferenceDataRequestType request);

	/**
	 * @param request the {@link GetServiceStatusRequestType}.
	 * @return the {@link GetServiceStatusResponseType}.
	 */
	GetServiceStatusResponseType getServiceStatus(GetServiceStatusRequestType request);

}