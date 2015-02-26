/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core.failure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
/**
 * The failure annotation. Error codes of {@link Severity#ERROR} should start with E_, error codes of
 * {@link Severity#WARNING} should start with W_.
 * @author bhausen
 *
 */
@interface Failure {
	/** The {@link Failure} severity. */
	public enum Severity {
		/** Severity waring and error. */
		WARNING, ERROR
	}

	/**
	 * The error code of {@link Severity#ERROR} should start with E_, the error code of
	 * {@link Severity#WARNING} should start with W_.
	 * @return the error code.
	 */
	String code();

	/**
	 * @return the severity of the {@link Failure} (defaults to {@link Severity#ERROR}).
	 */
	Severity severity() default Severity.ERROR;

	/**
	 * @return the user message.
	 */
	String userMessage() default "";

	/**
	 * @return the error details.
	 */
	String errorDetails() default "";
}
