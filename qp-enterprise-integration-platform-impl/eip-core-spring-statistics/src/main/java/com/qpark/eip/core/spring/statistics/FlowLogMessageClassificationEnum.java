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
 * Flow log message classification enum.
 *
 * @author bhausen
 */
public enum FlowLogMessageClassificationEnum {
	/** Message classification DataIncompatibility */
	DataIncompatibility("0a5c2391-e205-3d9b-0016-000000000003"),
	/** Message classification DataInconsistency */
	DataInconsistency("0a5c2391-e205-3d9b-0016-000000000001"),
	/** Message classification TransferReceipt */
	TransferReceipt("0a5c2391-e205-3d9b-0016-000000000004"),
	/** Message classification ValueNotProvided */
	ValueNotProvided("0a5c2391-e205-3d9b-0016-000000000002");
	/**
	 * @param uuid
	 * @return the {@link FlowLogMessageClassificationEnum}.
	 */
	public static String get(final String uuid) {
		String value = "";
		if (Objects.nonNull(uuid)) {
			for (FlowLogMessageClassificationEnum p : FlowLogMessageClassificationEnum
					.values()) {
				if (p.uuid.equals(uuid)) {
					value = p.name();
				}
			}
		}
		return value;
	}

	/** The UUID. */
	private final String uuid;

	FlowLogMessageClassificationEnum(final String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return this.uuid;
	}
}
