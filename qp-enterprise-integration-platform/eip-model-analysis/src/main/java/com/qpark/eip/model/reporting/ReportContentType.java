//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.19 at 04:46:23 PM CEST 
//


package com.qpark.eip.model.reporting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
 * <p>Java class for ReportContentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReportContentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="row" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="rowContent" type="{http://www.qpark-consulting.com/EIP/Utility/ReportingModel}CellType" maxOccurs="unbounded"/&gt;
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
@Entity(name = "ReportContentType")
@Table(name = "REPORTCONTENTTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ReportContentType
    implements Serializable, Equals, HashCode
{

    protected int row;
    @XmlElement(required = true)
    protected List<CellType> rowContent;
    @XmlTransient
    protected Long hjid;

    /**
     * Gets the value of the row property.
     * 
     */
    @Basic
    @Column(name = "ROW_", precision = 10, scale = 0)
    public int getRow() {
        return row;
    }

    /**
     * Sets the value of the row property.
     * 
     */
    public void setRow(int value) {
        this.row = value;
    }

    @Transient
    public boolean isSetRow() {
        return true;
    }

    /**
     * Gets the value of the rowContent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rowContent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRowContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CellType }
     * 
     * 
     */
    @OneToMany(targetEntity = CellType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ROWCONTENT_REPORTCONTENTTYPE_0")
    public List<CellType> getRowContent() {
        if (rowContent == null) {
            rowContent = new ArrayList<CellType>();
        }
        return this.rowContent;
    }

    /**
     * 
     * 
     */
    public void setRowContent(List<CellType> rowContent) {
        this.rowContent = rowContent;
    }

    @Transient
    public boolean isSetRowContent() {
        return ((this.rowContent!= null)&&(!this.rowContent.isEmpty()));
    }

    public void unsetRowContent() {
        this.rowContent = null;
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
        final ReportContentType that = ((ReportContentType) object);
        {
            int lhsRow;
            lhsRow = this.getRow();
            int rhsRow;
            rhsRow = that.getRow();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "row", lhsRow), LocatorUtils.property(thatLocator, "row", rhsRow), lhsRow, rhsRow)) {
                return false;
            }
        }
        {
            List<CellType> lhsRowContent;
            lhsRowContent = (this.isSetRowContent()?this.getRowContent():null);
            List<CellType> rhsRowContent;
            rhsRowContent = (that.isSetRowContent()?that.getRowContent():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "rowContent", lhsRowContent), LocatorUtils.property(thatLocator, "rowContent", rhsRowContent), lhsRowContent, rhsRowContent)) {
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
            int theRow;
            theRow = this.getRow();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "row", theRow), currentHashCode, theRow);
        }
        {
            List<CellType> theRowContent;
            theRowContent = (this.isSetRowContent()?this.getRowContent():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "rowContent", theRowContent), currentHashCode, theRowContent);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}