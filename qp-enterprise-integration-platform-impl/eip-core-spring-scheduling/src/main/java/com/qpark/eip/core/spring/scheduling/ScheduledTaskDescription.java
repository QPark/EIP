package com.qpark.eip.core.spring.scheduling;

public class ScheduledTaskDescription {
	private String beanName;
	private String cronExpression;
	private boolean enabled;
	private long fixedDelay;
	private String id;
	private String name;

	/**
	 * @return the className
	 */
	public String getBeanName() {
		return this.beanName;
	}

	/**
	 * @return the cronExpression
	 */
	public String getCronExpression() {
		return this.cronExpression;
	}

	/**
	 * @return the fixedDelay
	 */
	public long getFixedDelaySeconds() {
		return this.fixedDelay;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setBeanName(final String className) {
		this.beanName = className;
	}

	/**
	 * @param cronExpression
	 *            the cronExpression to set
	 */
	public void setCronExpression(final String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param fixedDelay
	 *            the fixedDelay to set
	 */
	public void setFixedDelay(final long fixedDelay) {
		this.fixedDelay = fixedDelay;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
}
