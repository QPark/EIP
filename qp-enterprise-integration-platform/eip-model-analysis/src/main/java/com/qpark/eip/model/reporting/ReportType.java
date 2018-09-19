//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.19 at 04:46:23 PM CEST 
//


package com.qpark.eip.model.reporting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XMLGregorianCalendarAsDateTime;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for ReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
@Entity(name = "ReportType")
@Table(name = "REPORTTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ReportType
    implements Serializable, Equals, HashCode
{

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
    @XmlTransient
    protected Long hjid;

    /**
     * Gets the value of the reportUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "REPORTUUID", length = 255)
    public String getReportUUID() {
        return reportUUID;
    }

    /**
     * Sets the value of the reportUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportUUID(String value) {
        this.reportUUID = value;
    }

    @Transient
    public boolean isSetReportUUID() {
        return (this.reportUUID!= null);
    }

    /**
     * Gets the value of the reportName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "REPORTNAME", length = 255)
    public String getReportName() {
        return reportName;
    }

    /**
     * Sets the value of the reportName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportName(String value) {
        this.reportName = value;
    }

    @Transient
    public boolean isSetReportName() {
        return (this.reportName!= null);
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Transient
    public XMLGregorianCalendar getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    @Transient
    public boolean isSetCreated() {
        return (this.created!= null);
    }

    /**
     * Gets the value of the updated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Transient
    public XMLGregorianCalendar getUpdated() {
        return updated;
    }

    /**
     * Sets the value of the updated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdated(XMLGregorianCalendar value) {
        this.updated = value;
    }

    @Transient
    public boolean isSetUpdated() {
        return (this.updated!= null);
    }

    /**
     * Gets the value of the artefact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ARTEFACT", length = 255)
    public String getArtefact() {
        return artefact;
    }

    /**
     * Sets the value of the artefact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtefact(String value) {
        this.artefact = value;
    }

    @Transient
    public boolean isSetArtefact() {
        return (this.artefact!= null);
    }

    /**
     * Gets the value of the artefactVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ARTEFACTVERSION", length = 255)
    public String getArtefactVersion() {
        return artefactVersion;
    }

    /**
     * Sets the value of the artefactVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtefactVersion(String value) {
        this.artefactVersion = value;
    }

    @Transient
    public boolean isSetArtefactVersion() {
        return (this.artefactVersion!= null);
    }

    /**
     * Gets the value of the environment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ENVIRONMENT", length = 255)
    public String getEnvironment() {
        return environment;
    }

    /**
     * Sets the value of the environment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvironment(String value) {
        this.environment = value;
    }

    @Transient
    public boolean isSetEnvironment() {
        return (this.environment!= null);
    }

    /**
     * Gets the value of the reportInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ReportInfoType }
     *     
     */
    @ManyToOne(targetEntity = ReportInfoType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "REPORTINFO_REPORTTYPE_HJID")
    public ReportInfoType getReportInfo() {
        return reportInfo;
    }

    /**
     * Sets the value of the reportInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportInfoType }
     *     
     */
    public void setReportInfo(ReportInfoType value) {
        this.reportInfo = value;
    }

    @Transient
    public boolean isSetReportInfo() {
        return (this.reportInfo!= null);
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
    @OneToMany(targetEntity = ReportHeaderDataType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "REPORTHEADERDATA_REPORTTYPE__0")
    public List<ReportHeaderDataType> getReportHeaderData() {
        if (reportHeaderData == null) {
            reportHeaderData = new ArrayList<ReportHeaderDataType>();
        }
        return this.reportHeaderData;
    }

    /**
     * 
     * 
     */
    public void setReportHeaderData(List<ReportHeaderDataType> reportHeaderData) {
        this.reportHeaderData = reportHeaderData;
    }

    @Transient
    public boolean isSetReportHeaderData() {
        return ((this.reportHeaderData!= null)&&(!this.reportHeaderData.isEmpty()));
    }

    public void unsetReportHeaderData() {
        this.reportHeaderData = null;
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
    @OneToMany(targetEntity = ReportContentType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "REPORTCONTENT_REPORTTYPE_HJID")
    public List<ReportContentType> getReportContent() {
        if (reportContent == null) {
            reportContent = new ArrayList<ReportContentType>();
        }
        return this.reportContent;
    }

    /**
     * 
     * 
     */
    public void setReportContent(List<ReportContentType> reportContent) {
        this.reportContent = reportContent;
    }

    @Transient
    public boolean isSetReportContent() {
        return ((this.reportContent!= null)&&(!this.reportContent.isEmpty()));
    }

    public void unsetReportContent() {
        this.reportContent = null;
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

    @Basic
    @Column(name = "CREATEDITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedItem() {
        return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsDateTime.class, this.getCreated());
    }

    public void setCreatedItem(Date target) {
        setCreated(XmlAdapterUtils.marshall(XMLGregorianCalendarAsDateTime.class, target));
    }

    @Basic
    @Column(name = "UPDATEDITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdatedItem() {
        return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsDateTime.class, this.getUpdated());
    }

    public void setUpdatedItem(Date target) {
        setUpdated(XmlAdapterUtils.marshall(XMLGregorianCalendarAsDateTime.class, target));
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if ((object == null)||(this.getClass()!= object.getClass())) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ReportType that = ((ReportType) object);
        {
            String lhsReportUUID;
            lhsReportUUID = this.getReportUUID();
            String rhsReportUUID;
            rhsReportUUID = that.getReportUUID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "reportUUID", lhsReportUUID), LocatorUtils.property(thatLocator, "reportUUID", rhsReportUUID), lhsReportUUID, rhsReportUUID)) {
                return false;
            }
        }
        {
            String lhsReportName;
            lhsReportName = this.getReportName();
            String rhsReportName;
            rhsReportName = that.getReportName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "reportName", lhsReportName), LocatorUtils.property(thatLocator, "reportName", rhsReportName), lhsReportName, rhsReportName)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsCreated;
            lhsCreated = this.getCreated();
            XMLGregorianCalendar rhsCreated;
            rhsCreated = that.getCreated();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "created", lhsCreated), LocatorUtils.property(thatLocator, "created", rhsCreated), lhsCreated, rhsCreated)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsUpdated;
            lhsUpdated = this.getUpdated();
            XMLGregorianCalendar rhsUpdated;
            rhsUpdated = that.getUpdated();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "updated", lhsUpdated), LocatorUtils.property(thatLocator, "updated", rhsUpdated), lhsUpdated, rhsUpdated)) {
                return false;
            }
        }
        {
            String lhsArtefact;
            lhsArtefact = this.getArtefact();
            String rhsArtefact;
            rhsArtefact = that.getArtefact();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "artefact", lhsArtefact), LocatorUtils.property(thatLocator, "artefact", rhsArtefact), lhsArtefact, rhsArtefact)) {
                return false;
            }
        }
        {
            String lhsArtefactVersion;
            lhsArtefactVersion = this.getArtefactVersion();
            String rhsArtefactVersion;
            rhsArtefactVersion = that.getArtefactVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "artefactVersion", lhsArtefactVersion), LocatorUtils.property(thatLocator, "artefactVersion", rhsArtefactVersion), lhsArtefactVersion, rhsArtefactVersion)) {
                return false;
            }
        }
        {
            String lhsEnvironment;
            lhsEnvironment = this.getEnvironment();
            String rhsEnvironment;
            rhsEnvironment = that.getEnvironment();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "environment", lhsEnvironment), LocatorUtils.property(thatLocator, "environment", rhsEnvironment), lhsEnvironment, rhsEnvironment)) {
                return false;
            }
        }
        {
            ReportInfoType lhsReportInfo;
            lhsReportInfo = this.getReportInfo();
            ReportInfoType rhsReportInfo;
            rhsReportInfo = that.getReportInfo();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "reportInfo", lhsReportInfo), LocatorUtils.property(thatLocator, "reportInfo", rhsReportInfo), lhsReportInfo, rhsReportInfo)) {
                return false;
            }
        }
        {
            List<ReportHeaderDataType> lhsReportHeaderData;
            lhsReportHeaderData = (this.isSetReportHeaderData()?this.getReportHeaderData():null);
            List<ReportHeaderDataType> rhsReportHeaderData;
            rhsReportHeaderData = (that.isSetReportHeaderData()?that.getReportHeaderData():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "reportHeaderData", lhsReportHeaderData), LocatorUtils.property(thatLocator, "reportHeaderData", rhsReportHeaderData), lhsReportHeaderData, rhsReportHeaderData)) {
                return false;
            }
        }
        {
            List<ReportContentType> lhsReportContent;
            lhsReportContent = (this.isSetReportContent()?this.getReportContent():null);
            List<ReportContentType> rhsReportContent;
            rhsReportContent = (that.isSetReportContent()?that.getReportContent():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "reportContent", lhsReportContent), LocatorUtils.property(thatLocator, "reportContent", rhsReportContent), lhsReportContent, rhsReportContent)) {
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
            String theReportUUID;
            theReportUUID = this.getReportUUID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "reportUUID", theReportUUID), currentHashCode, theReportUUID);
        }
        {
            String theReportName;
            theReportName = this.getReportName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "reportName", theReportName), currentHashCode, theReportName);
        }
        {
            XMLGregorianCalendar theCreated;
            theCreated = this.getCreated();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "created", theCreated), currentHashCode, theCreated);
        }
        {
            XMLGregorianCalendar theUpdated;
            theUpdated = this.getUpdated();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "updated", theUpdated), currentHashCode, theUpdated);
        }
        {
            String theArtefact;
            theArtefact = this.getArtefact();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "artefact", theArtefact), currentHashCode, theArtefact);
        }
        {
            String theArtefactVersion;
            theArtefactVersion = this.getArtefactVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "artefactVersion", theArtefactVersion), currentHashCode, theArtefactVersion);
        }
        {
            String theEnvironment;
            theEnvironment = this.getEnvironment();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "environment", theEnvironment), currentHashCode, theEnvironment);
        }
        {
            ReportInfoType theReportInfo;
            theReportInfo = this.getReportInfo();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "reportInfo", theReportInfo), currentHashCode, theReportInfo);
        }
        {
            List<ReportHeaderDataType> theReportHeaderData;
            theReportHeaderData = (this.isSetReportHeaderData()?this.getReportHeaderData():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "reportHeaderData", theReportHeaderData), currentHashCode, theReportHeaderData);
        }
        {
            List<ReportContentType> theReportContent;
            theReportContent = (this.isSetReportContent()?this.getReportContent():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "reportContent", theReportContent), currentHashCode, theReportContent);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
