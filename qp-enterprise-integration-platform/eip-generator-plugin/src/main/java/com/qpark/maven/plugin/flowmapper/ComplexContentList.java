/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;

import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
class ComplexContentList {
	public static boolean isComplexMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}ComplexMappingType");
	}

	public static boolean isComplexUUIDMappingType(
			final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}ComplexUUIDMappingType");
	}

	public static boolean isDefaultMappingType(final SchemaType schemaType) {
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

	public static boolean isDirectMappingType(final SchemaType schemaType) {
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

	public static boolean isInterfaceType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}InterfaceType");
	}

	public static boolean isMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}MappingType");
	}

	public static boolean isMapRequestType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/Mapping}MappingInputType");
	}

	public static boolean isMapResponseType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/Mapping}MappingOutputType");
	}

	public static boolean isTabularMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://.*?/Interfaces/MappingTypes}TabularMappingType");
	}

	private final List<ComplexContent> complexMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> complexUUIDMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> defaultMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> directMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> interfaceTypes = new ArrayList<ComplexContent>();
	private final List<ComplexRequestResponse> requestResponses = new ArrayList<ComplexRequestResponse>();
	private final List<ComplexContent> tabularMappings = new ArrayList<ComplexContent>();

	public ComplexContent getComplexContent(final String namespace,
			final String ctName) {
		ComplexContent cc = null;
		String qname;
		for (ComplexContent complexContent : this.complexMappings) {
			qname = complexContent.ct.toQNameString();
			if (qname.endsWith(ctName) && qname.contains(namespace)) {
				cc = complexContent;
				break;
			}
		}
		if (cc == null) {
			for (ComplexContent complexContent : this.complexUUIDMappings) {
				qname = complexContent.ct.toQNameString();
				if (qname.endsWith(ctName) && qname.contains(namespace)) {
					cc = complexContent;
					break;
				}
			}
		}
		if (cc == null) {
			for (ComplexContent complexContent : this.defaultMappings) {
				qname = complexContent.ct.toQNameString();
				if (qname.endsWith(ctName) && qname.contains(namespace)) {
					cc = complexContent;
					break;
				}
			}
		}
		if (cc == null) {
			for (ComplexContent complexContent : this.directMappings) {
				qname = complexContent.ct.toQNameString();
				if (qname.endsWith(ctName) && qname.contains(namespace)) {
					cc = complexContent;
					break;
				}
			}
		}
		if (cc == null) {
			for (ComplexContent complexContent : this.interfaceTypes) {
				qname = complexContent.ct.toQNameString();
				if (qname.endsWith(ctName) && qname.contains(namespace)) {
					cc = complexContent;
					break;
				}
			}
		}
		if (cc == null) {
			for (ComplexContent complexContent : this.tabularMappings) {
				qname = complexContent.ct.toQNameString();
				if (qname.endsWith(ctName) && qname.contains(namespace)) {
					cc = complexContent;
					break;
				}
			}
		}
		return cc;
	}

	/**
	 * @return the complexMappings
	 */
	public List<ComplexContent> getComplexMappings() {
		return this.complexMappings;
	}

	/**
	 * @return the complexUUIDMappings
	 */
	public List<ComplexContent> getComplexUUIDMappings() {
		return this.complexUUIDMappings;
	}

	/**
	 * @return the defaultMappings
	 */
	public List<ComplexContent> getDefaultMappings() {
		return this.defaultMappings;
	}

	/**
	 * @return the directMappings
	 */
	public List<ComplexContent> getDirectMappings() {
		return this.directMappings;
	}

	/**
	 * @return the interfaceTypes
	 */
	public List<ComplexContent> getInterfaceTypes() {
		return this.interfaceTypes;
	}

	/**
	 * @return the requestResponses
	 */
	public List<ComplexRequestResponse> getRequestResponses() {
		return this.requestResponses;
	}

	/**
	 * @return the complexMappings
	 */
	public List<ComplexContent> getTabularMappings() {
		return this.tabularMappings;
	}

	public void setupComplexContentLists(final XsdsUtil config) {
		ComplexType response;
		for (ComplexType ct : config.getComplexTypes()) {
			response = XsdsUtil.findResponse(ct, config.getComplexTypes(),
					config);
			if (ct.isRequestType() && response != null
					&& isMapRequestType(ct.getType())
					&& isMapResponseType(response.getType())) {
				this.requestResponses
						.add(new ComplexRequestResponse(ct, response));
			} else if (isDirectMappingType(ct.getType())) {
				this.directMappings.add(new ComplexContent(ct).setDirect());
			} else if (isTabularMappingType(ct.getType())) {
				this.tabularMappings.add(new ComplexContent(ct).setTabular());
			} else if (isComplexUUIDMappingType(ct.getType())
					&& !ct.getClassName().toLowerCase().contains("lifecycle")
					&& GeneratorMapperMojo.getValidChildren(ct).size() > 1) {
				this.complexUUIDMappings
						.add(new ComplexContent(ct).setComplexUUID());
			} else if (isComplexMappingType(ct.getType())) {
				this.complexMappings.add(new ComplexContent(ct).setComplex());
			} else if (isInterfaceType(ct.getType())) {
				this.interfaceTypes.add(new ComplexContent(ct).setInterface());
			} else if (isDefaultMappingType(ct.getType())) {
				this.defaultMappings.add(new ComplexContent(ct).setDefault());
			} else if (isMappingType(ct.getType())) {
				/* Add all remaining mapping types as complex mapping types. */
				this.complexMappings.add(new ComplexContent(ct).setComplex());
			}
		}
	}

}
