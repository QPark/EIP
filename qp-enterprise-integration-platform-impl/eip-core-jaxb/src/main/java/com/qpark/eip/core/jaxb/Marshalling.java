package com.qpark.eip.core.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;

/**
 * @author bhausen
 */
public class Marshalling {
	/**
	 * @param is
	 *            the {@link InputStream} containing the object.
	 * @param type
	 *            the expected {@link Class}.
	 * @return the object of type.
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(final InputStream is, final Class<T> type)
			throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(type.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();
		Object o = um.unmarshal(is);
		T t = null;
		if (o != null && JAXBElement.class.isInstance(o)) {
			o = ((JAXBElement<?>) o).getValue();
		}
		if (type.isInstance(o)) {
			t = (T) o;
		}
		return t;
	}

	/**
	 * @param object
	 *            the object to clone.
	 * @param type
	 *            the type of the object to clone.
	 * @return the clone of the object.
	 * @throws JAXBException
	 */
	public static <T> T deepCopyJAXB(final T object, final Class<T> type)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(type);
		JAXBElement<T> contentObject = new JAXBElement<>(
				new QName(type.getSimpleName()), type, object);
		JAXBSource source = new JAXBSource(jaxbContext, contentObject);
		return jaxbContext.createUnmarshaller().unmarshal(source, type)
				.getValue();
	}

	/**
	 * @param element
	 *            the {@link JAXBElement} containing the value.
	 * @return the XML string of the value.
	 * @throws JAXBException
	 */
	public static <T> String marshal(final JAXBElement<T> element)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				element.getValue().getClass().getPackage().getName());
		return marshal(element, jaxbContext, true);
	}

	/**
	 * @param element
	 *            the {@link JAXBElement} containing the value.
	 * @param jaxbContext
	 *            the {@link JAXBContext} to use.
	 * @param formatted
	 *            XML formatted or as just one line.
	 * @return the XML string of the value.
	 * @throws JAXBException
	 */
	public static <T> String marshal(final JAXBElement<T> element,
			final JAXBContext jaxbContext, final boolean formatted)
			throws JAXBException {
		Marshaller m = jaxbContext.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos, true);
		m.marshal(element, pw);
		String value = "";
		try {
			value = new String(baos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// UTF-8 is always supported.
		}
		return value;
	}
}
