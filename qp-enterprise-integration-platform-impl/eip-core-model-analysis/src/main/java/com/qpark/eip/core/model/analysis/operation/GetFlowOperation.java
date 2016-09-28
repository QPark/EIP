package com.qpark.eip.core.model.analysis.operation;

import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.service.domain.doc.msg.GetFlowRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;

/**
 * Operation get flow on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetFlowOperation {
	/** The bean name to use. */
	public static final String BEAN_NAME = "operationProviderDomainDocGetFlow";

	/**
	 * Translate the pattern do SQL <i>like</i>.
	 *
	 * @param namePattern
	 *            the given name pattern.
	 * @return the translated pattern.
	 */
	private static Optional<String> translateNamePattern(
			final String namePattern) {
		Optional<String> value = Optional.empty();
		if (Objects.nonNull(namePattern)) {
			String s = namePattern.replace('*', '%');
			if (!s.endsWith("%")) {
				s = String.format("%s%s", s, "%");
			}
			if (!s.startsWith("%")) {
				s = String.format("%s%s", "%", s);
			}
			value = Optional.of(s);
		}
		return value;
	}

	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFlowOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetFlowRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetFlowResponseType}.
	 */
	public final JAXBElement<GetFlowResponseType> getFlow(
			final JAXBElement<GetFlowRequestType> message) {
		this.logger.debug("+getFlow");
		GetFlowRequestType request = message.getValue();
		GetFlowResponseType response = this.of.createGetFlowResponseType();
		long start = System.currentTimeMillis();
		try {
			translateNamePattern(request.getNamePattern()).ifPresent(s -> {
				String modelVersion = this.dao.getLastModelVersion();
				response.getFlow()
						.addAll(this.dao.getFlowByNamePattern(modelVersion, s));
			});
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFlow duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getFlow #{}", response.getFlow().size());
		}
		return this.of.createGetFlowResponse(response);
	}
}