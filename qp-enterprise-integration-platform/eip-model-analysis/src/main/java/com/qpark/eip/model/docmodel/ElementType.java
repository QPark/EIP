//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.09.13 um 08:58:01 PM CEST 
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
 * This is element defined in the cluster.
 * 
 * <p>Java-Klasse für ElementType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ElementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DataType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="complexTypeId" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}UUIDType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElementType", propOrder = {
    "complexTypeId"
})
@Entity(name = "ElementType")
@Table(name = "ELEMENTTYPE")
public class ElementType
    extends DataType
    implements Serializable, Equals, HashCode
{

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String complexTypeId;

    /**
     * Ruft den Wert der complexTypeId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "COMPLEXTYPEID", length = 36)
    public String getComplexTypeId() {
        return complexTypeId;
    }

    /**
     * Legt den Wert der complexTypeId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplexTypeId(String value) {
        this.complexTypeId = value;
    }

    @Transient
    public boolean isSetComplexTypeId() {
        return (this.complexTypeId!= null);
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
        final ElementType that = ((ElementType) object);
        {
            String lhsComplexTypeId;
            lhsComplexTypeId = this.getComplexTypeId();
            String rhsComplexTypeId;
            rhsComplexTypeId = that.getComplexTypeId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "complexTypeId", lhsComplexTypeId), LocatorUtils.property(thatLocator, "complexTypeId", rhsComplexTypeId), lhsComplexTypeId, rhsComplexTypeId)) {
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
            String theComplexTypeId;
            theComplexTypeId = this.getComplexTypeId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "complexTypeId", theComplexTypeId), currentHashCode, theComplexTypeId);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
