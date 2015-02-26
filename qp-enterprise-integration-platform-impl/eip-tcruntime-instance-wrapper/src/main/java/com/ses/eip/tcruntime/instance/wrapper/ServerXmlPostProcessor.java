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
package com.qpark.eip.tcruntime.instance.wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * @author bhausen
 * 
 */
public class ServerXmlPostProcessor {
	private final File catalinaBaseDirectory;
	private final File confDirectory;
	private final File webappsDirectory;
	/**
	 * The file <code>server.xml</code>. Normaly you find it in
	 * <code>${catalina.base}/conf</code>.
	 */
	private final File serverXml;
	private String contents;
	private boolean testMode;

	public static void main(final String[] args) {
		ServerXmlPostProcessor p = new ServerXmlPostProcessor(
				new File(
						"C:\\xnb\\bin\\vfabric-tc-server-developer-2.8.1.RELEASE\\busserver1"));
		p.testMode = true;
		try {
			p.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private TreeMap<String, String> getConnectors(final String catalina) {
		TreeMap<String, String> connectors = new TreeMap<String, String>();
		int startIndexConnector = catalina.indexOf(CONNECTOR_START);
		int startIndex = 0;
		int stopIndex = 0;
		while (startIndexConnector > 0) {
			startIndex = startIndexConnector;
			stopIndex = catalina.indexOf("/>", startIndex);
			if (startIndex > 0 && stopIndex > 0) {
				String connector = catalina.substring(startIndex, stopIndex
						+ "/>".length());
				String connectorId = null;
				startIndex = connector.indexOf(PROTOCOL);
				stopIndex = connector.indexOf("\"",
						startIndex + PROTOCOL.length());
				if (startIndex > 0 && stopIndex > 0) {
					String s = connector.substring(
							startIndex + PROTOCOL.length(), stopIndex);
					if (s.equals("org.apache.coyote.http11.Http11Protocol")) {
						connectorId = "http";
						if (connector.indexOf("SSLEnabled=\"true\"") > 0) {
							connectorId = "https";
						}
					} else if (s.equals("org.apache.coyote.ajp.AjpProtocol")) {
						connectorId = "ajp";
					}
					if (connectorId != null && connector != null) {
						connectors.put(connectorId, connector);
					}
				}
			}
			startIndexConnector = catalina.indexOf(CONNECTOR_START,
					startIndexConnector + 2);
		}
		return connectors;
	}

	private static String SERVICE_START = "<Service name=\"Catalina\">";
	private static String CONNECTOR_START = "<Connector";
	private static String PROTOCOL = "protocol=\"";
	private static String SERVICE_END = "</Service>";

	public void run() throws Exception {
		File f = new File(this.confDirectory,
				"server.xml.postprocessor.properties");
		if (f.exists()) {
			Properties p = new Properties();
			FileInputStream fis = new FileInputStream(f);
			p.load(fis);
			fis.close();
			TreeMap<String, ServiceDefinition> sds = this
					.getServiceDefinitions(p);
			if (!sds.isEmpty()) {
				StringBuffer sb = new StringBuffer(1024);
				String text = new Scanner(this.serverXml).useDelimiter("\\A")
						.next();
				int startIndex = text.indexOf(SERVICE_START);
				int stopIndex = text.indexOf(SERVICE_END, startIndex);
				if (startIndex > 0 && stopIndex > 0) {
					String prefix = text.substring(0, startIndex);
					sb.append(prefix);
					String postfix = text.substring(
							stopIndex + SERVICE_END.length(), text.length());
					String catalina = text.substring(startIndex, stopIndex
							+ SERVICE_END.length());
					String executorEngine = "";
					int startIndexConnector = catalina.indexOf(CONNECTOR_START);
					if (startIndexConnector > 0) {
						executorEngine = catalina.substring(
								SERVICE_START.length(), startIndexConnector);
					}
					TreeMap<String, String> connectors = this
							.getConnectors(catalina);

					for (String serviceId : sds.keySet()) {
						sb.append("\n\t<Service name=\"Catalina-");
						sb.append(serviceId);
						sb.append("\">");
						sb.append(this.handleExecutorEngine(serviceId,
								executorEngine));
						for (String key : connectors.keySet()) {
							sb.append(this.handleConnector(serviceId,
									sds.get(serviceId), key,
									connectors.get(key)));
						}
						sb.append("\n\t</Service>");
					}
					sb.append(postfix);
					System.out.println(sb.toString());
				}
			}
		}

	}

	private static class ServiceDefinition {
		String id;
		Integer httpPort;
		Integer httpsPort;
		Integer ajpPort;
	}

	private static final String SERVICE_DEF_START = "service.def.";

	private TreeMap<String, ServiceDefinition> getServiceDefinitions(
			final Properties p) {
		TreeMap<String, ServiceDefinition> map = new TreeMap<String, ServiceDefinition>();
		String key;
		String serviceId = null;
		ServiceDefinition sd = null;
		String s;
		for (int i = 0; i < 20; i++) {
			key = new StringBuffer(SERVICE_DEF_START).append(i).append(".name")
					.toString();
			serviceId = p.getProperty(key, "").trim();
			if (serviceId.length() > 0) {
				sd = new ServiceDefinition();
				sd.id = serviceId;
				map.put(sd.id, sd);
				key = new StringBuffer(SERVICE_DEF_START).append(i)
						.append(".port.http").toString();
				s = p.getProperty(key, "");
				try {
					sd.httpPort = Integer.parseInt(s.trim());
				} catch (NumberFormatException e) {
					sd.httpPort = -1;
				}
				key = new StringBuffer(SERVICE_DEF_START).append(i)
						.append(".port.https").toString();
				s = p.getProperty(key, "");
				try {
					sd.httpsPort = Integer.parseInt(s.trim());
				} catch (NumberFormatException e) {
					sd.httpsPort = -1;
				}
				key = new StringBuffer(SERVICE_DEF_START).append(i)
						.append(".port.ajp").toString();
				s = p.getProperty(key, "");
				try {
					sd.ajpPort = Integer.parseInt(s.trim());
				} catch (NumberFormatException e) {
					sd.ajpPort = -1;
				}
			}
		}
		return map;
	}

	private String handleExecutorEngine(final String serviceId,
			final String executorEngine) {
		String s = executorEngine;
		// overall
		s = s.replace("tomcatThreadPool", new StringBuffer("tomcatThreadPool-")
				.append(serviceId).toString());
		s = s.replace("localhost",
				new StringBuffer("localhost-").append(serviceId).toString());
		// Executor
		s = s.replace("maxThreads=\"300\"", "maxThreads=\"150\"");
		s = s.replace("namePrefix=\"tomcat-http--\"", new StringBuffer(
				"namePrefix=\"tomcat-").append(serviceId).append("-http--\"")
				.toString());
		// Engine
		s = s.replace("name=\"Catalina\"", new StringBuffer("name=\"Catalina-")
				.append(serviceId).toString());
		s = s.replace("appBase=\"webapps\"", new StringBuffer(
				"appBase=\"webapps-").append(serviceId).toString());
		return s;
	}

	private String handleConnector(final String serviceId,
			final ServiceDefinition sd, final String connectorId,
			final String connector) {
		String s = connector;
		// overall
		s = s.replace("tomcatThreadPool", new StringBuffer("tomcatThreadPool-")
				.append(serviceId).toString());
		if (connectorId.equals("http") && sd.httpPort > 0) {
			s = s.replaceAll("port=\".*?\"", new StringBuffer("port=\"")
					.append(sd.httpPort).append("\"").toString());
			s = s.replaceAll("redirectPort=\".*?\"", new StringBuffer(
					"redirectPort=\"").append(sd.httpsPort).append("\"")
					.toString());
		} else if (connectorId.equals("ajp") && sd.ajpPort > 0) {
			s = s.replaceAll("port=\".*?\"", new StringBuffer("port=\"")
					.append(sd.ajpPort).append("\"").toString());
			s = s.replaceAll("redirectPort=\".*?\"", new StringBuffer(
					"redirectPort=\"").append(sd.httpsPort).append("\"")
					.toString());
		} else if (connectorId.equals("https") && sd.httpsPort > 0) {
			s = s.replaceAll("port=\".*?\"", new StringBuffer("port=\"")
					.append(sd.httpsPort).append("\"").toString());
			s = s.replaceAll("redirectPort=\".*?\"", new StringBuffer(
					"redirectPort=\"").append(sd.httpsPort).append("\"")
					.toString());
		}
		return s;
	}

	/**
	 * 
	 * @param serverXml
	 *            The file <code>server.xml</code>.
	 */
	public ServerXmlPostProcessor(final File catalinaBaseDirectory) {
		this.catalinaBaseDirectory = catalinaBaseDirectory;
		if (this.catalinaBaseDirectory == null) {
			throw new IllegalArgumentException("catalina base is null");
		} else if (!this.catalinaBaseDirectory.exists()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.catalinaBaseDirectory.getAbsolutePath())
					.append(" does not exist.").toString());
		} else if (!this.catalinaBaseDirectory.canRead()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.catalinaBaseDirectory.getAbsolutePath())
					.append(" is not readable.").toString());
		}
		this.confDirectory = new File(catalinaBaseDirectory, "conf");
		if (!this.confDirectory.exists()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.confDirectory.getAbsolutePath())
					.append(" does not exist.").toString());
		} else if (!this.confDirectory.canRead()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.confDirectory.getAbsolutePath())
					.append(" is not readable.").toString());
		}
		this.webappsDirectory = new File(catalinaBaseDirectory, "webapps");
		if (!this.webappsDirectory.exists()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.webappsDirectory.getAbsolutePath())
					.append(" does not exist.").toString());
		} else if (!this.webappsDirectory.canRead()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.webappsDirectory.getAbsolutePath())
					.append(" is not readable.").toString());
		}
		this.serverXml = new File(this.confDirectory, "server.xml");
		if (!this.serverXml.exists()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.serverXml.getAbsolutePath())
					.append(" does not exist.").toString());
		} else if (!this.serverXml.canRead()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.serverXml.getAbsolutePath())
					.append(" is not readable.").toString());
		} else if (!this.serverXml.canWrite()) {
			throw new IllegalArgumentException(new StringBuffer(64)
					.append(this.serverXml.getAbsolutePath())
					.append(" is not writeable.").toString());
		}
		try {
			this.contents = new Scanner(this.serverXml).useDelimiter("\\A")
					.next();
		} catch (FileNotFoundException e) {
			// not possible because checked before.
		}
	}

}
