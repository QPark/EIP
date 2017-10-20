package com.qpark.maven.plugin.raml;

import java.io.File;
import java.util.Map;

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
import com.qpark.maven.xmlbeans.XsdContainer;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generate the model analysis XML.
 *
 * @author bhausen
 */
@Mojo(name = "generate-raml", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class GeneratorMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory",
			defaultValue = "${project.build.directory}/model")
	private File baseDirectory;
	/** The base package name where to place the object factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	/**
	 * The package name of the delta should contain this. Default is
	 * <code>delta</code>.
	 */
	@Parameter(property = "deltaPackageNameSuffix", defaultValue = "delta")
	private String deltaPackageNameSuffix;
	/**
	 * The package name of the messages should end with this. Default is
	 * <code>msg</code>.
	 */
	@Parameter(property = "messagePackageNameSuffix", defaultValue = "msg")
	private String messagePackageNameSuffix;
	/**
	 * The service request name need to end with this suffix (Default
	 * <code>Request</code>).
	 */
	@Parameter(property = "serviceRequestSuffix", defaultValue = "Request")
	private String serviceRequestSuffix;
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	private String serviceId;
	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	@Parameter(property = "serviceResponseSuffix", defaultValue = "Response")
	private String serviceResponseSuffix;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
	private File outputDirectory;
	/** The name of the enterprise (Defaults to the basePackageName). */
	@Parameter(property = "flattenQueryParameters", defaultValue = "true")
	private boolean flattenQueryParameters;
	/** The eip version to insert into generated tag. */
	private String eipVersion;
	/** The {@link MavenProject}. */
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	@Parameter(defaultValue = "${mojoExecution}", readonly = true)
	protected MojoExecution execution;

	/**
	 * Get the executing plugin version - the EIP version.
	 *
	 * @return the EIP version.
	 */
	protected String getEipVersion() {
		return this.execution.getVersion();
	}

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");

		final XsdsUtil xsds = XsdsUtil.getInstance(this.baseDirectory,
				this.basePackageName, this.messagePackageNameSuffix,
				this.deltaPackageNameSuffix, this.serviceRequestSuffix,
				this.serviceResponseSuffix);

		this.eipVersion = this.getEipVersion();
		Map<String, XsdContainer> xsdContainerMap = xsds.getXsdContainerMap();
		xsdContainerMap.values().stream().filter(
				xsd -> !xsd.getFile().getName().endsWith("maven-4.0.0.xsd"))
				.forEach(xsd -> {
					String raml = XsdToRaml.getRaml(xsd,
							this.flattenQueryParameters, xsds, this.eipVersion);
					if (xsd.getElementType().size() > 0) {
						this.writeRaml(raml,
								xsd.getRelativeName().replace(".xsd", ".raml"));
					} else {
						this.writeRaml(raml,
								xsd.getRelativeName().replace(".xsd", ".raml"));
					}
				});
		String raml = XsdToRaml.getRamlApi(this.flattenQueryParameters, xsds,
				this.eipVersion);
		this.writeRaml(raml, "api.raml");

	}

	private void writeRaml(final String raml, final String fileName) {
		try {
			final File f = Util.getFile(this.outputDirectory, fileName);
			this.getLog().info(new StringBuffer().append("Write ")
					.append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, raml);
			} catch (final Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}
