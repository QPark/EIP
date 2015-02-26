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
package com.qpark.maven.plugin.springconfig;

import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_FRAMEWORK_XSD_VERSION;
import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_INTEGRATION_XSD_VERSION;
import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_WS_XSD_VERSION;

import java.io.File;
import java.util.List;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.plugin.GetServiceIds;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * @author bhausen
 */
public class WebServiceDispatcherXmlGenerator {
	private final Log log;
	private final XsdsUtil config;
	private final boolean serviceCreationWithCommon;
	private final String serviceIdCommonServices;
	private final File outputDirectory;
	private final String warName;

	private final TreeSet<ElementType> elementTypes;
	private final String marshallerName;
	private final MavenProject project;
	private String serviceId = "esb";

	/**
	 * @param config
	 * @param elementTypes
	 */
	public WebServiceDispatcherXmlGenerator(final XsdsUtil config,
			final String serviceId, final String serviceIdCommonServices,
			final boolean serviceCreationWithCommon, final String warName,
			final File outputDirectory, final MavenProject project,
			final Log log) {
		this.config = config;
		this.serviceId = serviceId == null ? "" : serviceId;
		this.serviceIdCommonServices = serviceIdCommonServices;
		this.serviceCreationWithCommon = serviceCreationWithCommon;
		this.warName = warName;
		this.outputDirectory = outputDirectory;
		this.project = project;
		this.log = log;
		this.elementTypes = config.getElementTypes();
		this.marshallerName = new StringBuffer(32).append(serviceId)
				.append("Marshaller").toString();
	}

