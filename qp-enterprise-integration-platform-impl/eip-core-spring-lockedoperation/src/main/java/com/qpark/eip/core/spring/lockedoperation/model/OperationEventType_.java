package com.qpark.eip.core.spring.lockedoperation.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OperationEventType.class)
public abstract class OperationEventType_ {

	public static volatile SingularAttribute<OperationEventType, Date> operationTimeItem;
	public static volatile SingularAttribute<OperationEventType, String> operationUUID;
	public static volatile SingularAttribute<OperationEventType, OperationEventEnumType> event;
	public static volatile SingularAttribute<OperationEventType, Long> hjid;

}

