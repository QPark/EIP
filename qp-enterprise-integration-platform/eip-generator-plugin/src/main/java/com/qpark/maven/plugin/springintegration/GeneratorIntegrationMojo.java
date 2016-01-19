/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * This plugin creates:
 * <ul>
 * <li>all spring-integration gateways of all serviceIds and operations</li>
 * <li>for all service ids the spring-integration config defining the java
 * gateways, the channels, routers and ws-gatways</li>
 * <li>for all service ids one OperationProvider implementation proposal (in the
 * project.build.directory)</li>
 * </ul>
 *
 * @author bhausen
 */
@Mojo(name = "generate-integration",
		defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class GeneratorIntegrationMojo
		extends GeneratorIntegrationJavaSourcesMojo {
	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		XsdsUtil xsds = new XsdsUtil(this.baseDirectory, this.basePackageName,
				this.messagePackageNameSuffix, this.deltaPackageNameSuffix,
				this.serviceRequestSuffix, this.serviceResponseSuffix);
		String eipVersion = null;
		if (this.project.getArtifact() != null) {
			eipVersion = this.project.getArtifact().getVersion();
		}

		this.generate(xsds);

		SpringIntegrationConfigGenerator tc = new SpringIntegrationConfigGenerator(
				xsds, this.basePackageName, this.outputDirectory, eipVersion,
				this.getLog(), this.project);
		tc.generate();

		this.getLog().debug("-execute");
	}
}
