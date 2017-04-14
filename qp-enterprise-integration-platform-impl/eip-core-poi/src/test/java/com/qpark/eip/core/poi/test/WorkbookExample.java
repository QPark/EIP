/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi.test;

import com.qpark.eip.core.poi.ExcelFile;

/**
 * @author bhausen
 */
public class WorkbookExample {
	/**
	 * Create the excel file.
	 *
	 * @param args
	 *            the arguments we do not care for.
	 */
	public static void main(final String[] args) {
		createExcelFile();
	}

	/** Create the Excel file. */
	private static void createExcelFile() {
		ExcelFile excel = new ExcelFile();

		SheetPerson sheet = new SheetPerson(excel);
		sheet.createHeader();
		Person.getPersons().stream().forEach(p -> sheet.addRowValues(p));
		sheet.finish();

		excel.writeWorkbook("Persons");
	}
}
