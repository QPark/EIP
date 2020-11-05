/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.qpark.eip.core.model.analysis.operation.ExtendedDataProviderModelAnalysis;
import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.ServiceType;

/**
 * @author bhausen
 */
public abstract class AnalysisEnterpriseDao implements ExtendedDataProviderModelAnalysis {
	private EnterpriseType enterprise;

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getClusterByTargetNamespace(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ClusterType getClusterByTargetNamespace(final String modelVersion, final String targetNamespace) {
		return Optional.ofNullable(targetNamespace).map(tn -> modelVersion).map(mv -> this.enterprise).map(e -> {
			if (e.getModelVersion().equals(modelVersion)) {
				return e.getDomains().stream().flatMap(d -> d.getCluster().stream())
						.filter(c -> c.getName().equals(targetNamespace)).findAny().orElse(null);
			}
			return null;
		}).orElse(null);
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getComplexType(java.lang.String)
	 */
	@Override
	public Optional<ComplexType> getComplexType(final String complexTypeId) {
		return Optional.ofNullable(complexTypeId).map(ctid -> this.enterprise)
				.map(e -> e.getDomains().stream().flatMap(d -> d.getCluster().stream())
						.flatMap(c -> c.getComplexType().stream()).filter(ct -> ct.getId().equals(complexTypeId))
						.findAny())
				.orElse(Optional.empty());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getComplexTypesById(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	public List<ComplexType> getComplexTypesById(final String modelVersion, final List<String> ids) {
		List<ComplexType> value = new ArrayList<>();
		if (Objects.nonNull(ids) && ids.size() > 0) {
			Optional.ofNullable(modelVersion).map(mv -> this.enterprise).ifPresent(e -> {
				if (e.getModelVersion().equals(modelVersion)) {
					value.addAll(e.getDomains().stream().flatMap(d -> d.getCluster().stream())
							.flatMap(c -> c.getComplexType().stream()).filter(ct -> ids.contains(ct.getId()))
							.collect(Collectors.toList()));
				}
			});
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getDataTypes(java.util.List)
	 */
	@Override
	public List<DataType> getDataTypes(final List<String> ids) {
		final List<DataType> value = new ArrayList<>();
		if (Objects.nonNull(ids) && ids.size() > 0) {
			Optional.ofNullable(this.enterprise).ifPresent(e -> {
				e.getDomains().stream().forEach(d -> d.getCluster().stream().forEach(c -> c.getComplexType().stream()
						.filter(ct -> ids.contains(ct.getId())).forEach(ct -> value.add(ct))));
				e.getDomains().stream().forEach(d -> d.getCluster().stream().forEach(c -> c.getElementType().stream()
						.filter(el -> ids.contains(el.getId())).forEach(el -> value.add(el))));
				e.getBasicDataTypes().stream().filter(b -> ids.contains(b.getId())).forEach(b -> value.add(b));
			});
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getDataTypesById(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	public List<DataType> getDataTypesById(final String modelVersion, final List<String> ids) {
		final List<DataType> value = new ArrayList<>();
		if (Objects.nonNull(ids) && ids.size() > 0) {
			Optional.ofNullable(modelVersion).map(mv -> this.enterprise).ifPresent(e -> {
				if (e.getModelVersion().equals(modelVersion)) {
					e.getDomains().stream().forEach(d -> d.getCluster().stream().forEach(c -> c.getComplexType()
							.stream().filter(ct -> ids.contains(ct.getId())).forEach(ct -> value.add(ct))));
					e.getDomains().stream().forEach(d -> d.getCluster().stream().forEach(c -> c.getElementType()
							.stream().filter(el -> ids.contains(el.getId())).forEach(el -> value.add(el))));
					e.getBasicDataTypes().stream().filter(b -> ids.contains(b.getId())).forEach(b -> value.add(b));
				}
			});
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getElement(java.lang.String)
	 */
	@Override
	public Optional<ElementType> getElement(final String elementId) {
		return Optional.ofNullable(elementId).map(eid -> this.enterprise)
				.map(e -> e.getDomains().stream().flatMap(d -> d.getCluster().stream())
						.flatMap(c -> c.getElementType().stream()).filter(el -> el.getId().equals(elementId)).findAny())
				.orElse(Optional.empty());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getElementTypesById(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	public List<ElementType> getElementTypesById(final String modelVersion, final List<String> ids) {
		List<ElementType> value = new ArrayList<>();
		if (Objects.nonNull(ids) && ids.size() > 0) {
			Optional.ofNullable(modelVersion).map(mv -> this.enterprise).ifPresent(e -> {
				if (e.getModelVersion().equals(modelVersion)) {
					value.addAll(e.getDomains().stream().flatMap(d -> d.getCluster().stream())
							.flatMap(c -> c.getElementType().stream()).filter(el -> ids.contains(el.getId()))
							.collect(Collectors.toList()));
				}
			});
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getEnterprises()
	 */
	@Override
	public List<EnterpriseType> getEnterprises() {
		return Optional.ofNullable(this.enterprise).map(e -> Arrays.asList(e)).orElse(new ArrayList<EnterpriseType>());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getFieldMapping(java.lang.String)
	 */
	@Override
	public Optional<FieldMappingType> getFieldMapping(final String id) {
		final List<FieldMappingType> value = new ArrayList<>();
		Optional.ofNullable(id).map(i -> this.enterprise).ifPresent(e -> {
			e.getDomains().stream().flatMap(d -> d.getCluster().stream()).forEach(c -> {
				c.getDirectMappingType().stream().filter(fm -> id.equals(fm.getId())).forEach(fm -> value.add(fm));
				c.getDefaultMappingType().stream().filter(fm -> id.equals(fm.getId())).forEach(fm -> value.add(fm));
				c.getComplexMappingType().stream().filter(fm -> id.equals(fm.getId())).forEach(fm -> value.add(fm));
				c.getComplexUUIDMappingType().stream().filter(fm -> id.equals(fm.getId())).forEach(fm -> value.add(fm));
			});
		});
		if (value.size() > 0) {
			return Optional.ofNullable(value.get(0));
		}
		return Optional.empty();
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getFieldMappingTypesById(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	public List<FieldMappingType> getFieldMappingTypesById(final String modelVersion, final List<String> ids) {
		final List<FieldMappingType> value = new ArrayList<>();
		if (Objects.nonNull(ids) && ids.size() > 0) {
			Optional.ofNullable(modelVersion).map(mv -> this.enterprise).ifPresent(e -> {
				if (e.getModelVersion().equals(modelVersion)) {
					e.getDomains().stream().flatMap(d -> d.getCluster().stream()).forEach(c -> {
						c.getDirectMappingType().stream().filter(fm -> ids.contains(fm.getId()))
								.forEach(fm -> value.add(fm));
						c.getDefaultMappingType().stream().filter(fm -> ids.contains(fm.getId()))
								.forEach(fm -> value.add(fm));
						c.getComplexMappingType().stream().filter(fm -> ids.contains(fm.getId()))
								.forEach(fm -> value.add(fm));
						c.getComplexUUIDMappingType().stream().filter(fm -> ids.contains(fm.getId()))
								.forEach(fm -> value.add(fm));
					});
				}
			});
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.operation.ExtendedDataProviderModelAnalysis#getFlowByNamePattern(java.lang.String,
	 *      java.util.Collection)
	 */
	@Override
	public List<FlowType> getFlowByNamePattern(final String modelVersion, final Collection<String> namePattern) {
		return Optional.ofNullable(namePattern).map(p -> this.enterprise)
				.map(e -> e.getFlows().stream().filter(f -> e.getModelVersion().equals(modelVersion))
						.filter(f -> f.getName().matches(String.format(".*%s.*", namePattern)))
						.collect(Collectors.toList()))
				.orElse(new ArrayList<>());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getFlows(java.util.Collection)
	 */
	@Override
	public List<FlowType> getFlows(final Collection<String> flowNameParts) {
		return Optional.ofNullable(flowNameParts).map(p -> this.enterprise).map(e -> e.getFlows().stream()
				.filter(f -> f.getName().matches(String.format(".*%s.*", flowNameParts))).collect(Collectors.toList()))
				.orElse(new ArrayList<>());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getLastModelVersion()
	 */
	@Override
	public String getLastModelVersion() {
		return Optional.ofNullable(this.enterprise).map(e -> e.getModelVersion()).orElse("");
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getRevisions()
	 */
	@Override
	public List<String> getRevisions() {
		final List<String> value = new ArrayList<>();
		Optional.ofNullable(this.enterprise).ifPresent(e -> value.add(e.getModelVersion()));
		return value;
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getService(java.lang.String)
	 */
	@Override
	public Optional<ServiceType> getService(final String serviceId) {
		return Optional
				.ofNullable(serviceId).map(ctid -> this.enterprise).map(e -> e.getDomains().stream()
						.flatMap(d -> d.getService().stream()).filter(s -> s.getId().equals(serviceId)).findAny())
				.orElse(Optional.empty());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getServiceByServiceId(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ServiceType getServiceByServiceId(final String modelVersion, final String serviceId) {
		return Optional.ofNullable(serviceId).map(sid -> modelVersion).map(mv -> this.enterprise).map(e -> {
			if (e.getModelVersion().equals(modelVersion)) {
				return e.getDomains().stream().flatMap(d -> d.getService().stream())
						.filter(s -> s.getServiceId().equals(serviceId)).findAny().orElse(null);
			}
			return null;
		}).orElse(null);
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getServiceIds()
	 */
	@Override
	public List<String> getServiceIds() {
		return Optional
				.ofNullable(this.enterprise).map(e -> e.getDomains().stream().flatMap(d -> d.getService().stream())
						.map(s -> s.getName()).sorted().distinct().collect(Collectors.toList()))
				.orElse(new ArrayList<String>());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getServiceIds(java.lang.String)
	 */
	@Override
	public List<String> getServiceIds(final String modelVersion) {
		return Optional.ofNullable(modelVersion).map(mv -> this.enterprise).map(e -> {
			if (e.getModelVersion().equals(modelVersion)) {
				return e.getDomains().stream().flatMap(d -> d.getService().stream()).map(s -> s.getServiceId())
						.distinct().sorted().collect(Collectors.toList());
			}
			return null;
		}).orElse(new ArrayList<>());
	}

	/**
	 * @see com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis#getTargetNamespaces(java.lang.String)
	 */
	@Override
	public List<String> getTargetNamespaces(final String modelVersion) {
		return Optional.ofNullable(modelVersion).map(mv -> this.enterprise).map(e -> {
			if (e.getModelVersion().equals(modelVersion)) {
				return e.getDomains().stream().flatMap(d -> d.getCluster().stream()).map(c -> c.getName()).distinct()
						.sorted().collect(Collectors.toList());
			}
			return null;
		}).orElse(new ArrayList<>());
	}

	/**
	 * @param enterprise
	 */
	public void setEnterpriseType(final EnterpriseType enterprise) {
		this.enterprise = enterprise;
	}
}
