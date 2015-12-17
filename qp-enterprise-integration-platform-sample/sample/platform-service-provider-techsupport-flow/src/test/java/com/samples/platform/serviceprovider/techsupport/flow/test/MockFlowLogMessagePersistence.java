/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.techsupport.flow.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.spring.statistics.AsyncFlowLogMessagePersistence;

public class MockFlowLogMessagePersistence
		implements AsyncFlowLogMessagePersistence {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(MockFlowLogMessagePersistence.class);

	@Override
	public void submitFlowLogMessage(final FlowLogMessageType log) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			this.logger.debug(mapper.writeValueAsString(log));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.logger.error(e.getMessage(), e);
		}
	}
}
