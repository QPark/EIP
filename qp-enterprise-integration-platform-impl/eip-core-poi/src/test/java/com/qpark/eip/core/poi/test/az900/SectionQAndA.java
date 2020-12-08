package com.qpark.eip.core.poi.test.az900;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.qpark.eip.core.poi.ColumnDefinition;
import com.qpark.eip.core.poi.ExcelFile;
import com.qpark.eip.core.poi.impl.AbstractSection;

/**
 * The {@link Section} of the values question and answer.
 *
 * @author bhausen
 */
public class SectionQAndA extends AbstractSection<SimpleEntry<String, String>> {
	/**
	 * @return the list of {@link ColumnDefinition}.
	 */
	private static List<ColumnDefinition> getSectionColumns() {
		List<ColumnDefinition> value = new ArrayList<>();
		value.add(new ColumnDefinition("No", 5, false));
		value.add(new ColumnDefinition("Question", 40, false));
		value.add(new ColumnDefinition("Answer", 40, false));
		return value;
	}

	/** the number. */
	private int number = 0;

	/** Constructor. */
	public SectionQAndA() {
		super(getSectionColumns());
	}

	/**
	 * @see com.qpark.eip.core.poi.Section#setSectionValues(java.lang.Object,
	 *      org.apache.poi.hssf.usermodel.HSSFRow, com.qpark.eip.core.poi.ExcelFile)
	 */
	@Override
	public void createCells(final SimpleEntry<String, String> value, final HSSFRow row, final ExcelFile excel) {
		excel.createCell(row, ++this.number);
		excel.createCell(row, value.getKey());
		excel.createCell(row, value.getValue());
	}

	/**
	 * @see com.qpark.eip.core.poi.Section#getSectionTitle()
	 */
	@Override
	public String getSectionTitle() {
		return "Question and Answer";
	}
}