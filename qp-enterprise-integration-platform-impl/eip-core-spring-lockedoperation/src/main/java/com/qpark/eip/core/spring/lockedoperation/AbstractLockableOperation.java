/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.lockedoperation;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao;
import com.qpark.eip.core.spring.lockedoperation.dao.LockedOperationDaoImpl;
import com.qpark.eip.core.spring.lockedoperation.model.OperationEventEnumType;
import com.qpark.eip.core.spring.lockedoperation.model.OperationStateEnumType;

/**
 * The base of the {@link LockableOperation}.
 *
 * @author bhausen
 */
public abstract class AbstractLockableOperation implements LockableOperation {
	/** The {@link LockedOperationDaoImpl} to lock and unlock the operation. */
	@Autowired
	private LockableOperationDao lockedOperationDao;

	/**
	 * Try to check the state of the operation.
	 *
	 * @param context
	 *            the {@link LockableOperationContext}.
	 */
	private OperationStateEnumType handleEventCheckState(
			final LockableOperationContext context) {
		OperationStateEnumType state = OperationStateEnumType.IDLE;
		/*
		 * Given event is to check the state of the operation. The reported
		 * states could be OperationStateEnumType.RUNNING or
		 * OperationStateEnumType.IDLE.
		 */
		if (this.lockedOperationDao.isLockedOperation(this)) {
			state = OperationStateEnumType.RUNNING;
		} else {
			state = OperationStateEnumType.IDLE;
		}
		this.getLogger().debug("Checked operation {} ({}) state: {}",
				new Object[] { this.getName(), this.getUUID(), state });
		return state;
	}

	/**
	 * Try to run the operation.
	 *
	 * @param context
	 *            the {@link LockableOperationContext}.
	 */
	private OperationStateEnumType handleEventStart(
			final LockableOperationContext context) {
		OperationStateEnumType state = OperationStateEnumType.IDLE;
		/* Given event asks to start the operation. */
		if (this.lockedOperationDao.lockOperation(this)) {
			/*
			 * A new lock for the operation was created. Now it is time to run
			 * the operation.
			 */
			try {
				state = OperationStateEnumType.STARTED;
				this.invokeOperation(context);
			} finally {
				if (!this.isAsync()) {
					/* Only synchronous operations need to be unlocked again. */
					this.lockedOperationDao.unlockOperation(this);
					state = OperationStateEnumType.IDLE;
				}
			}
		} else {
			/*
			 * The lock could not be set. So another call or another web
			 * application started the operation already and it is not finished.
			 */
			state = OperationStateEnumType.RUNNING;
			this.getLogger().debug(
					"Tried to start operation {} ({}), but was already locked.",
					new Object[] { this.getName(), this.getUUID() });
		}
		return state;
	}

	/**
	 * Invoke the real logic of the {@link LockableOperation}.
	 *
	 * @param context
	 *            the {@link LockableOperationContext} (could be
	 *            <code>null</code>) to pass to the {@link LockableOperation}.
	 */
	protected abstract void invokeOperation(LockableOperationContext context);

	/**
	 * Check if the request is asking to check the state of the operation.
	 *
	 * @param event
	 *            the {@link OperationEventEnumType}.
	 * @return <code>true</code> if asking to check the state.
	 */
	private static boolean isEventCheckState(
			final OperationEventEnumType event) {
		boolean value = false;
		if (event != null && event.value()
				.equals(OperationEventEnumType.CHECK_STATE.value())) {
			value = true;
		}
		return value;
	}

	/**
	 * Check if the request is asking to check the state of the operation.
	 *
	 * @param event
	 *            the {@link OperationEventEnumType}.
	 * @return <code>true</code> if asking to run the operation.
	 */
	private static boolean isEventStart(final OperationEventEnumType event) {
		boolean value = false;
		if (event != null
				&& event.value().equals(OperationEventEnumType.START.value())) {
			value = true;
		}
		return value;
	}

	/**
	 * Test if the requested UUID deals with this {@link LockableOperation}.
	 *
	 * @param operationUuid
	 *            the UUID to start.
	 * @return <code>true</code> if it deals with this {@link LockableOperation}
	 *         , else <code>false</code>.
	 */
	private boolean isRequestedOperation(final String operationUuid) {
		boolean value = false;
		if (operationUuid != null && this.getUUID().equals(operationUuid)) {
			value = true;
		}
		return value;
	}

	/**
	 * Run the operation, if nobody else locked the execution.
	 *
	 * @param operationUuid
	 *            the UUID of the {@link LockableOperation} to run.
	 * @param event
	 *            the {@link OperationEventEnumType}.
	 * @param context
	 *            the {@link LockableOperationContext} (could be
	 *            <code>null</code>) to pass to the {@link LockableOperation}.
	 * @return the resulting {@link OperationEventEnumType}.
	 */
	@Override
	public OperationStateEnumType runOperation(final String operationUuid,
			final OperationEventEnumType event,
			final LockableOperationContext context) {
		OperationStateEnumType state = null;
		if (this.isRequestedOperation(operationUuid)) {
			/*
			 * Request contains an operation definition to start and the
			 * implementing class equals that definition!
			 */
			this.getLogger().debug("Found operation {} ({})",
					new Object[] { this.getName(), this.getUUID() });
			if (isEventCheckState(event)) {
				/* Check the state. */
				state = this.handleEventCheckState(context);
			} else if (isEventStart(event)) {
				/* Start the operation. */
				state = this.handleEventStart(context);
			}
		}
		return state;
	}
}
