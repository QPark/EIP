/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics;

import com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType;
import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;

/**
 * Listening for statistics entries.
 *
 * @author bhausen
 */
public interface StatisticsListener {
	/**
	 * Add the {@link ApplicationUserLogType} to the database.
	 *
	 * @param channelName the name of the channel invoked
	 * @param log         the {@link ApplicationUserLogType} to add.
	 */
	void addChannelInvocation(String channelName, ApplicationUserLogType log);

	/**
	 * Add the {@link SystemUserLogType} to the database.
	 *
	 * @param channelName the name of the channel invoked
	 * @param log         the {@link SystemUserLogType} to add.
	 */
	void addChannelInvocation(String channelName, SystemUserLogType log);

	/**
	 * Add the {@link FlowLogMessageType} to the database.
	 *
	 * @param log the {@link FlowLogMessageType} to add.
	 */
	void addFlowLogMessage(FlowLogMessageType log);
}
