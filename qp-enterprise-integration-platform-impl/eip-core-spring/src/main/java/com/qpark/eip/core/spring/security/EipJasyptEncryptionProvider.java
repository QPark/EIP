/*******************************************************************************
 * Copyright (c) 2013, 2020 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import java.util.Optional;
import org.jasypt.util.text.StrongTextEncryptor;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;

/**
 * @author bhausen
 */
public class EipJasyptEncryptionProvider {
  /** The property name of the encryptor password. */
  public static String EIP_ENCRYPTOR_PWD_PROPERTY_NAME = "eip.jasypt.encryptor.password";

  /**
   * Get the encryptor password from Environment, system properties or properties.
   * @param properties the {@link ApplicationPlaceholderConfigurer}.
   * @return the password.
   */
  public static String getEncryptorPassword(final ApplicationPlaceholderConfigurer properties) {
    return Optional.ofNullable(System.getenv(EIP_ENCRYPTOR_PWD_PROPERTY_NAME))
        .orElse(System.getProperty(EIP_ENCRYPTOR_PWD_PROPERTY_NAME,
            properties.getProperty(EIP_ENCRYPTOR_PWD_PROPERTY_NAME, "eip")));
  }

  /**
   * @param properties the {@link ApplicationPlaceholderConfigurer}.
   * @return the jasypt {@link StrongTextEncryptor}
   */
  public static StrongTextEncryptor getEncryptor(
      final ApplicationPlaceholderConfigurer properties) {
    final StrongTextEncryptor encryptor = new StrongTextEncryptor();
    encryptor.setPassword(getEncryptorPassword(properties));
    return encryptor;
  }
}
