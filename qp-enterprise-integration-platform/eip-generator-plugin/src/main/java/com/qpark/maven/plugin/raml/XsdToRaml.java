package com.qpark.maven.plugin.raml;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class XsdToRaml {
	private static String getPathUp(final String base) {
		StringBuffer sb = new StringBuffer();
		int x = base.split("/").length - 1;
		for (int i = 0; i < x; i++) {
			sb.append("../");
		}
		return sb.toString();
	}

	private static String getDeclarationChild(final ComplexTypeChild ctc,
			final String usage, final XsdsUtil xsds) {
		String name = ctc.getChildName();
		String optional = ctc.isOptional() ? "?" : "";
		String type = getDefinitionChild(xsds, ctc.getComplexType(), usage);
		String list = (ctc.isJavaArray() || ctc.isList()) ? "[]" : "";
		String description = "";
		if (ctc.getAnnotationDocumentationNormalised().trim().length() > 0) {
			description = String.format(" # %s", ctc
					.getAnnotationDocumentationNormalised().replace("@", "#"));
		}
		return String.format("      %s%s: %s%s%s\n", name, optional, type, list,
				description);
	}

	private static String getDefinitionChild(final XsdsUtil xsds,
			final ComplexType ct, final String usage) {
		String type = "object";
		if (ct.getTargetNamespace()
				.equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)
				|| ct.getType().isPrimitiveType()) {
			type = XsdsUtil.getBuildInRamlType(ct.getType().getName());
		} else {
			type = String
					.format("%s.%s",
							Util.capitalizePackageName(xsds
									.getPackageName(ct.getTargetNamespace())),
							ct.getQNameLocalPart().replace(".", "_"))
					.replace(String.format("%s.", usage), "")
					.replace("JavaLang.", "");
		}
		return type;
	}

	private static String getDefinition(final ComplexType ct) {
		String type = "object";
		if (Objects.isNull(ct.getBaseComplexType())
				&& ct.getType().isSimpleType()) {
			type = XsdsUtil.getBuildInRamlType(
					XsdsUtil.getBuildInBaseType(ct.getType()).getName());
		} else if (Objects.nonNull(ct.getBaseComplexType())) {
			if (ct.getBaseComplexType().getTargetNamespace()
					.equals(ct.getTargetNamespace())) {
				type = ct.getBaseComplexType().getQNameLocalPart().replace(".",
						"_");
			} else {
				type = String.format("%s.%s",
						Util.capitalizePackageName(
								ct.getBaseComplexType().getPackageName()),
						ct.getBaseComplexType().getQNameLocalPart().replace(".",
								"_"));
			}
		} else if (ct.getTargetNamespace()
				.equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)
				|| ct.getType().isPrimitiveType()) {
			type = XsdsUtil.getBuildInRamlType(ct.getType().getName());
		}
		return type;
	}

	/**
	 * @param xsds
	 * @param eipVersion
	 * @return the RAML API importing all other APIs.
	 */
	public static String getRamlApi(final boolean flattenQueryParameters,
			final XsdsUtil xsds, final String eipVersion) {
		StringBuffer sb = new StringBuffer(512);

		sb.append("#%RAML 1.0\n");
		sb.append(String.format("#EIP version %s - %s\n", eipVersion,
				Util.getGeneratedAt(new Date(), XsdToRaml.class)));
		sb.append(String.format("title: %s\n", "The API"));

		Map<String, String> uses = new TreeMap<>();
		xsds.getXsdContainerMap().values().stream()
				// .filter(xsd -> xsd.getElementType().size() > 0)
				.filter(xsd -> !xsd.getFile().getName()
						.endsWith("maven-4.0.0.xsd"))
				.forEach(xsd -> uses.put(
						Util.capitalizePackageName(xsd.getPackageName()),
						xsd.getRelativeName().replace(".xsd", ".raml")));
		if (uses.size() > 0) {
			sb.append("uses: \n");
			uses.entrySet().stream().forEach(entry -> sb.append(String
					.format("  %s: %s\n", entry.getKey(), entry.getValue())));
		}
		xsds.getXsdContainerMap().values().stream()
				// .filter(xsd -> xsd.getElementType().size() > 0)
				.filter(xsd -> !xsd.getFile().getName()
						.endsWith("maven-4.0.0.xsd"))
				.forEach(xsd -> {
					List<ElementType> ets = xsd.getElementType();
					String usage = "API!";

					if (ets.size() > 0) {
						ets.stream().filter(et -> et.isRequest())
								.forEach(et -> {
									sb.append(getRamlOperation(et, usage,
											flattenQueryParameters, xsds));
								});
					}
				});
		return sb.toString();
	}

	private static void getUses(final XsdsUtil xsds,
			final String targetNameSpace, final String pathUp,
			final Map<String, String> uses) {
		if (Objects.nonNull(targetNameSpace)) {
			XsdContainer xsd = xsds.getXsdContainer(targetNameSpace);
			uses.put(
					Util.capitalizePackageName(
							xsds.getPackageName(targetNameSpace)),
					String.format("%s%s", pathUp,
							xsd.getRelativeName().replace(".xsd", ".raml")));
			xsd.getImportedTargetNamespaces().stream()
					.forEach(ns -> getUses(xsds, ns, pathUp, uses));
		}
	}

	/**
	 * @param xsd
	 * @param flattenQueryParameters
	 * @param xsds
	 * @param eipVersion
	 * @return the RAML of the specific XSD as Library or API
	 */
	public static String getRaml(final XsdContainer xsd,
			final boolean flattenQueryParameters, final XsdsUtil xsds,
			final String eipVersion) {
		StringBuffer sb = new StringBuffer(512);

		List<ElementType> ets = xsd.getElementType();
		List<ComplexType> cts = xsd.getComplexType();
		boolean isLibrary = ets.isEmpty();
		isLibrary = true;
		String pathUp = getPathUp(xsd.getRelativeName());
		String usage = Util.capitalizePackageName(xsd.getPackageName());
		Map<String, String> uses = new TreeMap<>();
		xsd.getImportedTargetNamespaces().stream()
				.forEach(ns -> getUses(xsds, ns, pathUp, uses));
		if (isLibrary) {
			sb.append("#%RAML 1.0 Library\n");
		} else {
			sb.append("#%RAML 1.0\n");
			sb.append(String.format("title: %s\n", xsd.getPackageName()));
		}
		sb.append(String.format("# EIP version %s - %s\n", eipVersion,
				Util.getGeneratedAt(new Date(), XsdToRaml.class)));
		sb.append(String.format("# target namespace %s\n",
				xsd.getTargetNamespace()));
		if (uses.size() > 0) {
			sb.append("uses: \n");
			uses.entrySet().stream().forEach(entry -> sb.append(String
					.format("  %s: %s\n", entry.getKey(), entry.getValue())));
		}
		if (isLibrary) {
			sb.append("usage: ").append(usage).append("\n");
		}
		if (cts.size() > 0) {
			sb.append("types:\n");
			cts.stream().filter(ct -> !ct.isEnumType())
					.sorted(Comparator.comparing(ComplexType::isSimpleType)
							.reversed()
							.thenComparing(ComplexType::getHierarchyLevel)
							.thenComparing(ComplexType::getQNameLocalPart))
					.forEach(ct -> {
						String name = ct.getQNameLocalPart();
						String type = getDefinition(ct);

						sb.append(String.format("  %s: \n", name));
						sb.append(String.format("    type: %s\n", type));
						if (ct.getAnnotationDocumentationNormalised() != null
								&& ct.getAnnotationDocumentationNormalised()
										.trim().length() > 0) {
							sb.append(String.format("    description: %s\n",
									ct.getAnnotationDocumentationNormalised()
											.replace("@", "#")));
						}
						if (ct.getChildren().size() > 0) {
							sb.append("    properties: \n");
							ct.getChildren().stream().forEach(ctc -> sb.append(
									getDeclarationChild(ctc, usage, xsds)));
						}
					});
		}
		if (!isLibrary) {
			ets.stream().filter(et -> et.isRequest()).forEach(et -> {
				sb.append(getRamlOperation(et, usage, flattenQueryParameters,
						xsds));
			});
		}
		return sb.toString();
	}

	private static String getRamlOperation(final ElementType et,
			final String usage, final boolean flattenQueryParameters,
			final XsdsUtil xsds) {
		StringBuffer sb = new StringBuffer(256);
		String name = et.getOperationName();
		if (name.toLowerCase().startsWith("get")) {
			sb.append(String.format("/%s/%s:\n  get: \n", et.getServiceId(),
					name.substring(3, name.length())));
			sb.append(getDeclarationQueryParameter(et.getComplexType(),
					flattenQueryParameters, usage, xsds));
			ElementType response = XsdsUtil.findResponse(et,
					xsds.getElementTypes(), xsds);
			if (Objects.nonNull(response)) {
				String type = getDefinitionChild(xsds,
						response.getComplexType(), usage);
				sb.append("    responses: \n");
				sb.append("      200: \n");
				sb.append("        body: \n");
				sb.append("          application/json: \n");
				sb.append(String.format("            type: %s\n", type));
			}
		}
		return sb.toString();
	}

	private static String getDeclarationQueryParameter(final ComplexType ct,
			final boolean flattenQueryParameters, final String usage,
			final XsdsUtil xsds) {
		StringBuffer sb = new StringBuffer(512);
		if (Objects.nonNull(ct) && !flattenQueryParameters) {
			String type = getDefinitionChild(xsds, ct, usage);
			sb.append("    queryParameters: \n");
			sb.append(String.format("      type: %s\n", type));
		} else if (Objects.nonNull(ct) && flattenQueryParameters) {
			sb.append("    queryParameters: \n");
			ct.getChildren().stream()
					.filter(ctc -> ctc.getComplexType().getTargetNamespace()
							.equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)
							|| ctc.getComplexType().getType().isPrimitiveType()
							|| ctc.getComplexType().isSimpleType())
					.forEach(ctc -> sb
							.append(getDeclarationChild(ctc, usage, xsds)));
			ct.getChildren().stream()
					.filter(ctc -> !ctc.getComplexType().getTargetNamespace()
							.equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)
							&& !ctc.getComplexType().getType().isPrimitiveType()
							&& !ctc.getComplexType().isSimpleType())
					.forEach(ctc -> ctc.getComplexType().getChildren().stream()
							.forEach(ctcx -> sb.append(
									getDeclarationChild(ctcx, usage, xsds))));
		}
		return sb.toString();
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(final String[] args) {
		String basePackageName = "com.qpark.eip.inf";
		String xsdPath = "C:\\xnb\\dev\\git\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-flow\\target\\model";
		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "mapping flow msg";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = XsdsUtil.getInstance(dif, "a.b.c.bus",
				messagePackageNameSuffix, "delta");
		Map<String, XsdContainer> xsdContainerMap = xsds.getXsdContainerMap();
		xsdContainerMap.values().stream()
				// .filter(xsd -> xsd.getPackageName().contains(".model."))
				.forEach(xsd -> {
					String raml = getRaml(xsd, true, xsds, "eipversion");
					System.out.println(raml);
					System.out.println(
							xsd.getRelativeName().replace(".xsd", ".raml"));
					System.out.println();
					System.out.println();
				});

		System.out.println(getRamlApi(true, xsds, "eipVersion"));
	}
}
