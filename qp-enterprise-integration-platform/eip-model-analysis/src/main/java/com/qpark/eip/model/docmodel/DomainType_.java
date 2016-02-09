package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DomainType.class)
public abstract class DomainType_ {

	public static volatile ListAttribute<DomainType, ClusterType> cluster;
	public static volatile SingularAttribute<DomainType, String> modelVersion;
	public static volatile ListAttribute<DomainType, ServiceType> service;
	public static volatile SingularAttribute<DomainType, String> subname;
	public static volatile SingularAttribute<DomainType, String> subsubsubname;
	public static volatile SingularAttribute<DomainType, String> name;
	public static volatile SingularAttribute<DomainType, String> subsubname;
	public static volatile SingularAttribute<DomainType, String> description;
	public static volatile SingularAttribute<DomainType, String> id;
	public static volatile SingularAttribute<DomainType, Long> hjid;
	public static volatile SingularAttribute<DomainType, String> parentId;

}

