/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springconfig;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * This plugin creates a org.springframework.oxm.jaxb.Jaxb2Marshaller for all
 * given serviceIds.
 *
 * @author bhausen
 */
@Mojo(name = "generate-mashaller-config",
		defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class Jaxb2MarshallerConfigMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory",
			defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;
	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg flow</code>.
	 */
	@Parameter(property = "messagePackageNameSuffix", defaultValue = "msg flow")
	protected String messagePackageNameSuffix;
	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	@Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
	protected String deltaPackageNameSuffix;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	protected String basePackageName;
	/**
	 * The service request name need to end with this suffix (Default
	 * <code>Request</code>).
	 */
	@Parameter(property = "serviceRequestSuffix", defaultValue = "Request")
	protected String serviceRequestSuffix;
	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	@Parameter(property = "serviceResponseSuffix", defaultValue = "Response")
	protected String serviceResponseSuffix;
	@Parameter(defaultValue = "${project}", readonly = true)
	protected MavenProject project;
	@Parameter(defaultValue = "${mojoExecution}", readonly = true)
	protected MojoExecution execution;
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	protected String serviceId;
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "java", defaultValue = "false")
	protected boolean java;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		XsdsUtil xsds = XsdsUtil.getInstance(this.baseDirectory,
				this.basePackageName, this.messagePackageNameSuffix,
				this.deltaPackageNameSuffix, this.serviceRequestSuffix,
				this.serviceResponseSuffix);

		if (this.java) {
			this.generateJava(xsds);
		} else {
			this.generate(xsds);
		}

		this.getLog().debug("-execute");
	}

	private void generateJava(final XsdsUtil xsds) {
		String eipVersion = this.getEipVersion();
		StringBuffer capitalizeName = new StringBuffer(
				Util.capitalizePackageName(this.basePackageName));
		StringBuffer fileName = new StringBuffer(64)
				.append(this.basePackageName);
		if (Objects.nonNull(this.serviceId)
				&& this.serviceId.trim().length() > 0) {
			fileName.append(".").append(this.serviceId.trim());
			capitalizeName.append(
					ServiceIdRegistry.capitalize(this.serviceId.trim()));
		}
		fileName.append("-jaxb2marshaller-spring-config.xml");
		List<String> sids = ServiceIdRegistry.splitServiceIds(this.serviceId);
		if (sids.isEmpty()) {
			sids.addAll(xsds.getServiceIdRegistry().getAllServiceIds());
		}

		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(this.basePackageName).append(";\n");
		sb.append("\n");
		sb.append("import org.springframework.context.annotation.Bean;\n");
		sb.append(
				"import org.springframework.context.annotation.Configuration;\n");
		sb.append("import org.springframework.oxm.jaxb.Jaxb2Marshaller;\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(
				" * Provides a {@link Jaxb2Marshaller} - which is not a real marshaller, more a\n");
		sb.append(" * marshaller factory holding a JAXBContext.\n");
		sb.append(" *\n");
		sb.append(" * generated\n");
		sb.append(" */\n");
		sb.append("@Configuration\n");
		sb.append("public class ").append(capitalizeName)
				.append("Jaxb2MarshallerConfig {\n");
		sb.append("	/**\n");
		sb.append("	 * @return the {@link Jaxb2Marshaller}.\n");
		sb.append("	 */\n");
		sb.append("	@Bean(name = \"eipCaller").append(capitalizeName)
				.append("Marshaller\")\n");
		sb.append("	public static Jaxb2Marshaller eipCaller")
				.append(capitalizeName).append("Marshaller() {\n");
		sb.append("		Jaxb2Marshaller value = new Jaxb2Marshaller();\n");
		sb.append("		value.setPackagesToScan(\n");
		sb.append("\n");

		sb.append(sids.stream()
				.map(sid -> xsds.getServiceIdRegistry().getServiceIdEntry(sid))
				.map(side -> String.format("				\"%s\"",
						side.getPackageName()))
				.collect(Collectors.joining(",\n")));

		sb.append("\n");
		sb.append("		);\n");
		sb.append("		return value;\n");
		sb.append("	}\n");
		sb.append("}\n");

		File f = Util.getFile(this.outputDirectory, this.basePackageName,
				String.format("%sJaxb2MarshallerConfig.java", capitalizeName));
		this.getLog().debug(new StringBuffer().append("Write Inf  ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(String.format("%s: %s", e.getClass().getName(),
					e.getMessage()));
			e.printStackTrace();
		}
	}

	protected void generate(final XsdsUtil xsds) {
		this.getLog().debug("+generateService");

		String eipVersion = this.getEipVersion();
		StringBuffer capitalizeName = new StringBuffer(
				Util.capitalizePackageName(this.basePackageName));
		StringBuffer fileName = new StringBuffer(64)
				.append(this.basePackageName);
		if (Objects.nonNull(this.serviceId)
				&& this.serviceId.trim().length() > 0) {
			fileName.append(".").append(this.serviceId.trim());
			capitalizeName.append(
					ServiceIdRegistry.capitalize(this.serviceId.trim()));
		}
		fileName.append("-jaxb2marshaller-spring-config.xml");

		StringBuffer xml = new StringBuffer();

		xml.append(this.getXmlDefinition());
		xml.append(Util.getGeneratedAtXmlComment(this.getClass(), eipVersion));

		xml.append("\t<!-- Marshaller of services -->\n");
		xml.append("\t<bean id=\"eipCaller").append(capitalizeName)
				.append("Marshaller\"");
		xml.append(
				" class=\"org.springframework.oxm.jaxb.Jaxb2Marshaller\">\n");

		xml.append("\t\t<property name=\"packagesToScan\">\n");
		xml.append("\t\t\t<list>\n");
		List<String> sids = ServiceIdRegistry.splitServiceIds(this.serviceId);
		if (sids.isEmpty()) {
			sids.addAll(xsds.getServiceIdRegistry().getAllServiceIds());
		}
		sids.stream()
				.map(sid -> xsds.getServiceIdRegistry().getServiceIdEntry(sid))
				.forEach(side -> {
					xml.append("\t\t\t\t<value>");
					xml.append(side.getPackageName());
					xml.append("</value>\n");
				});
		xml.append("\t\t\t</list>\n");
		xml.append("\t\t</property>\n");

		xml.append("\t</bean>\n");
		xml.append("\n</beans>\n");

		File f = Util.getFile(this.outputDirectory, "", fileName.toString());
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, xml.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		this.getLog().debug("-generateService");
	}

	/**
	 * Get the executing plugin version - the EIP version.
	 *
	 * @return the EIP version.
	 */
	protected String getEipVersion() {
		return this.execution.getVersion();
	}

	private String getXmlDefinition() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		sb.append(
				"<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append(
				"\txmlns:int=\"http://www.springframework.org/schema/integration\"\n");
		sb.append(
				"\txmlns:int-ws=\"http://www.springframework.org/schema/integration/ws\"\n");
		sb.append(
				"\txmlns:oxm=\"http://www.springframework.org/schema/oxm\" \n");
		sb.append(
				"\txmlns:util=\"http://www.springframework.org/schema/util\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		String springVersion = this.project.getProperties()
				.getProperty("org.springframework.version.xsd.version");
		sb.append(
				"\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append(
				"\t\thttp://www.springframework.org/schema/oxm http://www.springframework.org/schema/oxm/spring-oxm");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append(
				"\t\thttp://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");

		springVersion = this.project.getProperties().getProperty(
				"org.springframework.integration.version.xsd.version");
		sb.append(
				"\t\thttp://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append(
				"\t\thttp://www.springframework.org/schema/integration/ws http://www.springframework.org/schema/integration/ws/spring-integration-ws");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append("\t\"\n>\n");
		return sb.toString();
	}
}
