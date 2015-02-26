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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.PostConstruct;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.Assert;

public class HttpsRequester {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		String keystorePwd = "KeystorePwd";
		String keystoreSource;
		keystoreSource = "classpath:/servicebus-keystore.jks";
		String httpAuthUser = "userName";
		String httpAuthPwd = "password";
		try {
			HttpsRequester r = new HttpsRequester();
			r.setHttpAuthPwd(httpAuthPwd);
			r.setHttpAuthUser(httpAuthUser);
			r.setKeystorePwd(keystorePwd);
			r.setKeystoreSource(keystoreSource);

			// Call urls.
			String response = null;
			String url;

			// https://sesvdx00.sap.com:50201/dir/wsdl?p=sa/d0a2f8f335c53146bf4a80ab41295388/portType
			url = "https://sesvdx00.sap.com:50201/dir/wsdl";
			System.out.println("REQUEST: " + url);
			// response = r.read(url);
			System.out.println("RESPONSE:");
			System.out.println(response);
			url = "https://10.99.18.24:50201/dir/wsdl";
			System.out.println("REQUEST: " + url);
			response = r.read(url);
			System.out.println("RESPONSE:");
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String read(final String url, final String httpAuthBase64,
			final HostnameVerifier hostnameVerifier) throws IOException {
		return read(new URL(url), httpAuthBase64, hostnameVerifier);
	}

	private static String read(final URL url, final String httpAuthBase64,
			final HostnameVerifier hostnameVerifier) throws IOException {
		StringBuffer sb = new StringBuffer(1024);
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		if (url.getProtocol().equalsIgnoreCase("https")) {
			connection = (HttpsURLConnection) url.openConnection();
			((HttpsURLConnection) connection)
					.setHostnameVerifier(hostnameVerifier);
		}
		if (httpAuthBase64 != null) {
			connection
					.setRequestProperty("Authorization", new StringBuffer(
							httpAuthBase64.length() + 6).append("Basic ")
							.append(httpAuthBase64).toString());
		}
		connection.setRequestProperty("Content-Type",
				"text/plain; charset=\"utf8\"");
		connection.setRequestMethod("GET");

		int returnCode = connection.getResponseCode();
		InputStream connectionIn = null;
		if (returnCode == 200) {
			connectionIn = connection.getInputStream();
		} else {
			connectionIn = connection.getErrorStream();
		}
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				connectionIn));
		String inputLine;
		while ((inputLine = buffer.readLine()) != null) {
			sb.append(inputLine);
		}
		buffer.close();

		return sb.toString();
	}

	private String httpAuthBase64;
	private char[] httpAuthPwd;
	private String httpAuthUser;
	private char[] keystorePwd = "".toCharArray();

	private String keystoreSource;

	private EipX509TrustManager trustManager;

	@PostConstruct
	public void init() throws Exception {
		if (this.trustManager == null) {
			// HTTP AUTH
			if (this.httpAuthUser != null && this.httpAuthUser.length() > 0) {
				this.httpAuthBase64 = new String(
						Base64.encode(new StringBuffer(256)
								.append(this.httpAuthUser)
								.append(":")
								.append(this.httpAuthPwd == null ? ""
										: this.httpAuthPwd).toString()
								.getBytes("UTF-8")), "UTF-8");
			}
			// Keystore handler trust manager
			Resource keystore = null;
			if (this.keystoreSource == null) {
				Assert.isNull(this.keystoreSource);
			} else {
				if (this.keystoreSource.startsWith("classpath:")) {
					keystore = new ClassPathResource(this.keystoreSource);
				} else {
					keystore = new FileSystemResource(this.keystoreSource);
				}
			}
			if (keystore == null) {
				Assert.isNull(keystore);
			}
			this.trustManager = new EipX509TrustManager();
			this.trustManager.setKeystore(keystore);
			this.trustManager.setKeystorePassword(new String(this.keystorePwd));
			this.trustManager.init();
		}
		// SSL Context
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, new TrustManager[] { this.trustManager }, null);
		SSLContext.setDefault(ctx);
	}

	public String read(final String url) throws Exception {
		this.init();
		String response = read(url, this.httpAuthBase64, this.trustManager);
		return response;
	}

	/**
	 * @param httpAuthPwd the httpAuthPwd to set
	 */
	public void setHttpAuthPwd(final String httpAuthPwd) {
		if (httpAuthPwd != null) {
			this.httpAuthPwd = httpAuthPwd.toCharArray();
		}
	}

	/**
	 * @param httpAuthUser the httpAuthUser to set
	 */
	public void setHttpAuthUser(final String httpAuthUser) {
		this.httpAuthUser = httpAuthUser;
	}

	/**
	 * @param keystorePwd the keystorePwd to set
	 */
	public void setKeystorePwd(final String keystorePwd) {
		if (keystorePwd != null) {
			this.keystorePwd = keystorePwd.toCharArray();
		}
	}

	/**
	 * @param keystoreSource the keystoreSource to set
	 */
	public void setKeystoreSource(final String keystoreSource) {
		this.keystoreSource = keystoreSource;
	}
}
