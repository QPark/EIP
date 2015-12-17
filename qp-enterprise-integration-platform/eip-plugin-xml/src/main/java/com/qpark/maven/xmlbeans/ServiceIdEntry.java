/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.util.Set;
import java.util.TreeSet;

public class ServiceIdEntry implements Comparable<ServiceIdEntry> {
	private final String serviceId;
	private final String packageName;
	private final String targetNamespace;
	private String annotationDocumentation;

	public String getAnnotationDocumentation() {
		return this.annotationDocumentation;
	}

	void setAnnotationDocumentation(final String annotationDocumentation) {
		this.annotationDocumentation = annotationDocumentation;
	}

	private final Set<ServiceIdEntry> importedServiceEntries = new TreeSet<ServiceIdEntry>();
	private Set<String> totalImportedServiceIds = null;

	ServiceIdEntry(final String serviceId, final String packageName,
			final String targetNamespace) {
		this.serviceId = serviceId;
		this.packageName = packageName;
		this.targetNamespace = targetNamespace;
		if (this.serviceId == null || this.packageName == null
				|| this.targetNamespace == null) {
			throw new IllegalStateException(new StringBuffer(256)
					.append("Min one of the mandatory elements serviceId=\"")
					.append(this.serviceId).append("\" packageName=\"")
					.append(this.packageName).append("\" or targetNamespace=\"")
					.append(this.targetNamespace).append("\" is null!")
					.toString());
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(128);
		sb.append("serviceId=\"");
		sb.append(this.serviceId);
		sb.append("\" packageName=\"");
		sb.append(this.packageName).append("\" or targetNamespace=\"");
		sb.append(this.targetNamespace);
		sb.append("\"");
		return sb.toString();
	}

	private static void getTotalImportedServiceIds(final Set<String> imported,
			final ServiceIdEntry entry) {
		for (ServiceIdEntry child : entry.getImportedServiceEntries()) {
			imported.add(child.getServiceId());
			getTotalImportedServiceIds(imported, child);
		}
	}

	public Set<String> getTotalServiceIdImports() {
		if (this.totalImportedServiceIds == null) {
			this.totalImportedServiceIds = new TreeSet<String>();
			getTotalImportedServiceIds(this.totalImportedServiceIds, this);
		}
		return this.totalImportedServiceIds;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return this.serviceId;
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
	 * @return the importedServiceIds
	 */
	public Set<ServiceIdEntry> getImportedServiceEntries() {
		return this.importedServiceEntries;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final ServiceIdEntry o) {
		if (o == this) {
			return 0;
		}
		if (o == null) {
			return -1;
		}
		int value = this.packageName.compareTo(o.packageName);
		if (value == 0) {
			value = this.targetNamespace.compareTo(o.targetNamespace);
		}
		return value;
	}
}