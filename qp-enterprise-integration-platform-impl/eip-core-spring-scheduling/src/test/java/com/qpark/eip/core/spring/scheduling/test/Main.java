package com.qpark.eip.core.spring.scheduling.test;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class Main {
	public static void main(final String[] args) {
		ConfigurableApplicationContext context = SpringApplication
				.run(TestConfig.class, args);
		EipSchedulingConfigurerTest bean = context
				.getBean(EipSchedulingConfigurerTest.class);
		bean.x();
	}
}
