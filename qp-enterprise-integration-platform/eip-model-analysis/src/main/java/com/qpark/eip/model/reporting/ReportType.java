//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.09.14 um 12:08:24 PM CEST 
//


package com.qpark.eip.model.reporting;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für ReportType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReportType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="reportUUID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="reportName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="created" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="updated" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="artefact" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="artefactVersion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="environment" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="reportInfo" type="{http://www.qpark-consulting.com/EIP/Utility/ReportingModel}ReportInfoType"/&gt;
 *         &lt;element name="reportHeaderData" type="{http://www.qpark-consulting.com/EIP/Utility/ReportingModel}ReportHeaderDataType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="reportContent" type="{http://www.qpark-consulting.com/EIP/Utility/ReportingModel}ReportContentType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportType", propOrder = {
    "reportUUID",
    "reportName",
    "created",
    "updated",
    "artefact",
    "artefactVersion",
    "environment",
    "reportInfo",
    "reportHeaderData",
    "reportContent"
})
public class ReportType {

    @XmlElement(required = true)
    protected String reportUUID;
    @XmlElement(required = true)
    protected String reportName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updated;
    @XmlElement(required = true)
    protected String artefact;
    @XmlElement(required = true)
    protected String artefactVersion;
    @XmlElement(required = true)
    protected String environment;
    @XmlElement(required = true)
    protected ReportInfoType reportInfo;
    @XmlElement(required = true)
    protected List<ReportHeaderDataType> reportHeaderData;
    @XmlElement(required = true)
    protected List<ReportContentType> reportContent;

    /**
     * Ruft den Wert der reportUUID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportUUID() {
        return reportUUID;
    }

    /**
     * Legt den Wert der reportUUID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportUUID(String value) {
        this.reportUUID = value;
    }

    /**
     * Ruft den Wert der reportName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * Legt den Wert der reportName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportName(String value) {
        this.reportName = value;
    }

    /**
     * Ruft den Wert der created-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreated() {
        return created;
    }

    /**
     * Legt den Wert der created-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    /**
     * Ruft den Wert der updated-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdated() {
        return updated;
    }

    /**
     * Legt den Wert der updated-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdated(XMLGregorianCalendar value) {
        this.updated = value;
    }

    /**
     * Ruft den Wert der artefact-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtefact() {
        return artefact;
    }

    /**
     * Legt den Wert der artefact-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtefact(String value) {
        this.artefact = value;
    }

    /**
     * Ruft den Wert der artefactVersion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtefactVersion() {
        return artefactVersion;
    }

    /**
     * Legt den Wert der artefactVersion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtefactVersion(String value) {
        this.artefactVersion = value;
    }

    /**
     * Ruft den Wert der environment-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * Legt den Wert der environment-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvironment(String value) {
        this.environment = value;
    }

    /**
     * Ruft den Wert der reportInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReportInfoType }
     *     
     */
    public ReportInfoType getReportInfo() {
        return reportInfo;
    }

    /**
     * Legt den Wert der reportInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportInfoType }
     *     
     */
    public void setReportInfo(ReportInfoType value) {
        this.reportInfo = value;
    }

    /**
     * Gets the value of the reportHeaderData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportHeaderData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportHeaderData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReportHeaderDataType }
     * 
     * 
     */
    public List<ReportHeaderDataType> getReportHeaderData() {
        if (reportHeaderData == null) {
            reportHeaderData = new ArrayList<ReportHeaderDataType>();
        }
        return this.reportHeaderData;
    }

    /**
     * Gets the value of the reportContent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportContent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReportContentType }
     * 
     * 
     */
    public List<ReportContentType> getReportContent() {
        if (reportContent == null) {
            reportContent = new ArrayList<ReportContentType>();
        }
        return this.reportContent;
    }

}
