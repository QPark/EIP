package com.samples.platform.inf.iss.tech.support.operation.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qpark.eip.inf.icd.map.DirectMappingType;
import com.samples.platform.inf.iss.tech.support.map.SystemUserReportCriteriaDateMappingType;
import com.samples.platform.inf.iss.tech.support.mapper.direct.SystemUserReportCriteriaDateMapper;
import com.samples.platform.inf.iss.tech.support.operation.SystemUserLogCriteriaTypeMappingOperation;
import com.samples.platform.inf.iss.tech.support.svc.ObjectFactory;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserLogCriteriaTypeMapRequestType;
import com.samples.platform.inf.iss.tech.support.svc.SystemUserLogCriteriaTypeMapResponseType;
import com.samples.platform.inf.iss.tech.support.type.TechnicalSupportSystemUserLogCriteriaType;

/**
 * The {@link SystemUserLogCriteriaTypeMappingOperation} implementation.
 *
 * @author bhausen
 */
@Component
public class SystemUserLogCriteriaTypeMappingOperationImpl
		implements SystemUserLogCriteriaTypeMappingOperation {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SystemUserLogCriteriaTypeMappingOperationImpl.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/**
	 * The {@link DirectMappingType} {@link SystemUserReportCriteriaDateMapper}.
	 */
	@Autowired
	private SystemUserReportCriteriaDateMapper systemUserReportCriteriaDateMapper;

	/**
	 * Maps the {@link SystemUserLogCriteriaTypeMapResponseType} out of the
	 * {@link SystemUserLogCriteriaTypeMapRequestType}.
	 *
	 * @see com.samples.platform.inf.iss.tech.support.operation.SystemUserLogCriteriaTypeMappingOperation#invokeMapping(com.samples.platform.inf.iss.tech.support.svc.SystemUserLogCriteriaTypeMapRequestType)
	 */
	@Override
	public SystemUserLogCriteriaTypeMapResponseType invokeMapping(
			final SystemUserLogCriteriaTypeMapRequestType request) {
		this.logger.debug("+invokeMapping");
		SystemUserLogCriteriaTypeMapResponseType response = this.of
				.createSystemUserLogCriteriaTypeMapResponseType();

		/* Values to set in the response. */
		TechnicalSupportSystemUserLogCriteriaType response_criteria = new com.samples.platform.inf.iss.tech.support.type.ObjectFactory()
				.createTechnicalSupportSystemUserLogCriteriaType();
		response.setCriteria(response_criteria);
		/* Set date into criteria. */
		SystemUserReportCriteriaDateMappingType response_criteria_date = this.systemUserReportCriteriaDateMapper
				.createMappingType(request.getCriteria());
		response_criteria.setDate(response_criteria_date);

		this.logger.debug("-invokeMapping");
		return response;
	}
}
