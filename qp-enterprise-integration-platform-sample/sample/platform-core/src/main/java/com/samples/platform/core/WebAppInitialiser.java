/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.samples.platform.core;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;

import com.qpark.eip.core.EipSettings;
import com.qpark.eip.core.failure.BaseFailureHandler;
import com.qpark.eip.core.logback.LoggingInitializer;
import com.qpark.eip.core.persistence.JAXBStrategySetup;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;

public class WebAppInitialiser implements BeanPostProcessor,
		ServletContextAware, ApplicationContextAware {
	/** The {@link Logger}. */
	private static org.slf4j.Logger logger;
	private boolean initialised = false;

	/** The {@link ApplicationPlaceholderConfigurer}. */
	@Autowired
	private ApplicationPlaceholderConfigurer properties;

	@Autowired
	private LoggingInitializer loggingInitializer;

	private void initLogServletContext() {
		this.properties.put(BusSettings.BUS_TC_SERVER_INFO,
				this.servletContext.getServerInfo());
		this.properties.put(BusSettings.BUS_SERVLET_CONTEXT_NAME,
				this.servletContext.getServletContextName());
		logger.info("ServletContext server info: ",
				this.properties.get(BusSettings.BUS_TC_SERVER_INFO));
		logger.info("ServletContext servlet context name: ",
				this.properties.get(BusSettings.BUS_SERVLET_CONTEXT_NAME));

		logger.info("Web app service name: ",
				this.properties.get(EipSettings.EIP_SERVICE_NAME));
		logger.info("Web app service version: ",
				this.properties.get(EipSettings.EIP_SERVICE_VERSION));
		logger.info("Web app application build time: ",
				this.properties.get(EipSettings.EIP_APPLICATION_BUILD_TIME));
		logger.info("Web app application scm revision: ",
				this.properties.get(EipSettings.EIP_APPLICATION_SCM_REVISION));
		logger.info("Web app end point definition: ",
				this.properties.get(EipSettings.EIP_WEB_SERVICE_SERVER));

		logger.info("Web app maven groupId: ", this.properties
				.get(EipSettings.EIP_APPLICATION_ARTIFACT_GROUPID));
		logger.info("Web app maven artifactId: ", this.properties
				.get(EipSettings.EIP_APPLICATION_ARTIFACT_ARTIFACTID));
		logger.info("Web app maven version: ", this.properties
				.get(EipSettings.EIP_APPLICATION_ARTIFACT_VERSION));
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(final Object bean,
			final String beanName) throws BeansException {
		return bean;
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object bean,
			final String beanName) throws BeansException {
		if (!this.initialised) {
			JAXBStrategySetup.setup();
			this.loggingInitializer.initialize(
					this.properties.get(EipSettings.EIP_APPLICATION_NAME),
					this.properties.get(EipSettings.EIP_SERVICE_NAME),
					this.properties.get(EipSettings.EIP_SERVICE_VERSION));
			if (logger == null) {
				logger = org.slf4j.LoggerFactory
						.getLogger(WebAppInitialiser.class);
			}
			this.initLogServletContext();
			this.initFailureMessages();
			this.initialised = true;
		}
		return bean;
	}

	private void initFailureMessages() {
		logger.info("+initFailureMessages");
		try {
			Resource[] resources = this.applicationContext
					.getResources(new StringBuffer(32).append("classpath*:")
							.append(BaseFailureHandler.FAILURE_MESSAGES_XML)
							.toString());
			for (Resource resource : resources) {
				logger.debug(" initFailureMessages add: {}", resource.getURL());
				BaseFailureHandler
						.addFailureMessages(resource.getInputStream());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			logger.info("-initFailureMessages");
		}
	}

	private ApplicationContext applicationContext;

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private ServletContext servletContext;

	@Override
	public void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
