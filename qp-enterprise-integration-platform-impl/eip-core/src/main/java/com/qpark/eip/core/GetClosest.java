/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Get the closest value to the reference value.
 *
 * @author bhausen
 */
public abstract class GetClosest {
	/** Index with value container. */
	private static final class IndexValue {
		int index;
		BigDecimal value;

		IndexValue(final int index, final BigDecimal value) {
			this.index = index;
			this.value = value;
		}
	}

	public static void main(final String[] args) {
		System.out.println(getClosestIndex(BigDecimal.ONE, BigDecimal.TEN, null,
				BigDecimal.ZERO));
		System.out.println(getClosestIndex(BigDecimal.ONE, BigDecimal.ONE));
		System.out.println(getClosestIndex(BigDecimal.ONE, BigDecimal.ZERO,
				BigDecimal.ONE));
		System.out.println(getClosestIndex(BigDecimal.ONE, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ONE));
		System.out.println(getClosestIndex(BigDecimal.ONE, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE));
	}

	/**
	 * @param ref
	 *            The reference value to check against.
	 * @param bigDecimals
	 *            The list of {@link BigDecimal}s to check.
	 * @return the closest {@link BigDecimal} to the reference value out of the
	 *         list.
	 */
	public static int getClosestIndex(final BigDecimal ref,
			final BigDecimal... bigDecimals) {
		if (bigDecimals == null || bigDecimals.length == 0) {
			return -1;
		} else if (bigDecimals.length == 1) {
			return 0;
		} else {
			TreeMap<BigDecimal, IndexValue> tm = new TreeMap<BigDecimal, IndexValue>();
			for (int i = 0; i < bigDecimals.length; i++) {
				tm.put(bigDecimals[i], new IndexValue(i, bigDecimals[i]));
			}
			IndexValue found = null;
			IndexValue previous = null;
			for (IndexValue iv : tm.values()) {
				if (previous == null) {
					previous = iv;
				} else {
					if (iv.value.compareTo(ref) >= 0) {
						BigDecimal a = ref.subtract(previous.value);
						BigDecimal b = iv.value.subtract(ref);
						if (a.compareTo(b) >= 0) {
							found = iv;
						}
						break;
					}
				}
				previous = iv;
			}
			if (found == null) {
				found = previous;
			}
			return found.index;
		}
	}

	/**
	 * @param ref
	 *            The reference value to check against.
	 * @param bigDecimals
	 *            The {@link Collection} of {@link BigDecimal}s to check.
	 * @return the closest {@link BigDecimal} to the reference value out of the
	 *         list.
	 */
	public static int getClosestIndex(final BigDecimal ref,
			final Collection<BigDecimal> bigDecimals) {
		if (bigDecimals == null) {
			return -1;
		} else if (bigDecimals.size() == 1) {
			return 0;
		} else {
			return getClosestIndex(ref,
					bigDecimals.toArray(new BigDecimal[bigDecimals.size()]));
		}
	}

	/**
	 * @param ref
	 *            The reference value to check against.
	 * @param doubles
	 *            The list of doubles to check.
	 * @return the closest double to the reference value out of the list.
	 */
	public static int getClosestIndex(final double ref,
			final double... doubles) {
		if (doubles == null) {
			return -1;
		} else if (doubles.length == 1) {
			return 0;
		} else {
			LinkedList<BigDecimal> bigDecimals = new LinkedList<BigDecimal>();
			for (double d : doubles) {
				bigDecimals.add(new BigDecimal(d));
			}
			return getClosestIndex(new BigDecimal(ref),
					bigDecimals.toArray(new BigDecimal[bigDecimals.size()]));
		}
	}

	/**
	 * @param ref
	 *            The reference value to check against.
	 * @param doubles
	 *            The list of doubles to check.
	 * @return the closest double to the reference value out of the list.
	 */
	public static int getClosestIndex(final Double ref,
			final Double... doubles) {
		if (doubles == null) {
			return -1;
		} else if (doubles.length == 1) {
			return 0;
		} else {
			LinkedList<BigDecimal> bigDecimals = new LinkedList<BigDecimal>();
			for (double d : doubles) {
				bigDecimals.add(new BigDecimal(d));
			}
			return getClosestIndex(new BigDecimal(ref),
					bigDecimals.toArray(new BigDecimal[bigDecimals.size()]));
		}
	}

	/**
	 * @param ref
	 *            The reference value to check against.
	 * @param ints
	 *            The list of integers to check.
	 * @return the closest integer to the reference value out of the list.
	 */
	public static int getClosestIndex(final int ref, final int... ints) {
		if (ints == null) {
			return -1;
		} else if (ints.length == 1) {
			return 0;
		} else {
			LinkedList<BigDecimal> bigDecimals = new LinkedList<BigDecimal>();
			for (int i : ints) {
				bigDecimals.add(new BigDecimal(i));
			}
			return getClosestIndex(new BigDecimal(ref),
					bigDecimals.toArray(new BigDecimal[bigDecimals.size()]));
		}
	}
}
