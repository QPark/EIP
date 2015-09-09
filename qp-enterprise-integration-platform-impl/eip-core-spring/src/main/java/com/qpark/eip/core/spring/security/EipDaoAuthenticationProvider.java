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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class EipDaoAuthenticationProvider extends DaoAuthenticationProvider {
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(EipDaoAuthenticationProvider.class);

	/**
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(final Authentication authentication)
			throws AuthenticationException {
		this.logger.debug("+authenticate {}", authentication.getName());
		Authentication auth = null;
		try {
			auth = super.authenticate(authentication);
		} catch (AuthenticationException e) {
			this.logger.warn(" authenticate {} failed: {}",
					authentication.getName(), e.getMessage());
			throw e;
		} finally {
			this.logger.debug("-authenticate {}", authentication.getName());
		}
		return auth;
	}

	/**
	 * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails,
	 *      org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(
			final UserDetails userDetails,
			final UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		this.logger.debug("+additionalAuthenticationChecks {}",
				authentication.getName());
		try {
			super.additionalAuthenticationChecks(userDetails, authentication);
		} catch (AuthenticationException e) {
			this.logger.warn(
					" additionalAuthenticationChecks {}/{} failed: {}",
					authentication.getName(),
					(userDetails == null ? "NoUserDetailsFound" : userDetails
							.getUsername()), e.getMessage());
			throw e;
		} finally {
			this.logger.debug("-additionalAuthenticationChecks {}",
					authentication.getName());
		}
	}
}
