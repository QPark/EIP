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

import com.qpark.eip.core.poi.ColumnDefinition;
import com.qpark.eip.core.poi.ExcelFile;
import com.qpark.eip.core.poi.Section;
import com.qpark.eip.core.poi.impl.AbstractSection;

/**
 * The {@link Section} of the values age and gender of the {@link Person}.
 * 
 * @author bhausen
 */
public class SectionEtc extends AbstractSection<Person> {
	/**
	 * @return the list of {@link ColumnDefinition}.
	 */
	private static List<ColumnDefinition> getSectionColumns() {
		List<ColumnDefinition> value = new ArrayList<>();
		value.add(new ColumnDefinition("Age", 5));
		value.add(new ColumnDefinition("Female", 15));
		return value;
	}

	/** Constructor. */
	public SectionEtc() {
		super(getSectionColumns());
	}

	/**
	 * @see com.qpark.eip.core.poi.impl.AbstractSection#createCells(java.lang.Object,
	 *      org.apache.poi.hssf.usermodel.HSSFRow,
	 *      com.qpark.eip.core.poi.ExcelFile)
	 */
	@Override
	public void createCells(final Person value, final HSSFRow row,
			final ExcelFile excel) {
		excel.createCell(row, value.getAge());
		excel.createCell(row, value.isFemale());
	}

	/**
	 * @see com.qpark.eip.core.poi.Section#getSectionTitle()
	 */
	@Override
	public String getSectionTitle() {
		return "Etc";
	}
}