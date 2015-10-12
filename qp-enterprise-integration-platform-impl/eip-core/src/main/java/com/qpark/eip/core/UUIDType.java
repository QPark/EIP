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

import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author bhausen
 */
public class UUIDType {
    /** The regular expression of a UUID. */
    public static final String PATTERN_STRING = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    private static final Random RANDOM = new Random();

    /**
     * @return get a uuid.
     */
    public static String getUUID() {
	return UUID.nameUUIDFromBytes(new StringBuffer(256).append(System.currentTimeMillis()).append("#")
		.append(String.valueOf(RANDOM.nextDouble())).toString().getBytes()).toString();
    }

    public static String getUUID(final Class<?> type, final Object... data) {
	String s = null;
	StringBuffer sb = new StringBuffer(512);
	if (type != null) {
	    sb.append(type.getName()).append("#");
	}
	if (data != null && data.length > 0) {
	    for (Object element : data) {
		sb.append(element == null ? "null" : element.toString());
		sb.append("#");
	    }
	}
	s = UUID.nameUUIDFromBytes(sb.toString().getBytes()).toString();
	return s;
    }

    /**
     * Get a {@link UUID} name for the given {@link Class} and name. To get an
     * {@link UUID} object out of the returned string use
     * {@link UUID#fromString(String)}.
     * 
     * @param type
     *            the {@link Class} of the Object e.g.
     *            a.b.c.bus.domain.d.AbcType.
     * @param name
     *            The name of the object
     * @return The UUID as String.
     */
    public static String getUUID(final Class<?> type, final String name) {
	String s = null;
	if (type != null && name != null) {
	    s = UUID.nameUUIDFromBytes(
		    new StringBuffer(256).append(type.getName()).append("#").append(name).toString().getBytes())
		    .toString();
	} else if (name != null) {
	    s = UUID.nameUUIDFromBytes(new StringBuffer(256).append("#").append(name).toString().getBytes()).toString();
	} else {
	    s = UUID.nameUUIDFromBytes(
		    new StringBuffer(256).append("#").append(System.currentTimeMillis()).toString().getBytes())
		    .toString();
	}
	return s;
    }

    public static UUIDType getUUIDType(final Class<?> type, final String name) {
	return new UUIDType(getUUID(type, name));
    }

    public static UUIDType getUUIDType(final String name) {
	return new UUIDType(getUUID(null, name));
    }

    /**
     * @param s
     *            the string to check if it is an UUID string.
     * @return <code>true</code> if the string is not null and matches the rules
     *         of a UUID ({{@link #PATTERN_STRING}) else <code>false</code>.
     */
    public static boolean isUUIDString(final String s) {
	boolean isUUID = false;
	if (s != null) {
	    isUUID = PATTERN.matcher(s).matches();
	}
	return isUUID;
    }

    public static UUIDType parseUUIDType(final String value) {
	UUIDType uuid = new UUIDType();
	uuid.setValue(value);
	return uuid;
    }

    public static String printUUIDType(final UUIDType uuid) throws IllegalArgumentException {
	if (uuid == null || uuid.getValue() == null) {
	    throw new IllegalArgumentException("UUIDType is null");
	} else {
	    return uuid.value;
	}
    }

    private String value;

    public UUIDType() {
	// nothing to do
    }

    public UUIDType(final String value) {
	this.value = value;
    }

    /**
     * @return the value.
     */
    public String getValue() {
	return this.value;
    }

    /**
     * @param value
     *            the value to set.
     */
    public void setValue(final String value) {
	this.value = value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return String.valueOf(this.value);
    }
}
