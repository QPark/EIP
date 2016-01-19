/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.failure;

import com.qpark.eip.core.failure.FailureDescription;
import com.qpark.eip.service.base.msg.FailureType;

/**
 * @author bhausen
 */
public class FailureException extends RuntimeException {
	private static final long serialVersionUID = -3203342548649250542L;
	/** The {@link FailureType}. */
	private FailureType ft;

	/** The {@link FailureDescription}. */
	private FailureDescription fd;

	public FailureException() {
		super();
	}

	public FailureException(final FailureDescription fd) {
		super(fd.getUserMessage());
		this.fd = fd;
	}

	public FailureException(final FailureType ft) {
		super(ft.getUserMessage());
		this.ft = ft;
	}

	public FailureException(final String msg) {
		super(msg);
	}

	public FailureException(final Throwable cause) {
		super(cause);
	}

	public FailureException(final FailureType ft, final Throwable cause) {
		super(ft.getUserMessage(), cause);
	}

	/**
	 * @return the {@link FailureType}.
	 */
	public FailureType getFailure() {
		if (this.fd != null && this.ft == null) {
			this.ft = FailureHandler.getFailureType(this.fd);
		}
		return this.ft;
	}

	/**
	 * @param failure
	 *            the failure to set.
	 */
	public void setFailure(final FailureType failure) {
		this.ft = failure;
	}
}
