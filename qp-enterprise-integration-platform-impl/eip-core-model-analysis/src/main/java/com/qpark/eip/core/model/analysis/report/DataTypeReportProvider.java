/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.model.analysis.report.model.DataTypeReportRow;

/**
 * Collects {@link DataTypeReportRow} for the given services and operations.
 *
 * @author bhausen
 */
public class DataTypeReportProvider extends AbstractReportProvider {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(DataTypeReportProvider.class);

	/**
	 * Get the list of {@link DataTypeReportRow}s of the given DataType pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param DataTypePattern
	 *            the pattern the DataType names need to match.
	 * @return the list of {@link DataTypeReportRow}s.
	 */
	public List<DataTypeReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String DataTypePattern) {
		return this.getReportRows(dataProvider, DataTypePattern,
				new TreeSet<String>());
	}

	/**
	 * Get the list of {@link DataTypeReportRow}s of the given DataType pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param DataTypePattern
	 *            the pattern the DataType names need to match.
	 * @param ctIds
	 *            the set of used complex type ids.
	 * @return the list of {@link DataTypeReportRow}s.
	 */
	public List<DataTypeReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String DataTypePattern, final Set<String> ctIds) {
		this.logger.debug("+getReportRows");
		List<DataTypeReportRow> value = new ArrayList<>();
		ctIds.stream().map(ctid -> dataProvider.getComplexType(ctid))
				.filter(oct -> oct.isPresent()).map(oct -> oct.get())
				.forEach(ct -> {
					ct.getField().stream().filter(f -> Objects.nonNull(f))
							.sorted((f1, f2) -> Integer.compare(
									f1.getSequenceNumber(), f2
											.getSequenceNumber()))
							.forEach(f -> {
								DataTypeReportRow d = new DataTypeReportRow();
								value.add(d);
								d.setName(getRealShortName(ct));
								d.setTargetNamespace(ct.getNamespace());
								d.setDescription(ct.getDescription());
								getComplexTypeNamesParents(dataProvider, ct,
										d.getInheritedFrom());
								d.setFieldName(f.getName());
								d.setFieldCardinality(f.getCardinality());
								d.setFieldDescription(f.getDescription());
							});
				});
		this.logger.debug("-getReportRows");
		return value;
	}
}
