/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

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
			final SchemaProperty defaultProperty = schemaType
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

	private static void setServiceRequestResponseDetection(
			final XsdsUtil config, final ComplexType ct) {
		if (ct.getClassName() != null) {
			final int indexRequest = ct.getClassName()
					.lastIndexOf(config.getServiceRequestSuffix());
			final int indexResponse = ct.getClassName()
					.lastIndexOf(config.getServiceResponseSuffix());
			if (indexRequest > 0 && indexResponse > 0) {
				if (indexRequest > indexResponse) {
					ct.requestType = true;
				} else {
					ct.responseType = true;
				}
			} else if (indexRequest > 0) {
				ct.requestType = true;
			} else if (indexResponse > 0) {
				ct.responseType = true;
			}
		}
	}

	private final String annotationDocumentation;
	private ComplexType baseComplexType;
	private List<ComplexTypeChild> children;
	private final String className;
	private final String classNameFq;
	private final boolean complexMappingType;
	private final boolean complexUUIDMappingType;
	private final boolean defaultMappingType;
	private final boolean directMappingType;
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
	 * @param parent
	 * @param config
	 */
	public ComplexType(final SchemaType type, final ComplexType parent,
			final XsdsUtil config) {
		this.type = type;
		this.parent = parent;
		if (parent == null && XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI
				.equals(type.getName().getNamespaceURI())) {
			final Class<?> c = XsdsUtil.getBuildInJavaClass(type.getName());
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
			final Class<?> c = XsdsUtil
					.getBuildInJavaClass(buildInBase.getName());
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
			if (Objects.isNull(parent)) {
				final String pn = config.getPackageName(this.type.getName());
				this.packageName = Objects.isNull(pn) ? "" : pn.trim();
			} else {
				this.packageName = Objects.isNull(parent.getPackageName()) ? ""
						: parent.getPackageName();
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
					final String name = Util.getXjcClassName(
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
				final int index = this.classNameFq.lastIndexOf('.');
				if (index > 0) {
					this.className = this.classNameFq.substring(index + 1);
				} else {
					this.className = this.classNameFq;
				}
			}
			if (this.parent != null) {
				this.parent.getInnerTypeDefs().add(this);
			}
			setServiceRequestResponseDetection(config, this);

			for (final SchemaProperty o : type.getElementProperties()) {
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
			final StringBuffer sb = new StringBuffer(124);
			for (final XmlObject u : this.type.getAnnotation()
					.getUserInformation()) {
				if (u.getDomNode() != null) {
					final NodeList nl = u.getDomNode().getChildNodes();
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
	 * @param config
	 */
	public ComplexType(final SchemaType type, final XsdsUtil config) {
		this(type, null, config);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final ComplexType o) {
		int value = this.getTargetNamespace().compareTo(o.getTargetNamespace());
		if (value == 0) {
			value = this.getClassName().compareTo(o.getClassName());
		}
		return value;
	}

	/**
	 * Checks, if one of the children has the name.
	 *
	 * @param childName
	 *            the name to check.
	 * @return <code>true</code>, if one of the children has the name, else
	 *         <code>false</code>.
	 */
	private boolean containsChildName(final String childName) {
		boolean contains = false;
		for (final ComplexTypeChild child : this.getChildren()) {
			if (child.getChildName().equals(childName)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	/**
	 * @return the annotationDocumentation (never <code>null</code>).
	 */
	public String getAnnotationDocumentation() {
		return this.annotationDocumentation == null ? ""
				: this.annotationDocumentation;
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
	 * @return the base {@link ComplexType} - the ancestor - if any.
	 */
	public ComplexType getBaseComplexType() {
		return this.baseComplexType;
	}

	public int getHierarchyLevel() {
		AtomicInteger ai = new AtomicInteger(0);
		getHierarchyLevel(this, ai);
		return ai.get();
	}

	private static void getHierarchyLevel(final ComplexType ct,
			final AtomicInteger ai) {
		if (Objects.nonNull(ct)) {
			ai.incrementAndGet();
			getHierarchyLevel(ct.getBaseComplexType(), ai);
		}
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
			for (final ComplexTypeChild child : this.getChildren()) {
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
	 * @return a map of children name with the {@link ComplexTypeChild}s.
	 */
	public Map<String, ComplexTypeChild> getChildrenMap() {
		final Map<String, ComplexTypeChild> value = new HashMap<>();
		this.getChildren().stream()
				.forEach(ctc -> value.put(ctc.getChildName(), ctc));
		return value;
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
	 * Get the default value if {@link #isDefaultMappingType()}.
	 *
	 * @return the default value or <code>null</code>.
	 */
	public String getDefaultValue() {
		String defaultValue = null;
		if (this.isDefaultMappingType()) {
			final SchemaProperty defaultProperty = this.getType()
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
	 * @return the innerTypeDefs
	 */
	public List<ComplexType> getInnerTypeDefs() {
		if (this.innerTypeDefs == null) {
			this.innerTypeDefs = new ArrayList<ComplexType>();
		}
		return this.innerTypeDefs;
	}

	/**
	 * @return the set containing the names the JAX impl need as import to be
	 *         work with.
	 */
	public Set<String> getJavaImportClasses() {
		final TreeSet<String> ts = new TreeSet<String>(
				new JavaImportComparator());
		if (!this.isJavaPrimitive() && !this.isJavaArray()
				&& !this.getPackageName().startsWith("org.apache.xmlbeans")) {
			ts.add(this.getClassNameFullQualified());
		}
		for (final ComplexTypeChild child : this.getChildren()) {
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
	 * @return the name of the type without the target name space.
	 */
	public String getQNameLocalPart() {
		String localpart = "";
		if (Objects.nonNull(this.type.getName())
				&& Objects.nonNull(this.type.getName().getLocalPart())) {
			localpart = this.type.getName().getLocalPart();
		}
		return localpart;
	}

	/**
	 * @return the target name space.
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
	 * @return the {@link SchemaType}.
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
		for (final SchemaProperty o : this.getType().getElementProperties()) {
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
						final ComplexType c = config.getComplexType(childType);
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

	void initDescent(final XsdsUtil config) {
		final SchemaType st = this.type.getBaseType();
		if (Objects.nonNull(st) && Objects.nonNull(st.getName())) {
			this.baseComplexType = config.getComplexType(st.getName());
			if (Objects.nonNull(this.baseComplexType) && this.baseComplexType
					.toQNameString().equals(this.toQNameString())) {
				this.baseComplexType = null;
			}
		}
		if (Objects.isNull(this.baseComplexType) && this.type.isSimpleType()
				&& (this.type.getEnumerationValues() == null
						|| this.type.getEnumerationValues().length == 0)) {
			final SchemaType buildInBase = XsdsUtil
					.getBuildInBaseType(this.type);
			if (Objects.nonNull(buildInBase)
					&& Objects.nonNull(buildInBase.getName())) {
				this.baseComplexType = config
						.getComplexType(buildInBase.getName());
				if (Objects.nonNull(this.baseComplexType)
						&& this.baseComplexType.toQNameString()
								.equals(this.toQNameString())) {
					this.baseComplexType = null;
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

	/**
	 * @return is complex mapping type
	 */
	public boolean isComplexMappingType() {
		return this.complexMappingType;
	}

	/**
	 * @return is complex UUID mapping type
	 */
	public boolean isComplexUUIDMappingType() {
		return this.complexUUIDMappingType;
	}

	/**
	 * @return is default mapping type
	 */
	public boolean isDefaultMappingType() {
		return this.defaultMappingType;
	}

	/**
	 * @return is direct mapping type
	 */
	public boolean isDirectMappingType() {
		return this.directMappingType;
	}

	/**
	 * @return is XSD enum type
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

	/**
	 * @return is interface mapping type
	 */
	public boolean isInterfaceMappingType() {
		return this.interfaceMappingType;
	}

	/**
	 * @return is java array
	 */
	public boolean isJavaArray() {
		return this.javaArray;
	}

	/**
	 * @return is java primitive
	 */
	public boolean isJavaPrimitive() {
		return this.javaPrimitive;
	}

	/**
	 * @return is map request type
	 */
	public boolean isMapRequestType() {
		return this.mapRequestType;
	}

	/**
	 * @return is map response type
	 */
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
	 * @return is request type
	 */
	public boolean isRequestType() {
		return this.requestType;
	}

	/**
	 * @return is response type
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

	/**
	 * @return the {@link QName} string representation.
	 */
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
		final StringBuffer sb = new StringBuffer();
		sb.append(this.classNameFq).append("[").append(this.isAbstractType())
				.append("/").append(this.isEnumType()).append("/")
				.append(this.isSimpleType()).append("]");
		return sb.toString();
	}
}
