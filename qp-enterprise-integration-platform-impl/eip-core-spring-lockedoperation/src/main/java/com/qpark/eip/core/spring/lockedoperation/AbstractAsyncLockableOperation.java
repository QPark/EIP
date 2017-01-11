/*******************************************************************************
 * Copyright (c) 2013 - 2017 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.lockedoperation;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao;
import com.qpark.eip.core.spring.lockedoperation.dao.LockedOperationDaoImpl;
import com.springsource.insight.annotation.InsightOperation;

/**
 * Abstract asynchronous inheritance of the {@link AbstractLockableOperation}.
 * Runs the process in an {@link Callable}. Heirs need to implement
 * {@link #invokeOperationAsync(LockableOperationContext)} where there provide
 * the business logic.
 *
 * @author bhausen
 */
public abstract class AbstractAsyncLockableOperation
		extends AbstractLockableOperation implements ApplicationContextAware {
	/**
	 * The callable to start asynchronously.
	 */
	static class AsyncLockableOperationRunner implements Callable<Void> {
		/** The {@link LockableOperationContext}. */
		private LockableOperationContext context;
		/**
		 * The {@link LockedOperationDaoImpl} to lock and unlock the operation.
		 */
		@Autowired
		private LockableOperationDao lockedOperationDao;
		/** The operation to start. */
		private AbstractAsyncLockableOperation operation;

		/**
		 * Callable implementation calling the
		 * {@link AbstractAsyncLockableOperation#invokeOperationAsync(LockableOperationContext)}
		 * method and unlock the operation again.
		 *
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Void call() throws Exception {
			try {
				this.operation.getLogger().debug(
						"Run now the operation {} ({}) asynchronously.",
						new Object[] { this.operation.getName(),
								this.operation.getUUID() });

				/* Run the operation asynchronously. */
				this.operation.invokeOperationAsync(this.context);
			} finally {
				/*
				 * Unlock the operation after finishing or aborting the
				 * operation.
				 */
				this.lockedOperationDao.unlockOperation(this.operation);
				this.operation.getLogger().debug(
						"Finished the operation {} ({})",
						new Object[] { this.operation.getName(),
								this.operation.getUUID() });
			}
			return null;
		}

		/**
		 * Set the {@link LockableOperationContext} to run.
		 *
		 * @param context
		 *            the {@link LockableOperationContext}.
		 */
		public void setContext(final LockableOperationContext context) {
			this.context = context;
		}

		/**
		 * Set the {@link AbstractAsyncLockableOperation} to run.
		 *
		 * @param operation
		 *            the operation.
		 */
		public void setOperation(
				final AbstractAsyncLockableOperation operation) {
			this.operation = operation;
		}
	}

	/** The {@link ApplicationContext}. */
	protected ApplicationContext applicationContext;

	/**
	 * Invoke the real logic of the {@link LockableOperation}.
	 *
	 * @param context
	 *            the {@link LockableOperationContext} (could be
	 *            <code>null</code>) to pass to the {@link LockableOperation}.
	 */
	@Override
	protected final void invokeOperation(
			final LockableOperationContext context) {
		this.getLogger().debug("Create AsyncRunner for the operation {} ({})",
				new Object[] { this.getName(), this.getUUID() });
		AutowireCapableBeanFactory beanFactory = this.applicationContext
				.getAutowireCapableBeanFactory();
		AsyncLockableOperationRunner operationRunner = beanFactory
				.createBean(AsyncLockableOperationRunner.class);
		beanFactory.initializeBean(operationRunner,
				new StringBuffer(this.getName())
						.append(System.currentTimeMillis()).toString());

		operationRunner.setOperation(this);
		operationRunner.setContext(context);

		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor(
				new StringBuffer("exec").append(this.getName()).toString());
		executor.submit(operationRunner);
		this.getLogger().debug(
				"AsyncRunner of operation {} ({}) started with SimpleAsyncTaskExecutor",
				new Object[] { this.getName(), this.getUUID() });
	}

	/**
	 * Invoke the real logic of the {@link LockableOperation}.
	 *
	 * @param context
	 *            the {@link LockableOperationContext} (could be
	 *            <code>null</code>) to pass to the {@link LockableOperation}.
	 */
	@InsightOperation
	protected abstract void invokeOperationAsync(
			LockableOperationContext context);

	/**
	 * Always <code>true</code>.
	 *
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#isAsync()
	 */
	@Override
	public final boolean isAsync() {
		return true;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
