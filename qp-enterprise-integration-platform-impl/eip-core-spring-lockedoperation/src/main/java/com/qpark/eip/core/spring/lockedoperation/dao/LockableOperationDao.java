/*******************************************************************************
 * Copyright (c) 2013 - 2017 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.lockedoperation.dao;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.qpark.eip.core.spring.lockedoperation.LockableOperation;

/**
 * DAO providing locking feasibility.
 *
 * @author bhausen
 */
public interface LockableOperationDao
		extends ApplicationListener<ContextRefreshedEvent> {
	/**
	 * @return the hostAddress
	 */
	String getHostAddress();

	/**
	 * @return the hostName
	 */
	String getHostName();

	/**
	 * Checks if the {@link LockableOperation} is locked by this server.
	 *
	 * @param operation
	 *            the {@link LockableOperation}.
	 * @return <code>true</code>, if locked and the lock was set by the server
	 *         with the same {@link #getHostAddress}.
	 */
	boolean isLockedByThisServer(LockableOperation operation);

	/**
	 * Get the locking status of the operation.
	 *
	 * @param operation
	 *            the {@link LockableOperation} to create a lock for.
	 * @return <code>true</code> if operation is now locked for the caller, else
	 *         <code>false</code>.
	 */
	boolean isLockedOperation(LockableOperation operation);

	/**
	 * Tries to lock the operation.
	 *
	 * @param operation
	 *            the {@link LockableOperation} to create a lock for.
	 * @return <code>true</code> if operation is now locked for the caller, else
	 *         <code>false</code>.
	 */
	boolean lockOperation(LockableOperation operation);

	/**
	 * Tries to unlocks the operation.
	 *
	 * @param operation
	 *            the {@link LockableOperation}.
	 * @return <code>true</code> if the operation is now unlocked, else
	 *         <code>false</code>.
	 */
	boolean unlockOperation(LockableOperation operation);

	/**
	 * On startup unlock all operations that may be locked from a previous run
	 * by this host.
	 */
	void unlockOperationOnServerStart();
}