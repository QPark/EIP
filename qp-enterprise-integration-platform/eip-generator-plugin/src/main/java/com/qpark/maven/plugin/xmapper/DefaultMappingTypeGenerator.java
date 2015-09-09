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
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class DefaultMappingTypeGenerator extends AbstractMappingTypeGenerator {
	/**
	 * @see com.qpark.maven.plugin.xmapper.AbstractMappingTypeGenerator#getMappingType()
	 */
	@Override
	protected String getMappingType() {
		return "DefaultMappingType";
	}

	/**
	 * @see com.qpark.maven.plugin.xmapper.AbstractMappingTypeGenerator#getPackageNameInterface()
	 */
	@Override
	protected String getPackageNameInterface() {
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.complexType.getPackageName().substring(0,
				this.complexType.getPackageName().lastIndexOf('.')));
		sb.append(".mapper.direct");
		return sb.toString();
	}

	/**
	 * @see com.qpark.maven.plugin.xmapper.AbstractMappingTypeGenerator#getMethodName()
	 */
	@Override
	protected String getMethodName() {
		return "createMappingType";
	}

	public DefaultMappingTypeGenerator(final XsdsUtil config,
			final ComplexType complexType, final Log log) {
		super(config, complexType, log);
	}

	public void generateImpl(final File outputDirectory) {
		String s = this.generateImpl();
		File f = Util.getFile(outputDirectory, this.packageNameImpl,
				new StringBuffer().append(this.implName).append(".java")
						.toString());
		this.log.info(new StringBuffer().append("Write Impl ").append(
				f.getAbsolutePath()));
		try {
			Util.writeToFile(f, s);
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private String generateImpl() {
		this.log.debug("+generateImpl");

		List<ComplexTypeChild> children = this.getChildren();

		Set<String> importedClasses = this.complexType.getJavaImportClasses();
		for (ComplexTypeChild child : children) {
			importedClasses.addAll(child.getComplexType()
					.getJavaImportClasses());
		}
		List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree = this
				.getChildrenTree();
		importedClasses.addAll(this.getImplImports(childrenTree));

		String defaultValue = "";
		SchemaProperty defaultProperty = this.complexType.getType()
				.getElementProperties()[0];
		if (defaultProperty.getType().isSimpleType()
				&& defaultProperty.getType().getEnumerationValues() != null
				&& defaultProperty.getType().getEnumerationValues().length == 1) {
			defaultValue = defaultProperty.getType().getEnumerationValues()[0]
					.getStringValue();
		} else {
			defaultValue = defaultProperty.getDefaultText();
		}
		Class<?> defaultValueClass = XsdsUtil
				.getBuildInBaseTypeClass(defaultProperty.getType()
						.getBaseType());
		if (!defaultValueClass.isPrimitive()
				&& !defaultValueClass.getPackage().getName()
						.equals("java.lang")) {
			importedClasses.add(defaultValueClass.getName());
		}

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
			sb.append(" * ");
			sb.append(this.complexType.getAnnotationDocumentation());
			sb.append(".\n");
		}
		sb.append(" * <p/>\n");
		sb.append(" * The returned {@link ");
		sb.append(this.complexType.getClassName());
		sb.append("} is defined as \n");
		sb.append(" * <i>");
		sb.append(this.complexType.getType().getName().getLocalPart());
		sb.append("</i> in name space\n");
		sb.append(" * <i>");
		sb.append(this.complexType.getType().getName().getNamespaceURI());
		sb.append("</i>\n");
		sb.append(" * (see ");
		sb.append(this.config.getXsdContainerMap(
				this.complexType.getTargetNamespace()).getRelativeName());
		sb.append(").\n");
		sb.append(" * <p/>\n");
		sb.append(" * This is a ").append(this.getMappingType()).append(".\n");
		sb.append(" * <pre>");
		sb.append(Util.getGeneratedAt());
		sb.append("</pre>\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("@Component\n");
		sb.append("public class ");
		sb.append(this.implName);
		sb.append(" implements ");
		sb.append(this.interfaceName);
		sb.append(" {\n");

		sb.append("\t/** The default value to set. */\n");
		sb.append("\tpublic static final ");
		sb.append(defaultValueClass.getSimpleName());
		sb.append(" DEFAULT_VALUE = ");
		if (defaultValueClass.isPrimitive()) {
			sb.append(defaultValue);
		} else {
			sb.append(defaultValueClass.getSimpleName());
			sb.append(".valueOf(\"");
			sb.append(defaultValue);
			sb.append("\")");
		}
		sb.append(";\n");
		sb.append(this.getDefaultDefinitions("private static final"));
		sb.append("\n");

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
		sb.append(this.config.getXsdContainerMap(
				this.complexType.getTargetNamespace()).getRelativeName());
		sb.append(".\n");

		sb.append(this.getSeeInterfaceJavaDoc(children));
		sb.append("\t */\n");

		sb.append("\tpublic ");
		sb.append(this.complexType.getClassName());
		sb.append(" ");
		sb.append(this.getMethodName());
		sb.append("(");
		sb.append(this.getMethodArgs(children));
		sb.append(") {\n");
		sb.append("\t\t");
		sb.append(this.complexType.getClassName());
		sb.append(" mappingType = of.create");
		sb.append(this.complexType.getClassName());
		sb.append("();\n");
		sb.append(this.getSetterStatements("mappingType", children));
		sb.append("\n");

		sb.append("\n\t\tmappingType.setValue(DEFAULT_VALUE);\n");

		sb.append("\t\treturn mappingType;\n");
		sb.append("\t}\n");
		sb.append("}\n");
		this.log.debug("-generateImpl");
		return sb.toString();
	}

	public static void main(final String[] args) {
		String xsdPath = "C:\\xnb\\dev\\com.ses.domain-trx\\mapping\\target\\model";
		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "mapping flow";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = new XsdsUtil(dif, "a.b.c.bus",
				messagePackageNameSuffix, "delta");
		System.out
				.println(Util.getDuration(System.currentTimeMillis() - start));
		String ctNamespace = "http://www.ses.com/Interfaces/MonicsSCDBMappingTypes";
		String ctName = "Monics.DefaultCarrierDeleteMappingType";

		ComplexType ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		SchemaType type = ct.getType();
		listProperties(type);
		System.exit(0);
		ComplexMappingTypeGenerator gen = new ComplexMappingTypeGenerator(xsds,
				ct, new SystemStreamLog());
		String[] propertyNames = getDirectAccessProperties(ct.getType()
				.getName().getLocalPart());
		List<ComplexTypeChild> children = gen.getChildren();
		ComplexTypeChild satellite = children.get(0);

		System.out.println(gen.generateImpl());
	}

	private static void listProperties(final SchemaType type) {
		Object[] empty = new Object[0];
		Method[] ms = SchemaType.class.getMethods();
		for (Method m : ms) {
			if (
			// m.getModifiers() == Modifier.PUBLIC
			// &&
			m.getParameterTypes().length == 0) {
				try {
					System.out.println(m.getName() + " "
							+ m.invoke(type, empty));
				} catch (Exception e) {
					System.out.println("\t" + e.getMessage());
				}
			}
		}
	}

	private String getProperty(final ComplexTypeChild object, int index,
			final String[] propertyNames) {
		StringBuffer sb = new StringBuffer(128);
		String tabs = getTabs(index + 2);
		String propertyName = Util.getXjcPropertyName(propertyNames[index]);
		ComplexTypeChild child = object.getComplexType().getChild(
				propertyNames[index]);

		sb.append("").append(tabs);
		sb.append("if (").append(object.getJavaChildName()).append(" != null");
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

		return sb.toString();
	}

	private static String getTabs(final int number) {
		StringBuffer sb = new StringBuffer(number * 2);
		for (int i = 0; i < number; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

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
}
