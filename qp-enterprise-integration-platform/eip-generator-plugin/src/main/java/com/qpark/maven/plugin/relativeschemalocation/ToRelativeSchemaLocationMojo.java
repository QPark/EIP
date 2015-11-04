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
package com.qpark.maven.plugin.relativeschemalocation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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
@Mojo(name = "relative-schemalocation", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class ToRelativeSchemaLocationMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		Map<String, XsdContainer> map = XsdsUtil.getXsdContainers(this.baseDirectory);
		HashMap<String, String> replacements = new HashMap<String, String>();
		for (XsdContainer xc : map.values()) {
			replacements.clear();
			try {
				String xml = Util.readFile(xc.getFile());
				String basePath = Util.getRelativePathTranslated(this.baseDirectory, xc.getFile());
				this.getLog().debug(basePath);
				String backPath = this.getBackPath(basePath);
				String xsdPath;
				String newPath;
				int index = xml.indexOf("schemaLocation=\"http", 0);
				int quoteIndex;
				while (index > 0) {
					quoteIndex = xml.indexOf('"', index + 16);
					if (quoteIndex < 0) {
						break;
					}
					xsdPath = xml.substring(index + 16, quoteIndex);
					newPath = this.getNewPath(xsdPath, backPath, map.values());
					if (newPath != null) {
						this.getLog().debug(new StringBuffer(xsdPath.length() + 5 + newPath.length()).append(xsdPath)
								.append(" => ").append(newPath).toString());
						replacements.put(xsdPath, newPath);
					}
					index = xml.indexOf("schemaLocation=\"http", quoteIndex);
				}
				for (String key : replacements.keySet()) {
					xml = StringUtils.replace(xml, key, replacements.get(key));
				}
				File f = Util.getFile(this.outputDirectory, basePath);
				this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
				Util.writeToFile(f, xml);
			} catch (IOException e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		}
		this.getLog().debug("-execute");
	}

	private String getBackPath(final String basePath) {
		StringBuffer sb = new StringBuffer(32);
		int x = StringUtils.split(basePath, '/').length - 1;
		for (int i = 0; i < x; i++) {
			if (i != 0) {
				sb.append("/");
			}
			sb.append("..");
		}
		return sb.toString();
	}

	private String getNewPath(final String xsdPath, final String backPath, final Collection<XsdContainer> xsds) {
		String newPath = null;
		for (XsdContainer xcCheck : xsds) {
			String checkPath = Util.getRelativePathTranslated(this.baseDirectory, xcCheck.getFile());
			if (xsdPath.contains(checkPath)) {
				newPath = new StringBuffer(checkPath.length() + backPath.length()).append(backPath).append(checkPath)
						.toString();
				break;
			}
		}
		return newPath;
	}
}
