package com.samples.platform.core;

import javax.xml.bind.JAXBElement;

import org.springframework.messaging.Message;

import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.service.common.msg.RequestMessage;

/**
 * The {@link MessageContentProvider}.
 *
 * @author bhausen
 */
public class SamplesMessageContentProvider implements MessageContentProvider {
	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getNumberOfFailures(org.springframework.messaging.Message)
	 */
	@Override
	public int getNumberOfFailures(final Message<?> message) {
		return 0;
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getNumberOfReturns(org.springframework.messaging.Message)
	 */
	@Override
	public int getNumberOfReturns(final Message<?> message) {
		return 0;
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getUserName(org.springframework.messaging.Message)
	 */
	@Override
	public String getUserName(final Message<?> message) {
		Object payload = message.getPayload();
		if (JAXBElement.class.isInstance(payload)) {
			payload = ((JAXBElement<?>) payload).getValue();
		}
		return this.getUserName(payload);
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#getUserName(java.lang.Object)
	 */
	@Override
	public String getUserName(final Object object) {
		String value = "";
		if (RequestMessage.class.isInstance(object)) {
			value = ((RequestMessage) object).getUserName();
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.core.spring.statistics.MessageContentProvider#isSoapFaultContaint(org.springframework.messaging.Message)
	 */
	@Override
	public boolean isSoapFaultContaint(final Message<?> message) {
		return false;
	}
}
