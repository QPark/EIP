/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.auth.DatabaseUserProvider;
import com.qpark.eip.core.spring.auth.PropertyUserProvider;

/**
 * Provides the spring config of the eip auth. Requires a
 * {@link ContextNameProvider} with name
 * {@value #CONTEXTNAME_PROVIDER_BEAN_NAME} in the spring context deployed.
 *
 * @author bhausen
 */
@Configuration
@EnableScheduling
public class EipAuthPropertyConfig {
	/** The bean name of the {@link ContextNameProvider}. */
	public static final String CONTEXTNAME_PROVIDER_BEAN_NAME = "ComQparkEipCoreSpringAuthContextNameProvider";
	/** The bean name of the {@link DatabaseUserProvider}. */
	public static final String PROPERTY_USER_PROVIDER_BEAN_NAME = "ComQparkEipCoreSpringAuthPropertyUserProvider";
	/** The {@link ContextNameProvider}. */
	@Autowired
	@Qualifier(CONTEXTNAME_PROVIDER_BEAN_NAME)
	private ContextNameProvider contextNameProvider;

	/**
	 * Create the spring config of the eip core authority.
	 */
	public EipAuthPropertyConfig() {
	}

	/**
	 * Get the context name.
	 *
	 * @return the context definition.
	 */
	public String getContextDefinition() {
		return String.format("%s:%s", this.contextNameProvider.getContextName(),
				this.contextNameProvider.getContextVersion());
	}

	/**
	 * Get the {@link DatabaseUserProvider} bean.
	 *
	 * @return the {@link DatabaseUserProvider} bean.
	 */
	@Bean(name = PROPERTY_USER_PROVIDER_BEAN_NAME)
	public PropertyUserProvider getUserProvider() {
		PropertyUserProvider bean = new PropertyUserProvider();
		return bean;
	}
}
