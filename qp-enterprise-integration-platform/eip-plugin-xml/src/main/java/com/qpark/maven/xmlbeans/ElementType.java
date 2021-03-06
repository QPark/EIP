/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.NodeList;

/**
 * @author bhausen
 */
public class ElementType implements Comparable<ElementType> {
	/** Fixed value for web service channel name definition. */
	public static final String WEB_SERVICE_CHANNEL_NAME_CONTENT = "WsChannel";
	/** Fixed value for web service channel name prefix. */
	public static final String WEB_SERVICE_CHANNEL_NAME_PREFIX = "eip";
	/** Fixed value for web service channel name request suffix. */
	public static final String WEB_SERVICE_CHANNEL_NAME_REQUEST_SUFFIX = "Request";
	/** Fixed value for web service channel name response suffix. */
	public static final String WEB_SERVICE_CHANNEL_NAME_RESPONSE_SUFFIX = "Response";
	private final String annotationDocumentation;
	private final String beanIdMockOperationProvider;
	private final String beanIdOperationProvider;
	private final String beanIdWsInboundGateway;
	private final String channelNameWsRequest;
	private final String channelNameWsResponse;
	private final String channelSecurityPatternOperation;
	private final String channelSecurityPatternService;
	private final String classNameFqGateway;
	private final String classNameFqObject;
	private final String classNameFullQualifiedMockOperationProvider;
	private final String classNameGateway;
	private final String classNameMockOperationProvider;
	private final String classNameObject;
	private ComplexType complexType;
	private final SchemaGlobalElement element;
	private final boolean isRequest;
	private final String methodName;
	private final String operationName;
	private final String packageName;
	private final String packageNameGateway;
	private final String packageNameMockOperationProvider;
	private final String serviceId;

	/**
	 * @param elem
	 * @param config
	 */
	public ElementType(final SchemaGlobalElement elem, final XsdsUtil config) {
		this.element = elem;
		/* Object definition. */
		this.packageName = config.getPackageName(this.element.getName());
		config.getPackageNames().add(this.packageName);
		this.classNameObject = this.element.getName().getLocalPart();
		this.classNameFqObject = new StringBuffer(128).append(this.packageName)
				.append(".").append(this.getClassNameObject()).toString();

		/* Service Operation definition. */
		int index = this.getClassNameObject()
				.lastIndexOf(config.getServiceRequestSuffix());
		if (index > 0) {
			this.isRequest = true;
			this.operationName = this.getClassNameObject().substring(0, index);
		} else {
			this.isRequest = false;
			index = this.getClassNameObject()
					.lastIndexOf(config.getServiceResponseSuffix());
			if (index > 0) {
				this.operationName = this.getClassNameObject().substring(0,
						index);
			} else {
				this.operationName = "";
			}
		}
		if (this.operationName.length() > 0) {
			this.methodName = new StringBuffer()
					.append(this.operationName.substring(0, 1).toLowerCase())
					.append(this.operationName.substring(1,
							this.operationName.length()))
					.toString();
		} else {
			this.methodName = "";
		}

		/* Mock operation provider definition. */
		if (this.operationName.length() > 0) {
			this.classNameMockOperationProvider = new StringBuffer(128)
					.append(this.operationName).append("MockOperation")
					.toString();
		} else {
			this.classNameMockOperationProvider = new StringBuffer(128)
					.append(this.element.getName().getLocalPart())
					.append("MockOperation").toString();
		}
		if (this.packageName.contains(config.getMessagePackageNameSuffix())) {
			this.packageNameMockOperationProvider = this.packageName.substring(
					0,
					this.packageName.length()
							- config.getMessagePackageNameSuffix().length()
							- 1);
		} else {
			this.packageNameMockOperationProvider = this.packageName;
		}
		this.classNameFullQualifiedMockOperationProvider = new StringBuffer(128)
				.append(this.packageNameMockOperationProvider).append(".")
				.append(this.getClassNameMockOperationProvider()).toString();

		/* Service gateway definition. */
		if (this.operationName.length() > 0) {
			this.classNameGateway = new StringBuffer(128)
					.append(this.operationName).toString();
		} else {
			this.classNameGateway = new StringBuffer(128)
					.append(this.element.getName().getLocalPart()).toString();
		}
		if (this.packageName.contains(config.getMessagePackageNameSuffix())) {
			this.packageNameGateway = new StringBuffer(128)
					.append(this.packageName.substring(0,
							this.packageName.length() - config
									.getMessagePackageNameSuffix().length()
									- 1))
					.append(".gateway").toString();
		} else {
			this.packageNameGateway = new StringBuffer(128)
					.append(this.packageName).append(".gateway").toString();
		}
		if (this.element.getAnnotation() != null
				&& this.element.getAnnotation().getUserInformation() != null
				&& this.element.getAnnotation()
						.getUserInformation().length > 0) {
			final StringBuffer sb = new StringBuffer(124);
			for (final XmlObject u : this.element.getAnnotation()
					.getUserInformation()) {
				if (u.getDomNode() != null) {
					final NodeList nl = u.getDomNode().getChildNodes();
					for (int i = 0; i < nl.getLength(); i++) {
						if (i > 0 && sb.length() > 0) {
							sb.append(" ");
						}
						sb.append(nl.item(i).getNodeValue());
					}
				}
			}
			if (sb.length() > 0) {
				this.annotationDocumentation = sb.toString().trim();
			} else {
				this.annotationDocumentation = null;
			}
		} else {
			this.annotationDocumentation = null;
		}

		this.classNameFqGateway = new StringBuffer(128)
				.append(this.packageNameGateway).append(".")
				.append(this.getClassNameGateway()).toString();

		/* Service id definition. */
		this.serviceId = config.getServiceIdRegistry().getServiceId(
				this.packageName, this.getTargetNamespace(),
				config.getMessagePackageNameSuffix(),
				config.getDeltaPackageNameSuffix());

		this.beanIdOperationProvider = new StringBuffer(64)
				.append("operationProvider")
				.append(ServiceIdRegistry.capitalize(this.serviceId))
				.append(this.operationName).toString();
		this.beanIdMockOperationProvider = new StringBuffer(
				this.beanIdOperationProvider.length() + 4)
						.append(this.beanIdOperationProvider).append("Mock")
						.toString();

		final String serviceChannelNameStart = new StringBuffer(16)
				.append(WEB_SERVICE_CHANNEL_NAME_PREFIX)
				.append(ServiceIdRegistry.capitalize(this.serviceId))
				.toString();
		final String operationChannelNameStart = new StringBuffer(64)
				.append(serviceChannelNameStart).append(this.operationName)
				.append(WEB_SERVICE_CHANNEL_NAME_CONTENT).toString();

		this.channelSecurityPatternService = new StringBuffer(64)
				.append(serviceChannelNameStart).append(".*?")
				.append(WEB_SERVICE_CHANNEL_NAME_CONTENT).append(".*")
				.toString();
		this.channelSecurityPatternOperation = new StringBuffer(64)
				.append(operationChannelNameStart).append(".*").toString();

		this.channelNameWsRequest = new StringBuffer(64)
				.append(operationChannelNameStart)
				.append(WEB_SERVICE_CHANNEL_NAME_REQUEST_SUFFIX).toString();
		this.channelNameWsResponse = new StringBuffer(64)
				.append(operationChannelNameStart)
				.append(WEB_SERVICE_CHANNEL_NAME_RESPONSE_SUFFIX).toString();

		this.beanIdWsInboundGateway = new StringBuffer(64)
				.append(this.serviceId).append(this.operationName)
				.append("WsInboundGateway").toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final ElementType o) {
		if (this == o) {
			return 0;
		} else if (o == null) {
			return -1;
		} else {
			return XsdsUtil.getQNameComparator().compare(
					this.getElement().getName(), o.getElement().getName());
		}
	}

