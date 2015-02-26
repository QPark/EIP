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
package com.samples.platform.core;

import com.qpark.eip.core.EipSettings;

/**
 * @author bhausen
 */
public interface BusSettings extends EipSettings {
	/** The property name for tc server info. */
	public static final String BUS_TC_SERVER_INFO = "bus.tc.server.info";
	/** The property name for tc server info. */
	public static final String BUS_SERVLET_CONTEXT_NAME = "bus.servlet.context.name";
}
