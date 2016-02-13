/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType;
import com.qpark.eip.core.spring.statistics.dao.StatisticsLoggingDao;
import com.samples.platform.model.iss.tech.support.OperationReportType;

/**
 * Provides {@link OperationReportType}s.
 *
 * @author bhausen
 */
public class OperationLogProvider {
    /** The {@link org.slf4j.Logger}. */
    private Logger logger = LoggerFactory.getLogger(OperationLogProvider.class);
    /** The {@link StatisticsLoggingDao}. */
    @Autowired
    private StatisticsLoggingDao dao;

    /**
     * Get the list of {@link OperationReportType}s of the day.
     *
     * @param d
     *            the requested day.
     * @return the list of {@link OperationReportType}s of the day.
     */
    public List<OperationReportType> getOperationReport(final Date d) {
	this.logger.debug("+getOperationReport {}", d);
	List<OperationReportType> value = new ArrayList<OperationReportType>();
	List<ApplicationUserLogType> list = this.dao.getApplicationUserLogType(d);
	this.logger.debug(" getOperationReport found {} entries", list.size());
	OperationReportType or;
	for (ApplicationUserLogType app : list) {
	    or = new OperationReportType();
	    or.setServingHost(app.getHostName());
	    or.setConsumerName(app.getUserName());
	    or.setDate(DateUtil.get(app.getStartItem()));
	    or.setDuration(app.getDurationString());
	    or.setOperation(app.getOperationName());
	    or.setService(app.getServiceName());
	    or.setVersion(app.getVersion());
	}
	this.logger.debug("-getOperationReport {}", d);
	return value;
    }
}
