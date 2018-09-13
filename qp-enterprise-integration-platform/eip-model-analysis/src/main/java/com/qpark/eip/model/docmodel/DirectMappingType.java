//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.12 at 04:01:20 PM CEST 
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
 * This is the direct mapping type. It descends from DirectMappingType of name space is http://www.qpark.com/Interfaces/MappingTypes.
 * 
 * <p>Java class for DirectMappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectMappingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FieldMappingType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accessorFieldId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="accessor" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectMappingType", propOrder = {
    "accessorFieldId",
    "accessor"
})
@Entity(name = "DirectMappingType")
@Table(name = "DIRECTMAPPINGTYPE")
public class DirectMappingType
    extends FieldMappingType
    implements Serializable, Equals, HashCode
{

    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String accessorFieldId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String accessor;

    /**
     * Gets the value of the accessorFieldId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ACCESSORFIELDID", length = 36)
    public String getAccessorFieldId() {
        return accessorFieldId;
    }

    /**
     * Sets the value of the accessorFieldId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessorFieldId(String value) {
        this.accessorFieldId = value;
    }

    @Transient
    public boolean isSetAccessorFieldId() {
        return (this.accessorFieldId!= null);
    }

    /**
     * Gets the value of the accessor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ACCESSOR", length = 511)
    public String getAccessor() {
        return accessor;
    }

    /**
     * Sets the value of the accessor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessor(String value) {
        this.accessor = value;
    }

    @Transient
    public boolean isSetAccessor() {
        return (this.accessor!= null);
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
        final DirectMappingType that = ((DirectMappingType) object);
        {
            String lhsAccessorFieldId;
            lhsAccessorFieldId = this.getAccessorFieldId();
            String rhsAccessorFieldId;
            rhsAccessorFieldId = that.getAccessorFieldId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "accessorFieldId", lhsAccessorFieldId), LocatorUtils.property(thatLocator, "accessorFieldId", rhsAccessorFieldId), lhsAccessorFieldId, rhsAccessorFieldId)) {
                return false;
            }
        }
        {
            String lhsAccessor;
            lhsAccessor = this.getAccessor();
            String rhsAccessor;
            rhsAccessor = that.getAccessor();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "accessor", lhsAccessor), LocatorUtils.property(thatLocator, "accessor", rhsAccessor), lhsAccessor, rhsAccessor)) {
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
            String theAccessorFieldId;
            theAccessorFieldId = this.getAccessorFieldId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "accessorFieldId", theAccessorFieldId), currentHashCode, theAccessorFieldId);
        }
        {
            String theAccessor;
            theAccessor = this.getAccessor();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "accessor", theAccessor), currentHashCode, theAccessor);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
