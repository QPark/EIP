package com.qpark.eip.core.poi.test.az900;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.poi.ColumnDefinition;
import com.qpark.eip.core.poi.ExcelFile;
import com.qpark.eip.core.poi.impl.AbstractSheetProvider;
import com.qpark.eip.core.poi.test.Person;

/**
 * The {@link Person} sheet.
 *
 * @author bhausen
 */
public class SheetQAndA extends AbstractSheetProvider<SimpleEntry<String, String>> {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(SheetQAndA.class);
	/** The {@link SectionQAndA}. */
	private final SectionQAndA sectionQAndA;

	protected SheetQAndA(final ExcelFile excelFile) {
		super(excelFile);
		this.sectionQAndA = new SectionQAndA();
	}

	@Override
	public HSSFRow addRowValues(final SimpleEntry<String, String> value) {
		HSSFRow row = this.sheet.createRow(this.sheet.getLastRowNum() + 1);
		this.sectionQAndA.setSectionValues(value, row, this.excel);
		return row;
	}

	@Override
	public String getSheetTitle() {
		return "AZ-900 Question and Answers";
	}

	/**
	 * @see com.qpark.eip.core.poi.impl.AbstractSheetProvider#createSuperHeaderRows()
	 */
	@Override
	protected int createSuperHeaderRows() {
		/* Header row. */
		HSSFRow row = this.sheet.createRow(0);
		this.createMergedHeaderCell(row, "Questions and Answers", new CellRangeAddress(0, 0, 0, 1));
		return 1;
	}

	@Override
	protected List<ColumnDefinition> getColumnDefinition() {
		List<ColumnDefinition> value = new ArrayList<>();
		value.addAll(this.sectionQAndA.getColumnDefinition());
		return value;
	}

	@Override
	protected int getFreezedColumns() {
		return 0;
	}

	@Override
	protected Logger getLogger() {
		return this.logger;
	}

}