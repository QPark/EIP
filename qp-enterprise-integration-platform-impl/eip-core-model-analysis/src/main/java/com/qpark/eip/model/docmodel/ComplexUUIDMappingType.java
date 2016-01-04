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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 * This is the complex mapping type. It descends from ComplexUUIDMappingType of name space is http://www.qpark.com/Interfaces/MappingTypes.
 * 
 * <p>Java-Klasse für ComplexUUIDMappingType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ComplexUUIDMappingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ses.com/Utility/DocumentationModel}FieldMappingType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="categoryNameOrUUID" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexUUIDMappingType", propOrder = {
    "categoryNameOrUUID"
})
@Entity(name = "ComplexUUIDMappingType")
@Table(name = "COMPLEXUUIDMAPPINGTYPE")
public class ComplexUUIDMappingType
    extends FieldMappingType
    implements Serializable, Equals, HashCode
{

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String categoryNameOrUUID;

    /**
     * Ruft den Wert der categoryNameOrUUID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "CATEGORYNAMEORUUID", length = 255)
    public String getCategoryNameOrUUID() {
        return categoryNameOrUUID;
    }

    /**
     * Legt den Wert der categoryNameOrUUID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryNameOrUUID(String value) {
        this.categoryNameOrUUID = value;
    }

    @Transient
    public boolean isSetCategoryNameOrUUID() {
        return (this.categoryNameOrUUID!= null);
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if ((object == null)||(this.getClass()!= object.getClass())) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final ComplexUUIDMappingType that = ((ComplexUUIDMappingType) object);
        {
            String lhsCategoryNameOrUUID;
            lhsCategoryNameOrUUID = this.getCategoryNameOrUUID();
            String rhsCategoryNameOrUUID;
            rhsCategoryNameOrUUID = that.getCategoryNameOrUUID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "categoryNameOrUUID", lhsCategoryNameOrUUID), LocatorUtils.property(thatLocator, "categoryNameOrUUID", rhsCategoryNameOrUUID), lhsCategoryNameOrUUID, rhsCategoryNameOrUUID)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            String theCategoryNameOrUUID;
            theCategoryNameOrUUID = this.getCategoryNameOrUUID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "categoryNameOrUUID", theCategoryNameOrUUID), currentHashCode, theCategoryNameOrUUID);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
