package com.qpark.eip.core.persistence;

import com.qpark.eip.core.ReportingThreadPoolProvider;

/**
 * Provides the pool to execute {@link AsyncDatabaseOperation}s.
 * @author bhausen
 */
public class AsyncDatabaseOperationPoolProvider extends
		ReportingThreadPoolProvider {
	/**
	 * Set the default number of threads in the pool fix to 30.
	 * @see com.qpark.eip.core.ReportingThreadPoolProvider#getPoolSize()
	 */
	@Override
	protected int getPoolSize() {
		return 30;
	}

	/**
	 * Submits the {@link AsyncDatabaseOperation} to the pool.
	 * @param opertion the {@link AsyncDatabaseOperation}.
	 */
	public void submit(final AsyncDatabaseOperation opertion) {
		this.pool.submit(opertion);
	}
}
