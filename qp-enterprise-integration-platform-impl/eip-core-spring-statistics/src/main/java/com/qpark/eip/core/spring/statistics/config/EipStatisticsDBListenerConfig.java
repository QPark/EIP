/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.qpark.eip.core.persistence.config.EipPersistenceConfig;
import com.qpark.eip.core.spring.statistics.StatisticsListener;
import com.qpark.eip.core.spring.statistics.dao.StatisticsEraser;
import com.qpark.eip.core.spring.statistics.dao.StatisticsLoggingDao;

/**
 * Configure the default database implementation of the {@link StatisticsListener}.
 * @author bhausen
 */
@Configuration
@Import({EipPersistenceConfig.class})
@SuppressWarnings("static-method")
public class EipStatisticsDBListenerConfig {
  /** The number of weeks to keep the log entries in the database. */
  private int numberOfWeeksToKeepLogs = 2;

  /**
   * Get the {@link StatisticsLoggingDao} bean.
   *
   * @return the {@link StatisticsLoggingDao} bean.
   */
  @Bean(name = "ComQparkEipCoreSpringStatisticsLoggingDao")
  public StatisticsListener getStatisticsLoggingDao() {
    final StatisticsListener bean = new StatisticsLoggingDao();
    return bean;
  }

  /**
  * Get the {@link StatisticsEraser} bean.
  *
  * @return the {@link StatisticsEraser} bean.
  */
  @Bean
  public StatisticsEraser getSystemUserLogEraser() {
    final StatisticsEraser bean = new StatisticsEraser();
    bean.setNumberOfWeeksToKeepLogs(this.numberOfWeeksToKeepLogs);
    return bean;
  }

  /**
  * Set the number of weeks to keep the log entries in the database.
  *
  * @param numberOfWeeksToKeepLogs
  *            the number of weeks to keep the log entries in the database.
  */
  public void setNumberOfWeeksToKeepLogs(final int numberOfWeeksToKeepLogs) {
    this.numberOfWeeksToKeepLogs = numberOfWeeksToKeepLogs;
  }
}
