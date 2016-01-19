/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
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
	/** The {@link ApplicationContext}. */
	private ApplicationContext applicationContext;
	/** The {@link EipAuthConfig}. */
	@Autowired
	private EipAuthConfig eipAuthConfig;
	/** The {@link EipStatisticsConfig}. */
	@Autowired
	private EipStatisticsConfig eipStatisticsConfig;
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
			e.printStackTrace();
		}
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
	 * Get the {@link MessageContentProvider} of the eip statistics.
	 *
	 * @return the {@link MessageContentProvider} of the eip statistics.
	 */
	@Bean(name = EipStatisticsConfig.STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME)
	public MessageContentProvider getStatisticsMessageContentProvider() {
		MessageContentProvider bean = new SamplesMessageContentProvider();
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
	 * Set the {@link EipJpaVendorAdapterConfiguration} of
	 * {@link EipPersistenceConfig}.
	 *
	 * @return the {@link EipJpaVendorAdapterConfiguration} of
	 *         {@link EipPersistenceConfig}.
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
			String contextName = this.properties
					.getProperty("eip.application.maven.artifact.artifactid");
			this.systemUserInitDao.enterSystemUser(contextName, "bus",
					"password", "ROLE_ALL_OPERATIONS");
			this.systemUserInitDao.enterSystemUser(contextName, "library",
					"password", "ROLE_COMMON_GETREFERENCEDATA", "ROLE_LIBRARY");
		}
		if (bean.equals(this.eipStatisticsConfig)) {
			this.logger.debug("EipStatisticsConfig.context={}",
					this.eipStatisticsConfig.getContextDefinition());
		} else if (bean.equals(this.eipAuthConfig)) {
			this.logger.debug("EipAuthConfig.context={}",
					this.eipAuthConfig.getContextDefinition());
		}
		return bean;
	}

	private boolean loggingInitialized = false;

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object bean,
			final String beanName) throws BeansException {
		/* Logging initialization. */
		this.initializeLogging();
		/* Setup beans. */
		String contextName = this.properties
				.getProperty("eip.application.maven.artifact.artifactid");
		String contextVersion = this.properties
				.getProperty("eip.application.maven.artifact.version");
		if (bean.equals(this.eipStatisticsConfig)) {
			this.logger.debug("postProcessBeforeInitialization "
					+ this.eipStatisticsConfig.getClass().getName());
			this.eipStatisticsConfig.setContextName(contextName);
			this.eipStatisticsConfig.setContextVersion(contextVersion);
			this.eipStatisticsConfig.setNumberOfWeeksToKeepLogs(2);
		} else if (bean.equals(this.eipAuthConfig)) {
			this.logger.debug("postProcessBeforeInitialization "
					+ this.eipAuthConfig.getClass().getName());
			this.eipAuthConfig.setContextName(contextName);
			this.eipAuthConfig.setContextVersion(contextVersion);
		} else if (bean.equals(this.loggingInitializer)) {

		}
		return bean;
	}

	/** Initialize logging. */
	private void initializeLogging() {
		if (!this.loggingInitialized) {
			this.loggingInitializer.initialize(
					this.properties.get(EipSettings.EIP_APPLICATION_NAME),
					this.properties.get(EipSettings.EIP_SERVICE_NAME),
					this.properties.get(EipSettings.EIP_SERVICE_VERSION));
			if (this.logger == null) {
				this.logger = org.slf4j.LoggerFactory
						.getLogger(CoreSpringConfig.class);
			}
			this.loggingInitialized = true;
			this.properties.put(BusSettings.BUS_TC_SERVER_INFO,
					this.servletContext.getServerInfo());
			this.properties.put(BusSettings.BUS_SERVLET_CONTEXT_NAME,
					this.servletContext.getServletContextName());
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