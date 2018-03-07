/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.modelanalysis;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import com.qpark.eip.model.docmodel.FlowRuleType;
import com.qpark.eip.model.docmodel.FlowSubRequestType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.ObjectFactory;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.RequestResponseDataType;
import com.qpark.eip.model.docmodel.ServiceType;
import com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicBoolean;

public class AnalysisProvider implements DataProviderModelAnalysis {
	private static class RequestResponseDataFieldContainer {
		FieldType childIn;
		FieldType childOut;
		RequestResponseDataType rr;

		RequestResponseDataFieldContainer(final RequestResponseDataType rr,
				final FieldType childIn, final FieldType childOut) {
			this.rr = rr;
			this.childIn = childIn;
			this.childOut = childOut;
		}
	}

	/** The prefix to identify flow filter input. */
	public static final String FLOW_FILTER_PREFIX_IN = "filterIn";
	/** The prefix to identify flow filter output. */
	public static final String FLOW_FILTER_PREFIX_OUT = "filterOut";
	/** The prefix to identify flow map method input. */
	public static final String FLOW_MAP_PREFIX_IN = "mapIn";
	/** The prefix to identify flow map method output. */
	public static final String FLOW_MAP_PREFIX_OUT = "mapOut";
	/** The prefix to identify flow filter input. */
	public static final String FLOW_PROCESS_PREFIX_IN = "in";
	/** The prefix to identify flow filter output. */
	public static final String FLOW_PROCESS_PREFIX_OUT = "out";

	/** The prefix to identify flow rule input. */
	public static final String FLOW_RULE_PREFIX_IN = "ruleIn";

	/** The prefix to identify flow rule output. */
	public static final String FLOW_RULE_PREFIX_OUT = "ruleOut";

	/** The prefix to identify flow sub request input. */
	public static final String FLOW_SUBREQUEST_PREFIX_IN = "subRequest";

	/** The prefix to identify flow sub request output. */
	public static final String FLOW_SUBREQUEST_PREFIX_OUT = "subResponse";