	private void createWebServiceDynamicWsdlConfig(final String wsdlServiceId) {
		File f = Util.getFile(
				this.outputDirectory,
				"dispatcher",
				new StringBuffer(64)
						.append(GetServiceIds
								.getServiceIdBasename(wsdlServiceId))
						.append("DynamicWsdlConfig.xml").toString());
		this.log.info(new StringBuffer().append("Write ").append(
				f.getAbsolutePath()));
		try {
			Util.writeToFile(f,
					this.getWebServiceDynamicWsdlConfig(wsdlServiceId));
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
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
		sb.append(this.getApplicationMarshaller());
		sb.append("\n");
		sb.append(this.getServiceDefinition());
		sb.append("</beans>\n");

		File f = Util.getFile(
				this.outputDirectory,
				"dispatcher",
				new StringBuffer(64)
						.append(GetServiceIds
								.getServiceIdBasename(this.serviceId))
						.append("SpringIntegrationDispatcherConfig.xml")
						.toString());
		this.log.info(new StringBuffer().append("Write ").append(
				f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}

		if (this.serviceId.length() > 0) {
			List<String> serviceIds = GetServiceIds
					.getServiceIds(this.serviceId);
			for (String sid : serviceIds) {
				this.createWebServiceDynamicWsdlConfig(sid);
			}
		} else {
			TreeSet<String> serviceIds = new TreeSet<String>();
			for (ElementType elem : this.config.getElementTypes()) {
				if (elem.getServiceId() != null
						&& !elem.getServiceId().equals(
								this.serviceIdCommonServices)
						&& !elem.getServiceId().trim().isEmpty()) {
					serviceIds.add(elem.getServiceId());
				}
			}
			for (String wsdlServiceId : serviceIds) {
				this.createWebServiceDynamicWsdlConfig(wsdlServiceId);
			}
		}
		this.log.debug("-generate");
	}

	private String getApplicationMarshaller() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t<!-- Marshaller of the application -->\n");
		sb.append("\t<oxm:jaxb2-marshaller id=\"");
		sb.append(this.marshallerName);
		sb.append("\" \n");
		sb.append("\t\tcontextPath=\"");
		sb.append(Util.getContextPath(this.config.getPackageNames()));
		sb.append("\"\n\t/>\n");
		return sb.toString();
	}

	private String getApplicationName() {
		String s = this.warName;
		int index = this.warName.indexOf('#');
		if (index > 0) {
			s = this.warName.substring(0, index);
		}
		return s;
	}

	private String getApplicationVersion() {
		String s = "";
		int index = this.warName.lastIndexOf('#');
		if (index > 0 && this.warName.length() > index) {
			s = this.warName.substring(index + 1, this.warName.length());
		}
		return s.trim();
	}

	private boolean isValidServiceId(final String elementServiceId) {
		boolean valid = false;
		List<String> serviceIds = GetServiceIds.getServiceIds(this.serviceId);
		if (serviceIds.size() == 0) {
			serviceIds = GetServiceIds.getAllServiceIds(this.config);
		}
		for (String sid : serviceIds) {
			valid = Util.isValidServiceId(elementServiceId, sid,
					this.serviceIdCommonServices,
					this.serviceCreationWithCommon);
			if (valid) {
				break;
			}
		}
		return valid;
	}

	private String getServiceDefinition() {
		StringBuffer sb = new StringBuffer(1024);
		String operationNameElement;
		String serviceIdElement;
		for (ElementType element : this.elementTypes) {
			if (element.isRequest()
					&& this.isValidServiceId(element.getServiceId())) {
				ElementType elementResponse = XsdsUtil.findResponse(element,
						this.config.getElementTypes(), this.config);
				if (elementResponse != null) {
					ComplexType ctResponse = new ComplexType(elementResponse
							.getElement().getType(), this.config);
					if (ctResponse != null && !ctResponse.isSimpleType()
							&& !ctResponse.isPrimitiveType()) {
						operationNameElement = element.getOperationName();
						serviceIdElement = element.getServiceId();

						sb.append("\t<!-- ").append(serviceIdElement)
								.append(".").append(operationNameElement)
								.append(" -->");

						sb.append("\n\t<int:channel id=\"");
						sb.append(element.getChannelNameWsRequest());
						sb.append("\" />");
						sb.append("\n\t<int:channel id=\"");
						sb.append(element.getChannelNameWsResponse());
						sb.append("\" />");

						sb.append("\n\t<int-ws:inbound-gateway");
						sb.append(" id=\"");
						sb.append(element.getBeanIdWsInboundGateway());
						sb.append("\" marshaller=\"");
						sb.append(this.marshallerName);
						sb.append("\"\n");
						sb.append("\t\trequest-channel=\"");
						sb.append(element.getChannelNameWsRequest());
						sb.append("\"\n");
						sb.append("\t\treply-channel=\"");
						sb.append(element.getChannelNameWsResponse());
						sb.append("\"\n");
						sb.append("\t/>\n\n");
					}
				}
			}
		}
		return sb.toString();
	}

	private String getWebServiceDynamicWsdlConfig(final String wsdlServiceId) {
		String tns = "";
		String dynamicWsdlId = wsdlServiceId;
		String msg = new StringBuffer(32).append(".")
				.append(this.config.getMessagePackageNameSuffix()).toString();
		String srv = new StringBuffer(32).append(".")
				.append(this.serviceIdCommonServices).toString();
		if (wsdlServiceId.length() > 0) {
			srv = new StringBuffer(32).append(".").append(wsdlServiceId)
					.toString();
		} else {
			dynamicWsdlId = this.serviceIdCommonServices;
		}

		for (String targetNamespace : this.config.getXsdContainerMap().keySet()) {
			XsdContainer xc = this.config.getXsdContainerMap().get(
					targetNamespace);
			if (xc.getPackageName().contains(msg)
					&& xc.getPackageName().contains(srv)) {
				if (!xc.getTargetNamespace().contains(
						this.config.getDeltaPackageNameSuffix())) {
					tns = xc.getTargetNamespace();
				}
			}
		}
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:sws=\"http://www.springframework.org/schema/web-services\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/web-services http://www.springframework.org/schema/web-services/web-services-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.ws.version.xsd.version",
				DEFAULT_SPRING_WS_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\"\n");
		sb.append(">\n");
		sb.append("\t<sws:dynamic-wsdl \n");
		sb.append("\t\tid=\"");
		sb.append(dynamicWsdlId);
		sb.append("\" \n");
		sb.append("\t\tportTypeName=\"");
		sb.append(dynamicWsdlId);
		sb.append("\"\n");
		sb.append("\t\tlocationUri=\"${eip.web.service.server}/");
		sb.append(this.getApplicationName());
		if (this.getApplicationVersion().length() > 0) {
			sb.append("/").append(this.getApplicationVersion());
		}
		sb.append("/services/");
		sb.append(dynamicWsdlId);
		sb.append("\"\n");
		sb.append("\t\ttargetNamespace=\"");
		sb.append(tns);
		sb.append("\"\n");
		sb.append("\t\tcreateSoap11Binding=\"false\"\n");
		sb.append("\t\tcreateSoap12Binding=\"true\">\n");
		for (String targetNamespace : this.config.getXsdContainerMap().keySet()) {
			XsdContainer xc = this.config.getXsdContainerMap().get(
					targetNamespace);
			if (xc.getPackageName().contains(msg)
					&& (wsdlServiceId.length() == 0 || xc.getPackageName()
							.contains(srv))) {
				sb.append("\t\t<sws:xsd location=\"/WEB-INF/classes");
				sb.append(Util.getRelativePathTranslated(
						this.config.getBaseDirectory(), xc.getFile()));
				sb.append("\" />\n");
			}
		}
		sb.append("\t</sws:dynamic-wsdl>\n");
		sb.append("</beans>\n");
		return sb.toString();
	}

	private String getXmlDefinition() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:int=\"http://www.springframework.org/schema/integration\"\n");
		sb.append("\txmlns:int-ws=\"http://www.springframework.org/schema/integration/ws\"\n");
		sb.append("\txmlns:oxm=\"http://www.springframework.org/schema/oxm\" \n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.integration.version.xsd.version",
				DEFAULT_SPRING_INTEGRATION_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration/ws http://www.springframework.org/schema/integration/ws/spring-integration-ws-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.integration.version.xsd.version",
				DEFAULT_SPRING_INTEGRATION_XSD_VERSION));
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
