/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import java.util.Optional;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.StrongTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;

/**
 * @author bhausen
 */
public class DefaultEipSecurmentPropertyProvider implements SecurmentPropertyProvider {
  /** The {@link org.slf4j.Logger}. */
  private Logger logger = LoggerFactory.getLogger(DefaultEipSecurmentPropertyProvider.class);
  private String securementPassword;
  private String securementUsername;
  /** The {@link ApplicationPlaceholderConfigurer}. */
  @Autowired
  private ApplicationPlaceholderConfigurer properties;


  /**
   * @return <code>UsernameToken</code>
   * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementActions()
   */
  @Override
  public String getSecurementActions() {
    return "UsernameToken";
  }

  /**
   * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementPassword()
   */
  @Override
  public String getSecurementPassword() {
    return this.securementPassword;
  }

  /**
   * @return <code>PasswordDigest</code>
   * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementPasswordType()
   */
  @Override
  public String getSecurementPasswordType() {
    return "PasswordDigest";
  }

  /**
   * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementUsername()
   */
  @Override
  public String getSecurementUsername() {
    return this.securementUsername;
  }

  /**
   * @return <code>Nonce Created</code>
   * @see com.qpark.eip.core.spring.security.SecurmentPropertyProvider#getSecurementUsernameTokenElements()
   */
  @Override
  public String getSecurementUsernameTokenElements() {
    return "Nonce Created";
  }

  /**
   * @param securementPassword
   *            the securementPassword to set
   */
  public void setSecurementPassword(final String securementPassword) {
    this.securementPassword = Optional.ofNullable(securementPassword).map(pwd -> {
      String pwdEncrypted = null;
      if (pwd.trim().startsWith("ENC(") && pwd.trim().endsWith(")")) {
        this.logger.debug("setSecurementPassword: Try password decrpytion of user '{}' '{}'",
            this.securementUsername, pwd);
        final StrongTextEncryptor encryptor =
            EipJasyptEncryptionProvider.getEncryptor(this.properties);
        try {
          pwdEncrypted = encryptor.decrypt(pwd.substring(0, pwd.length() - 1).replace("ENC(", ""));
          this.logger.debug("setSecurementPassword: Password decrpytion of user '{}' '{}' passed.",
                  this.securementUsername, pwd);
        } catch (final EncryptionOperationNotPossibleException e) {
          this.logger.error("setSecurementPassword: Password decrpytion of user '{}' '{}' failed.",
              this.securementUsername, pwd);
          throw e;
        }
      }
      return pwdEncrypted;
    }).orElse(securementPassword);
  }

  /**
   * @param securementUsername
   *            the securementUsername to set
   */
  public void setSecurementUsername(final String securementUsername) {
    this.securementUsername = securementUsername;
  }
}
