/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.collectschemas;

import java.io.File;
import java.util.Map;
import java.util.TreeSet;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Collect the message defining schemas into one schema that importing them.
 * This is needed to use the maven-hyperjaxb3-plugin of org.jvnet.hyperjaxb3.
 *
 * @author bhausen
 */
@Mojo(name = "generate-collected-schemas",
		defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SchemaCollectionXsdMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory",
			defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;
	/** The length of a string to be mapped. */
	@Parameter(property = "stringLength", defaultValue = "255")
	protected String stringLength;
	/**
	 * The package names of the messages should end with - separation by space.
	 * Default is <code>msg restmsg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffixes",
			defaultValue = "msg restmsg")
	protected String messagePackageNameSuffixes;
	@Component
	protected MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		Map<String, XsdContainer> xsdContainerMap = XsdsUtil
				.getXsdContainers(this.baseDirectory);
		StringBuffer sb = new StringBuffer(1024);
		sb.append(this.getXmlDefinition());
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\n");

		sb.append(this.getJAXBAppInfo());
		sb.append("\n");
		sb.append(this.getSchemaImports(xsdContainerMap));
		sb.append("</schema>\n");

		File f = Util.getFile(this.outputDirectory, "collected-schemas.xsd");
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		this.getLog().debug("-execute");
	}

	private String getJAXBAppInfo() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t<annotation>\n");
		sb.append("\t\t<appinfo>\n");
		sb.append(
				"\t\t\t<jaxb:globalBindings generateIsSetMethod=\"true\" localScoping=\"toplevel\">\n");
		sb.append("\t\t\t\t<jaxb:serializable/>\n");
		sb.append("\t\t\t</jaxb:globalBindings>\n");
		sb.append("\t\t\t<hj:persistence>\n");
		sb.append(
				"\t\t\t\t<hj:default-generated-id transient=\"true\" name=\"Hjid\"/>\n");
		if (this.stringLength != null && this.stringLength.trim().length() > 0
				&& !this.stringLength.equals("255")) {
			sb.append(
					"\t\t\t\t<hj:default-single-property type=\"string\"><hj:basic><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append("\"/></hj:basic></hj:default-single-property>\n");
			sb.append(
					"\t\t\t\t<hj:default-single-property type=\"xsd:string\"><hj:basic><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append("\"/></hj:basic></hj:default-single-property>\n");
			sb.append(
					"\t\t\t\t<hj:default-single-property type=\"normalizedString\"><hj:basic><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append("\"/></hj:basic></hj:default-single-property>\n");
			sb.append(
					"\t\t\t\t<hj:default-single-property type=\"xsd:normalizedString\"><hj:basic><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append("\"/></hj:basic></hj:default-single-property>\n");

			sb.append(
					"\t\t\t\t<hj:default-collection-property type=\"string\"><hj:element-collection><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append(
					"\"/></hj:element-collection></hj:default-collection-property>\n");
			sb.append(
					"\t\t\t\t<hj:default-collection-property type=\"xsd:string\"><hj:element-collection><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append(
					"\"/></hj:element-collection></hj:default-collection-property>\n");
			sb.append(
					"\t\t\t\t<hj:default-collection-property type=\"normalizedString\"><hj:element-collection><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append(
					"\"/></hj:element-collection></hj:default-collection-property>\n");
			sb.append(
					"\t\t\t\t<hj:default-collection-property type=\"xsd:normalizedString\"><hj:element-collection><orm:column length=\"");
			sb.append(this.stringLength);
			sb.append(
					"\"/></hj:element-collection></hj:default-collection-property>\n");
		}
		sb.append("\t\t\t</hj:persistence>\n");
		sb.append("\t\t</appinfo>\n");
		sb.append("\t</annotation>\n");
		return sb.toString();
	}

	private boolean isMessagePackageName(final String packageName) {
		boolean isMessagePackageName = false;
		if (packageName != null) {
			TreeSet<String> packages = new TreeSet<String>();
			if (this.messagePackageNameSuffixes != null) {
				String[] suffixes = this.messagePackageNameSuffixes
						.replaceAll(",", " ").split(" ");
				for (String suffix : suffixes) {
					if (suffix != null && suffix.trim().length() > 0) {
						packages.add(new StringBuffer(suffix.length() + 1)
								.append(".").append(suffix.trim()).toString());
					}
				}
			}
			for (String string : packages) {
				if (packageName.contains(string)) {
					isMessagePackageName = true;
					break;
				}
			}
		}
		return isMessagePackageName;
	}

	private String getSchemaImports(
			final Map<String, XsdContainer> xsdContainerMap) {
		StringBuffer sb = new StringBuffer(1024);
		for (XsdContainer xc : xsdContainerMap.values()) {
			/* If NOT is message! */
			if (!this.isMessagePackageName(xc.getPackageName())) {
				sb.append("\t<import namespace=\"");
				sb.append(xc.getTargetNamespace());
				sb.append("\" schemaLocation=\"");
				sb.append(Util.getRelativePathTranslated(this.baseDirectory,
						xc.getFile()).substring(1));
				sb.append("\"/>\n");
			}
		}
		return sb.toString();
	}

	private String getXmlDefinition() {
		StringBuffer sb = new StringBuffer(1024);
		/*
		 * Hyperjaxb3 xjc execution neigther supports the new orm tns definition
		 * http://xmlns.jcp.org/xml/ns/persistence/orm nor the jaxb:version 2.2
		 * (even when executed with Java 8).
		 */
		sb.append("<schema xmlns=\"http://www.w3.org/2001/XMLSchema\" \n");
		sb.append(
				"\txmlns:hj=\"http://hyperjaxb3.jvnet.org/ejb/schemas/customizations\" \n");
		sb.append(
				"\txmlns:orm=\"http://java.sun.com/xml/ns/persistence/orm\"\n");
		sb.append("\txmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\" \n");
		sb.append("\tjaxb:version=\"2.1\"\n");
		sb.append("\tjaxb:extensionBindingPrefixes=\"hj orm\"\n");
		sb.append("\telementFormDefault=\"unqualified\" >\n");
		return sb.toString();
	}
}
