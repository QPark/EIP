/*******************************************************************************
<<<<<<< HEAD
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
=======
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.techsupport.flow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.UUID;
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.spring.statistics.AsyncFlowLogMessagePersistence;
<<<<<<< HEAD
import com.qpark.eip.inf.FlowContext;
=======
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
import com.qpark.eip.service.base.msg.FailureType;
import com.samples.platform.core.flow.SystemUserLogFlowGateway;
import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogCriteriaType;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlow;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowRequestType;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowResponseType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogRequestType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogResponseType;
import com.samples.platform.inf.iss.tech.support.operation.SystemUserLogCriteriaTypeMappingOperation;
import com.samples.platform.inf.iss.tech.support.operation.SystemUserReportTypeMappingOperation;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserLogCriteriaTypeMapRequestType;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserLogCriteriaTypeMapResponseType;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserReportTypeMapRequestType;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserReportTypeMapResponseType;
import com.samples.platform.inf.iss.tech.support.type.TechnicalSupportSystemUserReportType;
import com.samples.platform.model.iss.tech.support.SystemUserReportType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportResponseType;
import com.springsource.insight.annotation.InsightEndPoint;
import com.springsource.insight.annotation.InsightOperation;

/**
 * The {@link SystemUserReportFlow}.
 * <p/>
 * {@link #invokeFlow(SystemUserReportFlowRequestType, FlowContext)} calls
 * {@link #executeRequest(GetSystemUserReportRequestType, FlowContext)} and
 * {@link #processResponse(SystemUserLogResponseType, FlowContext)}.
 *
 * @author bhausen
 */
