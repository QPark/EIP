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
package com.qpark.maven.plugin.objectfactories;

import java.io.File;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * @author bhausen
 */
public class ServiceObjectFactoryGenerator {
	/** Parsed complex types. */
	private final TreeSet<ElementType> elementTypes;
	/** The {@link Log}. */
	private final Log log;
	/** The {@link XsdsUtil}. */
	private final XsdsUtil xsds;
	/** The output directory. */
	private final File outputDirectory;
	private final String messagePackageNameSuffix;

	/**
	 * @param config
	 * @param complexTypes
	 */
	public ServiceObjectFactoryGenerator(final XsdsUtil xsds,
			final File outputDirectory, final String messagePackageNameSuffix,
			final Log log) {
		this.xsds = xsds;
		this.outputDirectory = outputDirectory;
		if (messagePackageNameSuffix != null
				&& messagePackageNameSuffix.trim().length() > 0) {
			this.messagePackageNameSuffix = messagePackageNameSuffix;
		} else {
			this.messagePackageNameSuffix = "msg restmsg";
		}
		this.log = log;
		this.elementTypes = xsds.getElementTypes();
	}

	public void generate() {
		this.log.debug("+generate");
		StringBuffer imports = new StringBuffer();
		TreeSet<String> contextPath = new TreeSet<String>();
		StringBuffer methods = new StringBuffer();
		StringBuffer objectFactories = new StringBuffer();

		imports.append("\nimport javax.xml.bind.JAXBElement;\n");
		// imports.append("\nimport
		// org.springframework.stereotype.Component;\n");

		TreeMap<String, String> importedModels = new TreeMap<String, String>();
		for (Entry<String, XsdContainer> xx : this.xsds.getXsdContainerMap()
				.entrySet()) {
			if (XsdsUtil.isMessagePackageName(xx.getValue().getPackageName(),
					this.messagePackageNameSuffix)) {
				for (String imp : xx.getValue().getImportedTargetNamespaces()) {
					importedModels.put(xx.getKey(),
							xx.getValue().getFile().getAbsolutePath());
				}
			}
		}

		TreeMap<String, String> ofMap = new TreeMap<String, String>();
		TreeSet<String> methodNames = new TreeSet<String>();
		ComplexType ct;
		String ofName;
		String eName;
		String pOfName;
		String mName;
		for (ElementType element : this.elementTypes) {
			if (XsdsUtil.isMessagePackageName(element.getPackageName(),
					this.messagePackageNameSuffix)) {
				pOfName = element.getPackageName();
				contextPath.add(pOfName);
				ofName = ofMap.get(pOfName);
				if (ofName == null) {
					ofName = pOfName.replace('.', '_');
					objectFactories.append("\n\tprivate ").append(pOfName)
							.append(".ObjectFactory ").append(ofName)
							.append(" = new ").append(pOfName)
							.append(".ObjectFactory();");
					ofMap.put(pOfName, ofName);
				}
				eName = element.getClassNameObject();
				this.log.debug(
						"new ComplexType " + element.getElement().getType());
				this.log.debug(" packagename " + this.xsds.getPackageName(
						element.getElement().getType().getName()));
				ct = new ComplexType(element.getElement().getType(), this.xsds);
				if (ct.getPackageName().length() > 0) {
					// imports.append("\nimport ")
					// .append(ct.getClassNameFullQualified()).append(";");
					mName = new StringBuffer(6 + eName.length())
							.append("create").append(eName).toString();
					if (methodNames.contains(mName)) {
						mName = new StringBuffer(
								6 + eName.length() + pOfName.length())
										.append("create")
										.append(Util
												.capitalizePackageName(pOfName))
										.append(eName).toString();
					}

					methodNames.add(mName);

					methods.append("\n\t/**");
					methods.append("\n\t * @param value a {@link ");
					methods.append(ct.getClassNameFullQualified());
					methods.append("}");
					methods.append(
							"\n\t * @return a new {@link JAXBElement} containing a {@link ");
					methods.append(ct.getClassNameFullQualified());
					methods.append("}");

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
		QName tns;
		String cName;
		String pName;
		String mOfName;

		for (ComplexType type : this.xsds.getComplexTypes()) {
			if (XsdsUtil.isMessagePackageName(type.getPackageName(),
					this.messagePackageNameSuffix)) {
				tns = type.getType().getName();
				if (!type.isAbstractType() && !type.isEnumType()
						&& !type.isSimpleType() && !type.isPrimitiveType()
						&& (tns == null || importedModels
								.containsKey(tns.getNamespaceURI()))) {
					pName = type.getPackageName();
					ofName = ofMap.get(pName);
					if (ofName == null) {
						ofName = pName.replace('.', '_');
						objectFactories.append("\n\tprivate ").append(pName)
								.append(".ObjectFactory ").append(ofName)
								.append(" = new ").append(pName)
								.append(".ObjectFactory();");
						ofMap.put(pName, ofName);
					}
					cName = type.getClassName();
					mName = new StringBuffer(6 + cName.length())
							.append("create").append(cName).toString()
							.replaceAll("\\.", "");
					mOfName = mName;
					cName = type.getClassNameFullQualified();
					if (methodNames.contains(mName)) {
						mName = new StringBuffer(
								6 + cName.length() + pName.length())
										.append("create")
										.append(Util
												.capitalizePackageName(pName))
										.append(cName).toString()
										.replaceAll("\\.", "");
					}

					methodNames.add(mName);

					methods.append("\n\t/**\n\t * @return a new {@link ");
					methods.append(cName);
					methods.append("}");

					methods.append("\n\t * @see ");
					methods.append(pName);
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
				this.xsds.getBasePackageName(), "ServiceObjectFactory.java");
		this.log.info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));

		try {

			StringBuffer sb = new StringBuffer(1024);
			sb.append("package ");
			sb.append(this.xsds.getBasePackageName());
			sb.append(";\n\n");
			sb.append("\n");
			sb.append(imports.toString());
			sb.append("\n");
			sb.append(
					"\n/**\n * The ServiceObjectFactory contains the creation of all JAXBElement's defined\n * in the XSDs and extends the ModelObjectFactory.\n");
			sb.append(" * <pre>");
			sb.append(Util.getGeneratedAt());
			sb.append("</pre>\n");
			sb.append(" * @author bhausen\n");
			sb.append(" */\n");
			// sb.append("@Component\n");
			sb.append(
					"public class ServiceObjectFactory extends ModelObjectFactory {");

			sb.append(
					"\n\tpublic static final String CONTEXT_PATH_DEFINITON =\"");
			for (int i = 0; i < contextPath.size(); i++) {
				if (i > 0) {
					sb.append(":");
				}
				sb.append(contextPath.toArray()[i]);
			}
			sb.append("\";");

			sb.append(objectFactories.toString());
			sb.append("\n");
			sb.append(methods.toString());
			sb.append("}");
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generate");
	}

}
