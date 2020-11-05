/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.MethodDefinition;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class FlowInterfaceGenerator {
	static class InvokeInOut {
		ComplexTypeChild in;
		ComplexType out;
		boolean request;

		public InvokeInOut(final ComplexTypeChild in, final boolean request) {
			this.in = in;
			this.request = request;
		}
	}

	static class SimpleMethodDefinition extends MethodDefinition {
		private final boolean request;
		private String suffix = "";

		public SimpleMethodDefinition(final ComplexType out,
				final boolean request) {
			super(out);
			this.request = request;
		}

		public SimpleMethodDefinition(final ComplexTypeChild in,
				final boolean request) {
			super(in);
			this.request = request;
		}

		public SimpleMethodDefinition(final ComplexTypeChild in,
				final ComplexType out, final boolean request) {
			super(in, out);
			this.request = request;
		}

		public ComplexTypeChild getIn() {
			ComplexTypeChild ctc = null;
			for (final ComplexTypeChild i : this.getInput()) {
				ctc = i;
				break;
			}
			return ctc;
		}

		/**
		 * @return the suffix
		 */
		public String getSuffix() {
			return this.suffix;
		}

		/**
		 * @return the request
		 */
		public boolean isRequest() {
			return this.request;
		}

		/**
		 * @param suffix
		 *            the suffix to set
		 */
		public void setSuffix(final String suffix) {
			this.suffix = suffix;
		}
	}

	static class SubRequestResponse extends MethodDefinition {
		boolean request;
	}

	private static String getJavaDocCommentInvokeRequestResponseMethod(
			final SimpleMethodDefinition smd) {
		final StringBuffer sb = new StringBuffer(128);
		if (smd != null) {
			String a = "Process the ";
			String b = "response";
			if (smd.isRequest()) {
				a = "Execute the ";
				b = "request";
			}
			sb.append("\t * ");
			sb.append(a);
			sb.append(b);
			sb.append(". Contains the call of the <i>mapInOut");
			sb.append(Util.capitalize(b));
			sb.append("</i> method(s).\n");
		}
		return sb.toString();
	}

	private static String getJavaDocCommentMapMethod(
			final SimpleMethodDefinition smd) {
		final StringBuffer sb = new StringBuffer();
		sb.append("\t * Map");
		if (smd.getInput().size() > 0) {
			sb.append(" a");
			for (int i = 0; i < smd.getInput().size(); i++) {
				if (i > 0) {
					sb.append(",\n\t *");
				}
				sb.append(" {@link ");
				sb.append(smd.getInput().get(i).getComplexType()
						.getClassNameFullQualified());
				sb.append("}");
			}
		}
		if (smd.getOut() != null) {
			sb.append("\n\t * to a {@link ");
			sb.append(smd.getOut().getClassNameFullQualified());
			sb.append("}");
		}
		sb.append(".\n");
		return sb.toString();
	}

	private static String getJavaDocCommentSubRequest(
			final SimpleMethodDefinition smd) {
		final StringBuffer sb = new StringBuffer();
		sb.append("\t * Do a sub request to get ");
		if (smd.getOut() != null) {
			sb.append("\n\t * a {@link ");
			sb.append(smd.getOut().getClassNameFullQualified());
			sb.append("}");
		}
		if (smd.getInput().size() > 0) {
			sb.append(" with");
			for (int i = 0; i < smd.getInput().size(); i++) {
				if (i > 0) {
					sb.append(",\n\t *");
				}
				sb.append(" {@link ");
				sb.append(smd.getInput().get(i).getComplexType()
						.getClassNameFullQualified());
				sb.append("}");
			}
		}
		sb.append(".\n");
		return sb.toString();
	}

	private static String getMethodDeclaration(final SimpleMethodDefinition smd,
			final String methodName, final String javaDocComment) {
		final StringBuffer sb = new StringBuffer();

		if (smd != null) {
			sb.append("\n");
			sb.append("\t/**\n");
			if (javaDocComment != null) {
				sb.append(javaDocComment);
			}
			for (final ComplexTypeChild ctc : smd.getInput()) {
				sb.append("\t * @param ");
				sb.append(ctc.getChildName());
				sb.append(" the {@link ");
				sb.append(ctc.getComplexType().getClassNameFullQualified());
				sb.append("}\n");
			}
			sb.append("\t * @param flowContext the {@link FlowContext}\n");
			if (smd.getOut() != null) {
				sb.append("\t * @return the {@link ");
				sb.append(smd.getOut().getClassNameFullQualified());
				sb.append("}\n");
			}
			sb.append("\t */\n");

			//sb.append("\t@InsightOperation\n");

			sb.append("\t");
			if (smd.getOut() != null) {
				if (smd.isOutList()) {
					sb.append("List<");
					sb.append(smd.getOut().getClassNameFullQualified());
					sb.append(">");
				} else {
					sb.append(smd.getOut().getClassNameFullQualified());
				}
			} else {
				sb.append("void");
			}
			sb.append(" ");
			sb.append(methodName);
			if (methodName.equals("invokeFlow")) {
				/* Nothing to do. */
			} else if (methodName.equals("subRequest")) {
				sb.append(smd.suffix);
			} else if (methodName.equals("filter")) {
				sb.append(smd.suffix);
			} else if (methodName.equals("rule")) {
				sb.append(smd.suffix);
			} else if (smd.isRequest() && !methodName.endsWith("Request")) {
				sb.append("Request");
			} else if (!smd.isRequest() && !methodName.endsWith("Response")) {
				sb.append("Response");
			}
			sb.append("(");
			boolean addedParameter = false;
			if (smd.getInput().size() > 0) {
				for (int i = 0; i < smd.getInput().size(); i++) {
					if (i > 0) {
						sb.append(", ");
					}
					if (smd.getInput().get(i).isList()) {
						sb.append("List<");
						sb.append(smd.getInput().get(i).getComplexType()
								.getClassNameFullQualified());
						sb.append(">");
					} else {
						sb.append(smd.getInput().get(i).getComplexType()
								.getClassNameFullQualified());
					}
					sb.append(" ");
					sb.append(smd.getInput().get(i).getChildName());
					addedParameter = true;
				}
			}
			if (addedParameter) {
				sb.append(", ");
			}
			sb.append("FlowContext flowContext");
			sb.append(");\n");
		}
		return sb.toString();
	}

	private static String getMethodLink(final SimpleMethodDefinition smd,
			final String methodName) {
		final StringBuffer sb = new StringBuffer();

		if (smd != null) {
			sb.append("{@link #");
			sb.append(methodName);
			if (methodName.equals("invokeFlow")) {
				/* Nothing to do. */
			} else if (methodName.equals("subRequest")) {
				sb.append(smd.suffix);
			} else if (methodName.equals("filter")) {
				sb.append(smd.suffix);
			} else if (methodName.equals("rule")) {
				sb.append(smd.suffix);
			} else if (smd.isRequest() && !methodName.endsWith("Request")) {
				sb.append("Request");
			} else if (!smd.isRequest() && !methodName.endsWith("Response")) {
				sb.append("Response");
			}
			sb.append("(");
			for (int i = 0; i < smd.getInput().size(); i++) {
				final ComplexTypeChild ctc = smd.getInput().get(i);
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(ctc.getComplexType().getClassNameFullQualified());
			}
			sb.append(", FlowContext)}");
		}
		return sb.toString();
	}

	private static List<SimpleMethodDefinition> getSimpleMethodDefinition(
			final ComplexType ct, final String prefixIn, final String prefixOut,
			final boolean request) {
		final List<SimpleMethodDefinition> simpleMethods = new ArrayList<>();
		final Set<String> inChildrenFound = new TreeSet<>();
		SimpleMethodDefinition def;
		for (final ComplexTypeChild childOut : ct.getChildren()) {
			if (childOut.getChildName().startsWith(prefixOut)) {
				def = new SimpleMethodDefinition(childOut.getComplexType(),
						request);
				def.setOutList(childOut.isList());
				simpleMethods.add(def);
				def.setSuffix(getSuffix(childOut, prefixOut));
				for (final ComplexTypeChild childIn : ct.getChildren()) {
					if (childIn.getChildName().equals(new StringBuffer(16)
							.append(prefixIn).append(def.suffix).toString())) {
						def.getInput().add(childIn);
						inChildrenFound.add(childIn.getChildName());
					}
				}
			}
		}
		for (final ComplexTypeChild childIn : ct.getChildren()) {
			if (childIn.getChildName().startsWith(prefixIn)
					&& !inChildrenFound.contains(childIn.getChildName())) {
				def = new SimpleMethodDefinition(childIn, request);
				simpleMethods.add(def);
				def.setSuffix(getSuffix(childIn, prefixIn));
				for (final ComplexTypeChild childOut : ct.getChildren()) {
					if (childOut.getChildName().equals(new StringBuffer(16)
							.append(prefixOut).append(def.suffix).toString())) {
						def.setOut(childOut.getComplexType());
						def.setOutList(childOut.isList());
						break;
					}
				}
			}
		}
		return simpleMethods;
	}

	private static String getSuffix(final ComplexTypeChild ctc,
			final String prefix) {
		String suffix = "";
		if (ctc.getChildName().equals(prefix)) {
			suffix = "";
		} else {
			suffix = ctc.getChildName().substring(prefix.length(),
					ctc.getChildName().length());
		}
		return suffix;
	}

	private final XsdsUtil config;
	private final String eipVersion;
	private final List<SimpleMethodDefinition> filters = new ArrayList<>();
	private final SimpleMethodDefinition flow;
	private final ComplexType flowInput;
	private final String flowName;
	private final ComplexType flowOutput;
	private final Log log;
	private final List<SimpleMethodDefinition> mappings = new ArrayList<>();
	private final String packageName;
	private final SimpleMethodDefinition request;
	private final SimpleMethodDefinition response;
	private final List<SimpleMethodDefinition> rules = new ArrayList<>();
	private final List<SimpleMethodDefinition> subRequests = new ArrayList<>();

	/**
	 * @param config
	 * @param flowInput
	 * @param eipVersion
	 * @param log
	 */
	public FlowInterfaceGenerator(final XsdsUtil config,
			final ComplexType flowInput, final String eipVersion,
			final Log log) {
		this.flowInput = flowInput;
		this.config = config;
		this.eipVersion = eipVersion;
		this.packageName = new StringBuffer(this.flowInput.getPackageName())
				.append("").toString();
		this.flowName = this.flowInput.getClassName().substring(0,
				this.flowInput.getClassName().lastIndexOf("RequestType"));
		this.flowOutput = XsdsUtil.findResponse(this.flowInput,
				config.getComplexTypes(), config);

		this.flow = new SimpleMethodDefinition(new ComplexTypeChild("request",
				this.flowInput, BigInteger.ONE, BigInteger.ONE, null),
				this.flowOutput, true);

		this.log = log;

		List<SimpleMethodDefinition> list = getSimpleMethodDefinition(
				this.flowInput, "in", "out", true);
		if (!list.isEmpty()) {
			this.request = list.get(0);
		} else {
			this.request = null;
		}
		this.subRequests.addAll(getSimpleMethodDefinition(this.flowInput,
				"subRequest", "subResponse", true));
		this.filters.addAll(getSimpleMethodDefinition(this.flowInput,
				"filterIn", "filterOut", true));
		this.rules.addAll(getSimpleMethodDefinition(this.flowInput, "ruleIn",
				"ruleOut", true));
		this.mappings.addAll(getSimpleMethodDefinition(this.flowInput, "mapIn",
				"mapOut", true));

		if (this.flowOutput != null) {
			list = getSimpleMethodDefinition(this.flowOutput, "in", "out",
					false);
			if (!list.isEmpty()) {
				this.response = list.get(0);
			} else {
				this.response = null;
			}
			this.subRequests.addAll(getSimpleMethodDefinition(this.flowOutput,
					"subRequest", "subResponse", false));
			this.filters.addAll(getSimpleMethodDefinition(this.flowOutput,
					"filterIn", "filterOut", false));
			this.rules.addAll(getSimpleMethodDefinition(this.flowOutput,
					"ruleIn", "ruleOut", false));
			this.mappings.addAll(getSimpleMethodDefinition(this.flowOutput,
					"mapIn", "mapOut", false));
		} else {
			this.response = null;
		}

	}

	/**
	 * @param outputDirectory
	 * @param basicFlowPackageName
	 */
	public void generateInterface(final File outputDirectory,
			final String basicFlowPackageName) {
		final String source = this.generateInterface(basicFlowPackageName);
		this.log.debug("+generateInterface");
		final File f = Util.getFile(outputDirectory, this.packageName,
				new StringBuffer().append(this.flowName).append(".java")
						.toString());
		this.log.debug(new StringBuffer().append("Write Flow ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, source);
		} catch (final Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generateInterface");
	}

	String generateInterface(final String basicFlowPackageName) {
		this.log.debug("+generateInterface");

		final StringBuffer sb = new StringBuffer(1024);
		sb.append("package ");
		sb.append(this.packageName);
		sb.append(";\n");
		sb.append("\n");

		final Set<String> imports = new TreeSet<>();
		imports.add(new StringBuffer(basicFlowPackageName).append(".Flow")
				.toString());
		imports.add(new StringBuffer(basicFlowPackageName)
				.append(".FlowContext").toString());
		final Set<String> importedClasses = new TreeSet<>();
		importedClasses.add(this.flowInput.getClassNameFullQualified());

		for (final ComplexTypeChild child : this.flowInput.getChildren()) {
			AbstractGenerator.addImport(
					child.getComplexType().getClassNameFullQualified(), imports,
					importedClasses);
		}
		if (AbstractGenerator.isChildListImport(this.flowInput.getChildren())) {
			AbstractGenerator.addImport("java.util.List", imports,
					importedClasses);
		}

		if (this.flowOutput != null) {
			importedClasses.add(this.flowOutput.getClassNameFullQualified());
			for (final ComplexTypeChild child : this.flowOutput.getChildren()) {
				AbstractGenerator.addImport(
						child.getComplexType().getClassNameFullQualified(),
						imports, importedClasses);
			}
		}
		imports.add("java.util.List");
	//	imports.add("com.springsource.insight.annotation.InsightOperation");
		for (final String importedClass : imports) {
			sb.append("import ").append(importedClass).append(";\n");
		}

		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * The {@link ");
		sb.append(this.flowName);
		sb.append("}, defined in ");
		sb.append(
				this.config.getXsdContainer(this.flowInput.getTargetNamespace())
						.getRelativeName());
		sb.append(".\n");
		if (this.request != null || this.response != null) {
			sb.append(" * <p/>\n");
			sb.append(" * ");
			sb.append(getMethodLink(this.flow, "invokeFlow"));
			if (this.request != null || this.response != null) {
				sb.append(" calls ");
			}
			if (this.request != null) {
				sb.append("\n * ");
				sb.append(getMethodLink(this.request, "executeRequest"));
			}
			if (this.request != null && this.response != null) {
				sb.append(" and ");
			}
			if (this.response != null) {
				sb.append("\n * ");
				sb.append(getMethodLink(this.response, "processResponse"));
			}
			if (!this.subRequests.isEmpty()) {
				sb.append(".\n * <p/>\n * Be sure to call:\n * <ul>\n");
				for (final SimpleMethodDefinition smd : this.subRequests) {
					sb.append(" * </li>");
					sb.append(getMethodLink(smd, "subRequest"));
					sb.append("</li>\n");
				}
				sb.append(" * </ul>");
			}
			if (!this.filters.isEmpty()) {
				sb.append(".\n * <p/>\n * Apply the filters:\n * <ul>\n");
				for (final SimpleMethodDefinition smd : this.filters) {
					sb.append(" * </li>");
					sb.append(getMethodLink(smd, "filter"));
					sb.append("</li>\n");
				}
				sb.append(" * </ul>");
			}
			if (!this.rules.isEmpty()) {
				sb.append(".\n * <p/>\n * Apply the rules:\n * <ul>\n");
				for (final SimpleMethodDefinition smd : this.rules) {
					sb.append(" * </li>");
					sb.append(getMethodLink(smd, "rule"));
					sb.append("</li>\n");
				}
				sb.append(" * </ul>");
			}
			sb.append(".\n");
		}

		sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(),
				this.eipVersion));
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public interface ");
		sb.append(this.flowName);
		sb.append("\n");
		sb.append("\t\textends Flow<");
		if (this.request != null && this.request.getIn() != null) {
			if (this.request.getIn().isList()) {
				sb.append("List<");
				sb.append(this.request.getIn().getComplexType()
						.getClassNameFullQualified());
				sb.append(">");
			} else {
				sb.append(this.flow.getInput().get(0).getComplexType()
						.getClassNameFullQualified());
			}
		} else {
			sb.append("Void");
		}
		sb.append(", ");
		if (this.flow.getOut() != null) {
			if (this.response != null && this.response.isOutList()) {
				sb.append("List<");
				sb.append(this.flow.getOut().getClassNameFullQualified());
				sb.append(">");
			} else {
				sb.append(this.flow.getOut().getClassNameFullQualified());
			}
		} else {
			sb.append("Void");
		}
		sb.append(">");
		sb.append(" {\n");

		/* executeRequest. */
		sb.append(getMethodDeclaration(this.request, "executeRequest",
				getJavaDocCommentInvokeRequestResponseMethod(this.request)));

		/* processResponse. */
		sb.append(getMethodDeclaration(this.response, "processResponse",
				getJavaDocCommentInvokeRequestResponseMethod(this.response)));

		for (final SimpleMethodDefinition smd : this.subRequests) {
			sb.append(getMethodDeclaration(smd, "subRequest",
					getJavaDocCommentSubRequest(smd)));
		}
		for (final SimpleMethodDefinition smd : this.filters) {
			sb.append(getMethodDeclaration(smd, "filter",
					getJavaDocCommentSubRequest(smd)));
		}
		for (final SimpleMethodDefinition smd : this.rules) {
			sb.append(getMethodDeclaration(smd, "rule",
					getJavaDocCommentSubRequest(smd)));
		}
		final Set<String> inOutMethods = new TreeSet<>();
		String link;
		for (final SimpleMethodDefinition smd : this.mappings) {
			link = getMethodLink(smd, "mapInOut");
			if (!inOutMethods.contains(link)) {
				inOutMethods.add(link);
				sb.append(getMethodDeclaration(smd, "mapInOut",
						getJavaDocCommentMapMethod(smd)));
			} else {
				link = getMethodLink(smd, String.format("mapInOut%s",
						smd.getOut().getClassName()));
				if (!inOutMethods.contains(link)) {
					inOutMethods.add(link);
					sb.append(getMethodDeclaration(smd,
							String.format("mapInOut%s",
									smd.getOut().getClassName()),
							getJavaDocCommentMapMethod(smd)));
				}
			}
		}

		sb.append("}\n");
		this.log.debug("-generateInterface");
		return sb.toString();

	}

	/**
	 * @return the mappings
	 */
	public List<SimpleMethodDefinition> getMappings() {
		return this.mappings;
	}
}
