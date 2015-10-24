package com.qpark.eip.core.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.qpark.eip.core.persistence.AsyncDatabaseOperationPoolProvider;

/**
 * Provides the spring config of the bus.util authority.
 * @author bhausen
 */
@Configuration
@ImportResource(value = { "classpath:com.ses.osp.bus.util-persistence-spring-config.xml" })
public class BusUtilDatabaseConfig {
	/**
	 * Create the spring config of the bus.util authority.
	 * @param context the application scope.
	 */
	public BusUtilDatabaseConfig() {
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
	 * The {@link BusUtilDatabaseConfig} itself.
	 * @return the {@link BusUtilDatabaseConfig}.
	 */
	@Bean(name = "com.qpark.eip.core.persistence.config.BusUtilDatabaseConfig")
	public BusUtilDatabaseConfig getBusUtilAuthorityConfig() {
		return this;
	}
}
