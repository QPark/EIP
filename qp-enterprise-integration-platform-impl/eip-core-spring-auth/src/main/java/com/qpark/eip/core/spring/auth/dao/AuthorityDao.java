/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.domain.persistencedefinition.AuthenticationType;
import com.qpark.eip.core.domain.persistencedefinition.AuthenticationType_;
import com.qpark.eip.core.domain.persistencedefinition.GrantedAuthorityType;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType_;
import com.qpark.eip.core.persistence.AbstractEipDao;
import com.qpark.eip.core.spring.ContextNameProvider;

/**
 * DAO to access the authority database and to log the user calls.
 *
 * @author bhausen
 */
public class AuthorityDao extends AbstractEipDao {
	/** The {@link EntityManager}. */
	@PersistenceContext(unitName = "com.qpark.eip.core.persistence")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return this.em;
	}

	/** The eip {@link ContextNameProvider}. */
	@Autowired
	@Qualifier("ComQparkEipCoreSpringAuthContextNameProvider")
	private ContextNameProvider contextNameProvider;

	/**
	 * Get the {@link AuthenticationType}s out of the database.
	 *
	 * @param enabled
	 *            if not <code>null</code> and <code>true</code> only the
	 *            enabled {@link AuthenticationType}s are replied.
	 * @return the list of {@link AuthenticationType}s.
	 */
	@Transactional(value = "ComQparkEipCoreTransactionManager",
			propagation = Propagation.REQUIRED)
	public List<AuthenticationType> getAuthenticationTypes(
			final Boolean enabled) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<AuthenticationType> q = cb
				.createQuery(AuthenticationType.class);
		Root<AuthenticationType> c = q.from(AuthenticationType.class);
		Predicate ands = cb.conjunction();
		ands.getExpressions()
				.add(cb.equal(c.<String> get(AuthenticationType_.context),
						this.getContextName()));
		if (enabled != null) {
			ands.getExpressions().add(cb.equal(
					c.<Boolean> get(AuthenticationType_.enabled), enabled));
		}
		q.where(ands);
		q.orderBy(cb.asc(c.<String> get(AuthenticationType_.userName)));
		TypedQuery<AuthenticationType> typedQuery = this.em.createQuery(q);
		List<AuthenticationType> list = typedQuery.getResultList();
		for (AuthenticationType auth : list) {
			for (GrantedAuthorityType gr : auth.getGrantedAuthority()) {
				gr.getRoleName();
			}
			for (int i = 0; i < auth.getGrantedAuthority().size(); i++) {
				// eager loading...
				auth.getGrantedAuthority().get(i);
			}
		}
		return list;
	}

	/**
	 * Get the granted number of calls.
	 *
	 * @param userName
	 *            the user name.
	 * @param serviceName
	 *            the service name.
	 * @param operationName
	 *            the operation name.
	 * @return the number of calls.
	 */
	@Transactional(value = "ComQparkEipCoreTransactionManager",
			propagation = Propagation.REQUIRED)
	public int getGrantedRequestNumber(final String userName,
			final String serviceName, final String operationName) {
		Integer requests = 0;
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<AuthenticationType> q = cb
				.createQuery(AuthenticationType.class);
		Root<AuthenticationType> c = q.from(AuthenticationType.class);
		q.where(cb.equal(c.<String> get(AuthenticationType_.context),
				this.contextNameProvider.getContextName()),
				cb.equal(c.<String> get(AuthenticationType_.userName),
						userName));

		TypedQuery<AuthenticationType> typedQuery = this.em.createQuery(q);
		try {
			AuthenticationType log = typedQuery.getSingleResult();
			for (GrantedAuthorityType role : log.getGrantedAuthority()) {
				if (this.getRoleName(serviceName, operationName)
						.equals(role.getRoleName())) {
					requests = role.getMaxRequests();
				}
			}
			if (requests == null) {
				requests = Integer.MAX_VALUE;
			}
		} catch (Exception e) {
			requests = 0;
		}
		return requests;
	}

	/**
	 * @param serviceName
	 * @param operationName
	 * @return
	 */
	private Object getRoleName(final String serviceName,
			final String operationName) {
		StringBuffer sb = new StringBuffer(
				serviceName.length() + operationName.length() + 6);
		sb.append("ROLE_").append(serviceName.toUpperCase()).append("_")
				.append(operationName.toUpperCase());
		return sb.toString();
	}

	/**
	 * Get the total number of calls for that date.
	 *
	 * @param userName
	 *            the user name.
	 * @param serviceName
	 *            the service name.
	 * @param operationName
	 *            the operation name.
	 * @param date
	 *            the date the calls are recorded.
	 * @return the number of calls.
	 */
	@Transactional(value = "ComQparkEipCoreTransactionManager",
			propagation = Propagation.REQUIRED)
	public int getCurrentRequestNumber(final String userName,
			final String serviceName, final String operationName,
			final Date date) {
		int requests = 0;
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<SystemUserLogType> q = cb
				.createQuery(SystemUserLogType.class);
		Root<SystemUserLogType> c = q.from(SystemUserLogType.class);
		q.where(cb.equal(c.<String> get(SystemUserLogType_.context),
				this.contextNameProvider.getContextName()),
				cb.equal(c.<String> get(SystemUserLogType_.userName), userName),
				cb.equal(c.<String> get(SystemUserLogType_.serviceName),
						serviceName),
				cb.equal(c.<String> get(SystemUserLogType_.operationName),
						operationName),
				cb.equal(c.<Date> get(SystemUserLogType_.logDateItem), date));

		TypedQuery<SystemUserLogType> typedQuery = this.em.createQuery(q);
		try {
			SystemUserLogType log = typedQuery.getSingleResult();
			requests = log.getRequestsGranted();
		} catch (Exception e) {
			requests = 0;
		}
		return requests;
	}

	/**
	 * @see com.qpark.eip.core.persistence.BusUtilDao#getContextName()
	 */
	@Override
	protected String getContextName() {
		return this.contextNameProvider.getContextName();
	}

	/**
	 * @see com.qpark.eip.core.persistence.BusUtilDao#getContextVersion()
	 */
	@Override
	protected String getContextVersion() {
		return this.contextNameProvider.getContextVersion();
	}

	/**
	 * @see com.qpark.eip.core.persistence.BusUtilDao#doAsyncDatabaseOperation(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	@Transactional(value = "ComQparkEipCoreTransactionManager",
			propagation = Propagation.REQUIRED)
	public void doAsyncDatabaseOperation(final String userName,
			final Object obj) {
	}
}
