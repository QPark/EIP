package com.qpark.maven.xmlbeans;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.qpark.maven.Util;

@SuppressWarnings("unused")
public class XsdUtilTest {
	public static void main(final String[] args) {
		String xsdPath;
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain\\mapping\\target\\model";
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\com.ses.domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		// xsdPath =
		// "C:\\xnb\\dev\\9.1\\bus.app.monics-2.0\\monics-webapp\\target\\model";
		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "msg mapping flow";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = XsdsUtil.getInstance(dif, "a.b.c.bus",
				messagePackageNameSuffix, "delta");
		System.out.println(Util.getDuration(System.currentTimeMillis() - start)
				+ " needed for new XsdsUtil(...)");
		Map<String, String> notImportedModels = XsdsUtil
				.getNotImportedModels(xsds, messagePackageNameSuffix);
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
		// testPrintComplexTypeChildContent(xsds, notImportedModels, true);

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
					System.out.println(
							sp.getMinOccurs() + " " + t.getName().getLocalPart()
									+ " " + XsdsUtil.getBuildInBaseType(t)
									+ "  " + XsdsUtil.getBuildInBaseTypeClass(t)
											.getSimpleName());
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
	private static void testFlowInputTypes(final XsdsUtil config) {
		for (ComplexType ct : config.getComplexTypes()) {
			if (ct.isFlowInputType()) {
				System.out.println(ct.getPackageName() + " " + ct.getClassName()
						+ " " + ct.getClassNameFullQualified());
			}
		}
	}

	private static Map<String, String> testGetImportedModels(
			final XsdsUtil xsds, final String messagePackageNameSuffix) {
		TreeMap<String, String> importedModels = new TreeMap<String, String>();
		for (Entry<String, XsdContainer> xx : xsds.getXsdContainerMap()
				.entrySet()) {
			if (XsdsUtil.isMessagePackageName(xx.getValue().getPackageName(),
					messagePackageNameSuffix)) {
				for (String imp : xx.getValue().getImportedTargetNamespaces()) {
					importedModels.put(xx.getKey(),
							xx.getValue().getFile().getAbsolutePath());
				}
			}
		}
		return importedModels;
	}

	/** The hierarchy. */
	private static void testGetServiceIdTree(final ServiceIdEntry entry,
			final int hierarchy) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hierarchy; i++) {
			sb.append("\t");
		}
		System.out.println(sb.toString() + entry.getServiceId() + " "
				+ entry.getPackageName() + " "
				+ entry.getAnnotationDocumentation() + " "
				+ entry.getTargetNamespace());
		for (ServiceIdEntry imported : entry.getImportedServiceEntries()) {
			testGetServiceIdTree(imported, hierarchy + 1);
		}
	}

	private static void testGetServiceIdTree(final XsdsUtil xsds) {
		ServiceIdEntry entry;
		for (String serviceId : xsds.getServiceIdRegistry()
				.getAllServiceIds()) {
			entry = xsds.getServiceIdRegistry().getServiceIdEntry(serviceId);
			testGetServiceIdTree(entry, 1);
		}
		for (String serviceId : xsds.getServiceIdRegistry()
				.getAllServiceIds()) {
			entry = xsds.getServiceIdRegistry().getServiceIdEntry(serviceId);
			System.out.println(entry.getServiceId());
			System.out.println("\t" + entry.getAnnotationDocumentation());
			System.out.println("\t" + entry.getTotalServiceIdImports());
		}
		System.out.println(xsds.getServiceIdRegistry()
				.isValidServiceId("directory", "directory.v20"));
		System.out.println(xsds.getServiceIdRegistry()
				.isValidServiceId("common", "directory.v20"));
		System.out.println(xsds.getServiceIdRegistry()
				.isValidServiceId("satellite", "directory.v20"));
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

	private static void testPrintComplexTypeFlowsContent(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		ComplexType flowOutput;
		for (ComplexType ct : xsds.getComplexTypes()) {
			if (ct.isFlowInputType()) {
				flowOutput = XsdsUtil.findResponse(ct, xsds.getComplexTypes(),
						xsds);
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

	private static void testPrintComplexTypeResponseSample(final XsdsUtil xsds,
			final Map<String, String> notImportedModels,
			final boolean onlyUsed) {
		String xml;
		Object mock = null;
		String op = "GetContact";
		// op = "TransferSatellite";
		for (ComplexType ct : xsds.getComplexTypes()) {
			if (ct.getClassName().equals(op + "ResponseType")) {
				xml = XsdsUtil.getSampleCodeing(ct.getType(), op + "Response");
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
				sb.append(et.getAnnotationDocumentation());
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

	private static void testPrintNotUsedModels(final XsdsUtil xsds,
			final Map<String, String> notImportedModels) {
		for (Entry<String, String> model : notImportedModels.entrySet()) {
			System.out.println("Model " + model.getKey()
					+ " is not used (file: " + model.getValue() + ")");
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
				System.out.println(et.getServiceId() + " "
						+ xsds.getServiceIdRegistry().isValidServiceId(
								et.getServiceId(), serviceId));
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
						.getXsdContainer(importedTargetNamespace);
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
			for (String warning : xx.getWarnings()) {
				System.out.println("WARNING: " + xx.getTargetNamespace() + ":\t"
						+ warning);
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
			if (xsds.getXsdContainer(ct.getTargetNamespace()) == null) {
				System.out.println(ct.getClassNameFullQualified() + " "
						+ ct.getTargetNamespace() + " XXXXX");
			} else {
				System.out
						.println(
								ct.getClassNameFullQualified() + " "
										+ xsds.getXsdContainer(
												ct.getTargetNamespace())
										.getFile().getAbsolutePath()
										.replace(pathPrefix, ""));
			}
		}
	}

	private static void testXsdContainers(final File dif) {
		Map<String, XsdContainer> containers = XsdsUtil.getXsdContainers(dif);
		for (Entry<String, XsdContainer> entry : containers.entrySet()) {
			System.out.println(entry.getKey());
			File f = entry.getValue().getFile();
			System.out.println("\tlocation " + f.getAbsolutePath().substring(
					dif.getAbsolutePath().length(),
					f.getAbsolutePath().length()));
			System.out
					.println("\tpackage " + entry.getValue().getPackageName());
			System.out.println("\tdocumentation "
					+ entry.getValue().getAnnotationDocumentation());
			System.out.println("\timports "
					+ entry.getValue().getTotalImportedTargetNamespaces());
			for (String imported : entry.getValue()
					.getImportedTargetNamespaces()) {
				// System.out.println("\t\timports " + imported);
			}
		}
	}
}
