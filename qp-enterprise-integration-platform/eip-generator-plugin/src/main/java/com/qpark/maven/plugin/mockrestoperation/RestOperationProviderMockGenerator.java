/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.mockrestoperation;

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.apache.xmlbeans.SchemaProperty;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ElementType;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public class RestOperationProviderMockGenerator {
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

	public RestOperationProviderMockGenerator(final XsdsUtil config, final File outputDirectory,
			final ElementType element, final Log log) {
		this.config = config;
		this.outputDirectory = outputDirectory;
		this.log = log;
		this.elementRequest = element;
		this.packageName = element.getPackageNameMockOperationProvider();
		this.operationName = element.getOperationName();
		this.serviceId = element.getServiceId();

		if (element.getMethodName().endsWith("Rest")) {
			this.methodName = element.getMethodName().substring(0, element.getMethodName().length() - 4);
		} else {
			this.methodName = element.getMethodName();
		}

		this.elementResponse = XsdsUtil.findResponse(this.elementRequest, config.getElementTypes(), config);
		if (this.elementResponse != null) {
			this.ctRequest = new ComplexType(this.elementRequest.getElement().getType(), this.config);
			this.ctResponse = new ComplexType(this.elementResponse.getElement().getType(), this.config);

			this.fqRequestType = this.ctRequest.getClassNameFullQualified();
			this.fqResponseType = this.ctResponse.getClassNameFullQualified();

			this.requestType = this.ctRequest.getClassName();
			this.responseType = this.ctResponse.getClassName();
		}
	}

	public void generate() {
		this.log.debug("+generate");
		if (this.elementResponse != null && this.ctResponse != null && !this.ctResponse.isSimpleType()
				&& !this.ctResponse.isPrimitiveType()) {
			StringBuffer sb = new StringBuffer(1024);
			sb.append("package ");
			sb.append(this.packageName);
			sb.append(";\n");
			sb.append("\n");
			sb.append("import java.io.StringReader;\n");
			sb.append("import java.util.ArrayList;\n");
			sb.append("import java.util.Date;\n");
			sb.append("import java.util.List;\n");
			sb.append("\n");
			sb.append("import javax.xml.bind.JAXBContext;\n");
			sb.append("import javax.xml.bind.JAXBElement;\n");
			sb.append("import javax.xml.bind.JAXBException;\n");
			sb.append("import javax.xml.bind.Unmarshaller;\n");
			sb.append("\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			sb.append("import org.springframework.format.annotation.DateTimeFormat;\n");
			sb.append("import org.springframework.stereotype.Controller;\n");

			sb.append("import org.springframework.ui.Model;\n");
			sb.append("import org.springframework.web.bind.annotation.ModelAttribute;\n");
			sb.append("import org.springframework.web.bind.annotation.PathVariable;\n");
			sb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
			sb.append("import org.springframework.web.bind.annotation.RequestMethod;\n");
			sb.append("import org.springframework.web.bind.annotation.RequestParam;\n");
			sb.append("import org.springframework.web.bind.annotation.ResponseBody;\n");
			sb.append("import org.springframework.web.context.request.WebRequest;\n");
			// sb.append("import
			// org.springframework.beans.factory.annotation.Qualifier;\n");
			sb.append("import org.springframework.integration.annotation.ServiceActivator;\n");
			sb.append("import org.springframework.stereotype.Component;\n");
			sb.append("\n");
			sb.append("import ");
			sb.append(this.config.getBasePackageName());
			sb.append(".ServiceObjectFactory;\n");
			sb.append("import ");
			sb.append(this.config.getBasePackageName());
			sb.append(".RequestProperties;\n");
			sb.append("import ");
			sb.append(this.fqRequestType);
			sb.append(";\n");
			sb.append("import ");
			sb.append(this.fqResponseType);
			sb.append(";\n");
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
			sb.append("//@Controller(\"");
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

			sb.append("	/** The {@link ServiceObjectFactory}. */\n");
			sb.append("	@Autowired\n");
			sb.append("	private ServiceObjectFactory of;\n");
			sb.append("\n");

			SchemaProperty[] requestElements = this.ctRequest.getType().getElementProperties();

			this.requestType.trim();
			// sb.append("\t/* ");
			// for (int i = 0; i < requestElements.length; i++) {
			// sb.append("/");
			// sb.append(requestElements[i].getName().getLocalPart());
			// sb.append("/{");
			// sb.append(requestElements[i].getName().getLocalPart());
			// sb.append("} ");
			// }
			// sb.append("*/\n");
			// sb.append("\t/* \n");
			// for (int i = 0; i < requestElements.length; i++) {
			// sb.append("\t\t@PathVariable(value = \"");
			// sb.append(requestElements[i].getName().getLocalPart());
			// sb.append("\") ");
			// sb.append(XsdsUtil.getBuildInBaseTypeFormat(requestElements[i]
			// .getType()));
			// sb.append(" final ");
			// sb.append(XsdsUtil.getBuildInBaseTypeClass(
			// requestElements[i].getType()).getSimpleName());
			// sb.append(" ");
			// sb.append(requestElements[i].getName().getLocalPart());
			// sb.append(",\n");
			// }
			// sb.append("*/\n");

			sb.append("\t/**\n");
			for (int i = 0; i < requestElements.length; i++) {
				sb.append("\t * @param ");
				sb.append(requestElements[i].getName().getLocalPart());
				sb.append(" the ");
				sb.append(Util.splitOnCapital(requestElements[i].getName().getLocalPart()));
				sb.append(" as {@link ");
				sb.append(XsdsUtil.getBuildInBaseTypeClass(requestElements[i].getType()).getSimpleName());
				sb.append("}.\n");
			}
			sb.append("\t * @return the {@link ");
			sb.append(this.responseType);
			sb.append("}.\n");
			sb.append("\t */\n");
			sb.append("	@InsightEndPoint\n");

			sb.append("	@RequestMapping(value = \"");
			if (requestElements.length == 0 || !this.serviceId.equals(requestElements[0].getName().getLocalPart())) {
				sb.append("/");
				sb.append(this.serviceId);
			}
			for (int i = 0; i < requestElements.length; i++) {
				sb.append("/");
				sb.append(requestElements[i].getName().getLocalPart());
				sb.append("/{");
				sb.append(requestElements[i].getName().getLocalPart());
				sb.append("}");
			}
			sb.append("/");
			sb.append(this.methodName);

			sb.append("\", method = RequestMethod.GET)\n");
			sb.append("	public final @ResponseBody ");
			sb.append(this.responseType);
			sb.append(" ");
			sb.append(this.methodName);
			sb.append("(\n");

			for (int i = 0; i < requestElements.length; i++) {
				sb.append("\t\t\t@PathVariable(\"");
				sb.append(requestElements[i].getName().getLocalPart());
				sb.append("\") ");
				sb.append(XsdsUtil.getBuildInBaseTypeFormat(requestElements[i].getType()));
				sb.append(" final ");
				sb.append(XsdsUtil.getBuildInBaseTypeClass(requestElements[i].getType()).getSimpleName());
				sb.append(" ");
				sb.append(requestElements[i].getName().getLocalPart());
				sb.append(",\n");
			}

			sb.append("\t\t\tfinal Model model) {\n");

			sb.append("\t/*\n");
			sb.append("	@RequestMapping(value = \"/");
			sb.append(this.serviceId);
			sb.append("/");
			sb.append(this.methodName);
			sb.append("\", method = RequestMethod.GET)\n");
			sb.append("	public final @ResponseBody ");

			sb.append(this.responseType);

			sb.append(" ");
			sb.append(this.methodName);
			sb.append("(");

			for (int i = 0; i < requestElements.length; i++) {
				sb.append("\n\t\t\t@RequestParam(value = \"");
				sb.append(requestElements[i].getName().getLocalPart());
				sb.append("\", required = ");
				sb.append(XsdsUtil.isRequired(requestElements[i]));
				if (requestElements[i].getDefaultText() != null
						&& requestElements[i].getDefaultText().trim().length() > 0) {
					sb.append(", defaultValue = \"");
					sb.append(requestElements[i].getDefaultText());
					sb.append("\")");
				} else {
					sb.append(")");
				}
				sb.append(XsdsUtil.getBuildInBaseTypeFormat(requestElements[i].getType()));
				sb.append(" final ");
				sb.append(XsdsUtil.getBuildInBaseTypeClass(requestElements[i].getType()).getSimpleName());
				sb.append(" ");

				sb.append(requestElements[i].getName().getLocalPart());

				sb.append(", ");
			}

			sb.append(" final Model model) {\n");
			sb.append("*/\n");

			sb.append("\t\tthis.logger.debug(\"+");
			sb.append(this.methodName);
			if (requestElements.length > 0) {
				for (int i = 0; i < requestElements.length; i++) {
					sb.append(" {}");
				}
				sb.append("\", new Object[] {");
				for (int i = 0; i < requestElements.length; i++) {
					sb.append(requestElements[i].getName().getLocalPart());
					if (i < requestElements.length - 1) {
						sb.append(", ");
					}
				}
				sb.append("});\n");
			} else {
				sb.append("\");\n");
			}
			sb.append("\t\t");
			sb.append(this.responseType);
			sb.append(" response = this.of.\n\t\t\t\tcreate");
			sb.append(this.responseType);
			sb.append("();\n");

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
			sb.append("\t\t\t// response.getFailure().add(\"Operation ");
			sb.append(Util.splitOnCapital(this.methodName));
			sb.append(" of service ");
			sb.append(this.serviceId);
			sb.append(" not implemented.\");\n");

			sb.append("\t\t} catch (Throwable e) {\n");

			sb.append(
					"\t\t\t//FailureHandler.handleException(e, null /* rp */, \"E_ALL_NOT_KNOWN_ERROR\", this.logger);\n");

			sb.append("\t\t} finally {\n");

			sb.append("\t\t\tthis.logger.debug(\" ");
			sb.append(this.methodName);
			sb.append(" duration {}\",\n");
			sb.append("\t\t\t\t\trequestDuration(start));\n");

			sb.append("\t\t\tthis.logger.debug(\"-");
			sb.append(this.methodName);
			sb.append(" #{}, #f{}\",\n");
			sb.append("\t\t\t\t\tresponse/*.get()*/ != null ? 1 : 0, response.getFailure()\n");
			sb.append("\t\t\t\t\t\t\t.size());\n");

			sb.append("\t\t}\n");

			sb.append("		return response;\n");
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
			sb.append("\t\t\t\t\t.newInstance(ServiceObjectFactory.CONTEXT_PATH_DEFINITON);\n");
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

			sb.append("\t/**\n");
			sb.append("\t * @param start\n");
			sb.append("\t * @return the duration in 000:00:00.000 format.\n");
			sb.append("\t */\n");
			sb.append("\tprivate String requestDuration(final long start) {\n");
			sb.append("\t\tlong millis = System.currentTimeMillis() - start;\n");
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
			File f = Util.getFile(this.outputDirectory, this.packageName, new StringBuffer()
					.append(this.elementRequest.getClassNameMockOperationProvider()).append(".java").toString());
			this.log.info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
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
