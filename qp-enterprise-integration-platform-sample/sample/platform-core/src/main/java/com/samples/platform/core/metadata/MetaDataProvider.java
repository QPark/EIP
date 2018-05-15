/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.metadata;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.model.common.MetaDataColumnType;
import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.samples.platform.core.validation.XsdClassPathResolver;

/**
 * Parses the model meta data and provides {@link MetaDataColumnType}s for
 * defined types.
 *
 * @author bhausen
 */
public class MetaDataProvider {
	/** The container of the model meta data. */
	private static final String METADATA_XML = "com.samples.bus-ModelAnalysis.xml";

	/**
	 * @param id
	 *            the data type id to check.
	 * @param dataTypes
	 *            the map of the {@link DataType}s.
	 * @return the {@link Optional} of the base {@link DataType}.
	 */
	private static Optional<DataType> getBaseDataType(final String id,
			final Map<String, DataType> dataTypes) {
		Optional<DataType> value = Optional.empty();
		List<String> typeHierarchyIds = getTypeHierarchy(id, dataTypes);
		if (typeHierarchyIds.size() > 0) {
			value = getDataType(typeHierarchyIds.get(0), dataTypes);
		}
		return value;
	}

	/**
	 * @param enterprise
	 *            the {@link EnterpriseType}.
	 * @param packageName
	 * @return the {@link Optional} of the {@link ClusterType}.
	 */
	private static Optional<ClusterType> getCluster(
			final EnterpriseType enterprise, final String packageName) {
		AtomicReference<ClusterType> value = new AtomicReference<>(null);
		enterprise.getDomains().stream()
				.forEach(domain -> domain.getCluster().stream().filter(
						cluster -> cluster.getPackageName().equals(packageName))
						.findFirst().ifPresent(cluster -> value.set(cluster)));
		return Optional.ofNullable(value.get());
	}

	/**
	 * @param enterprise
	 *            the {@link EnterpriseType}.
	 * @param type
	 *            the {@link Class} type of the {@link ComplexType} to check.
	 * @return the {@link Optional} of the {@link ComplexType}.
	 */
	private static <T> Optional<ComplexType> getComplexType(
			final EnterpriseType enterprise, final Class<T> type) {
		AtomicReference<ComplexType> value = new AtomicReference<>(null);
		getCluster(enterprise, type.getPackage().getName())
				.ifPresent(cluster -> cluster.getComplexType().stream()
						.filter(ct -> Objects.nonNull(ct.getJavaClassName()))
						.filter(ct -> ct.getJavaClassName()
								.equals(type.getName()))
						.findFirst().ifPresent(ct -> value.set(ct)));
		return Optional.ofNullable(value.get());
	}

	/**
	 * @param id
	 *            the data type id to check.
	 * @param dataTypes
	 *            the map of the {@link DataType}s.
	 * @return the {@link Optional} of the {@link DataType} with id.
	 */
	private static Optional<DataType> getDataType(final String id,
			final Map<String, DataType> dataTypes) {
		Optional<DataType> value = Optional.empty();
		if (Objects.nonNull(id)) {
			value = Optional.ofNullable(dataTypes.get(id));
		}
		return value;
	}

	/**
	 * @param enterprise
	 * @return the {@link Map} of {@link DataType}s assigned to their ids.
	 */
	private static Map<String, DataType> getDataTypeHashMap(
			final EnterpriseType enterprise) {
		Map<String, DataType> value = new HashMap<>();
		enterprise.getBasicDataTypes().stream()
				.forEach(dt -> value.put(dt.getId(), dt));
		enterprise.getDomains().stream().forEach(
				domain -> domain.getCluster().stream().forEach(cluster -> {
					cluster.getComplexType().stream()
							.forEach(ct -> value.put(ct.getId(), ct));
					cluster.getComplexMappingType().stream()
							.forEach(ct -> value.put(ct.getId(), ct));
					cluster.getComplexUUIDMappingType().stream()
							.forEach(ct -> value.put(ct.getId(), ct));
					cluster.getDirectMappingType().stream()
							.forEach(ct -> value.put(ct.getId(), ct));
				}));
		return value;
	}

