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
package com.qpark.maven.xmlbeans;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class ComplexType {
	private List<ComplexType> children;
	private final String className;
	private final String classNameFq;
	private final String packageName;
	private final ComplexType parent;

	private final SchemaType type;

	/**
	 * @param type
	 * @param packageName
	 */
	public ComplexType(final SchemaType type, final ComplexType parent,
			final XsdsUtil config) {
		this.type = type;
		this.parent = parent;
		if (parent == null) {
			String pn = config.getPackageName(this.type.getName());
			this.packageName = pn == null || pn.trim().length() == 0 ? "" : pn;
		} else {
			this.packageName = parent.getPackageName();
		}
		if (!this.type.isSimpleType() && !this.type.isPrimitiveType()) {
			config.getPackageNames().add(this.packageName);
		}
		if (type.getFullJavaName() == null) {
			if (parent == null) {
				this.className = type.getName().getLocalPart();
				this.classNameFq = new StringBuffer(128)
						.append(this.packageName)
						.append(this.packageName == null
								|| this.packageName.trim().length() == 0 ? ""
								: ".").append(this.className).toString();
			} else {
				String name = type.getContainerField().getName().getLocalPart();
				this.className = new StringBuffer(128)
						.append(parent.getClassName()).append(".")
						.append(Util.capitalize(name)).toString();
				this.classNameFq = new StringBuffer(128)
						.append(this.packageName)
						.append(this.packageName == null
								|| this.packageName.trim().length() == 0 ? ""
								: ".").append(this.className).toString();
			}
		} else {
			this.classNameFq = this.type.getFullJavaName();
			int index = this.classNameFq.lastIndexOf('.');
			if (index > 0) {
				this.className = this.classNameFq.substring(index + 1);
			} else {
				this.className = this.classNameFq;
			}
		}
		if (parent != null) {
			this.parent.getChildren().add(this);
		}
		for (SchemaProperty o : type.getElementProperties()) {
			if (!o.getType().isSimpleType() && !o.getType().isPrimitiveType()
					&& o.getType().getName() == null
					&& o.getType().getContainerField() != null) {
				new ComplexType(o.getType(), this, config);
			}
		}
	}

	/**
	 * @param type
	 * @param packageName
	 */
	public ComplexType(final SchemaType type, final XsdsUtil config) {
		this(type, null, config);
	}

	/**
	 * @return the children
	 */
	public List<ComplexType> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<ComplexType>();
		}
		return this.children;
	}

	/**
	 * @return the simple javaClassName
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * @return the javaClassName
	 */
	public String getClassNameFullQualified() {
		return this.classNameFq;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the type
	 */
	public SchemaType getType() {
		return this.type;
	}

	/**
	 * @return is abstract type
	 */
	public boolean isAbstractType() {
		return this.type.isAbstract();
	}

	/**
	 * @return is enum type
	 */
	public boolean isEnumType() {
		return this.type.hasStringEnumValues();
	}

	/**
	 * @return is primitive type
	 */
	public boolean isPrimitiveType() {
		return this.type.isPrimitiveType();
	}

	/**
	 * @return is simple type
	 */
	public boolean isSimpleType() {
		return this.type.isSimpleType();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.classNameFq).append("[").append(this.isAbstractType())
				.append("/").append(this.isEnumType()).append("/")
				.append(this.isSimpleType()).append("]");
		return sb.toString();
	}
}