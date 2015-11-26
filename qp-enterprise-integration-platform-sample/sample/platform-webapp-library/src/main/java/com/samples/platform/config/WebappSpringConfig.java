package com.samples.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.samples.platform.core.flow.SystemUserLogFlowGateway;
import com.samples.platform.util.AggregatorGetReferenceData;
import com.samples.platform.util.ServiceExecutionLogAspect;
import com.samples.platform.util.SystemUserLogFlowGatewayImpl;

/**
 * Provides the java spring config of the webapp.
 *
 * @author bhausen
 */
@Configuration
@Import(value = com.samples.platform.serviceprovider.techsupport.flow.config.ServiceProviderConfig.class)
@ComponentScan("com.samples.platform.service")
public class WebappSpringConfig {
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

	@Bean(name = "comSamplesPlatformCommonRouterAggregatorGetReferenceData")
	public AggregatorGetReferenceData getAggregatorGetReferenceData() {
		AggregatorGetReferenceData bean = new AggregatorGetReferenceData();
		return bean;
	}

	@Bean
	public SystemUserLogFlowGateway getSystemUserLogFlowGateway() {
		SystemUserLogFlowGateway bean = new SystemUserLogFlowGatewayImpl();
		return bean;
	}
}