	/**
	 * @return the annotationDocumentation - never <code>null</code>.
	 */
	public String getAnnotationDocumentation() {
		return this.annotationDocumentation == null ? ""
				: this.annotationDocumentation;
	}

	/**
	 * @return the beanNameMockOperationProvider
	 */
	public String getBeanIdMockOperationProvider() {
		return this.beanIdMockOperationProvider;
	}

	/**
	 * @return the beanName
	 */
	public String getBeanIdOperationProvider() {
		return this.beanIdOperationProvider;
	}

	/**
	 * @return the beanIdGateway
	 */
	public String getBeanIdWsInboundGateway() {
		return this.beanIdWsInboundGateway;
	}

	/**
	 * @return the channelNameRequestWs
	 */
	public String getChannelNameWsRequest() {
		return this.channelNameWsRequest;
	}

	/**
	 * @return the channelNameResponseWs
	 */
	public String getChannelNameWsResponse() {
		return this.channelNameWsResponse;
	}

	/**
	 * @return the channelSecurityPatternOperation
	 */
	public String getChannelSecurityPatternOperation() {
		return this.channelSecurityPatternOperation;
	}

	/**
	 * @return the channelSecurityPatternService
	 */
	public String getChannelSecurityPatternService() {
		return this.channelSecurityPatternService;
	}

	/**
	 * @return the classNameFqGateway
	 */
	public String getClassNameFullQualifiedGateway() {
		return this.classNameFqGateway;
	}

	/**
	 * @return the classNameFullQualifiedMockOperationProvider
	 */
	public String getClassNameFullQualifiedMockOperationProvider() {
		return this.classNameFullQualifiedMockOperationProvider;
	}

	/**
	 * @return the fqObjectClassName
	 */
	public String getClassNameFullQualifiedObject() {
		return this.classNameFqObject;
	}

	/**
	 * @return the classNameGateway
	 */
	public String getClassNameGateway() {
		return this.classNameGateway;
	}

	/**
	 * @return the classNameMockOperationProvider
	 */
	public String getClassNameMockOperationProvider() {
		return this.classNameMockOperationProvider;
	}

	/**
	 * @return the ObjectClassName
	 */
	public String getClassNameObject() {
		return this.classNameObject;
	}

	/**
	 * @return the defining {@link ComplexType}.
	 */
	public ComplexType getComplexType() {
		return this.complexType;
	}

	/**
	 * @return the element
	 */
	public SchemaGlobalElement getElement() {
		return this.element;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 * @return the serviceName
	 */
	public String getOperationName() {
		return this.operationName;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the packageNameGateway
	 */
	public String getPackageNameGateway() {
		return this.packageNameGateway;
	}

	/**
	 * @return the packageNameMockOperationProvider
	 */
	public String getPackageNameMockOperationProvider() {
		return this.packageNameMockOperationProvider;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return this.serviceId;
	}

	/**
	 * @return the target name space of the element.
	 */
	public String getTargetNamespace() {
		return this.element.getName().getNamespaceURI();
	}

	/**
	 * @return is request or not.
	 */
	public boolean isRequest() {
		return this.isRequest;
	}

	/**
	 * Set the defining {@link ComplexType}.
	 *
	 * @param complexType
	 *                        the {@link ComplexType}.
	 */
	public void setComplexType(final ComplexType complexType) {
		this.complexType = complexType;
	}

	/**
	 * @return the {@link QName} string representation.
	 */
	public String toQNameString() {
		if (this.element.getName() == null) {
			return String.valueOf(this.element);
		} else {
			return this.element.getName().toString();
		}
	}
}
