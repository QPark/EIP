package com.qpark.eip.core.spring.lockedoperation.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OperationLockControllType.class)
public abstract class OperationLockControllType_ {

	public static volatile SingularAttribute<OperationLockControllType, Date> lockDateItem;
	public static volatile SingularAttribute<OperationLockControllType, String> serverIpAddress;
	public static volatile SingularAttribute<OperationLockControllType, String> serverName;
	public static volatile SingularAttribute<OperationLockControllType, String> operationName;
	public static volatile SingularAttribute<OperationLockControllType, Long> hjid;

}

