package com.qpark.eip.core.spring.lockedoperation.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.spring.lockedoperation.LockableOperation;
import com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig;

public interface LockableOperationDao {

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
	 *         with the same {@link #hostAddress}.
	 */
	boolean isLockedByThisServer(LockableOperation operation);

	/**
	 * Get the locking status of the operation.
	 *
	 * @param operationName
	 *            the name of the operation to create a lock for.
	 * @return <code>true</code> if operation is now locked for the caller, else
	 *         <code>false</code>.
	 */
	boolean isLockedOperation(LockableOperation operation);

	/**
	 * Tries to lock the operation.
	 *
	 * @param operationName
	 *            the name of the operation to create a lock for.
	 * @return <code>true</code> if operation is now locked for the caller, else
	 *         <code>false</code>.
	 */
	boolean lockOperation(LockableOperation operation);

	/**
	 * Tries to unlocks the operation.
	 *
	 * @param operationName
	 *            the name of the operation.
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