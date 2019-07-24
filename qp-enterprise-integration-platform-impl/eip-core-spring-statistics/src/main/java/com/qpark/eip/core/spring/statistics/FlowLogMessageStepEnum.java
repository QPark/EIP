/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics;

import java.util.Objects;

/**
 * Flow log message step enum.
 *
 * @author bhausen
 */
public enum FlowLogMessageStepEnum {
	/** Message step AttributeMapping */
	AttributeMapping("0a5c2391-e205-3d9b-0015-000000000006"),
	/** Message step EntityMapping */
	EntityMapping("0a5c2391-e205-3d9b-0015-000000000005"),
	/** Message step FlowGatewayExecution */
	FlowGatewayExecution("0a5c2391-e205-3d9b-0015-000000000007"),
	/** Message step RequestExecution */
	RequestExecution("0a5c2391-e205-3d9b-0015-000000000001"),
	/** Message step RequestMapping */
	RequestMapping("0a5c2391-e205-3d9b-0015-000000000003"),
	/** Message step ResponseMapping */
	ResponseMapping("0a5c2391-e205-3d9b-0015-000000000004"),
	/** Message step ResponseProcessing */
	ResponseProcessing("0a5c2391-e205-3d9b-0015-000000000002");
	/**
	 * @param uuid
	 * @return the {@link FlowLogMessageStepEnum}.
	 */
	public static String get(final String uuid) {
		String value = "";
		if (Objects.nonNull(uuid)) {
			for (FlowLogMessageStepEnum p : FlowLogMessageStepEnum.values()) {
				if (p.uuid.equals(uuid)) {
					value = p.name();
				}
			}
		}
		return value;
	}

	/** The UUID. */
	private final String uuid;

	FlowLogMessageStepEnum(final String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return this.uuid;
	}
}
