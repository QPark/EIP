package com.qpark.eip.model.reporting;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReportType.class)
public abstract class ReportType_ {

	public static volatile SingularAttribute<ReportType, String> reportUUID;
	public static volatile SingularAttribute<ReportType, String> environment;
	public static volatile ListAttribute<ReportType, ReportContentType> reportContent;
	public static volatile SingularAttribute<ReportType, String> reportName;
	public static volatile SingularAttribute<ReportType, String> artefactVersion;
	public static volatile SingularAttribute<ReportType, String> artefact;
	public static volatile ListAttribute<ReportType, ReportHeaderDataType> reportHeaderData;
	public static volatile SingularAttribute<ReportType, Date> updatedItem;
	public static volatile SingularAttribute<ReportType, ReportInfoType> reportInfo;
	public static volatile SingularAttribute<ReportType, Date> createdItem;
	public static volatile SingularAttribute<ReportType, Long> hjid;

}

