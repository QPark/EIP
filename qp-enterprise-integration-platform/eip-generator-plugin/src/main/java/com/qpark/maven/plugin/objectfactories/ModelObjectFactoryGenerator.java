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
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ModelObjectFactory out of the XSDs containing complex types.
 * @author bhausen
 */
public class ModelObjectFactoryGenerator {
	/** Parsed complex types. */
	private final TreeSet<ComplexType> complexTypes;
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
	public ModelObjectFactoryGenerator(final XsdsUtil xsds,
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
		this.complexTypes = xsds.getComplexTypes();
	}

	public void generate() {
		this.log.debug("+generate");
		StringBuffer imports = new StringBuffer();
		StringBuffer methods = new StringBuffer();
		StringBuffer objectFactories = new StringBuffer();

		// imports.append("\nimport org.springframework.stereotype.Component;\n");
		TreeMap<String, String> importedModels = new TreeMap<String, String>();
		for (Entry<String, XsdContainer> xx : this.xsds.getXsdContainerMap()
				.entrySet()) {
			if (XsdsUtil.isMessagePackageName(xx.getValue().getPackageName(),
					this.messagePackageNameSuffix)) {
				for (String imp : xx.getValue().getImportedTargetNamespaces()) {
					importedModels.put(imp, xx.getValue().getFile()
							.getAbsolutePath());
				}
			}
		}

		TreeMap<String, String> ofMap = new TreeMap<String, String>();
		TreeSet<String> methodNames = new TreeSet<String>();
		QName tns;
		String ofName;
		String cName;
		String pName;
		String mName;
		String mOfName;
		for (ComplexType type : this.complexTypes) {
			tns = type.getType().getName();
			if (!type.isAbstractType()
					&& !type.isEnumType()
					&& !type.isSimpleType()
					&& !type.isPrimitiveType()
					&& (tns == null || importedModels.containsKey(tns
							.getNamespaceURI()))) {
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
				mName = new StringBuffer(6 + cName.length()).append("create")
						.append(cName).toString().replaceAll("\\.", "");
				mOfName = mName;
				if (methodNames.contains(mName)) {
					mName = new StringBuffer(6 + cName.length()
							+ pName.length()).append("create")
							.append(Util.capitalizePackageName(pName))
							.append(cName).toString().replaceAll("\\.", "");
					cName = type.getClassNameFullQualified();
				} else {
					imports.append("\nimport ")
							.append(type.getClassNameFullQualified())
							.append(";");
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

		File f = Util.getFile(this.outputDirectory,
				this.xsds.getBasePackageName(), "ModelObjectFactory.java");
		this.log.info(new StringBuffer().append("Write ").append(
				f.getAbsolutePath()));
		try {
			StringBuffer sb = new StringBuffer(1024);
			sb.append("package ");
			sb.append(this.xsds.getBasePackageName());
			sb.append(";\n\n");
			sb.append(imports.toString());
			sb.append("\n");
			sb.append("\n/**\n * The ModelObjectFactory contains the creation of all complex types defined in\n * the XSDs.\n");
			sb.append(" * <pre>");
			sb.append(Util.getGeneratedAt());
			sb.append("</pre>\n");
			sb.append(" * @author bhausen\n");
			sb.append(" */\n");
			// sb.append("@Component\n");
			sb.append("public class ModelObjectFactory {");
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
