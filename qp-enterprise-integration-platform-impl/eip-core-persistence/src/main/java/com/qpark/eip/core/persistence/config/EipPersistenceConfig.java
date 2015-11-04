package com.qpark.eip.core.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.qpark.eip.core.persistence.AsyncDatabaseOperationPoolProvider;

/**
 * Provides the spring config of the eip persistence authority.
 * @author bhausen
 */
@Configuration
@ImportResource(value = { "classpath:com.qpark.eip.core-persistence-spring-config.xml" })
public class EipPersistenceConfig {
	/**
	 * Create the spring config of the eip persistence authority.
	 * @param context the application scope.
	 */
	public EipPersistenceConfig() {
	}

	/**
	 * Get the {@link AsyncDatabaseOperationPoolProvider} bean.
	 * @return the {@link AsyncDatabaseOperationPoolProvider} bean.
	 */
	@Bean(name = "com.qpark.eip.core.persistence.AsyncDatabaseOperationPoolProvider")
	public AsyncDatabaseOperationPoolProvider getAsyncDatabaseOperationPoolProvider() {
		AsyncDatabaseOperationPoolProvider bean = new AsyncDatabaseOperationPoolProvider();
		return bean;
	}

	/**
	 * The {@link EipPersistenceConfig} itself.
	 * @return the {@link EipPersistenceConfig}.
	 */
	@Bean(name = "com.qpark.eip.core.persistence.config.EipPersistenceConfig")
	public EipPersistenceConfig getEipPersistenceConfig() {
		return this;
	}
}
