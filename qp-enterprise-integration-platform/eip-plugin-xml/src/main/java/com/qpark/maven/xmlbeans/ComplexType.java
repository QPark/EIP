/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
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
public class ComplexType implements Comparable<ComplexType> {

	private static boolean isComplexMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}ComplexMappingType");
	}

	private static boolean isComplexUUIDMappingType(
			final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}ComplexUUIDMappingType");
	}

	private static boolean isDefaultMappingType(final SchemaType schemaType) {
		boolean validType = false;
		if (schemaType != null && schemaType.getName() != null
				&& schemaType.getName().getLocalPart().toLowerCase()
						.contains("default")
				&& schemaType.getElementProperties() != null
				&& schemaType.getElementProperties().length == 1) {
			SchemaProperty defaultProperty = schemaType
					.getElementProperties()[0];
			if (defaultProperty.getType().isSimpleType()
					&& defaultProperty.getType().getEnumerationValues() != null
					&& defaultProperty.getType()
							.getEnumerationValues().length == 1) {
				validType = true;
			} else if (defaultProperty.getType().isSimpleType()
					&& defaultProperty.getDefaultText() != null) {
				validType = true;
			}
		}
		return validType;
	}

	private static boolean isDirectMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}DirectMappingType");
	}

	private static boolean isInstanceOf(final SchemaType schemaType,
			final String qName) {
		boolean validType = false;
		if (schemaType != null && schemaType.getBaseType() != null) {
			if (String.valueOf(schemaType.getBaseType().getName())
					.matches(qName.replace("{", "\\{").replace("}", "\\}"))) {
				validType = true;
			} else {
				validType = isInstanceOf(schemaType.getBaseType(), qName);
			}
		}
		return validType;
	}

	private static boolean isInterfaceType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}InterfaceType")
				|| isInstanceOf(schemaType,
						"{http://.*?/Interfaces/MappingTypes}InterfaceReferenceType");
	}

	private static boolean isMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}MappingType");
	}

	private static boolean isMapRequestType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/Mapping}MappingInputType");
	}

	private static boolean isMapResponseType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/Mapping}MappingOutputType");
	}

	private final String annotationDocumentation;
	private ComplexType baseComplexType;
	private List<ComplexTypeChild> children;
	private final String className;
	private final String classNameFq;
	private final boolean complexMappingType;
	private final boolean complexUUIDMappingType;
	private final boolean directMappingType;
	private final boolean defaultMappingType;
	private boolean flowInputType;
	private boolean flowOutputType;
	private List<ComplexType> innerTypeDefs;
	private final boolean interfaceMappingType;
	private boolean javaArray = false;
	private boolean javaPrimitive = false;
	private final boolean mapRequestType;
	private final boolean mapResponseType;
	private final String packageName;
	private final ComplexType parent;
	private boolean requestType;
	private boolean responseType;
	private final SchemaType type;

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
			} else if (c.isArray()) {
				this.javaArray = true;
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
			} else if (c.isArray()) {
				this.javaArray = true;
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
			SchemaType st = type.getBaseType();
			if (st != null && st.getName() != null) {
				this.baseComplexType = config.getComplexType(st.getName());
				if (this.baseComplexType != null && this.baseComplexType
						.toQNameString().equals(this.toQNameString())) {
					this.baseComplexType = null;
				}
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
		this.directMappingType = isDirectMappingType(type);
		this.defaultMappingType = isDefaultMappingType(type);
		this.complexUUIDMappingType = isComplexUUIDMappingType(type);
		if (!this.directMappingType && !this.defaultMappingType
				&& !this.complexUUIDMappingType
				&& (isComplexMappingType(type) || isMappingType(type))) {
			this.complexMappingType = true;
		} else {
			this.complexMappingType = false;
		}
		this.interfaceMappingType = isInterfaceType(type);

		this.mapRequestType = isMapRequestType(type);
		this.mapResponseType = isMapResponseType(type);
	}

	/**
	 * @param type
	 * @param packageName
	 */
	public ComplexType(final SchemaType type, final XsdsUtil config) {
		this(type, null, config);
	}

	@Override
	public int compareTo(final ComplexType o) {
		int value = this.getTargetNamespace().compareTo(o.getTargetNamespace());
		if (value == 0) {
			value = this.getClassName().compareTo(o.getClassName());
		}
		return value;
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
	 * @return the annotationDocumentation
	 */
	public String getAnnotationDocumentationNormalised() {
		return this.getAnnotationDocumentation().replaceAll("\\n", " ")
				.replaceAll("\\r", " ").replaceAll("(\\t)+", " ")
				.replaceAll("( )+", " ");
	}

	/**
	 * @return the annotationDocumentation
	 */
	public String getAnnotationDocumentation() {
		return this.annotationDocumentation == null ? ""
				: this.annotationDocumentation;
	}

	public ComplexType getBaseComplexType() {
		return this.baseComplexType;
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
	 * @return the children
	 */
	public List<ComplexTypeChild> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<ComplexTypeChild>();
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
	 * @return the innerTypeDefs
	 */
	public List<ComplexType> getInnerTypeDefs() {
		if (this.innerTypeDefs == null) {
			this.innerTypeDefs = new ArrayList<ComplexType>();
		}
		return this.innerTypeDefs;
	}

	public Set<String> getJavaImportClasses() {
		TreeSet<String> ts = new TreeSet<String>(new JavaImportComparator());
		if (!this.isJavaPrimitive() && !this.isJavaArray()
				&& !this.getPackageName().startsWith("org.apache.xmlbeans")) {
			ts.add(this.getClassNameFullQualified());
		}
		for (ComplexTypeChild child : this.getChildren()) {
			if (!child.getJavaPackage().startsWith("java.lang")
					&& !child.getJavaPackage().startsWith("org.apache.xmlbeans")
					&& !child.isJavaPrimitive() && !child.isJavaArray()) {
				ts.add(child.getJavaImportClass());
			}
			if (child.isList()) {
				ts.add("java.util.List");
			}
		}
		return ts;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the parent {@link ComplexType}.
	 */
	public ComplexType getParent() {
		return this.parent;
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
	 * @return the type
	 */
	public SchemaType getType() {
		return this.type;
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
			if (o.getName() != null && o.getType() != null
					&& o.getType().getName() != null) {
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

	/**
	 * @return is abstract type
	 */
	public boolean isAbstractType() {
		return this.type.isAbstract();
	}

	public boolean isComplexMappingType() {
		return this.complexMappingType;
	}

	public boolean isComplexUUIDMappingType() {
		return this.complexUUIDMappingType;
	}

	public boolean isDirectMappingType() {
		return this.directMappingType;
	}

	public boolean isDefaultMappingType() {
		return this.defaultMappingType;
	}

	/**
	 * Get the default value if {@link #isDefaultMappingType()}.
	 *
	 * @return the default value or <code>null</code>.
	 */
	public String getDefaultValue() {
		String defaultValue = null;
		if (this.isDefaultMappingType()) {
			SchemaProperty defaultProperty = this.getType()
					.getElementProperties()[0];
			if (defaultProperty.getType().isSimpleType()
					&& defaultProperty.getType().getEnumerationValues() != null
					&& defaultProperty.getType()
							.getEnumerationValues().length == 1) {
				defaultValue = defaultProperty.getType()
						.getEnumerationValues()[0].getStringValue();
			} else {
				defaultValue = defaultProperty.getDefaultText();
			}
		}
		return defaultValue;
	}

	/**
	 * @return is enum type
	 */
	public boolean isEnumType() {
		return this.type.hasStringEnumValues();
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
	 * @return <code>true</code>, if the &lt; <code>complexType<code>&gt; is
	 *         created as an inner &lt;<code>complexType<code>&gt;.
	 */
	public boolean isInnerTypeDefinition() {
		return this.parent != null;
	}

	public boolean isInterfaceMappingType() {
		return this.interfaceMappingType;
	}

	/**
	 * @return the javaPrimitive
	 */
	public boolean isJavaArray() {
		return this.javaArray;
	}

	/**
	 * @return the javaPrimitive
	 */
	public boolean isJavaPrimitive() {
		return this.javaPrimitive;
	}

	public boolean isMapRequestType() {
		return this.mapRequestType;
	}

	public boolean isMapResponseType() {
		return this.mapResponseType;
	}

	/**
	 * @return is primitive type
	 */
	public boolean isPrimitiveType() {
		return this.type.isPrimitiveType();
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
	 * @return is simple type
	 */
	public boolean isSimpleType() {
		return this.type.isSimpleType();
	}

	public String toQNameString() {
		if (this.type.getName() == null) {
			return String.valueOf(this.type);
		} else {
			return this.type.getName().toString();
		}
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