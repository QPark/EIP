/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.ServiceType;

/**
 * Provides data of model analysis.
 *
 * @author bhausen
 */
public interface DataProviderModelAnalysis {
	/**
	 * Get the {@link ComplexType} by complex type id.
	 *
	 * @param complexTypeId
	 *            the complex type id.
	 * @return the {@link ComplexType}.
	 */
	Optional<ComplexType> getComplexType(String complexTypeId);

	/**
	 * Get the {@link DataType} by id.
	 *
	 * @param id
	 *            the id of the {@link DataType}.
	 * @return the {@link DataType}.
	 */
	default Optional<DataType> getDataType(final String id) {
		Optional<DataType> value = Optional.empty();
		final List<DataType> list = getDataTypes(Arrays.asList(id));
		if (Objects.nonNull(list) && list.size() == 1) {
			value = Optional.of(list.get(0));
		}
		return value;
	}

	/**
	 * Get the list of {@link DataType}s according to the ids.
	 *
	 * @param ids
	 *            the ids
	 * @return the list of {@link DataType}s.
	 */
	List<DataType> getDataTypes(List<String> ids);

	/**
	 * Get the {@link ElementType} by element id.
	 *
	 * @param elementId
	 *            the element id.
	 * @return the {@link ElementType}.
	 */
	Optional<ElementType> getElement(String elementId);

	/**
	 * Get the {@link FieldMappingType} of the id.
	 * 
	 * @param id
	 *            the id of the
	 * @return the {@link FieldMappingType}.
	 */
	Optional<FieldMappingType> getFieldMapping(String id);

	/**
	 * Get the list of {@link FlowType}s which names matching the pattern.
	 *
	 * @param flowNamePattern
	 *            the flow name pattern.
	 * @return the list of {@link FlowType}s.
	 */
	List<FlowType> getFlows(String flowNamePattern);

	/**
	 * Get the list of {@link InterfaceMappingType}s of the flow with the given
	 * id.
	 *
	 * @param flowId
	 *            the id of the flow.
	 * @return the list of {@link InterfaceMappingType}s.
	 */
	List<InterfaceMappingType> getInterfaceMappings(String flowId);

	/**
	 * Get the {@link ServiceType} by service id.
	 *
	 * @param serviceId
	 *            the id of the service.
	 * @return the {@link ServiceType} .
	 */
	Optional<ServiceType> getService(String serviceId);
}
