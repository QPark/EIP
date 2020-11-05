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

import com.qpark.eip.core.spring.lockedoperation.AbstractLockableOperation;
import com.qpark.eip.core.spring.lockedoperation.LockableOperationContext;

/**
 * 5 seconds running operation with UUID {@value #OPERATION_UUID}.
 *
 * @author bhausen
 */
@Component
public class SyncShortRunningLockedOperation extends AbstractLockableOperation {
	/** The UUID of the locked operation. */
	public static final String OPERATION_UUID = "cc05a4b0-6e21-30c6-b894-09a653408a94";
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(SyncShortRunningLockedOperation.class);

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
	 * @see com.qpark.eip.core.spring.lockedoperation.AbstractLockableOperation#invokeOperation(com.qpark.eip.core.spring.lockedoperation.LockableOperationContext)
	 */
	@Override
	protected void invokeOperation(final LockableOperationContext context) {
		this.logger.info("+invokeOperation {} {}", this.getName(), this.getUUID());
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.logger.info("-invokeOperation {} {}", this.getName(), this.getUUID());
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#isAsync()
	 */
	@Override
	public boolean isAsync() {
		return false;
	}

}
