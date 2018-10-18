/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.xjc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Create the schema file list and the catalog files to prepare the
 * org.codehaus.mojo jaxb2-maven-plugin xjc goal.
 *
 * @author bhausen
 */
@Mojo(name = "xjc-drop-xmlseealso", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class XmlSeeAlsoDropMojo extends AbstractMojo {
	private static String XMLSEEALSO_END = "})";
	private static String XMLSEEALSO_START = "@XmlSeeAlso({";

	protected static Optional<String> dropXmlSeeAlso(final File f, List<String> childTypeNames) throws IOException {
		Optional<String> value = Optional.empty();
		String originalContent = new String(Files.readAllBytes(f.toPath()));
		int indexStart = originalContent.indexOf(XMLSEEALSO_START);
		int indexPackage = originalContent.indexOf("package ");
		String packageDeclaration = originalContent.substring(indexPackage,
				originalContent.indexOf(";", indexPackage) + 1);
		String fqjn = String.format("%s.%s", packageDeclaration.replaceFirst("package", "").replaceAll(";", "").trim(),
				f.getName().replaceAll("\\.java", ""));

		if (indexStart > 0 && !childTypeNames.contains(fqjn)) {
			int indexEnd = indexStart + originalContent.substring(indexStart).indexOf(XMLSEEALSO_END)
					+ XMLSEEALSO_END.length();
			int indexFirstImport = originalContent.indexOf("import ");

			String xmlSeeAlso = originalContent.substring(indexStart, indexEnd).replaceAll("\\n", "")
					.replaceAll("\\r", "").replaceAll("(\\t)+", " ").replaceAll("( )+", " ");
			String implementation = originalContent.substring(indexEnd, originalContent.length());

			TreeSet<String> imports = new TreeSet<>();
			String documentationAndAnnotations = "";
			try (Scanner importScanner = new Scanner(originalContent.substring(indexFirstImport, indexStart));) {
				importScanner.useDelimiter("import ");
				while (importScanner.hasNext()) {
					String next = importScanner.next();
					String importDeclaration = next.substring(0, next.indexOf(";"));
					String importClass = importDeclaration.substring(importDeclaration.lastIndexOf('.') + 1,
							importDeclaration.length());
					if (importDeclaration.startsWith("java")) {
						imports.add(importDeclaration);
					} else if (implementation.contains(importClass)) {
						imports.add(importDeclaration);
					}
					if (!importScanner.hasNext()) {
						documentationAndAnnotations = next.substring(next.indexOf(';') + 1);
					}
				}
			}
			StringBuffer src = new StringBuffer(originalContent.length());
			src.append(packageDeclaration).append("\n\n");
			imports.stream().forEach(imp -> src.append("import ").append(imp).append(";\n"));

			src.append(documentationAndAnnotations);
			src.append("//").append(xmlSeeAlso);
			src.append(implementation);

			value = Optional.of(src.toString());
		}
		return value;
	}

	/**
	 * Collect the Java files recursively.
	 *
	 * @param f         the file to check.
	 * @param javaFiles the list of Java files.
	 */
	private static void scanForJavaFiles(final File f, final List<File> javaFiles) {
		final File[] cs = f.listFiles();
		if (cs != null && cs.length > 0) {
			for (final File c : cs) {
				if (c.isDirectory()) {
					scanForJavaFiles(c, javaFiles);
				} else if (c.getName().endsWith(".java")) {
					javaFiles.add(c);
				}
			}
		}
	}

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	protected File baseDirectory;
	/** The base message target namespace to include for orphan models. */
	@Parameter(property = "baseMessageTargetNamespace", defaultValue = "")
	protected String baseMessageTargetNamespace = "http://www.ses.com/Common/BaseMessage-1.0";
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	@Parameter(defaultValue = "${mojoExecution}", readonly = true)
	protected MojoExecution execution;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "inputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	protected File inputDirectory;
	/**
	 * The package names of the messages should end with - separation by space.
	 * Default is <code>msg restmsg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffixes", defaultValue = "msg restmsg")
	protected String messagePackageNameSuffixes;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	protected File outputDirectory;
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		List<File> javaFiles = new ArrayList<>();
		scanForJavaFiles(this.inputDirectory, javaFiles);
		javaFiles.parallelStream().forEach(f -> {
			try {
				List<String> childTypeNames = new CopyOnWriteArrayList<>();
				XsdsUtil config = XsdsUtil.getInstance(f, basePackageName, messagePackageNameSuffixes, "delta");
				config.getComplexTypes().stream().forEach(ct -> ct.getChildren().stream()
						.forEach(ctc -> childTypeNames.add(ctc.getComplexType().getClassNameFullQualified())));
				dropXmlSeeAlso(f, childTypeNames).ifPresent(src -> {
					try {
						Util.writeToFile(f, src);
					} catch (IOException e) {
						this.getLog().error(e.getMessage());
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		});
		this.getLog().debug("-execute");
	}

	/**
	 * Get the executing plugin version - the EIP version.
	 *
	 * @return the EIP version.
	 */
	protected String getEipVersion() {
		return this.execution.getVersion();
	}
}
