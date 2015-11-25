package com.qpark.eip.core.spring.statistics.config;

import java.lang.reflect.Constructor;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.statistics.AppUserStatisticsChannelAdapter;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.core.spring.statistics.SysUserStatisticsChannelInvocationListener;
import com.qpark.eip.core.spring.statistics.dao.StatisticsEraser;
import com.qpark.eip.core.spring.statistics.dao.StatisticsLoggingDao;
import com.qpark.eip.core.spring.statistics.flow.AsyncFlowLogMessagePersistence;
import com.qpark.eip.core.spring.statistics.flow.FlowExecutionLogAspect;

/**
 * Provides the spring config of the eip core authority.
 *
 * @author bhausen
 */
@Configuration
@EnableScheduling
@Import({ EipPersistenceConfig.class })
public class EipStatisticsConfig {
	/** The {@link org.slf4j.Logger} for the application user statistics. */
	public static Logger LOGGER_STATISTICS_APP_USER = LoggerFactory.getLogger(
			"com.qpark.eip.core.spring.statistics.statistics.AppUserStats");
	/** The {@link org.slf4j.Logger} for the system user statistics. */
	public static Logger LOGGER_STATISTICS_SYS_USER = LoggerFactory.getLogger(
			"com.qpark.eip.core.spring.statistics.statistics.SysUserStats");
	/** The context name of the eip core authority. */
	private String contextName;
	/** The version of the context. */
	private String contextVersion;
	/** The {@link EipPersistenceConfig}. */
	@Autowired
	private EipPersistenceConfig eipPersistenceConfig;
	/** The class name of the of the {@link MessageContentProvider}. */
	private String messageContentProviderClassName;
	/** The number of weeks to keep the log entries in the database. */
	private int numberOfWeeksToKeepLogs = 2;

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
	 * Get the {@link AsyncFlowLogMessagePersistence} bean.
	 *
	 * @return the {@link AsyncFlowLogMessagePersistence} bean.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsAsyncFlowLogMessagePersistence")
	public AsyncFlowLogMessagePersistence getAsyncFlowLogMessagePersistence() {
		AsyncFlowLogMessagePersistence bean = new AsyncFlowLogMessagePersistence();
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
	 * The {@link EipStatisticsConfig} itself.
	 *
	 * @return the {@link EipStatisticsConfig}.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsConfig")
	public EipStatisticsConfig getBusUtilAuthorityConfig() {
		return this;
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
	@Bean(name = "ComQparkEipCoreSpringStatisticsFlowExecutionLogAspect")
	public FlowExecutionLogAspect getFlowLoggingAspect() {
		FlowExecutionLogAspect bean = new FlowExecutionLogAspect();
		return bean;
	}

	/**
	 * Get the {@link MessageContentProvider} bean.
	 *
	 * @return the {@link MessageContentProvider} implemented by
	 *         {@link #messageContentProviderClassName}.
	 */
	@Bean(name = "ComQparkEipCoreSpringStatisticsMessageContentProvider")
	public MessageContentProvider getMessageContentProvider() {
		MessageContentProvider bean = null;
		try {
			Class<?> clazz = Class
					.forName(this.messageContentProviderClassName);
			Constructor<?> ctor = clazz.getConstructor(new Class<?>[0]);
			bean = (MessageContentProvider) ctor.newInstance(new Object[0]);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
	 * Set the JPA vendor adapter settings for {@link EipPersistenceConfig}.
	 */
	@PostConstruct
	private void init() {
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
	 * Set the context version.
	 *
	 * @param contextVersion
	 *            the context version.
	 */
	public void setContextVersion(final String contextVersion) {
		this.contextVersion = contextVersion;
	}

	/**
	 * Set the datasource name in the JNDI context.
	 *
	 * @param dataSourceJndiName
	 *            the datasource name in the JNDI context.
	 */
	public void setDataSourceJndiName(final String dataSourceJndiName) {
		this.eipPersistenceConfig.setDataSourceJndiName(dataSourceJndiName);
	}

	/**
	 * Set the jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 *
	 * @param jpaVendorAdapterClassName
	 *            the jpa Vendor adapter class name to be set in the
	 *            {@link LocalContainerEntityManagerFactoryBean}.
	 */
	public void setJpaVendorAdapterClassName(
			final String jpaVendorAdapterClassName) {
		this.eipPersistenceConfig
				.setJpaVendorAdapterClassName(jpaVendorAdapterClassName);
	}

	/**
	 * Set the database platform to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 *
	 * @param jpaVendorAdpaterDatabasePlatform
	 *            the database platform to be set into the
	 *            {@link AbstractJpaVendorAdapter}.
	 */
	public void setJpaVendorAdpaterDatabasePlatform(
			final String jpaVendorAdpaterDatabasePlatform) {
		this.eipPersistenceConfig.setJpaVendorAdpaterDatabasePlatform(
				jpaVendorAdpaterDatabasePlatform);
	}

	/**
	 * Set the class name of the of the {@link MessageContentProvider}.
	 *
	 * @param messageContentProviderClassName
	 *            the class name of the of the {@link MessageContentProvider}.
	 */
	public void setMessageContentProviderClassName(
			final String messageContentProviderClassName) {
		this.messageContentProviderClassName = messageContentProviderClassName;
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
