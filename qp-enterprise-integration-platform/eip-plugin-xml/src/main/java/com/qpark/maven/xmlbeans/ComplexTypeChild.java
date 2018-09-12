/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.math.BigInteger;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class ComplexTypeChild {
	private String annotationDocumentation;
	private String annotationAppInfo;
	private final String childName;
	private final ComplexType ct;
	private final XmlAnySimpleType defaultValue;
	private String getterName;
	private final String javaChildName;
	private final boolean list;
	private final BigInteger maxOccurs;
	private final BigInteger minOccurs;
	private final boolean optional;
	private final String setterName;

	/**
	 * @param childName
	 * @param ct
	 * @param minOccurs
	 * @param maxOccurs
	 * @param defaultValue
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

	/**
	 * @param childName
	 * @param childType
	 * @param ct
	 * @param minOccurs
	 * @param maxOccurs
	 * @param defaultValue
	 */
	public ComplexTypeChild(final String childName, final SchemaType childType,
			final ComplexType ct, final BigInteger minOccurs,
			final BigInteger maxOccurs, final XmlAnySimpleType defaultValue) {
		this.childName = childName;
		this.javaChildName = Util.getXjcPropertyName(this.childName);
		this.ct = ct;
		this.minOccurs = minOccurs;
		this.optional = minOccurs == null || minOccurs.intValue() == 0;
		this.maxOccurs = maxOccurs;
		this.list = maxOccurs == null || maxOccurs.intValue() > 1;
		this.defaultValue = defaultValue;
		this.getterName = Util.getXjcGetterName(this.childName);
		if (this.isJavaPrimitive() && ct.getClassName().equals("boolean")) {
			this.getterName = new StringBuffer(this.getterName.length())
					.append("is").append(this.getterName.substring(3,
							this.getterName.length()))
					.toString();
		}
		this.setterName = Util.getXjcSetterName(this.childName);
	}

	/**
	 * @return the annotationDocumentation - never <code>null</code>.
	 */
	public String getAnnotationDocumentation() {
		return this.annotationDocumentation == null ? ""
				: this.annotationDocumentation;
	}

	/**
	 * @return the annotationAppInfo - never <code>null</code>.
	 */
	public String getAnnotationAppInfo() {
		return this.annotationAppInfo == null ? "" : this.annotationAppInfo;
	}

	/**
	 * @return the annotationDocumentation
	 */
	public String getAnnotationDocumentationNormalised() {
		return this.getAnnotationDocumentation().replaceAll("\\n", " ")
				.replaceAll("\\r", " ").replaceAll("(\\t)+", " ")
				.replaceAll("( )+", " ");
	}

	/**
	 * Get cardinality string e.g. <i>[0..*]</i> or <i>[1..1]</i>.
	 *
	 * @return the cardinality as string.
	 */
	public String getCardinality() {
		StringBuffer sb = new StringBuffer(8);
		sb.append("[");
		sb.append(this.minOccurs);
		sb.append("..");
		sb.append(this.maxOccurs == null ? "*" : this.maxOccurs);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @return the childName
	 */
	public String getChildName() {
		return this.childName;
	}

	/**
	 * @return the {@link ComplexType}.
	 */
	public ComplexType getComplexType() {
		return this.ct;
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
	 * @return the javaChildName
	 */
	public String getJavaChildName() {
		return this.javaChildName;
	}

	/**
	 * @return the default value defined in the XSDs. If nothing specified the
	 *         value is the {@link String} <i>"null"</i>.
	 */
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

	/**
	 * @return the java class name.
	 */
	public String getJavaImportClass() {
		return this.getComplexType().getClassNameFullQualified();
	}

	/**
	 * @return the java package name.
	 */
	public String getJavaPackage() {
		return this.getComplexType().getPackageName();
	}

	/**
	 * @return the java variable definition (after the <i>=</i> sign).
	 */
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
	 * @return the java variable definition (after the <i>=</i> sign) with the
	 *         {@link ComplexType} full qualified class name.
	 */
	public String getJavaVarDefinitionFullQualified() {
		StringBuffer sb = new StringBuffer(32);
		if (this.isList()) {
			sb.append("List<");
		}
		sb.append(this.getComplexType().getClassNameFullQualified());
		if (this.isList()) {
			sb.append(">");
		}

		sb.append(" ");
		sb.append(this.javaChildName);
		return sb.toString();
	}

	/**
	 * @return the maxOccurs
	 */
	public BigInteger getMaxOccurs() {
		return this.maxOccurs;
	}

	/**
	 * @return the minOccurs
	 */
	public BigInteger getMinOccurs() {
		return this.minOccurs;
	}

	/**
	 * @return the setterName
	 */
	public String getSetterName() {
		return this.setterName;
	}

	/**
	 * @return is java array.
	 */
	public boolean isJavaArray() {
		return this.getComplexType().isJavaArray();
	}

	/**
	 * @return is java primitive
	 */
	public boolean isJavaPrimitive() {
		return this.getComplexType().isJavaPrimitive();
	}

	/**
	 * @return is list
	 */
	public boolean isList() {
		return this.list;
	}

	/**
	 * @return is optional
	 */
	public boolean isOptional() {
		return this.optional;
	}

	/**
	 * Set the annotationDocumentation.
	 *
	 * @param annotationDocumentation
	 */
	public void setAnnotationDocumentation(
			final String annotationDocumentation) {
		this.annotationDocumentation = annotationDocumentation;
	}

	/**
	 * Set the annotationAppInfo.
	 *
	 * @param annotationAppInfo
	 */
	public void setAnnotationAppInfo(final String annotationAppInfo) {
		this.annotationAppInfo = annotationAppInfo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.getChildName());
		sb.append(this.getCardinality());
		sb.append(this.getComplexType().toQNameString());
		return sb.toString();
	}
}
