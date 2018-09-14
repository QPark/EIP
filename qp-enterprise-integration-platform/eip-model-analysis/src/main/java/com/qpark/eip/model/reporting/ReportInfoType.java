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
 * <p>Java-Klasse für ReportInfoType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReportInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numberOfHeaderRows" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="numberOfColumns" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="numberOfRows" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="delimiter" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportInfoType", propOrder = {
    "numberOfHeaderRows",
    "numberOfColumns",
    "numberOfRows",
    "delimiter"
})
public class ReportInfoType {

    protected int numberOfHeaderRows;
    protected int numberOfColumns;
    protected int numberOfRows;
    @XmlElement(required = true)
    protected String delimiter;

    /**
     * Ruft den Wert der numberOfHeaderRows-Eigenschaft ab.
     * 
     */
    public int getNumberOfHeaderRows() {
        return numberOfHeaderRows;
    }

    /**
     * Legt den Wert der numberOfHeaderRows-Eigenschaft fest.
     * 
     */
    public void setNumberOfHeaderRows(int value) {
        this.numberOfHeaderRows = value;
    }

    /**
     * Ruft den Wert der numberOfColumns-Eigenschaft ab.
     * 
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * Legt den Wert der numberOfColumns-Eigenschaft fest.
     * 
     */
    public void setNumberOfColumns(int value) {
        this.numberOfColumns = value;
    }

    /**
     * Ruft den Wert der numberOfRows-Eigenschaft ab.
     * 
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Legt den Wert der numberOfRows-Eigenschaft fest.
     * 
     */
    public void setNumberOfRows(int value) {
        this.numberOfRows = value;
    }

    /**
     * Ruft den Wert der delimiter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Legt den Wert der delimiter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelimiter(String value) {
        this.delimiter = value;
    }

}
