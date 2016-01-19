/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.relativeschemalocation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Make the schema location of the xsds relative in the local area. This is
 * needed inside of the web application so that the dynamic WSDL and the
 * validation could be done.
 *
 * @author bhausen
 */
@Mojo(name = "webapp-schemalocation",
		defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class ToWebappSchemaLocationMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory",
			defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;

	public static void main(final String[] args) {
		String xsdPath;
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain\\mapping\\target\\model";
		xsdPath = "C:\\xnb\\dev\\domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\com.ses.domain\\com.ses.domain.gen\\domain-gen-jaxb\\target\\model";
		// xsdPath =
		// "C:\\xnb\\dev\\git\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";
		// xsdPath =
		// "C:\\xnb\\dev\\38\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-jaxb\\target\\model";

		String basePackageName = "com.ses.osp.bus";

		ToWebappSchemaLocationMojo a = new ToWebappSchemaLocationMojo();
		a.baseDirectory = new File(xsdPath);

		Map<String, XsdContainer> map = XsdsUtil
				.getXsdContainers(a.baseDirectory);

		try {
			for (XsdContainer xc : map.values()) {
				a.changeSchemaLocation(xc, map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String changeSchemaLocation(final XsdContainer xc,
			final Map<String, XsdContainer> map) throws IOException {
		HashMap<String, String> replacements = new HashMap<String, String>();
		String xml = Util.readFile(xc.getFile());

		String basePath = Util.getRelativePathTranslated(this.baseDirectory,
				xc.getFile());
		this.getLog().debug(basePath);
		String namespace;
		String schemaLocation;
		XsdContainer imported;
		int importEndIndex;
		String importString = "<import";
		int importLength = importString.length();
		int importStartIndex = xml.indexOf(importString, 0);
		while (importStartIndex > 0) {
			importEndIndex = xml.indexOf('>', importStartIndex + importLength);
			if (importEndIndex < 0) {
				break;
			}
			namespace = Util.getAttributeValue(xml, "namespace",
					importStartIndex, importEndIndex);
			imported = map.get(namespace);
			if (imported != null) {
				schemaLocation = Util.getAttributeValue(xml, "schemaLocation",
						importStartIndex, importEndIndex);
				replacements.put(
						new StringBuffer(64).append("schemaLocation=\"")
								.append(schemaLocation).append("\"").toString(),
						new StringBuffer(64).append("schemaLocation=\"/")
								.append(imported.getRelativeName()).append("\"")
								.toString());
			}
			importStartIndex = xml.indexOf(importString, importEndIndex);
		}
		for (String key : replacements.keySet()) {
			xml = StringUtils.replace(xml, key, replacements.get(key));
		}
		return xml;
	}

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		Map<String, XsdContainer> map = XsdsUtil
				.getXsdContainers(this.baseDirectory);
		for (XsdContainer xc : map.values()) {
			try {
				String basePath = Util.getRelativePathTranslated(
						this.baseDirectory, xc.getFile());
				this.getLog().debug(basePath);
				String xml = this.changeSchemaLocation(xc, map);
				File f = Util.getFile(this.outputDirectory, basePath);
				this.getLog().info(new StringBuffer().append("Write ")
						.append(f.getAbsolutePath()));
				Util.writeToFile(f, xml);
			} catch (IOException e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		}
		this.getLog().debug("-execute");
	}
}
