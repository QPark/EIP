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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class XsdContainer {
	private final File file;
	private final String packageName;
	private final String targetNamespace;
	private final String relativeName;

	private final Collection<String> importedTargetNamespaces;
	private final Collection<String> totalImportedTargetNamespaces = new TreeSet<String>();

	public Collection<String> getImportedTargetNamespaces() {
		return this.importedTargetNamespaces;
	}

	public Collection<String> getTotalImportedTargetNamespaces() {
		return this.totalImportedTargetNamespaces;
	}

	XsdContainer(final File f, final File baseDirectory,
			final String packageName, final String targetNamespace,
			final List<String> importedTargetNamespaces) {
		this.file = f;
		if (f != null) {
			String s = f.getAbsolutePath().replace(
					baseDirectory.getAbsolutePath(), "");
			if (s.length() > 0) {
				s = s.substring(1, s.length());
			}
			this.relativeName = s.replaceAll("\\\\", "/");
		} else {
			this.relativeName = null;
		}
		this.packageName = packageName;
		this.targetNamespace = targetNamespace;
		if (importedTargetNamespaces == null) {
			this.importedTargetNamespaces = new ArrayList<String>();
		} else {
			this.importedTargetNamespaces = importedTargetNamespaces;
			this.importedTargetNamespaces.remove(this.targetNamespace);
		}
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