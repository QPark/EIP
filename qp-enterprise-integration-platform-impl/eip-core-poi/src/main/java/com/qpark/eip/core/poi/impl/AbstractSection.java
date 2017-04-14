/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi.impl;

import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.qpark.eip.core.poi.ColumnDefinition;
import com.qpark.eip.core.poi.ExcelFile;
import com.qpark.eip.core.poi.Section;

/**
 * Abstract implementation of a {@link Section}.
 *
 * @author bhausen
 * @param <SectionData>
 */
public abstract class AbstractSection<SectionData>
		implements Section<SectionData> {
	/** The list of {@link ColumnDefinition}s. */
	protected final List<ColumnDefinition> columns;
	/** The size of the {@link Section} - the number of columns. */
	private int size;

	/**
	 * @param columns
	 *            list of {@link ColumnDefinition}s.
	 */
	protected AbstractSection(final List<ColumnDefinition> columns) {
		this.columns = columns;
		this.size = columns.size();
	}

	/**
	 * @param value
	 *            the section data.
	 * @param row
	 *            the row.
	 * @param excel
	 *            the {@link ExcelFile}.
	 */
	protected abstract void createCells(SectionData value, HSSFRow row,
			ExcelFile excel);

	/**
	 * @see com.qpark.eip.core.poi.Section#getColumnDefinition()
	 */
	@Override
	public List<ColumnDefinition> getColumnDefinition() {
		return this.columns;
	}

	/**
	 * @see com.qpark.eip.core.poi.Section#setSectionValues(java.lang.Object,
	 *      org.apache.poi.hssf.usermodel.HSSFRow,
	 *      com.qpark.eip.core.poi.ExcelFile)
	 */
	@Override
	public void setSectionValues(final SectionData value, final HSSFRow row,
			final ExcelFile excel) {
		if (Objects.nonNull(value)) {
			this.createCells(value, row, excel);
		} else {
			for (int i = 0; i < this.size(); i++) {
				excel.createCell(row);
			}
		}
	}

	/**
	 * @see com.qpark.eip.core.poi.Section#size()
	 */
	@Override
	public int size() {
		return this.size;
	}
}
