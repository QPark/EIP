/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.model.analysis.config.EipModelAnalysisPersistenceConfig;
import com.qpark.eip.model.docmodel.EnterpriseType;

/**
 * The dao to save the model analysis.
 *
 * @author bhausen
 */
public class AnalysisDao {
	/** The {@link EntityManager}. */
	@PersistenceContext(
			unitName = EipModelAnalysisPersistenceConfig.PERSISTENCE_UNIT_NAME,
			name = EipModelAnalysisPersistenceConfig.ENTITY_MANAGER_FACTORY_NAME)
	private EntityManager em;
	private static volatile SingularAttribute<EnterpriseType, Long> hjid;

	/**
	 * Save the {@link EnterpriseType}.
	 *
	 * @param value
	 *            the {@link EnterpriseType}.
	 */
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public void saveEnterprise(final EnterpriseType value) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<EnterpriseType> c = q.from(EnterpriseType.class);
		q.select(c.<Long> get(hjid));
		TypedQuery<Long> typedQuery = this.em.createQuery(q);
		try {
			List<Long> pks = typedQuery.getResultList();
			for (Long pk : pks) {
				Object enterpriseRef = this.em
						.getReference(EnterpriseType.class, pk);
				if (enterpriseRef != null) {
					this.em.remove(enterpriseRef);
				}
			}
		} catch (Exception e) {
			// noting
		}
		this.em.merge(value);
	}
}
