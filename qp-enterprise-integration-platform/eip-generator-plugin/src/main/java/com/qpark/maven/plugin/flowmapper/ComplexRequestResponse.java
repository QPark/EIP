/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import javax.xml.namespace.QName;

import com.qpark.maven.xmlbeans.ComplexType;

/**
 * @author bhausen
 */
public class ComplexRequestResponse {
	String interfaceName;

	String packageName;
	ComplexType request;
	QName requestQName;
	ComplexType response;
	QName responseQName;

	ComplexRequestResponse(final ComplexType request,
			final ComplexType response) {
		this.request = request;
		this.requestQName = request.getType().getName();
		this.response = response;
		this.responseQName = response.getType().getName();
	}
}
