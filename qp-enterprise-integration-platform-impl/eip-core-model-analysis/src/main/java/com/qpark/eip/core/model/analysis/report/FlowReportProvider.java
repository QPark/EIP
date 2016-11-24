/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.model.analysis.report.model.FlowReportRow;

/**
 * Collects {@link FlowReportRow} for the given services and operations.
 *
 * @author bhausen
 */
public class FlowReportProvider extends AbstractReportProvider {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(FlowReportProvider.class);

	/**
	 * Get the list of {@link FlowReportRow}s of the given flow pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param flowPattern
	 *            the pattern the flow names need to match.
	 * @return the list of {@link FlowReportRow}s.
	 */
	public List<FlowReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String flowPattern) {
		return this.getReportRows(dataProvider, flowPattern,
				new TreeSet<String>());
	}

	/**
	 * Get the list of {@link FlowReportRow}s of the given flow pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param flowNamePattern
	 *            the pattern the flow names need to match.
	 * @param ctIds
	 *            the set of used complex type ids.
	 * @return the list of {@link FlowReportRow}s.
	 */
	public List<FlowReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String flowNamePattern, final Set<String> ctIds) {
		this.logger.debug("+getReportRows");
		List<FlowReportRow> value = new ArrayList<>();
		this.logger.debug("-getReportRows");
		return value;
	}
}
