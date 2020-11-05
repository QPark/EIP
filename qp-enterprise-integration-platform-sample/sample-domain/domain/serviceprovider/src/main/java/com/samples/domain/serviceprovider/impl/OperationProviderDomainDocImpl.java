/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider.impl;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.qpark.eip.service.domain.doc.msg.GetClusterRequestType;
import com.qpark.eip.service.domain.doc.msg.GetClusterResponseType;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetDataTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetDataTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetFlowRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowResponseType;
import com.qpark.eip.service.domain.doc.msg.GetRevisionRequestType;
import com.qpark.eip.service.domain.doc.msg.GetRevisionResponseType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdResponseType;
import com.qpark.eip.service.domain.doc.msg.GetServiceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceResponseType;
import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetCluster;
import com.qpark.eip.service.domain.doc.msg.gateway.GetComplexType;
import com.qpark.eip.service.domain.doc.msg.gateway.GetDataType;
import com.qpark.eip.service.domain.doc.msg.gateway.GetElementType;
import com.qpark.eip.service.domain.doc.msg.gateway.GetFieldMappingType;
import com.qpark.eip.service.domain.doc.msg.gateway.GetFlow;
import com.qpark.eip.service.domain.doc.msg.gateway.GetFlowInterfaceMappingType;
import com.qpark.eip.service.domain.doc.msg.gateway.GetRevision;
import com.qpark.eip.service.domain.doc.msg.gateway.GetService;
import com.qpark.eip.service.domain.doc.msg.gateway.GetServiceId;
import com.qpark.eip.service.domain.doc.msg.gateway.GetTargetNamespace;
import com.samples.domain.serviceprovider.AppSecurityContextHandler;
import com.samples.domain.serviceprovider.OperationProviderDomainDoc;

/**
 * Operation provider of service <code>domain.doc</code>.
 *
 * @author bhausen
 */
