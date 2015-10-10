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
package com.qpark.maven.plugin.router;

import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_FRAMEWORK_XSD_VERSION;
import static com.qpark.maven.plugin.EipGeneratorDefaults.DEFAULT_SPRING_INTEGRATION_XSD_VERSION;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;

/**
 * Create the spring-integration xml files of the router. The base to create
 * these files are the router defintion property files.
 * @author bhausen
 */
@Mojo(name = "generate-router-xml", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class RouterXmlMojo extends AbstractMojo {
	/**
	 * Collect the property files recursively.
	 * @param f the file to check.
	 * @param propertyFiles the list of property files.
	 */
	private static void scanForProperties(final File f,
			final List<File> propertyFiles) {
		File[] cs = f.listFiles();
		if (cs != null && cs.length > 0) {
			for (File c : cs) {
				if (c.isDirectory()) {
					scanForProperties(c, propertyFiles);
				} else if (c.getName().endsWith(".properties")) {
					propertyFiles.add(c);
				}
			}
		}
	}

	private static void waitOneMilli() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
	}

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/classes/router/definitions")
	protected File baseDirectory;

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/classes/router")
	protected File outputDirectory;

	@Component
	private MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		List<File> propertyFiles = new ArrayList<File>();
		StringBuffer applicationRouterConfig = new StringBuffer(1024);
		scanForProperties(this.baseDirectory, propertyFiles);

		Properties p;
		BufferedInputStream bis;
		String serviceId;
		String operationName;
		String channelNameWsRequest;
		String channelNameWsResponse;
		String routerType;
		List<String> operationProviderBeanNames;
		List<SimpleEntry<String, String>> headerEnricherBeans;
		List<SimpleEntry<String, String>> operationProviderChannelPairs;
		for (File file : propertyFiles) {
			p = new Properties();
			try {
				bis = new BufferedInputStream(new FileInputStream(file));
				p.load(bis);
				bis.close();
				serviceId = p.getProperty(
						RouterProperitesMojo.ROUTER_SERVICE_ID, "").trim();
				operationName = p.getProperty(
						RouterProperitesMojo.ROUTER_OPERATION_NAME, "").trim();
				routerType = p
						.getProperty(RouterProperitesMojo.ROUTER_TYPE, "");
				operationProviderBeanNames = this
						.getOperationProviderBeanNames(p);
				headerEnricherBeans = this.getHeaderEnricher(p);
				operationProviderChannelPairs = this
						.getOperationProviderChannelPairs(p);
				channelNameWsRequest = p.getProperty(
						RouterProperitesMojo.ROUTER_CHANNEL_WS_REQUEST, "")
						.trim();
				channelNameWsResponse = p.getProperty(
						RouterProperitesMojo.ROUTER_CHANNEL_WS_RESPONSE, "")
						.trim();
				if (serviceId.length() == 0) {
					this.getLog().error(
							new StringBuffer(128)
									.append("The service is empty in ")
									.append(file.getAbsolutePath()).append(".")
									.toString());
				} else if (operationName.length() == 0) {
					this.getLog().error(
							new StringBuffer(128)
									.append("The operation name is empty in ")
									.append(file.getAbsolutePath()).append(".")
									.toString());
				} else if (channelNameWsRequest.length() == 0) {
					this.getLog()
							.error(new StringBuffer(128)
									.append("The web service request channel name is empty in ")
									.append(file.getAbsolutePath()).append(".")
									.toString());
				} else if (channelNameWsResponse.length() == 0) {
					this.getLog()
							.error(new StringBuffer(128)
									.append("The web service response channel name is empty in ")
									.append(file.getAbsolutePath()).append(".")
									.toString());
				} else if (routerType.length() == 0
						|| routerType.trim().equals(
								RouterProperitesMojo.ROUTER_TYPE_FORWARD)) {
					if (operationProviderBeanNames.size() != 1) {
						this.getLog()
								.error(new StringBuffer(128)
										.append("The list size of operation provider bean names has to be 1 when using router type ")
										.append(RouterProperitesMojo.ROUTER_TYPE_FORWARD)
										.append(" in ")
										.append(file.getAbsolutePath())
										.append(".").toString());
					} else if (operationProviderBeanNames.size() == 0) {
						this.getLog()
								.error(new StringBuffer(128)
										.append("The list of operation provider bean names is empty in ")
										.append(file.getAbsolutePath())
										.append(".").toString());
					}
					String xml = this.getRouterForward(serviceId,
							operationName, channelNameWsRequest,
							channelNameWsResponse, operationProviderBeanNames,
							p);
					applicationRouterConfig.append(xml);
					this.write(serviceId, operationName, xml);
				} else if (routerType.length() == 0
						|| routerType
								.trim()
								.equals(RouterProperitesMojo.ROUTER_TYPE_RECIPIENT_LIST_ROUTER)) {
					String aggregatorBeanName = p
							.getProperty(
									RouterProperitesMojo.ROUTER_RESPONSE_AGGREGATOR_BEAN_NAME,
									"");
					if (operationProviderBeanNames.size() < 1) {
						this.getLog()
								.error(new StringBuffer(128)
										.append("The list size of operation provider bean names has to be greater than 0 when using router type ")
										.append(RouterProperitesMojo.ROUTER_TYPE_RECIPIENT_LIST_ROUTER)
										.append(" in ")
										.append(file.getAbsolutePath())
										.append(".").toString());
					} else if (aggregatorBeanName.trim().length() == 0) {
						this.getLog()
								.error(new StringBuffer(128)
										.append("The aggregator bean names is empty in ")
										.append(file.getAbsolutePath())
										.append(".").toString());
					}
					String xml = this.getRouterRecipientListRouter(serviceId,
							operationName, channelNameWsRequest,
							channelNameWsResponse, operationProviderBeanNames,
							headerEnricherBeans, operationProviderChannelPairs,
							aggregatorBeanName, p);
					applicationRouterConfig.append(xml);
					this.write(serviceId, operationName, xml);
				} else if (routerType.trim().equals(
						RouterProperitesMojo.ROUTER_TYPE_CHANNEL_ROUTER)) {
					String channelOutgoing = p.getProperty(
							RouterProperitesMojo.ROUTER_CHANNEL_OUTGOING, "");
					String channelIncoming = p.getProperty(
							RouterProperitesMojo.ROUTER_CHANNEL_INCOMING, "");
					if (channelOutgoing.trim().length() == 0
							|| channelIncoming.trim().length() == 0) {
						this.getLog()
								.error(new StringBuffer(128)
										.append("The both channels ")
										.append(RouterProperitesMojo.ROUTER_CHANNEL_OUTGOING)
										.append(" and ")
										.append(RouterProperitesMojo.ROUTER_CHANNEL_INCOMING)
										.append(" need to be defined in ")
										.append(file.getAbsolutePath())
										.append(".").toString());
					}
					String xml = this.getRouterChannelRouter(serviceId,
							operationName, channelNameWsRequest,
							channelNameWsResponse, channelOutgoing,
							channelIncoming, p);
					applicationRouterConfig.append(xml);
					this.write(serviceId, operationName, xml);
				}
			} catch (IOException e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		}
		this.write(null, null, applicationRouterConfig.toString());
		this.getLog().debug("-execute");
	}

	/**
	 * @param p
	 * @return
	 */
	private List<SimpleEntry<String, String>> getOperationProviderChannelPairs(
			final Properties p) {
		List<SimpleEntry<String, String>> list = new ArrayList<SimpleEntry<String, String>>();
		SimpleEntry<String, String> entry;
		String suffix;
		for (String key : p.stringPropertyNames()) {
			if (key != null
					&& key.startsWith(RouterProperitesMojo.ROUTER_OPERATION_PROVIDER_OUTGOINGCHANNEL_NAME)) {
				entry = new SimpleEntry<String, String>(p.getProperty(key), "");
				suffix = key.substring(key.lastIndexOf('.'), key.length());
				for (String keyx : p.stringPropertyNames()) {
					if (keyx != null
							&& keyx.startsWith(RouterProperitesMojo.ROUTER_OPERATION_PROVIDER_INCOMINGCHANNEL_NAME)) {
						if (keyx.endsWith(suffix)) {
							entry.setValue(p.getProperty(keyx));
							list.add(entry);
							break;
						}
					}
				}
			}
		}
		return list;
	}

	private String getLastTransformerBeanName(final Properties p) {
		String lastTransformerBeanName = p
				.getProperty(RouterProperitesMojo.ROUTER_LAST_TRANSFORMER_BEAN_NAME);
		return lastTransformerBeanName;
	}

	/**
	 * @param p
	 * @return
	 */
	private List<SimpleEntry<String, String>> getHeaderEnricher(
			final Properties p) {
		List<SimpleEntry<String, String>> list = new ArrayList<SimpleEntry<String, String>>();
		SimpleEntry<String, String> entry;
		String suffix;
		for (String key : p.stringPropertyNames()) {
			if (key != null
					&& key.startsWith(RouterProperitesMojo.ROUTER_HEADER_ENRICHER_HEADER_NAME)) {
				entry = new SimpleEntry<String, String>(p.getProperty(key), "");
				suffix = key.substring(key.lastIndexOf('.'), key.length());
				for (String keyx : p.stringPropertyNames()) {
					if (keyx != null
							&& keyx.startsWith(RouterProperitesMojo.ROUTER_HEADER_ENRICHER_BEAN_NAME)) {
						if (keyx.endsWith(suffix)) {
							entry.setValue(p.getProperty(keyx));
							list.add(entry);
							break;
						}
					}
				}
			}
		}
		return list;
	}

	private List<String> getOperationProviderBeanNames(final Properties p) {
		List<String> list = new ArrayList<String>();
		String value;
		for (String key : p.stringPropertyNames()) {
			if (key != null
					&& key.startsWith(RouterProperitesMojo.ROUTER_OPERATION_PROVIDER_BEAN_NAME)) {
				value = p.getProperty(key);
				if (value != null && value.trim().length() > 0) {
					list.add(value.trim());
				}
			}
		}
		return list;
	}

	private String getRouterChannelRouter(final String serviceId,
			final String operationName, final String channelNameWsRequest,
			final String channelNameWsResponse, final String channelOutgoing,
			final String channelIncoming, final Properties p) {
		String transformerBeanName = new StringBuffer(64)
				.append("routingTranformerOf")
				.append(ServiceIdRegistry.capitalize(serviceId))
				.append(operationName).toString();

		StringBuffer sb = new StringBuffer(1024);

		sb.append("\t<bean id=\"");
		sb.append(transformerBeanName);
		sb.append("\" class=\"com.qpark.eip.core.spring.NoOperationTransformer\" />\n");

		sb.append("\t<int:transformer ref=\"");
		sb.append(transformerBeanName);
		sb.append("\" method=\"transform\"\n");
		sb.append("\t\tinput-channel=\"");
		sb.append(channelNameWsRequest);
		sb.append("\" \n");
		sb.append("\t\toutput-channel=\"");
		sb.append(channelOutgoing);
		sb.append("\"\n");
		sb.append("\t/>\n");

		String lastTransformerBeanName = this.getLastTransformerBeanName(p);
		if (lastTransformerBeanName != null) {
			transformerBeanName = lastTransformerBeanName;
		}

		sb.append("\t<int:transformer ref=\"");
		sb.append(transformerBeanName);
		sb.append("\" method=\"transform\"\n");
		sb.append("\t\tinput-channel=\"");
		sb.append(channelIncoming);
		sb.append("\" \n");
		sb.append("\t\toutput-channel=\"");
		sb.append(channelNameWsResponse);
		sb.append("\"\n");
		sb.append("\t/>\n");

		return sb.toString();
	}

	private String getRouterForward(final String serviceId,
			final String operationName, final String channelNameWsRequest,
			final String channelNameWsResponse,
			final List<String> operationProviderBeanNames, final Properties p) {

		String operationProviderMockClassName = p.getProperty(
				RouterProperitesMojo.ROUTER_OPERATION_PROVIDER_CLASS_NAME_MOCK,
				"");

		StringBuffer sb = new StringBuffer(1024);

		if (operationProviderMockClassName.trim().length() > 0) {
			sb.append("\t<!-- Using the mock since there is no real operation implementation. -->\n");
			sb.append("\t<bean id=\"");
			sb.append(operationProviderBeanNames.get(0));
			sb.append("\" class=\"");
			sb.append(operationProviderMockClassName);
			sb.append("\" />\n");
		}
		String lastTransformerBeanName = this.getLastTransformerBeanName(p);
		String serviceActivatorOutputChannel = channelNameWsResponse;
		if (lastTransformerBeanName != null) {
			serviceActivatorOutputChannel = new StringBuffer(
					"internalServiceActivatorOutputChannel").append(
					System.currentTimeMillis()).toString();
			sb.append("\t<int:transformer ref=\"");
			sb.append(lastTransformerBeanName);
			sb.append("\" method=\"transform\"\n");
			sb.append("\t\tinput-channel=\"");
			sb.append(serviceActivatorOutputChannel);
			sb.append("\" \n");
			sb.append("\t\toutput-channel=\"");
			sb.append(channelNameWsResponse);
			sb.append("\"\n");
			sb.append("\t/>\n");
			sb.append("\t<int:channel id=\"");
			sb.append(serviceActivatorOutputChannel);
			sb.append("\" />\n");
		}

		sb.append("\t<int:service-activator \n");
		sb.append("\t\tinput-channel=\"");
		sb.append(channelNameWsRequest);
		sb.append("\" \n");
		sb.append("\t\toutput-channel=\"");
		sb.append(serviceActivatorOutputChannel);
		sb.append("\" \n");
		sb.append("\t\tref=\"");
		sb.append(operationProviderBeanNames.get(0));
		sb.append("\" \n");
		sb.append("\t/>\n");

		return sb.toString();
	}

	private String getRouterRecipientListRouter(
			final String serviceId,
			final String operationName,
			final String channelNameWsRequest,
			final String channelNameWsResponse,
			final List<String> operationProviderBeanNames,
			final List<SimpleEntry<String, String>> headerEnricherBeans,
			final List<SimpleEntry<String, String>> operationProviderChannelPairs,
			final String aggregatorBeanName, final Properties p) {

		int numberOfRecipients = 0;
		StringBuffer recipients = new StringBuffer(128);
		StringBuffer serviceActivator = new StringBuffer(128);
		StringBuffer headerEnricher = new StringBuffer(128);
		StringBuffer headerFilter = new StringBuffer(128);
		StringBuffer channels = new StringBuffer(128);

		StringBuffer sb = new StringBuffer(1024);

		String releaseStrategyBeanName = new StringBuffer(32)
				.append(aggregatorBeanName).append("ReleaseStrategy")
				.append(System.currentTimeMillis()).toString();
		String aggregatorInputChannelName = new StringBuffer(32)
				.append("internal").append(Util.capitalize(aggregatorBeanName))
				.append("InputChannel").append(System.currentTimeMillis())
				.toString();
		String recipientListRouterChannelName = new StringBuffer(32)
				.append("internal")
				.append(ServiceIdRegistry.capitalize(serviceId))
				.append(operationName).append("RouterChannel").toString();
		channels.append("\t<int:publish-subscribe-channel id=\"");
		channels.append(aggregatorInputChannelName);
		channels.append("\" />\n");
		channels.append("\t<int:channel id=\"");
		channels.append(recipientListRouterChannelName);
		channels.append("\" />\n");

		String correlationHeaderEnricherOutputChannel = recipientListRouterChannelName;
		// <int:header-enricher input-channel="in" output-channel="out">
		// <int:header name="foo" method="computeValue" ref="myBean"/>
		if (!headerEnricherBeans.isEmpty()) {
			correlationHeaderEnricherOutputChannel = new StringBuffer(
					recipientListRouterChannelName).append(
					System.currentTimeMillis()).toString();
			channels.append("\t<int:channel id=\"");
			channels.append(correlationHeaderEnricherOutputChannel);
			channels.append("\" />\n");
			headerEnricher.append("\t<int:header-enricher input-channel=\"");
			headerEnricher.append(correlationHeaderEnricherOutputChannel);
			headerEnricher.append("\" output-channel=\"");
			headerEnricher.append(recipientListRouterChannelName);
			headerEnricher.append("\" >\n");
			for (SimpleEntry<String, String> headerEnricherBean : headerEnricherBeans) {
				headerEnricher.append("\t\t<int:header name=\"");
				headerEnricher.append(headerEnricherBean.getKey());
				headerEnricher.append("\" method=\"getHeaderValue\" ref=\"");
				headerEnricher.append(headerEnricherBean.getValue());
				headerEnricher.append("\" />\n");
			}
			headerEnricher.append("\t</int:header-enricher>\n");
		}

		for (String operationProviderBeanName : operationProviderBeanNames) {
			String operationChannelName = new StringBuffer(64)
					.append("internal")
					.append(ServiceIdRegistry.capitalize(serviceId))
					.append(operationName).append(System.currentTimeMillis())
					.toString();
			channels.append("\t<int:channel id=\"");
			channels.append(operationChannelName);
			channels.append("\" />\n");

			recipients.append("\t\t<int:recipient channel=\"");
			recipients.append(operationChannelName);
			recipients.append("\" />\n");
			numberOfRecipients++;

			serviceActivator.append("\t<int:service-activator ref=\"");
			serviceActivator.append(operationProviderBeanName);
			serviceActivator.append("\" \n");
			serviceActivator.append("\t\tinput-channel=\"");
			serviceActivator.append(operationChannelName);
			serviceActivator.append("\" \n");
			serviceActivator.append("\t\toutput-channel=\"");
			serviceActivator.append(aggregatorInputChannelName);
			serviceActivator.append("\" \n");
			serviceActivator.append("\t/>\n");

			waitOneMilli();
		}

		for (SimpleEntry<String, String> channelOutgoinIncoming : operationProviderChannelPairs) {
			recipients.append("\t\t<int:recipient channel=\"");
			recipients.append(channelOutgoinIncoming.getKey());
			recipients.append("\" />\n");
			numberOfRecipients++;

			headerFilter.append("\t<int:header-filter input-channel=\"");
			headerFilter.append(channelOutgoinIncoming.getValue());
			headerFilter.append("\" \n");
			headerFilter.append("\t\toutput-channel=\"");
			headerFilter.append(aggregatorInputChannelName);
			headerFilter.append("\" header-names=\"notSetHeaderToFilter\" \n");
			headerFilter.append("\t/>\n");
		}

		String lastTransformerBeanName = this.getLastTransformerBeanName(p);
		String aggregatorOutputChannelName = channelNameWsResponse;
		if (lastTransformerBeanName != null) {
			aggregatorOutputChannelName = new StringBuffer(
					"internalServiceActivatorOutputChannel").append(
					System.currentTimeMillis()).toString();
			sb.append("\t<int:transformer ref=\"");
			sb.append(lastTransformerBeanName);
			sb.append("\" method=\"transform\"\n");
			sb.append("\t\tinput-channel=\"");
			sb.append(aggregatorOutputChannelName);
			sb.append("\" \n");
			sb.append("\t\toutput-channel=\"");
			sb.append(channelNameWsResponse);
			sb.append("\"\n");
			sb.append("\t/>\n");
			channels.append("\t<int:channel id=\"");
			channels.append(aggregatorOutputChannelName);
			channels.append("\" />\n");
		}

		sb.append("\t<int:aggregator id=\"");
		sb.append(channelNameWsRequest);
		sb.append("Aggregator\" \n");
		sb.append("\t\tinput-channel=\"");
		sb.append(aggregatorInputChannelName);
		sb.append("\" \n");
		sb.append("\t\toutput-channel=\"");
		sb.append(aggregatorOutputChannelName);
		sb.append("\" \n");
		sb.append("\t\tref=\"");
		sb.append(aggregatorBeanName);
		sb.append("\" method=\"aggregate\" \n");

		// sb.append("\t\trelease-strategy-expression=\"size() == ");
		// sb.append(numberOfRecipients);
		// sb.append("\" \n");

		sb.append("\t\trelease-strategy=\"");
		// sb.append(releaseStrategyBeanName);
		sb.append(aggregatorBeanName);
		sb.append("\" release-strategy-method=\"canRelease\" \n");

		sb.append("\t\tcorrelation-strategy=\"");
		sb.append(aggregatorBeanName);
		sb.append("\" correlation-strategy-method=\"getCorrelationKey\" \n");
		sb.append("\t\tsend-partial-result-on-expiry=\"true\" \n");
		sb.append("\t/>\n");

		sb.append("\t<int:header-enricher input-channel=\"");
		sb.append(channelNameWsRequest);
		sb.append("\" output-channel=\"");
		sb.append(correlationHeaderEnricherOutputChannel);
		sb.append("\">\n");
		sb.append("\t\t<int:correlation-id expression=\"headers['id']\" />\n");
		// No need to create a new UUID!
		// sb.append("\t\t<int:correlation-id expression=\"T(java.util.UUID).randomUUID().toString()\" />\n");
		sb.append("\t\t<int:header name=\"sequenceSize\" expression=\"");
		sb.append(numberOfRecipients);
		sb.append("\" />\n");
		sb.append("\t</int:header-enricher>\n");

		sb.append(headerEnricher);

		sb.append("\t<int:recipient-list-router id=\"");
		sb.append(channelNameWsRequest);
		sb.append("Router\" \n");
		sb.append("\t\tinput-channel=\"");
		sb.append(recipientListRouterChannelName);
		sb.append("\"> \n");
		sb.append(recipients);
		sb.append("\t</int:recipient-list-router>\n");
		sb.append("\n");
		sb.append(serviceActivator);
		sb.append("\n");
		sb.append("\n");
		sb.append(headerFilter);
		sb.append("\n");

		sb.append(channels);

		/*
		 * sb.append("\t<bean id=\""); sb.append(releaseStrategyBeanName);
		 * sb.append(
		 * "\" class=\"org.springframework.integration.aggregator.MessageCountReleaseStrategy\" >\n"
		 * ); sb.append("\t\t<constructor-arg value=\"");
		 * sb.append(numberOfRecipients); sb.append("\" />\n");
		 * sb.append("\t</bean>\n");
		 */

		return sb.toString();
	}

	private String getXmlDefinition() {
		StringBuffer sb = new StringBuffer(512);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		sb.append("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("\txmlns:int=\"http://www.springframework.org/schema/integration\"\n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append("\t\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.version.xsd.version",
				DEFAULT_SPRING_FRAMEWORK_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\thttp://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration-");
		sb.append(this.project.getProperties().getProperty(
				"org.springframework.integration.version.xsd.version",
				DEFAULT_SPRING_INTEGRATION_XSD_VERSION));
		sb.append(".xsd\n");
		sb.append("\t\"\n");
		sb.append(">\n");
		return sb.toString();
	}

	private void write(final String serviceId, final String operationName,
			final String xml) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append(this.getXmlDefinition());
		sb.append("\t<!-- ");
		sb.append(Util.getGeneratedAt());
		sb.append(" -->\n");
		sb.append("");
		sb.append(xml);
		sb.append("</beans>\n");
		File f = null;
		if (serviceId == null && operationName == null) {
			f = Util.getFile(new File(this.project.getBasedir(), "target"),
					"applicationRouterConfig.xml");
		} else {
			f = Util.getFile(
					this.outputDirectory,
					new StringBuffer(32)
							.append(Util.lowerize(Util
									.getXjcClassName(serviceId)))
							.append(operationName).append("RouterConfig.xml")
							.toString());
		}
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
	}
}
