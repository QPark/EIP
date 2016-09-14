package com.qpark.eip.model.docmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClusterType.class)
public abstract class ClusterType_ {

	public static volatile SingularAttribute<ClusterType, String> fileName;
	public static volatile ListAttribute<ClusterType, ComplexMappingType> complexMappingType;
	public static volatile ListAttribute<ClusterType, InterfaceMappingType> interfaceMappingType;
	public static volatile SingularAttribute<ClusterType, String> modelVersion;
	public static volatile ListAttribute<ClusterType, ComplexType> complexType;
	public static volatile ListAttribute<ClusterType, DirectMappingType> directMappingType;
	public static volatile SingularAttribute<ClusterType, String> description;
	public static volatile ListAttribute<ClusterType, DefaultMappingType> defaultMappingType;
	public static volatile SingularAttribute<ClusterType, String> version;
	public static volatile SingularAttribute<ClusterType, Long> hjid;
	public static volatile SingularAttribute<ClusterType, String> parentId;
	public static volatile SingularAttribute<ClusterType, String> name;
	public static volatile ListAttribute<ClusterType, String> warning;
	public static volatile SingularAttribute<ClusterType, String> id;
	public static volatile SingularAttribute<ClusterType, String> packageName;
	public static volatile ListAttribute<ClusterType, ComplexUUIDMappingType> complexUUIDMappingType;
	public static volatile ListAttribute<ClusterType, ElementType> elementType;

}

