/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.util;

import java.util.Collection;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.qpark.eip.core.spring.AbstractAggregator;
import com.qpark.eip.service.common.msg.GetReferenceDataResponseType;

/**
 * Aggregate the messages of the get reference data.
 *
 * @author bhausen
 */
public class AggregatorGetReferenceData extends AbstractAggregator {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(AggregatorGetReferenceData.class);

	/**
	 * Aggregate the messages.
	 *
	 * @param messages
	 *            The list of {@link GetReferenceDataResponseType} containing
	 *            messages.
	 * @return One Message with the content of all messages.
	 */
	public Message<?> aggregate(
			final Collection<Message<JAXBElement<GetReferenceDataResponseType>>> messages) {
		this.logger.trace("+aggregate {}", messages != null ? messages.size()
				: " collection of messages is null");
		JAXBElement<GetReferenceDataResponseType> response = null;
		if (messages != null) {
			for (Message<JAXBElement<GetReferenceDataResponseType>> message : messages) {
				if (response == null) {
					response = message.getPayload();
				} else {
					JAXBElement<GetReferenceDataResponseType> payload = message
							.getPayload();
					response.getValue().getReferenceData()
							.addAll(payload.getValue().getReferenceData());
					response.getValue().getFailure()
							.addAll(payload.getValue().getFailure());
				}
			}
		}
		MessageBuilder<JAXBElement<GetReferenceDataResponseType>> m = MessageBuilder
				.withPayload(response);
		this.logger.trace("-aggregate {}", messages != null ? messages.size()
				: " collection of messages is null");
		return m.build();
	}
}
