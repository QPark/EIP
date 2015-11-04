/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.security.channel.ChannelInvocation;
import org.springframework.messaging.Message;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Checks if the limit of calls of the user is reached or not.
 *
 * @author bhausen
 */
public class EipRoleVoter extends RoleVoter {
	/**
	 * Get the channel name of the object is an instance of the
	 * {@link ChannelInvocation}.
	 *
	 * @param object
	 *            the object to get the channel name from.
	 * @return the channel name or <code>null</code>.
	 */
	public static String getChannelName(final Object object) {
		String channelName = String.valueOf(object);
		if (ChannelInvocation.class.isInstance(object)) {
			channelName = ((ChannelInvocation) object).getChannel().toString();
		}
		return channelName;
	}

	/**
	 * Get the {@link Message} of the object is an instance of the
	 * {@link ChannelInvocation}.
	 *
	 * @param object
	 *            the object to get the channel name from.
	 * @return the {@link Message} or <code>null</code>.
	 */
	public static Message<?> getMessage(final Object object) {
		Message<?> message = null;
		if (ChannelInvocation.class.isInstance(object)) {
			message = ((ChannelInvocation) object).getMessage();
		}
		return message;

	}

	/**
	 * Get the name of the operation out of the channel name.
	 *
	 * @param channelName
	 *            the channel name.
	 * @param serviceName
	 *            the service name.
	 * @return the operation name.
	 */
	public static String getOperationName(final String channelName, final String serviceName) {
		StringBuffer operation = new StringBuffer();
		char ch;
		int l = channelName.length();
		int j = -1;
		if (channelName.contains("WsChannelRequest")) {
			j = "WsChannelRequest".length();
		} else if (channelName.contains("WsChannelResponse")) {
			j = "WsChannelResponse".length();
		}
		if (j > 0) {
			for (int i = "eip".length() + serviceName.replace(".", "").length(); i < l - j; i++) {
				ch = channelName.charAt(i);
				operation.append(ch);
			}
		}
		return operation.toString();
	}

	/**
	 * Get the service name by using the class name of the
	 * {@link ChannelInvocation#getMessage()} payload
	 *
	 * @param object
	 *            the object to get the message from.
	 * @param serviceIdentifierStart
	 *            e.g. <code>".service."</code>
	 * @param serviceIdentifierEnd
	 *            e.g. <code>".msg"</code>
	 * @return the service name.
	 */
	public static String getOperationName(final Object object) {
		String operationName = "";
		if (ChannelInvocation.class.isInstance(object)) {
			Message<?> m = EipRoleVoter.getMessage(object);
			operationName = getOperationName(m);
		}
		return operationName;
	}

	/**
	 * Get the service name by using the class name of the {@link Message}
	 * payload
	 *
	 * @param m
	 *            the {@link Message}.
	 * @return the service name.
	 */
	public static String getOperationName(final Message<?> m) {
		String operationName = "";
		if (m != null && m.getPayload() != null && JAXBElement.class.isInstance(m.getPayload())
				&& ((JAXBElement<?>) m.getPayload()).getValue() != null) {
			String className = ((JAXBElement<?>) m.getPayload()).getValue().getClass().getSimpleName();
			if (className.endsWith("ResponseType")) {
				operationName = className.substring(0, className.length() - 12);
			} else if (className.endsWith("RequestType")) {
				operationName = className.substring(0, className.length() - 11);
			}
		}
		return operationName;
	}

	/**
	 * Get the string of the result.
	 *
	 * @param result
	 *            the result.
	 * @return the string.
	 */
	public static String getResultString(final int result) {
		String s = "not defined";
		if (result == ACCESS_ABSTAIN) {
			s = "abstained";
		} else if (result == ACCESS_DENIED) {
			s = "denied";
		} else if (result == ACCESS_GRANTED) {
			s = "granted";
		}
		return s;
	}

	/**
	 * Get the name of the service out of the channel name.
	 *
	 * @param channelName
	 *            the channel name.
	 * @return the service name.
	 */
	public static String getServiceName(final String channelName) {
		StringBuffer service = new StringBuffer();
		if (channelName.startsWith("eip")) {
			service.append(Character.toLowerCase(channelName.charAt("eip".length())));
			char ch;
			int l = channelName.length();
			for (int i = "eip".length() + 1; i < l; i++) {
				ch = channelName.charAt(i);
				if (Character.isUpperCase(ch)) {
					break;
				} else {
					service.append(ch);
				}
			}
		}
		return service.toString();
	}

	/**
	 * Get the service name by using the class name of the
	 * {@link ChannelInvocation#getMessage()} payload
	 *
	 * @param object
	 *            the object to get the message from.
	 * @param serviceIdentifierStart
	 *            e.g. <code>".service."</code>
	 * @param serviceIdentifierEnd
	 *            e.g. <code>".msg"</code>
	 * @return the service name.
	 */
	public static String getServiceName(final Object object, final String serviceIdentifierStart,
			final String serviceIdentifierEnd) {
		String serviceName = "";
		if (ChannelInvocation.class.isInstance(object)) {
			Message<?> m = EipRoleVoter.getMessage(object);
			serviceName = getServiceName(m, serviceIdentifierStart, serviceIdentifierEnd);
		}
		return serviceName;
	}

