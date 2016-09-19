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

	protected static void condense(final XsdsUtil xsds, final String serviceId,
			final Log log) {
		List<String> serviceIds = ServiceIdRegistry.splitServiceIds(serviceId);
		if (Objects.nonNull(serviceIds) && serviceIds.size() > 0) {
			xsds.getXsdContainerMap().values().stream()
					.forEach(xc -> log
							.info(String.format("Contains namespaces: %s %s",
									xc.getTargetNamespace(), xc.getFile())));
			TreeSet<String> totalTargetNamespaces = new TreeSet<String>();
			xsds.getServiceIdRegistry().getAllServiceIds().stream()
					.filter(si -> serviceIds.contains(si))
					.map(si -> xsds.getServiceIdRegistry()
							.getServiceIdEntry(si))
					.map(sie -> sie.getTargetNamespace())
					.filter(tn -> Objects.nonNull(tn))
					.map(tn -> xsds.getXsdContainer(tn))
					.filter(xc -> Objects.nonNull(xc))
					.map(xc -> xc.getTotalImportedTargetNamespaces())
					.forEach(tns -> totalTargetNamespaces.addAll(tns));
			log.info(String.format("Keep namespaces: %s",
					totalTargetNamespaces));
			xsds.getXsdContainerMap().values().stream()
					.filter(xc -> !totalTargetNamespaces
							.contains(xc.getTargetNamespace()))
					.forEach(xc -> {
						if (xc.getFile().delete()) {
							log.info(String.format("Delete file %s",
									xc.getFile()));
						} else {
							log.info(String.format("Could not delete file %s",
									xc.getFile()));
						}
					});
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

		condense(xsds, this.serviceId, this.getLog());
		this.getLog().debug("-execute");
	}
}
