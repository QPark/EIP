package com.qpark.eip.model.reporting;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReportHeaderDataType.class)
public abstract class ReportHeaderDataType_ {

	public static volatile SingularAttribute<ReportHeaderDataType, Boolean> finalRow;
	public static volatile ListAttribute<ReportHeaderDataType, ReportMetaDataType> reportMetaData;
	public static volatile SingularAttribute<ReportHeaderDataType, Integer> rowNumber;
	public static volatile SingularAttribute<ReportHeaderDataType, Long> hjid;

}

