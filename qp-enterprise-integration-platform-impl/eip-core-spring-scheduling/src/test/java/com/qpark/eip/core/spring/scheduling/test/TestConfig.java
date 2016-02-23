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

	@Bean
	public EipSchedulingConfigurerTest getEipSchedulingConfigurerTest() {
		EipSchedulingConfigurerTest bean = new EipSchedulingConfigurerTest();
		return bean;
	}

	/**
	 * Get the {@link SimpleSysOutScheduledTaskExecutor}.
	 *
	 * @return the {@link SimpleSysOutScheduledTaskExecutor}.
	 */
	@Bean
	public SimpleSysOutScheduledTaskExecutor getSimpleSysOutScheduledTaskExecutor() {
		SimpleSysOutScheduledTaskExecutor bean = new SimpleSysOutScheduledTaskExecutor();
		return bean;
	}

}
