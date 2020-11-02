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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qpark.eip.core.EagerLoader;
import com.qpark.eip.core.model.analysis.config.EipModelAnalysisPersistenceConfig;
import com.qpark.eip.core.model.analysis.operation.ExtendedDataProviderModelAnalysis;
import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.ClusterType_;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.ComplexType_;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.DataType_;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.ElementType_;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.EnterpriseType_;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FieldMappingType_;
import com.qpark.eip.model.docmodel.FieldType;
import com.qpark.eip.model.docmodel.FieldType_;
import com.qpark.eip.model.docmodel.FlowMapInOutType;
import com.qpark.eip.model.docmodel.FlowMapInOutType_;
import com.qpark.eip.model.docmodel.FlowProcessType;
import com.qpark.eip.model.docmodel.FlowProcessType_;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.FlowType_;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.InterfaceMappingType_;
import com.qpark.eip.model.docmodel.ServiceType;
import com.qpark.eip.model.docmodel.ServiceType_;

/**
 * The dao of the domain model analysis.
 *
 * @author bhausen
 */
public class AnalysisDao implements ExtendedDataProviderModelAnalysis {
	/** The {@link EntityManager}. */
	@PersistenceContext(
			unitName = EipModelAnalysisPersistenceConfig.PERSISTENCE_UNIT_NAME,
			name = EipModelAnalysisPersistenceConfig.ENTITY_MANAGER_FACTORY_NAME)
	private EntityManager em;

	/**
	 * Check if the {@link EnterpriseType} with given name and modelVersion
	 * exists.
	 *
	 * @param name
	 *                         the name of the {@link EnterpriseType}.
	 * @param modelVersion
	 *                         the version of the model.
	 * @return <code>true</code> if exists, else <code>false</code>.
	 */
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public boolean existsEnterprise(final String name,
			final String modelVersion) {
		boolean value = false;

		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<Long> q = cb.createQuery(Long.class);
		final Root<EnterpriseType> f = q.from(EnterpriseType.class);
		q.select(f.<Long> get(EnterpriseType_.hjid));
		q.where(cb.equal(f.<String> get(EnterpriseType_.name), name), cb.equal(
				f.<String> get(EnterpriseType_.modelVersion), modelVersion));
		final TypedQuery<Long> typedQuery = this.em.createQuery(q);
		try {
			final Long l = typedQuery.getSingleResult();
			if (l != null && l.longValue() != 0) {
				value = true;
			}
		} catch (final Exception e) {
			value = false;
		}
		return value;
	}

