/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.persistence;

import com.qpark.eip.core.ReportingThreadPoolProvider;

/**
 * Provides the pool to execute {@link AsyncDatabaseOperation}s.
 * 
 * @author bhausen
 */
public class AsyncDatabaseOperationPoolProvider
		extends ReportingThreadPoolProvider {
	/**
	 * Set the default number of threads in the pool fix to 30.
	 * 
	 * @see com.qpark.eip.core.ReportingThreadPoolProvider#getPoolSize()
	 */
	@Override
	protected int getPoolSize() {
		return 30;
	}

	/**
	 * Submits the {@link AsyncDatabaseOperation} to the pool.
	 * 
	 * @param opertion
	 *            the {@link AsyncDatabaseOperation}.
	 */
	public void submit(final AsyncDatabaseOperation opertion) {
		this.pool.submit(opertion);
	}
}
