package com.qpark.eip.core.spring.security;

import org.springframework.integration.security.channel.ChannelInvocation;

/**
 * @author bhausen
 */
public interface EipChannelInvocationListener {
    void channelInvocation(ChannelInvocation channel, String userName, int result);
}
