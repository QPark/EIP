package com.qpark.eip.core.model.analysis;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.qpark.eip.model.docmodel.ObjectFactory;

public class AnalysisMarshaller {
    private JAXBContext context = null;

    protected final JAXBContext getJAXBContext() throws JAXBException {
	if (this.context == null) {
	    this.context = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
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

}
