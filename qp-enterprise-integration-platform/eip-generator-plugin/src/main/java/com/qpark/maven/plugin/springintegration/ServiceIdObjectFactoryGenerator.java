/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import java.io.File;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdEntry;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * @author bhausen
 */
public class ServiceIdObjectFactoryGenerator {
	/** Parsed complex types. */
	private final TreeSet<ElementType> elementTypes;
	/** The {@link Log}. */
	private final Log log;
	/** The {@link XsdsUtil}. */
	private final XsdsUtil xsds;
	/** The output directory. */
	private final File outputDirectory;
	private final String serviceId;

	/**
	 * @param config
	 * @param complexTypes
	 */
	public ServiceIdObjectFactoryGenerator(final XsdsUtil xsds,
			final String serviceId, final File outputDirectory, final Log log) {
		this.xsds = xsds;
		this.serviceId = serviceId;
		this.outputDirectory = outputDirectory;
		this.log = log;
		this.elementTypes = xsds.getElementTypes();
	}

	public void generate() {
		this.log.debug("+generate");
		StringBuffer imports = new StringBuffer();
		TreeSet<String> contextPath = new TreeSet<String>();
		StringBuffer methods = new StringBuffer();
		StringBuffer objectFactories = new StringBuffer();
		ServiceIdEntry entry = ServiceIdRegistry
				.getServiceIdEntry(this.serviceId);
		if (entry != null) {
			XsdContainer xc = this.xsds
					.getXsdContainer(entry.getTargetNamespace());
			String className = new StringBuffer(32)
					.append(ServiceIdRegistry.capitalize(this.serviceId))
					.append("ObjectFactory").toString();

			imports.append("import javax.xml.bind.JAXBElement;\n");

			TreeMap<String, String> ofMap = new TreeMap<String, String>();
			TreeSet<String> methodNames = new TreeSet<String>();
			ComplexType ct;
			String ofName;
			String eName;
			String pOfName;
			String mName;
			for (ElementType element : this.elementTypes) {
				if (element.getServiceId().equals(this.serviceId)) {
					pOfName = element.getPackageName();
					contextPath.add(pOfName);
					ofName = ofMap.get(pOfName);
					if (ofName == null) {
						ofName = pOfName.replace('.', '_');
						objectFactories.append("\n\t/* The {@link ");
						objectFactories.append(pOfName);
						objectFactories.append(".ObjectFactory}. */");
						objectFactories.append("\n\tprivate ").append(pOfName)
								.append(".ObjectFactory ").append(ofName)
								.append(" = new ").append(pOfName)
								.append(".ObjectFactory();");
						ofMap.put(pOfName, ofName);
					}
					eName = element.getClassNameObject();
					this.log.debug("new ComplexType "
							+ element.getElement().getType());
					this.log.debug(" packagename " + this.xsds.getPackageName(
							element.getElement().getType().getName()));
					ct = new ComplexType(element.getElement().getType(),
							this.xsds);
					if (ct.getPackageName().length() > 0) {
						mName = new StringBuffer(6 + eName.length())
								.append("create").append(eName).toString();
						if (methodNames.contains(mName)) {
							mName = new StringBuffer(
									6 + eName.length() + pOfName.length())
											.append("create")
											.append(Util.capitalizePackageName(
													pOfName))
											.append(eName).toString();
						}

						methodNames.add(mName);

						methods.append("\n\t/**");
						methods.append("\n\t * @param value a {@link ");
						methods.append(ct.getClassNameFullQualified());
						methods.append("}.");
						methods.append(
								"\n\t * @return a new {@link JAXBElement} containing a {@link ");
						methods.append(ct.getClassNameFullQualified());
						methods.append("}.");

						methods.append("\n\t * @see ");
						methods.append(pOfName);
						methods.append(".ObjectFactory#create");
						methods.append(eName);
						methods.append("(");
						methods.append(ct.getClassNameFullQualified());
						methods.append(")");

						methods.append("\n\t */");
						methods.append("\n\tpublic JAXBElement<");
						methods.append(ct.getClassNameFullQualified());
						methods.append("> ").append(mName);
						methods.append("(final ");
						methods.append(ct.getClassNameFullQualified());
						methods.append(" value) {");

						methods.append("\n\t\treturn this.");
						methods.append(ofName).append(".create");
						methods.append(eName);
						methods.append("(value);");

						methods.append("\n\t}\n");
					}
				}
			}

			String cName;
			String mOfName;
			String targetNamespace;

			for (ComplexType type : this.xsds.getComplexTypes()) {
				targetNamespace = type.getTargetNamespace();
				if (type.getPackageName().equals(entry.getPackageName())
						|| xc.getImportedTargetNamespaces()
								.contains(targetNamespace)) {
					if (!type.isAbstractType() && !type.isEnumType()
							&& !type.isSimpleType() && !type.isPrimitiveType()
							&& !type.isInnerTypeDefinition()) {
						pOfName = type.getPackageName();
						ofName = ofMap.get(pOfName);
						if (ofName == null) {
							ofName = pOfName.replace('.', '_');
							objectFactories.append("\n\t/* The {@link ");
							objectFactories.append(pOfName);
							objectFactories.append(".ObjectFactory}. */");
							objectFactories.append("\n\tprivate ")
									.append(pOfName).append(".ObjectFactory ")
									.append(ofName).append(" = new ")
									.append(pOfName)
									.append(".ObjectFactory();");
							ofMap.put(pOfName, ofName);
						}
						cName = type.getClassName();
						mName = new StringBuffer(6 + cName.length())
								.append("create").append(cName).toString()
								.replaceAll("\\.", "");
						mOfName = mName;
						cName = type.getClassNameFullQualified();
						if (methodNames.contains(mName)) {
							mName = new StringBuffer(
									6 + cName.length() + pOfName.length())
											.append("create")
											.append(Util.capitalizePackageName(
													pOfName))
											.append(cName).toString()
											.replaceAll("\\.", "");
						}

						methodNames.add(mName);

						methods.append("\n\t/**\n\t * @return a new {@link ");
						methods.append(cName);
						methods.append("}.");

						methods.append("\n\t * @see ");
						methods.append(pOfName);
						methods.append(".ObjectFactory#");
						methods.append(mOfName);

						methods.append("\n\t */");
						methods.append("\n\tpublic ");
						methods.append(cName).append(" ");
						methods.append(mName);
						methods.append("() {");

						methods.append("\n\t\treturn this.");
						methods.append(ofName);
						methods.append(".");
						methods.append(mOfName);
						methods.append("();");

						methods.append("\n\t}\n");
					}
				}
			}

			File f = Util.getFile(this.outputDirectory,
					this.xsds.getBasePackageName(), new StringBuffer(64)
							.append(className).append(".java").toString());
			this.log.info(new StringBuffer().append("Write ")
					.append(f.getAbsolutePath()));

			try {

				StringBuffer sb = new StringBuffer(1024);
				sb.append("package ");
				sb.append(this.xsds.getBasePackageName());
				sb.append(";\n");
				sb.append("\n");
				sb.append(imports.toString());
				sb.append("\n");
				sb.append("\n/**\n * The {@link ");
				sb.append(className);
				sb.append(
						"} contains the creation of the objects defined\n * in the target name space <i>");
				sb.append(entry.getTargetNamespace());
				sb.append("</i>\n * and imported xsds.\n");
				sb.append(" * <pre>");
				sb.append(Util.getGeneratedAt());
				sb.append("</pre>\n");
				sb.append(" * @author bhausen\n");
				sb.append(" */\n");
				sb.append("public class ");
				sb.append(className);
				sb.append(" {");
				sb.append(objectFactories.toString());
				sb.append("\n");
				sb.append(methods.toString());
				sb.append("}");
				Util.writeToFile(f, sb.toString());
			} catch (Exception e) {
				this.log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		this.log.debug("-generate");
	}

}
