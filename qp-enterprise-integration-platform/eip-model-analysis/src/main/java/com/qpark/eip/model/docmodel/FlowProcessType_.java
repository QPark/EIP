package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FlowProcessType.class)
public abstract class FlowProcessType_ {

	public static volatile SingularAttribute<FlowProcessType, String> modelVersion;
	public static volatile ListAttribute<FlowProcessType, FlowRuleType> rule;
	public static volatile SingularAttribute<FlowProcessType, String> requestFieldDescription;
	public static volatile SingularAttribute<FlowProcessType, Long> hjid;
	public static volatile SingularAttribute<FlowProcessType, String> parentId;
	public static volatile ListAttribute<FlowProcessType, String> executionOrder;
	public static volatile ListAttribute<FlowProcessType, FlowSubRequestType> subRequest;
	public static volatile ListAttribute<FlowProcessType, FlowFilterType> filter;
	public static volatile SingularAttribute<FlowProcessType, String> name;
	public static volatile SingularAttribute<FlowProcessType, String> namespace;
	public static volatile SingularAttribute<FlowProcessType, RequestResponseDataType> requestResponse;
	public static volatile SingularAttribute<FlowProcessType, String> id;
	public static volatile ListAttribute<FlowProcessType, FlowMapInOutType> mapInOut;
	public static volatile SingularAttribute<FlowProcessType, String> responseFieldDescription;

}

