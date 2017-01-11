/*******************************************************************************
 * Copyright (c) 2013 - 2017 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.lockedoperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhausen
 */
public class LockableOperationTestAsync extends AbstractLockableOperation {
	/** The UUID of the {@link LockableOperationTestAsync}. */
	public static final String UUID = "0ded3aa5-e0b2-485e-8744-0d3d0a7a53b0";
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(LockableOperationTestAsync.class);

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
		return UUID;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#isAsync()
	 */
	@Override
	public boolean isAsync() {
		return true;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.AbstractLockableOperation#invokeOperation(com.qpark.eip.core.spring.lockedoperation.LockableOperationContext)
	 */
	@Override
	protected void invokeOperation(final LockableOperationContext context) {
		this.logger.info("+invokeOperation {}", this.getName());
		this.logger.info(" invokeOperation {} sleep a little bit", this.getName());
		EipTest.sleep();
		this.logger.info("-invokeOperation {}", this.getName());
	}
}
