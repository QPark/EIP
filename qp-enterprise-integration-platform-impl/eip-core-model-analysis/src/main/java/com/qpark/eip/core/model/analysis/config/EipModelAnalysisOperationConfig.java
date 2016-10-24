/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qpark.eip.core.model.analysis.operation.GetClusterOperation;
import com.qpark.eip.core.model.analysis.operation.GetComplexTypeOperation;
import com.qpark.eip.core.model.analysis.operation.GetDataTypeOperation;
import com.qpark.eip.core.model.analysis.operation.GetElementTypeOperation;
import com.qpark.eip.core.model.analysis.operation.GetFieldMappingTypeOperation;
import com.qpark.eip.core.model.analysis.operation.GetFlowInterfaceMappingTypeOperation;
import com.qpark.eip.core.model.analysis.operation.GetFlowOperation;
import com.qpark.eip.core.model.analysis.operation.GetRevisionOperation;
import com.qpark.eip.core.model.analysis.operation.GetServiceIdOperation;
import com.qpark.eip.core.model.analysis.operation.GetServiceOperation;
import com.qpark.eip.core.model.analysis.operation.GetTargetNamespaceOperation;

/**
 * Spring configuration of the service provider
 *
 * @author bhausen
 */
@Configuration
public class EipModelAnalysisOperationConfig {
	/**
	 * @return the {@link GetClusterOperation} bean.
	 */
	@Bean(name = GetClusterOperation.BEAN_NAME)
	public GetClusterOperation getClusterOperation() {
		return new GetClusterOperation();
	}

	/**
	 * @return the {@link GetComplexTypeOperation} bean.
	 */
	@Bean(name = GetComplexTypeOperation.BEAN_NAME)
	public GetComplexTypeOperation getComplexTypeOperation() {
		return new GetComplexTypeOperation();
	}

	/**
	 * @return the {@link GetDataTypeOperation} bean.
	 */
	@Bean(name = GetDataTypeOperation.BEAN_NAME)
	public GetDataTypeOperation getDataTypeOperation() {
		return new GetDataTypeOperation();
	}

	/**
	 * @return the {@link GetElementTypeOperation} bean.
	 */
	@Bean(name = GetElementTypeOperation.BEAN_NAME)
	public GetElementTypeOperation getElementTypeOperation() {
		return new GetElementTypeOperation();
	}

	/**
	 * @return the {@link GetFieldMappingTypeOperation} bean.
	 */
	@Bean(name = GetFieldMappingTypeOperation.BEAN_NAME)
	public GetFieldMappingTypeOperation getFieldMappingTypeOperation() {
		return new GetFieldMappingTypeOperation();
	}

	/**
	 * @return the {@link GetFlowInterfaceMappingTypeOperation} bean.
	 */
	@Bean(name = GetFlowInterfaceMappingTypeOperation.BEAN_NAME)
	public GetFlowInterfaceMappingTypeOperation getFlowInterfaceMappingTypeOperation() {
		return new GetFlowInterfaceMappingTypeOperation();
	}

	/**
	 * @return the {@link GetFlowOperation} bean.
	 */
	@Bean(name = GetFlowOperation.BEAN_NAME)
	public GetFlowOperation getFlowOperation() {
		return new GetFlowOperation();
	}

	/**
	 * @return the {@link GetRevisionOperation} bean.
	 */
	@Bean(name = GetRevisionOperation.BEAN_NAME)
	public GetRevisionOperation getRevisionOperation() {
		return new GetRevisionOperation();
	}

	/**
	 * @return the {@link GetServiceIdOperation} bean.
	 */
	@Bean(name = GetServiceIdOperation.BEAN_NAME)
	public GetServiceIdOperation getServiceIdOperation() {
		return new GetServiceIdOperation();
	}

	/**
	 * @return the {@link GetServiceOperation} bean.
	 */
	@Bean(name = GetServiceOperation.BEAN_NAME)
	public GetServiceOperation getServiceOperation() {
		return new GetServiceOperation();
	}

	/**
	 * @return the {@link GetTargetNamespaceOperation} bean.
	 */
	@Bean(name = GetTargetNamespaceOperation.BEAN_NAME)
	public GetTargetNamespaceOperation getTargetNamespaceOperation() {
		return new GetTargetNamespaceOperation();
	}
}
