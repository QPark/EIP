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
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An {@link ErrorHandler} keeping the messages in a {@link List}.
 *
 * @author bhausen
 */
class ListErrorHandler implements ErrorHandler {
	/** The list containing the error messages. */
	private final List<String> errorList = new ArrayList<>();

	/**
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	public void error(final SAXParseException e) throws SAXException {
		this.errorList.add(e.getMessage());
	}

	/**
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	public void fatalError(final SAXParseException e) throws SAXException {
		this.errorList.add(e.getMessage());
	}

	/**
	 * @return the errorList.
	 */
	public List<String> getErrorList() {
		return this.errorList;
	}

	/**
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	public void warning(final SAXParseException e) throws SAXException {
		this.errorList.add(e.getMessage());
	}
}
