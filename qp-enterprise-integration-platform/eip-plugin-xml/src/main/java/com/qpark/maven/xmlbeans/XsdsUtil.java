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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class XsdsUtil {
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
			final TreeSet<ElementType> elementTypes, final XsdsUtil config) {
		ElementType response = null;
		int index = request.getClassNameObject().lastIndexOf(
				config.getServiceRequestSuffix());
		if (index > 0) {
			String responseName = new StringBuffer()
					.append(request.getClassNameObject().substring(0, index))
					.append(config.getServiceResponseSuffix()).toString();
			for (ElementType elementType : elementTypes) {
				if (elementType.getClassNameObject().equals(responseName)
						&& elementType.getPackageName().equals(
								request.getPackageName())) {
					response = elementType;
					break;
				}
			}
		}
		return response;
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
		if (type.getName() != null && type.getName().getNamespaceURI() != null) {
			String prefix = findNamespace(xml, type.getName().getNamespaceURI());
			if (prefix.length() > 0) {
				xml = xml.replace(
						"xml-fragment",
						new StringBuffer(32).append(prefix).append(":")
								.append(elementName).toString());
			} else {
				xml = xml.replace(
						"<xml-fragment",
						new StringBuffer(32).append("<").append(elementName)
								.append(" xmlns=\"")
								.append(type.getName().getNamespaceURI())
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

	private static XsdContainer getXsdContainer(final File f)
			throws IOException {
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
		List<String> importedTargetNamespaces = new ArrayList<String>();
		index0 = text.indexOf("<import");
		while (index0 > 0) {
			int index1 = text.indexOf('>', index0);
			if (index1 > 0) {
				index0 = text.indexOf("namespace=\"", index0);
				if (index0 > 0) {
					index0 += "namespace=\"".length();
					index1 = text.substring(index0).indexOf("\"");
					importedTargetNamespaces.add(text.substring(index0, index0
							+ index1));
				}
				index0 = text.indexOf("<import", index0 + 1);
			} else {
				index0 = 0;
			}
		}
		if (targetNameSpace != null && packageName != null) {
			xsdContainer = new XsdContainer(f, packageName, targetNameSpace,
					importedTargetNamespaces);
		}
		return xsdContainer;
	}

	public static TreeMap<String, XsdContainer> getXsdContainers(
			final File baseDirectory) {
		return getXsdContainers(getXsdFiles(baseDirectory));
	}

	private static TreeMap<String, XsdContainer> getXsdContainers(
			final List<File> xsdFiles) {
		TreeMap<String, XsdContainer> xsdContainerMap = new TreeMap<String, XsdContainer>();
		for (File f : xsdFiles) {
			XsdContainer xsdContainer;
			try {
				xsdContainer = getXsdContainer(f);
				if (xsdContainer != null
						&& xsdContainer.getTargetNamespace() != null) {
					xsdContainerMap.put(xsdContainer.getTargetNamespace(),
							xsdContainer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return xsdContainerMap;
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
								packages.add(new StringBuffer(
										suffix.length() + 1).append(".")
										.append(suffix.trim()).toString());
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

	public static void main(final String[] args) {
		File dif = new File("C:\\xnb\\dev\\x\\target\\model");
		XsdsUtil xsds = new XsdsUtil(dif, "a.b.c.bus", "msg", "delta");

		TreeMap<String, String> notImportedModels = new TreeMap<String, String>();
		TreeMap<String, String> importedModels = new TreeMap<String, String>();
		for (Entry<String, XsdContainer> xx : xsds.getXsdContainerMap()
				.entrySet()) {
			if (!isMessagePackageName(xx.getValue().getPackageName(),
					"msg restmsg")) {
				notImportedModels.put(xx.getKey(), xx.getValue().getFile()
						.getAbsolutePath());
			} else {
				for (String imp : xx.getValue().getImportedTargetNamespaces()) {
					importedModels.put(xx.getKey(), xx.getValue().getFile()
							.getAbsolutePath());
				}
			}
		}
		for (Entry<String, XsdContainer> xx : xsds.getXsdContainerMap()
				.entrySet()) {
			for (String imp : xx.getValue().getImportedTargetNamespaces()) {
				System.out.println("Remove " + imp + " because of "
						+ xx.getKey());
				notImportedModels.remove(imp);

			}
		}
		for (Entry<String, String> model : notImportedModels.entrySet()) {
			System.out.println(model.getKey() + " " + model.getValue());
		}
		for (ComplexType ct : xsds.getComplexTypes()) {
			if (ct.getType().getName() != null) {
				System.out.println(!notImportedModels.containsKey(ct.getType()
						.getName().getNamespaceURI())
						+ " " + ct.getType().getName() + " via not imported");
			}
		}

		for (ComplexType ct : xsds.getComplexTypes()) {
			if (ct.getType().getName() != null) {
				System.out.println(importedModels.containsKey(ct.getType()
						.getName().getNamespaceURI())
						+ " " + ct.getType().getName() + " via  imported");
			}
		}

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
					System.exit(0);
					// System.out.println(sp.getName().getLocalPart());
				}
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

	public static Class<?> getBuildInBaseTypeClass(final SchemaType t) {
		Class<?> c = Object.class;
		SchemaType base = getBuildInBaseType(t);
		if (base.getName().getLocalPart().equals("string")) {
			c = String.class;
		} else if (base.getName().getLocalPart().equals("dateTime")) {
			c = Date.class;
		} else if (base.getName().getLocalPart().equals("time")) {
			c = Date.class;
		} else if (base.getName().getLocalPart().equals("date")) {
			c = Date.class;
		} else if (base.getName().getLocalPart().equals("boolean")) {
			c = Boolean.class;
		} else if (base.getName().getLocalPart().equals("float")) {
			c = Float.class;
		} else if (base.getName().getLocalPart().equals("decimal")) {
			c = Double.class;
		} else if (base.getName().getLocalPart().equals("integer")) {
			c = Integer.class;
		} else if (base.getName().getLocalPart().equals("double")) {
			c = Double.class;
		} else if (base.getName().getLocalPart().equals("anyURI")) {
			c = URI.class;
		} else if (base.getName().getLocalPart().equals("hexBinary")) {
			c = byte[].class;
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
				SchemaType basex = base;
				do {
					base = basex;
					basex = basex.getBaseType();
					// System.out.println("---" + basex);
				} while (basex != null
						&& !basex.getName().getLocalPart()
								.equals("anySimpleType")
						&& !basex.getName().getLocalPart().equals("integer"));
			}
		}
		// System.out.println("--" + base);
		return base;
	}

	/**
	 * Collect the xsd files recursively.
	 * @param f the file to check.
	 * @param xsdFiles the list of xsd files.
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
						return o1.getClassNameFullQualified().compareTo(
								o2.getClassNameFullQualified());
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
				InputSource is = new InputSource(new FileInputStream(
						XsdsUtil.this.getXsdContainerMap().get(publicId)
								.getFile()));
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

	/** A {@link TreeMap} containing the {@link XsdContainer}s. */
	private final TreeMap<String, XsdContainer> xsdContainerMap;

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
		this.xsdContainerMap = getXsdContainers(this.xsdFiles);
		for (File f : this.xsdFiles) {
			SchemaTypeSystem sts = getSchemaTypeSystem(f, this.entityResolver);
			for (SchemaGlobalElement elem : sts.globalElements()) {
				ElementType elementType = new ElementType(elem, this);
				this.elementTypes.add(elementType);
			}
			for (SchemaType type : sts.globalTypes()) {
				ComplexType complexType = new ComplexType(type, this);
				this.setupComplexTypes(complexType, this.complexTypes);
			}
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
	public TreeMap<String, XsdContainer> getXsdContainerMap() {
		return this.xsdContainerMap;
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
		for (ComplexType ctx : ct.getChildren()) {
			this.setupComplexTypes(ctx, complexTypes);
		}
	}
}
