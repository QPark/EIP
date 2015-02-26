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

import javax.xml.bind.JAXBElement;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

/**
 * Get the payload value out of the {@link Message} containing a {@link JAXBElement}.
 * @author bhausen
 */
public class JAXBElementValueGetterTransformer<T> {
	/**
	 * @param message
	 * @return The value out of the {@link JAXBElement}
	 */
	@SuppressWarnings("unchecked")
	public Message<? extends T> transform(final Message<JAXBElement<?>> message) {
		JAXBElement<?> payload = message.getPayload();
		return MessageBuilder.withPayload((T) payload.getValue())
				.copyHeaders(message.getHeaders()).build();
	}
}
