package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.ServiceIdRegistry;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
@Mojo(name = "generate-flow-mapper",
		defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class GeneratorMapperMojo extends AbstractMojo {
	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory",
			defaultValue = "${project.build.directory}/model")
	private File baseDirectory;
	/** The directory where to put the generated interfaces. */
	@Parameter(property = "outputInterfacesDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
	private File outputInterfacesDirectory;
	/** The directory where to put the generated classes. */
	@Parameter(property = "outputClassesDirectory",
			defaultValue = "${project.build.directory}/generated-sources")
	private File outputClassesDirectory;
	/** The base package name where to place the mappings factories. */
	@Parameter(property = "basePackageName", defaultValue = "")
	private String basePackageName;
	/** The name of the interface id to generate. If empty use all. */
	@Parameter(property = "interfaceId", defaultValue = "")
	private String interfaceId;
	/**
	 * The package names of the mappings should end with - separation by space.
	 * Default is <code>mapping</code>.
	 */
	@Parameter(property = "mappingPackageNameSuffixes",
			defaultValue = "map svc flow")
	protected String mappingPackageNameSuffixes;
	/**
	 * The service request name need to end with this suffix (Default
	 * <code>Request</code>).
	 */
	@Parameter(property = "mappingRequestSuffix", defaultValue = "Request")
	private String mappingRequestSuffix;
	/**
	 * The service response name need to end with this suffix (Default
	 * <code>Response</code>).
	 */
	@Parameter(property = "mappingResponseSuffix", defaultValue = "Response")
	private String mappingResponseSuffix;
	@Component
	protected MavenProject project;

	public static List<ComplexTypeChild> getValidChildren(
			final ComplexType complexType) {
		List<ComplexTypeChild> list = new ArrayList<ComplexTypeChild>(
				complexType.getChildren().size());
		for (ComplexTypeChild child : complexType.getChildren()) {
			if (!child.getComplexType().isSimpleType()
			// && !child.getComplexType().isAbstractType()
			) {
				if (child.getComplexType().getType().getName().getLocalPart()
						.equals("NoMappingType")) {
					/* not to add. */
				} else if (child.getChildName().equals("return")) {
					/* not to add. */
				} else if (child.getComplexType().getType().getName()
						.getLocalPart().equals("anyType")) {
					/* not to add. */
				} else {
					list.add(child);
				}
			}
		}
		return list;
	}

	private Collection<String> getInterfaceIds(final XsdsUtil config) {
		Collection<String> interfaceIds = ServiceIdRegistry
				.getServiceIds(this.interfaceId);
		if (interfaceIds.size() == 0) {
			interfaceIds.addAll(ServiceIdRegistry.getAllServiceIds());
		}
		interfaceIds.add("core");
		return interfaceIds;
	}

	private void generateBasicFlowInterface(final String basicPackageName) {
		StringBuffer sb = new StringBuffer();
		sb.append("package ");
		sb.append(basicPackageName);
		sb.append(";\n");
		sb.append("\n");
		sb.append(
				"import com.springsource.insight.annotation.InsightOperation;");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * Basic flow interface.\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public interface Flow<Request, Response> {\n");
		sb.append("\t/**\n");
		sb.append(
				"\t * Invoke the flow. This calls executeRequest and processResponse.\n");
		sb.append("\t * @param request the {@link Request}\n");
		sb.append("\t * @return the {@link Response}\n");
		sb.append("\t */\n");
		sb.append("\t@InsightOperation\n");
		sb.append("\tResponse invokeFlow(Request request);\n");
		sb.append("}\n");
		sb.append("\n");
		File f = Util.getFile(this.outputInterfacesDirectory, basicPackageName,
				"Flow.java");
		this.getLog().info(new StringBuffer().append("Write ")
				.append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void generateReferenceDataTypeProvider(final XsdsUtil config,
			final String basicPackageName) {
		ComplexType referenceDataType = null;
		for (ComplexType ct : config.getComplexTypes()) {
			if (ct.getClassNameFullQualified().contains("ReferenceDataType")
					&& ct.getClassNameFullQualified().contains("common")) {
				referenceDataType = ct;
				break;
			}
		}
		if (referenceDataType != null) {
			StringBuffer sb = new StringBuffer(256);
			sb.append("package ");
			sb.append(basicPackageName);
			sb.append(";\n");
			sb.append("\n");
			sb.append("import java.util.List;\n");
			sb.append("\n");
			sb.append("import ");
			sb.append(referenceDataType.getClassNameFullQualified());
			sb.append(";\n");
			sb.append("\n");
			sb.append("/**\n");
			sb.append(" * Provides {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("}s to the flows.\n");
			sb.append(" * @author bhausen\n");
			sb.append(" */\n");
			sb.append("public interface ReferenceDataProvider {\n");
			sb.append("\t/**\n");
			sb.append("\t * Get the list of all <i>active</i> {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("}s.\n");
			sb.append("\t * @param userName the user name.\n");
			sb.append("\t * @return the list of all <i>active</i> {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("}s.\n");
			sb.append("\t */\n");
			sb.append("\tList<");
			sb.append(referenceDataType.getClassName());
			sb.append("> getReferenceData(String userName);\n");
			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * Get the list of <i>active</i> {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("}s of a specific\n");
			sb.append("\t * category.\n");
			sb.append("\t * @param category the name of the category.\n");
			sb.append("\t * @param userName the user name.\n");
			sb.append("\t * @return the list of <i>active</i> {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("}s.\n");
			sb.append("\t */\n");
			sb.append("\tList<");
			sb.append(referenceDataType.getClassName());
			sb.append("> getReferenceDataByCategory(final String category,\n");
			sb.append("\t\t\tString userName);\n");
			sb.append("\n");
			sb.append("\t/**\n");
			sb.append("\t * Get the <i>active</i> {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("}s of a specific\n");
			sb.append("\t * referenceData UUID.\n");
			sb.append("\t * @param uuid the UUID of the {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("}.\n");
			sb.append("\t * @param userName the user name.\n");
			sb.append("\t * @return the <i>active</i> {@link ");
			sb.append(referenceDataType.getClassName());
			sb.append("} or <code>null</code>.\n");
			sb.append("\t */\n");
			sb.append("\t");
			sb.append(referenceDataType.getClassName());
			sb.append(
					" getReferenceDataById(final String uuid, String userName);\n");
			sb.append("}\n");
			sb.append("\n");

			File f = Util.getFile(this.outputInterfacesDirectory,
					basicPackageName, "ReferenceDataProvider.java");
			this.getLog().info(new StringBuffer().append("Write ")
					.append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sb.toString());
			} catch (Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().debug("+execute");
		this.getLog().debug("get xsds");
		XsdsUtil config = new XsdsUtil(this.baseDirectory, this.basePackageName,
				this.mappingPackageNameSuffixes, null,
				this.mappingRequestSuffix, this.mappingResponseSuffix);
		ComplexContentList complexContentList = new ComplexContentList();
		complexContentList.setupComplexContentLists(config);

		Collection<String> interfaceIds = this.getInterfaceIds(config);

		String basicPackageName = null;
		for (ComplexType ct : config.getComplexTypes()) {
			if (ct.getPackageName().contains(".inf.")) {
				basicPackageName = ct.getPackageName().substring(0,
						ct.getPackageName().indexOf(".inf.") + 4);
				break;
			}
		}
		if (basicPackageName != null) {
			IllegalStateException ex = null;
			this.generateBasicFlowInterface(basicPackageName);
			for (ComplexType ct : config.getComplexTypes()) {
				if (ct.isFlowInputType()) {
					FlowInterfaceGenerator fig = new FlowInterfaceGenerator(
							config, ct, this.getLog());
					fig.generateInterface(this.outputInterfacesDirectory,
							basicPackageName);
				}
			}
			this.generateReferenceDataTypeProvider(config, basicPackageName);
			Entry<String, String> entry;
			for (ComplexContent cc : complexContentList.getDirectMappings()) {
				try {
					DirectMappingTypeGenerator mtg = new DirectMappingTypeGenerator(
							config, cc.ct, complexContentList, this.getLog());
					entry = mtg
							.generateInterface(this.outputInterfacesDirectory);
					cc.packageName = entry.getKey();
					cc.interfaceName = entry.getValue();
					mtg.generateImpl(this.outputInterfacesDirectory);
				} catch (IllegalStateException e) {
					if (ex != null) {
						ex = e;
					}
				}
			}
			for (ComplexContent cc : complexContentList.getDefaultMappings()) {
				try {
					DefaultMappingTypeGenerator mtg = new DefaultMappingTypeGenerator(
							config, cc.ct, complexContentList, this.getLog());
					entry = mtg
							.generateInterface(this.outputInterfacesDirectory);
					cc.packageName = entry.getKey();
					cc.interfaceName = entry.getValue();
					mtg.generateImpl(this.outputInterfacesDirectory);
				} catch (IllegalStateException e) {
					if (ex != null) {
						ex = e;
					}
				}
			}
			for (ComplexContent cc : complexContentList
					.getComplexUUIDMappings()) {
				ComplexUUIDReferenceDataMappingTypeGenerator mtg = new ComplexUUIDReferenceDataMappingTypeGenerator(
						config, cc.ct, complexContentList, this.getLog());
				entry = mtg.generateInterface(this.outputInterfacesDirectory);
				cc.packageName = entry.getKey();
				cc.interfaceName = entry.getValue();
				mtg.generateImpl(this.outputInterfacesDirectory);
			}
			for (ComplexContent cc : complexContentList.getComplexMappings()) {
				ComplexMappingTypeGenerator mtg = new ComplexMappingTypeGenerator(
						config, cc.ct, complexContentList, this.getLog());
				entry = mtg.generateInterface(this.outputInterfacesDirectory);
				cc.packageName = entry.getKey();
				cc.interfaceName = entry.getValue();
				mtg.generateImpl(this.outputClassesDirectory);
			}
			for (ComplexContent cc : complexContentList.getInterfaceTypes()) {
				InterfaceMappingTypeGenerator mtg = new InterfaceMappingTypeGenerator(
						config, cc.ct, complexContentList, this.getLog());
				entry = mtg.generateInterface(this.outputInterfacesDirectory);
				cc.packageName = entry.getKey();
				cc.interfaceName = entry.getValue();
				mtg.generateImpl(this.outputClassesDirectory);
			}
			for (ComplexRequestResponse crr : complexContentList
					.getRequestResponses()) {
				MappingOperationGenerator mog = new MappingOperationGenerator(
						config, crr.request, crr.response, complexContentList,
						this.getLog());
				entry = mog.generateInterface(this.outputInterfacesDirectory);
				crr.packageName = entry.getKey();
				crr.interfaceName = entry.getValue();
				mog.generateImpl(this.outputClassesDirectory);
			}
			if (ex != null) {
				throw ex;
			}
		}

		this.getLog().debug("-execute");

	}
}
