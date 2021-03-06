//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.19 at 04:46:23 PM CEST 
//


package com.qpark.eip.model.reporting;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for ReportMetaDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReportMetaDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="columnName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="columnSpan" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="columnWidth" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
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
    "columnWidth",
    "columnDescription",
    "columnJavaDataType",
    "columnJavaFormatString"
})
@Entity(name = "ReportMetaDataType")
@Table(name = "REPORTMETADATATYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ReportMetaDataType
    implements Serializable, Equals, HashCode
{

    @XmlElement(required = true)
    protected String columnName;
    protected int columnSpan;
    protected double columnWidth;
    @XmlElement(required = true)
    protected String columnDescription;
    @XmlElement(required = true)
    protected String columnJavaDataType;
    protected String columnJavaFormatString;
    @XmlTransient
    protected Long hjid;

    /**
     * Gets the value of the columnName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "COLUMNNAME", length = 255)
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of the columnName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnName(String value) {
        this.columnName = value;
    }

    @Transient
    public boolean isSetColumnName() {
        return (this.columnName!= null);
    }

    /**
     * Gets the value of the columnSpan property.
     * 
     */
    @Basic
    @Column(name = "COLUMNSPAN", precision = 10, scale = 0)
    public int getColumnSpan() {
        return columnSpan;
    }

    /**
     * Sets the value of the columnSpan property.
     * 
     */
    public void setColumnSpan(int value) {
        this.columnSpan = value;
    }

    @Transient
    public boolean isSetColumnSpan() {
        return true;
    }

    /**
     * Gets the value of the columnWidth property.
     * 
     */
    @Basic
    @Column(name = "COLUMNWIDTH", precision = 20, scale = 10)
    public double getColumnWidth() {
        return columnWidth;
    }

    /**
     * Sets the value of the columnWidth property.
     * 
     */
    public void setColumnWidth(double value) {
        this.columnWidth = value;
    }

    @Transient
    public boolean isSetColumnWidth() {
        return true;
    }

    /**
     * Gets the value of the columnDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "COLUMNDESCRIPTION", length = 255)
    public String getColumnDescription() {
        return columnDescription;
    }

    /**
     * Sets the value of the columnDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnDescription(String value) {
        this.columnDescription = value;
    }

    @Transient
    public boolean isSetColumnDescription() {
        return (this.columnDescription!= null);
    }

    /**
     * Gets the value of the columnJavaDataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "COLUMNJAVADATATYPE", length = 255)
    public String getColumnJavaDataType() {
        return columnJavaDataType;
    }

    /**
     * Sets the value of the columnJavaDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnJavaDataType(String value) {
        this.columnJavaDataType = value;
    }

    @Transient
    public boolean isSetColumnJavaDataType() {
        return (this.columnJavaDataType!= null);
    }

    /**
     * Gets the value of the columnJavaFormatString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "COLUMNJAVAFORMATSTRING", length = 255)
    public String getColumnJavaFormatString() {
        return columnJavaFormatString;
    }

    /**
     * Sets the value of the columnJavaFormatString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnJavaFormatString(String value) {
        this.columnJavaFormatString = value;
    }

    @Transient
    public boolean isSetColumnJavaFormatString() {
        return (this.columnJavaFormatString!= null);
    }

    /**
     * 
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getHjid() {
        return hjid;
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHjid(Long value) {
        this.hjid = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if ((object == null)||(this.getClass()!= object.getClass())) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ReportMetaDataType that = ((ReportMetaDataType) object);
        {
            String lhsColumnName;
            lhsColumnName = this.getColumnName();
            String rhsColumnName;
            rhsColumnName = that.getColumnName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "columnName", lhsColumnName), LocatorUtils.property(thatLocator, "columnName", rhsColumnName), lhsColumnName, rhsColumnName)) {
                return false;
            }
        }
        {
            int lhsColumnSpan;
            lhsColumnSpan = this.getColumnSpan();
            int rhsColumnSpan;
            rhsColumnSpan = that.getColumnSpan();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "columnSpan", lhsColumnSpan), LocatorUtils.property(thatLocator, "columnSpan", rhsColumnSpan), lhsColumnSpan, rhsColumnSpan)) {
                return false;
            }
        }
        {
            double lhsColumnWidth;
            lhsColumnWidth = this.getColumnWidth();
            double rhsColumnWidth;
            rhsColumnWidth = that.getColumnWidth();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "columnWidth", lhsColumnWidth), LocatorUtils.property(thatLocator, "columnWidth", rhsColumnWidth), lhsColumnWidth, rhsColumnWidth)) {
                return false;
            }
        }
        {
            String lhsColumnDescription;
            lhsColumnDescription = this.getColumnDescription();
            String rhsColumnDescription;
            rhsColumnDescription = that.getColumnDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "columnDescription", lhsColumnDescription), LocatorUtils.property(thatLocator, "columnDescription", rhsColumnDescription), lhsColumnDescription, rhsColumnDescription)) {
                return false;
            }
        }
        {
            String lhsColumnJavaDataType;
            lhsColumnJavaDataType = this.getColumnJavaDataType();
            String rhsColumnJavaDataType;
            rhsColumnJavaDataType = that.getColumnJavaDataType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "columnJavaDataType", lhsColumnJavaDataType), LocatorUtils.property(thatLocator, "columnJavaDataType", rhsColumnJavaDataType), lhsColumnJavaDataType, rhsColumnJavaDataType)) {
                return false;
            }
        }
        {
            String lhsColumnJavaFormatString;
            lhsColumnJavaFormatString = this.getColumnJavaFormatString();
            String rhsColumnJavaFormatString;
            rhsColumnJavaFormatString = that.getColumnJavaFormatString();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "columnJavaFormatString", lhsColumnJavaFormatString), LocatorUtils.property(thatLocator, "columnJavaFormatString", rhsColumnJavaFormatString), lhsColumnJavaFormatString, rhsColumnJavaFormatString)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            String theColumnName;
            theColumnName = this.getColumnName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "columnName", theColumnName), currentHashCode, theColumnName);
        }
        {
            int theColumnSpan;
            theColumnSpan = this.getColumnSpan();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "columnSpan", theColumnSpan), currentHashCode, theColumnSpan);
        }
        {
            double theColumnWidth;
            theColumnWidth = this.getColumnWidth();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "columnWidth", theColumnWidth), currentHashCode, theColumnWidth);
        }
        {
            String theColumnDescription;
            theColumnDescription = this.getColumnDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "columnDescription", theColumnDescription), currentHashCode, theColumnDescription);
        }
        {
            String theColumnJavaDataType;
            theColumnJavaDataType = this.getColumnJavaDataType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "columnJavaDataType", theColumnJavaDataType), currentHashCode, theColumnJavaDataType);
        }
        {
            String theColumnJavaFormatString;
            theColumnJavaFormatString = this.getColumnJavaFormatString();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "columnJavaFormatString", theColumnJavaFormatString), currentHashCode, theColumnJavaFormatString);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
