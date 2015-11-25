/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core.spring;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.aggregator.HeaderAttributeCorrelationStrategy;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;

/**
 * @author bhausen
 */
public class AbstractAggregator extends HeaderAttributeCorrelationStrategy {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(AbstractAggregator.class);

	/**
	 * @param attributeName
	 */
	public AbstractAggregator() {
		super(IntegrationMessageHeaderAccessor.CORRELATION_ID);
	}

	/**
	 * Remove the header values of
	 * {@link IntegrationMessageHeaderAccessor#CORRELATION_ID} and
	 * {@link IntegrationMessageHeaderAccessor#SEQUENCE_SIZE}.
	 *
	 * @param m
	 *            the Message
	 */
	protected static void removeAddedHeader(final Message<?> m) {
		if (m != null) {
			m.getHeaders()
					.remove(IntegrationMessageHeaderAccessor.CORRELATION_ID);
			m.getHeaders()
					.remove(IntegrationMessageHeaderAccessor.SEQUENCE_SIZE);
		}
	}

	/**
	 * @param messages
	 * @return
	 */
	public boolean canRelease(final Collection<Message<?>> messages) {
		boolean canRelease = false;
		this.logger.trace("-canRelease collection {}", messages != null
				? messages.size() : " collection of messages is null");
		if (messages != null && !messages.isEmpty()) {
			Message<?> m = messages.toArray(new Message<?>[messages.size()])[0];
			Integer sequenceSize = new IntegrationMessageHeaderAccessor(m)
					.getSequenceSize();
			this.logger.trace(
					" canRelease collection messages.size()={}, sequenceSize={}",
					messages.size(), sequenceSize);
			if (sequenceSize != null && messages.size() == sequenceSize) {
				canRelease = true;
			}
		}
		this.logger.trace("-canRelease collection canRelease={}", canRelease);
		return canRelease;
	}

	/**
	 * @param group
	 * @return
	 */
	public boolean canRelease(final MessageGroup group) {
		boolean canRelease = false;
		this.logger.trace("+canRelease group messages.size={}, sequenceSize={}",
				group.getMessages().size(), group.getSequenceSize());
		if (group.getSequenceSize() > 0
				&& group.getSequenceSize() == group.getMessages().size()) {
			canRelease = true;
		}
		this.logger.trace("-canRelease group canRelease={}", canRelease);
		return canRelease;
	}
}
