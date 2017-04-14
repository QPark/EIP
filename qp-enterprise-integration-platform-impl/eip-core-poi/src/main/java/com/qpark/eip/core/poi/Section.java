/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * A section of columns in a sheet.
 *
 * @author bhausen
 * @param <SectionData>
 *            the type containing the data.
 */
public interface Section<SectionData> {
	/**
	 * @return the list of {@link ColumnDefinition}.
	 */
	List<ColumnDefinition> getColumnDefinition();

	/**
	 * @return the title of the section.
	 */
	String getSectionTitle();

	/**
	 * Set the values in the section of the row.
	 *
	 * @param value
	 *            the section data.
	 * @param row
	 *            the row.
	 * @param excel
	 *            the {@link ExcelFile}.
	 */
	void setSectionValues(SectionData value, HSSFRow row, ExcelFile excel);

	/**
	 * @return the number of columns in the section.
	 */
	int size();
}
