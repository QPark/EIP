/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Provides a {@link HSSFSheet}.
 *
 * @author bhausen
 * @param <SheetData>
 *            the type containing the data.
 */
public interface Sheet<SheetData> {
	/**
	 * Set the values of T into the {@link HSSFRow}.
	 *
	 * @param value
	 *            the T containing the values.
	 * @return the {@link HSSFRow}.
	 */
	HSSFRow addRowValues(SheetData value);

	/** Create the header of the {@link Sheet}. */
	void createHeader();

	/** Finalize the {@link HSSFSheet}. */
	void finish();

	/**
	 * @return the title of the sheet.
	 */
	String getSheetTitle();
}
