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

class ComplexContent {
	ComplexType ct;
	String interfaceClassName = "";
	boolean isComplex;
	boolean isComplexUUID;
	boolean isDefault;
	boolean isDirect;
	boolean isInterfaceType;
	boolean isTabular;
	String interfacePackageName;
	QName qName;

	ComplexContent(final ComplexType ct) {
		this.ct = ct;
		this.qName = ct.getType().getName();
	}

	public String getFQInterfaceName() {
		return new StringBuffer(this.ct.getPackageName().length() + 1
				+ this.interfaceClassName.length()).append(this.ct.getPackageName())
						.append(".").append(this.interfaceClassName).toString();
	}

	ComplexContent setComplex() {
		this.isComplex = true;
		return this;
	}

	ComplexContent setComplexUUID() {
		this.isComplexUUID = true;
		return this;
	}

	ComplexContent setDefault() {
		this.isDefault = true;
		return this;
	}

	ComplexContent setDirect() {
		this.isDirect = true;
		return this;
	}

	ComplexContent setInterface() {
		this.isInterfaceType = true;
		return this;
	}

	ComplexContent setTabular() {
		this.isTabular = true;
		return this;
	}
}
