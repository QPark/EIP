package com.qpark.eip.core.model.analysis.operation;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;

/**
 * Operation get complex type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetComplexTypeOperation {
	/** The bean name to use. */
	public static final String BEAN_NAME = "operationProviderDomainDocGetComplexType";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetComplexTypeOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetComplexTypeRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetComplexTypeResponseType}
	 *         .
	 */
	public final JAXBElement<GetComplexTypeResponseType> getComplexType(
			final JAXBElement<GetComplexTypeRequestType> message) {
		this.logger.debug("+getComplexType");
		GetComplexTypeRequestType request = message.getValue();
		GetComplexTypeResponseType response = this.of
				.createGetComplexTypeResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.getComplexType().addAll(this.dao
					.getComplexTypesById(modelVersion, request.getId()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getComplexType duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getComplexType #{}",
					response.getComplexType().size());
		}
		return this.of.createGetComplexTypeResponse(response);
	}
}
