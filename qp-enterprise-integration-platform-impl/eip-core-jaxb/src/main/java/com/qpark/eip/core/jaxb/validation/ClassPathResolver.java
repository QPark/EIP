/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.jaxb.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author bhausen
 */
public abstract class ClassPathResolver
		implements EntityResolver, LSResourceResolver {
	/** Class logger. */
	private final Logger logger = LoggerFactory
			.getLogger(ClassPathResolver.class);

	/** Get the {@link InputStream} containing the resolving stuff. */
	protected abstract InputStream getResourceAsStream(String resource);

	/**
	 * @param publicId
	 *            the publicId to resolve.
	 * @param systemId
	 *            the systemId to resolve.
	 * @return the {@link InputStream} - if resolvable.
	 */
	private InputStream resolve(final String publicId, final String systemId) {
		this.logger.trace("+resolve {} {}", publicId, systemId);
		String resource = systemId.replaceFirst("classpath:", "");
		if (!resource.startsWith("/")) {
			resource = String.format("/%s", resource);
		}
		InputStream is = this.getResourceAsStream(resource);
		this.logger.trace("-resolve {} {} {}", publicId, systemId, is);
		return is;
	}

	/**
	 * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@SuppressWarnings("resource")
	public InputSource resolveEntity(final String publicId,
			final String systemId) throws SAXException, IOException {
		InputStream is = this.resolve(publicId, systemId);
		if (Objects.nonNull(is)) {
			return new InputSource(is);
		}
		return null;
	}

	/**
	 * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@SuppressWarnings("resource")
	public LSInput resolveResource(final String type, final String namespaceURI,
			final String publicId, final String systemId,
			final String baseURI) {
		InputStream is = this.resolve(publicId, systemId);
		if (Objects.nonNull(is)) {
			return new InputStreamLSInput(publicId, systemId,
					this.resolve(publicId, systemId));
		}
		return null;
	}
}