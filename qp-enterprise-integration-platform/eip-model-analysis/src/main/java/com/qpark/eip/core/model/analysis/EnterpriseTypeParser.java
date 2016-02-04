package com.qpark.eip.core.model.analysis;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.ObjectFactory;

/**
 * Provides a parsed {@link EnterpriseType}.
 *
 * @author bhausen
 */
public class EnterpriseTypeParser {
	/**
	 * Provides the {@link EnterpriseType} defined in with the
	 * {@link InputStream}
	 *
	 * @param is
	 *            the {@link InputStream}.
	 * @return the {@link EnterpriseType}.
	 * @throws JAXBException
	 */
	public static EnterpriseType parseEnterprise(final InputStream is)
			throws JAXBException {
		Object value = null;
		EnterpriseType enterprise = null;
		if (is != null) {
			JAXBContext context = JAXBContext
					.newInstance(ObjectFactory.class.getPackage().getName());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Object element = null;
			element = unmarshaller.unmarshal(is);
			if (element != null) {
				if (JAXBElement.class.isInstance(element)) {
					value = ((JAXBElement<?>) element).getValue();
				} else {
					value = element;
				}
			}
		}
		if (value != null && EnterpriseType.class.isInstance(value)) {
			enterprise = (EnterpriseType) value;
		}
		return enterprise;
	}
}
