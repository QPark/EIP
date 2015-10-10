package com.qpark.maven.plugin.xmapper;

import javax.xml.namespace.QName;

import com.qpark.maven.xmlbeans.ComplexType;

public class ComplexContent {
	ComplexContent(final ComplexType ct, final boolean isDirect,
			final boolean isComplexUUID, final boolean isComplex,
			final boolean isInterfaceType) {
		this.ct = ct;
		this.qName = ct.getType().getName();
		this.isDirect = isDirect;
		this.isComplexUUID = isComplexUUID;
		this.isComplex = isComplex;
		this.isInterfaceType = isInterfaceType;
	}

	public String getFQInterfaceName() {
		return new StringBuffer(this.ct.getPackageName().length() + 1
				+ this.interfaceName.length()).append(this.ct.getPackageName())
				.append(".").append(this.interfaceName).toString();
	}

	ComplexType ct;
	QName qName;
	String packageName;
	String interfaceName;
	boolean isDirect;
	boolean isComplexUUID;
	boolean isComplex;
	boolean isInterfaceType;
}
