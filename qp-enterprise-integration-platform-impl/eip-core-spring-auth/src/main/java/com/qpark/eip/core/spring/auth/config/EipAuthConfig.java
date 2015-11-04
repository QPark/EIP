package com.qpark.eip.core.spring.auth.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
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

	/** The {@link EipPersistenceConfig}. */
	@Autowired
	private EipPersistenceConfig eipPersistenceConfig;

	/**
	 * The jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 */
	private String jpaVendorAdapterClassName = EipPersistenceConfig.DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME;

	/**
	 * The database platform to be set into the {@link AbstractJpaVendorAdapter}
	 * .
	 */
	private String jpaVendorAdpaterDatabasePlatform = EipPersistenceConfig.DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM;

	/**
	 * Create the spring config of the eip core authority.
	 *
	 * @param contextName
	 *            the context name.
	 * @param contextVersion
	 *            the context version.
	 * @param jpaVendorAdapterClassName
	 *            the jpa Vendor adapter class name to be set in the
	 *            {@link LocalContainerEntityManagerFactoryBean}.
	 * @param jpaVendorAdpaterDatabasePlatform
	 *            the database platform to be set into the
	 *            {@link AbstractJpaVendorAdapter}.
	 */
	public EipAuthConfig(final String contextName, final String contextVersion,
			final String jpaVendorAdapterClassName,
			final String jpaVendorAdpaterDatabasePlatform) {
		this.contextName = contextName;
		this.contextVersion = contextVersion;
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
	 * The {@link EipAuthConfig} itself.
	 *
	 * @return the {@link EipAuthConfig}.
	 */
	@Bean(name = "ComQparkEipCoreSpringAuthEipSecurityConfig")
	public EipAuthConfig getEipSecurityConfig() {
		return this;
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
	 * Set the JPA vendor adapter settings for {@link EipPersistenceConfig}.
	 */
	@PostConstruct
	private void init() {
		this.eipPersistenceConfig
				.setJpaVendorAdapterClassName(this.jpaVendorAdapterClassName);
		this.eipPersistenceConfig.setJpaVendorAdpaterDatabasePlatform(
				this.jpaVendorAdpaterDatabasePlatform);
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

	/**
	 * Set the jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 *
	 * @param jpaVendorAdapterClassName
	 *            the jpa Vendor adapter class name to be set in the
	 *            {@link LocalContainerEntityManagerFactoryBean}.
	 */
	public void setJpaVendorAdapterClassName(
			final String jpaVendorAdapterClassName) {
		this.jpaVendorAdapterClassName = jpaVendorAdapterClassName;
	}

	/**
	 * Set the database platform to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 *
	 * @param jpaVendorAdpaterDatabasePlatform
	 *            the database platform to be set into the
	 *            {@link AbstractJpaVendorAdapter}.
	 */
	public void setJpaVendorAdpaterDatabasePlatform(
			final String jpaVendorAdpaterDatabasePlatform) {
		this.jpaVendorAdpaterDatabasePlatform = jpaVendorAdpaterDatabasePlatform;
	}
}
