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
package com.qpark.eip.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that there is a thing to be attend.
 * @author bhausen
 */
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface Attention {
	public static enum Category {
		/** Take attention that here a implementation is missing. */
		MISSING_IMPLEMENTATION,
		/** This code may run into errors. */
		ERROR_POSSIBLE,
		/** Not categorized. */
		NOT_CATEGORIESED
	}

	String value();

	Category category() default Category.NOT_CATEGORIESED;

	String date() default "";
}
