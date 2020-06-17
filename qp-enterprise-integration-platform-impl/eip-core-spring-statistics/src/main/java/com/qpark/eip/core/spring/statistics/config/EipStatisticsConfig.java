/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.statistics.AsyncFlowLogMessagePersistence;
import com.qpark.eip.core.spring.statistics.FlowExecutionLog;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.core.spring.statistics.StatisticsListener;
import com.qpark.eip.core.spring.statistics.dao.StatisticsLoggingDao;
import com.qpark.eip.core.spring.statistics.impl.AppUserStatisticsChannelAdapter;
import com.qpark.eip.core.spring.statistics.impl.AsyncFlowLogMessagePersistenceImpl;
import com.qpark.eip.core.spring.statistics.impl.SysUserStatisticsChannelInvocationListener;

/**
 * Provides the spring config of the eip core statistics. Requires a
 * {@link MessageContentProvider} with name
 * {@value #STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME} in the spring context
 * deployed. Requires a {@link ContextNameProvider} with name
 * {@value #CONTEXTNAME_PROVIDER_BEAN_NAME} in the spring context deployed.
 *
 * Be sure to have a {@link StatisticsListener} implemented and bound to the
 * spring context. The default implementation of the {@link StatisticsListener}
 * is the {@link StatisticsLoggingDao}.
 *
 * @author bhausen
 */
@Configuration
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SuppressWarnings("static-method")
public class EipStatisticsConfig {
	/** The bean name of the {@link ContextNameProvider}. */
	public static final String CONTEXTNAME_PROVIDER_BEAN_NAME = "ComQparkEipCoreSpringStatisticsContextNameProvider";
	/** The {@link org.slf4j.Logger} for the application user statistics. */
	public static Logger LOGGER_STATISTICS_APP_USER = LoggerFactory.getLogger(
			"com.qpark.eip.core.spring.statistics.statistics.AppUserStats");
	/** The {@link org.slf4j.Logger} for the system user statistics. */
	public static Logger LOGGER_STATISTICS_SYS_USER = LoggerFactory.getLogger(
			"com.qpark.eip.core.spring.statistics.statistics.SysUserStats");
	/** The name of the statistics message content provider bean. */
	public static final String STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME = "ComQparkEipCoreSpringStatisticsMessageContentProvider";
	/** The name of the statistics message async flow handler bean. */
	public static final String STATISTICS_ASYNC_FLOWLOG_HANDLER_BEAN_NAME = "ComQparkEipCoreSpringStatisticsAsyncFlowLogMessagePersistence";
	/** The name of the statistics channel invocation listener bean. */
	public static final String STATISTICS_CHANNEL_INVOCATION_LISTENER_BEAN_NAME = "ComQparkEipCoreSpringStatisticsSysUserStatisticsChannelInvocationListener";
	/** The name of the statistics channel invocation listener bean. */
	public static final String STATISTICS_FLOWLOG_ASPECT_BEAN_NAME = "ComQparkEipCoreSpringStatisticsFlowExecutionLog";
	/** The {@link ContextNameProvider}. */
	@Autowired
	@Qualifier(CONTEXTNAME_PROVIDER_BEAN_NAME)
	private ContextNameProvider contextNameProvider;
	/** The {@link MessageContentProvider} of the statistics. */
	@Autowired
	@Qualifier(STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME)
	private MessageContentProvider messageContentProvider;

	/**
	 * Get the {@link AppUserStatisticsChannelAdapter} bean.
	 *
	 * @return the {@link AppUserStatisticsChannelAdapter} bean.
	 */
	@Bean
	public AppUserStatisticsChannelAdapter getAppUserStatisticsChannelAdapter() {
		final AppUserStatisticsChannelAdapter bean = new AppUserStatisticsChannelAdapter();
		return bean;
	}

	/**
	 * Get the {@link AsyncFlowLogMessagePersistenceImpl} bean.
	 *
	 * @return the {@link AsyncFlowLogMessagePersistenceImpl} bean.
	 */
	@Bean(name = STATISTICS_ASYNC_FLOWLOG_HANDLER_BEAN_NAME)
	public AsyncFlowLogMessagePersistence getAsyncFlowLogMessagePersistence() {
		final AsyncFlowLogMessagePersistence bean = new AsyncFlowLogMessagePersistenceImpl();
		return bean;
	}

	/**
	 * Get the {@link SysUserStatisticsChannelInvocationListener} bean.
	 *
	 * @return the {@link SysUserStatisticsChannelInvocationListener} bean.
	 */
	@Bean(name = STATISTICS_CHANNEL_INVOCATION_LISTENER_BEAN_NAME)
	public SysUserStatisticsChannelInvocationListener getBusChannelInvocationListener() {
		final SysUserStatisticsChannelInvocationListener bean = new SysUserStatisticsChannelInvocationListener();
		return bean;
	}

	/**
	 * Get the context name.
	 *
	 * @return the context definition.
	 */
	public String getContextDefinition() {
		return String.format("%s:%s", this.contextNameProvider.getContextName(),
				this.contextNameProvider.getContextVersion());
	}

	/**
	 * Get the {@link FlowExecutionLog} bean.
	 *
	 * @return the {@link FlowExecutionLog} bean.
	 */
	@Bean(name = STATISTICS_FLOWLOG_ASPECT_BEAN_NAME)
	public FlowExecutionLog getFlowExecutionLog() {
		final FlowExecutionLog bean = new FlowExecutionLog();
		return bean;
	}
}
