/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.servletconfig;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Create the servlet definitions in <code>web.xml</code>,
 * <code>ws-servlet.xml</code> and <code>http-servlet.xml</code>.
 *
 * @author bhausen
 */
@Mojo(name = "generate-servlet-config",
		defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class GeneratorMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory",
			defaultValue = "${project.build.directory}/model")
	private File baseDirectory;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
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
	/** The name of the war file. */
	@Parameter(property = "warName", defaultValue = "bus.war")
	private String warName;
	/** The revision number. */
	@Parameter(property = "revisionNumber", defaultValue = "")
	private String revisionNumber;
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	private String serviceId;
	/**
	 * The version of the service to generate. Defaults to <code>trunk</code>.
	 */
	@Parameter(property = "serviceVersion", defaultValue = "trunk")
	private String serviceVersion;
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
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	/**
	 * The bean definition of the additional web service payload interceptors.
	 */
	@Parameter(property = "additionalWebservicePayloadInterceptors",
			defaultValue = "")
	private String additionalWebservicePayloadInterceptors;
	/**
	 * <code>true</code>, if no payload validation should be added to the web
	 * service endpoint .
	 */
	@Parameter(property = "disableWebservicePayloadValidation",
			defaultValue = "false")
	private boolean disableWebservicePayloadValidation;

	/** The class name of the implementing webservice pay load logger. */
	@Parameter(property = "webservicePayloadLoggerImplementation",
			defaultValue = "com.qpark.eip.core.spring.PayloadLogger")
	private String webservicePayloadLoggerImplementation;
	/**
	 * The class name of the implementing a javax.servlet.Filter to be applied
	 * at <code>/*</code>.
	 */
	@Parameter(property = "additionalWebappFilter", defaultValue = "")
	private String additionalWebappFilter;
	@Parameter(defaultValue = "${mojoExecution}", readonly = true)
	protected MojoExecution execution;

	/**
	 * Get the executing plugin version - the EIP version.
	 *
	 * @return the EIP version.
	 */
	protected String getEipVersion() {
		return this.execution.getVersion();
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
		String eipVersion = this.getEipVersion();

		WebXmlGenerator wx = new WebXmlGenerator(xsds, this.serviceId,
				this.serviceVersion, this.revisionNumber, this.warName,
				this.additionalWebappFilter, this.outputDirectory, eipVersion,
				this.getLog());
		wx.generate();

		WsServletXmlGenerator wsx = new WsServletXmlGenerator(xsds,
				this.basePackageName, this.serviceId,
				this.disableWebservicePayloadValidation,
				this.webservicePayloadLoggerImplementation,
				this.additionalWebservicePayloadInterceptors,
				this.outputDirectory, this.project, eipVersion, this.getLog());
		wsx.generate();

		this.getLog().debug("-execute");
	}
}
