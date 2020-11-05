/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider;

/**
 * @author bhausen
 */
public interface AppSecurityContextHandler {
  /** Clears the authentication again. */
  void clearBusSecurityContextAuthentication();

  /** Initialize. */
  void init();

  /**
   * Set the authentication into the security context if needed.
   *
   * @return set or not.
   */
  boolean setAppSecurityContextAuthentication();

  /**
   * Set the property name of containing the name of the webapp system user.
   *
   * @param propertyNameUserWebapp the property name of containing the name of the webapp system
   *        user.
   */
  void setPropertyNameUserWebapp(String propertyNameUserWebapp);
}
