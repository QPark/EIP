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
import java.util.Optional;

/**
 * Flow log message severity enum.
 *
 * @author bhausen
 */
public enum FlowLogMessageSeverityEnum {
	/** Message severity Error */
	Error("0a5c2391-e205-3d9b-0014-000000000003"),
	/** Message severity Information */
	Information("0a5c2391-e205-3d9b-0014-000000000001"),
	/** Message severity Warning */
	Warning("0a5c2391-e205-3d9b-0014-000000000002");
	/**
	 * @param uuid
	 * @return the {@link FlowLogMessageSeverityEnum}.
	 */
	public static String get(final String uuid) {
		String value = "";
		if (Objects.nonNull(uuid)) {
			for (FlowLogMessageSeverityEnum p : FlowLogMessageSeverityEnum
					.values()) {
				if (p.uuid.equals(uuid)) {
					value = p.name();
				}
			}
		}
		return value;
	}

	/**
	 * @param uuid
	 * @return the {@link FlowLogMessageSeverityEnum}.
	 */
	public static Optional<FlowLogMessageSeverityEnum> getEnum(
			final String uuid) {
		FlowLogMessageSeverityEnum value = null;
		if (Objects.nonNull(uuid)) {
			for (FlowLogMessageSeverityEnum p : FlowLogMessageSeverityEnum
					.values()) {
				if (p.uuid.equals(uuid)) {
					value = p;
				}
			}
		}
		return Optional.ofNullable(value);
	}

	/** The UUID. */
	private final String uuid;

	FlowLogMessageSeverityEnum(final String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return this.uuid;
	}
}
