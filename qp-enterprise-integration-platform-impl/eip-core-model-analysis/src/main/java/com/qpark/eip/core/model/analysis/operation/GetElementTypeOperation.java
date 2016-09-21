package com.qpark.eip.core.model.analysis.operation;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;

/**
 * Operation get element type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetElementTypeOperation {
	/** The bean name to use. */
	public static final String BEAN_NAME = "operationProviderDomainDocGetElementType";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetElementTypeOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetElementTypeRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetElementTypeResponseType}
	 *         .
	 */
	public final JAXBElement<GetElementTypeResponseType> getElementType(
			final JAXBElement<GetElementTypeRequestType> message) {
		this.logger.debug("+getElementType");
		GetElementTypeRequestType request = message.getValue();
		GetElementTypeResponseType response = this.of
				.createGetElementTypeResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.getElementType().addAll(this.dao
					.getElementTypesById(modelVersion, request.getId()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getElementType duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getElementType #{}",
					response.getElementType().size());
		}
		return this.of.createGetElementTypeResponse(response);
	}
}