	/**
	 * Get the {@link ClusterType} with targetNamespace of the modelVersion.
	 *
	 * @param modelVersion
	 *                            the model version.
	 * @param targetNamespace
	 *                            the target namespace.
	 * @return the {@link ClusterType} with targetNamespace of the modelVersion.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public ClusterType getClusterByTargetNamespace(final String modelVersion,
			final String targetNamespace) {
		ClusterType value = null;
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<ClusterType> q = cb.createQuery(ClusterType.class);
		final Root<ClusterType> f = q.from(ClusterType.class);
		q.where(cb.equal(f.<String> get(ClusterType_.modelVersion),
				modelVersion),
				cb.equal(f.<String> get(ClusterType_.name), targetNamespace));
		final TypedQuery<ClusterType> typedQuery = this.em.createQuery(q);
		final List<ClusterType> list = typedQuery.getResultList();
		if (Objects.nonNull(list) && list.size() != 1) {
			value = list.get(0);
			EagerLoader.load(value);
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.ExtendedDataProviderModelAnalysis.DataProviderModelAnalysis#getComplexType(java.lang.String)
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public Optional<ComplexType> getComplexType(final String complexTypeId) {
		final Optional<ComplexType> value = this
				.getDataTypes(Arrays.asList(complexTypeId)).stream()
				.filter(dt -> Objects.nonNull(dt)
						&& ComplexType.class.isInstance(dt))
				.map(dt -> (ComplexType) dt).findFirst();
		return value;
	}

	/**
	 * Get the list of {@link ComplexType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link ComplexType}.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<ComplexType> getComplexTypesById(final String modelVersion,
			final List<String> ids) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<ComplexType> q = cb.createQuery(ComplexType.class);
		final Root<ComplexType> f = q.from(ComplexType.class);
		final Predicate orIds = cb.disjunction();
		ids.stream().forEach(id -> orIds.getExpressions()
				.add(cb.equal(f.<String> get(ComplexType_.id), id)));
		q.where(cb.equal(f.<String> get(ComplexType_.modelVersion),
				modelVersion), orIds);
		final TypedQuery<ComplexType> typedQuery = this.em.createQuery(q);
		final List<ComplexType> value = typedQuery.getResultList();
		value.stream().forEach(ct -> EagerLoader.load(ct));
		return value;
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.ExtendedDataProviderModelAnalysis.DataProviderModelAnalysis#getDataTypes(java.util.List)
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<DataType> getDataTypes(final List<String> ids) {
		return this.getDataTypesById(this.getLastModelVersion(), ids);
	}

	/**
	 * Get the list of {@link DataType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link DataType}.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<DataType> getDataTypesById(final String modelVersion,
			final List<String> ids) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<DataType> q = cb.createQuery(DataType.class);
		final Root<DataType> f = q.from(DataType.class);
		final Predicate orIds = cb.disjunction();
		ids.stream().forEach(id -> orIds.getExpressions()
				.add(cb.equal(f.<String> get(DataType_.id), id)));
		q.where(cb.equal(f.<String> get(DataType_.modelVersion), modelVersion),
				orIds);
		q.orderBy(cb.asc(f.<String> get(DataType_.name)),
				cb.asc(f.<String> get(DataType_.id)));
		final TypedQuery<DataType> typedQuery = this.em.createQuery(q);
		final List<DataType> value = typedQuery.getResultList();
		value.stream().forEach(dt -> EagerLoader.load(dt));
		return value;
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.ExtendedDataProviderModelAnalysis.DataProviderModelAnalysis#getElement(java.lang.String)
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public Optional<ElementType> getElement(final String elementId) {
		return this
				.getElementTypesById(this.getLastModelVersion(),
						Arrays.asList(elementId))
				.stream().filter(e -> Objects.nonNull(e)).findFirst();
	}

	/**
	 * Get the list of {@link ElementType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link ElementType}.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<ElementType> getElementTypesById(final String modelVersion,
			final List<String> ids) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<ElementType> q = cb.createQuery(ElementType.class);
		final Root<ElementType> f = q.from(ElementType.class);
		final Predicate orIds = cb.disjunction();
		ids.stream().forEach(id -> orIds.getExpressions()
				.add(cb.equal(f.<String> get(ElementType_.id), id)));
		q.where(cb.equal(f.<String> get(ElementType_.modelVersion),
				modelVersion), orIds);
		final TypedQuery<ElementType> typedQuery = this.em.createQuery(q);
		final List<ElementType> value = typedQuery.getResultList();
		value.stream().forEach(ct -> EagerLoader.load(ct));
		return value;
	}

	/**
	 * Get the list of all {@link EnterpriseType}s.
	 *
	 * @return the list of all {@link EnterpriseType}s.
	 */
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<EnterpriseType> getEnterprises() {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<EnterpriseType> q = cb
				.createQuery(EnterpriseType.class);
		q.from(EnterpriseType.class);
		final TypedQuery<EnterpriseType> typedQuery = this.em.createQuery(q);
		final List<EnterpriseType> value = typedQuery.getResultList();
		return value;
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.ExtendedDataProviderModelAnalysis.DataProviderModelAnalysis#getFieldMapping(java.lang.String)
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public Optional<FieldMappingType> getFieldMapping(final String id) {
		return this.getFieldMappingTypeById(this.getLastModelVersion(), id);
	}

	/**
	 * Get the {@link FieldMappingType} with the id.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param id
	 *                         the id to return.
	 * @return the {@link FieldMappingType}.
	 * @since 3.5.1
	 */
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public Optional<FieldMappingType> getFieldMappingTypeById(
			final String modelVersion, final String id) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<FieldMappingType> q = cb
				.createQuery(FieldMappingType.class);
		final Root<FieldMappingType> f = q.from(FieldMappingType.class);
		q.where(cb.equal(f.<String> get(FieldMappingType_.modelVersion),
				modelVersion),
				cb.equal(f.<String> get(FieldMappingType_.id), id));
		final TypedQuery<FieldMappingType> typedQuery = this.em.createQuery(q);
		final List<FieldMappingType> list = typedQuery.getResultList();

		final Optional<FieldMappingType> value = list.stream().findFirst();
		value.ifPresent(ct -> EagerLoader.load(ct));
		return value;
	}

