/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.router;

import java.io.File;
import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Create standard router definitions as property files for all not existing
 * definitions. These standard router properties are using the
 * {@link RouterProperitesMojo#ROUTER_TYPE_FORWARD} to the generated mock
 * operation providers.
 *
 * @author bhausen
 */
@Mojo(name = "generate-router-properties", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class RouterProperitesMojo extends AbstractMojo {
	public static final String ROUTER_CHANNEL_WS_REQUEST = "router.channel.ws.request";
	public static final String ROUTER_CHANNEL_WS_RESPONSE = "router.channel.ws.response";
	public static final String ROUTER_OPERATION_NAME = "router.operation.name";
	public static final String ROUTER_OPERATION_PROVIDER_BEAN_NAME = "router.operation.provider.bean.name";
	public static final String ROUTER_OPERATION_PROVIDER_INCOMINGCHANNEL_NAME = "router.operation.provider.channel.incoming.name";
	public static final String ROUTER_OPERATION_PROVIDER_OUTGOINGCHANNEL_NAME = "router.operation.provider.channel.outging.name";
	public static final String ROUTER_OPERATION_PROVIDER_CLASS_NAME_MOCK = "router.operation.provider.class.name.mock";
	public static final String ROUTER_RESPONSE_AGGREGATOR_BEAN_NAME = "router.response.aggregator.bean.name";
	public static final String ROUTER_CHANNEL_OUTGOING = "router.channel.outging.name";
	public static final String ROUTER_CHANNEL_INCOMING = "router.channel.incoming.name";
	public static final String ROUTER_SERVICE_ID = "router.service.id";
	public static final String ROUTER_TYPE = "router.type";
	public static final String ROUTER_HEADER_ENRICHER_HEADER_NAME = "router.header.enricher.header.name";
	public static final String ROUTER_HEADER_ENRICHER_BEAN_NAME = "router.header.enricher.bean.name";
	public static final String ROUTER_LAST_TRANSFORMER_BEAN_NAME = "router.response.last.transformer.bean.name";
	public static final String ROUTER_TYPE_FORWARD = "forward";
	public static final String ROUTER_TYPE_CHANNEL_ROUTER = "channel-router";
	public static final String ROUTER_TYPE_RECIPIENT_LIST_ROUTER = "recipient-list-router";
	public static final String ROUTER_TYPE_PAYLOAD_TYPE_ROUTER = "payload-type-router";
	public static final String ROUTER_TYPE_HEADER_VALUE_ROUTER = "header-value-router";
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	protected String basePackageName;
	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	@Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
	private String deltaPackageNameSuffix;
	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffix", defaultValue = "msg")
	protected String messagePackageNameSuffix;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/classes/router/definitions")
	protected File outputDirectory;
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	private String serviceId;
	/**
	 * The service request name need to end with this suffix (Default
	 * <code>Request</code>).
	 */
	@Parameter(property = "serviceRequestSuffix", defaultValue = "Request")
	private String serviceRequestSuffix;
	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	@Parameter(property = "serviceResponseSuffix", defaultValue = "Response")
	private String serviceResponseSuffix;

	private String getRouterProperties(final XsdsUtil xsds, final ElementType element) {
		StringBuffer sb = new StringBuffer(1024);

		ElementType elementResponse = XsdsUtil.findResponse(element, xsds.getElementTypes(), xsds);
		if (elementResponse != null) {
			ComplexType ctResponse = new ComplexType(elementResponse.getElement().getType(), xsds);
			if (ctResponse != null && !ctResponse.isSimpleType() && !ctResponse.isPrimitiveType()) {
				sb.append("# Router properties of ");
				sb.append(element.getServiceId());
				sb.append(".");
				sb.append(element.getOperationName());
				sb.append("\n");
				sb.append(Util.getGeneratedAtPropertiesComment(this.getClass(), this.eipVersion));
				sb.append(ROUTER_SERVICE_ID);
				sb.append("=");
				sb.append(element.getServiceId());
				sb.append("\n");
				sb.append(ROUTER_OPERATION_NAME);
				sb.append("=");
				sb.append(element.getOperationName());
				sb.append("\n");
				sb.append(ROUTER_CHANNEL_WS_REQUEST);
				sb.append("=");
				sb.append(element.getChannelNameWsRequest());
				sb.append("\n");
				sb.append(ROUTER_CHANNEL_WS_RESPONSE);
				sb.append("=");
				sb.append(element.getChannelNameWsResponse());
				sb.append("\n");
				sb.append(ROUTER_TYPE);
				sb.append("=");
				sb.append(ROUTER_TYPE_FORWARD);
				sb.append("\n");
				sb.append(ROUTER_OPERATION_PROVIDER_BEAN_NAME);
				sb.append(".0=");
				sb.append(element.getBeanIdMockOperationProvider());
				sb.append("\n");
				sb.append("# Only when using the a mock ");
				sb.append(ROUTER_OPERATION_PROVIDER_CLASS_NAME_MOCK);
				sb.append(" needs to be set.\n");
				sb.append(ROUTER_OPERATION_PROVIDER_CLASS_NAME_MOCK);
				sb.append("=");
				sb.append(element.getClassNameFullQualifiedMockOperationProvider());
				sb.append("\n");
				sb.append("#\n");
				sb.append("# Alternativly you can route to directly to a channel.\n");
				sb.append("# In this case you need to define an outgoing and incoming channel.\n");
				sb.append("#");
				sb.append(ROUTER_TYPE);
				sb.append("=");
				sb.append(ROUTER_TYPE_CHANNEL_ROUTER);
				sb.append("\n");
				sb.append("#");
				sb.append(ROUTER_CHANNEL_OUTGOING);
				sb.append("=");
				sb.append("\n");
				sb.append("#");
				sb.append(ROUTER_CHANNEL_INCOMING);
				sb.append("=");
				sb.append("\n");
				sb.append("#\n");
				sb.append(
						"# Or you can route to a list of beans or channels (request to output and response to input.\n");
				sb.append("#");
				sb.append(ROUTER_TYPE);
				sb.append("=");
				sb.append(ROUTER_TYPE_RECIPIENT_LIST_ROUTER);
				sb.append("\n");
				sb.append("#");
				sb.append(ROUTER_OPERATION_PROVIDER_BEAN_NAME);
				sb.append(".0=\n");
				sb.append("#");
				sb.append(ROUTER_OPERATION_PROVIDER_BEAN_NAME);
				sb.append(".1=\n");
				sb.append("#");
				sb.append(ROUTER_OPERATION_PROVIDER_OUTGOINGCHANNEL_NAME);
				sb.append(".0=\n");
				sb.append("#");
				sb.append(ROUTER_OPERATION_PROVIDER_INCOMINGCHANNEL_NAME);
				sb.append(".0=\n");
				sb.append("#");
				sb.append(ROUTER_OPERATION_PROVIDER_OUTGOINGCHANNEL_NAME);
				sb.append(".1=\n");
				sb.append("#");
				sb.append(ROUTER_OPERATION_PROVIDER_INCOMINGCHANNEL_NAME);
				sb.append(".1=\n");
				sb.append("#\n");
				sb.append("#");
				sb.append(ROUTER_TYPE);
				sb.append("=");
				sb.append(ROUTER_TYPE_PAYLOAD_TYPE_ROUTER);
				sb.append("\n");
				sb.append("#");
				sb.append(ROUTER_TYPE);
				sb.append("=");
				sb.append(ROUTER_TYPE_HEADER_VALUE_ROUTER);
				sb.append("\n");

				sb.append("#\n");
				sb.append(
						"# Additionaly a list header enricher bean names could be entered before routing the message.\n");
				sb.append(
						"# The ref bean needs to implement the method Object getHeaderValue(Message<JAXBElement<?>> message)\n");
				sb.append("#");
				sb.append(ROUTER_HEADER_ENRICHER_HEADER_NAME);
				sb.append(".0=\n");
				sb.append("#");
				sb.append(ROUTER_HEADER_ENRICHER_BEAN_NAME);
				sb.append(".0=\n");

				sb.append("#\n");
				sb.append("# In the case of multiple recipients of the request\n");
				sb.append("# you need to define an aggregator that combines the responses\n");
				sb.append("# to route the collected responses to the web service caller.\n");
				sb.append("#");
				sb.append(ROUTER_RESPONSE_AGGREGATOR_BEAN_NAME);
				sb.append("=\n");

				sb.append("#\n");
				sb.append(
						"# At least an last transformation could be run before releasing the response to the caller.\n");
				sb.append(
						"# The ref bean needs to implement the method Message<JAXBElement<?>> transform(Message<JAXBElement<?>> message)\n");
				sb.append("#");
				sb.append(ROUTER_LAST_TRANSFORMER_BEAN_NAME);
				sb.append("=\n");

			}
		}
		return sb.toString();
	}

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	private String eipVersion;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		XsdsUtil xsds = XsdsUtil.getInstance(this.baseDirectory, this.basePackageName, this.messagePackageNameSuffix,
				this.deltaPackageNameSuffix, this.serviceRequestSuffix, this.serviceResponseSuffix);

		if (this.project.getExecutionProject() != null) {
			this.eipVersion = this.project.getExecutionProject().getVersion();
		}

		String fileName;
		File f;
		Collection<String> serviceIds = ServiceIdRegistry.splitServiceIds(this.serviceId);
		if (serviceIds.size() == 0) {
			serviceIds = ServiceIdRegistry.getAllServiceIds();
		}
		for (String sid : serviceIds) {
			for (ElementType element : xsds.getElementTypes()) {
				if (element.isRequest() && ServiceIdRegistry.isValidServiceId(element.getServiceId(), sid)) {
					String s = this.getRouterProperties(xsds, element);
					fileName = new StringBuffer(32).append(Util.lowerize(Util.getXjcClassName(element.getServiceId())))
							.append(element.getOperationName()).append("RouterConfig.properties").toString();
					f = Util.getFile(this.outputDirectory, fileName);
					if (s != null && s.trim().length() > 0 && !f.exists()) {
						this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
						try {
							Util.writeToFile(f, s);
						} catch (Exception e) {
							this.getLog().error(e.getMessage());
							e.printStackTrace();
						}

					}
				}
			}
		}
		this.getLog().debug("-execute");
	}
}
