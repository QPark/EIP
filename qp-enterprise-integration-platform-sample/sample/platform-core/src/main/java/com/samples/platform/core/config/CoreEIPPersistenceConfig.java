/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.config;

import static com.samples.platform.core.config.CoreSpringConfig.APPLICATION_CONTEXT_NAME;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.qpark.eip.core.EipJpaVendorAdapterConfiguration;
import com.qpark.eip.core.model.analysis.config.EipModelAnalysisPersistenceConfig;
import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.auth.DatabaseUserProvider;
import com.qpark.eip.core.spring.auth.config.EipAuthConfig;
import com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.core.spring.statistics.StatisticsListener;
import com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig;
import com.qpark.eip.core.spring.statistics.impl.StatisticsInfoLogger;
import com.samples.platform.core.SamplesMessageContentProvider;
import com.samples.platform.persistenceconfig.PersistenceConfig;

/**
 * EIP core persistence config.
 *
 * @author bhausen
 */
@Configuration
@Import(value = {

		com.qpark.eip.core.persistence.config.EipPersistenceConfig.class,
		com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig.class,
		com.qpark.eip.core.spring.auth.config.EipAuthConfig.class,
		com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig.class,
		com.qpark.eip.core.model.analysis.config.EipModelAnalysisPersistenceConfig.class,

		com.samples.platform.persistenceconfig.PersistenceConfig.class,
		com.samples.platform.persistenceconfig.JndiDataSourceConfig.class

})
@SuppressWarnings("static-method")
public class CoreEIPPersistenceConfig {
	/**
	 * Get the {@link DatabaseUserProvider} of {@link EipAuthConfig}.
	 *
	 * @return the {@link DatabaseUserProvider} of {@link EipAuthConfig}.
	 */
	@Bean(name = "ComSamplesIssLibraryUserProvider")
	public DatabaseUserProvider getEipAuthDatabaseUserProvider() {
		DatabaseUserProvider bean = new DatabaseUserProvider();
		return bean;
	}

	/**
	 * Get the {@link StatisticsListener} of {@link EipStatisticsConfig}.
	 *
	 * @return the {@link StatisticsListener} of {@link EipStatisticsConfig}.
	 */
	@Bean
	public StatisticsListener getStatisticsListener() {
		StatisticsListener bean = new StatisticsInfoLogger();
		return bean;
	}

	/**
	 * Get the {@link ContextNameProvider} of the {@link EipAuthConfig}.
	 *
	 * @param applicationProperties The
	 *                              {@link ApplicationPlaceholderConfigurer}.
	 * @return the {@link ContextNameProvider} of the {@link EipAuthConfig}.
	 */
	@Bean(name = EipAuthConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	public ContextNameProvider getEipAuthContextNameProvider(
			final ApplicationPlaceholderConfigurer applicationProperties) {
		ContextNameProvider bean = new ContextNameProvider();
		String contextVersion = applicationProperties
				.getProperty("eip.application.maven.artifact.version", "2.0.0");
		bean.setContextName(APPLICATION_CONTEXT_NAME);
		bean.setContextVersion(contextVersion);
		return bean;
	}

	/**
	 * Get the {@link DataSource} of {@link EipLockedoperationConfig}.
	 *
	 * @return the {@link DataSource} of {@link EipLockedoperationConfig}.
	 */
	@Bean(name = EipLockedoperationConfig.DATASOURCE_BEAN_NAME)
	public DataSource getEipLockedoperationDataSource() {
		JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		DataSource bean = dsLookup
				.getDataSource(PersistenceConfig.DATASOURCE_JDNI_NAME);
		return bean;
	}

	/**
	 * Get the {@link DataSource} of {@link EipModelAnalysisPersistenceConfig}.
	 *
	 * @return the {@link DataSource} of
	 *         {@link EipModelAnalysisPersistenceConfig}.
	 */
	@Bean(name = EipModelAnalysisPersistenceConfig.DATASOURCE_BEAN_NAME)
	public DataSource getEipModelAnalysisDataSource() {
		JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		DataSource bean = dsLookup
				.getDataSource(PersistenceConfig.DATASOURCE_JDNI_NAME);
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
				EipJpaVendorAdapterConfiguration.DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME);

		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");
		bean.setJpaVendorAdapterGenerateDdl(true);
		return bean;
	}

