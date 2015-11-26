package com.samples.platform.serviceprovider.techsupport.flow.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.samples.platform.core.flow.SystemUserLogFlowGateway;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogRequestType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogResponseType;

/**
 * This implementation of the {@link SystemUserLogFlowGateway} is only valid in
 * single threaded environments!
 *
 * @author bhausen
 */
public class SystemUserLogFlowGatewayImpl implements SystemUserLogFlowGateway {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SystemUserLogFlowGatewayImpl.class);
	/** The {@link SystemUserLogRequestType}. */
	private SystemUserLogRequestType request;

	/**
	 * Get the {@link SystemUserLogRequestType}.
	 *
	 * @return the {@link SystemUserLogRequestType}.
	 */
	public SystemUserLogRequestType getRequest() {
		return this.request;
	}

	/**
	 * Set the {@link SystemUserLogRequestType}.
	 *
	 * @param request
	 *            the {@link SystemUserLogRequestType}.
	 */
	public void setRequest(final SystemUserLogRequestType request) {
		this.request = request;
	}

	/**
	 * @see com.samples.platform.core.flow.SystemUserLogFlowGateway#getSystemUserLog(com.samples.platform.inf.iss.tech.support.msg.SystemUserLogRequestType)
	 */
	@Override
	public SystemUserLogResponseType getSystemUserLog(
			final SystemUserLogRequestType request) {
		this.logger.debug("+getSystemUserLog");
		this.request = request;
		SystemUserLogResponseType response = new SystemUserLogResponseType();
		this.logger.debug("-getSystemUserLog");
		return response;
	}

}
