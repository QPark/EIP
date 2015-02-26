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

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Acting to {@link BigDecimal}.
 * @author bhausen
 */
public abstract class NumberUtil {
	/** The value 100 with a scale of 0. */
	public static final BigDecimal HUNDRED = ONE.multiply(TEN).multiply(TEN);
	/** The value 1000 with a scale of 0. */
	public static final BigDecimal THOUSEND = HUNDRED.multiply(TEN);
	/** The value 1000000 with a scale of 0. */
	public static final BigDecimal MILLION = THOUSEND.multiply(THOUSEND);

	/**
	 * Null safe equals with accepted delta.
	 * @param b0 the first {@link BigDecimal}.
	 * @param b1 the second {@link BigDecimal}.
	 * @param delta the accepted variance to be interpreted as equal.
	 * @return equal or not.
	 */
	public static boolean equals(final BigDecimal b0, final BigDecimal b1,
			final double delta) {
		if (b0 == b1) {
			return true;
		} else if (b0 == null || b1 == null) {
			return false;
		} else {
			return b0.subtract(b1).abs()
					.compareTo(BigDecimal.valueOf(Math.abs(delta))) <= 0;
		}
	}

	/**
	 * Equals with accepted delta.
	 * @param d0 the first {@link BigDecimal}.
	 * @param d1 the second {@link BigDecimal}.
	 * @param delta the accepted variance to be interpreted as equal.
	 * @return equal or not.
	 */
	public static boolean equals(final double d0, final double d1,
			final double delta) {
		return Math.abs(d0 - d1) <= Math.abs(delta);
	}

	/**
	 * Null safe compareTo of two {@link BigDecimal}s.
	 * @param b0 the first {@link BigDecimal}.
	 * @param b1 the second {@link BigDecimal}.
	 * @return equal, more little or greater.
	 * @see BigDecimal#compareTo(Object)
	 */
	public static int compareTo(final BigDecimal b0, final BigDecimal b1) {
		if (b0 == b1) {
			return 0;
		} else if (b0 == null) {
			return 1;
		} else if (b1 == null) {
			return -1;
		} else {
			return b0.compareTo(b1);
		}
	}

	/**
	 * Null safe compareTo of two {@link BigDecimal}s.
	 * @param d0 the first {@link BigDecimal}.
	 * @param d1 the second {@link BigDecimal}.
	 * @return equal, more little or greater.
	 * @see BigDecimal#compareTo(Object)
	 */
	public static int compareTo(final Double d0, final Double d1) {
		if (d0 == d1) {
			return 0;
		} else if (d0 == null) {
			return 1;
		} else if (d1 == null) {
			return -1;
		} else {
			return d0.compareTo(d1);
		}
	}

	/**
	 * Null safe equals of two {@link BigDecimal}s.
	 * @param b0 the first {@link BigDecimal}.
	 * @param b1 the second {@link BigDecimal}.
	 * @return equal or not
	 * @see BigDecimal#equals(Object)
	 */
	public static boolean equals(final BigDecimal b0, final BigDecimal b1) {
		if (b0 == b1) {
			return true;
		} else if (b0 == null || b1 == null) {
			return false;
		} else {
			return b0.equals(b1);
		}
	}

