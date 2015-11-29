/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring;

import org.springframework.messaging.support.ChannelInterceptor;

/**
 * Interface of {@link ChannelInterceptor}s to be added to the EIP web service
 * request and response channels.
 *
 * @author bhausen
 */
public interface EipWsChannelInterceptor extends ChannelInterceptor {
}
