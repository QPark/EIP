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
 * Flow log message type enum.
 *
 * @author bhausen
 */
public enum FlowLogMessageTypeEnum {
	/** Message type Attribut */
	Attribute("0a5c2391-e205-3d9b-0013-000000000004"),
	/** Message type Entity */
	Entity("0a5c2391-e205-3d9b-0013-000000000003"),
	/** Message type Flow */
	Flow("0a5c2391-e205-3d9b-0013-000000000001"),
	/** Message type Mapping */
	Mapping("0a5c2391-e205-3d9b-0013-000000000002");
	/**
	 * @param uuid
	 * @return the {@link FlowLogMessageTypeEnum}.
	 */
	public static String get(final String uuid) {
		String value = "";
		if (Objects.nonNull(uuid)) {
			for (FlowLogMessageTypeEnum p : FlowLogMessageTypeEnum.values()) {
				if (p.uuid.equals(uuid)) {
					value = p.name();
				}
			}
		}
		return value;
	}

	/** The UUID. */
	private final String uuid;

	FlowLogMessageTypeEnum(final String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return this.uuid;
	}
}
