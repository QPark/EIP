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
package com.qpark.eip.core.failure;

import com.qpark.eip.core.failure.domain.FailureMessageSeverity;

/**
 * @author bhausen
 */
public class FailureDescription {
	private String errorCode;
	private FailureMessageSeverity severity;
	private String supportInformation = "";
	private String whatHappened;
	private String whatToDo;
	private String whyHappened;
	private String errorDetails;

	/**
	 * @return the errorDetails
	 */
	public String getErrorDetails() {
		return this.errorDetails;
	}

	/**
	 * @param errorDetails the errorDetails to set
	 */
	public void setErrorDetails(final String errorDetails) {
		this.errorDetails = errorDetails;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * @return the severity.
	 */
	public FailureMessageSeverity getSeverity() {
		return this.severity;
	}

	/**
	 * @return the supportInformation.
	 */
	public String getSupportInformation() {
		return this.supportInformation;
	}

	public String getUserMessage() {
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.whatHappened);
		if (this.whyHappened != null && this.whyHappened.length() > 0) {
			sb.append("\n");
			sb.append(this.whyHappened);
		}
		if (this.whatToDo != null && this.whatToDo.length() > 0) {
			sb.append("\n");
			sb.append(this.whatToDo);
		}
		return sb.toString();
	}

	/**
	 * @return the whatHappened.
	 */
	public String getWhatHappened() {
		return this.whatHappened;
	}

	/**
	 * @return the whatToDo.
	 */
	public String getWhatToDo() {
		return this.whatToDo;
	}

	/**
	 * @return the whyHappened.
	 */
	public String getWhyHappened() {
		return this.whyHappened;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @param severity the severity to set.
	 */
	public void setSeverity(final FailureMessageSeverity severity) {
		this.severity = severity;
	}

	/**
	 * @param supportInformation the supportInformation to set.
	 */
	public void setSupportInformation(final String supportInformation) {
		this.supportInformation = supportInformation;
	}

	/**
	 * @param whatHappened the whatHappened to set.
	 */
	public void setWhatHappened(final String whatHappened) {
		this.whatHappened = whatHappened;
	}

	/**
	 * @param whatToDo the whatToDo to set.
	 */
	public void setWhatToDo(final String whatToDo) {
		this.whatToDo = whatToDo;
	}

	/**
	 * @param whyHappened the whyHappened to set.
	 */
	public void setWhyHappened(final String whyHappened) {
		this.whyHappened = whyHappened;
	}

	public String toString(final boolean appendStack) {
		return this.toString(appendStack, false);
	}

	public String toString(final boolean appendStack, final boolean prettyPrint) {
		StringBuffer sb = new StringBuffer(256);
		sb.append("<FailureDescription ");
		if (this.errorCode != null) {
			sb.append("errorCode=\"").append(this.errorCode).append("\" ");
		}
		if (this.severity != null) {
			sb.append("severity=\"").append(this.severity.name()).append("\" ");
		}
		sb.append(">");
		if (prettyPrint) {
			sb.append("\n\t");
		}
		sb.append("<userMessage><![CDATA[");
		sb.append(this.getUserMessage().replaceAll("\\n", " "));
		sb.append("]]></userMessage>");
		if (appendStack) {
			if (prettyPrint) {
				sb.append("\n\t");
			}
			sb.append("<errorDetails><![CDATA[");
			if (prettyPrint) {
				sb.append(this.getErrorDetails());
			} else {
				sb.append(this.getErrorDetails().replaceAll("\\n", " "));
			}
			sb.append("]]></errorDetails>");
		}
		if (prettyPrint) {
			sb.append("\n");
		}
		sb.append("</FailureDescription>");
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.toString(false);
	}
}