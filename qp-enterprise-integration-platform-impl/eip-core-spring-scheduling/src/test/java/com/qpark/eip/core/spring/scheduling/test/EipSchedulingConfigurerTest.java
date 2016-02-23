package com.qpark.eip.core.spring.scheduling.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.spring.scheduling.EipSchedulingConfigurer;

public class EipSchedulingConfigurerTest {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(EipSchedulingConfigurerTest.class);

	@Autowired
	private EipSchedulingConfigurer eipSchedulingConfigurer;

	public void x() {
		this.logger.debug("test");
		this.eipSchedulingConfigurer.reInitalize();
		this.eipSchedulingConfigurer.reInitalize();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		this.eipSchedulingConfigurer.reInitalize();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		this.eipSchedulingConfigurer.reInitalize();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		this.eipSchedulingConfigurer.reInitalize();
	}
}
