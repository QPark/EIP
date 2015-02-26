/**
 * 
 */
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.springframework.dao.DataAccessException;
import org.springframework.integration.security.channel.ChannelInvocation;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author hausen
 */
public class EipUserDetailsService extends RoleVoter implements
		UserDetailsService {
	static class BusUserDefinition {
		private String name;
		private String password = "";
		private final TreeSet<String> rolenames = new TreeSet<String>();

		public void addRolename(final String rolename) {
			if (rolename.startsWith("ROLE_")) {
				this.rolenames.add(rolename);
			}
		}

		/**
		 * @return the name.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * @return the password.
		 */
		public String getPassword() {
			return this.password;
		}

		/**
		 * @return the roles.
		 */
		public List<SimpleGrantedAuthority> getRoles() {
			List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
			for (String rolename : this.rolenames) {
				roles.add(new SimpleGrantedAuthority(rolename));
			}
			return roles;
		}

		/**
		 * @param name the name to set.
		 */
		public void setName(final String name) {
			this.name = name;
		}

		/**
		 * @param password the password to set.
		 */
		public void setPassword(final String password) {
			this.password = password;
		}

		public int size() {
			return this.rolenames.size();
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer(512);
			sb.append("User \"").append(this.name).append("\" with roles ");
			int i = 0;
			for (String rolename : this.rolenames) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(rolename);
				i++;
			}
			return sb.toString();
		}
	}

	/** The name of the anonymous role. */
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

	/**	 */
	public static void clearSecurityContextHolderAuthentication() {
		SecurityContextHolder.clearContext();
	}

	/**
	 * @param userDetailService
	 * @param userName
	 * @return
	 */
	public static boolean setSecurityContextHolderAuthentication(
			final EipUserProvider userDetailService, final String userName) {
		boolean doLogin = SecurityContextHolder.getContext()
				.getAuthentication() == null;
		if (doLogin) {
			User user = userDetailService.getUser(userName);
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(user, null));
		}
		return doLogin;
	}

	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(EipUserDetailsService.class);

	private EipUserProvider userProvider;

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException, DataAccessException {
		this.logger.debug("+loadUserByUsername user {}", username);
		User user = this.userProvider.getUser(username);
		if (user == null) {
			throw new UsernameNotFoundException("Application user '" + username
					+ "' is not known");
		}
		this.logger.debug("-loadUserByUsername user {} found!", username);
		return user;
	}

	/**
	 * @param userDetailService the userDetailService to set
	 */
	public void setUserProvider(final EipUserProvider userDetailService) {
		this.userProvider = userDetailService;
	}

	/**
	 * @see org.springframework.security.access.vote.RoleVoter#vote(org.springframework.security.core.Authentication,
	 *      java.lang.Object, java.util.Collection)
	 */
	@Override
	public int vote(final Authentication authentication, final Object object,
			final Collection<ConfigAttribute> attributes) {
		int result = super.vote(authentication, object, attributes);
		if (this.logger.isDebugEnabled()) {
			String channel = "unknown";
			if (ChannelInvocation.class.isInstance(object)) {
				channel = ((ChannelInvocation) object).getChannel().toString();
			}
			StringBuffer sb = new StringBuffer(1024);
			int roleCount = 0;
			for (ConfigAttribute attribute : attributes) {
				if (this.supports(attribute)) {
					roleCount++;
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(attribute);
				}
			}
			if (!sb.toString().equals(EipUserDetailsService.ROLE_ANONYMOUS)) {
				if (roleCount > 1) {
					this.logger
							.debug("+vote for channel \"{}\": {} (only one fitting role needed).",
									channel, sb.toString());
				} else if (roleCount == 1) {
					this.logger.debug("+vote for channel \"{}\": {}.", channel,
							sb.toString());
				} else {
					this.logger.debug(
							"+vote for channel \"{}\": No role is needed.",
							channel);
				}
				roleCount = 0;
				sb.setLength(0);
				Collection<? extends GrantedAuthority> authorities = authentication
						.getAuthorities();
				for (GrantedAuthority authority : authorities) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(authority.getAuthority());
				}
				if (result == ACCESS_ABSTAIN) {
					this.logger.debug(
							"-vote => abstained, available roles: {}",
							sb.toString());
				} else if (result == ACCESS_DENIED) {
					this.logger.debug("-vote => denied, available roles: {}",
							sb.toString());
				} else if (result == ACCESS_GRANTED) {
					this.logger.debug("-vote => grandted, available roles: {}",
							sb.toString());
				}
			}
		}
		return result;
	}

}
