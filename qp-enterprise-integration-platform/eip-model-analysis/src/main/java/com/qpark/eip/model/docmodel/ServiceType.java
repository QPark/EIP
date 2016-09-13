//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.09.13 um 08:58:01 PM CEST 
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
 * This is basically the schema file. 
 * 
 * <p>Java-Klasse für ServiceType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ServiceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType"/&gt;
 *         &lt;element name="modelVersion" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}modelVersionType"/&gt;
 *         &lt;element name="parentId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="namespace" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}namespaceType" minOccurs="0"/&gt;
 *         &lt;element name="clusterId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType"/&gt;
 *         &lt;element name="serviceId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="packageName" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="securityRoleName" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="operation" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}OperationType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}descriptionType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceType", propOrder = {
    "id",
    "modelVersion",
    "parentId",
    "name",
    "namespace",
    "clusterId",
    "serviceId",
    "packageName",
    "securityRoleName",
    "operation",
    "description"
})
@Entity(name = "ServiceType")
@Table(name = "SERVICETYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ServiceType
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
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String clusterId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String serviceId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String packageName;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String securityRoleName;
    protected List<OperationType> operation;
    @XmlElement(required = true)
    protected String description;
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
     * Ruft den Wert der clusterId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "CLUSTERID", length = 36)
    public String getClusterId() {
        return clusterId;
    }

    /**
     * Legt den Wert der clusterId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClusterId(String value) {
        this.clusterId = value;
    }

    @Transient
    public boolean isSetClusterId() {
        return (this.clusterId!= null);
    }

    /**
     * Ruft den Wert der serviceId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "SERVICEID", length = 511)
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Legt den Wert der serviceId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    @Transient
    public boolean isSetServiceId() {
        return (this.serviceId!= null);
    }

    /**
     * Ruft den Wert der packageName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "PACKAGENAME", length = 511)
    public String getPackageName() {
        return packageName;
    }

    /**
     * Legt den Wert der packageName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageName(String value) {
        this.packageName = value;
    }

    @Transient
    public boolean isSetPackageName() {
        return (this.packageName!= null);
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
     * Gets the value of the operation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OperationType }
     * 
     * 
     */
    @OneToMany(targetEntity = OperationType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "OPERATION__SERVICETYPE_HJID")
    public List<OperationType> getOperation() {
        if (operation == null) {
            operation = new ArrayList<OperationType>();
        }
        return this.operation;
    }

    /**
     * 
     * 
     */
    public void setOperation(List<OperationType> operation) {
        this.operation = operation;
    }

    @Transient
    public boolean isSetOperation() {
        return ((this.operation!= null)&&(!this.operation.isEmpty()));
    }

    public void unsetOperation() {
        this.operation = null;
    }

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "DESCRIPTION", length = 2047)
    public String getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    @Transient
    public boolean isSetDescription() {
        return (this.description!= null);
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
        final ServiceType that = ((ServiceType) object);
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
            String lhsClusterId;
            lhsClusterId = this.getClusterId();
            String rhsClusterId;
            rhsClusterId = that.getClusterId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "clusterId", lhsClusterId), LocatorUtils.property(thatLocator, "clusterId", rhsClusterId), lhsClusterId, rhsClusterId)) {
                return false;
            }
        }
        {
            String lhsServiceId;
            lhsServiceId = this.getServiceId();
            String rhsServiceId;
            rhsServiceId = that.getServiceId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "serviceId", lhsServiceId), LocatorUtils.property(thatLocator, "serviceId", rhsServiceId), lhsServiceId, rhsServiceId)) {
                return false;
            }
        }
        {
            String lhsPackageName;
            lhsPackageName = this.getPackageName();
            String rhsPackageName;
            rhsPackageName = that.getPackageName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "packageName", lhsPackageName), LocatorUtils.property(thatLocator, "packageName", rhsPackageName), lhsPackageName, rhsPackageName)) {
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
            List<OperationType> lhsOperation;
            lhsOperation = (this.isSetOperation()?this.getOperation():null);
            List<OperationType> rhsOperation;
            rhsOperation = (that.isSetOperation()?that.getOperation():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "operation", lhsOperation), LocatorUtils.property(thatLocator, "operation", rhsOperation), lhsOperation, rhsOperation)) {
                return false;
            }
        }
        {
            String lhsDescription;
            lhsDescription = this.getDescription();
            String rhsDescription;
            rhsDescription = that.getDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
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
            String theClusterId;
            theClusterId = this.getClusterId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "clusterId", theClusterId), currentHashCode, theClusterId);
        }
        {
            String theServiceId;
            theServiceId = this.getServiceId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "serviceId", theServiceId), currentHashCode, theServiceId);
        }
        {
            String thePackageName;
            thePackageName = this.getPackageName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "packageName", thePackageName), currentHashCode, thePackageName);
        }
        {
            String theSecurityRoleName;
            theSecurityRoleName = this.getSecurityRoleName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityRoleName", theSecurityRoleName), currentHashCode, theSecurityRoleName);
        }
        {
            List<OperationType> theOperation;
            theOperation = (this.isSetOperation()?this.getOperation():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "operation", theOperation), currentHashCode, theOperation);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
