/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.poi;

/**
 * Defines a column of an excel sheet.
 *
 * @author bhausen
 */
public class ColumnDefinition {
	/** If the column should get auto sized. */
	private boolean autoSize = false;
	/** The header. */
	private String header;
	/** The width. */
	private int width;

	/**
	 * @param header
	 * @param width
	 */
	public ColumnDefinition(final String header, final int width) {
		this(header, width, false);
	}

	/**
	 * @param header
	 * @param width
	 * @param autoSize
	 */
	public ColumnDefinition(final String header, final int width,
			final boolean autoSize) {
		this.header = header;
		this.width = width;
		this.autoSize = autoSize;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return this.header;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @return the autoSize
	 */
	public boolean isAutoSize() {
		return this.autoSize;
	}
}
