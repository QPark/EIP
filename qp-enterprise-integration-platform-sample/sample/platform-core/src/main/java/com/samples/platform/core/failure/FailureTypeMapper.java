/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.samples.platform.core.failure;

import com.qpark.eip.core.failure.FailureDescription;
import com.qpark.eip.core.failure.domain.FailureMessageSeverity;
import com.qpark.eip.service.common.msg.ErrorCode;
import com.qpark.eip.service.common.msg.FailureType;
import com.qpark.eip.service.common.msg.Severity;

/**
 * Maps a {@link FailureDescription} (description of a failure) into a
 * {@link FailureType} (part of a message to send).
 * @author bhausen
 */
public class FailureTypeMapper {
	/**
	 * @param errorCode The string error code of the {@link FailureDescription}.
	 * @return The {@link ErrorCode}, defaulting to
	 *         {@link ErrorCode#E_ALL_NOT_KNOWN_ERROR}.
	 */
	private static ErrorCode getErrorCode(final String errorCode) {
		ErrorCode ec = ErrorCode.E_ALL_NOT_KNOWN_ERROR;
		try {
			ErrorCode.fromValue(errorCode);
		} catch (IllegalArgumentException e) {
			ec = ErrorCode.E_ALL_NOT_KNOWN_ERROR;
		}
		return ec;
	}

	/**
	 * @param fd The {@link FailureDescription}.
	 * @return The {@link FailureType}.
	 */
	public static FailureType getFailureType(final FailureDescription fd) {
		if (fd != null) {
			FailureType ft = new FailureType();
			ft.setCode(getErrorCode(fd.getErrorCode()));
			ft.setErrorDetails(fd.getSupportInformation());
			ft.setUserMessage(fd.getUserMessage());
			ft.setSeverity(getSeverity(fd.getSeverity()));
			return ft;
		} else {
			return null;
		}
	}

	/**
	 * @param severity The {@link FailureMessageSeverity} of the
	 *            {@link FailureDescription}.
	 * @return The {@link Severity}.
	 */
	private static Severity getSeverity(final FailureMessageSeverity severity) {
		if (severity.equals(FailureMessageSeverity.ERROR)) {
			return Severity.ERROR;
		} else if (severity.equals(FailureMessageSeverity.INFORMATION)) {
			return Severity.WARNING;
		} else if (severity.equals(FailureMessageSeverity.WARNING)) {
			return Severity.WARNING;
		} else {
			return Severity.WARNING;
		}
	}
}
