/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Config to get a {@link Marshaller} and a {@link Unmarshaller} supporting the
 * entire domain.
 *
 * @author bhausen
 */
public class JAXBMarshallerUnmarshallerProvider {
	/** The spring {@link Jaxb2Marshaller}. */
	@Autowired
	private Jaxb2Marshaller jaxb2Marshaller;

	/**
	 * @return the spring {@link Jaxb2Marshaller}.
	 */
	public Jaxb2Marshaller getJaxb2Marshaller() {
		return this.jaxb2Marshaller;
	}

	/**
	 * @return the {@link JAXBContext} of the entire SES domain.
	 */
	public JAXBContext getJAXBContextComSesDomain() {
		return this.jaxb2Marshaller.getJaxbContext();
	}

	/**
	 * @return the {@link Marshaller} supporting the entire SES domain.
	 * @throws Exception
	 */
	public Marshaller getMarshaller() throws Exception {
		Marshaller value = this.jaxb2Marshaller.getJaxbContext()
				.createMarshaller();
		value.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		value.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		return value;
	}

	/**
	 * @return the {@link Unmarshaller} supporting the entire SES domain.
	 * @throws Exception
	 */
	public Unmarshaller getUnmarshaller() throws Exception {
		Unmarshaller value = this.jaxb2Marshaller.getJaxbContext()
				.createUnmarshaller();
		return value;
	}
}