	/**
	 * Get the list of {@link FieldMappingType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link FieldMappingType}.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<FieldMappingType> getFieldMappingTypesById(
			final String modelVersion, final List<String> ids) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<FieldMappingType> q = cb
				.createQuery(FieldMappingType.class);
		final Root<FieldMappingType> f = q.from(FieldMappingType.class);
		final Predicate orIds = cb.disjunction();
		ids.stream().forEach(id -> orIds.getExpressions()
				.add(cb.equal(f.<String> get(FieldMappingType_.id), id)));
		q.where(cb.equal(f.<String> get(FieldMappingType_.modelVersion),
				modelVersion), orIds);
		final TypedQuery<FieldMappingType> typedQuery = this.em.createQuery(q);
		final List<FieldMappingType> value = typedQuery.getResultList();
		value.stream().forEach(ct -> EagerLoader.load(ct));
		return value;
	}

	/**
	 * Get the list of {@link FlowType}s matching the name pattern.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param namePattern
	 *                         the name pattern.
	 * @return the list of {@link FlowType}s matching the name pattern.
	 * @since 3.5.1
	 */
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<FlowType> getFlowByNamePattern(final String modelVersion,
			final Collection<String> namePattern) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<FlowType> q = cb.createQuery(FlowType.class);
		final Root<FlowType> f = q.from(FlowType.class);

