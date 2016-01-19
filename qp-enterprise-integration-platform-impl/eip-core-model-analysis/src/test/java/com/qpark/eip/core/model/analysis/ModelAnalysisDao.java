/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.model.analysis.config.PersistenceConfig;
import com.qpark.eip.model.docmodel.EnterpriseType;

/**
 * The dao to save the model analysis.
 *
 * @author bhausen
 */
public class ModelAnalysisDao {
	/** The {@link EntityManager}. */
	@PersistenceContext(unitName = PersistenceConfig.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	/**
	 * Save the {@link EnterpriseType}.
	 *
	 * @param value
	 *            the {@link EnterpriseType}.
	 */
	@Transactional(value = PersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public void saveEnterprise(final EnterpriseType value) {
		this.em.merge(value);
	}
}
