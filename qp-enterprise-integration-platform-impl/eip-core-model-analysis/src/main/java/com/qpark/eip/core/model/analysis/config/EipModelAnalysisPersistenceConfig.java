/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.qpark.eip.core.EipJpaVendorAdapterConfiguration;
import com.qpark.eip.core.model.analysis.AnalysisDao;

/**
 * Provides the spring config of the persistence unit
 * {@value #PERSISTENCE_UNIT_NAME}. The {@link DataSource} to use has the bean
 * name {@value #DATASOURCE_BEAN_NAME}. The
 * {@link EipJpaVendorAdapterConfiguration} to use has the bean name
 * {@value #JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME}.
 *
 * @author bhausen
 */
@Configuration
@EnableTransactionManagement
public class EipModelAnalysisPersistenceConfig {
	/** The name of the {@link DataSource} bean name. */
	public static final String DATASOURCE_BEAN_NAME = "ComQparkEipModelAnalysisDataSourceJndi";
	/** The name of the persistence unit. */
	public static final String PERSISTENCE_UNIT_NAME = "com.qpark.eip.model.docmodel";
	/** The name of the entity manager factory. */
	public static final String ENTITY_MANAGER_FACTORY_NAME = "ComQparkEipModelAnalysisEntityManagerFactory";
	/** The name of the transaction manager. */
	public static final String TRANSACTION_MANAGER_NAME = "ComQparkEipModelAnalysisTransactionManager";
	/** The name of the {@link EipJpaVendorAdapterConfiguration} bean name. */
	public static final String JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME = "ComQparkEipModelAnalysisEipPersistenceJpaVendorAdapterConfiguration";

	/** The {@link EipJpaVendorAdapterConfiguration} to use. */
	@Autowired
	@Qualifier(JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME)
	private EipJpaVendorAdapterConfiguration jpaVendorAdapterConfiguration;

	/** The {@link DataSource} to use. */
	@Autowired
	@Qualifier(DATASOURCE_BEAN_NAME)
	private DataSource dataSource;

	/** Create the spring config of {@value #PERSISTENCE_UNIT_NAME}. */
	public EipModelAnalysisPersistenceConfig() {
	}

	/**
	 * @return the {@link AnalysisDao}.
	 */
	@Bean
	public AnalysisDao getAnalysisDao() {
		return new AnalysisDao();
	}

	/**
	 * Get the {@link LocalContainerEntityManagerFactoryBean}.
	 *
	 * @return the {@link LocalContainerEntityManagerFactoryBean}.
	 */
	@Bean(name = ENTITY_MANAGER_FACTORY_NAME)
	public EntityManagerFactory getEntityManagerFactory() {
		AbstractJpaVendorAdapter jpaVendorAdapter = this.getJpaVendorAdapter();
		if (jpaVendorAdapter == null) {
			throw new RuntimeException(
					String.format("%s jpaVendorAdpater not set properly %s.",
							ENTITY_MANAGER_FACTORY_NAME,
							String.valueOf(jpaVendorAdapter)));
		}
		String jpaVendorAdapterDatabasePlatform = this.jpaVendorAdapterConfiguration
				.getJpaVendorAdpaterDatabasePlatform();
		if (jpaVendorAdapterDatabasePlatform == null
				|| jpaVendorAdapterDatabasePlatform.trim().length() == 0) {
			throw new RuntimeException(String.format(
					"%s jpaVendorAdpaterDatabasePlatform not set properly %s.",
					ENTITY_MANAGER_FACTORY_NAME,
					String.valueOf(jpaVendorAdapterDatabasePlatform)));
		}

		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setPersistenceXmlLocation(
				String.format("classpath:/META-INF/%s/persistence.xml",
						PERSISTENCE_UNIT_NAME));
		bean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
		bean.setDataSource(this.dataSource);

		jpaVendorAdapter.setDatabasePlatform(jpaVendorAdapterDatabasePlatform);
		jpaVendorAdapter.setShowSql(false);
		if (this.isJpaVendorAdapterGenerateDdl()) {
			jpaVendorAdapter.setGenerateDdl(true);
			if (HibernateJpaVendorAdapter.class.isInstance(jpaVendorAdapter)) {
				bean.getJpaPropertyMap().put("hibernate.hbm2ddl.auto",
						"update");
			}
		} else {
			jpaVendorAdapter.setGenerateDdl(false);
		}

		bean.setJpaVendorAdapter(jpaVendorAdapter);
		bean.afterPropertiesSet();
		return bean.getObject();
	}

	/**
	 * Get the {@link JpaTransactionManager}.
	 *
	 * @return the {@link JpaTransactionManager}.
	 */
	@Bean(name = TRANSACTION_MANAGER_NAME)
	public JpaTransactionManager getJpaTransactionManager() {
		JpaTransactionManager bean = new JpaTransactionManager();
		EntityManagerFactory emf = this.getEntityManagerFactory();
		bean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
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
		String jpaVendorAdapterClassName = this.jpaVendorAdapterConfiguration
				.getJpaVendorAdapterClassName();
		if (jpaVendorAdapterClassName == null
				|| jpaVendorAdapterClassName
						.equals("org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter")
				|| jpaVendorAdapterClassName.equalsIgnoreCase("Hibernate")) {
			jpaVendorAdapter = new HibernateJpaVendorAdapter();
		} else if (jpaVendorAdapterClassName
				.equals("org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter")
				|| jpaVendorAdapterClassName.equalsIgnoreCase("EclipseLink")) {
			jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
		} else if (jpaVendorAdapterClassName
				.equals("org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter")
				|| jpaVendorAdapterClassName.equalsIgnoreCase("OpenJpa")) {
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
		Boolean value = this.jpaVendorAdapterConfiguration
				.getJpaVendorAdapterGenerateDdl();
		if (value == null) {
			if (this.jpaVendorAdapterConfiguration
					.getJpaVendorAdpaterDatabasePlatform()
					.equals("org.hibernate.dialect.HSQLDialect")
					|| this.jpaVendorAdapterConfiguration
							.getJpaVendorAdpaterDatabasePlatform()
							.equals(Database.HSQL.name())) {
				value = Boolean.TRUE;
			} else {
				value = Boolean.FALSE;
			}
		}
		return value.booleanValue();
	}
}
