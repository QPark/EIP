/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * The excel file.
 *
 * @author bhausen
 */
public class ExcelFile {
	/**
	 * Create a cell in the sheet row at index and set the value and apply style
	 *
	 * @param row
	 *            the {@link HSSFRow}. the {@link HSSFRow}.
	 * @param index
	 *            the index index in row
	 * @param value
	 *            value object
	 * @param style
	 *            cell style
	 * @return the {@link HSSFCell} created.
	 */
	private static HSSFCell createCell(final HSSFRow row, final int index,
			final Object value, final HSSFCellStyle style) {
		HSSFCell cell = row.createCell(index < 0 ? 0 : index);
		if (Objects.isNull(value)) {
			cell.setCellValue("---");
		} else if (Boolean.class.isInstance(value)) {
			cell.setCellValue(((Boolean) value));
		} else if (BigDecimal.class.isInstance(value)) {
			cell.setCellValue(((BigDecimal) value).doubleValue());
		} else if (BigInteger.class.isInstance(value)) {
			cell.setCellValue(((BigInteger) value).longValue());
		} else if (Integer.class.isInstance(value)) {
			cell.setCellValue(((Integer) value).longValue());
		} else if (Number.class.isInstance(value)) {
			cell.setCellValue(((Number) value).doubleValue());
		} else if (String.class.isInstance(value)) {
			cell.setCellValue(((String) value).trim());
		} else {
			cell.setCellValue((String.valueOf(value)));
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * Get the file of the excel.
	 *
	 * @param directoryName
	 * @param baseName
	 * @return <datetime>-<baseName>.xls
	 */
	public static File getFile(final String directoryName,
			final String baseName) {
		File f = new File(String.format("%s%s%s-%s.xls", directoryName,
				File.separator,
				new SimpleDateFormat("yyyyMMdd'-'HHmm").format(new Date()),
				baseName));
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		return f;
	}

	/**
	 * Get the file name of the excel file.
	 *
	 * @param baseName
	 * @return <datetime>-<baseName>.xls
	 */
	public static String getFileName(final String baseName) {
		return String.format("%s-%s.xls",
				new SimpleDateFormat("yyyyMMdd'-'HHmm").format(new Date()),
				baseName);
	}

	/**
	 * Get the file name of the excel file.
	 *
	 * @param baseName
	 * @return <datetime>-<baseName>.xls
	 */
	public static String getTargetFileName(final String baseName) {
		return String.format("target%s%s-%s.xls", File.separator,
				new SimpleDateFormat("yyyyMMdd'-'HHmm").format(new Date()),
				baseName);
	}

	/** The body style. */
	private final HSSFCellStyle bodyStyle;
	/** The body style. */
	private final HSSFCellStyle bodyStyleTextWrapped;
	/** The bold Arial font. */
	private final Font boldArialFont;
	/** The header style. */
	private final HSSFCellStyle headerStyle;
	/** The list of assigned sheets. */
	private final List<Sheet<?>> sheets = new ArrayList<>();

	/** The {@link HSSFWorkbook}. */
	private final HSSFWorkbook workbook;

	/** The Constructor. */
	public ExcelFile() {
		this.workbook = new HSSFWorkbook();

		this.boldArialFont = this.workbook.createFont();
		this.boldArialFont.setFontHeightInPoints((short) 10);
		this.boldArialFont.setFontName("Arial");
		this.boldArialFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		this.boldArialFont.setItalic(false);

		this.headerStyle = this.workbook.createCellStyle();
		this.headerStyle.setFont(this.boldArialFont);
		this.headerStyle.setWrapText(true);
		this.headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		this.headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		this.headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		this.headerStyle.setBorderLeft(CellStyle.BORDER_THIN);

		this.bodyStyle = this.workbook.createCellStyle();
		this.bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
		this.bodyStyle.setBorderTop(CellStyle.BORDER_THIN);
		this.bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
		this.bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);

		this.bodyStyleTextWrapped = this.workbook.createCellStyle();
		this.bodyStyleTextWrapped.cloneStyleFrom(this.bodyStyle);
		this.bodyStyleTextWrapped.setWrapText(true);
	}

	/**
	 * @param sheet
	 *            the {@link Sheet} to add.
	 */
	public void addSheet(final Sheet<?> sheet) {
		this.sheets.add(sheet);
	}

	/**
	 * Clone the style of {@link #getBodyStyle()} and set wrapped text to true
	 * for this one.
	 *
	 * @return the cloned {@link CellStyle}.
	 */
	public CellStyle cloneCellStyleWrappedText() {
		CellStyle style = this.workbook.createCellStyle();
		style.cloneStyleFrom(this.getBodyStyle());
		style.setWrapText(true);
		return style;
	}

	/**
	 * Clone the given style and set wrapped text to true for this one.
	 *
	 * @param baseStyle
	 *            the {@link CellStyle} to clone.
	 * @return the cloned {@link CellStyle}.
	 */
	public CellStyle cloneCellStyleWrappedText(final CellStyle baseStyle) {
		CellStyle style = this.workbook.createCellStyle();
		style.cloneStyleFrom(baseStyle);
		style.setWrapText(true);
		return style;
	}

	/**
	 * @param row
	 *            the {@link HSSFRow}. the {@link HSSFRow}.
	 * @return the {@link HSSFCell}.
	 */
	public HSSFCell createCell(final HSSFRow row) {
		return createCell(row, row.getLastCellNum(), null, this.bodyStyle);
	}

	/**
	 * @param row
	 *            the {@link HSSFRow}. the {@link HSSFRow}.
	 * @param index
	 *            the index
	 * @param value
	 *            the value object.
	 * @return the {@link HSSFCell}.
	 */
	public HSSFCell createCell(final HSSFRow row, final int index,
			final Object value) {
		return createCell(row, index, value, this.bodyStyle);
	}

	/**
	 * @param row
	 *            the {@link HSSFRow}.
	 * @param value
	 *            the value object.
	 * @return the {@link HSSFCell}.
	 */
	public HSSFCell createCell(final HSSFRow row, final Object value) {
		return createCell(row, row.getLastCellNum(), value, this.bodyStyle);
	}

	/**
	 * @param row
	 *            the {@link HSSFRow}.
	 * @param index
	 *            the index
	 * @param value
	 *            the value object.
	 * @return the {@link HSSFCell}.
	 */
	public HSSFCell createHeaderCell(final HSSFRow row, final int index,
			final String value) {
		return createCell(row, index, value, this.headerStyle);
	}

	/**
	 * @param row
	 *            the {@link HSSFRow}.
	 * @param value
	 *            the value object.
	 * @return the {@link HSSFCell}.
	 */
	public HSSFCell createHeaderCell(final HSSFRow row, final String value) {
		return createCell(row, row.getLastCellNum(), value, this.headerStyle);
	}

	/**
	 * @param name
	 *            the name of the sheet.
	 * @return the {@link HSSFSheet}.
	 */
	public HSSFSheet createSheet(final String name) {
		return this.workbook.createSheet(name);
	}

	/** Call {@link Sheet#finish()} on every {@link Sheet}. */
	public void finishSheets() {
		this.sheets.stream().filter(sheet -> Objects.nonNull(sheet))
				.forEach(sheet -> sheet.finish());
	}

	/**
	 * @return the bodyStyle {@link HSSFCellStyle}
	 */
	public HSSFCellStyle getBodyStyle() {
		return this.bodyStyle;
	}

	/**
	 * @return the bodyStyleTextWrapped {@link HSSFCellStyle}
	 */
	public HSSFCellStyle getBodyStyleTextWrapped() {
		return this.bodyStyleTextWrapped;
	}

	/**
	 * @return the boldArial {@link Font}
	 */
	public Font getBoldArialFont() {
		return this.boldArialFont;
	}

	/**
	 * @return the header {@link HSSFCellStyle}
	 */
	public HSSFCellStyle getHeaderStyle() {
		return this.headerStyle;
	}

	/**
	 * @return the sheets.
	 */
	public List<Sheet<?>> getSheets() {
		return this.sheets;
	}

	/**
	 * @return the {@link HSSFWorkbook}
	 */
	public HSSFWorkbook getWorkbook() {
		return this.workbook;
	}

	/**
	 * Write workbook
	 *
	 * @param f
	 *            the {@link File} to write the {@link Workbook} to.
	 */
	public void writeWorkbook(final File f) {
		if (Objects.nonNull(f)) {
			this.sheets.stream().filter(sheet -> Objects.nonNull(sheet))
					.forEach(sheet -> sheet.finish());
			try {
				if (!f.exists()) {
					f.getParentFile().mkdirs();
					f.createNewFile();
				}
				try (FileOutputStream fis = new FileOutputStream(f)) {
					this.workbook.write(fis);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Write workbook
	 *
	 * @param baseName
	 */
	public void writeWorkbook(final String baseName) {
		String fileName = getFileName(baseName);
		File f = new File(fileName);
		this.writeWorkbook(f);
	}
}
