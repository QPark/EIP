package com.qpark.eip.core.spring.statistics.flow;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * @author bhausen
 */
@Aspect
@Order(11)
public class FlowExecutionLogAspect {
	/**
	 * @param millis
	 * @return the duration in 000:00:00.000 format.
	 */
	static String getDuration(final long millis) {
		String hmss = String.format(
				"%03d:%02d:%02d.%03d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)),
				TimeUnit.MILLISECONDS.toMillis(millis)
						- TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS
								.toSeconds(millis)));
		return hmss;
	}

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(FlowExecutionLogAspect.class);

	/**
	 * Get the flowName out of the implementing class.
	 * @param joinPoint the {@link ProceedingJoinPoint}.
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
	 * @param joinPoint The {@link ProceedingJoinPoint}.
	 * @return the result of the flow.
	 * @throws Throwable
	 */
	@Around(value = "execution(* com.ses.osp.bus.inf.Flow+.invokeFlow(..))")
	public Object invokeFlowAspect(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		long start = System.currentTimeMillis();
		String flowName = this.getFlowName(joinPoint);
		this.logger.debug("+{}", flowName);
		Object result = null;
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			this.logger.debug(" {} failed with {}: {}", flowName, t.getClass()
					.getSimpleName(), t.getMessage());
			throw t;
		} finally {
			this.logger.debug("-{} {}", flowName,
					getDuration(System.currentTimeMillis() - start));
		}
		return result;
	}
}
