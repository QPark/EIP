/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security.https;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * {@link SimpleClientHttpRequestFactory} extension supplying HTTPs and basic
 * HTTP authentication.
 * </p>
 * To be used with the {@link org.springframework.web.client.RestTemplate}.
 *
 * <pre>
 * EipHttpsClientHttpRequestFactory requestFactory = new EipHttpsClientHttpRequestFactory();
 * requestFactory.setUserName(userName);
 * requestFactory.setPassword(password);
 *
 * RestTemplate template = new RestTemplate(requestFactory);
 * </pre>
 *
 * @author bhausen
 */
public class EipHttpsClientHttpRequestFactory
		extends SimpleClientHttpRequestFactory {
	/** base64(userName:password) */
	private String base64UserNamePassword;
	/** The password of the basic HTTP-authentication. */
	private String password;
	/** The user name of the basic HTTP-authentication. */
	private String userName;
	/** The {@link X509TrustManager} using the keystore.jks. */
	@Autowired
	private EipX509TrustManager x509TrustManager;

	/**
	 * Initialize the HTTPs connection.
	 *
	 * @throws UnsupportedEncodingException
	 */
	@PostConstruct
	public void init() throws UnsupportedEncodingException {
		if (this.userName != null) {
			if (this.password == null) {
				this.password = "";
			}
			this.base64UserNamePassword = new String(
					Base64.encodeBase64String(new StringBuffer(this.userName)
							.append(":").append(this.password).toString()
							.getBytes("UTF-8"))).replaceAll("\n", "");
		}
	}

	/**
	 * @see org.springframework.http.client.SimpleClientHttpRequestFactory#prepareConnection(java.net.HttpURLConnection,
	 *      java.lang.String)
	 */
	@Override
	protected void prepareConnection(final HttpURLConnection connection,
			final String httpMethod) {
		try {
			/* Setup HttpsURLConnection. */
			if (HttpsURLConnection.class.isInstance(connection)) {
				HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
				httpsConnection.setHostnameVerifier(this.x509TrustManager);
				TrustManager[] trustManagers = new TrustManager[] {
						this.x509TrustManager };
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, trustManagers,
						new java.security.SecureRandom());
				((HttpsURLConnection) connection)
						.setSSLSocketFactory(sslContext.getSocketFactory());
			}
			super.prepareConnection(connection, httpMethod);
			/* Setup the basic Authentication. */
			if (HttpURLConnection.class.isInstance(connection)
					&& this.userName != null) {
				HttpURLConnection httpsConnection = connection;
				httpsConnection.setRequestProperty("Authorization",
						new StringBuffer(128).append("Basic ")
								.append(this.base64UserNamePassword)
								.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param password
	 *            the password to set.
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @param userName
	 *            the userName to set.
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @param x509TrustManager
	 *            the x509TrustManager to set.
	 */
	public void setX509TrustManager(
			final EipX509TrustManager x509TrustManager) {
		this.x509TrustManager = x509TrustManager;
	}
}
