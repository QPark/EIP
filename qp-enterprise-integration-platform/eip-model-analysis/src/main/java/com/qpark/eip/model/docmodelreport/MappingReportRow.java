/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.model.docmodelreport;

import java.util.List;

/**
 * One row in the mapping report.
 *
 * @author bhausen
 */
public class MappingReportRow {
	/** The name of the flow. */
	private String flowName;
	/** The name of the interface type. */
	private String interfaceName;
	/** The name of the interface type field. */
	private String interfaceFieldName;
	/** The cardinality of the interface type field. */
	private String interfaceFieldCardinality;
	/** The description of the interface type field. */
	private String interfaceFieldDescription;
	/** The name of the mapping type. */
	private String mappingTypeName;
	/** The type of the mapping type. */
	private String mappingTypeType;
	/** The description of the mapping type. */
	private String mappingTypeDescription;
	/** The list of input types of the mapping type. */
	private List<String> mappingTypeInputTypes;

	/**
	 * @return the flowName
	 */
	public String getFlowName() {
		return this.flowName;
	}

	/**
	 * @param flowName
	 *            the flowName to set
	 */
	public void setFlowName(final String flowName) {
		this.flowName = flowName;
	}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return this.interfaceName;
	}

	/**
	 * @param interfaceName
	 *            the interfaceName to set
	 */
	public void setInterfaceName(final String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * @return the interfaceFieldName
	 */
	public String getInterfaceFieldName() {
		return this.interfaceFieldName;
	}

	/**
	 * @param interfaceFieldName
	 *            the interfaceFieldName to set
	 */
	public void setInterfaceFieldName(final String interfaceFieldName) {
		this.interfaceFieldName = interfaceFieldName;
	}

	/**
	 * @return the interfaceFieldCardinality
	 */
	public String getInterfaceFieldCardinality() {
		return this.interfaceFieldCardinality;
	}

	/**
	 * @param interfaceFieldCardinality
	 *            the interfaceFieldCardinality to set
	 */
	public void setInterfaceFieldCardinality(
			final String interfaceFieldCardinality) {
		this.interfaceFieldCardinality = interfaceFieldCardinality;
	}

	/**
	 * @return the interfaceFieldDescription
	 */
	public String getInterfaceFieldDescription() {
		return this.interfaceFieldDescription;
	}

	/**
	 * @param interfaceFieldDescription
	 *            the interfaceFieldDescription to set
	 */
	public void setInterfaceFieldDescription(
			final String interfaceFieldDescription) {
		this.interfaceFieldDescription = interfaceFieldDescription;
	}

	/**
	 * @return the mappingTypeName
	 */
	public String getMappingTypeName() {
		return this.mappingTypeName;
	}

	/**
	 * @param mappingTypeName
	 *            the mappingTypeName to set
	 */
	public void setMappingTypeName(final String mappingTypeName) {
		this.mappingTypeName = mappingTypeName;
	}

	/**
	 * @return the mappingTypeType
	 */
	public String getMappingTypeType() {
		return this.mappingTypeType;
	}

	/**
	 * @param mappingTypeType
	 *            the mappingTypeType to set
	 */
	public void setMappingTypeType(final String mappingTypeType) {
		this.mappingTypeType = mappingTypeType;
	}

	/**
	 * @return the mappingTypeDescription
	 */
	public String getMappingTypeDescription() {
		return this.mappingTypeDescription;
	}

	/**
	 * @param mappingTypeDescription
	 *            the mappingTypeDescription to set
	 */
	public void setMappingTypeDescription(final String mappingTypeDescription) {
		this.mappingTypeDescription = mappingTypeDescription;
	}

	/**
	 * @return the mappingTypeInputTypes
	 */
	public List<String> getMappingTypeInputTypes() {
		return this.mappingTypeInputTypes;
	}

	/**
	 * @param mappingTypeInputTypes
	 *            the mappingTypeInputTypes to set
	 */
	public void setMappingTypeInputTypes(
			final List<String> mappingTypeInputTypes) {
		this.mappingTypeInputTypes = mappingTypeInputTypes;
	}
}
