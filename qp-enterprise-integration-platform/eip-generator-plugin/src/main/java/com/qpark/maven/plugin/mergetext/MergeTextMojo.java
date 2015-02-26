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
package com.qpark.maven.plugin.mergetext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.qpark.maven.Util;

/**
 * Create the spring-integration xml files of the router. The base to create
 * these files are the router defintion property files.
 * @author bhausen
 */
@Mojo(name = "merge-text", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class MergeTextMojo extends AbstractMojo {
	private static String readText(final File f) {
		StringBuffer sb = new StringBuffer(1024);
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(f));
			String inputLine;
			while ((inputLine = buffer.readLine()) != null) {
				sb.append(inputLine).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Collect the property files recursively.
	 * @param f the file to check.
	 * @param propertyFiles the list of property files.
	 */
	private static void scanForFiles(final File f,
			final List<File> propertyFiles) {
		File[] cs = f.listFiles();
		if (cs != null && cs.length > 0) {
			for (File c : cs) {
				if (c.isDirectory()) {
					scanForFiles(c, propertyFiles);
				} else if (c.getName().endsWith(".properties")) {
					propertyFiles.add(c);
				}
			}
		}
	}

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.basedir}/src/main/resources")
	protected File baseDirectory;

	@Parameter(property = "exclude", defaultValue = "")
	protected String exclude;

	@Parameter(property = "include", defaultValue = ".*")
	protected String include;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-resources")
	protected File outputDirectory;

	@Parameter(property = "outputFileName", defaultValue = "mergeText.txt")
	protected String outputFileName;

	@Component
	private MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().debug("+execute");
		List<File> files = new ArrayList<File>();
		StringBuffer mergedText = new StringBuffer(1024);
		scanForFiles(this.baseDirectory, files);

		Pattern patternInclude = Pattern.compile(this.include);
		Pattern patternExclude = Pattern.compile(this.exclude);
		Matcher matcherInclude = null;
		Matcher matcherExclude = null;

		for (File file : files) {
			matcherInclude = patternInclude.matcher(file.getName());
			matcherExclude = patternExclude.matcher(file.getName());
			if (matcherInclude.matches() && !matcherExclude.matches()) {
				mergedText.append(readText(file));
			}
		}
		File f = Util.getFile(this.outputDirectory, this.outputFileName);
		this.getLog()
				.info(new StringBuffer().append("Write ").append(
						f.getAbsolutePath()));
		try {
			Util.writeToFile(f, mergedText.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		this.getLog().debug("-execute");
	}
}
