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
import java.util.List;

public class XsdContainer {
	private final File file;
	private final String packageName;
	private final String targetNamespace;

	private final List<String> importedTargetNamespaces;

	public List<String> getImportedTargetNamespaces() {
		return this.importedTargetNamespaces;
	}

	public XsdContainer(final File f, final String packageName,
			final String targetNamespace,
			final List<String> importedTargetNamespaces) {
		this.file = f;
		this.packageName = packageName;
		this.targetNamespace = targetNamespace;
		if (importedTargetNamespaces == null) {
			this.importedTargetNamespaces = new ArrayList<String>();
		} else {
			this.importedTargetNamespaces = importedTargetNamespaces;
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
}