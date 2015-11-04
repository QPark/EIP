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
package com.qpark.eip.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * @author bhausen
 */
public abstract class AbstractUnmarschaller {
    private JAXBContext context = null;

    protected abstract String getContextPath();

    protected final JAXBContext getJAXBContext() throws JAXBException {
	if (this.context == null) {
	    this.context = JAXBContext.newInstance(this.getContextPath());
	}
	return this.context;
    }

    public Object getValue(final InputStream is) throws JAXBException {
	return this.getValue(is, null);
    }

    public Object getValue(final InputStream is, final Class<?> rootType) throws JAXBException {
	Object value = null;
	if (is != null) {
	    JAXBContext context = this.getJAXBContext();
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    Object element = null;
	    if (rootType == null) {
		element = unmarshaller.unmarshal(is);
	    } else {
		StreamSource ss = new StreamSource(is);
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

    public void writeXml(final OutputStream os, final JAXBElement<?> elem) throws JAXBException {
	if (os != null && elem != null) {
	    JAXBContext context = this.getJAXBContext();
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    marshaller.marshal(elem, os);
	}
    }

    public String getXml(final JAXBElement<?> elem) throws JAXBException {
	JAXBContext context = this.getJAXBContext();
	Marshaller marshaller = context.createMarshaller();
	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	StringWriter sw = new StringWriter();
	marshaller.marshal(elem, sw);
	return sw.toString();
    }
}
