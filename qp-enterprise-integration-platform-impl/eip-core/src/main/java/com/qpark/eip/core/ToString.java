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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;

import com.qpark.eip.core.PropertyDescriptorUtil.ObjectProperties;

public class ToString {
	static class Y {
		public Y() {
			this.z = new Z();
		}

		private String y = "y";

		/**
		 * @return the y.
		 */
		public String getY() {
			return this.y;
		}

		/**
		 * @param y the y to set.
		 */
		public void setY(final String y) {
			this.y = y;
		}

		/**
		 * @return the z.
		 */
		public Z getZ() {
			return this.z;
		}

		/**
		 * @param z the z to set.
		 */
		public void setZ(final Z z) {
			this.z = z;
		}

		private Z z;
	}

	static class Z {
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Baetsch";
		}

		private String z = "z";

		/**
		 * @return the z.
		 */
		public String getZ() {
			return this.z;
		}

		/**
		 * @param z the z to set.
		 */
		public void setZ(final String z) {
			this.z = z;
		}
	}

	static class X {
		private BigDecimal db = new BigDecimal(2.0);
		private String x = "x";
		private ArrayList<Y> ys = new ArrayList<ToString.Y>();

		/**
		 * @return the db.
		 */
		public BigDecimal getDb() {
			return this.db;
		}

		/**
		 * @param db the db to set.
		 */
		public void setDb(final BigDecimal db) {
			this.db = db;
		}

		/**
		 * @return the x.
		 */
		public String getX() {
			return this.x;
		}

		/**
		 * @param x the x to set.
		 */
		public void setX(final String x) {
			this.x = x;
		}

		/**
		 * @return the ys.
		 */
		public ArrayList<Y> getYs() {
			return this.ys;
		}

		/**
		 * @param ys the ys to set.
		 */
		public void setYs(final ArrayList<Y> ys) {
			this.ys = ys;
		}

		public X() {
			this.ys.add(new Y());
			this.ys.add(new Y());
		}
	}

	public static void main(final String[] args) {
		X x = new X();
		X y = new X();
		ArrayList<X> list = new ArrayList<ToString.X>();
		list.add(x);
		list.add(y);
		System.out.println(toString(x));
		System.out.println(toString(new Z()));
	}

	private static String toStringNativeArray(final String name, final Object o) {
		StringBuffer sb = new StringBuffer(4098);
		int n = Array.getLength(o);
		if (byte[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]byte{...}");
		} else if (boolean[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]boolean{");
			boolean[] booleans = (boolean[]) o;
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					sb.append(",");
				}
				if (booleans[j]) {
					sb.append("1");
				} else {
					sb.append("0");
				}
			}
			sb.append("}");
		} else if (char[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]char{");
			sb.append(new String((char[]) o));
			sb.append("}");
		} else if (short[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]short{");
			short[] shorts = (short[]) o;
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					sb.append(",");
				}
				sb.append(shorts[j]);
			}
			sb.append("}");
		} else if (int[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]int{");
			int[] ints = (int[]) o;
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					sb.append(",");
				}
				sb.append(ints[j]);
			}
			sb.append("}");
		} else if (long[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]long{");
			long[] longs = (long[]) o;
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					sb.append(",");
				}
				sb.append(longs[j]);
			}
			sb.append("}");
		} else if (float[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]float{");
			float[] floats = (float[]) o;
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					sb.append(",");
				}
				sb.append(floats[j]);
			}
			sb.append("}");
		} else if (double[].class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append("[").append(n).append("]double{");
			double[] doubles = (double[]) o;
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					sb.append(",");
				}
				sb.append(doubles[j]);
			}
			sb.append("}");
		} else {
			toString(name, (Object[]) o);
		}
		return sb.toString();
	}

	private static String toString(final String name, final Object[] o) {
		StringBuffer sb = new StringBuffer(4098);
		// sb.append("\n");
		sb.append(outName(name, null));
		int j = 1;
		for (Object obj : o) {
			sb.append("\n");
			if (name != null && name.length() > 0) {
				sb.append("\t");
			}
			sb.append("[").append(j++);
			sb.append("/").append(o.length);
			sb.append("]").append(toString(obj));
		}
		return sb.toString();
	}

	private static String toString(final String name, final Enum<?> o) {
		StringBuffer sb = new StringBuffer(4098);
		sb.append(outName(name, o));
		sb.append(o.toString());
		return sb.toString();
	}

	private static String outName(final String name, final Object o) {
		return outName(name, o == null ? Object.class : o.getClass());
	}

	private static String outName(final String name, final Class<?> o) {
		StringBuffer sb = new StringBuffer(256);
		if (isComClass(o)) {
			// sb.append("\n");
		}
		sb.append("\n");
		if (name != null && name.length() > 0) {
			sb.append(name);
		}
		if (o != null) {
			sb.append("<");
			sb.append(o.getSimpleName());
			sb.append(">");
			if (name != null && name.length() > 0) {

			} else {
				sb.append(":");
			}
		}
		if (name != null && name.length() > 0) {
			sb.append("=");
		}
		return sb.toString();
	}

	private static String toString(final String name, final Object o,
			final boolean outInputStreams) {
		StringBuffer sb = new StringBuffer(4098);
		if (o == null) {
			sb.append(outName(name, o));
			sb.append("null");
		} else if (o.getClass().isEnum()) {
			sb.append(toString(name, (Enum<?>) o));
		} else if (o.getClass().isArray()) {
			sb.append(toStringNativeArray(name, o));
		} else if (Collection.class.isInstance(o)) {
			sb.append(toString(name, ((Collection<?>) o).toArray(new Object[0])));
		} else if (InputStream.class.isInstance(o)) {
			sb.append(outName(name, o));
			try {
				if (outInputStreams) {
					sb.append(new String(IOUtils.toByteArray((InputStream) o)));
				} else {
					sb.append(IOUtils.toByteArray((InputStream) o).length);
				}
			} catch (IOException e) {
				sb.append("Can not get bytes out of InputStream ").append(name);
			}
		} else if (Reader.class.isInstance(o)) {
			sb.append(outName(name, o));
			try {
				sb.append(new String(IOUtils.toByteArray((Reader) o)));
			} catch (IOException e) {
				sb.append("Can not get bytes out of Reader ").append(name);
			}
		} else if (Throwable.class.isInstance(o)) {
			sb.append(outName(name, o));
			sb.append(getStackTrace((Throwable) o));
		} else if (hasToStringMethod(o)) {
			sb.append(outName(name, o));
			sb.append(o.toString());
		} else {
			sb.append(outName(name, o));
			TreeMap<String, ObjectProperties> ops = PropertyDescriptorUtil
					.getObjectProperties(o);
			Object ox;
			int i = 0;
			for (ObjectProperties op : ops.values()) {
				if (i > 0) {
					sb.append(", ");
				}
				try {
					ox = op.read.invoke(o, PropertyDescriptorUtil.EMPTY);
					if (ox == null) {
						sb.append(outName(op.name, op.type));
						sb.append("null");
					} else {
						sb.append(toString(op.name, ox, outInputStreams));
					}
				} catch (Exception e) {
					sb.append(outName(op.name, op.type));
					sb.append(e.getMessage());
				}
				i++;
			}
			// if (i > 0) {
			// sb.append(" ");
			// }
		}
		return sb.toString();
	}

	public static String toString(final Object o) {
		return toString(o, false);
	}

	public static String toString(final Object o, final boolean outInputStreams) {
		StringBuffer sb = new StringBuffer(4098);
		sb.append(toString("", o, outInputStreams));
		return sb.toString();
	}

	private static boolean isComClass(final Object o) {
		return o != null && isComClass(o.getClass());
	}

	private static boolean isComClass(final Class<?> c) {
		return c != null && c.getName().startsWith("com");
	}

	public static String getStackTrace(final Throwable t) {
		return getStackTrace(t, false);
	}

	public static final String CLASSNAME = "com.ses.osp";

	private static String getStackTrace(final Throwable t, final boolean isCause) {
		StringBuffer sb = new StringBuffer(1024);
		if (isCause) {
			sb.append("Caused by: ");
		}
		sb.append(t.getClass().getName()).append(": ").append(t.getMessage())
				.append("\n");
		StackTraceElement[] stack = t.getStackTrace();
		int classNameLines = 0;
		int classNameLinesMax = 3;
		boolean printDots = false;
		boolean firstLine = true;
		for (StackTraceElement elem : stack) {
			if (firstLine || elem.getClassName().startsWith(CLASSNAME)
					&& classNameLines <= classNameLinesMax) {
				sb.append("\tat ").append(elem.toString()).append("\n");
				classNameLines++;
				firstLine = false;
				printDots = false;
			} else {
				if (!printDots) {
					sb.append("\tat ...\n");
					printDots = true;
				}
				classNameLines = 0;
			}
		}
		Throwable cause = t.getCause();
		if (cause != null) {
			sb.append(getStackTrace(cause, true));
		}
		return sb.toString();
	}

	private static boolean hasToStringMethod(final Object o) {
		boolean b = false;
		if (isComClass(o)) {
			b = false;
			// try {
			// Method m = o.getClass().getMethod("toString", new Class[0]);
			// if (m != null && isComClass(m.getDeclaringClass())) {
			// b = true;
			// }
			// } catch (Exception e) {
			// b = false;
			// }
		} else {
			b = true;
		}
		return b;
	}
}
