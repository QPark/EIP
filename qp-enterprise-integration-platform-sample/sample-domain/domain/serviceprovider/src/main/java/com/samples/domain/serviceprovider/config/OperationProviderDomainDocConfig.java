/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider;
import com.qpark.eip.core.spring.security.EipWss4jSecurityInterceptor;
import com.samples.domain.serviceprovider.OperationProviderDomainDoc;
import com.samples.domain.serviceprovider.impl.OperationProviderDomainDocImpl;

/**
 * Config to use the {@link OperationProviderDomainDoc}.
 *
 * @author bhausen
 */
@Configuration
@ImportResource({

		"classpath:com.samples.bus.icd-jaxb2marshaller-spring-config.xml",
		"classpath:bus.domain.doc-integration-spring-config.xml"

})
public class OperationProviderDomainDocConfig {
	/** @return the {@link OperationProviderDomainDoc}. */
	@Bean
	public static OperationProviderDomainDoc getOperationProviderDomainDoc() {
		return new OperationProviderDomainDocImpl();
	}

	/**
	 * @param defaultEipSecurmentPropertyProvider
	 *                                                the
	 *                                                {@link DefaultEipSecurmentPropertyProvider}.
	 * @return the {@link EipWss4jSecurityInterceptor}.
	 */
	@Bean(name = "eipCallerComSamplesBusDomainDocWss4jSecurityInterceptor")
	public static EipWss4jSecurityInterceptor getOperationProviderDomainDocEipWss4jSecurityInterceptor(
			final DefaultEipSecurmentPropertyProvider defaultEipSecurmentPropertyProvider) {
		EipWss4jSecurityInterceptor bean = new EipWss4jSecurityInterceptor();
		bean.setSecurementPropertyProvider(defaultEipSecurmentPropertyProvider);
		return bean;
	}
}
