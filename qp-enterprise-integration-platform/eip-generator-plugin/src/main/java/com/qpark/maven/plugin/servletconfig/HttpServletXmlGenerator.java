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
package com.qpark.maven.plugin.servletconfig;

import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_FRAMEWORK_XSD_VERSION;
import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_INTEGRATION_XSD_VERSION;

import java.io.File;
import java.util.Date;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * @author bhausen
 */
public class HttpServletXmlGenerator {
	private final Log log;
	@SuppressWarnings("unused")
	private final XsdsUtil config;
	private final String serviceVersion;
	private final File outputDirectory;
	private final String serviceId;
	private final String warName;
	private final String revisionNumber;
	private final Date d = new Date();
	private final MavenProject project;

	/**
	 * @param config
	 * @param elementTypes
	 */
	public HttpServletXmlGenerator(final XsdsUtil config,
			final String serviceId, final String serviceVersion,
			final String revisionNumber, final String warName,
			final File outputDirectory, final MavenProject project,
			final Log log) {
		this.config = config;
		this.serviceId = serviceId == null ? "" : serviceId.replace(',', '-')
				.replaceAll(" ", "");
		this.serviceVersion = serviceVersion;
		this.revisionNumber = revisionNumber;
		this.warName = warName;
		this.outputDirectory = outputDirectory;
		this.project = project;
		this.log = log;
	}

	public void generate() {
		this.log.debug("+generate");

		File f = Util.getFile(this.outputDirectory, "", "http-servlet.xml");
		this.log.info(new StringBuffer().append("Write ").append(
				f.getAbsolutePath()));

		try {
			Util.writeToFile(f, this.getHttpServletXml());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generate");
	}

	private String getHttpServletXml() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:context=\"http://www.springframework.org/schema/context\"\n");
		sb.append("\txmlns:aop=\"http://www.springframework.org/schema/aop\" xmlns:int=\"http://www.springframework.org/schema/integration\" xmlns:int-ws=\"http://www.springframework.org/schema/integration/ws\"\n");
		sb.append("\txmlns:oxm=\"http://www.springframework.org/schema/oxm\" xmlns:int-http=\"http://www.springframework.org/schema/integration/http\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.integration.version.xsd.version",
				DEFAULT_SPRING_INTEGRATION_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration/ws http://www.springframework.org/schema/integration/ws/spring-integration-ws-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.integration.version.xsd.version",
				DEFAULT_SPRING_INTEGRATION_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/oxm http://www.springframework.org/schema/oxm/spring-oxm-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration/http http://www.springframework.org/schema/integration/http/spring-integration-http.xsd\n");
		sb.append("\"\n");
		sb.append(">\n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt(this.d));
		sb.append(" -->\n");
		sb.append("</beans>\n");
		return sb.toString();
	}
}
