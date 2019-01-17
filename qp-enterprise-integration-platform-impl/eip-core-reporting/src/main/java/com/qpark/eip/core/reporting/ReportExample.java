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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.reporting.ReportExample.PropertyStringKeys;
import com.qpark.eip.model.reporting.ObjectFactory;
import com.qpark.eip.model.reporting.ReportContentType;
import com.qpark.eip.model.reporting.ReportHeaderDataType;
import com.qpark.eip.model.reporting.ReportInfoType;
import com.qpark.eip.model.reporting.ReportMetaDataType;
import com.qpark.eip.model.reporting.ReportType;

/**
 * @author sneurohr
 */
public class ReportExample extends Report<PropertyStringKeys> {
	/** Data container. */
	static class PropertyStringKeys {
		public Stream<String> keys() {
			return System.getProperties().keySet().stream()
					.map(k -> String.valueOf(k)).collect(Collectors.toList())
					.stream();
		}
	}

	/** The {@link ObjectFactory}. */
	private static ObjectFactory of = new ObjectFactory();

	/**
	 * Create Header
	 * 
	 * @return ReportHeaderDataType
	 */
	private static ReportHeaderDataType createHeaderRow() {
		ReportHeaderDataType header = of.createReportHeaderDataType();
		header.setRowNumber(1);
		List<ReportMetaDataType> metaData = header.getReportMetaData();
		metaData.add(ReportUtil.getColumnHeader("PropertyName",
				"SystemProperty key"));
		metaData.add(ReportUtil.getColumnHeader("PropertyValue"));

		return header;
	}

	/**
	 * @return the {@link ReportType}.
	 */
	private static ReportType createReportDefinition() {
		ReportType report = of.createReportType();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar now = datatypeFactory
					.newXMLGregorianCalendar(gregorianCalendar);
			report.setCreated(now);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ReportInfoType reportInfo = of.createReportInfoType();
		report.setReportInfo(reportInfo);
		reportInfo.setDelimiter(ReportUtil.DELIMITER);

		// Add Header
		report.getReportHeaderData().add(createHeaderRow());

		int csvHeaderRow = 0;
		reportInfo.setNumberOfColumns(report.getReportHeaderData()
				.get(csvHeaderRow).getReportMetaData().size());

		return report;
	}

	public static void main(final String[] args) {
		Date reportDate = new Date();
		ReportRenderer renderer = new ReportRenderer();
		renderer.renderReportCSV(new ReportExample(reportDate)
				.createReportContent(new PropertyStringKeys()).getReport());
	}

	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(ReportExample.class);

	/**
	 * @param reportDate
	 */
	public ReportExample(final Date reportDate) {
		super(createReportDefinition(), reportDate);
		this.report.setReportName(this.getReportName());
		this.report.setReportUUID(UUID.randomUUID().toString());
		this.report.setArtefact(ReportExample.class.getPackage().getName());
		this.report.setArtefactVersion("0.1");
		this.report.setEnvironment("SatApps Repository");
	}

	@Override
	public Report<PropertyStringKeys> createReportContent(
			final PropertyStringKeys data) {
		this.logger.debug("+createReport");

		this.report.setReportName(String.format("%s-%s",
				ReportUtil.getExecutionTime(this.reportDate),
				this.getReportName()));

		AtomicInteger rowNumber = new AtomicInteger(1);

		data.keys().sorted().forEach(keyName -> {
			String value = System.getProperty(keyName);
			ReportContentType row = ReportUtil.getRow(rowNumber.get(),
					ReportUtil.DELIMITER, new String[] { keyName, value });
			this.report.getReportContent().add(row);
			rowNumber.set(rowNumber.get() + 1);
		});

		// # of header rows + # of content rows
		int numRows = this.report.getReportHeaderData().size()
				+ this.report.getReportContent().size();
		this.report.getReportInfo().setNumberOfRows(numRows);

		this.logger.debug("-createReport");
		return this;
	}

	@Override
	public String getReportName() {
		return "ExampleReport";
	}
}
