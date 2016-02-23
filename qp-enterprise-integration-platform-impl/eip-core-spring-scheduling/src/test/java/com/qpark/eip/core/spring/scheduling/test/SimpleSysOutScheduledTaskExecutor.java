package com.qpark.eip.core.spring.scheduling.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.spring.scheduling.AbstractScheduledTaskExecutorBean;

/**
 * @author bhausen
 */
public class SimpleSysOutScheduledTaskExecutor
		extends AbstractScheduledTaskExecutorBean {
	/** The {@link SimpleDateFormat}. */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SimpleSysOutScheduledTaskExecutor.class);

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.logger.debug(String
				.format("%s - %s, %s, %s, %s", this.sdf.format(new Date()),
						this.scheduledTaskDescription.getId(),
						this.scheduledTaskDescription.getName(),
						String.valueOf(this.scheduledTaskDescription
								.getCronExpression()),
				String.valueOf(
						this.scheduledTaskDescription.getFixedDelaySeconds())));
	}
}
