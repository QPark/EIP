/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ws.MarshallingWebServiceOutboundGateway;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapVersion;

/**
 * {@link WebServiceMessageCallback} setting the HTTP request header
 * <i>Content-Type</i> part <i>action</i> to a not empty value (
 * <i>http://tempuri.org/Action</i>) when doing SOAP 1.2 requests.
 * <p/>
 * If {@link EipSoapActionWebServiceMessageCallback} is defined as a spring bean
 * it hooks itself to each and every
 * {@link MarshallingWebServiceOutboundGateway} deployed in the application
 * context.
 *
 * @author bhausen
 */
public class EipSoapActionWebServiceMessageCallback
		implements WebServiceMessageCallback {
	/**
	 * The list of all {@link MarshallingWebServiceOutboundGateway}s in the
	 * context (if any).
	 */
	@Autowired(required = false)
	private List<MarshallingWebServiceOutboundGateway> gateways;

	/**
	 * Add <code>this</code> to all {@link MarshallingWebServiceOutboundGateway}
	 * s in the context.
	 */
	@PostConstruct
	private void setRequestCallbacks() {
		if (this.gateways != null) {
			for (MarshallingWebServiceOutboundGateway gateway : this.gateways) {
				gateway.setRequestCallback(this);
			}
		}
	}

	/**
	 * @see org.springframework.ws.client.core.WebServiceMessageCallback#doWithMessage(org.springframework.ws.WebServiceMessage)
	 */
	@Override
	public void doWithMessage(final WebServiceMessage message)
			throws IOException, TransformerException {
		if (SoapMessage.class.isInstance(message)) {
			SoapVersion version = ((SoapMessage) message).getVersion();
			if (version != null && SoapVersion.SOAP_12.equals(version)) {
				((SoapMessage) message)
						.setSoapAction("http://tempuri.org/Action");
			}
		}
	}
}