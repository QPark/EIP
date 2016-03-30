/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.ArrayList;
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
public class ComplexMappingTypeGenerator extends AbstractMappingTypeGenerator {

	private static String[] getDirectAccessProperties(final String name) {
		String[] x = new String[0];
		int index = name.indexOf('.');
		if (index > 0 && name.endsWith("MappingType")) {
			String s = name.substring(index + 1,
					name.length() - "MappingType".length());
			x = s.split("\\.");
		}
		return x;
	}

	private static String getTabs(final int number) {
		StringBuffer sb = new StringBuffer(number * 2);
		for (int i = 0; i < number; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	public ComplexMappingTypeGenerator(final XsdsUtil config,
			final String basicFlowPackageName, final ComplexType complexType,
			final ComplexContentList complexContentList,
			final String eipVersion, final Log log) {
		super(config, basicFlowPackageName, complexType, complexContentList,
				eipVersion, log);
	}

	/**
	 * @param children
	 * @return
	 */
	private List<String> getDefaultDefinitionNames() {
		List<String> names = new ArrayList<String>();
		for (ComplexTypeChild child : this
				.getDefaultingChildren(this.complexType)) {
			names.add(String.format("DEFAULT_%s",
					child.getJavaChildName().toUpperCase()));
		}
		return names;
	}

	private String getConstructor(final List<String> names) {
		StringBuffer sb = new StringBuffer(1024);
		if (names.size() > 0) {
			sb.append("\tpublic ");
			sb.append(this.implName);
			sb.append("() {\n");
			String suffixIn;
			String suffixOut;
			for (String nameIn : names) {
				if (nameIn.startsWith("DEFAULT_IN")) {
					suffixIn = nameIn.substring("DEFAULT_IN".length());
					for (String nameOut : names) {
						if (nameOut.startsWith("DEFAULT_OUT")) {
							suffixOut = nameOut
									.substring("DEFAULT_OUT".length());
							if (suffixIn.equals(suffixOut)) {
								sb.append(
										"\t\tthis.tabularValueMap.put(DEFAULT_IN");
								sb.append(suffixIn);
								sb.append(", DEFAULT_OUT");
								sb.append(suffixIn);
								sb.append(");\n");
							}
						}
					}
				}
			}
			sb.append("\t}\n\n");
		}
		return sb.toString();
	}

	String generateImpl() {
		this.log.debug("+generateImpl");

		String[] propertyNames = getDirectAccessProperties(
				this.complexType.getType().getName().getLocalPart());
		List<ComplexTypeChild> children = this.getChildren();

		boolean returnValueIsList = this.isReturnValueList();
		String returnValueClassName = this.getReturnValueClassName();

		ComplexTypeChild ctc;
		Set<String> importedClasses = this.complexType.getJavaImportClasses();
		if (propertyNames.length > 0 && children != null
				&& children.size() > 0) {
			ctc = children.get(0);
			for (int i = 0; i < propertyNames.length; i++) {
				ctc = ctc.getComplexType().getChild(propertyNames[i]);
				if (ctc != null) {
					importedClasses.addAll(
							ctc.getComplexType().getJavaImportClasses());
				} else {
					break;
				}
			}
		}
		for (ComplexTypeChild child : children) {
			importedClasses
					.addAll(child.getComplexType().getJavaImportClasses());
		}
		List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree = this
				.getChildrenTree();
		importedClasses.addAll(this.getImplImports(childrenTree));
		importedClasses.add(new StringBuffer(this.basicFlowPackageName)
				.append(".FlowContext").toString());

		StringBuffer sb = new StringBuffer(1024);
		sb.append("package ");
		sb.append(this.packageNameImpl);
		sb.append(";\n");
		sb.append("\n");

		List<String> defaultDefinitionNames = this.getDefaultDefinitionNames();
		boolean tabularMapping = defaultDefinitionNames.size() > 1;
		if (tabularMapping) {
			importedClasses.add("java.util.HashMap");
			importedClasses.add("java.util.Map");
		}
		for (String importedClass : importedClasses) {
			sb.append("import ").append(importedClass).append(";\n");
		}

		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * The {@link ");
		sb.append(this.interfaceName);
		sb.append("} implementation.\n");
		if (this.complexType.getAnnotationDocumentation() != null) {
			sb.append(" * <p/>\n");
			sb.append(toJavadocHeader(
					this.complexType.getAnnotationDocumentation()));
		}
		sb.append(" * <p/>\n");
		sb.append(" * This is a ").append(this.getMappingType()).append(".\n");
		sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(),
				this.eipVersion));
		sb.append(" */\n");
		sb.append("@Component\n");
		sb.append("public class ");
		sb.append(this.implName);
		sb.append(" implements ");
		sb.append(this.interfaceName);
		sb.append(" {\n");

		sb.append(this.getDefaultDefinitions("private static final"));

		sb.append("\t/** The {@link ObjectFactory}. */\n");
		sb.append("\tprivate final ObjectFactory of = new ObjectFactory();\n");

		if (tabularMapping) {
			sb.append(
					"\t/** The {@link Map} to support tabular mappings. */\n");
			sb.append(
					"\tprivate final Map<String, String> tabularValueMap = new HashMap<String, String>();\n\n");
			sb.append(this.getConstructor(defaultDefinitionNames));
		}

		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Creates the {@link ");
		sb.append(this.complexType.getClassNameFullQualified());
		sb.append("} defined as \n");
		sb.append("\t * <i>");
		sb.append(this.complexType.getType().getName().getLocalPart());
		sb.append("</i> in name space\n");
		sb.append("\t * <i>");
		sb.append(this.complexType.getType().getName().getNamespaceURI());
		sb.append("</i>.\n");
		sb.append("\t * This name space is stored in file ");
		sb.append(this.config
				.getXsdContainer(this.complexType.getTargetNamespace())
				.getRelativeName());
		sb.append(".\n");
		sb.append(this.getSeeInterfaceJavaDoc(children));
		sb.append("\t */\n");

		sb.append("\tpublic ");
		sb.append(this.complexType.getClassNameFullQualified());
		sb.append(" ");
		sb.append(this.getMethodName());

		String methodArgs = this.getMethodArgs(children);
		sb.append("(");
		sb.append(methodArgs);
		if (methodArgs.trim().length() > 0) {
			sb.append(", ");
		}
		sb.append("FlowContext flowContext");
		sb.append(") {\n");
		sb.append("\t\t");
		sb.append(this.complexType.getClassNameFullQualified());
		sb.append(" mappingType = of.create");
		sb.append(this.complexType.getClassName());
		sb.append("();\n");
		sb.append(this.getSetterStatements("mappingType", children));
		sb.append("\n");

		if (tabularMapping && children.size() == 1
				&& children.get(0).getComplexType().isDirectMappingType()) {
			sb.append("\t\tObject mappedValue = this.tabularValueMap.get(");
			sb.append(children.get(0).getChildName());
			sb.append(".getReturn());\n");

			sb.append("\t\tif (mappedValue == null && ");
			sb.append(children.get(0).getChildName());
			sb.append(".getReturn() != null) {\n");
			sb.append(
					"\t\t\tfor (String key : this.tabularValueMap.keySet()) {\n");
			sb.append("\t\t\t\tif (");
			sb.append(children.get(0).getChildName());
			sb.append(".getReturn().matches(key)) {\n");
			sb.append(
					"\t\t\t\t\tmappedValue = this.tabularValueMap.get(key);\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");

		} else {
			sb.append("\t\tObject mappedValue = null;\n");
		}
		if (returnValueClassName != null) {
			sb.append("\t\t");
			sb.append(returnValueClassName);
			sb.append(" mappedReturnValue;\n");
		}
		if (children.size() == 0) {
			this.log.warn(this.complexType.getType().getName()
					+ " as ComplexMapper does not have children to access");
		}

		String propertiesValue = "mappedValue";

		if (propertyNames.length > 0 && children != null
				&& children.size() > 0) {
			ctc = children.get(0);
			for (int i = 0; i < propertyNames.length; i++) {
				ctc = ctc.getComplexType().getChild(propertyNames[i]);
				if (ctc != null) {
					propertiesValue = ctc.getChildName();
					sb.append("\t\t");
					sb.append(ctc.getJavaVarDefinition());
					sb.append(" = ");
					sb.append(ctc.getJavaDefaultValue());
					sb.append(";\n");
				} else {
					break;
				}
			}
			sb.append(this.getProperty(children.get(0), 0, propertyNames));
		}

		if (children != null) {
			for (ComplexTypeChild child : children) {
				if (child.getJavaImportClass().endsWith("ReferenceDataType")) {
					sb.append("\t\tif (");
					sb.append(propertiesValue);
					sb.append(" != null) {\n");
					sb.append(
							"\t\t\tfor (ReferenceDataType entryOfEnumerations : enumerations) {\n");
					sb.append("\t\t\t\tif (");
					sb.append(propertiesValue);
					sb.append(".equals(entryOfEnumerations.getUUID())) {\n");
					sb.append(
							"\t\t\t\t\tmappedValue = entryOfEnumerations.getName();\n");
					sb.append("\t\t\t\t\tbreak;\n");
					sb.append("\t\t\t\t}\n");
					sb.append("\t\t\t}\n");
					sb.append("\t\t}\n");
					break;
				}
			}
		}

		sb.append("\n");
		if (tabularMapping) {
			if (defaultDefinitionNames.contains("DEFAULT_DEFAULT")) {
				sb.append("\t\tif (mappedValue ==null){\n");
			}
			sb.append("\t\tmappedValue = DEFAULT_DEFAULT;\n");
			sb.append("\t\t}\n");
		}
		sb.append("\t\tmappingType.setValue(mappedValue);\n");

		if (returnValueClassName != null) {
			sb.append("\t\tif (mappedValue !=null){\n");
			sb.append("\t\t\t/* Convert mapped value to ");
			sb.append(returnValueClassName);
			sb.append(". */\n");
			sb.append("\t\t\t//mappedReturnValue = (");
			sb.append(returnValueClassName);
			sb.append(") mappedValue;\n");
			sb.append("\t\t}\n");
			sb.append("\t\tmappingType.setReturn(mappedReturnValue);\n");
		}

		sb.append("\t\treturn mappingType;\n");
		sb.append("\t}\n");
		sb.append("}\n");

		this.log.debug("-generateImpl");
		return sb.toString();
	}

	public void generateImpl(final File outputDirectory) {
		String s = this.generateImpl();

		File f = Util.getFile(outputDirectory, this.packageNameImpl,
				new StringBuffer().append(this.implName).append(".java")
						.toString());
		this.log.info(new StringBuffer().append("Write Impl ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, s);
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingTypeGenerator#getMappingType()
	 */
	@Override
	protected String getMappingType() {
		return "ComplexMappingType";
	}

	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingTypeGenerator#getMethodName()
	 */
	@Override
	protected String getMethodName() {
		return "createMappingType";
	}

	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingTypeGenerator#getPackageNameInterface()
	 */
	@Override
	protected String getPackageNameInterface() {
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.complexType.getPackageName().substring(0,
				this.complexType.getPackageName().lastIndexOf('.')));
		sb.append(".mapper.complex");
		return sb.toString();
	}

	private String getProperty(final ComplexTypeChild object, int index,
			final String[] propertyNames) {
		StringBuffer sb = new StringBuffer(128);
		String tabs = getTabs(index + 2);
		String propertyName = Util.getXjcPropertyName(propertyNames[index]);
		ComplexTypeChild child = object.getComplexType()
				.getChild(propertyNames[index]);
		if (child != null) {
			sb.append("").append(tabs);
			sb.append("if (").append(object.getJavaChildName())
					.append(" != null");
			if (object.isList()) {
				sb.append(" && ").append(object.getJavaChildName())
						.append(".size() > 0");
			}
			sb.append("){\n");
			sb.append(tabs).append("\t/* Get the ");
			sb.append(propertyName);
			sb.append(" of ");
			sb.append(object.getComplexType().getType().getName());
			if (object.isList()) {
				sb.append(" (first entry out of the list)");
			}
			sb.append(". */\n");

			sb.append(tabs).append("\t");
			sb.append(propertyName).append(" = ");
			sb.append(object.getJavaChildName());
			if (object.isList()) {
				sb.append(".get(0)");
			}
			sb.append(".").append(child.getGetterName()).append("();\n");
			index++;
			if (index < propertyNames.length) {
				sb.append(this.getProperty(child, index, propertyNames));
			}

			sb.append(tabs).append("}\n");
		}
		return sb.toString();
	}
}
