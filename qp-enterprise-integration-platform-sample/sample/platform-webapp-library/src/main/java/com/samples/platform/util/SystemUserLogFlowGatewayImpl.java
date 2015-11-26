package com.samples.platform.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;
import com.qpark.eip.core.spring.statistics.dao.StatisticsLoggingDao;
import com.samples.platform.core.flow.SystemUserLogFlowGateway;
import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogRequestType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogResponseType;

/**
 * The {@link SystemUserLogFlowGateway} implementation.
 *
 * @author bhausen
 */
public class SystemUserLogFlowGatewayImpl implements SystemUserLogFlowGateway {
	/** The {@link StatisticsLoggingDao}. */
	@Autowired
	private StatisticsLoggingDao dao;
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SystemUserLogFlowGatewayImpl.class);

	/**
	 * @see com.samples.platform.core.flow.SystemUserLogFlowGateway#getSystemUserLog(com.samples.platform.inf.iss.tech.support.msg.SystemUserLogRequestType)
	 */
	@Override
	public SystemUserLogResponseType getSystemUserLog(
			final SystemUserLogRequestType request) {
		this.logger.debug("+getSystemUserLog");
		SystemUserLogResponseType response = new SystemUserLogResponseType();
		List<SystemUserLogType> list = this.dao.getSystemUserLogType(
				DateUtil.get(request.getCriteria().getLogDate()));
		ExtSystemUserLogType log;
		for (SystemUserLogType x : list) {
			log = new ExtSystemUserLogType();
			response.getSystemUserLog().add(log);

			/* Basic mapping of the SystemUserLogType. */
			log.setContext(x.getContext());
			log.setLogDate(x.getLogDate());
			log.setOperationName(x.getOperationName());
			log.setRequestsDenied(x.getRequestsDenied());
			log.setRequestsGranted(x.getRequestsGranted());
			log.setResponseFaults(x.getResponseFaults());
			log.setServiceName(x.getServiceName());
			log.setUserName(x.getUserName());
			log.setVersion(x.getVersion());
		}
		this.logger.debug("-getSystemUserLog {}",
				response.getSystemUserLog().size());
		return response;
	}
}
