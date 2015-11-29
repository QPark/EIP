/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.NodeList;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class ComplexType {

	private List<ComplexTypeChild> children;
	private List<ComplexType> innerTypeDefs;
	private final String className;
	private final String classNameFq;
	private final String packageName;
	private final ComplexType parent;
	private final SchemaType type;
	private boolean requestType;
	private boolean responseType;
	private boolean flowInputType;
	private boolean flowOutputType;
	private final String annotationDocumentation;

	/**
	 * @return <code>true</code>, if the &lt; <code>complexType<code>&gt; is
	 *         created as an inner &lt;<code>complexType<code>&gt;.
	 */
	public boolean isInnerTypeDefinition() {
		return this.parent != null;
	}

	private boolean javaPrimitive = false;

	/**
	 * @param type
	 * @param packageName
	 */
	public ComplexType(final SchemaType type, final ComplexType parent,
			final XsdsUtil config) {
		this.type = type;
		this.parent = parent;
		if (parent == null && XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI
				.equals(type.getName().getNamespaceURI())) {
			Class<?> c = XsdsUtil.getBuildInJavaClass(type.getName());
			if (c.isPrimitive()) {
				this.javaPrimitive = true;
			}
			if (c.getPackage() != null) {
				this.packageName = c.getPackage().getName();
			} else {
				this.packageName = "";
			}
			this.className = c.getSimpleName();
			this.classNameFq = c.getName();
		} else if (this.type.isSimpleType()
				&& (this.type.getEnumerationValues() == null
						|| this.type.getEnumerationValues().length == 0)) {
			SchemaType buildInBase = XsdsUtil.getBuildInBaseType(this.type);
			if (buildInBase.isSimpleType()) {
				buildInBase = XsdsUtil.getBuildInBaseType(buildInBase);
			}
			Class<?> c = XsdsUtil.getBuildInJavaClass(buildInBase.getName());
			if (c.isPrimitive()) {
				this.javaPrimitive = true;
			}
			if (c.getPackage() != null) {
				this.packageName = c.getPackage().getName();
			} else {
				this.packageName = "";
			}
			this.className = c.getSimpleName();
			this.classNameFq = c.getName();
		} else {
			if (parent == null) {
				String pn = config.getPackageName(this.type.getName());
				this.packageName = pn == null || pn.trim().length() == 0 ? ""
						: pn;
			} else {
				this.packageName = parent.getPackageName();
			}
			if (!this.type.isSimpleType() && !this.type.isPrimitiveType()) {
				config.getPackageNames().add(this.packageName);
			}
			if (type.getFullJavaName() == null) {
				if (parent == null) {
					this.className = Util
							.getXjcClassName(type.getName().getLocalPart());
					this.classNameFq = new StringBuffer(128)
							.append(this.packageName)
							.append(this.packageName == null
									|| this.packageName.trim().length() == 0
											? "" : ".")
							.append(this.className).toString();
				} else {
					String name = Util.getXjcClassName(
							type.getContainerField().getName().getLocalPart());
					this.className = new StringBuffer(128)
							.append(parent.getClassName()).append(".")
							.append(Util.capitalize(name)).toString();
					this.classNameFq = new StringBuffer(128)
							.append(this.packageName)
							.append(this.packageName == null
									|| this.packageName.trim().length() == 0
											? "" : ".")
							.append(this.className).toString();
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
			if (this.parent != null) {
				this.parent.getInnerTypeDefs().add(this);
			}
			if (this.className
					.lastIndexOf(config.getServiceRequestSuffix()) > 0) {
				this.requestType = true;
			} else {
				this.requestType = false;
			}
			if (this.className
					.lastIndexOf(config.getServiceResponseSuffix()) > 0) {
				this.responseType = true;
			} else {
				this.responseType = false;
			}
			for (SchemaProperty o : type.getElementProperties()) {
				if (!o.getType().isSimpleType()
						&& !o.getType().isPrimitiveType()
						&& o.getType().getName() == null
						&& o.getType().getContainerField() != null) {
					new ComplexType(o.getType(), this, config);
				}
			}
		}
		if (this.type.getAnnotation() != null
				&& this.type.getAnnotation().getUserInformation() != null
				&& this.type.getAnnotation().getUserInformation().length > 0) {
			StringBuffer sb = new StringBuffer(124);
			for (XmlObject u : this.type.getAnnotation().getUserInformation()) {
				if (u.getDomNode() != null) {
					NodeList nl = u.getDomNode().getChildNodes();
					for (int i = 0; i < nl.getLength(); i++) {
						if (i > 0 && sb.length() > 0) {
							sb.append(" ");
						}
						sb.append(nl.item(i).getNodeValue());
					}
				}
			}
			if (sb.length() > 0) {
				this.annotationDocumentation = sb.toString().trim();
			} else {
				this.annotationDocumentation = null;
			}
		} else {
			this.annotationDocumentation = null;
		}

		if (this.type.getBaseType() != null
				&& this.type.getBaseType().getName() != null
				&& this.type.getBaseType().getName().getNamespaceURI()
						.endsWith("/Interfaces/Flow")
				&& this.type.getBaseType().getName().getLocalPart()
						.equals("FlowInputType")) {
			this.flowInputType = true;
		} else {
			this.flowInputType = false;
		}
		if (type.getBaseType() != null && type.getBaseType().getName() != null
				&& type.getBaseType().getName().getNamespaceURI()
						.endsWith("/Interfaces/Flow")
				&& type.getBaseType().getName().getLocalPart()
						.equals("FlowOutputType")) {
			this.flowOutputType = true;
		} else {
			this.flowOutputType = false;
		}
	}

	/**
	 * Setup the list of children {@link Entry}s.
	 *
	 * @param config
	 */
	public void initChildren(final XsdsUtil config) {
		ComplexTypeChild child = null;
		QName childType = null;
		String childName;
		for (SchemaProperty o : this.getType().getElementProperties()) {
			if (o.getName() != null && o.getType() != null&& o.getType().getName() != null) {
				childType = o.getType().getName();
				childName = o.getName().getLocalPart();
				if (childName != null && !this.containsChildName(childName)) {
					if (childType.getNamespaceURI()
							.equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)) {
						child = new ComplexTypeChild(childName,
								new ComplexType(o.getType(), config),
								o.getMinOccurs(), o.getMaxOccurs(),
								o.getDefaultValue());
						this.getChildren().add(child);
					} else {
						ComplexType c = config.getComplexType(childType);
						if (c != null) {
							child = new ComplexTypeChild(childName, c,
									o.getMinOccurs(), o.getMaxOccurs(),
									o.getDefaultValue());
							this.getChildren().add(child);
						}
					}
				}
			}
		}
	}

	public Set<String> getJavaImportClasses() {
		TreeSet<String> ts = new TreeSet<String>(new JavaImportComparator());
		if (!this.isJavaPrimitive()
				&& !this.getPackageName().startsWith("org.apache.xmlbeans")) {
			ts.add(this.getClassNameFullQualified());
		}
		for (ComplexTypeChild child : this.getChildren()) {
			if (!child.getJavaPackage().startsWith("java.lang")
					&& !child.getJavaPackage().startsWith("org.apache.xmlbeans")
					&& !child.isJavaPrimitive()) {
				ts.add(child.getJavaImportClass());
			}
			if (child.isList()) {
				ts.add("java.util.List");
			}
		}
		return ts;
	}

	private boolean containsChildName(final String childName) {
		boolean contains = false;
		for (ComplexTypeChild child : this.getChildren()) {
			if (child.getChildName().equals(childName)) {
				contains = true;
				break;
			}
		}
		return contains;
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
	public List<ComplexTypeChild> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<ComplexTypeChild>();
		}
		return this.children;
	}

	/**
	 * Get the {@link ComplexTypeChild} with the give property name.
	 *
	 * @param propertyName
	 *            the property name.
	 * @return the {@link ComplexTypeChild} found or <code>null</code>.
	 */
	public ComplexTypeChild getChild(final String propertyName) {
		ComplexTypeChild c = null;
		if (propertyName != null) {
			for (ComplexTypeChild child : this.getChildren()) {
				if (child.getChildName().equals(propertyName)) {
					c = child;
					break;
				}
			}
		}
		return c;
	}

	/**
	 * @return the innerTypeDefs
	 */
	public List<ComplexType> getInnerTypeDefs() {
		if (this.innerTypeDefs == null) {
			this.innerTypeDefs = new ArrayList<ComplexType>();
		}
		return this.innerTypeDefs;
	}

	/**
	 * @return the simple javaClassName
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * @return the parent {@link ComplexType}.
	 */
	public ComplexType getParent() {
		return this.parent;
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

	/**
	 * @return the requestType
	 */
	public boolean isRequestType() {
		return this.requestType;
	}

	/**
	 * @return the responseType
	 */
	public boolean isResponseType() {
		return this.responseType;
	}

	/**
	 * @return
	 */
	public String getTargetNamespace() {
		String targetNamespace = "";
		if (this.type.getName() != null) {
			targetNamespace = this.type.getName().getNamespaceURI();
		}
		if (targetNamespace == null
				|| targetNamespace.length() == 0 && this.parent != null) {
			targetNamespace = this.parent.getTargetNamespace();
		}
		return targetNamespace;
	}

	/**
	 * @return the flowInputType
	 */
	public boolean isFlowInputType() {
		return this.flowInputType;
	}

	/**
	 * @return the flowOutputType
	 */
	public boolean isFlowOutputType() {
		return this.flowOutputType;
	}

	/**
	 * @return the annotationDocumentation
	 */
	public String getAnnotationDocumentation() {
		return this.annotationDocumentation;
	}

	/**
	 * @return the javaPrimitive
	 */
	public boolean isJavaPrimitive() {
		return this.javaPrimitive;
	}
}