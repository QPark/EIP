/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.jaxb.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Validate JAXB objects and {@link JAXBElement}s.
 *
 * @author bhausen
 */
public class Validator {
	/**
	 * The {@link Map} of already setup {@link JAXBContext} with their context
	 * path.
	 */
	private Map<String, JAXBContext> jaxbContextMap = new HashMap<>();
	/** The {@link ClassPathResolver}. */
	private ClassPathResolver resolver;
	/** The {@link Schema} to validate with. */
	private Schema schema;

	/** The validation XSD. */
	// /collected-schemas.xsd
	private String validationXsd = "/validation.xsd";

	/**
	 * @param contextPath
	 *            the context path.
	 * @return the {@link JAXBContext}.
	 * @throws JAXBException
	 */
	private JAXBContext getJAXBContext(final String contextPath)
			throws JAXBException {
		JAXBContext value = this.jaxbContextMap.get(contextPath);
		if (Objects.isNull(value)) {
			value = JAXBContext.newInstance(contextPath);
			this.jaxbContextMap.put(contextPath, value);
		}
		return value;
	}

	/**
	 * Get the {@link Schema}.
	 *
	 * @return the {@link Schema}.
	 * @throws SAXException
	 */
	private Schema getSchema() throws SAXException {
		if (Objects.isNull(this.schema)) {
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			sf.setResourceResolver(this.resolver);
			this.schema = sf.newSchema(new StreamSource(
					this.resolver.getResourceAsStream(this.validationXsd)));
		}
		return this.schema;
	}

	/**
	 * @param resolver
	 * @return the {@link Validator}.
	 */
	public Validator setResolver(final ClassPathResolver resolver) {
		this.resolver = resolver;
		return this;
	}

	/**
	 * @param validationXsd
	 * @return the {@link Validator}.
	 */
	public Validator setValidationXsd(final String validationXsd) {
		this.validationXsd = validationXsd;
		return this;
	}

	/**
	 * Validate the {@link JAXBElement}.
	 *
	 * @param element
	 *            the {@link JAXBElement} to validate.
	 * @return the list of error messages.
	 * @throws Exception
	 */
	public List<String> validate(final JAXBElement<?> element)
			throws Exception {
		return this.validate(element,
				element.getValue().getClass().getPackage().getName());
	}

	/**
	 * Validate the {@link JAXBElement}.
	 *
	 * @param element
	 *            the {@link JAXBElement} to validate.
	 * @param contextPath
	 *            the context path of the {@link JAXBElement}.
	 * @return the list of error messages.
	 * @throws Exception
	 */
	private List<String> validate(final JAXBElement<?> element,
			final String contextPath) throws Exception {
		List<String> value = new ArrayList<>();
		ListErrorHandler errorHandler = new ListErrorHandler();
		javax.xml.validation.Validator validator = this.getSchema()
				.newValidator();
		validator.setErrorHandler(errorHandler);

		JAXBSource source = new JAXBSource(this.getJAXBContext(contextPath),
				element);
		validator.validate(source);
		value.addAll(errorHandler.getErrorList().stream()
				.filter(e -> !e.contains(
						ObjectFactory._RootElement_QNAME.getNamespaceURI()))
				.collect(Collectors.toList()));
		return value;
	}

	/**
	 * Validate the {@link JAXBElement}.
	 *
	 * @param value
	 *            the JAXB {@link Object} to validate.
	 * @return the list of error messages.
	 * @throws Exception
	 */
	public List<String> validate(final Object value) throws Exception {
		ObjectFactory of = new ObjectFactory();
		RootElementType root = of.createRootElementType();
		root.setElement(value);
		JAXBElement<RootElementType> element = of.createRootElement(root);

		return this.validate(element,
				String.format("%s:%s", root.getClass().getPackage().getName(),
						value.getClass().getPackage().getName()));
	}
}
