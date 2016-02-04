package com.qpark.maven.plugin.modelanalysis;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.model.analysis.Analysis;
import com.qpark.eip.core.model.analysis.UUIDProvider;
import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.ComplexMappingType;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.ComplexUUIDMappingType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.DefaultMappingType;
import com.qpark.eip.model.docmodel.DirectMappingType;
import com.qpark.eip.model.docmodel.DomainType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FieldType;
import com.qpark.eip.model.docmodel.FlowFilterType;
import com.qpark.eip.model.docmodel.FlowMapInOutType;
import com.qpark.eip.model.docmodel.FlowProcessType;
import com.qpark.eip.model.docmodel.FlowSubRequestType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.ObjectFactory;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.RequestResponseDataType;
import com.qpark.eip.model.docmodel.ServiceType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

public class AnalysisProvider {
	/** The prefix to identify flow filter input. */
	public static final String FLOW_PROCESS_PREFIX_IN = "in";
	/** The prefix to identify flow filter output. */
	public static final String FLOW_PROCESS_PREFIX_OUT = "out";
	/** The prefix to identify flow filter input. */
	public static final String FLOW_FILTER_PREFIX_IN = "filterIn";
	/** The prefix to identify flow filter output. */
	public static final String FLOW_FILTER_PREFIX_OUT = "filterOut";
	/** The prefix to identify flow sub request input. */
	public static final String FLOW_SUBREQUEST_PREFIX_IN = "subRequest";

	/** The prefix to identify flow sub request output. */
	public static final String FLOW_SUBREQUEST_PREFIX_OUT = "subResponse";

	/** The prefix to identify flow map method input. */
	public static final String FLOW_MAP_PREFIX_IN = "mapIn";

	/** The prefix to identify flow map method output. */
	public static final String FLOW_MAP_PREFIX_OUT = "mapOut";

