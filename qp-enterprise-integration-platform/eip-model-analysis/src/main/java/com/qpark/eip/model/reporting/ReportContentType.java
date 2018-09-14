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
 * <p>Java-Klasse für ReportContentType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReportContentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="row" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="rowContent" type="{http://www.qpark-consulting.com/EIP/Utility/ReportingModel}CellType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportContentType", propOrder = {
    "row",
    "rowContent"
})
public class ReportContentType {

    protected int row;
    @XmlElement(required = true)
    protected CellType rowContent;

    /**
     * Ruft den Wert der row-Eigenschaft ab.
     * 
     */
    public int getRow() {
        return row;
    }

    /**
     * Legt den Wert der row-Eigenschaft fest.
     * 
     */
    public void setRow(int value) {
        this.row = value;
    }

    /**
     * Ruft den Wert der rowContent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CellType }
     *     
     */
    public CellType getRowContent() {
        return rowContent;
    }

    /**
     * Legt den Wert der rowContent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CellType }
     *     
     */
    public void setRowContent(CellType value) {
        this.rowContent = value;
    }

}
