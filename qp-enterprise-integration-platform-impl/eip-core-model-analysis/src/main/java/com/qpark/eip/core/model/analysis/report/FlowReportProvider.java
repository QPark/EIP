/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.model.analysis.report.model.FlowReportRow;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.FlowFilterType;
import com.qpark.eip.model.docmodel.FlowMapInOutType;
import com.qpark.eip.model.docmodel.FlowProcessType;
import com.qpark.eip.model.docmodel.FlowRuleType;
import com.qpark.eip.model.docmodel.FlowSubRequestType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.RequestResponseDataType;

/**
 * Collects {@link FlowReportRow} for the given services and operations.
 *
 * @author bhausen
 */
public class FlowReportProvider extends AbstractReportProvider {
	private static String STEP_MAPPING = "Mapping";

	private static String STEP_RULE = "Rule";

	private static String STEP_SUBREQUEST = "Information Request";

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(FlowReportProvider.class);

	/**
	 * @param f
	 * @param isInbound
	 * @param in
	 * @param out
	 * @param stepType
	 * @param stepName
	 * @param stepDescription
	 * @param arrayList
	 * @return
	 */
	private FlowReportRow createReportRow(
			final DataProviderModelAnalysis dataProvider, final FlowType f,
			final boolean isInbound, final Optional<ComplexType> in,
			final Optional<ComplexType> out, final String stepType,
			final String stepName, final String stepDescription,
			final ArrayList<String> mappingInputs) {
		final FlowReportRow row = new FlowReportRow();
		row.setFlowName(f.getName());
		row.setInbound(isInbound);
		row.setInputOutput(
				getComplexTypeInOutFieldElements(dataProvider, in, out));
		row.setProcessingStepType(stepType);
		row.setProcessingStepName(stepName);
		row.setStepDescription(stepDescription);
		if (Objects.nonNull(mappingInputs)) {
			row.getMappingInputType().addAll(mappingInputs);
		}
		return row;
	}
	/**
	 * @param dataProvider
	 * @param f
	 * @param im
	 * @param p
	 * @param isInbound
	 * @param rr
	 * @param stepId
	 * @return
	 */
	private Optional<FlowReportRow> getReportRowFilter(
			final DataProviderModelAnalysis dataProvider, final FlowType f,
			final InterfaceMappingType im, final FlowProcessType p,
			final boolean isInbound, final RequestResponseDataType rr,
			final String stepId) {
		Optional<FlowReportRow> value = Optional.empty();
		for (final FlowFilterType step : p.getFilter()) {
			if (step.getId().equals(stepId)) {
				final Optional<ComplexType> in = dataProvider
						.getComplexType(rr.getRequestId());
				final Optional<ComplexType> out = dataProvider
						.getComplexType(rr.getResponseId());
				final StringBuffer description = new StringBuffer(128);
				if (in.isPresent() && out.isPresent()) {
					description.append(step.getFilterInFieldDescription());
					description.append("\n");
					description.append(step.getFilterOutFieldDescription());
				} else if (in.isPresent()) {
					description.append(step.getFilterInFieldDescription());
				} else if (out.isPresent()) {
					description.append(step.getFilterOutFieldDescription());
				}
				value = Optional.of(this.createReportRow(dataProvider, f,
						isInbound, in, out, STEP_RULE, step.getName(),
						description.toString(), null));
			}
		}
		return value;
	}
	private Optional<FlowReportRow> getReportRowMapping(
			final DataProviderModelAnalysis dataProvider, final FlowType f,
			final InterfaceMappingType im, final FlowProcessType p,
			final boolean isInbound, final RequestResponseDataType rr,
			final String stepId) {
		Optional<FlowReportRow> value = Optional.empty();
		for (final FlowMapInOutType step : p.getMapInOut()) {
			if (step.getId().equals(stepId)) {
				final Optional<ComplexType> in = dataProvider
						.getComplexType(rr.getRequestId());
				final Optional<ComplexType> out = dataProvider
						.getComplexType(rr.getResponseId());
				final StringBuffer description = new StringBuffer(128);
				if (in.isPresent() && out.isPresent()) {
					description.append(step.getMapInFieldDescription());
					description.append("\n");
					description.append(step.getMapOutFieldDescription());
				} else if (in.isPresent()) {
					description.append(step.getMapInFieldDescription());
				} else if (out.isPresent()) {
					description.append(step.getMapOutFieldDescription());
				}
				final Set<String> mappingInputs = new TreeSet<>();
				dataProvider.getInterfaceMappings(f.getId()).stream()
						.filter(inf -> step.getInterfaceMappingId()
								.contains(inf.getId()))
						.forEach(inf -> {
							mappingInputs
									.addAll(getComplexTypeNames(dataProvider,
											inf.getFieldMappingInputType()));
						});
				value = Optional.of(this.createReportRow(dataProvider, f,
						isInbound, in, out, STEP_MAPPING, step.getName(),
						description.toString(),
						new ArrayList<String>(mappingInputs)));
			}
		}
		return value;
	}

