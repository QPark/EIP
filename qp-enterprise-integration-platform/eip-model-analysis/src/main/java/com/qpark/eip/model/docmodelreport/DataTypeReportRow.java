/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.model.docmodelreport;

import java.util.List;

/**
 * One row in the data type report.
 *
 * @author bhausen
 */
public class DataTypeReportRow {
	/** The name of the type. */
	private String name;
	/** The target name space of the type. */
	private String targetNamespace;
	/** The description of the type. */
	private String description;
	/** The list of ancestor types of the type. */
	private List<String> inheritedFrom;
	/** The name of the field of the type. */
	private String fieldName;
	/** The cardinality of the field of the type. */
	private String fieldCardinality;
	/** The description of the field of the type. */
	private String fieldDescription;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the targetNamespace
	 */
	public String getTargetNamespace() {
		return this.targetNamespace;
	}

	/**
	 * @param targetNamespace
	 *            the targetNamespace to set
	 */
	public void setTargetNamespace(final String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the inheritedFrom
	 */
	public List<String> getInheritedFrom() {
		return this.inheritedFrom;
	}

	/**
	 * @param inheritedFrom
	 *            the inheritedFrom to set
	 */
	public void setInheritedFrom(final List<String> inheritedFrom) {
		this.inheritedFrom = inheritedFrom;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldCardinality
	 */
	public String getFieldCardinality() {
		return this.fieldCardinality;
	}

	/**
	 * @param fieldCardinality
	 *            the fieldCardinality to set
	 */
	public void setFieldCardinality(final String fieldCardinality) {
		this.fieldCardinality = fieldCardinality;
	}

	/**
	 * @return the fieldDescription
	 */
	public String getFieldDescription() {
		return this.fieldDescription;
	}

	/**
	 * @param fieldDescription
	 *            the fieldDescription to set
	 */
	public void setFieldDescription(final String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}
}
