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
 * <p>Java-Klasse für EnterpriseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="EnterpriseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}nameType"/&gt;
 *         &lt;element name="modelVersion" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}modelVersionType"/&gt;
 *         &lt;element name="domains" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DomainType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="flows" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FlowType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="basicDataTypes" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}DataType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnterpriseType", propOrder = {
    "name",
    "modelVersion",
    "domains",
    "flows",
    "basicDataTypes"
})
@Entity(name = "EnterpriseType")
@Table(name = "ENTERPRISETYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class EnterpriseType
    implements Serializable, Equals, HashCode
{

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelVersion;
    protected List<DomainType> domains;
    protected List<FlowType> flows;
    protected List<DataType> basicDataTypes;
    @XmlTransient
    protected Long hjid;

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
     * Gets the value of the domains property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domains property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomains().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomainType }
     * 
     * 
     */
    @OneToMany(targetEntity = DomainType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "DOMAINS_ENTERPRISETYPE_HJID")
    public List<DomainType> getDomains() {
        if (domains == null) {
            domains = new ArrayList<DomainType>();
        }
        return this.domains;
    }

    /**
     * 
     * 
     */
    public void setDomains(List<DomainType> domains) {
        this.domains = domains;
    }

    @Transient
    public boolean isSetDomains() {
        return ((this.domains!= null)&&(!this.domains.isEmpty()));
    }

    public void unsetDomains() {
        this.domains = null;
    }

    /**
     * Gets the value of the flows property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flows property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlows().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlowType }
     * 
     * 
     */
    @OneToMany(targetEntity = FlowType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "FLOWS_ENTERPRISETYPE_HJID")
    public List<FlowType> getFlows() {
        if (flows == null) {
            flows = new ArrayList<FlowType>();
        }
        return this.flows;
    }

    /**
     * 
     * 
     */
    public void setFlows(List<FlowType> flows) {
        this.flows = flows;
    }

    @Transient
    public boolean isSetFlows() {
        return ((this.flows!= null)&&(!this.flows.isEmpty()));
    }

    public void unsetFlows() {
        this.flows = null;
    }

    /**
     * Gets the value of the basicDataTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the basicDataTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBasicDataTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataType }
     * 
     * 
     */
    @OneToMany(targetEntity = DataType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "BASICDATATYPES_ENTERPRISETYP_0")
    public List<DataType> getBasicDataTypes() {
        if (basicDataTypes == null) {
            basicDataTypes = new ArrayList<DataType>();
        }
        return this.basicDataTypes;
    }

    /**
     * 
     * 
     */
    public void setBasicDataTypes(List<DataType> basicDataTypes) {
        this.basicDataTypes = basicDataTypes;
    }

    @Transient
    public boolean isSetBasicDataTypes() {
        return ((this.basicDataTypes!= null)&&(!this.basicDataTypes.isEmpty()));
    }

    public void unsetBasicDataTypes() {
        this.basicDataTypes = null;
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
        final EnterpriseType that = ((EnterpriseType) object);
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
            String lhsModelVersion;
            lhsModelVersion = this.getModelVersion();
            String rhsModelVersion;
            rhsModelVersion = that.getModelVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "modelVersion", lhsModelVersion), LocatorUtils.property(thatLocator, "modelVersion", rhsModelVersion), lhsModelVersion, rhsModelVersion)) {
                return false;
            }
        }
        {
            List<DomainType> lhsDomains;
            lhsDomains = (this.isSetDomains()?this.getDomains():null);
            List<DomainType> rhsDomains;
            rhsDomains = (that.isSetDomains()?that.getDomains():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "domains", lhsDomains), LocatorUtils.property(thatLocator, "domains", rhsDomains), lhsDomains, rhsDomains)) {
                return false;
            }
        }
        {
            List<FlowType> lhsFlows;
            lhsFlows = (this.isSetFlows()?this.getFlows():null);
            List<FlowType> rhsFlows;
            rhsFlows = (that.isSetFlows()?that.getFlows():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "flows", lhsFlows), LocatorUtils.property(thatLocator, "flows", rhsFlows), lhsFlows, rhsFlows)) {
                return false;
            }
        }
        {
            List<DataType> lhsBasicDataTypes;
            lhsBasicDataTypes = (this.isSetBasicDataTypes()?this.getBasicDataTypes():null);
            List<DataType> rhsBasicDataTypes;
            rhsBasicDataTypes = (that.isSetBasicDataTypes()?that.getBasicDataTypes():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "basicDataTypes", lhsBasicDataTypes), LocatorUtils.property(thatLocator, "basicDataTypes", rhsBasicDataTypes), lhsBasicDataTypes, rhsBasicDataTypes)) {
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
            String theName;
            theName = this.getName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name", theName), currentHashCode, theName);
        }
        {
            String theModelVersion;
            theModelVersion = this.getModelVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "modelVersion", theModelVersion), currentHashCode, theModelVersion);
        }
        {
            List<DomainType> theDomains;
            theDomains = (this.isSetDomains()?this.getDomains():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "domains", theDomains), currentHashCode, theDomains);
        }
        {
            List<FlowType> theFlows;
            theFlows = (this.isSetFlows()?this.getFlows():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "flows", theFlows), currentHashCode, theFlows);
        }
        {
            List<DataType> theBasicDataTypes;
            theBasicDataTypes = (this.isSetBasicDataTypes()?this.getBasicDataTypes():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "basicDataTypes", theBasicDataTypes), currentHashCode, theBasicDataTypes);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}