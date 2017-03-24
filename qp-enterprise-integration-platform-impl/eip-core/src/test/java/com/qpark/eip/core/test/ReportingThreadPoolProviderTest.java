/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;

import com.qpark.eip.core.ReportingThreadPoolProvider;

/**
 * @author bhausen
 */
public class ReportingThreadPoolProviderTest
		extends ReportingThreadPoolProvider {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(ReportingThreadPoolProviderTest.class);

	/** Test to run {@link Callable}s in the pool. */
	@Test
	public void testRun() {
		if (Objects.isNull(this.pool)) {
			this.postConstruct();
		}

		List<Callable<Integer>> callables = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			callables.add(this.getCallable(i));
		}
		List<Future<Integer>> futures = new ArrayList<>();
		callables.stream().forEach(c -> futures.add(this.pool.submit(c)));
		futures.stream().forEach(f -> {
			try {
				this.logger.info("+future.get   max={} active={} queue={}",
						this.pool.getMaximumPoolSize(),
						this.pool.getActiveCount(),
						this.pool.getQueue().size());
				int number = f.get();
				this.logger.info("-future.get {} max={} active={} queue={}",
						number, this.pool.getMaximumPoolSize(),
						this.pool.getActiveCount(),
						this.pool.getQueue().size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
	}

	private Callable<Integer> getCallable(final int index) {
		final Callable<Integer> value = new Callable<Integer>() {
			/** The {@link Logger}. */
			private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
					.getLogger(String.format("%s.%s",
							ReportingThreadPoolProviderTest.class.getName(),
							"Callable"));
			private int number = index;

			@Override
			public Integer call() throws Exception {
				this.logger.info("+call {}", this.number);
				Thread.sleep(3000);
				this.logger.info("-call {}", this.number);
				return this.number;
			}
		};
		return value;
	}

	/**
	 * @see com.qpark.eip.core.ReportingThreadPoolProvider#getPoolSize()
	 */
	@Override
	protected int getPoolSize() {
		return 2;
	}
}
