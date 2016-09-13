//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.09.13 um 10:39:07 PM CEST 
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
 * This is the flow executeRequest or processResponse.
 * 
 * <p>Java-Klasse für FlowProcessType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FlowProcessType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType"/&gt;
 *         &lt;element name="modelVersion" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}modelVersionType"/&gt;
 *         &lt;element name="parentId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="namespace" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}namespaceType" minOccurs="0"/&gt;
 *         &lt;element name="requestResponse" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}RequestResponseDataType"/&gt;
 *         &lt;element name="subRequest" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FlowSubRequestType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="filter" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FlowFilterType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="mapInOut" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FlowMapInOutType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlowProcessType", propOrder = {
    "id",
    "modelVersion",
    "parentId",
    "name",
    "namespace",
    "requestResponse",
    "subRequest",
    "filter",
    "mapInOut"
})
@Entity(name = "FlowProcessType")
@Table(name = "FLOWPROCESSTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class FlowProcessType
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
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String namespace;
    @XmlElement(required = true)
    protected RequestResponseDataType requestResponse;
    protected List<FlowSubRequestType> subRequest;
    protected List<FlowFilterType> filter;
    protected List<FlowMapInOutType> mapInOut;
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
    @JoinColumn(name = "REQUESTRESPONSE_FLOWPROCESST_0")
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
     * Gets the value of the subRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlowSubRequestType }
     * 
     * 
     */
    @OneToMany(targetEntity = FlowSubRequestType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "SUBREQUEST_FLOWPROCESSTYPE_H_0")
    public List<FlowSubRequestType> getSubRequest() {
        if (subRequest == null) {
            subRequest = new ArrayList<FlowSubRequestType>();
        }
        return this.subRequest;
    }

    /**
     * 
     * 
     */
    public void setSubRequest(List<FlowSubRequestType> subRequest) {
        this.subRequest = subRequest;
    }

    @Transient
    public boolean isSetSubRequest() {
        return ((this.subRequest!= null)&&(!this.subRequest.isEmpty()));
    }

    public void unsetSubRequest() {
        this.subRequest = null;
    }

    /**
     * Gets the value of the filter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlowFilterType }
     * 
     * 
     */
    @OneToMany(targetEntity = FlowFilterType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "FILTER_FLOWPROCESSTYPE_HJID")
    public List<FlowFilterType> getFilter() {
        if (filter == null) {
            filter = new ArrayList<FlowFilterType>();
        }
        return this.filter;
    }

    /**
     * 
     * 
     */
    public void setFilter(List<FlowFilterType> filter) {
        this.filter = filter;
    }

    @Transient
    public boolean isSetFilter() {
        return ((this.filter!= null)&&(!this.filter.isEmpty()));
    }

    public void unsetFilter() {
        this.filter = null;
    }

    /**
     * Gets the value of the mapInOut property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mapInOut property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMapInOut().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlowMapInOutType }
     * 
     * 
     */
    @OneToMany(targetEntity = FlowMapInOutType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "MAPINOUT_FLOWPROCESSTYPE_HJID")
    public List<FlowMapInOutType> getMapInOut() {
        if (mapInOut == null) {
            mapInOut = new ArrayList<FlowMapInOutType>();
        }
        return this.mapInOut;
    }

    /**
     * 
     * 
     */
    public void setMapInOut(List<FlowMapInOutType> mapInOut) {
        this.mapInOut = mapInOut;
    }

    @Transient
    public boolean isSetMapInOut() {
        return ((this.mapInOut!= null)&&(!this.mapInOut.isEmpty()));
    }

    public void unsetMapInOut() {
        this.mapInOut = null;
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
        final FlowProcessType that = ((FlowProcessType) object);
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
            String lhsNamespace;
            lhsNamespace = this.getNamespace();
            String rhsNamespace;
            rhsNamespace = that.getNamespace();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "namespace", lhsNamespace), LocatorUtils.property(thatLocator, "namespace", rhsNamespace), lhsNamespace, rhsNamespace)) {
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
            List<FlowSubRequestType> lhsSubRequest;
            lhsSubRequest = (this.isSetSubRequest()?this.getSubRequest():null);
            List<FlowSubRequestType> rhsSubRequest;
            rhsSubRequest = (that.isSetSubRequest()?that.getSubRequest():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "subRequest", lhsSubRequest), LocatorUtils.property(thatLocator, "subRequest", rhsSubRequest), lhsSubRequest, rhsSubRequest)) {
                return false;
            }
        }
        {
            List<FlowFilterType> lhsFilter;
            lhsFilter = (this.isSetFilter()?this.getFilter():null);
            List<FlowFilterType> rhsFilter;
            rhsFilter = (that.isSetFilter()?that.getFilter():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "filter", lhsFilter), LocatorUtils.property(thatLocator, "filter", rhsFilter), lhsFilter, rhsFilter)) {
                return false;
            }
        }
        {
            List<FlowMapInOutType> lhsMapInOut;
            lhsMapInOut = (this.isSetMapInOut()?this.getMapInOut():null);
            List<FlowMapInOutType> rhsMapInOut;
            rhsMapInOut = (that.isSetMapInOut()?that.getMapInOut():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "mapInOut", lhsMapInOut), LocatorUtils.property(thatLocator, "mapInOut", rhsMapInOut), lhsMapInOut, rhsMapInOut)) {
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
            String theNamespace;
            theNamespace = this.getNamespace();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "namespace", theNamespace), currentHashCode, theNamespace);
        }
        {
            RequestResponseDataType theRequestResponse;
            theRequestResponse = this.getRequestResponse();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requestResponse", theRequestResponse), currentHashCode, theRequestResponse);
        }
        {
            List<FlowSubRequestType> theSubRequest;
            theSubRequest = (this.isSetSubRequest()?this.getSubRequest():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "subRequest", theSubRequest), currentHashCode, theSubRequest);
        }
        {
            List<FlowFilterType> theFilter;
            theFilter = (this.isSetFilter()?this.getFilter():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "filter", theFilter), currentHashCode, theFilter);
        }
        {
            List<FlowMapInOutType> theMapInOut;
            theMapInOut = (this.isSetMapInOut()?this.getMapInOut():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "mapInOut", theMapInOut), currentHashCode, theMapInOut);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
