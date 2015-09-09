/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.maven.plugin.mockoperation;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class OperationProviderMockGenerator {
	private final Log log;
	private final XsdsUtil config;
	private ComplexType ctRequest;
	private ComplexType ctResponse;
	private final ElementType elementRequest;
	private ElementType elementResponse = null;
	private String fqRequestType;
	private String fqResponseType;
	private final String methodName;
	private final String packageName;
	private String requestType;
	private String responseType;
	private final String operationName;
	private final String serviceId;
	private final File outputDirectory;
	private final String failureHandlerClassName;

	public OperationProviderMockGenerator(final XsdsUtil config,
			final File outputDirectory, final ElementType element,
			final String failureHandlerClassName, final Log log) {
		this.config = config;
		this.outputDirectory = outputDirectory;
		this.failureHandlerClassName = failureHandlerClassName;
		this.log = log;
		this.elementRequest = element;
		this.packageName = element.getPackageNameMockOperationProvider();
		this.operationName = element.getOperationName();
		this.serviceId = element.getServiceId();
		this.methodName = element.getMethodName();

		this.elementResponse = XsdsUtil.findResponse(this.elementRequest,
				config.getElementTypes(), config);
		if (this.elementResponse != null) {
			this.ctRequest = new ComplexType(this.elementRequest.getElement()
					.getType(), this.config);
			this.ctResponse = new ComplexType(this.elementResponse.getElement()
					.getType(), this.config);

			this.fqRequestType = this.ctRequest.getClassNameFullQualified();
			this.fqResponseType = this.ctResponse.getClassNameFullQualified();

			this.requestType = this.ctRequest.getClassName();
			this.responseType = this.ctResponse.getClassName();
		}
	}

	public void generate() {
		this.log.debug("+generate");
		if (this.elementResponse != null && this.ctResponse != null
				&& !this.ctResponse.isSimpleType()
				&& !this.ctResponse.isPrimitiveType()) {
			StringBuffer sb = new StringBuffer(1024);
			sb.append("package ");
			sb.append(this.packageName);
			sb.append(";\n");
			sb.append("\n");
			sb.append("import java.io.StringReader;\n");
			sb.append("\n");
			sb.append("import javax.xml.bind.JAXBContext;\n");
			sb.append("import javax.xml.bind.JAXBElement;\n");
			sb.append("import javax.xml.bind.JAXBException;\n");
			sb.append("import javax.xml.bind.Unmarshaller;\n");
			sb.append("\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			// sb.append("import org.springframework.beans.factory.annotation.Qualifier;\n");
			sb.append("import org.springframework.integration.annotation.ServiceActivator;\n");
			sb.append("import org.springframework.stereotype.Component;\n");
			sb.append("\n");
			sb.append("import com.qpark.eip.core.DateUtil;\n");
			sb.append("import ");
			sb.append(this.failureHandlerClassName);
			sb.append(";\n");
			// sb.append("import ");
			// sb.append(this.config.getBasePackageName());
			// sb.append(".ServiceObjectFactory;\n");
			sb.append("import ");
			sb.append(this.config.getBasePackageName());
			sb.append(".RequestProperties;\n");
			sb.append("import ");
			sb.append(this.fqRequestType);
			sb.append(";\n");
			sb.append("import ");
			sb.append(this.fqResponseType);
			sb.append(";\n");
			sb.append("import ");
			sb.append(this.elementRequest.getPackageName());
			sb.append(".ObjectFactory;\n");
			sb.append("import com.springsource.insight.annotation.InsightEndPoint;\n");
			sb.append("\n");
			sb.append("/**\n");
			sb.append(" * Operation ");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append(" on service <code>");
			sb.append(this.serviceId);
			sb.append("</code>.\n");
			sb.append(" * <pre>");
			sb.append(Util.getGeneratedAt());
			sb.append("</pre>\n");
			sb.append(" * @author bhausen\n");
			sb.append(" */\n");
			sb.append("//@Component(\"");
			sb.append(this.elementRequest.getBeanIdMockOperationProvider());
			sb.append("\")\n");
			sb.append("public class ");
			sb.append(this.elementRequest.getClassNameMockOperationProvider());
			sb.append(" {\n");

			sb.append("\t/** The {@link Logger}. */\n");
			sb.append("\tprivate final org.slf4j.Logger logger = org.slf4j.LoggerFactory\n");
			sb.append("\t\t\t.getLogger(");
			sb.append(this.elementRequest.getClassNameMockOperationProvider());
			sb.append(".class);\n");

			// sb.append("	/** The {@link ServiceObjectFactory}. */\n");
			// sb.append("	@Autowired\n");
			// sb.append("	private ServiceObjectFactory of;\n");
			sb.append("\n");
			sb.append("	/** The {@link ObjectFactory}. */\n");
			sb.append("	private final ObjectFactory of = new ObjectFactory();\n");
			sb.append("\n");

			sb.append("\t/**\n");
			sb.append("\t * @param message the {@link JAXBElement} containing a {@link ");
			sb.append(this.requestType);
			sb.append("}.\n");
			sb.append("\t * @return the {@link JAXBElement} with a {@link ");
			sb.append(this.responseType);
			sb.append("}.\n");
			sb.append("\t */\n");

			sb.append("	@InsightEndPoint\n");
			sb.append("	@ServiceActivator\n");
			sb.append("	public final JAXBElement<");
			sb.append(this.responseType);
			sb.append("> ");
			sb.append(this.methodName);
			sb.append("(\n");
			sb.append("			final JAXBElement<");
			sb.append(this.requestType);
			sb.append("> message) {\n");

			sb.append("\t\tthis.logger.debug(\"+");
			sb.append(this.methodName);
			sb.append("\");\n");

			sb.append("\t\t");
			sb.append(this.requestType);
			sb.append("	request = message.getValue();\n");

			sb.append("\t\t");
			sb.append(this.responseType);
			sb.append(" response = this.of.\n\t\t\t\tcreate");
			sb.append(this.responseType);
			sb.append("();\n");

			sb.append("\t\tRequestProperties<");
			sb.append(this.requestType);
			sb.append("> rp = new RequestProperties<");
			sb.append(this.requestType);
			sb.append(">(request, response);\n");

			sb.append("\t\tlong start = System.currentTimeMillis();\n");

			sb.append("\t\ttry {\n");

			sb.append("\t\t\t");
			sb.append(this.responseType);
			sb.append(" responseSample = this.getSampleResponseObject();\n");

			sb.append("\t\t\tif (responseSample != null) {\n");
			sb.append("\t\t\t\tresponse = responseSample;\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\t// rp.addFailures(response);\n");
			sb.append("\t\t\tresponse.getFailure().clear();\n");
			sb.append("\t\t\t// The operation {0} of service {1} is not implement!!\n");
			sb.append("\t\t\tresponse.getFailure().add(\n");
			sb.append("\t\t\t\tFailureHandler.getFailureType(\"E_NOT_IMPLEMENTED_OPERATION\", \n");
			sb.append("\t\t\t\t\t\"");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append("\", \"");
			sb.append(this.serviceId);
			sb.append("\"));\n");

			sb.append("\t\t} catch (Throwable e) {\n");

			sb.append("\t\t\t/* Add a not covered error to the response. */\n");
			sb.append("\t\t\tthis.logger.error(e.getMessage(), e);\n");
			sb.append("\t\t\tFailureHandler.handleException(e, rp, \"E_ALL_NOT_KNOWN_ERROR\",\n");
			sb.append("\t\t\t\t\tthis.logger);\n");

			sb.append("\t\t} finally {\n");

			sb.append("\t\t\tthis.logger.debug(\" ");
			sb.append(this.methodName);
			sb.append(" duration {}\",\n");
			sb.append("\t\t\t\t\tDateUtil.getDuration(start, System.currentTimeMillis()));\n");

			sb.append("\t\t\tthis.logger.debug(\"-");
			sb.append(this.methodName);
			sb.append(" #{}, #f{}\",\n");
			sb.append("\t\t\t\t\tresponse/*.get()*/ != null ? 1 : 0, response.getFailure()\n");
			sb.append("\t\t\t\t\t\t\t.size());\n");

			sb.append("\t\t}\n");

			sb.append("		return this.of\n");
			sb.append("				.create");
			sb.append(this.elementResponse.getClassNameObject());
			sb.append("(response);\n");
			sb.append("	}\n");

			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * @return a mock {@link ");
			sb.append(this.responseType);
			sb.append("}.\n");
			sb.append("\t */\n");
			sb.append("\tprivate ");
			sb.append(this.responseType);
			sb.append(" getSampleResponseObject() {\n");
			sb.append("\t\t");
			sb.append(this.responseType);
			sb.append(" mock = null;\n");
			sb.append("\t\tString xml = this.getSampleXml();\n");
			sb.append("\t\ttry {\n");
			sb.append("\t\t\tJAXBContext jaxbContext = JAXBContext\n");
			sb.append("\t\t\t\t\t.newInstance(\"");
			sb.append(this.ctRequest.getPackageName());
			sb.append("\");\n");
			sb.append("\t\t\tUnmarshaller unmarshaller = jaxbContext.createUnmarshaller();\n");
			sb.append("\t\t\tJAXBElement<");
			sb.append(this.responseType);
			sb.append("> jaxb = (JAXBElement<");
			sb.append(this.responseType);
			sb.append(">) unmarshaller.unmarshal(new StringReader(xml));\n");
			sb.append("\t\t\tif (jaxb != null) {\n");
			sb.append("\t\t\t\tmock = jaxb.getValue();\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t} catch (Exception e) {\n");
			sb.append("\t\t\tthis.logger.debug(\"");
			sb.append(this.operationName);
			sb.append(" generate sample message error: {}\", e.getMessage());\n");
			sb.append("\t\t\tmock = null;\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn mock;\n");
			sb.append("\t}\n");

			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * @return a sample xml.\n");
			sb.append("\t */\n");
			sb.append("\tprivate String getSampleXml() {\n");
			sb.append(XsdsUtil.getSampleCodeing(this.ctResponse.getType(),
					this.elementResponse.getElement().getName().getLocalPart()));
			sb.append("\t\treturn sb.toString();\n");
			sb.append("\t}\n");

			sb.append("}\n");
			sb.append("\n");
			File f = Util.getFile(
					this.outputDirectory,
					this.packageName,
					new StringBuffer()
							.append(this.elementRequest
									.getClassNameMockOperationProvider())
							.append(".java").toString());
			this.log.info(new StringBuffer().append("Write ").append(
					f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sb.toString());
			} catch (Exception e) {
				this.log.error(e.getMessage());
				e.printStackTrace();
			}
			this.log.debug("-generate");
		}
	}
}
