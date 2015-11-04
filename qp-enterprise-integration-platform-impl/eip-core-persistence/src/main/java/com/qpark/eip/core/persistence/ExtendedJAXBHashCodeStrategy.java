/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.�r.l. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License
 * v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core.persistence;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

/**
 * {@link JAXBHashCodeStrategy} extended by hash code generators for
 * {@link BigDecimal} and {@link XMLGregorianCalendar}.
 *
 * @author bhausen
 */
public class ExtendedJAXBHashCodeStrategy extends JAXBHashCodeStrategy {
	private static final int BIG_DECIMAL_SCALE = 10;
	public static HashCodeStrategy INSTANCE = new ExtendedJAXBHashCodeStrategy();

	@Override
	protected int hashCodeInternal(final ObjectLocator locator,
			final int hashCode, final Object value) {
		if (value instanceof BigDecimal) {
			final BigDecimal scaled = ((BigDecimal) value)
					.setScale(BIG_DECIMAL_SCALE);
			return super.hashCodeInternal(locator, hashCode, scaled);
		} else if (value instanceof List<?>) {
			/* Only hashCodes of sorted lists could be compared. */
			final List<?> list = ExtendedJAXBEqualsStrategy
					.getSortedByObjectHashCode((List<?>) value);
			return this.hashCodeInternal(locator, hashCode, list);
		} else if (value instanceof XMLGregorianCalendar) {
			final GregorianCalendar gc = ((XMLGregorianCalendar) value)
					.toGregorianCalendar();
			return super.hashCodeInternal(locator, hashCode, gc);
		} else {
			return super.hashCodeInternal(locator, hashCode, value);
		}
	}
}
