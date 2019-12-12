/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Provides user details from the {@link EipUserProvider} to the
 * {@link UserDetailsService} of spring.
 *
 * @author bhausen
 */
public class EipUserDetailsService extends EipRoleVoter
		implements UserDetailsService {
	/** The name of the anonymous role. */
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

	/**	 */
	public static void clearSecurityContextHolderAuthentication() {
		SecurityContextHolder.clearContext();
	}

	/**
	 * @param userDetailService
	 * @param userName
	 * @return details were not set
	 */
	public static boolean setSecurityContextHolderAuthentication(
			final EipUserProvider userDetailService, final String userName) {
		Optional<Authentication> auth = Optional.ofNullable(
				SecurityContextHolder.getContext().getAuthentication());
		if (!auth.isPresent()) {
			Optional.ofNullable(userName)
					.map(name -> userDetailService.getUser(name))
					.ifPresent(user -> SecurityContextHolder.getContext()
							.setAuthentication(
									new UsernamePasswordAuthenticationToken(
											user, user.getPassword())));
		}
		return !auth.isPresent();
	}

	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(EipUserDetailsService.class);
	/** The {@link EipUserProvider}. */
	private EipUserProvider userProvider;

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException, DataAccessException {
		this.logger.debug("+loadUserByUsername user {}", username);
		Optional<User> user = Optional.ofNullable(this.userProvider)
				.map(up -> up.getUser(username));
		if (!user.isPresent()) {
			throw new UsernameNotFoundException(String
					.format("Application user '%s' is unknown", username));
		}
		this.logger.debug("-loadUserByUsername user {} found!",
				user.get().getUsername());
		return user.get();
	}

	/**
	 * @param userDetailService the userDetailService to set
	 */
	public void setUserProvider(final EipUserProvider userDetailService) {
		this.userProvider = userDetailService;
	}
}
