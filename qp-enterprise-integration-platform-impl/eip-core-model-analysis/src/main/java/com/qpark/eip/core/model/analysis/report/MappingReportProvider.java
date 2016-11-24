/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.model.analysis.report.model.MappingReportRow;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;

/**
 * Collects {@link MappingReportRow} for the given services and operations.
 *
 * @author bhausen
 */
public class MappingReportProvider extends AbstractReportProvider {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(MappingReportProvider.class);

	/**
	 * Get the list of {@link MappingReportRow}s of the given Mapping pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param MappingPattern
	 *            the pattern the Mapping names need to match.
	 * @return the list of {@link MappingReportRow}s.
	 */
	public List<MappingReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String MappingPattern) {
		return this.getReportRows(dataProvider, MappingPattern,
				new TreeSet<String>());
	}

	/**
	 * Get the list of {@link MappingReportRow}s of the given Mapping pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param flowNamePattern
	 *            the pattern the flow names need to match.
	 * @param ctIds
	 *            the set of used complex type ids.
	 * @return the list of {@link MappingReportRow}s.
	 */
	public List<MappingReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String flowNamePattern, final Set<String> ctIds) {
		this.logger.debug("+getReportRows");
		List<MappingReportRow> value = new ArrayList<>();
		dataProvider.getFlows(flowNamePattern).stream()
				.filter(f -> Objects
						.nonNull(f))
				.sorted(Comparator
						.comparing(FlowType::getName,
								Comparator.nullsLast(Comparator.naturalOrder()))
						.thenComparing(FlowType::getName))
				.forEach(flow -> {
					dataProvider.getInterfaceMappings(flow.getId()).stream()
							.filter(i -> Objects.nonNull(i)
									&& Objects.nonNull(i.getName()))
							.sorted(Comparator
									.comparing(InterfaceMappingType::getName))
							.forEach(i -> {
								i.getFieldMappings().stream()
										.filter(f -> Objects.nonNull(f))
										.sorted((f1, f2) -> Integer.compare(
												f1.getSequenceNumber(), f2
														.getSequenceNumber()))
										.forEach(f -> {
											MappingReportRow m = new MappingReportRow();
											value.add(m);
											m.setFlowName(f.getName());
											m.setInterfaceName(i.getName());
											m.setInterfaceFieldName(
													f.getName());
											m.setInterfaceFieldCardinality(
													f.getCardinality());
											m.setInterfaceFieldDescription(
													f.getDescription());
											dataProvider
													.getFieldMapping(
															f.getFieldTypeDefinitionId())
													.ifPresent(fm -> {
														m.setMappingTypeName(fm
																.getShortName());
														m.setMappingTypeType(fm
																.getMappingType());
														m.setMappingTypeDescription(
																fm.getDescription());
														m.getMappingTypeInputTypes()
																.addAll(fm
																		.getFieldMappingInputType());
													});
										});
							});
				});
		this.logger.debug("-getReportRows");
		return value;
	}
}
