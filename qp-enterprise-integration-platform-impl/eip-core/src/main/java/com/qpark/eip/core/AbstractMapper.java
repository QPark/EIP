/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

import org.apache.commons.lang.StringUtils;

/**
 * Maps <code>T</code> into <code>V</code>.
 *
 * @author bhausen
 * @param <T>
 *            type of source.
 * @param <V>
 *            type of destination.
 */
public abstract class AbstractMapper<T, V> {

	protected final T source;
	protected V destination;

	public AbstractMapper(final T pT) {
		super();
		this.source = pT;
	}

	/** Maps <code>T</code> into <code>V</code>. */
	public void run() {
		this.destination = this.map();
	}

	protected abstract V map();

	/**
	 * @return the mapped object.
	 */
	public V getDestination() {
		return this.destination;
	}

	public boolean hasValue(final Object o) {
		if (o instanceof String) {
			return StringUtils.isNotBlank((String) o);
		}
		return o != null;
	}

}
