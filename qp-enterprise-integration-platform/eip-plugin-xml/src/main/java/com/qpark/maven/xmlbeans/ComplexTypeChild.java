/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.math.BigInteger;

import org.apache.xmlbeans.XmlAnySimpleType;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class ComplexTypeChild {
	private final ComplexType ct;
	private final String childName;
	private final String javaChildName;
	private String getterName;
	private final String setterName;
	private final BigInteger minOccurs;
	private final BigInteger maxOccurs;
	private final boolean optional;
	private final boolean list;
	private final XmlAnySimpleType defaultValue;

	/**
	 * @param childName
	 * @param ct
	 * @param minOccurs
	 * @param maxOccurs
	 */
	public ComplexTypeChild(final String childName, final ComplexType ct,
			final BigInteger minOccurs, final BigInteger maxOccurs,
			final XmlAnySimpleType defaultValue) {
		this.childName = childName;
		this.javaChildName = Util.getXjcPropertyName(childName);
		this.ct = ct;
		this.minOccurs = minOccurs;
		this.optional = minOccurs == null || minOccurs.intValue() == 0;
		this.maxOccurs = maxOccurs;
		this.list = maxOccurs == null || maxOccurs.intValue() > 1;
		this.defaultValue = defaultValue;
		this.getterName = Util.getXjcGetterName(childName);
		if (this.isJavaPrimitive() && ct.getClassName().equals("boolean")) {
			this.getterName = new StringBuffer(this.getterName.length())
					.append("is").append(this.getterName.substring(3,
							this.getterName.length()))
					.toString();
		}
		this.setterName = Util.getXjcSetterName(childName);
	}

	public String getJavaImportClass() {
		return this.getComplexType().getClassNameFullQualified();
	}

	public String getJavaPackage() {
		return this.getComplexType().getPackageName();
	}

	public boolean isJavaPrimitive() {
		return this.getComplexType().isJavaPrimitive();
	}

	public boolean isJavaArray() {
		return this.getComplexType().isJavaArray();
	}

	public String getJavaDefaultValue() {
		String s = "null";
		if (this.defaultValue != null) {
			s = this.defaultValue.getStringValue();
		} else if (this.isJavaPrimitive()) {
			if (this.ct.getClassName().equals("boolean")) {
				s = "false";
			} else {
				s = "0";
			}
		}
		return s;
	}

	public String getJavaVarDefinition() {
		StringBuffer sb = new StringBuffer(32);
		if (this.isList()) {
			sb.append("List<");
		}
		sb.append(this.getComplexType().getClassName());
		if (this.isList()) {
			sb.append(">");
		}

		sb.append(" ");
		sb.append(this.javaChildName);
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.childName);
		sb.append(" ");
		sb.append(this.ct.getType().getName().getLocalPart());
		sb.append(" ");
		sb.append(this.minOccurs);
		sb.append("/");
		sb.append(this.maxOccurs);
		sb.append(" ");
		sb.append(this.isOptional());
		sb.append("/");
		sb.append(this.isList());
		return sb.toString();
	}

	/**
	 * @return the ct
	 */
	public ComplexType getComplexType() {
		return this.ct;
	}

	/**
	 * @return the childName
	 */
	public String getChildName() {
		return this.childName;
	}

	/**
	 * @return the isOptional
	 */
	public boolean isOptional() {
		return this.optional;
	}

	/**
	 * @return the isList
	 */
	public boolean isList() {
		return this.list;
	}

	/**
	 * @return the minOccurs
	 */
	public BigInteger getMinOccurs() {
		return this.minOccurs;
	}

	/**
	 * @return the maxOccurs
	 */
	public BigInteger getMaxOccurs() {
		return this.maxOccurs;
	}

	/**
	 * @return the defaultValue
	 */
	public XmlAnySimpleType getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @return the getterName
	 */
	public String getGetterName() {
		return this.getterName;
	}

	/**
	 * @return the setterName
	 */
	public String getSetterName() {
		return this.setterName;
	}

	/**
	 * @return the javaChildName
	 */
	public String getJavaChildName() {
		return this.javaChildName;
	}
}
