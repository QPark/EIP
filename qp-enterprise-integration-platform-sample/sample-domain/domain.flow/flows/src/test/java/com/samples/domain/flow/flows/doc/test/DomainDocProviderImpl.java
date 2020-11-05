/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.flow.flows.doc.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.ServiceType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;

/**
 * Implementation of {@link DomainDocProvider}.
 *
 * @author bhausen
 */
public class DomainDocProviderImpl implements DomainDocProvider {
  /** Service {@link ObjectFactory}. */
  protected final ObjectFactory objectFactory = new ObjectFactory();
  private EnterpriseType enterprise;
  /** The {@link org.slf4j.Logger}. */
  private Logger logger = LoggerFactory.getLogger(DomainDocProviderImpl.class);

  /**
   * @param enterprise
   */
  public DomainDocProviderImpl(final EnterpriseType enterprise) {
    this.enterprise = enterprise;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getComplexType(java.lang.String)
   */
  @Override
  public ComplexType getComplexType(final String complexTypeUUID) {
    final AtomicReference<ComplexType> value = new AtomicReference<>();
    this.enterprise.getDomains().stream().forEach(d -> d.getCluster().stream()
        .forEach(c -> c.getComplexType().stream().filter(ct -> ct.getId().equals(complexTypeUUID))
            .findAny().ifPresent(ct -> value.set(ct))));

    return value.get();
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getComplexTypes(java.util.Collection)
   */
  @Override
  public List<ComplexType> getComplexTypes(final Collection<String> complexTypeUUIDs) {
    final List<ComplexType> value = new ArrayList<>();
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getCluster().stream().forEach(c -> c.getComplexType().stream()
            .filter(ct -> complexTypeUUIDs.contains(ct.getId())).forEach(ct -> value.add(ct))));
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getDataType(java.lang.String)
   */
  @Override
  public DataType getDataType(final String id) {
    DataType value = null;
    final List<DataType> dataTypes = this.getDataTypes(Arrays.asList(id));
    if (!dataTypes.isEmpty()) {
      value = dataTypes.get(0);
    }
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getDataTypes(java.util.List)
   */
  @Override
  public List<DataType> getDataTypes(final List<String> ids) {
    final List<DataType> value = new ArrayList<>();
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getCluster().stream().forEach(c -> c.getComplexType().stream()
            .filter(ct -> ids.contains(ct.getId())).forEach(ct -> value.add(ct))));
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getCluster().stream().forEach(c -> c.getElementType().stream()
            .filter(e -> ids.contains(e.getId())).forEach(e -> value.add(e))));
    this.enterprise.getBasicDataTypes().stream().filter(b -> ids.contains(b.getId()))
        .forEach(b -> value.add(b));
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getElement(java.lang.String)
   */
  @Override
  public ElementType getElement(final String elementId) {
    final AtomicReference<ElementType> value = new AtomicReference<>();
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getCluster().stream().forEach(c -> c.getElementType().stream()
            .filter(e -> e.getId().equals(elementId)).findAny().ifPresent(e -> value.set(e))));
    return value.get();
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getFieldMapping(java.lang.String)
   */
  @Override
  public List<FieldMappingType> getFieldMapping(final String fieldDefinitionID) {
    final List<FieldMappingType> value = new ArrayList<>();
    this.enterprise.getDomains().stream().forEach(d -> d.getCluster().forEach(c -> {
      c.getDirectMappingType().stream().filter(fm -> fieldDefinitionID.equals(fm.getId()))
          .forEach(fm -> value.add(fm));
      c.getDefaultMappingType().stream().filter(fm -> fieldDefinitionID.equals(fm.getId()))
          .forEach(fm -> value.add(fm));
      c.getComplexMappingType().stream().filter(fm -> fieldDefinitionID.equals(fm.getId()))
          .forEach(fm -> value.add(fm));
      c.getComplexUUIDMappingType().stream().filter(fm -> fieldDefinitionID.equals(fm.getId()))
          .forEach(fm -> value.add(fm));
    }));
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getFlow(java.lang.String)
   */
  @Override
  public List<FlowType> getFlow(final String pattern) {
    final List<FlowType> value = this.enterprise.getFlows().stream()
        .filter(f -> f.getName().matches(String.format(".*%s.*", pattern)))
        .collect(Collectors.toList());
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getFlowMapping(java.lang.String)
   */
  @Override
  public List<InterfaceMappingType> getFlowMapping(final String flowUUID) {
    final List<String> interfaceMappingIds = new ArrayList<>();
    final List<InterfaceMappingType> value = new ArrayList<>();
    this.enterprise.getFlows().stream().filter(flow -> flow.getId().equals(flowUUID))
        .forEach(flow -> {
          if (Objects.nonNull(flow.getExecuteRequest())) {
            flow.getExecuteRequest().getMapInOut().stream()
                .forEach(x -> interfaceMappingIds.addAll(x.getInterfaceMappingId()));
          }
          if (Objects.nonNull(flow.getProcessResponse())) {
            flow.getProcessResponse().getMapInOut().stream()
                .forEach(x -> interfaceMappingIds.addAll(x.getInterfaceMappingId()));
          }
        });
    value.addAll(this.getInterfaceMappingTypes(interfaceMappingIds));
    this.getInterfaceMappingInheritents(interfaceMappingIds, value);
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getInterfaceMapping(java.lang.String)
   */
  @Override
  public InterfaceMappingType getInterfaceMapping(final String interfaceMappingId) {
    final List<InterfaceMappingType> value =
        this.getInterfaceMappingTypes(Arrays.asList(interfaceMappingId));
    if (Objects.nonNull(value) && !value.isEmpty()) {
      return value.get(0);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getInterfaceMappings(java.lang.String)
   */
  @Override
  public List<InterfaceMappingType> getInterfaceMappings(final String packagePattern) {
    final List<String> interfaceMappingIds = new ArrayList<>();
    final List<InterfaceMappingType> value = new ArrayList<>();
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getCluster().stream()
            .forEach(c -> c.getInterfaceMappingType().stream()
                .filter(i -> i.getJavaPackageName().matches(packagePattern))
                .forEach(i -> interfaceMappingIds.add(i.getId()))));
    value.addAll(this.getInterfaceMappingTypes(interfaceMappingIds));
    this.getInterfaceMappingInheritents(interfaceMappingIds, value);
    return value;
  }

  private List<String> getInterfaceMappingFieldDefinitionIds(
      final List<String> interfaceMappingIds) {
    final List<String> value = new ArrayList<>();
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getCluster().stream()
            .forEach(c -> c.getComplexType().stream()
                .forEach(ct -> ct.getField().stream()
                    .filter(f -> interfaceMappingIds.contains(f.getParentId()))
                    .forEach(f -> value.add(f.getFieldTypeDefinitionId())))));
    // final CriteriaBuilder cb = this.em.getCriteriaBuilder();
    // final CriteriaQuery<String> q = cb.createQuery(String.class);
    // final Root<FieldType> f = q.from(FieldType.class);
    //
    // final Predicate orParentIds = cb.disjunction();
    // interfaceMappingIds.stream().forEach(id ->
    // orParentIds.getExpressions()
    // .add(cb.equal(f.<String>get(FieldType_.parentId), id)));
    //
    // q.select(f.<String>get(FieldType_.fieldTypeDefinitionId));
    // q.where(cb.equal(f.<String>get(FieldType_.modelVersion),
    // modelVersion),
    // orParentIds);
    // final TypedQuery<String> typedQuery = this.em.createQuery(q);
    // final List<String> value = typedQuery.getResultList();
    return value;
  }

  private void getInterfaceMappingInheritents(final List<String> interfaceMappingIds,
      final List<InterfaceMappingType> interfaceMappings) {
    if (interfaceMappingIds.size() > 0) {
      final List<InterfaceMappingType> value = new ArrayList<>();
      final List<String> interfaceMappingsFromfieldDefinitionIds =
          this.getInterfaceMappingFieldDefinitionIds(interfaceMappingIds);
      value.addAll(this.getInterfaceMappingTypes(interfaceMappingsFromfieldDefinitionIds));
      interfaceMappings.addAll(value);
      this.getInterfaceMappingInheritents(interfaceMappingsFromfieldDefinitionIds,
          interfaceMappings);
    }
  }

  private List<InterfaceMappingType> getInterfaceMappingTypes(
      final List<String> interfaceMappingIds) {
    final List<InterfaceMappingType> value = new ArrayList<>();
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getCluster().stream().forEach(c -> c.getInterfaceMappingType().stream()
            .filter(i -> interfaceMappingIds.contains(i.getId())).forEach(i -> value.add(i))));
    return value;
  }

  /**
   * @return the {@link ObjectFactory} of the service.
   */
  protected ObjectFactory getObjectFactory() {
    return this.objectFactory;
  }

  /**
   * Get the name of the latest revision.
   *
   * @return the revision name.
   */
  @Override
  public String getRevision() {
    final String value = this.enterprise.getModelVersion();
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getService(java.lang.String)
   */
  @Override
  public ServiceType getService(final String serviceId) {
    final AtomicReference<ServiceType> value = new AtomicReference<>();
    this.enterprise.getDomains().stream().forEach(d -> d.getService().stream()
        .filter(s -> s.getServiceId().equals(serviceId)).findAny().ifPresent(s -> value.set(s)));
    return value.get();
  }

  /**
   * {@inheritDoc}
   *
   * @see com.ses.domain.doc.report.DomainDocProvider#getServiceIds()
   */
  @Override
  public List<String> getServiceIds() {
    final List<String> value = new ArrayList<>();
    this.enterprise.getDomains().stream()
        .forEach(d -> d.getService().stream().forEach(s -> value.add(s.getServiceId())));
    return value;
  }
}
