/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.statistics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.qpark.eip.core.DateUtil;

/**
 * @author bhausen
 */
@Aspect
@Order(11)
public class FlowExecutionLog {
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(FlowExecutionLog.class);
	/** The bean providing the message content. */
	@Autowired
	private MessageContentProvider messageContentProvider;

	/**
	 * Get the interface name of the implementing class.
	 *
	 * @param joinPoint
	 *            the {@link ProceedingJoinPoint}.
	 * @return the name of the interface.
	 */
	private String getInterfaceName(final ProceedingJoinPoint joinPoint) {
		String interfaceName = joinPoint.getTarget().getClass().getSimpleName();
		if (interfaceName.toLowerCase().endsWith("impl")) {
			interfaceName = interfaceName.substring(0,
					interfaceName.length() - 4);
		}
		return interfaceName;
	}

	/**
	 * Get the method name of the {@link ProceedingJoinPoint}.
	 *
	 * @param joinPoint
	 *            the method name.
	 * @return the method name.
	 */
	private String getMethodName(final ProceedingJoinPoint joinPoint) {
		String className = joinPoint.getSignature().getName();
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
	@Around(value = "execution(* com.qpark.eip.inf.Flow+.invokeFlow(..)) || execution(public * com.qpark.eip.inf.FlowGateway+.*(..))")
	public Object invokeFlowAspect(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		long start = System.currentTimeMillis();
		String interfaceName = this.getInterfaceName(joinPoint);
		String methodName = this.getMethodName(joinPoint);
		String userName = "";
		if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
			userName = this.messageContentProvider
					.getUserName(joinPoint.getArgs()[0]);
		}
		this.logger.debug("+{}.{}({})", interfaceName, methodName, userName);
		Object result = null;
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			this.logger.debug(" {}.{}({}) failed with {}: {}", interfaceName,
					methodName, userName, t.getClass().getSimpleName(),
					t.getMessage());
			throw t;
		} finally {
			this.logger.debug("-{}.{}({}) {}", interfaceName, methodName,
					userName,
					DateUtil.getDuration(start, System.currentTimeMillis()));
		}
		return result;
	}
}
