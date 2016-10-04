package com.qpark.eip.core.model.analysis.operation;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetFieldMappingType;

/**
 * Operation get field mapping type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetFieldMappingTypeOperation implements GetFieldMappingType {
	/** The bean name to use. */
	public static final String BEAN_NAME = "operationProviderDomainDocGetFieldMappingType";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFieldMappingTypeOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetFieldMappingTypeRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetFieldMappingTypeResponseType} .
	 */
	@Override
	public final JAXBElement<GetFieldMappingTypeResponseType> invoke(
			final JAXBElement<GetFieldMappingTypeRequestType> message) {
		this.logger.debug("+getFieldMappingType");
		GetFieldMappingTypeRequestType request = message.getValue();
		GetFieldMappingTypeResponseType response = this.of
				.createGetFieldMappingTypeResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.getFieldMappingType().addAll(this.dao
					.getFieldMappingTypesById(modelVersion, request.getId()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFieldMappingType duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getFieldMappingType #{}",
					response.getFieldMappingType().size());
		}
		return this.of.createGetFieldMappingTypeResponse(response);
	}
}
