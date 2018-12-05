/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.parallel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * @author bhausen
 * @param <T>
 */
public class MethodCallable<T> implements Callable<T> {
	/** The {@link org.slf4j.Logger}. */
	private final static Logger logger = LoggerFactory
			.getLogger(MethodCallable.class);

	/**
	 * @param bean       the bean implementing the {@link Method}.
	 * @param methodName the name of the method to get.
	 * @return the {@link Method}. Note that the method needs to be
	 *         <i>public</i> to be executable by an {@link Callable}.
	 */
	public static Optional<Method> getBeanMethod(final Object bean,
			final String methodName) {
		AtomicReference<Method> value = new AtomicReference<>(null);
		try {
			value.set(BeanUtils.findMethodWithMinimalParameters(bean.getClass(),
					methodName));
		} catch (SecurityException e) {
			logger.error("getBeanMethod {}", e);
		}
		logger.debug("getBeanMethod {}.{} = {}", bean, methodName, value.get());
		return Optional.ofNullable(value.get());
	}

	/** The bean implementing the {@link Method}. */
	private Object bean;
	/** The Request object to call the {@link Method} with. */
	private final Object[] input;
	/** The method to execute. */
	private Method method;

	/**
	 * @param bean   bean implementing the {@link Method}.
	 * @param method The {@link Method} bean to call.
	 * @param input  the input data.
	 */
	public MethodCallable(final Object bean, final Method method,
			final Object... input) {
		this.bean = bean;
		this.method = method;
		this.input = input;
	}

	/**
	 * @see java.util.concurrent.Callable#call()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T call() throws Exception {
		logger.trace("+call {} {} {}", this.method.getName(), this.bean,
				this.input.length);
		AtomicReference<T> value = new AtomicReference<>();
		Instant start = Instant.now();
		try {
			value.set((T) this.method.invoke(this.bean, this.input));
		} catch (IllegalArgumentException e) {
			logger.error(String.format(" call: Failed to execute %s: %s",
					this.method.getName(), e.getMessage()), e);
		} catch (InvocationTargetException e) {
			logger.error(" call: Failed to execute {}", this.method.getName());
			logger.error(String.format("%s: %s", e.getMessage(),
					e.getCause().getMessage()), e.getCause());
		} catch (final Exception e) {
			logger.error(" call: Failed to execute {}", this.method.getName());
			logger.error(e.getMessage(), e);
		} finally {
			logger.trace("-call {} {}", this.method.getName(),
					Duration.between(start, Instant.now()));
		}
		return value.get();
	}
}
