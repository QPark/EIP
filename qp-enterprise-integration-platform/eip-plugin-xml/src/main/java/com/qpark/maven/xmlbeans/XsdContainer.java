/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

public class XsdContainer {
	private final String annotationDocumentation;
	private final List<ComplexType> complexType = new ArrayList<>();
	private final List<ElementType> elementType = new ArrayList<>();
	private final File file;

	private final Collection<String> importedTargetNamespaces;
	private final TreeMap<String, String> imports;
	private final String packageName;
	private final String relativeName;
	private final String targetNamespace;
	private final Collection<String> totalImportedTargetNamespaces = new TreeSet<String>();
	private final TreeSet<String> usedXmlnss;
	private final String version;

	private final TreeMap<String, String> xmlnss;

	XsdContainer(final File f, final File baseDirectory,
			final String packageName, final String targetNamespace,
			final String annotationDocumentation,
			final TreeMap<String, String> imports,
			final TreeMap<String, String> xmlnss,
			final TreeSet<String> usedXmlns) {
		this.file = f;
		if (f != null) {
			String s = f.getAbsolutePath()
					.replace(baseDirectory.getAbsolutePath(), "");
			if (s.length() > 0) {
				s = s.substring(1, s.length());
			}
			this.relativeName = s.replaceAll("\\\\", "/");
		} else {
			this.relativeName = null;
		}
		this.packageName = packageName;
		this.targetNamespace = targetNamespace;
		this.annotationDocumentation = annotationDocumentation;
		if (imports == null) {
			this.imports = new TreeMap<String, String>();
			this.importedTargetNamespaces = new ArrayList<String>();
		} else {
			this.imports = imports;
			this.importedTargetNamespaces = imports.keySet();
			this.importedTargetNamespaces.remove(this.targetNamespace);
		}
		if (xmlnss == null) {
			this.xmlnss = new TreeMap<String, String>();
		} else {
			this.xmlnss = xmlnss;
		}
		this.usedXmlnss = usedXmlns;
		if (targetNamespace.matches("^.*?-\\d\\.\\d$")) {
			this.version = targetNamespace.substring(
					targetNamespace.lastIndexOf('-') + 1,
					targetNamespace.length());
		} else {
			this.version = "1.0";
		}
	}

	/**
	 * @param ct
	 */
	public synchronized void addComplexType(final ComplexType ct) {
		if (Objects.nonNull(ct)) {
			if (!this.complexType.stream()
					.filter(ctx -> ctx.getType().getName().toString()
							.equals(ct.getType().getName().toString()))
					.findFirst().isPresent()) {
				this.complexType.add(ct);
			}
		}
	}

	/**
	 * @param et
	 */
	public synchronized void addElementType(final ElementType et) {
		if (Objects.nonNull(et) && !this.elementType.contains(et)) {
			this.elementType.add(et);
		}
	}

	public String getAnnotationDocumentation() {
		return this.annotationDocumentation;
	}

	/**
	 * @return the complexType
	 */
	public List<ComplexType> getComplexType() {
		return Collections.unmodifiableList(this.complexType);
	}

	/**
	 * @return the domain path name.
	 */
	public String getDomainPathName() {
		return this.relativeName.substring(0,
				this.relativeName.lastIndexOf('/'));
	}

	/**
	 * @return the elementType
	 */
	public List<ElementType> getElementType() {
		return Collections.unmodifiableList(this.elementType);
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

	public String getImportedSchemaLocation(final String targetNamespace) {
		return this.imports.get(targetNamespace);
	}

	public Collection<String> getImportedTargetNamespaces() {
		return this.importedTargetNamespaces;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the relativeName
	 */
	public String getRelativeName() {
		return this.relativeName;
	}

	/**
	 * @return the targetNamespace
	 */
	public String getTargetNamespace() {
		return this.targetNamespace;
	}

	public Collection<String> getTotalImportedTargetNamespaces() {
		return this.totalImportedTargetNamespaces;
	}

	/**
	 * Get the version. If no version is specified 1.0 is assumed.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	public List<String> getWarnings() {
		List<String> list = new ArrayList<String>();
		String namespace;
		for (String xmlnsNamespaceToken : this.xmlnss.keySet()) {
			namespace = this.xmlnss.get(xmlnsNamespaceToken);
			if (namespace == null) {
				list.add(new StringBuffer(128).append("Token xmlns:")
						.append(xmlnsNamespaceToken)
						.append(" defined but no namespace assigned!")
						.toString());
			} else if (!namespace.equals(this.targetNamespace)
					&& !namespace.equals("http://java.sun.com/xml/ns/jaxb")) {
				if (!this.usedXmlnss.contains(xmlnsNamespaceToken)) {
					list.add(new StringBuffer(128).append("Token xmlns:")
							.append(xmlnsNamespaceToken).append("=\"")
							.append(namespace)
							.append("\" defined but not used!").toString());
				}
				if (!this.importedTargetNamespaces.contains(namespace)) {
					list.add(new StringBuffer(128).append("Token xmlns:")
							.append(xmlnsNamespaceToken).append("=\"")
							.append(namespace).append("\" not imported!")
							.toString());
				}
			}
		}
		for (String importedTargetNamespace : this.importedTargetNamespaces) {
			if (!this.xmlnss.values().contains(importedTargetNamespace)) {
				list.add(new StringBuffer(128).append("Imported namespace ")
						.append(importedTargetNamespace)
						.append(" not defined as xmlns!").toString());
			}
		}
		return list;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{").append(this.targetNamespace).append("}")
				.append(this.packageName);
		return sb.toString();
	}
}