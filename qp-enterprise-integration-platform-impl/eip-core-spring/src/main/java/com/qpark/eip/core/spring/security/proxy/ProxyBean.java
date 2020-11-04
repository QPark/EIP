/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security.proxy;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.security.EipJasyptEncryptionProvider;

/**
 * This class is meant to store and reuse the proxy configuration.
 *
 * @author fdewasm
 *
 */
public final class ProxyBean {
  /** the proxy server host IP or name. */
  private String proxyHost;
  /** the proxy server port. */
  private String proxyPort;
  /** the username to connect to proxy server. */
  private String username;
  /** the password to connect to proxy server. */
  private String password;
  /** The {@link ApplicationPlaceholderConfigurer} if available. */
  @Autowired(required = false)
  private ApplicationPlaceholderConfigurer properties;

  /**
   * gets the password used to connect to proxy server.
   *
   * @return the password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * gets the proxy host name or IP address.
   *
   * @return proxy host name or IP address as a String
   */
  public String getProxyHost() {
    return this.proxyHost;
  }

  /**
   * gets the proxy port.
   *
   * @return proxy port as a String
   */
  public String getProxyPort() {
    return this.proxyPort;
  }

  /**
   * gets the username used to connect to proxy server.
   *
   * @return the username.
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * sets the password that will be used to connect to the proxy server.
   *
   * @param aPassword
   *            the password.
   */
  public void setPassword(final String aPassword) {
    this.password = Optional.ofNullable(aPassword)
        .map(p -> EipJasyptEncryptionProvider.decrypt(p, this.properties)).orElse(aPassword);
  }

  /**
   * sets the proxy host name or IP address.
   *
   * @param aProxyHost
   *            proxy host name or IP address as a String
   */
  public void setProxyHost(final String aProxyHost) {
    this.proxyHost = aProxyHost;
  }

  /**
   * sets the proxy port.
   *
   * @param aProxyPort
   *            proxy port a String
   */
  public void setProxyPort(final String aProxyPort) {
    this.proxyPort = aProxyPort;
  }

  /**
   * sets the username that will be used to connect to the proxy server.
   *
   * @param aUsername
   *            the username.
   */
  public void setUsername(final String aUsername) {
    this.username = aUsername;
  }
}
