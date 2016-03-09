package com.qpark.eip.core.spring.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The factory to create {@link ScheduledTaskRunnable}s.
 *
 * @author bhausen
 */
public class ScheduledTaskFactory implements ApplicationContextAware {
	/** The {@link ApplicationContext}. */
	private ApplicationContext applicationContext;
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(ScheduledTaskFactory.class);

	private final List<ScheduledTaskRunnable> runners = new ArrayList<ScheduledTaskRunnable>();

	/**
	 * Get the instances of the {@link ScheduledTaskRunnable}s.
	 *
	 * @param descriptions
	 *            the {@link ScheduledTaskDescription}s.
	 * @return the list of new instances of the {@link ScheduledTaskRunnable} s.
	 */
	public List<ScheduledTaskRunnable> getInstances(
			final List<ScheduledTaskDescription> descriptions) {
		this.logger.debug("+getInstances {}", descriptions.size());
		for (ScheduledTaskRunnable r : this.runners) {
			r.disable();
		}
		List<ScheduledTaskRunnable> value = new ArrayList<ScheduledTaskRunnable>();
		ScheduledTaskRunnable runner;
		Object bean;
		for (ScheduledTaskDescription description : descriptions) {
			if (description.isEnabled()) {
				this.logger.debug(" getInstances ENABLED {}({}/{}) '{}'/'{}'",
						description.getId(), description.getName(),
						description.getBeanName(),
						description.getCronExpression(),
						description.getFixedDelaySeconds());
				bean = this.applicationContext
						.getBean(description.getBeanName());
				runner = new ScheduledTaskRunnable(description, bean);
				this.runners.add(runner);
				value.add(runner);
			} else {
				this.logger.debug(" getInstances DISABLED {}({}/{}) '{}'/'{}'",
						description.getId(), description.getName(),
						description.getBeanName(),
						description.getCronExpression(),
						description.getFixedDelaySeconds());
			}
		}
		this.logger.debug("-getInstances {}", descriptions.size());
		return value;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
