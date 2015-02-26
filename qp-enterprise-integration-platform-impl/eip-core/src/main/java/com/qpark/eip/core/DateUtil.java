/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author bhausen
 */
public abstract class DateUtil {
	/**
	 * @param d the {@link Date}.
	 * @return an {@link XMLGregorianCalendar} from a {@link Date}.
	 */
	public static XMLGregorianCalendar get(final Date d) {
		XMLGregorianCalendar x = null;
		if (d != null) {
			try {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(d);
				DatatypeFactory df = DatatypeFactory.newInstance();
				x = df.newXMLGregorianCalendar(gc);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
		}
		return x;
	}

	/**
	 * @param x the {@link XMLGregorianCalendar}.
	 * @return a {@link Date} from a {@link XMLGregorianCalendar}.
	 */
	public static Date get(final XMLGregorianCalendar x) {
		Date d = null;
		if (x != null) {
			GregorianCalendar gc = x.toGregorianCalendar();
			d = gc.getTime();
		}
		return d;
	}

	/**
	 * Set {@link Calendar#HOUR_OF_DAY}, {@link Calendar#MINUTE},
	 * {@link Calendar#SECOND} and {@link Calendar#MILLISECOND} to
	 * <code>0</code>.
	 * @param c the {@link Calendar}.
	 */
	public static void clearTime(final Calendar c) {
		if (c != null) {
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		}
	}

	/**
	 * Returns a date with hour, minute, second and millisecond set to
	 * <code>0</code>.
	 * @param d the {@link Date} with hour, minute, second and millisecond set.
	 * @return the {@link Date} with cleared time.
	 */
	public static Date clearTime(final Date d) {
		if (d != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			clearTime(c);
			return c.getTime();
		} else {
			return d;
		}
	}

	/**
	 * @param millis the duration in milliseconds.
	 * @return a string with the duration formatted with
	 *         <code>HH:mm:ss.SSS</code>.
	 */
	public static String getDuration(final long millis) {
		DecimalFormat df20 = new DecimalFormat("00");
		DecimalFormat df30 = new DecimalFormat("000");
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTimeInMillis(millis);
		return new StringBuffer(12)
				.append(df20.format(c.get(Calendar.HOUR_OF_DAY))).append(":")
				.append(df20.format(c.get(Calendar.MINUTE))).append(":")
				.append(df20.format(c.get(Calendar.SECOND))).append(".")
				.append(df30.format(c.get(Calendar.MILLISECOND))).toString();
	}

	/**
	 * @param millis the duration in milliseconds.
	 * @return a string with the duration formatted with
	 *         <code>HH:mm:ss.SSS</code>.
	 */
	public static String getDuration(final long start, final long stop) {
		return getDuration(stop - start);
	}

}
