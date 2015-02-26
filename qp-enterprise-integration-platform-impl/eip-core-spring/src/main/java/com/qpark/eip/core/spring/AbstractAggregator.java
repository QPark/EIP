/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core.spring;

import java.util.Collection;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.aggregator.HeaderAttributeCorrelationStrategy;
import org.springframework.integration.store.MessageGroup;

/**
 * @author bhausen
 */
public class AbstractAggregator extends HeaderAttributeCorrelationStrategy {
	/**
	 * @param attributeName
	 */
	public AbstractAggregator() {
		super(MessageHeaders.CORRELATION_ID);
	}

	/**
	 * Remove the header values of {@link MessageHeaders#CORRELATION_ID} and
	 * {@link MessageHeaders#SEQUENCE_SIZE}.
	 * @param m the Message
	 */
	protected static void removeAddedHeader(final Message<?> m) {
		if (m != null) {
			m.getHeaders().remove(MessageHeaders.CORRELATION_ID);
			m.getHeaders().remove(MessageHeaders.SEQUENCE_SIZE);
		}
	}

	/**
	 * @param messages
	 * @return
	 */
	public boolean canRelease(final Collection<Message<?>> messages) {
		if (messages != null && !messages.isEmpty()) {
			Message<?> m = messages.toArray(new Message<?>[messages.size()])[0];
			if (messages.size() == m.getHeaders().getSequenceSize()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param group
	 * @return
	 */
	public boolean canRelease(final MessageGroup group) {
		if (group.getSequenceSize() > 0
				&& group.getSequenceSize() == group.getMessages().size()) {
			return true;
		}
		return false;
	}
}
