/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.client.config.util;

import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

/**
 * The {@link Wss4jSecurityInterceptor}.
 * 
 * @author bhausen
 */
public class ClientCallWss4jSecurityInterceptor
		extends Wss4jSecurityInterceptor {
	/**
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		this.setSecurementActions("UsernameToken");
		this.setSecurementPasswordType("PasswordDigest");
		this.setSecurementUsernameTokenElements("Nonce Created");
	}
}
