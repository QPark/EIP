package com.qpark.eip.core.spring.statistics.dao;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Erase the system and application user log database older than
 * {@link #numberOfWeeksToKeepLogs} weeks.
 *
 * @author bhausen
 */
public class StatisticsEraser {
	/** The {@link StatisticsLoggingDao}. */
	@Autowired
	private StatisticsLoggingDao dao;
	/** The number of weeks to keep the log entries in the database. */
	private int numberOfWeeksToKeepLogs;

	/**
	 * Each day at 0:00 remove old out dated entries.
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void erase() {
		Calendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		gc.add(Calendar.WEEK_OF_YEAR, -1 * Math.abs(this.numberOfWeeksToKeepLogs));
		this.dao.eraseSystemUserLog(gc.getTime());
		this.dao.eraseApplicationUserLog(gc.getTime());
		this.dao.eraseFlowLogMessage(gc.getTime());
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
