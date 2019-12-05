/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;

/**
 * Copy of SpringIntegrationConfigGenerator
 *
 * @author bhausen
 */
public class WsEndPointGenerator {
	/** The {@link Log}. */
	private final Log log;
	/** The output directory. */
	private final File outputDirectory;

	private final MavenProject project;
	private final String basePackageName;
	private final List<IntegrationGatewayGenerator> igs;
	private final String serviceId;
	private final String eipVersion;

	public WsEndPointGenerator(final String serviceId,
			final List<IntegrationGatewayGenerator> igs,
			final String basePackageName, final String eipVersion,
			final Log log, final MavenProject project) {
		this.serviceId = serviceId;
		this.igs = igs;
		this.basePackageName = basePackageName;
		this.eipVersion = eipVersion;
		this.log = log;
		this.project = project;
		this.outputDirectory = new File(this.project.getBuild().getDirectory());
	}

	public void generate() {
		this.log.debug("+generate");

		String className = new StringBuffer()
				.append(ServiceIdRegistry.capitalize(this.serviceId))
				.append("EndPoint").toString();
		StringBuffer sb = new StringBuffer(1024);

		StringBuffer imports = new StringBuffer(1024);
		StringBuffer methods = new StringBuffer(1024);
		imports.append("import java.time.Duration;\n");
		imports.append("import java.time.Instant;\n");
		imports.append("import javax.xml.bind.JAXBElement;\n");
		imports.append(
				"import org.springframework.ws.server.endpoint.annotation.Endpoint;\n");
		imports.append(
				"import org.springframework.ws.server.endpoint.annotation.PayloadRoot;\n");
		imports.append(
				"import org.springframework.ws.server.endpoint.annotation.RequestPayload;\n");
		imports.append(
				"import org.springframework.ws.server.endpoint.annotation.ResponsePayload;\n");
		imports.append("import org.springframework.ws.soap.SoapHeader;\n");
		imports.append("import org.slf4j.Logger;\n");
		imports.append("import org.slf4j.LoggerFactory;\n");
		imports.append("\n");
		String targetNamespace = "";
		if (this.igs.size() > 0) {
			imports.append("import ");
			imports.append(this.igs.get(0).getFqRequestType().substring(0,
					this.igs.get(0).getFqRequestType().lastIndexOf('.')));
			imports.append(".ObjectFactory;\n");
			imports.append("\n");
			if (Objects.nonNull(this.igs.get(0).getElementResponse())) {
				targetNamespace = this.igs.get(0).getElementResponse()
						.getTargetNamespace();
			} else if (Objects.nonNull(this.igs.get(0).getElementRequest())) {
				targetNamespace = this.igs.get(0).getElementRequest()
						.getTargetNamespace();
			}

		}

		StringBuffer classContent = new StringBuffer();
		classContent.append("\t/** The target name space. */\n");
		classContent.append("\tprivate static final String NAMESPACE_URI = \"")
				.append(targetNamespace).append("\";\n");
		classContent.append("\t/** The {@link Logger}. */\n");
		classContent.append(
				"\tprivate final Logger logger = LoggerFactory.getLogger(");
		classContent.append(className);
		classContent.append(".class);\n");
		classContent.append("\t/** The {@link ObjectFactory}. */\n");
		classContent.append(
				"\tprivate final ObjectFactory of = new ObjectFactory();\n");

		for (IntegrationGatewayGenerator ig : this.igs) {
			imports.append("import ");
			imports.append(ig.getFqRequestType());
			imports.append(";\n");
			imports.append("import ");
			imports.append(ig.getFqResponseType());
			imports.append(";\n");

			methods.append("\t/**\n");
			methods.append("\t * @param request the {@link ");
			methods.append(ig.getRequestType());
			methods.append("}.\n");
			methods.append("\t * @param soapHeader the {@link SoapHeader}.\n");
			methods.append(
					"\t * @return the {@link JAXBElement} with value {@link ");
			methods.append(ig.getResponseType());
			methods.append("}.\n");
			methods.append("\t */\n");
			methods.append(
					"\t@PayloadRoot(namespace = NAMESPACE_URI, localPart = \"");
			methods.append(ig.getElementRequest().getClassNameObject());
			methods.append("\")\n\t@ResponsePayload\n");
			methods.append("\tpublic JAXBElement<");
			methods.append(ig.getResponseType());
			methods.append("> ");
			methods.append(ig.getMethodName());
			methods.append("(\n");
			methods.append("\t\t\t@RequestPayload final ");
			methods.append(ig.getRequestType());
			methods.append(" request,\n\t\t\tfinal SoapHeader header) {\n");
			methods.append("\t\tthis.logger.debug(\"+");
			methods.append(ig.getMethodName());
			methods.append("\");\n");
			methods.append("\t\t");
			methods.append("\t\tInstant start = Instant.now();\n\t\t");
			methods.append(ig.getResponseType());
			methods.append(" value = this.of.create");
			methods.append(ig.getResponseType());
			methods.append("();\n");
			methods.append("\t\ttry {\n");

			methods.append("\t\t\t// Implementation goes here!\n");

			methods.append("\t\t} finally {\n");
			methods.append("\t\t\tthis.logger.debug(\" ");
			methods.append(ig.getMethodName());
			methods.append(
					" duration {}\", Duration.between(start, Instant.now()));\n");
			methods.append("\t\t\tthis.logger.debug(\"-");
			methods.append(ig.getMethodName());
			methods.append("\");\n");
			methods.append("\t\t}\n");
			methods.append("\t\treturn this.of.create");
			methods.append(ig.getElementRequest().getOperationName());
			methods.append("Response(value);\n");
			methods.append("\t}\n");
		}

		sb.append(imports.toString());
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * WebService {@link EndPoint} of service <code>");
		sb.append(this.serviceId);
		sb.append("</code>.\n");
		sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(),
				this.eipVersion));
		sb.append(" */\n");
		sb.append("@Endpoint\n");
		sb.append("public class ");
		sb.append(className);
		sb.append(" {\n");
		sb.append(classContent);
		sb.append("\n");
		sb.append(methods);
		sb.append("}");

		File f = Util.getFile(this.outputDirectory, "",
				String.format("%s.java", className));
		this.log.info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generate");
	}
}