public class SystemUserReportFlowImpl implements SystemUserReportFlow {
	/** The {@link SystemUserLogFlowGateway}. */
	@Autowired
	private SystemUserLogFlowGateway flowGateway;
	/** The {@link AsyncFlowLogMessagePersistence}. */
	@Autowired
	private AsyncFlowLogMessagePersistence flowLogMessagePersistence;
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SystemUserReportFlowImpl.class);
	/** The {@link SystemUserLogCriteriaTypeMappingOperation}. */
	@Autowired
	private SystemUserLogCriteriaTypeMappingOperation systemUserLogCriteriaTypeMappingOperation;
	/** The {@link SystemUserReportTypeMappingOperation}. */
	@Autowired
	private SystemUserReportTypeMappingOperation systemUserReportTypeMappingOperation;

	/**
	 * Execute the request. Contains the call of the <i>mapInOutRequest</i>
	 * method(s).
	 *
	 * @param in
	 *            the {@link GetSystemUserReportRequestType}
	 * @param flowContext
	 *            the {@link FlowContext}
	 * @return the {@link SystemUserLogResponseType}
	 */
	@Override
	@InsightOperation
	public SystemUserLogRequestType executeRequest(
			final GetSystemUserReportRequestType in,
			final FlowContext flowContext) {
		this.logger.debug("+executeRequest {}", flowContext.getSessionId());
		/* Create mapIn. */
		SystemUserLogCriteriaTypeMapRequestType mapIn = new SystemUserLogCriteriaTypeMapRequestType();
		mapIn.setUserName(in.getUserName());
		mapIn.setCriteria(in.getCriteria());

		/* Execute mapInOutRequest. */
		SystemUserLogCriteriaTypeMapResponseType mapOut = this
				.mapInOutRequest(mapIn, flowContext);

		/* Setup value to return. */
		ExtSystemUserLogCriteriaType criteria = new ExtSystemUserLogCriteriaType();
		SystemUserLogRequestType value = new SystemUserLogRequestType();
		value.setCriteria(criteria);
		value.setUserName(in.getUserName());

		/* BasicMapping: Map criteria as part of return. */
		criteria.setLogDate(mapOut.getCriteria().getDate().getReturn());

		this.logger.debug("-executeRequest {}", flowContext.getSessionId());
		return value;
	}

	/**
	 * Invoke the flow. This calls executeRequest and processResponse.
	 *
	 * @param request
	 *            the {@link SystemUserReportFlowResponseType}
	 * @param flowContext
	 *            the {@link FlowContext}
	 * @return the {@link SystemUserReportFlowRequestType}
	 */
	@Override
	@InsightEndPoint
	public SystemUserReportFlowResponseType invokeFlow(
<<<<<<< HEAD
			final SystemUserReportFlowRequestType request,
			final FlowContext flowContext) {
=======
			final SystemUserReportFlowRequestType request) {
		UUID sessionId = UUID.randomUUID();
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
		List<FailureType> failures = new ArrayList<FailureType>();
		SystemUserLogRequestType executeRequest = null;
		SystemUserLogResponseType gatewayResponse = null;
		GetSystemUserReportResponseType processResponse = new GetSystemUserReportResponseType();
<<<<<<< HEAD

		/* Execute request. */
		try {
			executeRequest = this.executeRequest(request.getIn(), flowContext);
		} catch (Exception e) {
			this.flowLogMessagePersistence
					.submitFlowLogMessage(this.getFlowLogMessage(flowContext,
							AsyncFlowLogMessagePersistence.TYPE_FLOW,
							AsyncFlowLogMessagePersistence.STEP_REQUEST_EXECUTION,
							AsyncFlowLogMessagePersistence.SEVERITY_ERROR,
							e.getMessage()));
			this.logger.error(new StringBuffer(128)
					.append(flowContext.getSessionId()).append(" ")
					.append(flowContext.getRequesterUserName()).append(": ")
					.append(e.getMessage()).toString(), e);

=======

		/* Execute request. */
		try {
			executeRequest = this.executeRequest(request.getIn());
		} catch (Exception e) {
			this.flowLogMessagePersistence
					.submitFlowLogMessage(this.getFlowLogMessage(sessionId,
							AsyncFlowLogMessagePersistence.TYPE_FLOW,
							AsyncFlowLogMessagePersistence.STEP_REQUEST_EXECUTION,
							AsyncFlowLogMessagePersistence.SEVERITY_ERROR,
							e.getMessage()));
			this.logger.error(e.getMessage(), e);
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
		}
		/* Call the gateway. */
		try {
			gatewayResponse = this.flowGateway.getSystemUserLog(executeRequest);
			if (gatewayResponse != null) {
				failures.addAll(gatewayResponse.getFailure());
			}
			this.flowLogMessagePersistence
<<<<<<< HEAD
					.submitFlowLogMessage(this.getFlowLogMessage(flowContext,
=======
					.submitFlowLogMessage(this.getFlowLogMessage(sessionId,
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
							AsyncFlowLogMessagePersistence.TYPE_FLOW,
							AsyncFlowLogMessagePersistence.STEP_FLOW_GATEWAY_EXECUTION,
							AsyncFlowLogMessagePersistence.SEVERITY_INFORMATION,
							"Retrieved SystemUserLogMessages"));
		} catch (Exception e) {
			this.flowLogMessagePersistence
<<<<<<< HEAD
					.submitFlowLogMessage(this.getFlowLogMessage(flowContext,
=======
					.submitFlowLogMessage(this.getFlowLogMessage(sessionId,
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
							AsyncFlowLogMessagePersistence.TYPE_FLOW,
							AsyncFlowLogMessagePersistence.STEP_FLOW_GATEWAY_EXECUTION,
							AsyncFlowLogMessagePersistence.SEVERITY_ERROR,
							e.getMessage()));
<<<<<<< HEAD
			this.logger.error(new StringBuffer(128)
					.append(flowContext.getSessionId()).append(" ")
					.append(flowContext.getRequesterUserName()).append(": ")
					.append(e.getMessage()).toString(), e);
=======
			this.logger.error(e.getMessage(), e);
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
		}

		/* Process response. */
		try {
<<<<<<< HEAD
			processResponse = this.processResponse(gatewayResponse,
					flowContext);
		} catch (Exception e) {
			this.flowLogMessagePersistence
					.submitFlowLogMessage(this.getFlowLogMessage(flowContext,
=======
			processResponse = this.processResponse(gatewayResponse);
		} catch (Exception e) {
			this.flowLogMessagePersistence
					.submitFlowLogMessage(this.getFlowLogMessage(sessionId,
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
							AsyncFlowLogMessagePersistence.TYPE_FLOW,
							AsyncFlowLogMessagePersistence.STEP_RESPONSE_PROCESSING,
							AsyncFlowLogMessagePersistence.SEVERITY_ERROR,
							e.getMessage()));
<<<<<<< HEAD
			this.logger.error(new StringBuffer(128)
					.append(flowContext.getSessionId()).append(" ")
					.append(flowContext.getRequesterUserName()).append(": ")
					.append(e.getMessage()).toString(), e);
=======
			this.logger.error(e.getMessage(), e);
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
		}
		/* Enter failure messages. */
		processResponse.getFailure().addAll(failures);

		/* Prepare return value. */
		SystemUserReportFlowResponseType value = new SystemUserReportFlowResponseType();
		value.setOut(processResponse);
		return value;
	}

	/**
	 * Get the {@link FlowLogMessageType}.
	 *
<<<<<<< HEAD
	 * @param flowContext
	 *            the {@link FlowContext}.
=======
	 * @param sessionId
	 *            the session id.
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
	 * @param type
	 *            the type.
	 * @param step
	 *            the step.
	 * @param severity
	 *            the severity.
	 * @param data
	 *            the data.
	 * @return the {@link FlowLogMessageType}.
	 */
<<<<<<< HEAD
	private FlowLogMessageType getFlowLogMessage(final FlowContext flowContext,
=======
	private FlowLogMessageType getFlowLogMessage(final UUID sessionId,
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
			final String type, final String step, final String severity,
			final String data) {
		FlowLogMessageType log = new FlowLogMessageType();
		log.setClassification("");
		log.setFlowIdentifier(this.getClass().getName());
		log.setFlowName(this.getClass().getSimpleName());
<<<<<<< HEAD
		log.setFlowSession(flowContext.getSessionId());
=======
		log.setFlowSession(sessionId.toString());
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
		log.setFlowStep(step);
		log.setLogMessageType(type);
		log.setLogTime(DateUtil.get(new Date()));
		log.setSeverity(severity);
		log.setSubClassification("");
		log.setDataDescription(data);
		return log;
	}

	/**
	 * Map a {@link SystemUserLogCriteriaTypeMapRequestType} to a
	 * {@link SystemUserLogCriteriaTypeMapResponseType}.
	 *
	 * @param mapIn
	 *            the {@link SystemUserLogCriteriaTypeMapRequestType}
	 * @param flowContext
	 *            the {@link FlowContext}
	 * @return the {@link SystemUserLogCriteriaTypeMapResponseType}
	 */
	@Override
	@InsightOperation
	public SystemUserLogCriteriaTypeMapResponseType mapInOutRequest(
			final SystemUserLogCriteriaTypeMapRequestType mapIn,
			final FlowContext flowContext) {
		this.logger.debug("+mapInOutRequest {}", flowContext.getSessionId());
		SystemUserLogCriteriaTypeMapResponseType value = this.systemUserLogCriteriaTypeMappingOperation
				.invokeMapping(mapIn, flowContext);
		this.logger.debug("-mapInOutRequest {}", flowContext.getSessionId());
		return value;
	}

	/**
	 * Map a {@link SystemUserReportTypeMapRequestType} to a
	 * {@link SystemUserReportTypeMapResponseType}.
	 *
	 * @param mapIn
	 *            the {@link SystemUserReportTypeMapRequestType}
	 * @param flowContext
	 *            the {@link FlowContext}
	 * @return the {@link SystemUserReportTypeMapResponseType}
	 */
	@Override
	@InsightOperation
	public SystemUserReportTypeMapResponseType mapInOutResponse(
			final SystemUserReportTypeMapRequestType mapIn,
			final FlowContext flowContext) {
		this.logger.debug("+mapInOutResponse {}", flowContext.getSessionId());
		SystemUserReportTypeMapResponseType value = this.systemUserReportTypeMappingOperation
				.invokeMapping(mapIn, flowContext);
		this.logger.debug("-mapInOutResponse {}", flowContext.getSessionId());
		return value;
	}

	/**
	 * Process the response. Contains the call of the <i>mapInOutResponse</i>
	 * method(s).
	 *
	 * @param in
	 *            the {@link SystemUserLogResponseType}
	 * @param flowContext
	 *            the {@link FlowContext}
	 * @return the {@link GetSystemUserReportResponseType}
	 */
	@Override
	@InsightOperation
	public GetSystemUserReportResponseType processResponse(
			final SystemUserLogResponseType in, final FlowContext flowContext) {
		this.logger.debug("+processResponse {}", flowContext.getSessionId());
		/* Create mapIn. */
		SystemUserReportTypeMapRequestType mapIn = new SystemUserReportTypeMapRequestType();
		mapIn.getSystemUserLog().addAll(in.getSystemUserLog());

		/* Execute mapInOutResponse. */
		SystemUserReportTypeMapResponseType mapOut = this
				.mapInOutResponse(mapIn, flowContext);
		mapOut.getSystemUserReport();
		/* Setup value to return. */
		SystemUserReportType report;
		GetSystemUserReportResponseType value = new GetSystemUserReportResponseType();
		for (TechnicalSupportSystemUserReportType mapped : mapOut
				.getSystemUserReport()) {
			report = new SystemUserReportType();
			value.getReport().add(report);

			/* BasicMapping: Map report as part of return. */
			report.setConsumerSystemUser(
					mapped.getConsumerSystemUser().getReturn());
			report.setDate(mapped.getDate().getReturn());
			report.setErrors(mapped.getErrors().getReturn());
			report.setOperation(mapped.getOperation().getReturn());
			report.setRequests(mapped.getRequests().getReturn());
			report.setRequestsDenied(mapped.getRequestsDenied().getReturn());
			report.setService(mapped.getService().getReturn());
			report.setServiceProvider(mapped.getServiceProvider().getReturn());
			report.setVersion(mapped.getVersion().getReturn());
		}
		this.logger.debug("-processResponse {}", flowContext.getSessionId());
		return value;
	}
}
