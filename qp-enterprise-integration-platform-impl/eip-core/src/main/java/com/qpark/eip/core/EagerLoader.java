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
import java.util.TreeMap;

import com.qpark.eip.core.PropertyDescriptorUtil.ObjectProperties;

/**
 * Loads complete objects via JPA.
 *
 * @see http://stackoverflow.com/questions/15359306/how-to-load-lazy-fetched-
 *      items-from-hibernate-jpa-in-my-controller
 * @author bhausen
 */
public class EagerLoader {
    private static final Object[] EMPTY = new Object[] {};

    /**
     * You will have to make an explicit call on the lazy collection in order to
     * initialize it (common practice is to call .size() for this purpose). In
     * Hibernate there is a dedicated method for this (Hibernate.initialize()),
     * but JPA has no equivalent of that. Of course you will have to make sure
     * that the invokation is done, when the session is still available, so
     * annotate your controller method with @Transactional. An alternative is to
     * create an intermediate Service layer between the Controller and the
     * Repository that could expose methods which initialize lazy collections.
     *
     * @param os
     */
    public static void loadCollection(final Collection<?> os) {
	os.size();
	for (Object o : os) {
	    load(o);
	}
    }

    /**
     * Load each {@link Object} out of the array of {@link Object}s.
     *
     * @param os
     */
    public static void loadArray(final Object... os) {
	for (Object o : os) {
	    load(o);
	}
    }

    /**
     * Load each property of the {@link Object}.
     * 
     * @param o
     *            the {@link Object}.
     */
    public static void load(final Object o) {
	if (o != null) {
	    if (Collection.class.isInstance(o)) {
		loadCollection((Collection<?>) o);
	    } else if (o.getClass().isArray()) {
		loadArray((Object[]) o);
	    } else {
		TreeMap<String, ObjectProperties> pdos = PropertyDescriptorUtil.getObjectProperties(o);
		Object property;
		for (ObjectProperties pdx : pdos.values()) {
		    try {
			property = pdx.read.invoke(o, EMPTY);
			if (property != null) {
			    if (!pdx.type.getPackage().getName().startsWith("java")) {
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
