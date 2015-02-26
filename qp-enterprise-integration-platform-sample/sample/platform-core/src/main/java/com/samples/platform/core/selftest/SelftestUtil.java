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
package com.samples.platform.core.selftest;

import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;

/**
 * @author bhausen
 */
public class SelftestUtil {
	/** The {@link ApplicationPlaceholderConfigurer}. */
	@Autowired
	private ApplicationPlaceholderConfigurer properties;

	/**
	 * @return HTML
	 */
	public String getHtmlStart() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<!doctype html><html>");
		sb.append("<script>");
		sb.append("function getStyle(divname){");
		sb.append("\tvar temp = document.getElementById(divname).style.visibility;");
		sb.append("\treturn temp;");
		sb.append("}");
		sb.append("function switchStyle(divname){");
		sb.append("\tvar current = getStyle();");
		sb.append("\tif( current == \"visible\" ){");
		sb.append("\t\tdocument.getElementById(divname).style.visibility = \"hidden\";");
		sb.append("\t}else{");
		sb.append("\t\tdocument.getElementById(divname).style.visibility = \"visible\";");
		sb.append("\t}");
		sb.append("}");
		sb.append("</script><body>");
		sb.append("<h2>Selftest</h2>");
		return sb.toString();
	}

	/**
	 * @return HTML
	 */
	public String getHtmlEnd() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("</body></html>");
		return sb.toString();
	}

	/**
	 * @param t
	 * @return {@link Throwable} to HTML.
	 */
	public String getException(final Throwable t) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<h4>").append(t.getMessage()).append("</h4>\n");
		sb.append("<pre><code>\n");
		sb.append(ExceptionUtils.getFullStackTrace(t));
		sb.append("\n</code></pre>\n");
		return sb.toString();
	}

	/**
	 * @return {@link ServicebusPropertyPlaceholderConfigurer} to HTML.
	 */
	public String getApplicationProperties() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<h3>Selftest: System.getProperties</h2>");
		sb.append("<div id=\"System.getProperties\">");
		Map<String, String> map = this.properties.getProperties();
		sb.append("<ul>\n");
		for (String key : map.keySet()) {
			sb.append("<li>").append(key).append("=").append(map.get(key))
					.append("</li>\n");
		}
		sb.append("</ul></div>");
		return sb.toString();
	}

}
