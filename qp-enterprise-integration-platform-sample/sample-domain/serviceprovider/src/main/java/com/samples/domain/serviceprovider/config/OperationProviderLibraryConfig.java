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
import com.samples.domain.serviceprovider.OperationProviderLibrary;
import com.samples.domain.serviceprovider.impl.OperationProviderLibraryImpl;

/**
 * Config to use the {@link OperationProviderLibrary}.
 *
 * @author bhausen
 */
@Configuration
@ImportResource({

    "classpath:com.samples.bus.icd-jaxb2marshaller-spring-config.xml",
    "classpath:bus.library-integration-spring-config.xml"

})
public class OperationProviderLibraryConfig {
  /** @return the {@link OperationProviderLibrary}. */
  @Bean
  public static OperationProviderLibrary getOperationProviderLibrary() {
    return new OperationProviderLibraryImpl();
  }

  /**
   * @param defaultEipSecurmentPropertyProvider the {@link DefaultEipSecurmentPropertyProvider}.
   * @return the {@link EipWss4jSecurityInterceptor}.
   */
  @Bean(name = "eipCallerComSamplesBusLibraryWss4jSecurityInterceptor")
  public static EipWss4jSecurityInterceptor getOperationProviderLibraryEipWss4jSecurityInterceptor(
      final DefaultEipSecurmentPropertyProvider defaultEipSecurmentPropertyProvider) {
    EipWss4jSecurityInterceptor bean = new EipWss4jSecurityInterceptor();
    bean.setSecurementPropertyProvider(defaultEipSecurmentPropertyProvider);
    return bean;
  }
}
