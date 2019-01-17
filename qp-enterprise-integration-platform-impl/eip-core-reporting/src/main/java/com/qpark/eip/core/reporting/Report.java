/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.reporting;

import java.util.Date;

import com.qpark.eip.model.reporting.ObjectFactory;
import com.qpark.eip.model.reporting.ReportType;

/**
 * @author bhausen
 * @param <DataObject>
 */
public abstract class Report<DataObject> {
	/** the {@link ObjectFactory}. */
	protected static ObjectFactory ofr = new ObjectFactory();
	/** The {@link ReportType}. */
	protected final ReportType report;
	/** The {@link Date} of the report execution. */
	protected final Date reportDate;

	/**
	 * @param report
	 * @param reportDate
	 */
	public Report(final ReportType report, final Date reportDate) {
		this.report = report;
		this.reportDate = reportDate;
	}

	/**
	 * @param data the data object of type DataObject
	 * @return this.
	 */
	public abstract Report<DataObject> createReportContent(
			final DataObject data);

	/**
	 * @return the report
	 */
	public ReportType getReport() {
		return this.report;
	}

	/** @return the report name. */
	public abstract String getReportName();
}
