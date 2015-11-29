/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class GetServiceIds {
	public static List<String> getServiceIds(final String serviceId) {
		List<String> list = new ArrayList<String>();
		if (serviceId != null && serviceId.trim().length() > 0) {
			for (String string : serviceId.split(",")) {
				if (string.trim().length() > 0) {
					list.add(string.trim());
				}
			}
		}
		return list;
	}

	public static List<String> getAllServiceIds(final XsdsUtil xsds) {
		List<String> list = new ArrayList<String>();
		TreeSet<String> ids = new TreeSet<String>();
		for (ElementType element : xsds.getElementTypes()) {
			ids.add(element.getServiceId());
		}
		list.addAll(ids);
		return list;
	}

	public static String getServiceIdBasename(final String serviceId) {
		StringBuffer sb = new StringBuffer();
		List<String> list = getServiceIds(serviceId);
		if (list.size() > 0) {
			for (String string : list) {
				sb.append(string);
			}
		}
		return sb.toString();
	}
}
