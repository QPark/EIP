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
package com.qpark.maven.plugin.objectfactories;

import java.io.File;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class SpringIntegrationConfigGenerator {
	/** The {@link Log}. */
	private final Log log;
	/** The {@link XsdsUtil}. */
	private final XsdsUtil config;
	/** The output directory. */
	private final File outputDirectory;

	private final MavenProject project;
	private final String basePackageName;

	public SpringIntegrationConfigGenerator(final XsdsUtil config, final String basePackageName,
			final File outputDirectory, final Log log, final MavenProject project) {
		this.config = config;
		this.basePackageName = basePackageName;
		this.outputDirectory = outputDirectory;
		this.log = log;
		this.project = project;
	}

	public void generate() {
		this.log.debug("+generate");
		TreeSet<String> serviceIds = new TreeSet<String>();
		for (ElementType element : this.config.getElementTypes()) {
			if (element.isRequest()) {
				serviceIds.add(element.getServiceId());
			}
		}
		for (String serviceId : serviceIds) {
			this.generateService(serviceId);
		}
		this.log.debug("-generate");
	}

	public void generateService(final String serviceId) {
		this.log.debug("+generateService");
		String capitalizeName = new StringBuffer(Util.capitalizePackageName(this.basePackageName))
				.append(ServiceIdRegistry.capitalize(serviceId)).toString();
		String servicePackageName = new StringBuffer(this.basePackageName).append(".").append(serviceId).toString();
		String fileName = new StringBuffer(64).append(servicePackageName).append("-integration-spring-config.xml")
				.toString();
		XsdContainer messageDefinitionXsdContainer = null;

		TreeSet<String> packageNames = new TreeSet<String>();
		for (ElementType element : this.config.getElementTypes()) {
			if (element.isRequest() && element.getServiceId().equals(serviceId)) {
				packageNames.add(element.getPackageName());
			}
		}
		for (ElementType element : this.config.getElementTypes()) {
			if (element.getServiceId().equals(serviceId)) {
				messageDefinitionXsdContainer = this.config.getXsdContainerMap(element.getTargetNamespace());
				break;
			}
		}

		StringBuffer xml = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		StringBuffer properties = new StringBuffer();

		xml.append(this.getXmlDefinition());
		xml.append("\t<!-- ");
		xml.append(Util.getGeneratedAt());
		xml.append(" -->\n");

		xml.append("\n\t<!-- Define the security interceptor \"eipCaller").append(capitalizeName)
				.append("Wss4jSecurityInterceptor\" somewhere outside:\n");
		xml.append("\t<import resource=\"classpath:").append(fileName).append("\" />\n");
		xml.append("\t<bean id=\"").append(capitalizeName).append(
				"WebServiceSecurmentPropertyProvider\" class=\"com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider\">\n");
		xml.append("\t\t<property name=\"securementUsername\" value=\"${").append(servicePackageName)
				.append(".userName}\"/>\n");
		xml.append("\t\t<property name=\"securementPassword\" value=\"${").append(servicePackageName)
				.append(".password}\"/>\n");
		xml.append("\t</bean>\n");
		xml.append("\t<bean id=\"eipCaller").append(capitalizeName).append(
				"Wss4jSecurityInterceptor\" class=\"com.qpark.eip.core.spring.security.EipWss4jSecurityInterceptor\">\n");
		xml.append("\t\t<property name=\"securementPropertyProvider\" ref=\"").append(capitalizeName)
				.append("WebServiceSecurmentPropertyProvider\" />\n");
		xml.append("\t</bean>\n");

		properties.append("\n#Properties used in ").append(fileName).append(":\n");
		properties.append(servicePackageName).append(".userName=XXXXXX\n");
		properties.append(servicePackageName).append(".password=password\n");

		if (messageDefinitionXsdContainer != null && messageDefinitionXsdContainer.getRelativeName() != null) {
			sb.append("\n\t<!-- Payload validation interceptor -->\n");
			sb.append("\t<bean id=\"eipCaller").append(capitalizeName).append(
					"PayloadValidatingInterceptor\" class=\"org.springframework.ws.client.support.interceptor.PayloadValidatingInterceptor\">\n");
			sb.append("\t\t<property name=\"schemas\">\n");
			sb.append("\t\t\t<list>\n");
			sb.append("\t\t\t\t<value>/WEB-INF/classes/");
			sb.append(messageDefinitionXsdContainer.getRelativeName());
			sb.append("</value>\n");
			sb.append("\t\t\t</list>\n");
			sb.append("\t\t</property>\n");
			sb.append("\t\t<property name=\"validateRequest\" value=\"${");
			sb.append(servicePackageName);
			sb.append(".validate.request.message:false}\" />\n");
			sb.append("\t\t<property name=\"validateResponse\" value=\"${");
			sb.append(servicePackageName);
			sb.append(".validate.response.message:false}\" />\n");
			sb.append("\t</bean>\n");
			properties.append(servicePackageName).append(".validate.request.message=false\n");
			properties.append(servicePackageName).append(".validate.response.message=false\n");

		}

		sb.append("\t<!-- Interceptor list of services -->\n");
		sb.append("\t<util:list id=\"eipCaller").append(capitalizeName).append("WsInterceptors\">\n");
		sb.append("\t\t<ref bean=\"eipCaller").append(capitalizeName).append("Wss4jSecurityInterceptor\"/>\n");
		if (messageDefinitionXsdContainer != null && messageDefinitionXsdContainer.getRelativeName() != null) {
			sb.append("\t\t<ref bean=\"eipCaller").append(capitalizeName).append("PayloadValidatingInterceptor\"/>\n");
		}
		sb.append("\t</util:list>\n");

		sb.append("\t<!-- Marshaller of services -->\n");
		sb.append("\t<oxm:jaxb2-marshaller id=\"eipCaller").append(capitalizeName).append("Marshaller\" \n");
		sb.append("\t\tcontextPath=\"");
		// sb.append(Util.getContextPath(packageNames));
		sb.append(ServiceIdRegistry.getMarshallerContextPath(serviceId));
		sb.append("\"\n\t/>\n");

		sb.append("\n");
		StringBuffer channels = new StringBuffer();
		StringBuffer gateways = new StringBuffer();
		StringBuffer router = new StringBuffer();
		channels.append("\t<!-- Channel definitions of service ").append(serviceId).append(" -->\n");
		channels.append("\t<int:channel id=\"internalEipCaller").append(capitalizeName).append("RequestChannel\" />\n");
		router.append("\t<!-- Response type router of service ").append(serviceId).append(" -->\n");
		router.append("\t<int:router id=\"internalEipCaller").append(capitalizeName)
				.append("ResponseTypeRouter\" input-channel=\"internalEipCaller").append(capitalizeName)
				.append("WsOutgoingChannel\" ref=\"internalEipCaller").append(capitalizeName)
				.append("JAXBElementAwarePayloadTypeRouter\" >\n");
		for (ElementType element : this.config.getElementTypes()) {
			if (element.getServiceId().equals(serviceId)) {
				ElementType elementResponse = XsdsUtil.findResponse(element, this.config.getElementTypes(),
						this.config);
				if (element.isRequest() && elementResponse != null) {
					ComplexType ctResponse = new ComplexType(elementResponse.getElement().getType(), this.config);
					if (ctResponse != null && !ctResponse.isSimpleType() && !ctResponse.isPrimitiveType()) {
						gateways.append("\t<int:gateway id=\"eipCaller").append(capitalizeName)
								.append(Util.capitalize(element.getMethodName())).append("Gateway\"\n");
						gateways.append("\t\tservice-interface=\"").append(element.getPackageNameGateway()).append(".")
								.append(element.getClassNameGateway()).append("\"\n");
						gateways.append("\t\tdefault-request-channel=\"internalEipCaller").append(capitalizeName)
								.append("RequestChannel\"\n");
						gateways.append("\t\tdefault-reply-channel=\"internalEipCaller").append(capitalizeName)
								.append(Util.capitalize(element.getMethodName())).append("ResponseChannel\"\n\t/>\n");
						channels.append("\t<int:channel id=\"internalEipCaller").append(capitalizeName)
								.append(Util.capitalize(element.getMethodName())).append("ResponseChannel\" />\n");
						router.append("\t\t<int:mapping value=\"").append(ctResponse.getClassNameFullQualified())
								.append("\" channel=\"internalEipCaller").append(capitalizeName)
								.append(Util.capitalize(element.getMethodName())).append("ResponseChannel\" />\n");
					}
				}
			}
		}
		channels.append("\t<int:channel id=\"internalEipCaller").append(capitalizeName)
				.append("WsOutgoingChannel\" />\n");
		router.append("\t</int:router>\n");

		sb.append("<!-- ").append(ServiceIdRegistry.capitalize(serviceId)).append(" definition start -->\n");
		sb.append("\t<!-- Gateway of the service ").append(serviceId).append(" (to be used in your code) -->\n");
		sb.append(gateways);
		sb.append(channels);
		sb.append(router);
		sb.append("\t<bean id=\"internalEipCaller").append(capitalizeName).append(
				"JAXBElementAwarePayloadTypeRouter\" class=\"com.qpark.eip.core.spring.JAXBElementAwarePayloadTypeRouter\"/>\n");
		sb.append("\t<!-- Outbound gateway of the service ").append(serviceId).append(" -->\n");
		sb.append("\t<int-ws:outbound-gateway id=\"eipCaller").append(capitalizeName).append("OutboundGateway\"\n");
		sb.append("\t\trequest-channel=\"internalEipCaller").append(capitalizeName).append("RequestChannel\"\n");
		sb.append("\t\treply-channel=\"internalEipCaller").append(capitalizeName).append("WsOutgoingChannel\"\n");
		sb.append("\t\treply-timeout=\"${service.endpoint.timeout.ms.").append(servicePackageName).append(":-1}\"\n");

		sb.append("\t\turi=\"${service.endpoint.url.").append(servicePackageName).append("}\"\n");
		properties.append("service.endpoint.url.").append(servicePackageName)
				.append("=http://localhost:8080/webapp-name-and-version/services/").append(serviceId).append("\n");
		properties.append("service.endpoint.timeout.ms.").append(servicePackageName).append("=-1\n");

		sb.append("\t\tinterceptors=\"eipCaller").append(capitalizeName).append("WsInterceptors\"\n");
		sb.append("\t\tmessage-factory=\"soap12MessageFactory\"\n");
		sb.append("\t\tmarshaller=\"eipCaller").append(capitalizeName).append("Marshaller\" unmarshaller=\"eipCaller")
				.append(capitalizeName).append("Marshaller\"\n");
		sb.append("\t/>\n");
		sb.append("<!-- ").append(ServiceIdRegistry.capitalize(serviceId)).append(" definition end -->\n\n");

		sb.append("\n</beans>\n");

		properties.append("\t-->\n\n");

		xml.append(properties);
		xml.append(sb);
		File f = Util.getFile(this.outputDirectory, "", fileName);
		this.log.info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, xml.toString());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generateService");
	}

	private String getXmlDefinition() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:int=\"http://www.springframework.org/schema/integration\"\n");
		sb.append("\txmlns:int-ws=\"http://www.springframework.org/schema/integration/ws\"\n");
		sb.append("\txmlns:oxm=\"http://www.springframework.org/schema/oxm\" \n");
		sb.append("\txmlns:util=\"http://www.springframework.org/schema/util\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		String springVersion = this.project.getProperties().getProperty("org.springframework.version.xsd.version");
		sb.append(
				"\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans");
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
		sb.append("\t\"\n>\n");
		return sb.toString();
	}

}
