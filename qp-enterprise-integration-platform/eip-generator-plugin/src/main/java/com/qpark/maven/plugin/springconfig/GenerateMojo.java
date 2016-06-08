/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springconfig;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generate various spring configuration files:
 * <ul>
 * <li>spring-integration dispatcher config</li>
 * <li>spring-ws dynamic wsdl config</li>
 * <li>application bases
 * <code>com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer</code>
 * config</li>
 * <li>main spring application conifg</li>
 * </ul>
 *
 * @author bhausen
 */
@Mojo(name = "generate-spring-config", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class GenerateMojo extends AbstractMojo {
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
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	private String serviceId;
	/**
	 * The version of the service to generate. Defaults to <code>trunk</code>.
	 */
	@Parameter(property = "serviceVersion", defaultValue = "trunk")
	private String serviceVersion;
	/** The SCM revision number to generate. Defaults to <code>r0</code>. */
	@Parameter(property = "revisionNumber", defaultValue = "r0")
	private String revisionNumber;
	/**
	 * The this set to <code>true</code> the Application context does not load
	 * the persistence-spring-config.xml (e.g. needed if you only the generate
	 * the wsld for the Interface control document).
	 */
	@Parameter(property = "applicationWithoutPersistenceConfig", defaultValue = "false")
	private String applicationWithoutPersistenceConfig;
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
	/** The name of the war file. */
	@Parameter(property = "warName", defaultValue = "bus.war")
	private String warName;
	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	@Parameter(property = "placeholderConfigurerImpl", defaultValue = "com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer")
	private String placeholderConfigurerImpl;
	@Component
	private MavenProject project;

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
		String eipVersion = null;
		if (this.project.getArtifact() != null) {
			eipVersion = this.project.getArtifact().getVersion();
		}

		WebServiceDispatcherXmlGenerator wsdx = new WebServiceDispatcherXmlGenerator(xsds, this.serviceId, this.warName,
				this.outputDirectory, this.project, eipVersion, this.getLog());
		wsdx.generate();

		ApplicationPropertiesConfigXmlGenerator pcx = new ApplicationPropertiesConfigXmlGenerator(this.basePackageName,
				this.serviceId, this.serviceVersion, this.revisionNumber, this.placeholderConfigurerImpl,
				this.outputDirectory, this.project, eipVersion, this.getLog());
		pcx.generate();

		MainApplicationContextXmlGenerator macx = new MainApplicationContextXmlGenerator(this.basePackageName,
				this.applicationWithoutPersistenceConfig, this.outputDirectory, this.project, eipVersion,
				this.getLog());
		macx.generate();

		this.getLog().debug("-execute");
	}
}
