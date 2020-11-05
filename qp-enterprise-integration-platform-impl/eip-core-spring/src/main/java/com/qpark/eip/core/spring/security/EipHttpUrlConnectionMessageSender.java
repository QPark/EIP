/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;

/**
 * @author bhausen
 */
public class EipHttpUrlConnectionMessageSender extends HttpUrlConnectionMessageSender {
  /** The user name of the basic HTTP-authentication. */
  private String userName;
  /** The password of the basic HTTP-authentication. */
  private String password;
  /** The HTTP request content type. */
  private String contentType;
  /** base64(userName:password) */
  private String base64UserNamePassword;
  /** The {@link ApplicationPlaceholderConfigurer} if available. */
  @Autowired(required = false)
  private ApplicationPlaceholderConfigurer properties;

  /**
   * @return the contentType.
   */
  public String getContentType() {
    return this.contentType;
  }

  /**
   * @return the password.
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * @return the userName.
   */
  public String getUserName() {
    return this.userName;
  }

  /**
   * Initialize the HTTP connection.
   *
   * @throws UnsupportedEncodingException
   */
  @PostConstruct
  public void init() throws UnsupportedEncodingException {
    if (this.userName != null) {
      if (this.password == null) {
        this.password = "";
      }
      this.base64UserNamePassword =
          new String(Base64.encodeBase64String(new StringBuffer(this.userName).append(":")
              .append(this.password).toString().getBytes("UTF-8"))).replaceAll("\n", "");
    }
  }

  /**
   * @see org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender#prepareConnection(java.net.HttpURLConnection)
   */
  @Override
  protected void prepareConnection(final HttpURLConnection connection) throws IOException {
    /* call the super method. */
    super.prepareConnection(connection);

    /* Setup ContentType HTTP header. */
    if (Objects.nonNull(this.contentType)) {
      connection.setRequestProperty("Content-Type", this.contentType);
    }

    /* Setup the basic Authentication. */
    if (Objects.nonNull(this.userName)) {
      this.logger
          .debug(String.format("prepareConnection add request header '%s' basic AUTH userName '%s'",
              "Authorization", this.userName));
      connection.setRequestProperty("Authorization",
          new StringBuffer(128).append("Basic ").append(this.base64UserNamePassword).toString());
      this.logger.debug(String.format("prepareConnection request Headers: %s",
          connection.getRequestProperties().entrySet().stream()
              .sorted(Comparator.comparing(v -> String.valueOf(v.getKey())))
              .map(v -> String.format("%s: %s", v.getKey(),
                  v.getValue().stream().collect(Collectors.joining(","))))
              .collect(Collectors.joining(", "))));
    }
  }

  /**
   * @param contentType
   */
  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  /**
   * @param password the password to set.
   */
  public void setPassword(final String password) {
    this.password = Optional.ofNullable(password)
        .map(p -> EipJasyptEncryptionProvider.decrypt(p, this.properties)).orElse(password);
  }

  /**
   * @param userName the userName to set.
   */
  public void setUserName(final String userName) {
    this.userName = userName;
  }
}
