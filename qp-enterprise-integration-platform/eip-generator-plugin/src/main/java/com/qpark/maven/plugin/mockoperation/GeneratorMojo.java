/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.mockoperation;

import java.io.File;
import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * The mock operation providers will be created by this plugin. They provide a
 * simple valid response of the given response type.
 *
 * @author bhausen
 */
@Mojo(name = "generate-mock-operations", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class GeneratorMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	private File baseDirectory;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	private File outputDirectory;
	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffix", defaultValue = "msg")
	private String messagePackageNameSuffix;
	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	@Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
	private String deltaPackageNameSuffix;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	/** The list of names of the service ids to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	private String serviceId;
	/**
	 * The service request name need to end with this suffix (Default
	 * <code>Request</code>).
	 */
	@Parameter(property = "serviceRequestSuffix", defaultValue = "Request")
	private String serviceRequestSuffix;
	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	@Parameter(property = "serviceResponseSuffix", defaultValue = "Response")
	private String serviceResponseSuffix;
	/**
	 * <code>true</code>, if the spring insight InsightEndPoint annotation
	 * should be added to the mocked operation.
	 */
	@Parameter(property = "useSpringInsightAnnotation", defaultValue = "true")
	private boolean useSpringInsightAnnotation;
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
		XsdsUtil xsds = XsdsUtil.getInstance(this.baseDirectory, this.basePackageName, this.messagePackageNameSuffix,
				this.deltaPackageNameSuffix, this.serviceRequestSuffix, this.serviceResponseSuffix);
		OperationProviderMockGenerator mop;

		Collection<String> serviceIds = ServiceIdRegistry.splitServiceIds(this.serviceId);
		if (serviceIds.size() == 0) {
			serviceIds = ServiceIdRegistry.getAllServiceIds();
		}
		String version = null;
		if (this.project.getArtifact() != null) {
			version = this.project.getArtifact().getVersion();
		}
		for (String sid : serviceIds) {
			for (ElementType element : xsds.getElementTypes()) {
				if (element.isRequest() && ServiceIdRegistry.isValidServiceId(element.getServiceId(), sid)) {
					mop = new OperationProviderMockGenerator(xsds, this.outputDirectory, element,
							this.useSpringInsightAnnotation, version, this.getLog());
					mop.generate();
				}
			}
		}
		this.getLog().debug("-execute");
	}
}
