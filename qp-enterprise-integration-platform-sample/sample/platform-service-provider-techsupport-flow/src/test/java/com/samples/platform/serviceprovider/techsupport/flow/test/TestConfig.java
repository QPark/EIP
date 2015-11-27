package com.samples.platform.serviceprovider.techsupport.flow.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.samples.platform.core.flow.SystemUserLogFlowGateway;
import com.samples.platform.serviceprovider.techsupport.flow.config.ServiceProviderConfig;

@Configuration
@Import(value = { ServiceProviderConfig.class })
public class TestConfig {
    @Bean
    public SystemUserLogFlowGateway getSystemUserLogFlowGateway() {
	SystemUserLogFlowGateway bean = new FlowGatewayMockImpl();
	return bean;
    }

    @Bean
    public FlowExecutionLog getFlowExecutionLog() {
	FlowExecutionLog bean = new FlowExecutionLog();
	return bean;
    }
}
