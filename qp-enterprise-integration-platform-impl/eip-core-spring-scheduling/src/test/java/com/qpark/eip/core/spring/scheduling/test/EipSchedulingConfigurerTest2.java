package com.qpark.eip.core.spring.scheduling.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.qpark.eip.core.spring.scheduling.EipSchedulingConfigurer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class },
		loader = AnnotationConfigContextLoader.class)
public class EipSchedulingConfigurerTest2 {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(EipSchedulingConfigurerTest2.class);

	@Autowired
	private EipSchedulingConfigurer eipSchedulingConfigurer;

	@Test
	public void x() {
		this.logger.debug("test");
		// this.eipSchedulingConfigurer.reInitalize();
		// this.eipSchedulingConfigurer.reInitalize();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
	}
}
