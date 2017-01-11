/*******************************************************************************
 * Copyright (c) 2013 - 2017 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.lockedoperation;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.qpark.eip.core.EipJpaVendorAdapterConfiguration;
import com.qpark.eip.core.spring.lockedoperation.config.EipLockedoperationConfig;

/**
 * Test {@link Configuration}
 *
 * @author bhausen
 */
@Configuration
@Import(value = { EipLockedoperationConfig.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class TestConfig {
	/**
	 * @return the {@link LockableOperationTestAsync}.
	 */
	@Bean
	public LockableOperationTestAsync LockableOperationTestAsync() {
		return new LockableOperationTestAsync();
	}

	/**
	 * @return the {@link LockableOperationTestSync}.
	 */
	@Bean
	public LockableOperationTestSync LockableOperationTestSync() {
		return new LockableOperationTestSync();
	}

	/**
	 * Get the {@link DataSource} auto wired in the
	 * {@link EipLockedoperationConfig}.
	 *
	 * @return The {@link DataSource}
	 */
	@Bean(name = EipLockedoperationConfig.DATASOURCE_BEAN_NAME)
	public DataSource DataSource() {
		DriverManagerDataSource bean = new DriverManagerDataSource();
		bean.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		bean.setUrl("jdbc:hsqldb:file:target/hsqldb/testHSQLDB.bin");
		bean.setUsername("platformUser");
		bean.setPassword("platformUserPwd");
		return bean;
	}

	/**
	 * Set the {@link EipJpaVendorAdapterConfiguration} of
	 * {@link EipLockedoperationConfig}.
	 *
	 * @return the {@link EipJpaVendorAdapterConfiguration} of
	 *         {@link EipLockedoperationConfig}.
	 */
	@Bean(name = EipLockedoperationConfig.JPA_VENDOR_ADAPTER_CONFIGURATION_BEAN_NAME)
	public EipJpaVendorAdapterConfiguration EipJpaVendorAdapterConfiguration() {
		EipJpaVendorAdapterConfiguration bean = new EipJpaVendorAdapterConfiguration();
		bean.setJpaVendorAdapterClassName(
				"org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter");
		bean.setJpaVendorAdpaterDatabasePlatform(
				"org.hibernate.dialect.HSQLDialect");

		bean.setJpaVendorAdapterGenerateDdl(true);
		return bean;
	}

}