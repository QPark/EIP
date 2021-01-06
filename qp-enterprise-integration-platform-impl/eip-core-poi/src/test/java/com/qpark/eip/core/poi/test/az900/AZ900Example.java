/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi.test.az900;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import com.qpark.eip.core.poi.ExcelFile;

/**
 * @author bhausen
 */
public class AZ900Example {
	/**
	 * Create the excel file.
	 * @param args the arguments we do not care for.
	 */
	public static void main(final String[] args) {
		try {
			createExcelFile("AZ-900-questions.txt");
			createExcelFile("AZ-900-questions-and-answers.txt");
			createExcelFile("AZ-900-keywords.txt");
			createExcelFile("AZ-900-keywords-0.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the Excel file.
	 * @throws IOException
	 */
	private static void createExcelFile(final String fileName) throws IOException {
		ExcelFile excel = new ExcelFile();

		List<String> readAllLines = Files.readAllLines(Paths.get("src", "test", "resources", fileName));
		List<SimpleEntry<String, String>> qanda = new ArrayList<>();
		String a = "";
		String q = "";
		boolean isAnswer = false;
		for (String line : readAllLines) {
			if (line.equals("###")) {
				if (a.trim().length() > 0 && q.trim().length() > 0) {
					qanda.add(new SimpleEntry<>(q, a));
				}
				isAnswer = false;
				a = "";
				q = "";
			} else if (line.equals("#")) {
				isAnswer = true;
			} else {
				if (isAnswer) {
					a = String.format("%s\n%s", a, line);
				} else {
					q = String.format("%s\n%s", q, line);
				}
			}
		}
		SheetQAndA sheet = new SheetQAndA(excel);
		sheet.createHeader();
		qanda.stream().forEach(p -> sheet.addRowValues(p));
		sheet.finish();

		excel.writeWorkbook(Paths.get(".", fileName.replace(".txt", "") + ".xls").toFile());
	}
}
