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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
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
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CoreSpringConfig implements BeanPostProcessor, ServletContextAware,
		ApplicationContextAware, InitializingBean {
	/** The applications context name. */
	public static final String APPLICATION_CONTEXT_NAME = "iss-library";

	/** The {@link ApplicationContext}. */
	private ApplicationContext applicationContext;
	/** The {@link ContextNameProvider} of {@link EipAuthConfig}. */
	@Autowired
	@Qualifier(EipAuthConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	private ContextNameProvider eipAuthContextNameProvider;
	/** The {@link ContextNameProvider} of {@link EipStatisticsConfig}. */
	@Autowired
	@Qualifier(EipStatisticsConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	private ContextNameProvider eipStatisticsContextNameProvider;
	/** Number of beans already initialized. */
	private short initCount = 0;
	/** The {@link Logger}. */
	private Logger logger = null;
	/** The {@link LoggingInitializer}. */
	@Autowired
	private LoggingInitializer loggingInitializer;

	/**
	 * The {@link ApplicationPlaceholderConfigurer} containing the configuration
	 * properties.
	 */
	@Autowired
	private ApplicationPlaceholderConfigurer properties;

	/** The {@link ServletContext}. */
	private ServletContext servletContext;

	/** The {@link SystemUserInitDao}. */
	@Autowired
	private SystemUserInitDao systemUserInitDao;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.initializeLogging();
		this.initializeContextNameProviders();
		this.initializeFailureMessages();
	}

	/**
	 * Get the {@link ContextNameProvider} of the {@link EipAuthConfig}.
	 *
	 * @return the {@link ContextNameProvider} of the {@link EipAuthConfig}.
	 */
	@Bean(name = EipAuthConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	public ContextNameProvider getEipAuthContextNameProvider() {
		ContextNameProvider bean = new ContextNameProvider();
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
		bean.setJpaVendorAdapterGenerateDdl(true);
		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");
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
	public ContextNameProvider getEipStatisticsContextNameProvider() {
		ContextNameProvider bean = new ContextNameProvider();
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
	public LoggingInitializer getLoggingInitializer() {
		LoggingInitializer bean = new LoggingInitializer();
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

	/** Initialize {@link ContextNameProvider}s. */
	private void initializeContextNameProviders() {
		String contextVersion = this.properties
				.getProperty("eip.application.maven.artifact.version", "2.2.0");
		this.eipAuthContextNameProvider
				.setContextName(APPLICATION_CONTEXT_NAME);
		this.eipAuthContextNameProvider.setContextVersion(contextVersion);
		this.eipStatisticsContextNameProvider
				.setContextName(APPLICATION_CONTEXT_NAME);
		this.eipStatisticsContextNameProvider.setContextVersion(contextVersion);
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
		} catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	/** Initialize logback logging. */
	private void initializeLogging() {
		this.loggingInitializer.initialize(
				this.properties.get(EipSettings.EIP_APPLICATION_NAME),
				this.properties.get(EipSettings.EIP_SERVICE_NAME),
				this.properties.get(EipSettings.EIP_SERVICE_VERSION));
		if (this.logger == null) {
			this.logger = org.slf4j.LoggerFactory
					.getLogger(CoreSpringConfig.class);
		}
		this.properties.put(BusSettings.BUS_TC_SERVER_INFO,
				this.servletContext.getServerInfo());
		this.properties.put(BusSettings.BUS_SERVLET_CONTEXT_NAME,
				this.servletContext.getServletContextName());
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(final Object bean,
			final String beanName) throws BeansException {
		this.initCount++;
		if (this.initCount == this.applicationContext
				.getBeanDefinitionCount()) {
			/* Do at last. */
			/* Insert system user credentials into database. */
			this.systemUserInitDao.enterSystemUser(APPLICATION_CONTEXT_NAME,
					"bus", "password", "ROLE_ALL_OPERATIONS");
			this.systemUserInitDao.enterSystemUser(APPLICATION_CONTEXT_NAME,
					"library", "password", "ROLE_COMMON_GETREFERENCEDATA",
					"ROLE_LIBRARY");
			/* Log the configuration. */
			this.logger.info("ServletContext server info: ",
					this.properties.get(BusSettings.BUS_TC_SERVER_INFO));
			this.logger.info("ServletContext servlet context name: ",
					this.properties.get(BusSettings.BUS_SERVLET_CONTEXT_NAME));

			this.logger.info("Web app service name: ",
					this.properties.get(EipSettings.EIP_SERVICE_NAME));
			this.logger.info("Web app service version: ",
					this.properties.get(EipSettings.EIP_SERVICE_VERSION));
			this.logger.info("Web app application build time: ", this.properties
					.get(EipSettings.EIP_APPLICATION_BUILD_TIME));
			this.logger.info("Web app application scm revision: ",
					this.properties
							.get(EipSettings.EIP_APPLICATION_SCM_REVISION));
			this.logger.info("Web app end point definition: ",
					this.properties.get(EipSettings.EIP_WEB_SERVICE_SERVER));

			this.logger.info("Web app maven groupId: ", this.properties
					.get(EipSettings.EIP_APPLICATION_ARTIFACT_GROUPID));
			this.logger.info("Web app maven artifactId: ", this.properties
					.get(EipSettings.EIP_APPLICATION_ARTIFACT_ARTIFACTID));
			this.logger.info("Web app maven version: ", this.properties
					.get(EipSettings.EIP_APPLICATION_ARTIFACT_VERSION));
		}
		return bean;
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object bean,
			final String beanName) throws BeansException {
		return bean;
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
