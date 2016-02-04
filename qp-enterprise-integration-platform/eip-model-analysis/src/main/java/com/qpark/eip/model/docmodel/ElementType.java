//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2016.01.28 um 05:38:13 AM CET
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
 * This is element defined in the cluster.
 * <p>
 * Java-Klasse für ElementType complex type.
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ElementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="modelVersion" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="parentId" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="namespace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="complexTypeId" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElementType", propOrder = { "id", "modelVersion", "parentId",
		"name", "namespace", "complexTypeId", "description" })
@Entity(name = "ElementType")
@Table(name = "ELEMENTTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ElementType implements Serializable, Equals, HashCode {

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
	protected String name;
	protected String namespace;
	@XmlElement(required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	@XmlSchemaType(name = "normalizedString")
	protected String complexTypeId;
	@XmlElement(required = true)
	protected String description;
	@XmlTransient
	protected Long hjid;

	/**
	 * Ruft den Wert der id-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "ID", length = 36)
	public String getId() {
		return this.id;
	}

	/**
	 * Legt den Wert der id-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setId(final String value) {
		this.id = value;
	}

	@Transient
	public boolean isSetId() {
		return (this.id != null);
	}

	/**
	 * Ruft den Wert der modelVersion-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "MODELVERSION", length = 64)
	public String getModelVersion() {
		return this.modelVersion;
	}

	/**
	 * Legt den Wert der modelVersion-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setModelVersion(final String value) {
		this.modelVersion = value;
	}

	@Transient
	public boolean isSetModelVersion() {
		return (this.modelVersion != null);
	}

	/**
	 * Ruft den Wert der parentId-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "PARENTID", length = 36)
	public String getParentId() {
		return this.parentId;
	}

	/**
	 * Legt den Wert der parentId-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setParentId(final String value) {
		this.parentId = value;
	}

	@Transient
	public boolean isSetParentId() {
		return (this.parentId != null);
	}

	/**
	 * Ruft den Wert der name-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "NAME_", length = 512)
	public String getName() {
		return this.name;
	}

	/**
	 * Legt den Wert der name-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setName(final String value) {
		this.name = value;
	}

	@Transient
	public boolean isSetName() {
		return (this.name != null);
	}

	/**
	 * Ruft den Wert der namespace-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "NAMESPACE", length = 512)
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Legt den Wert der namespace-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setNamespace(final String value) {
		this.namespace = value;
	}

	@Transient
	public boolean isSetNamespace() {
		return (this.namespace != null);
	}

	/**
	 * Ruft den Wert der complexTypeId-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "COMPLEXTYPEID", length = 255)
	public String getComplexTypeId() {
		return this.complexTypeId;
	}

	/**
	 * Legt den Wert der complexTypeId-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setComplexTypeId(final String value) {
		this.complexTypeId = value;
	}

	@Transient
	public boolean isSetComplexTypeId() {
		return (this.complexTypeId != null);
	}

	/**
	 * Ruft den Wert der description-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "DESCRIPTION", length = 2048)
	public String getDescription() {
		return this.description;
	}

	/**
	 * Legt den Wert der description-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setDescription(final String value) {
		this.description = value;
	}

	@Transient
	public boolean isSetDescription() {
		return (this.description != null);
	}

	/**
	 * @return possible object is {@link Long }
	 */
	@Id
	@Column(name = "HJID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getHjid() {
		return this.hjid;
	}

	/**
	 * @param value
	 *            allowed object is {@link Long }
	 */
	public void setHjid(final Long value) {
		this.hjid = value;
	}

	@Override
	public boolean equals(final ObjectLocator thisLocator,
			final ObjectLocator thatLocator, final Object object,
			final EqualsStrategy strategy) {
		if ((object == null) || (this.getClass() != object.getClass())) {
			return false;
		}
		if (this == object) {
			return true;
		}
		final ElementType that = ((ElementType) object);
		{
			String lhsId;
			lhsId = this.getId();
			String rhsId;
			rhsId = that.getId();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "id", lhsId),
					LocatorUtils.property(thatLocator, "id", rhsId), lhsId,
					rhsId)) {
				return false;
			}
		}
		{
			String lhsModelVersion;
			lhsModelVersion = this.getModelVersion();
			String rhsModelVersion;
			rhsModelVersion = that.getModelVersion();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "modelVersion",
							lhsModelVersion),
					LocatorUtils.property(thatLocator, "modelVersion",
							rhsModelVersion),
					lhsModelVersion, rhsModelVersion)) {
				return false;
			}
		}
		{
			String lhsParentId;
			lhsParentId = this.getParentId();
			String rhsParentId;
			rhsParentId = that.getParentId();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "parentId", lhsParentId),
					LocatorUtils.property(thatLocator, "parentId", rhsParentId),
					lhsParentId, rhsParentId)) {
				return false;
			}
		}
		{
			String lhsName;
			lhsName = this.getName();
			String rhsName;
			rhsName = that.getName();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "name", lhsName),
					LocatorUtils.property(thatLocator, "name", rhsName),
					lhsName, rhsName)) {
				return false;
			}
		}
		{
			String lhsNamespace;
			lhsNamespace = this.getNamespace();
			String rhsNamespace;
			rhsNamespace = that.getNamespace();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "namespace",
							lhsNamespace),
					LocatorUtils.property(thatLocator, "namespace",
							rhsNamespace),
					lhsNamespace, rhsNamespace)) {
				return false;
			}
		}
		{
			String lhsComplexTypeId;
			lhsComplexTypeId = this.getComplexTypeId();
			String rhsComplexTypeId;
			rhsComplexTypeId = that.getComplexTypeId();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "complexTypeId",
							lhsComplexTypeId),
					LocatorUtils.property(thatLocator, "complexTypeId",
							rhsComplexTypeId),
					lhsComplexTypeId, rhsComplexTypeId)) {
				return false;
			}
		}
		{
			String lhsDescription;
			lhsDescription = this.getDescription();
			String rhsDescription;
			rhsDescription = that.getDescription();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "description",
							lhsDescription),
					LocatorUtils.property(thatLocator, "description",
							rhsDescription),
					lhsDescription, rhsDescription)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(final Object object) {
		final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
		return this.equals(null, null, object, strategy);
	}

	@Override
	public int hashCode(final ObjectLocator locator,
			final HashCodeStrategy strategy) {
		int currentHashCode = 1;
		{
			String theId;
			theId = this.getId();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "id", theId),
					currentHashCode, theId);
		}
		{
			String theModelVersion;
			theModelVersion = this.getModelVersion();
			currentHashCode = strategy
					.hashCode(
							LocatorUtils.property(locator, "modelVersion",
									theModelVersion),
							currentHashCode, theModelVersion);
		}
		{
			String theParentId;
			theParentId = this.getParentId();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "parentId", theParentId),
					currentHashCode, theParentId);
		}
		{
			String theName;
			theName = this.getName();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "name", theName),
					currentHashCode, theName);
		}
		{
			String theNamespace;
			theNamespace = this.getNamespace();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "namespace", theNamespace),
					currentHashCode, theNamespace);
		}
		{
			String theComplexTypeId;
			theComplexTypeId = this.getComplexTypeId();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "complexTypeId",
							theComplexTypeId),
					currentHashCode, theComplexTypeId);
		}
		{
			String theDescription;
			theDescription = this.getDescription();
			currentHashCode = strategy
					.hashCode(
							LocatorUtils.property(locator, "description",
									theDescription),
							currentHashCode, theDescription);
		}
		return currentHashCode;
	}

	@Override
	public int hashCode() {
		final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
		return this.hashCode(null, strategy);
	}

}
