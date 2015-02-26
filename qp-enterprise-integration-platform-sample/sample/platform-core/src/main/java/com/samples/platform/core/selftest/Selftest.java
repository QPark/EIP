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
package com.samples.platform.core.selftest;

import org.springframework.integration.Message;

/**
 * @author bhausen
 */
public interface Selftest {
	/**
	 * Do the self test.
	 * @param message
	 * @return HTML.
	 */
	String selftest(Message<?> message);
}
