package com.qpark.eip.core.spring.scheduling;

import java.util.List;

/**
 * The DAO provides a list of {@link ScheduledTaskDescription}s.
 *
 * @author bhausen
 */
public interface ScheduledTaskDao {
	/**
	 * Get the list of all descriptions.
	 *
	 * @return the list of {@link ScheduledTaskDescription}s.
	 */
	List<ScheduledTaskDescription> getScheduleTaskDescriptions();
}
