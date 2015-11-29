/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.client;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.qpark.eip.model.common.ReferenceDataCriteriaType;
import com.samples.platform.model.iss.tech.support.SystemUserReportCriteriaType;
import com.samples.platform.service.iss.tech.support.msg.GetAggregatedReferenceDataRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetAggregatedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportResponseType;

public class IssTechSupportServiceClientExtension
		extends IssTechSupportServiceClient {
	/**
	 * Invoke operation get aggregated reference data on service
	 * <code>iss.tech.support</code>.
	 *
	 * @param userName
	 *            the user name.
	 * @return a {@link GetAggregatedReferenceDataResponseType}.
	 */
	public final GetAggregatedReferenceDataResponseType getAggregatedReferenceData(
			final String userName) {
		this.logger.debug("+getAggregatedReferenceData");
		GetAggregatedReferenceDataRequestType request = this.getObjectFactory()
				.createGetAggregatedReferenceDataRequestType();
		request.setUserName(userName);
		request.setCriteria(new ReferenceDataCriteriaType());
		GetAggregatedReferenceDataResponseType response = this
				.getAggregatedReferenceData(request);
		this.logger.debug("-getAggregatedReferenceData");
		return response;
	}

	/**
	 * Invoke operation get forwarded reference data on service
	 * <code>iss.tech.support</code>.
	 *
	 * @param userName
	 *            the user name.
	 * @return a {@link GetForwardedReferenceDataResponseType}.
	 */
	public final GetForwardedReferenceDataResponseType getForwardedReferenceData(
			final String userName) {
		this.logger.debug("+getForwardedReferenceData");
		GetForwardedReferenceDataRequestType request = this.getObjectFactory()
				.createGetForwardedReferenceDataRequestType();
		request.setUserName(userName);
		request.setCriteria(new ReferenceDataCriteriaType());
		GetForwardedReferenceDataResponseType response = this
				.getForwardedReferenceData(request);
		this.logger.debug("-getForwardedReferenceData");
		return response;
	}

	/**
	 * Invoke operation get system user report on service
	 * <code>iss.tech.support</code>.
	 *
	 * @param userName
	 *            the user name.
	 * @return a {@link GetSystemUserReportResponseType}.
	 */
	public final GetSystemUserReportResponseType getSystemUserReport(
			final String userName, final Date day) {
		this.logger.debug("+getSystemUserReport");
		GetSystemUserReportRequestType request = this.getObjectFactory()
				.createGetSystemUserReportRequestType();
		request.setUserName(userName);
		request.setCriteria(new SystemUserReportCriteriaType());
		if (day != null) {
			try {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(day);
				DatatypeFactory df = DatatypeFactory.newInstance();
				request.getCriteria().setDate(df.newXMLGregorianCalendar(gc));
			} catch (DatatypeConfigurationException e) {
				this.logger.error(e.getMessage(), e);
			}
		}
		GetSystemUserReportResponseType response = this
				.getSystemUserReport(request);
		this.logger.debug("-getSystemUserReport");
		return response;
	}

}
