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
package com.qpark.maven.plugin.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;

/**
 * Collect the message defining schemas into one schema that importing them.
 * This is needed to use the maven-hyperjaxb3-plugin of org.jvnet.hyperjaxb3.
 * @author bhausen
 */
@Mojo(name = "download", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DownloadMojo extends AbstractMojo {
	/** {@link HostnameVerifier} that always trust. */
	static class AlwaysTrustHostnameVerifier implements HostnameVerifier {
		/**
		 * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
		 *      javax.net.ssl.SSLSession)
		 */
		@Override
		public boolean verify(final String hostname, final SSLSession session) {
			return true;
		}
	}

	/** TrustManager. */
	static class DefaultTrustManager implements X509TrustManager {
		/**
		 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[],
		 *      java.lang.String)
		 */
		@Override
		public void checkClientTrusted(final X509Certificate[] arg0,
				final String arg1) throws CertificateException {
		}

		/**
		 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[],
		 *      java.lang.String)
		 */
		@Override
		public void checkServerTrusted(final X509Certificate[] arg0,
				final String arg1) throws CertificateException {
		}

		/**
		 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
		 */
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	public static void main(final String[] args) {
		System.out.println(new String(Base64
				.encodeBase64("username:password".getBytes())));
		System.out
				.println(getFileName("https://host.sap.com:50201/dir/wsdl?p=sa/3f62caad5b3f3151a01ab6582572c48a/binding"));
	}

	static String read(final URL url, final String base64User, final Proxy proxy)
			throws IOException {
		StringBuffer sb = new StringBuffer(1024);
		HttpURLConnection connection = null;
		if (url.getProtocol().equalsIgnoreCase("https")) {
			if (proxy == null) {
				connection = (HttpsURLConnection) url.openConnection();
			} else {
				connection = (HttpsURLConnection) url.openConnection(proxy);
			}
			((HttpsURLConnection) connection)
					.setHostnameVerifier(new AlwaysTrustHostnameVerifier());
		} else {
			if (proxy == null) {
				connection = (HttpURLConnection) url.openConnection();
			} else {
				connection = (HttpURLConnection) url.openConnection(proxy);
			}
		}
		if (base64User != null) {
			connection.setRequestProperty("Authorization", "Basic "
					+ base64User);
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
			sb.append(inputLine).append("\n");
		}
		buffer.close();

		return sb.toString();
	}

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-resources")
	private File outputDirectory;
	@Component
	private MavenProject project;
	/** The URLs to be downloaded. */
	@Parameter(property = "downloads")
	private List<Download> downloads;
	/** The username of the user required at download. */
	@Parameter(property = "username")
	private String username;
	/** The password of the user required at download. */
	@Parameter(property = "password")
	private String password;
	/** The proxy host to be used at download. */
	@Parameter(property = "proxyHost")
	private String proxyHost;
	/** The proxy port to be used at download. */
	@Parameter(property = "proxyPort")
	private Integer proxyPort;
	/** The proxy username to be used at download. */
	@Parameter(property = "proxyUsername")
	private String proxyUsername;
	/** The proxy password to be used at download. */
	@Parameter(property = "proxyPassword")
	private String proxyPassword;

	private static String getFileName(final String url) {
		String f = new StringBuffer(16).append(System.currentTimeMillis())
				.append(".txt").toString();
		if (url != null) {
			String s = url;
			int index = s.lastIndexOf('/');
			if (index >= 0) {
				s = s.substring(index + 1);
			}
			index = s.lastIndexOf('&');
			if (index >= 0) {
				s = s.substring(index + 1);
			}
			if (!s.equals(url)) {
				f = s;
			}
		}
		if (f.endsWith(".wsdl") || f.endsWith(".xml") || f.endsWith(".html")
				|| f.endsWith("xsd")) {
			// nothing
		} else {
			f = new StringBuffer(f).append(".txt").toString();
		}
		return f;
	}

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		String base64User = null;
		Proxy proxy = null;
		Authenticator authenticator = null;
		if (this.username != null && this.username.trim().length() > 0
				&& this.password != null && this.password.trim().length() > 0) {
			base64User = new String(Base64.encodeBase64(new StringBuffer(
					this.username).append(":").append(this.password).toString()
					.getBytes()));
		}
		if (this.proxyHost != null && this.proxyHost.trim().length() > 0
				&& this.proxyPort != null && !this.proxyPort.equals(0)) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					this.proxyHost, this.proxyPort));
		}
		if (this.proxyUsername != null
				&& this.proxyUsername.trim().length() > 0
				&& this.proxyPassword != null
				&& this.proxyPassword.trim().length() > 0) {
			authenticator = new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							DownloadMojo.this.proxyUsername,
							DownloadMojo.this.proxyPassword.toCharArray());
				}
			};
			Authenticator.setDefault(authenticator);
		}
		if (this.downloads != null) {
			File f = null;
			String s;
			String filename;
			for (Download download : this.downloads) {
				try {
					s = read(new URL(download.getUrl()), base64User, proxy);
					if (s != null && s.length() > 0) {
						if (download.getFilename() != null
								&& download.getFilename().trim().length() > 0) {
							filename = download.getFilename();
						} else {
							filename = new StringBuffer().append(
									getFileName(download.getUrl())).toString();
							int i = 0;
							while (true) {
								f = new File(this.outputDirectory, filename);
								if (f.exists()) {
									i++;
									filename = new StringBuffer()
											.append(getFileName(download
													.getUrl())).append(i)
											.toString();
								} else {
									break;
								}
							}
						}
						f = Util.getFile(this.outputDirectory, filename);
						this.getLog().info(
								new StringBuffer().append("Write ").append(
										f.getAbsolutePath()));
						Util.writeToFile(f, s);
					}
				} catch (Exception e) {
					this.getLog().error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		this.getLog().debug("-execute");
	}
}
