/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.mockoperation;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
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
	private final boolean useInsightAnnotation;
	private final boolean addSampleResponse;
	private final String eipVersion;

	public OperationProviderMockGenerator(final XsdsUtil config,
			final File outputDirectory, final ElementType element,
			final boolean useInsightAnnotation, boolean addSampleResponse,
			final String eipVersion, final Log log) {
		this.config = config;
		this.outputDirectory = outputDirectory;
		this.log = log;
		this.elementRequest = element;
		this.packageName = element.getPackageNameMockOperationProvider();
		this.operationName = element.getOperationName();
		this.serviceId = element.getServiceId();
		this.methodName = element.getMethodName();
		this.useInsightAnnotation = useInsightAnnotation;
		this.addSampleResponse = addSampleResponse;
		this.eipVersion = eipVersion;

		this.elementResponse = XsdsUtil.findResponse(this.elementRequest,
				config.getElementTypes(), config);
		if (this.elementResponse != null) {
			this.ctRequest = new ComplexType(
					this.elementRequest.getElement().getType(), this.config);
			this.ctResponse = new ComplexType(
					this.elementResponse.getElement().getType(), this.config);

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
			sb.append("import java.util.concurrent.TimeUnit;\n");
			sb.append("\n");
			if (addSampleResponse) {
				sb.append("import javax.xml.bind.JAXBContext;\n");
				sb.append("import javax.xml.bind.JAXBElement;\n");
				sb.append("import javax.xml.bind.JAXBException;\n");
				sb.append("import javax.xml.bind.Unmarshaller;\n");
				sb.append("\n");
			} else {
				sb.append("import javax.xml.bind.JAXBElement;\n");
				sb.append("\n");
			}
			sb.append(
					"import org.springframework.beans.factory.annotation.Autowired;\n");
			// sb.append("import
			// org.springframework.beans.factory.annotation.Qualifier;\n");
			sb.append(
					"import org.springframework.integration.annotation.ServiceActivator;\n");
			sb.append("import org.springframework.stereotype.Component;\n");
			sb.append("\n");
			sb.append("import ");
			sb.append(this.fqRequestType);
			sb.append(";\n");
			sb.append("import ");
			sb.append(this.fqResponseType);
			sb.append(";\n");
			sb.append("import ");
			sb.append(this.elementRequest.getPackageName());
			sb.append(".ObjectFactory;\n");
			if (this.useInsightAnnotation) {
//				sb.append(
//						"import com.springsource.insight.annotation.InsightEndPoint;\n");
			}
			sb.append("\n");
			sb.append("/**\n");
			sb.append(" * Operation ");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append(" on service <code>");
			sb.append(this.serviceId);
			sb.append("</code>.\n");
			sb.append(Util.getGeneratedAtJavaDocClassHeader(this.getClass(),
					this.eipVersion));
			sb.append(" */\n");
			sb.append("//@Component(\"");
			sb.append(this.elementRequest.getBeanIdMockOperationProvider());
			sb.append("\")\n");
			sb.append("public class ");
			sb.append(this.elementRequest.getClassNameMockOperationProvider());
			sb.append(" {\n");

			sb.append("\t/** The {@link Logger}. */\n");
			sb.append(
					"\tprivate final org.slf4j.Logger logger = org.slf4j.LoggerFactory\n");
			sb.append("\t\t\t.getLogger(");
			sb.append(this.elementRequest.getClassNameMockOperationProvider());
			sb.append(".class);\n");

			sb.append("\n");
			sb.append("	/** The {@link ObjectFactory}. */\n");
			sb.append(
					"	private final ObjectFactory of = new ObjectFactory();\n");
			sb.append("\n");

			sb.append("\t/**\n");
			sb.append(
					"\t * @param message the {@link JAXBElement} containing a {@link ");
			sb.append(this.requestType);
			sb.append("}.\n");
			sb.append("\t * @return the {@link JAXBElement} with a {@link ");
			sb.append(this.responseType);
			sb.append("}.\n");
			sb.append("\t */\n");

			if (this.useInsightAnnotation) {
				//sb.append("	@InsightEndPoint\n");
			}
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

			sb.append("\t\tlong start = System.currentTimeMillis();\n");

			sb.append("\t\ttry {\n");

			if (addSampleResponse) {
				sb.append("\t\t\t");
				sb.append(this.responseType);
				sb.append(
						" responseSample = this.getSampleResponseObject();\n");

				sb.append("\t\t\tif (responseSample != null) {\n");
				sb.append("\t\t\t\tresponse = responseSample;\n");
				sb.append("\t\t\t}\n");
				sb.append("\t\t\t// response.getFailure().clear();\n");
			}
			sb.append(
					"\t\t\t// The operation {0} of service {1} is not implement!!\n");
			sb.append("\t\t\t// response.getFailure().add(\n");
			sb.append(
					"\t\t\t//\tFailureHandler.getFailureType(\"E_NOT_IMPLEMENTED_OPERATION\", \n");
			sb.append("\t\t\t//\t\t\"");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append("\", \"");
			sb.append(this.serviceId);
			sb.append("\"));\n");

			sb.append("\t\t} catch (Throwable e) {\n");

			sb.append("\t\t\t/* Add a not covered error to the response. */\n");
			sb.append("\t\t\tthis.logger.error(e.getMessage(), e);\n");
			sb.append("\t\t\t// response.getFailure().add(\n");
			sb.append(
					"\t\t\t// FailureHandler.getFailureType(\"E_NOT_KNOWN_ERROR\", e);\n");
			sb.append("\t\t\t// response.getFailure().add(\n");
			sb.append(
					"\t\t\t// FailureHandler.handleException(e, \"E_NOT_KNOWN_ERROR\", this.logger);\n");
			sb.append("\t\t} finally {\n");

			sb.append("\t\t\tthis.logger.debug(\" ");
			sb.append(this.methodName);
			sb.append(" duration {}\",\n");
			sb.append("\t\t\t\t\trequestDuration(start));\n");

			sb.append("\t\t\tthis.logger.debug(\"-");
			sb.append(this.methodName);
			Optional<ComplexTypeChild> listChild = this.ctResponse.getChildren()
					.stream()
					.filter(ctc -> !ctc.getChildName().equals("failure")
							&& ctc.isList())
					.findFirst();

			if (this.hasFailureList(this.ctResponse)) {
				if (listChild.isPresent()) {
					sb.append(" #{}, #f{}\",\n");
					sb.append("\t\t\t\t\t");
					sb.append("response/*.get");
					sb.append(Util.capitalize(listChild.get().getChildName()));
					sb.append("().size()*/,");
					sb.append(" response.getFailure().size());\n");
				} else {
					sb.append(" #1, #f{}\",\n");
					sb.append("\t\t\t\t\t");
					sb.append(" response.getFailure().size());\n");
				}
			} else {
				if (listChild.isPresent()) {
					sb.append(" #{}, #f-\",\n");
					sb.append("\t\t\t\t\t");
					sb.append("response/*.get");
					sb.append(Util.capitalize(listChild.get().getChildName()));
					sb.append("().size()*/);\n");
				} else {
					sb.append(" #1, #f-\");\n");
				}
			}

			sb.append("\t\t}\n");

			sb.append("		return this.of\n");
			sb.append("				.create");
			sb.append(this.elementResponse.getClassNameObject());
			sb.append("(response);\n");
			sb.append("	}\n");

			if (addSampleResponse) {
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
				sb.append(
						"\t\t\tUnmarshaller unmarshaller = jaxbContext.createUnmarshaller();\n");
				sb.append("\t\t\tJAXBElement<");
				sb.append(this.responseType);
				sb.append("> jaxb = (JAXBElement<");
				sb.append(this.responseType);
				sb.append(
						">) unmarshaller.unmarshal(new StringReader(xml));\n");
				sb.append("\t\t\tif (jaxb != null) {\n");
				sb.append("\t\t\t\tmock = jaxb.getValue();\n");
				sb.append("\t\t\t}\n");
				sb.append("\t\t} catch (Exception e) {\n");
				sb.append("\t\t\tthis.logger.debug(\"");
				sb.append(this.operationName);
				sb.append(
						" generate sample message error: {}\", e.getMessage());\n");
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
						this.elementResponse.getElement().getName()
								.getLocalPart()));
				sb.append("\t\treturn sb.toString();\n");
				sb.append("\t}\n");
			}

			sb.append("\t/**\n");
			sb.append("\t * @param start\n");
			sb.append("\t * @return the duration in 000:00:00.000 format.\n");
			sb.append("\t */\n");
			sb.append("\tprivate String requestDuration(final long start) {\n");
			sb.append(
					"\t\tlong millis = System.currentTimeMillis() - start;\n");
			sb.append(
					"\t\tString hmss = String.format(\"%03d:%02d:%02d.%03d\",TimeUnit.MILLISECONDS.toHours(millis),\n");
			sb.append(
					"\t\t\tTimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),\n");
			sb.append(
					"\t\t\tTimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),\n");
			sb.append(
					"\t\t\tTimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));\n");
			sb.append("\t\treturn hmss;\n");
			sb.append("\t}\n");

			sb.append("}\n");
			sb.append("\n");
			File f = Util.getFile(this.outputDirectory, this.packageName,
					new StringBuffer()
							.append(this.elementRequest
									.getClassNameMockOperationProvider())
							.append(".java").toString());
			this.log.info(new StringBuffer().append("Write ")
					.append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sb.toString());
			} catch (Exception e) {
				this.log.error(e.getMessage());
				e.printStackTrace();
			}
			this.log.debug("-generate");
		}
	}

	private boolean hasFailureList(final ComplexType ct) {
		boolean value = ct.getChildren().stream().filter(
				ctc -> ctc.getChildName().equals("failure") && ctc.isList())
				.findFirst().isPresent();
		if (!value && Objects.nonNull(ct.getParent())) {
			value = this.hasFailureList(ct.getParent());
		}
		return value;
	}
}
