/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core.spring.security.https;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import javax.annotation.PostConstruct;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

/**
 * @author bhausen
 */
public class EipHttpsUrlConnectionMessageSender extends
		HttpsUrlConnectionMessageSender {
	/** The user name of the basic HTTP-authentication. */
	private String userName;

	/** The password of the basic HTTP-authentication. */
	private String password;

	/** The {@link X509TrustManager} using the keystore.jks. */
	@Autowired
	private EipX509TrustManager x509TrustManager;

	/** base64(userName:password) */
	private String base64UserNamePassword;

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

	/**
	 * Initialize the HTTPs conncection.
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
	 * @see org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender#prepareConnection(java.net.HttpURLConnection)
	 */
	@Override
	protected void prepareConnection(final HttpURLConnection connection)
			throws IOException {
		if (HttpsURLConnection.class.isInstance(connection)) {
			this.setHostnameVerifier(this.x509TrustManager);
			this.setTrustManagers(new TrustManager[] { this.x509TrustManager });
		}
		/* call the super method. */
		super.prepareConnection(connection);

		/* Setup the basic Authentication. */
		if (HttpURLConnection.class.isInstance(connection)
				&& this.userName != null) {
			HttpURLConnection httpsConnection = connection;
			httpsConnection.setRequestProperty(
					"Authorization",
					new StringBuffer(128).append("Basic ")
							.append(this.base64UserNamePassword).toString());
		}
	}

	/**
	 * @param password the password to set.
	 */
	public void setPassword(final String password) {
		this.password = password;
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
