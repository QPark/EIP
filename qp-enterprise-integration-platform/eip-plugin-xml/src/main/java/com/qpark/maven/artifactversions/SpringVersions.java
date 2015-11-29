/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.artifactversions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

/**
 * @author bhausen
 */
public class SpringVersions {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(SpringVersions.class);

	public static void main(final String[] args) {
		System.out.println(excutePost(
		// "http://repo.springsource.org/release/org/springframework/spring-core/",
				"http://www.spiegel.de/", ""));
	}

	public static String excutePost(final String targetURL,
			final String urlParameters) {
		URL url;
		Proxy proxy;
		HttpURLConnection connection = null;
		Authenticator authenticator = new Authenticator() {

			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("blub",
						"".toCharArray());
			}
		};
		try {
			// Create connection
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					"213.169.104.45", 8080));
			Authenticator.setDefault(authenticator);
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
