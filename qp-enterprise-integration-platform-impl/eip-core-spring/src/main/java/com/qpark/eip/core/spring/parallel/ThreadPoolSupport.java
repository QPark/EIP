/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.qpark.eip.core.ReportingThreadPoolProvider;

/**
 * @author bhausen
 */
public class ThreadPoolSupport extends ReportingThreadPoolProvider
		implements InitializingBean {
	/**
	 * The default thread factory
	 */
	static class PoolsThreadFactory implements ThreadFactory {
		/** The {@link ThreadGroup}. */
		private final ThreadGroup group;
		/** The name prefix of the pool. */
		private final String namePrefix;
		/** The thread number counter. */
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		/** Constructor. */
		PoolsThreadFactory(final String namePrefix) {
			SecurityManager s = System.getSecurityManager();
			this.group = (s != null) ? s.getThreadGroup()
					: Thread.currentThread().getThreadGroup();
			this.namePrefix = namePrefix;
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
	private Logger logger = LoggerFactory.getLogger(ThreadPoolSupport.class);
	/** The thread pool size. */
	private int size = 4;

	/** The thread pool name prefix. */
	private String threadPoolNamePrefix = "EIP-ThreadPool-thread-";

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.postConstruct();
		this.pool.setThreadFactory(
				new PoolsThreadFactory(this.getThreadPoolNamePrefix()));
		this.initialized = true;
	}

	/**
	 * Execute the {@link Callable}s
	 *
	 * @param callables     the list of {@link Callable}s to execute
	 * @param partitionSize the number of {@link Callable}s to pass to the
	 *                      executor at once.
	 * @return the list of {@link Future}s.
	 */
	public <T> List<Future<T>> executePartitioned(
			final List<Callable<T>> callables, final int partitionSize) {
		List<Future<T>> value = new ArrayList<>();
		if (callables.size() > 0) {
			if (partitionSize > 0 && partitionSize < this.getSize()) {
				List<List<Callable<T>>> partitions = partition(callables
						.stream().filter(callable -> Objects.nonNull(callable))
						.collect(Collectors.toList()), partitionSize);
				this.logger.info(
						" execute {} callables in {} partitions of size {}",
						callables.size(), partitions.size(), partitionSize);
				AtomicInteger partitionCount = new AtomicInteger(0);
				partitions.stream().forEach(partition -> {
					partitionCount.set(partitionCount.get() + 1);
					partition.stream().forEach(
							callable -> value.add(this.pool.submit(callable)));
					this.logger.info(" execute partition {} and wait",
							partitionCount.get());
					this.futureGet(value);
				});
			} else {
				this.execute(callables, true);
			}
		}
		return value;
	}

	/**
	 * Execute the {@link Callable}s
	 *
	 * @param callables     the list of {@link Callable}s to execute
	 * @param waitForResult <code>true</code>, if the return waits for the
	 *                      result.
	 * @return the list of {@link Future}s.
	 */
	public <T> List<Future<T>> execute(final List<Callable<T>> callables,
			final boolean waitForResult) {
		List<Future<T>> value = new ArrayList<>();
		if (callables.size() > 0) {
			this.logger.info(" execute {} callables", callables.size());
			callables.stream().filter(callable -> Objects.nonNull(callable))
					.forEach(callable -> value.add(this.pool.submit(callable)));
			if (waitForResult) {
				this.futureGet(value);
			}
		}
		return value;
	}

	/**
	 * Call the method {@link Future#get()} on the list of {@link Future}s.
	 *
	 * @param futures the list of {@link Future}s.
	 */
	public <T> void futureGet(final List<Future<T>> futures) {
		futures.forEach(future -> {
			try {
				future.get();
				if (this.pool.getQueue().size() % this.getSize() == 0) {
					this.logger.info("{} callables waiting for execution",
							this.pool.getQueue().size());
				}
			} catch (InterruptedException e) {
				this.logger.error(e.getMessage(), e);
			} catch (ExecutionException e) {
				this.logger.error(e.getMessage(), e);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.qpark.eip.core.ReportingThreadPoolProvider#getPoolSize()
	 */
	@Override
	protected int getPoolSize() {
		return this.getSize();
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Get the thread pool name prefix, defaults to
	 * <i>EIP-ThreadPool-thread-</i>.
	 *
	 * @return the thread pool name prefix.
	 */
	public String getThreadPoolNamePrefix() {
		return this.threadPoolNamePrefix;
	}

	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return this.initialized;
	}

	/**
	 * @param size the pool size to set
	 */
	public void setSize(final int size) {
		this.size = size;
	}

	/**
	 * @param threadPoolNamePrefix the threadPoolNamePrefix to set
	 */
	public void setThreadPoolNamePrefix(final String threadPoolNamePrefix) {
		this.threadPoolNamePrefix = threadPoolNamePrefix;
	}
}
