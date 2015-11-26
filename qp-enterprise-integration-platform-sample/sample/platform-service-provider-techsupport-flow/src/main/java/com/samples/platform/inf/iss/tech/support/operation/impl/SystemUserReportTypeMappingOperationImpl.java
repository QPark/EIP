package com.samples.platform.inf.iss.tech.support.operation.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qpark.eip.inf.icd.map.DirectMappingType;
import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType;
import com.samples.platform.inf.iss.tech.support.map.SystemUserLogContextMappingType;
import com.samples.platform.inf.iss.tech.support.map.SystemUserLogLogDateMappingType;
import com.samples.platform.inf.iss.tech.support.map.SystemUserLogOperationNameMappingType;
import com.samples.platform.inf.iss.tech.support.map.SystemUserLogServiceNameMappingType;
import com.samples.platform.inf.iss.tech.support.map.SystemUserLogUserNameMappingType;
import com.samples.platform.inf.iss.tech.support.map.SystemUserLogVersionMappingType;
import com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogRequestsDeniedMappingType;
import com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogRequestsGrantedMappingType;
import com.samples.platform.inf.iss.tech.support.map.TechSupportSystemUserLogResponseFaultsMappingType;
import com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogRequestsDeniedMapper;
import com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogRequestsGrantedMapper;
import com.samples.platform.inf.iss.tech.support.mapper.complex.TechSupportSystemUserLogResponseFaultsMapper;
import com.samples.platform.inf.iss.tech.support.mapper.direct.SystemUserLogContextMapper;
import com.samples.platform.inf.iss.tech.support.mapper.direct.SystemUserLogLogDateMapper;
import com.samples.platform.inf.iss.tech.support.mapper.direct.SystemUserLogOperationNameMapper;
import com.samples.platform.inf.iss.tech.support.mapper.direct.SystemUserLogServiceNameMapper;
import com.samples.platform.inf.iss.tech.support.mapper.direct.SystemUserLogUserNameMapper;
import com.samples.platform.inf.iss.tech.support.mapper.direct.SystemUserLogVersionMapper;
import com.samples.platform.inf.iss.tech.support.operation.SystemUserReportTypeMappingOperation;
import com.samples.platform.inf.iss.tech.support.svc.ObjectFactory;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserReportTypeMapRequestType;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserReportTypeMapResponseType;
import com.samples.platform.inf.iss.tech.support.type.TechnicalSupportSystemUserReportType;

/**
 * The {@link SystemUserReportTypeMappingOperation} implementation.
 *
 * <pre>
 * Generated at 2015-11-26T15:44:53
 * </pre>
 *
 * @author bhausen
 */
