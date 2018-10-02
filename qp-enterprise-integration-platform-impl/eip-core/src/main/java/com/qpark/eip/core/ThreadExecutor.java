/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhausen
 */
public class ThreadExecutor extends ReportingThreadPoolProvider {
	/**
	 * The default thread factory
	 */
	static class ExecutorThreadFactory implements ThreadFactory {
		/** The {@link ThreadGroup}. */
		private final ThreadGroup group;
		/** The name prefix of the pool. */
		private final String namePrefix;
		/** The thread number counter. */
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		/** Constructor. */
		ExecutorThreadFactory(final String namePrefix) {
			SecurityManager s = System.getSecurityManager();
			this.group = (s != null) ? s.getThreadGroup()
					: Thread.currentThread().getThreadGroup();
			this.namePrefix = namePrefix == null
					|| namePrefix.trim().length() == 0 ? "Executor-pool-thread-"
							: namePrefix;
		}

		/**
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(final Runnable r) {
			Thread t = new Thread(this.group, r,
					this.namePrefix + this.threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) {
				t.setDaemon(false);
			}
			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
	}

	/**
	 * @param callables the {@link Callable}s to execute
	 * @param pool      the {@link Thread} pool.
	 * @param logger    the logger to use.
	 */
	protected static <T> List<Future<T>> executeThreads(
			final List<Callable<T>> callables,
			final ReportingThreadPoolExecutor pool, final Logger logger) {
		List<Future<T>> value = new ArrayList<>(callables.size());
		try {
			logger.info(
					"executeThreads adding #{} callables to pool #max {}, #active {}, #queued {}",
					callables.size(), pool.getMaximumPoolSize(),
					pool.getActiveCount(), pool.getQueue().size());
			/* Submit the Callable and add the Future to the list. */
			callables.stream().forEach(c -> value.add(pool.submit(c)));
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
		}
		return value;
	}

	/**
	 * @param callables the {@link Callable}s to execute
	 * @param pool      the {@link Thread} pool.
	 * @param logger    the logger to use.
	 */
	protected static List<Future<?>> executeThreadsx(
			final List<Callable<?>> callables,
			final ReportingThreadPoolExecutor pool, final Logger logger) {
		List<Future<?>> value = new ArrayList<>(callables.size());
		try {
			logger.info(
					"executeThreads adding #{} callables to pool #max {}, #active {}, #queued {}",
					callables.size(), pool.getMaximumPoolSize(),
					pool.getActiveCount(), pool.getQueue().size());
			/* Submit the Callable and add the Future to the list. */
			callables.stream().forEach(c -> value.add(pool.submit(c)));
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
		}
		return value;
	}

	/**
	 * Split the input list into partition lists of partitionSize.
	 *
	 * @param list          the input list.
	 * @param partitionSize the size of the partitions.
	 * @return the list of partition lists.
	 */
	public static <T> List<List<T>> partition(final List<T> list,
			final int partitionSize) {
		return IntStream
				.range(0, (list.size() + partitionSize - 1) / partitionSize)
				.mapToObj(i -> list.subList(i * partitionSize,
						Math.min((i + 1) * partitionSize, list.size())))
				.collect(Collectors.toList());
	}

	/** Indicator if the spring application context is loaded well. */
	private boolean initialized;

	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(ThreadExecutor.class);
	/** The size of the {@link Thread} pool. */
	private int poolSize = 30;

	/**
	 * Create the {@link ThreadExecutor} with a pool size of 30 {@link Thread}s.
	 */
	public ThreadExecutor() {
		this(30);
	}

	/**
	 * Create the {@link ThreadExecutor}.
	 *
	 * @param poolSize the pool size.
	 */
	public ThreadExecutor(final int poolSize) {
		this.poolSize = poolSize;
	}

