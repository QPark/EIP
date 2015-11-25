package com.qpark.eip.core.spring.statistics;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;

import com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType;
import com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType;
import com.qpark.eip.core.spring.ContextNameProvider;

/**
 * @author bhausen
 */
@Aspect
@Order(11)
public class FlowExecutionLog {
	/** The {@link AppUserStatisticsChannelAdapter}. */
	@Autowired
	private AppUserStatisticsChannelAdapter appUserStatistics;
	/** The eip {@link ContextNameProvider}. */
	@Autowired
	@Qualifier("ComQparkEipCoreSpringStatisticsContextNameProvider")
	private ContextNameProvider contextNameProvider;
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(FlowExecutionLog.class);
	/** The {@link SysUserStatisticsChannelInvocationListener}. */
	@Autowired
	private SysUserStatisticsChannelInvocationListener sysUserStatistics;
	/** The bean providing the message content. */
	@Autowired
	@Qualifier("ComQparkEipCoreSpringStatisticsMessageContentProvider")
	private MessageContentProvider messageContentProvider;

	/**
	 * Get the flowName out of the implementing class.
	 *
	 * @param joinPoint
	 *            the {@link ProceedingJoinPoint}.
	 * @return the name of the flow.
	 */
	private String getFlowName(final ProceedingJoinPoint joinPoint) {
		String flowName = joinPoint.getTarget().getClass().getSimpleName();
		if (flowName.toLowerCase().endsWith("impl")) {
			flowName = flowName.substring(0, flowName.length() - 4);
		}
		return flowName;
	}

	/**
	 * Aspect around the execution of the invokeFlow method of all
	 * {@link com.ses.osp.bus.inf.Flow} implementations.
	 *
	 * @param joinPoint
	 *            The {@link ProceedingJoinPoint}.
	 * @return the result of the flow.
	 * @throws Throwable
	 */
	@Around(value = "execution(* com.qpark.eip.inf.Flow+.invokeFlow(..))")
	public Object invokeFlowAspect(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		long start = System.currentTimeMillis();
		String flowName = this.getFlowName(joinPoint);
		String userName = "";
		if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
			userName = this.messageContentProvider
					.getUserName(joinPoint.getArgs()[0]);
		}
		this.logger.debug("+{}", flowName);
		Object result = null;
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			this.logger.debug(" {} failed with {}: {}", flowName,
					t.getClass().getSimpleName(), t.getMessage());
			throw t;
		} finally {
			this.logger.debug("-{} {}", flowName,
					AppUserStatisticsChannelAdapter.getDuration(start));
			try {
				this.logApplicationUserLogType(flowName, userName, start);
			} catch (Exception e) {
				// nothing to do here.
			}
			try {
				this.logAndPersistSysUserLogType(flowName, userName);
			} catch (Exception e) {
				// nothing to do here.
			}
		}
		return result;
	}

	/**
	 * Log the application user log - each single flow execution is logged in
	 * the log files.
	 *
	 * @param flowName
	 *            the name of the flow.
	 * @param userName
	 *            the execution application user.
	 * @param start
	 *            the time the execution started.
	 */
	private void logApplicationUserLogType(final String flowName,
			final String userName, final long start) {
		ApplicationUserLogType log = new ApplicationUserLogType();
		log.setStopItem(new Date());
		log.setDurationMillis(System.currentTimeMillis() - start);
		log.setStartItem(new Date(start));
		log.setDurationString(AppUserStatisticsChannelAdapter
				.getDuration(log.getDurationMillis()));

		log.setContext(this.contextNameProvider.getContextName());
		log.setVersion(this.contextNameProvider.getContextVersion());
		log.setUserName(userName);
		log.setServiceName("flow");
		log.setOperationName(flowName);

		log.setReturnedEntities(0);
		log.setReturnedFailures(0);

		log.setHostName(this.appUserStatistics.getHostName());

		this.appUserStatistics.logApplicationUserLogType(log);
	}

	/**
	 * Log and persist the number of flow execution with the application user
	 * name. At this point the systemuser name is not known.
	 *
	 * @param flowName
	 *            the name of the flow.
	 * @param userName
	 *            the name of the application user.
	 */
	public void logAndPersistSysUserLogType(final String flowName,
			final String userName) {
		SystemUserLogType log = new SystemUserLogType();
		log.setUserName(userName);
		log.setServiceName("flow");
		log.setOperationName(flowName);
		log.setLogDateItem(SysUserStatisticsChannelInvocationListener
				.getRequestDate().getTime());
		log.setContext(this.contextNameProvider.getContextName());
		log.setVersion(this.contextNameProvider.getContextVersion());
		log.setRequestsGranted(1);

		this.sysUserStatistics.submitSystemUserLogType(log, "executed");
	}
}
