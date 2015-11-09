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
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.xmlbeans.SchemaType;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class DirectMappingTypeGenerator extends AbstractMappingTypeGenerator {
	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingTypeGenerator#getMappingType()
	 */
	@Override
	protected String getMappingType() {
		return "DirectMappingType";
	}

	/**
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingTypeGenerator#getPackageNameInterface()
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
	 * @see com.qpark.maven.plugin.flowmapper.AbstractMappingTypeGenerator#getMethodName()
	 */
	@Override
	protected String getMethodName() {
		return "createMappingType";
	}

	public DirectMappingTypeGenerator(final XsdsUtil config,
			final ComplexType complexType,
			final ComplexContentList complexContentList, final Log log) {
		super(config, complexType, complexContentList, log);
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

		String[] propertyNames = getDirectAccessProperties(this.complexType
				.getType().getName().getLocalPart());

		String returnValueClassName = this.getReturnValueClassName();
		boolean returnValueIsList = this.isReturnValueList();

		List<ComplexTypeChild> children = this.getChildren();

		ComplexTypeChild ctc;

		if (propertyNames.length == 0) {
			String msg = new StringBuffer(132)
					.append(this.complexType.getType().getName().getLocalPart())
					.append(" does not provide a direct mapping!").toString();
			this.log.error(msg);
			throw new IllegalStateException(msg);
		}
		if (children.isEmpty()) {
			String msg = new StringBuffer(132)
					.append(this.complexType.getType().getName().getLocalPart())
					.append(" does not contain a child to get the direct mapping value from!")
					.toString();
			this.log.error(msg);
			throw new IllegalStateException(msg);
		}

		ctc = children.get(0);
		for (int i = 0; i < propertyNames.length; i++) {
			ctc = ctc.getComplexType().getChild(propertyNames[i]);
			if (ctc == null) {
				String msg = new StringBuffer(128)
						.append("Can not find the child ")
						.append(propertyNames[i]).append(" in ")
						.append(this.complexType.getType().getName())
						.toString();
				this.log.error(msg);
				throw new IllegalStateException(msg);
			}
		}

		Set<String> importedClasses = this.complexType.getJavaImportClasses();
		ctc = children.get(0);
		for (int i = 0; i < propertyNames.length; i++) {
			ctc = ctc.getComplexType().getChild(propertyNames[i]);
			importedClasses.addAll(ctc.getComplexType().getJavaImportClasses());
		}
		for (ComplexTypeChild child : children) {
			importedClasses.addAll(child.getComplexType()
					.getJavaImportClasses());
		}
		List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree = this
				.getChildrenTree();
		importedClasses.addAll(this.getImplImports(childrenTree));

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
			sb.append(toJavadocHeader(this.complexType
					.getAnnotationDocumentation()));
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

		sb.append(this.getDefaultDefinitions("private static final"));

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

		String targetXjcPropertyName = Util
				.getXjcPropertyName(propertyNames[propertyNames.length - 1]);
		ctc = children.get(0);
		String lastCascadedClassDefinition = null;
		boolean lastCascadedIsList = false;
		for (int i = 0; i < propertyNames.length; i++) {
			ctc = ctc.getComplexType().getChild(propertyNames[i]);
			lastCascadedClassDefinition = ctc.getComplexType().getClassName();
			lastCascadedIsList = ctc.isList();
			sb.append("\t\t");
			sb.append(ctc.getJavaVarDefinition());
			sb.append(" = ");
			sb.append(ctc.getJavaDefaultValue());
			sb.append(";\n");
		}
		sb.append(this.getProperty(children.get(0), 0, propertyNames));

		sb.append("\n\t\tmappingType.setValue(");
		sb.append(targetXjcPropertyName);
		sb.append(");\n");
		if (returnValueClassName != null && lastCascadedClassDefinition != null
				&& returnValueClassName.trim().length() > 0
				&& returnValueClassName.equals(lastCascadedClassDefinition)
				&& returnValueIsList == lastCascadedIsList) {
			if (returnValueIsList) {
				sb.append("\t\tif (");
				sb.append(targetXjcPropertyName);
				sb.append(" != null) {\n");
				sb.append("\t\t\tmappingType.getReturn().addAll(");
				sb.append(targetXjcPropertyName);
				sb.append(");\n");
				sb.append("\t\t}\n");
			} else {
				sb.append("\t\tmappingType.setReturn(");
				sb.append(targetXjcPropertyName);
				sb.append(");\n");
			}
		}

		sb.append("\t\treturn mappingType;\n");
		sb.append("\t}\n");
		sb.append("}\n");
		this.log.debug("-generateImpl");
		return sb.toString();
	}

	public static void main(final String[] args) {
		String xsdPath = "C:\\xnb\\dev\\com.ses.domain.gen\\domain-gen-flow\\target\\model";
		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "mapping flow";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = new XsdsUtil(dif, "a.b.c.bus",
				messagePackageNameSuffix, "delta");
		ComplexContentList complexContentList = new ComplexContentList();
		complexContentList.setupComplexContentLists(xsds);
		System.out
				.println(Util.getDuration(System.currentTimeMillis() - start));

		String source;
		ComplexType ct;
		SchemaType type;
		String ctNamespace;
		String ctName;

		ctNamespace = "http://www.ses.com/Interfaces/MonicsSCDBMappingTypes";
		ctName = "Monics.DefaultCarrierDeleteMappingType";
		// listProperties(type);

		// System.out.println("xx");
		// x(xsds, ctNamespace, ctName);
		//
		// ctNamespace = "http://www.ses.com/Interfaces/CLBTMappingTypes";
		// ctName = "CLBT.DefaultInstanceMappingType";
		// System.out.println("int");
		// x(xsds, ctNamespace, ctName);
		//
		// ctName = "CLBT.DefaultZeroConfigSatMappingType";
		// System.out.println("int");
		// x(xsds, ctNamespace, ctName);
		// ctName = "CLBT.DefaultCarrierMultiAccessMappingType";
		// System.out.println("string");
		// x(xsds, ctNamespace, ctName);
		// ctName = "CLBT.DefaultCarrierThresholdCoverNTypeMappingType";
		// System.out.println("float");
		// x(xsds, ctNamespace, ctName);

		ctName = "Monics.Satellite.orbitMappingType";
		ctNamespace = "http://www.ses.com/Interfaces/MonicsSCDBMappingTypes";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		ComplexMappingTypeGenerator complexMapper = new ComplexMappingTypeGenerator(
				xsds, ct, complexContentList, new SystemStreamLog());
		String[] propertyNames = getDirectAccessProperties(ct.getType()
				.getName().getLocalPart());
		List<ComplexTypeChild> children = complexMapper.getChildren();
		ComplexTypeChild satellite = children.get(0);
		source = complexMapper.generateImpl();

		ctName = "TransferTransponderViewFlowRequestType";
		ctNamespace = "http://www.ses.com/Interfaces/Flow/MonitoringViews";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		FlowInterfaceGenerator flow = new FlowInterfaceGenerator(xsds, ct,
				new SystemStreamLog());
		source = flow.generateInterface("com.ses.domain.flow.x");

		ctName = "Monics.DefaultCurrMonFlagMappingType";
		ctNamespace = "http://www.ses.com/Interfaces/MonicsViewsMappingTypes";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		ctName = "Monics.DefaultCarrierMonitoringMappingType";
		ctNamespace = "http://www.ses.com/Interfaces/MonicsSCDBMappingTypes";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		ctName = "CLBT.DefaultAntennaTemperatureMappingType";
		ctNamespace = "http://www.ses.com/Interfaces/CLBTMappingTypes";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		DefaultMappingTypeGenerator def = new DefaultMappingTypeGenerator(xsds,
				ct, complexContentList, new SystemStreamLog());
		source = def.generateImpl();

		ctName = "CarrierViewTypeMapRequestType";
		ctNamespace = "http://www.ses.com/Interfaces/MonicsViewsMappings";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		// for (ComplexRequestResponse crr : complexContentList
		// .getRequestResponses()) {
		// if (crr.request.getClassName().equals(ct.getClassName())) {
		// MappingOperationGenerator oper = new MappingOperationGenerator(
		// xsds, crr.request, crr.response, complexContentList,
		// new SystemStreamLog());
		// source = oper.generateImpl();
		// break;
		// }
		// }

		ctName = "Beacon.beam.beamPointing.cSMBeamConfigurationMappingType";
		ctNamespace = "http://www.ses.com/Interfaces/SatelliteMappingTypes";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		DirectMappingTypeGenerator direct = new DirectMappingTypeGenerator(
				xsds, ct, complexContentList, new SystemStreamLog());
		source = direct.generateImpl();

		ctName = "CLBT.DefaultDigitalCarrierTypeMappingType";
		ctNamespace = "http://www.ses.com/Interfaces/CLBTMappingTypes";
		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();

		// DefaultMappingTypeGenerator
		def = new DefaultMappingTypeGenerator(xsds, ct, complexContentList,
				new SystemStreamLog());
		source = def.generateImpl();

		System.out.println(source);
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
		sb.append(") {\n");
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
