/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
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
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.statistics.AsyncFlowLogMessagePersistence;
import com.qpark.eip.core.spring.statistics.FlowExecutionLog;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.core.spring.statistics.dao.StatisticsEraser;
import com.qpark.eip.core.spring.statistics.dao.StatisticsLoggingDao;
import com.qpark.eip.core.spring.statistics.impl.AppUserStatisticsChannelAdapter;
import com.qpark.eip.core.spring.statistics.impl.AsyncFlowLogMessagePersistenceImpl;
import com.qpark.eip.core.spring.statistics.impl.SysUserStatisticsChannelInvocationListener;

/**
 * Provides the spring config of the eip core statistics. Requires a
 * {@link MessageContentProvider} with name
 * {@value #STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME} in the spring context
 * deployed.
 *
 * @author bhausen
 */
@Configuration
@Import({ EipPersistenceConfig.class })
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class EipStatisticsConfig {
	/** The {@link org.slf4j.Logger} for the application user statistics. */
	public static Logger LOGGER_STATISTICS_APP_USER = LoggerFactory.getLogger(
			"com.qpark.eip.core.spring.statistics.statistics.AppUserStats");
	/** The {@link org.slf4j.Logger} for the system user statistics. */
	public static Logger LOGGER_STATISTICS_SYS_USER = LoggerFactory.getLogger(
			"com.qpark.eip.core.spring.statistics.statistics.SysUserStats");
	/** The name of the statistics message content provider bean. */
	public static final String STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME = "ComQparkEipCoreSpringStatisticsMessageContentProvider";
	/** The context name of the eip core authority. */
	private String contextName;
	/** The version of the context. */
	private String contextVersion;
	/** The number of weeks to keep the log entries in the database. */
	private int numberOfWeeksToKeepLogs = 2;
	/** The {@link MessageContentProvider} of the statistics. */
	@Autowired
	@Qualifier(STATISTICS_MESSAGE_CONTENT_PROVIDER_BEAN_NAME)
	private MessageContentProvider messageContentProvider;

	/**
	 * Create the spring config of the eip core statistics with 2 weeks keeping
	 * the logs.
	 */
	public EipStatisticsConfig() {
	}

	/**
	 * Get the {@link StatisticsChannelAdapter} bean.
	 *
	 * @return the {@link StatisticsChannelAdapter} bean.
	 */
	@Bean
	public AppUserStatisticsChannelAdapter getAppUserStatisticsChannelAdapter() {
		AppUserStatisticsChannelAdapter bean = new AppUserStatisticsChannelAdapter();
		return bean;
	}

	/**
	 * Get the {@link AsyncFlowLogMessagePersistenceImpl} bean.
	 *
	 * @return the {@link AsyncFlowLogMessagePersistenceImpl} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsAsyncFlowLogMessagePersistence")
	public AsyncFlowLogMessagePersistence getAsyncFlowLogMessagePersistence() {
		AsyncFlowLogMessagePersistence bean = new AsyncFlowLogMessagePersistenceImpl();
		return bean;
	}

	/**
	 * Get the {@link SysUserStatisticsChannelInvocationListener} bean.
	 *
	 * @return the {@link SysUserStatisticsChannelInvocationListener} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsSysUserStatisticsChannelInvocationListener")
	public SysUserStatisticsChannelInvocationListener getBusChannelInvocationListener() {
		SysUserStatisticsChannelInvocationListener bean = new SysUserStatisticsChannelInvocationListener();
		return bean;
	}

	/**
	 * Get the {@link ContextNameProvider} bean.
	 *
	 * @return the {@link ContextNameProvider} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsContextNameProvider")
	public ContextNameProvider getContextNameProvider() {
		ContextNameProvider bean = new ContextNameProvider();
		bean.setContextName(this.contextName);
		bean.setContextVersion(this.contextVersion);
		return bean;
	}

	/**
	 * Get the {@link FlowExecutionLogAspect} bean.
	 *
	 * @return the {@link FlowExecutionLogAspect} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsFlowExecutionLog")
	public FlowExecutionLog getFlowExecutionLog() {
		FlowExecutionLog bean = new FlowExecutionLog();
		return bean;
	}

	/**
	 * Get the {@link FlowLogMessageDao} bean.
	 *
	 * @return the {@link FlowLogMessageDao} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsLoggingDao")
	public StatisticsLoggingDao getStatisticsLoggingDao() {
		StatisticsLoggingDao bean = new StatisticsLoggingDao();
		return bean;
	}

	/**
	 * Get the {@link StatisticsEraser} bean.
	 *
	 * @return the {@link StatisticsEraser} bean.
	 */
	@Bean
	public StatisticsEraser getSystemUserLogEraser() {
		StatisticsEraser bean = new StatisticsEraser();
		bean.setNumberOfWeeksToKeepLogs(this.numberOfWeeksToKeepLogs);
		return bean;
	}

	/**
	 * Set the context name.
	 *
	 * @param contextName
	 *            the context name.
	 */
	public void setContextName(final String contextName) {
		this.contextName = contextName;
	}

	/**
	 * Get the context name.
	 *
	 * @return the context definition.
	 */
	public String getContextDefinition() {
		return String.format("%s:%s", this.contextName, this.contextVersion);
	}

	/**
	 * Set the context version.
	 *
	 * @param contextVersion
	 *            the context version.
	 */
	public void setContextVersion(final String contextVersion) {
		this.contextVersion = contextVersion;
	}

	/**
	 * Set the number of weeks to keep the log entries in the database.
	 *
	 * @param numberOfWeeksToKeepLogs
	 *            the number of weeks to keep the log entries in the database.
	 */
	public void setNumberOfWeeksToKeepLogs(final int numberOfWeeksToKeepLogs) {
		this.numberOfWeeksToKeepLogs = numberOfWeeksToKeepLogs;
	}

}
