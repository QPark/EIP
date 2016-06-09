
/*******************************************************************************
* Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
* accompanying materials are made available under the terms of the Eclipse
* Public License v1.0. The Eclipse Public License is available at
* http://www.eclipse.org/legal/epl-v10.html.
******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
 * This goal generates Flow and mapping interfaces as well as implementations of
 * direct mappers.
 *
 * @author bhausen
 */
@Mojo(name = "generate-flow-mapper", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class GeneratorMapperMojo extends AbstractMojo {
	public static List<ComplexTypeChild> getValidChildren(final ComplexType complexType) {
		List<ComplexTypeChild> list = new ArrayList<ComplexTypeChild>(complexType.getChildren().size());
		for (ComplexTypeChild child : complexType.getChildren()) {
			if (!child.getComplexType().isSimpleType()
			// && !child.getComplexType().isAbstractType()
			) {
				if (child.getComplexType().getType().getName().getLocalPart().equals("NoMappingType")) {
					/* not to add. */
				} else if (child.getChildName().equals("return")) {
					/* not to add. */
				} else if (child.getComplexType().getType().getName().getLocalPart().equals("anyType")) {
					/* not to add. */
				} else {
					list.add(child);
				}
			}
		}
		return list;
	}

	/** The base directory where to start the scan of xsd files. */
	@Parameter(property = "baseDirectory", defaultValue = "${project.build.directory}/model")
	private File baseDirectory;
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
	@Parameter(property = "mappingPackageNameSuffixes", defaultValue = "map svc flow")
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
	/**
	 * The directory where to put the prepared implementation source of the
	 * classes.
	 */
	@Parameter(property = "outputClassesDirectory", defaultValue = "${project.build.directory}/prepared-sources")
	private File outputClassesDirectory;
	/** The directory where to put the generated interfaces. */
	@Parameter(property = "outputInterfacesDirectory", defaultValue = "${project.build.directory}/generated-sources")
	private File outputInterfacesDirectory;
	@Parameter(defaultValue = "${project}", readonly = true)
	protected MavenProject project;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		StaticLoggerBinder.getSingleton().setLog(this.getLog());
		this.getLog().info("+execute");
		this.getLog().debug("get xsds");

		XsdsUtil config = XsdsUtil.getInstance(this.baseDirectory, this.basePackageName,
				this.mappingPackageNameSuffixes, null, this.mappingRequestSuffix, this.mappingResponseSuffix);
		String eipVersion = null;
		if (this.project.getExecutionProject() != null) {
			eipVersion = this.project.getExecutionProject().getVersion();
		}

		ComplexContentList complexContentList = new ComplexContentList();
		complexContentList.setupComplexContentLists(config);

		Collection<String> interfaceIds = this.getInterfaceIds(config);

		String basicPackageName = null;
		for (ComplexType ct : config.getComplexTypes()) {
			if (ct.getPackageName().contains(".inf.")) {
				basicPackageName = ct.getPackageName().substring(0, ct.getPackageName().indexOf(".inf.") + 4);
				break;
			}
		}
		int flows = 0;
		int directMappers = 0;
		int defaultMappers = 0;
		int complexUUIDMappers = 0;
		int complexMappers = 0;
		int tabularMappers = 0;
		int interfaceMappers = 0;
		int mappingOperations = 0;

		if (basicPackageName != null) {
			this.generateBasicFlowInterface(basicPackageName);
			for (ComplexType ct : config.getComplexTypes()) {
				if (ct.isFlowInputType()) {
					FlowInterfaceGenerator fig = new FlowInterfaceGenerator(config, ct, this.getLog());
					fig.generateInterface(this.outputInterfacesDirectory, basicPackageName);
					flows++;
				}
			}
			this.generateReferenceDataTypeProvider(config, basicPackageName);
			List<String> errorMessages = new ArrayList<String>();
			List<AbstractGenerator> generators = new ArrayList<AbstractGenerator>();

			for (ComplexContent cc : complexContentList.getDirectMappings()) {
				try {
					DirectMappingTypeGenerator mtg = new DirectMappingTypeGenerator(config, basicPackageName, cc,
							complexContentList, eipVersion, this.outputInterfacesDirectory, this.outputClassesDirectory,
							this.getLog());
					mtg.generateInterface();
					if (!cc.ct.toQNameString().startsWith("{http://www.qpark.com/Interfaces/MappingTypes}")) {
						generators.add(mtg);
						directMappers++;
					}
				} catch (Exception e) {
					errorMessages.add(e.getMessage());
				}
			}
			for (ComplexContent cc : complexContentList.getDefaultMappings()) {
				try {
					DefaultMappingTypeGenerator mtg = new DefaultMappingTypeGenerator(config, basicPackageName, cc,
							complexContentList, eipVersion, this.outputInterfacesDirectory, this.outputClassesDirectory,
							this.getLog());
					mtg.generateInterface();
					generators.add(mtg);
					defaultMappers++;
				} catch (IllegalStateException e) {
					errorMessages.add(e.getMessage());
				}
			}
			for (ComplexContent cc : complexContentList.getComplexUUIDMappings()) {
				try {
					ComplexUUIDReferenceDataMappingTypeGenerator mtg = new ComplexUUIDReferenceDataMappingTypeGenerator(
							config, basicPackageName, cc, complexContentList, eipVersion,
							this.outputInterfacesDirectory, this.outputClassesDirectory, this.getLog());
					mtg.generateInterface();
					generators.add(mtg);
					complexUUIDMappers++;
				} catch (Exception e) {
					errorMessages.add(e.getMessage());
				}
			}
			for (ComplexContent cc : complexContentList.getComplexMappings()) {
				try {
					ComplexMappingTypeGenerator mtg = new ComplexMappingTypeGenerator(config, basicPackageName, cc,
							complexContentList, eipVersion, this.outputInterfacesDirectory, this.outputClassesDirectory,
							this.getLog());
					mtg.generateInterface();
					generators.add(mtg);
					complexMappers++;
				} catch (Exception e) {
					errorMessages.add(e.getMessage());
				}
			}
			for (ComplexContent cc : complexContentList.getTabularMappings()) {
				try {
					TabularMappingTypeGenerator mtg = new TabularMappingTypeGenerator(config, basicPackageName, cc,
							complexContentList, eipVersion, this.outputInterfacesDirectory, this.outputClassesDirectory,
							this.getLog());
					mtg.generateInterface();
					generators.add(mtg);
					tabularMappers++;
				} catch (Exception e) {
					errorMessages.add(e.getMessage());
				}
			}
			for (ComplexContent cc : complexContentList.getInterfaceTypes()) {
				InterfaceMappingTypeGenerator mtg = new InterfaceMappingTypeGenerator(config, basicPackageName, cc,
						complexContentList, eipVersion, this.outputInterfacesDirectory, this.outputClassesDirectory,
						this.getLog());
				mtg.generateInterface();
				generators.add(mtg);
				interfaceMappers++;
			}
			for (ComplexRequestResponse crr : complexContentList.getRequestResponses()) {
				MappingOperationGenerator mog = new MappingOperationGenerator(config, basicPackageName, crr,
						complexContentList, eipVersion, this.outputInterfacesDirectory, this.outputClassesDirectory,
						this.getLog());
				mog.generateInterface();
				generators.add(mog);
				mappingOperations++;
			}
			for (AbstractGenerator gen : generators) {
				try {
					gen.generateImpl();
				} catch (Exception e) {
					errorMessages.add(e.getMessage());
				}
			}
			for (String errorMessage : errorMessages) {
				this.getLog().error(errorMessage);
			}
		}

		this.getLog().info(String.format("%-40s:%s", "EIP version", eipVersion));
		this.getLog().info(String.format("%-40s:%5d", "Namespaces", config.getXsdContainerMap().size()));
		this.getLog().info(String.format("%-40s:%5d", "ComplexTypes", config.getComplexTypes().size()));
		this.getLog().info(String.format("%-40s:%5d", "ElementTypes", config.getElementTypes().size()));

		this.getLog().info(String.format("%-40s:%5d", "Generated flows", flows));
		this.getLog().info(String.format("%-40s:%5d", "Generated direct mappers", directMappers));
		this.getLog().info(String.format("%-40s:%5d", "Generated default mappers", defaultMappers));
		this.getLog().info(String.format("%-40s:%5d", "Generated complex UUID mappers", complexUUIDMappers));
		this.getLog().info(String.format("%-40s:%5d", "Generated complex mappers", complexMappers));
		this.getLog().info(String.format("%-40s:%5d", "Generated tabular mappers", tabularMappers));
		this.getLog().info(String.format("%-40s:%5d", "Generated interface mappers", interfaceMappers));
		this.getLog().info(String.format("%-40s:%5d", "Generated mapping operations", mappingOperations));

		this.getLog().debug("-execute");

	}

	private void generateBasicFlowInterface(final String basicPackageName) {
		StringBuffer sb = new StringBuffer();
		sb.append("package ");
		sb.append(basicPackageName);
		sb.append(";\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * Basic flow interface.\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public interface Flow<Request, Response> {\n");
		sb.append("\t/**\n");
		sb.append("\t * Invoke the flow. This calls executeRequest and processResponse.\n");
		sb.append("\t * @param request the {@link Request}\n");
		sb.append("\t * @param flowContext the {@link FlowContext}\n");
		sb.append("\t * @return the {@link Response}\n");
		sb.append("\t */\n");
		sb.append("\tResponse invokeFlow(Request request, FlowContext flowContext);\n");
		sb.append("}\n");
		sb.append("\n");
		File f = Util.getFile(this.outputInterfacesDirectory, basicPackageName, "Flow.java");
		this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
		sb.setLength(0);
		sb.append("package ");
		sb.append(basicPackageName);
		sb.append(";\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * Basic flow gateway interface.\n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public interface FlowGateway {\n");
		sb.append("}\n");
		sb.append("\n");
		f = Util.getFile(this.outputInterfacesDirectory, basicPackageName, "FlowGateway.java");
		this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}

		sb.setLength(0);
		sb.append("package ");
		sb.append(basicPackageName);
		sb.append(";\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" * The flow context containing the requester user name, service name and version\n");
		sb.append(" * and the operation name.\n");
		sb.append(" * \n");
		sb.append(" * @author bhausen\n");
		sb.append(" */\n");
		sb.append("public interface FlowContext {\n");
		sb.append("\t/**\n");
		sb.append("\t * Get the operation name of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @return the operation name of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tString getRequesterOperationName();\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Get the service name of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @return the service name of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tString getRequesterServiceName();\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Get the service version of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @return the service version of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tString getRequesterServiceVersion();\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Get the user name of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @return the user name of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tString getRequesterUserName();\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Get the session id.\n");
		sb.append("\t *\n");
		sb.append("\t * @return the session id.\n");
		sb.append("\t */\n");
		sb.append("\tString getSessionId();\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Set the operation name of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @param requesterOperationName\n");
		sb.append("\t *            the operation name of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tvoid setRequesterOperationName(String requesterOperationName);\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Set the service name of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @param requesterServiceName\n");
		sb.append("\t *            the service name of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tvoid setRequesterServiceName(String requesterServiceName);\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Set the service version of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @param requesterServiceVersion\n");
		sb.append("\t *            the service version of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tvoid setRequesterServiceVersion(String requesterServiceVersion);\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Set the user name of the flow requester.\n");
		sb.append("\t *\n");
		sb.append("\t * @param requesterUserName\n");
		sb.append("\t *            the user name of the flow requester.\n");
		sb.append("\t */\n");
		sb.append("\tvoid setRequesterUserName(String requesterUserName);\n");
		sb.append("\n");
		sb.append("\t/**\n");
		sb.append("\t * Set the session id.\n");
		sb.append("\t *\n");
		sb.append("\t * @param sessionId\n");
		sb.append("\t *            the session id.\n");
		sb.append("\t */\n");
		sb.append("\tvoid setSessionId(String sessionId);\n");
		sb.append("\n");
		sb.append("}\n");

		f = Util.getFile(this.outputInterfacesDirectory, basicPackageName, "FlowContext.java");
		this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
		try {
			Util.writeToFile(f, sb.toString());
		} catch (Exception e) {
			this.getLog().error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void generateReferenceDataTypeProvider(final XsdsUtil config, final String basicPackageName) {
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
			sb.append(" getReferenceDataById(final String uuid, String userName);\n");
			sb.append("}\n");
			sb.append("\n");

			File f = Util.getFile(this.outputInterfacesDirectory, basicPackageName, "ReferenceDataProvider.java");
			this.getLog().info(new StringBuffer().append("Write ").append(f.getAbsolutePath()));
			try {
				Util.writeToFile(f, sb.toString());
			} catch (Exception e) {
				this.getLog().error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private Collection<String> getInterfaceIds(final XsdsUtil config) {
		Collection<String> interfaceIds = ServiceIdRegistry.splitServiceIds(this.interfaceId);
		if (interfaceIds.size() == 0) {
			interfaceIds.addAll(ServiceIdRegistry.getAllServiceIds());
		}
		interfaceIds.add("core");
		return interfaceIds;
	}
}
