package com.qpark.eip.core.spring.scheduling;

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
	/** The factory to create {@link ScheduledTaskRunnable}s. */
	@Autowired
	private ScheduledTaskFactory scheduledTaskFactory;

	/**
	 * @see org.springframework.scheduling.annotation.SchedulingConfigurer#configureTasks(org.springframework.scheduling.config.ScheduledTaskRegistrar)
	 */
	@Override
	public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
		this.logger.debug("+configureTasks");
		List<ScheduledTaskDescription> descriptions = this.scheduledTaskDao
				.getScheduleTaskDescriptions();
		if (descriptions != null) {
			this.logger.debug(" configureTasks got {} scheduled tasks",
					descriptions.size());
			List<ScheduledTaskRunnable> runners = this.scheduledTaskFactory
					.getInstances(descriptions);
			Trigger trigger;
			for (ScheduledTaskRunnable runner : runners) {
				try {
					trigger = this.getTrigger(runner.getDescription());
					if (trigger != null) {
						taskRegistrar.addTriggerTask(runner, trigger);
						this.logger.debug(" configureTasks got added");
					} else {
						this.logger.debug(" configureTasks trigger is null!");
					}
				} catch (Exception e) {
					this.logger.error(e.getMessage(), e);
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
	 * @param description
	 *            the {@link ScheduledTaskDescription}.
	 * @return the {@link Trigger}.
	 */
	private Trigger getTrigger(final ScheduledTaskDescription description) {
		Trigger trigger = null;
		if (description.getCronExpression() != null
				&& description.getCronExpression().trim().length() > 0) {
			try {
				trigger = new CronTrigger(description.getCronExpression());
			} catch (IllegalArgumentException e) {
				this.logger.error(e.getMessage(), e);
			}
		} else if (description.getFixedDelaySeconds() > 0) {
			trigger = new PeriodicTrigger(description.getFixedDelaySeconds(),
					TimeUnit.SECONDS);
		}
		return trigger;
	}

	/**
	 * Force the {@link ScheduledTaskRegistrar} to be configured again.
	 *
	 * @see com.qpark.eip.core.ReInitalizeable#reInitalize()
	 */
	@Override
	public void reInitalize() {
		this.logger.debug("+reInitalize");
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
