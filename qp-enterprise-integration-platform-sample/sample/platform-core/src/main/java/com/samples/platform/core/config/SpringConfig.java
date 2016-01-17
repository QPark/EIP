/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig;
import com.samples.platform.persistenceconfig.JndiDataSourceConfig;
import com.samples.platform.persistenceconfig.PersistenceConfig;

/**
 * Provides the java spring config.
 *
 * @author bhausen
 */
@Configuration
@Import(value = { EipPersistenceConfig.class, EipStatisticsConfig.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringConfig {
    /** The {@link EipPersistenceConfig}. */
    @Autowired
    private EipPersistenceConfig eipPersistenceConfig;
    /** The {@link EipStatisticsConfig}. */
    @Autowired
    private EipStatisticsConfig eipStatisticsConfig;
    /**
     * The {@link ApplicationPlaceholderConfigurer} containing the configuration
     * properties.
     */
    @Autowired
    private ApplicationPlaceholderConfigurer properties;
    /** The {@link PersistenceConfig}. */
    @Autowired
    private PersistenceConfig persistenceConfig;

    /**
     * Get the {@link DataSource} out of the JNDI naming context.
     *
     * @return the {@link DataSource} out of the JNDI naming context.
     */
    @Bean(name = EipPersistenceConfig.DATASOURCE_BEAN_NAME)
    public DataSource getJndiDataSource() {
	JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	DataSource bean = dsLookup.getDataSource(
		this.properties.getProperty("com.samples.platform.jndi.datasource.name", "jdbc/PlatformDB"));
	return bean;
    }

    /**
     * Get the {@link JndiDataSourceConfig} to be used in the
     * {@link PersistenceConfig}.
     *
     * @return the {@link JndiDataSourceConfig} to be used in the
     *         {@link PersistenceConfig}.
     */
    @Bean
    public JndiDataSourceConfig getJndiDataSourceConfig() {
	JndiDataSourceConfig bean = new JndiDataSourceConfig();
	return bean;
    }

    /**
     * Get the {@link PersistenceConfig} defining the WEBAPP persistence unit.
     *
     * @return the {@link DataSource} defining the WEBAPP persistence unit.
     */
    @Bean
    public PersistenceConfig getPersistenceConfig() {
	PersistenceConfig bean = new PersistenceConfig();
	return bean;
    }

    /**
     * Set the JPA vendor adapter settings for {@link EipPersistenceConfig}.
     */
    @PostConstruct
    private void init() {
	this.persistenceConfig.setJpaVendorAdapterClassName(
		this.properties.getProperty("com.samples.platform.jpa.vendor.adapter.class.name"));
	this.persistenceConfig.setJpaVendorAdpaterDatabasePlatform(
		this.properties.getProperty("com.samples.platform.jpa.vendor.adapter.database.platform"));

	this.eipPersistenceConfig.setJpaVendorAdapterClassName(
		this.properties.getProperty("com.samples.platform.jpa.vendor.adapter.class.name"));
	this.eipPersistenceConfig.setJpaVendorAdpaterDatabasePlatform(
		this.properties.getProperty("com.samples.platform.jpa.vendor.adapter.database.platform"));

	this.eipStatisticsConfig
		.setContextName(this.properties.getProperty("eip.application.maven.artifact.artifactid"));
	this.eipStatisticsConfig
		.setContextVersion(this.properties.getProperty("eip.application.maven.artifact.version"));
	this.eipStatisticsConfig
		.setJpaVendorAdapterClassName("com.samples.platform.core.SamplesMessageContentProvider");
	this.eipStatisticsConfig.setJpaVendorAdpaterDatabasePlatform(
		this.properties.getProperty("com.samples.platform.jpa.vendor.adapter.database.platform"));
	this.eipStatisticsConfig.setNumberOfWeeksToKeepLogs(2);
    }

}
