/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.qpark.eip.core.jaxb.validation.Validator;
import com.samples.platform.core.metadata.MetaDataProvider;
import com.samples.platform.core.validation.EntityValidator;
import com.samples.platform.core.validation.XsdClassPathResolver;

/**
 * @author bhausen
 */
@Configuration
@SuppressWarnings("static-method")
public class CoreValidatorConfig {
	/** @return the {@link EntityValidator}. */
	@Bean
	public EntityValidator EntityValidator() {
		return new EntityValidator();
	}

	/** @return the {@link MetaDataProvider}. */
	@Bean
	public MetaDataProvider MetaDataProvider() {
		return new MetaDataProvider();
	}

	/** @return {@link PathMatchingResourcePatternResolver}. */
	@Bean(name = "xsdPathMatchingResourcePatternResolver")
	public PathMatchingResourcePatternResolver PathMatchingResourcePatternResolver() {
		return new PathMatchingResourcePatternResolver();
	}

	/**
	 * @param resolver
	 *            the {@link XsdClassPathResolver}.
	 * @return the {@link Validator}.
	 */
	@Bean
	public Validator Validator(final XsdClassPathResolver resolver) {
		return new Validator().setResolver(resolver);
	}

	/** @return the {@link XsdClassPathResolver}. */
	@Bean
	public XsdClassPathResolver XsdClassPathResolver() {
		return new XsdClassPathResolver();
	}
}
