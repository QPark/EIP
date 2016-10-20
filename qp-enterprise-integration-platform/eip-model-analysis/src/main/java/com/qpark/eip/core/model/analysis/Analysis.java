/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.DomainType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.ServiceType;

public class Analysis {
	private final Map<String, Object> identifiables = new HashMap<>();
	private final EnterpriseType enterprise;
	/** The {@link Map} of names with their {@link DomainType}. */
	private Map<String, DomainType> domainMap = new TreeMap<>();
	/** The {@link Map} of names with their {@link ClusterType}. */
	private Map<String, ClusterType> clusterMap = new TreeMap<>();
	/** The {@link Map} of names with their {@link ServiceType}. */
	private Map<String, ServiceType> serviceMap = new TreeMap<>();
	/** The {@link Map} of names with their {@link ElementType}. */
	private Map<String, ElementType> elementMap = new TreeMap<>();
	/** The {@link Map} of names with their {@link DataType}. */
	private Map<String, DataType> dataTypeMap = new HashMap<>();
	/** The {@link Map} of ids with their {@link DataType}. */
	private Map<String, DataType> dataTypeIdMap = new HashMap<>();

	public Analysis(final EnterpriseType enterprise) {
		this.enterprise = enterprise;
	}

	void add(final String uuid, final Object o) {
		this.identifiables.put(uuid, o);
		if (DomainType.class.isInstance(o)) {
			this.domainMap.put(((DomainType) o).getName(), (DomainType) o);
		} else if (ClusterType.class.isInstance(o)) {
			this.clusterMap.put(((ClusterType) o).getName(), (ClusterType) o);
		} else if (ServiceType.class.isInstance(o)) {
			this.serviceMap.put(((ServiceType) o).getName(), (ServiceType) o);
		} else if (ElementType.class.isInstance(o)) {
			this.elementMap.put(((ElementType) o).getName(), (ElementType) o);
		} else if (DataType.class.isInstance(o)) {
			this.dataTypeMap.put(((DataType) o).getName(), (DataType) o);
			this.dataTypeIdMap.put(((DataType) o).getId(), (DataType) o);
		}
	}

	public Object get(final String uuid) {
		return this.identifiables.get(uuid);
	}

	/**
	 * Get the {@link ClusterType} according to the name space.
	 *
	 * @param namespace
	 *            the name space.
	 * @return the {@link ClusterType} or <code>null</code> .
	 */
	public ClusterType getCluster(final String namespace) {
		return this.clusterMap.get(namespace);
	}

	/**
	 * Get the {@link DataType} according to the QName of the complexType.
	 *
	 * @param name
	 *            the QName of the {@link DataType}.
	 * @return the {@link DataType} or <code>null</code> .
	 */
	public DataType getDataType(final String qname) {
		return this.dataTypeMap.get(qname);
	}

	/**
	 * Get the {@link DataType} according to the QName of the complexType.
	 *
	 * @param id
	 *            the id Name of the {@link DataType}.
	 * @return the {@link DataType} or <code>null</code> .
	 */
	public DataType getDataTypeById(final String id) {
		return this.dataTypeIdMap.get(id);
	}

	/**
	 * Get the {@link DataType}s.
	 *
	 * @return all {@link DataType}s.
	 */
	public Collection<DataType> getDataTypes() {
		return this.dataTypeMap.values();
	}

	/**
	 * Get the {@link DomainType} according to the path name.
	 *
	 * @param pathName
	 *            the path name.
	 * @return the {@link DomainType} or <code>null</code> .
	 */
	public DomainType getDomainType(final String pathName) {
		return this.domainMap.get(pathName);
	}

	/**
	 * Get the {@link ElementType} according to the QName of the element.
	 *
	 * @param name
	 *            the QName of the element.
	 * @return the {@link ElementType} or <code>null</code> .
	 */
	public ElementType getElementType(final String qname) {
		return this.elementMap.get(qname);
	}

	public EnterpriseType getEnterprise() {
		return this.enterprise;
	}

	/**
	 * Get the {@link ServiceType} according to the service id.
	 *
	 * @param serviceId
	 *            the service id.
	 * @return the {@link ServiceType} or <code>null</code> .
	 */
	public ServiceType getServiceType(final String serviceId) {
		return this.serviceMap.get(serviceId);
	}
}
