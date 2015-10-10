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

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class IntegrationGatewayGenerator {
	private final String className;
	/** The {@link XsdsUtil}. */
	private final XsdsUtil config;
	private ComplexType ctRequest;

	private ComplexType ctResponse;
	private final ElementType elementRequest;
	private ElementType elementResponse = null;
	private String fqRequestType;
	private String fqResponseType;
	private boolean generated = false;
	/** The {@link Log}. */
	private final Log log;

	private final String methodName;

	/** The output directory. */
	private final File outputDirectory;

	private final String packageName;

	private String requestType;

	private String responseType;

	private final String serviceId;

	public IntegrationGatewayGenerator(final XsdsUtil config,
			final File outputDirectory, final ElementType element, final Log log) {
		this.config = config;
		this.outputDirectory = outputDirectory;
		this.log = log;
		this.elementRequest = element;
		this.packageName = element.getPackageNameGateway();
		this.className = element.getClassNameGateway();
		this.serviceId = element.getServiceId();
		this.methodName = element.getMethodName();

		this.elementResponse = XsdsUtil.findResponse(this.elementRequest,
				config.getElementTypes(), config);
		if (this.elementResponse != null) {
			this.ctRequest = new ComplexType(this.elementRequest.getElement()
					.getType(), this.config);
			this.ctResponse = new ComplexType(this.elementResponse.getElement()
					.getType(), this.config);

			this.fqRequestType = this.ctRequest.getClassNameFullQualified();
			this.fqResponseType = this.ctResponse.getClassNameFullQualified();

			this.requestType = this.ctRequest.getClassName();
			this.responseType = this.ctResponse.getClassName();
		}
	}

	public void generate() {
		this.log.debug("+generate");
		if (this.elementResponse != null && this.ctResponse != null
				&& !this.ctResponse.isSimpleType()
				&& !this.ctResponse.isPrimitiveType()) {
			StringBuffer sb = new StringBuffer(1024);
			sb.append("package ");
			sb.append(this.packageName);
			sb.append(";\n");
			sb.append("\n");
			sb.append("import javax.xml.bind.JAXBElement;\n");
			sb.append("\n");
			sb.append("import ");
			sb.append(this.fqRequestType);
			sb.append(";\n");
			sb.append("import ");
			sb.append(this.fqResponseType);
			sb.append(";\n");
			sb.append("/**\n");
			sb.append(" * Gateway to call operation ");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append(" on service <code>");
			sb.append(this.serviceId);
			sb.append("</code>.\n");
			sb.append(" * <pre>");
			sb.append(Util.getGeneratedAt());
			sb.append("</pre>\n");
			sb.append(" * @author bhausen\n");
			sb.append(" */\n");
			sb.append("public interface ");
			sb.append(this.className);
			sb.append(" {\n");

			sb.append("\t/**\n");
			sb.append("\t * @param message the {@link JAXBElement} containing a {@link ");
			sb.append(this.requestType);
			sb.append("}\n");
			sb.append("\t * @return the {@link JAXBElement} with a {@link ");
			sb.append(this.responseType);
			sb.append("}\n");
			sb.append("\t */\n");

			sb.append("\tJAXBElement<");
			sb.append(this.responseType);
			sb.append("> invoke(\n");
			sb.append("			JAXBElement<");
			sb.append(this.requestType);
			sb.append("> message);\n");
			sb.append("}\n");
			sb.append("\n");
			File f = Util.getFile(this.outputDirectory, this.packageName,
					new StringBuffer().append(this.className).append(".java")
							.toString());
			this.log.info(new StringBuffer().append("Write ").append(
					f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sb.toString());
				this.generated = true;
			} catch (Exception e) {
				this.log.error(e.getMessage());
				e.printStackTrace();
			}
			this.log.debug("-generate");
		}
	}

	/**
	 * @return the implName
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * @return the implName
	 */
	public String getFqClassName() {
		return new StringBuffer(128).append(this.packageName).append(".")
				.append(this.className).toString();
	}

	/**
	 * @return the ctRequest
	 */
	public ComplexType getCtRequest() {
		return this.ctRequest;
	}

	/**
	 * @return the ctResponse
	 */
	public ComplexType getCtResponse() {
		return this.ctResponse;
	}

	/**
	 * @return the elementRequest
	 */
	public ElementType getElementRequest() {
		return this.elementRequest;
	}

	/**
	 * @return the elementResponse
	 */
	public ElementType getElementResponse() {
		return this.elementResponse;
	}

	/**
	 * @return the fqRequestType
	 */
	public String getFqRequestType() {
		return this.fqRequestType;
	}

	/**
	 * @return the fqResponseType
	 */
	public String getFqResponseType() {
		return this.fqResponseType;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return this.requestType;
	}

	/**
	 * @return the responseType
	 */
	public String getResponseType() {
		return this.responseType;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return this.serviceId;
	}

	/**
	 * @return the generated
	 */
	public boolean isGenerated() {
		return this.generated;
	}

	/**
	 * @param fqRequestType the fqRequestType to set
	 */
	public void setFqRequestType(final String fqRequestType) {
		this.fqRequestType = fqRequestType;
	}

	/**
	 * @param fqResponseType the fqResponseType to set
	 */
	public void setFqResponseType(final String fqResponseType) {
		this.fqResponseType = fqResponseType;
	}
}
