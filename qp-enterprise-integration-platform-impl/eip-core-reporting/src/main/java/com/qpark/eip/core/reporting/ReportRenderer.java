/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.reporting;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.model.reporting.CellType;
import com.qpark.eip.model.reporting.ReportContentType;
import com.qpark.eip.model.reporting.ReportHeaderDataType;
import com.qpark.eip.model.reporting.ReportMetaDataType;
import com.qpark.eip.model.reporting.ReportType;

/**
 * @author sneurohr
 */
public class ReportRenderer {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(ReportRenderer.class);
	private static final String Root = "target/";

	public void renderReportCSV(final ReportType report) {

		List<ReportHeaderDataType> headers = report.getReportHeaderData();
		ReportHeaderDataType header = headers.get(0);
		int i = 0;
		String[] h = new String[report.getReportInfo().getNumberOfColumns()];
		for (ReportMetaDataType md : header.getReportMetaData()) {
			h[i] = md.getColumnName();
			i++;
		}

		i = 0;
		String[] hd = new String[report.getReportInfo().getNumberOfColumns()];
		for (ReportMetaDataType md : header.getReportMetaData()) {
			hd[i] = md.getColumnDescription();
			i++;
		}

		try {
			Path path = Paths.get(Root + report.getReportName() + ".csv");
			BufferedWriter writer = Files.newBufferedWriter(path);

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader(h));
			csvPrinter.printRecord(hd);

			for (ReportContentType content : report.getReportContent()) {
				String[] c = new String[report.getReportInfo()
						.getNumberOfColumns()];
				for (CellType cell : content.getRowContent()) {
					c[cell.getColumn()] = cell.getCellContent();
				}
				csvPrinter.printRecord(c);
			}

			csvPrinter.flush();
			this.logger.info("Wrote file {}", path.toFile().getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
