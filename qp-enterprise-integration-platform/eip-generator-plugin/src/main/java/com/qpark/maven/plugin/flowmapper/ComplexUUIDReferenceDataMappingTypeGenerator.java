/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class ComplexUUIDReferenceDataMappingTypeGenerator
		extends AbstractMappingTypeGenerator {
	private static String[] getDirectAccessProperties(final String name) {
		String[] x = new String[0];
		int index = name.indexOf('.');
		if (index > 0 && name.toLowerCase().endsWith("valuemappingtype")) {
			String s = name.substring(index + 1,
					name.length() - "valuemappingtype".length());
			x = s.split("\\.");
		} else if (index > 0 && name.endsWith("UUIDMappingType")) {
			String s = name.substring(index + 1,
					name.length() - "UUIDMappingType".length());
			x = s.split("\\.");
		} else if (index > 0 && name.endsWith("NameMappingType")) {
			String s = name.substring(index + 1,
					name.length() - "NameMappingType".length());
			x = s.split("\\.");
		} else if (index > 0 && name.endsWith("MappingType")) {
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

	/**
	 * @param config
	 * @param basicFlowPackageName
	 * @param complexContent
	 * @param complexContentList
	 * @param eipVersion
	 * @param compileableSourceDirectory
	 * @param preparedSourceDirectory
	 * @param log
	 */
	public ComplexUUIDReferenceDataMappingTypeGenerator(final XsdsUtil config,
			final String basicFlowPackageName,
			final ComplexContent complexContent,
			final ComplexContentList complexContentList,
			final String eipVersion, final File compileableSourceDirectory,
			final File preparedSourceDirectory, final Log log) {
		super(config, basicFlowPackageName, complexContent, complexContentList,
				eipVersion, compileableSourceDirectory, preparedSourceDirectory,
				log);
	}

	@Override
	public void generateImpl() {
		String s = this.generateImplContent();

		File f = Util.getFile(this.compileableSourceDirectory,
				this.packageNameImpl, new StringBuffer().append(this.implName)
						.append(".java").toString());
		this.log.debug(new StringBuffer().append("Write Impl ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, s);
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	String generateImplContent() {
		this.log.debug("+generateImpl");
		boolean isRefenenceUUIDValueMappingType = this.complexType
				.getClassName().toLowerCase().endsWith("valuemappingtype");
		boolean isRefenenceUUIDUUIDMappingType = this.complexType.getClassName()
				.endsWith("UUIDMappingType");
		boolean isRefenenceUUIDNameMappingType = this.complexType.getClassName()
				.endsWith("NameMappingType");

		/* Check general naming. */
		AbstractGenerator.getDirectAccessProperties(this.complexType);

		String[] propertyNames = getDirectAccessProperties(
				this.complexType.getType().getName().getLocalPart());

		List<ComplexTypeChild> children = this.getChildren();
		if (children == null || children.size() == 0) {
			throw new InvalidComplexUUIDMappingTypeException(this.complexType,
					"Not any parameter defined to get the UUID Description");
		}

		if (propertyNames == null || propertyNames.length == 0) {
			throw new InvalidComplexUUIDMappingTypeException(this.complexType,
					"Not any parameter field defined to get the UUID Description");
		}

		Set<String> importedClasses = this.complexType.getJavaImportClasses();
		ComplexTypeChild ctc = children.get(0);
		if (Objects.nonNull(ctc)) {
			for (String propertyName : propertyNames) {
				ctc = ctc.getComplexType().getChild(propertyName);
				if (Objects.nonNull(ctc)) {
					importedClasses.addAll(
							ctc.getComplexType().getJavaImportClasses());
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

		String defaultDefinitions = this
				.getDefaultDefinitions("private static final");
		if (defaultDefinitions.length() > 0) {
			sb.append(defaultDefinitions);
		} else {
			throw new MissingDefaultException(this.complexType);
		}
		sb.append("\t/** The {@link ObjectFactory}. */\n");
		sb.append("\tprivate final ObjectFactory of = new ObjectFactory();\n");

		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Creates the {@link ");
		sb.append(this.complexType.getClassName());
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
		sb.append(this.complexType.getClassName());
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

		String uuidPropertyName = new StringBuffer(32).append(propertyNames[0])
				// .append("UUIDValue")
				.toString();
		String uuidMappedPropertyName = "mappedUUIDValue";
		sb.append("\t\tString ");
		sb.append(uuidMappedPropertyName);
		sb.append(" = null;\n");

		ctc = children.get(0);

		for (String propertyName : propertyNames) {
			ctc = ctc.getComplexType().getChild(propertyName);
			if (Objects.nonNull(ctc)) {
				sb.append("\t\t");
				sb.append(ctc.getJavaVarDefinition());
				sb.append(" = ");
				sb.append(ctc.getJavaDefaultValue());
				sb.append(";\n");
			}
		}
		sb.append(this.getProperty(children.get(0), 0, propertyNames));

		if (children != null && !isRefenenceUUIDUUIDMappingType) {
			for (ComplexTypeChild child : children) {
				if (child.getJavaImportClass().endsWith("ReferenceDataType")) {
					sb.append("\t\tif (");
					sb.append(uuidPropertyName);
					sb.append(" != null) {\n");
					sb.append(
							"\t\t\tfor (ReferenceDataType entryOfEnumerations : enumerations) {\n");
					sb.append("\t\t\t\tif (");
					sb.append(uuidPropertyName);
					sb.append(".equals(entryOfEnumerations.getUUID())) {\n");
					sb.append("\t\t\t\t\t");
					sb.append(uuidMappedPropertyName);
					if (isRefenenceUUIDValueMappingType) {
						sb.append(" = entryOfEnumerations.getValue();\n");
					} else if (isRefenenceUUIDNameMappingType) {
						sb.append(" = entryOfEnumerations.getName();\n");
					} else {
						sb.append(" = entryOfEnumerations.getName();\n");
					}
					sb.append("\t\t\t\t\tbreak;\n");
					sb.append("\t\t\t\t}\n");
					sb.append("\t\t\t}\n");
					sb.append("\t\t}\n");
					break;
				}
			}
		} else if (isRefenenceUUIDUUIDMappingType) {
			sb.append("\t\t");
			sb.append(uuidMappedPropertyName);
			sb.append(" = ");
			sb.append(uuidPropertyName);
			sb.append(";\n");
		}

		sb.append("\n");
		sb.append("\t\tmappingType.setValue(");
		sb.append(uuidMappedPropertyName);
		sb.append(");\n");
		sb.append("\t\tmappingType.setReturn(");
		sb.append(uuidMappedPropertyName);
		sb.append(");\n");

		sb.append("\t\treturn mappingType;\n");
		sb.append("\t}\n");
		sb.append("}\n");

		this.log.debug("-generateImpl");
		return sb.toString();
	}

	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingTypeGenerator#getMappingType()
	 */
	@Override
	protected String getMappingType() {
		return "ComplexUUIDMappingType";
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

	private String getProperty(final ComplexTypeChild object, final int index,
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
			int nextIndex = index + 1;
			if (nextIndex < propertyNames.length) {
				sb.append(this.getProperty(child, nextIndex, propertyNames));
			}

			sb.append(tabs).append("}\n");
		}
		return sb.toString();
	}
}
