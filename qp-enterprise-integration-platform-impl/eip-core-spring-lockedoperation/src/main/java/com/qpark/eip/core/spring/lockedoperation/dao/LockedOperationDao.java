/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.lockedoperation.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.spring.lockedoperation.LockableOperation;
import com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig;
import com.qpark.eip.core.spring.lockedoperation.model.OperationLockControllType;

public class LockedOperationDao implements InitializingBean {
	/**
	 * @param operation
	 * @return a unique lock string.
	 */
	public static String getOperationLockString(
			final LockableOperation operation) {
		return new StringBuffer(operation.getUUID()).append("#")
				.append(operation.getName()).toString();
	}

	/** The {@link EntityManager}. */
	@PersistenceContext(
			unitName = EipLockedoperationConfig.PERSISTENCE_UNIT_NAME,
			name = EipLockedoperationConfig.ENTITY_MANAGER_FACTORY_NAME)
	private EntityManager em;
	/** First run indicator. */
	private boolean firstRun = true;
	/** The IP address of the host. */
	private String hostAddress;
	/** The host name. */
	private String hostName;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(LockedOperationDao.class);

	/**
	 * @return the hostAddress
	 */
	public String getHostAddress() {
		return this.hostAddress;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return this.hostName;
	}

	/**
	 * Set the host values and unlock operations locked by this host if needed.
	 *
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			InetAddress localaddr = InetAddress.getLocalHost();
			this.hostAddress = localaddr.getHostAddress();
			this.hostName = localaddr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			this.hostName = "localhost";
		}
		this.logger.debug("Host name    : {}", this.hostName);
		this.logger.debug("Host address : {}", this.hostAddress);
		this.unlockOperationOnServerStart();
	}

	/**
	 * Checks if the {@link LockableOperation} is locked by this server.
	 *
	 * @param operation
	 *            the {@link LockableOperation}.
	 * @return <code>true</code>, if locked and the lock was set by the server
	 *         with the same {@link #hostAddress}.
	 */
	@Transactional(value = EipLockedoperationConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public synchronized boolean isLockedByThisServer(
			final LockableOperation operation) {
		boolean lockedByThisServer = false;
		OperationLockControllType value = null;
		String logString = getOperationLockString(operation);
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<OperationLockControllType> q = cb
				.createQuery(OperationLockControllType.class);
		Root<OperationLockControllType> c = q
				.from(OperationLockControllType.class);
		q.where(cb.equal(c.<String> get("operationName"), logString));
		TypedQuery<OperationLockControllType> typedQuery = this.em
				.createQuery(q);
		try {
			value = typedQuery.getSingleResult();
			if (value != null) {
				if (value.getServerIpAddress().equals(this.hostAddress)) {
					lockedByThisServer = true;
				}
				this.logger.debug(
						" isLockedByThisServer# {} opeation {} {} locked by: {} {}",
						new Object[] { lockedByThisServer, operation.getUUID(),
								operation.getName(), value.getServerName(),
								value.getLockDate() });
			}
		} catch (NoResultException e) {
			lockedByThisServer = false;
		} catch (NonUniqueResultException e) {
			lockedByThisServer = false;
		}
		return lockedByThisServer;
	}

	/**
	 * Get the locking status of the operation.
	 *
	 * @param operationName
	 *            the name of the operation to create a lock for.
	 * @return <code>true</code> if operation is now locked for the caller, else
	 *         <code>false</code>.
	 */
	@Transactional(value = EipLockedoperationConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public synchronized boolean isLockedOperation(
			final LockableOperation operation) {
		this.logger.debug("+isLocked# {} {} {}", new Object[] { this.hostName,
				operation.getUUID(), operation.getName() });
		if (this.firstRun) {
			this.unlockOperationOnServerStart();
			this.firstRun = false;
		}
		boolean locked = false;
		String logString = getOperationLockString(operation);
		OperationLockControllType value = null;
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<OperationLockControllType> q = cb
				.createQuery(OperationLockControllType.class);
		Root<OperationLockControllType> c = q
				.from(OperationLockControllType.class);
		q.where(cb.equal(c.<String> get("operationName"), logString));
		TypedQuery<OperationLockControllType> typedQuery = this.em
				.createQuery(q);
		try {
			value = typedQuery.getSingleResult();
			if (value != null) {
				this.logger.debug(" isLocked# opeation {} {} locked by: {} {}",
						new Object[] { operation.getUUID(), operation.getName(),
								value.getServerName(), value.getLockDate() });
				locked = true;
			}
		} catch (NoResultException e) {
			locked = false;
		} catch (NonUniqueResultException e) {
			locked = true;
		}
		this.logger
				.debug("-isLocked# {} {} {} {}",
						new Object[] { this.hostName,
								locked ? "locked" : "not locked",
								operation.getUUID(), operation.getName() });
		return locked;
	}

	/**
	 * Tries to lock the operation.
	 *
	 * @param operationName
	 *            the name of the operation to create a lock for.
	 * @return <code>true</code> if operation is now locked for the caller, else
	 *         <code>false</code>.
	 */
	@Transactional(value = EipLockedoperationConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public synchronized boolean lockOperation(
			final LockableOperation operation) {
		this.logger.debug("+lock# {} {} {}", new Object[] { this.hostName,
				operation.getUUID(), operation.getName() });
		if (this.firstRun) {
			this.unlockOperationOnServerStart();
			this.firstRun = false;
		}
		boolean locked = false;
		String logString = getOperationLockString(operation);
		OperationLockControllType value = null;
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<OperationLockControllType> q = cb
				.createQuery(OperationLockControllType.class);
		Root<OperationLockControllType> c = q
				.from(OperationLockControllType.class);
		q.where(cb.equal(c.<String> get("operationName"), logString));
		TypedQuery<OperationLockControllType> typedQuery = this.em
				.createQuery(q);
		try {
			value = typedQuery.getSingleResult();
			this.logger
					.debug(" lock# opeation {} {} locked by: {} {}",
							new Object[] { operation.getUUID(),
									operation.getName(), value.getServerName(),
									value.getLockDate() });
		} catch (NoResultException e) {
			value = null;
		} catch (NonUniqueResultException e) {
			return false;
		}
		if (value == null) {
			value = new OperationLockControllType();
			value.setOperationName(logString);
			value.setServerName(this.hostName);
			value.setServerIpAddress(this.hostAddress);
			value.setLockDate(DateUtil.get(new Date()));
			this.em.persist(value);
			locked = true;
		}
		this.logger
				.debug("-lock# {} {} {} {}",
						new Object[] { this.hostName,
								locked ? "locked" : "not locked",
								operation.getUUID(), operation.getName() });
		return locked;
	}

	/**
	 * @param hostAddress
	 *            the hostAddress to set
	 */
	public void setHostAddress(final String hostAddress) {
		this.hostAddress = hostAddress;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Tries to unlocks the operation.
	 *
	 * @param operationName
	 *            the name of the operation.
	 * @return <code>true</code> if the operation is now unlocked, else
	 *         <code>false</code>.
	 */
	@Transactional(value = EipLockedoperationConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public boolean unlockOperation(final LockableOperation operation) {
		this.logger.debug("+unlock# {} {} {}", new Object[] { this.hostName,
				operation.getUUID(), operation.getName() });
		boolean unlocked = false;
		String logString = getOperationLockString(operation);
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<OperationLockControllType> q = cb
				.createQuery(OperationLockControllType.class);
		Root<OperationLockControllType> c = q
				.from(OperationLockControllType.class);
		q.where(cb.equal(c.<String> get("operationName"), logString));
		TypedQuery<OperationLockControllType> typedQuery = this.em
				.createQuery(q);
		try {
			List<OperationLockControllType> locks = typedQuery.getResultList();
			for (OperationLockControllType lock : locks) {
				this.em.remove(this.em.merge(lock));
			}
			unlocked = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.logger.debug("-unlock# {} {} {} {}",
					new Object[] { this.hostName,
							unlocked ? "unlocked" : "not unlocked",
							operation.getUUID(), operation.getName() });
		}
		return unlocked;
	}

	/**
	 * On startup unlock all operations that may be locked from a previous run
	 * by this host.
	 */
	@Transactional(value = EipLockedoperationConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public void unlockOperationOnServerStart() {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<OperationLockControllType> q = cb
				.createQuery(OperationLockControllType.class);
		Root<OperationLockControllType> c = q
				.from(OperationLockControllType.class);
		q.where(cb.equal(c.<String> get("serverName"), this.hostName));
		TypedQuery<OperationLockControllType> typedQuery = this.em
				.createQuery(q);
		try {
			List<OperationLockControllType> locks = typedQuery.getResultList();
			for (OperationLockControllType lock : locks) {
				this.logger.debug(
						" remove lock on server start (this {}): {} {}",
						this.hostName, lock.getServerName(),
						lock.getLockDate());
				this.em.remove(this.em.merge(lock));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
