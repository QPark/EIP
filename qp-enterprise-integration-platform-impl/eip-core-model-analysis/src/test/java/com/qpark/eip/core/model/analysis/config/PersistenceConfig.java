package com.qpark.eip.core.model.analysis.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.qpark.eip.core.persistence.JAXBStrategySetup;

/**
 * Provides the spring config of persistence unit
 * {@value #PERSISTENCE_UNIT_NAME}.
 *
 * @author bhausen
 */
@Configuration
@EnableTransactionManagement
public class PersistenceConfig {
	/** The name of the {@link DataSource} bean. */
	public static final String DATASOURCE_BEAN_NAME = "ComQparkEipModelAnalysisDataSource";
	/** The JNDI name of the {@link DataSource}. */
	public static final String DATASOURCE_JDNI_NAME = "jdbc/ComQparkEipModelAnalysisDB";
	/** The name of the persistence unit. */
	public static final String PERSISTENCE_UNIT_NAME = "com.qpark.eip.model.docmodel";
	/** The name of the persistence units transaction manager. */
	public static final String TRANSACTION_MANAGER_NAME = "ComQparkEipModelAnalysisTransactionManager";
	/**
	 * The default value of the jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 */
	public static final String DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME = "org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter";
	/**
	 * The default value of the database platform to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 */
	public static final Boolean DEFAULT_JPA_VENDOR_ADPATER_GENERATE_DDL = Boolean.FALSE;
	/**
	 * The default value of the database platform to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 */
	public static final String DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM = "org.hibernate.dialect.Oracle10gDialect";
	/**
	 * The jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 */
	private String jpaVendorAdapterClassName = DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME;

	public String getJpaVendorAdapterClassName() {
		return this.jpaVendorAdapterClassName;
	}

	/**
	 * The generateDdl parameter to be set into the
	 * {@link AbstractJpaVendorAdapter}. Defaults to <code>false</code> if not
	 * set and {@link #jpaVendorAdpaterDatabasePlatform} not equals to
	 * {@link org.hibernate.dialect.HSQLDialect} and not equals
	 * {@link org.springframework.orm.jpa.vendor.Database#HSQL}.
	 */
	private Boolean jpaVendorAdapterGenerateDdl = null;
	/**
	 * The database platform to be set into the {@link AbstractJpaVendorAdapter}
	 * .
	 */
	private String jpaVendorAdpaterDatabasePlatform = DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM;
	/** The {@link DataSource} with name {@value #DATASOURCE_BEAN_NAME}. */
	@Autowired
	@Qualifier(DATASOURCE_BEAN_NAME)
	private DataSource datasource;

	/**
	 * Create the spring config of persistence unit
	 * {@value #PERSISTENCE_UNIT_NAME}.
	 */
	public PersistenceConfig() {
		this(DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME,
				DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM);
		// this("Hibernate", "org.hibernate.dialect.HSQLDialect");
	}

	/**
	 * Create the spring config of persistence unit
	 * {@value #PERSISTENCE_UNIT_NAME}.
	 *
	 * @param jpaVendorAdapterClassName
	 *            the jpa Vendor adapter class name to be set in the
	 *            {@link LocalContainerEntityManagerFactoryBean}.
	 * @param jpaVendorAdpaterDatabasePlatform
	 *            the database platform to be set into the
	 *            {@link AbstractJpaVendorAdapter}.
	 */
	public PersistenceConfig(final String jpaVendorAdapterClassName,
			final String jpaVendorAdpaterDatabasePlatform) {
		JAXBStrategySetup.setup();
		this.jpaVendorAdapterClassName = jpaVendorAdapterClassName;
		this.jpaVendorAdpaterDatabasePlatform = jpaVendorAdpaterDatabasePlatform;
	}

