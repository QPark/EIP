package com.qpark.eip.core.spring.scheduling.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.spring.scheduling.annotation.ScheduledTaskMethod;

/**
 * @author bhausen
 */
public class SimpleSysOutScheduledMethodContainer {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SimpleSysOutScheduledMethodContainer.class);
	/** The {@link SimpleDateFormat}. */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");

	/**
	 * Does some stuff - printing execution date.
	 */
	@ScheduledTaskMethod
	public void doSomeStuff() {
		this.logger.debug(
				String.format("running at %s", this.sdf.format(new Date())));
	}
}
