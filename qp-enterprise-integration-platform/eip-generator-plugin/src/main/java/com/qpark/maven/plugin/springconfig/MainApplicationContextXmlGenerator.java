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

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * 
 * @author bhausen
 */
public class MainApplicationContextXmlGenerator {
    private final String basePackageName;
    private final File outputDirectory;
    private final boolean applicationWithoutPersistenceConfig;
    private final Log log;

    private final MavenProject project;

    /**
     * @param config
     * @param elementTypes
     */
    public MainApplicationContextXmlGenerator(final String basePackageName,
	    final String applicationWithoutPersistenceConfig, final File outputDirectory, final MavenProject project,
	    final Log log) {
	this.basePackageName = basePackageName;
	this.outputDirectory = outputDirectory;
	this.project = project;
	this.log = log;
	this.applicationWithoutPersistenceConfig = Boolean.parseBoolean(
		applicationWithoutPersistenceConfig != null ? applicationWithoutPersistenceConfig : "false");
    }

    public void generate() {
	this.log.debug("+generate");
	StringBuffer sb = new StringBuffer(1024);
	sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
	sb.append("	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
	sb.append("	xmlns:util=\"http://www.springframework.org/schema/util\"\n");
	sb.append("	xmlns:context=\"http://www.springframework.org/schema/context\"\n");
	sb.append("	xmlns:aop=\"http://www.springframework.org/schema/aop\"\n");
	sb.append("	xsi:schemaLocation=\"\n");
	String springVersion = this.project.getProperties().getProperty("org.springframework.version.xsd.version");
	sb.append(
		"\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans");
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
	sb.append(
		"\t\thttp://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsdn");
	sb.append("\t\thttp://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsdn");
	sb.append("\">\n");
	sb.append("\t<!-- ");
	sb.append(Util.getGeneratedAt());
	sb.append(" -->\n");
	sb.append("\t<!--<context:component-scan base-package=\"");
	sb.append(this.basePackageName);
	sb.append("\" />-->\n");
	sb.append("\t<context:annotation-config />\n");
	// Not needed any more.
	// sb.append("\t<!-- Making @Autowired in @ServiceActivator available.
	// -->\n");
	// sb.append("\t<aop:config proxy-target-class=\"true\" />\n");
	sb.append("\n");
	sb.append("\t<!-- Properties -->\n");
	sb.append("\t<import resource=\"classpath:/");
	sb.append(this.basePackageName);
	sb.append(".properties-config.xml\" />\n");
	if (this.applicationWithoutPersistenceConfig) {
	    // sb.append("\t<!-- Object factories -->\n");
	    // sb.append("\t<bean class=\"");
	    // sb.append(this.basePackageName);
	    // sb.append(".ModelObjectFactory\" />\n");
	    // sb.append("\t<bean class=\"");
	    // sb.append(this.basePackageName);
	    // sb.append(".ServiceObjectFactory\" />\n");
	    // sb.append("\n");
	} else {
	    sb.append("\t<import resource=\"classpath:/");
	    sb.append(this.basePackageName);
	    sb.append("-persistence-spring-config.xml\" />\n");
	}
	sb.append("\t<import resource=\"classpath:/security-spring-config.xml\"/>\n");
	sb.append("\n");
	sb.append("\t<!-- The web service wsdl, dispatching and service provider routing. -->\n");
	sb.append("\t<import resource=\"classpath:/dispatcher/*.xml\"/>\n");
	sb.append("\t<import resource=\"classpath:/router/*.xml\"/>\n");
	sb.append("\n");
	sb.append("\t<!-- All the service providers and utility configs. -->\n");
	sb.append("\t<import resource=\"classpath*:/service-provider-spring-config.xml\" />\n");
	sb.append("\t<import resource=\"classpath*:/utility-spring-config.xml\" />\n");
	sb.append("</beans>\n");

	File f = Util.getFile(this.outputDirectory, "MainApplicationContext.xml");
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