	public static void main(final String[] args) {

		String xsdPath;
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain\\mapping\\target\\model";
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\com.ses.domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		// xsdPath =
		// "C:\\xnb\\dev\\git\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";
		// xsdPath =
		// "C:\\xnb\\dev\\38\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";

		String basePackageName = "com.ses.osp.bus";
		String modelVersion = "4.0.0";
		Analysis a = new AnalysisProvider().createEnterprise(basePackageName,
				modelVersion, basePackageName, xsdPath);

		try {
			ObjectFactory of = new ObjectFactory();
			JAXBElement<EnterpriseType> enterprise = of
					.createEnterprise(a.getEnterprise());
			JAXBContext context = JAXBContext
					.newInstance("com.qpark.eip.model.docmodel");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter sw = new StringWriter();
			marshaller.marshal(enterprise, sw);
			System.out.println(sw.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private UUIDProvider uuidProvider;

	private ObjectFactory of = new ObjectFactory();

	private XsdsUtil xsds;

	private Analysis analysis;

	private EnterpriseType enterprise;

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(AnalysisProvider.class);

	/**
	 * Create the {@link Analysis} out of the mode.
	 *
	 * @param enterpriseName
	 *            the name of the enterprise.
	 * @param basePackageName
	 *            the name of the base package, e.g <i>com.samples.platform</i>.
	 * @param modelPath
	 *            the path, where the model could be found.
	 * @return the {@link Analysis}.
	 */
	public Analysis createEnterprise(final String enterpriseName,
			final String modelVersion, final String basePackageName,
			final String modelPath) {
		XsdsUtil xsds = this.createXsdsUtil(basePackageName, modelPath);
		return this.createEnterprise(enterpriseName, modelVersion, xsds);
	}

	/**
	 * Create the {@link Analysis} out of the mode.
	 *
	 * @param enterpriseName
	 *            the name of the enterprise.
	 * @param xsds
	 *            the {@link XsdsUtil}.
	 * @return the {@link Analysis}.
	 */
	public Analysis createEnterprise(final String enterpriseName,
			final String modelVersion, final XsdsUtil xsds) {
		this.xsds = xsds;
		this.analysis = new Analysis(this.of.createEnterpriseType());
		this.enterprise = this.analysis.getEnterprise();
		this.enterprise.setName(enterpriseName);
		this.enterprise.setModelVersion(modelVersion);

		this.uuidProvider = new UUIDProvider(this.analysis);

		DomainType domain;
		ClusterType cluster;
		for (XsdContainer file : this.xsds.getXsdContainerMap().values()) {
			domain = this.getDomainType(file, modelVersion);
			cluster = this.getClusterType(domain, file);
		}
		for (com.qpark.maven.xmlbeans.ComplexType ct : this.xsds
				.getComplexTypes()) {
			this.getDataType(this.analysis.getCluster(ct.getTargetNamespace()),
					ct);
		}
		for (com.qpark.maven.xmlbeans.ComplexType ct : this.xsds
				.getComplexTypes()) {
			this.setFieldTypes(
					this.analysis.getCluster(ct.getTargetNamespace()), ct);
		}
		for (com.qpark.eip.model.docmodel.DataType dt : this.analysis
				.getDataTypes()) {

		}
		for (com.qpark.maven.xmlbeans.ElementType element : this.xsds
				.getElementTypes()) {
			this.getElementType(
					this.analysis.getCluster(element.getTargetNamespace()),
					element);
		}
		for (com.qpark.maven.xmlbeans.ElementType element : this.xsds
				.getElementTypes()) {
			if (element.isRequest()) {
				com.qpark.maven.xmlbeans.ElementType response = XsdsUtil
						.findResponse(element, this.xsds.getElementTypes(),
								this.xsds);
				if (response != null) {
					this.getServiceOperation(
							this.analysis
									.getCluster(element.getTargetNamespace()),
							element.getServiceId(), element.getOperationName(),
							this.analysis
									.getElementType(element.toQNameString()),
							this.analysis
									.getElementType(response.toQNameString()));
				}
			}
		}

		for (com.qpark.maven.xmlbeans.ComplexType ctRequest : this.xsds
				.getComplexTypes()) {
			if (ctRequest.isRequestType() && ctRequest.isFlowInputType()) {
				com.qpark.maven.xmlbeans.ComplexType ctResponse = XsdsUtil
						.findResponse(ctRequest, this.xsds.getComplexTypes(),
								this.xsds);
				if (ctResponse != null && ctResponse.isFlowOutputType()) {
					FlowType flow = this.getFlowType(
							this.analysis
									.getCluster(ctRequest.getTargetNamespace()),
							(ComplexType) this.analysis
									.getDataType(ctRequest.toQNameString()),
							(ComplexType) this.analysis
									.getDataType(ctResponse.toQNameString()));
					this.enterprise.getFlows().add(flow);
				}
			}
		}

		return this.analysis;
	}

	private XsdsUtil createXsdsUtil(final String basePackageName,
			final String modelPath) {
		File f = new File(modelPath);
		String messagePackageNameSuffix = "msg mapping flow";

		XsdsUtil xsds = new XsdsUtil(f, basePackageName,
				messagePackageNameSuffix, "delta");
		return xsds;
	}

	/**
	 * Get the {@link ClusterType} of the {@link XsdContainer}.
	 *
	 * @param domain
	 *            the {@link DomainType}.
	 * @param file
	 *            the {@link XsdContainer}.
	 * @return the {@link ClusterType}.
	 */
	private ClusterType getClusterType(final DomainType domain,
			final XsdContainer file) {
		ClusterType value = this.analysis.getCluster(file.getTargetNamespace());
		if (value == null) {
			value = this.of.createClusterType();
			value.setName(file.getTargetNamespace());
			value.setModelVersion(domain.getModelVersion());
			this.uuidProvider.setUUID(value);
			value.setParentId(domain.getId());
			domain.getCluster().add(value);

			value.setDescription(file.getAnnotationDocumentation());
			value.setFileName(file.getFile().getName());
			value.setPackageName(file.getPackageName());
			value.setVersion(file.getVersion());
			value.getWarning().addAll(file.getWarnings());
		}
		return value;
	}

	private DataType getDataType(final ClusterType cluster,
			final com.qpark.maven.xmlbeans.ComplexType ct) {
		String elemId = this.uuidProvider.getDataTypeUUID(ct.toQNameString(),
				cluster.getModelVersion());
		DataType value = (DataType) this.analysis.get(elemId);

		if (value != null) {
			// Noting to do.
		} else if (ct.getTargetNamespace()
				.equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)
				&& this.analysis.getDataType(ct.toQNameString()) == null) {
			DataType dt = this.of.createDataType();
			dt.setName(ct.toQNameString());
			this.setDataType(cluster.getModelVersion(), ct, dt);
			this.uuidProvider.setUUID(dt);
			this.enterprise.getBasicDataTypes().add(dt);
		} else if (ct.isDefaultMappingType()) {
			DefaultMappingType x = this.of.createDefaultMappingType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			x.setDefaultValue(ct.getDefaultValue());
			cluster.getDefaultMappingType().add(x);
			value = x;
		} else if (ct.isDirectMappingType()) {
			DirectMappingType x = this.of.createDirectMappingType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			x.setAccessor("Accessor-TBD");
			cluster.getDirectMappingType().add(x);
			value = x;
		} else if (ct.isComplexMappingType()) {
			ComplexMappingType x = this.of.createComplexMappingType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			cluster.getComplexMappingType().add(x);
			value = x;
		} else if (ct.isComplexUUIDMappingType()) {
			ComplexUUIDMappingType x = this.of.createComplexUUIDMappingType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			cluster.getComplexUUIDMappingType().add(x);
			value = x;
		} else if (ct.isInterfaceMappingType()) {
			InterfaceMappingType x = this.of.createInterfaceMappingType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			cluster.getInterfaceMappingType().add(x);
			value = x;
		} else if (this.analysis.get(elemId) == null) {
			ComplexType x = this.of.createComplexType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			x.setIsFlowInputType(ct.isFlowInputType());
			x.setIsFlowOutputType(ct.isFlowOutputType());
			x.setIsMappingRequestType(ct.isMapRequestType());
			x.setIsMappingResponseType(ct.isMapResponseType());
			cluster.getComplexType().add(x);
			if (ct.getParent() != null) {
				DataType parent = this.getDataType(cluster, ct.getParent());
				x.setDescendedFromId(parent.getId());
			}
			for (com.qpark.maven.xmlbeans.ComplexType innerCt : ct
					.getInnerTypeDefs()) {
				this.getDataType(cluster, innerCt);
			}
			value = x;
		}
		return value;
	}

	/**
	 * Get the {@link DomainType} of the {@link XsdContainer}. If the
	 * {@link DomainType} is not present at this stage, it will be created.
	 *
	 * @param file
	 *            the {@link XsdContainer}.
	 * @return the {@link DomainType}.
	 */
	private DomainType getDomainType(final XsdContainer file,
			final String modelVersion) {
		DomainType value = this.analysis
				.getDomainType(file.getDomainPathName());
		if (value == null) {
			value = this.of.createDomainType();
			value.setName(file.getDomainPathName());
			value.setModelVersion(modelVersion);
			this.uuidProvider.setUUID(value);
			this.enterprise.getDomains().add(value);
		}
		return value;
	}

	private ElementType getElementType(final ClusterType cluster,
			final com.qpark.maven.xmlbeans.ElementType element) {
		ElementType value = this.of.createElementType();
		value.setDescription(element.getAnnotationDocumentation());
		value.setName(element.toQNameString());
		value.setNamespace(element.getTargetNamespace());
		value.setModelVersion(cluster.getModelVersion());
		value.setParentId(cluster.getId());
		this.uuidProvider.setUUID(value);
		cluster.getElementType().add(value);
		return value;
	}

	private List<FieldType> getFieldTypes(final ClusterType cluster,
			final com.qpark.maven.xmlbeans.ComplexType element,
			final String parentId) {
		List<FieldType> value = new ArrayList<FieldType>();
		for (ComplexTypeChild child : element.getChildren()) {
			DataType dt = this.getDataType(cluster, child.getComplexType());
			FieldType field = this.of.createFieldType();
			field.setParentId(parentId);
			field.setModelVersion(cluster.getModelVersion());
			field.setName(child.getChildName());
			field.setCardinality(child.getCardinality());
			field.setCardinalityMaxOccurs(child.getMaxOccurs() == null ? null
					: child.getMaxOccurs().intValue());
			field.setCardinalityMinOccurs(child.getMinOccurs() == null ? null
					: child.getMinOccurs().intValue());
			field.setDescription(child.getAnnotationDocumentation());
			field.setFieldTypeDefinitionId(dt == null ? null : dt.getId());
			field.setListField(child.isList());
			field.setNamespace(element.getTargetNamespace());
			field.setOptionalField(child.isOptional());
			this.uuidProvider.setUUID(field);
			value.add(field);

		}
		return value;
	}

	/**
	 * Get name of the {@link RequestResponseDataType}.
	 *
	 * @param fieldName
	 *            the name of the field.
	 * @param prefix
	 *            the prefix to subtract.
	 * @return the name.
	 */
	private static String getFlowMethodName(final String fieldName,
			final String prefix) {
		String suffix = "";
		if (fieldName.equals(prefix)) {
			suffix = "";
		} else {
			suffix = fieldName.substring(prefix.length(), fieldName.length());
		}
		if (suffix.contains("#")) {
			suffix = suffix.substring(0, suffix.indexOf('#'));
		}
		return suffix;
	}

	private FlowProcessType getFlowProcessType(final String modelVersion,
			final String parentId, final String name, final ComplexType ct) {
		FlowProcessType value = null;
		List<RequestResponseDataType> rrs = this.getFlowRequestResponse(ct,
				FLOW_PROCESS_PREFIX_IN, FLOW_PROCESS_PREFIX_OUT, modelVersion);
		if (rrs.size() > 0) {
			value = this.of.createFlowProcessType();
			value.setName(name);
			value.setModelVersion(modelVersion);
			value.setParentId(parentId);
			value.setNamespace(ct.getNamespace());
			this.uuidProvider.setUUID(value);

			rrs.get(0).setParentId(value.getId());
			value.setRequestResponse(rrs.get(0));

			rrs = this.getFlowRequestResponse(ct, FLOW_SUBREQUEST_PREFIX_IN,
					FLOW_SUBREQUEST_PREFIX_OUT, modelVersion);
			for (RequestResponseDataType rr : rrs) {
				FlowSubRequestType sub = this.of.createFlowSubRequestType();
				sub.setName(rr.getName());
				sub.setNamespace(rr.getNamespace());
				sub.setParentId(value.getId());
				sub.setModelVersion(modelVersion);
				this.uuidProvider.setUUID(sub);
				rr.setParentId(sub.getId());
				sub.setSubRequestInOut(rr);

				value.getSubRequest().add(sub);
			}

			rrs = this.getFlowRequestResponse(ct, FLOW_FILTER_PREFIX_IN,
					FLOW_FILTER_PREFIX_IN, modelVersion);
			for (RequestResponseDataType rr : rrs) {
				FlowFilterType filter = this.of.createFlowFilterType();
				filter.setName(rr.getName());
				filter.setNamespace(rr.getNamespace());
				filter.setParentId(value.getId());
				filter.setModelVersion(modelVersion);
				this.uuidProvider.setUUID(filter);

				rr.setParentId(filter.getId());
				filter.setFilterInOut(rr);

				value.getFilter().add(filter);
			}

			rrs = this.getFlowRequestResponse(ct, FLOW_MAP_PREFIX_IN,
					FLOW_MAP_PREFIX_OUT, modelVersion);
			for (RequestResponseDataType rr : rrs) {
				FlowMapInOutType mapInOut = this.of.createFlowMapInOutType();
				mapInOut.setName(rr.getName());
				mapInOut.setNamespace(rr.getNamespace());
				mapInOut.setParentId(value.getId());
				mapInOut.setModelVersion(modelVersion);
				this.uuidProvider.setUUID(mapInOut);

				rr.setParentId(mapInOut.getId());
				mapInOut.setMapInOut(rr);

				value.getMapInOut().add(mapInOut);

				for (RequestResponseDataType rrx : rrs) {
					this.setFlowMapInOutTypeInterfaceMappingIds(mapInOut,
							rrx.getRequestId());
					this.setFlowMapInOutTypeInterfaceMappingIds(mapInOut,
							rrx.getResponseId());
				}
			}
		}
		return value;
	}

	/**
	 * Get the list of {@link FlowRequestResponseNameContainer}s out of the flow
	 * input or output type.
	 *
	 * @param ct
	 *            the {@link DataType} which is the flow input or output type.
	 * @param prefixIn
	 *            the prefix of the request part.
	 * @param prefixOut
	 *            the prefix of the response part.
	 * @return the {@link FlowRequestResponseNameContainer}.
	 */
	private List<RequestResponseDataType> getFlowRequestResponse(
			final ComplexType ct, final String prefixIn, final String prefixOut,
			final String modelVersion) {
		List<RequestResponseDataType> requestResponses = new ArrayList<RequestResponseDataType>();
		RequestResponseDataType requestResponse = null;
		DataType request = null;
		DataType response = null;
		String name;
		Set<String> inChildrenFound = new TreeSet<String>();
		for (FieldType childOut : ct.getField()) {
			if (childOut.getName().startsWith(prefixOut)) {
				name = getFlowMethodName(childOut.getName(), prefixOut);
				for (FieldType childIn : ct.getField()) {
					if (childIn.getName().equals(new StringBuffer(16)
							.append(prefixIn).append(name).toString())) {
						request = (DataType) this.analysis
								.get(childIn.getFieldTypeDefinitionId());
						response = (DataType) this.analysis
								.get(childOut.getFieldTypeDefinitionId());
						requestResponse = this.getRequestResponseDataType(
								modelVersion, null, request, response);
						if (name.trim().length() == 0) {
							name = new StringBuffer(childIn.getName())
									.append(childOut.getName()).toString();
						}
						requestResponse.setName(name);
						requestResponses.add(requestResponse);
						inChildrenFound.add(
								new StringBuffer(requestResponse.getRequestId())
										.append(requestResponse.getRequestId())
										.toString());
					}
				}
			}
		}
		for (FieldType childIn : ct.getField()) {
			if (childIn.getName().startsWith(prefixIn)
					&& !inChildrenFound.contains(childIn.getName())) {
				name = getFlowMethodName(childIn.getName(), prefixIn);
				for (FieldType childOut : ct.getField()) {
					if (childOut.getName().equals(new StringBuffer(16)
							.append(prefixOut).append(name).toString())) {
						request = (DataType) this.analysis
								.get(childIn.getFieldTypeDefinitionId());
						response = (DataType) this.analysis
								.get(childOut.getFieldTypeDefinitionId());
						requestResponse = this.getRequestResponseDataType(
								modelVersion, null, request, response);
						if (name.trim().length() == 0) {
							name = new StringBuffer(childIn.getName())
									.append(childOut.getName()).toString();
						}
						requestResponse.setName(name);
						if (!inChildrenFound.contains(
								new StringBuffer(requestResponse.getRequestId())
										.append(requestResponse.getRequestId())
										.toString())) {
							requestResponses.add(requestResponse);
							inChildrenFound.add(requestResponse.getName());
						}
					}
				}
			}
		}
		return requestResponses;
	}

	private FlowType getFlowType(final ClusterType cluster,
			final ComplexType ctRequest, final ComplexType ctResponse) {
		FlowType value = this.of.createFlowType();
		/* Parent is enterprise which does not have an id. */
		value.setClusterId(cluster.getId());
		value.setModelVersion(cluster.getModelVersion());
		value.setName(ctRequest.getJavaClassName().substring(0,
				ctRequest.getJavaClassName().lastIndexOf("RequestType")));
		value.setNamespace(ctRequest.getNamespace());
		value.setDescription(ctRequest.getDescription());

		this.uuidProvider.setUUID(value);

		value.setInvokeFlowDefinition(
				this.getRequestResponseDataType(cluster.getModelVersion(),
						value.getId(), ctRequest, ctResponse));

		value.setExecuteRequest(
				this.getFlowProcessType(cluster.getModelVersion(),
						value.getId(), "executeRequest", ctRequest));
		value.setProcessResponse(
				this.getFlowProcessType(cluster.getModelVersion(),
						value.getId(), "processResponse", ctResponse));

		DataType dataType;
		DataType ct;
		for (FieldType field : ctRequest.getField()) {
			dataType = (DataType) this.analysis
					.get(field.getFieldTypeDefinitionId());
			if (dataType != null && DataType.class.isInstance(dataType)) {
				ct = dataType;
			}
		}
		return value;
	}

	private RequestResponseDataType getRequestResponseDataType(
			final String modelVersion, final String parentId,
			final DataType request, final DataType response) {
		RequestResponseDataType value = this.of.createRequestResponseDataType();
		value.setName(new StringBuffer(128).append(request.getName())
				.append("#").append(response.getName()).toString());
		value.setParentId(parentId);
		value.setModelVersion(modelVersion);
		value.setNamespace(request.getNamespace());
		value.setRequestId(request.getId());
		value.setResponseId(response.getId());

		this.uuidProvider.setUUID(value);
		return value;
	}

	private OperationType getServiceOperation(final ClusterType cluster,
			final String serviceId, final String operationName,
			final ElementType request, final ElementType response) {
		ServiceType service = this.analysis.getServiceType(serviceId);
		if (service == null) {
			DomainType domain = (DomainType) this.analysis
					.get(cluster.getParentId());
			service = this.of.createServiceType();
			service.setName(serviceId);
			service.setModelVersion(cluster.getModelVersion());
			this.uuidProvider.setUUID(service);
			service.setClusterId(cluster.getId());
			service.setDescription("");
			service.setNamespace(cluster.getName());
			service.setPackageName(cluster.getPackageName());
			service.setServiceId(serviceId);

			domain.getService().add(service);
		}

		OperationType value = this.of.createOperationType();
		if (request.getDescription() != null
				&& response.getDescription() != null) {
			value.setDescription(
					new StringBuffer(128).append(request.getDescription())
							.append(response.getDescription()).toString());
		} else if (request.getDescription() != null) {
			value.setDescription(request.getDescription());
		} else if (response.getDescription() != null) {
			value.setDescription(response.getDescription());
		}
		value.setName(new StringBuffer(128).append(cluster.getPackageName())
				.append(".").append(operationName).toString());
		value.setNamespace(cluster.getName());
		value.setModelVersion(cluster.getModelVersion());
		value.setParentId(service.getId());
		value.setRequest(request);
		value.setResponse(response);

		this.uuidProvider.setUUID(value);
		service.getOperation().add(value);
		return value;
	}

	private void setDataType(final String modelVersion,
			final com.qpark.maven.xmlbeans.ComplexType element,
			final DataType value) {
		value.setDescription(element.getAnnotationDocumentation());
		value.setName(element.toQNameString());
		value.setModelVersion(modelVersion);
		value.setNamespace(element.getTargetNamespace());
		value.setJavaClassName(element.getClassNameFullQualified());
		value.setJavaPackageName(element.getPackageName());
		this.uuidProvider.setUUID(value);
	}

	private void setFieldMappingType(final FieldMappingType type,
			final List<FieldType> fields) {
		FieldType returnField = null;
		for (FieldType f : fields) {
			if (f.getName().equals("return")) {
				returnField = f;
			}
		}
		if (returnField != null) {
			type.setReturnValueTypeId(returnField.getFieldTypeDefinitionId());
		}
		type.getInput().addAll(fields);
	}

	private void setFieldTypes(final ClusterType cluster,
			final com.qpark.maven.xmlbeans.ComplexType ct) {
		String elemId = this.uuidProvider.getDataTypeUUID(ct.toQNameString(),
				cluster.getModelVersion());
		DataType dt = (DataType) this.analysis.get(elemId);

		List<FieldType> fields = this.getFieldTypes(cluster, ct, elemId);
		if (ComplexType.class.isInstance(dt)) {
			((ComplexType) dt).getField().addAll(fields);
		} else if (InterfaceMappingType.class.isInstance(dt)) {
			((InterfaceMappingType) dt).getFieldMappings().addAll(fields);
		} else if (FieldMappingType.class.isInstance(dt)) {
			this.setFieldMappingType((FieldMappingType) dt, fields);
		}
	}

	private void setFlowMapInOutTypeInterfaceMappingIds(
			final FlowMapInOutType mapInOut, final String dataTypeId) {
		DataType dt = (DataType) this.analysis.get(dataTypeId);
		if (dt != null && ComplexType.class.isInstance(dt)) {
			for (FieldType field : ((ComplexType) dt).getField()) {
				DataType dtx = (DataType) this.analysis
						.get(field.getFieldTypeDefinitionId());
				if (dtx != null && InterfaceMappingType.class.isInstance(dtx)
						&& !mapInOut.getInterfaceMappingId()
								.contains(field.getFieldTypeDefinitionId())) {
					mapInOut.getInterfaceMappingId()
							.add(field.getFieldTypeDefinitionId());
				}
			}
		}
	}
}
