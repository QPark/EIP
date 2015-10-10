/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.maven.plugin.securityconfig;

import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_FRAMEWORK_XSD_VERSION;
import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_SECURITY_XSD_VERSION;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
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
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Create the <code>securityPolicy.xml</code> and the
 * <code>security-spring-config.xml</code> which imports the
 * <code>security-authentication-spring-config.xml</code> and the
 * <code>security-authorisation-spring-config.xml</code>. Additionaly all roles
 * used in the configuration are listed in the file
 * <code>security-roles-available.xml</code>.
 * <p>
 * The <code>securityPolicy.xml</code> is referenced by the
 * <code>ws-servlet.xml</code>.
 * @author bhausen
 */
@Mojo(name = "generate-security", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SecurityConfigMojo extends AbstractMojo {
	private static List<String> getSplitted(final String s) {
		List<String> list = new ArrayList<String>();
		if (s != null && s.trim().length() > 0) {
			for (String string : s.split(",")) {
				if (string.trim().length() > 0) {
					list.add(string.trim());
				}
			}
		}
		return list;
	}

	/** A list of channel patterns that allow users to update. */
	@Parameter(property = "channelPatternsUpdate")
	private String channelPatternsUpdate;
	/** A list of channel patterns that allow users to read. */
	@Parameter(property = "channelPatternsRead")
	private String channelPatternsRead;
	/** A list of channel patterns that allow users to create. */
	@Parameter(property = "channelPatternsCreate")
	private String channelPatternsCreate;
	/** A list of channel patterns that allow users to delete. */
	@Parameter(property = "channelPatternsDelete")
	private String channelPatternsDelete;
	/**
	 * A list of channel patterns that do need the role ROLE_ADMIN
	 * authorisation.
	 */
	@Parameter(property = "channelPatternsAdminAuthorisation")
	private String channelPatternsAdminAuthorisation;
	/** A list of channel patterns that do not need any authorisation. */
	@Parameter(property = "channelPatternsAnnonymousAuthorisation", defaultValue = "internal.*")
	private String channelPatternsAnnonymousAuthorisation;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	@Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
	private String deltaPackageNameSuffix;
	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffix", defaultValue = "msg")
	protected String messagePackageNameSuffix;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;
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
	private final List<String> roleList = new ArrayList<String>();

	@Component
	private MavenProject project;

	/** The name of the service id of common services. */
	@Parameter(property = "serviceIdCommonServices", defaultValue = "common")
	private String serviceIdCommonServices;
	/**
	 * The name of the SecurityContextHolder strategy name (see
	 * org.springframework.security.core.context.SecurityContextHolder).
	 */
	@Parameter(property = "securityContextHolderStrategyName", defaultValue = "")
	private String securityContextHolderStrategyName;
	/**
	 * An implementation of the
	 * <code>com.qpark.eip.core.spring.security.EipLimitedAccessDataProvider</code>
	 * .
	 */
	@Parameter(property = "limitedAccessDataProviderBeanName", defaultValue = "")
	private String limitedAccessDataProviderBeanName;
	/**
	 * An implementation of the
	 * <code>com.qpark.eip.core.spring.security.EipUserProvider</code> .
	 */
	@Parameter(property = "userProviderBeanName", defaultValue = "")
	private String userProviderBeanName;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		XsdsUtil xsds = new XsdsUtil(this.baseDirectory, this.basePackageName,
				this.messagePackageNameSuffix, this.deltaPackageNameSuffix,
				this.serviceRequestSuffix, this.serviceResponseSuffix);

		File f = Util.getFile(this.outputDirectory,
				"security-spring-config.xml");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, this.getSecuritySpringConfig());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		f = Util.getFile(this.outputDirectory,
				"security-authentication-spring-config.xml");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, this.getSecurityAuthenticationSpringConfig());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		f = Util.getFile(this.outputDirectory,
				"security-authorisation-spring-config.xml");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, this.getSecurityAuthorisationSpringConfig(xsds));
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		f = Util.getFile(this.outputDirectory, "security-roles-available.xml");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, this.getAvailableRoles(xsds));
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		f = Util.getFile(this.outputDirectory, "securityPolicy.xml");
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, this.getSecurityPolicy());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		this.getLog().debug("-execute");
	}

	private String getAccessPolicyAdminAnonymous(final List<String> pattern,
			final String role) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t\t<!-- ");
		sb.append(role);
		sb.append(" channels -->\n");
		for (String string : pattern) {
			sb.append("\t\t<int-security:access-policy pattern=\"");
			sb.append(string);
			sb.append("\" send-access=\"");
			sb.append(role);
			sb.append("\" receive-access=\"");
			sb.append(role);
			sb.append("\" />\n");
		}
		return sb.toString();
	}

	private String getAccessPolicyReadCreateUpdateDelete(
			final List<String> pattern, final String role) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t\t<!-- ");
		sb.append(role);
		sb.append(" channels -->\n");
		for (String string : pattern) {
			sb.append("\t\t<int-security:access-policy pattern=\"");
			sb.append(ElementType.WEB_SERVICE_CHANNEL_NAME_PREFIX);
			sb.append(".*?");
			sb.append(string);
			sb.append(".*?");
			sb.append(ElementType.WEB_SERVICE_CHANNEL_NAME_CONTENT);
			sb.append(".*");
			sb.append("\" send-access=\"");
			sb.append(role);
			sb.append(", ROLE_ALL_OPERATIONS");
			sb.append("\" receive-access=\"");
			sb.append(role);
			sb.append(", ROLE_ALL_OPERATIONS");
			sb.append("\" />\n");
		}
		this.roleList.add(role);
		return sb.toString();
	}

	// ACHTUNG: For users UUID use the java.security.Principal class.
	private String getAvailableRoles(final XsdsUtil xsds) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<com:GetReferenceDataResponse xmlns:com=\"http://www.ses.com/CommonServiceMessages-1.0\">\n");
		for (String role : this.roleList) {
			sb.append("\t<com:referenceData>\n");
			sb.append("\t\t<UUID>");
			sb.append(Util.getUUID(Permission.class, role));
			sb.append("</UUID>\n");
			sb.append("\t\t<name>");
			sb.append(role);
			sb.append("</name>\n");
			sb.append("\t\t<id>");
			sb.append(role);
			sb.append("</id>\n");
			sb.append("\t\t<displayValue>");
			sb.append(role);
			sb.append("</displayValue>\n");
			sb.append("\t\t<category>EipSpringIntegrationSecurityRole</category>\n");
			sb.append("\t\t<description>Spring integration security role: ");
			sb.append(role);
			sb.append("</description>\n");
			sb.append("\t\t<active>true</active>\n");
			sb.append("\t</com:referenceData>\n");
		}
		sb.append("</com:GetReferenceDataResponse>\n");
		return sb.toString();
	}

	private String getSecurityAuthenticationSpringConfig() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n");
		sb.append("\txmlns:security=\"http://www.springframework.org/schema/security\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.security.version.xsd.version",
				DEFAULT_SPRING_SECURITY_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\">\n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\t<!-- Authentication manager using the eipDaoAuthenticationProvider. -->\n");
		sb.append("\t<security:authentication-manager alias=\"eipAuthenticationManager\">\n");
		sb.append("\t\t<security:authentication-provider ref=\"eipDaoAuthenticationProvider\" />\n");
		sb.append("\t</security:authentication-manager>\n");
		sb.append("\t<!-- Authentication provider -->\n");
		sb.append("\t<bean id=\"eipAuthenticationProvider\" class=\"org.springframework.security.authentication.ProviderManager\">\n");
		sb.append("\t\t<property name=\"providers\">\n");
		sb.append("\t\t\t<list>\n");
		sb.append("\t\t\t\t<ref local=\"eipDaoAuthenticationProvider\" />\n");
		sb.append("\t\t\t</list>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<!-- DaoAuthenticationProvider without password check. The password check is done by the wsSecurityInterceptor in the ws-servlet.xml! -->\n");
		sb.append("\t<bean id=\"eipDaoAuthenticationProvider\" class=\"com.qpark.eip.core.spring.security.EipDaoAuthenticationProvider\">\n");
		sb.append("\t\t<property name=\"userDetailsService\" ref=\"eipUserDetailsService\" />\n");
		sb.append("\t</bean>\n");
		sb.append("</beans>\n");
		return sb.toString();
	}

	private String getSecurityAuthorisationSpringConfig(final XsdsUtil xsds) {
		List<String> annonyoums = getSplitted(this.channelPatternsAnnonymousAuthorisation);
		List<String> admins = getSplitted(this.channelPatternsAdminAuthorisation);
		List<String> create = getSplitted(this.channelPatternsCreate);
		List<String> read = getSplitted(this.channelPatternsRead);
		List<String> update = getSplitted(this.channelPatternsUpdate);
		List<String> delete = getSplitted(this.channelPatternsDelete);

		this.roleList.add("ROLE_ANONYMOUS");
		this.roleList.add("ROLE_ADMIN");
		this.roleList.add("ROLE_ALL_OPERATIONS");

		StringBuffer sb = new StringBuffer(1024);
		TreeSet<String> serviceIds = new TreeSet<String>();
		StringBuffer operationPolicies = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n");
		sb.append("\txmlns:int-security=\"http://www.springframework.org/schema/integration/security\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration/security http://www.springframework.org/schema/integration/security/spring-integration-security.xsd\n");
		sb.append("\">\n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\n");
		sb.append("\t<!-- Authorization -->\n");
		sb.append("\t<!-- Role voter. -->\n");

		sb.append("\t<bean id=\"eipRoleVoter\" class=\"com.qpark.eip.core.spring.security.EipRoleVoter\"");
		if (this.limitedAccessDataProviderBeanName != null
				&& this.limitedAccessDataProviderBeanName.trim().length() > 0) {
			sb.append(">\n");
			sb.append("\t\t<property name=\"eipLimitedAccessDataProvider\" ref=\"");
			sb.append(this.limitedAccessDataProviderBeanName);
			sb.append("\"/>\n");
			sb.append("\t</bean>\n");
		} else {
			sb.append("/>\n");
		}

		sb.append("\t<bean id=\"eipAccessDecisionManager\" class=\"com.qpark.eip.core.spring.security.EipAffirmativeBased\">\n");
		sb.append("\t\t<property name=\"decisionVoters\">\n");
		sb.append("\t\t\t<list>\n");
		sb.append("\t\t\t\t<ref bean=\"eipRoleVoter\"/>\n");
		sb.append("\t\t\t</list>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		sb.append("\n");
		sb.append("\t<!-- \n");
		sb.append("\tThe pattern (java.util.regexp.Pattern) of the access policies regarding \n");
		sb.append("\tthe channel names used in the spring integration configurations.\n");
		sb.append("\n");
		sb.append("\tEach user gets the role ROLE_ANONYMOUS. If the user has an other role \n");
		sb.append("\tthen ROLE_ANONYMOUS the role ROLE_COMMON need to be added too.\n");
		sb.append("\n");
		sb.append("\tThe user needs only one of the listed roles. All access-policy\n");
		sb.append("\twill be checked until the user has a sufficient role or is\n");
		sb.append("\tnot authorisized to do the operation.");
		sb.append("\t-->\n");
		sb.append("\t<int-security:secured-channels \n");
		sb.append("\t\taccess-decision-manager=\"eipAccessDecisionManager\"\n");
		sb.append("\t\tauthentication-manager=\"eipAuthenticationManager\">\n");
		sb.append(this.getAccessPolicyAdminAnonymous(annonyoums,
				"ROLE_ANONYMOUS"));
		sb.append(this.getAccessPolicyAdminAnonymous(admins, "ROLE_ADMIN"));
		sb.append(this.getAccessPolicyReadCreateUpdateDelete(read, "ROLE_READ"));
		sb.append(this.getAccessPolicyReadCreateUpdateDelete(create,
				"ROLE_CREATE"));
		sb.append(this.getAccessPolicyReadCreateUpdateDelete(update,
				"ROLE_UPDATE"));
		sb.append(this.getAccessPolicyReadCreateUpdateDelete(delete,
				"ROLE_DELETE"));

		sb.append("\t\t<!-- Service wide role securement -->\n");

		operationPolicies
				.append("\t\t<!-- Operation web service operation channels -->\n");
		for (ElementType element : xsds.getElementTypes()) {
			if (element.isRequest()) {
				ElementType elementResponse = XsdsUtil.findResponse(element,
						xsds.getElementTypes(), xsds);
				if (elementResponse != null) {
					ComplexType ctResponse = new ComplexType(elementResponse
							.getElement().getType(), xsds);
					if (ctResponse != null && !ctResponse.isSimpleType()
							&& !ctResponse.isPrimitiveType()) {
						String serviceRole = new StringBuffer(32)
								.append("ROLE_")
								.append(element.getServiceId().toUpperCase())
								.toString();
						String serviceVersionLessRole = serviceRole;
						if (serviceRole.indexOf(".V") > 0) {
							serviceVersionLessRole = serviceRole.substring(0,
									serviceRole.indexOf(".V"));
						}
						String operationRole = new StringBuffer(64)
								.append(serviceRole)
								.append("_")
								.append(element.getOperationName()
										.toUpperCase()).toString();
						if (!serviceIds.contains(element.getServiceId())) {
							serviceIds.add(element.getServiceId());
							this.roleList.add(serviceRole);
							sb.append("\t\t<int-security:access-policy pattern=\"");
							sb.append(element
									.getChannelSecurityPatternService());
							sb.append("\" send-access=\"");
							sb.append(serviceRole);
							if (!serviceRole.equals(serviceVersionLessRole)) {
								sb.append(", ");
								sb.append(serviceVersionLessRole);
								this.roleList.add(serviceVersionLessRole);
							}
							sb.append(", ROLE_ALL_OPERATIONS");
							sb.append("\" receive-access=\"");
							sb.append(serviceRole);
							if (!serviceRole.equals(serviceVersionLessRole)) {
								sb.append(", ");
								sb.append(serviceVersionLessRole);
								this.roleList.add(serviceVersionLessRole);
							}
							sb.append(", ROLE_ALL_OPERATIONS");
							sb.append("\" />\n");
						}

						operationPolicies
								.append("\t\t<int-security:access-policy pattern=\"");
						operationPolicies.append(element
								.getChannelSecurityPatternOperation());
						operationPolicies.append("\" send-access=\"");
						operationPolicies.append(operationRole);
						operationPolicies.append(", ");
						operationPolicies.append(serviceRole);
						operationPolicies.append(", ROLE_ALL_OPERATIONS");
						operationPolicies.append("\" receive-access=\"");
						operationPolicies.append(operationRole);
						operationPolicies.append(", ");
						operationPolicies.append(serviceRole);
						operationPolicies.append(", ROLE_ALL_OPERATIONS");
						operationPolicies.append("\" />\n");
						this.roleList.add(operationRole);
					}
				}
			}
		}

		sb.append(operationPolicies);

		sb.append("\t</int-security:secured-channels>\n");
		sb.append("</beans>\n");
		return sb.toString();
	}

	private String getSecurityPolicy() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<xwss:SecurityConfiguration dumpMessages=\"true\" xmlns:xwss=\"http://java.sun.com/xml/ns/xwss/config\">\n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\t<!-- Used in the ws-servlet.xml by the org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor. -->\n");
		sb.append("     <xwss:RequireUsernameToken passwordDigestRequired=\"true\" nonceRequired=\"true\"/>\n");
		sb.append("</xwss:SecurityConfiguration>\n");
		sb.append("\n");
		return sb.toString();
	}

	private String getSecuritySpringConfig() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns:s=\"http://www.springframework.org/schema/security\"\n");
		sb.append("\txmlns:p=\"http://www.springframework.org/schema/p\"\n");
		sb.append("\txmlns:util=\"http://www.springframework.org/schema/util\"\n");
		sb.append("\txmlns:int-security=\"http://www.springframework.org/schema/integration/security\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.security.version.xsd.version",
				DEFAULT_SPRING_SECURITY_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.0.xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration/security http://www.springframework.org/schema/integration/security/spring-integration-security.xsd\n");
		sb.append("\">\n");
		sb.append("\n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\t<!-- The properties -->\n");
		sb.append("\t<import resource=\"classpath:/");
		sb.append(this.basePackageName);
		sb.append(".properties-config.xml\" />\n");
		sb.append("\t<!-- User and role definition -->\n");
		sb.append("\t<import resource=\"classpath:/security-authentication-spring-config.xml\" />\n");
		sb.append("\t<import resource=\"classpath:/security-authorisation-spring-config.xml\" />\n");
		sb.append("\n");
		sb.append("\t<!-- Spring security base definition. -->\n");
		sb.append("\t<s:http>\n");
		sb.append("\t\t<s:intercept-url pattern=\"/services/**\" access=\"ROLE_ANONYMOUS\" />\n");
		sb.append("\t\t<s:http-basic />\n");
		sb.append("\t\t<s:anonymous />\n");
		sb.append("\t</s:http>\n");

		if (this.securityContextHolderStrategyName != null
				&& this.securityContextHolderStrategyName.trim().length() > 0) {
			sb.append("\t<!-- Set the SecurityContextHolder strategy name -->\n");
			sb.append("\t<bean class=\"org.springframework.beans.factory.config.MethodInvokingFactoryBean\"\n");
			sb.append("\t\tp:targetClass=\"org.springframework.security.core.context.SecurityContextHolder\"\n");
			sb.append("\t\tp:targetMethod=\"setStrategyName\"\n");
			sb.append("\t\tp:arguments=\"");
			sb.append(this.securityContextHolderStrategyName);
			sb.append("\"\n");
			sb.append("\t/>\n");
		}

		sb.append("\t<!-- This bean is used in the ws-servlet.xml wsSecurityInterceptor and in the authentication and authorization part. -->\n");
		sb.append("\t<bean id=\"eipUserDetailsService\" class=\"com.qpark.eip.core.spring.security.EipUserDetailsService\" >\n");
		sb.append("\t\t<property name=\"userProvider\" ref=\"");
		if (this.userProviderBeanName != null
				&& this.userProviderBeanName.trim().length() > 0) {
			sb.append(this.userProviderBeanName);
		} else {
			sb.append(Util.capitalizePackageName(this.basePackageName));
			sb.append("UserProvider");
		}
		sb.append("\" />\n");
		sb.append("\t</bean>\n");

		sb.append("\n");
		sb.append("\t<bean id=\"proxyBean\" class=\"com.qpark.eip.core.spring.security.proxy.ProxyBean\">\n");
		sb.append("\t\t<property name=\"proxyHost\" value=\"${eip.proxy.host:localhost}\" />\n");
		sb.append("\t\t<property name=\"proxyPort\" value=\"${eip.proxy.port:8080}\" />\n");
		sb.append("\t\t<property name=\"username\" value=\"${eip.proxy.username:userName}\" />\n");
		sb.append("\t\t<property name=\"password\" value=\"${eip.proxy.password:password}\" />\n");
		sb.append("\t</bean>\n");
		sb.append("\t<bean id=\"proxyHostConfiguration\" class=\"com.qpark.eip.core.spring.security.proxy.ProxyHostConfiguration\">\n");
		sb.append("\t\t<property name=\"proxyBean\" ref=\"proxyBean\" />\n");
		sb.append("\t</bean>\n");
		sb.append("\t<bean id=\"httpClient\" class=\"org.apache.commons.httpclient.HttpClient\">\n");
		sb.append("\t\t<property name=\"hostConfiguration\" ref=\"proxyHostConfiguration\" />\n");
		sb.append("\t</bean>\n");
		sb.append("\n");

		sb.append("\t<!-- Key store handler -->\n");
		sb.append("\t<bean id=\"eipX509TrustManager\" class=\"com.qpark.eip.core.spring.security.https.EipX509TrustManager\" init-method=\"init\">\n");
		sb.append("\t\t<property name=\"keystore\" value=\"${eip.jks.keystore.url}\"/>\n");
		sb.append("\t\t<property name=\"keystorePassword\" value=\"${eip.jks.keystore.password}\" />\n");
		sb.append("\t</bean>\n");

		sb.append("\t<!-- EipWsChannelInterceptor initialisation -->\n");
		sb.append("\t<bean class=\"com.qpark.eip.core.spring.EipWsChannelInterceptorInitializer\" />\n");
		sb.append("\t<!-- EipWsChannelInterceptors have to have a name! -->\n");
		sb.append("\t<bean name=\"ComQparkEipCoreSpringRequestIdMessageHeaderEnhancer\" class=\"com.qpark.eip.core.spring.RequestIdMessageHeaderEnhancer\" />\n");

		sb.append("\n");
		sb.append("</beans>\n");
		return sb.toString();
	}
}
