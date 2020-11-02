package com.qpark.eip.core.model.analysis.operation;

import java.util.List;

import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.ServiceType;
import com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis;

/**
 * Provides data of model analysis.
 *
 * @author bhausen
 */
public interface ExtendedDataProviderModelAnalysis
		extends DataProviderModelAnalysis {
	/**
	 * Get the {@link ClusterType} with targetNamespace of the modelVersion.
	 *
	 * @param modelVersion
	 *                            the model version.
	 * @param targetNamespace
	 *                            the target namespace.
	 * @return the {@link ClusterType} with targetNamespace of the modelVersion.
	 */
	ClusterType getClusterByTargetNamespace(String modelVersion,
			String targetNamespace);

	/**
	 * Get the list of {@link ComplexType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link ComplexType}.
	 */
	List<ComplexType> getComplexTypesById(String modelVersion,
			List<String> ids);

	/**
	 * Get the list of {@link DataType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link DataType}.
	 */
	List<DataType> getDataTypesById(String modelVersion, List<String> ids);

	/**
	 * Get the list of {@link ElementType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link ElementType}.
	 */
	List<ElementType> getElementTypesById(String modelVersion,
			List<String> ids);

	/**
	 * Get the list of {@link FieldMappingType} with the ids.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param ids
	 *                         the list of ids to return.
	 * @return the list of {@link FieldMappingType}.
	 */
	List<FieldMappingType> getFieldMappingTypesById(String modelVersion,
			List<String> ids);

	/**
	 * Get the list of {@link InterfaceMappingType}s of the flow with id.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param flowId
	 *                         the flow id.
	 * @return the list of {@link InterfaceMappingType}s of the flow with id.
	 */
	List<InterfaceMappingType> getFlowInterfaceMappingTypes(String modelVersion,
			String flowId);

	/**
	 * Get the last model version (lexical compare).
	 *
	 * @return the last model version.
	 */
	String getLastModelVersion();

	/**
	 * Get the list of serviceIds available.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @return the list of serviceIds available.
	 */
	List<String> getRevisions();

	/**
	 * Get the {@link ServiceType} with serviceId of the modelVersion.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @param serviceId
	 *                         the serviceId.
	 * @return the {@link ServiceType} with serviceId of the modelVersion.
	 */
	ServiceType getServiceByServiceId(String modelVersion, String serviceId);

	/**
	 * Get the list of serviceIds available.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @return the list of serviceIds available.
	 */
	List<String> getServiceIds(String modelVersion);

	/**
	 * Get the list of target name spaces available.
	 *
	 * @param modelVersion
	 *                         the model version.
	 * @return the list of target name spaces available.
	 */
	List<String> getTargetNamespaces(String modelVersion);
}
