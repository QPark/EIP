package com.samples.platform.service.common;

public class X {
	private void x() {
		ClassLoader cl;
		java.net.URL[] urls;
		ClassLoader sysCl = ClassLoader.getSystemClassLoader();

		StringBuffer sb = new StringBuffer();
		sb.append("<h2>Service is available</h2>");

		/* System properties */
		sb.append("<h3>System Properties</h3>");
		java.util.TreeSet<String> propertyNames = new java.util.TreeSet<String>();
		propertyNames.addAll(System.getProperties().stringPropertyNames());
		sb.append("<table class=\"portletlrborder\">\n");
		sb.append("<tr class=\"tablerowheader\">");
		sb.append("<th>Name</th><th>Value</th></tr>\n");
		for (String propertyName : propertyNames) {
			sb.append("<tr class=\"ListRow\"><td>");
			sb.append(propertyName);
			sb.append("</td><td>");
			sb.append(System.getProperty(propertyName));
			sb.append("</td></tr>\n");
		}
		sb.append("</table>\n");

		/* Current lass loader */
		cl = this.getClass().getClassLoader();
		sb.append("<h3>This ClassLoader</h3>");

		if (java.net.URLClassLoader.class.isInstance(cl)) {
			urls = ((java.net.URLClassLoader) cl).getURLs();
			sb.append("<table class=\"portletlrborder\">\n");
			sb.append("<tr class=\"tablerowheader\">");
			sb.append("<th>Url</th></tr>\n");
			for (java.net.URL url : urls) {
				sb.append("<tr class=\"ListRow\"><td>");
				sb.append(url.toString());
				sb.append("</td></tr>\n");
			}
		}
		cl = cl.getParent();
		while (cl != sysCl) {
			sb.append("<h3>Parent Classloader</h3>");
			if (java.net.URLClassLoader.class.isInstance(cl)) {
				urls = ((java.net.URLClassLoader) cl).getURLs();
				sb.append("<table class=\"portletlrborder\">\n");
				sb.append("<tr class=\"tablerowheader\">");
				sb.append("<th>Url</th></tr>\n");
				for (java.net.URL url : urls) {
					sb.append("<tr class=\"ListRow\"><td>");
					sb.append(url.toString());
					sb.append("</td></tr>\n");
				}
			}
			cl = cl.getParent();
		}

		/* System class loader */
		cl = sysCl;
		sb.append("<h3>SystemClassLoader</h3>");
		if (java.net.URLClassLoader.class.isInstance(cl)) {
			urls = ((java.net.URLClassLoader) cl).getURLs();
			sb.append("<table class=\"portletlrborder\">\n");
			sb.append("<tr class=\"tablerowheader\">");
			sb.append("<th>Url</th></tr>\n");
			for (java.net.URL url : urls) {
				sb.append("<tr class=\"ListRow\"><td>");
				sb.append(url.toString());
				sb.append("</td></tr>\n");
			}
		}
	}
}
