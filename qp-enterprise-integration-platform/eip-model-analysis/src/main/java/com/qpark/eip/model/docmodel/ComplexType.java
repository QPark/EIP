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
 * This is the complexType.
 * 
 * <p>Java-Klasse für ComplexType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ComplexType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DataType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="descendedFromId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="field" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FieldType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="isFlowInputType" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="isFlowOutputType" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="isMappingRequestType" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="isMappingResponseType" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexType", propOrder = {
    "descendedFromId",
    "field",
    "isFlowInputType",
    "isFlowOutputType",
    "isMappingRequestType",
    "isMappingResponseType"
})
@Entity(name = "ComplexType")
@Table(name = "COMPLEXTYPE")
public class ComplexType
    extends DataType
    implements Serializable, Equals, HashCode
{

    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String descendedFromId;
    protected List<FieldType> field;
    protected boolean isFlowInputType;
    protected boolean isFlowOutputType;
    protected boolean isMappingRequestType;
    protected boolean isMappingResponseType;

    /**
     * Ruft den Wert der descendedFromId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "DESCENDEDFROMID", length = 36)
    public String getDescendedFromId() {
        return descendedFromId;
    }

    /**
     * Legt den Wert der descendedFromId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescendedFromId(String value) {
        this.descendedFromId = value;
    }

    @Transient
    public boolean isSetDescendedFromId() {
        return (this.descendedFromId!= null);
    }

    /**
     * Gets the value of the field property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getField().add(newItem);
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
    @JoinColumn(name = "FIELD_COMPLEXTYPE_HJID")
    public List<FieldType> getField() {
        if (field == null) {
            field = new ArrayList<FieldType>();
        }
        return this.field;
    }

    /**
     * 
     * 
     */
    public void setField(List<FieldType> field) {
        this.field = field;
    }

    @Transient
    public boolean isSetField() {
        return ((this.field!= null)&&(!this.field.isEmpty()));
    }

    public void unsetField() {
        this.field = null;
    }

    /**
     * Ruft den Wert der isFlowInputType-Eigenschaft ab.
     * 
     */
    @Basic
    @Column(name = "ISFLOWINPUTTYPE")
    public boolean isIsFlowInputType() {
        return isFlowInputType;
    }

    /**
     * Legt den Wert der isFlowInputType-Eigenschaft fest.
     * 
     */
    public void setIsFlowInputType(boolean value) {
        this.isFlowInputType = value;
    }

    @Transient
    public boolean isSetIsFlowInputType() {
        return true;
    }

    /**
     * Ruft den Wert der isFlowOutputType-Eigenschaft ab.
     * 
     */
    @Basic
    @Column(name = "ISFLOWOUTPUTTYPE")
    public boolean isIsFlowOutputType() {
        return isFlowOutputType;
    }

    /**
     * Legt den Wert der isFlowOutputType-Eigenschaft fest.
     * 
     */
    public void setIsFlowOutputType(boolean value) {
        this.isFlowOutputType = value;
    }

    @Transient
    public boolean isSetIsFlowOutputType() {
        return true;
    }

    /**
     * Ruft den Wert der isMappingRequestType-Eigenschaft ab.
     * 
     */
    @Basic
    @Column(name = "ISMAPPINGREQUESTTYPE")
    public boolean isIsMappingRequestType() {
        return isMappingRequestType;
    }

    /**
     * Legt den Wert der isMappingRequestType-Eigenschaft fest.
     * 
     */
    public void setIsMappingRequestType(boolean value) {
        this.isMappingRequestType = value;
    }

    @Transient
    public boolean isSetIsMappingRequestType() {
        return true;
    }

    /**
     * Ruft den Wert der isMappingResponseType-Eigenschaft ab.
     * 
     */
    @Basic
    @Column(name = "ISMAPPINGRESPONSETYPE")
    public boolean isIsMappingResponseType() {
        return isMappingResponseType;
    }

    /**
     * Legt den Wert der isMappingResponseType-Eigenschaft fest.
     * 
     */
    public void setIsMappingResponseType(boolean value) {
        this.isMappingResponseType = value;
    }

    @Transient
    public boolean isSetIsMappingResponseType() {
        return true;
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
        final ComplexType that = ((ComplexType) object);
        {
            String lhsDescendedFromId;
            lhsDescendedFromId = this.getDescendedFromId();
            String rhsDescendedFromId;
            rhsDescendedFromId = that.getDescendedFromId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "descendedFromId", lhsDescendedFromId), LocatorUtils.property(thatLocator, "descendedFromId", rhsDescendedFromId), lhsDescendedFromId, rhsDescendedFromId)) {
                return false;
            }
        }
        {
            List<FieldType> lhsField;
            lhsField = (this.isSetField()?this.getField():null);
            List<FieldType> rhsField;
            rhsField = (that.isSetField()?that.getField():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "field", lhsField), LocatorUtils.property(thatLocator, "field", rhsField), lhsField, rhsField)) {
                return false;
            }
        }
        {
            boolean lhsIsFlowInputType;
            lhsIsFlowInputType = this.isIsFlowInputType();
            boolean rhsIsFlowInputType;
            rhsIsFlowInputType = that.isIsFlowInputType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "isFlowInputType", lhsIsFlowInputType), LocatorUtils.property(thatLocator, "isFlowInputType", rhsIsFlowInputType), lhsIsFlowInputType, rhsIsFlowInputType)) {
                return false;
            }
        }
        {
            boolean lhsIsFlowOutputType;
            lhsIsFlowOutputType = this.isIsFlowOutputType();
            boolean rhsIsFlowOutputType;
            rhsIsFlowOutputType = that.isIsFlowOutputType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "isFlowOutputType", lhsIsFlowOutputType), LocatorUtils.property(thatLocator, "isFlowOutputType", rhsIsFlowOutputType), lhsIsFlowOutputType, rhsIsFlowOutputType)) {
                return false;
            }
        }
        {
            boolean lhsIsMappingRequestType;
            lhsIsMappingRequestType = this.isIsMappingRequestType();
            boolean rhsIsMappingRequestType;
            rhsIsMappingRequestType = that.isIsMappingRequestType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "isMappingRequestType", lhsIsMappingRequestType), LocatorUtils.property(thatLocator, "isMappingRequestType", rhsIsMappingRequestType), lhsIsMappingRequestType, rhsIsMappingRequestType)) {
                return false;
            }
        }
        {
            boolean lhsIsMappingResponseType;
            lhsIsMappingResponseType = this.isIsMappingResponseType();
            boolean rhsIsMappingResponseType;
            rhsIsMappingResponseType = that.isIsMappingResponseType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "isMappingResponseType", lhsIsMappingResponseType), LocatorUtils.property(thatLocator, "isMappingResponseType", rhsIsMappingResponseType), lhsIsMappingResponseType, rhsIsMappingResponseType)) {
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
            String theDescendedFromId;
            theDescendedFromId = this.getDescendedFromId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "descendedFromId", theDescendedFromId), currentHashCode, theDescendedFromId);
        }
        {
            List<FieldType> theField;
            theField = (this.isSetField()?this.getField():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "field", theField), currentHashCode, theField);
        }
        {
            boolean theIsFlowInputType;
            theIsFlowInputType = this.isIsFlowInputType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isFlowInputType", theIsFlowInputType), currentHashCode, theIsFlowInputType);
        }
        {
            boolean theIsFlowOutputType;
            theIsFlowOutputType = this.isIsFlowOutputType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isFlowOutputType", theIsFlowOutputType), currentHashCode, theIsFlowOutputType);
        }
        {
            boolean theIsMappingRequestType;
            theIsMappingRequestType = this.isIsMappingRequestType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isMappingRequestType", theIsMappingRequestType), currentHashCode, theIsMappingRequestType);
        }
        {
            boolean theIsMappingResponseType;
            theIsMappingResponseType = this.isIsMappingResponseType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isMappingResponseType", theIsMappingResponseType), currentHashCode, theIsMappingResponseType);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
