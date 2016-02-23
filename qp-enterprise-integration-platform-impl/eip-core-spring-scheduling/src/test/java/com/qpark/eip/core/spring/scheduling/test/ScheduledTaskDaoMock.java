package com.qpark.eip.core.spring.scheduling.test;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.spring.scheduling.ScheduledTaskDao;
import com.qpark.eip.core.spring.scheduling.ScheduledTaskDescription;

public class ScheduledTaskDaoMock implements ScheduledTaskDao {
	private int callCount;
	private ScheduledTaskDescription scheduledTaskDescription;
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(ScheduledTaskDaoMock.class);

	public ScheduledTaskDaoMock() {
		this.callCount = 0;
		this.scheduledTaskDescription = new ScheduledTaskDescription();
		this.scheduledTaskDescription
				.setId("00000000-abcd-0001-0045-000000000000");
		this.scheduledTaskDescription.setName("Name");
		this.scheduledTaskDescription.setEnabled(false);
		this.scheduledTaskDescription
				.setExecutor(SimpleSysOutScheduledTaskExecutor.class);
	}

	/**
	 * @see com.qpark.eip.core.spring.scheduling.ScheduledTaskDao#getScheduleTaskDescriptions()
	 */
	@Override
	public List<ScheduledTaskDescription> getScheduleTaskDescriptions() {
		this.logger.debug("getScheduleTaskDescriptions {}", this.callCount);
		List<ScheduledTaskDescription> list = null;
		if (this.callCount > 0) {
			list = new ArrayList<ScheduledTaskDescription>();
			list.add(this.scheduledTaskDescription);
			if (this.callCount == 1) {
				list.clear();
			} else if (this.callCount == 2) {
				/* every second */
				this.scheduledTaskDescription.setEnabled(true);
				this.scheduledTaskDescription
						.setCronExpression("0/1 * * * * *");
			} else if (this.callCount == 3) {
				/* every two second */
				this.scheduledTaskDescription
						.setCronExpression("0/2 * * * * *");
			} else if (this.callCount == 4) {
				/* Fixed delay 3 seconds. */
				this.scheduledTaskDescription.setCronExpression("");
				this.scheduledTaskDescription.setFixedDelay(3);
			} else if (this.callCount == 5) {
				this.scheduledTaskDescription.setEnabled(false);
			}
		}
		this.callCount++;
		return list;
	}
}
