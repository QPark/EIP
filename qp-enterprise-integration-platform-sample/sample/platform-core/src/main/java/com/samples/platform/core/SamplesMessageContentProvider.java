/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core;

import javax.xml.bind.JAXBElement;

import org.springframework.messaging.Message;

import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.service.base.msg.RequestMessage;
import com.qpark.eip.service.base.msg.ResponseMessage;

/**
 * The {@link MessageContentProvider}.
 *
 * @author bhausen
 */
public class SamplesMessageContentProvider implements MessageContentProvider {
	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getNumberOfFailures(org.springframework.messaging.Message)
	 */
	@Override
	public int getNumberOfFailures(final Message<?> message) {
		int numberOfFailures = 0;
		Object payload = message.getPayload();
		if (JAXBElement.class.isInstance(payload)) {
			payload = ((JAXBElement<?>) payload).getValue();
		}
		if (ResponseMessage.class.isInstance(payload)) {
			numberOfFailures = ((ResponseMessage) payload).getFailure().size();
		}
		return numberOfFailures;
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getNumberOfReturns(org.springframework.messaging.Message)
	 */
	@Override
	public int getNumberOfReturns(final Message<?> message) {
		return -1;
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getUserName(org.springframework.messaging.Message)
	 */
	@Override
	public String getUserName(final Message<?> message) {
		Object payload = message.getPayload();
		if (JAXBElement.class.isInstance(payload)) {
			payload = ((JAXBElement<?>) payload).getValue();
		}
		return this.getUserName(payload);
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getUserName(java.lang.Object)
	 */
	@Override
	public String getUserName(final Object object) {
		String value = "";
		if (RequestMessage.class.isInstance(object)) {
			value = ((RequestMessage) object).getUserName();
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#isSoapFaultContaint(org.springframework.messaging.Message)
	 */
	@Override
	public boolean isSoapFaultContaint(final Message<?> message) {
		return false;
	}
}
