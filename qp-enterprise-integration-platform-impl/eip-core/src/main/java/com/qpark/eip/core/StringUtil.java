/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

/**
 * @author bhausen
 */
public abstract class StringUtil {
	/**
	 * Compares s and base.
	 *
	 * @param s
	 *            the {@link String}.
	 * @param base
	 *            the base.
	 * @return equal or not.
	 */
	public static final boolean equals(final String s, final String base) {
		return equals(s, base, false);
	}

	/**
	 * Compares s and base using the ignore case parameter.
	 *
	 * @param s
	 *            the {@link String}.
	 * @param base
	 *            the base.
	 * @param ignoreCase
	 *            flag to ignore case.
	 * @return equal or not.
	 */
	public static final boolean equals(final String s, final String base,
			final boolean ignoreCase) {
		if (s == base) {
			return true;
		}
		if (isValue(base) && isValue(s)) {
			if (ignoreCase) {
				return s.equalsIgnoreCase(base);
			} else {
				return s.equals(base);
			}
		} else {
			return false;
		}
	}

	public static final String shrinkToMaxLength(final String s,
			final int maxLength) {
		if (s == null) {
			return s;
		} else {
			if (s.length() >= maxLength && maxLength >= 0) {
				return s.substring(0, maxLength);
			} else {
				return s;
			}
		}
	}

	/**
	 * Checks if the {@link String} is not <code>null</code> and is filled. Non
	 * blanks will be accepted.
	 *
	 * @param s
	 *            The checked {@link String}.
	 * @return is a filled value.
	 * @see StringUtil#isValue(String, boolean)
	 */
	public static final boolean isValue(final String s) {
		return isValue(s, false);
	}

	/**
	 * Checks if the {@link String} is not <code>null</code> and is filled.
	 *
	 * @param s
	 *            The checked {@link String}.
	 * @param acceptBlank
	 *            accept a blank value, otherwise there is a
	 *            {@link String#trim()} called.
	 * @return is a filled value.
	 */
	public static final boolean isValue(final String s,
			final boolean acceptBlank) {
		if (s == null) {
			return false;
		}
		if (acceptBlank && s.length() > 0) {
			return true;
		}
		if (!acceptBlank && s.trim().length() > 0) {
			return true;
		}
		return false;
	}
}
