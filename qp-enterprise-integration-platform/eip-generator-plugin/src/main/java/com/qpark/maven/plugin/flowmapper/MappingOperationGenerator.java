/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class MappingOperationGenerator
		extends AbstractMappingOperationGenerator {
	public MappingOperationGenerator(final XsdsUtil config,
			final String basicFlowPackageName, final ComplexType request,
			final ComplexType response,
			final ComplexContentList complexContentList,
			final String eipVersion, final Log log) {
		super(config, basicFlowPackageName, request, response,
				complexContentList, eipVersion, log);
	}

	String generateImpl() {
		this.log.debug("+generateImpl");
		List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree = this
				.getChildrenTree();

		StringBuffer sb = new StringBuffer(1024);
		sb.append("package ");
		sb.append(this.packageNameImpl);
		sb.append(";\n");
		sb.append("\n");

		Set<String> importedClasses = this.getImplImports(childrenTree);
		importedClasses.add(new StringBuffer(this.basicFlowPackageName)
				.append(".FlowContext").toString());

		for (String importedClass : importedClasses) {
			sb.append("import ").append(importedClass).append(";\n");
		}

		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * The {@link ");
		sb.append(this.interfaceName);
		sb.append("} implementation.\n");
		sb.append(" * <p/>\n");
		sb.append(" * Mapping a {@link ");
		sb.append(this.request.getClassNameFullQualified());
		sb.append("}\n * into a {@link ");
		sb.append(this.response.getClassNameFullQualified());
		sb.append("}.\n");
		sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(),
				this.eipVersion));
		sb.append(" */\n");
		sb.append("@Component\n");
		sb.append("public class ");
		sb.append(this.implName);
		sb.append(" implements ");
		sb.append(this.interfaceName);
		sb.append(" {\n");

		sb.append(this.getLoggerDefinition());
		sb.append("\n");

		sb.append("\t/** The {@link ObjectFactory}. */\n");
		sb.append("\tprivate final ObjectFactory of = new ObjectFactory();\n");

		sb.append(this.getMapperDefinitions(childrenTree, importedClasses));

		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Maps the {@link ");
		sb.append(this.request.getClassNameFullQualified());
		sb.append("} into a {@link ");
		sb.append(this.response.getClassNameFullQualified());
		sb.append("}.\n");
		sb.append(this.getSeeInterfaceJavaDoc());
		sb.append("\t */\n");

		sb.append("\tpublic ");
		sb.append(this.response.getClassNameFullQualified());
		sb.append(" ");
		sb.append(this.getMethodName());
		sb.append("(");
		sb.append(this.request.getClassNameFullQualified());
		sb.append(" request, FlowContext flowContext) {\n");
		sb.append("\t\t");
		sb.append(this.response.getClassName());
		sb.append(" response = of.create");
		sb.append(this.response.getClassName());
		sb.append("();\n");
		sb.append("\n");

		for (ComplexTypeChild requestChild : GeneratorMapperMojo
				.getValidChildren(this.request)) {
			if (!"mapping".equals(requestChild.getChildName())) {
				sb.append("\t\t/* Request contained value ")
						.append(requestChild.getChildName()).append(". */\n");
				sb.append("\t\t");
				if (requestChild.isList()) {
					sb.append("List<");
				}
				sb.append(requestChild.getComplexType()
						.getClassNameFullQualified());
				if (requestChild.isList()) {
					sb.append(">");
				}
				sb.append(" request_");
				sb.append(requestChild.getChildName());
				sb.append(" = request.");
				sb.append(requestChild.getGetterName());
				sb.append("();\n");
			}
		}
		sb.append("\n");

		ComplexTypeChild mapping = this.getIndirectMapRequestTypeMappingChild();
		if (mapping != null) {
			sb.append("\n");
			sb.append("\t\t/* Values to set in the mapping. */\n");
			sb.append("\t\t");
			if (importedClasses.contains(
					mapping.getComplexType().getClassNameFullQualified())) {
				sb.append(mapping.getComplexType().getClassName());
			} else {
				sb.append(mapping.getComplexType().getClassNameFullQualified());
			}
			sb.append(" request_mapping = new ");
			sb.append(mapping.getComplexType().getPackageName());
			sb.append(".ObjectFactory().create");
			sb.append(mapping.getComplexType().getClassName());
			sb.append("();\n");
			sb.append("\t\trequest.setMapping(request_mapping);\n");

			for (ComplexTypeChild grandChild : GeneratorMapperMojo
					.getValidChildren(mapping.getComplexType())) {
				sb.append("\t\t/* Set ").append(grandChild.getChildName())
						.append(". */\n");
				sb.append(this.getComplexContentSetter("request_mapping",
						mapping.getComplexType(), mapping.isList(), grandChild,
						importedClasses));
				sb.append("\n");
			}
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("\t\t/* Values to set in the response. */\n");
		for (ComplexTypeChild responseChild : GeneratorMapperMojo
				.getValidChildren(this.response)) {
			sb.append(this.getComplexContentSetter("response", this.response,
					false, responseChild, importedClasses));
			String newParentName = new StringBuffer(32).append("response_")
					.append(responseChild.getChildName()).toString();
			for (ComplexTypeChild grandChild : GeneratorMapperMojo
					.getValidChildren(responseChild.getComplexType())) {
				sb.append("\t\t/* Set ").append(grandChild.getChildName())
						.append(" into ").append(responseChild.getChildName())
						.append(". */\n");
				sb.append(this.getComplexContentSetter(newParentName,
						responseChild.getComplexType(), responseChild.isList(),
						grandChild, importedClasses));
				sb.append("\n");
			}
		}
		sb.append("\n");

		sb.append("\t\treturn response;\n");
		sb.append("\t}\n");

		// sb.append(this.getMapperDefinitionSetter(childrenTree,
		// importedClasses));
		sb.append("}\n");
		this.log.debug("-generateImpl");
		return sb.toString();
	}

	public void generateImpl(final File outputDirectory) {
		this.log.debug("+generateImpl");
		String source = this.generateImpl();
		File f = Util.getFile(outputDirectory, this.packageNameImpl,
				new StringBuffer().append(this.implName).append(".java")
						.toString());
		this.log.info(new StringBuffer().append("Write Impl ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, source);
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generateImpl");
	}

	private String getComplexContentSetter(final String parentName,
			final ComplexType parent, final boolean isParentList,
			final ComplexTypeChild child, final Set<String> importedClasses) {
		StringBuffer sb = new StringBuffer(1024);
		String childName = new StringBuffer(32).append(parentName).append("_")
				.append(child.getChildName()).toString();
		ComplexContent cc = this.getMapperDefinition(child.getComplexType());
		sb.append("\t\t");
		if (child.isList()) {
			sb.append("List<");
		}
		if (importedClasses
				.contains(child.getComplexType().getClassNameFullQualified())) {
			sb.append(child.getComplexType().getClassName());
		} else {
			sb.append(child.getComplexType().getClassNameFullQualified());
		}
		if (child.isList()) {
			sb.append(">");
		}
		sb.append(" ");
		sb.append(childName);
		sb.append(" = ");
		if (cc == null || cc.isInterfaceType) {
			if (child.isList()) {
				sb.append(
						"java.util.Collections.emptyList();// new ArrayList\n");
				sb.insert(0, "\t\t// TODO Nothing found to set!\n");
			} else {
				String s = this.getMatchingRequestParameter(child);
				if (s == null) {
					sb.append("new ");
					sb.append(child.getComplexType().getPackageName());
					sb.append(".ObjectFactory().create");
					sb.append(child.getComplexType().getClassName());
					sb.append("();\n");
					if (!"response".equals(parentName)) {
						sb.insert(0,
								"\t\t// TODO object creation entered here!\n");
					}
				} else {
					sb.append(s);
					sb.append(";\n");
				}
			}
		} else if (cc.isDirect || cc.isComplex) {
			sb.append("this.");
			sb.append(Util.lowerize(cc.interfaceName));
			sb.append(".createMappingType(");
			int i = 0;
			for (ComplexTypeChild ccChild : cc.ct.getChildren()) {
				String s = this.getMatchingRequestParameter(ccChild);
				if (s != null) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(s);
					i++;
				}
			}
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(" flowContext);\n");
			if (i == 0) {
				sb.insert(0,
						"\t\t// TODO no matching object found in request to set!\n");
			}
		} else {
			sb.append("null;//");
			sb.append(Util.lowerize(cc.interfaceName));
			sb.append(".createMappingType(flowContext);\n");
			sb.insert(0, "\t\t// TODO Nothing found to be done here!\n");
		}

		if (child.isList()) {
			sb.append("\t\t// TODO Verify if the call of ");
			sb.append(child.getGetterName());
			sb.append("().addAll() need to be set at the end of the method.\n");
			sb.append(
					"\t\t// TODO The list is most probably filled later in this method!!!\n");
			sb.append("\t\t");
			sb.append(parentName);
			sb.append(".");
			sb.append(child.getGetterName());
			sb.append("().addAll(");
			sb.append(childName);
			sb.append(")");
		} else {
			sb.append("\t\t");
			sb.append(parentName);
			sb.append(".");
			sb.append(child.getSetterName());
			sb.append("(");
			sb.append(childName);
			sb.append(")");
		}
		sb.append(";\n");

		return sb.toString();
	}

	private ComplexTypeChild getIndirectMapRequestTypeMappingChild() {
		ComplexTypeChild value = null;
		for (ComplexTypeChild child : GeneratorMapperMojo
				.getValidChildren(this.request)) {
			if ("mapping".equals(child.getChildName())) {
				value = child;
				break;
			}
		}
		return value;
	}

	private String getMatchingRequestParameter(
			final ComplexTypeChild destination) {
		String s = null;
		StringBuffer sb = new StringBuffer(256);
		for (ComplexTypeChild requestChild : GeneratorMapperMojo
				.getValidChildren(this.request)) {
			if (destination.getComplexType().getClassNameFullQualified()
					.equals(requestChild.getComplexType()
							.getClassNameFullQualified())) {
				sb.append("request.");
				sb.append(requestChild.getGetterName());
				sb.append("()");
				s = sb.toString();
				break;
			}
		}
		if (s == null) {
			for (ComplexTypeChild requestChild : GeneratorMapperMojo
					.getValidChildren(this.request)) {
				sb.setLength(0);
				sb.append("request.");
				sb.append(requestChild.getGetterName());
				sb.append("().");
				for (ComplexTypeChild grandChild : GeneratorMapperMojo
						.getValidChildren(requestChild.getComplexType())) {
					if (destination.getComplexType().getClassNameFullQualified()
							.equals(grandChild.getComplexType()
									.getClassNameFullQualified())) {
						sb.append(grandChild.getGetterName());
						sb.append("()");
						s = sb.toString();
						break;
					}
				}
				if (s != null) {
					break;
				}
			}
		}
		return s;
	}

	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingOperationGenerator#getMethodName()
	 */
	@Override
	protected String getMethodName() {
		return "invokeMapping";
	}

	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingOperationGenerator#getPackageNameInterface()
	 */
	@Override
	protected String getPackageNameInterface() {
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.request.getPackageName().substring(0,
				this.request.getPackageName().length() - ".svc".length()));
		sb.append(".operation");
		return sb.toString();
	}
}
