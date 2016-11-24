/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.FieldType;

/**
 * Basic methods of the report providers.
 *
 * @author bhausen
 */
public abstract class AbstractReportProvider {
	/**
	 * Get the {@link ComplexType} of the {@link ElementType} with id.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param elementId
	 *            the id of the element.
	 * @return the {@link ComplexType}.
	 */
	protected static Optional<ComplexType> getComplexTypeByElementId(
			final DataProviderModelAnalysis dataProvider,
			final String elementId) {
		Optional<ComplexType> value = Optional.empty();
		Optional<ElementType> element = dataProvider.getElement(elementId);
		if (element.isPresent()) {
			value = dataProvider
					.getComplexType(element.get().getComplexTypeId());
		}
		return value;
	}

	/**
	 * Enter the descended types of {@link ComplexType} into the list.
	 *
	 * @param dataProvider
	 *            the {@link DataTypeReportProvider}.
	 * @param ct
	 *            the {@link ComplexType}.
	 * @param descendends
	 *            the list to enter the descended in.
	 */
	protected static void getComplexTypeNamesParents(
			final DataProviderModelAnalysis dataProvider, final ComplexType ct,
			final List<String> descendends) {
		if (Objects.nonNull(ct) && Objects.nonNull(ct.getDescendedFromId())) {
			dataProvider.getDataType(ct.getDescendedFromId()).ifPresent(dt -> {
				descendends.add(0, String.format("%s {%s}",
						getRealShortName(dt), dt.getNamespace()));
				if (ComplexType.class.isInstance(dt)) {
					getComplexTypeNamesParents(dataProvider, (ComplexType) dt,
							descendends);
				}
			});
		}
	}

	/**
	 * Get the list of field ids having a {@link DataType} assigned.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param ct
	 *            the {@link ComplexType}.
	 * @return the list of field ids.
	 */
	protected static List<String> getFieldDataTypeIds(
			final DataProviderModelAnalysis dataProvider,
			final ComplexType ct) {
		List<String> value = new ArrayList<>();
		List<FieldType> fields = ct.getField();
		Map<String, DataType> dataTypeMap = new HashMap<>();
		dataProvider
				.getDataTypes(
						fields.stream().map(f -> f.getFieldTypeDefinitionId())
								.collect(Collectors.toList()))
				.stream().forEach(dt -> dataTypeMap.put(dt.getId(), dt));
		fields.stream().forEach(f -> {
			DataType dt = dataTypeMap.get(f.getFieldTypeDefinitionId());
			if (Objects.nonNull(dt)) {
				value.add(dt.getId());
			}
		});
		return value;
	}

	/**
	 * Get a description of all field of a ComplexType
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param ct
	 *            ComplexType
	 * @return (name: type cardinality)
	 */
	protected static String getFieldList(
			final DataProviderModelAnalysis dataProvider,
			final ComplexType ct) {
		StringBuffer value = new StringBuffer();
		List<FieldType> fields = ct.getField();
		dataProvider.getDataTypes(
				fields.stream().map(f -> f.getFieldTypeDefinitionId())
						.collect(Collectors.toList()));
		fields.stream().forEach(f -> {
			Optional<DataType> dt = dataProvider
					.getDataType(f.getFieldTypeDefinitionId());
			if (dt.isPresent()) {
				value.append(String.format("(%s:%s%s)\n", f.getName(),
						getRealShortName(dt.get()), f.getCardinality()));
			} else {
				value.append(String.format("(%s:Type?%s)\n", f.getName(),
						f.getCardinality()));
			}
		});
		return value.toString();
	}

	/**
	 * Remove the namespace from the {@link DataType#getName()}.
	 *
	 * @param dt
	 *            the {@link DataType}
	 * @return the real short name.
	 */
	protected static String getRealShortName(final DataType dt) {
		return dt.getName().replace(dt.getNamespace(), "").replace('{', ' ')
				.replace('}', ' ').replaceAll(" ", "");
	}
}
