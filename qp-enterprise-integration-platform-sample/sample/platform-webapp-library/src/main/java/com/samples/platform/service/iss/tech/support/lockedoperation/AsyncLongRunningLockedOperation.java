/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.service.iss.tech.support.lockedoperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.spring.lockedoperation.AbstractAsyncLockableOperation;
import com.qpark.eip.core.spring.lockedoperation.LockableOperationContext;

/**
 * 60 seconds running operation with UUID {@value #OPERATION_UUID}.
 *
 * @author bhausen
 */
@Component
public class AsyncLongRunningLockedOperation extends AbstractAsyncLockableOperation {
	/** The UUID of the locked operation. */
	public static final String OPERATION_UUID = "a5a70aea-86b0-3a74-86dd-2f0ea139c950";
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(AsyncLongRunningLockedOperation.class);

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return this.logger;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#getUUID()
	 */
	@Override
	public String getUUID() {
		return OPERATION_UUID;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.AbstractAsyncLockableOperation#invokeOperationAsync(com.qpark.eip.core.spring.lockedoperation.LockableOperationContext)
	 */
	@Override
	protected void invokeOperationAsync(final LockableOperationContext context) {
		this.logger.info("+invokeOperationAsync {} {}", this.getName(), this.getUUID());
		try {
			Thread.sleep(15 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.logger.info("-invokeOperationAsync {} {}", this.getName(), this.getUUID());
	}

}
