/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.persistenceconfig;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.jaxb2.AbstractJaxbMojo;
import org.codehaus.mojo.jaxb2.javageneration.AbstractJavaGeneratorMojo;
import org.codehaus.mojo.jaxb2.shared.FileSystemUtilities;
import org.codehaus.mojo.jaxb2.shared.filters.Filter;
import org.codehaus.mojo.jaxb2.shared.filters.Filters;
import org.codehaus.mojo.jaxb2.shared.filters.pattern.PatternFileFilter;

/**
 * Generates the <code>persistence-spring-config.xml</code> containing the bean
 * declaration of the
 * <ul>
 * <li>JNDI data source</li>
 * <li>JPA EntityManagerFactory to access the persistence unit</li>
 * <li>JPA SessionFactory</li>
 * <li>JPA TransactionManager</li>
 * <li>Hibernate statistics MBean</li>
 * </ul>
 *
 * @author bhausen
 */
@Mojo(name = "execute-hyperjaxb3", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class Hyperjaxb3Mojo extends AbstractJavaGeneratorMojo {
	static {

		final List<Filter<File>> xjbTemp = new ArrayList<Filter<File>>();
		xjbTemp.addAll(AbstractJaxbMojo.STANDARD_EXCLUDE_FILTERS);
		xjbTemp.add(new PatternFileFilter(Arrays.asList("\\.xsd"), true));
		STANDARD_XJB_EXCLUDE_FILTERS = Collections.unmodifiableList(xjbTemp);

		final List<Filter<File>> xsdTemp = new ArrayList<Filter<File>>();
		xsdTemp.addAll(AbstractJaxbMojo.STANDARD_EXCLUDE_FILTERS);
		xsdTemp.add(new PatternFileFilter(Arrays.asList("\\.xjb"), true));
		STANDARD_SOURCE_EXCLUDE_FILTERS = Collections.unmodifiableList(xsdTemp);
	}

	/** The name of the persistence unit. */
	@Parameter(property = "persistenceUnitName", defaultValue = "")
	private String persistenceUnitName = "com.qpark.xxx";
	private File persistenceXmlTemplate = new File("xxx.xml");
	/** The class name of the RoundtripTest class. */
	@Parameter(property = "roundtripTestClassName", defaultValue = "")
	private String roundtripTestClassName;

	@Parameter(property = "schemaDirectory")
	private String schemaDirectory;
	/**
	 * <p>
	 * Corresponding XJC parameter: {@code d}.
	 * </p>
	 * <p>
	 * The working directory where the generated Java source files are created.
	 * </p>
	 */
	@Parameter(defaultValue = "${project.build.directory}/generated-sources", required = true)
	private File outputDirectory;

	private void setArguments() {
		this.arguments.add("-Xhyperjaxb3-jpa2");
		this.arguments.add("-Xhyperjaxb3-jpa2-result=annotations");
		if (this.roundtripTestClassName != null && this.roundtripTestClassName.trim().length() > 0) {
			this.arguments.add("-Xhyperjaxb3-jpa2-roundtripTestClassName=" + this.roundtripTestClassName);
		}
		this.arguments.add("-Xhyperjaxb3-jpa2-persistenceUnitName=" + this.persistenceUnitName);
		this.arguments.add("-Xhyperjaxb3-jpa2-persistenceXml=" + this.persistenceXmlTemplate.getAbsolutePath());
		this.arguments.add("-Xequals");
		this.arguments.add("-Xinheritance");
		this.arguments.add("-XhashCode");
		this.arguments.add("-XtoString");
	}

	/**
	 * <p>
	 * Parameter holding a List of Filters, used to match all files under the
	 * {@code sources} directories which should <strong>not</strong> be
	 * considered XJC source files. (The filters identify files to exclude, and
	 * hence this parameter is called {@code xjcSourceExcludeFilters}). If a
	 * file under any of the source directories matches at least one of the
	 * Filters supplied in the {@code xjcSourceExcludeFilters}, it is not
	 * considered an XJC source file, and therefore excluded from processing.
	 * </p>
	 * <p>
	 * If not explicitly provided, the Mojo uses the value within
	 * {@code STANDARD_SOURCE_EXCLUDE_FILTERS}. The algorithm for finding XJC
	 * sources is as follows:
	 * </p>
	 * <ol>
	 * <li>Find all files given in the sources List. Any Directories provided
	 * are searched for files recursively.</li>
	 * <li>Exclude any found files matching any of the supplied
	 * {@code xjcSourceExcludeFilters} List.</li>
	 * <li>The remaining Files are submitted for processing by the XJC tool.
	 * </li>
	 * </ol>
	 * <p>
	 * <strong>Example:</strong> The following configuration would exclude any
	 * sources whose names end with {@code txt} or {@code foo}:
	 * </p>
	 *
	 * <pre>
	 *     <code>
	 *         &lt;configuration>
	 *         ...
	 *              &lt;xjcSourceExcludeFilters>
	 *                  &lt;filter implementation="org.codehaus.mojo.jaxb2.shared.filters.pattern.PatternFileFilter">
	 *                      &lt;patterns>
	 *                          &lt;pattern>\.txt&lt;/pattern>
	 *                          &lt;pattern>\.foo&lt;/pattern>
	 *                      &lt;/patterns>
	 *                  &lt;/filter>
	 *              &lt;/xjcSourceExcludeFilters>
	 *         &lt;/configuration>
	 *     </code>
	 * </pre>
	 * <p>
	 * Note that inner workings of the Dependency Injection mechanism used by
	 * Maven Plugins (i.e. the DI from the Plexus container) requires that the
	 * full class name to the Filter implementation should be supplied for each
	 * filter, as is illustrated in the sample above. This is true also if you
	 * implement custom Filters.
	 * </p>
	 *
	 * @see #STANDARD_SOURCE_EXCLUDE_FILTERS
	 * @see org.codehaus.mojo.jaxb2.shared.filters.pattern.PatternFileFilter
	 * @see org.codehaus.mojo.jaxb2.shared.filters.pattern.AbstractPatternFilter
	 * @see org.codehaus.mojo.jaxb2.shared.filters.AbstractFilter
	 */
	@Parameter(required = false)
	private List<Filter<File>> sourceExcludeFilters;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<URL> getSources() {

		final List<Filter<File>> excludePatterns = this.sourceExcludeFilters == null ? STANDARD_SOURCE_EXCLUDE_FILTERS
				: this.sourceExcludeFilters;
		Filters.initialize(this.getLog(), excludePatterns);

		return FileSystemUtilities.filterFiles(this.getProject().getBasedir(), Arrays.asList(this.schemaDirectory),
				Arrays.asList(STANDARD_SOURCE_DIRECTORY), this.getLog(), "sources", excludePatterns);
	}

	@Override
	protected boolean shouldExecutionBeSkipped() {
		this.setArguments();
		return this.skipXjc;
	}

	/**
	 * Indicate if the XjcMojo execution should be skipped.
	 */
	@Parameter(property = "xjc.skip", defaultValue = "false")
	private boolean skipXjc;

	// /**
	// * @see org.apache.maven.plugin.Mojo#execute()
	// */
	// public void executex() throws MojoExecutionException {
	// this.getLog().debug("+execute");
	//
	// if (this.persistenceUnitName == null ||
	// this.persistenceUnitName.trim().length() == 0) {
	// throw new MojoExecutionException(
	// "No persistence unit name entered. Please use property
	// persistenceUnitName to define.");
	// }
	//
	// if (this.datasourceJndiName == null ||
	// this.datasourceJndiName.trim().length() == 0) {
	// throw new MojoExecutionException(
	// "No JNDI datasource name entered. Please use property datasourceJndiName
	// to define.");
	// }
	// if (this.springJpaVendorAdapter == null ||
	// this.springJpaVendorAdapter.trim().length() == 0) {
	// throw new MojoExecutionException(
	// "No spring JPA vendor adapter supplied. Please use property
	// springJpaVendorAdapter which defaults to
	// org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter.");
	// }
	//
	// StringBuffer sb = new StringBuffer(1024);
	// sb.append(this.getSpringPersistenceConfigXml());
	//
	// File f = Util.getFile(this.outputDirectory,
	// new
	// StringBuffer().append(this.basePackageName).append("-persistence-spring-config.xml").toString());
	// this.getLog().info(new StringBuffer().append("Write
	// ").append(f.getAbsolutePath()));
	// try {
	// Util.writeToFile(f, sb.toString());
	// } catch (Exception e) {
	// this.getLog().error(e.getMessage());
	// e.printStackTrace();
	// }
	// this.getLog().debug("-execute");
	// }

	private String getSpringPersistenceConfigXml() throws MojoExecutionException {
		StringBuffer sb = new StringBuffer(1024);

		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStaleFileName() {
		return STALE_FILENAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected File getOutputDirectory() {
		return this.outputDirectory;
	}

	// protected void resolveXJCPluginArtifacts()
	// throws ArtifactResolutionException, ArtifactNotFoundException,
	// InvalidDependencyVersionException {
	//
	// this.xjcPluginArtifacts =
	// ArtifactUtils.resolveTransitively(this.getArtifactFactory(),
	// this.getArtifactResolver(), getLocalRepository(),
	// getArtifactMetadataSource(), getPlugins(),
	// this.getProject());
	// this.xjcPluginFiles = ArtifactUtils.getFiles(this.xjcPluginArtifacts);
	// this.xjcPluginURLs = CollectionUtils.apply(this.xjcPluginFiles,
	// IOUtils.GET_URL);
	// }
	//
	// public ArtifactFactory getArtifactFactory() {
	// return this.artifactFactory;
	// }
	//
	// public ArtifactResolver getArtifactResolver() {
	// return this.artifactResolver;
	// }
	//
	// private Collection<Artifact> xjcPluginArtifacts;
	//
	// private Collection<File> xjcPluginFiles;
	//
	// private List<URL> xjcPluginURLs;
	//
	// @Component
	// private ArtifactResolver artifactResolver;
	//
	// @Component
	// private ArtifactFactory artifactFactory;
	//
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<String> getClasspath() throws MojoExecutionException {
		try {
			return this.getProject().getCompileClasspathElements();

		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException("Could not retrieve Compile classpath.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addGeneratedSourcesToProjectSourceRoot() {
		this.getProject().addCompileSourceRoot(this.getOutputDirectory().getAbsolutePath());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addResource(final Resource resource) {
		this.getProject().addResource(resource);
	}

	/**
	 * The last part of the stale fileName for this XjcMojo.
	 */
	public static final String STALE_FILENAME = "xjcStaleFlag";

	/**
	 * <p>
	 * Standard directory path (relative to basedir) searched recursively for
	 * source files (typically XSDs), unless overridden by an
	 * <code>sources</code> configuration element.
	 * </p>
	 */
	public static final String STANDARD_SOURCE_DIRECTORY = "src/main/xsd";

	/**
	 * Default exclude Filters for sources, used unless overridden by an
	 * explicit configuration in the {@code xjcSourceExcludeFilters} parameter.
	 */
	public static final List<Filter<File>> STANDARD_SOURCE_EXCLUDE_FILTERS;

	/**
	 * <p>
	 * Standard directory path (relative to basedir) searched recursively for
	 * XJB files, unless overridden by an <code>xjbSources</code> configuration
	 * element. As explained in the JAXB specification, XJB files (JAXB Xml
	 * Binding files) are used to configure parts of the Java source generation.
	 * </p>
	 */
	public static final String STANDARD_XJB_DIRECTORY = "src/main/xjb";

	/**
	 * Default List of exclude Filters for XJB files, unless overridden by
	 * providing an explicit configuration in the {@code xjbExcludeSuffixes}
	 * parameter.
	 */
	public static final List<Filter<File>> STANDARD_XJB_EXCLUDE_FILTERS;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<File> getSourceXJBs() {

		// final List<Filter<File>> excludePatterns = xjbExcludeFilters == null
		// ? STANDARD_XJB_EXCLUDE_FILTERS
		// : xjbExcludeFilters;
		// Filters.initialize(this.getLog(), excludePatterns);
		//
		// return
		// FileSystemUtilities.filterFiles(this.getProject().getBasedir(),
		// xjbSources, STANDARD_XJB_DIRECTORY,
		// this.getLog(), "xjbSources", excludePatterns);
		return new ArrayList<File>(0);
	}

}
