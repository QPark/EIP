/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.service.domain.doc.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FieldType;
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
	 *                          the complex type id.
	 * @return the {@link ComplexType}.
	 */
	Optional<ComplexType> getComplexType(String complexTypeId);

	/**
	 * Get the {@link DataType} by id.
	 *
	 * @param id
	 *               the id of the {@link DataType}.
	 * @return the {@link DataType}.
	 */
	default Optional<DataType> getDataType(final String id) {
		Optional<DataType> value = Optional.empty();
		final List<DataType> list = this.getDataTypes(Arrays.asList(id));
		if (Objects.nonNull(list) && list.size() == 1) {
			value = Optional.of(list.get(0));
		}
		return value;
	}

	/**
	 * @param dt
	 * @return the short name without namespace.
	 */
	default String getRealShortName(final DataType dt) {
		return dt.getName().replace(dt.getNamespace(), "").replace('{', ' ').replace('}', ' ').replaceAll(" ", "");
	}

	/**
	 * Get a description of all field of a ComplexType
	 *
	 * @param ct
	 *               ComplexType
	 * @return (name: type cardinality)
	 */
	default String getFieldList(final ComplexType ct) {
		final List<String> value = new ArrayList<>();
		final List<FieldType> fields = ct.getField();
		this.getDataTypes(fields.stream().map(f -> f.getFieldTypeDefinitionId()).collect(Collectors.toList()));
		fields.stream().forEach(f -> {
			Optional<DataType> dto = this.getDataType(f.getFieldTypeDefinitionId());
			if (dto.isPresent()) {
				value.add(String.format("(%s:%s%s{%s})", f.getName(), f.getCardinality(),
						this.getRealShortName(dto.get()), dto.get().getNamespace()));
			} else {
				value.add(String.format("(%s:Type?%s)", f.getName(), f.getCardinality()));
			}
		});
		return value.stream().collect(Collectors.joining("\n"));
	}

	/**
	 * Get the list of {@link DataType}s according to the ids.
	 *
	 * @param ids
	 *                the ids
	 * @return the list of {@link DataType}s.
	 */
	List<DataType> getDataTypes(List<String> ids);

	/**
	 * Get the {@link ElementType} by element id.
	 *
	 * @param elementId
	 *                      the element id.
	 * @return the {@link ElementType}.
	 */
	Optional<ElementType> getElement(String elementId);

	/**
	 * Get all {@link EnterpriseType}s.
	 *
	 * @return the list of {@link EnterpriseType}s.
	 */
	List<EnterpriseType> getEnterprises();

	/**
	 * Get the {@link FieldMappingType} of the id.
	 *
	 * @param id
	 *               the id of the
	 * @return the {@link FieldMappingType}.
	 */
	Optional<FieldMappingType> getFieldMapping(String id);

	/**
	 * Get the list of {@link FlowType}s which names matching the pattern.
	 *
	 * @param flowNameParts
	 *                          the flow name parts.
	 * @return the list of {@link FlowType}s.
	 */
	List<FlowType> getFlows(Collection<String> flowNameParts);

	/**
	 * Get the list of {@link InterfaceMappingType}s of the flow with the given id.
	 *
	 * @param flowId
	 *                   the id of the flow.
	 * @return the list of {@link InterfaceMappingType}s.
	 */
	List<InterfaceMappingType> getInterfaceMappings(String flowId);

	/**
	 * Get the {@link ServiceType} by service id.
	 *
	 * @param serviceId
	 *                      the id of the service.
	 * @return the {@link ServiceType} .
	 */
	Optional<ServiceType> getService(String serviceId);

	/**
	 * Get all ServiceIds.
	 *
	 * @return the list of all ServiceIds.
	 */
	List<String> getServiceIds();
}
