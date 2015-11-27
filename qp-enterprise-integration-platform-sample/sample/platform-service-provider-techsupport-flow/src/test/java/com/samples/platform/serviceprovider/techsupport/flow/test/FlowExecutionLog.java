package com.samples.platform.serviceprovider.techsupport.flow.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.DateUtil;

/**
 * @author bhausen
 */
@Aspect
public class FlowExecutionLog {
    /** The {@link org.slf4j.Logger}. */
    private final Logger logger = LoggerFactory.getLogger(FlowExecutionLog.class);

    /**
     * Get the flowName out of the implementing class.
     *
     * @param joinPoint
     *            the {@link ProceedingJoinPoint}.
     * @return the name of the flow.
     */
    private String getInterfaceName(final ProceedingJoinPoint joinPoint) {
	String className = joinPoint.getTarget().getClass().getSimpleName();
	if (className.toLowerCase().endsWith("impl")) {
	    className = className.substring(0, className.length() - 4);
	}
	return className;
    }

    /**
     * Aspect around the execution of the invokeFlow method of all
     * {@link com.qpark.eip.inf.Flow} implementations.
     *
     * @param joinPoint
     *            The {@link ProceedingJoinPoint}.
     * @return the result of the flow.
     * @throws Throwable
     */
    // @Around(value = "execution(* com.qpark.eip.inf.Flow+.invokeFlow(..)) ||
    // execution(* com.qpark.eip.inf.FlowGateway+.*(..))")
    // @Around(value = "execution(* com.qpark.eip.inf.Flow+.invokeFlow(..))")
    @Around(value = "execution(* com.samples.platform.serviceprovider.techsupport.flow.SystemUserReportFlowImpl.invokeFlow(..))")
    public Object invokeFlowAspect(final ProceedingJoinPoint joinPoint) throws Throwable {
	long start = System.currentTimeMillis();
	String interfaceName = this.getInterfaceName(joinPoint);
	String userName = "";
	if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
	    userName = "";
	}
	this.logger.debug("+{}", interfaceName);
	Object result = null;
	try {
	    result = joinPoint.proceed();
	} catch (Throwable t) {
	    this.logger.debug(" {} failed with {}: {}", interfaceName, t.getClass().getSimpleName(), t.getMessage());
	    throw t;
	} finally {
	    this.logger.debug("-{} {}", interfaceName, DateUtil.getDuration(start));
	}
	return result;
    }

}
