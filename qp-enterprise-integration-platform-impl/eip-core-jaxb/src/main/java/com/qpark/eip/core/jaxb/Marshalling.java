/*******************************************************************************
 * Copyright (c) 2018 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
	 * @param object the object to clone.
	 * @param type   the type of the object to clone.
	 * @return the clone of the object.
	 * @throws JAXBException
	 */
	public static <T> T deepCopyJAXB(final T object, final Class<T> type) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(type);
		JAXBElement<T> contentObject = new JAXBElement<>(new QName(type.getSimpleName()), type, object);
		JAXBSource source = new JAXBSource(jaxbContext, contentObject);
		return jaxbContext.createUnmarshaller().unmarshal(source, type).getValue();
	}

	/**
	 * @param jaxbType the {@link Class} of the type to create the
	 *                 {@link JAXBContext} from.
	 * @return the {@link JAXBContext} for the type.
	 * @throws JAXBException
	 */
	public static <T> JAXBContext getJAXBContext(final Class<? extends T> jaxbType) throws JAXBException {
		JAXBContext value;
		if (Collection.class.isAssignableFrom(jaxbType)) {
			@SuppressWarnings("unchecked")
			T[] array = (T[]) Array.newInstance(jaxbType, 0);
			value = JAXBContext.newInstance(array.getClass());
		} else {
			value = JAXBContext.newInstance(jaxbType);
		}
		return value;
	}

	/**
	 * @param jaxbObjects the JAXB object used to
	 *                    {@link JAXBElement#setValue(Object)}.
	 * @param jaxbType    the {@link Class} of the type to create the
	 *                    {@link JAXBElement} from.
	 * @return the root {@link JAXBElement}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Optional<JAXBElement<T>> getRootJAXBElement(final Collection<T> jaxbObjects,
			final Class<? extends T> jaxbType) {
		Optional<JAXBElement<T>> value = Optional.empty();
		if (Objects.nonNull(jaxbObjects) && Objects.nonNull(jaxbType)) {
			T[] array = (T[]) Array.newInstance(jaxbType, jaxbObjects.size());
			jaxbObjects.toArray(array);
			value = Optional.of(new JAXBElement(new QName(jaxbType.getSimpleName()), array.getClass(), array));
		}
		return value;
	}

	/**
	 * @param jaxbObject the JAXB object used to
	 *                   {@link JAXBElement#setValue(Object)}.
	 * @param jaxbType   the {@link Class} of the type to create the
	 *                   {@link JAXBElement} from.
	 * @return the root {@link JAXBElement}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Optional<JAXBElement<T>> getRootJAXBElement(final T jaxbObject,
			final Class<? extends T> jaxbType) {
		Optional<JAXBElement<T>> value = Optional.empty();
		if (Objects.nonNull(jaxbObject) && Objects.nonNull(jaxbType) && !Collection.class.isAssignableFrom(jaxbType)) {
			value = Optional.of(new JAXBElement(new QName(jaxbType.getSimpleName()), jaxbType, jaxbObject));
		}
		return value;
	}

	/**
	 * @param element the {@link JAXBElement} containing the value.
	 * @return the XML string of the value.
	 * @throws JAXBException
	 */
	public static <T> String marshal(final JAXBElement<T> element) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(element.getValue().getClass().getPackage().getName());
		return marshal(element, jaxbContext).orElse("");
	}

	/**
	 * @param jaxbRootElement the JAXB root element to marshal.
	 * @param jaxbType        the {@link Class} of the type to create the
	 *                        {@link JAXBElement} from.
	 * @return the XML string.
	 * @throws JAXBException
	 */
	public static <T> String marshal(final JAXBElement<T> jaxbRootElement, final Class<T> jaxbType)
			throws JAXBException {
		AtomicReference<String> value = new AtomicReference<>("");
		JAXBContext jc = getJAXBContext(jaxbType);
		marshal(jaxbRootElement, jc).ifPresent(xml -> value.set(xml));
		return value.get();
	}

	/**
	 * Get the XML string.
	 *
	 * @param jaxbRootElement the JAXB root element to marshal.
	 * @param jaxbContext     the JAXB Context to use while marshaling.
	 * @return the XML {@link String}.
	 * @throws JAXBException
	 */
	public static <T> Optional<String> marshal(final JAXBElement<T> jaxbRootElement, final JAXBContext jaxbContext)
			throws JAXBException {
		return marshal(jaxbRootElement, jaxbContext);
	}

	/**
	 * @param jaxbRootElement the {@link JAXBElement} containing the value.
	 * @param jaxbContext     the {@link JAXBContext} to use.
	 * @param formatted       XML formatted or as just one line.
	 * @return the XML string of the value.
	 * @throws JAXBException
	 */
	public static <T> String marshal(final JAXBElement<T> jaxbRootElement, final JAXBContext jaxbContext,
			final boolean formatted) throws JAXBException {
		return marshal(jaxbRootElement, jaxbContext, formatted, new ByteArrayOutputStream()).orElse("");
	}

	/**
	 * Provide the entire information and you will get the XML string, if
	 * <i>outputStream</i> is null or a {@link ByteArrayOutputStream}.
	 *
	 * @param jaxbRootElement the JAXB root element to marshal.
	 * @param jaxbContext     the JAXB Context to use while marshaling.
	 * @param formatted       flag, whether to format the resulting XML or not.
	 * @param outputStream    the {@link OutputStream} to use. If <code>null</code>,
	 *                        a {@link ByteArrayOutputStream} is used.
	 * @return the XML {@link String}, if a <i>outputStream</i> is null or a
	 *         {@link ByteArrayOutputStream}.
	 * @throws JAXBException
	 */
	public static <T> Optional<String> marshal(final JAXBElement<T> jaxbRootElement, final JAXBContext jaxbContext,
			final boolean formatted, final OutputStream outputStream) throws JAXBException {
		Optional<String> value = Optional.empty();
		Marshaller m = jaxbContext.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
		OutputStream os = outputStream;
		if (Objects.isNull(os)) {
			os = new ByteArrayOutputStream();
		}
		PrintWriter pw = new PrintWriter(os, true);
		m.marshal(jaxbRootElement, pw);
		if (ByteArrayOutputStream.class.isInstance(os)) {
			try {
				value = Optional.ofNullable(new String(((ByteArrayOutputStream) os).toByteArray(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// UTF-8 is always supported.
			}
		}
		return value;
	}

	/**
	 * @param jaxbObjects
	 * @param jaxbType
	 * @return the XML string.
	 * @throws JAXBException
	 */
	public static <T> String marshalCollection(final Collection<T> jaxbObjects, final Class<? extends T> jaxbType)
			throws JAXBException {
		AtomicReference<String> value = new AtomicReference<>("");
		AtomicReference<JAXBException> exception = new AtomicReference<>();
		JAXBContext jc = getJAXBContext(jaxbType);
		getRootJAXBElement(jaxbObjects, jaxbType).ifPresent(jaxbRootElement -> {
			try {
				marshal(jaxbRootElement, jc).ifPresent(xml -> value.set(xml));
			} catch (JAXBException e) {
				exception.set(e);
			}
		});
		return value.get();
	}

	/**
	 * @param jaxbObject the JAXB object used to
	 *                   {@link JAXBElement#setValue(Object)}.
	 * @param jaxbType   the {@link Class} of the type to create the
	 *                   {@link JAXBElement} from.
	 * @return the XML string.
	 * @throws JAXBException
	 */
	public static <T> String marshalObject(final T jaxbObject, final Class<? extends T> jaxbType) throws JAXBException {
		AtomicReference<String> value = new AtomicReference<>("");
		AtomicReference<JAXBException> exception = new AtomicReference<>();
		JAXBContext jc = getJAXBContext(jaxbType);
		getRootJAXBElement(jaxbObject, jaxbType).ifPresent(jaxbRootElement -> {
			try {
				marshal(jaxbRootElement, jc).ifPresent(xml -> value.set(xml));
			} catch (JAXBException e) {
				exception.set(e);
			}
		});
		if (Objects.nonNull(exception.get())) {
			throw exception.get();
		}
		return value.get();
	}

	/**
	 * @param is   the {@link InputStream} containing the object.
	 * @param type the expected {@link Class}.
	 * @return the object of type.
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(final InputStream is, final Class<T> type) throws JAXBException {
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
}
