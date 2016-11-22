/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlShort;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlTime;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.XmlUnsignedLong;
import org.apache.xmlbeans.XmlUnsignedShort;
import org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class XsdsUtil {
	/** XMLSchema name space . */
	public static final String QNAME_BASE_SCHEMA_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema";

	/**
	 * Get the namespace to be used in the sample coding.
	 *
	 * @param xml
	 *            the xml.
	 * @param targetNamespace
	 *            the namespace
	 * @return the name space abbreviation.
	 */
	private static String getSampleCodingNamespaceAbbreviation(final String xml,
			final String targetNamespace) {
		String prefix = "";
		final int index = xml.indexOf(targetNamespace);
		if (index > 0) {
			if (xml.charAt(index - 1) == '"' && xml.charAt(index - 2) == '=') {
				int i = 0;
				while (true) {
					if (index - (2 + i) == 0) {
						break;
					} else if (xml.charAt(index - (2 + i)) == ' ') {
						break;
					} else if (xml.charAt(index - (2 + i)) == ':') {
						prefix = xml.substring(index - (2 + i - 1), index - 2);
						break;
					} else {
						i++;
					}
				}
			}
		}
		return prefix;
	}

	/**
	 * Find the corresponding request {@link ComplexType}.
	 *
	 * @param response
	 *            the response {@link ComplexType}.
	 * @param complexTypes
	 *            the set of {@link ComplexType}s available.
	 * @param config
	 *            the {@link XsdsUtil}.
	 * @return the request {@link ComplexType} or <code>null</code>.
	 */
	public static ComplexType findRequest(final ComplexType response,
			final Set<ComplexType> complexTypes, final XsdsUtil config) {
		ComplexType request = null;
		if (response.isResponseType()) {
			final int index = response.getClassName()
					.lastIndexOf(config.getServiceResponseSuffix());
			final String baseName = response.getClassName().substring(0, index);
			for (final ComplexType complexType : complexTypes) {
				if (complexType.isRequestType()
						&& complexType.getPackageName()
								.equals(response.getPackageName())
						&& complexType.getClassName().startsWith(baseName)) {
					request = complexType;
					break;
				}
			}
		}
		return request;
	}

	/**
	 * Find the corresponding response {@link ComplexType}.
	 *
	 * @param request
	 *            the request {@link ComplexType}.
	 * @param complexTypes
	 *            the set of {@link ComplexType}s available.
	 * @param config
	 *            the {@link XsdsUtil}.
	 * @return the response {@link ComplexType} or <code>null</code>.
	 */
	public static ComplexType findResponse(final ComplexType request,
			final Set<ComplexType> complexTypes, final XsdsUtil config) {
		ComplexType response = null;
		if (request.isRequestType()) {
			final int index = request.getClassName()
					.lastIndexOf(config.getServiceRequestSuffix());
			final String baseName = request.getClassName().substring(0, index);
			for (final ComplexType complexType : complexTypes) {
				if (complexType.isResponseType()
						&& complexType.getPackageName()
								.equals(request.getPackageName())
						&& complexType.getClassName().startsWith(baseName)) {
					response = complexType;
					break;
				}
			}
		}
		return response;
	}

	/**
	 * Find the corresponding response {@link ElementType}.
	 *
	 * @param request
	 *            the request {@link ElementType}.
	 * @param elementTypes
	 *            the set of {@link ElementType}s available.
	 * @param config
	 *            the {@link XsdsUtil}.
	 * @return the response {@link ElementType} or <code>null</code>.
	 */
	public static ElementType findResponse(final ElementType request,
			final Set<ElementType> elementTypes, final XsdsUtil config) {
		ElementType response = null;
		final int index = request.getClassNameObject()
				.lastIndexOf(config.getServiceRequestSuffix());
		if (index > 0) {
			final String responseName = new StringBuffer()
					.append(request.getClassNameObject().substring(0, index))
					.append(config.getServiceResponseSuffix()).toString();
			for (final ElementType elementType : elementTypes) {
				if (elementType.getClassNameObject().equals(responseName)
						&& elementType.getPackageName()
								.equals(request.getPackageName())) {
					response = elementType;
					break;
				}
			}
		}
		return response;
	}

	/**
	 * Get the base build in {@link SchemaType} of the given {@link SchemaType}.
	 *
	 * @param schemaType
	 *            the {@link SchemaType} to determine.
	 * @return the base build in {@link SchemaType}.
	 */
	public static SchemaType getBuildInBaseType(final SchemaType schemaType) {
		SchemaType base = schemaType.isBuiltinType() ? schemaType
				: schemaType.getBaseType();
		if (base != null) {
			if (!base.isBuiltinType()) {
				SchemaType basex = base;
				do {
					base = basex;
					basex = basex.getBaseType();
					// System.out.println("+++" + basex);
				} while (basex != null && !basex.isBuiltinType()
						&& !basex.getBaseType().equals(basex));
			}
			if (base.isBuiltinType()) {
				// SchemaType basex = base;
				// do {
				// base = basex;
				// basex = basex.getBaseType();
				// // System.out.println("---" + basex);
				// } while (basex != null
				// && !basex.getName().getLocalPart()
				// .equals("anySimpleType")
				// && !basex.getName().getLocalPart().equals("integer"));
			}
		}
		return base;
	}

	/**
	 * Get the java class implementing the base build in {@link SchemaType}.
	 *
	 * @param schemaType
	 *            the {@link SchemaType}.
	 * @return the implementing {@link Class}.
	 */
	public static Class<?> getBuildInBaseTypeClass(
			final SchemaType schemaType) {
		Class<?> c = getBuildInBaseTypeClassInternal(schemaType);
		if (c == null) {
			final SchemaType baseSchemaType = getBuildInBaseType(schemaType);
			c = getBuildInBaseTypeClassInternal(baseSchemaType);
		}
		if (c == null) {
			c = Object.class;
		}
		return c;
	}

	/**
	 * Provide {@link Class}s for the base build in {@link SchemaType}s.
	 *
	 * @param schemaType
	 *            the {@link SchemaType}.
	 * @return the implementing {@link Class}.
	 */
	private static Class<?> getBuildInBaseTypeClassInternal(
			final SchemaType schemaType) {
		Class<?> c = null;
		if (schemaType != null && schemaType.getName() != null) {
			if (schemaType.getName().getLocalPart().equals("string")) {
				c = String.class;
			} else if (schemaType.getName().getLocalPart()
					.equals("normalizedString")) {
				c = String.class;
			} else if (schemaType.getName().getLocalPart().equals("dateTime")) {
				c = Date.class;
			} else if (schemaType.getName().getLocalPart().equals("time")) {
				c = Date.class;
			} else if (schemaType.getName().getLocalPart().equals("date")) {
				c = Date.class;
			} else if (schemaType.getName().getLocalPart().equals("boolean")) {
				c = Boolean.class;
			} else if (schemaType.getName().getLocalPart().equals("float")) {
				c = Float.class;
			} else if (schemaType.getName().getLocalPart().equals("decimal")) {
				c = BigDecimal.class;
			} else if (schemaType.getName().getLocalPart().equals("long")) {
				c = Long.class;
			} else if (schemaType.getName().getLocalPart().equals("int")) {
				c = int.class;
			} else if (schemaType.getName().getLocalPart().equals("integer")) {
				c = Integer.class;
			} else if (schemaType.getName().getLocalPart().equals("double")) {
				c = Double.class;
			} else if (schemaType.getName().getLocalPart().equals("anyURI")) {
				c = URI.class;
			} else if (schemaType.getName().getLocalPart()
					.equals("hexBinary")) {
				c = byte[].class;

			} else if (schemaType.getName().getLocalPart()
					.equals("positiveInteger")) {
				c = Integer.class;
			} else if (schemaType.getName().getLocalPart()
					.equals("negativeInteger")) {
				c = Integer.class;
			} else if (schemaType.getName().getLocalPart()
					.equals("nonPositiveInteger")) {
				c = Integer.class;
			} else if (schemaType.getName().getLocalPart()
					.equals("nonNegativeInteger")) {
				c = Integer.class;

			} else if (schemaType.getName().getLocalPart()
					.equals("anySimpleType")) {
				c = String.class;
			}
		}
		return c;
	}

	public static String getBuildInBaseTypeFormat(final SchemaType schemaType) {
		String s = "";
		final SchemaType base = getBuildInBaseType(schemaType);
		if (base.getName().getLocalPart().equals("dateTime")) {
			s = "@DateTimeFormat(pattern = \"yyyyMMdd\")";
		} else if (base.getName().getLocalPart().equals("time")) {
			s = "@DateTimeFormat(pattern = \"yyyy-MM-dd\")";
		} else if (base.getName().getLocalPart().equals("date")) {
			s = "@DateTimeFormat(pattern = \"yyyy-MM-dd\")";
		}
		return s;
	}

	/**
	 * Get the build in {@link Class} for the {@link QName}.
	 *
	 * @param qName
	 *            the {@link QName}.
	 * @return the build in {@link Class}.
	 */
	public static Class<?> getBuildInJavaClass(final QName qName) {
		Class<?> javaType = null;
		if (qName != null && QNAME_BASE_SCHEMA_NAMESPACE_URI
				.equals(qName.getNamespaceURI())) {
			final String typeName = qName.getLocalPart();
			if (typeName.equals("anyType")) {
				javaType = org.apache.xmlbeans.XmlObject.class;
			} else if (typeName.equals("anySimpleType")) {
				javaType = String.class;
			} else if (typeName.equals("anyURI")) {
				javaType = String.class;
			} else if (typeName.equals("base64Binary")) {
				javaType = byte[].class;
			} else if (typeName.equals("boolean")) {
				javaType = boolean.class;
			} else if (typeName.equals("byte")) {
				javaType = byte.class;
			} else if (typeName.equals("date")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("dateTime")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("decimal")) {
				javaType = java.math.BigDecimal.class;
			} else if (typeName.equals("double")) {
				javaType = double.class;
			} else if (typeName.equals("duration")) {
				javaType = javax.xml.datatype.Duration.class;
			} else if (typeName.equals("ENTITIES")) {
				javaType = String.class;
			} else if (typeName.equals("ENTITY")) {
				javaType = String.class;
			} else if (typeName.equals("float")) {
				javaType = float.class;
			} else if (typeName.equals("gDay")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("gMonth")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("gMonthDay")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("gYear")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("gYearMonth")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("hexBinary")) {
				javaType = byte[].class;
			} else if (typeName.equals("ID")) {
				javaType = String.class;
			} else if (typeName.equals("IDREF")) {
				javaType = String.class;
			} else if (typeName.equals("IDREFS")) {
				javaType = String.class;
			} else if (typeName.equals("int")) {
				javaType = int.class;
			} else if (typeName.equals("integer")) {
				javaType = java.math.BigInteger.class;
			} else if (typeName.equals("language")) {
				javaType = String.class;
			} else if (typeName.equals("long")) {
				javaType = long.class;
			} else if (typeName.equals("Name")) {
				javaType = String.class;
			} else if (typeName.equals("NCName")) {
				javaType = String.class;
			} else if (typeName.equals("negativeInteger")) {
				javaType = java.math.BigInteger.class;
			} else if (typeName.equals("NMTOKEN")) {
				javaType = String.class;
			} else if (typeName.equals("NMTOKENS")) {
				javaType = String.class;
			} else if (typeName.equals("nonNegativeInteger")) {
				javaType = java.math.BigInteger.class;
			} else if (typeName.equals("nonPositiveInteger")) {
				javaType = java.math.BigInteger.class;
			} else if (typeName.equals("normalizedString")) {
				javaType = String.class;
			} else if (typeName.equals("NOTATION")) {
				javaType = null;
			} else if (typeName.equals("positiveInteger")) {
				javaType = java.math.BigInteger.class;
			} else if (typeName.equals("QName")) {
				javaType = javax.xml.namespace.QName.class;
			} else if (typeName.equals("short")) {
				javaType = short.class;
			} else if (typeName.equals("string")) {
				javaType = String.class;
			} else if (typeName.equals("time")) {
				javaType = javax.xml.datatype.XMLGregorianCalendar.class;
			} else if (typeName.equals("token")) {
				javaType = String.class;
			} else if (typeName.equals("unsignedByte")) {
				javaType = short.class;
			} else if (typeName.equals("unsignedInt")) {
				javaType = long.class;
			} else if (typeName.equals("unsignedLong")) {
				javaType = java.math.BigInteger.class;
			} else if (typeName.equals("unsignedShort")) {
				javaType = int.class;
			}
		}
		return javaType;
	}

	/**
	 * Get model XSDs that are not imported in service XSDs or other models.
	 *
	 * @param xsds
	 *            the map of all {@link XsdContainer}s.
	 * @param messagePackageNameSuffix
	 *            the suffix defining message (service) XSDs.
	 * @return the {@link Map} of target name spaces containing not used model
	 *         {@link XsdContainer}s.
	 */
	public static Map<String, String> getNotImportedModels(
			final Map<String, XsdContainer> xsds,
			final String messagePackageNameSuffix) {
		final TreeMap<String, String> notImportedModels = new TreeMap<String, String>();
		for (final Entry<String, XsdContainer> entry : xsds.entrySet()) {
			if (!isMessagePackageName(entry.getValue().getPackageName(),
					messagePackageNameSuffix)) {
				notImportedModels.put(entry.getKey(),
						entry.getValue().getFile().getAbsolutePath());
			}
		}
		for (final Entry<String, XsdContainer> entry : xsds.entrySet()) {
			for (final String imp : entry.getValue()
					.getImportedTargetNamespaces()) {
				notImportedModels.remove(imp);
			}
		}
		return notImportedModels;
	}

	/**
	 * Get model XSDs that are not imported in service XSDs or other models.
	 *
	 * @param xsds
	 *            the {@link XsdsUtil}s.
	 * @param messagePackageNameSuffix
	 *            the suffix defining message (service) XSDs.
	 * @return the {@link Map} of target name spaces containing not used model
	 *         {@link XsdContainer}s.
	 */
	public static Map<String, String> getNotImportedModels(final XsdsUtil xsds,
			final String messagePackageNameSuffix) {
		final Map<String, String> notImportedModels = getNotImportedModels(
				xsds.getXsdContainerMap(), messagePackageNameSuffix);
		return notImportedModels;
	}

	/**
	 * Get a new {@link Comparator} comparing {@link QName}s.
	 *
	 * @return the {@link Comparator}.
	 */
	public static Comparator<QName> getQNameComparator() {
		final Comparator<QName> comparator = (o1, o2) -> {
			if (o1 == o2) {
				return 0;
			} else if (o2 == null) {
				return -1;
			} else if (o1 == null) {
				return 1;
			} else {
				return o1.toString().compareTo(o2.toString());
			}
		};
		return comparator;
	}

	/**
	 * Get coding producing sample XML for the {@link SchemaType}.
	 *
	 * @param schemaType
	 *            the {@link SchemaType}.
	 * @param elementName
	 *            the name of the element the sample XML should be contained in.
	 * @return the XML.
	 */
	public static String getSampleCodeing(final SchemaType schemaType,
			final String elementName) {
		String xml = SampleXmlUtil.createSampleForType(schemaType);
		if (schemaType.getName() != null
				&& schemaType.getName().getNamespaceURI() != null) {
			final String prefix = getSampleCodingNamespaceAbbreviation(xml,
					schemaType.getName().getNamespaceURI());
			if (prefix.length() > 0) {
				xml = xml.replace("xml-fragment",
						new StringBuffer(32).append(prefix).append(":")
								.append(elementName).toString());
			} else {
				xml = xml.replace("<xml-fragment",
						new StringBuffer(32).append("<").append(elementName)
								.append(" xmlns=\"")
								.append(schemaType.getName().getNamespaceURI())
								.append("\"").toString());
				xml = xml.replace("</xml-fragment", new StringBuffer(32)
						.append("</").append(elementName).toString());
			}
		}
		xml = xml.replace("<severity>ERROR</severity>",
				"<severity>WARNING</severity>");
		final StringBuffer sb = new StringBuffer();
		sb.append("\t\tStringBuffer sb = new StringBuffer();\n");
		final String[] ss = xml.split("\\n");
		for (final String string : ss) {
			if (!string.contains("<!--Optional:-->")
					&& !string.contains("<!--Zero or more repetitions:-->")) {
				sb.append("\t\tsb.append(\"");
				sb.append(string.replaceAll("\"", "\\\\\"")
						.replaceAll("  ", "\t").replace('\r', ' '));
				sb.append("\");\n");
			}
		}
		return sb.toString().replaceAll("stringstringstringstringstringstring",
				"00000000-0000-0000-0000-000000000000");
	}

	/**
	 * Parse the {@link File} with apache XML Beans.
	 *
	 * @param file
	 *            the File to parse.
	 * @param entityResolver
	 *            the {@link EntityResolver}.
	 * @return the parsed {@link SchemaTypeSystem}.
	 */
	public static SchemaTypeSystem getSchemaTypeSystem(final File file,
			final EntityResolver entityResolver) {
		SchemaTypeSystem sts = null;
		final ArrayList<XmlObject> parsedMessages = new ArrayList<XmlObject>();
		final XmlOptions xopt = new XmlOptions();
		xopt.setLoadLineNumbers();
		xopt.setLoadMessageDigest();
		final XmlOptions compileOptions = new XmlOptions();
		compileOptions.setCompileDownloadUrls();
		compileOptions.setEntityResolver(entityResolver);

		try {
			parsedMessages.add(XmlObject.Factory.parse(file, xopt));
			final XmlObject[] schemas = parsedMessages
					.toArray(new XmlObject[parsedMessages.size()]);
			sts = XmlBeans.compileXsd(schemas, XmlBeans.getBuiltinTypeSystem(),
					compileOptions);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return sts;
	}

	public static String getXmlObjectAsSetterParam(final String objAndSetter,
			final XmlAnySimpleType obj) {
		final StringBuffer sb = new StringBuffer(32);
		if (XmlString.class.isInstance(obj)
				|| XmlAnyURI.class.isInstance(obj)) {
			sb.append(objAndSetter).append("(\"").append(obj.getStringValue())
					.append("\");");
		} else if (XmlShort.class.isInstance(obj)) {
			sb.append(objAndSetter).append("((short)")
					.append(obj.getStringValue()).append(");");
		} else if (XmlInt.class.isInstance(obj)
				|| XmlUnsignedShort.class.isInstance(obj)) {
			sb.append(objAndSetter).append("(").append(obj.getStringValue())
					.append(");");
		} else if (XmlLong.class.isInstance(obj)
				|| XmlUnsignedInt.class.isInstance(obj)) {
			sb.append(objAndSetter).append("(").append(obj.getStringValue())
					.append("L);");
		} else if (XmlFloat.class.isInstance(obj)) {
			sb.append(objAndSetter).append("(").append(obj.getStringValue())
					.append("f);");
		} else if (XmlInteger.class.isInstance(obj)
				|| XmlUnsignedLong.class.isInstance(obj)) {
			sb.append(objAndSetter).append("(")
					.append("new java.math.BigInteger(\"")
					.append(obj.getStringValue()).append("\"));");
		} else if (XmlDecimal.class.isInstance(obj)) {
			sb.append(objAndSetter).append("(")
					.append("new java.math.BigDecimal(\"")
					.append(obj.getStringValue()).append("\"));");
		} else if (XmlDate.class.isInstance(obj)
				|| XmlDateTime.class.isInstance(obj)
				|| XmlTime.class.isInstance(obj)) {
			sb.append("try { ");
			sb.append(objAndSetter)
					.append("(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(\"")
					.append(obj.getStringValue()).append("\"));");
			sb.append(
					" } catch (javax.xml.datatype.DatatypeConfigurationException datatypeConfigurationException) { datatypeConfigurationException.printStackTrace(); }");
		} else {
			sb.append(objAndSetter).append("(").append(obj.getStringValue())
					.append(");");
		}
		return sb.toString();
	}

	private static XsdContainer getXsdContainer(final File f,
			final File baseDirectory) throws IOException {
		XsdContainer xsdContainer = null;
		final String text = Util.readFile(f);
		String targetNameSpace = null;
		String annotationDocumentation = null;
		String packageName = null;
		int index0 = text.indexOf("targetNamespace=\"");
		if (index0 > 0) {
			index0 += "targetNamespace=\"".length();
			final int index1 = text.substring(index0).indexOf("\"");
			targetNameSpace = text.substring(index0, index0 + index1);
		}
		index0 = text.indexOf("schemaBindings");
		if (index0 > 0) {
			index0 = text.indexOf("package");
			if (index0 > 0) {
				index0 = text.indexOf("name=\"");
				if (index0 > 0) {
					index0 += "name=\"".length();
					final int index1 = text.substring(index0).indexOf("\"");
					packageName = text.substring(index0, index0 + index1);
				}
			}
		}
		if (packageName != null) {
			index0 = text.indexOf("<documentation>");
			final int index1 = text.indexOf("</documentation>");
			if (index0 > 0 && index1 > index0) {
				index0 += "<documentation>".length();
				annotationDocumentation = text.substring(index0, index1).trim();
			}
		}

		final TreeMap<String, String> imports = new TreeMap<String, String>();
		index0 = text.indexOf("<import");
		String namespace = null;
		String location = null;
		int indexNamespace;
		int indexLocation;
		while (index0 > 0) {
			int index1 = text.indexOf('>', index0);
			if (index1 > 0) {
				namespace = null;
				location = null;
				indexNamespace = text.indexOf("namespace=\"", index0);
				if (indexNamespace > 0) {
					indexNamespace += "namespace=\"".length();
					index1 = text.substring(indexNamespace).indexOf("\"");
					namespace = text.substring(indexNamespace,
							indexNamespace + index1);
				}
				indexLocation = text.indexOf("schemaLocation=\"", index0);
				if (indexLocation > 0) {
					indexLocation += "schemaLocation=\"".length();
					index1 = text.substring(indexLocation).indexOf("\"");
					location = text.substring(indexLocation,
							indexLocation + index1);
				}
				if (namespace != null && location != null) {
					if (imports.containsKey(namespace)) {
						throw new RuntimeException(
								"Double import of targetNamespace in "
										+ f.getAbsolutePath() + ":"
										+ namespace);
					}
					imports.put(namespace, location);
				} else {
					throw new RuntimeException(
							"Not declared targetNamespace or location in "
									+ f.getAbsolutePath() + ": "
									+ targetNameSpace + " " + location);
				}
				index0 = text.indexOf("<import", index0 + 1);
			} else {
				index0 = 0;
			}
		}

		final TreeMap<String, String> xmlnss = new TreeMap<String, String>();
		final TreeSet<String> usedXmlnss = new TreeSet<String>();
		String namespaceToken;
		index0 = text.indexOf("xmlns:");
		while (index0 > 0) {
			index0 += "xmlns:".length();
			final int index1 = text.indexOf("=\"", index0);
			final int index2 = text.indexOf('"', index1 + "=\"".length());
			if (index1 > 0 && index2 > 0) {
				namespaceToken = text.substring(index0, index1);
				namespace = text.substring(index1 + "=\"".length(), index2);
				if (namespace != null && namespaceToken != null) {
					if (xmlnss.containsKey(namespaceToken)) {
						throw new RuntimeException(
								"Double xmlns namespace token in "
										+ f.getAbsolutePath() + ":"
										+ namespaceToken);
					} else if (xmlnss.containsValue(namespace)) {
						throw new RuntimeException("Double xmlns namespace in "
								+ f.getAbsolutePath() + ":" + namespace);
					}
					xmlnss.put(namespaceToken, namespace);
				} else {
					throw new RuntimeException("Not declared xmlns in "
							+ f.getAbsolutePath() + ": " + targetNameSpace
							+ " xmlns:" + namespaceToken + "=" + namespace);
				}
				index0 = text.indexOf("xmlns:", index0 + 1);
			} else {
				index0 = 0;
			}
		}
		for (final String xmlnsNamespaceToken : xmlnss.keySet()) {
			index0 = text.indexOf(new StringBuffer(16)
					.append(xmlnsNamespaceToken).append(":").toString());
			if (index0 > 0) {
				usedXmlnss.add(xmlnsNamespaceToken);
			}
		}

		if (targetNameSpace != null && packageName != null) {
			xsdContainer = new XsdContainer(f, baseDirectory, packageName,
					targetNameSpace, annotationDocumentation, imports, xmlnss,
					usedXmlnss);
		}
		return xsdContainer;
	}

	public static Map<String, XsdContainer> getXsdContainers(
			final File baseDirectory) {
		return setupXsdContainers(getXsdFiles(baseDirectory), baseDirectory);
	}

	public static List<File> getXsdFiles(final File baseDirectory) {
		final List<File> xsdFiles = new ArrayList<File>();
		scanForXsds(baseDirectory, xsdFiles);
		return xsdFiles;
	}

	public static boolean isList(final SchemaProperty p) {
		if (p.getMaxOccurs() != null && p.getMaxOccurs().intValue() > 1) {
			return true;
		}
		return false;
	}

	public static boolean isMessagePackageName(final String packageName,
			final String... messagePackageNameSuffixes) {
		boolean isMessagePackageName = false;
		if (packageName != null) {
			final TreeSet<String> packages = new TreeSet<String>();
			if (messagePackageNameSuffixes != null) {
				for (final String messagePackageNameSuffix : messagePackageNameSuffixes) {
					if (messagePackageNameSuffix != null
							&& messagePackageNameSuffix.trim().length() > 0) {
						final String[] suffixes = messagePackageNameSuffix
								.replaceAll(",", " ").split(" ");
						for (final String suffix : suffixes) {
							if (suffix != null && suffix.trim().length() > 0) {
								packages.add(
										new StringBuffer(suffix.length() + 1)
												.append(".")
												.append(suffix.trim())
												.toString());
							}
						}
					}
				}
			}
			for (final String string : packages) {
				if (packageName.contains(string)) {
					isMessagePackageName = true;
					break;
				}
			}
		}
		return isMessagePackageName;
	}

	public static boolean isRequired(final SchemaProperty p) {
		if (p.getMinOccurs() != null && p.getMinOccurs().intValue() < 1) {
			return false;
		}
		return true;
	}

	/**
	 * Collect the xsd files recursively.
	 *
	 * @param f
	 *            the file to check.
	 * @param xsdFiles
	 *            the list of xsd files.
	 */
	private static void scanForXsds(final File f, final List<File> xsdFiles) {
		final File[] cs = f.listFiles();
		if (cs != null && cs.length > 0) {
			for (final File c : cs) {
				if (c.isDirectory()) {
					scanForXsds(c, xsdFiles);
				} else if (c.getName().endsWith(".xsd")) {
					xsdFiles.add(c);
				}
			}
		}
	}

	public static void setupComplexTypeChildrenDocumentation(
			final XsdContainer xsd,
			final Map<String, ComplexType> complexTypeMap) throws IOException {
		final String text = Util.readFile(xsd.getFile());
		try (Scanner scct = new Scanner(text);) {
			scct.useDelimiter("<complexType");
			String sct;
			String se;
			String ctName;
			String eName;
			ComplexType ct;
			ComplexTypeChild ctc;
			int index;
			while (scct.hasNext()) {
				sct = scct.next().trim();
				if (sct.startsWith("name=\"")) {
					ctName = new StringBuffer(128).append("{")
							.append(xsd.getTargetNamespace()).append("}")
							.append(sct.substring(6, sct.indexOf('"', 7)))
							.toString();
					ct = complexTypeMap.get(ctName);
					if (ct != null) {
						try (Scanner sce = new Scanner(sct.substring(7));) {
							sce.useDelimiter("<element");
							while (sce.hasNext()) {
								se = sce.next().trim();
								if (se.startsWith("name=\"")) {
									eName = se.substring(6, se.indexOf('"', 7));
									ctc = ct.getChild(eName);
									if (ctc != null) {
										index = se.indexOf("/>");
										if (index < 0) {
											index = se.indexOf("</");
										} else {
											final int x = se.indexOf("</");
											if (x > 0 && x < index) {
												index = x;
											}
										}
										se = se.substring(7, index);
										index = se.indexOf("<documentation>");
										if (index > 0) {
											ctc.setAnnotationDocumentation(
													se.substring(index + 15)
															.trim());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static Map<String, XsdContainer> setupXsdContainers(
			final List<File> xsdFiles, final File baseDirectory) {
		final Map<String, XsdContainer> xsdContainerMap = new ConcurrentHashMap<String, XsdContainer>();
		for (final File f : xsdFiles) {
			XsdContainer xsdContainer;
			try {
				xsdContainer = getXsdContainer(f, baseDirectory);
				if (xsdContainer != null
						&& xsdContainer.getTargetNamespace() != null) {
					if (xsdContainerMap
							.containsKey(xsdContainer.getTargetNamespace())) {
						throw new RuntimeException(new StringBuffer(256)
								.append("Target namespace \"")
								.append(xsdContainer.getTargetNamespace())
								.append("\" defined in file ")
								.append(xsdContainerMap
										.get(xsdContainer.getTargetNamespace())
										.getFile().getAbsolutePath())
								.append(" and ")
								.append(xsdContainer.getFile()
										.getAbsolutePath())
								.append("!").toString());
					} else {
						xsdContainerMap.put(xsdContainer.getTargetNamespace(),
								xsdContainer);
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		for (final XsdContainer container : xsdContainerMap.values()) {
			logger.debug("setup totat imports  container  {}{}",
					container.getTargetNamespace(),
					container.getFile().getAbsolutePath());
			setupXsdContainerTotalImports(container, xsdContainerMap);
		}
		final TreeMap<String, XsdContainer> value = new TreeMap<String, XsdContainer>();
		value.putAll(xsdContainerMap);
		return value;
	}

	private static void setupXsdContainerTotalImports(
			final XsdContainer container, final Map<String, XsdContainer> map) {
		XsdContainer child;
		for (final String importedTargetNamespace : container
				.getImportedTargetNamespaces()) {
			if (!container.getTotalImportedTargetNamespaces()
					.contains(importedTargetNamespace)) {
				child = map.get(importedTargetNamespace);
				if (child != null) {
					container.getTotalImportedTargetNamespaces()
							.add(child.getTargetNamespace());
					setupXsdContainerTotalImports(child, map);
				}
			}
		}
	}

	/** The root directory where the xsds could be found. */
	private final File baseDirectory;

	/**
	 * The name of the package where the object factories, gateway package and
	 * service activators should be generated.
	 */
	private final String basePackageName;

	/** The map of {@link QName}s with their {@link ComplexType}s. */
	private final Map<String, ComplexType> complexTypeMap = new ConcurrentHashMap<String, ComplexType>();

	/** The {@link TreeSet} of {@link ComplexType}. */
	private final TreeSet<ComplexType> complexTypes = new TreeSet<ComplexType>(
			(o1, o2) -> {
				if (o1 == o2) {
					return 0;
				} else if (o2 == null) {
					return -1;
				} else if (o1 == null) {
					return 1;
				} else {
					return o1.getClassNameFullQualified()
							.compareTo(o2.getClassNameFullQualified());
				}
			});

	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	private final String deltaPackageNameSuffix;
	/** The {@link TreeSet} of {@link ElementType}. */
	private final TreeSet<ElementType> elementTypes = new TreeSet<ElementType>(
			(o1, o2) -> {
				if (o1 == o2) {
					return 0;
				} else if (o2 == null) {
					return -1;
				} else if (o1 == null) {
					return 1;
				} else {
					return getQNameComparator().compare(
							o1.getElement().getName(),
							o2.getElement().getName());
				}
			});

	/** The {@link EntityResolver} for the local xsds. */
	private final EntityResolver entityResolver = (publicId, systemId) -> {
		if (XsdsUtil.this.getXsdContainerMap().containsKey(publicId)) {
			final InputSource is = new InputSource(
					new FileInputStream(XsdsUtil.this.getXsdContainerMap()
							.get(publicId).getFile()));
			return is;
		} else {
			return null;
		}
	};

	/** The {@link Logger}. */
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(XsdsUtil.class);

	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg</code>.
	 */
	private final String messagePackageNameSuffix;

	/** Package names contained in the xsds. */
	private final TreeSet<String> packageNames = new TreeSet<String>();

	/**
	 * The service request name need to end with this suffix (Default
	 * <code>Request</code>).
	 */
	private final String serviceRequestSuffix;

	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	private final String serviceResponseSuffix;

	/** A target name space {@link Map} containing the {@link XsdContainer}s. */
	private final Map<String, XsdContainer> xsdContainerMap;

	/** A {@link List} of xsd files. */
	private final List<File> xsdFiles;

	private XsdsUtil(final File baseDirectory, final String basePackageName,
			final String messagePackageNameSuffix,
			final String deltaPackageNameSuffix) {
		this(baseDirectory, basePackageName, messagePackageNameSuffix,
				deltaPackageNameSuffix, null, null);
	}

	private static XsdsUtil instance0;
	private static XsdsUtil instance1;
	private static XsdsUtil instance2;

	public static XsdsUtil getInstance(final File baseDirectory,
			final String basePackageName, final String messagePackageNameSuffix,
			final String deltaPackageNameSuffix) {
		return getInstance(baseDirectory, basePackageName,
				messagePackageNameSuffix, deltaPackageNameSuffix, null, null);
	}

	private static int reused = 0;
	private static int used = 0;

	private static boolean isSame(final XsdsUtil test, final File baseDirectory,
			final String basePackageName, final String messagePackageNameSuffix,
			final String deltaPackageNameSuffix,
			final String serviceRequestSuffix,
			final String serviceResponseSuffix) {
		boolean value = false;
		if (Objects.nonNull(test)
				&& (baseDirectory == test.baseDirectory
						|| baseDirectory.equals(test.baseDirectory))
				&& String.valueOf(basePackageName)
						.equals(String.valueOf(test.basePackageName))
				&& String.valueOf(messagePackageNameSuffix)
						.equals(String.valueOf(test.messagePackageNameSuffix))
				&& String.valueOf(deltaPackageNameSuffix)
						.equals(String.valueOf(test.deltaPackageNameSuffix))
				&& String.valueOf(serviceRequestSuffix)
						.equals(String.valueOf(test.serviceRequestSuffix))
				&& String.valueOf(serviceResponseSuffix)
						.equals(String.valueOf(test.serviceResponseSuffix))) {
			value = true;
		}
		return value;
	}

	public static XsdsUtil getInstance(final File baseDirectory,
			final String basePackageName, final String messagePackageNameSuffix,
			final String deltaPackageNameSuffix,
			final String serviceRequestSuffix,
			final String serviceResponseSuffix) {
		XsdsUtil value;
		if (isSame(instance0, baseDirectory, basePackageName,
				messagePackageNameSuffix, deltaPackageNameSuffix,
				serviceRequestSuffix, serviceResponseSuffix)) {

			reused++;
			value = instance0;
			logger.info("{}", String.format("XsdsUtil reusing %s at %s",
					value.getBasePackageName(), value.getBaseDirectory()));
		} else if (isSame(instance1, baseDirectory, basePackageName,
				messagePackageNameSuffix, deltaPackageNameSuffix,
				serviceRequestSuffix, serviceResponseSuffix)) {

			reused++;
			value = instance1;
			logger.info("{}", String.format("XsdsUtil reusing %s at %s",
					value.getBasePackageName(), value.getBaseDirectory()));
		} else if (isSame(instance2, baseDirectory, basePackageName,
				messagePackageNameSuffix, deltaPackageNameSuffix,
				serviceRequestSuffix, serviceResponseSuffix)) {

			reused++;
			value = instance2;
			logger.info("{}", String.format("XsdsUtil reusing %s at %s",
					value.getBasePackageName(), value.getBaseDirectory()));
		} else {
			value = new XsdsUtil(baseDirectory, basePackageName,
					messagePackageNameSuffix, deltaPackageNameSuffix,
					serviceRequestSuffix, serviceResponseSuffix);

			logger.info("{}", String.format("XsdsUtil creating %s at %s",
					value.getBasePackageName(), value.getBaseDirectory()));

			if (instance0 == null) {
				instance0 = value;
			} else if (instance1 == null) {
				instance1 = value;
			} else if (instance2 == null) {
				instance2 = value;
			} else {
				if (instance0.start < instance1.start) {
					if (instance0.start < instance2.start) {
						instance0 = value;
					} else {
						instance2 = value;
					}
				} else {
					if (instance1.start < instance2.start) {
						instance1 = value;
					} else {
						instance2 = value;
					}
				}
			}
		}
		used++;
		logger.info("XsdsUtil used " + used + " times and reused " + reused
				+ " times");
		return value;
	}

	private final long start;

	private XsdsUtil(final File baseDirectory, final String basePackageName,
			final String messagePackageNameSuffix,
			final String deltaPackageNameSuffix,
			final String serviceRequestSuffix,
			final String serviceResponseSuffix) {
		this.start = System.currentTimeMillis();
		long startX = System.currentTimeMillis();
		logger.debug("XsdsUtil");
		this.baseDirectory = baseDirectory;
		this.basePackageName = basePackageName == null ? "" : basePackageName;
		this.messagePackageNameSuffix = messagePackageNameSuffix == null
				|| messagePackageNameSuffix.trim().length() == 0 ? "msg"
						: messagePackageNameSuffix;
		this.deltaPackageNameSuffix = deltaPackageNameSuffix == null
				|| deltaPackageNameSuffix.trim().length() == 0 ? "delta"
						: deltaPackageNameSuffix;
		this.serviceRequestSuffix = serviceRequestSuffix == null
				|| serviceRequestSuffix.trim().length() == 0 ? "Request"
						: serviceRequestSuffix;
		this.serviceResponseSuffix = serviceResponseSuffix == null
				|| serviceResponseSuffix.trim().length() == 0 ? "Response"
						: serviceResponseSuffix;

		startX = System.currentTimeMillis();
		logger.info("Scanning {} for xsd files ...", this.baseDirectory);
		this.xsdFiles = getXsdFiles(this.baseDirectory);
		this.xsdFiles.stream().forEach(xsdFile -> logger
				.debug(String.format("Contains xsdFile: %s", xsdFile)));
		logger.debug("{} to get {} xsd files",
				Util.getDuration(System.currentTimeMillis() - startX),
				this.xsdFiles.size());

		startX = System.currentTimeMillis();
		this.xsdContainerMap = setupXsdContainers(this.xsdFiles,
				this.baseDirectory);
		this.getXsdContainerMap().values().stream()
				.forEach(xc -> logger.debug(String.format("XsdContainer: %s %s",
						xc.getTargetNamespace(), xc.getFile())));
		logger.debug("{} to get XsdContainers out of {} files",
				Util.getDuration(System.currentTimeMillis() - startX),
				this.xsdFiles.size());

		startX = System.currentTimeMillis();
		this.serviceIdRegistry = new ServiceIdRegistry();
		this.serviceIdRegistry.setupServiceIdTree(this);
		logger.debug("{} to get serviceIds: {}",
				Util.getDuration(System.currentTimeMillis() - startX),
				this.serviceIdRegistry.getAllServiceIds());

		startX = System.currentTimeMillis();
		final List<ComplexType> synchronizedComplexTypeList = Collections
				.synchronizedList(new ArrayList<ComplexType>());
		final List<ElementType> synchronizedElementTypeList = Collections
				.synchronizedList(new ArrayList<ElementType>());
		this.xsdFiles.parallelStream().parallel().forEach(f -> {
			final long startf = System.currentTimeMillis();
			final SchemaTypeSystem sts = getSchemaTypeSystem(f,
					this.entityResolver);
			if (sts == null) {
				throw new IllegalStateException(String.format(
						"Could not parse a valid SchemaTypeSystem from file %s",
						f.getAbsolutePath()));
			}
			Arrays.asList(sts.globalElements()).stream()
					.forEach(elem -> synchronizedElementTypeList
							.add(new ElementType(elem, this)));
			Arrays.asList(sts.globalTypes()).stream().forEach(type -> {
				if (!this.complexTypeMap
						.containsKey(String.valueOf(type.getName()))) {
					final ComplexType ct = new ComplexType(type, this);
					this.complexTypeMap
							.put(String.valueOf(ct.getType().getName()), ct);
					this.setupComplexTypes(ct, synchronizedComplexTypeList);
				}
			});
			logger.debug("{} to get elements and complex type of file {}",
					Util.getDuration(System.currentTimeMillis() - startf),
					f.getAbsolutePath());
		});
		this.elementTypes.addAll(synchronizedElementTypeList);
		this.complexTypes.addAll(synchronizedComplexTypeList);
		logger.info(
				"{} to get {} complexTypes and {} elementTypes out of {} files",
				Util.getDuration(System.currentTimeMillis() - startX),
				this.complexTypes.size(), this.elementTypes.size(),
				this.xsdFiles.size());

		startX = System.currentTimeMillis();
		this.complexTypes.stream().parallel()
				.forEach(ct -> ct.initDescent(this));
		this.complexTypes.stream().parallel()
				.filter(ct -> Objects.nonNull(ct.getType())).forEach(ct -> {
					ct.initChildren(this);
					this.complexTypeMap
							.put(String.valueOf(ct.getType().getName()), ct);
				});
		logger.debug("{} to init complexType children",
				Util.getDuration(System.currentTimeMillis() - startX));

		startX = System.currentTimeMillis();
		this.elementTypes.stream()
				.filter(et -> Objects.nonNull(et.getElement())
						&& Objects.nonNull(et.getElement().getType()))
				.forEach(et -> {
					et.setComplexType(this.complexTypeMap.get(String
							.valueOf(et.getElement().getType().getName())));
				});
		logger.debug("{} to setup elementType complexType",
				Util.getDuration(System.currentTimeMillis() - startX));

		startX = System.currentTimeMillis();
		this.xsdContainerMap.values().stream().forEach(xsdContainer -> {
			try {
				setupComplexTypeChildrenDocumentation(xsdContainer,
						this.complexTypeMap);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
		this.complexTypeMap.values().stream().forEach(ct -> {
			this.setupBaseComplexTypeFieldDocumentation(ct);
		});
		logger.debug("{} to setup complexType children documentation",
				Util.getDuration(System.currentTimeMillis() - startX));
		logger.info("{} to create {} with ServiceIds: ",
				Util.getDuration(System.currentTimeMillis() - this.start),
				this.getClass().getSimpleName(),
				this.serviceIdRegistry.getAllServiceIds());
	}

	private final ServiceIdRegistry serviceIdRegistry;

	public ServiceIdRegistry getServiceIdRegistry() {
		return this.serviceIdRegistry;
	}

	private void setupBaseComplexTypeFieldDocumentation(final ComplexType ct) {
		final ComplexType base = ct.getBaseComplexType();
		if (Objects.nonNull(base)) {
			final Map<String, ComplexTypeChild> baseChildren = base
					.getChildrenMap();
			final Map<String, ComplexTypeChild> childrenMap = ct
					.getChildrenMap();
			baseChildren.keySet().stream().forEach(baseName -> {
				final ComplexTypeChild child = childrenMap.get(baseName);
				if (child.getAnnotationDocumentation().equals("")) {
					child.setAnnotationDocumentation(baseChildren.get(baseName)
							.getAnnotationDocumentation());
				}
			});
			this.setupBaseComplexTypeFieldDocumentation(base);
		}
	}

	/**
	 * @return the baseDirectory
	 */
	public File getBaseDirectory() {
		return this.baseDirectory;
	}

	/**
	 * @return the basePackageName
	 */
	public String getBasePackageName() {
		return this.basePackageName;
	}

	/**
	 * @return the complexType
	 */
	public ComplexType getComplexType(final QName name) {
		return this.complexTypeMap.get(String.valueOf(name));
	}

	/**
	 * @return the complexTypes
	 */
	public TreeSet<ComplexType> getComplexTypes() {
		return this.complexTypes;
	}

	/**
	 * @return the deltaPackageNameSuffix
	 */
	public String getDeltaPackageNameSuffix() {
		return this.deltaPackageNameSuffix;
	}

	/**
	 * @return the elementTypes
	 */
	public TreeSet<ElementType> getElementTypes() {
		return this.elementTypes;
	}

	/**
	 * @return the messagePackageNameSuffix
	 */
	public String getMessagePackageNameSuffix() {
		return this.messagePackageNameSuffix;
	}

	public String getPackageName(final QName qname) {
		if (qname != null) {
			return this.getPackageName(qname.getNamespaceURI());
		} else {
			return null;
		}
	}

	public String getPackageName(final String targetNamespace) {
		String packageName = null;
		final XsdContainer xc = this.xsdContainerMap.get(targetNamespace);
		if (xc != null) {
			packageName = this.xsdContainerMap.get(targetNamespace)
					.getPackageName();
		}
		return packageName;
	}

	/**
	 * @return the packageNames
	 */
	public TreeSet<String> getPackageNames() {
		return this.packageNames;
	}

	/**
	 * @return the serviceRequestSuffix
	 */
	public String getServiceRequestSuffix() {
		return this.serviceRequestSuffix;
	}

	/**
	 * @return the serviceResponseSuffix
	 */
	public String getServiceResponseSuffix() {
		return this.serviceResponseSuffix;
	}

	/**
	 * @return the xsdContainerMap
	 */
	public Map<String, XsdContainer> getXsdContainerMap() {
		return this.xsdContainerMap;
	}

	/**
	 * @return the xsdContainerMap
	 */
	public XsdContainer getXsdContainer(final String targetNamespace) {
		return this.xsdContainerMap.get(targetNamespace);
	}

	/**
	 * @return the xsdFiles
	 */
	public List<File> getXsdFiles() {
		return this.xsdFiles;
	}

	private void setupComplexTypes(final ComplexType ct,
			final List<ComplexType> complexTypeList) {
		complexTypeList.add(ct);
		for (final ComplexType ctx : ct.getInnerTypeDefs()) {
			this.setupComplexTypes(ctx, complexTypeList);
		}
	}
}
