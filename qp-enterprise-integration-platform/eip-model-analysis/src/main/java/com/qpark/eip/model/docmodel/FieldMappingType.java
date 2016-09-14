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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
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
 * .
 * 
 * <p>Java-Klasse für FieldMappingType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FieldMappingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DataType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="input" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FieldType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="returnValueTypeId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="mappingType" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldMappingType", propOrder = {
    "input",
    "returnValueTypeId",
    "mappingType"
})
@XmlSeeAlso({
    DirectMappingType.class,
    DefaultMappingType.class,
    ComplexMappingType.class,
    ComplexUUIDMappingType.class
})
@Entity(name = "FieldMappingType")
@Table(name = "FIELDMAPPINGTYPE")
public abstract class FieldMappingType
    extends DataType
    implements Serializable, Equals, HashCode
{

    protected List<FieldType> input;
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String returnValueTypeId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String mappingType;

    /**
     * Gets the value of the input property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the input property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInput().add(newItem);
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
    @JoinColumn(name = "INPUT__FIELDMAPPINGTYPE_HJID")
    public List<FieldType> getInput() {
        if (input == null) {
            input = new ArrayList<FieldType>();
        }
        return this.input;
    }

    /**
     * 
     * 
     */
    public void setInput(List<FieldType> input) {
        this.input = input;
    }

    @Transient
    public boolean isSetInput() {
        return ((this.input!= null)&&(!this.input.isEmpty()));
    }

    public void unsetInput() {
        this.input = null;
    }

    /**
     * Ruft den Wert der returnValueTypeId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "RETURNVALUETYPEID", length = 36)
    public String getReturnValueTypeId() {
        return returnValueTypeId;
    }

    /**
     * Legt den Wert der returnValueTypeId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnValueTypeId(String value) {
        this.returnValueTypeId = value;
    }

    @Transient
    public boolean isSetReturnValueTypeId() {
        return (this.returnValueTypeId!= null);
    }

    /**
     * Ruft den Wert der mappingType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "MAPPINGTYPE", length = 255)
    public String getMappingType() {
        return mappingType;
    }

    /**
     * Legt den Wert der mappingType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMappingType(String value) {
        this.mappingType = value;
    }

    @Transient
    public boolean isSetMappingType() {
        return (this.mappingType!= null);
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
        final FieldMappingType that = ((FieldMappingType) object);
        {
            List<FieldType> lhsInput;
            lhsInput = (this.isSetInput()?this.getInput():null);
            List<FieldType> rhsInput;
            rhsInput = (that.isSetInput()?that.getInput():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "input", lhsInput), LocatorUtils.property(thatLocator, "input", rhsInput), lhsInput, rhsInput)) {
                return false;
            }
        }
        {
            String lhsReturnValueTypeId;
            lhsReturnValueTypeId = this.getReturnValueTypeId();
            String rhsReturnValueTypeId;
            rhsReturnValueTypeId = that.getReturnValueTypeId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "returnValueTypeId", lhsReturnValueTypeId), LocatorUtils.property(thatLocator, "returnValueTypeId", rhsReturnValueTypeId), lhsReturnValueTypeId, rhsReturnValueTypeId)) {
                return false;
            }
        }
        {
            String lhsMappingType;
            lhsMappingType = this.getMappingType();
            String rhsMappingType;
            rhsMappingType = that.getMappingType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "mappingType", lhsMappingType), LocatorUtils.property(thatLocator, "mappingType", rhsMappingType), lhsMappingType, rhsMappingType)) {
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
            List<FieldType> theInput;
            theInput = (this.isSetInput()?this.getInput():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "input", theInput), currentHashCode, theInput);
        }
        {
            String theReturnValueTypeId;
            theReturnValueTypeId = this.getReturnValueTypeId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "returnValueTypeId", theReturnValueTypeId), currentHashCode, theReturnValueTypeId);
        }
        {
            String theMappingType;
            theMappingType = this.getMappingType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "mappingType", theMappingType), currentHashCode, theMappingType);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
