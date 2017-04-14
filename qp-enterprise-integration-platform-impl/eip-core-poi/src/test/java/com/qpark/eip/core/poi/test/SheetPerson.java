/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.poi.ColumnDefinition;
import com.qpark.eip.core.poi.ExcelFile;
import com.qpark.eip.core.poi.impl.AbstractSheetProvider;

/**
 * The {@link Person} sheet.
 *
 * @author bhausen
 */
public class SheetPerson extends AbstractSheetProvider<Person> {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(SheetPerson.class);
	/** The {@link SectionName}. */
	private final SectionName sectionName;
	/** The {@link SectionEtc}. */
	private final SectionEtc sectionEtc;
	/** The {@link SectionResidence}. */
	private final SectionResidence sectionResidence;

	/**
	 * @param excelFile
	 *            the {@link ExcelFile}.
	 */
	protected SheetPerson(final ExcelFile excelFile) {
		super(excelFile);
		this.sectionName = new SectionName();
		this.sectionEtc = new SectionEtc();
		this.sectionResidence = new SectionResidence();
	}

	/**
	 * @see com.qpark.eip.core.poi.Sheet#addRowValues(java.lang.Object)
	 */
	@Override
	public HSSFRow addRowValues(final Person value) {
		HSSFRow row = this.sheet.createRow(this.sheet.getLastRowNum() + 1);
		this.sectionName.setSectionValues(value, row, this.excel);
		this.sectionEtc.setSectionValues(value, row, this.excel);
		this.sectionResidence.setSectionValues(value, row, this.excel);
		return row;
	}

	/**
	 * @see com.qpark.eip.core.poi.impl.AbstractSheetProvider#createSuperHeaderRows()
	 */
	@Override
	protected int createSuperHeaderRows() {
		int rowNum = 0;
		int start = 0;
		HSSFRow row;
		CellRangeAddress merged;

		/* First header row. */
		row = this.sheet.createRow(rowNum);
		rowNum++;

		start = 0;
		merged = getMergedHeader(rowNum - 1, start, this.sectionName,
				this.sectionEtc, this.sectionResidence);
		this.createMergedHeaderCell(row, "Person data", merged);

		/* Second header row. */
		row = this.sheet.createRow(rowNum);
		rowNum++;

		start = 0;
		merged = getMergedHeader(rowNum - 1, start, this.sectionName);
		this.createMergedHeaderCell(row, "Name", merged);

		start = merged.getLastColumn() + 1;
		merged = getMergedHeader(rowNum - 1, start, this.sectionEtc);
		this.createMergedHeaderCell(row, "Etc", merged);

		start = merged.getLastColumn() + 1;
		merged = getMergedHeader(rowNum - 1, start, this.sectionResidence);
		this.createMergedHeaderCell(row, "Address", merged);

		return rowNum;
	}

	/**
	 * @see com.qpark.eip.core.poi.impl.AbstractSheetProvider#getColumnDefinition()
	 */
	@Override
	public List<ColumnDefinition> getColumnDefinition() {
		List<ColumnDefinition> value = new ArrayList<>();
		value.addAll(this.sectionName.getColumnDefinition());
		value.addAll(this.sectionEtc.getColumnDefinition());
		value.addAll(this.sectionResidence.getColumnDefinition());
		return value;
	}

	/**
	 * @see com.qpark.eip.core.poi.impl.AbstractSheetProvider#getFreezedColumns()
	 */
	@Override
	protected int getFreezedColumns() {
		return this.sectionName.size();
	}

	/**
	 * @see com.qpark.eip.core.poi.impl.AbstractSheetProvider#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return this.logger;
	}

	/**
	 * @see com.qpark.eip.core.poi.Sheet#getSheetTitle()
	 */
	@Override
	public String getSheetTitle() {
		return Person.class.getSimpleName();
	}
}
