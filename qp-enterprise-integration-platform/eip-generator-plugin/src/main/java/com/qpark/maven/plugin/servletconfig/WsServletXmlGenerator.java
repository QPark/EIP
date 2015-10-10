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
package com.qpark.maven.plugin.servletconfig;

import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_FRAMEWORK_XSD_VERSION;
import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_WS_XSD_VERSION;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdEntry;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * @author bhausen
 */
public class WsServletXmlGenerator {
	// private static final String PAYLOAD_LOGGER =
	// "com.qpark.eip.core.spring.PayloadLogger";
	private static final String PAYLOAD_LOGGER = "com.qpark.eip.core.spring.PayloadLogger";
	private final Log log;
	private final XsdsUtil config;
	private final boolean serviceCreationWithCommon;
	private final String serviceIdCommonServices;
	private final File outputDirectory;
	private final String serviceId;
	private final TreeSet<ElementType> elementTypes;
	private final String basePackageName;
	private final String additionalWebservicePayloadInterceptors;
	private final String webservicePayloadLoggerImplementation;
	private final boolean disableWebservicePayloadValidation;
	private final MavenProject project;

	public static void main(final String[] args) {
		String s = "<bean id=\"\neins\" lkajsdfid=\"zwei\"kljadf";
		String[] ss = s.split("id=\\\"");
		for (String string : ss) {
			if (string.indexOf('"') > 0) {
				System.out.println(string.substring(0, string.indexOf('"'))
						.replaceAll("\\\n", "").replaceAll("\\\r", ""));
			}
		}
	}

	private List<String> getAdditionalWebservicePayloadInterceptors() {
		List<String> beanIds = new ArrayList<String>();
		if (this.additionalWebservicePayloadInterceptors != null) {
			String[] ss = this.additionalWebservicePayloadInterceptors
					.split("id=\\\"");
			for (String s : ss) {
				if (s.indexOf('"') > 0) {
					beanIds.add(s.substring(0, s.indexOf('"'))
							.replaceAll("\\\n", "").replaceAll("\\\r", ""));
				}
			}
		}
		return beanIds;
	}

	/**
	 * @param config
	 * @param elementTypes
	 */
	public WsServletXmlGenerator(final XsdsUtil config,
			final String basePackageName, final String serviceId,
			final String serviceIdCommonServices,
			final boolean serviceCreationWithCommon,
			final boolean disableWebservicePayloadValidation,
			final String webservicePayloadLoggerImplementation,
			final String additionalWebservicePayloadInterceptors,
			final File outputDirectory, final MavenProject project,
			final Log log) {
		this.config = config;
		this.basePackageName = basePackageName;
		this.serviceId = serviceId == null ? "" : serviceId;
		this.serviceIdCommonServices = serviceIdCommonServices;
		this.serviceCreationWithCommon = serviceCreationWithCommon;
		this.outputDirectory = outputDirectory;
		this.project = project;
		this.log = log;
		this.elementTypes = config.getElementTypes();
		this.disableWebservicePayloadValidation = disableWebservicePayloadValidation;
		if (webservicePayloadLoggerImplementation == null) {
			this.webservicePayloadLoggerImplementation = PAYLOAD_LOGGER;
		} else {
			this.webservicePayloadLoggerImplementation = webservicePayloadLoggerImplementation
					.trim();
		}
		if (additionalWebservicePayloadInterceptors == null) {
			this.additionalWebservicePayloadInterceptors = "";
		} else {
			this.additionalWebservicePayloadInterceptors = additionalWebservicePayloadInterceptors
					.trim();
		}
	}

