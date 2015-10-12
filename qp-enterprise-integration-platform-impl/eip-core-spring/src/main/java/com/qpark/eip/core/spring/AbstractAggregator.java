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

import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.aggregator.HeaderAttributeCorrelationStrategy;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;

/**
 * @author bhausen
 */
public class AbstractAggregator extends HeaderAttributeCorrelationStrategy {
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
	    m.getHeaders().remove(IntegrationMessageHeaderAccessor.CORRELATION_ID);
	    m.getHeaders().remove(IntegrationMessageHeaderAccessor.SEQUENCE_SIZE);
	}
    }

    /**
     * @param messages
     * @return
     */
    public boolean canRelease(final Collection<Message<?>> messages) {
	if (messages != null && !messages.isEmpty()) {
	    Message<?> m = messages.toArray(new Message<?>[messages.size()])[0];
	    Integer sequenceSize = new IntegrationMessageHeaderAccessor(m).getSequenceSize();
	    if (sequenceSize != null && messages.size() == sequenceSize) {
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
	if (group.getSequenceSize() > 0 && group.getSequenceSize() == group.getMessages().size()) {
	    return true;
	}
	return false;
    }
}
