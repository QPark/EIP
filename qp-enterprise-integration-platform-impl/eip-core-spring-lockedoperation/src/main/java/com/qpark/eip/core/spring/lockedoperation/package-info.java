/**
 * Provides a generic interface to start processes identified by UUIDs. These
 * processes have a locking mechanism: Even if a the process is deployed at
 * several web applications, it will run only once at a point in time. The
 * processes are able to respond in a synchronous or asynchronous mode.
 * <p/>
 * {@link com.qpark.eip.core.spring.lockedoperation.LockableOperation} contains
 * the implementation of the process to run. The consumer contains a list of all
 * {@link com.qpark.eip.core.spring.lockedoperation.LockableOperation}s
 * available in the spring context. This consumer informs each and every
 * {@link com.qpark.eip.core.spring.lockedoperation.LockableOperation} to run by
 * calling
 * {@link com.qpark.eip.core.spring.lockedoperation.LockableOperation#runOperation(String, com.qpark.eip.core.spring.lockedoperation.model.OperationEventEnumType, LockableOperationContext)}
 * passing the UUID of the operation to start. If the passed UUID equals to the
 * {@link com.qpark.eip.core.spring.lockedoperation.LockableOperation#getUUID()}
 * , the
 * {@link com.qpark.eip.core.spring.lockedoperation.AbstractLockableOperation}
 * enters a lock via the
 * {@link com.qpark.eip.core.spring.lockedoperation.dao.LockedOperationDaoImpl}, and
 * runs
 * {@link com.qpark.eip.core.spring.lockedoperation.AbstractLockableOperation#invokeOperation(LockableOperationContext)}
 * or - if asynchronous
 * {@link com.qpark.eip.core.spring.lockedoperation.AbstractAsyncLockableOperation#invokeOperationAsync(LockableOperationContext)}
 * which contains the code of the process to be executed. After finishing the
 * work, the lock is removed via the
 * {@link com.qpark.eip.core.spring.lockedoperation.dao.LockedOperationDaoImpl} again.
 * <p/>
 * The following image shows the flow of the process:
 * <p/>
 * <img src="doc-files/LockableOperationFlow.png"/>
 */
package com.qpark.eip.core.spring.lockedoperation;
