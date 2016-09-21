package com.qpark.eip.core.model.analysis.operation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.model.analysis.AnalysisDao;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.ObjectFactory;

/**
 * Operation get flow interface mapping type on service <code>domain.doc</code>.
 *
 * @author bhausen
 */
public class GetFlowInterfaceMappingTypeOperation {
	/** The bean name to use. */
	public static final String BEAN_NAME = "operationProviderDomainDocGetFlowInterfaceMappingType";
	/** The {@link AnalysisDao}. */
	@Autowired
	private AnalysisDao dao;
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetFlowInterfaceMappingTypeOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	private void getComplexTypes(final String modelVersion,
			final Set<String> fieldMappingIds,
			final Map<String, ComplexType> ctMap) {
		List<FieldMappingType> fieldMappings = this.dao
				.getFieldMappingTypesById(modelVersion,
						Arrays.asList(fieldMappingIds.toArray(new String[0])));
		if (Objects.nonNull(fieldMappings) && fieldMappings.size() > 0) {
			Set<String> ids = new TreeSet<String>();
			fieldMappings.stream()
					.forEach(fm -> fm.getInput().stream()
							.filter(i -> Objects.nonNull(i.getName())
									&& !i.getName().equals("value")
									&& !i.getName().equals("return"))
					.forEach(i -> ids.add(i.getFieldTypeDefinitionId())));
			this.dao.getComplexTypesById(modelVersion,
					Arrays.asList(ids.toArray(new String[0]))).stream()
					.forEach(ct -> ctMap.put(ct.getId(), ct));
			this.getComplexTypes(modelVersion, ids, ctMap);
		}
	}

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetFlowInterfaceMappingTypeRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetFlowInterfaceMappingTypeResponseType}.
	 */
	public final JAXBElement<GetFlowInterfaceMappingTypeResponseType> getFlowInterfaceMappingType(
			final JAXBElement<GetFlowInterfaceMappingTypeRequestType> message) {
		this.logger.debug("+getFlowInterfaceMappingType");
		GetFlowInterfaceMappingTypeRequestType request = message.getValue();
		GetFlowInterfaceMappingTypeResponseType response = this.of
				.createGetFlowInterfaceMappingTypeResponseType();
		long start = System.currentTimeMillis();
		try {
			String modelVersion = this.dao.getLastModelVersion();
			response.getInterfaceType()
					.addAll(this.dao.getFlowInterfaceMappingTypes(modelVersion,
							request.getFlowId()));
			Set<String> fieldMappingIds = new TreeSet<String>();
			response.getInterfaceType().stream().map(i -> i.getFieldMappings())
					.forEach(fml -> fml.stream().forEach(fm -> fieldMappingIds
							.add(fm.getFieldTypeDefinitionId())));
			Map<String, ComplexType> ctMap = new HashMap<String, ComplexType>();
			this.getComplexTypes(modelVersion, fieldMappingIds, ctMap);
			this.logger.info("ComplexTypes used by Flow: {}",
					request.getFlowId());
			ctMap.values().stream().filter(ct -> Objects.nonNull(ct.getName()))
					.sorted((ct1, ct2) -> ct1.getName()
							.compareTo(ct2.getName()))
					.forEach(ct -> this.logger.info("\t{}", ct.getName()));
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" getFlowInterfaceMappingType duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-getFlowInterfaceMappingType #{}",
					response.getInterfaceType().size());
		}
		return this.of.createGetFlowInterfaceMappingTypeResponse(response);
	}
}
