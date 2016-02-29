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

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.spring.lockedoperation.LockableOperation;
import com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig;
import com.qpark.eip.core.spring.lockedoperation.model.OperationLockControllType;

public class LockedOperationDaoImpl implements LockableOperationDao {
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
			.getLogger(LockedOperationDaoImpl.class);

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao#getHostAddress()
	 */
	@Override
	public String getHostAddress() {
		return this.hostAddress;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao#getHostName()
	 */
	@Override
	public String getHostName() {
		return this.hostName;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao#isLockedByThisServer(com.qpark.eip.core.spring.lockedoperation.LockableOperation)
	 */
	@Override
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
	 * @see com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao#isLockedOperation(com.qpark.eip.core.spring.lockedoperation.LockableOperation)
	 */
	@Override
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
	 * @see com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao#lockOperation(com.qpark.eip.core.spring.lockedoperation.LockableOperation)
	 */
	@Override
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
	 * Remove left over locks of previously stopped processes.
	 *
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	@Transactional(value = EipLockedoperationConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		this.setupHostValues();
		this.logger.debug("Host name    : {}", this.hostName);
		this.logger.debug("Host address : {}", this.hostAddress);
		if (this.firstRun) {
			this.unlockOperationOnServerStart();
			this.firstRun = false;
		}
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

	private void setupHostValues() {
		if (this.hostName == null) {
			try {
				InetAddress localaddr = InetAddress.getLocalHost();
				this.hostAddress = localaddr.getHostAddress();
				this.hostName = localaddr.getHostName();
			} catch (UnknownHostException e) {
				this.logger.error(e.getMessage(), e);
				this.hostName = "localhost";
			}
		}
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao#unlockOperation(com.qpark.eip.core.spring.lockedoperation.LockableOperation)
	 */
	@Override
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
	 * @see com.qpark.eip.core.spring.lockedoperation.dao.LockableOperationDao#unlockOperationOnServerStart()
	 */
	@Override
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
