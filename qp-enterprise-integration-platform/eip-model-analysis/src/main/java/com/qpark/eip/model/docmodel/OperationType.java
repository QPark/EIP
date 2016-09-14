//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.09.14 um 06:01:36 AM CEST 
//


package com.qpark.eip.model.docmodel;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * This is a combination of request and response ElementTypes defined in a service.
 * 
 * <p>Java-Klasse für OperationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="OperationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType"/&gt;
 *         &lt;element name="modelVersion" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}modelVersionType"/&gt;
 *         &lt;element name="parentId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="shortName" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="namespace" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}namespaceType" minOccurs="0"/&gt;
 *         &lt;element name="securityRoleName" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="requestFieldDescription" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}descriptionType" minOccurs="0"/&gt;
 *         &lt;element name="responseFieldDescription" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}descriptionType" minOccurs="0"/&gt;
 *         &lt;element name="requestResponse" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}RequestResponseDataType"/&gt;
 *         &lt;element name="invokes" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FlowType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationType", propOrder = {
    "id",
    "modelVersion",
    "parentId",
    "name",
    "shortName",
    "namespace",
    "securityRoleName",
    "requestFieldDescription",
    "responseFieldDescription",
    "requestResponse",
    "invokes"
})
@Entity(name = "OperationType")
@Table(name = "OPERATIONTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class OperationType
    implements Serializable, Equals, HashCode
{

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String id;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelVersion;
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String parentId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String shortName;
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String namespace;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String securityRoleName;
    protected String requestFieldDescription;
    protected String responseFieldDescription;
    @XmlElement(required = true)
    protected RequestResponseDataType requestResponse;
    protected List<FlowType> invokes;
    @XmlTransient
    protected Long hjid;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ID", length = 36)
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    @Transient
    public boolean isSetId() {
        return (this.id!= null);
    }

    /**
     * Ruft den Wert der modelVersion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "MODELVERSION", length = 127)
    public String getModelVersion() {
        return modelVersion;
    }

    /**
     * Legt den Wert der modelVersion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelVersion(String value) {
        this.modelVersion = value;
    }

    @Transient
    public boolean isSetModelVersion() {
        return (this.modelVersion!= null);
    }

    /**
     * Ruft den Wert der parentId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "PARENTID", length = 36)
    public String getParentId() {
        return parentId;
    }

    /**
     * Legt den Wert der parentId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentId(String value) {
        this.parentId = value;
    }

    @Transient
    public boolean isSetParentId() {
        return (this.parentId!= null);
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "NAME_", length = 511)
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    @Transient
    public boolean isSetName() {
        return (this.name!= null);
    }

    /**
     * Ruft den Wert der shortName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "SHORTNAME", length = 511)
    public String getShortName() {
        return shortName;
    }

    /**
     * Legt den Wert der shortName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortName(String value) {
        this.shortName = value;
    }

    @Transient
    public boolean isSetShortName() {
        return (this.shortName!= null);
    }

    /**
     * Ruft den Wert der namespace-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "NAMESPACE", length = 511)
    public String getNamespace() {
        return namespace;
    }

    /**
     * Legt den Wert der namespace-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamespace(String value) {
        this.namespace = value;
    }

    @Transient
    public boolean isSetNamespace() {
        return (this.namespace!= null);
    }

    /**
     * Ruft den Wert der securityRoleName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "SECURITYROLENAME", length = 511)
    public String getSecurityRoleName() {
        return securityRoleName;
    }

    /**
     * Legt den Wert der securityRoleName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityRoleName(String value) {
        this.securityRoleName = value;
    }

    @Transient
    public boolean isSetSecurityRoleName() {
        return (this.securityRoleName!= null);
    }

    /**
     * Ruft den Wert der requestFieldDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "REQUESTFIELDDESCRIPTION", length = 2047)
    public String getRequestFieldDescription() {
        return requestFieldDescription;
    }

    /**
     * Legt den Wert der requestFieldDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestFieldDescription(String value) {
        this.requestFieldDescription = value;
    }

    @Transient
    public boolean isSetRequestFieldDescription() {
        return (this.requestFieldDescription!= null);
    }

    /**
     * Ruft den Wert der responseFieldDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "RESPONSEFIELDDESCRIPTION", length = 2047)
    public String getResponseFieldDescription() {
        return responseFieldDescription;
    }

    /**
     * Legt den Wert der responseFieldDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseFieldDescription(String value) {
        this.responseFieldDescription = value;
    }

    @Transient
    public boolean isSetResponseFieldDescription() {
        return (this.responseFieldDescription!= null);
    }

    /**
     * Ruft den Wert der requestResponse-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RequestResponseDataType }
     *     
     */
    @ManyToOne(targetEntity = RequestResponseDataType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "REQUESTRESPONSE_OPERATIONTYP_0")
    public RequestResponseDataType getRequestResponse() {
        return requestResponse;
    }

    /**
     * Legt den Wert der requestResponse-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestResponseDataType }
     *     
     */
    public void setRequestResponse(RequestResponseDataType value) {
        this.requestResponse = value;
    }

    @Transient
    public boolean isSetRequestResponse() {
        return (this.requestResponse!= null);
    }

    /**
     * Gets the value of the invokes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invokes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvokes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlowType }
     * 
     * 
     */
    @OneToMany(targetEntity = FlowType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "INVOKES_OPERATIONTYPE_HJID")
    public List<FlowType> getInvokes() {
        if (invokes == null) {
            invokes = new ArrayList<FlowType>();
        }
        return this.invokes;
    }

    /**
     * 
     * 
     */
    public void setInvokes(List<FlowType> invokes) {
        this.invokes = invokes;
    }

    @Transient
    public boolean isSetInvokes() {
        return ((this.invokes!= null)&&(!this.invokes.isEmpty()));
    }

    public void unsetInvokes() {
        this.invokes = null;
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
        final OperationType that = ((OperationType) object);
        {
            String lhsId;
            lhsId = this.getId();
            String rhsId;
            rhsId = that.getId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "id", lhsId), LocatorUtils.property(thatLocator, "id", rhsId), lhsId, rhsId)) {
                return false;
            }
        }
        {
            String lhsModelVersion;
            lhsModelVersion = this.getModelVersion();
            String rhsModelVersion;
            rhsModelVersion = that.getModelVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "modelVersion", lhsModelVersion), LocatorUtils.property(thatLocator, "modelVersion", rhsModelVersion), lhsModelVersion, rhsModelVersion)) {
                return false;
            }
        }
        {
            String lhsParentId;
            lhsParentId = this.getParentId();
            String rhsParentId;
            rhsParentId = that.getParentId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "parentId", lhsParentId), LocatorUtils.property(thatLocator, "parentId", rhsParentId), lhsParentId, rhsParentId)) {
                return false;
            }
        }
        {
            String lhsName;
            lhsName = this.getName();
            String rhsName;
            rhsName = that.getName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName), LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
                return false;
            }
        }
        {
            String lhsShortName;
            lhsShortName = this.getShortName();
            String rhsShortName;
            rhsShortName = that.getShortName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "shortName", lhsShortName), LocatorUtils.property(thatLocator, "shortName", rhsShortName), lhsShortName, rhsShortName)) {
                return false;
            }
        }
        {
            String lhsNamespace;
            lhsNamespace = this.getNamespace();
            String rhsNamespace;
            rhsNamespace = that.getNamespace();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "namespace", lhsNamespace), LocatorUtils.property(thatLocator, "namespace", rhsNamespace), lhsNamespace, rhsNamespace)) {
                return false;
            }
        }
        {
            String lhsSecurityRoleName;
            lhsSecurityRoleName = this.getSecurityRoleName();
            String rhsSecurityRoleName;
            rhsSecurityRoleName = that.getSecurityRoleName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityRoleName", lhsSecurityRoleName), LocatorUtils.property(thatLocator, "securityRoleName", rhsSecurityRoleName), lhsSecurityRoleName, rhsSecurityRoleName)) {
                return false;
            }
        }
        {
            String lhsRequestFieldDescription;
            lhsRequestFieldDescription = this.getRequestFieldDescription();
            String rhsRequestFieldDescription;
            rhsRequestFieldDescription = that.getRequestFieldDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requestFieldDescription", lhsRequestFieldDescription), LocatorUtils.property(thatLocator, "requestFieldDescription", rhsRequestFieldDescription), lhsRequestFieldDescription, rhsRequestFieldDescription)) {
                return false;
            }
        }
        {
            String lhsResponseFieldDescription;
            lhsResponseFieldDescription = this.getResponseFieldDescription();
            String rhsResponseFieldDescription;
            rhsResponseFieldDescription = that.getResponseFieldDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "responseFieldDescription", lhsResponseFieldDescription), LocatorUtils.property(thatLocator, "responseFieldDescription", rhsResponseFieldDescription), lhsResponseFieldDescription, rhsResponseFieldDescription)) {
                return false;
            }
        }
        {
            RequestResponseDataType lhsRequestResponse;
            lhsRequestResponse = this.getRequestResponse();
            RequestResponseDataType rhsRequestResponse;
            rhsRequestResponse = that.getRequestResponse();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requestResponse", lhsRequestResponse), LocatorUtils.property(thatLocator, "requestResponse", rhsRequestResponse), lhsRequestResponse, rhsRequestResponse)) {
                return false;
            }
        }
        {
            List<FlowType> lhsInvokes;
            lhsInvokes = (this.isSetInvokes()?this.getInvokes():null);
            List<FlowType> rhsInvokes;
            rhsInvokes = (that.isSetInvokes()?that.getInvokes():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "invokes", lhsInvokes), LocatorUtils.property(thatLocator, "invokes", rhsInvokes), lhsInvokes, rhsInvokes)) {
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
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        {
            String theModelVersion;
            theModelVersion = this.getModelVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "modelVersion", theModelVersion), currentHashCode, theModelVersion);
        }
        {
            String theParentId;
            theParentId = this.getParentId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "parentId", theParentId), currentHashCode, theParentId);
        }
        {
            String theName;
            theName = this.getName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name", theName), currentHashCode, theName);
        }
        {
            String theShortName;
            theShortName = this.getShortName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shortName", theShortName), currentHashCode, theShortName);
        }
        {
            String theNamespace;
            theNamespace = this.getNamespace();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "namespace", theNamespace), currentHashCode, theNamespace);
        }
        {
            String theSecurityRoleName;
            theSecurityRoleName = this.getSecurityRoleName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityRoleName", theSecurityRoleName), currentHashCode, theSecurityRoleName);
        }
        {
            String theRequestFieldDescription;
            theRequestFieldDescription = this.getRequestFieldDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requestFieldDescription", theRequestFieldDescription), currentHashCode, theRequestFieldDescription);
        }
        {
            String theResponseFieldDescription;
            theResponseFieldDescription = this.getResponseFieldDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "responseFieldDescription", theResponseFieldDescription), currentHashCode, theResponseFieldDescription);
        }
        {
            RequestResponseDataType theRequestResponse;
            theRequestResponse = this.getRequestResponse();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requestResponse", theRequestResponse), currentHashCode, theRequestResponse);
        }
        {
            List<FlowType> theInvokes;
            theInvokes = (this.isSetInvokes()?this.getInvokes():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "invokes", theInvokes), currentHashCode, theInvokes);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
