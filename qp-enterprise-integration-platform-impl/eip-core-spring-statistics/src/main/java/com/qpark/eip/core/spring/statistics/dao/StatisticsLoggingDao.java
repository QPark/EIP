/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType;
import com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType_;
import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType_;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType_;
import com.qpark.eip.core.persistence.AbstractEipDao;
import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.statistics.StatisticsListener;
import com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig;

/**
 * DAO to access the authority database and to log the user calls.
 *
 * @author bhausen
 */
public class StatisticsLoggingDao extends AbstractEipDao
		implements StatisticsListener {
	private static final TimeZone LOGGING_TIMEZONE = TimeZone
			.getTimeZone("UTC");

	/**
	 * Get a {@link Date}, where hours, minutes, seconds and milliseconds are
	 * set to 0.
	 *
	 * @return the {@link Date} .
	 */
	private static Date getDayEnd(final Date d) {
		final Calendar gc = new GregorianCalendar(LOGGING_TIMEZONE);
		gc.setTime(d);
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 59);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	/**
	 * Get a {@link Date}, where hours, minutes, seconds and milliseconds are
	 * set to 0.
	 *
	 * @return the {@link Date} .
	 */
	private static Date getDayStart(final Date d) {
		final Calendar gc = new GregorianCalendar(LOGGING_TIMEZONE);
		gc.setTime(d);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	private static void setupSystemUserLog(final SystemUserLogType l,
			final SystemUserLogType addition) {
		if (l != null && addition != null) {
			l.setRequestsDenied(
					l.getRequestsDenied() + addition.getRequestsDenied());
			l.setRequestsGranted(
					l.getRequestsGranted() + addition.getRequestsGranted());
			l.setResponseFaults(
					l.getResponseFaults() + addition.getResponseFaults());
		}
		if (l != null && l.getRequestsGranted() == 0) {
			l.setRequestsGranted(1);
		}
	}

	/** The eip {@link ContextNameProvider}. */
	@Autowired
	@Qualifier(EipStatisticsConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	private ContextNameProvider contextNameProvider;
	/** The {@link EntityManager}. */
	@PersistenceContext(unitName = EipPersistenceConfig.PERSISTENCE_UNIT_NAME, name = EipPersistenceConfig.ENTITY_MANAGER_FACTORY_NAME)
	private EntityManager em;

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(StatisticsLoggingDao.class);

	/**
	 * Add the {@link ApplicationUserLogType} to the database.
	 *
	 * @param log the {@link ApplicationUserLogType} to add.
	 */
	@Override
	public void addChannelInvocation(final String channelName,
			final ApplicationUserLogType log) {
		if (this.logger.isTraceEnabled()) {
			this.logger.trace("{},{},{},{},{},{},{},{}", log.getContext(),
					log.getHostName(), log.getServiceName(),
					log.getOperationName(), log.getUserName(),
					log.getReturnedFailures(), log.getDurationString(),
					log.getStartItem());
		}
		this.em.persist(log);
	}

	/**
	 * Add the {@link SystemUserLogType} to the database.
	 *
	 * @param log the {@link SystemUserLogType} to add.
	 */
	@Override
	public void addChannelInvocation(final String channelName,
			final SystemUserLogType log) {
		/* Setup context and version. */
		log.setContext(this.getContextName());
		log.setVersion(this.getContextVersion());
		if (log.getUserName() != null
				&& log.getUserName().trim().length() == 0) {
			log.setUserName(null);
		}

		/* Setup to search existing one. */
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<SystemUserLogType> q = cb
				.createQuery(SystemUserLogType.class);
		final Root<SystemUserLogType> c = q.from(SystemUserLogType.class);

		final List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(c.<String>get(SystemUserLogType_.context),
				log.getContext()));
		predicates.add(cb.equal(c.<String>get(SystemUserLogType_.version),
				log.getVersion()));
		if (log.getUserName() == null) {
			predicates
					.add(cb.isNull(c.<String>get(SystemUserLogType_.userName)));
		} else {
			predicates.add(cb.equal(c.<String>get(SystemUserLogType_.userName),
					log.getUserName()));
		}
		predicates.add(cb.equal(c.<String>get(SystemUserLogType_.serviceName),
				log.getServiceName()));
		predicates.add(cb.equal(c.<String>get(SystemUserLogType_.operationName),
				log.getOperationName()));
		predicates.add(cb.between(c.<Date>get(SystemUserLogType_.logDateItem),
				getDayStart(log.getLogDateItem()),
				getDayEnd(log.getLogDateItem())));

		q.where(predicates.toArray(new Predicate[predicates.size()]));
		q.orderBy(cb.desc(c.<Long>get(SystemUserLogType_.hjid)));
		TypedQuery<SystemUserLogType> typedQuery = this.em.createQuery(q);

		SystemUserLogType persistence = null;
		synchronized (StatisticsLoggingDao.class) {
			try {
				persistence = typedQuery.getSingleResult();
				if (persistence == null) {
					/* Not found -> persist */
					persistence = log;
					StatisticsLoggingDao.setupSystemUserLog(persistence, null);
					this.em.persist(persistence);
				} else {
					/* Found -> add and merge */
					StatisticsLoggingDao.setupSystemUserLog(persistence, log);
					this.em.merge(persistence);
				}
			} catch (final NoResultException e) {
				/* Not found -> persist */
				persistence = log;
				StatisticsLoggingDao.setupSystemUserLog(persistence, null);
				this.em.persist(persistence);
			} catch (final NonUniqueResultException e) {
				/* Found more */
				typedQuery = this.em.createQuery(q);
				final List<SystemUserLogType> list = typedQuery.getResultList();
				SystemUserLogType l;
				for (final SystemUserLogType element : list) {
					l = element;
					if (persistence == null && l.getHjid() != null) {
						persistence = l;
						break;
					}
				}
				if (persistence != null) {
					/* Found more -> condense to first valid one -> merge. */
					StatisticsLoggingDao.setupSystemUserLog(persistence, log);
					for (int i = list.size() - 1; i >= 0; i--) {
						l = list.get(i);
						if (l != null && l.getHjid() != null) {
							if (persistence.getHjid().equals(l.getHjid())) {
								// Noting to be done here.
							} else {
								StatisticsLoggingDao
										.setupSystemUserLog(persistence, l);
								list.remove(i);
								this.em.remove(l);
							}
						}
					}
					this.em.merge(persistence);
				} else {
					/* Found more -> no valid one in list -> persist. */
					persistence = log;
					StatisticsLoggingDao.setupSystemUserLog(persistence, null);
					this.em.persist(persistence);
				}
			}
		}
		this.logger.debug(
				"addChannelInvocation SystemUserLog {} {} {} {} {} {}",
				this.contextNameProvider.getContextName(),
				this.contextNameProvider.getContextVersion(),
				String.valueOf(persistence.getUserName()),
				persistence.getServiceName(), persistence.getOperationName(),
				persistence.getLogDate().toXMLFormat());
	}

	/**
	 * Add the {@link FlowLogMessageType} to the database.
	 *
	 * @param log the {@link FlowLogMessageType} to add.
	 */
	@Override
	public void addFlowLogMessage(final FlowLogMessageType log) {
		this.em.persist(log);
	}

	/**
	 * @see com.qpark.eip.core.persistence.AbstractEipDao#doAsyncDatabaseOperation(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	@Transactional(value = EipPersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public void doAsyncDatabaseOperation(final String userName,
			final Object obj) {
		if (ApplicationUserLogType.class.isInstance(obj)) {
			this.addChannelInvocation("", (ApplicationUserLogType) obj);
		} else if (SystemUserLogType.class.isInstance(obj)) {
			this.addChannelInvocation("", (SystemUserLogType) obj);
		} else if (FlowLogMessageType.class.isInstance(obj)) {
			this.addFlowLogMessage((FlowLogMessageType) obj);
		}
	}

	/**
	 * Erase all {@link ApplicationUserLogType}s of the application scope older
	 * than the given date.
	 *
	 * @param toDate the date.
	 */
	@Transactional(value = EipPersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public void eraseApplicationUserLog(final Date toDate) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaDelete<ApplicationUserLogType> q = cb
				.createCriteriaDelete(ApplicationUserLogType.class);
		final Root<ApplicationUserLogType> c = q
				.from(ApplicationUserLogType.class);
		q.where(cb.lessThan(c.<Date>get(ApplicationUserLogType_.stopItem),
				toDate),
				cb.equal(c.<String>get(ApplicationUserLogType_.context),
						this.contextNameProvider.getContextName()));
		try {
			this.em.createQuery(q).executeUpdate();
		} catch (final Exception e) {
			this.logger.error("eraseApplicationUserLog: {}", e.getMessage());
		}
	}

	/**
	 * Erase all {@link FlowLogMessageType}s of the application scope older than
	 * the given date.
	 *
	 * @param toDate the date.
	 */
	@Transactional(value = EipPersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public void eraseFlowLogMessage(final Date toDate) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaDelete<FlowLogMessageType> q = cb
				.createCriteriaDelete(FlowLogMessageType.class);
		final Root<FlowLogMessageType> c = q.from(FlowLogMessageType.class);
		q.where(cb.lessThan(c.<Date>get(FlowLogMessageType_.logTimeItem),
				toDate));
		try {
			this.em.createQuery(q).executeUpdate();
		} catch (final Exception e) {
			this.logger.error("eraseFlowLogMessage: {}", e.getMessage());
		}
	}

	/**
	 * Erase all {@link SystemUserLogType}s of the application scope older than
	 * the given date.
	 *
	 * @param toDate the date.
	 */
	@Transactional(value = EipPersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public void eraseSystemUserLog(final Date toDate) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaDelete<SystemUserLogType> q = cb
				.createCriteriaDelete(SystemUserLogType.class);
		final Root<SystemUserLogType> c = q.from(SystemUserLogType.class);
		q.where(cb.lessThan(c.<Date>get(SystemUserLogType_.logDateItem),
				toDate),
				cb.equal(c.<String>get(SystemUserLogType_.context),
						this.contextNameProvider.getContextName()));
		try {
			this.em.createQuery(q).executeUpdate();
		} catch (final Exception e) {
			this.logger.error("eraseSystemUserLog: {}", e.getMessage());
		}
	}

	/**
	 * Get all {@link ApplicationUserLogType}s of the day of the application.
	 *
	 * @param date the date the calls are recorded.
	 * @return the list of {@link ApplicationUserLogType}s.
	 */
	@Transactional(value = EipPersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public List<ApplicationUserLogType> getApplicationUserLogType(
			final Date date) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		Date d = date;
		if (d == null) {
			d = new Date();
		}
		final CriteriaQuery<ApplicationUserLogType> q = cb
				.createQuery(ApplicationUserLogType.class);
		final Root<ApplicationUserLogType> c = q
				.from(ApplicationUserLogType.class);
		q.where(cb.equal(c.<String>get(ApplicationUserLogType_.context),
				this.contextNameProvider.getContextName()),
				cb.between(c.<Date>get(ApplicationUserLogType_.startItem),
						getDayStart(d), getDayEnd(d)));

		final TypedQuery<ApplicationUserLogType> typedQuery = this.em
				.createQuery(q);
		return typedQuery.getResultList();
	}

	/**
	 * @see com.qpark.eip.core.persistence.AbstractEipDao#getContextName()
	 */
	@Override
	protected String getContextName() {
		return this.contextNameProvider.getContextName();
	}

	/**
	 * @see com.qpark.eip.core.persistence.AbstractEipDao#getContextVersion()
	 */
	@Override
	protected String getContextVersion() {
		return this.contextNameProvider.getContextVersion();
	}

	/**
	 * @see com.qpark.eip.core.persistence.AbstractEipDao#getEntityManager()
	 */
	@Override
	protected EntityManager getEntityManager() {
		return this.em;
	}

	/**
	 * Get all {@link SystemUserLogType}s of the day of the application.
	 *
	 * @param date the date the calls are recorded.
	 * @return the list of {@link SystemUserLogType}s.
	 */
	@Transactional(value = EipPersistenceConfig.TRANSACTION_MANAGER_NAME, propagation = Propagation.REQUIRED)
	public List<SystemUserLogType> getSystemUserLogType(final Date date) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		Date d = date;
		if (d == null) {
			d = new Date();
		}
		final CriteriaQuery<SystemUserLogType> q = cb
				.createQuery(SystemUserLogType.class);
		final Root<SystemUserLogType> c = q.from(SystemUserLogType.class);
		q.where(cb.equal(c.<String>get(SystemUserLogType_.context),
				this.contextNameProvider.getContextName()),
				cb.between(c.<Date>get(SystemUserLogType_.logDateItem),
						getDayStart(d), getDayEnd(d)));

		final TypedQuery<SystemUserLogType> typedQuery = this.em.createQuery(q);
		return typedQuery.getResultList();
	}
}
