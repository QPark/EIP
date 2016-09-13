package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EnterpriseType.class)
public abstract class EnterpriseType_ {

	public static volatile ListAttribute<EnterpriseType, FlowType> flows;
	public static volatile SingularAttribute<EnterpriseType, String> modelVersion;
	public static volatile SingularAttribute<EnterpriseType, String> name;
	public static volatile ListAttribute<EnterpriseType, DomainType> domains;
	public static volatile SingularAttribute<EnterpriseType, Long> hjid;
	public static volatile ListAttribute<EnterpriseType, DataType> basicDataTypes;

}

