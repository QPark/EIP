/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
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
public class MockFlowGateway implements SystemUserLogFlowGateway {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(MockFlowGateway.class);
	/** The {@link SystemUserLogRequestType}. */
	private SystemUserLogRequestType request;
	/** The {@link SystemUserLogResponseType}. */
	private SystemUserLogResponseType response;
	/** The {@link RuntimeException} to throw. */
	private RuntimeException runtimeException;

	/**
	 * Get the {@link SystemUserLogRequestType}.
	 *
	 * @return the {@link SystemUserLogRequestType}.
	 */
	SystemUserLogRequestType getRequest() {
		return this.request;
	}

	/**
	 * Get the {@link SystemUserLogResponseType}.
	 *
	 * @return the {@link SystemUserLogResponseType}.
	 */
	SystemUserLogResponseType getResponse() {
		return this.response;
	}

	/**
	 * Get the {@link RuntimeException} to throw.
	 *
	 * @return the {@link RuntimeException} to throw.
	 */
	RuntimeException getRuntimeException() {
		return this.runtimeException;
	}

	/**
	 * @see com.samples.platform.core.flow.SystemUserLogFlowGateway#getSystemUserLog(com.samples.platform.inf.iss.tech.support.msg.SystemUserLogRequestType)
	 */
	@Override
	public SystemUserLogResponseType getSystemUserLog(
			final SystemUserLogRequestType request) {
		this.logger.debug("+getSystemUserLog");
		this.request = request;
		if (this.runtimeException != null) {
			this.logger.error(" getSystemUserLog {}",
					this.runtimeException.getMessage());
			throw this.runtimeException;
		}
		this.logger.debug("-getSystemUserLog");
		return this.response;
	}

	/**
	 * Set the {@link SystemUserLogRequestType}.
	 *
	 * @param request
	 *            the {@link SystemUserLogRequestType}.
	 */
	void setRequest(final SystemUserLogRequestType request) {
		this.request = request;
	}

	/**
	 * Set the {@link SystemUserLogResponseType}.
	 *
	 * @param response
	 *            the {@link SystemUserLogResponseType}.
	 */
	void setResponse(final SystemUserLogResponseType response) {
		this.response = response;
	}

	/**
	 * Set the {@link RuntimeException} to throw.
	 *
	 * @param runtimeException
	 *            the {@link RuntimeException} to throw.
	 */
	void setRuntimeException(final RuntimeException runtimeException) {
		this.runtimeException = runtimeException;
	}

}