		final Predicate or = cb.disjunction();
		namePattern.stream().forEach(id -> or.getExpressions()
				.add(cb.like(f.<String> get(FlowType_.name), "%" + id + "%")));
		final Predicate and = cb.conjunction();
		and.getExpressions().add(
				cb.equal(f.<String> get(FlowType_.modelVersion), modelVersion));
		and.getExpressions().add(or);
		q.where(and);
		final TypedQuery<FlowType> typedQuery = this.em.createQuery(q);
		final List<FlowType> value = typedQuery.getResultList();
		value.stream().forEach(ft -> EagerLoader.load(ft));
		return value;
	}

	/**
	 * Get the list of {@link InterfaceMappingType}s of the flow with id.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param flowId
	 *                         the flow id.
	 * @return the list of {@link InterfaceMappingType}s of the flow with id.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<InterfaceMappingType> getFlowInterfaceMappingTypes(
			final String modelVersion, final String flowId) {
		final List<InterfaceMappingType> value = new ArrayList<>();
		final List<String> flowProcessTypeIds = this
				.getFlowProcessTypeIds(modelVersion, flowId);
		final List<String> flowMappingInterfaceMappingTypeIds = this
				.getFlowMappingInterfaceMappingTypeIds(modelVersion,
						flowProcessTypeIds);
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<InterfaceMappingType> q = cb
				.createQuery(InterfaceMappingType.class);
		final Root<InterfaceMappingType> f = q.from(InterfaceMappingType.class);

		final Predicate orIds = cb.disjunction();
		flowMappingInterfaceMappingTypeIds.stream().forEach(id -> orIds
				.getExpressions()
				.add(cb.equal(f.<String> get(InterfaceMappingType_.id), id)));

		q.where(cb.equal(f.<String> get(InterfaceMappingType_.modelVersion),
				modelVersion), orIds);

		final TypedQuery<InterfaceMappingType> typedQuery = this.em
				.createQuery(q);
		value.addAll(typedQuery.getResultList());

		final List<String> interfaceMappingIds = value.stream()
				.map(i -> i.getId()).collect(Collectors.toList());
		this.getInterfaceMappingInheritents(modelVersion, interfaceMappingIds,
				value);
		value.stream().forEach(im -> EagerLoader.load(im));
		return value;
	}

	private List<String> getFlowMappingInterfaceMappingTypeIds(
			final String modelVersion, final List<String> flowProcessTypeIds) {
		final List<String> value = new ArrayList<>();
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<Long> q = cb.createQuery(Long.class);
		final Root<FlowMapInOutType> f = q.from(FlowMapInOutType.class);

		final Predicate orParentIds = cb.disjunction();
		flowProcessTypeIds.stream().forEach(id -> orParentIds.getExpressions()
				.add(cb.equal(f.<String> get(FlowMapInOutType_.parentId), id)));

		q.select(f.<Long> get(FlowMapInOutType_.hjid));
		q.where(cb.equal(f.<String> get(FlowMapInOutType_.modelVersion),
				modelVersion), orParentIds);

		final TypedQuery<Long> typedQuery = this.em.createQuery(q);
		typedQuery.getResultList().stream().forEach(hjid -> {
			value.addAll(this.em.getReference(FlowMapInOutType.class, hjid)
					.getInterfaceMappingId());
		});

		return value;
	}

	private List<String> getFlowProcessTypeIds(final String modelVersion,
			final String flowId) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<String> q = cb.createQuery(String.class);
		final Root<FlowProcessType> f = q.from(FlowProcessType.class);

		q.select(f.<String> get(FlowProcessType_.id));
		q.where(cb.equal(f.<String> get(FlowProcessType_.modelVersion),
				modelVersion),
				cb.equal(f.<String> get(FlowProcessType_.parentId), flowId));
		q.orderBy(cb.asc(f.<Long> get(FlowProcessType_.hjid)));
		final TypedQuery<String> typedQuery = this.em.createQuery(q);
		final List<String> value = typedQuery.getResultList();
		return value;
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.ExtendedDataProviderModelAnalysis.DataProviderModelAnalysis#getFlows(java.lang.String)
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<FlowType> getFlows(final Collection<String> flowNamePattern) {
		return this.getFlowByNamePattern(this.getLastModelVersion(),
				flowNamePattern);
	}

	private List<String> getInterfaceMappingFieldDefinitionIds(
			final String modelVersion, final List<String> interfaceMappingIds) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<String> q = cb.createQuery(String.class);
		final Root<FieldType> f = q.from(FieldType.class);

		final Predicate orParentIds = cb.disjunction();
		interfaceMappingIds.stream().forEach(id -> orParentIds.getExpressions()
				.add(cb.equal(f.<String> get(FieldType_.parentId), id)));

		q.select(f.<String> get(FieldType_.fieldTypeDefinitionId));
		q.where(cb.equal(f.<String> get(FieldType_.modelVersion), modelVersion),
				orParentIds);
		final TypedQuery<String> typedQuery = this.em.createQuery(q);
		final List<String> value = typedQuery.getResultList();
		return value;
	}

	private void getInterfaceMappingInheritents(final String modelVersion,
			final List<String> interfaceMappingIds,
			final List<InterfaceMappingType> interfaceMappings) {
		if (interfaceMappingIds.size() > 0) {
			List<InterfaceMappingType> value = new ArrayList<>();
			final List<String> fieldDefinitionIds = this
					.getInterfaceMappingFieldDefinitionIds(modelVersion,
							interfaceMappingIds);

			final CriteriaBuilder cb = this.em.getCriteriaBuilder();
			final CriteriaQuery<InterfaceMappingType> q = cb
					.createQuery(InterfaceMappingType.class);
			final Root<InterfaceMappingType> f = q
					.from(InterfaceMappingType.class);

			final Predicate orIds = cb.disjunction();
			fieldDefinitionIds.stream()
					.forEach(id -> orIds.getExpressions().add(cb.equal(
							f.<String> get(InterfaceMappingType_.id), id)));

			q.where(cb.equal(f.<String> get(InterfaceMappingType_.modelVersion),
					modelVersion), orIds);

			final TypedQuery<InterfaceMappingType> typedQuery = this.em
					.createQuery(q);
			value = typedQuery.getResultList();
			interfaceMappings.addAll(value);
			final List<String> foundIds = value.stream().map(i -> i.getId())
					.collect(Collectors.toList());
			this.getInterfaceMappingInheritents(modelVersion, foundIds,
					interfaceMappings);
		}
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.ExtendedDataProviderModelAnalysis.DataProviderModelAnalysis#getInterfaceMappings(java.lang.String)
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<InterfaceMappingType> getInterfaceMappings(
			final String flowId) {
		return this.getFlowInterfaceMappingTypes(this.getLastModelVersion(),
				flowId);
	}

	/**
	 * Get the last model version (lexical compare).
	 *
	 * @return the last model version.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public String getLastModelVersion() {
		String value = null;
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<String> q = cb.createQuery(String.class);
		final Root<EnterpriseType> f = q.from(EnterpriseType.class);
		q.select(f.<String> get(EnterpriseType_.modelVersion));
		q.orderBy(cb.desc(f.<String> get(EnterpriseType_.modelVersion)));
		final TypedQuery<String> typedQuery = this.em.createQuery(q);
		final List<String> list = typedQuery.getResultList();
		if (Objects.nonNull(list) && list.size() > 0) {
			value = list.get(0);
		}
		return value;
	}

	/**
	 * Get the list of serviceIds available.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @return the list of serviceIds available.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<String> getRevisions() {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<String> q = cb.createQuery(String.class);
		final Root<EnterpriseType> f = q.from(EnterpriseType.class);
		q.select(f.<String> get(EnterpriseType_.modelVersion));
		q.orderBy(cb.asc(f.<String> get(EnterpriseType_.modelVersion)));
		final TypedQuery<String> typedQuery = this.em.createQuery(q);
		final List<String> value = typedQuery.getResultList();
		return value;
	}

	/**
	 * @see com.qpark.eip.core.model.analysis.ExtendedDataProviderModelAnalysis.DataProviderModelAnalysis#getService(java.lang.String)
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public Optional<ServiceType> getService(final String serviceId) {
		final Optional<ServiceType> value = Optional.ofNullable(this
				.getServiceByServiceId(this.getLastModelVersion(), serviceId));
		return value;
	}

	/**
	 * Get the {@link ServiceType} with serviceId of the modelVersion.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param serviceId
	 *                         the serviceId.
	 * @return the {@link ServiceType} with serviceId of the modelVersion.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public ServiceType getServiceByServiceId(final String modelVersion,
			final String serviceId) {
		ServiceType value = null;
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<ServiceType> q = cb.createQuery(ServiceType.class);
		final Root<ServiceType> f = q.from(ServiceType.class);
		q.where(cb.equal(f.<String> get(ServiceType_.modelVersion),
				modelVersion),
				cb.equal(f.<String> get(ServiceType_.serviceId), serviceId));
		final TypedQuery<ServiceType> typedQuery = this.em.createQuery(q);
		final List<ServiceType> list = typedQuery.getResultList();
		if (Objects.nonNull(list) && list.size() == 1) {
			value = list.get(0);
			EagerLoader.load(value);
		}
		return value;
	}

	/**
	 * Get the list of serviceIds available.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @return the list of serviceIds available.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<String> getServiceIds(final String modelVersion) {
		final List<String> value = new ArrayList<>();
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<ServiceType> q = cb.createQuery(ServiceType.class);
		final Root<ServiceType> f = q.from(ServiceType.class);
		q.where(cb.equal(f.<String> get(ServiceType_.modelVersion),
				modelVersion));
		q.orderBy(cb.asc(f.<String> get(ServiceType_.serviceId)),
				cb.asc(f.<String> get(ServiceType_.namespace)));
		final TypedQuery<ServiceType> typedQuery = this.em.createQuery(q);
		final List<ServiceType> services = typedQuery.getResultList();
		services.stream().filter(s -> Objects.nonNull(s.getServiceId()))
				.forEach(s -> value.add(s.getServiceId()));
		return value;
	}

	/**
	 * Get the list of target name spaces available.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @return the list of target name spaces available.
	 * @since 3.5.1
	 */
	@Override
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public List<String> getTargetNamespaces(final String modelVersion) {
		final List<String> value = new ArrayList<>();
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<ClusterType> q = cb.createQuery(ClusterType.class);
		final Root<ClusterType> f = q.from(ClusterType.class);
		q.where(cb.equal(f.<String> get(ClusterType_.modelVersion),
				modelVersion));
		q.orderBy(cb.asc(f.<String> get(ClusterType_.name)),
				cb.asc(f.<String> get(ClusterType_.id)));
		final TypedQuery<ClusterType> typedQuery = this.em.createQuery(q);
		final List<ClusterType> cluster = typedQuery.getResultList();
		cluster.stream().filter(c -> Objects.nonNull(c.getName()))
				.forEach(c -> value.add(c.getName()));
		return value;
	}

	/**
	 * Save the {@link EnterpriseType}.
	 *
	 * @param value
	 *                  the {@link EnterpriseType}.
	 */
	@Transactional(
			value = EipModelAnalysisPersistenceConfig.TRANSACTION_MANAGER_NAME,
			propagation = Propagation.REQUIRED)
	public void saveEnterprise(final EnterpriseType value) {
		this.em.merge(value);
	}
}
