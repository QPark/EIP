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
 * The {@link Section} of the residence values of the {@link Person}.
 *
 * @author bhausen
 */
public class SectionResidence extends AbstractSection<Person> {
	/**
	 * @return the list of {@link ColumnDefinition}.
	 */
	private static List<ColumnDefinition> getSectionColumns() {
		List<ColumnDefinition> value = new ArrayList<>();
		value.add(new ColumnDefinition("Street", 25));
		value.add(new ColumnDefinition("House number", 10));
		value.add(new ColumnDefinition("ZipCode", 10, true));
		value.add(new ColumnDefinition("City", 25, true));
		value.add(new ColumnDefinition("Country", 25, true));
		return value;
	}

	/** Constructor. */
	public SectionResidence() {
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
		excel.createCell(row, value.getStreet());
		excel.createCell(row, value.getHouseNumber());
		excel.createCell(row, value.getZipCode());
		excel.createCell(row, value.getCity());
		excel.createCell(row, value.getCountry());
	}

	/**
	 * @see com.qpark.eip.core.poi.Section#getSectionTitle()
	 */
	@Override
	public String getSectionTitle() {
		return "Residence";
	}
}