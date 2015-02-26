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

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Hibernate;

import com.qpark.eip.core.PropertyDescriptorUtil.ObjectProperties;

public class EagerLoader {
	private static final Object[] EMPTY = new Object[] {};

	public static void loadCollection(final Collection<Object> os) {
		for (Object o : os) {
			load(o);
		}
	}

	public static void loadArray(final Object... os) {
		for (Object o : os) {
			load(o);
		}
	}

	public static void load(final Object o) {
		if (o != null) {
			if (List.class.isInstance(o)) {
				loadCollection((Collection) o);
			} else if (Collection.class.isInstance(o)) {
				loadCollection((Collection) o);
			} else if (o.getClass().isArray()) {
				loadArray((Object[]) o);
			} else {
				Hibernate.initialize(o);
				TreeMap<String, ObjectProperties> pdos = PropertyDescriptorUtil
						.getObjectProperties(o);
				Object property;
				for (ObjectProperties pdx : pdos.values()) {
					try {
						property = pdx.read.invoke(o, EMPTY);
						if (property != null) {
							if (!pdx.type.getPackage().getName()
									.startsWith("java")) {
								load(property);
							} else if (List.class.isInstance(property)) {
								load(property);
							} else if (Collection.class.isInstance(property)) {
								load(property);
							} else if (property.getClass().isArray()) {
								load(property);
							}
						}
					} catch (Exception e) {
						// nothing to do.
					}
				}
			}
		}
	}

}
