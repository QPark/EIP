/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.config;

import java.util.TimeZone;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.qpark.eip.core.EipJpaVendorAdapterConfiguration;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.core.model.analysis.PersistModelAnalysis;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(value = { EipModelAnalysisPersistenceConfig.class })
@ConfigurationProperties
public class ModelAnalysisPersistence implements InitializingBean {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(ModelAnalysisPersistence.class);

	/**
	 * The <b>static</b> {@link PropertySourcesPlaceholderConfigurer} to get the
	 * properties.
	 *
	 * @return the {@link PropertySourcesPlaceholderConfigurer}.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * Run the spring boot application.
	 *
	 * @param args
	 *            the arguments.
	 */
	public static void main(final String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("file.encoding", "UTF-8");
		SpringApplication.run(ModelAnalysisPersistence.class, args);
	}

	/**
	 * Get the {@link PersistModelAnalysis}.
	 *
	 * @return the {@link PersistModelAnalysis}.
	 */
	@Bean
	public PersistModelAnalysis getPersistModelAnalysis() {
		PersistModelAnalysis bean = new PersistModelAnalysis();
		return bean;
	}

	/**
	 * Get the {@link AnalysisDao}.
	 *
	 * @return the {@link AnalysisDao}.
	 */
	@Bean
	public AnalysisDao getModelAnalysisDao() {
		AnalysisDao bean = new AnalysisDao();
		return bean;
	}

	/**
	 * Get the {@link DataSource} auto wired in the
	 * {@link EipModelAnalysisPersistenceConfig}.
	 *
	 * @return The {@link DataSource}
	 */
	@Bean(name = EipModelAnalysisPersistenceConfig.DATASOURCE_BEAN_NAME)
	public DataSource getDataSource() {
		DriverManagerDataSource bean = new DriverManagerDataSource();
		bean.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		bean.setUrl("jdbc:hsqldb:file:src/test/hsqldb/domainDocHSQLDB.bin");
		bean.setUsername("platformUser");
		bean.setPassword("platformUserPwd");
		return bean;
	}

	/**
	 * Set the {@link EipJpaVendorAdapterConfiguration} of
	 * {@link EipModelAnalysisPersistenceConfig}.
	 *
	 * @return the {@link EipJpaVendorAdapterConfiguration} of
	 *         {@link EipModelAnalysisPersistenceConfig}.
	 */
	@Bean(name = EipModelAnalysisPersistenceConfig.JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME)
	public EipJpaVendorAdapterConfiguration getEipModelAnalysisJpaVendorConfiguration() {
		EipJpaVendorAdapterConfiguration bean = new EipJpaVendorAdapterConfiguration();
		bean.setJpaVendorAdapterClassName(
				"org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter");
		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");

		bean.setJpaVendorAdapterGenerateDdl(true);
		return bean;
	}

	@Autowired
	private PersistModelAnalysis persistModelAnalysis;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.logger.info("+afterPropertiesSet ");
		this.persistModelAnalysis.test();
		this.logger.info("-afterPropertiesSet");
	}
}
