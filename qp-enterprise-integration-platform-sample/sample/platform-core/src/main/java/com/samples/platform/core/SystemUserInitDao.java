/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.domain.persistencedefinition.AuthenticationType;
import com.qpark.eip.core.domain.persistencedefinition.AuthenticationType_;
import com.qpark.eip.core.domain.persistencedefinition.GrantedAuthorityType;
import com.qpark.eip.core.domain.persistencedefinition.ObjectFactory;
import com.qpark.eip.core.persistence.config.EipPersistenceConfig;

public class SystemUserInitDao {
	/** The {@link EntityManager}. */
	@PersistenceContext(unitName = EipPersistenceConfig.PERSISTENCE_UNIT_NAME,
			name = EipPersistenceConfig.ENTITY_MANAGER_FACTORY_NAME)
	private EntityManager em;
	private ObjectFactory of = new ObjectFactory();

	/**
	 * Get the {@link AuthenticationType}s out of the database.
	 *
	 * @param enabled
	 *            if not <code>null</code> and <code>true</code> only the
	 *            enabled {@link AuthenticationType}s are replied.
	 * @return the list of {@link AuthenticationType}s.
	 */
	@Transactional(value = EipPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public void enterSystemUser(final String contextName, final String userName,
			final String password, final String... roleNames) {
		AuthenticationType ac = this.of.createAuthenticationType();
		ac.setContext(contextName);
		ac.setEnabled(true);
		GrantedAuthorityType r;
		for (String roleName : roleNames) {
			r = this.of.createGrantedAuthorityType();
			r.setRoleName(roleName);
			ac.getGrantedAuthority().add(r);
		}
		ac.setPassword(password);
		ac.setUserName(userName);
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<AuthenticationType> q = cb
				.createQuery(AuthenticationType.class);
		Root<AuthenticationType> c = q.from(AuthenticationType.class);
		Predicate ands = cb.conjunction();
		ands.getExpressions().add(cb.equal(
				c.<String> get(AuthenticationType_.context), contextName));
		ands.getExpressions().add(cb
				.equal(c.<String> get(AuthenticationType_.userName), userName));
		q.where(ands);
		q.orderBy(cb.asc(c.<String> get(AuthenticationType_.userName)));
		TypedQuery<AuthenticationType> typedQuery = this.em.createQuery(q);
		try {
			AuthenticationType stored = typedQuery.getSingleResult();
			if (stored != null) {
				this.em.persist(ac);
			}
		} catch (NoResultException e) {
			this.em.persist(ac);
		}
	}

}
