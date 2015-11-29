/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.techsupport.flow.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.spring.statistics.AsyncFlowLogMessagePersistence;
import com.qpark.eip.core.spring.statistics.FlowExecutionLog;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.samples.platform.core.SamplesMessageContentProvider;
import com.samples.platform.core.flow.SystemUserLogFlowGateway;
import com.samples.platform.serviceprovider.techsupport.flow.config.ServiceProviderConfig;

/**
 * Test spring configuration.
 *
 * @author bhausen
 */
@Configuration
@Import(value = { ServiceProviderConfig.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestConfig {
	/**
	 * Log the flow execution in the log file
	 *
	 * @return the {@link FlowExecutionLog}
	 */
	@Bean
	public FlowExecutionLog getFlowExecutionLog() {
		FlowExecutionLog bean = new FlowExecutionLog();
		return bean;
	}

	/**
	 * Get the {@link SamplesMessageContentProvider}.
	 *
	 * @return the {@link MessageContentProvider} implementation.
	 */
	@Bean
	public MessageContentProvider getMessageContentProvider() {
		MessageContentProvider bean = new SamplesMessageContentProvider();
		return bean;
	}

	/**
	 * Show what {@link FlowLogMessageType}s would be saved in the database.
	 *
	 * @return the {@link AsyncFlowLogMessagePersistence} mock implementation.
	 */
	@Bean
	public AsyncFlowLogMessagePersistence getAsyncFlowLogMessagePersistence() {
		AsyncFlowLogMessagePersistence bean = new MockFlowLogMessagePersistence();
		return bean;
	}

	/**
	 * Get the testing {@link SystemUserLogFlowGateway}.
	 *
	 * @return the {@link SystemUserLogFlowGateway} mock implementation.
	 */
	@Bean
	public SystemUserLogFlowGateway getSystemUserLogFlowGateway() {
		MockFlowGateway bean = new MockFlowGateway();
		return bean;
	}
}
