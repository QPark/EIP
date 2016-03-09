/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.scheduling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.spring.scheduling.EipSchedulingConfigurer;
import com.qpark.eip.core.spring.scheduling.ScheduledTaskFactory;

/**
 * Provides {@link EipSchedulingConfigurer} to initialize and reinitialize the
 * scheduled tasks.
 *
 * @author bhausen
 */
@Configuration
@EnableScheduling
public class EipSchedulingConfig {
	/**
	 * Get the {@link EipSchedulingConfigurer}.
	 *
	 * @return the {@link EipSchedulingConfigurer}.
	 */
	@Bean
	public EipSchedulingConfigurer getEipSchedulingConfigurer() {
		EipSchedulingConfigurer bean = new EipSchedulingConfigurer();
		return bean;
	}

	@Bean
	public ScheduledTaskFactory getScheduledTaskFactory() {
		ScheduledTaskFactory bean = new ScheduledTaskFactory();
		return bean;
	}
}
