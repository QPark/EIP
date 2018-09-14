//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.09.14 um 12:08:24 PM CEST 
//


package com.qpark.eip.model.reporting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ReportMetaDataType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReportMetaDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="columnName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="columnSpan" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="columnDescription" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="columnJavaDataType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="columnJavaFormatString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportMetaDataType", propOrder = {
    "columnName",
    "columnSpan",
    "columnDescription",
    "columnJavaDataType",
    "columnJavaFormatString"
})
public class ReportMetaDataType {

    @XmlElement(required = true)
    protected String columnName;
    protected int columnSpan;
    @XmlElement(required = true)
    protected String columnDescription;
    @XmlElement(required = true)
    protected String columnJavaDataType;
    protected String columnJavaFormatString;

    /**
     * Ruft den Wert der columnName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Legt den Wert der columnName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnName(String value) {
        this.columnName = value;
    }

    /**
     * Ruft den Wert der columnSpan-Eigenschaft ab.
     * 
     */
    public int getColumnSpan() {
        return columnSpan;
    }

    /**
     * Legt den Wert der columnSpan-Eigenschaft fest.
     * 
     */
    public void setColumnSpan(int value) {
        this.columnSpan = value;
    }

    /**
     * Ruft den Wert der columnDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnDescription() {
        return columnDescription;
    }

    /**
     * Legt den Wert der columnDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnDescription(String value) {
        this.columnDescription = value;
    }

    /**
     * Ruft den Wert der columnJavaDataType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnJavaDataType() {
        return columnJavaDataType;
    }

    /**
     * Legt den Wert der columnJavaDataType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnJavaDataType(String value) {
        this.columnJavaDataType = value;
    }

    /**
     * Ruft den Wert der columnJavaFormatString-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnJavaFormatString() {
        return columnJavaFormatString;
    }

    /**
     * Legt den Wert der columnJavaFormatString-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnJavaFormatString(String value) {
        this.columnJavaFormatString = value;
    }

}
