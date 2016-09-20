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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * This plugin deletes all Xsd files not being included by the given serviceIds.
 *
 * @author bhausen
 */
@Mojo(name = "condense-service-schemas",
		defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class CondenseServiceSchemasMojo extends AbstractMojo {
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
	private String serviceId;

	/**
	 * Get the executing plugin version - the EIP version.
	 *
	 * @return the EIP version.
	 */
	protected String getEipVersion() {
		return this.execution.getVersion();
	}

	private static void copyFile(final File baseDirectory,
			final File outputDirectory, final File file, final Log log)
					throws IOException {
		String outName = file.getAbsolutePath().replace(
				baseDirectory.getAbsolutePath(),
				outputDirectory.getAbsolutePath());
		Path out = Paths.get(new File(outName).toURI());
		if (!Files.isWritable(out.getParent())) {
			Files.createDirectories(out.getParent());
		}
		try (FileInputStream fis = new FileInputStream(file)) {
			Files.copy(fis, out);
			log.info(String.format("Copy xsd %s", file.getAbsolutePath()
					.replace(baseDirectory.getAbsolutePath(), "")));
		} catch (IOException e) {
			log.info(String.format("Could not copy file %s to ", file,
					out.toFile().getAbsolutePath()));
			throw e;
		}
	}

	private static void addImportedNamespaces(final XsdsUtil xsds,
			final XsdContainer xc, final TreeSet<String> imports) {
		imports.add(xc.getTargetNamespace());
		xc.getImportedTargetNamespaces().stream()
				.filter(ns -> !imports.contains(ns)).forEach(ns -> {
					imports.add(ns);
					addImportedNamespaces(xsds, xsds.getXsdContainer(ns),
							imports);
				});
	}

	protected static void condense(final XsdsUtil xsds, final String serviceId,
			final File baseDirectory, final File outputDirectory, final Log log)
					throws IOException {
		List<String> serviceIds = ServiceIdRegistry.splitServiceIds(serviceId);
		if (Objects.nonNull(serviceIds) && serviceIds.size() > 0) {
			xsds.getXsdContainerMap().values().stream()
					.forEach(xc -> log
							.debug(String.format("Contains namespaces: %s %s",
									xc.getTargetNamespace(), xc.getFile())));
			TreeSet<String> importedNamespaces = new TreeSet<String>();
			xsds.getServiceIdRegistry().getAllServiceIds().stream()
					.filter(si -> serviceIds.contains(si))
					.map(si -> xsds.getServiceIdRegistry()
							.getServiceIdEntry(si))
					.map(sie -> sie.getTargetNamespace())
					.filter(tn -> Objects.nonNull(tn))
					.map(tn -> xsds.getXsdContainer(tn))
					.filter(xc -> Objects.nonNull(xc)).forEach(xc -> {
						addImportedNamespaces(xsds, xc, importedNamespaces);
					});
			if (Objects.nonNull(outputDirectory)
					&& !baseDirectory.equals(outputDirectory)) {
				log.info(String.format("Copy namespaces: %s",
						importedNamespaces));
				xsds.getXsdContainerMap().values().stream()
						.filter(xc -> importedNamespaces
								.contains(xc.getTargetNamespace()))
						.forEach(xc -> {
							try {
								copyFile(baseDirectory, outputDirectory,
										xc.getFile(), log);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
			} else {
				log.info(String.format("Keep namespaces: %s",
						importedNamespaces));
				xsds.getXsdContainerMap().values().stream()
						.filter(xc -> !importedNamespaces
								.contains(xc.getTargetNamespace()))
						.forEach(xc -> {
							if (xc.getFile().delete()) {
								log.info(String.format("Delete file %s",
										xc.getFile()));
							} else {
								log.info(String.format(
										"Could not delete file %s",
										xc.getFile()));
							}
						});
			}
		}
	}

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
		try {
			condense(xsds, this.serviceId, this.baseDirectory,
					this.outputDirectory, this.getLog());
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage());
		}
		this.getLog().debug("-execute");
	}
}
