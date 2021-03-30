/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;

/**
 * @author bhausen
 */
public class ServiceOperationProviderGenerator {
	/** The {@link Log}. */
	private final Log log;
	/** The output directory. */
	private final File outputDirectory;

	private final MavenProject project;
	private final String basePackageName;
	private final List<IntegrationGatewayGenerator> igs;
	private final String serviceId;
	private final String eipVersion;
	private final boolean reactive;

	public ServiceOperationProviderGenerator(final String serviceId, final List<IntegrationGatewayGenerator> igs,
			final String basePackageName, final boolean reactive, final String eipVersion, final Log log,
			final MavenProject project) {
		this.serviceId = serviceId;
		this.igs = igs;
		this.basePackageName = basePackageName;
		this.reactive = reactive;
		this.eipVersion = eipVersion;
		this.log = log;
		this.project = project;
		this.outputDirectory = new File(this.project.getBuild().getDirectory());
	}

	public void generate() {
		this.log.debug("+generate");

		String className = String.format("%sOperationProvider%s", this.reactive ? "Reactive" : "",
				ServiceIdRegistry.capitalize(this.serviceId));
		StringBuffer sb = new StringBuffer(1024);

		StringBuffer imports = new StringBuffer(1024);
		StringBuffer methods = new StringBuffer(1024);

		imports.append("import java.time.Duration;\n");
		imports.append("import java.time.Instant;\n");
		imports.append("import javax.xml.bind.JAXBElement;\n");
		imports.append("import java.util.concurrent.TimeUnit;\n");
		imports.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		imports.append("import org.springframework.beans.factory.annotation.Qualifier;\n");
		imports.append("import org.slf4j.Logger;\n");
		imports.append("import org.slf4j.LoggerFactory;\n");
		if (this.reactive) {
			imports.append("import reactor.core.publisher.Mono;\n");
		}
		imports.append("\n");
		if (this.igs.size() > 0) {
			imports.append("import ");
			imports.append(this.igs.get(0).getFqRequestType().substring(0,
					this.igs.get(0).getFqRequestType().lastIndexOf('.')));
			imports.append(".ObjectFactory;\n");
			imports.append("\n");
		}

		StringBuffer classContent = new StringBuffer();

		classContent.append("\t/** The {@link Logger}. */\n");
		classContent.append("\tprivate final Logger logger = LoggerFactory.getLogger(");
		classContent.append(className);
		classContent.append(".class);\n");
		classContent.append("\t/** The {@link ObjectFactory}. */\n");
		classContent.append("\tprivate final ObjectFactory of = new ObjectFactory();\n");

		classContent.append("\t/** Authenticated? */\n");
		classContent.append("\t@Autowired\n");
		classContent.append("\tprivate SetAppSecurityContextAuthentication setSecurityContextAuth;\n");

		for (IntegrationGatewayGenerator ig : this.igs) {
			imports.append("import ");
			imports.append(ig.getFqRequestType());
			imports.append(";\n");
			imports.append("import ");
			imports.append(ig.getFqResponseType());
			imports.append(";\n");
			imports.append("import ");
			imports.append(ig.getFqClassName());
			imports.append(";\n");

			String gatewayId = ig.getIntegrationConfigGatewayId();

			classContent.append("\t/** Gateway to ");
			classContent.append(Util.splitOnCapital(ig.getMethodName()));
			classContent.append(". */\n");
			classContent.append("\t@Autowired\n");
			classContent.append("\t@Qualifier(\"");
			classContent.append(gatewayId);
			classContent.append("\")\n");
			classContent.append("\tprivate ");
			classContent.append(ig.getClassName());
			classContent.append(" ");
			classContent.append(ig.getMethodName());
			classContent.append(";\n");

			methods.append("\t/**\n");
			methods.append("\t * @param request the {@link ");
			methods.append(ig.getRequestType());
			methods.append("}.\n");
			methods.append("\t * @return the {@link ");
			methods.append(ig.getResponseType());
			methods.append("}.\n");
			methods.append("\t */\n");
			methods.append("\tpublic ");
			methods.append(ig.getResponseType());
			methods.append(" ");
			methods.append(ig.getMethodName());
			methods.append("(\n");
			methods.append("\t\t\tfinal ");
			methods.append(ig.getRequestType());
			methods.append(" request) {\n");
			methods.append("\t\tthis.logger.debug(\"+");
			methods.append(ig.getMethodName());
			methods.append("\");\n");
			methods.append("\t\t");
			if (this.reactive) {
				methods.append(ig.getResponseType());
				methods.append("[] value = new ").append(ig.getResponseType()).append("[1];\n");
			} else {
				methods.append(ig.getResponseType());
				methods.append(" value = null;\n");
			}
			methods.append("\t\tInstant start = Instant.now();\n");
			methods.append("\t\ttry {\n");
			methods.append("\t\t\tthis.requestInit();\n");
			if (this.reactive) {
				methods.append("\t\t\tthis.");
				methods.append(ig.getMethodName());
				methods.append(".invoke(this.of\n");
				methods.append("\t\t\t\t.create");
				methods.append(ig.getRequestType().substring(0, ig.getRequestType().lastIndexOf("Type")));
				methods.append("(request))\n");
				methods.append("\t\t\t\t.subscribe(response -> value[0] = response.getValue());\n");
			} else {
				methods.append("\t\t\tJAXBElement<");
				methods.append(ig.getResponseType());
				methods.append("> response = this.");
				methods.append(ig.getMethodName());
				methods.append("\n");
				methods.append("\t\t\t\t\t.invoke(this.of\n");
				methods.append("\t\t\t\t\t\t\t.create");
				methods.append(ig.getRequestType().substring(0, ig.getRequestType().lastIndexOf("Type")));
				methods.append("(request));\n");
				methods.append("\t\t\tif (response != null) {\n");
				methods.append("\t\t\t\tvalue = response.getValue();\n");
				methods.append("\t\t\t}\n");
			}
			methods.append("\t\t} finally {\n");
			methods.append("\t\t\tthis.requestFinalization();\n");
			methods.append("\t\t\tthis.logger.debug(\" ");
			methods.append(ig.getMethodName());
			methods.append(" duration {}\", Duration.between(start, Instant.now));\n");
			methods.append("\t\t\tthis.logger.debug(\"-");
			methods.append(ig.getMethodName());
			methods.append("\");\n");
			methods.append("\t\t}\n");
			if (this.reactive) {
				methods.append("\t\treturn value[0];\n");
			} else {
				methods.append("\t\treturn value;\n");
			}
			methods.append("\t}\n");
		}

		classContent.append("\t/**\n");
		classContent.append("\t * @param start\n");
		classContent.append("\t * @return the duration in 000:00:00.000 format.\n");
		classContent.append("\t */\n");
		classContent.append("\tprivate String requestDuration(final long start) {\n");
		classContent.append("\t\tlong millis = System.currentTimeMillis() - start;\n");
		classContent.append(
				"\t\tString hmss = String.format(\"%03d:%02d:%02d.%03d\",TimeUnit.MILLISECONDS.toHours(millis),\n");
		classContent.append(
				"\t\t\tTimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),\n");
		classContent.append(
				"\t\t\tTimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),\n");
		classContent.append(
				"\t\t\tTimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));\n");
		classContent.append("\t\treturn hmss;\n");
		classContent.append("\t}\n");
		classContent.append("\t/** Initalize the request. */\n");
		classContent.append("\tprivate void requestInit() {\n");
		classContent.append("\t\tif (this.setSecurityContextAuth != null) {\n");
		classContent.append("\t\t\tthis.setSecurityContextAuth.setAppSecurityContextAuthentication();\n");
		classContent.append("\t\t}\n");
		classContent.append("\t}\n");

		classContent.append("\t/** Finalize the request. */\n");
		classContent.append("\tprivate void requestFinalization() {\n");
		classContent.append("\t}\n");

		classContent.append("\t///** Get the size of the response value. */\n");
		classContent.append("\t//private String responseValueSize(Object value) {\n");
		classContent.append("\t\t//String s = \"null\";\n");
		classContent.append("\t\t//if (value != null) {\n");
		classContent.append("\t\t\t//if (Collection.class.isInstance(value)) {\n");
		classContent.append("\t\t\t\t//s = String.valueOf(((Collection<?>) value).size());\n");
		classContent.append("\t\t\t//} else if (value.getClass().isArray()) {\n");
		classContent.append("\t\t\t\t//s = String.valueOf(((Object[]) value).length);\n");
		classContent.append("\t\t\t//} else {\n");
		classContent.append("\t\t\t\t//s = \"1\";\n");
		classContent.append("\t\t\t//}\n");
		classContent.append("\t\t//}\n");
		classContent.append("\t\t//return s;\n");
		classContent.append("\t//}\n");

		sb.append(imports.toString());
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * Operation provider of service <code>");
		sb.append(this.serviceId);
		sb.append("</code>.\n");
		sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(), this.eipVersion));
		sb.append(" */\n");
		sb.append("@Component\n");
		sb.append("public class ");
		sb.append(className);
		sb.append(" {\n");
		sb.append(classContent);
		sb.append("\n");
		sb.append(methods);
		sb.append("}");

		File f = Util.getFile(this.outputDirectory, "",
				new StringBuffer(className.length() + 5).append(className).append(".java").toString());
		this.log.info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generate");
	}
}
