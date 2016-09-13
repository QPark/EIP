package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FlowType.class)
public abstract class FlowType_ {

	public static volatile SingularAttribute<FlowType, String> modelVersion;
	public static volatile SingularAttribute<FlowType, String> name;
	public static volatile SingularAttribute<FlowType, String> namespace;
	public static volatile SingularAttribute<FlowType, RequestResponseDataType> invokeFlowDefinition;
	public static volatile SingularAttribute<FlowType, String> description;
	public static volatile SingularAttribute<FlowType, String> id;
	public static volatile SingularAttribute<FlowType, String> clusterId;
	public static volatile SingularAttribute<FlowType, String> shortName;
	public static volatile SingularAttribute<FlowType, FlowProcessType> processResponse;
	public static volatile SingularAttribute<FlowType, Long> hjid;
	public static volatile SingularAttribute<FlowType, String> parentId;
	public static volatile SingularAttribute<FlowType, FlowProcessType> executeRequest;

}

