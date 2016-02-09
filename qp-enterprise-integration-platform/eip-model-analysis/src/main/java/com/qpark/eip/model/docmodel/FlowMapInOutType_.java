package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FlowMapInOutType.class)
public abstract class FlowMapInOutType_ {

	public static volatile SingularAttribute<FlowMapInOutType, String> modelVersion;
	public static volatile SingularAttribute<FlowMapInOutType, String> name;
	public static volatile SingularAttribute<FlowMapInOutType, String> namespace;
	public static volatile ListAttribute<FlowMapInOutType, String> interfaceMappingId;
	public static volatile SingularAttribute<FlowMapInOutType, String> id;
	public static volatile SingularAttribute<FlowMapInOutType, Long> hjid;
	public static volatile SingularAttribute<FlowMapInOutType, String> parentId;
	public static volatile SingularAttribute<FlowMapInOutType, RequestResponseDataType> mapInOut;

}

