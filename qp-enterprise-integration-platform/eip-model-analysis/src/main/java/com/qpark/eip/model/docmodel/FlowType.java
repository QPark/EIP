//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2016.01.28 um 05:38:13 AM CET
//

package com.qpark.eip.model.docmodel;

import java.io.Serializable;

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
import javax.persistence.ManyToOne;
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
 * This is the flow definition.
 * <p>
 * Java-Klasse für FlowType complex type.
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="FlowType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="modelVersion" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="parentId" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="namespace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clusterId" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="invokeFlowDefinition" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}RequestResponseDataType"/&gt;
 *         &lt;element name="executeRequest" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FlowProcessType"/&gt;
 *         &lt;element name="processResponse" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}FlowProcessType"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlowType",
		propOrder = { "id", "modelVersion", "parentId", "name", "namespace",
				"clusterId", "invokeFlowDefinition", "executeRequest",
				"processResponse", "description" })
@Entity(name = "FlowType")
@Table(name = "FLOWTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class FlowType implements Serializable, Equals, HashCode {

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
	protected String clusterId;
	@XmlElement(required = true)
	protected RequestResponseDataType invokeFlowDefinition;
	@XmlElement(required = true)
	protected FlowProcessType executeRequest;
	@XmlElement(required = true)
	protected FlowProcessType processResponse;
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
	 * Ruft den Wert der clusterId-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 */
	@Basic
	@Column(name = "CLUSTERID", length = 36)
	public String getClusterId() {
		return this.clusterId;
	}

	/**
	 * Legt den Wert der clusterId-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setClusterId(final String value) {
		this.clusterId = value;
	}

	@Transient
	public boolean isSetClusterId() {
		return (this.clusterId != null);
	}

	/**
	 * Ruft den Wert der invokeFlowDefinition-Eigenschaft ab.
	 * 
	 * @return possible object is {@link RequestResponseDataType }
	 */
	@ManyToOne(targetEntity = RequestResponseDataType.class,
			cascade = { CascadeType.ALL })
	@JoinColumn(name = "INVOKEFLOWDEFINITION_FLOWTYP_0")
	public RequestResponseDataType getInvokeFlowDefinition() {
		return this.invokeFlowDefinition;
	}

	/**
	 * Legt den Wert der invokeFlowDefinition-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link RequestResponseDataType }
	 */
	public void setInvokeFlowDefinition(final RequestResponseDataType value) {
		this.invokeFlowDefinition = value;
	}

	@Transient
	public boolean isSetInvokeFlowDefinition() {
		return (this.invokeFlowDefinition != null);
	}

	/**
	 * Ruft den Wert der executeRequest-Eigenschaft ab.
	 * 
	 * @return possible object is {@link FlowProcessType }
	 */
	@ManyToOne(targetEntity = FlowProcessType.class,
			cascade = { CascadeType.ALL })
	@JoinColumn(name = "EXECUTEREQUEST_FLOWTYPE_HJID")
	public FlowProcessType getExecuteRequest() {
		return this.executeRequest;
	}

	/**
	 * Legt den Wert der executeRequest-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link FlowProcessType }
	 */
	public void setExecuteRequest(final FlowProcessType value) {
		this.executeRequest = value;
	}

	@Transient
	public boolean isSetExecuteRequest() {
		return (this.executeRequest != null);
	}

	/**
	 * Ruft den Wert der processResponse-Eigenschaft ab.
	 * 
	 * @return possible object is {@link FlowProcessType }
	 */
	@ManyToOne(targetEntity = FlowProcessType.class,
			cascade = { CascadeType.ALL })
	@JoinColumn(name = "PROCESSRESPONSE_FLOWTYPE_HJID")
	public FlowProcessType getProcessResponse() {
		return this.processResponse;
	}

	/**
	 * Legt den Wert der processResponse-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link FlowProcessType }
	 */
	public void setProcessResponse(final FlowProcessType value) {
		this.processResponse = value;
	}

	@Transient
	public boolean isSetProcessResponse() {
		return (this.processResponse != null);
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
		final FlowType that = ((FlowType) object);
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
			String lhsClusterId;
			lhsClusterId = this.getClusterId();
			String rhsClusterId;
			rhsClusterId = that.getClusterId();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "clusterId",
							lhsClusterId),
					LocatorUtils.property(thatLocator, "clusterId",
							rhsClusterId),
					lhsClusterId, rhsClusterId)) {
				return false;
			}
		}
		{
			RequestResponseDataType lhsInvokeFlowDefinition;
			lhsInvokeFlowDefinition = this.getInvokeFlowDefinition();
			RequestResponseDataType rhsInvokeFlowDefinition;
			rhsInvokeFlowDefinition = that.getInvokeFlowDefinition();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "invokeFlowDefinition",
							lhsInvokeFlowDefinition),
					LocatorUtils.property(thatLocator, "invokeFlowDefinition",
							rhsInvokeFlowDefinition),
					lhsInvokeFlowDefinition, rhsInvokeFlowDefinition)) {
				return false;
			}
		}
		{
			FlowProcessType lhsExecuteRequest;
			lhsExecuteRequest = this.getExecuteRequest();
			FlowProcessType rhsExecuteRequest;
			rhsExecuteRequest = that.getExecuteRequest();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "executeRequest",
							lhsExecuteRequest),
					LocatorUtils.property(thatLocator, "executeRequest",
							rhsExecuteRequest),
					lhsExecuteRequest, rhsExecuteRequest)) {
				return false;
			}
		}
		{
			FlowProcessType lhsProcessResponse;
			lhsProcessResponse = this.getProcessResponse();
			FlowProcessType rhsProcessResponse;
			rhsProcessResponse = that.getProcessResponse();
			if (!strategy.equals(
					LocatorUtils.property(thisLocator, "processResponse",
							lhsProcessResponse),
					LocatorUtils.property(thatLocator, "processResponse",
							rhsProcessResponse),
					lhsProcessResponse, rhsProcessResponse)) {
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
			String theClusterId;
			theClusterId = this.getClusterId();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "clusterId", theClusterId),
					currentHashCode, theClusterId);
		}
		{
			RequestResponseDataType theInvokeFlowDefinition;
			theInvokeFlowDefinition = this.getInvokeFlowDefinition();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "invokeFlowDefinition",
							theInvokeFlowDefinition),
					currentHashCode, theInvokeFlowDefinition);
		}
		{
			FlowProcessType theExecuteRequest;
			theExecuteRequest = this.getExecuteRequest();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "executeRequest",
							theExecuteRequest),
					currentHashCode, theExecuteRequest);
		}
		{
			FlowProcessType theProcessResponse;
			theProcessResponse = this.getProcessResponse();
			currentHashCode = strategy.hashCode(
					LocatorUtils.property(locator, "processResponse",
							theProcessResponse),
					currentHashCode, theProcessResponse);
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
