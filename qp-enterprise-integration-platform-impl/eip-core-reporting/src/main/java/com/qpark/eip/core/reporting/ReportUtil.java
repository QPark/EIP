/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.reporting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.model.reporting.CellType;
import com.qpark.eip.model.reporting.ReportContentType;
import com.qpark.eip.model.reporting.ReportMetaDataType;

/**
 * 
 * @author sneurohr
 */
public class ReportUtil {

	private static com.qpark.eip.model.reporting.ObjectFactory ofr = new com.qpark.eip.model.reporting.ObjectFactory();
	public static String DELIMITER = ",";
	public static String EMPTYCELL = "";
	private static Logger logger = LoggerFactory.getLogger(ReportUtil.class);

	/**
	 * @param reportDate
	 * @return the execution time string in format <i>yyyyMMdd-HHmmss</i>.
	 */
	public static String getExecutionTime(final Date reportDate) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd-HHmmss");
		if (Objects.nonNull(reportDate))
			return sdfDate.format(reportDate);
		return sdfDate.format(new Date());
	}

	/**
	 * @return the execution time (now) string in format <i>yyyyMMdd-HHmmss</i>.
	 */
	public static String getExecutionTime() {
		return getExecutionTime(new Date());
	}

	/**
	 * Create a Row with Cells
	 *
	 * @param number
	 * @param content
	 * @param delimiter
	 * @return ReportContentType
	 */
	public static ReportContentType getRow(final int number,
			final String delimiter, final Object[] content) {
		ReportContentType reportContent = ofr.createReportContentType();
		reportContent.setRow(number);

		for (int i = 0; i < content.length; i++) {
			CellType ct = ofr.createCellType();
			ct.setColumn(i);
			if (Objects.nonNull(content[i])) {
				ct.setCellContent(String.valueOf(content[i]));
			} else {
				ct.setCellContent(EMPTYCELL);
			}
			reportContent.getRowContent().add(ct);
		}

		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (CellType ct : reportContent.getRowContent()) {
			buf.append(ct.getCellContent());
			if (i < reportContent.getRowContent().size() - 1) {
				buf.append(delimiter);
			}
			i++;
		}

		return reportContent;
	}

	/**
	 * Create ReportMetaDataType
	 *
	 * @param name        the name of the header.
	 * @param description the description.
	 * @return {@link ReportMetaDataType}, with empty description,
	 *         {@link String} as data type, <i>%s</i> as format and a span of 1.
	 */
	public static ReportMetaDataType getColumnHeader(final String name,
			final String description) {
		return getColumnHeader(name, description, String.class.getName(), "%s",
				1);
	}

	/**
	 * Create ReportMetaDataType
	 *
	 * @param name the name of the header.
	 * @return {@link ReportMetaDataType}, with empty description,
	 *         {@link String} as data type, <i>%s</i> as format and a span of 1.
	 */
	public static ReportMetaDataType getColumnHeader(final String name) {
		return getColumnHeader(name, "", String.class.getName(), "%s", 1);
	}

	/**
	 * Create ReportMetaDataType
	 *
	 * @param name     the name of the header.
	 * @param dataType the {@link Class} of the data type.
	 * @param format   the format.
	 * @return {@link ReportMetaDataType}, with empty description,
	 *         {@link String} as data type, <i>%s</i> as format and a span of 1.
	 */
	public static ReportMetaDataType getColumnHeader(final String name,
			final Class<?> dataType, final String format) {
		return getColumnHeader(name, "", dataType.getName(), format, 1);
	}

	/**
	 * Create ReportMetaDataType
	 *
	 * @param name
	 * @param description
	 * @param dataType
	 * @param format
	 * @param span
	 * @return ReportMetaDataType
	 */
	public static ReportMetaDataType getColumnHeader(final String name,
			final String description, final String dataType,
			final String format, final int span) {
		ReportMetaDataType reportHeaderColumn = ofr.createReportMetaDataType();

		reportHeaderColumn.setColumnName(name);
		reportHeaderColumn.setColumnDescription(description);
		reportHeaderColumn.setColumnJavaDataType(dataType);
		reportHeaderColumn.setColumnJavaFormatString(format);
		reportHeaderColumn.setColumnSpan(span);
		return reportHeaderColumn;

	}

}
