/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.�r.l. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License
 * v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

/**
 * {@link JAXBEqualsStrategy} extended by equals for {@link BigDecimal},
 * {@link XMLGregorianCalendar} and {@link List}.
 *
 * @author bhausen
 */
public class ExtendedJAXBEqualsStrategy extends JAXBEqualsStrategy {

	public static EqualsStrategy INSTANCE = new ExtendedJAXBEqualsStrategy();

	/**
	 * Sort the entries of a list according to their hashCodes
	 *
	 * @param list
	 *            The list to sort
	 * @return a new instance of a list with the sorted entries.
	 */
	static List<?> getSortedByObjectHashCode(final List<?> list) {
		ArrayList<Object> l = new ArrayList<Object>(list.size());
		l.addAll(list);
		Collections.sort(l, new Comparator<Object>() {
			@Override
			public int compare(final Object o1, final Object o2) {
				long ret = 0;
				if (o1 != o2) {
					if (o1 == null) {
						ret = 1;
					} else if (o2 == null) {
						ret = -1;
					} else {
						ret = Long.valueOf(o1.hashCode())
								- Long.valueOf(o2.hashCode());
					}
				}
				if (ret > 0) {
					ret = 1;
				} else if (ret < 0) {
					ret = -1;
				}
				return (int) ret;
			}
		});
		return l;
	}

	/**
	 * Compares {@link BigDecimal} by setting the scale.
	 *
	 * @param leftLocator
	 * @param rightLocator
	 * @param left
	 * @param right
	 * @return
	 */
	protected boolean equalsInternal(final ObjectLocator leftLocator,
			final ObjectLocator rightLocator, final BigDecimal left,
			final BigDecimal right) {
		boolean equals = false;
		if (left == right) {
			equals = true;
		} else if (left != null && right != null) {
			int maxScale = Math.max(left.scale(), right.scale());
			BigDecimal leftScaled = left.setScale(maxScale);
			BigDecimal rightScaled = right.setScale(maxScale);
			equals = leftScaled.equals(rightScaled);
		}
		return equals;
	}

	/**
	 * @see org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy#equalsInternal(org.jvnet.jaxb2_commons.locator.ObjectLocator,
	 *      org.jvnet.jaxb2_commons.locator.ObjectLocator, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected boolean equalsInternal(final ObjectLocator leftLocator,
			final ObjectLocator rightLocator, final Object lhs,
			final Object rhs) {
		if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
			/* Compare BigDecimals with the same scale. */
			final BigDecimal left = (BigDecimal) lhs;
			final BigDecimal right = (BigDecimal) rhs;
			return this.equalsInternal(leftLocator, rightLocator, left, right);
		} else if (lhs instanceof List<?> && rhs instanceof List<?>) {
			/* Only sorted lists could be compared. */
			final List<?> left = getSortedByObjectHashCode((List<?>) lhs);
			final List<?> right = getSortedByObjectHashCode((List<?>) rhs);
			return this.equalsInternal(leftLocator, rightLocator, left, right);
		} else if (lhs instanceof XMLGregorianCalendar
				&& rhs instanceof XMLGregorianCalendar) {
			/* Compare the GregorianCalendar instead of XMLGregorianCalendar. */
			/* 2012-10-30 != 2012-10-30T00:00:00 */
			final XMLGregorianCalendar left = (XMLGregorianCalendar) lhs;
			final XMLGregorianCalendar right = (XMLGregorianCalendar) rhs;
			return this.equalsInternal(leftLocator, rightLocator, left, right);
		} else {
			return super.equalsInternal(leftLocator, rightLocator, lhs, rhs);
		}
	}

	/**
	 * Compares {@link XMLGregorianCalendar} by
	 * {@link XMLGregorianCalendar#toGregorianCalendar()}.
	 *
	 * @param leftLocator
	 * @param rightLocator
	 * @param left
	 * @param right
	 * @return
	 */
	protected boolean equalsInternal(final ObjectLocator leftLocator,
			final ObjectLocator rightLocator, final XMLGregorianCalendar left,
			final XMLGregorianCalendar right) {
		boolean equals = false;
		if (left == right) {
			equals = true;
		} else if (left != null && right != null) {
			GregorianCalendar gcLeft = left.toGregorianCalendar();
			gcLeft.setTimeZone(TimeZone.getDefault());
			GregorianCalendar gcRight = right.toGregorianCalendar();
			gcRight.setTimeZone(TimeZone.getDefault());
			equals = gcLeft.equals(gcRight);
		}
		return equals;
	}
}