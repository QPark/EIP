/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis;

import java.util.UUID;

import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.DomainType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.FieldType;
import com.qpark.eip.model.docmodel.FlowFilterType;
import com.qpark.eip.model.docmodel.FlowMapInOutType;
import com.qpark.eip.model.docmodel.FlowProcessType;
import com.qpark.eip.model.docmodel.FlowSubRequestType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.RequestResponseDataType;
import com.qpark.eip.model.docmodel.ServiceType;

public class UUIDProvider {
    private final Analysis analysis;

    public UUIDProvider(final Analysis analysis) {
	this.analysis = analysis;
    }

    /**
     * Set the UUID of the {@link DomainType}.
     *
     * @param value
     *            the {@link DomainType}.
     */
    public void setUUID(final DomainType value) {
	value.setId(String
		.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(DomainType.class.getName()).append("#")
			.append(value.getModelVersion()).append("#").append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final ClusterType value) {
	value.setId(String
		.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(ClusterType.class.getName()).append("#")
			.append(value.getModelVersion()).append("#").append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final FlowType value) {
	value.setId(
		String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(FlowType.class.getName()).append("#")
			.append(value.getModelVersion()).append("#").append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final FlowProcessType value) {
	value.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(FlowProcessType.class.getName())
		.append("#").append(value.getModelVersion()).append("#").append(value.getName()).append("#")
		.append(value.getParentId()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final FlowSubRequestType value) {
	value.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128)
		.append(FlowSubRequestType.class.getName()).append("#").append("#").append(value.getModelVersion())
		.append(value.getName()).append("#").append(value.getParentId()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final FlowFilterType value) {
	value.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(FlowFilterType.class.getName())
		.append("#").append(value.getModelVersion()).append("#").append(value.getName()).append("#")
		.append(value.getParentId()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final FlowMapInOutType value) {
	value.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(FlowMapInOutType.class.getName())
		.append("#").append(value.getModelVersion()).append("#").append(value.getName()).append("#")
		.append(value.getParentId()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final RequestResponseDataType value) {
	value.setId(String.valueOf(
		UUID.nameUUIDFromBytes(new StringBuffer(128).append(RequestResponseDataType.class.getName()).append("#")
			.append(value.getModelVersion()).append("#").append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final ServiceType value) {
	value.setId(String
		.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(ServiceType.class.getName()).append("#")
			.append(value.getModelVersion()).append("#").append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final OperationType value) {
	value.setId(String
		.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(OperationType.class.getName()).append("#")
			.append(value.getModelVersion()).append("#").append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final ElementType value) {
	value.setId(String
		.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(ElementType.class.getName()).append("#")
			.append(value.getModelVersion()).append("#").append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }

    public void setUUID(final DataType value) {
	value.setId(this.getDataTypeUUID(value.getName(), value.getModelVersion()));
	this.analysis.add(value.getId(), value);
    }

    public String getDataTypeUUID(final String dataTypeName, final String modelVersion) {
	return String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(DataType.class.getName()).append("#")
		.append(dataTypeName).toString().getBytes()));
    }

    public void setUUID(final FieldType value) {
	value.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(FieldType.class.getName())
		.append("#").append(value.getParentId()).append("#").append(value.getModelVersion()).append("#")
		.append(value.getName()).toString().getBytes())));
	this.analysis.add(value.getId(), value);
    }
}
