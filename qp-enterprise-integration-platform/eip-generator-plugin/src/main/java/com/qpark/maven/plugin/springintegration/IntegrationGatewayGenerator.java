/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
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
	private final String integrationConfigGatewayId;
	private final String eipVersion;
	private final boolean reactive;


	public IntegrationGatewayGenerator(final XsdsUtil config, final File outputDirectory, final ElementType element,
			final String basePackageName, final boolean reactive, final String eipVersion, final Log log) {
		this.config = config;
		this.outputDirectory = outputDirectory;
		this.eipVersion = eipVersion;
		this.log = log;
		this.elementRequest = element;
		this.packageName = element.getPackageNameGateway();
		if (reactive) {
			this.className = String.format("Reactive%s", element.getClassNameGateway());
		} else {
			this.className = element.getClassNameGateway();
		}
		this.serviceId = element.getServiceId();
		this.integrationConfigGatewayId = String.format("eipCaller%s%s%sGateway",
				Util.capitalizePackageName(basePackageName), ServiceIdRegistry.capitalize(this.serviceId),
				element.getClassNameGateway());
		this.methodName = element.getMethodName();
		this.reactive = reactive;

		this.elementResponse = XsdsUtil.findResponse(this.elementRequest, config.getElementTypes(), config);
		if (this.elementResponse != null) {
			this.ctRequest = new ComplexType(this.elementRequest.getElement().getType(), this.config);
			this.ctResponse = new ComplexType(this.elementResponse.getElement().getType(), this.config);

			this.fqRequestType = this.ctRequest.getClassNameFullQualified();
			this.fqResponseType = this.ctResponse.getClassNameFullQualified();

			this.requestType = this.ctRequest.getClassName();
			this.responseType = this.ctResponse.getClassName();
		}
	}

	public void generate() {
		this.log.debug("+generate");
		if (this.elementResponse != null && this.ctResponse != null && !this.ctResponse.isSimpleType()
				&& !this.ctResponse.isPrimitiveType()) {
			StringBuffer sb = new StringBuffer(1024);
			sb.append("package ");
			sb.append(this.packageName);
			sb.append(";\n");
			sb.append("\n");
			sb.append("import javax.xml.bind.JAXBElement;\n");
			sb.append("\n");
			sb.append("import com.qpark.eip.IntegrationGateway;\n");
			sb.append("import ");
			sb.append(this.fqRequestType);
			sb.append(";\n");
			sb.append("import ");
			sb.append(this.fqResponseType);
			sb.append(";\n");
			if (this.reactive) {
				sb.append("import reactor.core.publisher.Mono;\n");
			}
			sb.append("/**\n");
			sb.append(" * Gateway to call operation ");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append(" on service <code>");
			sb.append(this.serviceId);
			sb.append("</code>.\n");
			sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(), this.eipVersion));
			sb.append(" */\n");
			sb.append("public interface ");
			sb.append(this.className);
			sb.append(" extends IntegrationGateway<JAXBElement<");
			sb.append(this.requestType);
			if (this.reactive) {
				sb.append(">, Mono<JAXBElement<");
				sb.append(this.responseType);
				sb.append(">>> {\n");
			} else {
				sb.append(">, JAXBElement<");
				sb.append(this.responseType);
				sb.append(">> {\n");
			}
			// sb.append("\t/**\n");
			// sb.append(
			// "\t * @param message the {@link JAXBElement} containing a {@link
			// ");
			// sb.append(this.requestType);
			// sb.append("}\n");
			// sb.append("\t * @return the {@link JAXBElement} with a {@link ");
			// sb.append(this.responseType);
			// sb.append("}\n");
			// sb.append("\t */\n");
			//
			// sb.append("\tJAXBElement<");
			// sb.append(this.responseType);
			// sb.append("> invoke(\n");
			// sb.append(" JAXBElement<");
			// sb.append(this.requestType);
			// sb.append("> message);\n");
			sb.append("}\n");
			sb.append("\n");
			File f = Util.getFile(this.outputDirectory, this.packageName,
					new StringBuffer().append(this.className).append(".java").toString());
			this.log.info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
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
		return new StringBuffer(128).append(this.packageName).append(".").append(this.className).toString();
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

	public String getIntegrationConfigGatewayId() {
		return this.integrationConfigGatewayId;
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
