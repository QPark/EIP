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
	public static final String CLASSIFICATION_DATA_INCOMPATIBILITY = "0a5c2391-e205-3d9b-0016-000000000003";
	/** UUID FlowLogMessage Classification Data Inconsistency */
	public static final String CLASSIFICATION_DATA_INCONSISTENCY = "0a5c2391-e205-3d9b-0016-000000000001";
	/** UUID FlowLogMessage Classification Transfer Receipt */
	public static final String CLASSIFICATION_TRANSFER_RECEIPT = "0a5c2391-e205-3d9b-0016-000000000004";
	/**
	 * UUID FlowLogMessage Classification Value not provided - default or null
	 */
	public static final String CLASSIFICATION_VALUE_NOT_PROVIDED_DEFAULTED_OR_NULL = "0a5c2391-e205-3d9b-0016-000000000002";
	/** UUID FlowLogMessage Severity Error */
	public static final String SEVERITY_ERROR = "0a5c2391-e205-3d9b-0014-000000000003";
	/** UUID FlowLogMessage Severity Information */
	public static final String SEVERITY_INFORMATION = "0a5c2391-e205-3d9b-0014-000000000001";
	/** UUID FlowLogMessage Severity Warning */
	public static final String SEVERITY_WARNING = "0a5c2391-e205-3d9b-0014-000000000002";
	/** UUID FlowLogMessage Step Request execution */
	public static final String STEP_REQUEST_EXECUTION = "0a5c2391-e205-3d9b-0015-000000000001";
	/** UUID FlowLogMessage Step Request mapping */
	public static final String STEP_REQUEST_MAPPING = "0a5c2391-e205-3d9b-0015-000000000003";
	/** UUID FlowLogMessage Step Response mapping */
	public static final String STEP_RESPONSE_MAPPING = "0a5c2391-e205-3d9b-0015-000000000004";
	/** UUID FlowLogMessage Step Entity mapping */
	public static final String STEP_ENTITY_MAPPING = "0a5c2391-e205-3d9b-0015-000000000005";
	/** UUID FlowLogMessage Step Attribute mapping */
	public static final String STEP_ATTRIBUTE_MAPPING = "0a5c2391-e205-3d9b-0015-000000000006";
	/** UUID FlowLogMessage Step Response processing */
	public static final String STEP_RESPONSE_PROCESSING = "0a5c2391-e205-3d9b-0015-000000000002";
	/** UUID FlowLogMessage Step flow gateway execution */
	public static final String STEP_FLOW_GATEWAY_EXECUTION = "0a5c2391-e205-3d9b-0015-000000000007";
	/** UUID FlowLogMessage Type Attribute */
	public static final String TYPE_ATTRIBUTE = "0a5c2391-e205-3d9b-0013-000000000004";
	/** UUID FlowLogMessage Type Entity */
	public static final String TYPE_ENTITY = "0a5c2391-e205-3d9b-0013-000000000003";
	/** UUID FlowLogMessage Type Flow */
	public static final String TYPE_FLOW = "0a5c2391-e205-3d9b-0013-000000000001";
	/** UUID FlowLogMessage Type Mapping */
	public static final String TYPE_MAPPING = "0a5c2391-e205-3d9b-0013-000000000002";

	/**
	 * Submit the {@link FlowLogMessageType} to be stored in the database.
	 *
	 * @param log
	 *            the {@link FlowLogMessageType}
	 */
	void submitFlowLogMessage(final FlowLogMessageType log);
}
