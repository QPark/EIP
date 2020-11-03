/*******************************************************************************
 * Copyright (c) 2017 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.springconfig;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
 *
 * @author bhausen
 */
public class WebServiceDispatcherXmlGenerator {
	private final Log log;
	private final XsdsUtil config;
	private final File outputDirectory;
	private final String combinedServiceIdName;
	private final Set<ElementType> elementTypes;
	private final String marshallerName;
	private final MavenProject project;
	private String serviceId = "esb";
	private final String eipVersion;

	/**
	 * @param config
	 * @param elementTypes
	 */
	public WebServiceDispatcherXmlGenerator(final XsdsUtil config,
			final String serviceId, final String warName,
			final File outputDirectory, final MavenProject project,
			final String eipVersion, final Log log) {
		this.config = config;
		this.serviceId = serviceId == null ? "" : serviceId;
		this.outputDirectory = outputDirectory;
		this.project = project;
		this.eipVersion = eipVersion;
		this.log = log;
		this.elementTypes = config.getElementTypes();
		if (serviceId == null) {
			final Collection<String> serviceIds = config.getServiceIdRegistry()
					.getAllServiceIds();
			final StringBuffer sb = new StringBuffer(128);
			for (final String sid : serviceIds) {
				sb.append(sid).append(", ");
			}
			this.combinedServiceIdName = ServiceIdRegistry
					.getCombinedServiceIdCapitalizedPackageName(sb.toString());
		} else {
			this.combinedServiceIdName = ServiceIdRegistry
					.getCombinedServiceIdCapitalizedPackageName(serviceId);
		}
		this.marshallerName = new StringBuffer(32).append("marshaller")
				.append(this.combinedServiceIdName).toString();
	}

