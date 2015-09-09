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
package com.qpark.maven.plugin.mockrestoperation;

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

import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * The mock operation providers will be created by this plugin. They provide a
 * simple valid response of the given response type.
 * @author bhausen
 */
@Mojo(name = "generate-mock-rest-operations", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
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
	@Parameter(property = "restMessagePackageNameSuffix", defaultValue = "restmsg")
	private String restMessagePackageNameSuffix;
	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	@Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
	private String deltaPackageNameSuffix;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	private String serviceId;
	/** The name of the service id of common services. */
	@Parameter(property = "serviceIdCommonServices", defaultValue = "common")
	private String serviceIdCommonServices;
	/** <code>true</code>, if common service/config... should be generated too. */
	@Parameter(property = "serviceCreationWithCommon", defaultValue = "true")
	private boolean serviceCreationWithCommon;
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
	/** The name of the failure handler class. */
	@Parameter(property = "failureHandlerClassName")
	private String failureHandlerClassName;
	@Component
	protected MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		XsdsUtil xsds = new XsdsUtil(this.baseDirectory, this.basePackageName,
				this.restMessagePackageNameSuffix, this.deltaPackageNameSuffix,
				this.serviceRequestSuffix, this.serviceResponseSuffix);
		RestOperationProviderMockGenerator mop;

		Collection<String> serviceIds = ServiceIdRegistry
				.getServiceIds(this.serviceId);
		if (serviceIds.size() == 0) {
			serviceIds = ServiceIdRegistry.getAllServiceIds();
		}
		for (String sid : serviceIds) {
			for (ElementType element : xsds.getElementTypes()) {
				if (element.isRequest()
						&& ServiceIdRegistry.isValidServiceId(
								element.getServiceId(), sid)) {
					mop = new RestOperationProviderMockGenerator(xsds,
							this.outputDirectory, element,
							this.failureHandlerClassName, this.getLog());
					mop.generate();
				}
			}
		}
		this.getLog().debug("-execute");
	}
}