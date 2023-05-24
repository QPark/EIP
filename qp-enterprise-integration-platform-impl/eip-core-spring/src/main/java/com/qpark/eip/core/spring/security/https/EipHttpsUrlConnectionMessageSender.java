/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security.https;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.security.EipHttpHeaderProvider;
import com.qpark.eip.core.spring.security.EipJasyptEncryptionProvider;

/**
 * @author bhausen
 */
public class EipHttpsUrlConnectionMessageSender extends HttpsUrlConnectionMessageSender {
	/** The user name of the basic HTTP-authentication. */
	private String userName;
	/** The password of the basic HTTP-authentication. */
	private String password;
	/** The HTTP request content type. */
	private String contentType;
	/** The {@link X509TrustManager} using the keystore.jks. */
	@Autowired(required = false)
	private EipX509TrustManager x509TrustManager;
	/** The {@link ApplicationPlaceholderConfigurer} if available. */
	@Autowired(required = false)
	private ApplicationPlaceholderConfigurer properties;
	/** base64(userName:password) */
	private String base64UserNamePassword;
	/** The {@link EipHttpHeaderProvider} if available. */
	@Autowired(required = false)
	private EipHttpHeaderProvider headerProvider;

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
	 * @return the x509TrustManager.
	 */
	public EipX509TrustManager getX509TrustManager() {
		return this.x509TrustManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	/**
	 * Initialize the HTTPs conncection.
	 *
	 * @throws UnsupportedEncodingException
	 */
	@PostConstruct
	public void init() throws UnsupportedEncodingException {
		if (this.userName != null) {
			if (this.password == null) {
				this.password = "";
			}
			this.base64UserNamePassword = new String(Base64.encodeBase64String(
					new StringBuffer(this.userName).append(":").append(this.password).toString().getBytes("UTF-8")))
					.replaceAll("\n", "");
		}
	}

	/**
	 * @see org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender#prepareConnection(java.net.HttpURLConnection)
	 */
	@Override
	protected void prepareConnection(final HttpURLConnection connection) throws IOException {
		if (HttpsURLConnection.class.isInstance(connection)) {
			Optional.ofNullable(this.x509TrustManager).map(trustManager -> {
				this.setHostnameVerifier(trustManager);
				this.setTrustManagers(new TrustManager[] { trustManager });
				return trustManager;
			}).orElseGet(() -> {
				this.logger.error("Missconfiguration: No EipX509TrustManager set!");
				return null;
			});
		}
		/* call the super method. */
		super.prepareConnection(connection);

		/* Enhance the request with the HTTP header values. */
		Optional.ofNullable(this.headerProvider).map(h -> h.getHttpHeaders(connection.getURL()))
				.ifPresent(headers -> headers.entrySet().stream()
						.forEach(header -> connection.setRequestProperty(header.getKey(), header.getValue())));

		/* Setup ContentType HTTP header. */
		Optional.ofNullable(this.contentType).ifPresent(ct -> connection.setRequestProperty("Content-Type", ct));

		/* Setup the basic Authentication. */
		if (Objects.nonNull(this.userName)) {
			this.logger.error(String.format("prepareConnection add request header '%s' basic AUTH userName '%s'",
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
		this.password = Optional.ofNullable(password).map(p -> EipJasyptEncryptionProvider.decrypt(p, this.properties))
				.orElse(password);
	}

	/**
	 * @param userName the userName to set.
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @param x509TrustManager the x509TrustManager to set.
	 */
	public void setX509TrustManager(final EipX509TrustManager x509TrustManager) {
		this.x509TrustManager = x509TrustManager;
	}
}
