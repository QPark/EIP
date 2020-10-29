/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider;

/**
 * Reads user credential to call other services out of the database.
 *
 * @author bhausen
 */
public class DatabaseSecurementProvider extends DefaultEipSecurmentPropertyProvider {
  /** The {@link DatabaseUserProvider}. */
  @Autowired
  private DatabaseUserProvider databaseUserProvider;

  /**
   * @see com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider#setSecurementPassword(java.lang.String)
   */
  @Override
  public void setSecurementPassword(final String securementPassword) {
    throw new IllegalArgumentException(
        "In DatabaseSecurementProvider the password is read out of the database.");
  }

  /**
   * @see com.qpark.eip.core.spring.security.DefaultEipSecurmentPropertyProvider#getSecurementPassword()
   */
  @Override
  public String getSecurementPassword() {
    return Optional.ofNullable(this.databaseUserProvider.getUser(this.getSecurementUsername()))
        .map(user -> user.getPassword()).orElse(null);
  }
}
