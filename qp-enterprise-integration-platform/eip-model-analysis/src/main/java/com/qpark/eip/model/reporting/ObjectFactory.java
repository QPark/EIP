//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.09.14 um 12:08:24 PM CEST 
//


package com.qpark.eip.model.reporting;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.qpark.eip.model.reporting package. 
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

    private final static QName _Report_QNAME = new QName("http://www.qpark-consulting.com/EIP/Utility/ReportingModel", "Report");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.qpark.eip.model.reporting
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReportType }
     * 
     */
    public ReportType createReportType() {
        return new ReportType();
    }

    /**
     * Create an instance of {@link ReportInfoType }
     * 
     */
    public ReportInfoType createReportInfoType() {
        return new ReportInfoType();
    }

    /**
     * Create an instance of {@link ReportHeaderDataType }
     * 
     */
    public ReportHeaderDataType createReportHeaderDataType() {
        return new ReportHeaderDataType();
    }

    /**
     * Create an instance of {@link ReportMetaDataType }
     * 
     */
    public ReportMetaDataType createReportMetaDataType() {
        return new ReportMetaDataType();
    }

    /**
     * Create an instance of {@link ReportContentType }
     * 
     */
    public ReportContentType createReportContentType() {
        return new ReportContentType();
    }

    /**
     * Create an instance of {@link CellType }
     * 
     */
    public CellType createCellType() {
        return new CellType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReportType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.qpark-consulting.com/EIP/Utility/ReportingModel", name = "Report")
    public JAXBElement<ReportType> createReport(ReportType value) {
        return new JAXBElement<ReportType>(_Report_QNAME, ReportType.class, null, value);
    }

}
