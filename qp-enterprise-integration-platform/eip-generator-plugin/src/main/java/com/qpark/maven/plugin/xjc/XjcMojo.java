/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.xjc;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.mojo.jaxb2.javageneration.AbstractJavaGeneratorMojo;
import org.codehaus.mojo.jaxb2.shared.FileSystemUtilities;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * <p>
 * Mojo that creates compile-scope Java source or binaries from XML schema(s) by
 * invoking the JAXB XJC binding compiler. This implementation is tailored to
 * use the JAXB Reference Implementation from project Kenai.
 * </p>
 * <p>
 * Note that the XjcMojo was completely re-implemented for the 2.x versions. Its
 * configuration semantics and parameter set is <strong>not necessarily
 * backwards compatible</strong> with the 1.x plugin versions. If you are
 * upgrading from version 1.x of the plugin, read the documentation carefully.
 * </p>
 *
 * @see <a href="https://jaxb.java.net/">The JAXB Reference Implementation</a>
 */
@Mojo(name = "xjc", threadSafe = false,
		defaultPhase = LifecyclePhase.GENERATE_SOURCES,
		requiresDependencyResolution = ResolutionScope.COMPILE)
public class XjcMojo extends AbstractJavaGeneratorMojo {

	/**
	 * The last part of the stale fileName for this XjcMojo.
	 */
	public static final String STALE_FILENAME = "xjcStaleFlag";

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory",
			defaultValue = "${project.build.directory}/model")
	private File baseDirectory;

	/**
	 * All model xsds should be taken into account, even if they are not
	 * referenced in the message xsds. Default is <code>false</code>.
	 */
	@Parameter(property = "includeAllModels", defaultValue = "false")
	private boolean includeAllModels;

	/**
	 * The package names of the messages should end with - separation by space.
	 * Default is <code>msg svc mapping flow</code>.
	 */
	@Parameter(property = "messagePackageNameSuffixes",
			defaultValue = "msg svc mapping flow")
	private String messagePackageNameSuffixes;
	/**
	 * <p>
	 * Corresponding XJC parameter: {@code d}.
	 * </p>
	 * <p>
	 * The working directory where the generated Java source files are created.
	 * </p>
	 */
	@Parameter(defaultValue = "${project.build.directory}/generated-sources",
			required = true)
	private File outputDirectory;
	/**
	 * The schema location prefixes separated by <code>,</code>. This is used to
	 * generate the catalog file.
	 */
	@Parameter(property = "schemalocationPrefix")
	protected String schemalocationPrefix;

	/**
	 * @return
	 * @throws MojoExecutionException
	 * @throws MojoFailureException
	 */
	@Override
	protected boolean performExecution()
			throws MojoExecutionException, MojoFailureException {
		this.clearOutputDir = true;
		this.quiet = false;
		this.generateEpisode = false;
		this.createCatalog();
		boolean updateStaleFileTimestamp = false;
		try {
			updateStaleFileTimestamp = super.performExecution();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			sw.toString();
			if (e.getCause() != null && e.getCause() != e) {
				e.getCause().printStackTrace(pw);
				this.getLog().info(e.getCause().getMessage());
				this.getLog().info(sw.toString());
			} else {
				e.printStackTrace(pw);
				this.getLog().info(e.getMessage());
				this.getLog().info(sw.toString());
			}
			this.getLog().error(e.getMessage(), e);
		}
		return updateStaleFileTimestamp;
	}

	private void createCatalog() {
		StringBuffer catalogXml = new StringBuffer(1024);
		StringBuffer catalogCat = new StringBuffer(1024);
		if (this.schemalocationPrefix == null
				|| this.schemalocationPrefix.trim().length() == 0) {
			this.getLog().info(
					"No schema location prefix to generate catalog file provided.");
		} else {
			catalogXml.append("<?xml version=\"1.0\"?>\n");
			catalogXml.append(
					"<!DOCTYPE catalog PUBLIC \"-//OASIS/DTD Entity Resolution XML Catalog V1.0//EN\" \"http://www.oasis-open.org/committees/entity/release/1.0/catalog.dtd\">\n");
			catalogXml.append(
					"<catalog xmlns=\"urn:oasis:names:tc:entity:xmlns:xml:catalog\">\n");
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
		this.catalog = Util.getFile(
				new File(this.getProject().getBuild().getDirectory()),
				"catalog.cat");
		this.getLog().info(new StringBuffer().append("Write ")
				.append(this.catalog.getAbsolutePath()));
		try {
			Util.writeToFile(this.catalog, catalogCat.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		File f = Util.getFile(
				new File(this.getProject().getBuild().getDirectory()),
				"catalog.xml");
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, catalogXml.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * @see org.codehaus.mojo.jaxb2.javageneration.AbstractJavaGeneratorMojo#addGeneratedSourcesToProjectSourceRoot()
	 */
	@Override
	protected void addGeneratedSourcesToProjectSourceRoot() {
		this.getProject().addCompileSourceRoot(
				this.getOutputDirectory().getAbsolutePath());
	}

	/**
	 * @see org.codehaus.mojo.jaxb2.javageneration.AbstractJavaGeneratorMojo#addResource(org.apache.maven.model.Resource)
	 */
	@Override
	protected void addResource(final Resource resource) {
		this.getProject().addResource(resource);
	}

	/**
	 * @see org.codehaus.mojo.jaxb2.AbstractJaxbMojo#getClasspath()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getClasspath() throws MojoExecutionException {
		try {
			return this.getProject().getCompileClasspathElements();
		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException(
					"Could not retrieve Compile classpath.", e);
		}
	}

	/**
	 * @see org.codehaus.mojo.jaxb2.AbstractJaxbMojo#getOutputDirectory()
	 */
	@Override
	protected File getOutputDirectory() {
		return this.outputDirectory;
	}

	private List<String> getSchemaList(
			final Map<String, XsdContainer> xsdContainerMap) {
		List<String> sources = new ArrayList<String>();
		StringBuffer sb = new StringBuffer(1024);
		Set<String> imported = new TreeSet<String>();
		List<String> xsdWarnings = new ArrayList<String>();
		StringBuffer warnings = new StringBuffer(128);
		if (!this.includeAllModels) {
			for (XsdContainer xc : xsdContainerMap.values()) {
				for (String warning : xc.getWarnings()) {
					warnings.append(xc.getTargetNamespace()).append(":\t")
							.append(warning).append("\n");
				}
				if (XsdsUtil.isMessagePackageName(xc.getPackageName(),
						this.messagePackageNameSuffixes,
						this.messagePackageNameSuffixes)) {
					if (sb.length() > 0) {
						sb.append(",\n");
					}
					sources.add(xc.getFile().getAbsolutePath());
					sb.append(Util.getRelativePathTranslated(this.baseDirectory,
							xc.getFile()).substring(1));
					this.setImportedNamespaces(xc, xsdContainerMap, imported);
				}
			}
		} else {
			for (XsdContainer xc : xsdContainerMap.values()) {
				imported.addAll(xc.getTotalImportedTargetNamespaces());
			}
			for (XsdContainer xc : xsdContainerMap.values()) {
				for (String warning : xc.getWarnings()) {
					warnings.append(xc.getTargetNamespace()).append(":\t")
							.append(warning).append("\n");
				}
				if (!imported.contains(xc.getTargetNamespace())) {
					if (sb.length() > 0) {
						sb.append(",\n");
					}
					sources.add(xc.getFile().getAbsolutePath());
					sb.append(Util.getRelativePathTranslated(this.baseDirectory,
							xc.getFile()).substring(1));
				}
			}
		}
		this.getLog().info(new StringBuilder("Using source files in ")
				.append(this.baseDirectory).append(": ").toString());
		for (String source : sources) {
			this.getLog().info("\t" + source);
		}
		File f = Util.getFile(new File(this.outputDirectory, ".."),
				"domain-warnings.txt");
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, warnings.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		return sources;
	}

	/**
	 * @see org.codehaus.mojo.jaxb2.javageneration.AbstractJavaGeneratorMojo#getSources()
	 */
	@Override
	protected List<URL> getSources() {
		List<String> standardDirectories = new ArrayList<String>(1);
		standardDirectories.add(this.baseDirectory.getAbsolutePath());

		Map<String, XsdContainer> xsdContainerMap = XsdsUtil
				.getXsdContainers(this.baseDirectory);

		return FileSystemUtilities.filterFiles(this.getProject().getBasedir(),
				this.getSchemaList(xsdContainerMap), standardDirectories,
				this.getLog(), "sources", Collections.emptyList());
	}

	/**
	 * @see org.codehaus.mojo.jaxb2.javageneration.AbstractJavaGeneratorMojo#getSourceXJBs()
	 */
	@Override
	protected List<File> getSourceXJBs() {
		return Collections.emptyList();
	}

	/**
	 * @see org.codehaus.mojo.jaxb2.AbstractJaxbMojo#getStaleFileName()
	 */
	@Override
	protected String getStaleFileName() {
		return STALE_FILENAME;
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

	/**
	 * @see org.codehaus.mojo.jaxb2.AbstractJaxbMojo#shouldExecutionBeSkipped()
	 */
	@Override
	protected boolean shouldExecutionBeSkipped() {
		return false;
	}
}
