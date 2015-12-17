/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.failure;

import org.hsqldb.error.ErrorCode;

import com.qpark.eip.core.failure.BaseFailureHandler;
import com.qpark.eip.core.failure.FailureDescription;
import com.qpark.eip.service.base.msg.FailureType;

/**
 * @author bhausen
 */
public class FailureHandler extends BaseFailureHandler {
	/** The default {@link ErrorCode} if non presented. */
	public static final String DEFAULT = "E_NOT_KNOWN_ERROR";
	/** The default database {@link ErrorCode} if non presented. */
	public static final String DEFAULT_DATABASE = "E_DATABASE_ERROR";

	/**
	 * @param fd
	 *            The {@link FailureDescription}.
	 * @return The {@link FailureType}.
	 */
	public static FailureType getFailureType(final FailureDescription fd) {
		if (fd != null) {
			FailureType ft = new FailureType();
			ft.setCode(fd.getErrorCode());
			ft.setErrorDetails(fd.getSupportInformation());
			ft.setUserMessage(fd.getUserMessage());
			ft.setSeverity(fd.getSeverity().name());
			return ft;
		} else {
			return null;
		}
	}

	/**
	 * @return the unknown {@link FailureType}.
	 */
	public static FailureType getUnknownFailure() {
		return getFailureTypeDefault((Object[]) null);
	}

	/**
	 * @param data
	 * @return the default {@link FailureType}.
	 */
	private static FailureType getFailureTypeDefault(final Object... data) {
		FailureDescription fd = getFailure(null, null, DEFAULT, data);
		return getFailureType(fd);
	}

	/**
	 * @param data
	 * @return the unknow {@link FailureType}.
	 */
	public static FailureType getUnknownFailure(final Object... data) {
		return getFailureTypeDefault(data);
	}

	/**
	 * @param code
	 * @return the {@link FailureType}.
	 */
	public static FailureType getFailureType(final String code) {
		return getFailureType(code, (Throwable) null, (Object[]) null);
	}

	/**
	 * @param code
	 * @param data
	 * @return the {@link FailureType}.
	 */
	public static FailureType getFailureType(final String code,
			final Object... data) {
		return getFailureType(code, (Throwable) null, data);
	}

	/**
	 * @param code
	 * @param data
	 * @return the {@link FailureType}.
	 */
	public static FailureType getFailureType(final String code,
			final Throwable t) {
		return getFailureType(code, t, (Object[]) null);
	}

	/**
	 * @param o
	 * @param ft
	 * @param data
	 * @return the {@link FailureType}.
	 */
	public static FailureType getFailureType(final String code,
			final Throwable t, final Object... data) {
		FailureDescription fd = getFailure(code, t, data);
		return getFailureType(fd);
	}
}
