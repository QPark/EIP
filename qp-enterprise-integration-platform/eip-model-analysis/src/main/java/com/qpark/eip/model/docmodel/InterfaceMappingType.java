//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.12 at 04:01:20 PM CEST 
//


package com.qpark.eip.model.docmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * This is the interface mapping type using the MappingTypeTypes. It descends from InterfaceType of name space is http://www.qpark.com/Interfaces/MappingTypes.
 * 
 * <p>Java class for InterfaceMappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InterfaceMappingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DataType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fieldMappings" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FieldType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="fieldMappingInputType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterfaceMappingType", propOrder = {
    "fieldMappings",
    "fieldMappingInputType"
})
@Entity(name = "InterfaceMappingType")
@Table(name = "INTERFACEMAPPINGTYPE")
public class InterfaceMappingType
    extends DataType
    implements Serializable, Equals, HashCode
{

    protected List<FieldType> fieldMappings;
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected List<String> fieldMappingInputType;

    /**
     * Gets the value of the fieldMappings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldMappings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldMappings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldType }
     * 
     * 
     */
    @OneToMany(targetEntity = FieldType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "FIELDMAPPINGS_INTERFACEMAPPI_0")
    public List<FieldType> getFieldMappings() {
        if (fieldMappings == null) {
            fieldMappings = new ArrayList<FieldType>();
        }
        return this.fieldMappings;
    }

    /**
     * 
     * 
     */
    public void setFieldMappings(List<FieldType> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    @Transient
    public boolean isSetFieldMappings() {
        return ((this.fieldMappings!= null)&&(!this.fieldMappings.isEmpty()));
    }

    public void unsetFieldMappings() {
        this.fieldMappings = null;
    }

    /**
     * Gets the value of the fieldMappingInputType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldMappingInputType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldMappingInputType().add(newItem);
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
    @Column(name = "HJVALUE", length = 36)
    @CollectionTable(name = "INTERFACEMAPPINGTYPE_FIELDMA_0", joinColumns = {
        @JoinColumn(name = "HJID")
    })
    public List<String> getFieldMappingInputType() {
        if (fieldMappingInputType == null) {
            fieldMappingInputType = new ArrayList<String>();
        }
        return this.fieldMappingInputType;
    }

    /**
     * 
     * 
     */
    public void setFieldMappingInputType(List<String> fieldMappingInputType) {
        this.fieldMappingInputType = fieldMappingInputType;
    }

    @Transient
    public boolean isSetFieldMappingInputType() {
        return ((this.fieldMappingInputType!= null)&&(!this.fieldMappingInputType.isEmpty()));
    }

    public void unsetFieldMappingInputType() {
        this.fieldMappingInputType = null;
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
        final InterfaceMappingType that = ((InterfaceMappingType) object);
        {
            List<FieldType> lhsFieldMappings;
            lhsFieldMappings = (this.isSetFieldMappings()?this.getFieldMappings():null);
            List<FieldType> rhsFieldMappings;
            rhsFieldMappings = (that.isSetFieldMappings()?that.getFieldMappings():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "fieldMappings", lhsFieldMappings), LocatorUtils.property(thatLocator, "fieldMappings", rhsFieldMappings), lhsFieldMappings, rhsFieldMappings)) {
                return false;
            }
        }
        {
            List<String> lhsFieldMappingInputType;
            lhsFieldMappingInputType = (this.isSetFieldMappingInputType()?this.getFieldMappingInputType():null);
            List<String> rhsFieldMappingInputType;
            rhsFieldMappingInputType = (that.isSetFieldMappingInputType()?that.getFieldMappingInputType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "fieldMappingInputType", lhsFieldMappingInputType), LocatorUtils.property(thatLocator, "fieldMappingInputType", rhsFieldMappingInputType), lhsFieldMappingInputType, rhsFieldMappingInputType)) {
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
            List<FieldType> theFieldMappings;
            theFieldMappings = (this.isSetFieldMappings()?this.getFieldMappings():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "fieldMappings", theFieldMappings), currentHashCode, theFieldMappings);
        }
        {
            List<String> theFieldMappingInputType;
            theFieldMappingInputType = (this.isSetFieldMappingInputType()?this.getFieldMappingInputType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "fieldMappingInputType", theFieldMappingInputType), currentHashCode, theFieldMappingInputType);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
