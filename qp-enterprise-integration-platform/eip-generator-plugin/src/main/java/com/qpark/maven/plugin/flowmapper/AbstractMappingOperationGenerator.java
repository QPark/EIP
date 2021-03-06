/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
@SuppressWarnings("static-method")
public abstract class AbstractMappingOperationGenerator
		extends AbstractGenerator {
	private static String getInterfaceName(final ComplexType ct) {
		StringBuffer sb = new StringBuffer(128);
		if (ct.getClassName().contains("MapResponseType")) {
			sb.append(Util.capitalizePackageName(ct.getClassName().substring(0,
					ct.getClassName().length() - "MapResponseType".length())));
		} else {
			sb.append(Util.capitalizePackageName(ct.getClassName()));
		}
		sb.append("MappingOperation");

		return sb.toString();
	}

	protected final String basicFlowPackageName;
	protected final String eipVersion;
	protected final String implName;
	protected final String interfaceName;
	protected final ComplexType request;
	protected final ComplexType response;

	/**
	 * @param config
	 * @param basicFlowPackageName
	 * @param crr
	 * @param complexContentList
	 * @param eipVersion
	 * @param compileableSourceDirectory
	 * @param preparedSourceDirectory
	 * @param log
	 */
	public AbstractMappingOperationGenerator(final XsdsUtil config,
			final String basicFlowPackageName, final ComplexRequestResponse crr,
			final ComplexContentList complexContentList,
			final String eipVersion, final File compileableSourceDirectory,
			final File preparedSourceDirectory, final Log log) {
		super(config, complexContentList, compileableSourceDirectory,
				preparedSourceDirectory, log);
		this.basicFlowPackageName = basicFlowPackageName;
		this.request = crr.request;
		this.response = crr.response;
		this.packageName = this.getPackageNameInterface();
		this.packageNameImpl = this.getPackageNameImpl();
		this.interfaceName = getInterfaceName(this.response);
		this.implName = this.getImplName();
		this.eipVersion = eipVersion;
		crr.packageName = this.packageName;
		crr.interfaceName = this.interfaceName;
	}

	@Override
	public final void generateInterface() {
		this.log.debug("+generateInterface");
		List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> children = this
				.getChildrenTree();

		StringBuffer sb = new StringBuffer(1024);
		sb.append("package ");
		sb.append(this.packageName);
		sb.append(";\n");
		sb.append("\n");

		sb.append("import ");
		sb.append(this.basicFlowPackageName);
		sb.append(".FlowContext;\n");
		sb.append("import ");
		sb.append(this.request.getClassNameFullQualified());
		sb.append(";\n");
		sb.append("import ");
		sb.append(this.response.getClassNameFullQualified());
		sb.append(";\n");
//		sb.append(
//				"import com.springsource.insight.annotation.InsightOperation;\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * The ");
		sb.append(Util.splitOnCapital(this.interfaceName));
		sb.append(".\n");
		sb.append(" * <p/>\n");
		sb.append(" * Mapping a {@link ");
		sb.append(this.request.getClassNameFullQualified());
		sb.append("}\n * into a {@link ");
		sb.append(this.response.getClassNameFullQualified());
		sb.append("}.\n");
		sb.append(" * <p/>\n");
		sb.append(" * The request {@link ");
		sb.append(this.request.getClassNameFullQualified());
		sb.append("}\n * is defined as <code>");
		sb.append(this.request.getType().getName());
		sb.append("</code>.\n");
		sb.append(" * The response {@link ");
		sb.append(this.response.getClassNameFullQualified());
		sb.append("}\n * is defined as <code>");
		sb.append(this.response.getType().getName());
		sb.append("</code>.\n");
		sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(),
				this.eipVersion));
		sb.append(" */\n");
		sb.append("public interface ").append(this.interfaceName)
				.append(" {\n");

		sb.append("\t/**\n");
		sb.append("\t * Invoke ");
		sb.append(Util.splitOnCapital(this.interfaceName));
		sb.append(".\n");
		for (int i = 0; i < children.size(); i++) {
			// Nothing so far.
		}
		sb.append("\t * @param request");
		sb.append(" the {@link ");
		sb.append(this.request.getClassNameFullQualified());
		sb.append("}.\n");
		sb.append("\t * @param flowContext the {@link FlowContext}.\n");
		sb.append("\t * @return the {@link ");
		sb.append(this.response.getClassNameFullQualified());
		sb.append("}.\n");
		sb.append("\t */\n");

		//sb.append("\t@InsightOperation\n");

		sb.append("\t").append(this.response.getClassNameFullQualified());
		sb.append(" ");
		sb.append(this.getMethodName());
		sb.append("(");
		sb.append(this.request.getClassNameFullQualified());
		sb.append(" request, FlowContext flowContext);\n");
		sb.append("}\n");
		File f = Util.getFile(this.compileableSourceDirectory, this.packageName,
				new StringBuffer().append(this.interfaceName).append(".java")
						.toString());
		this.log.debug(new StringBuffer().append("Write Inf  ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.log.error(String.format("%s: %s", e.getClass().getName(),
					e.getMessage()));
			e.printStackTrace();
		}
		this.log.debug("-generateInterface");
	}

	@Override
	protected List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> getChildrenTree() {
		List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> list = new ArrayList<>();
		Entry<ComplexTypeChild, List<ComplexTypeChild>> grandchild;
		for (ComplexTypeChild child : GeneratorMapperMojo
				.getValidChildren(this.request)) {
			grandchild = new SimpleEntry<>(child, GeneratorMapperMojo
					.getValidChildren(child.getComplexType()));
			list.add(grandchild);
		}
		for (ComplexTypeChild child : GeneratorMapperMojo
				.getValidChildren(this.response)) {
			grandchild = new SimpleEntry<>(child, GeneratorMapperMojo
					.getValidChildren(child.getComplexType()));
			list.add(grandchild);
		}
		return list;
	}

	protected Set<String> getImplImports(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> children) {
		Set<String> imports = this.getImplImports(children,
				this.getFqInterfaceName(),
				this.request.getClassNameFullQualified(),
				this.response.getClassNameFullQualified(),
				new StringBuffer(64).append(this.request.getPackageName())
						.append(".ObjectFactory").toString(),
				"org.springframework.beans.factory.annotation.Autowired",
				"org.springframework.stereotype.Component", "org.slf4j.Logger",
				"org.slf4j.LoggerFactory");
		return imports;
	}

	@Override
	public String getInterfaceName() {
		return this.interfaceName;
	}

	protected String getMapperDefinitionSetter(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> children,
			final Set<String> importedClasses) {
		StringBuffer sb = new StringBuffer(1024);
		ComplexContent cc = null;
		TreeSet<String> usedInterfaces = new TreeSet<>();
		for (Entry<ComplexTypeChild, List<ComplexTypeChild>> child : children) {
			for (ComplexTypeChild grandchild : child.getValue()) {
				cc = this.getMapperDefinition(grandchild.getComplexType());
				if (cc != null
						&& !usedInterfaces.contains(cc.getFQInterfaceName())) {
					usedInterfaces.add(cc.getFQInterfaceName());
					String varName = Util.lowerize(cc.interfaceClassName);
					String className = cc.interfaceClassName;
					if (!importedClasses.contains(cc.getFQInterfaceName())) {
						className = cc.getFQInterfaceName();
					}
					sb.append("\t/**\n\t * Set the {@link ");
					sb.append(className);
					sb.append("}.\n\t * @param ");
					sb.append(varName);
					sb.append(" the {@ link ");
					sb.append(className);
					sb.append("}.\n\t */\n ");
					sb.append("\tpublic void set");
					sb.append(cc.interfaceClassName);
					sb.append("(");
					sb.append(className);
					sb.append(" ");
					sb.append(varName);
					sb.append(") {\n\t\tthis.");
					sb.append(varName);
					sb.append(" = ");
					sb.append(varName);
					sb.append(";\n\t}\n\n");
				}

			}
		}
		return sb.toString();
	}

	protected abstract String getMethodName();

	protected String getSeeInterfaceJavaDoc() {
		StringBuffer sb = new StringBuffer(256);
		sb.append("\t * @see ");
		sb.append(this.packageName).append(".").append(this.interfaceName);
		sb.append("#");
		sb.append(this.getMethodName());
		sb.append("(");
		sb.append(this.request.getClassNameFullQualified());
		sb.append(", com.qpark.eip.inf.FlowContext)\n");
		return sb.toString();
	}

	protected String getSetterStatements(final String varName,
			final List<ComplexTypeChild> children) {
		StringBuffer sb = new StringBuffer(256);
		for (ComplexTypeChild child : children) {
			if (child.isList()) {
				sb.append("\t\t");
				sb.append(varName);
				sb.append(".get");
				sb.append(Util.capitalize(child.getChildName()));
				sb.append("().addAll(");
				sb.append(child.getChildName());
				sb.append(");\n");
			} else if (child.getDefaultValue() != null) {
				String objAndSetter = new StringBuffer(36).append(varName)
						.append(".set")
						.append(Util.capitalize(child.getChildName()))
						.toString();
				sb.append("\t\t");
				sb.append(XsdsUtil.getXmlObjectAsSetterParam(objAndSetter,
						child.getDefaultValue()));
				sb.append("\n");
			} else {
				sb.append("\t\t");
				sb.append(varName);
				sb.append(".set");
				sb.append(Util.capitalize(child.getChildName()));
				sb.append("(");
				sb.append(child.getChildName());
				sb.append(");\n");
			}
		}
		return sb.toString();
	}
}
