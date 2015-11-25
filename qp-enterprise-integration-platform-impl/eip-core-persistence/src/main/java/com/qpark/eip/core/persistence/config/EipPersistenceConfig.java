/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.�r.l. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License
 * v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core.persistence.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.qpark.eip.core.persistence.AsyncDatabaseOperationPoolProvider;
import com.qpark.eip.core.persistence.JAXBStrategySetup;

/**
 * Provides the spring config of the eip persistence authority.
 *
 * @author bhausen
 */
@Configuration
@EnableTransactionManagement
public class EipPersistenceConfig {
	/** The default value of the datasource name in the JNDI context. */
	public static final String DEFAULT_DATASOURCE_JNDI_NAME = "jdbc/BusUtilityDB";
	/**
	 * The default value of the jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 */
	public static final String DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME = "org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter";
	/**
	 * The default value of the database platform to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 */
	public static final String DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM = "org.hibernate.dialect.Oracle10gDialect";
	/**
	 * The default value of the database platform to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 */
	public static final Boolean DEFAULT_JPA_VENDOR_ADPATER_GENERATE_DDL = Boolean.FALSE;

	/** The datasource name in the JNDI context. */
	private String dataSourceJndiName = DEFAULT_DATASOURCE_JNDI_NAME;

	/**
	 * The jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 */
	private String jpaVendorAdapterClassName = DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME;

	/**
	 * The generateDdl parameter to be set into the
	 * {@link AbstractJpaVendorAdapter}. Defaults to <code>false</code> if not
	 * set and {@link #jpaVendorAdpaterDatabasePlatform} not equals to
	 * {@link org.hibernate.dialect.HSQLDialect} and not equals
	 * {@link org.springframework.orm.jpa.vendor.Database#HSQL}.
	 */
	private Boolean jpaVendorAdapterGenerateDdl = null;

	/**
	 * The database platform to be set into the {@link AbstractJpaVendorAdapter}
	 * .
	 */
	private String jpaVendorAdpaterDatabasePlatform = DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM;

	/**
	 * Create the spring config of the eip persistence authority.
	 */
	public EipPersistenceConfig() {
		JAXBStrategySetup.setup();
	}

	/**
	 * Get the {@link AsyncDatabaseOperationPoolProvider} bean.
	 *
	 * @return the {@link AsyncDatabaseOperationPoolProvider} bean.
	 */
	@Bean(name = "ComQparkEipCoreAsyncDatabaseOperationPoolProvider")
	public AsyncDatabaseOperationPoolProvider getAsyncDatabaseOperationPoolProvider() {
		AsyncDatabaseOperationPoolProvider bean = new AsyncDatabaseOperationPoolProvider();
		return bean;
	}

	/**
	 * Get the {@link LocalContainerEntityManagerFactoryBean}.
	 *
	 * @return the {@link LocalContainerEntityManagerFactoryBean}.
	 */
	@Bean(name = "ComQparkEipCoreEntityManagerFactory")
	public EntityManagerFactory getEntityManagerFactory() {
		AbstractJpaVendorAdapter jpaVendorAdapter = this.getJpaVendorAdapter();
		if (jpaVendorAdapter == null) {
			throw new RuntimeException(
					"ComQparkEipCoreEntityManagerFactory jpaVendorAdpater not set properly "
							+ String.valueOf(jpaVendorAdapter) + ".");
		}

		if (this.jpaVendorAdpaterDatabasePlatform == null
				|| this.jpaVendorAdpaterDatabasePlatform.trim().length() == 0) {
			throw new RuntimeException(
					"ComQparkEipCoreEntityManagerFactory jpaVendorAdpaterDatabasePlatform not set properly "
							+ String.valueOf(
									this.jpaVendorAdpaterDatabasePlatform)
							+ ".");
		}

		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setPersistenceXmlLocation(
				"classpath:/META-INF/com.qpark.eip.core.persistence/persistence.xml");
		bean.setPersistenceUnitName("com.qpark.eip.core.persistence");
		bean.setDataSource(this.getJndiDataSource());

		jpaVendorAdapter.setShowSql(false);
		jpaVendorAdapter.setGenerateDdl(this.isJpaVendorAdapterGenerateDdl());
		jpaVendorAdapter
				.setDatabasePlatform(this.jpaVendorAdpaterDatabasePlatform);
		bean.setJpaVendorAdapter(jpaVendorAdapter);
		bean.afterPropertiesSet();
		return bean.getObject();
	}

