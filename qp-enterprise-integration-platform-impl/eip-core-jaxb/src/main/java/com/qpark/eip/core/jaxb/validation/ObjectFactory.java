/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.jaxb.validation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * com.ses.osp.bus.library.flow.core.boot.test.validation package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
@SuppressWarnings("static-method")
public class ObjectFactory {
	/** The {@link QName} of the RootElement. */
	public final static QName _RootElement_QNAME = new QName(
			"http://www.qpark.com/ValidationMessages-1.0", "RootElement");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * com.ses.osp.bus.library.flow.core.boot.test.validation
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link RootElementType
	 * }{@code >}}
	 */
	/**
	 * @param value
	 *            the instance of {@link RootElementType}.
	 * @return the {@link JAXBElement} containing the {@link RootElementType}.
	 */
	@XmlElementDecl(namespace = "http://www.qpark.com/ValidationMessages-1.0",
			name = "RootElement")
	public JAXBElement<RootElementType> createRootElement(
			final RootElementType value) {
		return new JAXBElement<>(_RootElement_QNAME, RootElementType.class,
				null, value);
	}

	/**
	 * @return the created instance of {@link RootElementType}.
	 */
	public RootElementType createRootElementType() {
		return new RootElementType();
	}
}