	/**
	 * @return The default {@link DecimalFormat} of the pattern.
	 */
	public static NumberFormat getNumberFormat(final String pattern) {
		DecimalFormat df = new DecimalFormat(pattern,
				DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		return df;
	}

	/**
	 * Null save comparison against {@link BigDecimal#ZERO}.
	 * @param b the {@link BigDecimal} to compare.
	 * @return <code>true</code> if not null and equal to
	 *         {@link BigDecimal#ZERO} else <code>false</code>.
	 */
	public static boolean isZero(final BigDecimal b) {
		if (b != null && b.equals(ZERO)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Null save convert of a {@link Double} to a {@link BigDecimal}.
	 * @param d the {@link Double} to convert.
	 * @return the {@link BigDecimal}.
	 */
	public static BigDecimal toBigDecimal(final Double d) {
		if (d != null) {
			return BigDecimal.valueOf(d);
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link Double} to a {@link BigDecimal} with a
	 * scale. The scale is only set if the existing is larger than the given
	 * one.
	 * @param d the double to convert.
	 * @param scale the scale to reduce the {@link BigDecimal} to.
	 * @return the {@link BigDecimal}.
	 */
	public static BigDecimal toBigDecimal(final Double d, final int scale) {
		if (d != null) {
			BigDecimal bd = toBigDecimal(d);
			if (bd.scale() > scale) {
				bd = bd.setScale(scale, RoundingMode.HALF_UP);
			}
			return bd;
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link Float} to a {@link BigDecimal}.
	 * @param d the {@link Float} to convert.
	 * @return the {@link BigDecimal}.
	 */
	public static BigDecimal toBigDecimal(final Float f) {
		if (f != null) {
			return BigDecimal.valueOf(f);
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link Float} to a {@link BigDecimal} with a
	 * scale. The scale is only set if the existing is larger than the given
	 * one.
	 * @param d the double to convert.
	 * @param scale the scale to reduce the {@link BigDecimal} to.
	 * @return the {@link BigDecimal}.
	 */
	public static BigDecimal toBigDecimal(final Float f, final int scale) {
		if (f != null) {
			BigDecimal bd = toBigDecimal(f);
			if (bd.scale() > scale) {
				bd = bd.setScale(scale, RoundingMode.HALF_UP);
			}
			return bd;
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link Integer} to a {@link BigDecimal}.
	 * @param d the {@link Integer} to convert.
	 * @return the {@link BigDecimal}.
	 */
	public static BigDecimal toBigDecimal(final Integer i) {
		if (i != null) {
			return BigDecimal.valueOf(i);
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link Short} to a {@link BigDecimal}.
	 * @param d the {@link Short} to convert.
	 * @return the {@link BigDecimal}.
	 */
	public static BigDecimal toBigDecimal(final Short s) {
		if (s != null) {
			return BigDecimal.valueOf(s);
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link Integer} to a {@link BigInteger}.
	 * @param d the {@link Integer} to convert.
	 * @return the {@link BigInteger}.
	 */
	public static BigInteger toBigInteger(final Integer i) {
		if (i != null) {
			return BigInteger.valueOf(i);
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link Short} to a {@link BigInteger}.
	 * @param d the {@link Short} to convert.
	 * @return the {@link BigDeBigIntegercimal}.
	 */
	public static BigInteger toBigInteger(final Short s) {
		if (s != null) {
			return BigInteger.valueOf(s);
		} else {
			return null;
		}
	}

	/**
	 * Null save convert of a {@link BigDecimal} to a double.
	 * @param b the {@link BigDecimal} to convert.
	 * @return the double or 0.0 if b is <code>null</code>.
	 */
	public static double toDouble(final BigDecimal b) {
		if (b != null) {
			return b.doubleValue();
		} else {
			return 0d;
		}
	}

	/**
	 * Null save convert of a {@link BigDecimal} to a float.
	 * @param b the {@link BigDecimal} to convert.
	 * @return the float or 0.0 if b is <code>null</code>.
	 */
	public static float toFloat(final BigDecimal b) {
		if (b != null) {
			return b.floatValue();
		} else {
			return 0f;
		}
	}

	/**
	 * Converts a byte to an integer as <code>
	 * 2 &circ; 8 + 2 &circ; 7 + 2 &circ; 6 + 2 &circ; 5 + 2 &circ; 4 + 2 &circ; 3 + 2 &circ; 2 + 2 &circ; 1 + 2 &circ; 0
	 * </code>
	 * @param b the byte.
	 * @return The integer.
	 */
	public static int toInt(final byte b) {
		int value = 0;
		for (int i = 0; i < 8; i++) {
			if ((b & 1 << i) > 0) {
				value += Math.pow(2, i);
			}
		}
		return value;
	}

	/**
	 * Returns an int. If the {@link String} is <code>null</code> or
	 * {@link Integer#parseInt(String)} throws a {@link NumberFormatException}
	 * it returns the defaultValue.
	 * @param s the {@link String}.#
	 * @param defaultValue the default value.
	 * @return the int.
	 */
	public static int toInt(final String s, final int defaultValue) {
		int i = defaultValue;
		if (s != null) {
			try {
				i = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				// nothing to do
			}
		}
		return i;
	}
}
