package com.qpark.eip.core.model.analysis.operation;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;
import com.qpark.eip.service.domain.doc.msg.gateway.GetTargetNamespace;

/**
 * Operation get target namespace on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetTargetNamespaceOperation implements GetTargetNamespace {
	/** The bean name to use. */
	public static final String BEAN_NAME = "operationProviderDomainDocGetTargetNamespace";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetTargetNamespaceOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetTargetNamespaceRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetTargetNamespaceResponseType}.
	 */
	@Override
	public final JAXBElement<GetTargetNamespaceResponseType> invoke(
			final JAXBElement<GetTargetNamespaceRequestType> message) {
		this.logger.debug("+getTargetNamespace");
		GetTargetNamespaceResponseType response = this.of
				.createGetTargetNamespaceResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.getTargetNamespace()
					.addAll(this.dao.getTargetNamespaces(modelVersion));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getTargetNamespace duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getTargetNamespace #{}",
					response.getTargetNamespace().size());
		}
		return this.of.createGetTargetNamespaceResponse(response);
	}
}