	/**
	 * @param enterprise
	 *            the {@link EnterpriseType}.
	 * @param dataType
	 *            the {@link DataType}.
	 * @return the list of {@link MetaDataColumnType}s.
	 */
	private static List<MetaDataColumnType> getMetaData(
			final EnterpriseType enterprise, final DataType dataType) {
		List<MetaDataColumnType> value = new ArrayList<>();
		Map<String, DataType> dataTypes = getDataTypeHashMap(enterprise);
		List<String> typeHierarchyIds = getTypeHierarchy(dataType.getId(),
				dataTypes);
		typeHierarchyIds.stream().map(id -> getDataType(id, dataTypes))
				.filter(dt -> dt.isPresent()).map(dt -> dt.get())
				.filter(dt -> ComplexType.class.isInstance(dt))
				.map(dt -> (ComplexType) dt)
				.forEach(ct -> ct.getField().stream().forEach(f -> {
					MetaDataColumnType m = new MetaDataColumnType();
					value.add(m);
					getBaseDataType(f.getFieldTypeDefinitionId(), dataTypes)
							.ifPresent(bdt -> m
									.setDataType(bdt.getJavaClassName()));
					m.setDescription(f.getDescription());
					m.setMandatory(!f.isOptionalField());
					m.setName(f.getName());
					m.setReference("");
					m.setRestrictions("");
				}));
		return value;
	}

	/**
	 * @param id
	 *            the data type id to check.
	 * @param dataTypes
	 *            the map of the {@link DataType}s.
	 * @return the list of {@link DataType} ids, starting with the base.
	 */
	private static List<String> getTypeHierarchy(final String id,
			final Map<String, DataType> dataTypes) {
		List<String> value = new ArrayList<>();
		getDataType(id, dataTypes).ifPresent(dt -> {
			if (ComplexType.class.isInstance(dt) && Objects
					.nonNull(((ComplexType) dt).getDescendedFromId())) {
				value.addAll(getTypeHierarchy(
						((ComplexType) dt).getDescendedFromId(), dataTypes));
			}
			value.add(dt.getId());
		});
		return value;
	}

	/**
	 * @param is
	 *            the
	 * @return the {@link EnterpriseType}.
	 * @throws JAXBException
	 */
	private static Optional<EnterpriseType> unmarshal(final InputStream is)
			throws JAXBException {
		Optional<EnterpriseType> value = Optional.empty();
		JAXBContext jc = JAXBContext
				.newInstance(EnterpriseType.class.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();
		Object o = um.unmarshal(is);
		if (Objects.nonNull(o)) {
			if (JAXBElement.class.isInstance(o)) {
				o = ((JAXBElement<?>) o).getValue();
			}
			value = Optional.of((EnterpriseType) o);
		}
		return value;
	}

	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(MetaDataProvider.class);
	/** The {@link XsdClassPathResolver}. */
	@Autowired
	private XsdClassPathResolver resolver;

	/**
	 * @return the {@link Optional} of the {@link EnterpriseType}.
	 */
	private Optional<EnterpriseType> getEnterPrise() {
		Optional<EnterpriseType> value = Optional.empty();
		try {
			value = unmarshal(this.resolver.getResourceAsStream(METADATA_XML));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @param type
	 *            the {@link Class} type of the {@link ComplexType} to check.
	 * @return the list of {@link MetaDataColumnType}s.
	 */
	public <T> List<MetaDataColumnType> getMetaData(final Class<T> type) {
		this.logger.debug("+getMetaData");
		Instant start = Instant.now();
		List<MetaDataColumnType> value = new ArrayList<>();
		this.getEnterPrise().ifPresent(
				enterprise -> getComplexType(enterprise, type).ifPresent(
						ct -> value.addAll(getMetaData(enterprise, ct))));
		this.logger.debug("-getMetaData {}",
				Duration.between(start, Instant.now()));
		return value;
	}
}
