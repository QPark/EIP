/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import java.io.File;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * The mock operation providers will be created by this plugin. They provide a
 * simple valid response of the given response type.
 *
 * @author bhausen
 */
@Mojo(name = "generate-test-client",
		defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class TestClientGeneratorMojo extends AbstractMojo {
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
	@Parameter(property = "useSpringInsightAnnotation", defaultValue = "false")
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
		XsdsUtil xsds = new XsdsUtil(this.baseDirectory, this.basePackageName,
				this.messagePackageNameSuffix, this.deltaPackageNameSuffix,
				this.serviceRequestSuffix, this.serviceResponseSuffix);
		TestClientGenerator tc;

		Collection<String> serviceIds = ServiceIdRegistry
				.splitServiceIds(this.serviceId);
		if (serviceIds.size() == 0) {
			serviceIds = ServiceIdRegistry.getAllServiceIds();
		}

		this.getLog().info("ServiceId size " + serviceIds.size());

		for (String sid : serviceIds) {
			this.getLog().info("ServiceId " + sid);
			StringBuffer sb = new StringBuffer(1024);
			TreeSet<String> imports = new TreeSet<String>();
			imports.add("javax.xml.bind.JAXBElement");
			imports.add(
					"org.springframework.ws.client.core.support.WebServiceGatewaySupport");

			StringBuffer impl = new StringBuffer();
			String s;
			String packageName = this.basePackageName;
			String objectFactoryClassName = "";
			for (ElementType element : xsds.getElementTypes()) {
				if (element.isRequest() && element.getServiceId().equals(sid)) {
					tc = new TestClientGenerator(xsds, element,
							this.useSpringInsightAnnotation, this.getLog());
					s = tc.generate();
					if (s.length() > 0) {
						if (packageName == null
								|| packageName.trim().length() == 0) {
							packageName = element.getPackageNameGateway()
									.replace(".gateway", ".client");
						}
						objectFactoryClassName = new StringBuffer(
								element.getPackageName())
										.append(".ObjectFactory").toString();
						impl.append("\n").append(s);
						imports.addAll(tc.getImports());
						imports.add(objectFactoryClassName);
					}
				}
			}
			if (impl.length() > 0) {
				String className = new StringBuffer()
						.append(Util.getXjcClassName(sid))
						.append("ServiceClient").toString();
				if (packageName.length() > 0) {
					sb.append("package ");
					sb.append(packageName);
					sb.append(";\n");
					sb.append("\n");
				}
				for (String imported : imports) {
					if (imported != null && imported.trim().length() > 0) {
						sb.append("import ");
						sb.append(imported);
						sb.append(";\n");
					}
				}
				sb.append("/**\n");
				sb.append(" * Client implementation of service <code>");
				sb.append(sid);
				sb.append("</code> using\n");
				sb.append(" * the {@link WebServiceGatewaySupport}.\n");
				sb.append(" * <pre>");
				sb.append(Util.getGeneratedAt());
				sb.append("</pre>\n");
				sb.append(" * @author bhausen\n");
				sb.append(" */\n");
				sb.append("public class ");
				sb.append(className);
				sb.append(" extends WebServiceGatewaySupport {\n");

				sb.append("\t/** Service {@link ObjectFactory}. */\n");
				sb.append(
						"\tprivate final ObjectFactory objectFactory = new ObjectFactory();\n");

				sb.append("\n");
				sb.append("\t/**\n");
				sb.append(
						"\t * @return the {@link ObjectFactory} of the service.\n");
				sb.append("\t */\n");
				sb.append("\tpublic ObjectFactory getObjectFactory() {\n");
				sb.append("\t\treturn this.objectFactory;\n");
				sb.append("\t}\n");
				sb.append("\n");

				sb.append(impl);

				sb.append("}\n");

				File f = Util.getFile(this.outputDirectory, packageName,
						new StringBuffer(className).append(".java").toString());
				this.getLog().info(new StringBuffer().append("Write ")
						.append(f.getAbsolutePath()));
				try {
					Util.writeToFile(f, sb.toString());
				} catch (Exception e) {
					this.getLog().error(e.getMessage());
					e.printStackTrace();
				}
			}
		}

		this.getLog().debug("-execute");
	}
}
