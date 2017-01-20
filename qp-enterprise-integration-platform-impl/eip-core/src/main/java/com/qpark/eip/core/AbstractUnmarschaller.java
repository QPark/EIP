/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * Wrapper of an {@link Unmarshaller}.
 *
 * @author bhausen
 */
public abstract class AbstractUnmarschaller {
	/** The {@link JAXBContext}. */
	private JAXBContext context = null;

	/**
	 * @return the classes to be used in the {@link JAXBContext}. If the
	 *         {@link JAXBContext} path is given, these are not taken into
	 *         account.
	 */
	protected abstract Class<?>[] getContextClasses();

	/**
	 * @return the path of the {@link JAXBContext}. If <code>null</code>, the
	 *         {@link JAXBContext} classes need to be specified.
	 */
	protected abstract String getContextPath();

	/**
	 * Creates the {@link JAXBContext}. If the {@link #getContextPath()}
	 * provides a not null and not empty path, this is used. Otherwise the
	 * {@link #getContextClasses()} will be used to create the
	 * {@link JAXBContext}.
	 *
	 * @return the {@link JAXBContext}.
	 * @throws JAXBException
	 */
	protected JAXBContext getJAXBContext() throws JAXBException {
		if (this.context == null) {
			final String contextPath = this.getContextPath();
			if (Objects.nonNull(contextPath) && !contextPath.isEmpty()) {
				this.context = JAXBContext.newInstance(this.getContextPath());
			} else {
				this.context = JAXBContext
						.newInstance(this.getContextClasses());
			}
		}
		return this.context;
	}

	/**
	 * @param is
	 *            the {@link InputStream}.
	 * @return the value object.
	 * @throws JAXBException
	 */
	public Object getValue(final InputStream is) throws JAXBException {
		return this.getValue(is, null);
	}

	/**
	 * @param is
	 *            the {@link InputStream}.
	 * @param rootType
	 *            the type of the value object.
	 * @return the value object.
	 * @throws JAXBException
	 */
	public Object getValue(final InputStream is, final Class<?> rootType)
			throws JAXBException {
		Object value = null;
		if (is != null) {
			final JAXBContext ctx = this.getJAXBContext();
			final Unmarshaller unmarshaller = ctx.createUnmarshaller();
			Object element = null;
			if (rootType == null) {
				element = unmarshaller.unmarshal(is);
			} else {
				final StreamSource ss = new StreamSource(is);
				element = unmarshaller.unmarshal(ss, rootType);
			}
			if (element != null) {
				if (JAXBElement.class.isInstance(element)) {
					value = ((JAXBElement<?>) element).getValue();
				} else {
					value = element;
				}
			}
		}
		return value;
	}

	/**
	 * @param elem
	 *            the {@link JAXBElement} to write.
	 * @return the String representation of the {@link JAXBElement}
	 * @throws JAXBException
	 */
	public String getXml(final JAXBElement<?> elem) throws JAXBException {
		final JAXBContext ctx = this.getJAXBContext();
		final Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		final StringWriter sw = new StringWriter();
		marshaller.marshal(elem, sw);
		return sw.toString();
	}

	/**
	 * @param os
	 *            the {@link OutputStream} to write to.
	 * @param elem
	 *            the {@link JAXBElement} to write.
	 * @throws JAXBException
	 */
	public void writeXml(final OutputStream os, final JAXBElement<?> elem)
			throws JAXBException {
		if (os != null && elem != null) {
			final JAXBContext ctx = this.getJAXBContext();
			final Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(elem, os);
		}
	}
}
