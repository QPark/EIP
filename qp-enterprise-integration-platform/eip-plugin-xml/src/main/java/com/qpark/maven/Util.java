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
package com.qpark.maven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author bhausen
 */
public class Util {
	public static String capitalize(final String name) {
		String s = name;
		if (s != null && s.length() > 0) {
			StringBuffer sb = new StringBuffer(s.length());
			sb.append(s.substring(0, 1).toUpperCase());
			if (s.length() > 1) {
				sb.append(s.substring(1, s.length()));
			}
			s = sb.toString();
		}
		return s;
	}

	/**
	 * @param millis the duration in milliseconds.
	 * @return a string with the duration formatted with
	 *         <code>HH:mm:ss.SSS</code>.
	 */
	public static String getDuration(final long millis) {
		DecimalFormat df20 = new DecimalFormat("00");
		DecimalFormat df30 = new DecimalFormat("000");
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTimeInMillis(millis);
		return new StringBuffer(12)
				.append(df20.format(c.get(Calendar.HOUR_OF_DAY))).append(":")
				.append(df20.format(c.get(Calendar.MINUTE))).append(":")
				.append(df20.format(c.get(Calendar.SECOND))).append(".")
				.append(df30.format(c.get(Calendar.MILLISECOND))).toString();
	}

	public static String lowerize(final String name) {
		String s = name;
		if (s != null && s.length() > 0) {
			StringBuffer sb = new StringBuffer(s.length());
			sb.append(s.substring(0, 1).toLowerCase());
			if (s.length() > 1) {
				sb.append(s.substring(1, s.length()));
			}
			s = sb.toString();
		}
		return s;
	}

	public static String capitalizePackageName(final String packageName) {
		String s = packageName;
		if (packageName != null) {
			String[] ss = packageName.replaceAll("-", ".").split("\\.");
			StringBuffer sb = new StringBuffer(packageName.length());
			for (String sx : ss) {
				if (sx.length() > 0) {
					sb.append(sx.substring(0, 1).toUpperCase());
					if (sx.length() > 1) {
						sb.append(sx.substring(1, sx.length()));
					}
				}
			}
			s = sb.toString();
		}
		return s;
	}

	public static void main(final String[] args) {
		System.out.println(capitalizePackageName("Alkj.llkj-ljaf2j34lkj"));
		System.out.println(getXjcSetterName("v2kConstraintRow"));
		System.out.println(getXjcSetterName("organi_s2a.tion"));
		// @XmlElement(name = "organi_s2a.tion", required = true)
		// protected String organiS2ATion;

		System.out.println(getXjcCamelCase("Alkj.llkj-ljaf2j34lkj"));
		System.out.println(getXjcCamelCase("v2kConstraintRow"));
		System.out.println(getXjcCamelCase("Ab-Bc-cd_Ef_fg.Gh.hi2Ij3jk"));
		System.out.println(getXjcCamelCase("v2kConstraintRow"));
		System.out.println(getXjcCamelCase("organi_s2a.tion"));
	}

	public static String getXjcClassName(final String typeQNameLocalPart) {
		String s = typeQNameLocalPart;
		if (typeQNameLocalPart != null) {
			s = getXjcCamelCase(typeQNameLocalPart).toString();
		}
		return s;
	}

	public static String getXjcGetterName(final String propertyName) {
		String s = propertyName;
		if (propertyName != null) {
			StringBuffer sb = getXjcCamelCase(propertyName);
			sb.insert(0, "get");
			s = sb.toString();
		}
		return s;
	}

	public static String getXjcSetterName(final String propertyName) {
		String s = propertyName;
		if (propertyName != null) {
			StringBuffer sb = getXjcCamelCase(propertyName);
			sb.insert(0, "set");
			s = sb.toString();
		}
		return s;
	}

	public static String getXjcPropertyName(final String propertyName) {
		String s = propertyName;
		if (propertyName != null) {
			s = getXjcTranslated(propertyName, false).toString();
		}
		return s;
	}

