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
package com.qpark.maven.plugin.selfcallingconfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates the <code>persistence-spring-config.xml</code> containing the bean
 * declaration of the
 * <ul>
 * <li>JNDI data source</li>
 * <li>JPA EntityManagerFactory to access the persistence unit</li>
 * <li>JPA SessionFactory</li>
 * <li>JPA TransactionManager</li>
 * <li>Hibernate statistics MBean</li>
 * </ul>
 *
 * @author bhausen
 */
@Deprecated
@Mojo(name = "generate-selfcalling-integration-config", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SpringSelfcallingIntegrationConfigMojo extends AbstractMojo {
    /** The base directory where to start the scan of xsd files. */
    @Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
    protected File baseDirectory;
    /** The base directory where to start the scan of xsd files. */
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
    protected File outputDirectory;
    @Component
    private MavenProject project;
    /**
     * The package name of the messages should end with this. Default is
     * <code>msg</code>.
     */
    @Parameter(property = "messagePackageNameSuffix", defaultValue = "msg")
    protected String messagePackageNameSuffix;
    /**
     * The package name of the delta should contain this. Default is
     * <code>delta</code>.
     */
    @Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
    private String deltaPackageNameSuffix;
    /** The base package name where to place the object factories. */
    @Parameter(property = "basePackageName", defaultValue = "")
    protected String basePackageName;
    /** The name of the service id of common services. */
    @Parameter(property = "serviceIdCommonServices", defaultValue = "common")
    private String serviceIdCommonServices;
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
    private TreeSet<ElementType> elementTypes;

    private static final String MARSHALLER_NAME = "internalSelfCallingWsMarshaller";

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
	this.getLog().debug("+execute");
	this.getLog().debug("get xsds");
	XsdsUtil config = new XsdsUtil(this.baseDirectory, this.basePackageName, this.messagePackageNameSuffix,
		this.deltaPackageNameSuffix, this.serviceRequestSuffix, this.serviceResponseSuffix);
	this.elementTypes = config.getElementTypes();

	StringBuffer sb = new StringBuffer(1024);
	sb.append(this.getXmlDefinition());
	sb.append("\t<!-- ");
	sb.append(Util.getGeneratedAt());
	sb.append(" -->\n");
	sb.append("\n");
	sb.append(this.getSelfCallingConfig(config));
	sb.append("</beans>\n");

	File f = Util.getFile(this.outputDirectory, "SelfCallingIntegrationConfig.xml");
	this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
	try {
	    Util.writeToFile(f, sb.toString());
	} catch (Exception e) {
	    this.getLog().error(e.getMessage());
	    e.printStackTrace();
	}
	this.getLog().debug("-execute");
    }

    private String getSelfCallingConfig(final XsdsUtil config) {
	StringBuffer gateways = new StringBuffer(1024);
	TreeSet<String> channels = new TreeSet<String>();
	TreeMap<String, List<String>> routerMappings = new TreeMap<String, List<String>>();
	String capitalizedServiceId;
	StringBuffer sb = new StringBuffer(1024);
	for (ElementType element : this.elementTypes) {
	    if (element.isRequest()) {
		ElementType elementResponse = XsdsUtil.findResponse(element, config.getElementTypes(), config);
		if (elementResponse != null) {
		    ComplexType ctResponse = new ComplexType(elementResponse.getElement().getType(), config);
		    if (ctResponse != null && !ctResponse.isSimpleType() && !ctResponse.isPrimitiveType()) {
			capitalizedServiceId = ServiceIdRegistry.capitalize(element.getServiceId());
			gateways.append("\t<int:gateway id=\"internalSelfCallingWs");
			gateways.append(capitalizedServiceId);
			gateways.append(element.getOperationName());
			gateways.append("Gateway\"\n");
			gateways.append("\t\tservice-interface=\"");
			gateways.append(element.getClassNameFullQualifiedGateway());
			gateways.append("\"\n");
			gateways.append("\t\tdefault-request-channel=\"internalSelfCallingWs");
			gateways.append(capitalizedServiceId);
			gateways.append("RequestChannel\"\n");
			gateways.append("\t\tdefault-reply-channel=\"internalSelfCallingWs");
			gateways.append(capitalizedServiceId);
			gateways.append(element.getOperationName());
			gateways.append("ResponseChannel\"\n");
			gateways.append("\t/>\n");

			channels.add(new StringBuffer(256).append("<int:channel id=\"internalSelfCallingWs")
				.append(capitalizedServiceId).append("RequestChannel\" />").toString());
			channels.add(new StringBuffer(256).append("<int:channel id=\"internalSelfCallingWs")
				.append(capitalizedServiceId).append(element.getOperationName())
				.append("ResponseChannel\" />").toString());
			List<String> mappings = routerMappings.get(element.getServiceId());
			if (mappings == null) {
			    mappings = new ArrayList<String>();
			    routerMappings.put(element.getServiceId(), mappings);
			}
			mappings.add(new StringBuffer(256).append("").append("<int:mapping value=\"")
				.append(ctResponse.getClassNameFullQualified())
				.append("\" channel=\"internalSelfCallingWs").append(capitalizedServiceId)
				.append(element.getOperationName()).append("ResponseChannel\" />").toString());
		    }
		}
	    }
	}
	sb.append(gateways);
	for (String serviceId : routerMappings.keySet()) {
	    capitalizedServiceId = ServiceIdRegistry.capitalize(serviceId);
	    sb.append("\t<int:channel id=\"internalSelfCallingWs");
	    sb.append(capitalizedServiceId);
	    sb.append("WebServiceResponseChannel\" />\n");
	    sb.append("\t<bean id=\"internalSelfCallingWs");
	    sb.append(capitalizedServiceId);
	    sb.append(
		    "JAXBElementAwarePayloadTypeRouter\" class=\"com.qpark.eip.core.spring.JAXBElementAwarePayloadTypeRouter\"/>\n");
	    sb.append("\t<int-ws:outbound-gateway\n");
	    sb.append("\t\trequest-channel=\"internalSelfCallingWs");
	    sb.append(capitalizedServiceId);
	    sb.append("RequestChannel\"\n");
	    sb.append("\t\treply-channel=\"internalSelfCallingWs");
	    sb.append(capitalizedServiceId);
	    sb.append("WebServiceResponseChannel\"\n");
	    sb.append("\t\turi=\"${eip.internal.self.calling.");
	    sb.append(serviceId);
	    sb.append(".endpoint:__ENDPOINT_NOT_SET__}\"\n");
	    sb.append("\t\tinterceptors=\"internalSelfCallingWsInterceptors\"\n");
	    sb.append("\t\tmessage-factory=\"internalSelfCallingWsSoap12MessageFactory\"\n");
	    sb.append("\t\tmarshaller=\"");
	    sb.append(SpringSelfcallingIntegrationConfigMojo.MARSHALLER_NAME);
	    sb.append("\"\n");
	    sb.append("\t\tunmarshaller=\"");
	    sb.append(SpringSelfcallingIntegrationConfigMojo.MARSHALLER_NAME);
	    sb.append("\"\n");
	    sb.append("\t/>\n");

	    sb.append("\t<int:router input-channel=\"internalSelfCallingWs");
	    sb.append(capitalizedServiceId);
	    sb.append("WebServiceResponseChannel\" ref=\"internalSelfCallingWs");
	    sb.append(capitalizedServiceId);
	    sb.append("JAXBElementAwarePayloadTypeRouter\">\n");
	    List<String> mappings = routerMappings.get(serviceId);
	    for (String mapping : mappings) {
		sb.append("\t\t").append(mapping).append("\n");
	    }
	    sb.append("\t</int:router>\n");
	}
	sb.append(
		"\t<bean id=\"internalSelfCallingWsPayloadLogger\" class=\"com.qpark.eip.core.spring.PayloadLogger\" >\n");
	sb.append("\t\t<property name=\"loggerName\" value=\"");
	sb.append(config.getBasePackageName());
	sb.append(".internal.self.calling.webservice\"/>\n");
	sb.append("\t</bean>\n");

	sb.append(
		"\t<bean id=\"internalSelfCallingWsWss4jSecurityInterceptor\" class=\"org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor\">\n");
	sb.append("\t\t<property name=\"securementActions\" value=\"UsernameToken\" />\n");
	sb.append("\t\t<property name=\"securementUsername\" value=\"${eip.internal.self.calling.username:eip}\" />\n");
	sb.append(
		"\t\t<property name=\"securementPassword\" value=\"${eip.internal.self.calling.password:password}\" />\n");
	sb.append("\t\t<property name=\"securementPasswordType\" value=\"PasswordDigest\" />\n");
	sb.append("\t\t<property name=\"securementUsernameTokenElements\" value=\"Nonce Created\" />\n");
	sb.append("\t</bean>\n");

	sb.append("\t<util:list id=\"internalSelfCallingWsInterceptors\">\n");
	sb.append("\t\t<ref bean=\"internalSelfCallingWsWss4jSecurityInterceptor\"/>\n");
	sb.append("\t\t<ref bean=\"internalSelfCallingWsPayloadLogger\" />\n");
	sb.append("\t</util:list>\n");

	sb.append(
		"\t<bean id=\"internalSelfCallingWsSoap12MessageFactory\" class=\"org.springframework.ws.soap.saaj.SaajSoapMessageFactory\">\n");
	sb.append("\t\t<property name=\"soapVersion\">\n");
	sb.append("\t\t\t<util:constant static-field=\"org.springframework.ws.soap.SoapVersion.SOAP_12\" />\n");
	sb.append("\t\t</property>\n");
	sb.append("\t</bean>\n");

	sb.append("\t<oxm:jaxb2-marshaller id=\"");
	sb.append(SpringSelfcallingIntegrationConfigMojo.MARSHALLER_NAME);
	sb.append("\" \n");
	sb.append("\t\tcontextPath=\"");
	// sb.append(Util.getContextPath(config.getPackageNames()));
	// sb.append(ServiceIdRegistry.getMarshallerContextPath(serviceId));//
	// (config.getPackageNames()));
	sb.append("\"\n\t/>\n");

	for (String channel : channels) {
	    sb.append("\t").append(channel).append("\n");
	}
	return sb.toString();
    }

    private String getXmlDefinition() {
	StringBuffer sb = new StringBuffer(1024);
	sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");

	sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
	sb.append("\txmlns:int=\"http://www.springframework.org/schema/integration\"\n");
	sb.append("\txmlns:int-ws=\"http://www.springframework.org/schema/integration/ws\"\n");
	sb.append("\txmlns:context=\"http://www.springframework.org/schema/context\"\n");
	sb.append("\txmlns:oxm=\"http://www.springframework.org/schema/oxm\"\n");
	sb.append("\txmlns:util=\"http://www.springframework.org/schema/util\"\n");
	sb.append("\txmlns:aop=\"http://www.springframework.org/schema/aop\"\n");

	sb.append("\txsi:schemaLocation=\"\n");
	String springVersion = this.project.getProperties().getProperty("org.springframework.version.xsd.version");
	sb.append(
		"\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");
	sb.append(
		"\t\thttp://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");
	sb.append("\t\thttp://www.springframework.org/schema/oxm http://www.springframework.org/schema/oxm/spring-oxm");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");
	sb.append(
		"\t\thttp://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");
	sb.append("\t\thttp://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");

	springVersion = this.project.getProperties().getProperty("org.springframework.integration.version.xsd.version");
	sb.append(
		"\t\thttp://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");
	sb.append(
		"\t\thttp://www.springframework.org/schema/integration/ws http://www.springframework.org/schema/integration/ws/spring-integration-ws");
	if (springVersion != null) {
	    sb.append("-").append(springVersion);
	}
	sb.append(".xsd\n");

	sb.append("\t\"\n");
	sb.append(">\n");
	return sb.toString();
    }
}
