package com.qpark.eip.model.docmodel;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SimpleType.class)
public abstract class SimpleType_ extends com.qpark.eip.model.docmodel.DataType_ {

	public static volatile SingularAttribute<SimpleType, Integer> restrictionMaxLength;
	public static volatile SingularAttribute<SimpleType, Integer> restrictionMinLength;
	public static volatile SingularAttribute<SimpleType, BigDecimal> restrictionMinExclusive;
	public static volatile SingularAttribute<SimpleType, BigDecimal> restrictionMinInclusive;
	public static volatile SingularAttribute<SimpleType, Integer> restrictionTotalDigits;
	public static volatile SingularAttribute<SimpleType, String> restrictionPattern;
	public static volatile SingularAttribute<SimpleType, String> defaultValue;
	public static volatile SingularAttribute<SimpleType, BigDecimal> restrictionMaxInclusive;
	public static volatile SingularAttribute<SimpleType, Integer> restrictionFractionDigits;
	public static volatile SingularAttribute<SimpleType, BigDecimal> restrictionMaxExclusive;
	public static volatile SingularAttribute<SimpleType, String> descendedFromId;
	public static volatile SingularAttribute<SimpleType, Integer> restrictionLength;
	public static volatile ListAttribute<SimpleType, String> restrictionEnumeration;

}

