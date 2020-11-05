package com.samples.domain.serviceprovider;

import com.qpark.eip.service.domain.doc.msg.GetClusterRequestType;
import com.qpark.eip.service.domain.doc.msg.GetClusterResponseType;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetComplexTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetDataTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetDataTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetElementTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFieldMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowInterfaceMappingTypeResponseType;
import com.qpark.eip.service.domain.doc.msg.GetFlowRequestType;
import com.qpark.eip.service.domain.doc.msg.GetFlowResponseType;
import com.qpark.eip.service.domain.doc.msg.GetRevisionRequestType;
import com.qpark.eip.service.domain.doc.msg.GetRevisionResponseType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceIdResponseType;
import com.qpark.eip.service.domain.doc.msg.GetServiceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetServiceResponseType;
import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceRequestType;
import com.qpark.eip.service.domain.doc.msg.GetTargetNamespaceResponseType;

public interface OperationProviderDomainDoc {

	/**
	 * @param request
	 *                    the {@link GetClusterRequestType}.
	 * @return the {@link GetClusterResponseType}.
	 */
	GetClusterResponseType getCluster(GetClusterRequestType request);

	/**
	 * @param request
	 *                    the {@link GetComplexTypeRequestType}.
	 * @return the {@link GetComplexTypeResponseType}.
	 */
	GetComplexTypeResponseType getComplexType(
			GetComplexTypeRequestType request);

	/**
	 * @param request
	 *                    the {@link GetDataTypeRequestType}.
	 * @return the {@link GetDataTypeResponseType}.
	 */
	GetDataTypeResponseType getDataType(GetDataTypeRequestType request);

	/**
	 * @param request
	 *                    the {@link GetElementTypeRequestType}.
	 * @return the {@link GetElementTypeResponseType}.
	 */
	GetElementTypeResponseType getElementType(
			GetElementTypeRequestType request);

	/**
	 * @param request
	 *                    the {@link GetFieldMappingTypeRequestType}.
	 * @return the {@link GetFieldMappingTypeResponseType}.
	 */
	GetFieldMappingTypeResponseType getFieldMappingType(
			GetFieldMappingTypeRequestType request);

	/**
	 * @param request
	 *                    the {@link GetFlowInterfaceMappingTypeRequestType}.
	 * @return the {@link GetFlowInterfaceMappingTypeResponseType}.
	 */
	GetFlowInterfaceMappingTypeResponseType getFlowInterfaceMappingType(
			GetFlowInterfaceMappingTypeRequestType request);

	/**
	 * @param request
	 *                    the {@link GetFlowRequestType}.
	 * @return the {@link GetFlowResponseType}.
	 */
	GetFlowResponseType getFlow(GetFlowRequestType request);

	/**
	 * @param request
	 *                    the {@link GetRevisionRequestType}.
	 * @return the {@link GetRevisionResponseType}.
	 */
	GetRevisionResponseType getRevision(GetRevisionRequestType request);

	/**
	 * @param request
	 *                    the {@link GetServiceIdRequestType}.
	 * @return the {@link GetServiceIdResponseType}.
	 */
	GetServiceIdResponseType getServiceId(GetServiceIdRequestType request);

	/**
	 * @param request
	 *                    the {@link GetServiceRequestType}.
	 * @return the {@link GetServiceResponseType}.
	 */
	GetServiceResponseType getService(GetServiceRequestType request);

	/**
	 * @param request
	 *                    the {@link GetTargetNamespaceRequestType}.
	 * @return the {@link GetTargetNamespaceResponseType}.
	 */
	GetTargetNamespaceResponseType getTargetNamespace(
			GetTargetNamespaceRequestType request);

}