package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OperationType.class)
public abstract class OperationType_ {

	public static volatile SingularAttribute<OperationType, ElementType> request;
	public static volatile SingularAttribute<OperationType, String> modelVersion;
	public static volatile SingularAttribute<OperationType, ElementType> response;
	public static volatile SingularAttribute<OperationType, String> name;
	public static volatile SingularAttribute<OperationType, String> namespace;
	public static volatile SingularAttribute<OperationType, String> description;
	public static volatile SingularAttribute<OperationType, String> id;
	public static volatile SingularAttribute<OperationType, String> securityRoleName;
	public static volatile SingularAttribute<OperationType, String> shortName;
	public static volatile ListAttribute<OperationType, FlowType> invokes;
	public static volatile SingularAttribute<OperationType, Long> hjid;
	public static volatile SingularAttribute<OperationType, String> parentId;

}

