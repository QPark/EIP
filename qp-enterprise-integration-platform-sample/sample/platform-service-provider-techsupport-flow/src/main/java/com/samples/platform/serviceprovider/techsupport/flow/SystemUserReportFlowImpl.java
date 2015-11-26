package com.samples.platform.serviceprovider.techsupport.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
 * {@link #invokeFlow(SystemUserReportFlowRequestType)} calls
 * {@link #executeRequest(GetSystemUserReportRequestType)} and
 * {@link #processResponse(SystemUserLogResponseType)}.
 *
 * @author bhausen
 */
public class SystemUserReportFlowImpl implements SystemUserReportFlow {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SystemUserReportFlowImpl.class);
	/** The {@link SystemUserLogFlowGateway}. */
	@Autowired
	private SystemUserLogFlowGateway flowGateway;
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
	 * @return the {@link SystemUserLogResponseType}
	 */
	@Override
	@InsightOperation
	public SystemUserLogRequestType executeRequest(
			final GetSystemUserReportRequestType in) {
		this.logger.debug("+executeRequest");
		/* Create mapIn. */
		SystemUserLogCriteriaTypeMapRequestType mapIn = new SystemUserLogCriteriaTypeMapRequestType();
		mapIn.setUserName(in.getUserName());
		mapIn.setCriteria(in.getCriteria());

		/* Execute mapInOutRequest. */
		SystemUserLogCriteriaTypeMapResponseType mapOut = this
				.mapInOutRequest(mapIn);

		/* Setup value to return. */
		ExtSystemUserLogCriteriaType criteria = new ExtSystemUserLogCriteriaType();
		SystemUserLogRequestType value = new SystemUserLogRequestType();
		value.setCriteria(criteria);
		value.setUserName(in.getUserName());

		/* BasicMapping: Map criteria as part of return. */
		criteria.setLogDate(mapOut.getCriteria().getDate().getReturn());

		this.logger.debug("-executeRequest");
		return value;
	}

	/**
	 * Invoke the flow. This calls executeRequest and processResponse.
	 *
	 * @param request
	 *            the {@link SystemUserReportFlowResponseType}
	 * @return the {@link SystemUserReportFlowRequestType}
	 */
	@Override
	@InsightEndPoint
	public SystemUserReportFlowResponseType invokeFlow(
			final SystemUserReportFlowRequestType request) {
		/* Execute request. */
		SystemUserLogRequestType executeRequest = this
				.executeRequest(request.getIn());

		/* Call the gateway. */
		SystemUserLogResponseType gatewayResponse = this.flowGateway
				.getSystemUserLog(executeRequest);

		/* Process response. */
		GetSystemUserReportResponseType processResponse = this
				.processResponse(gatewayResponse);

		/* Prepare return value. */
		SystemUserReportFlowResponseType value = new SystemUserReportFlowResponseType();
		value.setOut(processResponse);
		return value;
	}

	/**
	 * Map a {@link SystemUserLogCriteriaTypeMapRequestType} to a
	 * {@link SystemUserLogCriteriaTypeMapResponseType}.
	 *
	 * @param mapIn
	 *            the {@link SystemUserLogCriteriaTypeMapRequestType}
	 * @return the {@link SystemUserLogCriteriaTypeMapResponseType}
	 */
	@Override
	@InsightOperation
	public SystemUserLogCriteriaTypeMapResponseType mapInOutRequest(
			final SystemUserLogCriteriaTypeMapRequestType mapIn) {
		this.logger.debug("+mapInOutRequest");
		SystemUserLogCriteriaTypeMapResponseType value = this.systemUserLogCriteriaTypeMappingOperation
				.invokeMapping(mapIn);
		this.logger.debug("-mapInOutRequest");
		return value;
	}

	/**
	 * Map a {@link SystemUserReportTypeMapRequestType} to a
	 * {@link SystemUserReportTypeMapResponseType}.
	 *
	 * @param mapIn
	 *            the {@link SystemUserReportTypeMapRequestType}
	 * @return the {@link SystemUserReportTypeMapResponseType}
	 */
	@Override
	@InsightOperation
	public SystemUserReportTypeMapResponseType mapInOutResponse(
			final SystemUserReportTypeMapRequestType mapIn) {
		this.logger.debug("+mapInOutResponse");
		SystemUserReportTypeMapResponseType value = this.systemUserReportTypeMappingOperation
				.invokeMapping(mapIn);
		this.logger.debug("-mapInOutResponse");
		return value;
	}

	/**
	 * Process the response. Contains the call of the <i>mapInOutResponse</i>
	 * method(s).
	 *
	 * @param in
	 *            the {@link SystemUserLogResponseType}
	 * @return the {@link GetSystemUserReportResponseType}
	 */
	@Override
	@InsightOperation
	public GetSystemUserReportResponseType processResponse(
			final SystemUserLogResponseType in) {
		this.logger.debug("+processResponse");
		/* Create mapIn. */
		SystemUserReportTypeMapRequestType mapIn = new SystemUserReportTypeMapRequestType();
		mapIn.getSystemUserLog().addAll(in.getSystemUserLog());

		/* Execute mapInOutResponse. */
		SystemUserReportTypeMapResponseType mapOut = this
				.mapInOutResponse(mapIn);
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
		this.logger.debug("-processResponse");
		return value;
	}
}
