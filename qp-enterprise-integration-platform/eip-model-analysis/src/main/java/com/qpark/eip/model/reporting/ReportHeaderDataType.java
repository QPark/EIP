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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ReportHeaderDataType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReportHeaderDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rowNumber" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="reportMetaData" type="{http://www.qpark-consulting.com/EIP/Utility/ReportingModel}ReportMetaDataType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="final" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportHeaderDataType", propOrder = {
    "rowNumber",
    "reportMetaData",
    "_final"
})
public class ReportHeaderDataType {

    protected int rowNumber;
    @XmlElement(required = true)
    protected List<ReportMetaDataType> reportMetaData;
    @XmlElement(name = "final")
    protected boolean _final;

    /**
     * Ruft den Wert der rowNumber-Eigenschaft ab.
     * 
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Legt den Wert der rowNumber-Eigenschaft fest.
     * 
     */
    public void setRowNumber(int value) {
        this.rowNumber = value;
    }

    /**
     * Gets the value of the reportMetaData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportMetaData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportMetaData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReportMetaDataType }
     * 
     * 
     */
    public List<ReportMetaDataType> getReportMetaData() {
        if (reportMetaData == null) {
            reportMetaData = new ArrayList<ReportMetaDataType>();
        }
        return this.reportMetaData;
    }

    /**
     * Ruft den Wert der final-Eigenschaft ab.
     * 
     */
    public boolean isFinal() {
        return _final;
    }

    /**
     * Legt den Wert der final-Eigenschaft fest.
     * 
     */
    public void setFinal(boolean value) {
        this._final = value;
    }

}
