/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring;

import org.springframework.integration.ws.MarshallingWebServiceInboundGateway;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ws.soap.security.xwss.XwsSecurityValidationException;
import org.springframework.ws.soap.server.endpoint.AbstractSoapFaultDefinitionExceptionResolver;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;

import com.qpark.eip.core.failure.BaseFailureHandler;
import com.qpark.eip.core.failure.FailureDescription;

/**
 * @author bhausen
 */
public class EipSoapFaultDefinitionExceptionResolver extends AbstractSoapFaultDefinitionExceptionResolver {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(EipSoapFaultDefinitionExceptionResolver.class);

	private SoapFaultDefinition getSoapFaultDefinition(final Throwable t) {
		SoapFaultDefinition sfd = new SoapFaultDefinition();
		if (XwsSecurityValidationException.class.isInstance(t)) {
			sfd = this.getSoapFaultDefinition(t, false, "AUTHENTICATION_USERNAME_PASSWORD_TOKEN_FAILED",
					(Object[]) null);
		} else if (AccessDeniedException.class.isInstance(t)) {
			sfd = this.getSoapFaultDefinition(t, false, "AUTHORISATION_ACCESS_DENIED", (Object[]) null);
		} else if (UnmarshallingFailureException.class.isInstance(t)) {
			sfd = this.getSoapFaultDefinition(t, true, "E_SOAP_FAULT_MARSHALLING_ERROR", (Object[]) null);
		} else if (ValidationFailureException.class.isInstance(t)) {
			sfd = this.getSoapFaultDefinition(t, true, "E_SOAP_MESSAGE_VALIDATION_ERROR", (Object[]) null);
		} else {
			final FailureDescription fd = BaseFailureHandler.handleException(t, null, this.logger, (Object[]) null);
			System.out.println(fd);
			sfd.setFaultCode(SoapFaultDefinition.SERVER);
			sfd.setFaultStringOrReason(fd.toString(true));
		}
		return sfd;
	}

	private SoapFaultDefinition getSoapFaultDefinition(final Throwable t, final boolean addErrorDetails,
			final String code, final Object... data) {
		final FailureDescription fd = BaseFailureHandler.getFailure(code, t, data);
		System.out.println(fd);
		final SoapFaultDefinition sfd = new SoapFaultDefinition();
		sfd.setFaultCode(SoapFaultDefinition.SERVER);
		if (addErrorDetails) {
			sfd.setFaultStringOrReason(new StringBuffer(256).append(fd.getUserMessage()).append("\nError details:\n")
					.append(fd.toString(true, true)).toString());
		} else {
			sfd.setFaultStringOrReason(fd.getUserMessage());
		}
		return sfd;
	}

	/**
	 * @see org.springframework.ws.soap.server.endpoint.AbstractSoapFaultDefinitionExceptionResolver#getFaultDefinition(java.lang.Object,
	 *      java.lang.Exception)
	 */
	@Override
	protected SoapFaultDefinition getFaultDefinition(final Object endpoint, final Exception ex) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String msg = String.format("%s: %s", ex.getClass().getName(), ex.getMessage());
		if (MarshallingWebServiceInboundGateway.class.isInstance(endpoint)) {
			msg = String.format("%s: MarshallingWebServiceInboundGateway[%s/%s]", msg,
					((MarshallingWebServiceInboundGateway) endpoint).getComponentName(),
					((MarshallingWebServiceInboundGateway) endpoint).getComponentType());
		} else {
			msg = String.format("%s: %s", msg, endpoint);
		}
		this.logger.error(msg, ex);
		final SoapFaultDefinition sfd = this.getSoapFaultDefinition(ex);
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		return sfd;
	}
}
