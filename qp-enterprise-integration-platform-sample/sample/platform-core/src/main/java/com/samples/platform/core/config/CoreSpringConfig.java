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
import org.springframework.web.context.ServletContextAware;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import com.qpark.eip.core.EipSettings;
import com.qpark.eip.core.failure.BaseFailureHandler;
import com.qpark.eip.core.logback.LoggingInitializer;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.EipSoapActionWebServiceMessageCallback;
import com.samples.domain.serviceprovider.AppSecurityContextHandler;
import com.samples.platform.core.BusSettings;
import com.samples.platform.core.SystemUserInitDao;
import com.samples.platform.core.security.SetAppSecurityContextAuthentication;

/**
 * Provides the java spring config.
 *
 * @author bhausen
 */
@Configuration
@Import(value = {

		com.samples.platform.core.config.CoreEIPPersistenceConfig.class,
		com.samples.platform.core.config.CoreValidatorConfig.class

})
@ImportResource(value = {

		/* This is needed to have a setup ApplicationPlaceholderConfigurer. */
		"classpath:com.samples.platform.properties-config.xml",

})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SuppressWarnings("static-method")
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
	 * Get the {@link EipSoapActionWebServiceMessageCallback}.
	 *
	 * @return the {@link EipSoapActionWebServiceMessageCallback}.
	 */
	@Bean
	public EipSoapActionWebServiceMessageCallback getEipSoapActionWebServiceMessageCallback() {
		EipSoapActionWebServiceMessageCallback bean = new EipSoapActionWebServiceMessageCallback();
		return bean;
	}

	@Bean
	public AppSecurityContextHandler getAppSecurityContextHandler() {
		return new SetAppSecurityContextAuthentication();
	}

	/**
	 * Get the {@link LoggingInitializer}.
	 *
	 * @param applicationProperties
	 * @return the {@link LoggingInitializer}.
	 */
	@Bean
	public LoggingInitializer getLoggingInitializer(
			final ApplicationPlaceholderConfigurer applicationProperties) {
		LoggingInitializer bean = new LoggingInitializer();
		bean.initialize(
				applicationProperties.get(EipSettings.EIP_APPLICATION_NAME),
				applicationProperties.get(EipSettings.EIP_SERVICE_NAME),
				applicationProperties.get(EipSettings.EIP_SERVICE_VERSION));
		if (this.logger == null) {
			this.logger = org.slf4j.LoggerFactory
					.getLogger(CoreSpringConfig.class);
		}
		applicationProperties.put(BusSettings.BUS_TC_SERVER_INFO,
				this.servletContext.getServerInfo());
		applicationProperties.put(BusSettings.BUS_SERVLET_CONTEXT_NAME,
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
