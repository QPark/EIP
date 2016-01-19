/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.techsupport.flow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlow;
import com.samples.platform.serviceprovider.techsupport.flow.SystemUserReportFlowImpl;

/**
 * Provides the java spring config of the service provider.
 *
 * @author bhausen
 */
@Configuration
@ComponentScan(
		basePackages = { "com.qpark.eip.inf", "com.samples.platform.inf" })
public class ServiceProviderConfig {
	/**
	 * Get the {@link SystemUserReportFlow} bean.
	 *
	 * @return the {@link SystemUserReportFlow} bean.
	 */
	@Bean
	public SystemUserReportFlow getSystemUserReportFlow() {
		SystemUserReportFlow bean = new SystemUserReportFlowImpl();
		return bean;
	}
}
