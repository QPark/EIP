package com.qpark.eip.core.model.analysis.operation;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetDataTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetDataTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetDataType;

/**
 * Operation get data type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetDataTypeOperation implements GetDataType {
	/** The bean name to use. */
	public static final String BEAN_NAME = "operationProviderDomainDocGetDataType";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetDataTypeOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetDataTypeRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetDataTypeResponseType}.
	 */
	@Override
	public final JAXBElement<GetDataTypeResponseType> invoke(
			final JAXBElement<GetDataTypeRequestType> message) {
		this.logger.debug("+getDataType");
		GetDataTypeRequestType request = message.getValue();
		GetDataTypeResponseType response = this.of
				.createGetDataTypeResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.getDataType().addAll(
					this.dao.getDataTypesById(modelVersion, request.getId()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getDataType duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getDataType #{}",
					response.getDataType().size());
		}
		return this.of.createGetDataTypeResponse(response);
	}
}
