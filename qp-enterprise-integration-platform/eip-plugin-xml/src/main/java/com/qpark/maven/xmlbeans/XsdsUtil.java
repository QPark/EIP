/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaGlobalElement;
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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class XsdsUtil {
	public static final String QNAME_BASE_SCHEMA_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema";

	private static String findNamespace(final String xml,
			final String targetNamespace) {
		String prefix = "";
		int index = xml.indexOf(targetNamespace);
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

	public static ElementType findResponse(final ElementType request,
			final Set<ElementType> elementTypes, final XsdsUtil config) {
		ElementType response = null;
		int index = request.getClassNameObject()
				.lastIndexOf(config.getServiceRequestSuffix());
		if (index > 0) {
			String responseName = new StringBuffer()
					.append(request.getClassNameObject().substring(0, index))
					.append(config.getServiceResponseSuffix()).toString();
			for (ElementType elementType : elementTypes) {
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

	public static ComplexType findResponse(final ComplexType request,
			final Set<ComplexType> complexTypes, final XsdsUtil config) {
		ComplexType response = null;
		int index = request.getClassName()
				.lastIndexOf(config.getServiceRequestSuffix());
		if (request.isRequestType()) {
			String baseName = request.getClassName().substring(0, index);
			for (ComplexType complexType : complexTypes) {
				if (complexType.isResponseType()
						&& complexType.getPackageName()
								.equals(request.getPackageName())
						&& complexType.getClassName().contains(baseName)) {
					response = complexType;
					break;
				}
			}
		}
		return response;
	}

	public static ComplexType findRequest(final ComplexType response,
			final Set<ComplexType> complexTypes, final XsdsUtil config) {
		ComplexType request = null;
		int index = response.getClassName()
				.lastIndexOf(config.getServiceResponseSuffix());
		if (response.isResponseType()) {
			String baseName = response.getClassName().substring(0, index);
			for (ComplexType complexType : complexTypes) {
				if (complexType.isRequestType()
						&& complexType.getPackageName()
								.equals(response.getPackageName())
						&& complexType.getClassName().contains(baseName)) {
					request = complexType;
					break;
				}
			}
		}
		return request;
	}

	public static Comparator<QName> getQNameComparator() {
		Comparator<QName> comparator = new Comparator<QName>() {
			@Override
			public int compare(final QName o1, final QName o2) {
				if (o1 == o2) {
					return 0;
				} else if (o2 == null) {
					return -1;
				} else if (o1 == null) {
					return 1;
				} else {
					return o1.toString().compareTo(o2.toString());
				}
			}
		};
		return comparator;
	}

	public static String getSampleCodeing(final SchemaType type,
			final String elementName) {
		String xml = SampleXmlUtil.createSampleForType(type);
		// System.out.println("#################### " + type.getName());
		// System.out.println("#################### "
		// + findNamespace(xml, type.getName().getNamespaceURI()));
		// System.out.println(xml);
		if (type.getName() != null
				&& type.getName().getNamespaceURI() != null) {
			String prefix = findNamespace(xml,
					type.getName().getNamespaceURI());
			if (prefix.length() > 0) {
				xml = xml.replace("xml-fragment",
						new StringBuffer(32).append(prefix).append(":")
								.append(elementName).toString());
			} else {
				xml = xml
						.replace("<xml-fragment",
								new StringBuffer(32).append("<")
										.append(elementName).append(" xmlns=\"")
										.append(type.getName()
												.getNamespaceURI())
										.append("\"").toString());
				xml = xml.replace("</xml-fragment", new StringBuffer(32)
						.append("</").append(elementName).toString());
			}
		}
		// System.out.println(xml);
		xml = xml.replace("<severity>ERROR</severity>",
				"<severity>WARNING</severity>");
		StringBuffer sb = new StringBuffer();
		sb.append("\t\tStringBuffer sb = new StringBuffer();\n");
		String[] ss = xml.split("\\n");
		for (String string : ss) {
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

	public static SchemaTypeSystem getSchemaTypeSystem(final File f,
			final EntityResolver entityResolver) {
		SchemaTypeSystem sts = null;
		ArrayList<XmlObject> parsedMessages = new ArrayList<XmlObject>();
		XmlOptions xopt = new XmlOptions();
		xopt.setLoadLineNumbers();
		xopt.setLoadMessageDigest();
		XmlOptions compileOptions = new XmlOptions();
		compileOptions.setCompileDownloadUrls();
		compileOptions.setEntityResolver(entityResolver);

		try {
			parsedMessages.add(XmlObject.Factory.parse(f, xopt));
			XmlObject[] schemas = parsedMessages
					.toArray(new XmlObject[parsedMessages.size()]);
			sts = XmlBeans.compileXsd(schemas, XmlBeans.getBuiltinTypeSystem(),
					compileOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sts;
	}

	private static XsdContainer getXsdContainer(final File f,
			final File baseDirectory) throws IOException {
		XsdContainer xsdContainer = null;
		String text = Util.readFile(f);
		String targetNameSpace = null;
		String packageName = null;
		int index0 = text.indexOf("targetNamespace=\"");
		if (index0 > 0) {
			index0 += "targetNamespace=\"".length();
			int index1 = text.substring(index0).indexOf("\"");
			targetNameSpace = text.substring(index0, index0 + index1);
		}
		index0 = text.indexOf("schemaBindings");
		if (index0 > 0) {
			index0 = text.indexOf("package");
			if (index0 > 0) {
				index0 = text.indexOf("name=\"");
				if (index0 > 0) {
					index0 += "name=\"".length();
					int index1 = text.substring(index0).indexOf("\"");
					packageName = text.substring(index0, index0 + index1);
				}
			}
		}
		TreeMap<String, String> imports = new TreeMap<String, String>();
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
				if (namespace != null & location != null) {
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

		if (targetNameSpace != null && packageName != null) {
			xsdContainer = new XsdContainer(f, baseDirectory, packageName,
					targetNameSpace, imports);
		}
		return xsdContainer;
	}

	public static Map<String, XsdContainer> getXsdContainers(
			final File baseDirectory) {
		return setupXsdContainers(getXsdFiles(baseDirectory), baseDirectory);
	}

	private static Map<String, XsdContainer> setupXsdContainers(
			final List<File> xsdFiles, final File baseDirectory) {
		TreeMap<String, XsdContainer> xsdContainerMap = new TreeMap<String, XsdContainer>();
		for (File f : xsdFiles) {
			XsdContainer xsdContainer;
			try {
				xsdContainer = getXsdContainer(f, baseDirectory);
				if (xsdContainer != null
						&& xsdContainer.getTargetNamespace() != null) {
					if (xsdContainerMap
							.containsKey(xsdContainer.getTargetNamespace())) {
						throw new RuntimeException(
								new StringBuffer(256)
										.append("Target namespace \"")
										.append(xsdContainer
												.getTargetNamespace())
								.append("\" defined in file ")
								.append(xsdContainerMap
										.get(xsdContainer.getTargetNamespace())
										.getFile().getAbsolutePath())
								.append(" and ")
								.append(xsdContainer.getFile()
										.getAbsolutePath()).append("!")
								.toString());
					} else {
						xsdContainerMap.put(xsdContainer.getTargetNamespace(),
								xsdContainer);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (XsdContainer container : xsdContainerMap.values()) {
			setupXsdContainerTotalImports(container, xsdContainerMap);
		}
		return xsdContainerMap;
	}

	private static void setupXsdContainerTotalImports(
			final XsdContainer container, final Map<String, XsdContainer> map) {
		XsdContainer child;
		for (String importedTargetNamespace : container
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

	public static List<File> getXsdFiles(final File baseDirectory) {
		List<File> xsdFiles = new ArrayList<File>();
		scanForXsds(baseDirectory, xsdFiles);
		return xsdFiles;
	}

	public static boolean isMessagePackageName(final String packageName,
			final String... messagePackageNameSuffixes) {
		boolean isMessagePackageName = false;
		if (packageName != null) {
			TreeSet<String> packages = new TreeSet<String>();
			if (messagePackageNameSuffixes != null) {
				for (String messagePackageNameSuffix : messagePackageNameSuffixes) {
					if (messagePackageNameSuffix != null
							&& messagePackageNameSuffix.trim().length() > 0) {
						String[] suffixes = messagePackageNameSuffix
								.replaceAll(",", " ").split(" ");
						for (String suffix : suffixes) {
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
			for (String string : packages) {
				if (packageName.contains(string)) {
					isMessagePackageName = true;
					break;
				}
			}
		}
		return isMessagePackageName;
	}

	public static Map<String, String> getNotImportedModels(
			final Map<String, XsdContainer> xsds,
			final String messagePackageNameSuffix) {
		TreeMap<String, String> notImportedModels = new TreeMap<String, String>();
		for (Entry<String, XsdContainer> xx : xsds.entrySet()) {
			if (!isMessagePackageName(xx.getValue().getPackageName(),
					messagePackageNameSuffix)) {
				notImportedModels.put(xx.getKey(),
						xx.getValue().getFile().getAbsolutePath());
			}
		}
		for (Entry<String, XsdContainer> xx : xsds.entrySet()) {
			for (String imp : xx.getValue().getImportedTargetNamespaces()) {
				// System.out.println("Remove imported " + imp +
				// ". Imported in "
				// + xx.getKey());
				notImportedModels.remove(imp);

			}
		}
		return notImportedModels;
	}

	public static Map<String, String> getNotImportedModels(final XsdsUtil xsds,
			final String messagePackageNameSuffix) {
		TreeMap<String, String> notImportedModels = new TreeMap<String, String>();
		for (Entry<String, XsdContainer> xx : xsds.getXsdContainerMap()
				.entrySet()) {
			if (!isMessagePackageName(xx.getValue().getPackageName(),
					messagePackageNameSuffix)) {
				notImportedModels.put(xx.getKey(),
						xx.getValue().getFile().getAbsolutePath());
			}
		}
		for (Entry<String, XsdContainer> xx : xsds.getXsdContainerMap()
				.entrySet()) {
			for (String imp : xx.getValue().getImportedTargetNamespaces()) {
				// System.out.println("Remove imported " + imp +
				// ". Imported in "
				// + xx.getKey());
				notImportedModels.remove(imp);

			}
		}
		return notImportedModels;
	}

	private static Map<String, String> testGetImportedModels(
			final XsdsUtil xsds, final String messagePackageNameSuffix) {
		TreeMap<String, String> importedModels = new TreeMap<String, String>();
		for (Entry<String, XsdContainer> xx : xsds.getXsdContainerMap()
				.entrySet()) {
			if (isMessagePackageName(xx.getValue().getPackageName(),
					messagePackageNameSuffix)) {
				for (String imp : xx.getValue().getImportedTargetNamespaces()) {
					importedModels.put(xx.getKey(),
							xx.getValue().getFile().getAbsolutePath());
				}
			}
		}
		return importedModels;
	}

	private static void testGetServiceIdTree(final XsdsUtil xsds) {
		ServiceIdEntry entry;
		for (String serviceId : ServiceIdRegistry.getAllServiceIds()) {
			entry = ServiceIdRegistry.getServiceIdEntry(serviceId);
			testGetServiceIdTree(entry, 1);
		}
		for (String serviceId : ServiceIdRegistry.getAllServiceIds()) {
			entry = ServiceIdRegistry.getServiceIdEntry(serviceId);
			System.out.println(entry.getServiceId());
			System.out.println("\t" + entry.getTotalServiceIdImports());
		}
		System.out.println(ServiceIdRegistry.isValidServiceId("directory",
				"directory.v20"));
		System.out.println(
				ServiceIdRegistry.isValidServiceId("common", "directory.v20"));
		System.out.println(ServiceIdRegistry.isValidServiceId("satellite",
				"directory.v20"));
	}

	/** The hierarchy. */
	private static void testGetServiceIdTree(final ServiceIdEntry entry,
			final int hierarchy) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hierarchy; i++) {
			sb.append("\t");
		}
		System.out.println(sb.toString() + entry.getServiceId() + " "
				+ entry.getPackageName() + " " + entry.getTargetNamespace());
		for (ServiceIdEntry imported : entry.getImportedServiceEntries()) {
			testGetServiceIdTree(imported, hierarchy + 1);
		}
	}

	private static void testPrintComplexTypeFlowsContent(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		StringBuffer sb = new StringBuffer(128);
		boolean used;
		ComplexType flowOutput;
		for (ComplexType ct : xsds.getComplexTypes()) {
			used = false;
			// sb.setLength(0);

			if (ct.isFlowInputType()) {
				flowOutput = findResponse(ct, xsds.getComplexTypes(), xsds);
				System.out.print(
						ct.getClassName() + "  " + ct.isRequestType() + " ");
				if (flowOutput != null) {
					System.out.println(flowOutput.getClassName());
					for (ComplexTypeChild child : ct.getChildren()) {
						System.out.println("\t" + child.getChildName() + " "
								+ child.getComplexType().getClassName());
					}
					System.out.println("->" + flowOutput.getClassName());
					for (ComplexTypeChild child : flowOutput.getChildren()) {
						System.out.println("\t" + child.getChildName() + " "
								+ child.getComplexType().getClassName());
					}
				} else {
					System.out.println(" xxxxx not flow output found.");
					for (ComplexTypeChild child : ct.getChildren()) {
						System.out.println("\t" + child.getChildName() + " "
								+ child.getComplexType().getClassName());
					}
				}
			}
			// System.out.println(sb);
		}
	}

	private static void testPrintComplexTypeContent(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		StringBuffer sb = new StringBuffer(128);
		boolean used;
		for (ComplexType ct : xsds.getComplexTypes()) {
			used = false;
			sb.setLength(0);
			sb.append(ct.getClassName()).append(" ")
					.append(ct.getType().getName());
			sb.append(" ").append(ct.isRequestType()).append("/")
					.append(ct.isResponseType());
			if (ct.getType().getName() != null && !notImportedModels
					.containsKey(ct.getType().getName().getNamespaceURI())) {
				sb.insert(0, "ComplexType imported     : ");
				used = true;
			} else {
				sb.insert(0, "ComplexType not Imported : ");
			}
			if (!onlyUsed || onlyUsed && used) {
				System.out.println(sb);
			}
		}
	}

	private static void testPrintComplexTypeChildContent(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		StringBuffer sb = new StringBuffer(128);
		boolean used = false;
		for (ComplexType ct : xsds.getComplexTypes()) {
			used = false;
			if (ct.getType().getName() != null && !notImportedModels
					.containsKey(ct.getType().getName().getNamespaceURI())) {
				used = true;
			}
			List<ComplexTypeChild> children = ct.getChildren();
			if (children.size() > 0) {
				sb.setLength(0);
				sb.append(ct.getClassName()).append(" ")
						.append(ct.getType().getName()).append("\n");
				for (ComplexTypeChild child : children) {
					sb.append("\t");
					sb.append(child.getChildName()).append(" ");
					sb.append(
							child.getComplexType().getClassNameFullQualified())
							.append(" ");
					sb.append(child.getGetterName()).append(" ");
					sb.append(child.getSetterName()).append(" ");
					if (child.getDefaultValue() != null) {
						sb.append(" DEFAULT ");
						sb.append(child.getDefaultValue().getStringValue())
								.append(" ");
					}
					sb.append("\n");
				}
			}
			if (!onlyUsed || onlyUsed && used) {
				System.out.println(sb);
			}
		}
	}

	private static void testPrintComplexTypeResponseSample(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		StringBuffer sb = new StringBuffer(128);
		ElementType response;
		boolean used;

		String xml;
		Object mock = null;
		String op = "GetContact";
		// op = "TransferSatellite";
		for (ComplexType ct : xsds.getComplexTypes()) {
			if (ct.getClassName().equals(op + "ResponseType")) {
				xml = getSampleCodeing(ct.getType(), op + "Response");
				System.out.println(ct.getPackageName());
				System.out.println(xml);

				try {
					JAXBContext jaxbContext = JAXBContext
							.newInstance(ct.getPackageName());
					Unmarshaller unmarshaller = jaxbContext
							.createUnmarshaller();
					JAXBElement<?> jaxb = (JAXBElement<?>) unmarshaller
							.unmarshal(new StringReader(xml));
					if (jaxb != null) {
						mock = jaxb.getValue();
					}
				} catch (Exception e) {
					e.printStackTrace();
					mock = null;
				}
				System.out.println(mock);
			}
		}
	}

	private static void testPrintElementTypeRequestResponse(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		StringBuffer sb = new StringBuffer(128);
		ElementType response;
		boolean used;
		for (ElementType et : xsds.getElementTypes()) {
			used = false;
			if (et.isRequest()) {
				sb.setLength(0);
				response = XsdsUtil.findResponse(et, xsds.getElementTypes(),
						xsds);
				sb.append(et.getClassNameObject());
				if (response != null) {
					sb.insert(0, "[+] : ");
					sb.append(" / ").append(response.getClassNameObject());
				} else {
					sb.insert(0, "[-] : ");
				}
				sb.append("  ");
				sb.append(et.getPackageName());
				sb.append("  ");
				sb.append(et.getTargetNamespace());

				if (et.getTargetNamespace() != null && !notImportedModels
						.containsKey(et.getTargetNamespace())) {
					sb.insert(0, "ElementType imported     ");
					used = true;
				} else {
					sb.insert(0, "ElementType not Imported ");
				}
				if (!onlyUsed || onlyUsed && used) {
					System.out.println(sb);
				}
			}
		}
	}

	private static void testPrintComplexTypeRequestResponse(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		StringBuffer sb = new StringBuffer(128);
		ComplexType response;
		boolean used;
		for (ComplexType ct : xsds.getComplexTypes()) {
			used = false;
			if (ct.isRequestType()) {
				sb.setLength(0);
				response = XsdsUtil.findResponse(ct, xsds.getComplexTypes(),
						xsds);
				sb.append(ct.getClassName());
				if (response != null) {
					sb.insert(0, "[+] : ");
					sb.append(" / ").append(response.getClassName());
				} else {
					sb.insert(0, "[-] : ");
				}
				sb.append("  ");
				sb.append(ct.getPackageName());
				sb.append("  ");
				sb.append(ct.getTargetNamespace());

				if (ct.getTargetNamespace() != null && !notImportedModels
						.containsKey(ct.getTargetNamespace())) {
					sb.insert(0, "ComplexType imported     ");
					used = true;
				} else {
					sb.insert(0, "ComplexType not Imported ");
				}
				if (!onlyUsed || onlyUsed && used) {
					System.out.println(sb);
				}
			}
		}
	}

	private static void testVerifyModel(final XsdsUtil xsds,
			final Map<String, String> notImportedModels) {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> model : notImportedModels.entrySet()) {
			System.out.println("Model " + model.getKey()
					+ " is not used (file: " + model.getValue() + ")");
			sb.append("\t<import namespace=\"");
			sb.append(model.getKey());
			sb.append("\" schemaLocation=\"http://www.ses.com/model/");
			sb.append(model.getValue()
					.substring(model.getValue().indexOf("\\model\\") + 7,
							model.getValue().length())
					.replaceAll("\\\\", "/").replaceAll("//", "/"));
			sb.append("\" />\n");
		}
		System.out.println(sb.toString());
		for (XsdContainer xx : xsds.getXsdContainerMap().values()) {
			for (String importedTargetNamespace : xx
					.getImportedTargetNamespaces()) {
				XsdContainer imported = xsds
						.getXsdContainerMap(importedTargetNamespace);
				if (imported == null) {
					System.out.println(
							"Target namespace " + importedTargetNamespace
									+ " is not defined but imported by "
									+ xx.getFile() + "!");
				} else {
					String fileName = xx
							.getImportedSchemaLocation(importedTargetNamespace);
					if (fileName.indexOf('/') > 0) {
						fileName = fileName.substring(
								fileName.lastIndexOf('/') + 1,
								fileName.length());
					}
					if (!imported.getFile().getAbsolutePath()
							.endsWith(fileName)) {
						System.out.println(
								"File " + xx.getFile().getAbsolutePath()
										+ " uses " + fileName + " instead of "
										+ imported.getFile().getAbsolutePath());
					}
				}
			}
		}
	}

	private static void testPrintNotUsedModels(final XsdsUtil xsds,
			final Map<String, String> notImportedModels) {
		for (Entry<String, String> model : notImportedModels.entrySet()) {
			System.out.println("Model " + model.getKey()
					+ " is not used (file: " + model.getValue() + ")");
		}
	}

	private static void testXsdContainers(final File dif) {
		Map<String, XsdContainer> containers = getXsdContainers(dif);
		for (Entry<String, XsdContainer> entry : containers.entrySet()) {
			System.out.println(entry.getKey());
			File f = entry.getValue().getFile();
			System.out.println("\tlocation " + f.getAbsolutePath().substring(
					dif.getAbsolutePath().length(),
					f.getAbsolutePath().length()));
			System.out
					.println("\tpackage " + entry.getValue().getPackageName());
			System.out.println("\timports "
					+ entry.getValue().getTotalImportedTargetNamespaces());
			for (String imported : entry.getValue()
					.getImportedTargetNamespaces()) {
				// System.out.println("\t\timports " + imported);
			}
		}
	}

	public static void printCatalog(final XsdsUtil xsds,
			final String pathPrefix) {
		for (ComplexType ct : xsds.getComplexTypes()) {
			if (ct.getClassName()
					.equals("MonicsSatelliteInclinedOrbitFlagMappingType")) {
				System.out.println(ct.getType().getAnnotation());
				SchemaAnnotation annotation = ct.getType().getAnnotation();
				XmlObject[] app = annotation.getUserInformation();
				if (app != null && app.length > 0) {
					XmlObject u = app[0];
					System.out.println(app.length);
					System.out.println(u.getDomNode());
					Element e = (Element) u.getDomNode();
					NodeList nl = e.getChildNodes();
					if (nl.getLength() > 0) {
						System.out.println(nl.item(0));
						System.out.println(nl.item(0).getNodeValue());
					}
					System.out.println(
							"  getUserInformation: " + app[0].getDomNode()
									+ "=\"" + app[0].getClass() + "\"");
				}
			}
			if (xsds.getXsdContainerMap(ct.getTargetNamespace()) == null) {
				System.out.println(ct.getClassNameFullQualified() + " "
						+ ct.getTargetNamespace() + " XXXXX");
			} else {
				System.out
						.println(ct.getClassNameFullQualified() + " "
								+ xsds.getXsdContainerMap(
										ct.getTargetNamespace()).getFile()
								.getAbsolutePath().replace(pathPrefix, ""));
			}
		}
	}

	public static void main(final String[] args) {
		String xsdPath;
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain\\mapping\\target\\model";
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";
		// xsdPath =
		// "C:\\xnb\\dev\\9.1\\bus.app.monics-2.0\\monics-webapp\\target\\model";
		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "msg mapping flow";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = new XsdsUtil(dif, "a.b.c.bus", messagePackageNameSuffix,
				"delta");
		System.out.println(Util.getDuration(System.currentTimeMillis() - start)
				+ " needed for new XsdsUtil(...)");
		XsdsUtil config = xsds;
		Map<String, String> notImportedModels = getNotImportedModels(xsds,
				messagePackageNameSuffix);
		Map<String, String> importedModels = testGetImportedModels(xsds,
				messagePackageNameSuffix);
		testVerifyModel(xsds, notImportedModels);

		// printCatalog(xsds, xsdPath);
		// testValidServiceId(xsds);
		// testGetServiceIdTree(xsds);
		// testFlowInputTypes(xsds);
		testXsdContainers(dif);
		// testPrintComplexTypeContent(xsds, notImportedModels, true);
		// testPrintElementTypeRequestResponse(xsds, notImportedModels, true);
		// testPrintComplexTypeRequestResponse(xsds, notImportedModels, true);
		// testPrintComplexTypeFlowsContent(xsds, notImportedModels, true);
		testPrintComplexTypeChildContent(xsds, notImportedModels, true);

		// testPrintComplexTypeResponseSample(xsds, notImportedModels, true);
		// testPrintComplexTypeChildContent

		System.out
				.println(Util.getDuration(System.currentTimeMillis() - start));
		System.exit(0);
		SchemaType t;
		for (ComplexType ct : xsds.getComplexTypes()) {
			// System.out.println(ct.getClassName() + " "
			// + ct.getType().getElementProperties().length);
			for (SchemaProperty sp : ct.getType().getElementProperties()) {
				t = sp.getType();
				if (t != null && t.getName() != null) {
					// System.out.println(sp.getName().getLocalPart());
					System.out.println(sp.getMinOccurs() + " "
							+ t.getName().getLocalPart() + " "
							+ getBuildInBaseType(t) + "  "
							+ getBuildInBaseTypeClass(t).getSimpleName());
				} else {
					System.out.println(sp.getName() + " " + t);
					// System.exit(0);
					// System.out.println(sp.getName().getLocalPart());
				}
			}
		}
	}

	/**
	 * @param xsds
	 */
	private static void testValidServiceId(final XsdsUtil xsds) {
		String serviceId;
		serviceId = "directory.v20";
		serviceId = "appcontrolling, monitoring, busappmonics";
		for (ElementType et : xsds.getElementTypes()) {
			if (et.isRequest()) {
				System.out.println(et.getServiceId() + " " + ServiceIdRegistry
						.isValidServiceId(et.getServiceId(), serviceId));
			}
		}
	}

	/**
	 * @param xsds
	 */
	private static void testFlowInputTypes(final XsdsUtil config) {
		for (ComplexType ct : config.getComplexTypes()) {
			if (ct.isFlowInputType()) {
				System.out.println(ct.getPackageName() + " " + ct.getClassName()
						+ " " + ct.getClassNameFullQualified());
			}
		}
	}

	public static boolean isRequired(final SchemaProperty p) {
		if (p.getMinOccurs() != null && p.getMinOccurs().intValue() < 1) {
			return false;
		}
		return true;
	}

	public static boolean isList(final SchemaProperty p) {
		if (p.getMaxOccurs() != null && p.getMaxOccurs().intValue() > 1) {
			return true;
		}
		return false;
	}

	public static String getBuildInBaseTypeFormat(final SchemaType t) {
		String s = "";
		SchemaType base = getBuildInBaseType(t);
		if (base.getName().getLocalPart().equals("dateTime")) {
			s = "@DateTimeFormat(pattern = \"yyyyMMdd\")";
		} else if (base.getName().getLocalPart().equals("time")) {
			s = "@DateTimeFormat(pattern = \"yyyy-MM-dd\")";
		} else if (base.getName().getLocalPart().equals("date")) {
			s = "@DateTimeFormat(pattern = \"yyyy-MM-dd\")";
		}
		return s;
	}

	public static Class<?> getBuildInBaseTypeClass(
			final SchemaType schemaType) {
		Class<?> c = getBuildInBaseTypeClassInternal(schemaType);
		if (c == null) {
			SchemaType baseSchemaType = getBuildInBaseType(schemaType);
			c = getBuildInBaseTypeClassInternal(baseSchemaType);
		}
		if (c == null) {
			c = Object.class;
		}
		return c;
	}

	private static Class<?> getBuildInBaseTypeClassInternal(
			final SchemaType schemaType) {
		Class<?> c = null;
		if (schemaType != null && schemaType.getName() != null) {
			if (schemaType.getName().getLocalPart().equals("string")) {
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
			}
		}
		return c;
	}

	public static SchemaType getBuildInBaseType(final SchemaType t) {
		SchemaType base = t.isBuiltinType() ? t : t.getBaseType();
		// System.out.println("++" + base);
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
		// System.out.println("--" + base);
		return base;
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
		File[] cs = f.listFiles();
		if (cs != null && cs.length > 0) {
			for (File c : cs) {
				if (c.isDirectory()) {
					scanForXsds(c, xsdFiles);
				} else if (c.getName().endsWith(".xsd")) {
					xsdFiles.add(c);
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
	private final Map<QName, ComplexType> complexTypeMap = new HashMap<QName, ComplexType>();

	/** The {@link TreeSet} of {@link ComplexType}. */
	private final TreeSet<ComplexType> complexTypes = new TreeSet<ComplexType>(
			new Comparator<ComplexType>() {
				@Override
				public int compare(final ComplexType o1, final ComplexType o2) {
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
				}
			});

	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	private final String deltaPackageNameSuffix;

	/** The {@link TreeSet} of {@link ElementType}. */
	private final TreeSet<ElementType> elementTypes = new TreeSet<ElementType>(
			new Comparator<ElementType>() {
				@Override
				public int compare(final ElementType o1, final ElementType o2) {
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
				}
			});

	/** The {@link EntityResolver} for the local xsds. */
	private final EntityResolver entityResolver = new EntityResolver() {
		@Override
		public InputSource resolveEntity(final String publicId,
				final String systemId) throws SAXException, IOException {
			if (XsdsUtil.this.getXsdContainerMap().containsKey(publicId)) {
				InputSource is = new InputSource(
						new FileInputStream(XsdsUtil.this.getXsdContainerMap()
								.get(publicId).getFile()));
				return is;
			} else {
				return null;
			}
		}
	};
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

	public XsdsUtil(final File baseDirectory, final String basePackageName,
			final String messagePackageNameSuffix,
			final String deltaPackageNameSuffix) {
		this(baseDirectory, basePackageName, messagePackageNameSuffix,
				deltaPackageNameSuffix, null, null);
	}

	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(XsdsUtil.class);

	public XsdsUtil(final File baseDirectory, final String basePackageName,
			final String messagePackageNameSuffix,
			final String deltaPackageNameSuffix,
			final String serviceRequestSuffix,
			final String serviceResponseSuffix) {
		long start = System.currentTimeMillis();
		this.logger.debug("XsdsUtil");
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
		this.xsdFiles = getXsdFiles(this.baseDirectory);
		this.logger.debug("{} Got xsd files",
				Util.getDuration(System.currentTimeMillis() - start));
		this.xsdContainerMap = setupXsdContainers(this.xsdFiles, baseDirectory);
		this.logger.debug("{} Got xsd containers",
				Util.getDuration(System.currentTimeMillis() - start));
		int i = 0;
		for (File f : this.xsdFiles) {
			i++;
			long startf = System.currentTimeMillis();
			SchemaTypeSystem sts = getSchemaTypeSystem(f, this.entityResolver);
			for (SchemaGlobalElement elem : sts.globalElements()) {
				ElementType elementType = new ElementType(elem, this);
				this.elementTypes.add(elementType);
			}
			for (SchemaType type : sts.globalTypes()) {
				if (!this.complexTypeMap.containsKey(type.getName())) {
					ComplexType ct = new ComplexType(type, this);
					this.complexTypeMap.put(ct.getType().getName(), ct);
					this.setupComplexTypes(ct, this.complexTypes);
				}
			}
			this.logger.debug("{} Got elements and complex type of file {}",
					Util.getDuration(System.currentTimeMillis() - startf),
					f.getAbsolutePath());
		}
		this.logger.debug("{} Got elements and complex type of {} files",
				Util.getDuration(System.currentTimeMillis() - start), i);
		ServiceIdRegistry.setupServiceIdTree(this);
		this.logger.info("Setup serviceIds: {}",
				ServiceIdRegistry.getAllServiceIds());
		for (ComplexType ct : this.complexTypes) {
			ct.initChildren(this);
			if (ct.getType() != null) {
				this.complexTypeMap.put(ct.getType().getName(), ct);
			}
		}
		this.logger.debug("{} Init complex type children",
				Util.getDuration(System.currentTimeMillis() - start));
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
	 * @return the complexTypes
	 */
	public TreeSet<ComplexType> getComplexTypes() {
		return this.complexTypes;
	}

	/**
	 * @return the complexType
	 */
	public ComplexType getComplexType(final QName name) {
		return this.complexTypeMap.get(name);
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
		XsdContainer xc = this.xsdContainerMap.get(targetNamespace);
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
	public XsdContainer getXsdContainerMap(final String targetNamespace) {
		return this.xsdContainerMap.get(targetNamespace);
	}

	/**
	 * @return the xsdFiles
	 */
	public List<File> getXsdFiles() {
		return this.xsdFiles;
	}

	private void setupComplexTypes(final ComplexType ct,
			final TreeSet<ComplexType> complexTypes) {
		this.complexTypes.add(ct);
		for (ComplexType ctx : ct.getInnerTypeDefs()) {
			this.setupComplexTypes(ctx, complexTypes);
		}
	}

	public static String getXmlObjectAsSetterParam(final String objAndSetter,
			final XmlAnySimpleType obj) {
		StringBuffer sb = new StringBuffer(32);
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

	/**
	 * @param qName
	 * @return
	 */
	public static Class<?> getBuildInJavaClass(final QName qName) {
		Class<?> javaType = null;
		if (qName != null && QNAME_BASE_SCHEMA_NAMESPACE_URI
				.equals(qName.getNamespaceURI())) {
			String typeName = qName.getLocalPart();
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

}