	private void createWebServiceDynamicWsdlConfig(final String sid) {
		String sids = Util.capitalizePackageName(sid);
		if (sids.length() > 32) {
			sids = new StringBuffer(48).append(sids.substring(0, 32))
					.append(System.currentTimeMillis()).toString();
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
			}
		}
		final String fileName = new StringBuffer(128).append(sids)
				.append("DynamicWsdlConfig.xml").toString();
		final File f = Util.getFile(this.outputDirectory, "dispatcher",
				fileName);
		this.log.info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, this.getWebServiceDynamicWsdlConfig(sid));
		} catch (final Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void generate() {
		this.log.debug("+generate");
		final StringBuffer sb = new StringBuffer(1024);
		sb.append(this.getXmlDefinition());
		sb.append(Util.getGeneratedAtXmlComment(this.getClass(),
				this.eipVersion));
		sb.append("\n");
		sb.append(this.getApplicationMarshaller());
		sb.append("\n");
		sb.append(this.getServiceDefinition());
		sb.append("</beans>\n");

		final String fileName = new StringBuffer(64)
				// .append(this.combinedServiceIdName)
				.append("SpringIntegrationDispatcherConfig.xml").toString();
		final File f = Util.getFile(this.outputDirectory, "dispatcher",
				fileName);
		this.log.info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (final Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}

		if (this.serviceId.length() > 0) {
			final Set<String> totalServiceIds = new TreeSet<>();
			final List<String> list = ServiceIdRegistry
					.splitServiceIds(this.serviceId);
			totalServiceIds.addAll(list);
			for (final String sid : list) {
				totalServiceIds.addAll(this.config.getServiceIdRegistry()
						.getServiceIdEntry(sid).getTotalServiceIdImports());
			}
			for (final String sid : totalServiceIds) {
				this.createWebServiceDynamicWsdlConfig(sid);
			}
		} else {
			for (final String sid : this.config.getServiceIdRegistry()
					.getAllServiceIds()) {
				this.createWebServiceDynamicWsdlConfig(sid);
			}
		}
		this.log.debug("-generate");
	}

	private String getApplicationMarshaller() {
		final StringBuffer sb = new StringBuffer(1024);

		sb.append("\t<!-- Marshaller of the application -->\n");
		sb.append("\t<bean id=\"");
		sb.append(this.marshallerName);
		sb.append(
				"\" class=\"org.springframework.oxm.jaxb.Jaxb2Marshaller\">\n");

		sb.append("\t\t<property name=\"packagesToScan\">\n");
		sb.append("\t\t\t<list>\n");
		final List<String> sids = ServiceIdRegistry
				.splitServiceIds(this.serviceId);
		if (sids.isEmpty()) {
			sids.addAll(this.config.getServiceIdRegistry().getAllServiceIds());
		}
		sids.stream().map(sid -> this.config.getServiceIdRegistry()
				.getServiceIdEntry(sid)).forEach(side -> {
					sb.append("\t\t\t\t<value>");
					sb.append(side.getPackageName());
					sb.append("</value>\n");
				});
		sb.append("\t\t\t</list>\n");
		sb.append("\t\t</property>\n");

		sb.append("\t</bean>\n");

		return sb.toString();
	}

	// private String getApplicationName() {
	// String s = this.warName;
	// int index = this.warName.indexOf('#');
	// if (index > 0) {
	// s = this.warName.substring(0, index);
	// }
	// return s;
	// }
	//
	// private String getApplicationVersion() {
	// String s = "";
	// int index = this.warName.lastIndexOf('#');
	// if (index > 0 && this.warName.length() > index) {
	// s = this.warName.substring(index + 1, this.warName.length());
	// }
	// return s.trim();
	// }

	private String getServiceDefinition() {
		final StringBuffer sb = new StringBuffer(1024);
		String operationNameElement;
		String serviceIdElement;
		for (final ElementType element : this.elementTypes) {
			if (element.isRequest() && this.config.getServiceIdRegistry()
					.isValidServiceId(element.getServiceId(), this.serviceId)) {
				final ElementType elementResponse = XsdsUtil.findResponse(
						element, this.config.getElementTypes(), this.config);
				if (elementResponse != null) {
					final ComplexType ctResponse = new ComplexType(
							elementResponse.getElement().getType(),
							this.config);
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

	private String getWebServiceDynamicWsdlConfig(final String sid) {
		final ServiceIdEntry entry = this.config.getServiceIdRegistry()
				.getServiceIdEntry(sid);
		final XsdContainer xc = this.config
				.getXsdContainer(entry.getTargetNamespace());

		final StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append(
				"<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append(
				"\txmlns:sws=\"http://www.springframework.org/schema/web-services\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		String springVersion = this.project.getProperties()
				.getProperty("org.springframework.version.xsd.version");
		sb.append(
				"\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");

		springVersion = this.project.getProperties()
				.getProperty("org.springframework.ws.version.xsd.version");
		sb.append(
				"\t\thttp://www.springframework.org/schema/web-services http://www.springframework.org/schema/web-services/web-services");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append("\t\"\n");
		sb.append(">\n");
		sb.append("\t<sws:dynamic-wsdl \n");
		sb.append("\t\tid=\"");
		sb.append(sid);
		sb.append("\" \n");
		sb.append("\t\tportTypeName=\"");
		sb.append(sid);
		sb.append("\"\n");
		sb.append("\t\tlocationUri=\"/services/");
		sb.append(sid);
		sb.append("\"\n");
		sb.append("\t\ttargetNamespace=\"");
		sb.append(entry.getTargetNamespace());
		sb.append("\"\n");
		sb.append("\t\tcreateSoap11Binding=\"false\"\n");
		sb.append("\t\tcreateSoap12Binding=\"true\">\n");
		sb.append("\t\t<sws:xsd location=\"");
		sb.append(Util.getRelativePathTranslated(this.config.getBaseDirectory(),
				xc.getFile()));
		sb.append("\" />\n");
		sb.append("\t</sws:dynamic-wsdl>\n");
		sb.append("</beans>\n");
		return sb.toString();
	}

	private String getXmlDefinition() {
		final StringBuffer sb = new StringBuffer(1024);
		sb.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		sb.append(
				"<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append(
				"\txmlns:int=\"http://www.springframework.org/schema/integration\"\n");
		sb.append(
				"\txmlns:int-ws=\"http://www.springframework.org/schema/integration/ws\"\n");
		sb.append(
				"\txmlns:oxm=\"http://www.springframework.org/schema/oxm\" \n");
		sb.append("\txsi:schemaLocation=\"\n");
		String springVersion = this.project.getProperties()
				.getProperty("org.springframework.version.xsd.version");
		sb.append(
				"\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append(
				"\t\thttp://www.springframework.org/schema/oxm http://www.springframework.org/schema/oxm/spring-oxm");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");

		springVersion = this.project.getProperties().getProperty(
				"org.springframework.integration.version.xsd.version");
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
