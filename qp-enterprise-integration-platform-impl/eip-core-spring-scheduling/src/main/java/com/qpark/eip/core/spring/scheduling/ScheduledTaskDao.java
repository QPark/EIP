package com.qpark.eip.core.spring.scheduling;

import java.util.List;

public interface ScheduledTaskDao {

	List<ScheduledTaskDescription> getScheduleTaskDescriptions();
}
