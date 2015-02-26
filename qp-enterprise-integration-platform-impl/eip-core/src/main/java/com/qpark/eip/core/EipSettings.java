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
package com.qpark.eip.core;

/**
 * @author bhausen
 */
public interface EipSettings {
	/** The maximumg database lines default number. */
	public static final int EIP_UTIL_MAX_DATABASE_LINES_NUMBER = 200;
	/** The property name of the maximumg database lines default. */
	public static final String EIP_UTIL_MAX_DATABASE_LINES = "eip.util.database.max.lines";

	/** The property name of the application name. */
	public static final String EIP_APPLICATION_NAME = "eip.application.name";
	/** The property name of the service name. */
	public static final String EIP_SERVICE_NAME = "eip.service.name";
	/** The property name of the web service version. */
	public static final String EIP_SERVICE_VERSION = "eip.service.version";
	/** The property name of the applications war name. */
	public static final String EIP_APPLICATION_WAR_NAME = "eip.application.war.name";
	/** The property name of the application build time. */
	public static final String EIP_APPLICATION_BUILD_TIME = "eip.application.build.time";
	/** The property name of the application scm revision. */
	public static final String EIP_APPLICATION_SCM_REVISION = "eip.application.scm.revision";

	/** The property name of the application maven artifact group id. */
	public static final String EIP_APPLICATION_ARTIFACT_GROUPID = "eip.application.maven.artifact.groupid";
	/** The property name of the application maven artifact artifact id. */
	public static final String EIP_APPLICATION_ARTIFACT_ARTIFACTID = "eip.application.maven.artifact.artifactid";
	/** The property name of the application maven artifact version. */
	public static final String EIP_APPLICATION_ARTIFACT_VERSION = "eip.application.maven.artifact.version";

	/** The property name of the web service server (used by dynamic wsdl). */
	public static final String EIP_WEB_SERVICE_SERVER = "eip.web.service.server";
	/** The property name of the web service validation of incoming messages. */
	public static final String EIP_WEB_SERVICE_MESSAG_VALIDATION_INCOMING = "eip.web.service.message.validation.incoming";
	/** The property name of the web service validation of outgoing messages. */
	public static final String EIP_WEB_SERVICE_MESSAG_VALIDATION_OUTGOING = "eip.web.service.message.validation.outgoing";

	/** The property name of the jks keystore url. */
	public static final String EIP_JKS_KEYSTORE_RESOURCE_URL = "eip.jks.keystore.url";
	/** The property name of the jks keystore password. */
	public static final String EIP_JKS_KEYSTORE_PASSWORD = "eip.jks.keystore.password";

	/** The property name of the proxy host. */
	public static final String EIP_PROXY_HOST = "eip.proxy.host";
	/** The property name of the proxy port. */
	public static final String EIP_PROXY_PORT = "eip.proxy.port";
	/** The property name of the proxy username. */
	public static final String EIP_PROXY_USERNAME = "eip.proxy.username";
	/** The property name of the proxy password. */
	public static final String EIP_PROXY_PASSWORD = "eip.proxy.password";

	/** The ReferenceDataType.category of users. */
	public static final String EIP_SPRING_INTEGRATION_USER_CATEGORY = "EipSpringIntegrationSecurityUser";
	/** The ReferenceDataType.category of user role relations. */
	public static final String EIP_SPRING_INTEGRATION_USERROLE_CATEGORY = "EipSpringIntegrationSecurityUserRole";
	/** The ReferenceDataType.category of roles. */
	public static final String EIP_SPRING_INTEGRATION_ROLE_CATEGORY = "EipSpringIntegrationSecurityRole";

}
