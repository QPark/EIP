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
package com.qpark.maven.plugin.xjcprepare;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Create the schema file list and the catalog files to prepare the
 * org.codehaus.mojo jaxb2-maven-plugin xjc goal.
 * @author bhausen
 */
@Mojo(name = "generate-xjc-preparations", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class GenerateMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;
	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffix", defaultValue = "")
	@Deprecated
	protected String messagePackageNameSuffix;
	/**
	 * The package names of the messages should end with - separation by space.
	 * Default is <code>msg restmsg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffixes", defaultValue = "msg restmsg")
	protected String messagePackageNameSuffixes;
	/**
	 * All model xsds should be taken into account, even if they are not
	 * referenced in the message xsds. Default is <code>false</code>.
	 */
	@Parameter(property = "includeAllModels", defaultValue = "false")
	protected boolean includeAllModels;
	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg</code>.
	 */
	@Parameter(property = "schemalocationPrefix")
	protected String schemalocationPrefix;
	@Component
	private MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		Map<String, XsdContainer> xsdContainerMap = XsdsUtil
				.getXsdContainers(this.baseDirectory);
		StringBuffer schemaFileList = new StringBuffer(1024);
		schemaFileList.append(this.getSchemaList(xsdContainerMap));

		File f = Util.getFile(this.outputDirectory, "schemaFileList.txt");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, schemaFileList.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		StringBuffer catalogXml = new StringBuffer(1024);
		StringBuffer catalogCat = new StringBuffer(1024);
		if (this.schemalocationPrefix == null
				|| this.schemalocationPrefix.trim().length() == 0) {
			this.getLog()
					.info("No schema location prefix to generate catalog file provided.");
		} else {
			catalogXml.append("<?xml version=\"1.0\"?>\n");
			catalogXml
					.append("<!DOCTYPE catalog PUBLIC \"-//OASIS/DTD Entity Resolution XML Catalog V1.0//EN\" \"http://www.oasis-open.org/committees/entity/release/1.0/catalog.dtd\">\n");
			catalogXml
					.append("<catalog xmlns=\"urn:oasis:names:tc:entity:xmlns:xml:catalog\">\n");
			String[] slps = this.schemalocationPrefix.split(",");
			String s = this.baseDirectory.getAbsolutePath();
			s = s.replace("\\", "/");
			if (s.indexOf('/') > 0) {
				s = s.substring(s.indexOf('/'));
			}
			for (String slp : slps) {
				slp = slp.trim();
				if (slp.endsWith("/") && slp.length() > 1) {
					slp = slp.substring(0, slp.length() - 1);
				}
				if (slp.length() > 0) {
					catalogXml
							.append("\t<rewriteSystem systemIdStartString=\"");
					catalogXml.append(slp);
					catalogXml.append("\" rewritePrefix=\"");
					catalogXml.append(s);
					catalogXml.append("\"/>\n");
					catalogCat.append("REWRITE_SYSTEM \"");
					catalogCat.append(slp);
					catalogCat.append("\" \"");
					catalogCat.append(s);
					catalogCat.append("\"\n");
				}
			}
			catalogXml.append("</catalog>\n");
		}
		f = Util.getFile(this.outputDirectory, "catalog.cat");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, catalogCat.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		f = Util.getFile(this.outputDirectory, "catalog.xml");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, catalogXml.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		this.getLog().debug("-execute");
	}

	private void setImportedNamespaces(final XsdContainer container,
			final Map<String, XsdContainer> xsdContainerMap,
			final Set<String> imported) {
		if (container != null) {
			for (String namespace : container.getImportedTargetNamespaces()) {
				if (namespace != container.getTargetNamespace()
						&& !imported.contains(namespace)) {
					imported.add(namespace);
					this.setImportedNamespaces(xsdContainerMap.get(namespace),
							xsdContainerMap, imported);
				}
			}
		}
	}

	private String getSchemaList(final Map<String, XsdContainer> xsdContainerMap) {
		StringBuffer sb = new StringBuffer(1024);
		Set<String> imported = new TreeSet<String>();
		if (!this.includeAllModels) {
			for (XsdContainer xc : xsdContainerMap.values()) {
				if (XsdsUtil.isMessagePackageName(xc.getPackageName(),
						this.messagePackageNameSuffix,
						this.messagePackageNameSuffixes)) {
					if (sb.length() > 0) {
						sb.append(",\n");
					}
					sb.append(Util.getRelativePathTranslated(
							this.baseDirectory, xc.getFile()).substring(1));
					this.setImportedNamespaces(xc, xsdContainerMap, imported);
				}
			}
		} else {
			for (XsdContainer xc : xsdContainerMap.values()) {
				imported.addAll(xc.getTotalImportedTargetNamespaces());
			}
			for (XsdContainer xc : xsdContainerMap.values()) {
				if (!imported.contains(xc.getTargetNamespace())) {
					if (sb.length() > 0) {
						sb.append(",\n");
					}
					sb.append(Util.getRelativePathTranslated(
							this.baseDirectory, xc.getFile()).substring(1));
				}
			}
			// TreeMap<String, File> files = new TreeMap<String, File>();
			// for (XsdContainer xc : xsdContainerMap.values()) {
			// files.put(xc.getTargetNamespace(), xc.getFile());
			// }
			// for (XsdContainer xc : xsdContainerMap.values()) {
			// for (String importedNamespace : xc
			// .getImportedTargetNamespaces()) {
			// files.remove(importedNamespace);
			// }
			// }
			// for (Entry<String, File> entry : files.entrySet()) {
			// if (sb.length() > 0) {
			// sb.append(",\n");
			// }
			// sb.append(Util.getRelativePathTranslated(this.baseDirectory,
			// entry.getValue()).substring(1));
			// }
		}
		return sb.toString();
	}
}
