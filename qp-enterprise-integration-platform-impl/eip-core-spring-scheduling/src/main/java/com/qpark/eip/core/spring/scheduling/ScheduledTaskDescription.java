package com.qpark.eip.core.spring.scheduling;

public class ScheduledTaskDescription {
	private Class<? extends AbstractScheduledTaskExecutorBean> executor;

	/**
	 * @return the executor
	 */
	public Class<? extends AbstractScheduledTaskExecutorBean> getExecutor() {
		return this.executor;
	}

	/**
	 * @param executor
	 *            the executor to set
	 */
	public void setExecutor(
			final Class<? extends AbstractScheduledTaskExecutorBean> executor) {
		this.executor = executor;
	}

	private String id;
	private String name;
	private boolean enabled;
	private String cronExpression;
	private long fixedDelay;

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the cronExpression
	 */
	public String getCronExpression() {
		return this.cronExpression;
	}

	/**
	 * @param cronExpression
	 *            the cronExpression to set
	 */
	public void setCronExpression(final String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * @return the fixedDelay
	 */
	public long getFixedDelaySeconds() {
		return this.fixedDelay;
	}

	/**
	 * @param fixedDelay
	 *            the fixedDelay to set
	 */
	public void setFixedDelay(final long fixedDelay) {
		this.fixedDelay = fixedDelay;
	}
}
