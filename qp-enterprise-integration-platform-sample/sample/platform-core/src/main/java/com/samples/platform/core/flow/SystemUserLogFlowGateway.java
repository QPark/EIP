/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.flow;

import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;
import com.qpark.eip.inf.FlowGateway;
import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogCriteriaType;
import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogRequestType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogResponseType;

/**
 * The gateway to get the {@link SystemUserLogType}s out of the database.
 *
 * @author bhausen
 */
public interface SystemUserLogFlowGateway extends FlowGateway {
	/**
	 * Get the {@link SystemUserLogType} out of the database.
	 *
	 * @param request
	 *            the {@link SystemUserLogRequestType} containing the
	 *            {@link ExtSystemUserLogCriteriaType}.
	 * @return the {@link SystemUserLogResponseType} containing
	 *         {@link ExtSystemUserLogType}
	 */
	SystemUserLogResponseType getSystemUserLog(
			SystemUserLogRequestType request);
}
