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
package com.qpark.maven.plugin.persistenceconfig;

import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_FRAMEWORK_XSD_VERSION;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;

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
 * @author bhausen
 */
@Mojo(name = "generate-persistence-config", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SpringPersistenceConfigMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	private File outputDirectory;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	/** The name of the hibernate dialect. */
	@Parameter(property = "hibernateDialect", defaultValue = "org.hibernate.dialect.Oracle10gDialect")
	private String hibernateDialect;
	/** The name of the JNDI datasource. */
	@Parameter(property = "datasourceJndiName", defaultValue = "")
	private String datasourceJndiName;
	/** The name of the persistence unit. */
	@Parameter(property = "persistenceUnitName", defaultValue = "")
	private String persistenceUnitName;

	@Component
	private MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		StringBuffer sb = new StringBuffer(1024);
		sb.append(this.getSpringPersistenceConfigXml());

		File f = Util.getFile(
				this.outputDirectory,
				new StringBuffer().append(this.basePackageName)
						.append("-persistence-spring-config.xml").toString());
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		this.getLog().debug("-execute");
	}

	private String getSpringPersistenceConfigXml() {
		StringBuffer sb = new StringBuffer(1024);

		String prefix = Util.capitalizePackageName(this.basePackageName);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:util=\"http://www.springframework.org/schema/util\"\n");
		sb.append("\txmlns:tx=\"http://www.springframework.org/schema/tx\"\n");
		sb.append("\txmlns:context=\"http://www.springframework.org/schema/context\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n\t\"\n>");
		sb.append("\n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\t<!-- Properties -->\n");
		sb.append("\t<import resource=\"classpath:/");
		sb.append(this.basePackageName);
		sb.append(".properties-config.xml\" />\n");
		// sb.append("\t<!-- Object factories -->\n");
		// sb.append("\t<bean class=\"");
		// sb.append(this.basePackageName);
		// sb.append(".ModelObjectFactory\" />\n");
		// sb.append("\t<bean class=\"");
		// sb.append(this.basePackageName);
		// sb.append(".ServiceObjectFactory\" />\n");
		// sb.append("\n");

		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("DataSourceJndi\" class=\"org.springframework.jndi.JndiObjectFactoryBean\">\n");
		sb.append("\t\t<property name=\"jndiName\" value=\"java:comp/env/jdbc/");
		sb.append(this.datasourceJndiName);
		sb.append("\" />\n");
		sb.append("\t\t<property name=\"resourceRef\" value=\"true\" />\n");
		sb.append("\t\t<property name=\"defaultObject\" ref=\"");
		sb.append(prefix);
		sb.append("DataSourceJdbc\" />\n");
		sb.append("\t</bean>\n");

		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("DataSourceJdbc\" class=\"org.springframework.jdbc.datasource.DriverManagerDataSource\">\n");
		sb.append("\t\t<property name=\"driverClassName\" value=\"${");
		sb.append(prefix);
		sb.append(".jdbc.driverClassName:com.qpark.eip.core.DummyJdbcDriver}\" />\n");
		sb.append("\t\t<property name=\"url\" value=\"${");
		sb.append(prefix);
		sb.append(".jdbc.url:__");
		sb.append(prefix);
		sb.append("_URL_NOT_SET__}\" />\n");
		sb.append("\t\t<property name=\"username\" value=\"${");
		sb.append(prefix);
		sb.append(".jdbc.username:__");
		sb.append(prefix);
		sb.append("_USERNAME_NOT_SET__}\" />\n");
		sb.append("\t\t<property name=\"password\" value=\"${");
		sb.append(prefix);
		sb.append(".jdbc.password:__");
		sb.append(prefix);
		sb.append("_PASSWORD_NOT_SET__}\" />\n");
		sb.append("\t</bean>\n");

		sb.append("\t<!-- Database -->\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("EntityManagerFactory\" class=\"org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean\">\n");
		sb.append("\t\t<property name=\"persistenceXmlLocation\" value=\"classpath:/META-INF/");
		sb.append(this.persistenceUnitName);
		sb.append("/persistence.xml\" />\n");
		sb.append("\t\t<property name=\"persistenceUnitName\" value=\"");
		sb.append(this.persistenceUnitName);
		sb.append("\" />\n");
		sb.append("\t\t<property name=\"dataSource\" ref=\"");
		sb.append(prefix);
		sb.append("DataSourceJndi\"/>\n");
		sb.append("\t\t<property name=\"jpaVendorAdapter\">\n");
		sb.append("\t\t\t<bean class=\"org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter\">\n");
		sb.append("\t\t\t\t<property name=\"showSql\" value=\"false\" />\n");
		sb.append("\t\t\t\t<property name=\"generateDdl\" value=\"false\" />\n");
		sb.append("\t\t\t\t<property name=\"databasePlatform\" value=\"");
		sb.append(this.hibernateDialect);
		sb.append("\" />\n");
		sb.append("\t\t\t</bean>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("SessionFactory\" factory-bean=\"");
		sb.append(prefix);
		sb.append("EntityManagerFactory\" factory-method=\"getSessionFactory\" />\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("TransactionManager\" class=\"org.springframework.orm.jpa.JpaTransactionManager\" >\n");
		sb.append("\t\t<property name=\"entityManagerFactory\" ref=\"");
		sb.append(prefix);
		sb.append("EntityManagerFactory\"/>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<tx:annotation-driven transaction-manager=\"");
		sb.append(prefix);
		sb.append("TransactionManager\" />\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("JmxExporter\" class=\"org.springframework.jmx.export.MBeanExporter\">\n");
		sb.append("\t\t<property name=\"beans\">\n");
		sb.append("\t\t\t<map>\n");
		sb.append("\t\t\t\t<entry key=\"Hibernate:type=statistics,application=");
		sb.append(prefix);
		sb.append("-${eip.application.war.name:__WAR_NAME__}\" value-ref=\"");
		sb.append(prefix);
		sb.append("HibernateStatisticsBean\"/>\n");
		sb.append("\t\t\t</map>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("HibernateStatisticsBean\" class=\"org.hibernate.jmx.StatisticsService\">\n");
		sb.append("\t\t<property name=\"statisticsEnabled\" value=\"true\"/>\n");
		sb.append("\t\t<property name=\"sessionFactory\"><util:property-path path=\"");
		sb.append(prefix);
		sb.append("EntityManagerFactory.sessionFactory\" /></property>\n");
		sb.append("\t</bean>\n");
		sb.append("</beans>\n");
		return sb.toString();
	}

}
