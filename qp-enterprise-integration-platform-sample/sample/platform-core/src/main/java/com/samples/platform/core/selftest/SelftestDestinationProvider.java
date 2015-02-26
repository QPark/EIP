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
package com.samples.platform.core.selftest;

import java.net.URI;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.ws.client.support.destination.DestinationProvider;

/**
 * @author bhausen
 */
public class SelftestDestinationProvider implements ServletContextAware,
		DestinationProvider {
	/**
	 * The default destination - to be updated by
	 * {@link #setServletContext(ServletContext)}.
	 */
	private String destination = "http://localhost:8080/";

	/** The service name out of the web.xml. */
	private String serviceName = "common";
	/** The service version out of the web.xml. */
	private String serviceVersion = "1.x";

	/** The {@link ServletContext}. */
	private ServletContext servletContext;

	/**
	 * @see org.springframework.ws.client.support.destination.DestinationProvider#getDestination()
	 */
	@Override
	public URI getDestination() {
		return URI.create(this.destination);
	}

	/**
	 * @return the service name out of the web.xml. .
	 */
	public String getServiceName() {
		return this.serviceName;
	}

	/**
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;

		try {
			InitialContext ctx = new InitialContext();
			this.serviceName = (String) ctx
					.lookup("java:comp/env/servicebus-service-name");
			this.serviceVersion = (String) ctx
					.lookup("java:comp/env/serviceplatform.version-name");
		} catch (NamingException e) {
			// Nothing to do here.		
		}
		this.destination = new StringBuffer(256).append("http://localhost:")
				.append(System.getProperty("bio.http.port"))
				.append("/servicebus-").append(this.serviceName).append("/")
				.append(this.serviceVersion).append("/services/")
				.append(this.serviceName).toString();
	}
}
