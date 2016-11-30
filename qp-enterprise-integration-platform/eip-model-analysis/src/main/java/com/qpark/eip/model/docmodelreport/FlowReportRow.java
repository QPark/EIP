/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.model.docmodelreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * One row in the flow report.
 *
 * @author bhausen
 */
public class FlowReportRow {
	/** The stepDescription of the request or response. */
	private String stepDescription;
	/** The name of the flow. */
	private String flowName;
	/** The input/output type. */
	private String inputOutput;
	/** The described row is a in or out bound. */
	private boolean isInbound;
	/** The list of mapping input types. */
	private List<String> mappingInputType;
	/** The name of the processing step. */
	private String processingStepName;
	/** The type of the processing step. */
	private String processingStepType;

	/**
	 * @return the stepDescription
	 */
	public String getStepDescription() {
		return this.stepDescription;
	}

	/**
	 * @return the flowName
	 */
	public String getFlowName() {
		return this.flowName;
	}

	/**
	 * @return the inputOutput
	 */
	public String getInputOutput() {
		return this.inputOutput;
	}

	/**
	 * @return the mappingInputType
	 */
	public List<String> getMappingInputType() {
		if (Objects.isNull(this.mappingInputType)) {
			this.mappingInputType = new ArrayList<>();
		}
		return this.mappingInputType;
	}

	/**
	 * @return the processingStepName
	 */
	public String getProcessingStepName() {
		return this.processingStepName;
	}

	/**
	 * @return the processingStepType
	 */
	public String getProcessingStepType() {
		return this.processingStepType;
	}

	/**
	 * @return the isInbound
	 */
	public boolean isInbound() {
		return this.isInbound;
	}

	/**
	 * @param stepDescription
	 *            the stepDescription to set
	 */
	public void setStepDescription(final String stepDescription) {
		this.stepDescription = stepDescription;
	}

	/**
	 * @param flowName
	 *            the flowName to set
	 */
	public void setFlowName(final String flowName) {
		this.flowName = flowName;
	}

	/**
	 * @param isInbound
	 *            the isInbound to set
	 */
	public void setInbound(final boolean isInbound) {
		this.isInbound = isInbound;
	}

	/**
	 * @param inputOutput
	 *            the inputOutput to set
	 */
	public void setInputOutput(final String inputOutput) {
		this.inputOutput = inputOutput;
	}

	/**
	 * @param mappingInputType
	 *            the mappingInputType to set
	 */
	public void setMappingInputType(final List<String> mappingInputType) {
		this.mappingInputType = mappingInputType;
	}

	/**
	 * @param processingStepName
	 *            the processingStepName to set
	 */
	public void setProcessingStepName(final String processingStepName) {
		this.processingStepName = processingStepName;
	}

	/**
	 * @param processingStepType
	 *            the processingStepType to set
	 */
	public void setProcessingStepType(final String processingStepType) {
		this.processingStepType = processingStepType;
	}
}