	private static StringBuffer getXjcTranslated(final String name,
			final boolean camelCase) {
		char[] chars = name.toCharArray();
		StringBuffer sb = new StringBuffer(chars.length);
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if (camelCase && i == 0) {
				ch = Character.toUpperCase(ch);
			}
			if ((ch == '.' || ch == '-' || ch == '_' || Character.isDigit(ch))
					&& i < chars.length - 1) {
				chars[i + 1] = Character.toUpperCase(chars[i + 1]);
			}
			if (ch != '.' && ch != '-' && ch != '_') {
				sb.append(ch);
			}
		}
		return sb;

	}

	private static StringBuffer getXjcCamelCase(final String name) {
		return getXjcTranslated(name, true);
	}

	/**
	 * Get a {@link UUID} name for the given {@link Class} and name. To get an
	 * {@link UUID} object out of the returned string use
	 * {@link UUID#fromString(String)}.
	 * @param type the {@link Class} of the Object e.g.
	 *            com.a.b.bus.domain.c.AbcType.
	 * @param name The name of the object.
	 * @return The UUID as String.
	 */
	public static String getUUID(final Class<?> type, final String name) {
		String s = null;
		if (type != null && name != null) {
			s = UUID.nameUUIDFromBytes(
					new StringBuffer(256).append(type.getName()).append("#")
							.append(name).toString().getBytes()).toString();
		} else if (name != null) {
			s = UUID.nameUUIDFromBytes(
					new StringBuffer(256).append("#").append(name).toString()
							.getBytes()).toString();
		} else {
			s = UUID.nameUUIDFromBytes(
					new StringBuffer(256).append("#")
							.append(System.currentTimeMillis()).toString()
							.getBytes()).toString();
		}
		return s;
	}

	private static String getContextPath(final Collection<String> packageNames) {
		StringBuffer sb = new StringBuffer(128);
		String[] array = packageNames.toArray(new String[packageNames.size()]);
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(":");
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * @return the file
	 */
	public static File getFile(final File outputDirectory, final String fileName) {
		return getFile(outputDirectory, "", fileName);
	}

	/**
	 * @return the file
	 */
	public static File getFile(final File outputDirectory,
			final String packageName, final String fileName) {
		File f = new File(new StringBuffer(outputDirectory.getAbsolutePath())
				.append(File.separatorChar)
				.append(packageName == null ? "" : packageName.replace('.',
						File.separatorChar)).append(File.separatorChar)
				.append(fileName).toString());
		if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
			throw new RuntimeException("Can not create directory "
					+ f.getParent());
		}
		return f;
	}

	public static String getGeneratedAt() {
		return getGeneratedAt(null, null);
	}

	public static String getGeneratedAt(final Date d) {
		return getGeneratedAt(d, null);
	}

	public static String getGeneratedAt(final Date d, final Class<?> c) {
		StringBuffer sb = new StringBuffer(128);
		if (c != null) {
			sb.append(c.getSimpleName());
			sb.append(": ");
		}
		sb.append("Generated at ");
		sb.append(getXsdDateTime(d));
		return sb.toString();
	}

	public static String getRelativePath(final File baseDirectory, final File f) {
		String s = f.getAbsolutePath();
		String base = baseDirectory.getAbsolutePath();
		if (s.startsWith(base)) {
			s = s.substring(base.length());
		}
		return s;
	}

	public static String getRelativePathTranslated(final File baseDirectory,
			final File f) {
		String s = Util.getRelativePath(baseDirectory, f);
		s = s.replace("\\", "/");
		return s;
	}

	public static String getXsdDateTime(final Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (d != null) {
			return sdf.format(d);
		} else {
			return sdf.format(new Date());
		}
	}

	/**
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static String readFile(final File f) throws IOException {
		String s = null;
		if (f.exists()) {
			s = new Scanner(f).useDelimiter("\\A").next();
		}
		return s;
	}

	public static String splitOnCapital(final String name) {
		StringBuffer sb = new StringBuffer();
		if (name != null) {
			char ch;
			int l = name.length();
			for (int i = 0; i < l; i++) {
				ch = name.charAt(i);
				if (Character.isUpperCase(ch)) {
					if (sb.length() == 0) {
						// No additional space
					} else if (i - 1 > -1
							&& !Character.isUpperCase(name.charAt(i - 1))) {
						sb.append(' ');
					} else if (i + 1 < l
							&& Character.isUpperCase(name.charAt(i + 1))) {
						// No additional space
					} else {
						sb.append(' ');
					}
					sb.append(Character.toLowerCase(ch));
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @param f
	 * @param s
	 * @throws IOException
	 */
	public static void writeToFile(final File f, final String s)
			throws IOException {
		FileWriter writer = new FileWriter(f);
		writer.write(s);
		writer.flush();
		writer.close();
	}
}
