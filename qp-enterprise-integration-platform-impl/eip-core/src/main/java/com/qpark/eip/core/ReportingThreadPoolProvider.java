/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author bhausen
 */
public abstract class ReportingThreadPoolProvider {
	/**
	 * Shutdown hook which calls
	 * {@code java.util.concurrent.ExecutorService#shutdownNow()}
	 *
	 * @param executorService
	 *            the executor which we want to attach to, and shutdown.
	 */
	private static void shutdown(final ExecutorService executorService) {
		List<Runnable> runnables = executorService.shutdownNow();
		Arrays.toString(runnables.toArray(new Runnable[runnables.size()]));
	}

	/** The {@link ReportingThreadPoolExecutor}. */
	protected ReportingThreadPoolExecutor pool;

	/**
	 * Get the pool size.
	 *
	 * @return the pool size.
	 */
	protected abstract int getPoolSize();

	/** Create the pool of threads to enhance the number of requested calls. */
	@PostConstruct
	protected synchronized void postConstruct() {
		if (Objects.isNull(this.pool)) {
			this.pool = new ReportingThreadPoolExecutor(this.getPoolSize());
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					shutdown(ReportingThreadPoolProvider.this.pool);
				}
			});
		}
	}

	/**
	 * Shutdown the pool of threads to enhance the number of requested calls.
	 */
	@PreDestroy
	protected synchronized void preDestroy() {
		if (Objects.nonNull(this.pool)) {
			this.pool.shutdown();
			this.pool = null;
		}
	}
}
