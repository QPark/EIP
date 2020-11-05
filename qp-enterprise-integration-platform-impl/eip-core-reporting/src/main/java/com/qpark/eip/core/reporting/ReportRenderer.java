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
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.model.reporting.ReportType;

/**
 * @author sneurohr
 */
public class ReportRenderer {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(ReportRenderer.class);
	private static final String Root = "target/";

	/**
	 * @param report
	 */
	public void renderReportCSV(final ReportType report) {
		Path path = Paths.get(Root + report.getReportName() + ".csv");
		this.renderReportCSV(report, path);
	}

	/**
	 * @param report
	 * @param appendable
	 */
	public void renderReportCSV(final ReportType report, final Appendable appendable) {
		if (report.getReportHeaderData().size() > 0) {
			Optional.ofNullable(report.getReportHeaderData().get(0)).ifPresent(header -> {
				String[] h = header.getReportMetaData().stream().map(md -> String.valueOf(md.getColumnName()))
						.collect(Collectors.toList()).toArray(new String[0]);
				this.logger.debug("+renderReportCSV Columns #{} Rows #{}", h.length, report.getReportContent().size());

				try (CSVPrinter csvPrinter = new CSVPrinter(appendable, CSVFormat.DEFAULT.withHeader(h));) {
					csvPrinter.printRecord(header.getReportMetaData().stream().map(md -> md.getColumnDescription())
							.map(d -> (Object) d).collect(Collectors.toList()).toArray(new Object[0]));

					List<Object[]> rows = report.getReportContent().stream().map(row -> row.getRowContent().stream()
							.map(c -> (Object) c.getCellContent()).collect(Collectors.toList()).toArray(new Object[0]))
							.collect(Collectors.toList());
					for (Object[] row : rows) {
						csvPrinter.printRecord(row);
					}
					csvPrinter.flush();

					this.logger.info("Rendered report of #{} columns and #{} rows", h.length,
							report.getReportContent().size());
				} catch (IOException e) {
					this.logger.error(e.getMessage(), e);
				}
				this.logger.debug("-renderReportCSV Columns #{} Rows #{}", h.length, report.getReportContent().size());
			});
		}
	}

	/**
	 * @param report
	 * @param path
	 */
	public void renderReportCSV(final ReportType report, final Path path) {
		if (!path.getParent().toFile().exists()) {
			path.getParent().toFile().mkdirs();
		}
		try {
			BufferedWriter writer = Files.newBufferedWriter(path);
			this.renderReportCSV(report, writer);
			this.logger.info("Wrote file {}", path.toFile().getAbsolutePath());
		} catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}
}
