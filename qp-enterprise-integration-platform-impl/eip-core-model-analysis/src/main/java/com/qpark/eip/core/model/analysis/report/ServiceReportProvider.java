/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.model.analysis.report.model.ServiceReportRow;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.RequestResponseDataType;
import com.qpark.eip.model.docmodel.ServiceType;

/**
 * Collects {@link ServiceReportRow} for the given services and operations.
 *
 * @author bhausen
 */
public class ServiceReportProvider extends AbstractReportProvider {
	/**
	 * Create the {@link ServiceReportRow}.
	 *
	 * @param dataProvider
	 * @param s
	 * @param o
	 * @param isRequest
	 * @param ct
	 * @param ctIds
	 * @return the {@link ServiceReportRow}.
	 */
	private static ServiceReportRow getServiceReportRow(
			final DataProviderModelAnalysis dataProvider, final ServiceType s,
			final OperationType o, final boolean isRequest,
			final ComplexType ct, final Set<String> ctIds) {
		final ServiceReportRow value = new ServiceReportRow();
		value.setServiceId(s.getName());
		value.setOperationName(o.getShortName());
		value.setRequest(isRequest);
		value.setDescription(o.getRequestFieldDescription());
		value.setFields(getComplexTypeFieldElements(dataProvider, ct));
		// Currently Flow links are not supported.
		// value.setFlowLinks(flowLinks);

		ctIds.add(ct.getId());
		ctIds.addAll(getFieldDataTypeIds(dataProvider, ct));
		return value;
	}

	/**
	 * @param operationNamePattern
	 * @param operationShortName
	 * @return <code>true</code>, if no operationNamePattern is provided or it
	 *         matches the operation short name.
	 */
	private static boolean isValidOperation(final String operationNamePattern,
			final String operationShortName) {
		boolean value = false;
		if (Objects.isNull(operationNamePattern)
				|| operationNamePattern.trim().equals("")
				|| operationShortName.matches(operationNamePattern)) {
			value = true;
		}
		return value;
	}

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(ServiceReportProvider.class);

	/**
	 * Get the list of {@link ServiceReportRow}s of the given services and
	 * operations.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param serviceIds
	 *            the service ids to provide.
	 * @param operationNamePattern
	 *            the pattern of the operation names to match.
	 * @return the list of {@link ServiceReportRow}s.
	 */
	public List<ServiceReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final List<String> serviceIds, final String operationNamePattern) {
		return this.getReportRows(dataProvider, serviceIds,
				operationNamePattern, new TreeSet<String>());
	}

	/**
	 * Get the list of {@link ServiceReportRow}s of the given services and
	 * operations.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param serviceIds
	 *            the service ids to provide.
	 * @param operationNamePattern
	 *            the pattern of the operation names to match.
	 * @param ctIds
	 *            the set of used complex type ids.
	 * @return the list of {@link ServiceReportRow}s.
	 */
	public List<ServiceReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final List<String> serviceIds, final String operationNamePattern,
			final Set<String> ctIds) {
		this.logger.debug("+getReportRows");
		final List<ServiceReportRow> value = new ArrayList<>();
		serviceIds.stream().filter(serviceId -> Objects.nonNull(serviceId))
				.map(serviceId -> dataProvider.getService(serviceId))
				.forEach(os -> os.ifPresent(s -> {
					/* Service. */
					s.getOperation().stream()
							.filter(o -> Objects.nonNull(o.getRequestResponse())
									&& Objects.nonNull(o.getShortName())
									&& isValidOperation(operationNamePattern,
											o.getShortName()))
							.forEach(o -> {
								/* Operation. */
								final RequestResponseDataType rr = o
										.getRequestResponse();
								/* Request. */
								getComplexTypeByElementId(dataProvider,
										rr.getRequestId()).ifPresent(ct -> value
												.add(getServiceReportRow(
														dataProvider, s, o,
														true, ct, ctIds)));
								/* Response. */
								getComplexTypeByElementId(dataProvider, rr
										.getResponseId()).ifPresent(ct -> value
												.add(getServiceReportRow(
														dataProvider, s, o,
														false, ct, ctIds)));

							});
				}));
		this.logger.debug("-getReportRows");
		return value;
	}
}
