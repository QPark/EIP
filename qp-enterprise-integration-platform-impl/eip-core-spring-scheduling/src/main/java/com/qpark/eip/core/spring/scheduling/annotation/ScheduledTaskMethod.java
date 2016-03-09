package com.qpark.eip.core.spring.scheduling.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Only a method without parameters is allowed to be annotated with
 * {@link ScheduledTaskMethod}.
 *
 * @author bhausen
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledTaskMethod {
}
