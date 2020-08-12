package com.qpark.eip.core.spring.statistics.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType;
import com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;
import com.qpark.eip.core.spring.statistics.StatisticsListener;

/**
 * Log the statistics into the {@link Logger}
 *
 * @author bhausen
 *
 */
public class StatisticsInfoLogger implements StatisticsListener {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(StatisticsInfoLogger.class);

	/**
	 *
	 * @see com.qpark.eip.core.spring.statistics.StatisticsListener#addChannelInvocation(String,com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType)
	 */
	@Override
	public void addChannelInvocation(final String channelName,
			final ApplicationUserLogType log) {
		this.logger.info("{}: {}, {}, {}, {}, {}, {}, {}, {}", channelName,
				log.getContext(), log.getVersion(), log.getUserName(),
				log.getServiceName(), log.getOperationName(),
				log.getDurationString(), log.getReturnedEntities(),
				log.getReturnedFailures());
	}

	/**
	 *
	 * @see com.qpark.eip.core.spring.statistics.StatisticsListener#addChannelInvocation(String,com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType)
	 */
	@Override
	public void addChannelInvocation(final String channelName,
			final SystemUserLogType log) {
		this.logger.info("{}: {}, {}, {}, {}, {}", channelName,
				log.getContext(), log.getVersion(), log.getUserName(),
				log.getServiceName(), log.getOperationName());
	}

	/**
	 *
	 * @see com.qpark.eip.core.spring.statistics.StatisticsListener#addFlowLogMessage(com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType)
	 */
	@Override
	public void addFlowLogMessage(final FlowLogMessageType log) {
		this.logger.info("{} {}", log.getFlowName(),
				log.getDataDescription() != null
						? log.getDataDescription().replaceAll("\\\\n", " ")
								.replaceAll("\\\\t", " ")
						: "");
	}
}
