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

import org.springframework.integration.annotation.Header;

/**
 * @author bhausen
 */
public interface SelftestGateway {
	/**
	 * @param serviceName the service name to call.
	 * @param message
	 * @return
	 */
	Object invoke(@Header("serviceName") String serviceName, Object message);

	/**
	 * @param message
	 * @return
	 */
	Object invoke(Object message);
}