	/**
	 * Get the {@link DataSource} out of the JNDI naming context.
	 *
	 * @return the {@link DataSource} out of the JNDI naming context.
	 */
	@Bean(name = "ComQparkEipCoreDataSourceJndi")
	public DataSource getJndiDataSource() {
		JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		DataSource bean = dsLookup.getDataSource(this.dataSourceJndiName);
		return bean;
	}

	/**
	 * Get the {@link JpaTransactionManager}.
	 *
	 * @return the {@link JpaTransactionManager}.
	 */
	@Bean(name = "ComQparkEipCoreTransactionManager")
	public JpaTransactionManager getJpaTransactionManager() {
		JpaTransactionManager bean = new JpaTransactionManager();
		EntityManagerFactory emf = this.getEntityManagerFactory();
		bean.setPersistenceUnitName("com.qpark.eip.core.persistence");
		bean.setEntityManagerFactory(emf);
		return bean;
	}

	/**
	 * Get the {@link AbstractJpaVendorAdapter} out of the property
	 * {@link #jpaVendorAdapterClassName}.
	 *
	 * @return the {@link AbstractJpaVendorAdapter} .
	 */
	private AbstractJpaVendorAdapter getJpaVendorAdapter() {
		AbstractJpaVendorAdapter jpaVendorAdapter = null;
		if (this.jpaVendorAdapterClassName == null
				|| this.jpaVendorAdapterClassName
						.equals("org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter")
				|| this.jpaVendorAdapterClassName
						.equalsIgnoreCase("Hibernate")) {
			jpaVendorAdapter = new HibernateJpaVendorAdapter();
		} else if (this.jpaVendorAdapterClassName
				.equals("org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter")
				|| this.jpaVendorAdapterClassName
						.equalsIgnoreCase("EclipseLink")) {
			jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
		} else if (this.jpaVendorAdapterClassName
				.equals("org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter")
				|| this.jpaVendorAdapterClassName.equalsIgnoreCase("OpenJpa")) {
			jpaVendorAdapter = new OpenJpaVendorAdapter();
		}
		return jpaVendorAdapter;
	}

	/**
	 * Get the generateDdl parameter to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 *
	 * @return the generateDdl parameter to be set into the
	 *         {@link AbstractJpaVendorAdapter}.
	 */
	private boolean isJpaVendorAdapterGenerateDdl() {
		boolean value = DEFAULT_JPA_VENDOR_ADPATER_GENERATE_DDL.booleanValue();
		if (this.jpaVendorAdapterGenerateDdl != null) {
			value = this.jpaVendorAdapterGenerateDdl.booleanValue();
		} else {
			if (this.jpaVendorAdpaterDatabasePlatform
					.equals("org.hibernate.dialect.HSQLDialect")
					|| this.jpaVendorAdpaterDatabasePlatform
							.equals(Database.HSQL.name())) {
				value = true;
			}
		}
		return value;
	}

	/**
	 * Set the datasource name in the JNDI context.
	 *
	 * @param dataSourceJndiName
	 *            the datasource name in the JNDI context.
	 */
	public void setDataSourceJndiName(final String dataSourceJndiName) {
		this.dataSourceJndiName = dataSourceJndiName;
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
	 * Set the generateDdl parameter to be set into the
	 * {@link AbstractJpaVendorAdapter}. Defaults to <code>false</code> if not
	 * set and {@link #setJpaVendorAdpaterDatabasePlatform(String)} not equals
	 * to {@link org.hibernate.dialect.HSQLDialect}.
	 *
	 * @param jpaVendorAdpaterDatabasePlatform
	 *            the generateDdl parameter to be set into the
	 *            {@link AbstractJpaVendorAdapter}.
	 */
	public void setJpaVendorAdapterGenerateDdl(
			final boolean jpaVendorAdapterGenerateDdl) {
		this.jpaVendorAdapterGenerateDdl = jpaVendorAdapterGenerateDdl;
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
