package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.apache.xmlbeans.SchemaType;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

public class XsdDirectMapperGenerator {
	/**
	 * Accepts a function that extracts a {@link java.lang.Comparable
	 * Comparable} sort key from a type {@code T}, and returns a {@code
	 * Comparator<T>} that compares by that sort key.
	 * <p>
	 * The returned comparator is serializable if the specified function is also
	 * serializable.
	 *
	 * @apiNote For example, to obtain a {@code Comparator} that compares {@code
	 * Person} objects by their last name,
	 *
	 *          <pre>
	 * {@code
	 *     Comparator<Person> byLastName = Comparator.comparing(Person::getLastName);
	 * }
	 *          </pre>
	 *
	 * @param <T>
	 *            the type of element to be compared
	 * @param <U>
	 *            the type of the {@code Comparable} sort key
	 * @param keyExtractor
	 *            the function used to extract the {@link Comparable} sort key
	 * @return a comparator that compares by an extracted key
	 * @throws NullPointerException
	 *             if the argument is null
	 * @since 1.8
	 */
	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
			final Function<? super T, ? extends U> keyExtractor) {
		Objects.requireNonNull(keyExtractor);
		return (Comparator<T> & Serializable) (c1, c2) -> keyExtractor.apply(c1)
				.compareTo(keyExtractor.apply(c2));
	}

	/**
	 * Accepts a function that extracts a {@link java.lang.Comparable
	 * Comparable} sort key from a type {@code T}, and returns a {@code
	 * Comparator<T>} that compares by that sort key.
	 * <p>
	 * The returned comparator is serializable if the specified function is also
	 * serializable.
	 *
	 * @apiNote For example, to obtain a {@code Comparator} that compares {@code
	 * Person} objects by their last name,
	 *
	 *          <pre>
	 * {@code
	 *     Comparator<Person> byLastName = Comparator.comparing(Person::getLastName);
	 * }
	 *          </pre>
	 *
	 * @param <T>
	 *            the type of element to be compared
	 * @param <U>
	 *            the type of the {@code Comparable} sort key
	 * @param keyExtractor
	 *            the function used to extract the {@link Comparable} sort key
	 * @return a comparator that compares by an extracted key
	 * @throws NullPointerException
	 *             if the argument is null
	 */
	public static <T> Comparator<T> comparingIgnoreCase(
			final Function<? super T, String> keyExtractor) {
		Objects.requireNonNull(keyExtractor);
		return (Comparator<T> & Serializable) (c1, c2) -> keyExtractor.apply(c1)
				.compareToIgnoreCase(keyExtractor.apply(c2));
	}

	public static void main(final String[] args) {

		String basePackageName = "com.qpark.eip.inf";
		String xsdPath = "C:\\xnb\\dev\\xxxx\\domain-gen-flow\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\com.ses.domain\\com.ses.domain.gen\\domain-gen-flow\\target\\model";
		xsdPath = "C:\\xnb\\dev\\bus.app.cpo\\cpo-model-service\\target\\model";

		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "mapping flow";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = XsdsUtil.getInstance(dif, "a.b.c.bus",
				messagePackageNameSuffix, "delta");
		ComplexContentList complexContentList = new ComplexContentList();
		complexContentList.setupComplexContentLists(xsds);
		System.out
				.println(Util.getDuration(System.currentTimeMillis() - start));

		String source = null;
		ComplexContent cc;
		SchemaType type;
		String ctNamespace;
		String prefix;

		ctNamespace = "http://www.ses.com/SatelliteAssetManagementAndPlanning/SatelliteAsset-2.1";
		prefix = "OSPam21";
		StringBuffer sb = new StringBuffer(512);
		xsds.getComplexTypes().stream()
				.filter(ct -> ct.getTargetNamespace().equals(ctNamespace))
				.sorted(Comparator.comparing(ComplexType::getQNameLocalPart))
				.forEach(ct -> {
					AtomicReference<String> ctName = new AtomicReference<>(
							ct.getClassName());
					if (ctName.get().endsWith("Type")) {
						ctName.set(ctName.get().substring(0,
								ctName.get().length() - 4));
					}
					ct.getChildren().stream()
							.sorted(comparingIgnoreCase(
									ComplexTypeChild::getChildName))
							.forEach(ctc -> {
								sb.append("\t<complexType name=\"");
								sb.append(ctName.get());
								sb.append(".");
								sb.append(ctc.getChildName());
								sb.append("MappingType\">\n");

								sb.append(
										"\t\t<annotation><documentation>Direct mapping of ");
								sb.append(ct.getClassName());
								sb.append(" ");
								sb.append(ctc.getChildName());
								sb.append(".</documentation></annotation>\n");

								sb.append(
										"\t\t<complexContent>\n\t\t\t<extension base=\"MPmp:DirectMappingType\">\n\t\t\t\t<sequence>\n");
								sb.append("\t\t\t\t\t<element name=\"");
								sb.append(toPropertyName(ctName.get()));
								sb.append("\" type=\"");
								sb.append(prefix);
								sb.append(":");
								sb.append(ct.getQNameLocalPart());
								sb.append("\" />\n");
								sb.append(
										"\t\t\t\t\t<element name=\"return\" type=\"");
								if (ctc.getComplexType().getTargetNamespace()
										.equals("http://www.w3.org/2001/XMLSchema")) {
								} else {
									sb.append("OSPcty10:");
								}
								sb.append(ctc.getComplexType()
										.getQNameLocalPart());
								sb.append("\" />\n");

								sb.append(
										"\t\t\t\t</sequence>\n\t\t\t</extension>\n\t\t</complexContent>\n\t</complexType>\n");
							});
				});
		System.out.println(sb.toString());
		// xsds.getComplexTypes().stream().filter(ComplexType::)
	}

	/**
	 * @param name
	 * @return the property name.
	 */
	public static String toPropertyName(final String name) {
		String s = name;
		if (s != null && s.length() > 0) {
			StringBuffer sb = new StringBuffer(s.length());
			sb.append(s.substring(0, 1).toLowerCase());
			if (s.length() > 1) {
				sb.append(s.substring(1, s.length()));
			}
			s = sb.toString();
		}
		return s;
	}

}
