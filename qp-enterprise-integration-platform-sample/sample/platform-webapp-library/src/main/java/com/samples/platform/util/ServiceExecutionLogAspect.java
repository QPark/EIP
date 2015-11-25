package com.samples.platform.util;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * Logs durations of the service execution.
 * 
 * @author bhausen
 */
@Aspect
@Order(10)
public class ServiceExecutionLogAspect {
	/**
	 * @param millis
	 * @return the duration in 000:00:00.000 format.
	 */
	static String getDuration(final long millis) {
		String hmss = String.format("%03d:%02d:%02d.%03d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS
						.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES
						.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				TimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS
						.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		return hmss;
	}

	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(ServiceExecutionLogAspect.class);

	/**
	 * Get the name of the signature of the {@link ProceedingJoinPoint}.
	 *
	 * @param joinPoint
	 *            the {@link ProceedingJoinPoint}.
	 * @return the name of the signature.
	 */
	private String getSignatureName(final ProceedingJoinPoint joinPoint) {
		Signature sig = joinPoint.getSignature();
		return new StringBuffer(64).append(sig.getDeclaringTypeName())
				.append("#").append(sig.getName()).toString();
	}

	/**
	 * Aspect around the execution of the services.
	 *
	 * @param joinPoint
	 *            The {@link ProceedingJoinPoint}.
	 * @return the result of the service execution.
	 * @throws Throwable
	 */
	@Around(value = "execution(* com.qpark.eip.serice.*.*(..)) || execution(* com.samples.platform.serice.*.*(..)) ")
	public Object invokeFlowAspect(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		long start = System.currentTimeMillis();
		String signatureName = this.getSignatureName(joinPoint);
		this.logger.debug("+{}", signatureName);
		Object result = null;
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			this.logger.debug(" {} failed with {}: {}", signatureName,
					t.getClass().getSimpleName(), t.getMessage());
			throw t;
		} finally {
			this.logger.debug("-{} {}", signatureName,
					getDuration(System.currentTimeMillis() - start));
		}
		return result;
	}
}
