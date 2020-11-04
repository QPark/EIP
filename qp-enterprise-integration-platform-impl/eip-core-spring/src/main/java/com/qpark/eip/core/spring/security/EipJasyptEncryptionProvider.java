/*******************************************************************************
 * Copyright (c) 2013, 2020 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.StrongTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;

/**
 * @author bhausen
 */
public class EipJasyptEncryptionProvider {
  /** The property name of the encryptor password. */
  public static String EIP_ENCRYPTOR_PWD_PROPERTY_NAME = "eip_jasypt_encryptor_password";
  /** The {@link Logger}. */
  private static final Logger logger = LoggerFactory.getLogger(EipJasyptEncryptionProvider.class);

  /**
   * Encrypts the text if it starts with <i>ENC(</i> and ends with <i>)</i>.
   *
   * @param text
   *                       the text to encrypt.
   * @param properties
   *                       the {@link ApplicationPlaceholderConfigurer}.
   * @return the encrypted text.
   * @throws EncryptionOperationNotPossibleException
   */
  public static String decrypt(final String text, final ApplicationPlaceholderConfigurer properties)
      throws EncryptionOperationNotPossibleException {
    return Optional.ofNullable(properties).map(p -> decryptUsingProperties(text, p.toProperties()))
        .orElse(decryptUsingProperties(text, (Properties) null));
  }

  /**
   * Encrypts the text if it starts with <i>ENC(</i> and ends with <i>)</i>.
   *
   * @param text
   *                     the text to encrypt.
   * @param password
   *                     the password.
   * @return the encrypted text.
   * @throws EncryptionOperationNotPossibleException
   */
  public static String decryptWithPassword(final String text, final char[] password)
      throws EncryptionOperationNotPossibleException {
    return Optional.ofNullable(password).map(p -> text).map(t -> {
      String value = null;
      if (t.trim().startsWith("ENC(") && t.trim().endsWith(")") && password.length > 0) {
        final StrongTextEncryptor encryptor = new StrongTextEncryptor();
        encryptor.setPassword(String.valueOf(password));
        value = encryptor.decrypt(t.substring(0, t.length() - 1).replace("ENC(", ""));
      }
      return value;
    }).orElse(text);
  }

  /**
   * Encrypts the text if it starts with <i>ENC(</i> and ends with <i>)</i>.
   *
   * @param text
   *                       the text to encrypt.
   * @param properties
   *                       the {@link ApplicationPlaceholderConfigurer}.
   * @return the encrypted text.
   * @throws EncryptionOperationNotPossibleException
   */
  public static String decryptUsingProperties(final String text, final Properties properties)
      throws EncryptionOperationNotPossibleException {
    return Optional.ofNullable(text).map(t -> {
      String value = null;
      if (t.trim().startsWith("ENC(") && t.trim().endsWith(")")) {
        value = getEncryptorUsingProperties(properties)
            .decrypt(t.substring(0, t.length() - 1).replace("ENC(", ""));
      }
      return value;
    }).orElse(text);
  }

  /**
   * @param password
   *                       the {@link ApplicationPlaceholderConfigurer}.
   * @return the jasypt {@link StrongTextEncryptor}
   */
  public static StrongTextEncryptor getEncryptorWithPassword(final char[] password) {
    final StrongTextEncryptor encryptor = new StrongTextEncryptor();
    Optional.ofNullable(password).ifPresent(p -> encryptor.setPasswordCharArray(password));
    return encryptor;
  }

  /**
   * @param properties
   *                       the {@link Properties}.
   * @return the jasypt {@link StrongTextEncryptor}
   */
  public static StrongTextEncryptor getEncryptorUsingProperties(final Properties properties) {
    final StrongTextEncryptor encryptor = new StrongTextEncryptor();
    encryptor.setPassword(getEncryptorPassword(properties));
    return encryptor;
  }

  /**
   * @param properties
   *                       the {@link Properties}.
   * @return the jasypt {@link StrongTextEncryptor}
   */
  public static StrongTextEncryptor getEncryptor(
      final ApplicationPlaceholderConfigurer properties) {
    final StrongTextEncryptor encryptor = new StrongTextEncryptor();
    encryptor.setPassword(getEncryptorPassword(properties));
    return encryptor;
  }

  /**
   * Get the encryptor password from Environment, system properties or
   * properties.
   *
   * @param properties
   *                       the {@link ApplicationPlaceholderConfigurer}.
   * @return the password.
   */
  public static String getEncryptorPassword(final ApplicationPlaceholderConfigurer properties) {
    return Optional.ofNullable(properties).map(p -> getEncryptorPassword(p.toProperties()))
        .orElse("eip");
  }

  /**
   * Get the encryptor password from Environment, system properties or properties.
   *
   * @param properties
   *                       the {@link Properties}.
   * @return the password.
   */
  public static String getEncryptorPassword(final Properties properties) {
    String pwd = Optional.ofNullable(System.getenv(EIP_ENCRYPTOR_PWD_PROPERTY_NAME)).orElse(null);
    if (Objects.nonNull(pwd)) {
      logger.info("Found OS environment variable {}.", EIP_ENCRYPTOR_PWD_PROPERTY_NAME);
    } else {
      pwd = System.getProperty(EIP_ENCRYPTOR_PWD_PROPERTY_NAME);
      if (Objects.nonNull(pwd)) {
        logger.info("Found JVM property {}.", EIP_ENCRYPTOR_PWD_PROPERTY_NAME);
      } else {
        pwd = Optional.ofNullable(properties)
            .map(p -> p.getProperty(EIP_ENCRYPTOR_PWD_PROPERTY_NAME)).orElse(null);
        if (Objects.nonNull(pwd)) {
          logger.info("Found in provided properties property {}.", EIP_ENCRYPTOR_PWD_PROPERTY_NAME);
        }
      }
    }
    if (Objects.isNull(pwd)) {
      pwd = "eip";
    }
    return pwd;
  }

}
