package com.qpark.eip.core.spring.scheduling.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.spring.scheduling.ScheduledTaskDao;
import com.qpark.eip.core.spring.scheduling.config.EipSchedulingConfig;

@Configuration
@Import(value = { EipSchedulingConfig.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class TestConfig {
	public static final String SCHEDULED_METHOD_CONTAINER_BEAN_NAME = "SimpleSysOutScheduledMethodContainer";

	/**
	 * Get the {@link ScheduledTaskDao}.
	 *
	 * @return the {@link ScheduledTaskDao}.
	 */
	@Bean
	public ScheduledTaskDao getScheduledTaskDao() {
		ScheduledTaskDao bean = new ScheduledTaskDaoMock();
		return bean;
	}

	/**
	 * Get the {@link SimpleSysOutScheduledMethodContainer}.
	 *
	 * @return the {@link SimpleSysOutScheduledMethodContainer}.
	 */
	@Bean(name = SCHEDULED_METHOD_CONTAINER_BEAN_NAME)
	public SimpleSysOutScheduledMethodContainer getSimpleSysOutScheduledTaskExecutor() {
		SimpleSysOutScheduledMethodContainer bean = new SimpleSysOutScheduledMethodContainer();
		return bean;
	}

}
