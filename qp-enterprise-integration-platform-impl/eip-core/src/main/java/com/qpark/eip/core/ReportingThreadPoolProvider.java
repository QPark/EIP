package com.qpark.eip.core;

import java.util.Arrays;
import java.util.List;
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
	 * @param executorService the executor which we want to attach to, and
	 *            shutdown.
	 */
	private static void shutdown(final ExecutorService executorService) {
		List<Runnable> runnables = executorService.shutdownNow();
		Arrays.toString(runnables.toArray(new Runnable[runnables.size()]));
	}

	/** The {@link ReportingThreadPoolExecutor}. */
	protected ReportingThreadPoolExecutor pool;

	/**
	 * Get the pool size.
	 * @return the pool size.
	 */
	protected abstract int getPoolSize();

	/** Create the pool of threads to enhance the number of requested calls. */
	@PostConstruct
	private void postConstruct() {
		this.pool = new ReportingThreadPoolExecutor(this.getPoolSize());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown(ReportingThreadPoolProvider.this.pool);
			}
		});
	}

	/** Shutdown the pool of threads to enhance the number of requested calls. */
	@PreDestroy
	private void preDestroy() {
		if (this.pool != null) {
			this.pool.shutdown();
		}
	}

}
