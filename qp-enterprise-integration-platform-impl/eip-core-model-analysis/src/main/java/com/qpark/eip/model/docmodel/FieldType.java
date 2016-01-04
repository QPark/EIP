//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.01.04 um 07:31:49 AM CET 
//


package com.qpark.eip.model.docmodel;

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
 * This is a element of a sequence inside a complexType.
 * 
 * <p>Java-Klasse für FieldType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FieldType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="parentId" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="namespace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fieldTypeDefinitionId" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="cardinality" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="cardinalityMinOccurs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="cardinalityMaxOccurs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="listField" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="optionalField" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldType", propOrder = {
    "id",
    "parentId",
    "name",
    "namespace",
    "fieldTypeDefinitionId",
    "cardinality",
    "cardinalityMinOccurs",
    "cardinalityMaxOccurs",
    "listField",
    "optionalField",
    "description"
})
@Entity(name = "FieldType")
@Table(name = "FIELDTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class FieldType
    implements Serializable, Equals, HashCode
{

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String id;
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String parentId;
    @XmlElement(required = true)
    protected String name;
    protected String namespace;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String fieldTypeDefinitionId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String cardinality;
    protected Integer cardinalityMinOccurs;
    protected Integer cardinalityMaxOccurs;
    protected boolean listField;
    protected boolean optionalField;
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
    @Column(name = "ID", length = 255)
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
     * Ruft den Wert der parentId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "PARENTID", length = 255)
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
    @Column(name = "NAME_", length = 255)
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
    @Column(name = "NAMESPACE", length = 255)
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
     * Ruft den Wert der fieldTypeDefinitionId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "FIELDTYPEDEFINITIONID", length = 255)
    public String getFieldTypeDefinitionId() {
        return fieldTypeDefinitionId;
    }

    /**
     * Legt den Wert der fieldTypeDefinitionId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldTypeDefinitionId(String value) {
        this.fieldTypeDefinitionId = value;
    }

    @Transient
    public boolean isSetFieldTypeDefinitionId() {
        return (this.fieldTypeDefinitionId!= null);
    }

    /**
     * Ruft den Wert der cardinality-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "CARDINALITY_", length = 255)
    public String getCardinality() {
        return cardinality;
    }

    /**
     * Legt den Wert der cardinality-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardinality(String value) {
        this.cardinality = value;
    }

    @Transient
    public boolean isSetCardinality() {
        return (this.cardinality!= null);
    }

    /**
     * Ruft den Wert der cardinalityMinOccurs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    @Basic
    @Column(name = "CARDINALITYMINOCCURS", precision = 10, scale = 0)
    public Integer getCardinalityMinOccurs() {
        return cardinalityMinOccurs;
    }

    /**
     * Legt den Wert der cardinalityMinOccurs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCardinalityMinOccurs(Integer value) {
        this.cardinalityMinOccurs = value;
    }

    @Transient
    public boolean isSetCardinalityMinOccurs() {
        return (this.cardinalityMinOccurs!= null);
    }

    /**
     * Ruft den Wert der cardinalityMaxOccurs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    @Basic
    @Column(name = "CARDINALITYMAXOCCURS", precision = 10, scale = 0)
    public Integer getCardinalityMaxOccurs() {
        return cardinalityMaxOccurs;
    }

    /**
     * Legt den Wert der cardinalityMaxOccurs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCardinalityMaxOccurs(Integer value) {
        this.cardinalityMaxOccurs = value;
    }

    @Transient
    public boolean isSetCardinalityMaxOccurs() {
        return (this.cardinalityMaxOccurs!= null);
    }

    /**
     * Ruft den Wert der listField-Eigenschaft ab.
     * 
     */
    @Basic
    @Column(name = "LISTFIELD")
    public boolean isListField() {
        return listField;
    }

    /**
     * Legt den Wert der listField-Eigenschaft fest.
     * 
     */
    public void setListField(boolean value) {
        this.listField = value;
    }

    @Transient
    public boolean isSetListField() {
        return true;
    }

    /**
     * Ruft den Wert der optionalField-Eigenschaft ab.
     * 
     */
    @Basic
    @Column(name = "OPTIONALFIELD")
    public boolean isOptionalField() {
        return optionalField;
    }

    /**
     * Legt den Wert der optionalField-Eigenschaft fest.
     * 
     */
    public void setOptionalField(boolean value) {
        this.optionalField = value;
    }

    @Transient
    public boolean isSetOptionalField() {
        return true;
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
    @Column(name = "DESCRIPTION", length = 255)
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
        final FieldType that = ((FieldType) object);
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
            String lhsFieldTypeDefinitionId;
            lhsFieldTypeDefinitionId = this.getFieldTypeDefinitionId();
            String rhsFieldTypeDefinitionId;
            rhsFieldTypeDefinitionId = that.getFieldTypeDefinitionId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "fieldTypeDefinitionId", lhsFieldTypeDefinitionId), LocatorUtils.property(thatLocator, "fieldTypeDefinitionId", rhsFieldTypeDefinitionId), lhsFieldTypeDefinitionId, rhsFieldTypeDefinitionId)) {
                return false;
            }
        }
        {
            String lhsCardinality;
            lhsCardinality = this.getCardinality();
            String rhsCardinality;
            rhsCardinality = that.getCardinality();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "cardinality", lhsCardinality), LocatorUtils.property(thatLocator, "cardinality", rhsCardinality), lhsCardinality, rhsCardinality)) {
                return false;
            }
        }
        {
            Integer lhsCardinalityMinOccurs;
            lhsCardinalityMinOccurs = this.getCardinalityMinOccurs();
            Integer rhsCardinalityMinOccurs;
            rhsCardinalityMinOccurs = that.getCardinalityMinOccurs();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "cardinalityMinOccurs", lhsCardinalityMinOccurs), LocatorUtils.property(thatLocator, "cardinalityMinOccurs", rhsCardinalityMinOccurs), lhsCardinalityMinOccurs, rhsCardinalityMinOccurs)) {
                return false;
            }
        }
        {
            Integer lhsCardinalityMaxOccurs;
            lhsCardinalityMaxOccurs = this.getCardinalityMaxOccurs();
            Integer rhsCardinalityMaxOccurs;
            rhsCardinalityMaxOccurs = that.getCardinalityMaxOccurs();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "cardinalityMaxOccurs", lhsCardinalityMaxOccurs), LocatorUtils.property(thatLocator, "cardinalityMaxOccurs", rhsCardinalityMaxOccurs), lhsCardinalityMaxOccurs, rhsCardinalityMaxOccurs)) {
                return false;
            }
        }
        {
            boolean lhsListField;
            lhsListField = this.isListField();
            boolean rhsListField;
            rhsListField = that.isListField();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "listField", lhsListField), LocatorUtils.property(thatLocator, "listField", rhsListField), lhsListField, rhsListField)) {
                return false;
            }
        }
        {
            boolean lhsOptionalField;
            lhsOptionalField = this.isOptionalField();
            boolean rhsOptionalField;
            rhsOptionalField = that.isOptionalField();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "optionalField", lhsOptionalField), LocatorUtils.property(thatLocator, "optionalField", rhsOptionalField), lhsOptionalField, rhsOptionalField)) {
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
            String theFieldTypeDefinitionId;
            theFieldTypeDefinitionId = this.getFieldTypeDefinitionId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "fieldTypeDefinitionId", theFieldTypeDefinitionId), currentHashCode, theFieldTypeDefinitionId);
        }
        {
            String theCardinality;
            theCardinality = this.getCardinality();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "cardinality", theCardinality), currentHashCode, theCardinality);
        }
        {
            Integer theCardinalityMinOccurs;
            theCardinalityMinOccurs = this.getCardinalityMinOccurs();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "cardinalityMinOccurs", theCardinalityMinOccurs), currentHashCode, theCardinalityMinOccurs);
        }
        {
            Integer theCardinalityMaxOccurs;
            theCardinalityMaxOccurs = this.getCardinalityMaxOccurs();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "cardinalityMaxOccurs", theCardinalityMaxOccurs), currentHashCode, theCardinalityMaxOccurs);
        }
        {
            boolean theListField;
            theListField = this.isListField();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "listField", theListField), currentHashCode, theListField);
        }
        {
            boolean theOptionalField;
            theOptionalField = this.isOptionalField();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "optionalField", theOptionalField), currentHashCode, theOptionalField);
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
