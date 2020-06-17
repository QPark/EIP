/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.EipWsChannelInterceptor;
import com.qpark.eip.core.spring.RequestIdMessageHeaderEnhancer;
import com.qpark.eip.core.spring.security.EipRoleVoter;
import com.qpark.eip.core.spring.statistics.MessageContentProvider;
import com.qpark.eip.core.spring.statistics.StatisticsListener;
import com.qpark.eip.core.spring.statistics.config.EipStatisticsConfig;

/**
 * @author bhausen
 */
public class AppUserStatisticsChannelAdapter implements EipWsChannelInterceptor {
  /** The bean providing the message content. */
  @Autowired()
  @Qualifier("ComQparkEipCoreSpringStatisticsMessageContentProvider")
  private MessageContentProvider messageContentProvider;

  /**
   * @param millis
   * @return the duration in 000:00:00.000 format.
   */
  static String getDuration(final long millis) {
    final String hmss = String.format("%03d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(millis),
        TimeUnit.MILLISECONDS.toMinutes(millis)
            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
        TimeUnit.MILLISECONDS.toSeconds(millis)
            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
        TimeUnit.MILLISECONDS.toMillis(millis)
            - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
    return hmss;
  }

  /** The eip {@link ContextNameProvider}. */
  @Autowired
  @Qualifier(EipStatisticsConfig.CONTEXTNAME_PROVIDER_BEAN_NAME)
  private ContextNameProvider contextNameProvider;

  /** The {@link StatisticsListener}. */
  @Autowired
  private StatisticsListener statisticsListener;
  private String hostName;
  /** The map of requests. */
  private final Map<UUID, SimpleEntry<String, Long>> requestMap =
      new ConcurrentHashMap<UUID, SimpleEntry<String, Long>>();

  /**
   * Get the host name of the server.
   *
   * @return the host name or <code>null</code>.
   */
  String getHostName() {
    if (this.hostName == null) {
      try {
        this.hostName = InetAddress.getLocalHost().getHostName();
      } catch (final UnknownHostException e) {
        e.printStackTrace();
      }
    }
    return this.hostName;
  }

  /**
   * Log the {@link ApplicationUserLogType}.
   *
   * @param log
   *            the {@link ApplicationUserLogType}.
   */
  public static void logApplicationUserLogType(final ApplicationUserLogType log) {
    EipStatisticsConfig.LOGGER_STATISTICS_APP_USER.debug("{}, {}, {}, {}, {}, {}, {}, {}",
        log.getContext(), log.getVersion(), log.getUserName(), log.getServiceName(),
        log.getOperationName(), log.getDurationString(), log.getReturnedEntities(),
        log.getReturnedFailures());
  }

  /**
   * Nothing to do here. Returns the entire {@link Message}.
   *
   * @see org.springframework.messaging.support.ChannelInterceptor#postReceive(org.springframework.messaging.Message,
   *      org.springframework.messaging.MessageChannel)
   */
  @Override
  public Message<?> postReceive(final Message<?> message, final MessageChannel channel) {
    return message;
  }

  /**
   * @see org.springframework.messaging.support.ChannelInterceptor#postSend(org.springframework.messaging.Message,
   *      org.springframework.messaging.MessageChannel, boolean)
   */
  @Override
  public void postSend(final Message<?> message, final MessageChannel channel, final boolean sent) {
    final UUID requestId =
        (UUID) message.getHeaders().get(RequestIdMessageHeaderEnhancer.HEADER_NAME_REQUEST_ID);
    if (channel.toString().contains("Response") && requestId != null) {
      final SimpleEntry<String, Long> entry = this.requestMap.get(requestId);
      if (entry != null) {
        final ApplicationUserLogType log = new ApplicationUserLogType();
        log.setStopItem(new Date());
        log.setDurationMillis(log.getStopItem().getTime() - entry.getValue().longValue());
        log.setStartItem(new Date(entry.getValue().longValue()));
        log.setDurationString(AppUserStatisticsChannelAdapter.getDuration(log.getDurationMillis()));

        log.setContext(this.contextNameProvider.getContextName());
        log.setVersion(this.contextNameProvider.getContextVersion());
        log.setUserName(entry.getKey());
        log.setServiceName(EipRoleVoter.getServiceName(message, ".service.", ".msg."));
        log.setOperationName(EipRoleVoter.getOperationName(message));

        log.setReturnedEntities(this.messageContentProvider.getNumberOfReturns(message));
        log.setReturnedFailures(this.messageContentProvider.getNumberOfFailures(message));

        log.setHostName(this.getHostName());
        if (this.logger.isTraceEnabled()) {
          this.logger.trace("{},{},{},{},{},{},{},{}", log.getContext(), log.getHostName(),
              log.getServiceName(), log.getOperationName(), log.getUserName(),
              log.getReturnedFailures(), log.getDurationString(), log.getStartItem());
        }

        this.submitApplicationUserLogType(log);
      }
    }
  }

  /** The {@link org.slf4j.Logger}. */
  private Logger logger = LoggerFactory.getLogger(AppUserStatisticsChannelAdapter.class);

  /**
   * @see org.springframework.messaging.support.ChannelInterceptor#afterSendCompletion(org.springframework.messaging.Message,
   *      org.springframework.messaging.MessageChannel, boolean,
   *      java.lang.Exception)
   */
  @Override
  public void afterSendCompletion(final Message<?> message, final MessageChannel channel,
      final boolean sent, final Exception ex) {
    // Nothing to do here.
  }

  /**
   * @see org.springframework.messaging.support.ChannelInterceptor#afterReceiveCompletion(org.springframework.messaging.Message,
   *      org.springframework.messaging.MessageChannel, java.lang.Exception)
   */
  @Override
  public void afterReceiveCompletion(final Message<?> message, final MessageChannel channel,
      final Exception ex) {
    // Nothing to do here.
  }

  /**
   * Nothing to do here. Returns <code>true</code>.
   *
   * @see org.springframework.messaging.support.ChannelInterceptor#preReceive(org.springframework.messaging.MessageChannel)
   */
  @Override
  public boolean preReceive(final MessageChannel channel) {
    return true;
  }

  /**
   * Add the request time stamp to the {@link #requestMap}.
   *
   * @see org.springframework.messaging.support.ChannelInterceptor#preSend(org.springframework.messaging.Message,
   *      org.springframework.messaging.MessageChannel)
   */
  @Override
  public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
    if (channel.toString().contains("Request")) {
      UUID requestId =
          (UUID) message.getHeaders().get(RequestIdMessageHeaderEnhancer.HEADER_NAME_REQUEST_ID);
      if (requestId == null) {
        requestId = message.getHeaders().getId();
      }
      this.requestMap.put(requestId, new SimpleEntry<String, Long>(
          this.messageContentProvider.getUserName(message), System.currentTimeMillis()));
    }
    return message;
  }

  /**
   * Submit the {@link ApplicationUserLogType} to be stored in the database
   * and log it with
   * {@link #logApplicationUserLogType(ApplicationUserLogType)}.
   *
   * @param log
   *            the {@link ApplicationUserLogType}
   */
  public void submitApplicationUserLogType(final ApplicationUserLogType log) {
    if (log != null) {
      new Thread(() -> {
        this.statisticsListener.addChannelInvocation(log);
      }).start();
      AppUserStatisticsChannelAdapter.logApplicationUserLogType(log);
    }
  }
}
