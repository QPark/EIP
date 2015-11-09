package com.qpark.maven.plugin.flowmapper;

import javax.xml.namespace.QName;

import com.qpark.maven.xmlbeans.ComplexType;

public class ComplexRequestResponse {
	ComplexRequestResponse(final ComplexType request, final ComplexType response) {
		this.request = request;
		this.requestQName = request.getType().getName();
		this.response = response;
		this.responseQName = response.getType().getName();
	}

	ComplexType request;
	QName requestQName;
	ComplexType response;
	QName responseQName;
	String packageName;
	String interfaceName;
}
