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
package com.qpark.maven.plugin.springconfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * 
 * @author bhausen
 */
public class ApplicationPropertiesConfigXmlGenerator {
    public static void main(final String[] args) {
	Properties p = new Properties();
	p.setProperty("#xlkjdsfg", "__NOT_SET_");

	ByteArrayOutputStream boas = new ByteArrayOutputStream();
	try {
	    p.store(boas, "commnetlkjasdf\n#xprop=__NOT_SET__TOOO__");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	System.out.println(new String(boas.toByteArray()));
    }

    private final XsdsUtil config;
    private final String basePackageName;
    private final String serviceId;
    private final String serviceVersion;
    private final File outputDirectory;
    private final Log log;
    private final String revisionNumber;
    private final MavenProject project;
    private final String placeholderConfigurerImpl;

    /**
     * @param config
     * @param elementTypes
     */
    public ApplicationPropertiesConfigXmlGenerator(final XsdsUtil config, final String basePackageName,
	    final String serviceId, final String serviceVersion, final String revisionNumber,
	    final String placeholderConfigurerImpl, final File outputDirectory, final MavenProject project,
	    final Log log) {
	this.config = config;
	this.basePackageName = basePackageName;
	this.serviceId = serviceId == null ? "" : serviceId.replace(',', '-').replaceAll(" ", "");
	this.serviceVersion = serviceVersion;
	this.revisionNumber = revisionNumber == null ? "" : revisionNumber;
	this.placeholderConfigurerImpl = placeholderConfigurerImpl == null
		|| placeholderConfigurerImpl.trim().length() == 0
			? "com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer" : placeholderConfigurerImpl;
	this.outputDirectory = outputDirectory;
	this.project = project;
	this.log = log;
    }

    public void generate() {
	this.log.debug("+generate");
	StringBuffer sb = new StringBuffer(1024);
	sb.append(this.getXmlDefinition());
	sb.append("\t<!-- ");
	sb.append(Util.getGeneratedAt());
	sb.append(" -->\n");
	sb.append("\n");
	sb.append(this.getPropertiesDefinition());
	sb.append("\n");
	sb.append("</beans>\n");

	File f = Util.getFile(this.outputDirectory,
		new StringBuffer(32).append(this.basePackageName).append(".properties-config.xml").toString());
	this.log.info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));

	try {
	    Util.writeToFile(f, sb.toString());
	} catch (Exception e) {
	    this.log.error(e.getMessage());
	    e.printStackTrace();
	}

	Properties p = this.getGeneratedProperties();
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	f = Util.getFile(this.outputDirectory, "eip.generated.properties");
	try {
	    String prefix = Util.capitalizePackageName(this.basePackageName);
	    p.store(baos, "Build time generated properties");
	    sb.setLength(0);
	    sb.append(new String(baos.toByteArray()));
	    sb.append("\n# Some additional properties that might to be set\n");
	    sb.append("# Properties of the ").append(prefix).append("DataSourceJdbc default object:\n");
	    sb.append("#").append(prefix).append(".jdbc.driverClassName=\n");
	    sb.append("#").append(prefix).append(".jdbc.url=\n");
	    sb.append("#").append(prefix).append(".jdbc.username=\n");
	    sb.append("#").append(prefix).append(".jdbc.password=\n");
	    sb.append("# Properties of the SelfCallingIntegrationConfig.xml\n");
	    sb.append("#eip.internal.self.calling.username=\n");
	    sb.append("#eip.internal.self.calling.password=\n");
	    sb.append("#eip.internal.self.calling.xxx.endpoint=\n");
	    sb.append("#").append(prefix).append("=\n");
	    // sb.append("#").append(prefix).append("=\n");

	    Util.writeToFile(f, sb.toString());
	} catch (Exception e) {
	    this.log.error(e.getMessage());
	    e.printStackTrace();
	}