	public void generate() {
		this.log.debug("+generate");
		StringBuffer sb = new StringBuffer(1024);

		sb.append(this.getXmlDefinition());
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\n");
		sb.append(this.getProperties());
		sb.append("\n");
		sb.append(this.getSoapDefaults());
		sb.append("\n");
		sb.append(this.getWsSecurityInterceptor());
		sb.append("\n");
		sb.append(this.getPayloadLoggingInterceptor());
		sb.append("\n");
		if (!this.disableWebservicePayloadValidation) {
			sb.append(this.getPayloadValidatingInterceptor());
			sb.append("\n");
		}
		sb.append(this.getEndPointMappings());
		sb.append("</beans>\n");

		File f = Util.getFile(this.outputDirectory, "", "ws-servlet.xml");
		this.log.info(new StringBuffer().append("Write ").append(
				f.getAbsolutePath()));

		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generate");
	}

	private String getEndPointMappings() {
		StringBuffer sb = new StringBuffer(1024);
		if (this.additionalWebservicePayloadInterceptors != null
				&& this.additionalWebservicePayloadInterceptors.length() > 0) {
			sb.append("\t<!-- Additional web service payload interceptors -->\n");
			if (!this.additionalWebservicePayloadInterceptors.startsWith("<")) {
				sb.append("<");
			}
			sb.append(this.additionalWebservicePayloadInterceptors);
			sb.append("\n");
		}
		sb.append("\t<!-- End point mappings -->\n");
		sb.append("\t<bean id=\"endPointMappings\" class=\"org.springframework.ws.server.endpoint.mapping.PayloadRootQNameEndpointMapping\">\n");
		sb.append("\t\t<description>The endpoint mapping maps the web services to the Spring Integration gateways. The interceptors define the logging and the authentication. The Spring Integration configuration defines the actual authorization by secured channels. </description>\n");
		sb.append("\t\t<property name=\"interceptors\">\n");
		sb.append("\t\t\t<list>\n");
		sb.append("\t\t\t\t<ref local=\"wsSecurityInterceptor\" />\n");
		sb.append("\t\t\t\t<ref local=\"payloadLoggingInterceptor\" />\n");
		if (!this.disableWebservicePayloadValidation) {
			sb.append("\t\t\t\t<ref local=\"payloadValidatingInterceptor\" />\n");
		}
		List<String> beanIds = this
				.getAdditionalWebservicePayloadInterceptors();
		for (String beanId : beanIds) {
			sb.append("\t\t\t\t<ref local=\"");
			sb.append(beanId);
			sb.append("\" />\n");

		}
		sb.append("\t\t\t</list>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t\t<property name=\"mappings\">\n");
		sb.append("\t\t\t<props>\n");
		String targetNamespace = null;
		for (ElementType element : this.elementTypes) {
			if (element.isRequest()
					&& ServiceIdRegistry.isValidServiceId(
							element.getServiceId(), this.serviceId)) {
				ElementType elementResponse = XsdsUtil.findResponse(element,
						this.config.getElementTypes(), this.config);
				if (elementResponse != null) {
					ComplexType ctResponse = new ComplexType(elementResponse
							.getElement().getType(), this.config);
					if (ctResponse != null && !ctResponse.isSimpleType()
							&& !ctResponse.isPrimitiveType()) {
						if (targetNamespace == null
								|| !targetNamespace.equals(element
										.getTargetNamespace())) {
							targetNamespace = element.getTargetNamespace();
							sb.append("\t\t\t\t<!-- ");
							sb.append(targetNamespace);
							sb.append(" -->\n");
						}
						sb.append("\t\t\t\t<prop ");
						sb.append("key=\"{");
						sb.append(element.getTargetNamespace());
						sb.append("}");
						sb.append(element.getClassNameObject());
						sb.append("\"");
						sb.append(">");
						sb.append(element.getBeanIdWsInboundGateway());
						sb.append("</prop>\n");
					}
				}
			}
		}
		sb.append("\t\t\t</props>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		return sb.toString();
	}

	private String getPayloadLoggingInterceptor() {
		StringBuffer sb = new StringBuffer(1024);

		// TreeSet<String> packageNames = new TreeSet<String>();
		// for (ElementType element : this.elementTypes) {
		// if (element.isRequest()
		// && ServiceIdRegistry.isValidServiceId(
		// element.getServiceId(), this.serviceId)) {
		// packageNames.add(element.getPackageName());
		// }
		// }

		sb.append("\t<!-- Payload logging interceptor -->\n");
		sb.append("\t<bean id=\"payloadLoggingInterceptor\" class=\"");
		sb.append(this.webservicePayloadLoggerImplementation);
		sb.append("\">\n");
		sb.append("\t\t<property name=\"loggerName\" value=\"");
		sb.append(this.basePackageName);
		sb.append(".messages.incoming\"/>\n");
		sb.append("\t\t<property name=\"contextPath\" value=\"");
		// sb.append(Util.getContextPath(packageNames));
		sb.append(ServiceIdRegistry.getMarshallerContextPath(this.serviceId));
		sb.append("\"/>\n");
		sb.append("\t</bean>\n");
		return sb.toString();
	}

	private String getPayloadValidatingInterceptor() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t<!-- Payload validation interceptor -->\n");
		sb.append("\t<bean id=\"payloadValidatingInterceptor\" class=\"org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor\">\n");
		sb.append("\t\t<property name=\"schemas\">\n");
		sb.append("\t\t\t<list>\n");
		if (this.serviceId.length() > 0) {
			Collection<String> serviceIds = ServiceIdRegistry
					.getServiceIds(this.serviceId);
			if (serviceIds.size() == 0) {
				serviceIds = ServiceIdRegistry.getAllServiceIds();
			}
			for (String sid : serviceIds) {
				ServiceIdEntry entry = ServiceIdRegistry.getServiceIdEntry(sid);
				XsdContainer xc = this.config.getXsdContainerMap(entry
						.getTargetNamespace());
				sb.append("\t\t\t\t<value>/WEB-INF/classes");
				sb.append(Util.getRelativePathTranslated(
						this.config.getBaseDirectory(), xc.getFile()));
				sb.append("</value>\n");
			}
		} else {
			for (File f : this.config.getXsdFiles()) {
				sb.append("\t\t\t\t<value>/WEB-INF/classes");
				sb.append(Util.getRelativePathTranslated(
						this.config.getBaseDirectory(), f));
				sb.append("</value>\n");
			}
		}
		sb.append("\t\t\t</list>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t\t<property name=\"validateRequest\" value=\"${eip.web.service.message.validation.incoming:false}\" />\n");
		sb.append("\t\t<property name=\"validateResponse\" value=\"${eip.web.service.message.validation.outgoing:false}\" />\n");
		sb.append("\t\t<property name=\"addValidationErrorDetail\" value=\"true\" />\n");
		sb.append("\t</bean>\n");
		return sb.toString();
	}

