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
package com.qpark.eip.core.spring.security;

import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

/**
 * A {@link Wss4jSecurityInterceptor} setup by a {@link SecurmentPropertyProvider}.
 * @author bhausen
 */
public class EipWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {
	/** The {@link SecurmentPropertyProvider}. */
	private SecurmentPropertyProvider securementPropertyProvider;

	/**
	 * @param securementPropertyProvider the securementPropertyProvider to set
	 */
	public void setSecurementPropertyProvider(
			final SecurmentPropertyProvider securementPropertyProvider) {
		this.securementPropertyProvider = securementPropertyProvider;
		if (this.securementPropertyProvider != null) {
			this.setSecurementActions(this.securementPropertyProvider
					.getSecurementActions());
			this.setSecurementUsername(this.securementPropertyProvider
					.getSecurementUsername());
			this.setSecurementPassword(this.securementPropertyProvider
					.getSecurementPassword());
			this.setSecurementPasswordType(this.securementPropertyProvider
					.getSecurementPasswordType());
			this.setSecurementUsernameTokenElements(this.securementPropertyProvider
					.getSecurementUsernameTokenElements());
		}
	}
}
