//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.01.04 um 07:31:49 AM CET 
//


package com.qpark.eip.model.docmodel;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.qpark.eip.model.docmodel package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Enterprise_QNAME = new QName("http://www.ses.com/Utility/DocumentationModel", "Enterprise");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.qpark.eip.model.docmodel
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EnterpriseType }
     * 
     */
    public EnterpriseType createEnterpriseType() {
        return new EnterpriseType();
    }

    /**
     * Create an instance of {@link DomainType }
     * 
     */
    public DomainType createDomainType() {
        return new DomainType();
    }

    /**
     * Create an instance of {@link ClusterType }
     * 
     */
    public ClusterType createClusterType() {
        return new ClusterType();
    }

    /**
     * Create an instance of {@link ServiceType }
     * 
     */
    public ServiceType createServiceType() {
        return new ServiceType();
    }

    /**
     * Create an instance of {@link DataType }
     * 
     */
    public DataType createDataType() {
        return new DataType();
    }

    /**
     * Create an instance of {@link DirectMappingType }
     * 
     */
    public DirectMappingType createDirectMappingType() {
        return new DirectMappingType();
    }

    /**
     * Create an instance of {@link DefaultMappingType }
     * 
     */
    public DefaultMappingType createDefaultMappingType() {
        return new DefaultMappingType();
    }

    /**
     * Create an instance of {@link ComplexMappingType }
     * 
     */
    public ComplexMappingType createComplexMappingType() {
        return new ComplexMappingType();
    }

    /**
     * Create an instance of {@link ComplexUUIDMappingType }
     * 
     */
    public ComplexUUIDMappingType createComplexUUIDMappingType() {
        return new ComplexUUIDMappingType();
    }

    /**
     * Create an instance of {@link InterfaceMappingType }
     * 
     */
    public InterfaceMappingType createInterfaceMappingType() {
        return new InterfaceMappingType();
    }

    /**
     * Create an instance of {@link ComplexType }
     * 
     */
    public ComplexType createComplexType() {
        return new ComplexType();
    }

    /**
     * Create an instance of {@link FieldType }
     * 
     */
    public FieldType createFieldType() {
        return new FieldType();
    }

    /**
     * Create an instance of {@link OperationType }
     * 
     */
    public OperationType createOperationType() {
        return new OperationType();
    }

    /**
     * Create an instance of {@link ElementType }
     * 
     */
    public ElementType createElementType() {
        return new ElementType();
    }

    /**
     * Create an instance of {@link FlowType }
     * 
     */
    public FlowType createFlowType() {
        return new FlowType();
    }

    /**
     * Create an instance of {@link FlowProcessType }
     * 
     */
    public FlowProcessType createFlowProcessType() {
        return new FlowProcessType();
    }

    /**
     * Create an instance of {@link FlowMapInOutType }
     * 
     */
    public FlowMapInOutType createFlowMapInOutType() {
        return new FlowMapInOutType();
    }

    /**
     * Create an instance of {@link FlowSubRequestType }
     * 
     */
    public FlowSubRequestType createFlowSubRequestType() {
        return new FlowSubRequestType();
    }

    /**
     * Create an instance of {@link FlowFilterType }
     * 
     */
    public FlowFilterType createFlowFilterType() {
        return new FlowFilterType();
    }

    /**
     * Create an instance of {@link RequestResponseDataType }
     * 
     */
    public RequestResponseDataType createRequestResponseDataType() {
        return new RequestResponseDataType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnterpriseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ses.com/Utility/DocumentationModel", name = "Enterprise")
    public JAXBElement<EnterpriseType> createEnterprise(EnterpriseType value) {
        return new JAXBElement<EnterpriseType>(_Enterprise_QNAME, EnterpriseType.class, null, value);
    }

}
