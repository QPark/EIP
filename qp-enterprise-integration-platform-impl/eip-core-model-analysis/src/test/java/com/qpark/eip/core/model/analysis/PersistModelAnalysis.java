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

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.model.docmodel.EnterpriseType;

/**
 * @author bhausen
 */
public class PersistModelAnalysis {
	@Autowired
	private AnalysisDao modelAnalysisDao;

	public void test() {
		List<EnterpriseType> enterprises = this.modelAnalysisDao
				.getEnterprises();
		for (EnterpriseType enterprise : enterprises) {
			System.out.println(enterprise.getName());
			System.out.println(enterprise.getModelVersion());
			System.out.println(this.modelAnalysisDao.existsEnterprise(
					enterprise.getName(), enterprise.getModelVersion() + "x"));
			System.out.println(this.modelAnalysisDao.existsEnterprise(
					enterprise.getName(), enterprise.getModelVersion()));
		}
		// Analysis a = new AnalysisProvider().createEnterprise(
		// this.enterpriseName, this.basePackageName, this.modelPath);
		// this.modelAnalysisDao.saveEnterprise(a.getEnterprise());
	}
}