	/**
	 * Get the {@link LocalContainerEntityManagerFactoryBean}.
	 *
	 * @return the {@link LocalContainerEntityManagerFactoryBean}.
	 */
	@Bean(name = "ComQparkEipModelAnalysisEntityManagerFactory")
	public EntityManagerFactory getEntityManagerFactory() {
		AbstractJpaVendorAdapter jpaVendorAdapter = this.getJpaVendorAdapter();
		if (jpaVendorAdapter == null) {
			throw new RuntimeException(
					"ComQparkEipModelAnalysisEntityManagerFactory jpaVendorAdpater not set properly "
							+ String.valueOf(jpaVendorAdapter) + ".");
		}

		if (this.jpaVendorAdpaterDatabasePlatform == null
				|| this.jpaVendorAdpaterDatabasePlatform.trim().length() == 0) {
			throw new RuntimeException(
					"ComQparkEipModelAnalysisEntityManagerFactory jpaVendorAdpaterDatabasePlatform not set properly "
							+ String.valueOf(
									this.jpaVendorAdpaterDatabasePlatform)
							+ ".");
		}

		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setPersistenceXmlLocation(
				new StringBuffer(96).append("classpath:/META-INF/")
						// .append(PERSISTENCE_UNIT_NAME)
						.append("/persistence.xml").toString());
		bean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
		bean.setDataSource(this.datasource);

		jpaVendorAdapter.setShowSql(false);
		jpaVendorAdapter.setGenerateDdl(this.isJpaVendorAdapterGenerateDdl());
		jpaVendorAdapter
				.setDatabasePlatform(this.jpaVendorAdpaterDatabasePlatform);
		bean.setJpaVendorAdapter(jpaVendorAdapter);
		bean.afterPropertiesSet();
		return bean.getObject();
	}

	/**
	 * Get the {@link JpaTransactionManager}.
	 *
	 * @return the {@link JpaTransactionManager}.
	 */
	@Bean(name = TRANSACTION_MANAGER_NAME)
	public JpaTransactionManager getJpaTransactionManager() {
		JpaTransactionManager bean = new JpaTransactionManager();
		EntityManagerFactory emf = this.getEntityManagerFactory();
		bean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
		bean.setEntityManagerFactory(emf);
		return bean;
	}

	/**
	 * Get the {@link AbstractJpaVendorAdapter} out of the property
	 * {@link #jpaVendorAdapterClassName}.
	 *
	 * @return the {@link AbstractJpaVendorAdapter} .
	 */
	private AbstractJpaVendorAdapter getJpaVendorAdapter() {
		AbstractJpaVendorAdapter jpaVendorAdapter = null;
		if (this.jpaVendorAdapterClassName == null
				|| this.jpaVendorAdapterClassName
						.equals("org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter")
				|| this.jpaVendorAdapterClassName
						.equalsIgnoreCase("Hibernate")) {
			jpaVendorAdapter = new HibernateJpaVendorAdapter();
		} else if (this.jpaVendorAdapterClassName
				.equals("org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter")
				|| this.jpaVendorAdapterClassName
						.equalsIgnoreCase("EclipseLink")) {
			jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
		} else if (this.jpaVendorAdapterClassName
				.equals("org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter")
				|| this.jpaVendorAdapterClassName.equalsIgnoreCase("OpenJpa")) {
			jpaVendorAdapter = new OpenJpaVendorAdapter();
		}
		return jpaVendorAdapter;
	}

	/**
	 * Get the generateDdl parameter to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 *
	 * @return the generateDdl parameter to be set into the
	 *         {@link AbstractJpaVendorAdapter}.
	 */
	private boolean isJpaVendorAdapterGenerateDdl() {
		boolean value = DEFAULT_JPA_VENDOR_ADPATER_GENERATE_DDL.booleanValue();
		if (this.jpaVendorAdapterGenerateDdl != null) {
			value = this.jpaVendorAdapterGenerateDdl.booleanValue();
		} else {
			if (this.jpaVendorAdpaterDatabasePlatform
					.equals("org.hibernate.dialect.HSQLDialect")
					|| this.jpaVendorAdpaterDatabasePlatform
							.equals(Database.HSQL.name())) {
				value = true;
			}
		}
		return value;
	}

	/**
	 * Set the jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 *
	 * @param jpaVendorAdapterClassName
	 *            the jpa Vendor adapter class name to be set in the
	 *            {@link LocalContainerEntityManagerFactoryBean}.
	 */
	public void setJpaVendorAdapterClassName(
			final String jpaVendorAdapterClassName) {
		this.jpaVendorAdapterClassName = jpaVendorAdapterClassName;
	}

	/**
	 * Set the database platform to be set into the
	 * {@link AbstractJpaVendorAdapter}.
	 *
	 * @param jpaVendorAdpaterDatabasePlatform
	 *            the database platform to be set into the
	 *            {@link AbstractJpaVendorAdapter}.
	 */
	public void setJpaVendorAdpaterDatabasePlatform(
			final String jpaVendorAdpaterDatabasePlatform) {
		this.jpaVendorAdpaterDatabasePlatform = jpaVendorAdpaterDatabasePlatform;
	}
}
