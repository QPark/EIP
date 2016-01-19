/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.persistenceconfig;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

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
 *
 * @author bhausen
 */
@Mojo(name = "generate-persistence-config",
		defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SpringPersistenceConfigMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
	private File outputDirectory;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	/**
	 * The name of the database platform. This is the hibernate dialect class
	 * name or eclipse link target database or ...
	 */
	@Parameter(property = "databasePlatform",
			defaultValue = "org.hibernate.dialect.Oracle10gDialect")
	private String databasePlatform;
	/** The name of the JNDI datasource. */
	@Parameter(property = "datasourceJndiName", defaultValue = "")
	private String datasourceJndiName;
	/** The name of the persistence unit. */
	@Parameter(property = "persistenceUnitName", defaultValue = "")
	private String persistenceUnitName;
	/** The name of the spring JPA vendor adapter. */
	@Parameter(property = "springJpaVendorAdapter",
			defaultValue = "org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter")
	private String springJpaVendorAdapter;
	@Component
	private MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		if (this.persistenceUnitName == null
				|| this.persistenceUnitName.trim().length() == 0) {
			throw new MojoExecutionException(
					"No persistence unit name entered. Please use property persistenceUnitName to define.");
		}
		if (this.datasourceJndiName == null
				|| this.datasourceJndiName.trim().length() == 0) {
			throw new MojoExecutionException(
					"No JNDI datasource name entered. Please use property datasourceJndiName to define.");
		}
		if (this.springJpaVendorAdapter == null
				|| this.springJpaVendorAdapter.trim().length() == 0) {
			throw new MojoExecutionException(
					"No spring JPA vendor adapter supplied. Please use property springJpaVendorAdapter which defaults to org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter.");
		}

		File f;
		StringBuffer sb = new StringBuffer(1024);

		// sb.append(this.getSpringPersistenceConfigXml());
		// File f = Util.getFile(this.outputDirectory,
		// new StringBuffer().append(this.basePackageName)
		// .append("-persistence-spring-config.xml").toString());
		// this.getLog().info(new StringBuffer().append("Write ")
		// .append(f.getAbsolutePath()));
		// try {
		// Util.writeToFile(f, sb.toString());
		// } catch (Exception e) {
		// this.getLog().error(e.getMessage());
		// e.printStackTrace();
		// }

		sb.setLength(0);
		sb.append(this.getSpringPersistenceJavaConfig());
		f = Util.getFile(this.outputDirectory,
				new StringBuffer().append(this.basePackageName)
						.append(".persistenceconfig").toString(),
				"PersistenceConfig.java");
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		sb.setLength(0);
		sb.append(this.getSpringDataSourceJavaConfig());
		f = Util.getFile(this.outputDirectory,
				new StringBuffer().append(this.basePackageName)
						.append(".persistenceconfig").toString(),
				"JndiDataSourceConfig.java");
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		this.getLog().debug("-execute");
	}

	private String getSpringDataSourceJavaConfig()
			throws MojoExecutionException {

		String prefix = Util.capitalizePackageName(this.basePackageName);
		StringBuffer sb = new StringBuffer(1024);

		sb.append("package ");
		sb.append(this.basePackageName);
		sb.append(".persistenceconfig;\n");
		sb.append("\n");

		sb.append("import javax.sql.DataSource;\n");
		sb.append("\n");
		sb.append("import org.springframework.context.annotation.Bean;\n");
		sb.append(
				"import org.springframework.context.annotation.Configuration;\n");
		sb.append(
				"import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;\n");
		sb.append("\n");

		sb.append("/**\n");
		sb.append(
				" * Provides the {@link DataSource} out of the JNDI context.\n");
		sb.append(" *\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("@Configuration\n");
		sb.append("public class JndiDataSourceConfig {\n");
		sb.append("\t/**\n");
		sb.append(
				"\t * Get the {@link DataSource} out of the JNDI naming context.\n");
		sb.append("\t *\n");
		sb.append(
				"\t * @return the {@link DataSource} out of the JNDI naming context.\n");
		sb.append("\t */\n");
		sb.append("\t@Bean(name = PersistenceConfig.DATASOURCE_BEAN_NAME)\n");
		sb.append("\tpublic DataSource getJndiDataSource() {\n");
		sb.append(
				"\t\tJndiDataSourceLookup dsLookup = new JndiDataSourceLookup();\n");
		sb.append("\t\tDataSource bean = dsLookup\n");
		sb.append(
				"\t\t\t.getDataSource(PersistenceConfig.DATASOURCE_JDNI_NAME);\n");
		sb.append("\t\treturn bean;\n");
		sb.append("\t}\n");
		sb.append("}\n");

		return sb.toString();

	}

	private String getSpringPersistenceJavaConfig()
			throws MojoExecutionException {

		String prefix = Util.capitalizePackageName(this.basePackageName);
		StringBuffer sb = new StringBuffer(1024);

		sb.append("package ");
		sb.append(this.basePackageName);
		sb.append(".persistenceconfig;\n");
		sb.append("\n");
		sb.append("import javax.persistence.EntityManagerFactory;\n");
		sb.append("import javax.sql.DataSource;\n");
		sb.append("\n");
		sb.append(
				"import org.springframework.beans.factory.annotation.Autowired;\n");
		sb.append(
				"import org.springframework.beans.factory.annotation.Qualifier;\n");
		sb.append("import org.springframework.context.annotation.Bean;\n");
		sb.append(
				"import org.springframework.context.annotation.Configuration;\n");
		sb.append(
				"import org.springframework.jdbc.datasource.DriverManagerDataSource;\n");
		sb.append(
				"import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;\n");
		sb.append(
				"import org.springframework.orm.jpa.JpaTransactionManager;\n");
		sb.append(
				"import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;\n");
		sb.append(
				"import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;\n");
		sb.append("import org.springframework.orm.jpa.vendor.Database;\n");
		sb.append(
				"import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;\n");
		sb.append(
				"import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;\n");
		sb.append(
				"import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;\n");
		sb.append(
				"import org.springframework.transaction.annotation.EnableTransactionManagement;\n");
		sb.append("\n");
		sb.append(
				"import com.qpark.eip.core.EipJpaVendorAdapterConfiguration;\n");
		sb.append(
				"import com.qpark.eip.core.persistence.AsyncDatabaseOperationPoolProvider;\n");
		sb.append("import com.qpark.eip.core.persistence.JAXBStrategySetup;\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * Provides the spring config of persistence unit <i>");
		sb.append(this.persistenceUnitName);
		sb.append("</i>.\n");
		sb.append(" *\n");
		sb.append(" * <pre>");
		sb.append(Util.getGeneratedAt());
		sb.append("</pre>\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("@Configuration\n");
		sb.append("@EnableTransactionManagement\n");
		sb.append("public class PersistenceConfig {\n");

		sb.append("\t/** The name of the {@link DataSource} bean. */\n");
		sb.append("\tpublic static final String DATASOURCE_BEAN_NAME = \"");
		sb.append(prefix);
		sb.append("DataSource\";\n");

		sb.append("\t/** The JNDI name of the {@link DataSource}. */\n");
		sb.append(
				"\tpublic static final String DATASOURCE_JDNI_NAME = \"jdbc/");
		sb.append(this.datasourceJndiName);
		sb.append("\";\n");

		sb.append("\t/** The name of the persistence unit. */\n");
		sb.append("\tpublic static final String PERSISTENCE_UNIT_NAME = \"");
		sb.append(this.persistenceUnitName);
		sb.append("\";\n");

		sb.append("\t/** The name of the entity manager factory. */\n");
		sb.append(
				"\tpublic static final String ENTITY_MANAGER_FACTORY_NAME = \"");
		sb.append(prefix);
		sb.append("EntityManagerFactory\";\n");

		sb.append(
				"\t/** The name of the persistence units transaction manager. */\n");
		sb.append("\tpublic static final String TRANSACTION_MANAGER_NAME = \"");
		sb.append(prefix);
		sb.append("TransactionManager\";\n");

		sb.append(
				"\t/** The name of the {@link EipJpaVendorAdapterConfiguration} bean name. */\n");
		sb.append(
				"\tpublic static final String JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME = \"");
		sb.append(prefix);
		sb.append("JpaVendorAdapterConfiguration\";\n");
		sb.append("\n");

		sb.append(
				"\t/** The {@link EipJpaVendorAdapterConfiguration} to use. */\n");
		sb.append("\t@Autowired\n");
		sb.append("\t@Qualifier(JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME)\n");
		sb.append(
				"\tprivate EipJpaVendorAdapterConfiguration jpaVendorAdapterConfiguration;\n");

		sb.append(
				"\t/** The {@link DataSource} with name {@value #DATASOURCE_BEAN_NAME}.*/\n");
		sb.append("\t@Autowired\n");
		sb.append("\t@Qualifier(DATASOURCE_BEAN_NAME)\n");
		sb.append("\tprivate DataSource datasource;\n");

		sb.append("\n");
		sb.append(
				"\t/** Create the spring config of {@value #PERSISTENCE_UNIT_NAME}. */\n");
		sb.append("\tpublic PersistenceConfig() {\n");
		sb.append("\t\tJAXBStrategySetup.setup();\n");
		sb.append("\t}\n");
		sb.append("\n");

		sb.append("\t/**\n");
		sb.append(
				"\t * Get the {@link LocalContainerEntityManagerFactoryBean}.\n");
		sb.append("\t *\n");
		sb.append(
				"\t * @return the {@link LocalContainerEntityManagerFactoryBean}.\n");
		sb.append("\t */\n");
		sb.append("\t@Bean(name = ENTITY_MANAGER_FACTORY_NAME)\n");
		sb.append(
				"\tpublic EntityManagerFactory getEntityManagerFactory() {\n");
		sb.append(
				"\t\tAbstractJpaVendorAdapter jpaVendorAdapter = this.getJpaVendorAdapter();\n");
		sb.append("\t\tif (jpaVendorAdapter == null) {\n");
		sb.append("\t\t\tthrow new RuntimeException(\n");
		sb.append(
				"\t\t\t\tString.format(\"%s jpaVendorAdpater not set properly %s.\",\n");
		sb.append("\t\t\t\t\tENTITY_MANAGER_FACTORY_NAME,\n");
		sb.append("\t\t\t\t\tString.valueOf(jpaVendorAdapter)));\n");
		sb.append("\t\t}\n");
		sb.append("\n");

		sb.append(
				"\t\tString jpaVendorAdapterDatabasePlatform = this.jpaVendorAdapterConfiguration\n");
		sb.append("\t\t\t.getJpaVendorAdpaterDatabasePlatform();\n");
		sb.append("\t\tif (jpaVendorAdapterDatabasePlatform == null\n");
		sb.append(
				"\t\t\t|| jpaVendorAdapterDatabasePlatform.trim().length() == 0) {\n");
		sb.append("\t\t\tthrow new RuntimeException(String.format(\n");
		sb.append(
				"\t\t\t\t\"%s jpaVendorAdpaterDatabasePlatform not set properly %s.\",\n");
		sb.append("\t\t\t\tENTITY_MANAGER_FACTORY_NAME,\n");
		sb.append(
				"\t\t\t\tString.valueOf(jpaVendorAdapterDatabasePlatform)));\n");
		sb.append("\t\t}\n");

		sb.append("\n");
		sb.append(
				"\t\tLocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();\n");
		sb.append("\t\tbean.setPersistenceXmlLocation(new StringBuffer(96)\n");
		sb.append("\t\t\t\t.append(\"classpath:/META-INF/\")\n");
		sb.append("\t\t\t\t.append(PERSISTENCE_UNIT_NAME)\n");
		sb.append("\t\t\t\t.append(\"/persistence.xml\")\n");
		sb.append("\t\t\t\t\t.toString());\n");
		sb.append("\t\tbean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);\n");

		sb.append("\t\tbean.setDataSource(this.datasource);\n");
		sb.append("\n");

		sb.append(
				"\t\tjpaVendorAdapter.setDatabasePlatform(jpaVendorAdapterDatabasePlatform);\n");
		sb.append("\t\tjpaVendorAdapter.setShowSql(false);\n");
		sb.append("\t\tif (this.isJpaVendorAdapterGenerateDdl()) {\n");
		sb.append("\t\tjpaVendorAdapter.setGenerateDdl(true);\n");
		sb.append(
				"\t\tif (HibernateJpaVendorAdapter.class.isInstance(jpaVendorAdapter)) {\n");
		sb.append(
				"\t\tbean.getJpaPropertyMap().put(\"hibernate.hbm2ddl.auto\",\"update\");\n");
		sb.append("\t\t}\n");
		sb.append("\t\t} else {\n");
		sb.append("\t\tjpaVendorAdapter.setGenerateDdl(false);\n");
		sb.append("\t\t}\n");
		sb.append("\n");

		sb.append("\t\tbean.setJpaVendorAdapter(jpaVendorAdapter);\n");
		sb.append("\t\tbean.afterPropertiesSet();\n");
		sb.append("\t\treturn bean.getObject();\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Get the {@link JpaTransactionManager}.\n");
		sb.append("\t *\n");
		sb.append("\t * @return the {@link JpaTransactionManager}.\n");
		sb.append("\t */\n");
		sb.append("\t@Bean(name = TRANSACTION_MANAGER_NAME)\n");
		sb.append(
				"\tpublic JpaTransactionManager getJpaTransactionManager() {\n");
		sb.append(
				"\t\tJpaTransactionManager bean = new JpaTransactionManager();\n");
		sb.append("\t\tEntityManagerFactory emf = this\n");
		sb.append("\t\t\t\t.getEntityManagerFactory();\n");
		sb.append("\t\tbean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);\n");
		sb.append("\t\tbean.setEntityManagerFactory(emf);\n");
		sb.append("\t\treturn bean;\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append(
				"\t * Get the {@link AbstractJpaVendorAdapter} out of the property\n");
		sb.append("\t * {@link #jpaVendorAdapterClassName}.\n");
		sb.append("\t * \n");
		sb.append("\t * @return the {@link AbstractJpaVendorAdapter} .\n");
		sb.append("\t */\n");
		sb.append(
				"\tprivate AbstractJpaVendorAdapter getJpaVendorAdapter() {\n");
		sb.append("\t\tAbstractJpaVendorAdapter jpaVendorAdapter = null;\n");
		sb.append(
				"\t\tString jpaVendorAdapterClassName = this.jpaVendorAdapterConfiguration\n");
		sb.append("\t\t\t\t.getJpaVendorAdapterClassName();\n");
		sb.append("\t\tif (jpaVendorAdapterClassName == null\n");
		sb.append("\t\t\t\t|| jpaVendorAdapterClassName\n");
		sb.append(
				"\t\t\t\t\t\t.equals(\"org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter\")\n");
		sb.append("\t\t\t\t|| jpaVendorAdapterClassName\n");
		sb.append("\t\t\t\t\t\t.equalsIgnoreCase(\"Hibernate\")) {\n");
		sb.append(
				"\t\t\tjpaVendorAdapter = new HibernateJpaVendorAdapter();\n");
		sb.append("\t\t} else if (jpaVendorAdapterClassName\n");
		sb.append(
				"\t\t\t\t.equals(\"org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter\")\n");
		sb.append("\t\t\t\t|| jpaVendorAdapterClassName\n");
		sb.append("\t\t\t\t\t\t.equalsIgnoreCase(\"EclipseLink\")) {\n");
		sb.append(
				"\t\t\tjpaVendorAdapter = new EclipseLinkJpaVendorAdapter();\n");
		sb.append("\t\t} else if (jpaVendorAdapterClassName\n");
		sb.append(
				"\t\t\t\t.equals(\"org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter\")\n");
		sb.append(
				"\t\t\t\t|| jpaVendorAdapterClassName.equalsIgnoreCase(\"OpenJpa\")) {\n");
		sb.append("\t\t\tjpaVendorAdapter = new OpenJpaVendorAdapter();\n");
		sb.append("\t\t}\n");
		sb.append("\t\treturn jpaVendorAdapter;\n");
		sb.append("\t}\n");
		sb.append("\n");

		sb.append("\t/**\n");
		sb.append("\t * Get the generateDdl parameter to be set into the\n");
		sb.append("\t * {@link AbstractJpaVendorAdapter}.\n");
		sb.append("\t *\n");
		sb.append(
				"\t * @return the generateDdl parameter to be set into the\n");
		sb.append("\t *         {@link AbstractJpaVendorAdapter}.\n");
		sb.append("\t */\n");
		sb.append("\tprivate boolean isJpaVendorAdapterGenerateDdl() {\n");
		sb.append("\t\tBoolean value = this.jpaVendorAdapterConfiguration\n");
		sb.append("\t\t\t\t.getJpaVendorAdapterGenerateDdl();\n");

		sb.append("\t\tif (value == null) {\n");
		sb.append("\t\t\tif (this.jpaVendorAdapterConfiguration\n");
		sb.append("\t\t\t\t\t.getJpaVendorAdpaterDatabasePlatform()\n");
		sb.append("\t\t\t\t\t.equals(\"org.hibernate.dialect.HSQLDialect\")\n");
		sb.append("\t\t\t\t\t|| this.jpaVendorAdapterConfiguration\n");
		sb.append("\t\t\t\t\t\t\t.getJpaVendorAdpaterDatabasePlatform()\n");
		sb.append("\t\t\t\t\t\t\t.equals(Database.HSQL.name())) {\n");
		sb.append("\t\t\t\tvalue = Boolean.TRUE;\n");
		sb.append("\t\t\t} else {\n");
		sb.append("\t\t\t\tvalue = Boolean.FALSE;\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t}\n");
		sb.append("\t\treturn value.booleanValue();\n");
		sb.append("\t}\n");
		sb.append("\n");

		sb.append("}\n");

		return sb.toString();
	}

	@SuppressWarnings("unused")
	private String getSpringPersistenceConfigXml()
			throws MojoExecutionException {
		StringBuffer sb = new StringBuffer(1024);

		String prefix = Util.capitalizePackageName(this.basePackageName);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append(
				"<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append(
				"\txmlns:util=\"http://www.springframework.org/schema/util\"\n");
		sb.append("\txmlns:tx=\"http://www.springframework.org/schema/tx\"\n");
		sb.append(
				"\txmlns:context=\"http://www.springframework.org/schema/context\"\n");
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
				"\t\thttp://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append(
				"\t\thttp://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n");
		sb.append(
				"\t\thttp://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context");
		if (springVersion != null) {
			sb.append("-").append(springVersion);
		}
		sb.append(".xsd\n\t\"\n>");
		sb.append("\n");
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("\t<!-- Properties -->\n");
		sb.append("\t<import resource=\"classpath:/");
		sb.append(this.basePackageName);
		sb.append(".properties-config.xml\" />\n");

		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append(
				"DataSourceJndi\" class=\"org.springframework.jndi.JndiObjectFactoryBean\">\n");
		sb.append(
				"\t\t<property name=\"jndiName\" value=\"java:comp/env/jdbc/");
		sb.append(this.datasourceJndiName);
		sb.append("\" />\n");
		sb.append("\t\t<property name=\"resourceRef\" value=\"true\" />\n");
		sb.append("\t\t<property name=\"defaultObject\" ref=\"");
		sb.append(prefix);
		sb.append("DataSourceJdbc\" />\n");
		sb.append("\t</bean>\n");

		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append(
				"DataSourceJdbc\" class=\"org.springframework.jdbc.datasource.DriverManagerDataSource\">\n");
		sb.append("\t\t<property name=\"driverClassName\" value=\"${");
		sb.append(prefix);
		sb.append(
				".jdbc.driverClassName:com.qpark.eip.core.DummyJdbcDriver}\" />\n");
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
		sb.append(
				"EntityManagerFactory\" class=\"org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean\">\n");
		sb.append(
				"\t\t<property name=\"persistenceXmlLocation\" value=\"classpath:/META-INF/");
		sb.append(this.persistenceUnitName);
		sb.append("/persistence.xml\" />\n");
		sb.append("\t\t<property name=\"persistenceUnitName\" value=\"");
		sb.append(this.persistenceUnitName);
		sb.append("\" />\n");
		sb.append("\t\t<property name=\"dataSource\" ref=\"");
		sb.append(prefix);
		sb.append("DataSourceJndi\"/>\n");
		sb.append("\t\t<property name=\"jpaVendorAdapter\">\n");

		sb.append("\t\t\t<bean class=\"");
		sb.append(this.springJpaVendorAdapter);
		sb.append("\">\n");
		sb.append("\t\t\t\t<property name=\"showSql\" value=\"false\" />\n");
		sb.append(
				"\t\t\t\t<property name=\"generateDdl\" value=\"false\" />\n");
		if (this.databasePlatform != null
				&& this.databasePlatform.trim().length() > 0) {
			sb.append("\t\t\t\t<property name=\"databasePlatform\" value=\"");
			sb.append(this.databasePlatform);
			sb.append("\" />\n");
		}
		sb.append("\t\t\t</bean>\n");

		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append("SessionFactory\" factory-bean=\"");
		sb.append(prefix);
		sb.append(
				"EntityManagerFactory\" factory-method=\"getSessionFactory\" />\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append(
				"TransactionManager\" class=\"org.springframework.orm.jpa.JpaTransactionManager\" >\n");
		sb.append("\t\t<property name=\"entityManagerFactory\" ref=\"");
		sb.append(prefix);
		sb.append("EntityManagerFactory\"/>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<tx:annotation-driven transaction-manager=\"");
		sb.append(prefix);
		sb.append("TransactionManager\" />\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append(
				"JmxExporter\" class=\"org.springframework.jmx.export.MBeanExporter\">\n");
		sb.append("\t\t<property name=\"beans\">\n");
		sb.append("\t\t\t<map>\n");
		sb.append(
				"\t\t\t\t<entry key=\"Hibernate:type=statistics,application=");
		sb.append(prefix);
		sb.append("-${eip.application.war.name:__WAR_NAME__}\" value-ref=\"");
		sb.append(prefix);
		sb.append("HibernateStatisticsBean\"/>\n");
		sb.append("\t\t\t</map>\n");
		sb.append("\t\t</property>\n");
		sb.append("\t</bean>\n");
		sb.append("\t<bean id=\"");
		sb.append(prefix);
		sb.append(
				"HibernateStatisticsBean\" class=\"org.hibernate.jmx.StatisticsService\">\n");
		sb.append(
				"\t\t<property name=\"statisticsEnabled\" value=\"true\"/>\n");
		sb.append(
				"\t\t<property name=\"sessionFactory\"><util:property-path path=\"");
		sb.append(prefix);
		sb.append("EntityManagerFactory.sessionFactory\" /></property>\n");
		sb.append("\t</bean>\n");
		sb.append("</beans>\n");
		return sb.toString();
	}
}
