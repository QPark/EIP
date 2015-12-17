/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.security.SecurmentPropertyProvider;

/**
 * @author bhausen
 */
public class ExternalSystemXWebServiceSecurmentPropertyProvider implements
		SecurmentPropertyProvider {
	/** The {@link ApplicationPlaceholderConfigurer}. */
	@Autowired
	private ApplicationPlaceholderConfigurer properties;

	/**
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementActions()
	 */
	@Override
	public String getSecurementActions() {
		return "UsernameToken";
	}

	/**
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementUsername()
	 */
	@Override
	public String getSecurementUsername() {
		return this.properties.getProperty("user.call.server.userName",
				"bus");
	}

	/**
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementPassword()
	 */
	@Override
	public String getSecurementPassword() {
		return this.properties.getProperty("user.call.server.password",
				"password");
	}

	/**
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementPasswordType()
	 */
	@Override
	public String getSecurementPasswordType() {
		return "PasswordDigest";
	}

	/**
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementUsernameTokenElements()
	 */
	@Override
	public String getSecurementUsernameTokenElements() {
		return "Nonce Created";
	}
}
