/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.auth.DatabaseUserProvider;
import com.qpark.eip.core.spring.auth.LimitedAccessDataProvider;
import com.qpark.eip.core.spring.auth.dao.AuthorityDao;

/**
 * Provides the spring config of the eip auth.
 *
 * @author bhausen
 */
@Configuration
@EnableScheduling
@Import({ EipPersistenceConfig.class })
public class EipAuthConfig {
	/** The context name of the eip core authority. */
	private String contextName;

	/** The version of the context. */
	private String contextVersion;

	/**
	 * Create the spring config of the eip core authority.
	 */
	public EipAuthConfig() {
	}

	/**
	 * Get the {@link AuthorityDao} bean.
	 *
	 * @return the {@link AuthorityDao} bean.
	 */
	@Bean
	public AuthorityDao getAuthorityDao() {
		AuthorityDao bean = new AuthorityDao();
		return bean;
	}

	/**
	 * Get the context name.
	 *
	 * @return the context definition.
	 */
	public String getContextDefinition() {
		return String.format("%s:%s", this.contextName, this.contextVersion);
	}

	/**
	 * Get the {@link ContextNameProvider} bean.
	 *
	 * @return the {@link ContextNameProvider} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringAuthContextNameProvider")
	public ContextNameProvider getContextNameProvider() {
		ContextNameProvider bean = new ContextNameProvider();
		bean.setContextName(this.contextName);
		bean.setContextVersion(this.contextVersion);
		return bean;
	}

	/**
	 * Get the {@link DatabaseUserProvider} bean.
	 *
	 * @return the {@link DatabaseUserProvider} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringAuthDatabaseUserProvider")
	public DatabaseUserProvider getDatabaseUserProvider() {
		DatabaseUserProvider bean = new DatabaseUserProvider();
		return bean;
	}

	/**
	 * Get the {@link LimitedAccessDataProvider} bean.
	 *
	 * @return the {@link LimitedAccessDataProvider} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringAuthLimitedAccessDataProvider")
	public LimitedAccessDataProvider getLimitedAccessDataProvider() {
		LimitedAccessDataProvider bean = new LimitedAccessDataProvider();
		return bean;
	}

	/**
	 * Set the context name.
	 *
	 * @param contextName
	 *            the context name.
	 */
	public void setContextName(final String contextName) {
		this.contextName = contextName;
	}

	/**
	 * Set the context version.
	 *
	 * @param contextVersion
	 *            the context version.
	 */
	public void setContextVersion(final String contextVersion) {
		this.contextVersion = contextVersion;
	}
}
