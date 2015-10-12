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

import org.springframework.messaging.Message;

/**
 * This spring integration transformer just replies the {@link Message}.
 * 
 * @author bhausen
 */
public class NoOperationTransformer {
    /**
     * @param message
     *            The {@link Message}.
     * @return The not changed {@link Message}.
     */
    public Message<?> transform(final Message<?> message) {
	return message;
    }
}
