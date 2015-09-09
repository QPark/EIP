package com.qpark.maven.xmlbeans;

import java.util.Comparator;

/**
 * @author bhausen
 */
public class JavaImportComparator implements Comparator<String> {
	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final String o1, final String o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return 1;
		}
		if (o2 == null) {
			return -1;
		}
		int value = 0;
		if (o1.startsWith("java.")) {
			if (o2.startsWith("java.")) {
				value = o1.compareTo(o2);
			} else {
				value = -1;
			}
		} else if (o1.startsWith("javax.")) {
			if (o2.startsWith("java.")) {
				value = 1;
			} else if (o2.startsWith("javax.")) {
				value = o1.compareTo(o2);
			} else {
				value = -1;
			}
		} else if (o1.startsWith("org.")) {
			if (o2.startsWith("java.") || o2.startsWith("javax.")) {
				value = 1;
			} else if (o2.startsWith("org.")) {
				value = o1.compareTo(o2);
			} else {
				value = -1;
			}
		} else {
			if (o2.startsWith("java.") || o2.startsWith("javax.")
					|| o2.startsWith("org.")) {
				value = 1;
			} else {
				value = o1.compareTo(o2);
			}
		}
		return value;
	}
}
