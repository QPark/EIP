package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ComplexType.class)
public abstract class ComplexType_ extends com.qpark.eip.model.docmodel.DataType_ {

	public static volatile SingularAttribute<ComplexType, Boolean> isMappingResponseType;
	public static volatile SingularAttribute<ComplexType, String> descendedFromId;
	public static volatile ListAttribute<ComplexType, FieldType> field;
	public static volatile SingularAttribute<ComplexType, Boolean> isMappingRequestType;
	public static volatile SingularAttribute<ComplexType, Boolean> isFlowOutputType;
	public static volatile SingularAttribute<ComplexType, Boolean> isFlowInputType;

}

