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

import java.io.File;
import java.util.Date;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generates a ServiceObjectFactory out of the XSDs containing elements.
 * @author bhausen
 */
public class WebXmlGenerator {
	private final Log log;
	@SuppressWarnings("unused")
	private final XsdsUtil config;
	private final String serviceVersion;
	private final String additionalWebappFilter;
	private final File outputDirectory;
	private final String serviceId;
	private final String warName;
	private final boolean enableHttpServlet;
	private final String httpServletUrl;
	private final String revisionNumber;
	private final Date d = new Date();

	/**
	 * @param config
	 * @param elementTypes
	 */
	public WebXmlGenerator(final XsdsUtil config, final String serviceId,
			final String serviceVersion, final String revisionNumber,
			final String warName, final String additionalWebappFilter,
			final String httpServletUrl, final File outputDirectory,
			final Log log) {
		this.config = config;
		this.serviceId = serviceId == null ? "" : serviceId.replace(',', '-')
				.replaceAll(" ", "");
		this.serviceVersion = serviceVersion;
		this.revisionNumber = revisionNumber;
		this.warName = warName;
		this.additionalWebappFilter = additionalWebappFilter == null ? ""
				: additionalWebappFilter;
		this.httpServletUrl = httpServletUrl;
		this.enableHttpServlet = this.httpServletUrl != null
				&& this.httpServletUrl.trim().length() > 0;
		this.outputDirectory = outputDirectory;
		this.log = log;
	}

	public void generate() {
		this.log.debug("+generate");

		File f = Util.getFile(this.outputDirectory, "", "web.xml");
		this.log.info(new StringBuffer().append("Write ").append(
				f.getAbsolutePath()));

		try {
			Util.writeToFile(f, this.getWebXml());
		} catch (Exception e) {
			this.log.error(e.getMessage());
			e.printStackTrace();
		}
		this.log.debug("-generate");
	}

