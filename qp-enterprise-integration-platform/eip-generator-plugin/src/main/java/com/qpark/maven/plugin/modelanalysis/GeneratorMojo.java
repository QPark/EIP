package com.qpark.maven.plugin.modelanalysis;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.qpark.eip.core.model.analysis.Analysis;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.ObjectFactory;
import com.qpark.eip.model.docmodelreport.DataTypeReportRow;
import com.qpark.eip.model.docmodelreport.FlowReportRow;
import com.qpark.eip.model.docmodelreport.MappingReportRow;
import com.qpark.eip.model.docmodelreport.ServiceReportRow;
import com.qpark.eip.service.domain.doc.report.DataTypeReportProvider;
import com.qpark.eip.service.domain.doc.report.FlowReportProvider;
import com.qpark.eip.service.domain.doc.report.MappingReportProvider;
import com.qpark.eip.service.domain.doc.report.ServiceReportProvider;
import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
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
	/** The name of the service id to generate. If empty use all. */
	@Parameter(property = "serviceId", defaultValue = "")
	private String serviceId;
	/** The pattern matching the flow name. Defaults to empty string . */
	@Parameter(property = "flowNameParts", defaultValue = "")
	private String flowNameParts;
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
	@Parameter(property = "modelVersion")
	private String modelVersion;
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

	static final class ReportHeader {
		String eipVersion;
		String mavenGroupId;
		String mavenArtefactId;
		String mavenVersion;
		String modelVersion;
		String buildTimestamp;

		/**
		 * @return the eipVersion
		 */
		public String getEipVersion() {
			return this.eipVersion;
		}

		/**
		 * @return the mavenGroupId
		 */
		public String getMavenGroupId() {
			return this.mavenGroupId;
		}

		/**
		 * @return the mavenArtefactId
		 */
		public String getMavenArtefactId() {
			return this.mavenArtefactId;
		}

		/**
		 * @return the mavenVersion
		 */
		public String getMavenVersion() {
			return this.mavenVersion;
		}

		/**
		 * @return the modelVersion
		 */
		public String getModelVersion() {
			return this.modelVersion;
		}

		/**
		 * @return the buildTimestamp
		 */
		public String getBuildTimestamp() {
			return this.buildTimestamp;
		}
	}

	private String getReportHtmlHeaderInformation(final ReportHeader rh) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<table class=\"portletlrborder\" stype=\"margin-top:0px;\" >\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th>EIP version</th>");
		sb.append("<td style=\"background-color: #EBF2F9;color: #3B73AF;\">");
		sb.append(rh.eipVersion);
		sb.append("</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th>Maven GroupId</th>");
		sb.append("<td style=\"background-color: #EBF2F9;color: #3B73AF;\">");
		sb.append(rh.mavenGroupId);
		sb.append("</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th>Maven ArtefactId</th>");
		sb.append("<td style=\"background-color: #EBF2F9;color: #3B73AF;\">");
		sb.append(rh.mavenArtefactId);
		sb.append("</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th>Maven Version</th>");
		sb.append("<td style=\"background-color: #EBF2F9;color: #3B73AF;\">");
		sb.append(rh.mavenVersion);
		sb.append("</td>\n");
		sb.append("</tr>\n");
		if (Objects.nonNull(rh.modelVersion) && rh.modelVersion.trim().length() > 0) {
			sb.append("<tr class=\"tablerowheader\">\n");
			sb.append("<th>Model Version</th>");
			sb.append("<td style=\"background-color: #EBF2F9;color: #3B73AF;\">");
			sb.append(rh.modelVersion);
			sb.append("</td>\n");
			sb.append("</tr>\n");
		}
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th>Build time (UTC)</th>");
		sb.append("<td style=\"background-color: #EBF2F9;color: #3B73AF;\">");
		sb.append(rh.buildTimestamp);
		sb.append("</td>\n");
		sb.append("</tr>\n");
		sb.append("</table>\n");
		return sb.toString();
	}

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		this.objectMapper.configure(Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		this.objectMapper.configure(SerializationFeature.CLOSE_CLOSEABLE, false);
		this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		this.objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(this.objectMapper.getTypeFactory()));

		final XsdsUtil xsds = XsdsUtil.getInstance(this.baseDirectory, this.basePackageName,
				this.messagePackageNameSuffix, this.deltaPackageNameSuffix, this.serviceRequestSuffix,
				this.serviceResponseSuffix);
		this.eipVersion = this.getEipVersion();
		Collection<String> serviceIds = ServiceIdRegistry.splitServiceIds(this.serviceId);
		final Collection<String> flowNames = ServiceIdRegistry.splitServiceIds(this.flowNameParts);
		this.getLog().info("ServiceIds: " + this.serviceId + " " + serviceIds.size());
		if (serviceIds.isEmpty()) {
			serviceIds = xsds.getServiceIdRegistry().getAllServiceIds();
			if (flowNames.isEmpty()) {
				flowNames.add("*");
			}
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		final ReportHeader reportHeader = new ReportHeader();
		reportHeader.eipVersion = this.eipVersion;
		reportHeader.mavenArtefactId = this.project.getArtifactId();
		reportHeader.mavenGroupId = this.project.getGroupId();
		reportHeader.mavenVersion = this.project.getVersion();
		reportHeader.modelVersion = this.modelVersion;
		reportHeader.buildTimestamp = sdf.format(Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()));
		final String htmlHeader = this.getReportHtmlHeaderInformation(reportHeader);
		if (this.enterpriseName == null || this.enterpriseName.trim().length() == 0) {
			this.enterpriseName = this.basePackageName;
		}
		if (this.modelVersion == null || this.modelVersion.trim().length() == 0) {
			this.modelVersion = String.format("%s#%s", this.project.getArtifact().getVersion(), sdf.format(new Date()));
		}
		final AnalysisProvider ap = new AnalysisProvider();
		final Analysis a = ap.createEnterprise(this.enterpriseName, this.modelVersion, xsds);
		this.getLog().info("AnalysisProvider: " + a.getEnterprise() + " " + xsds.getComplexTypes().size());
		if (!serviceIds.isEmpty()) {
			final Set<String> ctIds = new TreeSet<>();
			final ServiceReportProvider srp = new ServiceReportProvider();
			final List<ServiceReportRow> serviceReportRows = srp.getReportRows(ap, serviceIds, ".*", ctIds);
			this.getLog().info("ServiceReportProvider found " + serviceReportRows.size());
			final FlowReportProvider frp = new FlowReportProvider();
			final List<FlowReportRow> flowReportRows = frp.getReportRows(ap, flowNames, ctIds);
			this.getLog().info("FlowReportProvider found " + flowReportRows.size());
			final MappingReportProvider mrp = new MappingReportProvider();
			final List<MappingReportRow> mappingReportRows = mrp.getReportRows(ap, flowNames, ctIds);
			this.getLog().info("MappingReportProvider found " + mappingReportRows.size());
			final DataTypeReportProvider drp = new DataTypeReportProvider();
			final List<DataTypeReportRow> dataTypeReportRows = drp.getReportRows(ap, ctIds);
			this.getLog().info("DataTypeReportProvider found " + dataTypeReportRows.size());

			try {
				this.writeHeaderJson(this.objectMapper.writeValueAsString(reportHeader));
			} catch (final JsonProcessingException e) {
				this.getLog().error(e.getMessage());
			}
			this.writeReport("description-service", Report.getServiceReport(serviceReportRows, htmlHeader),
					Report.getJson(serviceReportRows, this.objectMapper));
			this.writeReport("description-flow", Report.getFlowReport(flowReportRows, htmlHeader),
					Report.getJson(flowReportRows, this.objectMapper));
			this.writeReport("description-mapping", Report.getMappingReport(mappingReportRows, htmlHeader),
					Report.getJson(mappingReportRows, this.objectMapper));
			this.writeReport("description-datatype", Report.getDataTypeReport(dataTypeReportRows, htmlHeader),
					Report.getJson(dataTypeReportRows, this.objectMapper));
			this.writeReport("description-overview", Report.getReportLinkPage(htmlHeader, "description-service",
					"description-flow", "description-mapping", "description-datatype"), null);
		}
		try {
			final ObjectFactory of = new ObjectFactory();
			final JAXBElement<EnterpriseType> enterprise = of.createEnterprise(a.getEnterprise());
			final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter sw = new StringWriter();
			marshaller.marshal(enterprise, sw);

			final File f = Util.getFile(this.outputDirectory,
					new StringBuffer(64).append(this.enterpriseName).append("-ModelAnalysis.xml").toString());
			this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sw.toString());
			} catch (final Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
			final File fjson = Util.getFile(this.outputDirectory,
					new StringBuffer(64).append(this.enterpriseName).append("-ModelAnalysis.json").toString());
			this.getLog().info(new StringBuffer().append("Write ").append(fjson.getAbsolutePath()));
			try {
				Util.writeToFile(fjson, this.objectMapper.writeValueAsString(enterprise));
			} catch (final Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void writeHeaderJson(final String json) {
		try {
			final File f = Util.getFile(this.outputDirectory, "header-description.json");
			this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, json);
			} catch (final Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	private void writeReport(final String name, final String html, final String json) {
		try {
			final File f = Util.getFile(this.outputDirectory, String.format("%s.html", name));
			this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, html);
			} catch (final Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		if (Objects.nonNull(json)) {
			try {
				final File f = Util.getFile(this.outputDirectory, String.format("%s.json", name));
				this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
				try {
					Util.writeToFile(f, json);
				} catch (final Exception e) {
					this.getLog().error(e.getMessage());
					e.printStackTrace();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
