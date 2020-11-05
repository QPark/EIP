/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author bhausen
 */
public abstract class DateUtil {
	/**
	 * Set {@link Calendar#HOUR_OF_DAY}, {@link Calendar#MINUTE},
	 * {@link Calendar#SECOND} and {@link Calendar#MILLISECOND} to <code>0</code>.
	 *
	 * @param c
	 *              the {@link Calendar}.
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
	 *
	 * @param d
	 *              the {@link Date} with hour, minute, second and millisecond set.
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
	 * @param date
	 *                 the {@link Date}.
	 * @return an {@link XMLGregorianCalendar} from a {@link Date}.
	 */
	public static XMLGregorianCalendar get(final Date date) {
		return Optional.ofNullable(date).map(d -> {
			try {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(date);
				DatatypeFactory df = DatatypeFactory.newInstance();
				return df.newXMLGregorianCalendar(gc);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			return null;
		}).orElse(null);
	}

	/**
	 * @param instant
	 *                    the {@link Instant}.
	 * @return an {@link XMLGregorianCalendar} from a {@link Instant}.
	 */
	public static XMLGregorianCalendar get(final Instant instant) {
		return get(toDate(instant));
	}

	/**
	 * @param x
	 *              the {@link XMLGregorianCalendar}.
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
	 * @param millis
	 *                   the duration in milliseconds.
	 * @return a string with the duration formatted with <code>HH:mm:ss.SSS</code>.
	 */
	public static String getDuration(final long millis) {
		DecimalFormat df20 = new DecimalFormat("00");
		DecimalFormat df30 = new DecimalFormat("000");
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTimeInMillis(millis);
		return new StringBuffer(12).append(df20.format(c.get(Calendar.HOUR_OF_DAY))).append(":")
				.append(df20.format(c.get(Calendar.MINUTE))).append(":").append(df20.format(c.get(Calendar.SECOND)))
				.append(".").append(df30.format(c.get(Calendar.MILLISECOND))).toString();
	}

	/**
	 * @param start
	 *                  the start in milliseconds.
	 * @param stop
	 *                  the stop in milliseconds.
	 * @return a string with the duration formatted with <code>HH:mm:ss.SSS</code>.
	 */
	public static String getDuration(final long start, final long stop) {
		return getDuration(stop - start);
	}

	/**
	 * @param instant
	 *                    the {@link Instant}.
	 * @return the {@link Date} of the {@link Instant}.
	 */
	public static Date toDate(final Instant instant) {
		return Optional.ofNullable(instant).map(i -> Date.from(i)).orElse(null);
	}

	/**
	 * @param date
	 *                 the {@link Date}.
	 * @return the {@link Instant} of the {@link Date}.
	 */
	public static Instant toInstant(final Date date) {
		return Optional.ofNullable(date).map(d -> d.toInstant()).orElse(null);
	}

	/**
	 * @param year
	 *                  the years number.
	 * @param month
	 *                  the months number (1 is January, 12 is December)
	 * @param day
	 *                  the days number.
	 * @return the {@link Date} at start of the day.
	 */
	public Date getDateUTC(final int year, final int month, final int day) {
		return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.of("UTC")).toInstant());
	}

	/**
	 * @param year
	 *                   the years number.
	 * @param month
	 *                   the months number (1 is January, 12 is December)
	 * @param day
	 *                   the days number.
	 * @param hour
	 *                   the hours number (0-23)
	 * @param minute
	 *                   the minutes number (0-59)
	 * @return the {@link Date} at start of the day.
	 */
	public Date getDateUTC(final int year, final int month, final int day, final int hour, final int minute) {
		return Date.from(LocalDateTime.of(year, month, day, hour, minute).toInstant(ZoneOffset.UTC));
	}

}
