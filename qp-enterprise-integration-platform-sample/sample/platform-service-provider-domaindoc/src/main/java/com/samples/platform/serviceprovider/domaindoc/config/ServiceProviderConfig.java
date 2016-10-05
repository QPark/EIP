/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.domaindoc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.qpark.eip.core.spring.FullQualifiedAnnotationBeanNameGenerator;

/**
 * Spring configuration of the service provider
 *
 * @author bhausen
 */
@Configuration
@Import(value = {
		com.qpark.eip.core.model.analysis.config.EipModelAnalysisOperationConfig.class })
@ComponentScan(
		basePackages = { "com.samples.platform.serviceprovider.domaindoc" },
		nameGenerator = FullQualifiedAnnotationBeanNameGenerator.class)
public class ServiceProviderConfig {
}
