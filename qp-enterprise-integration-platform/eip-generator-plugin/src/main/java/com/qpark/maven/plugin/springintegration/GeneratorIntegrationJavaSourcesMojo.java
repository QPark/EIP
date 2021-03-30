/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * This plugin creates:
 * <ul>
 * <li>all spring-integration gateways of all serviceIds and operations</li>
 * <li>for all service ids one OperationProvider implementation proposal (in the project.build.directory)</li>
 * </ul>
 * @author bhausen
 */
@Mojo(name = "generate-integration-java", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class GeneratorIntegrationJavaSourcesMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;
	/**
	 * The package name of the messages should end with this. Default is <code>msg flow</code>.
	 */
	@Parameter(property = "messagePackageNameSuffix", defaultValue = "msg flow")
	protected String messagePackageNameSuffix;
	/**
	 * The package name of the delta should contain this. Default is <code>delta</code>.
	 */
	@Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
	protected String deltaPackageNameSuffix;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	protected String basePackageName;
	/**
	 * The service request name need to end with this suffix (Default <code>Request</code>).
	 */
	@Parameter(property = "serviceRequestSuffix", defaultValue = "Request")
	protected String serviceRequestSuffix;
	/**
	 * The service response name need to end with this suffix (Default <code>Response</code>).
	 */
	@Parameter(property = "serviceResponseSuffix", defaultValue = "Response")
	protected String serviceResponseSuffix;
	@Parameter(defaultValue = "${project}", readonly = true)
	protected MavenProject project;
	@Parameter(defaultValue = "${mojoExecution}", readonly = true)
	protected MojoExecution execution;
	/**
	 * If <code>true</code>, the gateways and operation provider are generated in reactive manner.
	 */
	@Parameter(property = "reactive", defaultValue = "false")
	private boolean reactive;

	/**
	 * Get the executing plugin version - the EIP version.
	 * @return the EIP version.
	 */
	protected String getEipVersion() {
		return this.execution.getVersion();
	}

	protected void generate(final XsdsUtil xsds) {
		IntegrationGatewayGenerator ig;
		String eipVersion = this.getEipVersion();

		this.generateBasicIntegrationGatewayInterface(eipVersion);
		TreeMap<String, List<IntegrationGatewayGenerator>> serviceIgMap = new TreeMap<>();
		List<IntegrationGatewayGenerator> igs;
		for (ElementType element : xsds.getElementTypes()) {
			if (element.isRequest()) {
				ig = new IntegrationGatewayGenerator(xsds, this.outputDirectory, element, this.basePackageName,
						this.reactive, eipVersion, this.getLog());
				ig.generate();
				if (ig.isGenerated()) {
					igs = serviceIgMap.get(ig.getServiceId());
					if (igs == null) {
						igs = new ArrayList<>();
						serviceIgMap.put(ig.getServiceId(), igs);
					}
					igs.add(ig);
				}
			}
		}
		ServiceOperationProviderGenerator sopg;
		for (Entry<String, List<IntegrationGatewayGenerator>> entry : serviceIgMap.entrySet()) {
			sopg = new ServiceOperationProviderGenerator(entry.getKey(), entry.getValue(), this.basePackageName,
					this.reactive, eipVersion, this.getLog(), this.project);
			sopg.generate();
		}
	}

	private void generateBasicIntegrationGatewayInterface(final String eipVersion) {
		StringBuffer sb = new StringBuffer();
		sb.append("package com.qpark.eip;\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * Basic spring integration interface.\n");
		sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(), eipVersion));
		sb.append(" */\n");
		sb.append("public interface IntegrationGateway<JaxbRequest, JaxbResponse> {\n");
		sb.append("\t/**\n");
		sb.append("\t * Invoke the spring integration gateway.\n");
		sb.append("\t * @param request the {@link JaxbRequest}\n");
		sb.append("\t * @return the {@link JaxbResponse}\n");
		sb.append("\t */\n");
		sb.append("\tJaxbResponse invoke(JaxbRequest request);\n");
		sb.append("}\n");
		sb.append("\n");
		File f = Util.getFile(this.outputDirectory, "com.qpark.eip", "IntegrationGateway.java");
		this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
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
		XsdsUtil xsds = XsdsUtil.getInstance(this.baseDirectory, this.basePackageName, this.messagePackageNameSuffix,
				this.deltaPackageNameSuffix, this.serviceRequestSuffix, this.serviceResponseSuffix);

		this.generate(xsds);

		this.getLog().debug("-execute");
	}
}
