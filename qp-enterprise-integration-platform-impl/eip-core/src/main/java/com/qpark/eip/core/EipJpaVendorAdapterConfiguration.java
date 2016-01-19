package com.qpark.eip.core;

/**
 * Configuration settings for
 * {@link org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter}.
 *
 * @author bhausen
 */
public class EipJpaVendorAdapterConfiguration {
	/**
	 * The default value of the JPA Vendor adapter class name to be set in the
	 * {@link org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean}
	 * .
	 */
	public static final String DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME = "org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter";
	/**
	 * The default value of the database platform to be set into the
	 * {@link org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter}.
	 */
	public static final String DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM = "org.hibernate.dialect.Oracle10gDialect";
	/**
	 * The default value of the database platform to be set into the
	 * {@link org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter}.
	 */
	public static final Boolean DEFAULT_JPA_VENDOR_ADPATER_GENERATE_DDL = Boolean.FALSE;

	/**
	 * @return the jpaVendorAdapterClassName
	 */
	public String getJpaVendorAdapterClassName() {
		return this.jpaVendorAdapterClassName;
	}

	/**
	 * @return the jpaVendorAdapterGenerateDdl
	 */
	public Boolean getJpaVendorAdapterGenerateDdl() {
		return this.jpaVendorAdapterGenerateDdl;
	}

	/**
	 * @return the jpaVendorAdpaterDatabasePlatform
	 */
	public String getJpaVendorAdpaterDatabasePlatform() {
		return this.jpaVendorAdpaterDatabasePlatform;
	}

	/**
	 * @param jpaVendorAdapterClassName
	 *            the jpaVendorAdapterClassName to set
	 */
	public void setJpaVendorAdapterClassName(
			final String jpaVendorAdapterClassName) {
		this.jpaVendorAdapterClassName = jpaVendorAdapterClassName;
	}

	/**
	 * @param jpaVendorAdapterGenerateDdl
	 *            the jpaVendorAdapterGenerateDdl to set
	 */
	public void setJpaVendorAdapterGenerateDdl(
			final Boolean jpaVendorAdapterGenerateDdl) {
		this.jpaVendorAdapterGenerateDdl = jpaVendorAdapterGenerateDdl;
	}

	/**
	 * @param jpaVendorAdpaterDatabasePlatform
	 *            the jpaVendorAdpaterDatabasePlatform to set
	 */
	public void setJpaVendorAdpaterDatabasePlatform(
			final String jpaVendorAdpaterDatabasePlatform) {
		this.jpaVendorAdpaterDatabasePlatform = jpaVendorAdpaterDatabasePlatform;
	}

	/**
	 * The jpa Vendor adapter class name to be set in the
	 * {@link LocalContainerEntityManagerFactoryBean}.
	 */
	private String jpaVendorAdapterClassName = DEFAULT_JPA_VENDOR_ADAPTER_CLASSNAME;

	/**
	 * The generateDdl parameter to be set into the
	 * {@link AbstractJpaVendorAdapter}. Defaults to <code>false</code> if not
	 * set and {@link #jpaVendorAdpaterDatabasePlatform} not equals to
	 * {@link org.hibernate.dialect.HSQLDialect} and not equals
	 * {@link org.springframework.orm.jpa.vendor.Database#HSQL}.
	 */
	private Boolean jpaVendorAdapterGenerateDdl = DEFAULT_JPA_VENDOR_ADPATER_GENERATE_DDL;

	/**
	 * The database platform to be set into the {@link AbstractJpaVendorAdapter}
	 * .
	 */
	private String jpaVendorAdpaterDatabasePlatform = DEFAULT_JPA_VENDOR_ADPATER_DATABASEPLATFORM;

}
