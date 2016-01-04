package com.qpark.eip.core.model.analysis;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.ObjectFactory;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.ServiceType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

public class AnalysisProvider {
    private UUIDProvider uuidProvider;
    private ObjectFactory of = new ObjectFactory();
    private XsdsUtil xsds;
    private Analysis analysis;
    private EnterpriseType enterprise;

    private XsdsUtil createXsdsUtil(final String basePackageName, final String modelPath) {
	File f = new File(modelPath);
	String messagePackageNameSuffix = "msg mapping flow";

	XsdsUtil xsds = new XsdsUtil(f, basePackageName, messagePackageNameSuffix, "delta");
	return xsds;
    }

    /**
     * Get the {@link DomainType} of the {@link XsdContainer}. If the
     * {@link DomainType} is not present at this stage, it will be created.
     *
     * @param file
     *            the {@link XsdContainer}.
     * @return the {@link DomainType}.
     */
    private DomainType getDomainType(final XsdContainer file) {
	DomainType domain = this.analysis.getDomainType(file.getDomainPathName());
	if (domain == null) {
	    domain = this.of.createDomainType();
	    domain.setName(file.getDomainPathName());
	    this.uuidProvider.setUUID(domain);
	    this.enterprise.getDomains().add(domain);
	}
	return domain;
    }

