/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.qpark.eip.core.DateUtil;

/**
 * Be sure to have the {@link Jaxb2Marshaller} and the serviceId map available.
 *
 * <pre>
 * &#64;Autowired
 * &#64;Qualifier("eipCallerXXXMarshaller")
 * private Jaxb2Marshaller jaxb2Marshaller;
 * &#64;Resource(name = "eipXXXServiceIdMap")
 * private Map<String, Object> serviceIdMap;
 * </pre>
 *
 * @author bhausen
 */
public class JAXBElementPersistence {
	/**
	 * @param d the {@link Date}.
	 * @return the {@link Date} in format <b>yyyyMMdd-HHmmss</b>
	 */
	public static String getDateString(final Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		return sdf.format(d);
	}

	/**
	 * @param date      the {@link Date}.
	 * @param additions the additonal parts of the {@link Path}
	 * @return the directory {@link Path} base on mavens "target" {@link Path}.
	 */
	public static Path getDirectoryPath(final Date date,
			final String... additions) {
		return getDirectoryPath(getMavenTargetPath(), date, additions);
	}

	/**
	 * @param parent    the parent {@link Path}.
	 * @param date      the {@link Date} - could be <code>null</code>.
	 * @param additions the additonal parts of the {@link Path}
	 * @return the directory Path.
	 */
	public static Path getDirectoryPath(final Path parent, final Date date,
			final String... additions) {
		String add = "";
		if (Objects.nonNull(additions) && additions.length > 0) {
			add = Arrays.asList(additions).stream()
					.collect(Collectors.joining("-"));
		}
		String name = add;
		if (Objects.nonNull(date)) {
			if (add.trim().length() > 0) {
				name = String.format("%s-%s", getDateString(date), add.trim());
			} else {
				name = String.format("%s", getDateString(date));
			}
		}
		Path value = new File(String.format("%s%s%s",
				parent.toFile().getAbsolutePath(), File.separatorChar, name))
						.toPath();
		return value;
	}

	/**
	 * @param parent    the parent {@link Path}.
	 * @param additions the additonal parts of the {@link Path}
	 * @return the directory {@link Path}.
	 */
	public static Path getDirectoryPath(final Path parent,
			final String... additions) {
		return getDirectoryPath(parent, (Date) null, additions);
	}

	/**
	 * @param type         the {@link Class} - value of an {@link JAXBElement} -
	 *                     contained in a file.
	 * @param serviceIdMap the serviceId {@link Map}.
	 * @return the file name containing this type.
	 */
	public static Optional<String> getFileName(final Class<?> type,
			final Map<String, Object> serviceIdMap) {
		Optional<String> value = Optional.empty();
		if (Objects.nonNull(serviceIdMap) && Objects.nonNull(type)) {
			String serviceId = serviceIdMap.entrySet().stream()
					.filter(e -> String.valueOf(e.getValue())
							.equals(type.getPackage().getName()))
					.map(e -> e.getKey()).findAny().orElse("");
			if (serviceId.equals("")) {
				value = Optional
						.of(String.format("%s.xml", type.getSimpleName()));
			} else {
				value = Optional.of(String.format("%s.%s.xml", serviceId,
						type.getSimpleName()));
			}
		}
		return value;
	}

	/**
	 * @param jaxbElement  the {@link JAXBElement} to get the file name for.
	 * @param serviceIdMap the serviceId {@link Map}.
	 * @return the file name.
	 */
	public static Optional<String> getFileName(final JAXBElement<?> jaxbElement,
			final Map<String, Object> serviceIdMap) {
		Optional<String> value = Optional.empty();
		if (Objects.nonNull(serviceIdMap) && Objects.nonNull(jaxbElement)
				&& Objects.nonNull(jaxbElement.getValue())) {
			String serviceId = serviceIdMap.entrySet().stream()
					.filter(e -> String.valueOf(e.getValue())
							.equals(jaxbElement.getValue().getClass()
									.getPackage().getName()))
					.map(e -> e.getKey()).findAny().orElse("");
			if (serviceId.equals("")) {
				value = Optional.of(String.format("%s.xml",
						jaxbElement.getValue().getClass().getSimpleName()));
			} else {
				value = Optional.of(String.format("%s.%s.xml", serviceId,
						jaxbElement.getValue().getClass().getSimpleName()));
			}
		}
		return value;
	}

	/**
	 * @param object the object of type T
	 * @param type   the class of the object.
	 * @return the root {@link JAXBElement} to marshall.
	 */
	public static <T> JAXBElement<T> getGenericJAXBRootElement(final T object,
			final Class<T> type) {
		JAXBElement<T> value = new JAXBElement<>(
				new QName(type.getSimpleName()), type, object);
		return value;
	}

