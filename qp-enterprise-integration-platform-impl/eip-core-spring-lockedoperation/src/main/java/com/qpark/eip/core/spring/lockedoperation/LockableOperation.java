package com.qpark.eip.core.spring.lockedoperation;

import org.slf4j.Logger;

import com.qpark.eip.core.spring.lockedoperation.model.OperationEventEnumType;
import com.qpark.eip.core.spring.lockedoperation.model.OperationStateEnumType;

public interface LockableOperation {
	/**
	 * @return the Logger of the operation.
	 */
	Logger getLogger();

	/**
	 * @return the Name of the APP event listener.
	 */
	String getName();

	/**
	 * @return the UUID of the APP event listener.
	 */
	String getUUID();

	/**
	 * @return <code>true</code> if the operation should be started
	 *         asynchronously.
	 */
	boolean isAsync();

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
	OperationStateEnumType runOperation(String operationUuid,
			OperationEventEnumType event, LockableOperationContext context);
}
