package com.samples.platform.client.config.util;

import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

/**
 * A {@link Wss4jSecurityInterceptor} setup by a
 * {@link SecurmentPropertyProvider}.
 * @author bhausen
 */
public class ClientCallWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {
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
