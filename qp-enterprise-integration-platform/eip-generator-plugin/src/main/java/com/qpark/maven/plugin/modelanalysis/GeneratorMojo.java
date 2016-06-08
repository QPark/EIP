package com.qpark.maven.plugin.modelanalysis;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.eip.core.model.analysis.Analysis;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.ObjectFactory;
import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * Generate the model analysis XML.
 *
 * @author bhausen
 */
@Mojo(name = "generate-model-analysis", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class GeneratorMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
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
	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	@Parameter(property = "serviceResponseSuffix", defaultValue = "Response")
	private String serviceResponseSuffix;
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
	private File outputDirectory;
	/** The name of the enterprise (Defaults to the basePackageName). */
	@Parameter(property = "enterpriseName")
	private String enterpriseName;
	/** The version of the model. Defaults to artefact version. */
	private String modelVersion;
	/** The eip version to insert into generated tag. */
	private String eipVersion;
	/** The {@link MavenProject}. */
	@Component
	private MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");

		XsdsUtil xsds = XsdsUtil.getInstance(this.baseDirectory, this.basePackageName, this.messagePackageNameSuffix,
				this.deltaPackageNameSuffix, this.serviceRequestSuffix, this.serviceResponseSuffix);
		if (this.project.getArtifact() != null) {
			this.eipVersion = this.project.getArtifact().getVersion();
		}
		if (this.enterpriseName == null || this.enterpriseName.trim().length() == 0) {
			this.enterpriseName = this.basePackageName;
		}
		if (this.modelVersion == null || this.modelVersion.trim().length() == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			this.modelVersion = String.format("%s#%s", this.project.getArtifact().getVersion(), sdf.format(new Date()));
		}
		Analysis a = new AnalysisProvider().createEnterprise(this.enterpriseName, this.modelVersion, xsds);

		try {
			ObjectFactory of = new ObjectFactory();
			JAXBElement<EnterpriseType> enterprise = of.createEnterprise(a.getEnterprise());
			JAXBContext context = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter sw = new StringWriter();
			marshaller.marshal(enterprise, sw);

			File f = Util.getFile(this.outputDirectory,
					new StringBuffer(64).append(this.enterpriseName).append("-ModelAnalysis.xml").toString());
			this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sw.toString());
			} catch (Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
