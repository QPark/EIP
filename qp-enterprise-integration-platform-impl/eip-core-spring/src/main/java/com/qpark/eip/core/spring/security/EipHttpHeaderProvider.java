/*******************************************************************************
 * Copyright (c) 2013, 2023 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import java.net.URL;
import java.util.Map;

/**
 *
 * @author bhausen
 */
public interface EipHttpHeaderProvider {
	/**
	 * Get the {@link Map} of header key and values.
	 * 
	 * @param url the {@link URL} of the request.
	 * @return the {@link Map} of headers.
	 */
	Map<String, String> getHttpHeaders(URL url);
}