	this.log.debug("-generate");
    }

    private Properties getGeneratedProperties() {
	Properties appl = new Properties();
	Properties p = this.project.getProperties();
	appl.setProperty("eip.application.name",
		p.getProperty("service.application.name", "applicationNameNotApplicable"));
	appl.setProperty("eip.service.name", p.getProperty("service.name", "common"));
	appl.setProperty("eip.service.version", p.getProperty("service.version", "trunk"));
	appl.setProperty("eip.application.war.name", p.getProperty("service.war.name"));

	appl.setProperty("eip.application.build.time", Util.getXsdDateTime(new Date()));
	appl.setProperty("eip.application.scm.revision", this.revisionNumber);

	appl.setProperty("eip.application.maven.artifact.groupid", this.project.getGroupId());
	appl.setProperty("eip.application.maven.artifact.artifactid", this.project.getArtifactId());
	appl.setProperty("eip.application.maven.artifact.version", this.project.getVersion());
	// appl.setProperty("eip.application.jaxb.context.name",
	// Util.getContextPath(this.config.getPackageNames()));
	appl.setProperty("eip.application.jaxb.context.name",
		ServiceIdRegistry.getMarshallerContextPath(this.serviceId));
	appl.setProperty("eip.web.service.server", "http://localhost:8080/");

	return appl;
    }

    private String getPropertiesDefinition() {
	StringBuffer sb = new StringBuffer(1024);
	sb.append("\t<!-- The properties\n");
	sb.append("\tFirst the versions and the defaults will be taken out of the classpath.\n");
	sb.append("\t	If possible enhance and overwrite with files out of the\n");
	sb.append("\t${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append("*.property files.\n");
	sb.append("\tAt last the username and password properties will be taken from\n");
	sb.append("\t${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append("-user*properties.\n");
	sb.append("\t-->\n");

	sb.append("\t<bean id=\"");
	sb.append(Util.capitalizePackageName(this.basePackageName));
	sb.append("Properties\" ");
	sb.append("class=\"");
	if (this.placeholderConfigurerImpl == null) {
	    sb.append("com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer");
	} else {
	    sb.append(this.placeholderConfigurerImpl);
	}
	sb.append("\">\n");
	sb.append("\t\t<property name=\"ignoreResourceNotFound\" value=\"true\"/>\n");
	sb.append("\t\t<property name=\"ignoreUnresolvablePlaceholders\" value=\"true\"/>\n");
	sb.append("\t\t<property name=\"locations\">\n");
	sb.append("\t\t\t<list>\n");
	sb.append("\t\t\t\t<value>classpath*:");
	sb.append(this.basePackageName);
	sb.append(".version.properties</value>\n");
	sb.append("\t\t\t\t<value>classpath*:eip.util.properties</value>\n");
	sb.append("\t\t\t\t<value>classpath*:eip.generated.properties</value>\n");
	sb.append("\t\t\t\t<value>classpath*:");
	sb.append(this.basePackageName);
	sb.append(".properties</value>\n");
	sb.append("\t\t\t\t<value>file:///${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append(".properties</value>\n");
	sb.append("\t\t\t\t<value>file:///${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append("-");
	sb.append(this.serviceVersion);
	sb.append(".properties</value>\n");
	sb.append("\t\t\t\t<value>file:///${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append("-");
	sb.append(this.serviceVersion);
	sb.append("-");
	sb.append(this.serviceId);
	sb.append(".properties</value>\n");
	sb.append("\t\t\t\t<value>file:///${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append("-user.properties</value>\n");
	sb.append("\t\t\t\t<value>file:///${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append("-user-");
	sb.append(this.serviceVersion);
	sb.append(".properties</value>\n");
	sb.append("\t\t\t\t<value>file:///${catalina.base}/conf/");
	sb.append(this.basePackageName);
	sb.append("-user-");
	sb.append(this.serviceVersion);
	sb.append("-");
	sb.append(this.serviceId);
	sb.append(".properties</value>\n");
	sb.append("\t\t\t</list>\n");
	sb.append("\t\t</property>\n");
	sb.append("\t</bean>\n");
	return sb.toString();
    }

    private String getXmlDefinition() {
	StringBuffer sb = new StringBuffer(1024);
	sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
	sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
	sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
	sb.append("\txsi:schemaLocation=\"\n");
	String springVersion = this.project.getProperties().getProperty("org.springframework.version.xsd.version");
	sb.append(
		"\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");
	sb.append("\t\"\n>\n");
	return sb.toString();
    }
}
