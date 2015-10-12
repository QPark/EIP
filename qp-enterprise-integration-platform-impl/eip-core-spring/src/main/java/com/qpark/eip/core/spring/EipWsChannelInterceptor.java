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