	/**
	 * Get the service name by using the class name of the {@link Message}
	 * payload
	 *
	 * @param m
	 *            the {@link Message}.
	 * @param serviceIdentifierStart
	 *            e.g. <code>".service."</code>
	 * @param serviceIdentifierEnd
	 *            e.g. <code>".msg"</code>
	 * @return the service name.
	 */
	public static String getServiceName(final Message<?> m, final String serviceIdentifierStart,
			final String serviceIdentifierEnd) {
		String serviceName = "";
		if (m != null && m.getPayload() != null && JAXBElement.class.isInstance(m.getPayload())
				&& ((JAXBElement<?>) m.getPayload()).getValue() != null) {
			String className = ((JAXBElement<?>) m.getPayload()).getValue().getClass().getName();
			int serviceIndex = className.indexOf(serviceIdentifierStart);
			int msgIndex = className.indexOf(serviceIdentifierEnd);
			if (serviceIndex >= 0 && msgIndex > 0 && serviceIndex + serviceIdentifierStart.length() < msgIndex) {
				serviceName = className.substring(serviceIndex + serviceIdentifierStart.length(), msgIndex);
			}
		}
		return serviceName;
	}

	/** The {@link EipLimitedAccessDataProvider}. */
	private EipLimitedAccessDataProvider eipLimitedAccessDataProvider;
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory.getLogger(EipRoleVoter.class);

	/**
	 * @param eipLimitedAccessDataProvider
	 *            the eipLimitedAccessDataProvider to set
	 */
	public void setEipLimitedAccessDataProvider(final EipLimitedAccessDataProvider eipLimitedAccessDataProvider) {
		this.eipLimitedAccessDataProvider = eipLimitedAccessDataProvider;
	}

	public String getRequiredRoles(final Collection<ConfigAttribute> attributes) {
		/* Handle logging of required roles. */
		TreeSet<String> ts = new TreeSet<String>();
		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				ts.add(attribute.toString());
			}
		}
		StringBuffer sb = new StringBuffer(1024);
		for (String string : ts) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(string);
		}
		return sb.toString();
	}

	public String getGrantedRoles(final Authentication authentication) {
		TreeSet<String> ts = new TreeSet<String>();
		StringBuffer sb = new StringBuffer(1024);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			ts.add(authority.getAuthority());
		}
		for (String string : ts) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(string);
		}

		return sb.toString();
	}

	private void traceRoleSettings(final Authentication authentication, final String channelName,
			final Collection<ConfigAttribute> attributes) {
		this.logger.trace(" vote {} {}: required  [{}]", channelName, authentication.getName(),
				this.getRequiredRoles(attributes));
		this.logger.trace(" vote {} {}: userroles [{}]", channelName, authentication.getName(),
				this.getGrantedRoles(authentication));
	}

	/** The list of all {@link EipChannelInvocationListener}s. */
	@Autowired(required = false)
	private List<EipChannelInvocationListener> channelListeners;

	/** Inform all {@link EipChannelInvocationListener} about the invocation. */
	private void channelInvocation(final Object object, final String userName, final int result) {
		if (ChannelInvocation.class.isInstance(object) && this.channelListeners != null) {
			for (EipChannelInvocationListener listener : this.channelListeners) {
				listener.channelInvocation(((ChannelInvocation) object), userName, result);
			}
		}
	}

	/**
	 * @see org.springframework.security.access.vote.RoleVoter#vote(org.springframework.security.core.Authentication,
	 *      java.lang.Object, java.util.Collection)
	 */
	@Override
	public int vote(final Authentication authentication, final Object object,
			final Collection<ConfigAttribute> attributes) {
		String channelName = getChannelName(object);
		this.logger.debug("+vote {} {}", channelName, authentication.getName());
		if (this.logger.isTraceEnabled()) {
			this.traceRoleSettings(authentication, channelName, attributes);
		}
		int result = super.vote(authentication, object, attributes);
		this.logger.debug(" vote {} {}: Role based vote is {}", channelName, authentication.getName(),
				getResultString(result));
		if (this.eipLimitedAccessDataProvider != null && (result == ACCESS_ABSTAIN || result == ACCESS_GRANTED)
				&& channelName != null && channelName.startsWith("eip") && channelName.endsWith("WsChannelRequest")) {
			String userName = authentication.getName();
			String serviceName = getServiceName(channelName);
			String operationName = getOperationName(channelName, serviceName);
			int currentCalls = this.eipLimitedAccessDataProvider.getCurrentRequestNumber(userName, serviceName,
					operationName);
			int allowedCalls = this.eipLimitedAccessDataProvider.getAllowedRequestNumber(userName, serviceName,
					operationName);
			this.logger.trace(" vote {} {}: current calls={}, allowed calls={}", channelName, authentication.getName(),
					currentCalls, allowedCalls);
			if (currentCalls > allowedCalls) {
				result = ACCESS_DENIED;
			} else {
				result = ACCESS_GRANTED;
			}
			this.logger.debug(" vote {} {}: Limited access based vote is {}", channelName, authentication.getName(),
					getResultString(result));
		}
		/* Inform listeners. */
		this.channelInvocation(object, authentication.getName(), result);
		return result;
	}
}
