/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.flow;

import java.util.UUID;

import com.qpark.eip.inf.FlowContext;

/**
 * The implementation of the {@link FlowContext}.
 *
 * @author bhausen
 */
public class FlowContextImpl implements FlowContext {
	/** The operation name of the flow requester. */
	private String requesterOperationName;
	/** The service name of the flow requester. */
	private String requesterServiceName;
	/** The service version of the flow requester. */
	private String requesterServiceVersion;
	/** The user name of the flow requester. */
	private String requesterUserName;
	/** The session id. */
	private final String sessionId = UUID.randomUUID().toString();

	/**
	 * @see com.qpark.eip.inf.FlowContext#getRequesterOperationName()
	 */
	@Override
	public String getRequesterOperationName() {
		return this.requesterOperationName;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#getRequesterServiceName()
	 */
	@Override
	public String getRequesterServiceName() {
		return this.requesterServiceName;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#getRequesterServiceVersion()
	 */
	@Override
	public String getRequesterServiceVersion() {
		return this.requesterServiceVersion;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#getRequesterUserName()
	 */
	@Override
	public String getRequesterUserName() {
		return this.requesterUserName;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#getSessionId()
	 */
	@Override
	public String getSessionId() {
		return this.sessionId;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#setRequesterOperationName(java.lang.String)
	 */
	@Override
	public void setRequesterOperationName(final String requesterOperationName) {
		this.requesterOperationName = requesterOperationName;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#setRequesterServiceName(java.lang.String)
	 */
	@Override
	public void setRequesterServiceName(final String requesterServiceName) {
		this.requesterServiceName = requesterServiceName;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#setRequesterServiceVersion(java.lang.String)
	 */
	@Override
	public void setRequesterServiceVersion(
			final String requesterServiceVersion) {
		this.requesterServiceVersion = requesterServiceVersion;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#setRequesterUserName(java.lang.String)
	 */
	@Override
	public void setRequesterUserName(final String requesterUserName) {
		this.requesterUserName = requesterUserName;
	}

	/**
	 * @see com.qpark.eip.inf.FlowContext#setSessionId(java.lang.String)
	 */
	@Override
	public void setSessionId(final String sessionId) {
		throw new UnsupportedOperationException(
				"SessionId is set automatically.");
	}
}