	private String getWebXml() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n");
		sb.append("\txmlns=\"http://java.sun.com/xml/ns/javaee\"  \n");
		sb.append("\txmlns:web=\"http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\" \n");
		sb.append("\txsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\" \n");
		sb.append("\tid=\"WebApp_ID\" version=\"2.5\"> \n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt(this.d));
		sb.append(" -->\n");
		sb.append("\n");
		sb.append("\t<context-param>\n");
		sb.append("\t\t<param-name>contextConfigLocation</param-name>\n");
		sb.append("\t\t<param-value>classpath:MainApplicationContext.xml</param-value>\n");
		sb.append("\t</context-param>\n");
		sb.append("\t<display-name>Service bus: ");
		sb.append(this.serviceId);
		sb.append(" service ");
		sb.append(this.serviceVersion);
		sb.append("-r");
		sb.append(this.revisionNumber);
		sb.append(" build time: ");
		sb.append(Util.getXsdDateTime(this.d));
		sb.append("</display-name>\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>servicebus-name</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>Service bus: ");
		sb.append(this.serviceId);
		sb.append(" service ");
		sb.append(this.serviceVersion);
		sb.append("-");
		sb.append(this.revisionNumber);
		sb.append(" build time: ");
		sb.append(Util.getXsdDateTime(this.d));
		sb.append("</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>servicebus-service-name</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>");
		sb.append(this.serviceId);
		sb.append("</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>servicebus-version-name</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>");
		sb.append(this.serviceVersion);
		sb.append("</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>servicebus-build-svnrevision</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>");
		sb.append(this.revisionNumber);
		sb.append("</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>servicebus-build-time</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>");
		sb.append(Util.getXsdDateTime(this.d));
		sb.append("</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>servicebus-war-name</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>");
		sb.append(this.warName);
		sb.append(".war</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<!-- logback definitions -->\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>logback/context-name</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>");
		sb.append(this.warName);
		sb.append("</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<env-entry>\n");
		sb.append("\t\t<env-entry-name>logback/configuration-resource</env-entry-name>\n");
		sb.append("\t\t<env-entry-type>java.lang.String</env-entry-type>\n");
		sb.append("\t\t<env-entry-value>logback.xml</env-entry-value>\n");
		sb.append("\t</env-entry>\n");
		sb.append("\t<!-- Spring security -->\n");
		sb.append("\t<filter>\n");
		sb.append("\t\t<filter-name>springSecurityFilterChain</filter-name>\n");
		sb.append("\t\t<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>\n");
		sb.append("\t</filter>\n");

		if (this.additionalWebappFilter != null
				&& this.additionalWebappFilter.trim().length() > 0) {
			sb.append("\t<!-- Additional filter -->\n");
			sb.append("\t<filter>\n");
			sb.append("\t\t<filter-name>comQparkMavenPluginServletConfigAdditionalFilter</filter-name>\n");
			sb.append("\t\t<filter-class>");
			sb.append(this.additionalWebappFilter.trim());
			sb.append("</filter-class>\n");
			sb.append("\t</filter>\n");
		}

		sb.append("\t<!-- logback avoiding JNDI calls -->\n");
		sb.append("\t<filter>\n");
		sb.append("\t\t<filter-name>LoggerContextFilter</filter-name>\n");
		sb.append("\t\t<filter-class>ch.qos.logback.classic.selector.servlet.LoggerContextFilter</filter-class>\n");
		sb.append("\t</filter>\n");
		sb.append("\t<filter-mapping>\n");
		sb.append("\t\t<filter-name>springSecurityFilterChain</filter-name>\n");
		sb.append("\t\t<url-pattern>/*</url-pattern>\n");
		sb.append("\t</filter-mapping>\n");
		sb.append("\t<filter-mapping>\n");
		sb.append("\t\t<filter-name>LoggerContextFilter</filter-name>\n");
		sb.append("\t\t<url-pattern>/*</url-pattern>\n");
		sb.append("\t</filter-mapping>\n");

		if (this.additionalWebappFilter != null
				&& this.additionalWebappFilter.trim().length() > 0) {
			sb.append("\t<filter-mapping>\n");
			sb.append("\t\t<filter-name>comQparkMavenPluginServletConfigAdditionalFilter</filter-name>\n");
			sb.append("\t\t<url-pattern>/*</url-pattern>\n");
			sb.append("\t</filter-mapping>\n");
		}

		sb.append("\t<!-- logback should be the first in the list (http://logback.qos.ch/manual/loggingSeparation.html)! -->\n");
		sb.append("\t<listener>\n");
		sb.append("\t\t<listener-class>ch.qos.logback.classic.selector.servlet.ContextDetachingSCL</listener-class>\n");
		sb.append("\t</listener>\n");
		sb.append("\t<listener>\n");
		sb.append("\t\t<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>\n");
		sb.append("\t</listener>\n");

		if (this.enableHttpServlet) {
			sb.append("\t<servlet>\n");
			sb.append("\t\t<servlet-name>http</servlet-name>\n");
			sb.append("\t\t<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>\n");
			sb.append("\t\t<init-param>\n");
			sb.append("\t\t\t<param-name>contextConfigLocation</param-name>\n");
			sb.append("\t\t\t<param-value>/WEB-INF/http-servlet.xml</param-value>\n");
			sb.append("\t\t\t<param-name>defaultHtmlEscape</param-name>\n");
			sb.append("\t\t\t<param-value>true</param-value>\n");
			sb.append("\t\t</init-param>\n");
			sb.append("\t\t<load-on-startup>1</load-on-startup>\n");
			sb.append("\t</servlet>\n");
		}
		sb.append("\t<servlet>\n");
		sb.append("\t\t<servlet-name>ws</servlet-name>\n");
		sb.append("\t\t<servlet-class>org.springframework.ws.transport.http.MessageDispatcherServlet</servlet-class>\n");
		if (!this.enableHttpServlet) {
			sb.append("\t\t<load-on-startup>1</load-on-startup>\n");
		}
		sb.append("\t</servlet>\n");
		sb.append("\t<servlet-mapping>\n");
		sb.append("\t\t<servlet-name>ws</servlet-name>\n");
		sb.append("\t\t<url-pattern>/");
		if (this.warName.contains("##")) {
			sb.append(this.serviceVersion);
			sb.append("/");
		}
		sb.append("services/*</url-pattern>\n");
		sb.append("\t</servlet-mapping>\n");
		sb.append("\t<servlet-mapping>\n");
		sb.append("\t\t<servlet-name>ws</servlet-name>\n");
		sb.append("\t\t<url-pattern>*.wsdl</url-pattern>\n");
		sb.append("\t</servlet-mapping>\n");

		if (this.enableHttpServlet) {
			sb.append("\t<servlet-mapping>\n");
			sb.append("\t\t<servlet-name>http</servlet-name>\n");
			sb.append("\t\t<url-pattern>/");
			if (this.warName.contains("##")) {
				sb.append(this.serviceVersion);
				sb.append("/");
			}
			sb.append(this.httpServletUrl);
			sb.append("/*</url-pattern>\n");
			sb.append("\t</servlet-mapping>\n");
			// sb.append("\t<servlet-mapping>\n");
			// sb.append("\t\t<servlet-name>httpServlet</servlet-name>\n");
			// sb.append("\t\t<url-pattern>/");
			// if (this.warName.contains("##")) {
			// sb.append(this.serviceVersion);
			// sb.append("/");
			// }
			// sb.append("schemas/*</url-pattern>\n");
			// sb.append("\t</servlet-mapping>\n");
		}

		sb.append("\t<filter>\n");
		sb.append("\t\t<filter-name>httpMethodFilter</filter-name>\n");
		sb.append("\t\t	<filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>\n");
		sb.append("\t</filter>\n");
		sb.append("\t<filter-mapping>\n");
		sb.append("\t\t<filter-name>httpMethodFilter</filter-name>\n");
		sb.append("\t\t<servlet-name>dispatcher</servlet-name>\n");
		sb.append("\t</filter-mapping>\n");

		sb.append("\t<error-page>\n");
		sb.append("\t\t<exception-type>java.lang.Throwable</exception-type>\n");
		sb.append("\t\t<location>/WEB-INF/views/errors/container-exceptions.jsp</location>\n");
		sb.append("\t</error-page>\n");

		sb.append("\t<mime-mapping>\n");
		sb.append("\t\t<extension>xsd</extension>\n");
		sb.append("\t\t<mime-type>text/xml</mime-type>\n");
		sb.append("\t</mime-mapping>\n");
		sb.append("</web-app>\n");
		return sb.toString();
	}
}
