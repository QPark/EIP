package com.qpark.eip.core.spring.scheduling;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * The task executor bean contains the run method of the {@link Runnable} called
 * by the scheduler. This bean need to be of scope {@link Scope}
 * {@value ConfigurableBeanFactory#SCOPE_PROTOTYPE}.
 *
 * @author bhausen
 */
public abstract class AbstractScheduledTaskExecutorBean implements Runnable {
	/** The {@link ScheduledTaskDescription}. */
	protected ScheduledTaskDescription scheduledTaskDescription;

	/**
	 * Set the {@link ScheduledTaskDescription}.
	 *
	 * @param scheduledTaskDescription
	 *            the {@link ScheduledTaskDescription} to set.
	 */
	public void setScheduledTaskDescription(
			final ScheduledTaskDescription scheduledTaskDescription) {
		this.scheduledTaskDescription = scheduledTaskDescription;
	}
}
