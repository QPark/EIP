package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ServiceType.class)
public abstract class ServiceType_ {

	public static volatile SingularAttribute<ServiceType, String> modelVersion;
	public static volatile SingularAttribute<ServiceType, String> name;
	public static volatile SingularAttribute<ServiceType, String> namespace;
	public static volatile SingularAttribute<ServiceType, String> description;
	public static volatile SingularAttribute<ServiceType, String> id;
	public static volatile SingularAttribute<ServiceType, String> clusterId;
	public static volatile SingularAttribute<ServiceType, String> packageName;
	public static volatile SingularAttribute<ServiceType, String> securityRoleName;
	public static volatile SingularAttribute<ServiceType, String> serviceId;
	public static volatile ListAttribute<ServiceType, OperationType> operation;
	public static volatile SingularAttribute<ServiceType, Long> hjid;
	public static volatile SingularAttribute<ServiceType, String> parentId;

}