	private Optional<FlowReportRow> getReportRowRule(
			final DataProviderModelAnalysis dataProvider, final FlowType f,
			final InterfaceMappingType im, final FlowProcessType p,
			final boolean isInbound, final RequestResponseDataType rr,
			final String stepId) {
		Optional<FlowReportRow> value = Optional.empty();
		for (final FlowRuleType step : p.getRule()) {
			if (step.getId().equals(stepId)) {
				final Optional<ComplexType> in = dataProvider
						.getComplexType(rr.getRequestId());
				final Optional<ComplexType> out = dataProvider
						.getComplexType(rr.getResponseId());
				final StringBuffer description = new StringBuffer(128);
				if (in.isPresent() && out.isPresent()) {
					description.append(step.getRuleInFieldDescription());
					description.append("\n");
					description.append(step.getRuleOutFieldDescription());
				} else if (in.isPresent()) {
					description.append(step.getRuleInFieldDescription());
				} else if (out.isPresent()) {
					description.append(step.getRuleOutFieldDescription());
				}
				value = Optional.of(this.createReportRow(dataProvider, f,
						isInbound, in, out, STEP_RULE, step.getName(),
						description.toString(), null));
			}
		}
		return value;
	}

	/**
	 * @param dataProvider
	 * @param flow
	 * @param im
	 * @param executeRequest
	 * @param isInbound
	 * @return
	 */
	private List<FlowReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider, final FlowType f,
			final InterfaceMappingType im, final FlowProcessType p,
			final boolean isInbound) {
		final List<FlowReportRow> value = new ArrayList<>();
		final RequestResponseDataType rr = p.getRequestResponse();
		p.getExecutionOrder().stream().forEach(stepId -> {
			this.getReportRowFilter(dataProvider, f, im, p, isInbound, rr,
					stepId).ifPresent(row -> value.add(row));
			this.getReportRowMapping(dataProvider, f, im, p, isInbound, rr,
					stepId).ifPresent(row -> value.add(row));
			this.getReportRowSubRequest(dataProvider, f, im, p, isInbound, rr,
					stepId).ifPresent(row -> value.add(row));
			this.getReportRowRule(dataProvider, f, im, p, isInbound, rr, stepId)
					.ifPresent(row -> value.add(row));
		});
		return value;
	}

	/**
	 * Get the list of {@link FlowReportRow}s of the given flow pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param flowPattern
	 *            the pattern the flow names need to match.
	 * @return the list of {@link FlowReportRow}s.
	 */
	public List<FlowReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String flowPattern) {
		return this.getReportRows(dataProvider, flowPattern,
				new TreeSet<String>());
	}

	/**
	 * Get the list of {@link FlowReportRow}s of the given flow pattern.
	 *
	 * @param dataProvider
	 *            the {@link DataProviderModelAnalysis}.
	 * @param flowNamePattern
	 *            the pattern the flow names need to match.
	 * @param ctIds
	 *            the set of used complex type ids.
	 * @return the list of {@link FlowReportRow}s.
	 */
	public List<FlowReportRow> getReportRows(
			final DataProviderModelAnalysis dataProvider,
			final String flowNamePattern, final Set<String> ctIds) {
		this.logger.debug("+getReportRows");
		final List<FlowReportRow> value = new ArrayList<>();
		dataProvider.getFlows(flowNamePattern)
				.stream().filter(
						f -> Objects.nonNull(f))
				.sorted(Comparator
						.comparing(FlowType::getName,
								Comparator.nullsLast(Comparator.naturalOrder()))
						.thenComparing(FlowType::getName))
				.forEach(f -> {
					dataProvider.getInterfaceMappings(f.getId()).stream()
							.filter(i -> Objects.nonNull(i)
									&& Objects.nonNull(i.getName()))
							.sorted(Comparator
									.comparing(InterfaceMappingType::getName))
							.forEach(im -> {
								value.addAll(this.getReportRows(dataProvider, f,
										im, f.getExecuteRequest(), true));
								value.addAll(this.getReportRows(dataProvider, f,
										im, f.getProcessResponse(), false));
							});
				});
		this.logger.debug("-getReportRows");
		return value;
	}

	private Optional<FlowReportRow> getReportRowSubRequest(
			final DataProviderModelAnalysis dataProvider, final FlowType f,
			final InterfaceMappingType im, final FlowProcessType p,
			final boolean isInbound, final RequestResponseDataType rr,
			final String stepId) {
		Optional<FlowReportRow> value = Optional.empty();
		for (final FlowSubRequestType step : p.getSubRequest()) {
			if (step.getId().equals(stepId)) {
				final Optional<ComplexType> in = dataProvider
						.getComplexType(rr.getRequestId());
				final Optional<ComplexType> out = dataProvider
						.getComplexType(rr.getResponseId());
				final StringBuffer description = new StringBuffer(128);
				if (in.isPresent() && out.isPresent()) {
					description.append(step.getSubRequestFieldDescription());
					description.append("\n");
					description.append(step.getSubResponseFieldDescription());
				} else if (in.isPresent()) {
					description.append(step.getSubRequestFieldDescription());
				} else if (out.isPresent()) {
					description.append(step.getSubResponseFieldDescription());
				}
				value = Optional.of(this.createReportRow(dataProvider, f,
						isInbound, in, out, STEP_SUBREQUEST, step.getName(),
						description.toString(), null));
			}
		}
		return value;
	}
}
