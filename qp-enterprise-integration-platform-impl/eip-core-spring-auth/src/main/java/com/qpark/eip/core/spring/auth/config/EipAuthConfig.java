/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Provides the spring config of the eip auth. Requires a
 * {@link ContextNameProvider} with name
 * {@value #CONTEXTNAME_PROVIDER_BEAN_NAME} in the spring context deployed.
 *
 * @author bhausen
 */
@Configuration
@EnableScheduling
@Import({EipPersistenceConfig.class})
@SuppressWarnings("static-method")
public class EipAuthConfig {
  /** The bean name of the {@link ContextNameProvider}. */
  public static final String CONTEXTNAME_PROVIDER_BEAN_NAME =
      "ComQparkEipCoreSpringAuthContextNameProvider";
  /** The bean name of the {@link DatabaseUserProvider}. */
  public static final String DATABASE_USER_PROVIDER_BEAN_NAME =
      "ComQparkEipCoreSpringAuthDatabaseUserProvider";
  /** The bean name of the {@link LimitedAccessDataProvider}. */
  public static final String LIMITED_ACCESS_DATA_PROVIDER_BEAN_NAME =
      "ComQparkEipCoreSpringAuthLimitedAccessDataProvider";
  /** The {@link ContextNameProvider}. */
  @Autowired
  @Qualifier(CONTEXTNAME_PROVIDER_BEAN_NAME)
  private ContextNameProvider contextNameProvider;

  /**
   * Create the spring config of the eip core authority.
   */
  public EipAuthConfig() {}

  /**
   * Get the {@link AuthorityDao} bean.
   *
   * @return the {@link AuthorityDao} bean.
   */
  @Bean
  public AuthorityDao getAuthorityDao() {
    final AuthorityDao bean = new AuthorityDao();
    return bean;
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
  @Bean(name = DATABASE_USER_PROVIDER_BEAN_NAME)
  public DatabaseUserProvider getDatabaseUserProvider() {
    final DatabaseUserProvider bean = new DatabaseUserProvider();
    return bean;
  }

  /**
   * Get the {@link LimitedAccessDataProvider} bean.
   *
   * @return the {@link LimitedAccessDataProvider} bean.
   */
  @Bean(name = LIMITED_ACCESS_DATA_PROVIDER_BEAN_NAME)
  public LimitedAccessDataProvider getLimitedAccessDataProvider() {
    final LimitedAccessDataProvider bean = new LimitedAccessDataProvider();
    return bean;
  }
}
