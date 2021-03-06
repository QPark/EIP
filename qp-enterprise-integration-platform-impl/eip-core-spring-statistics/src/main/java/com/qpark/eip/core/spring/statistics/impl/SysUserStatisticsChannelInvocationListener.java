/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.security.channel.ChannelInvocation;
import org.springframework.messaging.Message;
import org.springframework.security.access.AccessDecisionVoter;

import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.security.EipChannelInvocationListener;
import com.qpark.eip.core.spring.security.EipRoleVoter;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.core.spring.statistics.StatisticsListener;
import com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig;

/**
 * Listens to spring integration channel invocations.
 *
 * @author bhausen
 */
public class SysUserStatisticsChannelInvocationListener
		implements EipChannelInvocationListener {
	private static final TimeZone LOGGING_TIMEZONE = TimeZone
			.getTimeZone("UTC");

	/**
	 * Get a {@link Date}, where hours, minutes, seconds and milliseconds are
	 * set to 0.
	 *
	 * @return the {@link Date} and the corresponding log string.
	 */
	public static Calendar getRequestDate() {
		final Calendar gc = new GregorianCalendar(LOGGING_TIMEZONE);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc;
	}

	/**
	 * Log the {@link SystemUserLogType}.
	 *
	 * @param log          the {@link SystemUserLogType}.
	 * @param resultString see {@link EipRoleVoter#getResultString(int)}
	 */
	public static void logSystemUserLogType(final SystemUserLogType log,
			final String resultString) {
		if (log != null) {
			EipStatisticsConfig.LOGGER_STATISTICS_SYS_USER.debug(
					"{}, {}, {}, {}, {}, {}", log.getContext(),
					log.getVersion(), log.getUserName(), log.getServiceName(),
					log.getOperationName(), resultString);
		}
	}

	/** The eip {@link ContextNameProvider}. */
	@Autowired
	@Qualifier(EipStatisticsConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
	private ContextNameProvider contextNameProvider;
	/** The {@link StatisticsListener}. */
	@Autowired
	private StatisticsListener statisticsListener;
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(SysUserStatisticsChannelInvocationListener.class);
	/** The bean providing the message content. */
	@Autowired
	@Qualifier("ComQparkEipCoreSpringStatisticsMessageContentProvider")
	private MessageContentProvider messageContentProvider;

	/**
	 * @see com.qpark.eip.core.spring.security.EipChannelInvocationListener#channelInvocation(org.springframework.integration.security.channel.ChannelInvocation,
	 *      java.lang.String, int)
	 */
	@Override
	public void channelInvocation(final ChannelInvocation channel,
			final String userName, final int result) {
		final String channelName = EipRoleVoter.getChannelName(channel);
		final String serviceName = EipRoleVoter.getServiceName(channel,
				".service.", ".msg.");
		final String operationName = EipRoleVoter.getOperationName(channel);
		if (serviceName != null && serviceName.trim().length() > 0
				&& operationName != null && operationName.trim().length() > 0) {
			final SystemUserLogType log = new SystemUserLogType();
			log.setUserName(userName);
			log.setServiceName(serviceName);
			log.setOperationName(operationName);
			log.setLogDateItem(SysUserStatisticsChannelInvocationListener
					.getRequestDate().getTime());
			log.setContext(this.contextNameProvider.getContextName());
			log.setVersion(this.contextNameProvider.getContextVersion());
			if (result == AccessDecisionVoter.ACCESS_DENIED) {
				log.setRequestsDenied(1);
			} else if (channelName.contains("WsChannelRequest")) {
				log.setRequestsGranted(1);
			} else if (channelName.contains("WsChannelResponse")
					&& this.messageContainsSoapFault(channel)) {
				log.setResponseFaults(1);
			}
			if (this.logger.isTraceEnabled()) {
				this.logger.trace("{},{},{},{},{},{},{},{}", log.getContext(),
						log.getServiceName(), log.getOperationName(),
						log.getUserName(), log.getRequestsDenied(),
						log.getRequestsGranted(), log.getResponseFaults(),
						log.getLogDateItem());
			}

			this.submitSystemUserLogType(channelName, log,
					EipRoleVoter.getResultString(result));
		}
	}

	/**
	 * Verifies if the channel response contains a SOAP fault.
	 *
	 * @param channel the {@link ChannelInvocation}.
	 * @return <code>true</code> or <code>false</code>.
	 */
	private boolean messageContainsSoapFault(final ChannelInvocation channel) {
		boolean soapFault = false;
		final Message<?> m = EipRoleVoter.getMessage(channel);
		if (m != null) {
			soapFault = this.messageContentProvider.isSoapFaultContaint(m);
		}
		return soapFault;
	}

	/**
	 * Submit the {@link SystemUserLogType} to be stored in the database and log
	 * it with {@link #logSystemUserLogType(SystemUserLogType, String)}.
	 *
	 * @param log          the {@link SystemUserLogType}
	 * @param resultString see {@link EipRoleVoter#getResultString(int)}
	 */
	public void submitSystemUserLogType(final String channelName,
			final SystemUserLogType log, final String resultString) {
		if (log != null) {
			new Thread(() -> {
				this.statisticsListener.addChannelInvocation(channelName, log);
			}).start();
			SysUserStatisticsChannelInvocationListener.logSystemUserLogType(log,
					resultString);
		}
	}
}