	private String getProperties() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t<!-- The properties -->\n");
		sb.append("\t<import resource=\"classpath:/");
		sb.append(this.basePackageName);
		sb.append(".properties-config.xml\" />\n");
		return sb.toString();
	}

	private String getSoapDefaults() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t<!-- Web service message factory to support SOAP 1.2  -->\n");
		sb.append("\t<bean id=\"messageFactory\" class=\"org.springframework.ws.soap.saaj.SaajSoapMessageFactory\">\n");
		sb.append("\t\t<property name=\"soapVersion\">\n");
		sb.append("\t\t\t<util:constant static-field=\"org.springframework.ws.soap.SoapVersion.SOAP_12\" />\n");
		sb.append("\t\t</property>\n");
		// sb.append("\t\t<property name=\"messageFactory\">\n");
		// sb.append("\t\t\t<bean class=\"org.apache.axis2.saaj.MessageFactoryImpl\">\n");
		// sb.append("\t\t\t\t<property name=\"SOAPVersion\">\n");
		// sb.append("\t\t\t\t\t<util:constant static-field=\"javax.xml.soap.SOAPConstants.SOAP_1_2_PROTOCOL\" />\n");
		// sb.append("\t\t\t\t</property>\n");
		// sb.append("\t\t\t</bean>\n");
		// sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<!-- SOAP fault defaults. -->\n");
		sb.append("\t<bean class=\"org.springframework.ws.soap.server.endpoint.SoapFaultAnnotationExceptionResolver\" />\n");
		sb.append("\t<bean id=\"eipSoapFaultDefinitionExceptionResolver\" class=\"com.qpark.eip.core.spring.EipSoapFaultDefinitionExceptionResolver\"/>\n");

		// sb.append("\t<bean class=\"org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver\">\n");
		// sb.append("\t\t<description> This exception resolver maps other exceptions to SOAP Faults. Both UnmarshallingException and ValidationFailureException are mapped to a SOAP Fault with a &quot;Client&quot; fault code. All other exceptions are mapped to a &quot;Server&quot; error code, the default. The message shall contain the causing exception.</description>\n");
		// sb.append("\t\t<property name=\"defaultFault\" value=\"SERVER, BUS error\" />\n");
		// sb.append("\t\t<property name=\"exceptionMappings\">\n");
		// sb.append("\t\t\t<props>\n");
		// sb.append("\t\t\t\t<prop key=\"org.springframework.oxm.UnmarshallingFailureException\"> CLIENT, BUS: Invalid request received (org.springframework.oxm.UnmarshallingFailureException)</prop>\n");
		// sb.append("\t\t\t\t<prop key=\"org.springframework.oxm.ValidationFailureException\"> CLIENT, BUS: Invalid request received (org.springframework.oxm.ValidationFailureException)</prop>\n");
		// sb.append("\t\t\t\t<prop key=\"org.springframework.security.access.AccessDeniedException\"> SERVER, BUS: Service Access not Granted (org.springframework.security.access.AccessDeniedException)</prop>\n");
		// sb.append("\t\t\t</props>\n");
		// sb.append("\t\t</property>\n");
		// sb.append("\t</bean>\n");
		return sb.toString();
	}

	private String getWsSecurityInterceptor() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t<!-- Web service security interceptor -->\n");
		sb.append("\t<bean id=\"wsSecurityInterceptor\" class=\"org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor\">\n");
		sb.append("\t\t<description> Interceptor of SUN's XML and Web Services Security package (XWSS). Messages are not encrypted, but the SOAP header contains a system user and a system password. In the security policy defines password digesting and the a nonce. The Interceptor refers to the authentication manager.</description>\n");
		sb.append("\t\t<property name=\"policyConfiguration\" value=\"classpath:securityPolicy.xml\" />\n");
		sb.append("\t\t<property name=\"exceptionResolver\" ref=\"eipSoapFaultDefinitionExceptionResolver\" />\n");
		sb.append("\t\t<property name=\"callbackHandlers\">\n");
		sb.append("\t\t\t<list>\n");
		sb.append("\t\t\t\t<bean class=\"org.springframework.ws.soap.security.xwss.callback.SpringDigestPasswordValidationCallbackHandler\">\n");
		sb.append("\t\t\t\t\t<property name=\"userDetailsService\" ref=\"eipUserDetailsService\" />\n");
		sb.append("\t\t\t\t</bean>\n");
		sb.append("\t\t\t</list>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		return sb.toString();
	}

	private String getXmlDefinition() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:util=\"http://www.springframework.org/schema/util\"\n");
		sb.append("\txmlns:aop=\"http://www.springframework.org/schema/aop\"\n");
		sb.append("\txmlns:sws=\"http://www.springframework.org/schema/web-services\"\n");
		sb.append("\txmlns:oxm=\"http://www.springframework.org/schema/oxm\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd \n");
		sb.append("\t\thttp://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/web-services http://www.springframework.org/schema/web-services/web-services-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.ws.version.xsd.version",
				DEFAULT_SPRING_WS_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/oxm http://www.springframework.org/schema/oxm/spring-oxm-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\"\n>\n");
		return sb.toString();
	}
}