	/**
	 * Set the {@link EipJpaVendorAdapterConfiguration} of
	 * {@link EipLockedoperationConfig}.
	 *
	 * @return the {@link EipJpaVendorAdapterConfiguration} of
	 *         {@link EipLockedoperationConfig}.
	 */
	@Bean(name = EipLockedoperationConfig.JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME)
	public EipJpaVendorAdapterConfiguration getEipLockedoperationJpaVendorConfiguration() {
		EipJpaVendorAdapterConfiguration bean = new EipJpaVendorAdapterConfiguration();
		bean.setJpaVendorAdapterClassName(
				EipJpaVendorAdapterConfiguration.DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME);

		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");
		bean.setJpaVendorAdapterGenerateDdl(true);
		return bean;
	}

	/**
	 * Get the {@link DataSource} of {@link EipPersistenceConfig}.
	 *
	 * @return the {@link DataSource} of {@link EipPersistenceConfig}.
	 */
	@Bean(name = EipPersistenceConfig.DATASOURCE_BEAN_NAME)
	public DataSource getEipPersistenceDataSource() {
		JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		DataSource bean = dsLookup
				.getDataSource(PersistenceConfig.DATASOURCE_JDNI_NAME);
		return bean;
	}

	/**
	 * Set the {@link EipJpaVendorAdapterConfiguration} of
	 * {@link EipPersistenceConfig}.
	 *
	 * @return the {@link EipJpaVendorAdapterConfiguration} of
	 *         {@link EipPersistenceConfig}.
	 */
	@Bean(name = EipPersistenceConfig.JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME)
	public EipJpaVendorAdapterConfiguration getEipPersistenceJpaVendorConfiguration() {
		EipJpaVendorAdapterConfiguration bean = new EipJpaVendorAdapterConfiguration();
		bean.setJpaVendorAdapterClassName(
				EipJpaVendorAdapterConfiguration.DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME);

		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");
		bean.setJpaVendorAdapterGenerateDdl(true);
		return bean;
	}

	/**
	 * Get the {@link ContextNameProvider} of the {@link EipStatisticsConfig}.
	 *
	 * @param applicationProperties The
	 *                              {@link ApplicationPlaceholderConfigurer}.
	 * @return the {@link ContextNameProvider} of the
	 *         {@link EipStatisticsConfig}.
	 */
	@Bean(name = EipStatisticsConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	public ContextNameProvider getEipStatisticsContextNameProvider(
			final ApplicationPlaceholderConfigurer applicationProperties) {
		ContextNameProvider bean = new ContextNameProvider();
		String contextVersion = applicationProperties
				.getProperty("eip.application.maven.artifact.version", "2.0.0");
		bean.setContextName(APPLICATION_CONTEXT_NAME);
		bean.setContextVersion(contextVersion);
		return bean;
	}

	/**
	 * Set the {@link EipJpaVendorAdapterConfiguration} of the web application.
	 *
	 * @return the {@link EipJpaVendorAdapterConfiguration} of the web
	 *         application.
	 */
	@Bean(name = PersistenceConfig.JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME)
	public EipJpaVendorAdapterConfiguration getWebAppPersistenceJpaVendorConfiguration() {
		EipJpaVendorAdapterConfiguration bean = new EipJpaVendorAdapterConfiguration();
		bean.setJpaVendorAdapterClassName(
				EipJpaVendorAdapterConfiguration.DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME);

		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");
		bean.setJpaVendorAdapterGenerateDdl(true);
		return bean;
	}

	/**
	 * Get the {@link MessageContentProvider} of {@link EipStatisticsConfig}.
	 *
	 * @param applicationProperties The
	 *                              {@link ApplicationPlaceholderConfigurer}.
	 * @return the {@link MessageContentProvider} of {@link EipStatisticsConfig}
	 *         .
	 */
	@Bean(name = EipStatisticsConfig.STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME)
	public MessageContentProvider getEipStatisticsMessageContentProvider(
			final ApplicationPlaceholderConfigurer applicationProperties) {
		MessageContentProvider bean = new SamplesMessageContentProvider();
		return bean;
	}
}
