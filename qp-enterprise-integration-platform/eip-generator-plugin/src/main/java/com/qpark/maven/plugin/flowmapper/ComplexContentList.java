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
public class ComplexContentList {

	private final List<ComplexRequestResponse> requestResponses = new ArrayList<ComplexRequestResponse>();
	private final List<ComplexContent> defaultMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> directMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> complexMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> complexUUIDMappings = new ArrayList<ComplexContent>();
	private final List<ComplexContent> interfaceTypes = new ArrayList<ComplexContent>();

	public void setupComplexContentLists(final XsdsUtil config) {
		ComplexType response;
		for (ComplexType complexType : config.getComplexTypes()) {
			response = XsdsUtil.findResponse(complexType,
					config.getComplexTypes(), config);
			if (complexType.isRequestType() && response != null
					&& isMapRequestType(complexType.getType())
					&& isMapResponseType(response.getType())) {
				this.requestResponses.add(new ComplexRequestResponse(
						complexType, response));
			} else if (isDirectMappingType(complexType.getType())) {
				this.directMappings.add(new ComplexContent(complexType, true,
						false, false, false));
			} else if (isComplexUUIDMappingType(complexType.getType())
					&& !complexType.getClassName().toLowerCase()
							.contains("lifecycle")
					&& GeneratorMapperMojo.getValidChildren(complexType).size() > 1) {
				this.complexUUIDMappings.add(new ComplexContent(complexType,
						false, true, false, false));
			} else if (isComplexMappingType(complexType.getType())) {
				this.complexMappings.add(new ComplexContent(complexType, false,
						false, true, false));
			} else if (isInterfaceType(complexType.getType())) {
				this.interfaceTypes.add(new ComplexContent(complexType, false,
						false, false, true));
			} else if (isDefaultMappingType(complexType.getType())) {
				this.defaultMappings.add(new ComplexContent(complexType, false,
						false, false, true));
			}
		}
	}

	public static boolean isDefaultMappingType(final SchemaType schemaType) {
		boolean validType = false;
		if (schemaType != null
				&& schemaType.getName() != null
				&& schemaType.getName().getLocalPart().toLowerCase()
						.contains("default")
				&& schemaType.getElementProperties() != null
				&& schemaType.getElementProperties().length == 1) {
			SchemaProperty defaultProperty = schemaType.getElementProperties()[0];
			if (defaultProperty.getType().isSimpleType()
					&& defaultProperty.getType().getEnumerationValues() != null
					&& defaultProperty.getType().getEnumerationValues().length == 1) {
				validType = true;
			} else if (defaultProperty.getType().isSimpleType()
					&& defaultProperty.getDefaultText() != null) {
				validType = true;
			}
		}
		return validType;
	}

	private static boolean isInstanceOf(final SchemaType schemaType,
			final String qName) {
		boolean validType = false;
		if (schemaType != null && schemaType.getBaseType() != null) {
			if (qName
					.equals(String.valueOf(schemaType.getBaseType().getName()))) {
				validType = true;
			} else {
				validType = isInstanceOf(schemaType.getBaseType(), qName);
			}
		}
		return validType;

	}

	public static boolean isDirectMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://www.ses.com/Interfaces/MappingTypes}DirectMappingType");
	}

	public static boolean isInterfaceType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://www.ses.com/Interfaces/MappingTypes}InterfaceType");
	}

	public static boolean isComplexMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://www.ses.com/Interfaces/MappingTypes}ComplexMappingType");
	}

	public static boolean isComplexUUIDMappingType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://www.ses.com/Interfaces/MappingTypes}ComplexUUIDMappingType");
	}

	public static boolean isMapRequestType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://www.ses.com/Interfaces/Mapping}MappingInputType");
	}

	public static boolean isMapResponseType(final SchemaType schemaType) {
		return isInstanceOf(schemaType,
				"{http://www.ses.com/Interfaces/Mapping}MappingOutputType");
	}

	/**
	 * @return the requestResponses
	 */
	public List<ComplexRequestResponse> getRequestResponses() {
		return this.requestResponses;
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
	 * @return the interfaceTypes
	 */
	public List<ComplexContent> getInterfaceTypes() {
		return this.interfaceTypes;
	}

}