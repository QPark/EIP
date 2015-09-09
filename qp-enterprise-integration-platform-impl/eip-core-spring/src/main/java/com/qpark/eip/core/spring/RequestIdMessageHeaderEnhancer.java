package com.qpark.eip.core.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

/**
 * Each message send should have its {@link Message#getHeaders()#getId()}
 * additionally in the header. With that header on the response the request
 * could be identified.
 * @author bhausen
 */
public class RequestIdMessageHeaderEnhancer implements EipWsChannelInterceptor {
	/** The name of the header field containing the requests message id. */
	public static final String HEADER_NAME_REQUEST_ID = RequestIdMessageHeaderEnhancer.class
			.getName();

	/**
	 * Enhance the message header with name of this class.
	 * @see org.springframework.integration.channel.ChannelInterceptor#preSend(org.springframework.integration.Message,
	 *      org.springframework.integration.MessageChannel)
	 */
	@Override
	public Message<?> preSend(final Message<?> message,
			final MessageChannel channel) {
		Message<?> msg = null;
		if (message.getHeaders().get(HEADER_NAME_REQUEST_ID) == null) {
			Map<String, Object> headerMap = new HashMap<String, Object>(
					message.getHeaders());
			headerMap.put(HEADER_NAME_REQUEST_ID, message.getHeaders().getId());
			msg = MessageBuilder.withPayload(message.getPayload())
					.copyHeaders(headerMap).build();
		} else {
			msg = message;
		}
		return msg;
	}

	/**
	 * @see org.springframework.integration.channel.ChannelInterceptor#postSend(org.springframework.integration.Message,
	 *      org.springframework.integration.MessageChannel, boolean)
	 */
	@Override
	public void postSend(final Message<?> message,
			final MessageChannel channel, final boolean sent) {
		/* Nothing to do here. */
	}

	/**
	 * Noting to do before receiving a message.
	 * @see org.springframework.integration.channel.ChannelInterceptor#preReceive(org.springframework.integration.MessageChannel)
	 */
	@Override
	public boolean preReceive(final MessageChannel channel) {
		return true;
	}

	/**
	 * @see org.springframework.integration.channel.ChannelInterceptor#postReceive(org.springframework.integration.Message,
	 *      org.springframework.integration.MessageChannel)
	 */
	@Override
	public Message<?> postReceive(final Message<?> message,
			final MessageChannel channel) {
		return message;
	}
}
