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
package com.qpark.maven.plugin.xmapper;

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
	static class SubRequestResponse extends MethodDefinition {
		boolean request;
	}

	static class SimpleMethodDefinition extends MethodDefinition {
		private final boolean request;
		private String suffix = "";

		public SimpleMethodDefinition(final ComplexTypeChild in,
				final ComplexType out, final boolean request) {
			super(in, out);
			this.request = request;
		}

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

		public ComplexTypeChild getIn() {
			ComplexTypeChild ctc = null;
			for (ComplexTypeChild i : this.getInput()) {
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
		 * @param suffix the suffix to set
		 */
		public void setSuffix(final String suffix) {
			this.suffix = suffix;
		}

		/**
		 * @return the request
		 */
		public boolean isRequest() {
			return this.request;
		}
	}

	static class InvokeInOut {
		ComplexTypeChild in;
		ComplexType out;
		boolean request;

		public InvokeInOut(final ComplexTypeChild in, final boolean request) {
			this.in = in;
			this.request = request;
		}
	}

	private static String getJavaDocCommentMapMethod(
			final SimpleMethodDefinition smd) {
		StringBuffer sb = new StringBuffer();
		sb.append("\t * Map");
		if (smd.getInput().size() > 0) {
			sb.append(" a");
			for (int i = 0; i < smd.getInput().size(); i++) {
				if (i > 0) {
					sb.append(",\n\t *");
				}
				sb.append(" {@link ");
				sb.append(smd.getInput().get(i).getComplexType().getClassName());
				sb.append("}");
			}
		}
		if (smd.getOut() != null) {
			sb.append("\n\t * to a {@link ");
			sb.append(smd.getOut().getClassName());
			sb.append("}");
		}
		sb.append(".\n");
		return sb.toString();
	}

	private static String getJavaDocCommentSubRequest(
			final SimpleMethodDefinition smd) {
		StringBuffer sb = new StringBuffer();
		sb.append("\t * Do a sub request to get ");
		if (smd.getOut() != null) {
			sb.append("\n\t * a {@link ");
			sb.append(smd.getOut().getClassName());
			sb.append("}");
		}
		if (smd.getInput().size() > 0) {
			sb.append(" with");
			for (int i = 0; i < smd.getInput().size(); i++) {
				if (i > 0) {
					sb.append(",\n\t *");
				}
				sb.append(" {@link ");
				sb.append(smd.getInput().get(i).getComplexType().getClassName());
				sb.append("}");
			}
		}
		sb.append(".\n");
		return sb.toString();
	}

	private static String getMethodDeclaration(
			final SimpleMethodDefinition smd, final String methodName,
			final String javaDocComment) {
		StringBuffer sb = new StringBuffer();

		if (smd != null) {
			sb.append("\n");
			sb.append("\t/**\n");
			if (javaDocComment != null) {
				sb.append(javaDocComment);
			}
			for (ComplexTypeChild ctc : smd.getInput()) {
				sb.append("\t * @param ");
				sb.append(ctc.getChildName());
				sb.append(" the {@link ");
				sb.append(ctc.getComplexType().getClassName());
				sb.append("}\n");
			}
			if (smd.getOut() != null) {
				sb.append("\t * @return the {@link ");
				sb.append(smd.getOut().getClassName());
				sb.append("}\n");
			}
			sb.append("\t */\n");

			sb.append("\t");
			if (smd.getOut() != null) {
				sb.append(smd.getOut().getClassName());
			} else {
				sb.append("void");
			}
			sb.append(" ");
			sb.append(methodName);
			if (!methodName.equals("invokeFlow")
					&& !methodName.equals("subRequest")) {
				if (smd.request) {
					sb.append("Request");
				} else {
					sb.append("Response");
				}
				sb.append(smd.suffix);
			}
			sb.append("(");
			if (smd.getInput().size() > 0) {
				for (int i = 0; i < smd.getInput().size(); i++) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(smd.getInput().get(i).getComplexType()
							.getClassName());
					sb.append(" ");
					sb.append(smd.getInput().get(i).getChildName());
				}
			}
			sb.append(");\n");
		}
		return sb.toString();
	}

	private static List<SimpleMethodDefinition> getSimpleMethodDefinition(
			final ComplexType ct, final String prefixIn,
			final String prefixOut, final boolean request) {
		List<SimpleMethodDefinition> simpleMethods = new ArrayList<SimpleMethodDefinition>();
		Set<String> inChildrenFound = new TreeSet<String>();
		SimpleMethodDefinition def;
		for (ComplexTypeChild childOut : ct.getChildren()) {
			if (childOut.getChildName().startsWith(prefixOut)) {
				def = new SimpleMethodDefinition(childOut.getComplexType(),
						request);
				simpleMethods.add(def);
				def.setSuffix(getSuffix(childOut, prefixOut));
				for (ComplexTypeChild childIn : ct.getChildren()) {
					if (childIn.getChildName().equals(
							new StringBuffer(16).append(prefixIn)
									.append(def.suffix).toString())) {
						def.getInput().add(childIn);
						inChildrenFound.add(childIn.getChildName());
					}
				}
			}
		}
		for (ComplexTypeChild childIn : ct.getChildren()) {
			if (childIn.getChildName().startsWith(prefixIn)
					&& !inChildrenFound.contains(childIn.getChildName())) {
				def = new SimpleMethodDefinition(childIn, request);
				simpleMethods.add(def);
				def.setSuffix(getSuffix(childIn, prefixIn));
				for (ComplexTypeChild childOut : ct.getChildren()) {
					if (childOut.getChildName().equals(
							new StringBuffer(16).append(prefixOut)
									.append(def.suffix).toString())) {
						def.setOut(childOut.getComplexType());
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
	private final ComplexType flowInput;

	private final String flowName;

	private final ComplexType flowOutput;

	private final Log log;
	private final SimpleMethodDefinition flow;
	private final SimpleMethodDefinition request;
	private final SimpleMethodDefinition response;
	private final List<SimpleMethodDefinition> subRequests = new ArrayList<SimpleMethodDefinition>();
	private final List<SimpleMethodDefinition> mappings = new ArrayList<SimpleMethodDefinition>();

	private final String packageName;

	public FlowInterfaceGenerator(final XsdsUtil config,
			final ComplexType flowInput, final Log log) {
		this.flowInput = flowInput;
		this.config = config;
		this.packageName = new StringBuffer(flowInput.getPackageName()).append(
				"").toString();
		this.flowName = flowInput.getClassName().substring(0,
				flowInput.getClassName().lastIndexOf("RequestType"));
		this.flowOutput = XsdsUtil.findResponse(flowInput,
				config.getComplexTypes(), config);

		this.flow = new SimpleMethodDefinition(new ComplexTypeChild("request",
				flowInput, BigInteger.ONE, BigInteger.ONE, null),
				this.flowOutput, true);

		this.log = log;

		List<SimpleMethodDefinition> list = getSimpleMethodDefinition(
				flowInput, "in", "out", true);
		if (!list.isEmpty()) {
			this.request = list.get(0);
		} else {
			this.request = null;
		}
		this.subRequests.addAll(getSimpleMethodDefinition(flowInput,
				"subRequest", "subResponse", true));
		this.mappings.addAll(getSimpleMethodDefinition(flowInput, "mapIn",
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
			this.mappings.addAll(getSimpleMethodDefinition(this.flowOutput,
					"mapIn", "mapOut", false));
		} else {
			this.response = null;
		}

	}

	public void generateInterface(final File outputDirectory,
			final String basicFlowPackageName) {
		this.log.debug("+generateImpl");

		StringBuffer sb = new StringBuffer(1024);
		sb.append("package ");
		sb.append(this.packageName);
		sb.append(";\n");
		sb.append("\n");

		Set<String> imports = new TreeSet<String>();
		imports.add(new StringBuffer(basicFlowPackageName).append(".Flow")
				.toString());
		Set<String> importedClasses = new TreeSet<String>();
		importedClasses.add(this.flowInput.getClassNameFullQualified());

		for (ComplexTypeChild child : this.flowInput.getChildren()) {
			AbstractGenerator.addImport(child.getComplexType()
					.getClassNameFullQualified(), imports, importedClasses);
		}
		if (AbstractGenerator.isChildListImport(this.flowInput.getChildren())) {
			AbstractGenerator.addImport("java.util.List", imports,
					importedClasses);
		}

		if (this.flowOutput != null) {
			importedClasses.add(this.flowOutput.getClassNameFullQualified());
			for (ComplexTypeChild child : this.flowOutput.getChildren()) {
				AbstractGenerator.addImport(child.getComplexType()
						.getClassNameFullQualified(), imports, importedClasses);
			}
			if (AbstractGenerator.isChildListImport(this.flowOutput
					.getChildren())) {
				AbstractGenerator.addImport("java.util.List", imports,
						importedClasses);
			}

		}
		for (String importedClass : imports) {
			sb.append("import ").append(importedClass).append(";\n");
		}

		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * The {@link ");
		sb.append(this.flowName);
		sb.append("}.\n");
		if (this.request != null || this.response != null) {
			sb.append(" * <p/>\n");
			sb.append(" * {@link #invokeFlow(");
			if (this.flow.getInput().size() > 0) {
				sb.append(this.flow.getInput().get(0).getComplexType()
						.getClassName());
			}
			sb.append(")} calls\n");
			if (this.request != null) {
				sb.append("\n * {@link #executeRequest(");
				for (int i = 0; i < this.request.getInput().size(); i++) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(this.request.getInput().get(i).getComplexType()
							.getClassName());
				}
				sb.append(")}");
			}
			if (this.request != null && this.response != null) {
				sb.append(" and ");
			}
			if (this.response != null) {
				sb.append("\n * {@link #processResponse(");
				for (int i = 0; i < this.response.getInput().size(); i++) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(this.response.getInput().get(i).getComplexType()
							.getClassName());
				}
				sb.append(")}");
			}
			sb.append(".\n");
		}

		sb.append(" * <pre>");
		sb.append(Util.getGeneratedAt());
		sb.append("</pre>\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public interface ");
		sb.append(this.flowName);
		sb.append("\n");
		sb.append("\t\textends Flow<");
		if (this.flow.getInput().size() > 0) {
			sb.append(this.flow.getInput().get(0).getComplexType()
					.getClassName());
		} else {
			sb.append("Void");
		}
		sb.append(", ");
		if (this.flow.getOut() != null) {
			sb.append(this.flow.getOut().getClassName());
		} else {
			sb.append("Void");
		}
		sb.append(">");
		sb.append(" {\n");

		// /* invoke flow. */
		// sb.append(getMethodDeclaration(this.flow, "invokeFlow",
		// FlowInterfaceGenerator.getJavaDocCommentInvokeFlowMethod(
		// this.flowName, this.flow, this.request, this.response)));

		/* executeRequest. */
		sb.append(getMethodDeclaration(this.request, "execute",
				getJavaDocCommentInvokeRequestResponseMethod(this.request)));

		/* processResponse. */
		sb.append(getMethodDeclaration(this.response, "process",
				getJavaDocCommentInvokeRequestResponseMethod(this.response)));

		for (SimpleMethodDefinition smd : this.subRequests) {
			sb.append(getMethodDeclaration(smd, "subRequest",
					getJavaDocCommentSubRequest(smd)));
		}
		for (SimpleMethodDefinition smd : this.mappings) {
			sb.append(getMethodDeclaration(smd, "mapInOut",
					getJavaDocCommentMapMethod(smd)));
		}

		sb.append("}\n");
		File f = Util.getFile(outputDirectory, this.packageName,
				new StringBuffer().append(this.flowName).append(".java")
						.toString());
		this.log.info(new StringBuffer().append("Write Flow ").append(
				f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generateImpl");
	}

	private static String getJavaDocCommentInvokeRequestResponseMethod(
			final SimpleMethodDefinition smd) {
		StringBuffer sb = new StringBuffer(128);
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

	private static String getJavaDocCommentInvokeFlowMethod(
			final String flowName, final SimpleMethodDefinition smd,
			final SimpleMethodDefinition request,
			final SimpleMethodDefinition response) {
		StringBuffer sb = new StringBuffer(128);
		sb.append("\t * The {@link ");
		sb.append(flowName);
		sb.append("}. The This calls");
		if (request != null) {
			sb.append("\n\t * {@link #executeRequest(");
			for (int i = 0; i < request.getInput().size(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(request.getInput().get(i).getComplexType()
						.getClassName());
			}
			sb.append(")}");
		}
		if (request != null && response != null) {
			sb.append(" and ");
		}
		if (response != null) {
			sb.append("\n\t * {@link #processResponse(");
			for (int i = 0; i < response.getInput().size(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(response.getInput().get(i).getComplexType()
						.getClassName());
			}
			sb.append(")}");
		}
		sb.append(".\n");

		return sb.toString();
	}
}
