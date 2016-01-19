/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
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
		String eipVersion = null;
		if (this.project.getArtifact() != null) {
			eipVersion = this.project.getArtifact().getVersion();
		}

		TestClientGenerator tc;

		Collection<String> serviceIds = ServiceIdRegistry
				.splitServiceIds(this.serviceId);
		if (serviceIds.size() == 0) {
			serviceIds = ServiceIdRegistry.getAllServiceIds();
		}

		this.getLog().info("ServiceId size " + serviceIds.size());

		TreeSet<String> configImports = new TreeSet<String>();
		StringBuffer configClients = new StringBuffer();
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
			String contextPathName = "";

			for (ElementType element : xsds.getElementTypes()) {
				if (element.isRequest() && element.getServiceId().equals(sid)) {
					tc = new TestClientGenerator(xsds, element,
							this.useSpringInsightAnnotation, eipVersion,
							this.getLog());
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
						contextPathName = element.getPackageName();
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
				configClients
						.append(this.generateClientServiceConfig(className));
				configImports.add(new StringBuffer(packageName).append(".")
						.append(className).toString());
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
				sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(),
						eipVersion));
				sb.append(" */\n");
				sb.append("public class ");
				sb.append(className);
				sb.append(" extends WebServiceGatewaySupport {\n");

				sb.append(
						"\t/** The {@link org.springframework.oxm.jaxb.Jaxb2Marshaller}s context path. */\n");
				sb.append(
						"\tpublic static final String CONTEXT_PATH_NAME = \"");
				sb.append(contextPathName);
				sb.append("\";\n");
				sb.append("\t/** The service id. */\n");
				sb.append("\tpublic static final String SERVICE_ID = \"");
				sb.append(sid);
				sb.append("\";\n");

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
		this.generateAbstractConfig(xsds, this.basePackageName,
				configClients.toString(), configImports);
		this.generateClientWss4jSecurityInterceptor(xsds, this.basePackageName);
		this.getLog().debug("-execute");
	}

	private String generateClientServiceConfig(final String className) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("	/**\n");
		sb.append("	 * Get the {@link ");
		sb.append(className);
		sb.append("}.\n");
		sb.append("	 *\n");
		sb.append("	 * @param messageFactory\n");
		sb.append("	 *            the {@link SaajSoapMessageFactory}.\n");
		sb.append("	 * @param securityInterceptor\n");
		sb.append(
				"	 *            the {@link ClientWss4jSecurityInterceptor}.\n");
		sb.append("	 * @return the {@link ");
		sb.append(className);
		sb.append("}.\n");
		sb.append("	 */\n");
		sb.append("	@Bean\n");
		sb.append("	public ");
		sb.append(className);
		sb.append(" get");
		sb.append(className);
		sb.append("(\n");
		sb.append("			final SaajSoapMessageFactory messageFactory,\n");
		sb.append(
				"			final ClientWss4jSecurityInterceptor securityInterceptor) {\n");
		sb.append("		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();\n");
		sb.append("		marshaller.setContextPath(");
		sb.append(className);
		sb.append(".CONTEXT_PATH_NAME);\n");
		sb.append("\n");
		sb.append("		");
		sb.append(className);
		sb.append(" bean = new ");
		sb.append(className);
		sb.append("();\n");
		sb.append(
				"		bean.setInterceptors(new ClientInterceptor[] { securityInterceptor });\n");
		sb.append("		bean.setDefaultUri(\n");
		sb.append("				this.getClientEndPointUrl(");
		sb.append(className);
		sb.append(".SERVICE_ID));\n");
		sb.append("		bean.setMarshaller(marshaller);\n");
		sb.append("		bean.setUnmarshaller(marshaller);\n");
		sb.append("		bean.setMessageFactory(messageFactory);\n");
		sb.append("		return bean;\n");
		sb.append("	}\n");
		sb.append("\n");
		return sb.toString();
	}

	private void generateAbstractConfig(final XsdsUtil xsds,
			final String basePackageName, final String clientConfig,
			final TreeSet<String> configImports) {
		String packageName = new StringBuffer(basePackageName).append(".config")
				.toString();
		StringBuffer sb = new StringBuffer(1024);
		configImports.add("org.springframework.context.annotation.Bean");
		configImports.add("org.springframework.oxm.jaxb.Jaxb2Marshaller");
		configImports.add(
				"org.springframework.ws.client.support.interceptor.ClientInterceptor");
		configImports.add("org.springframework.ws.soap.SoapVersion");
		configImports
				.add("org.springframework.ws.soap.saaj.SaajSoapMessageFactory");
		configImports.add(
				"org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor");

		sb.append("package ");
		sb.append(packageName);
		sb.append(";\n");
		sb.append("\n");
		for (String string : configImports) {
			sb.append("import ");
			sb.append(string);
			sb.append(";\n");
		}
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * Abstract spring client config.\n");
		sb.append(" *\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public abstract class AbstractClientConfig {\n");
		sb.append("	/**\n");
		sb.append("	 * Get the client end point URL.\n");
		sb.append("	 *\n");
		sb.append("	 * @param serviceId\n");
		sb.append("	 *            the service id.\n");
		sb.append("	 * @return the client end point URL.\n");
		sb.append("	 */\n");
		sb.append(
				"	public abstract String getClientEndPointUrl(String serviceId);\n");
		sb.append("\n");
		sb.append("	/**\n");
		sb.append("	 * Get the client system user name.\n");
		sb.append("	 *\n");
		sb.append("	 * @return the client system user name.\n");
		sb.append("	 */\n");
		sb.append("	public abstract String getClientSystemUserName();\n");
		sb.append("\n");
		sb.append("	/**\n");
		sb.append("	 * Get the client system user password.\n");
		sb.append("	 *\n");
		sb.append("	 * @return the client system user password.\n");
		sb.append("	 */\n");
		sb.append("	public abstract String getClientSystemUserPassword();\n");
		sb.append("\n");

		sb.append(clientConfig);
		sb.append("	/**\n");
		sb.append("	 * Get the message factory supporting SOAP version 1.2.\n");
		sb.append("	 *\n");
		sb.append("	 * @return the {@link SaajSoapMessageFactory}.\n");
		sb.append("	 */\n");
		sb.append("	@Bean\n");
		sb.append("	public SaajSoapMessageFactory messageFactory() {\n");
		sb.append(
				"		SaajSoapMessageFactory bean = new SaajSoapMessageFactory();\n");
		sb.append("		bean.setSoapVersion(SoapVersion.SOAP_12);\n");
		sb.append("		return bean;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	/**\n");
		sb.append(
				"	 * The clients {@link Wss4jSecurityInterceptor} providing the web service\n");
		sb.append("	 * security.\n");
		sb.append("	 *\n");
		sb.append(
				"	 * @return the {@link Wss4jSecurityInterceptor} implementation.\n");
		sb.append("	 */\n");
		sb.append("	@Bean\n");
		sb.append(
				"	public ClientWss4jSecurityInterceptor securityInterceptor() {\n");
		sb.append(
				"		ClientWss4jSecurityInterceptor bean = new ClientWss4jSecurityInterceptor();\n");
		sb.append(
				"		bean.setSecurementUsername(this.getClientSystemUserName());\n");
		sb.append(
				"		bean.setSecurementPassword(this.getClientSystemUserPassword());\n");
		sb.append("		return bean;\n");
		sb.append("	}\n");
		sb.append("}\n");
		sb.append("\n");

		File f = Util.getFile(this.outputDirectory, packageName,
				new StringBuffer("AbstractClientConfig").append(".java")
						.toString());
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

	}

	private void generateClientWss4jSecurityInterceptor(final XsdsUtil xsds,
			final String basePackageName) {
		String packageName = new StringBuffer(basePackageName).append(".config")
				.toString();
		StringBuffer sb = new StringBuffer(1024);

		sb.append("package ");
		sb.append(packageName);
		sb.append(";\n");
		sb.append("\n");
		sb.append(
				"import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * The {@link Wss4jSecurityInterceptor}.\n");
		sb.append(" * \n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public class ClientWss4jSecurityInterceptor\n");
		sb.append("		extends Wss4jSecurityInterceptor {\n");
		sb.append("	/**\n");
		sb.append(
				"	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#afterPropertiesSet()\n");
		sb.append("	 */\n");
		sb.append("	@Override\n");
		sb.append("	public void afterPropertiesSet() {\n");
		sb.append("		this.setSecurementActions(\"UsernameToken\");\n");
		sb.append("		this.setSecurementPasswordType(\"PasswordDigest\");\n");
		sb.append(
				"		this.setSecurementUsernameTokenElements(\"Nonce Created\");\n");
		sb.append("	}\n");
		sb.append("}\n");

		File f = Util.getFile(this.outputDirectory, packageName,
				new StringBuffer("ClientWss4jSecurityInterceptor")
						.append(".java").toString());
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