@Component
public class SystemUserReportTypeMappingOperationImpl
		implements SystemUserReportTypeMappingOperation {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SystemUserReportTypeMappingOperationImpl.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link DirectMappingType} {@link SystemUserLogLogDateMapper}. */
	@Autowired
	private SystemUserLogLogDateMapper systemUserLogLogDateMapper;
	/** The {@link DirectMappingType} {@link SystemUserLogServiceNameMapper}. */
	@Autowired
	private SystemUserLogServiceNameMapper systemUserLogServiceNameMapper;
	/**
	 * The {@link DirectMappingType} {@link SystemUserLogOperationNameMapper}.
	 */
	@Autowired
	private SystemUserLogOperationNameMapper systemUserLogOperationNameMapper;
	/** The {@link DirectMappingType} {@link SystemUserLogContextMapper}. */
	@Autowired
	private SystemUserLogContextMapper systemUserLogContextMapper;
	/** The {@link DirectMappingType} {@link SystemUserLogVersionMapper}. */
	@Autowired
	private SystemUserLogVersionMapper systemUserLogVersionMapper;
	/** The {@link DirectMappingType} {@link SystemUserLogUserNameMapper}. */
	@Autowired
	private SystemUserLogUserNameMapper systemUserLogUserNameMapper;
	/** The {@link TechSupportSystemUserLogRequestsGrantedMapper}. */
	@Autowired
	private TechSupportSystemUserLogRequestsGrantedMapper techSupportSystemUserLogRequestsGrantedMapper;
	/** The {@link TechSupportSystemUserLogRequestsDeniedMapper}. */
	@Autowired
	private TechSupportSystemUserLogRequestsDeniedMapper techSupportSystemUserLogRequestsDeniedMapper;
	/** The {@link TechSupportSystemUserLogResponseFaultsMapper}. */
	@Autowired
	private TechSupportSystemUserLogResponseFaultsMapper techSupportSystemUserLogResponseFaultsMapper;

	/**
	 * Maps the {@link SystemUserReportTypeMapResponseType} out of the
	 * {@link SystemUserReportTypeMapRequestType}.
	 *
	 * @see com.samples.platform.inf.iss.tech.support.operation.SystemUserReportTypeMappingOperation#invokeMapping(com.samples.platform.inf.iss.tech.support.svc.SystemUserReportTypeMapRequestType)
	 */
	@Override
	public SystemUserReportTypeMapResponseType invokeMapping(
			final SystemUserReportTypeMapRequestType request) {
		this.logger.debug("+invokeMapping");
		SystemUserReportTypeMapResponseType response = this.of
				.createSystemUserReportTypeMapResponseType();

		TechnicalSupportSystemUserReportType systemUserReport;
		for (ExtSystemUserLogType ext : request.getSystemUserLog()) {
			systemUserReport = new TechnicalSupportSystemUserReportType();
			response.getSystemUserReport().add(systemUserReport);

			/* Set date into systemUserReport. */
			SystemUserLogLogDateMappingType systemUserReport_date = this.systemUserLogLogDateMapper
					.createMappingType(ext);
			systemUserReport.setDate(systemUserReport_date);

			/* Set service into systemUserReport. */
			SystemUserLogServiceNameMappingType systemUserReport_service = this.systemUserLogServiceNameMapper
					.createMappingType(ext);
			systemUserReport.setService(systemUserReport_service);

			/* Set operation into systemUserReport. */
			SystemUserLogOperationNameMappingType systemUserReport_operation = this.systemUserLogOperationNameMapper
					.createMappingType(ext);
			systemUserReport.setOperation(systemUserReport_operation);

			/* Set serviceProvider into systemUserReport. */
			SystemUserLogContextMappingType systemUserReport_serviceProvider = this.systemUserLogContextMapper
					.createMappingType(ext);
			systemUserReport
					.setServiceProvider(systemUserReport_serviceProvider);

			/* Set version into systemUserReport. */
			SystemUserLogVersionMappingType systemUserReport_version = this.systemUserLogVersionMapper
					.createMappingType(ext);
			systemUserReport.setVersion(systemUserReport_version);

			/* Set consumerSystemUser into systemUserReport. */
			SystemUserLogUserNameMappingType systemUserReport_consumerSystemUser = this.systemUserLogUserNameMapper
					.createMappingType(ext);
			systemUserReport
					.setConsumerSystemUser(systemUserReport_consumerSystemUser);

			/* Set requests into systemUserReport. */
			TechSupportSystemUserLogRequestsGrantedMappingType systemUserReport_requests = this.techSupportSystemUserLogRequestsGrantedMapper
					.createMappingType(ext);
			systemUserReport.setRequests(systemUserReport_requests);

			/* Set requestsDenied into systemUserReport. */
			TechSupportSystemUserLogRequestsDeniedMappingType systemUserReport_requestsDenied = this.techSupportSystemUserLogRequestsDeniedMapper
					.createMappingType(ext);
			systemUserReport.setRequestsDenied(systemUserReport_requestsDenied);

			/* Set errors into systemUserReport. */
			TechSupportSystemUserLogResponseFaultsMappingType systemUserReport_errors = this.techSupportSystemUserLogResponseFaultsMapper
					.createMappingType(ext);
			systemUserReport.setErrors(systemUserReport_errors);
		}
		this.logger.debug("-invokeMapping {}",
				response.getSystemUserReport().size());
		return response;
	}
}
