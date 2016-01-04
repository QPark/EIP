package com.qpark.eip.core.model.analysis;

import java.util.UUID;

import com.qpark.eip.model.docmodel.ClusterType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.DomainType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.FieldType;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.ServiceType;

class UUIDProvider {
    private final Analysis analysis;

    UUIDProvider(final Analysis analysis) {
	this.analysis = analysis;
    }

    /**
     * Set the UUID of the {@link DomainType}.
     *
     * @param domain
     *            the {@link DomainType}.
     */
    public void setUUID(final DomainType domain) {
	domain.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(DomainType.class.getName())
		.append("#").append(domain.getName()).toString().getBytes())));
	this.analysis.add(domain.getId(), domain);
    }

    public void setUUID(final ClusterType cluster) {
	cluster.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(ClusterType.class.getName())
		.append("#").append(cluster.getName()).toString().getBytes())));
	this.analysis.add(cluster.getId(), cluster);
    }

    public void setUUID(final ServiceType service) {
	service.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(ServiceType.class.getName())
		.append("#").append(service.getName()).toString().getBytes())));
	this.analysis.add(service.getId(), service);
    }

    public void setUUID(final OperationType operation) {
	operation.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128)
		.append(OperationType.class.getName()).append("#").append(operation.getName()).toString().getBytes())));
	this.analysis.add(operation.getId(), operation);
    }

    public void setUUID(final ElementType element) {
	element.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(ElementType.class.getName())
		.append("#").append(element.getName()).toString().getBytes())));
	this.analysis.add(element.getId(), element);
    }

    public void setUUID(final DataType element) {
	element.setId(this.getDataTypeUUID(element.getName()));
	this.analysis.add(element.getId(), element);
    }

    public String getDataTypeUUID(final String elementName) {
	return String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(DataType.class.getName()).append("#")
		.append(elementName).toString().getBytes()));
    }

    public void setUUID(final FieldType field) {
	field.setId(String.valueOf(UUID.nameUUIDFromBytes(new StringBuffer(128).append(FieldType.class.getName())
		.append("#").append(field.getParentId()).append("#").append(field.getName()).toString().getBytes())));
	this.analysis.add(field.getId(), field);
    }
}
