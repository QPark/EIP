/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.spring.statistics.AsyncFlowLogMessagePersistence;
import com.qpark.eip.core.spring.statistics.StatisticsListener;

/**
 * @author bhausen
 */
public class AsyncFlowLogMessagePersistenceImpl implements AsyncFlowLogMessagePersistence {
  /** The {@link StatisticsListener}. */
  @Autowired
  private StatisticsListener statisticsListener;
  /** The {@link org.slf4j.Logger}. */
  private final Logger logger = LoggerFactory.getLogger(AsyncFlowLogMessagePersistenceImpl.class);

  /**
   * @see com.qpark.eip.core.spring.statistics.AsyncFlowLogMessagePersistence#submitFlowLogMessage(com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType)
   */
  @Override
  public void submitFlowLogMessage(final FlowLogMessageType log) {
    if (log != null) {
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("{} {}", log.getFlowName(),
            log.getDataDescription() != null
                ? log.getDataDescription().replaceAll("\\\\n", " ").replaceAll("\\\\t", " ")
                : "");
      }
      new Thread(() -> {
        this.statisticsListener.addFlowLogMessage(log);
      }).start();
    }
  }
}
