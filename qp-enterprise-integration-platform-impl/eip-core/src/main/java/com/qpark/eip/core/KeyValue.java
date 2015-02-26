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

import java.util.Map.Entry;

/**
 * Implementation of the {@link Entry} with {@link String} key and T value.
 * @author bhausen
 * @param <T> the type of the value.
 */
public class KeyValue<T> implements Entry<String, T>, Comparable<KeyValue<T>> {
	/** The key. */
	private String key;
	/** The value. */
	private T value;

	/**
	 * @param key the key.
	 */
	public KeyValue(final String key) {
		this.key = key;
	}

	/**
	 * @param key the key.
	 * @param value the value.
	 */
	public KeyValue(final String key, final T value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final KeyValue<T> o) {
		if (o == this) {
			return 0;
		}
		if (o == null) {
			return -1;
		}
		int ret = 0;
		if (this.key == o.key || this.key != null && o.key != null
				&& this.key.equals(o.key)) {
			if (this.value == o.value || this.value != null && o.value != null
					&& this.value.equals(o.value)) {
				ret = 0;
			} else if (this.value == null) {
				ret = 1;
			} else if (o.value == null) {
				ret = -1;
			} else {
				ret = String.valueOf(this.value).compareTo(
						String.valueOf(o.value));
			}
		} else if (this.key == null) {
			ret = 1;
		} else if (o.key == null) {
			ret = -1;
		} else {
			ret = this.key.compareTo(o.key);
		}
		return ret;
	}

	/**
	 * @see java.util.Map.Entry#getKey()
	 */
	@Override
	public String getKey() {
		return this.key;
	}

	/**
	 * @see java.util.Map.Entry#getValue()
	 */
	@Override
	public T getValue() {
		return this.value;
	}

	/**
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	@Override
	public T setValue(final T value) {
		this.value = value;
		return this.value;
	}

}
