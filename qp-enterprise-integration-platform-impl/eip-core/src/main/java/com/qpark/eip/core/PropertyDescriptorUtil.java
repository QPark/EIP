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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.Transient;

/**
 * @author bhausen
 */
public abstract class PropertyDescriptorUtil {
	/** The Object properties gotten from the {@link PropertyDescriptor}s. */
	public static class ObjectProperties implements Comparable<ObjectProperties> {
		String name;

		/**
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * @return the read
		 */
		public Method getRead() {
			return this.read;
		}

		/**
		 * @return the write
		 */
		public Method getWrite() {
			return this.write;
		}

		/**
		 * @return the type
		 */
		public Class<?> getType() {
			return this.type;
		}

		/**
		 * @return the list
		 */
		public boolean isList() {
			return this.list;
		}

		/**
		 * @return the xtype
		 */
		public Class<?> getXtype() {
			return this.xtype;
		}

		Method read;
		Method write;
		Class<?> type;
		boolean list;
		Class<?> xtype;

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer(256);
			sb.append(this.name).append(": ");
			if (this.list) {
				sb.append("List of ");
			}
			sb.append(this.type.getSimpleName());
			return sb.toString();
		}

		/**
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(final ObjectProperties o) {
			return this.name.compareTo(o.name);
		}
	}

	private static final HashMap<Class<?>, TreeMap<String, ObjectProperties>> OBJECT_PROPERTIES_CACHE = new HashMap<Class<?>, TreeMap<String, ObjectProperties>>();
	public static final Object[] EMPTY = new Object[] {};

	public static Constructor<?> getStandardConstructor(final Class<?> cl)
			throws SecurityException, NoSuchMethodException {
		return cl.getConstructor(new Class[0]);
	}

	public static TreeMap<String, ObjectProperties> getObjectProperties(final Object o) {
		if (o == null) {
			return new TreeMap<String, ObjectProperties>();
		} else {
			return getObjectProperties(o.getClass());
		}
	}

	public static final Class<?>[] EMPTY_CLASS = new Class[0];

	private static final HashMap<Class<?>, Method> ID_METHOD_CACHE = new HashMap<Class<?>, Method>();

	public static Method getIdMethod(final Object o) {
		Method m = null;
		if (o != null) {
			m = ID_METHOD_CACHE.get(o.getClass());
			if (m == null && !ID_METHOD_CACHE.containsKey(o.getClass())) {
				try {
					m = o.getClass().getMethod("getUUID", EMPTY_CLASS);
				} catch (Exception e0) {
					try {
						m = o.getClass().getMethod("getUuid", EMPTY_CLASS);
					} catch (Exception e1) {
						try {
							m = o.getClass().getMethod("getID", EMPTY_CLASS);
						} catch (Exception e2) {
							try {
								m = o.getClass().getMethod("getId", EMPTY_CLASS);
							} catch (Exception e3) {
							}
						}
					}
				}
			}
		}
		return m;
	}

	public static Enum<?> getEnum(final Object o, final Class<?> cl) throws Exception {
		Enum<?> value = null;
		if (o != null) {
			Method m = getEnumValueMethod(o.getClass());
			if (m != null) {
				Object enumFullName = m.invoke(o, EMPTY);
				if (enumFullName != null) {
					String[] ss = String.valueOf(enumFullName).split("\\.(?=[^\\.]+$)");
					String enumName = null;
					if (ss.length == 2) {
						enumName = ss[1];
					} else if (ss.length == 1) {
						enumName = ss[0];
					}
					if (enumName != null) {
						m = getEnumFromValueMethod(cl);
						if (m != null) {
							value = (Enum<?>) m.invoke(cl, enumName);
						}
					}
				}
			}
		}
		return value;
	}

	public static Object getObject(final Class<?> type) throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Constructor<?> ctor = type.getConstructor(EMPTY_CLASS);
		return ctor.newInstance(EMPTY);
	}

	private static final HashMap<Class<?>, Method> ENUM_VALUE_METHOD_CACHE = new HashMap<Class<?>, Method>();

	public static Method getEnumValueMethod(final Object o) {
		Method m = null;
		if (o != null) {
			m = ENUM_VALUE_METHOD_CACHE.get(o.getClass());
			if (m == null && !ENUM_VALUE_METHOD_CACHE.containsKey(o.getClass())) {
				try {
					m = o.getClass().getMethod("value", EMPTY_CLASS);
				} catch (Exception e0) {
				}
			}
		}
		return m;
	}

	private static final HashMap<Class<?>, Method> ENUM_FROM_VALUE_METHOD_CACHE = new HashMap<Class<?>, Method>();

	public static Method getEnumFromValueMethod(final Class<?> cl) {
		Method m = null;
		if (cl != null) {
			m = ENUM_FROM_VALUE_METHOD_CACHE.get(cl);
			if (m == null && !ENUM_FROM_VALUE_METHOD_CACHE.containsKey(cl)) {
				try {
					m = cl.getMethod("fromValue", new Class[] { String.class });
				} catch (Exception e0) {
				}
			}
		}
		return m;
	}

	public static Field getField(final Class<?> cl, final PropertyDescriptor pd) {
		Field f = null;
		for (Field df : cl.getDeclaredFields()) {
			if (df.getName().equalsIgnoreCase(pd.getName()) && df.getType().equals(pd.getPropertyType())) {
				f = df;
				break;
			}
		}
		if (f == null && cl.getSuperclass().getPackage().getName().startsWith("com.ses.")) {
			f = getField(cl.getSuperclass(), pd);
		}
		return f;
	}

	public static Class<?> getGenericType(final Class<?> cl, final PropertyDescriptor pd) {
		Class<?> c = null;
		Field f = getField(cl, pd);
		if (f != null) {
			Type type = f.getGenericType();
			if (type instanceof ParameterizedType) {
				for (Type t : ((ParameterizedType) type).getActualTypeArguments()) {
					c = (Class<?>) t;
					break;
				}
			}
		}
		return c;
	}

	public static boolean isTransient(final Method m) {
		boolean t = false;
		if (m != null) {
			t = Modifier.isTransient(m.getModifiers());
			if (!t) {
				t = m.getAnnotation(Transient.class) != null;
			}
		}
		return t;
	}

	public static TreeMap<String, ObjectProperties> getObjectProperties(final Class<?> cl) {
		TreeMap<String, ObjectProperties> pdos = null;
		if (cl != null) {
			pdos = OBJECT_PROPERTIES_CACHE.get(cl);
			if (pdos == null) {
				try {
					BeanInfo bi = Introspector.getBeanInfo(cl, Object.class);
					PropertyDescriptor[] pds = bi.getPropertyDescriptors();
					pdos = new TreeMap<String, ObjectProperties>();
					ObjectProperties op;

					for (PropertyDescriptor pd : pds) {
						if (!isTransient(pd.getReadMethod())) {
							op = new ObjectProperties();
							op.name = pd.getName();
							op.write = pd.getWriteMethod();
							op.read = pd.getReadMethod();
							op.type = pd.getPropertyType();
							if (List.class.isAssignableFrom(op.type)) {
								op.list = true;
								op.type = getGenericType(cl, pd);
							}
							if (op.name != null && op.type != null && op.read != null) {
								pdos.put(op.name, op);
							}
						}
					}
					OBJECT_PROPERTIES_CACHE.put(cl, pdos);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (pdos == null) {
			pdos = new TreeMap<String, ObjectProperties>();
		}
		return pdos;
	}
}
