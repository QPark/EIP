//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.02.20 um 06:03:00 AM CET 
//


package com.qpark.eip.model.docmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
 * <p>Java-Klasse für ClusterType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ClusterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType"/&gt;
 *         &lt;element name="modelVersion" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}modelVersionType"/&gt;
 *         &lt;element name="parentId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="fileName" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="packageName" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="description" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}descriptionType"/&gt;
 *         &lt;element name="complexType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}ComplexType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="interfaceMappingType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}InterfaceMappingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="directMappingType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DirectMappingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="defaultMappingType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DefaultMappingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="complexMappingType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}ComplexMappingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="complexUUIDMappingType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}ComplexUUIDMappingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="elementType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}ElementType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="warning" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}descriptionType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClusterType", propOrder = {
    "id",
    "modelVersion",
    "parentId",
    "name",
    "fileName",
    "version",
    "packageName",
    "description",
    "complexType",
    "interfaceMappingType",
    "directMappingType",
    "defaultMappingType",
    "complexMappingType",
    "complexUUIDMappingType",
    "elementType",
    "warning"
})
@Entity(name = "ClusterType")
@Table(name = "CLUSTERTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ClusterType
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
    protected String fileName;
    @XmlElement(required = true)
    protected String version;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String packageName;
    @XmlElement(required = true)
    protected String description;
    protected List<ComplexType> complexType;
    protected List<InterfaceMappingType> interfaceMappingType;
    protected List<DirectMappingType> directMappingType;
    protected List<DefaultMappingType> defaultMappingType;
    protected List<ComplexMappingType> complexMappingType;
    protected List<ComplexUUIDMappingType> complexUUIDMappingType;
    protected List<ElementType> elementType;
    protected List<String> warning;
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
     * Ruft den Wert der fileName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "FILENAME", length = 511)
    public String getFileName() {
        return fileName;
    }

    /**
     * Legt den Wert der fileName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    @Transient
    public boolean isSetFileName() {
        return (this.fileName!= null);
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "VERSION_", length = 255)
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    @Transient
    public boolean isSetVersion() {
        return (this.version!= null);
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
     * Gets the value of the complexType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complexType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplexType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComplexType }
     * 
     * 
     */
    @OneToMany(targetEntity = ComplexType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "COMPLEXTYPE_CLUSTERTYPE_HJID")
    public List<ComplexType> getComplexType() {
        if (complexType == null) {
            complexType = new ArrayList<ComplexType>();
        }
        return this.complexType;
    }

    /**
     * 
     * 
     */
    public void setComplexType(List<ComplexType> complexType) {
        this.complexType = complexType;
    }

    @Transient
    public boolean isSetComplexType() {
        return ((this.complexType!= null)&&(!this.complexType.isEmpty()));
    }

    public void unsetComplexType() {
        this.complexType = null;
    }

    /**
     * Gets the value of the interfaceMappingType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interfaceMappingType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterfaceMappingType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InterfaceMappingType }
     * 
     * 
     */
    @OneToMany(targetEntity = InterfaceMappingType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "INTERFACEMAPPINGTYPE_CLUSTER_0")
    public List<InterfaceMappingType> getInterfaceMappingType() {
        if (interfaceMappingType == null) {
            interfaceMappingType = new ArrayList<InterfaceMappingType>();
        }
        return this.interfaceMappingType;
    }

    /**
     * 
     * 
     */
    public void setInterfaceMappingType(List<InterfaceMappingType> interfaceMappingType) {
        this.interfaceMappingType = interfaceMappingType;
    }

    @Transient
    public boolean isSetInterfaceMappingType() {
        return ((this.interfaceMappingType!= null)&&(!this.interfaceMappingType.isEmpty()));
    }

    public void unsetInterfaceMappingType() {
        this.interfaceMappingType = null;
    }

    /**
     * Gets the value of the directMappingType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the directMappingType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirectMappingType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DirectMappingType }
     * 
     * 
     */
    @OneToMany(targetEntity = DirectMappingType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "DIRECTMAPPINGTYPE_CLUSTERTYP_0")
    public List<DirectMappingType> getDirectMappingType() {
        if (directMappingType == null) {
            directMappingType = new ArrayList<DirectMappingType>();
        }
        return this.directMappingType;
    }

    /**
     * 
     * 
     */
    public void setDirectMappingType(List<DirectMappingType> directMappingType) {
        this.directMappingType = directMappingType;
    }

    @Transient
    public boolean isSetDirectMappingType() {
        return ((this.directMappingType!= null)&&(!this.directMappingType.isEmpty()));
    }

    public void unsetDirectMappingType() {
        this.directMappingType = null;
    }

    /**
     * Gets the value of the defaultMappingType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defaultMappingType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefaultMappingType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DefaultMappingType }
     * 
     * 
     */
    @OneToMany(targetEntity = DefaultMappingType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "DEFAULTMAPPINGTYPE_CLUSTERTY_0")
    public List<DefaultMappingType> getDefaultMappingType() {
        if (defaultMappingType == null) {
            defaultMappingType = new ArrayList<DefaultMappingType>();
        }
        return this.defaultMappingType;
    }

    /**
     * 
     * 
     */
    public void setDefaultMappingType(List<DefaultMappingType> defaultMappingType) {
        this.defaultMappingType = defaultMappingType;
    }

    @Transient
    public boolean isSetDefaultMappingType() {
        return ((this.defaultMappingType!= null)&&(!this.defaultMappingType.isEmpty()));
    }

    public void unsetDefaultMappingType() {
        this.defaultMappingType = null;
    }

    /**
     * Gets the value of the complexMappingType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complexMappingType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplexMappingType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComplexMappingType }
     * 
     * 
     */
    @OneToMany(targetEntity = ComplexMappingType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "COMPLEXMAPPINGTYPE_CLUSTERTY_0")
    public List<ComplexMappingType> getComplexMappingType() {
        if (complexMappingType == null) {
            complexMappingType = new ArrayList<ComplexMappingType>();
        }
        return this.complexMappingType;
    }

    /**
     * 
     * 
     */
    public void setComplexMappingType(List<ComplexMappingType> complexMappingType) {
        this.complexMappingType = complexMappingType;
    }

    @Transient
    public boolean isSetComplexMappingType() {
        return ((this.complexMappingType!= null)&&(!this.complexMappingType.isEmpty()));
    }

    public void unsetComplexMappingType() {
        this.complexMappingType = null;
    }

    /**
     * Gets the value of the complexUUIDMappingType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complexUUIDMappingType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplexUUIDMappingType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComplexUUIDMappingType }
     * 
     * 
     */
    @OneToMany(targetEntity = ComplexUUIDMappingType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "COMPLEXUUIDMAPPINGTYPE_CLUST_0")
    public List<ComplexUUIDMappingType> getComplexUUIDMappingType() {
        if (complexUUIDMappingType == null) {
            complexUUIDMappingType = new ArrayList<ComplexUUIDMappingType>();
        }
        return this.complexUUIDMappingType;
    }

    /**
     * 
     * 
     */
    public void setComplexUUIDMappingType(List<ComplexUUIDMappingType> complexUUIDMappingType) {
        this.complexUUIDMappingType = complexUUIDMappingType;
    }

    @Transient
    public boolean isSetComplexUUIDMappingType() {
        return ((this.complexUUIDMappingType!= null)&&(!this.complexUUIDMappingType.isEmpty()));
    }

    public void unsetComplexUUIDMappingType() {
        this.complexUUIDMappingType = null;
    }

    /**
     * Gets the value of the elementType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elementType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElementType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ElementType }
     * 
     * 
     */
    @OneToMany(targetEntity = ElementType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ELEMENTTYPE_CLUSTERTYPE_HJID")
    public List<ElementType> getElementType() {
        if (elementType == null) {
            elementType = new ArrayList<ElementType>();
        }
        return this.elementType;
    }

    /**
     * 
     * 
     */
    public void setElementType(List<ElementType> elementType) {
        this.elementType = elementType;
    }

    @Transient
    public boolean isSetElementType() {
        return ((this.elementType!= null)&&(!this.elementType.isEmpty()));
    }

    public void unsetElementType() {
        this.elementType = null;
    }

    /**
     * Gets the value of the warning property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the warning property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWarning().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    @ElementCollection
    @OrderColumn(name = "HJINDEX")
    @Column(name = "HJVALUE", length = 2047)
    @CollectionTable(name = "CLUSTERTYPE_WARNING", joinColumns = {
        @JoinColumn(name = "HJID")
    })
    public List<String> getWarning() {
        if (warning == null) {
            warning = new ArrayList<String>();
        }
        return this.warning;
    }

    /**
     * 
     * 
     */
    public void setWarning(List<String> warning) {
        this.warning = warning;
    }

    @Transient
    public boolean isSetWarning() {
        return ((this.warning!= null)&&(!this.warning.isEmpty()));
    }

    public void unsetWarning() {
        this.warning = null;
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
        final ClusterType that = ((ClusterType) object);
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
            String lhsFileName;
            lhsFileName = this.getFileName();
            String rhsFileName;
            rhsFileName = that.getFileName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "fileName", lhsFileName), LocatorUtils.property(thatLocator, "fileName", rhsFileName), lhsFileName, rhsFileName)) {
                return false;
            }
        }
        {
            String lhsVersion;
            lhsVersion = this.getVersion();
            String rhsVersion;
            rhsVersion = that.getVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "version", lhsVersion), LocatorUtils.property(thatLocator, "version", rhsVersion), lhsVersion, rhsVersion)) {
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
            String lhsDescription;
            lhsDescription = this.getDescription();
            String rhsDescription;
            rhsDescription = that.getDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
                return false;
            }
        }
        {
            List<ComplexType> lhsComplexType;
            lhsComplexType = (this.isSetComplexType()?this.getComplexType():null);
            List<ComplexType> rhsComplexType;
            rhsComplexType = (that.isSetComplexType()?that.getComplexType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "complexType", lhsComplexType), LocatorUtils.property(thatLocator, "complexType", rhsComplexType), lhsComplexType, rhsComplexType)) {
                return false;
            }
        }
        {
            List<InterfaceMappingType> lhsInterfaceMappingType;
            lhsInterfaceMappingType = (this.isSetInterfaceMappingType()?this.getInterfaceMappingType():null);
            List<InterfaceMappingType> rhsInterfaceMappingType;
            rhsInterfaceMappingType = (that.isSetInterfaceMappingType()?that.getInterfaceMappingType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "interfaceMappingType", lhsInterfaceMappingType), LocatorUtils.property(thatLocator, "interfaceMappingType", rhsInterfaceMappingType), lhsInterfaceMappingType, rhsInterfaceMappingType)) {
                return false;
            }
        }
        {
            List<DirectMappingType> lhsDirectMappingType;
            lhsDirectMappingType = (this.isSetDirectMappingType()?this.getDirectMappingType():null);
            List<DirectMappingType> rhsDirectMappingType;
            rhsDirectMappingType = (that.isSetDirectMappingType()?that.getDirectMappingType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "directMappingType", lhsDirectMappingType), LocatorUtils.property(thatLocator, "directMappingType", rhsDirectMappingType), lhsDirectMappingType, rhsDirectMappingType)) {
                return false;
            }
        }
        {
            List<DefaultMappingType> lhsDefaultMappingType;
            lhsDefaultMappingType = (this.isSetDefaultMappingType()?this.getDefaultMappingType():null);
            List<DefaultMappingType> rhsDefaultMappingType;
            rhsDefaultMappingType = (that.isSetDefaultMappingType()?that.getDefaultMappingType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "defaultMappingType", lhsDefaultMappingType), LocatorUtils.property(thatLocator, "defaultMappingType", rhsDefaultMappingType), lhsDefaultMappingType, rhsDefaultMappingType)) {
                return false;
            }
        }
        {
            List<ComplexMappingType> lhsComplexMappingType;
            lhsComplexMappingType = (this.isSetComplexMappingType()?this.getComplexMappingType():null);
            List<ComplexMappingType> rhsComplexMappingType;
            rhsComplexMappingType = (that.isSetComplexMappingType()?that.getComplexMappingType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "complexMappingType", lhsComplexMappingType), LocatorUtils.property(thatLocator, "complexMappingType", rhsComplexMappingType), lhsComplexMappingType, rhsComplexMappingType)) {
                return false;
            }
        }
        {
            List<ComplexUUIDMappingType> lhsComplexUUIDMappingType;
            lhsComplexUUIDMappingType = (this.isSetComplexUUIDMappingType()?this.getComplexUUIDMappingType():null);
            List<ComplexUUIDMappingType> rhsComplexUUIDMappingType;
            rhsComplexUUIDMappingType = (that.isSetComplexUUIDMappingType()?that.getComplexUUIDMappingType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "complexUUIDMappingType", lhsComplexUUIDMappingType), LocatorUtils.property(thatLocator, "complexUUIDMappingType", rhsComplexUUIDMappingType), lhsComplexUUIDMappingType, rhsComplexUUIDMappingType)) {
                return false;
            }
        }
        {
            List<ElementType> lhsElementType;
            lhsElementType = (this.isSetElementType()?this.getElementType():null);
            List<ElementType> rhsElementType;
            rhsElementType = (that.isSetElementType()?that.getElementType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "elementType", lhsElementType), LocatorUtils.property(thatLocator, "elementType", rhsElementType), lhsElementType, rhsElementType)) {
                return false;
            }
        }
        {
            List<String> lhsWarning;
            lhsWarning = (this.isSetWarning()?this.getWarning():null);
            List<String> rhsWarning;
            rhsWarning = (that.isSetWarning()?that.getWarning():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "warning", lhsWarning), LocatorUtils.property(thatLocator, "warning", rhsWarning), lhsWarning, rhsWarning)) {
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
            String theFileName;
            theFileName = this.getFileName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "fileName", theFileName), currentHashCode, theFileName);
        }
        {
            String theVersion;
            theVersion = this.getVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "version", theVersion), currentHashCode, theVersion);
        }
        {
            String thePackageName;
            thePackageName = this.getPackageName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "packageName", thePackageName), currentHashCode, thePackageName);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            List<ComplexType> theComplexType;
            theComplexType = (this.isSetComplexType()?this.getComplexType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "complexType", theComplexType), currentHashCode, theComplexType);
        }
        {
            List<InterfaceMappingType> theInterfaceMappingType;
            theInterfaceMappingType = (this.isSetInterfaceMappingType()?this.getInterfaceMappingType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "interfaceMappingType", theInterfaceMappingType), currentHashCode, theInterfaceMappingType);
        }
        {
            List<DirectMappingType> theDirectMappingType;
            theDirectMappingType = (this.isSetDirectMappingType()?this.getDirectMappingType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "directMappingType", theDirectMappingType), currentHashCode, theDirectMappingType);
        }
        {
            List<DefaultMappingType> theDefaultMappingType;
            theDefaultMappingType = (this.isSetDefaultMappingType()?this.getDefaultMappingType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "defaultMappingType", theDefaultMappingType), currentHashCode, theDefaultMappingType);
        }
        {
            List<ComplexMappingType> theComplexMappingType;
            theComplexMappingType = (this.isSetComplexMappingType()?this.getComplexMappingType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "complexMappingType", theComplexMappingType), currentHashCode, theComplexMappingType);
        }
        {
            List<ComplexUUIDMappingType> theComplexUUIDMappingType;
            theComplexUUIDMappingType = (this.isSetComplexUUIDMappingType()?this.getComplexUUIDMappingType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "complexUUIDMappingType", theComplexUUIDMappingType), currentHashCode, theComplexUUIDMappingType);
        }
        {
            List<ElementType> theElementType;
            theElementType = (this.isSetElementType()?this.getElementType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "elementType", theElementType), currentHashCode, theElementType);
        }
        {
            List<String> theWarning;
            theWarning = (this.isSetWarning()?this.getWarning():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "warning", theWarning), currentHashCode, theWarning);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}