	/**
	 * Execute the {@link Callable}s
	 *
	 * @param callable      the list of {@link Callable}s to execute
	 * @param waitForResult <code>true</code>, if the return waits for the
	 *                      result.
	 * @param userName      the user name.
	 * @return the list of {@link Future}s.
	 */
	public <T> Future<T> executeCallable(final Callable<T> callable,
			final boolean waitForResult, final String userName) {
		List<Future<T>> futureList = this.executeCallables(
				Arrays.asList(callable), waitForResult, -1, userName);
		return futureList.get(0);
	}

	/**
	 * Execute the {@link Callable}s
	 *
	 * @param callables     the list of {@link Callable}s to execute
	 * @param waitForResult <code>true</code>, if the return waits for the
	 *                      result.
	 * @param partitionSize the number of {@link Callable}s to pass to the
	 *                      executor at once. If <i>0</i> the
	 *                      {@link #getPoolSize()} will be taken.
	 * @param userName      the user name.
	 * @return the list of {@link Future}s.
	 */
	public <T> List<Future<T>> executeCallables(
			final List<Callable<T>> callables, final boolean waitForResult,
			final int partitionSize, final String userName) {
		List<Future<T>> value = new ArrayList<>();
		if (callables.size() > 0) {
			this.getLogger().debug("execute {} callables", callables.size());
			int internalPartitionSize = this.getPoolSize();
			if (partitionSize > 0) {
				internalPartitionSize = partitionSize;
			}
			partition(callables, internalPartitionSize).stream()
					.forEach(partition -> {
						value.addAll(executeThreads(partition, this.pool,
								this.getLogger()));
						if (waitForResult) {
							value.forEach(future -> {
								try {
									future.get();
								} catch (InterruptedException e) {
									this.getLogger().error(e.getMessage(), e);
								} catch (ExecutionException e) {
									this.getLogger().error(e.getMessage(), e);
								}
							});
						}
					});

		} else {
			this.getLogger().debug("execute 0 callables to execute");
		}
		return value;
	}

	/**
	 * Execute the {@link Callable}s
	 *
	 * @param callables the list of {@link Callable}s to execute
	 * @param userName  the user name.
	 * @return the list of {@link Future}s.
	 */
	public <T> List<Future<T>> executeCallables(
			final List<Callable<T>> callables, final String userName) {
		return this.executeCallables(callables, false, -1, userName);
	}

	/** @return the {@link org.slf4j.Logger}. */
	protected Logger getLogger() {
		return this.logger;
	}

	/**
	 * @return the number of free {@link Thread}s in the thread pool
	 *         (<i>poolSize - activeCount</i>).
	 */
	public int getNumberOfFreeThreads() {
		return this.getPoolSize() - this.pool.getActiveCount();
	}

	/**
	 * @return the number of free {@link Thread}s in the thread pool
	 *         (<i>poolSize - activeCount</i>).
	 */
	public int getNumberOfMaxThreads() {
		return this.getPoolSize();
	}

	/**
	 * @see com.qpark.eip.core.ReportingThreadPoolProvider#getPoolSize()
	 */
	@Override
	protected int getPoolSize() {
		return this.poolSize;
	}

	/**
	 * @param threadPoolNamePrefix
	 */
	public void initialize(final String threadPoolNamePrefix) {
		this.postConstruct();
		this.pool.setThreadFactory(
				new ExecutorThreadFactory(threadPoolNamePrefix));
		this.initialized = true;
	}

	/**
	 * @return <code>true</code>, if the
	 *         {@link ReportingThreadPoolExecutor#getActiveCount} <
	 *         {@link #getPoolSize()}. Else false
	 */
	protected boolean isExecutable() {
		int max = this.getPoolSize();
		int queued = this.pool.getQueue().size();
		int active = this.pool.getActiveCount();
		int theshold = max > 5 ? 2 : 0;
		boolean value = this.initialized && queued < max
				&& active < max - theshold;
		this.getLogger().debug(
				"isExecutable {}: pool #max {}, #active {}, #queued {}, #threshold {} [queued<max && active < max-threshold]",
				value, max, active, queued, theshold);
		return value;
	}

	/** Shutdown the thread pool. */
	public void shutdown() {
		this.preDestroy();
	}
}
