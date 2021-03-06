/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.service.domain.doc.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodelreport.MappingReportRow;

/**
 * Collects {@link MappingReportRow} for the given services and operations.
 *
 * @author bhausen
 */
public class MappingReportProvider extends AbstractReportProvider {
	/**
	 * Get the list of {@link MappingReportRow}s of the given Mapping pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param flowNameParts
	 *            the pattern the flows names need to match.
	 * @return the list of {@link MappingReportRow}s.
	 */
	public List<MappingReportRow> getReportRows(final DataProviderModelAnalysis dataProvider,
			final Collection<String> flowNameParts) {
		return this.getReportRows(dataProvider, flowNameParts, new TreeSet<String>());
	}

	/**
	 * Get the list of {@link MappingReportRow}s of the given Mapping pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param flowNameParts
	 *            the pattern the flow names need to match.
	 * @param ctIds
	 *            the set of used complex type ids.
	 * @return the list of {@link MappingReportRow}s.
	 */
	public List<MappingReportRow> getReportRows(final DataProviderModelAnalysis dataProvider,
			final Collection<String> flowNameParts, final Set<String> ctIds) {
		final List<MappingReportRow> value = new ArrayList<>();
		dataProvider.getFlows(flowNameParts).stream().filter(f -> Objects.nonNull(f))
				.sorted(Comparator.comparing(FlowType::getName, Comparator.nullsLast(Comparator.naturalOrder()))
						.thenComparing(FlowType::getName))
				.forEach(flow -> {
					dataProvider.getInterfaceMappings(flow.getId()).stream()
							.filter(i -> Objects.nonNull(i) && Objects.nonNull(i.getName()))
							.sorted(Comparator.comparing(InterfaceMappingType::getName)).forEach(i -> {
								i.getFieldMappings().stream().filter(f -> Objects.nonNull(f)).sorted(
										(f1, f2) -> Integer.compare(f1.getSequenceNumber(), f2.getSequenceNumber()))
										.forEach(f -> {
											final MappingReportRow m = new MappingReportRow();
											value.add(m);
											m.setFlowName(f.getName());
											m.setInterfaceName(i.getName());
											m.setInterfaceFieldName(f.getName());
											m.setInterfaceFieldCardinality(f.getCardinality());
											m.setInterfaceFieldDescription(f.getDescription());
											dataProvider.getFieldMapping(f.getFieldTypeDefinitionId()).ifPresent(fm -> {
												m.setMappingTypeName(fm.getShortName());
												m.setMappingTypeType(fm.getMappingType());
												m.setMappingTypeDescription(fm.getDescription());
												m.getMappingTypeInputTypes().addAll(fm.getFieldMappingInputType());
											});
										});
							});
				});
		value.stream().forEach(m -> {
			List<String> names = new ArrayList<>();
			m.getMappingTypeInputTypes().stream()
					.forEach(id -> dataProvider.getDataType(id).ifPresent(dt -> names.add(dt.getName())));
			m.getMappingTypeInputTypes().clear();
			m.getMappingTypeInputTypes().addAll(names);
		});
		return value;
	}
}
