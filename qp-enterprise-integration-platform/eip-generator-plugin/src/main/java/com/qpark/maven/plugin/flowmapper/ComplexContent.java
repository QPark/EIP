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

public class ComplexContent {
	ComplexType ct;

	String interfaceName;

	boolean isComplex;
	boolean isComplexUUID;
	boolean isDirect;
	boolean isInterfaceType;
	String packageName;
	QName qName;
	ComplexContent(final ComplexType ct, final boolean isDirect,
			final boolean isComplexUUID, final boolean isComplex,
			final boolean isInterfaceType) {
		this.ct = ct;
		this.qName = ct.getType().getName();
		this.isDirect = isDirect;
		this.isComplexUUID = isComplexUUID;
		this.isComplex = isComplex;
		this.isInterfaceType = isInterfaceType;
	}
	public String getFQInterfaceName() {
		return new StringBuffer(this.ct.getPackageName().length() + 1
				+ this.interfaceName.length()).append(this.ct.getPackageName())
				.append(".").append(this.interfaceName).toString();
	}
}
