/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.core.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.qpark.eip.core.jaxb.validation.ClassPathResolver;

/**
 * @author bhausen
 */
public class XsdClassPathResolver extends ClassPathResolver {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(XsdClassPathResolver.class);
	/** The spring {@link PathMatchingResourcePatternResolver}. */
	@Autowired
	@Qualifier("xsdPathMatchingResourcePatternResolver")
	private PathMatchingResourcePatternResolver resolver;

	/**
	 * @see com.qpark.eip.core.jaxb.validation.ClassPathResolver#getResourceAsStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceAsStream(final String resource) {
		InputStream is = null;
		Resource r = this.resolver
				.getResource(String.format("classpath:%s", resource));
		if (Objects.nonNull(r)) {
			try {
				is = r.getInputStream();
				if (Objects.nonNull(is)) {
					this.logger.trace(" validate found {}", resource);
				} else {
					this.logger.error(" validate NOT found {}", resource);
				}
			} catch (IOException e) {
				this.logger.error(" validate error {}", e.getMessage());
			}
		} else {
			this.logger.error(" validate have not found {}", resource);
		}
		return is;
	}
}
