/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider.config;

import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider;
import com.samples.domain.serviceprovider.AppSecurityContextHandler;

/**
 * The abstract part of the operation provider configuration.
 *
 * @author bhausen
 */
public interface AbstractCoreOperationProviderSpringConfig
    extends ApplicationListener<ContextRefreshedEvent> {
  /**
   * @param applicationProperties the {@link ApplicationPlaceholderConfigurer}.
   * @return the {@link DefaultEipSecurmentPropertyProvider} to support the web service calls.
   */
  DefaultEipSecurmentPropertyProvider getDefaultEipSecurmentPropertyProvider(
      ApplicationPlaceholderConfigurer applicationProperties);

  /** @return the {@link Logger}. */
  Logger getLogger();

  /** @return the {@link AppSecurityContextHandler}. */
  AppSecurityContextHandler getSetAppSecurityContextAuthentication();

  /**
   * @return the {@link SaajSoapMessageFactory} with name <code>soap12MessageFactory</code>
   *         supporting soap messages version 1.2.
   */
  SaajSoapMessageFactory getSoap12MessageFactory();

  /**
   * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
   */
  @Override
  default void onApplicationEvent(final ContextRefreshedEvent event) {
    this.getLogger().info("ContextRefreshedEvent executed");
  }
}
