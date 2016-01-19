/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth;

import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.auth.dao.AuthorityDao;
import com.qpark.eip.core.spring.security.EipLimitedAccessDataProvider;

/**
 * Get the number of requested calls and the number of allowed calls of a user
 * for a specific operation.
 * <p/>
 * The actual implementation returns always {@link Integer#MAX_VALUE} as the
 * number of allowed calls .
 *
 * @author bhausen
 */
public class LimitedAccessDataProvider implements EipLimitedAccessDataProvider {
	/** The eip {@link ContextNameProvider}. */
	@Autowired
	@Qualifier("ComQparkEipCoreSpringAuthContextNameProvider")
	private ContextNameProvider contextNameProvider;
	/** The {@link BusUtilDao}. */
	@Autowired
	private AuthorityDao authorityDao;
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(LimitedAccessDataProvider.class);

	/**
	 * Get a {@link Date}, where hours, minutes, seconds and milliseconds are
	 * set to 0.
	 *
	 * @return the {@link Date} and the corresponding log string.
	 */
	private static SimpleEntry<Date, String> getRequestDate() {
		Calendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		String hmss = String.format("%04d%02d%02d", gc.get(Calendar.YEAR),
				gc.get(Calendar.MONTH) + 1, gc.get(Calendar.DAY_OF_MONTH));
		SimpleEntry<Date, String> entry = new SimpleEntry<Date, String>(
				gc.getTime(), hmss);
		return entry;
	}

	/**
	 * @see com.qpark.eip.core.spring.security.EipLimitedAccessDataProvider#getAllowedRequestNumber(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public int getAllowedRequestNumber(final String userName,
			final String serviceName, final String operationName) {
		int calls = this.authorityDao.getGrantedRequestNumber(userName,
				serviceName, operationName);
		return calls;
	}

	/**
	 * @see com.qpark.eip.core.spring.security.EipLimitedAccessDataProvider#getCurrentRequestNumber(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public int getCurrentRequestNumber(final String userName,
			final String serviceName, final String operationName) {
		SimpleEntry<Date, String> entry = getRequestDate();
		/* Get the number of actual calls done. */
		int calls = this.authorityDao.getCurrentRequestNumber(userName,
				serviceName, operationName, entry.getKey());
		/* This is the next call, so add one to the number of done calls */
		calls++;
		this.logger.debug("{}, {}, {}, {}, {}, {}",
				this.contextNameProvider.getContextName(), userName,
				serviceName, operationName, calls, entry.getValue());
		return calls;
	}
}