	private static XsdsUtil createXsdsUtil(final String basePackageName,
			final String modelPath) {
		final File f = new File(modelPath);
		final String messagePackageNameSuffix = "msg mapping flow";

		final XsdsUtil xsds = XsdsUtil.getInstance(f, basePackageName,
				messagePackageNameSuffix, "delta");
		return xsds;
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

	// private XsdsUtil xsds;

	public static void main(final String[] args) {

		String xsdPath;
		xsdPath = "C:\\xnb\\dev\\git\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\bus-dev\\src\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\xnb\\dev\\git\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";

		final String basePackageName = "com.samples.platform";
		final String modelVersion = "4.0.0";
		final Analysis a = new AnalysisProvider().createEnterprise(
				basePackageName, modelVersion, basePackageName, xsdPath);
		// System.exit(0);
		try {
			final ObjectFactory of = new ObjectFactory();
			final JAXBElement<EnterpriseType> enterprise = of
					.createEnterprise(a.getEnterprise());
			final JAXBContext context = JAXBContext
					.newInstance("com.qpark.eip.model.docmodel");
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter sw = new StringWriter();
			marshaller.marshal(enterprise, sw);
			System.out.println(sw.toString());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean testFlowExecutionFieldName(final String fieldName,
			final String prefixIn, final String prefixOut,
			final String flowExecutionName) {
		boolean value = false;
		value = String.format("%s%s", prefixIn, flowExecutionName).toString()
				.equals(fieldName)
				|| prefixIn.equals(fieldName)
						&& String.format("%s%s", prefixIn, prefixOut).toString()
								.equals(flowExecutionName);
		return value;
	}

	private Analysis analysis;

	private EnterpriseType enterprise;

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(AnalysisProvider.class);

	private final ObjectFactory of = new ObjectFactory();

	private UUIDProvider uuidProvider;

	@SuppressWarnings("static-method")
	private void addFlowExecutionOrder(final String modelVersion,
			final String parentId, final String name, final ComplexType ct,
			final FlowProcessType value) {
		for (final FieldType field : ct.getField()) {
			final String fieldName = field.getName();
			value.getFilter().stream()
					.filter(f -> testFlowExecutionFieldName(fieldName,
							FLOW_FILTER_PREFIX_IN, FLOW_FILTER_PREFIX_OUT,
							f.getName()))
					.findFirst()
					.ifPresent(f -> value.getExecutionOrder().add(f.getId()));
			value.getRule().stream()
					.filter(f -> testFlowExecutionFieldName(fieldName,
							FLOW_RULE_PREFIX_IN, FLOW_RULE_PREFIX_OUT,
							f.getName()))
					.findFirst()
					.ifPresent(f -> value.getExecutionOrder().add(f.getId()));
			value.getMapInOut().stream()
					.filter(f -> testFlowExecutionFieldName(fieldName,
							FLOW_MAP_PREFIX_IN, FLOW_MAP_PREFIX_OUT,
							f.getName()))
					.findFirst()
					.ifPresent(f -> value.getExecutionOrder().add(f.getId()));
			value.getSubRequest().stream()
					.filter(f -> testFlowExecutionFieldName(fieldName,
							FLOW_SUBREQUEST_PREFIX_IN,
							FLOW_SUBREQUEST_PREFIX_OUT, f.getName()))
					.findFirst()
					.ifPresent(f -> value.getExecutionOrder().add(f.getId()));
		}
	}

	private void addFlowFilter(final String modelVersion, final String parentId,
			final String name, final ComplexType ct,
			final FlowProcessType value) {
		final List<RequestResponseDataFieldContainer> rrdfs = this
				.getFlowRequestResponse(ct, FLOW_FILTER_PREFIX_IN,
						FLOW_FILTER_PREFIX_OUT, modelVersion);
		for (final RequestResponseDataFieldContainer rrdf : rrdfs) {
			final FlowFilterType filter = this.of.createFlowFilterType();
			filter.setName(rrdf.rr.getName());
			filter.setNamespace(ct.getNamespace());
			filter.setParentId(value.getId());
			filter.setModelVersion(modelVersion);
			this.uuidProvider.setUUID(filter);

			rrdf.rr.setParentId(filter.getId());
			filter.setFilterInOut(rrdf.rr);
			if (Objects.nonNull(rrdf.childIn)) {
				filter.setFilterInFieldDescription(
						rrdf.childIn.getDescription());
			}
			if (Objects.nonNull(rrdf.childOut)) {
				filter.setFilterOutFieldDescription(
						rrdf.childOut.getDescription());
			}
			value.getFilter().add(filter);
		}
	}

	private void addFlowMapping(final String modelVersion,
			final String parentId, final String name, final ComplexType ct,
			final FlowProcessType value) {
		final List<RequestResponseDataFieldContainer> rrdfs = this
				.getFlowRequestResponse(ct, FLOW_MAP_PREFIX_IN,
						FLOW_MAP_PREFIX_OUT, modelVersion);
		for (final RequestResponseDataFieldContainer rrdf : rrdfs) {
			final FlowMapInOutType mapInOut = this.of.createFlowMapInOutType();
			mapInOut.setName(rrdf.rr.getName());
			mapInOut.setNamespace(ct.getNamespace());
			mapInOut.setParentId(value.getId());
			mapInOut.setModelVersion(modelVersion);
			this.uuidProvider.setUUID(mapInOut);

			rrdf.rr.setParentId(mapInOut.getId());
			mapInOut.setMapInOut(rrdf.rr);
			if (Objects.nonNull(rrdf.childIn)) {
				mapInOut.setMapInFieldDescription(
						rrdf.childIn.getDescription());
			}
			if (Objects.nonNull(rrdf.childOut)) {
				mapInOut.setMapOutFieldDescription(
						rrdf.childOut.getDescription());
			}
			value.getMapInOut().add(mapInOut);

			this.setFlowMapInOutTypeInterfaceMappingIds(mapInOut,
					rrdf.rr.getRequestId());
			this.setFlowMapInOutTypeInterfaceMappingIds(mapInOut,
					rrdf.rr.getResponseId());
		}
	}

	private void addFlowRule(final String modelVersion, final String parentId,
			final String name, final ComplexType ct,
			final FlowProcessType value) {
		final List<RequestResponseDataFieldContainer> rrdfs = this
				.getFlowRequestResponse(ct, FLOW_RULE_PREFIX_IN,
						FLOW_RULE_PREFIX_OUT, modelVersion);
		for (final RequestResponseDataFieldContainer rrdf : rrdfs) {
			final FlowRuleType rule = this.of.createFlowRuleType();
			rule.setName(rrdf.rr.getName());
			rule.setNamespace(ct.getNamespace());
			rule.setParentId(value.getId());
			rule.setModelVersion(modelVersion);
			this.uuidProvider.setUUID(rule);

			rrdf.rr.setParentId(rule.getId());
			rule.setRuleInOut(rrdf.rr);
			if (Objects.nonNull(rrdf.childIn)) {
				rule.setRuleInFieldDescription(rrdf.childIn.getDescription());
			}
			if (Objects.nonNull(rrdf.childOut)) {
				rule.setRuleOutFieldDescription(rrdf.childOut.getDescription());
			}
			value.getRule().add(rule);
		}
	}

	private void addFlowSubRequest(final String modelVersion,
			final String parentId, final String name, final ComplexType ct,
			final FlowProcessType value) {
		final List<RequestResponseDataFieldContainer> rrdfs = this
				.getFlowRequestResponse(ct, FLOW_SUBREQUEST_PREFIX_IN,
						FLOW_SUBREQUEST_PREFIX_OUT, modelVersion);
		for (final RequestResponseDataFieldContainer rrdf : rrdfs) {
			final FlowSubRequestType sub = this.of.createFlowSubRequestType();
			sub.setName(rrdf.rr.getName());
			sub.setNamespace(ct.getNamespace());
			sub.setParentId(value.getId());
			sub.setModelVersion(modelVersion);
			this.uuidProvider.setUUID(sub);
			rrdf.rr.setParentId(sub.getId());
			sub.setSubRequestInOut(rrdf.rr);
			if (Objects.nonNull(rrdf.childIn)) {
				sub.setSubRequestFieldDescription(
						rrdf.childIn.getDescription());
			}
			if (Objects.nonNull(rrdf.childOut)) {
				sub.setSubResponseFieldDescription(
						rrdf.childOut.getDescription());
			}
			value.getSubRequest().add(sub);
		}
	}

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
		return this.createEnterprise(enterpriseName, modelVersion,
				createXsdsUtil(basePackageName, modelPath));
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
		this.logger.debug("+createEnterprise {} {}", enterpriseName,
				modelVersion);
		this.analysis = new Analysis(this.of.createEnterpriseType());
		this.enterprise = this.analysis.getEnterprise();
		this.enterprise.setName(enterpriseName);
		this.enterprise.setModelVersion(modelVersion);

		this.uuidProvider = new UUIDProvider(this.analysis);

		xsds.getXsdContainerMap().values().stream().forEach(file -> {
			final DomainType domain = this.parseDomainType(file, modelVersion);
			this.parseClusterType(domain, file);
		});
		xsds.getComplexTypes().stream().forEach(ct -> this.parseDataType(
				this.analysis.getCluster(ct.getTargetNamespace()), ct));
		xsds.getComplexTypes().stream().forEach(ct -> this.setFieldTypes(
				this.analysis.getCluster(ct.getTargetNamespace()), ct));
		xsds.getElementTypes().stream().forEach(et -> this.getElementType(
				this.analysis.getCluster(et.getTargetNamespace()), et));
		xsds.getElementTypes().stream().filter(et -> et.isRequest())
				.forEach(etRequest -> {
					final com.qpark.maven.xmlbeans.ElementType etResponse = XsdsUtil
							.findResponse(etRequest, xsds.getElementTypes(),
									xsds);
					if (Objects.nonNull(etResponse)) {
						String ctRequestDescription = null;
						if (Objects.nonNull(etRequest.getComplexType())) {
							ctRequestDescription = etRequest.getComplexType()
									.getAnnotationDocumentation();
						}
						String ctResponseDescription = null;
						if (Objects.nonNull(etResponse.getComplexType())) {
							ctResponseDescription = etResponse.getComplexType()
									.getAnnotationDocumentation();
						}
						this.getServiceOperation(
								this.analysis.getCluster(
										etRequest.getTargetNamespace()),
								etRequest.getServiceId(),
								etRequest.getOperationName(),
								this.analysis.getElementType(
										etRequest.toQNameString()),
								ctRequestDescription,
								this.analysis.getElementType(
										etResponse.toQNameString()),
								ctResponseDescription);
					}
				});
		xsds.getComplexTypes().stream()
				.filter(ct -> ct.isRequestType() && ct.isFlowInputType())
				.forEach(ctRequest -> {
					final com.qpark.maven.xmlbeans.ComplexType ctResponse = XsdsUtil
							.findResponse(ctRequest, xsds.getComplexTypes(),
									xsds);
					if (Objects.nonNull(ctResponse)
							&& ctResponse.isFlowOutputType()) {
						final FlowType flow = this.getFlowType(
								this.analysis.getCluster(
										ctRequest.getTargetNamespace()),
								(ComplexType) this.analysis
										.getDataType(ctRequest.toQNameString()),
								(ComplexType) this.analysis.getDataType(
										ctResponse.toQNameString()));
						this.enterprise.getFlows().add(flow);
					}
				});
		xsds.getComplexTypes().stream().parallel()
				.filter(ct -> Objects.nonNull(ct.getBaseComplexType())
						&& !ct.getBaseComplexType().isPrimitiveType())
				.forEach(ct -> {
					final DataType dt = this.analysis
							.getDataType(ct.toQNameString());
					final DataType dtp = this.analysis.getDataType(
							ct.getBaseComplexType().toQNameString());
					if (Objects.nonNull(dt)
							&& ComplexType.class.isInstance(dt)) {
						if (Objects.nonNull(dtp)) {
							((ComplexType) dt).setDescendedFromId(dtp.getId());
						}
					}
				});
		this.analysis.getDataTypes().stream().parallel()
				.filter(dt -> ComplexType.class.isInstance(dt))
				.filter(dt -> !dt.getNamespace()
						.equals("http://www.w3.org/2001/XMLSchema"))
				.filter(dt -> dt.getJavaPackageName().isEmpty()
						|| dt.getJavaPackageName().startsWith("java"))
				.forEach(dt -> {
					this.analysis.getDataTypes().stream()
							.filter(dtx -> dtx.getNamespace()
									.equals("http://www.w3.org/2001/XMLSchema")
									&& dtx.getJavaClassName()
											.equals(dt.getJavaClassName()))
							.findFirst().ifPresent(dtx -> ((ComplexType) dt)
									.setDescendedFromId(dtx.getId()));
				});
		this.analysis.getDataTypes().stream()
				.filter(dt -> FieldMappingType.class.isInstance(dt))
				.map(dt -> (FieldMappingType) dt).forEach(fm -> {
					final Set<String> fieldMappingIds = new TreeSet<>();
					final Map<String, ComplexType> ctMap = new HashMap<>();
					fieldMappingIds.addAll(fm.getInput().stream()
							.filter(in -> Objects.nonNull(in)
									&& Objects.nonNull(in.getName())
									&& !in.getName().equals("value")
									&& !in.getName().equals("return")
									&& !in.getName().equals("interfaceName")
									&& Objects.nonNull(
											in.getFieldTypeDefinitionId()))
							.map(in -> in.getFieldTypeDefinitionId())
							.collect(Collectors.toList()));
					this.getFieldMappingInputTypes(fieldMappingIds, ctMap);
					ctMap.values().stream()
							.filter(ct -> Objects.nonNull(ct.getName()))
							.forEach(ct -> fm.getFieldMappingInputType()
									.add(ct.getId()));
				});
		this.analysis.getDataTypes().stream()
				.filter(dt -> InterfaceMappingType.class.isInstance(dt))
				.map(dt -> (InterfaceMappingType) dt).forEach(inf -> {
					final Set<String> fieldMappingIds = new TreeSet<>();
					final Map<String, ComplexType> ctMap = new HashMap<>();
					fieldMappingIds.addAll(inf.getFieldMappings().stream()
							.map(fm -> fm.getFieldTypeDefinitionId())
							.collect(Collectors.toList()));
					this.getFieldMappingInputTypes(fieldMappingIds, ctMap);
					ctMap.values().stream()
							.filter(ct -> Objects.nonNull(ct.getName()))
							.forEach(ct -> inf.getFieldMappingInputType()
									.add(ct.getId()));
				});
		this.logger.debug("-createEnterprise {} {}", enterpriseName,
				modelVersion);
		return this.analysis;
	}

	private void getFieldMappingInputTypes(final Set<String> fieldMappingIds,
			final Map<String, ComplexType> ctMap) {
		final List<FieldMappingType> fieldMappings = new ArrayList<>();
		fieldMappingIds.stream().forEach(id -> {
			final DataType dt = this.analysis.getDataTypeById(id);
			if (FieldMappingType.class.isInstance(dt)) {
				fieldMappings.add((FieldMappingType) dt);
			} else if (ComplexType.class.isInstance(dt)) {
				ctMap.put(dt.getId(), (ComplexType) dt);
			}
		});
		if (Objects.nonNull(fieldMappings) && fieldMappings.size() > 0) {
			final Set<String> ids = new TreeSet<>();
			fieldMappings.stream().filter(fm -> Objects.nonNull(fm))
					.forEach(fm -> fm.getInput().stream()
							.filter(i -> Objects.nonNull(i.getName())
									&& !i.getName().equals("interfaceName")
									&& !i.getName().equals("value")
									&& !i.getName().equals("return"))
							.forEach(i -> ids
									.add(i.getFieldTypeDefinitionId())));
			this.analysis.getDataTypes().stream()
					.filter(dt -> ComplexType.class.isInstance(dt))
					.map(dt -> (ComplexType) dt)
					.filter(ct -> ids.contains(ct.getId()))
					.forEach(ct -> ctMap.put(ct.getId(), ct));
			this.getFieldMappingInputTypes(ids, ctMap);
		}
	}

	private ElementType getElementType(final ClusterType cluster,
			final com.qpark.maven.xmlbeans.ElementType element) {
		final ElementType value = this.of.createElementType();
		value.setDescription(element.getAnnotationDocumentation());
		value.setName(element.toQNameString());
		value.setNamespace(element.getTargetNamespace());
		value.setModelVersion(cluster.getModelVersion());
		value.setParentId(cluster.getId());
		if (element.getElement().getType() != null) {
			final String elemId = this.uuidProvider.getDataTypeUUID(
					String.valueOf(element.getElement().getType().getName()),
					cluster.getModelVersion());
			final DataType dt = (DataType) this.analysis.get(elemId);
			if (dt != null) {
				value.setComplexTypeId(dt.getId());
			}
		}
		this.uuidProvider.setUUID(value);
		cluster.getElementType().add(value);
		return value;
	}

	private List<FieldType> getFieldTypes(final ClusterType cluster,
			final com.qpark.maven.xmlbeans.ComplexType element,
			final String parentId) {
		final List<FieldType> value = new ArrayList<>();
		int sequenceNumber = 0;
		for (final ComplexTypeChild child : element.getChildren()) {
			final DataType dt = this.parseDataType(cluster,
					child.getComplexType());
			final FieldType field = this.of.createFieldType();
			field.setParentId(parentId);
			field.setModelVersion(cluster.getModelVersion());
			field.setName(child.getChildName());
			field.setSequenceNumber(sequenceNumber);
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
			sequenceNumber++;
		}
		return value;
	}

	private FlowProcessType getFlowProcessType(final String modelVersion,
			final String parentId, final String name, final ComplexType ct) {
		final List<RequestResponseDataFieldContainer> rrdfs = this
				.getFlowRequestResponse(ct, FLOW_PROCESS_PREFIX_IN,
						FLOW_PROCESS_PREFIX_OUT, modelVersion);
		if (rrdfs.size() <= 0) {
			return null;
		} else {
			final FlowProcessType value = this.of.createFlowProcessType();
			value.setName(name);
			value.setModelVersion(modelVersion);
			value.setParentId(parentId);
			value.setNamespace(ct.getNamespace());
			this.uuidProvider.setUUID(value);

			rrdfs.get(0).rr.setParentId(value.getId());
			value.setRequestResponse(rrdfs.get(0).rr);
			if (Objects.nonNull(rrdfs.get(0).childIn)) {
				value.setRequestFieldDescription(
						rrdfs.get(0).childIn.getDescription());
			}
			if (Objects.nonNull(rrdfs.get(0).childOut)) {
				value.setResponseFieldDescription(
						rrdfs.get(0).childOut.getDescription());
			}

			this.addFlowSubRequest(modelVersion, parentId, name, ct, value);
			this.addFlowFilter(modelVersion, parentId, name, ct, value);
			this.addFlowRule(modelVersion, parentId, name, ct, value);
			this.addFlowMapping(modelVersion, parentId, name, ct, value);
			this.addFlowExecutionOrder(modelVersion, parentId, name, ct, value);

			return value;
		}
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
	private List<RequestResponseDataFieldContainer> getFlowRequestResponse(
			final ComplexType ct, final String prefixIn, final String prefixOut,
			final String modelVersion) {
		final List<RequestResponseDataFieldContainer> requestResponses = new ArrayList<>();
		RequestResponseDataFieldContainer rrf;
		RequestResponseDataType requestResponse = null;
		DataType request = null;
		DataType response = null;
		String name;
		final Set<String> inChildrenFound = new TreeSet<>();
		for (final FieldType childOut : ct.getField()) {
			if (childOut.getName().startsWith(prefixOut)) {
				name = getFlowMethodName(childOut.getName(), prefixOut);
				for (final FieldType childIn : ct.getField()) {
					if (childIn.getName().equals(new StringBuffer(16)
							.append(prefixIn).append(name).toString())) {
						request = (DataType) this.analysis
								.get(childIn.getFieldTypeDefinitionId());
						response = (DataType) this.analysis
								.get(childOut.getFieldTypeDefinitionId());
						requestResponse = this.getRequestResponseDataType(
								modelVersion, null, request, response);
						if (name.trim().length() == 0) {
							name = String.format("%s%s", prefixIn, prefixOut)
									.toString();
						}
						requestResponse.setName(name);
						rrf = new RequestResponseDataFieldContainer(
								requestResponse, childIn, childOut);
						requestResponses.add(rrf);
						inChildrenFound.add(
								new StringBuffer(requestResponse.getRequestId())
										.append(requestResponse.getRequestId())
										.toString());
					}
				}
			}
		}
		for (final FieldType childIn : ct.getField()) {
			if (childIn.getName().startsWith(prefixIn)
					&& !inChildrenFound.contains(childIn.getName())) {
				name = getFlowMethodName(childIn.getName(), prefixIn);
				for (final FieldType childOut : ct.getField()) {
					if (childOut.getName().equals(new StringBuffer(16)
							.append(prefixOut).append(name).toString())) {
						request = (DataType) this.analysis
								.get(childIn.getFieldTypeDefinitionId());
						response = (DataType) this.analysis
								.get(childOut.getFieldTypeDefinitionId());
						requestResponse = this.getRequestResponseDataType(
								modelVersion, null, request, response);
						if (name.trim().length() == 0) {
							name = String.format("%s%s", prefixIn, prefixOut)
									.toString();
						}
						requestResponse.setName(name);
						if (!inChildrenFound.contains(
								new StringBuffer(requestResponse.getRequestId())
										.append(requestResponse.getRequestId())
										.toString())) {
							rrf = new RequestResponseDataFieldContainer(
									requestResponse, childIn, childOut);
							requestResponses.add(rrf);
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
		final FlowType value = this.of.createFlowType();
		/* Parent is enterprise which does not have an id. */
		value.setClusterId(cluster.getId());
		value.setModelVersion(cluster.getModelVersion());
		value.setName(ctRequest.getJavaClassName().substring(0,
				ctRequest.getJavaClassName().lastIndexOf("RequestType")));
		value.setNamespace(ctRequest.getNamespace());
		value.setDescription(ctRequest.getDescription());
		value.setShortName(value.getName()
				.substring(value.getName().lastIndexOf('.') + 1));

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

		return value;
	}

	private RequestResponseDataType getRequestResponseDataType(
			final String modelVersion, final String parentId,
			final DataType request, final DataType response) {
		final RequestResponseDataType value = this.of
				.createRequestResponseDataType();
		value.setName(new StringBuffer(128).append(request.getName())
				.append("#").append(response.getName()).toString());
		value.setParentId(parentId);
		value.setModelVersion(modelVersion);
		value.setNamespace(request.getNamespace());
		value.setRequestId(request.getId());
		value.setRequestDescription(request.getDescription());
		value.setResponseId(response.getId());
		value.setResponseDescription(response.getDescription());

		this.uuidProvider.setUUID(value);
		return value;
	}

	private OperationType getServiceOperation(final ClusterType cluster,
			final String serviceId, final String operationName,
			final ElementType request, final String ctRequestDescription,
			final ElementType response, final String ctResponseDescription) {
		ServiceType service = this.analysis.getServiceType(serviceId);
		if (service == null) {
			final DomainType domain = (DomainType) this.analysis
					.get(cluster.getParentId());
			service = this.of.createServiceType();
			service.setName(serviceId);
			service.setServiceId(serviceId);
			service.setModelVersion(cluster.getModelVersion());
			this.uuidProvider.setUUID(service);
			service.setClusterId(cluster.getId());
			service.setDescription(cluster.getDescription());
			service.setNamespace(cluster.getName());
			service.setPackageName(cluster.getPackageName());
			service.setSecurityRoleName(
					String.format("ROLE_%s", serviceId.toUpperCase()));

			domain.getService().add(service);
		}

		final OperationType value = this.of.createOperationType();
		value.setName(new StringBuffer(128).append(cluster.getPackageName())
				.append(".").append(operationName).toString());
		value.setNamespace(cluster.getName());
		value.setModelVersion(cluster.getModelVersion());
		value.setParentId(service.getId());
		value.setSecurityRoleName(String.format("%s_%s",
				service.getSecurityRoleName(), operationName.toUpperCase()));
		value.setShortName(operationName);
		value.setRequestFieldDescription(ctRequestDescription);
		value.setResponseFieldDescription(ctResponseDescription);
		final RequestResponseDataType rr = this.getRequestResponseDataType(
				cluster.getModelVersion(), service.getId(), request, response);
		value.setRequestResponse(rr);

		this.uuidProvider.setUUID(value);
		service.getOperation().add(value);
		return value;
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
	private ClusterType parseClusterType(final DomainType domain,
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

	/**
	 * @param ct
	 * @param cluster
	 * @return the UUID string.
	 */
	private String getDataTypeUUID(
			final com.qpark.maven.xmlbeans.ComplexType ct,
			final ClusterType cluster) {
		return this.uuidProvider.getDataTypeUUID(ct.toQNameString(),
				cluster != null ? cluster.getModelVersion()
						: ct.getTargetNamespace());
	}

	private DataType parseDataType(final ClusterType cluster,
			final com.qpark.maven.xmlbeans.ComplexType ct) {
		if (Objects.isNull(cluster)) {
			this.logger.info("ComplexType cluster null {} tns {}",
					ct.getQNameLocalPart(), ct.getTargetNamespace());
		}

		final String elemId = this.getDataTypeUUID(ct, cluster);
		DataType value = (DataType) this.analysis.get(elemId);

		if (value != null) {
			// Noting to do.
		} else if (ct.getTargetNamespace()
				.equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)
				&& this.analysis.getDataType(ct.toQNameString()) == null) {
			final DataType dt = this.of.createDataType();
			dt.setName(ct.toQNameString());
			this.setDataType("1.0", ct, dt);
			this.uuidProvider.setUUID(dt);
			this.enterprise.getBasicDataTypes().add(dt);
		} else if (ct.isDefaultMappingType()) {
			final DefaultMappingType x = this.of.createDefaultMappingType();
			x.setParentId(cluster.getId());
			x.setMappingType("default");
			this.setDataType(cluster.getModelVersion(), ct, x);
			this.setDefaultMappingType(cluster.getModelVersion(), ct, x);
			cluster.getDefaultMappingType().add(x);
			value = x;
		} else if (ct.isDirectMappingType()) {
			final DirectMappingType x = this.of.createDirectMappingType();
			x.setParentId(cluster.getId());
			x.setMappingType("direct");
			this.setDataType(cluster.getModelVersion(), ct, x);
			this.setDirectMappingType(cluster.getModelVersion(), ct, x);
			cluster.getDirectMappingType().add(x);
			value = x;
		} else if (ct.isComplexMappingType()) {
			final ComplexMappingType x = this.of.createComplexMappingType();
			x.setParentId(cluster.getId());
			x.setMappingType("complex");
			this.setDataType(cluster.getModelVersion(), ct, x);
			cluster.getComplexMappingType().add(x);
			value = x;
		} else if (ct.isComplexUUIDMappingType()) {
			final ComplexUUIDMappingType x = this.of
					.createComplexUUIDMappingType();
			x.setParentId(cluster.getId());
			x.setMappingType("complexUUID");
			this.setDataType(cluster.getModelVersion(), ct, x);
			cluster.getComplexUUIDMappingType().add(x);
			value = x;
		} else if (ct.isInterfaceMappingType()) {
			final InterfaceMappingType x = this.of.createInterfaceMappingType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			cluster.getInterfaceMappingType().add(x);
			value = x;
		} else if (this.analysis.get(elemId) == null) {
			final ComplexType x = this.of.createComplexType();
			x.setParentId(cluster.getId());
			this.setDataType(cluster.getModelVersion(), ct, x);
			x.setIsFlowInputType(ct.isFlowInputType());
			x.setIsFlowOutputType(ct.isFlowOutputType());
			x.setIsMappingRequestType(ct.isMapRequestType());
			x.setIsMappingResponseType(ct.isMapResponseType());
			cluster.getComplexType().add(x);
			if (ct.getParent() != null) {
				final DataType parent = this.parseDataType(cluster,
						ct.getParent());
				x.setDescendedFromId(parent.getId());
			}
			for (final com.qpark.maven.xmlbeans.ComplexType innerCt : ct
					.getInnerTypeDefs()) {
				this.parseDataType(cluster, innerCt);
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
	private DomainType parseDomainType(final XsdContainer file,
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

	private void setDataType(final String modelVersion,
			final com.qpark.maven.xmlbeans.ComplexType element,
			final DataType value) {
		value.setDescription(element.getAnnotationDocumentation());
		value.setName(element.toQNameString());
		value.setModelVersion(modelVersion);
		value.setNamespace(element.getTargetNamespace());
		value.setJavaClassName(element.getClassNameFullQualified());
		value.setJavaPackageName(element.getPackageName());
		value.setShortName(element.getQNameLocalPart());
		if (!value.getShortName().equals("")) {
			value.setShortName(element.getClassName());
		}
		this.uuidProvider.setUUID(value);
	}

	@SuppressWarnings("static-method")
	private void setDefaultMappingType(final String modelVersion,
			final com.qpark.maven.xmlbeans.ComplexType element,
			final DefaultMappingType value) {
		value.setDefaultValue(element.getDefaultValue());
		if (value.getDescription() == null
				|| value.getDescription().trim().length() == 0) {
			final StringBuffer sb = new StringBuffer(64);
			sb.append("Default value: ");
			sb.append(element.getDefaultValue());
			value.setDescription(sb.toString());
		}
	}

	private void setDirectMappingType(final String modelVersion,
			final com.qpark.maven.xmlbeans.ComplexType ct,
			final DirectMappingType value) {
		if (Objects.nonNull(ct.getType())
				&& ct.getType().getName().getLocalPart().indexOf('.') > 0) {
			final String accessorPart = ct.getType().getName().getLocalPart()
					.substring(
							ct.getType().getName().getLocalPart().indexOf('.')
									+ 1,
							ct.getType().getName().getLocalPart().length())
					.replace("MappingType", "");
			ct.getChildren().stream()
					.filter(ctc -> Objects.nonNull(ctc.getComplexType())
							&& !ctc.getChildName().equals("value")
							&& !ctc.getChildName().equals("return"))
					.findFirst().ifPresent(ctc -> {
						final String ctId = this.uuidProvider.getDataTypeUUID(
								ctc.getComplexType().toQNameString(),
								modelVersion);
						value.setAccessorFieldId(
								this.uuidProvider.getFieldTypeUUID(accessorPart,
										ctId, modelVersion));
						value.setAccessor(String.format("%s.%s",
								ctc.getChildName(), accessorPart));
					});
		}
		if (Objects.isNull(value.getAccessor())) {
			value.setAccessor(ct.getType().getName().getLocalPart()
					.replace("MappingType", ""));
		}
		if (Objects.nonNull(value.getAccessor())) {
			final String[] strs = value.getAccessor().split("\\.");
			if (value.getDescription() == null
					|| value.getDescription().trim().length() == 0
							&& strs.length > 0) {
				final StringBuffer sb = new StringBuffer(64);
				sb.append("Get ");
				for (int i = strs.length - 1; i > 0; i--) {
					sb.append(strs[i]).append(" of ");
				}
				sb.append(strs[0]).append("Type.");
				value.setDescription(sb.toString());
			}
		}
	}

	@SuppressWarnings("static-method")
	private void setFieldMappingType(final FieldMappingType type,
			final List<FieldType> fields) {
		FieldType returnField = null;
		for (final FieldType f : fields) {
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
		final String elemId = this.getDataTypeUUID(ct, cluster);
		final DataType dt = (DataType) this.analysis.get(elemId);

		final List<FieldType> fields = this.getFieldTypes(cluster, ct, elemId);
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
		final DataType dt = (DataType) this.analysis.get(dataTypeId);
		if (dt != null && ComplexType.class.isInstance(dt)) {
			for (final FieldType field : ((ComplexType) dt).getField()) {
				final DataType dtx = (DataType) this.analysis
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

	@Override
	public Optional<ComplexType> getComplexType(final String complexTypeId) {
		Optional<ComplexType> value = Optional.empty();
		final DataType dt = this.analysis.getDataTypeById(complexTypeId);
		if (Objects.nonNull(dt) && ComplexType.class.isInstance(dt)) {
			value = Optional.of((ComplexType) dt);
		}
		return value;
	}

	@Override
	public List<DataType> getDataTypes(final List<String> ids) {
		final List<DataType> value = this.analysis.getDataTypes().stream()
				.filter(dt -> Objects.nonNull(dt) && ids.contains(dt.getId()))
				.collect(Collectors.toList());
		return value;
	}

	@Override
	public Optional<ElementType> getElement(final String elementId) {
		Optional<ElementType> value = Optional.empty();
		final DataType dt = this.analysis.getDataTypeById(elementId);
		if (Objects.nonNull(dt) && ElementType.class.isInstance(dt)) {
			value = Optional.of((ElementType) dt);
		}
		return value;
	}

	@Override
	public Optional<FieldMappingType> getFieldMapping(final String id) {
		Optional<FieldMappingType> value = Optional.empty();
		final DataType dt = this.analysis.getDataTypeById(id);
		if (Objects.nonNull(dt) && FieldMappingType.class.isInstance(dt)) {
			value = Optional.of((FieldMappingType) dt);
		}
		return value;
	}

	public Optional<InterfaceMappingType> getInterfaceMappingType(
			final String id) {
		Optional<InterfaceMappingType> value = Optional.empty();
		final DataType dt = this.analysis.getDataTypeById(id);
		if (Objects.nonNull(dt) && InterfaceMappingType.class.isInstance(dt)) {
			value = Optional.of((InterfaceMappingType) dt);
		}
		return value;
	}

	@Override
	public List<FlowType> getFlows(final Collection<String> flowNameParts) {
		List<FlowType> value = new ArrayList<>();
		if (Objects.nonNull(flowNameParts)) {
			if (flowNameParts.size() == 1 && flowNameParts.stream()
					.filter(f -> Objects.nonNull(f) && f.equals("*"))
					.findFirst().isPresent()) {
				value.addAll(this.enterprise.getFlows());
			} else {
				value = this.enterprise.getFlows().stream()
						.filter(f -> Objects.nonNull(f)
								&& Objects.nonNull(f.getName())
								&& flowMatches(flowNameParts, f.getName()))
						.collect(Collectors.toList());
			}
		}
		return value;
	}

	private static boolean flowMatches(final Collection<String> flowNameParts,
			final String flowName) {
		final AtomicBoolean value = new AtomicBoolean(false);
		flowNameParts.stream().filter(f -> flowName.contains(f)).findFirst()
				.ifPresent(f -> value.set(true));
		return value.get();
	}

	@Override
	public List<InterfaceMappingType> getInterfaceMappings(
			final String flowId) {
		final List<InterfaceMappingType> value = new ArrayList<>();
		this.getInterfaceMappingIds(flowId)
				.forEach(iftId -> this.getInterfaceMappingType(iftId)
						.ifPresent(ift -> value.add(ift)));
		final Set<String> foundIds = new TreeSet<>();
		value.stream().forEach(ift -> foundIds.add(ift.getId()));
		this.getInterfaceMappingInheritents(foundIds, value);
		return value;
	}

	private void getInterfaceMappingInheritents(
			final Set<String> interfaceMappingIds,
			final List<InterfaceMappingType> interfaceMappings) {
		if (interfaceMappingIds.size() > 0) {
			final Set<String> allIftIds = interfaceMappings.stream()
					.map(ift -> ift.getId()).collect(Collectors.toSet());
			final List<InterfaceMappingType> value = new ArrayList<>();
			final Set<String> fieldDefinitionIds = new TreeSet<>();
			interfaceMappings.stream().forEach(ift -> {
				ift.getFieldMappings().stream().forEach(fm -> {
					fieldDefinitionIds.add(fm.getFieldTypeDefinitionId());
				});
			});

			fieldDefinitionIds.stream().filter(
					fdid -> Objects.nonNull(fdid) && !allIftIds.contains(fdid))
					.forEach(fdid -> {
						this.getInterfaceMappingType(fdid)
								.ifPresent(ift -> value.add(ift));
					});
			interfaceMappings.addAll(value);
			final Set<String> foundIds = value.stream().map(ift -> ift.getId())
					.collect(Collectors.toSet());
			this.getInterfaceMappingInheritents(foundIds, interfaceMappings);
		}
	}

	private List<String> getInterfaceMappingIds(final String flowId) {
		final List<String> value = new ArrayList<>();
		if (Objects.nonNull(flowId)) {
			final List<FlowMapInOutType> flowMapInOuts = new ArrayList<>();
			this.enterprise.getFlows().stream()
					.filter(f -> Objects.nonNull(f) && f.getId().equals(flowId))
					.findFirst().ifPresent(f -> {
						if (Objects.nonNull(f.getExecuteRequest())) {
							flowMapInOuts.addAll(
									f.getExecuteRequest().getMapInOut());
						}
						if (Objects.nonNull(f.getProcessResponse())) {
							flowMapInOuts.addAll(
									f.getProcessResponse().getMapInOut());
						}
					});
			flowMapInOuts.stream()
					.forEach(fm -> value.addAll(fm.getInterfaceMappingId()));
		}
		return value;
	}

	@Override
	public Optional<ServiceType> getService(final String serviceId) {
		Optional<ServiceType> value = Optional.empty();
		final ServiceType service = this.analysis.getServiceTypeById(serviceId);
		if (Objects.nonNull(service)) {
			value = Optional.of(service);
		}
		return value;
	}
}