	/**
	 * @param jaxbType
	 * @return the root {@link JAXBElement} to marshall.
	 */
	public static <T> Optional<JAXBContext> getGenericJAXBRootElementContext(
			final Class<T> jaxbType) {
		Optional<JAXBContext> value = Optional.empty();
		try {
			value = Optional.of(JAXBContext.newInstance(jaxbType));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @return the directory {@link Path} "target" used by maven.
	 */
	public static Path getMavenTargetPath() {
		return new File("target").toPath();
	}

	/**
	 * @param type        the {@link Class} of type T
	 * @param inputStream the {@link InputStream}.
	 * @param jaxbContext the {@link JAXBContext}.
	 * @return the object of type T.
	 * @throws JAXBException
	 */
	public static <T> Optional<T> unmarshal(final Class<T> type,
			final InputStream inputStream, final JAXBContext jaxbContext)
			throws JAXBException {
		Optional<T> value = Optional.empty();
		if (Objects.nonNull(inputStream) && Objects.nonNull(jaxbContext)
				&& Objects.nonNull(type)) {
			Unmarshaller um = jaxbContext.createUnmarshaller();
			Object o = um.unmarshal(inputStream);
			if (o != null && JAXBElement.class.isInstance(o)) {
				JAXBElement<?> jaxbElement = (JAXBElement<?>) o;
				if (Objects.nonNull(jaxbElement.getValue())
						&& jaxbElement.getValue().getClass().equals(type)) {
					@SuppressWarnings("unchecked")
					JAXBElement<T> t = (JAXBElement<T>) jaxbElement;
					value = Optional.of(t.getValue());
				}
			}
		}
		return value;
	}

	/** The {@link Jaxb2Marshaller}. */
	private final Jaxb2Marshaller jaxb2Marshaller;

	/** The service id map. */
	private final Map<String, Object> serviceIdMap;
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(JAXBElementPersistence.class);

	/**
	 * @param jaxb2Marshaller the {@link Jaxb2Marshaller}.
	 * @param serviceIdMap    the service id map.
	 */
	public JAXBElementPersistence(final Jaxb2Marshaller jaxb2Marshaller,
			final Map<String, Object> serviceIdMap) {
		this.jaxb2Marshaller = jaxb2Marshaller;
		this.serviceIdMap = serviceIdMap;
	}

	/**
	 * @param type the {@link Class} of type T
	 * @param path the path to the file. To read from class path pass a
	 *             <i>/</i>.
	 * @return the JAXBElement of type T.
	 */
	public <T> Optional<T> read(final Class<T> type, final String path) {
		Optional<T> value = Optional.empty();
		String thePath = path;
		if (Objects.isNull(thePath) || thePath.trim().length() == 0) {
			thePath = "/";
		}
		long start = System.currentTimeMillis();
		String fileName = JAXBElementPersistence
				.getFileName(type, this.serviceIdMap).orElse("x.xml");
		String resourceName = String.format("%s/%s", thePath, fileName);
		if (thePath.equals("/")) {
			resourceName = String.format("/%s", fileName);
		}

		try (InputStream inputSteam = this.getClass()
				.getResourceAsStream(resourceName)) {
			value = unmarshal(type, inputSteam,
					this.jaxb2Marshaller.getJaxbContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logger.debug("getJAXBElement {} #{} duration {}", resourceName,
				DateUtil.getDuration(start, System.currentTimeMillis()));
		return value;

	}

	/**
	 * @param jaxbElement the {@link JAXBElement} to store.
	 * @param path        the directory {@link Path} to store the
	 *                    {@link JAXBElement}.
	 * @throws JAXBException
	 */
	public void write(final JAXBElement<?> jaxbElement, final Path path)
			throws JAXBException {
		if (Objects.nonNull(jaxbElement)
				&& Objects.nonNull(jaxbElement.getValue())
				&& Objects.nonNull(path)) {
			AtomicReference<JAXBException> e = new AtomicReference<>();
			getFileName(jaxbElement, this.serviceIdMap).ifPresent(fileName -> {
				Path file = new File(
						String.format("%s%s%s", path.toFile().getAbsolutePath(),
								File.separatorChar, fileName)).toPath();
				Path parentDirectory = file.getParent();
				if (!parentDirectory.toFile().exists()) {
					parentDirectory.toFile().mkdirs();
				}
				try {
					Marshaller m = this.jaxb2Marshaller.getJaxbContext()
							.createMarshaller();
					m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
							Boolean.TRUE);
					try (PrintWriter pw = new PrintWriter(
							Files.newOutputStream(file,
									StandardOpenOption.CREATE,
									StandardOpenOption.TRUNCATE_EXISTING),
							true);) {
						m.marshal(jaxbElement, pw);
						this.logger.debug("wrote {}",
								file.toFile().getAbsolutePath());
					} catch (IOException ioe) {
						this.logger.error(String.format("FileName[%s] %s",
								file.toFile().getAbsolutePath(),
								ioe.getMessage()));
						ioe.printStackTrace();
					}
				} catch (JAXBException jaxbe) {
					e.set(jaxbe);
				}
			});
			if (Objects.nonNull(e.get())) {
				throw e.get();
			}
		}
	}
}
