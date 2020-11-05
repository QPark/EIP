/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.domaindoc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.model.analysis.AnalysisEnterpriseDao;
import com.qpark.eip.core.model.analysis.EnterpriseTypeParser;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis;

/**
 * @author bhausen
 */
@Component
public class EnterpriseDao extends AnalysisEnterpriseDao
		implements DataProviderModelAnalysis, InitializingBean {
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.setEnterpriseType(EnterpriseTypeParser
				.parseEnterprise(EnterpriseDao.class.getResourceAsStream(
						"/com.samples.bus-ModelAnalysis.xml")));
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getInterfaceMappings(java.lang.String)
	 */
	@Override
	public List<InterfaceMappingType> getInterfaceMappings(
			final String flowId) {
		return new ArrayList<>();
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.operation.ExtendedDataProviderModelAnalysis#getFlowInterfaceMappingTypes(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<InterfaceMappingType> getFlowInterfaceMappingTypes(
			final String modelVersion, final String flowId) {
		return new ArrayList<>();
	}
}
