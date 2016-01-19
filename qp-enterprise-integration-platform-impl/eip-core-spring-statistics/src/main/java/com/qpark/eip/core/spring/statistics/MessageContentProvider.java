/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics;

import javax.xml.bind.JAXBElement;

import org.springframework.messaging.Message;

/**
 * @author bhausen
 */
public interface MessageContentProvider {
	/**
	 * Get the number of failureTypes.
	 *
	 * @param message
	 *            the {@link Message}, which may be is a ResponseMessage .
	 * @return the number of failures contained in the ResponseMessage or
	 *         <code>0</code>.
	 */
	int getNumberOfFailures(Message<?> message);

	/**
	 * Get the number of returned objects
	 *
	 * @param message
	 *            the {@link Message}
	 * @return 0 if the {@link Message} is no ResponseMessage, otherwise the
	 *         number of returned objects in the ResponseMessage.
	 */
	int getNumberOfReturns(Message<?> message);

	/**
	 * Get the username out of a {@link Message#getPayload()}. If the payload is
	 * a {@link JAXBElement}, try to get it out of the
	 * {@link JAXBElement#getValue()}.
	 *
	 * @param message
	 *            the {@link Message}.
	 * @return the user name or <code>null</code>.
	 */
	String getUserName(Message<?> message);

	/**
	 * Get the username out of the object.
	 *
	 * @param object
	 *            the object containing the user name.
	 * @return the user name or <code>null</code>.
	 */
	String getUserName(Object object);

	/**
	 * <code>true</code> if the {@link Message} contains a SOAP fault, else
	 * <code>false</code>.
	 *
	 * @param message
	 *            the {@link Message}
	 * @return <code>true</code> if the {@link Message} contains a SOAP fault,
	 *         else <code>false</code>.
	 */
	boolean isSoapFaultContaint(Message<?> message);
}
