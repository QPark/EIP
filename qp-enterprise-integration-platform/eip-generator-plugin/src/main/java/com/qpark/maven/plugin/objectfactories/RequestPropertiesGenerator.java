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
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ModelObjectFactory out of the XSDs containing complex types.
 * @author bhausen
 */
public class RequestPropertiesGenerator {
	/** Parsed complex types. */
	private final TreeSet<ComplexType> complexTypes;
	/** The {@link Log}. */
	private final Log log;
	/** The {@link XsdsUtil}. */
	private final XsdsUtil xsds;
	/** The output directory. */
	private final File outputDirectory;
	/** The service id of the common services. */
	private final String serviceIdCommonServices;

	/**
	 * @param config
	 * @param complexTypes
	 */
	public RequestPropertiesGenerator(final XsdsUtil xsds,
			final File outputDirectory, final String serviceIdCommonServices,
			final Log log) {
		this.xsds = xsds;
		this.outputDirectory = outputDirectory;
		this.serviceIdCommonServices = serviceIdCommonServices;
		this.log = log;
		this.complexTypes = xsds.getComplexTypes();
	}

	public void generate() {
		this.log.debug("+generate");
		String failureType = new StringBuffer(64)
				.append(this.serviceIdCommonServices.toLowerCase()).append(".")
				.append(this.xsds.getMessagePackageNameSuffix())
				.append(".FailureType").toString();
		ComplexType failureTypeCt = null;
		String responseMessage = new StringBuffer(64)
				.append(this.serviceIdCommonServices.toLowerCase()).append(".")
				.append(this.xsds.getMessagePackageNameSuffix())
				.append(".ResponseMessage").toString();
		ComplexType responseMessageCt = null;

		for (ComplexType type : this.complexTypes) {
			if (!type.isAbstractType() && !type.isEnumType()
					&& !type.isSimpleType() && !type.isPrimitiveType()) {
				if (type.getClassNameFullQualified().endsWith(failureType)) {
					failureTypeCt = type;
				} else if (type.getClassNameFullQualified().endsWith(
						responseMessage)) {
					responseMessageCt = type;
				}

			}
		}
		if (failureTypeCt != null && responseMessageCt != null) {
			StringBuffer imports = new StringBuffer();
			imports.append("\nimport java.util.LinkedList;\nimport java.util.List;\n\nimport ");
			imports.append(failureTypeCt.getClassNameFullQualified());
			imports.append(";\nimport ");
			imports.append(responseMessageCt.getClassNameFullQualified());
			imports.append(";");

			StringBuffer sb = new StringBuffer(1024);
			sb.append("package ");
			sb.append(this.xsds.getBasePackageName());
			sb.append(";\n\n");
			sb.append(imports.toString());
			sb.append("\n");
			sb.append("\n/**\n * A class containing the incoming request and a list to collect the {@link FailureType}s.\n");
			sb.append(" * <pre>");
			sb.append(Util.getGeneratedAt());
			sb.append("</pre>\n");
			sb.append(" * @author bhausen\n");
			sb.append(" * @param <T> The request message type.\n");
			sb.append(" */\n");
			sb.append("public class RequestProperties<T> {");
			sb.append("\n");

			sb.append("\t/** The list of {@link FailureType}s collected. */\n");
			sb.append("\tprivate List<FailureType> failure;\n");
			sb.append("\t/** The incoming request. */\n");
			sb.append("\tprivate T request;\n");
			sb.append("\n");
			sb.append("\t/** Create the request properties. */\n");
			sb.append("\tpublic RequestProperties() {\n");
			sb.append("\t}\n");
			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * Create the request properties with request and {@link ResponseMessage}.\n");
			sb.append("\t * @param request the request.\n");
			sb.append("\t * @param response the {@link ResponseMessage}\n");
			sb.append("\t */\n");
			sb.append("\tpublic RequestProperties(final T request, final ResponseMessage response) {\n");
			sb.append("\t\tthis.request = request;\n");
			sb.append("\t\tthis.failure = response.getFailure();\n");
			sb.append("\t}\n");
			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * Add all the collected failures if not contained in the {@link ResponseMessage#getFailure()}\n");
			sb.append("\t * @param response\n");
			sb.append("\t */\n");
			sb.append("\tpublic void addFailures(final ResponseMessage response) {\n");
			sb.append("\t\tif (response != null && response.getFailure() != this.getFailure()) {\n");
			sb.append("\t\t\tresponse.getFailure().addAll(this.getFailure());\n");
			sb.append("\t\t}\n");
			sb.append("\t}\n");
			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * @return the failures.\n");
			sb.append("\t */\n");
			sb.append("\tpublic List<FailureType> getFailure() {\n");
			sb.append("\t\tif (this.failure == null) {\n");
			sb.append("\t\t\tthis.failure = new LinkedList<FailureType>();\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn this.failure;\n");
			sb.append("\t}\n");
			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * @return the request\n");
			sb.append("\t */\n");
			sb.append("\tpublic T getRequest() {\n");
			sb.append("\t\treturn this.request;\n");
			sb.append("\t}\n");
			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * @param request the request to set\n");
			sb.append("\t */\n");
			sb.append("\tpublic void setRequest(final T request) {\n");
			sb.append("\t\tthis.request = request;\n");
			sb.append("\t}\n");
			sb.append("}");

			File f = Util.getFile(this.outputDirectory,
					this.xsds.getBasePackageName(), "RequestProperties.java");
			this.log.info(new StringBuffer().append("Write ").append(
					f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sb.toString());
			} catch (Exception e) {
				this.log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		this.log.debug("-generate");
	}
}
