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
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class XsdContainer {
	private final File file;
	private final String packageName;
	private final String targetNamespace;
	private final String relativeName;

	private final TreeMap<String, String> imports;
	private final TreeMap<String, String> xmlnss;
	private final TreeSet<String> usedXmlnss;
	private final Collection<String> importedTargetNamespaces;
	private final Collection<String> totalImportedTargetNamespaces = new TreeSet<String>();
	private final String annotationDocumentation;

	public String getAnnotationDocumentation() {
		return this.annotationDocumentation;
	}

	public Collection<String> getImportedTargetNamespaces() {
		return this.importedTargetNamespaces;
	}

	public String getImportedSchemaLocation(final String targetNamespace) {
		return this.imports.get(targetNamespace);
	}

	public Collection<String> getTotalImportedTargetNamespaces() {
		return this.totalImportedTargetNamespaces;
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
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the targetNamespace
	 */
	public String getTargetNamespace() {
		return this.targetNamespace;
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

	/**
	 * @return the relativeName
	 */
	public String getRelativeName() {
		return this.relativeName;
	}
}