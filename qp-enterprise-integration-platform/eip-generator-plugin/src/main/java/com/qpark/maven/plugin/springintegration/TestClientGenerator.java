/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.maven.plugin.springintegration;

import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class TestClientGenerator {
	private final Log log;
	private final XsdsUtil config;
	private ComplexType ctRequest;
	private ComplexType ctResponse;
	private final ElementType elementRequest;
	private ElementType elementResponse = null;
	private final String methodName;
	private String requestType;
	private String responseType;
	private final String serviceId;
	private final boolean useInsightAnnotation;

	public TestClientGenerator(final XsdsUtil config, final ElementType element,
			final boolean useInsightAnnotation, final Log log) {
		this.config = config;
		this.log = log;
		this.elementRequest = element;
		this.serviceId = element.getServiceId();
		this.methodName = element.getMethodName();
		this.useInsightAnnotation = useInsightAnnotation;

		this.elementResponse = XsdsUtil.findResponse(this.elementRequest,
				config.getElementTypes(), config);
		if (this.elementResponse != null) {
			this.ctRequest = new ComplexType(
					this.elementRequest.getElement().getType(), this.config);
			this.ctResponse = new ComplexType(
					this.elementResponse.getElement().getType(), this.config);
			this.ctRequest.initChildren(config);
			this.requestType = this.ctRequest.getClassName();
			this.responseType = this.ctResponse.getClassName();
		}
	}

	public Set<String> getImports() {
		Set<String> imports = new TreeSet<String>();
		imports.add("javax.xml.bind.JAXBElement");
		imports.add(this.ctRequest.getClassNameFullQualified());
		imports.add(this.ctResponse.getClassNameFullQualified());
		if (this.useInsightAnnotation) {
			imports.add("com.springsource.insight.annotation.InsightEndPoint");
		}
		return imports;
	}

	public String generate() {
		this.log.debug("+generate");
		StringBuffer sb = new StringBuffer(1024);
		if (this.elementResponse != null && this.ctResponse != null
				&& !this.ctResponse.isSimpleType()
				&& !this.ctResponse.isPrimitiveType()) {

			sb.append("\t/**\n");
			sb.append("\t * Invoke operation ");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append(" on service <code>");
			sb.append(this.serviceId);
			sb.append("</code>.\n");
			sb.append("\t * @param message the {@link ");
			sb.append(this.requestType);
			sb.append("}.\n");
			sb.append("\t * @return a {@link ");
			sb.append(this.responseType);
			sb.append("}.\n");
			sb.append("\t */\n");

			if (this.useInsightAnnotation) {
				sb.append("	@InsightEndPoint\n");
			}
			sb.append("	public final ");
			sb.append(this.responseType);
			sb.append(" ");
			sb.append(this.methodName);
			sb.append("(\n");
			sb.append("\t\t\tfinal ");
			sb.append(this.requestType);
			sb.append(" request) {\n");

			sb.append("\t\tthis.logger.debug(\"+");
			sb.append(this.methodName);
			sb.append("\");\n");
			// sb.append("\t\tlong start = System.currentTimeMillis();\n");

			sb.append("\t\t");
			sb.append(this.responseType);
			sb.append(" value = this.objectFactory.create");
			sb.append(this.responseType);
			sb.append("();\n");

			sb.append("\t\tif (request != null) {\n");

			sb.append("\t\t\t@SuppressWarnings(\"unchecked\")\n");

			sb.append("\t\t\tJAXBElement<");
			sb.append(this.responseType);
			sb.append("> response = \n");
			sb.append("\t\t\t\t(JAXBElement<");
			sb.append(this.responseType);
			sb.append(">)\n");
			sb.append(
					"\t\t\t\t\tthis.getWebServiceTemplate().marshalSendAndReceive(\n");
			sb.append("\t\t\t\t\tthis.objectFactory.create");
			sb.append(this.elementRequest.getOperationName());
			sb.append("Request(request));\n");
			sb.append("\t\t\tif (response !=null) {\n");
			sb.append("\t\t\t\tvalue = response.getValue();\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");
			sb.append("\t\tthis.logger.debug(\"-");
			sb.append(this.methodName);
			sb.append("\");\n");
			sb.append("\t\treturn value;\n");
			sb.append("\t}\n");

			sb.append("\t/**\n");
			sb.append("\t * @return get a {@link ");
			sb.append(this.requestType);
			sb.append("}\n");
			sb.append("\t * @see ObjectFactory#create");
			sb.append(this.requestType);
			sb.append("()\n");
			sb.append("\t */\n");
			sb.append("\tpublic ");
			sb.append(this.requestType);
			sb.append(" create");
			sb.append(this.requestType);
			sb.append("() {\n");
			sb.append("\t\treturn this.getObjectFactory().create");
			sb.append(this.requestType);
			sb.append("();\n");
			sb.append("\t}\n");

		}
		return sb.toString();
	}
}
