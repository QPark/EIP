/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.config;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.web.context.ServletContextAware;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import com.qpark.eip.core.EipJpaVendorAdapterConfiguration;
import com.qpark.eip.core.EipSettings;
import com.qpark.eip.core.failure.BaseFailureHandler;
import com.qpark.eip.core.logback.LoggingInitializer;
import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.EipSoapActionWebServiceMessageCallback;
import com.qpark.eip.core.spring.auth.DatabaseUserProvider;
import com.qpark.eip.core.spring.auth.config.EipAuthConfig;
import com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig;
import com.samples.platform.core.BusSettings;
import com.samples.platform.core.SamplesMessageContentProvider;
import com.samples.platform.core.SystemUserInitDao;
import com.samples.platform.persistenceconfig.PersistenceConfig;

/**
 * Provides the java spring config.
 *
 * @author bhausen
 */
@Configuration
@Import(value = {

		com.qpark.eip.core.persistence.config.EipPersistenceConfig.class,
		com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig.class,
		com.qpark.eip.core.spring.auth.config.EipAuthConfig.class,
		com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig.class,

		com.samples.platform.persistenceconfig.PersistenceConfig.class,
		com.samples.platform.persistenceconfig.JndiDataSourceConfig.class

})
@ImportResource(value = {

		/* This is needed to have a setup ApplicationPlaceholderConfigurer. */
		"classpath:com.samples.platform.properties-config.xml",

})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CoreSpringConfig implements ServletContextAware,
		ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
	/** The applications context name. */
	public static final String APPLICATION_CONTEXT_NAME = "iss-library";
	/** The {@link ApplicationContext}. */
	private ApplicationContext applicationContext;
	/** The {@link Logger}. */
	private Logger logger = null;
	/**
	 * The {@link ApplicationPlaceholderConfigurer} containing the configuration
	 * properties.
	 */
	@Autowired
	@Qualifier("ComSamplesPlatformProperties")
	private ApplicationPlaceholderConfigurer properties;
	/** The {@link ServletContext}. */
	private ServletContext servletContext;

	/**
	 * Get the {@link ContextNameProvider} of the {@link EipAuthConfig}.
	 *
	 * @return the {@link ContextNameProvider} of the {@link EipAuthConfig}.
	 */
	@Bean(name = EipAuthConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	public ContextNameProvider getEipAuthContextNameProvider(
			final ApplicationPlaceholderConfigurer applicationProperties) {
		ContextNameProvider bean = new ContextNameProvider();
		String contextVersion = applicationProperties
				.getProperty("eip.application.maven.artifact.version", "2.2.0");
		bean.setContextName(APPLICATION_CONTEXT_NAME);
		bean.setContextVersion(contextVersion);
		return bean;
	}

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
				"org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter");
		bean.setJpaVendorAdapterGenerateDdl(true);
		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");
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
				"org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter");
		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");

		bean.setJpaVendorAdapterGenerateDdl(true);
		return bean;
	}

	/**
	 * Get the {@link EipSoapActionWebServiceMessageCallback}.
	 *
	 * @return the {@link EipSoapActionWebServiceMessageCallback}.
	 */
	@Bean
	public EipSoapActionWebServiceMessageCallback getEipSoapActionWebServiceMessageCallback() {
		EipSoapActionWebServiceMessageCallback bean = new EipSoapActionWebServiceMessageCallback();
		return bean;
	}

	/**
	 * Get the {@link ContextNameProvider} of the {@link EipStatisticsConfig}.
	 *
	 * @return the {@link ContextNameProvider} of the
	 *         {@link EipStatisticsConfig}.
	 */
	@Bean(name = EipStatisticsConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	public ContextNameProvider getEipStatisticsContextNameProvider(
			final ApplicationPlaceholderConfigurer applicationProperties) {
		ContextNameProvider bean = new ContextNameProvider();
		String contextVersion = applicationProperties
				.getProperty("eip.application.maven.artifact.version", "2.2.0");
		bean.setContextName(APPLICATION_CONTEXT_NAME);
		bean.setContextVersion(contextVersion);
		return bean;
	}

	/**
	 * Get the {@link MessageContentProvider} of {@link EipStatisticsConfig}.
	 *
	 * @return the {@link MessageContentProvider} of {@link EipStatisticsConfig}
	 *         .
	 */
	@Bean(name = EipStatisticsConfig.STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME)
	public MessageContentProvider getEipStatisticsMessageContentProvider() {
		MessageContentProvider bean = new SamplesMessageContentProvider();
		return bean;
	}

	/**
	 * Get the {@link LoggingInitializer}.
	 *
	 * @return the {@link LoggingInitializer}.
	 */
	@Bean
	public LoggingInitializer getLoggingInitializer(
			final ApplicationPlaceholderConfigurer properties) {
		LoggingInitializer bean = new LoggingInitializer();
		bean.initialize(properties.get(EipSettings.EIP_APPLICATION_NAME),
				properties.get(EipSettings.EIP_SERVICE_NAME),
				properties.get(EipSettings.EIP_SERVICE_VERSION));
		if (this.logger == null) {
			this.logger = org.slf4j.LoggerFactory
					.getLogger(CoreSpringConfig.class);
		}
		properties.put(BusSettings.BUS_TC_SERVER_INFO,
				this.servletContext.getServerInfo());
		properties.put(BusSettings.BUS_SERVLET_CONTEXT_NAME,
				this.servletContext.getServletContextName());
		return bean;
	}

	/**
	 * Get the {@link SaajSoapMessageFactory} with name <code></code> supporting
	 * soap messages version 1.2.
	 *
	 * @return the {@link SaajSoapMessageFactory}.
	 */
	@Bean(name = "soap12MessageFactory")
	public SaajSoapMessageFactory getSoap12MessageFactory() {
		SaajSoapMessageFactory bean = new SaajSoapMessageFactory();
		bean.setSoapVersion(SoapVersion.SOAP_12);
		return bean;
	}

	/**
	 * Get the {@link SystemUserInitDao}.
	 *
	 * @return the {@link SystemUserInitDao}.
	 */
	@Bean
	public SystemUserInitDao getSystemUserInitDao() {
		SystemUserInitDao bean = new SystemUserInitDao();
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
				"org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter");
		bean.setJpaVendorAdapterGenerateDdl(true);
		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");
		return bean;
	}

	/** Initialize failure messages. */
	private void initializeFailureMessages() {
		try {
			Resource[] resources = this.applicationContext
					.getResources(new StringBuffer(32).append("classpath*:")
							.append(BaseFailureHandler.FAILURE_MESSAGES_XML)
							.toString());
			for (Resource resource : resources) {
				BaseFailureHandler
						.addFailureMessages(resource.getInputStream());
			}
			// PathMatchingResourcePatternResolver resolver = new
			// PathMatchingResourcePatternResolver();
			// Resource[] xsds = resolver.getResources("classpath*:**/*.xsd");
			// for (Resource xsd : xsds) {
			// if (!String.valueOf(xsd.getURL()).startsWith("jar")) {
			// this.logger.debug("contained xsds: {}",
			// xsd.getFile().getAbsolutePath());
			// }
			// }
		} catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	/**
	 * After application context refresh event.
	 *
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		/* Do at last. */
		/* Insert system user credentials into database. */
		this.initializeFailureMessages();

		this.properties.put(BusSettings.BUS_TC_SERVER_INFO,
				this.servletContext.getServerInfo());
		this.properties.put(BusSettings.BUS_SERVLET_CONTEXT_NAME,
				this.servletContext.getServletContextName());

		/* Log the configuration. */
		this.logger.info("ServletContext server info:          {}",
				this.servletContext.getServerInfo());
		this.logger.info("ServletContext servlet context name: {}",
				this.servletContext.getServletContextName());

		this.logger.info("Web app service name:                {}",
				this.properties.getProperty(EipSettings.EIP_SERVICE_NAME));
		this.logger.info("Web app application build time:      {}",
				this.properties
						.getProperty(EipSettings.EIP_APPLICATION_BUILD_TIME));
		this.logger.info("Web app application scm revision:    {}",
				this.properties
						.getProperty(EipSettings.EIP_APPLICATION_SCM_REVISION));
		this.logger.info("Web app maven groupId:               {}",
				this.properties.getProperty(
						EipSettings.EIP_APPLICATION_ARTIFACT_GROUPID));
		this.logger.info("Web app maven artifactId:            {}",
				this.properties.getProperty(
						EipSettings.EIP_APPLICATION_ARTIFACT_ARTIFACTID));
		this.logger.info("Web app maven version:               {}",
				this.properties.getProperty(
						EipSettings.EIP_APPLICATION_ARTIFACT_VERSION));
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(ServletContext)
	 */
	@Override
	public void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
