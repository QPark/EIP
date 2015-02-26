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

/**
 * @author bhausen
 */
public class DefaultEipSecurmentPropertyProvider implements
		SecurmentPropertyProvider {
	private String securementPassword;

	private String securementUsername;

	/**
	 * @return <code>UsernameToken</code>
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementActions()
	 */
	@Override
	public String getSecurementActions() {
		return "UsernameToken";
	}

	/**
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementPassword()
	 */
	@Override
	public String getSecurementPassword() {
		return this.securementPassword;
	}

	/**
	 * @return <code>PasswordDigest</code>
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementPasswordType()
	 */
	@Override
	public String getSecurementPasswordType() {
		return "PasswordDigest";
	}

	/**
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementUsername()
	 */
	@Override
	public String getSecurementUsername() {
		return this.securementUsername;
	}

	/**
	 * @return <code>Nonce Created</code>
	 * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementUsernameTokenElements()
	 */
	@Override
	public String getSecurementUsernameTokenElements() {
		return "Nonce Created";
	}

	/**
	 * @param securementPassword the securementPassword to set
	 */
	public void setSecurementPassword(final String securementPassword) {
		this.securementPassword = securementPassword;
	}

	/**
	 * @param securementUsername the securementUsername to set
	 */
	public void setSecurementUsername(final String securementUsername) {
		this.securementUsername = securementUsername;
	}
}
