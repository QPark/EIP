package com.qpark.eip.core.spring.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import com.qpark.eip.core.ReInitalizeable;

/**
 * Schedules tasks and does a reconfiguration if the {@link #reInitalize()}
 * method is called.
 *
 * @author bhausen
 */
public class EipSchedulingConfigurer implements SchedulingConfigurer,
		ReInitalizeable, ApplicationContextAware {
	/** The {@link ApplicationContext}. */
	private ApplicationContext applicationContext;
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(EipSchedulingConfigurer.class);
	/** The {@link ScheduledTaskDao}. */
	@Autowired
	private ScheduledTaskDao scheduledTaskDao;
	/** The {@link ScheduledTaskRegistrar}. */
	private ScheduledTaskRegistrar taskRegistrar;

	/**
	 * @see org.springframework.scheduling.annotation.SchedulingConfigurer#configureTasks(org.springframework.scheduling.config.ScheduledTaskRegistrar)
	 */
	@Override
	public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
		this.logger.debug("+configureTasks");
		this.taskRegistrar = taskRegistrar;
		List<ScheduledTaskDescription> scheduleTaskDescriptions = this.scheduledTaskDao
				.getScheduleTaskDescriptions();
		if (scheduleTaskDescriptions != null) {
			this.logger.debug(" configureTasks got {} scheduled tasks",
					scheduleTaskDescriptions.size());
			AbstractScheduledTaskExecutorBean scheduledTaskExcutor;
			Trigger trigger;
			for (ScheduledTaskDescription scheduledTaskDescription : scheduleTaskDescriptions) {
				if (scheduledTaskDescription.isEnabled()) {
					this.logger
							.debug(" configureTasks ENABLED scheduled task {}({}) '{}'/'{}'",
									scheduledTaskDescription.getId(),
									scheduledTaskDescription.getName(),
									scheduledTaskDescription
											.getCronExpression(),
							scheduledTaskDescription.getFixedDelaySeconds());
					try {
						scheduledTaskExcutor = this.applicationContext.getBean(
								scheduledTaskDescription.getExecutor());
						scheduledTaskExcutor.setScheduledTaskDescription(
								scheduledTaskDescription);
						trigger = this.getTrigger(scheduledTaskDescription);
						if (trigger != null) {
							taskRegistrar.addTriggerTask(scheduledTaskExcutor,
									trigger);
							this.logger.debug(" configureTasks got added");
						} else {
							this.logger
									.debug(" configureTasks trigger is null!");
						}
					} catch (Exception e) {
						this.logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				} else {
					this.logger
							.debug(" configureTasks DISABLED scheduled task {}({}) '{}'/'{}'",
									scheduledTaskDescription.getId(),
									scheduledTaskDescription.getName(),
									scheduledTaskDescription
											.getCronExpression(),
							scheduledTaskDescription.getFixedDelaySeconds());
				}
			}
		} else {
			this.logger.debug(" configureTasks scheduled tasks not configured");
		}

		this.logger.debug("-configureTasks");
	}

	/**
	 * Get the {@link Trigger}.
	 *
	 * @param scheduledTaskDescription
	 *            the {@link ScheduledTaskDescription}.
	 * @return the {@link Trigger}.
	 */
	private Trigger getTrigger(
			final ScheduledTaskDescription scheduledTaskDescription) {
		Trigger trigger = null;
		if (scheduledTaskDescription.getCronExpression() != null
				&& scheduledTaskDescription.getCronExpression().trim()
						.length() > 0) {
			try {
				trigger = new CronTrigger(
						scheduledTaskDescription.getCronExpression());
			} catch (IllegalArgumentException e) {
				this.logger.error(e.getMessage(), e);
			}
		} else if (scheduledTaskDescription.getFixedDelaySeconds() > 0) {
			trigger = new PeriodicTrigger(
					scheduledTaskDescription.getFixedDelaySeconds(),
					TimeUnit.SECONDS);
		}
		return trigger;
	}

	/**
	 * @see com.qpark.eip.core.ReInitalizeable#reInitalize()
	 */
	@Override
	public void reInitalize() {
		this.logger.debug("+reInitalize");
		this.taskRegistrar.setTriggerTasksList(new ArrayList<TriggerTask>(0));
		// this.taskRegistrar.destroy();
		Map<String, ScheduledAnnotationBeanPostProcessor> postProcessorBeanMap = this.applicationContext
				.getBeansOfType(ScheduledAnnotationBeanPostProcessor.class);
		for (ScheduledAnnotationBeanPostProcessor postProcessor : postProcessorBeanMap
				.values()) {
			postProcessor.onApplicationEvent(
					new ContextRefreshedEvent(this.applicationContext));
		}
		this.logger.debug("+reInitalize");
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
