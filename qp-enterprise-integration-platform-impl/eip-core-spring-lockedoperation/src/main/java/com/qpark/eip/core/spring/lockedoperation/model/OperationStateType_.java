package com.qpark.eip.core.spring.lockedoperation.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OperationStateType.class)
public abstract class OperationStateType_ {

	public static volatile SingularAttribute<OperationStateType, String> operationUUID;
	public static volatile SingularAttribute<OperationStateType, OperationStateEnumType> state;
	public static volatile SingularAttribute<OperationStateType, Long> hjid;

}

