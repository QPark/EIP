/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.samples.platform.core.flow.SystemUserLogFlowGateway;
import com.samples.platform.util.AggregatorGetReferenceData;
import com.samples.platform.util.OperationLogProvider;
import com.samples.platform.util.ServiceExecutionLogAspect;
import com.samples.platform.util.SystemUserLogFlowGatewayImpl;

/**
 * Provides the java spring config of the webapp.
 *
 * @author bhausen
 */
@Configuration
@Import(value = {

		com.samples.platform.serviceprovider.domaindoc.config.ServiceProviderConfig.class,
		com.samples.platform.serviceprovider.techsupport.flow.config.ServiceProviderConfig.class,
		com.samples.platform.core.config.CoreSpringConfig.class

})
@ComponentScan("com.samples.platform.service")
@EnableWebMvc
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebappSpringConfig extends WebMvcConfigurerAdapter {
	/**
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureDefaultServletHandling(org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer)
	 */
	@Override
	public void configureDefaultServletHandling(
			final DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * @return the {@link InternalResourceViewResolver}.
	 */
	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/pages/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	/**
	 * Get the {@link ServiceExecutionLogAspect} bean.
	 *
	 * @return the {@link ServiceExecutionLogAspect} bean.
	 */
	@Bean
	public ServiceExecutionLogAspect getServiceExecutionLogAspect() {
		ServiceExecutionLogAspect bean = new ServiceExecutionLogAspect();
		return bean;
	}

	/**
	 * Get the {@link ServiceExecutionLogAspect} bean.
	 *
	 * @return the {@link ServiceExecutionLogAspect} bean.
	 */
	@Bean(name = "comSamplesPlatformCommonRouterAggregatorGetReferenceData")
	public AggregatorGetReferenceData getAggregatorGetReferenceData() {
		AggregatorGetReferenceData bean = new AggregatorGetReferenceData();
		return bean;
	}

	/**
	 * Get the {@link SystemUserLogFlowGateway} bean.
	 *
	 * @return the {@link SystemUserLogFlowGateway} bean.
	 */
	@Bean
	public SystemUserLogFlowGateway getSystemUserLogFlowGateway() {
		SystemUserLogFlowGateway bean = new SystemUserLogFlowGatewayImpl();
		return bean;
	}

	/**
	 * Get the {@link OperationLogProvider} bean.
	 *
	 * @return the {@link OperationLogProvider} bean.
	 */
	@Bean
	public OperationLogProvider getOperationLogProvider() {
		OperationLogProvider bean = new OperationLogProvider();
		return bean;
	}
}
