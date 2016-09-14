//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.09.14 um 09:30:20 AM CEST 
//


package com.qpark.eip.model.docmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * This is the interface mapping type using the MappingTypeTypes. It descends from InterfaceType of name space is http://www.qpark.com/Interfaces/MappingTypes.
 * 
 * <p>Java-Klasse für InterfaceMappingType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="InterfaceMappingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DataType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fieldMappings" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FieldType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "fieldMappings"
})
@Entity(name = "InterfaceMappingType")
@Table(name = "INTERFACEMAPPINGTYPE")
public class InterfaceMappingType
    extends DataType
    implements Serializable, Equals, HashCode
{

    protected List<FieldType> fieldMappings;

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
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
