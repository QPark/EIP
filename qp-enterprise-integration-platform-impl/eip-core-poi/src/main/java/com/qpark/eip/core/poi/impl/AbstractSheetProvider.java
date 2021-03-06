/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi.impl;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.slf4j.Logger;

import com.qpark.eip.core.poi.ColumnDefinition;
import com.qpark.eip.core.poi.ExcelFile;
import com.qpark.eip.core.poi.Section;
import com.qpark.eip.core.poi.Sheet;

/**
 * An abstract {@link Sheet}.
 *
 * @author bhausen
 * @param <T>
 */
public abstract class AbstractSheetProvider<T> implements Sheet<T> {
	/**
	 * Create the sheet and add the header.
	 *
	 * @param excelFile
	 *            the {@link ExcelFile} to create the {@link Sheet} for.
	 * @param type
	 *            the class of the {@link Sheet}.
	 * @return the {@link Sheet} object.
	 */
	public static <S extends AbstractSheetProvider<?>> Optional<S> createInstance(
			final ExcelFile excelFile, final Class<S> type) {
		Optional<S> value = Optional.empty();
		try {
			Constructor<S> constructor = type.getConstructor(ExcelFile.class);
			S sheet = constructor.newInstance(excelFile);
			sheet.createHeader();
			value = Optional.of(sheet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * Get the merging {@link CellRangeAddress} for a or a list of
	 * {@link Section}s.
	 *
	 * @param row
	 *            the row number the {@link CellRangeAddress} is merging.
	 * @param start
	 *            the starting column.
	 * @param sections
	 *            the {@link Section} or list of {@link Section}s.
	 * @return the {@link CellRangeAddress}.
	 */
	public static CellRangeAddress getMergedHeader(final int row,
			final int start, final Section<?>... sections) {
		AtomicInteger stop = new AtomicInteger(start);
		if (Objects.nonNull(sections)) {
			Arrays.asList(sections).stream().filter(s -> Objects.nonNull(s))
					.forEach(s -> stop.set(stop.get() + s.size()));
		}
		CellRangeAddress value = new CellRangeAddress(row, row, start,
				stop.get() - 1);
		return value;
	}

	/**
	 * Get the merging {@link CellRangeAddress}.
	 *
	 * @param row
	 *            the row number the {@link CellRangeAddress} is merging.
	 * @param start
	 *            the starting column.
	 * @param stop
	 *            the stop column.
	 * @return the {@link CellRangeAddress}.
	 */
	public static CellRangeAddress getMergedHeader(final int row,
			final int start, final int stop) {
		CellRangeAddress value = new CellRangeAddress(row, row, start,
				stop - 1);
		return value;
	}

	/** The {@link ExcelFile}. */
	protected final ExcelFile excel;
	/** Flag indicating the header is created or not. */
	private boolean headerCreated = false;
	/** The {@link HSSFSheet}. */
	protected final HSSFSheet sheet;
	/** The number of rows above the normal header of the columns. */
	private int superHeaderRowSize = 0;

	/**
	 * @param excelFile
	 *            the {@link ExcelFile}.
	 */
	protected AbstractSheetProvider(final ExcelFile excelFile) {
		this.excel = excelFile;
		this.excel.addSheet(this);
		this.sheet = this.excel.createSheet(this.getSheetTitle());
	}

	/**
	 * @see com.qpark.eip.core.poi.Sheet#createHeader()
	 */
	@Override
	public void createHeader() {
		if (!this.headerCreated) {
			this.headerCreated = true;
			this.superHeaderRowSize = this.createSuperHeaderRows();
			HSSFRow headerRow = this.sheet.createRow(this.superHeaderRowSize);
			AtomicInteger ai = new AtomicInteger(0);
			this.getColumnDefinition().stream().forEach(cd -> {
				this.sheet.setColumnWidth(ai.get(), cd.getWidth() * 256);
				ai.getAndIncrement();
			});
			ai.set(0);
			this.getColumnDefinition().stream().forEach(cd -> {
				this.excel.createHeaderCell(headerRow, ai.get(),
						cd.getHeader());
				ai.getAndIncrement();
			});
		}
	}

	/**
	 * @param row
	 *            the {@link HSSFRow}
	 * @param header
	 *            the header string.
	 * @param merged
	 *            the {@link CellRangeAddress} of the header.
	 * @return the {@link HSSFCell}.
	 */
	public Cell createMergedHeaderCell(final HSSFRow row, final String header,
			final CellRangeAddress merged) {
		this.sheet.addMergedRegion(merged);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, merged, this.sheet,
				this.sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, merged, this.sheet,
				this.sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, merged, this.sheet,
				this.sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, merged, this.sheet,
				this.sheet.getWorkbook());
		HSSFCell cell = this.excel.createCell(row, merged.getFirstColumn(),
				header);
		cell.setCellStyle(this.excel.getHeaderStyle());
		return cell;
	}

	/**
	 * Create rows at top of the header row.
	 *
	 * @return the number of rows created.
	 */
	protected abstract int createSuperHeaderRows();

	/**
	 * @see com.qpark.eip.core.poi.Sheet#finish()
	 */
	@Override
	public void finish() {
		if (Objects.nonNull(this.sheet.getRow(this.superHeaderRowSize))) {
			this.sheet.setAutoFilter(new CellRangeAddress(
					this.superHeaderRowSize,
					this.sheet.getRow(this.sheet.getLastRowNum()).getRowNum(),
					0,
					this.sheet.getRow(this.superHeaderRowSize).getLastCellNum()
							- 1));
		}
		this.sheet.createFreezePane(this.getFreezedColumns(),
				this.superHeaderRowSize + 1, this.getFreezedColumns() + 1,
				this.superHeaderRowSize + 1);
		AtomicInteger ai = new AtomicInteger(0);
		this.getColumnDefinition().stream().forEach(cd -> {
			if (cd.isAutoSize()) {
				this.sheet.autoSizeColumn(ai.get());
				// } else {
				// this.sheet.setColumnWidth(ai.get(), cd.getWidth());
			}
			ai.incrementAndGet();
		});

	}

	/**
	 * @return the list of {@link ColumnDefinition}.
	 */
	protected abstract List<ColumnDefinition> getColumnDefinition();

	/**
	 * @return the number of columns to set on
	 *         {@link HSSFSheet#createFreezePane(int, int, int, int)} using 0,
	 *         this value, 0 and the return of {@link #createSuperHeaderRows()}
	 */
	protected abstract int getFreezedColumns();

	/** The {@link org.slf4j.Logger}. */
	protected abstract Logger getLogger();

	/**
	 * @see com.qpark.eip.core.poi.Sheet#getSheet()
	 */
	@Override
	public HSSFSheet getSheet() {
		return this.sheet;
	}
}
