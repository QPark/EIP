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

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;

/**
 * @author bhausen
 */
public class EipAffirmativeBased extends AffirmativeBased {
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(EipAffirmativeBased.class);

	/** Constructor. */
	public EipAffirmativeBased() {
		super();
	}

	/**
	 * Constructor.
	 * @param decisionVoters list of {@link AccessDecisionVoter}s.
	 */
	public EipAffirmativeBased(final List<AccessDecisionVoter> decisionVoters) {
		super(decisionVoters);
	}

	/**
	 * @see org.springframework.security.access.vote.AffirmativeBased#decide(org.springframework.security.core.Authentication,
	 *      java.lang.Object, java.util.Collection)
	 */
	@Override
	public void decide(final Authentication authentication,
			final Object object,
			final Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		String channelName = EipRoleVoter.getChannelName(object);
		this.logger.debug("+decide {} {}", channelName,
				authentication.getName());
		try {
			super.decide(authentication, object, configAttributes);
		} catch (AccessDeniedException e) {
			for (AccessDecisionVoter<?> voter : this.getDecisionVoters()) {
				if (EipRoleVoter.class.isInstance(voter)) {
					this.logger.warn(
							" decide - vote failed {} {}: required  [{}]",
							channelName, authentication.getName(),
							((EipRoleVoter) voter)
									.getRequiredRoles(configAttributes));
					this.logger.warn(
							" decide - vote failed {} {}: userroles [{}]",
							channelName, authentication.getName(),
							((EipRoleVoter) voter)
									.getGrantedRoles(authentication));
					break;
				}
			}
			this.logger.info(" decide {} {}: {}", channelName,
					authentication.getName(), e.getMessage());
			throw e;
		} finally {
			this.logger.debug("-decide {} {}", channelName,
					authentication.getName());
		}
	}
}
