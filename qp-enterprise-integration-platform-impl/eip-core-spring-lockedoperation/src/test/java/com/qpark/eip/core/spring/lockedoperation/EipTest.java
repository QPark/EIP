/*******************************************************************************
 * Copyright (c) 2013 - 2017 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.lockedoperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.qpark.eip.core.spring.lockedoperation.model.OperationEventEnumType;
import com.qpark.eip.core.spring.lockedoperation.model.OperationStateEnumType;

/**
 * Execute the tests of the {@link LockableOperation}s.
 *
 * @author bhausen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class EipTest {
	/** The number of milliseconds to sleep. */
	private static final long SLEEP_MILLISECONDS = 1500;

	/** Sleep ... */
	static void sleep() {
		try {
			Thread.sleep(SLEEP_MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(EipTest.class);
	/** The {@link LockableOperationTestAsync}. */
	@Autowired
	private LockableOperationTestAsync operationAsync;

	/** The {@link LockableOperationTestSync}. */
	@Autowired
	private LockableOperationTestSync operationSync;

	/**
	 * Test to run several asynchronous {@link LockableOperation}s in parallel.
	 */
	@Test
	public void testLockableOperationTestAsyncCall() {
		this.logger.debug("+testLockableOperationTestAsyncCall");
		int threadCount = 4;
		OperationEventEnumType start = OperationEventEnumType.START;
		LockableOperationContext context = new LockableOperationContext();
		List<OperationStateEnumType> status = new ArrayList<>();
		IntStream.range(0, threadCount).forEach(i -> {
			OperationStateEnumType value = this.operationAsync.runOperation(this.operationAsync.getUUID(), start,
					context);
			status.add(value);
			this.logger.debug(" testLockableOperationTestAsyncCall returned {}", value);
		});

		int runnings = status.stream().filter(s -> s.equals(OperationStateEnumType.RUNNING))
				.collect(Collectors.toList()).size();
		int idle = status.stream().filter(s -> s.equals(OperationStateEnumType.STARTED)).collect(Collectors.toList())
				.size();
		Assert.assertEquals("No the right number of async proccesses got the RUNNING return.", runnings,
				threadCount - 1);
		Assert.assertEquals("To many STARTED async processes", idle, 1);

		EipTest.sleep();

		OperationStateEnumType idleResult = this.operationSync.runOperation(this.operationSync.getUUID(),
				OperationEventEnumType.CHECK_STATE, context);
		Assert.assertEquals("Cleanup missing at sync processes", idleResult, OperationStateEnumType.IDLE);
		this.logger.debug("+testLockableOperationTestAsyncCall");
	}

	/**
	 * Test to run several synchronous {@link LockableOperation}s in parallel.
	 */
	@Test
	public void testLockableOperationTestSyncCall() {
		this.logger.debug("+testLockableOperationTestSyncCall");
		int threadCount = 4;

		OperationEventEnumType start = OperationEventEnumType.START;
		LockableOperationContext context = new LockableOperationContext();
		List<OperationStateEnumType> status = new ArrayList<>();
		Callable<Void> task = () -> {
			OperationStateEnumType value = EipTest.this.operationSync.runOperation(EipTest.this.operationSync.getUUID(),
					start, context);
			status.add(value);
			this.logger.debug(" testLockableOperationTestSyncCall returned {}", value);
			return null;
		};
		List<Callable<Void>> tasks = Collections.nCopies(threadCount, task);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		List<Future<Void>> futures;
		try {
			futures = executorService.invokeAll(tasks);
			List<Void> resultList = new ArrayList<>(futures.size());
			// Check for exceptions
			for (Future<Void> future : futures) {
				// Throws an exception if an exception was thrown by the task.
				resultList.add(future.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int runnings = status.stream().filter(s -> s.equals(OperationStateEnumType.RUNNING))
				.collect(Collectors.toList()).size();
		int idle = status.stream().filter(s -> s.equals(OperationStateEnumType.IDLE)).collect(Collectors.toList())
				.size();
		Assert.assertEquals("No the right number of sync proccesses got the RUNNING return.", runnings,
				threadCount - 1);
		Assert.assertEquals("To many IDLE sync processes", idle, 1);
		OperationStateEnumType idleResult = this.operationSync.runOperation(this.operationSync.getUUID(),
				OperationEventEnumType.CHECK_STATE, context);
		Assert.assertEquals("Cleanup missing at sync processes", idleResult, OperationStateEnumType.IDLE);
		this.logger.debug("+testLockableOperationTestSyncCall");
	}
}
