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
package com.qpark.eip.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jmx.export.MBeanExporter;

/**
 * @author bhausen
 */
public class HibernateMBeanExporter extends MBeanExporter implements BeanPostProcessor {

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
	return bean;
    }

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
	if (!bean.getClass().getName().startsWith("org.hibernate")) {
	    StringBuffer sb = new StringBuffer(64);
	    // sb.append("Servicebus:type=beans,bean:name=");
	    sb.append("bean:name=");
	    if (beanName == null || beanName.trim().length() == 0) {
		sb.append(bean.getClass().getName());
	    } else {
		sb.append(beanName);
	    }
	    this.registerBeanNameOrInstance(bean, sb.toString());
	}
	return bean;
    }
}