@Component
public class OperationProviderDomainDocImpl implements OperationProviderDomainDoc {
	/** The {@link Logger}. */
	private final Logger logger = LoggerFactory.getLogger(OperationProviderDomainDocImpl.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** Authenticated? */
	@Autowired
	private AppSecurityContextHandler setSecurityContextAuth;
	/** Gateway to get cluster. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetClusterGateway")
	private GetCluster getCluster;
	/** Gateway to get complex type. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetComplexTypeGateway")
	private GetComplexType getComplexType;
	/** Gateway to get data type. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetDataTypeGateway")
	private GetDataType getDataType;
	/** Gateway to get element type. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetElementTypeGateway")
	private GetElementType getElementType;
	/** Gateway to get field mapping type. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetFieldMappingTypeGateway")
	private GetFieldMappingType getFieldMappingType;
	/** Gateway to get flow interface mapping type. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetFlowInterfaceMappingTypeGateway")
	private GetFlowInterfaceMappingType getFlowInterfaceMappingType;
	/** Gateway to get flow. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetFlowGateway")
	private GetFlow getFlow;
	/** Gateway to get revision. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetRevisionGateway")
	private GetRevision getRevision;
	/** Gateway to get service id. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetServiceIdGateway")
	private GetServiceId getServiceId;
	/** Gateway to get service. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetServiceGateway")
	private GetService getService;
	/** Gateway to get target namespace. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusDomainDocGetTargetNamespaceGateway")
	private GetTargetNamespace getTargetNamespace;

	/**
	 * @param start
	 * @return the duration in 000:00:00.000 format.
	 */
	private String requestDuration(final long start) {
		long millis = System.currentTimeMillis() - start;
		String hmss = String.format("%03d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				TimeUnit.MILLISECONDS.toMillis(millis)
						- TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		return hmss;
	}

	/** Initalize the request. */
	private void requestInit() {
		if (this.setSecurityContextAuth != null) {
			this.setSecurityContextAuth.setAppSecurityContextAuthentication();
		}
	}

	/** Finalize the request. */
	private void requestFinalization() {
	}
	/// ** Get the size of the response value. */
	// private String responseValueSize(Object value) {
	// String s = "null";
	// if (value != null) {
	// if (Collection.class.isInstance(value)) {
	// s = String.valueOf(((Collection<?>) value).size());
	// } else if (value.getClass().isArray()) {
	// s = String.valueOf(((Object[]) value).length);
	// } else {
	// s = "1";
	// }
	// }
	// return s;
	// }

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getCluster(com.qpark.eip.service.domain.doc.msg.GetClusterRequestType)
	 */
	@Override
	public GetClusterResponseType getCluster(final GetClusterRequestType request) {
		this.logger.debug("+getCluster");
		GetClusterResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetClusterResponseType> response = this.getCluster
					.invoke(this.of.createGetClusterRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getCluster duration {}", this.requestDuration(start));
			this.logger.debug("-getCluster");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getComplexType(com.qpark.eip.service.domain.doc.msg.GetComplexTypeRequestType)
	 */
	@Override
	public GetComplexTypeResponseType getComplexType(final GetComplexTypeRequestType request) {
		this.logger.debug("+getComplexType");
		GetComplexTypeResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetComplexTypeResponseType> response = this.getComplexType
					.invoke(this.of.createGetComplexTypeRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getComplexType duration {}", this.requestDuration(start));
			this.logger.debug("-getComplexType");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getDataType(com.qpark.eip.service.domain.doc.msg.GetDataTypeRequestType)
	 */
	@Override
	public GetDataTypeResponseType getDataType(final GetDataTypeRequestType request) {
		this.logger.debug("+getDataType");
		GetDataTypeResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetDataTypeResponseType> response = this.getDataType
					.invoke(this.of.createGetDataTypeRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getDataType duration {}", this.requestDuration(start));
			this.logger.debug("-getDataType");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getElementType(com.qpark.eip.service.domain.doc.msg.GetElementTypeRequestType)
	 */
	@Override
	public GetElementTypeResponseType getElementType(final GetElementTypeRequestType request) {
		this.logger.debug("+getElementType");
		GetElementTypeResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetElementTypeResponseType> response = this.getElementType
					.invoke(this.of.createGetElementTypeRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getElementType duration {}", this.requestDuration(start));
			this.logger.debug("-getElementType");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getFieldMappingType(com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeRequestType)
	 */
	@Override
	public GetFieldMappingTypeResponseType getFieldMappingType(final GetFieldMappingTypeRequestType request) {
		this.logger.debug("+getFieldMappingType");
		GetFieldMappingTypeResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetFieldMappingTypeResponseType> response = this.getFieldMappingType
					.invoke(this.of.createGetFieldMappingTypeRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getFieldMappingType duration {}", this.requestDuration(start));
			this.logger.debug("-getFieldMappingType");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getFlowInterfaceMappingType(com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeRequestType)
	 */
	@Override
	public GetFlowInterfaceMappingTypeResponseType getFlowInterfaceMappingType(
			final GetFlowInterfaceMappingTypeRequestType request) {
		this.logger.debug("+getFlowInterfaceMappingType");
		GetFlowInterfaceMappingTypeResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetFlowInterfaceMappingTypeResponseType> response = this.getFlowInterfaceMappingType
					.invoke(this.of.createGetFlowInterfaceMappingTypeRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getFlowInterfaceMappingType duration {}", this.requestDuration(start));
			this.logger.debug("-getFlowInterfaceMappingType");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getFlow(com.qpark.eip.service.domain.doc.msg.GetFlowRequestType)
	 */
	@Override
	public GetFlowResponseType getFlow(final GetFlowRequestType request) {
		this.logger.debug("+getFlow");
		GetFlowResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetFlowResponseType> response = this.getFlow.invoke(this.of.createGetFlowRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getFlow duration {}", this.requestDuration(start));
			this.logger.debug("-getFlow");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getRevision(com.qpark.eip.service.domain.doc.msg.GetRevisionRequestType)
	 */
	@Override
	public GetRevisionResponseType getRevision(final GetRevisionRequestType request) {
		this.logger.debug("+getRevision");
		GetRevisionResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetRevisionResponseType> response = this.getRevision
					.invoke(this.of.createGetRevisionRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getRevision duration {}", this.requestDuration(start));
			this.logger.debug("-getRevision");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getServiceId(com.qpark.eip.service.domain.doc.msg.GetServiceIdRequestType)
	 */
	@Override
	public GetServiceIdResponseType getServiceId(final GetServiceIdRequestType request) {
		this.logger.debug("+getServiceId");
		GetServiceIdResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetServiceIdResponseType> response = this.getServiceId
					.invoke(this.of.createGetServiceIdRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getServiceId duration {}", this.requestDuration(start));
			this.logger.debug("-getServiceId");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getService(com.qpark.eip.service.domain.doc.msg.GetServiceRequestType)
	 */
	@Override
	public GetServiceResponseType getService(final GetServiceRequestType request) {
		this.logger.debug("+getService");
		GetServiceResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetServiceResponseType> response = this.getService
					.invoke(this.of.createGetServiceRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getService duration {}", this.requestDuration(start));
			this.logger.debug("-getService");
		}
		return value;
	}

	/**
	 * @see com.samples.domain.serviceprovider.OperationProviderDomainDoc#getTargetNamespace(com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceRequestType)
	 */
	@Override
	public GetTargetNamespaceResponseType getTargetNamespace(final GetTargetNamespaceRequestType request) {
		this.logger.debug("+getTargetNamespace");
		GetTargetNamespaceResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetTargetNamespaceResponseType> response = this.getTargetNamespace
					.invoke(this.of.createGetTargetNamespaceRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getTargetNamespace duration {}", this.requestDuration(start));
			this.logger.debug("-getTargetNamespace");
		}
		return value;
	}
}