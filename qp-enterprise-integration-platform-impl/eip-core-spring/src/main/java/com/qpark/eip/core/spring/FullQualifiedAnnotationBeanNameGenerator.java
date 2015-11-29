/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * @author bhausen
 */
public class FullQualifiedAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {
    /**
     * Get the full qualified class name as part of the default spring bean id.
     * 
     * @see org.springframework.context.annotation.AnnotationBeanNameGenerator#buildDefaultBeanName(org.springframework.beans.factory.config.BeanDefinition)
     */
    @Override
    protected String buildDefaultBeanName(final BeanDefinition definition) {
	return definition.getBeanClassName();
    }
}
