/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class TabularMappingTypeGenerator extends ComplexMappingTypeGenerator {

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

	public TabularMappingTypeGenerator(final XsdsUtil config,
			final String basicFlowPackageName,
			final ComplexContent complexContent,
			final ComplexContentList complexContentList,
			final String eipVersion, final File compileableSourceDirectory,
			final File preparedSourceDirectory, final Log log) {
		super(config, basicFlowPackageName, complexContent, complexContentList,
				eipVersion, compileableSourceDirectory, preparedSourceDirectory,
				log);
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

	private String getConstructor(final List<String> names,
			final Class<?> inputValueClass, final Class<?> returnValueClass) {
		StringBuffer sb = new StringBuffer(1024);
		if (names.size() > 0) {
			sb.append("\t/**\n\t * Create the {@link ");
			sb.append(this.implName);
			sb.append(" and setup the tabular mapping @{link Map}.\n\t */\n");
			sb.append("\tpublic ");
			sb.append(this.implName);
			sb.append("() {\n");
			String suffixIn;
			String suffixOut;
			for (String nameIn : names) {
				if (nameIn.startsWith("DEFAULT_KEY")) {
					suffixIn = nameIn.substring("DEFAULT_KEY".length());
					for (String nameOut : names) {
						if (nameOut.startsWith("DEFAULT_VALUE")) {
							suffixOut = nameOut
									.substring("DEFAULT_VALUE".length());
							if (suffixIn.equals(suffixOut)) {
								sb.append("\t\tthis.tabularValueMap.put(");

								sb.append(getValueInstance(inputValueClass,
										String.format("DEFAULT_KEY%s",
												suffixIn)));
								sb.append(", ");
								sb.append(getValueInstance(returnValueClass,
										String.format("DEFAULT_VALUE%s",
												suffixIn)));

								sb.append(");\n");
							}
						}
					}
				}
			}
			sb.append("\t}\n");
		}
		return sb.toString();
	}

	private static String getValueInstance(final Class<?> valueClass,
			final String valueDefinition) {
		StringBuffer sb = new StringBuffer(256);
		if (valueClass.equals(String.class)) {
			sb.append(valueDefinition);
		} else if (valueClass.equals(BigDecimal.class)) {
			sb.append("new java.math.BigDecimal(");
			sb.append(valueDefinition);
			sb.append(".trim())");
		} else if (valueClass.equals(BigInteger.class)) {
			sb.append("new java.math.BigInteger(");
			sb.append(valueDefinition);
			sb.append(".trim())");
		} else {
			sb.append(valueClass.getName());
			sb.append(".valueOf(");
			sb.append(valueDefinition);
			sb.append(".trim())");
		}
		return sb.toString();
	}

	private ComplexTypeChild getInputValueReturnChild() {
		ComplexTypeChild returnChild = null;
		List<ComplexTypeChild> children = this.getChildren();
		ComplexTypeChild input = null;
		if (children.size() == 1) {
			input = children.get(0);
			if (input != null) {
				returnChild = this.getReturnChild(input.getComplexType());
			}
		}
		return returnChild;
	}

	private String getInputValueName() {
		String inputValueName = "inputValueName";
		List<ComplexTypeChild> children = this.getChildren();
		ComplexTypeChild input = null;
		if (children.size() == 1) {
			input = children.get(0);
			if (input != null) {
				inputValueName = input.getChildName();
			}
		}
		return inputValueName;
	}

	private boolean isValidTabularMapping() {
		ComplexTypeChild inputValueReturnChild = this
				.getInputValueReturnChild();
		boolean tabularMapping = this.getDefaultDefinitionNames().size() > 1
				&& inputValueReturnChild != null;
		return tabularMapping;
	}

	@Override
	String generateImplContent() {
		this.log.debug("+generateImpl");
		List<String> defaultDefinitionNames = this.getDefaultDefinitionNames();
		ComplexTypeChild inputValueReturnValue = this
				.getInputValueReturnChild();
		String inputValueName = this.getInputValueName();

		List<ComplexTypeChild> children = this.getChildren();
		if (children.size() == 0) {
			this.log.warn(this.complexType.getType().getName()
					+ " as ComplexMapper does not have children to access");
		}

		Class<?> inputValueClass = String.class;
		if (inputValueReturnValue != null) {
			inputValueClass = XsdsUtil.getBuildInJavaClass(
					inputValueReturnValue.getComplexType().getType().getName());
			if (inputValueClass == null) {
				inputValueClass = String.class;
			}
		}
		boolean inputValueIsPrimitive = inputValueClass.isPrimitive();
		if (inputValueIsPrimitive) {
			inputValueClass = ClassUtils.primitiveToWrapper(inputValueClass);
		}
		String inputValueClassName = inputValueClass.getName();
		if (inputValueClassName.startsWith("java.lang.")) {
			inputValueClassName = inputValueClassName
					.substring("java.lang.".length());
		}

		ComplexTypeChild returnValueChild = this.getReturnChild();
		Class<?> returnValueClass = XsdsUtil.getBuildInJavaClass(
				returnValueChild.getComplexType().getType().getName());
		if (returnValueClass == null) {
			returnValueClass = String.class;
		}
		boolean returnValueIsPrimitive = returnValueClass.isPrimitive();
		if (returnValueIsPrimitive) {
			returnValueClass = ClassUtils.primitiveToWrapper(returnValueClass);
		}
		String returnValueClassName = returnValueClass.getName();
		if (returnValueClassName.startsWith("java.lang.")) {
			returnValueClassName = returnValueClassName
					.substring("java.lang.".length());
		}

		ComplexTypeChild ctc;
		Set<String> importedClasses = this.complexType.getJavaImportClasses();
		String[] propertyNames = getDirectAccessProperties(
				this.complexType.getType().getName().getLocalPart());
		if (propertyNames.length > 0 && children.size() > 0) {
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

		List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree = new ArrayList<Entry<ComplexTypeChild, List<ComplexTypeChild>>>();

		importedClasses.addAll(this.getImplImports(childrenTree));
		importedClasses.add(new StringBuffer(this.basicFlowPackageName)
				.append(".FlowContext").toString());

		StringBuffer sb = new StringBuffer(1024);
		sb.append("package ");
		sb.append(this.packageNameImpl);
		sb.append(";\n");
		sb.append("\n");

		importedClasses.add(
				String.format("%s.%s", this.packageName, this.interfaceName));
		importedClasses.add("java.util.HashMap");
		importedClasses.add("java.util.Map");
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

		sb.append("\t/** The {@link ObjectFactory}. */\n");
		sb.append("\tprivate final ObjectFactory of = new ObjectFactory();\n");

		sb.append("\t/** The {@link Map} to support tabular mappings. */\n");
		sb.append("\tprivate final Map<");
		sb.append(inputValueClassName);
		sb.append(", ");
		sb.append(returnValueClassName);
		sb.append("> tabularValueMap = new HashMap<");
		sb.append(inputValueClassName);
		sb.append(", ");
		sb.append(returnValueClassName);
		sb.append(">();\n");

		sb.append("\n");
		sb.append(this.getConstructor(defaultDefinitionNames, inputValueClass,
				returnValueClass));

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

		sb.append("\t\t");
		sb.append(returnValueClassName);
		sb.append(" mappedValue = this.tabularValueMap.get(");
		sb.append(inputValueName);
		sb.append(".getReturn());\n");

		if (inputValueClass.equals(String.class)) {
			sb.append("\t\tif (mappedValue == null && ");
			sb.append(inputValueName);
			sb.append(".getReturn() != null) {\n");
			sb.append(
					"\t\t\tfor (String key : this.tabularValueMap.keySet()) {\n");
			sb.append("\t\t\t\tif (");
			sb.append(inputValueName);
			sb.append(".getReturn().matches(key)) {\n");
			sb.append(
					"\t\t\t\t\tmappedValue = this.tabularValueMap.get(key);\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");
		}

		sb.append("\n");

		if (defaultDefinitionNames.contains("DEFAULT_DEFAULT")) {
			sb.append("\t\tif (mappedValue == null){\n");
			sb.append("\t\t\tmappedValue = ");
			sb.append(getValueInstance(returnValueClass, "DEFAULT_DEFAULT"));
			sb.append(";\n");
			sb.append("\t\t}\n");
			sb.append("\n");
		}

		if (returnValueIsPrimitive) {
			sb.append("\t\tif (mappedValue != null) {\n");
			sb.append("\t\t\tmappingType.setValue(mappedValue);\n");
			sb.append("\t\t\tmappingType.setReturn(mappedValue);\n");
			sb.append("\t\t}\n");
		} else {
			sb.append("\t\tmappingType.setValue(mappedValue);\n");
			sb.append("\t\tmappingType.setReturn(mappedValue);\n");
		}
		sb.append("\t\treturn mappingType;\n");
		sb.append("\t}\n");
		sb.append("}\n");

		this.log.debug("-generateImpl");
		return sb.toString();
	}

	@Override
	public void generateImpl() {
		File output = this.compileableSourceDirectory;
		String s = this.generateImplContent();
		if (this.isValidTabularMapping()) {
			output = this.compileableSourceDirectory;
		} else {
			output = this.preparedSourceDirectory;
		}
		File f = Util.getFile(output, this.packageNameImpl, new StringBuffer()
				.append(this.implName).append(".java").toString());
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
}
