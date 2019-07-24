/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics;

import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;

/**
 * Provides async flow log messages.
 *
 * @author bhausen
 */
public interface AsyncFlowLogMessagePersistence {
	/** UUID FlowLogMessage Classification Data Incompatibility */
	public static final String CLASSIFICATION_DATA_INCOMPATIBILITY = FlowLogMessageClassificationEnum.DataIncompatibility
			.getUuid();
	/** UUID FlowLogMessage Classification Data Inconsistency */
	public static final String CLASSIFICATION_DATA_INCONSISTENCY = FlowLogMessageClassificationEnum.DataInconsistency
			.getUuid();
	/** UUID FlowLogMessage Classification Transfer Receipt */
	public static final String CLASSIFICATION_TRANSFER_RECEIPT = FlowLogMessageClassificationEnum.TransferReceipt
			.getUuid();
	/**
	 * UUID FlowLogMessage Classification Value not provided - default or null
	 */
	public static final String CLASSIFICATION_VALUE_NOT_PROVIDED_DEFAULTED_OR_NULL = FlowLogMessageClassificationEnum.ValueNotProvided
			.getUuid();
	/** UUID FlowLogMessage Severity Error */
	public static final String SEVERITY_ERROR = FlowLogMessageSeverityEnum.Error
			.getUuid();
	/** UUID FlowLogMessage Severity Information */
	public static final String SEVERITY_INFORMATION = FlowLogMessageSeverityEnum.Information
			.getUuid();
	/** UUID FlowLogMessage Severity Warning */
	public static final String SEVERITY_WARNING = FlowLogMessageSeverityEnum.Warning
			.getUuid();
	/** UUID FlowLogMessage Step Request execution */
	public static final String STEP_REQUEST_EXECUTION = FlowLogMessageStepEnum.RequestExecution
			.getUuid();
	/** UUID FlowLogMessage Step Request mapping */
	public static final String STEP_REQUEST_MAPPING = FlowLogMessageStepEnum.RequestMapping
			.getUuid();
	/** UUID FlowLogMessage Step Response mapping */
	public static final String STEP_RESPONSE_MAPPING = FlowLogMessageStepEnum.ResponseMapping
			.getUuid();
	/** UUID FlowLogMessage Step Entity mapping */
	public static final String STEP_ENTITY_MAPPING = FlowLogMessageStepEnum.EntityMapping
			.getUuid();
	/** UUID FlowLogMessage Step Attribute mapping */
	public static final String STEP_ATTRIBUTE_MAPPING = FlowLogMessageStepEnum.AttributeMapping
			.getUuid();
	/** UUID FlowLogMessage Step Response processing */
	public static final String STEP_RESPONSE_PROCESSING = FlowLogMessageStepEnum.ResponseProcessing
			.getUuid();
	/** UUID FlowLogMessage Step flow gateway execution */
	public static final String STEP_FLOW_GATEWAY_EXECUTION = FlowLogMessageStepEnum.FlowGatewayExecution
			.getUuid();
	/** UUID FlowLogMessage Type Attribute */
	public static final String TYPE_ATTRIBUTE = FlowLogMessageTypeEnum.Attribute
			.getUuid();
	/** UUID FlowLogMessage Type Entity */
	public static final String TYPE_ENTITY = FlowLogMessageTypeEnum.Entity
			.getUuid();
	/** UUID FlowLogMessage Type Flow */
	public static final String TYPE_FLOW = FlowLogMessageTypeEnum.Flow
			.getUuid();
	/** UUID FlowLogMessage Type Mapping */
	public static final String TYPE_MAPPING = FlowLogMessageTypeEnum.Mapping
			.getUuid();

	/**
	 * Submit the {@link FlowLogMessageType} to be stored in the database.
	 *
	 * @param log the {@link FlowLogMessageType}
	 */
	void submitFlowLogMessage(final FlowLogMessageType log);
}
