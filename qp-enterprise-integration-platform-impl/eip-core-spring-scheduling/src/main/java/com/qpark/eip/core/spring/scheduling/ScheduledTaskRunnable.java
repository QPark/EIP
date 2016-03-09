package com.qpark.eip.core.spring.scheduling;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.spring.scheduling.annotation.ScheduledTaskMethod;

/**
 * The bean contains the method of the annotated with {@link ScheduledTaskMethod}.
 * This method will be called from {@link #run()}.
 *
 * @author bhausen
 */
class ScheduledTaskRunnable implements Runnable {
	/** The {@link org.slf4j.Logger}. */
	private static Logger logger = LoggerFactory
			.getLogger(ScheduledTaskRunnable.class);
	/** Empty object [] as method calling parameters. */
	private static Object[] PARAMS = new Object[0];

	/**
	 * Get the scheduled method.
	 *
	 * @param clazz
	 * @return the method annotated with {@link ScheduledTaskMethod} or null.
	 */
	private static Method getScheduledMethod(final Class<?> clazz) {
		logger.trace("+getScheduledMethod {}", clazz.getName());
		Method value = null;
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameters().length == 0
					&& method.isAnnotationPresent(ScheduledTaskMethod.class)) {
				value = method;
				break;
			}
		}
		logger.trace("-getScheduledMethod {} {}", clazz.getName(),
				value == null ? "null" : value.getName());
		return value;
	}

	/**
	 * The bean containing having the method annotated with
	 * {@link ScheduledTaskMethod}.
	 */
	private Object bean;

	/** The {@link ScheduledTaskDescription}. */
	private ScheduledTaskDescription description;
	/** This {@link ScheduledTaskRunnable} is enabled to run. */
	private boolean enabled;
	/** The method annotated with {@link ScheduledTaskMethod} to execute. */
	private Method method;
	public ScheduledTaskRunnable(final ScheduledTaskDescription description,
			final Object bean) {
		this.description = description;
		logger.debug("{} ({}/{}): {} {}", this.description.getId(),
				this.description.getName(), description.getBeanName(),
				String.valueOf(this.description.getCronExpression()),
				String.valueOf(this.description.getFixedDelaySeconds()));
		this.bean = bean;
		if (this.bean != null) {
			this.method = getScheduledMethod(this.bean.getClass());
		}
		if (this.description.isEnabled() && this.method != null) {
			this.enabled = true;
		} else {
			this.enabled = false;
		}
	}

	/** Disables the {@link ScheduledTaskRunnable} to run. */
	void disable() {
		this.enabled = false;
		this.method = null;
		this.bean = null;
	}

	/**
	 * @return the description
	 */
	public ScheduledTaskDescription getDescription() {
		return this.description;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Runs the method annotated with {@link ScheduledTaskMethod}.
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (this.enabled) {
			logger.debug("+run {} ({}/{}): {} {}", this.description.getId(),
					this.description.getName(), this.description.getBeanName(),
					String.valueOf(this.description.getCronExpression()),
					String.valueOf(this.description.getFixedDelaySeconds()));
			try {
				this.method.invoke(this.bean, PARAMS);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				logger.debug("-run {} {}", this.description.getName());
			}
		}
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final Object bean) {
		this.bean = bean;
	}
}
