/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import org.springframework.integration.security.channel.ChannelInvocation;

/**
 * @author bhausen
 */
public interface EipChannelInvocationListener {
    void channelInvocation(ChannelInvocation channel, String userName, int result);
}