    /**
     * @param start
     * @return the duration in 000:00:00.000 format.
     */
    private static String getDuration(final long start) {
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

    public static void main(final String[] args) {

	String xsdPath;
	xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain\\mapping\\target\\model";
	xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
	xsdPath = "C:\\xnb\\dev\\38\\com.ses.domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
	xsdPath = "C:\\xnb\\dev\\git\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";

	String basePackageName = "com.ses.osp.bus";
	Analysis a = new AnalysisProvider().createEnterprise(basePackageName, basePackageName, xsdPath);

	try {
	    ObjectFactory of = new ObjectFactory();
	    JAXBElement<EnterpriseType> enterprise = of.createEnterprise(a.getEnterprise());
	    JAXBContext context = JAXBContext.newInstance("com.qpark.eip.model.docmodel");
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    StringWriter sw = new StringWriter();
	    marshaller.marshal(enterprise, sw);
	    System.out.println(sw.toString());
	} catch (Exception e) {
	    e.printStackTrace();
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
    public Analysis createEnterprise(final String enterpriseName, final String basePackageName,
	    final String modelPath) {
	long start = System.currentTimeMillis();
	this.xsds = this.createXsdsUtil(basePackageName, modelPath);

	this.analysis = new Analysis(this.of.createEnterpriseType());
	this.enterprise = this.analysis.getEnterprise();
	this.enterprise.setName(enterpriseName);

	this.uuidProvider = new UUIDProvider(this.analysis);

	DomainType domain;
	ClusterType cluster;
	for (XsdContainer file : this.xsds.getXsdContainerMap().values()) {
	    domain = this.getDomainType(file);
	    cluster = this.getClusterType(domain, file);
	}
	for (com.qpark.maven.xmlbeans.ComplexType ct : this.xsds.getComplexTypes()) {
	    this.getDataType(this.analysis.getCluster(ct.getTargetNamespace()), ct);
	}
	for (com.qpark.maven.xmlbeans.ElementType element : this.xsds.getElementTypes()) {
	    this.getElementType(this.analysis.getCluster(element.getTargetNamespace()), element);
	}
	for (com.qpark.maven.xmlbeans.ElementType element : this.xsds.getElementTypes()) {
	    if (element.isRequest()) {
		com.qpark.maven.xmlbeans.ElementType response = XsdsUtil.findResponse(element,
			this.xsds.getElementTypes(), this.xsds);
		if (response != null) {
		    this.getServiceOperation(this.analysis.getCluster(element.getTargetNamespace()),
			    element.getServiceId(), element.getOperationName(),
			    this.analysis.getElementType(element.toQNameString()),
			    this.analysis.getElementType(response.toQNameString()));
		}
	    }
	}
	return this.analysis;
    }

    private List<FieldType> getFieldTypes(final ClusterType cluster, final com.qpark.maven.xmlbeans.ComplexType element,
	    final String parentId) {
	List<FieldType> list = new ArrayList<FieldType>();
	for (ComplexTypeChild child : element.getChildren()) {
	    DataType dt = this.getDataType(cluster, child.getComplexType());
	    FieldType field = this.of.createFieldType();
	    field.setParentId(parentId);
	    field.setName(child.getChildName());
	    field.setCardinality(child.getCardinality());
	    field.setCardinalityMaxOccurs(child.getMaxOccurs() == null ? null : child.getMaxOccurs().intValue());
	    field.setCardinalityMinOccurs(child.getMinOccurs() == null ? null : child.getMinOccurs().intValue());
	    field.setDescription(child.getAnnotationDocumentation());
	    field.setFieldTypeDefinitionId(dt == null ? null : dt.getId());
	    field.setListField(child.isList());
	    field.setNamespace(element.getTargetNamespace());
	    field.setOptionalField(child.isOptional());
	    this.uuidProvider.setUUID(field);
	    list.add(field);

	}
	return list;
    }

    private void setFieldMappingType(final FieldMappingType type, final List<FieldType> fields) {
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

    private void setDataType(final com.qpark.maven.xmlbeans.ComplexType element, final DataType elem) {
	elem.setDescription(element.getAnnotationDocumentation());
	elem.setName(element.toQNameString());
	elem.setNamespace(element.getTargetNamespace());
	elem.setJavaClassName(element.getClassNameFullQualified());
	elem.setJavaPackageName(element.getPackageName());
	this.uuidProvider.setUUID(elem);
    }

    private DataType getDataType(final ClusterType cluster, final com.qpark.maven.xmlbeans.ComplexType ct) {
	String elemId = this.uuidProvider.getDataTypeUUID(ct.toQNameString());
	DataType elem = (DataType) this.analysis.get(elemId);

	List<FieldType> fields = this.getFieldTypes(cluster, ct, elemId);
	if (elem != null) {
	} else if (ct.getTargetNamespace().equals(XsdsUtil.QNAME_BASE_SCHEMA_NAMESPACE_URI)
		&& this.analysis.getDataType(ct.toQNameString()) == null) {
	    DataType dt = this.of.createDataType();
	    dt.setName(ct.toQNameString());
	    this.setDataType(ct, dt);
	    this.uuidProvider.setUUID(dt);
	    this.enterprise.getBasicDataTypes().add(dt);
	} else if (ct.isDefaultMappingType()) {
	    DefaultMappingType x = this.of.createDefaultMappingType();
	    x.setParentId(cluster.getId());
	    this.setDataType(ct, x);
	    this.setFieldMappingType(x, fields);
	    x.setDefaultValue(ct.getDefaultValue());
	    cluster.getDefaultMappingType().add(x);
	    elem = x;
	} else if (ct.isDirectMappingType()) {
	    DirectMappingType x = this.of.createDirectMappingType();
	    x.setParentId(cluster.getId());
	    this.setDataType(ct, x);
	    this.setFieldMappingType(x, fields);
	    cluster.getDirectMappingType().add(x);
	    elem = x;
	} else if (ct.isComplexMappingType()) {
	    ComplexMappingType x = this.of.createComplexMappingType();
	    x.setParentId(cluster.getId());
	    this.setDataType(ct, x);
	    this.setFieldMappingType(x, fields);
	    cluster.getComplexMappingType().add(x);
	    elem = x;
	} else if (ct.isComplexUUIDMappingType()) {
	    ComplexUUIDMappingType x = this.of.createComplexUUIDMappingType();
	    x.setParentId(cluster.getId());
	    this.setDataType(ct, x);
	    this.setFieldMappingType(x, fields);
	    cluster.getComplexUUIDMappingType().add(x);
	    elem = x;
	} else if (ct.isInterfaceMappingType()) {
	    InterfaceMappingType x = this.of.createInterfaceMappingType();
	    x.setParentId(cluster.getId());
	    this.setDataType(ct, x);
	    x.getFieldMappings().addAll(fields);
	    cluster.getInterfaceMappingType().add(x);
	    elem = x;
	} else if (this.analysis.get(elemId) == null) {
	    ComplexType x = this.of.createComplexType();
	    x.setParentId(cluster.getId());
	    this.setDataType(ct, x);
	    x.setIsFlowInputType(ct.isFlowInputType());
	    x.setIsFlowOutputType(ct.isFlowOutputType());
	    x.setIsMappingRequestType(ct.isMapRequestType());
	    x.setIsMappingResponseType(ct.isMapResponseType());
	    x.getField().addAll(fields);
	    cluster.getComplexType().add(x);
	    if (ct.getParent() != null) {
		DataType parent = this.getDataType(cluster, ct.getParent());
		x.setDescendedFromId(parent.getId());
	    }
	    for (com.qpark.maven.xmlbeans.ComplexType innerCt : ct.getInnerTypeDefs()) {
		this.getDataType(cluster, innerCt);
	    }
	    elem = x;
	}
	return elem;
    }

    private ElementType getElementType(final ClusterType cluster, final com.qpark.maven.xmlbeans.ElementType element) {
	ElementType elem = this.of.createElementType();
	elem.setDescription(element.getAnnotationDocumentation());
	elem.setName(element.toQNameString());
	elem.setNamespace(element.getTargetNamespace());
	elem.setParentId(cluster.getId());
	this.uuidProvider.setUUID(elem);
	cluster.getElementType().add(elem);
	return elem;
    }

    private OperationType getServiceOperation(final ClusterType cluster, final String serviceId,
	    final String operationName, final ElementType request, final ElementType response) {
	ServiceType service = this.analysis.getServiceType(serviceId);
	if (service == null) {
	    DomainType domain = (DomainType) this.analysis.get(cluster.getParentId());
	    service = this.of.createServiceType();
	    service.setName(serviceId);
	    this.uuidProvider.setUUID(service);
	    service.setClusterId(cluster.getId());
	    service.setDescription("");
	    service.setNamespace(cluster.getName());
	    service.setPackageName(cluster.getPackageName());
	    service.setServiceId(serviceId);

	    domain.getService().add(service);
	}

	OperationType operation = this.of.createOperationType();
	operation.setDescription(
		new StringBuffer(128).append(request.getDescription()).append(response.getDescription()).toString());
	operation.setName(
		new StringBuffer(128).append(cluster.getPackageName()).append(".").append(operationName).toString());
	operation.setNamespace(cluster.getName());
	operation.setParentId(service.getId());
	operation.setRequest(request);
	operation.setResponse(response);

	this.uuidProvider.setUUID(operation);
	service.getOperation().add(operation);
	return operation;
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
    private ClusterType getClusterType(final DomainType domain, final XsdContainer file) {
	ClusterType cluster = this.analysis.getCluster(file.getTargetNamespace());
	if (cluster == null) {
	    cluster = this.of.createClusterType();
	    cluster.setName(file.getTargetNamespace());
	    this.uuidProvider.setUUID(cluster);
	    cluster.setParentId(domain.getId());
	    domain.getCluster().add(cluster);

	    cluster.setDescription(file.getAnnotationDocumentation());
	    cluster.setFileName(file.getFile().getName());
	    cluster.setPackageName(file.getPackageName());
	    cluster.setVersion(file.getVersion());
	    cluster.getWarning().addAll(file.getWarnings());
	}
	return cluster;
    }

    /** The {@link org.slf4j.Logger}. */
    private final Logger logger = LoggerFactory.getLogger(AnalysisProvider.class);
}
