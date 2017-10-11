/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.flowmapper;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.namespace.QName;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.xmlbeans.SchemaType;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public abstract class AbstractGenerator {
	/**
	 * Adds the {@link Class} of fqName to the list of imports.
	 *
	 * @param fqName
	 * @param imports
	 * @param importClasses
	 */
	public static void addImport(final String fqName, final Set<String> imports,
			final Set<String> importClasses) {
		int index = fqName.lastIndexOf('.');
		if (index > 0) {
			String simpleName = fqName.substring(index + 1, fqName.length());
			if (!importClasses.contains(simpleName)
					&& !fqName.startsWith("java.lang")) {
				imports.add(fqName);
				importClasses.add(simpleName);
			}
		}
	}

	/**
	 * @param children
	 * @return <code>true</code>, if {@link List} needs to be imported, or
	 *         <code>false</code>, if not.
	 */
	public static boolean isChildListImport(
			final List<ComplexTypeChild> children) {
		boolean value = children.stream().filter(ctc -> ctc.isList())
				.findFirst().isPresent();
		return value;
	}

	/**
	 * @param childrenTree
	 * @return <code>true</code>, if {@link List} needs to be imported, or
	 *         <code>false</code>, if not.
	 */
	public static boolean isListImport(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree) {
		boolean value = childrenTree.stream()
				.filter(child -> child.getKey().isList()
						|| isChildListImport(child.getValue()))
				.findFirst().isPresent();
		return value;
	}

	@SuppressWarnings("unused")
	public static void main(final String[] args) {
		String basePackageName = "com.qpark.eip.inf";
		String xsdPath = "C:\\xnb\\dev\\git\\EIP\\qp-enterprise-integration-platform-sample\\sample-domain-gen\\domain-gen-flow\\target\\model";
		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "mapping flow";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = XsdsUtil.getInstance(dif, "a.b.c.bus",
				messagePackageNameSuffix, "delta");
		ComplexContentList complexContentList = new ComplexContentList();
		complexContentList.setupComplexContentLists(xsds);
		System.out
				.println(Util.getDuration(System.currentTimeMillis() - start));

		String eipVersion = "eipVersion";
		String source = null;
		ComplexType ct;
		ComplexContent cc;
		SchemaType type;
		String ctNamespace;
		String ctName;
		SystemStreamLog log = new org.apache.maven.plugin.logging.SystemStreamLog();

		ctName = "ApplicationUserLog.networkLongToDoubleTabularMappingType";
		ctNamespace = "http://www.samples.com/Interfaces/TechnicalSupportMappingTypes";

		// ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		// type = ct.getType().getElementProperties()[0].getType();
		// cc = complexContentList.getComplexContent(ctNamespace, ctName);
		//
		// DirectMappingTypeGenerator mapper = new
		// DirectMappingTypeGenerator(xsds,
		// basePackageName, cc, complexContentList, eipVersion, dif, dif,
		// log);
		// source = mapper.generateImplContent();

		ct = xsds.getComplexType(new QName(ctNamespace, ctName));
		type = ct.getType().getElementProperties()[0].getType();
		cc = complexContentList.getComplexContent(ctNamespace, ctName);

		TabularMappingTypeGenerator directMapper = new TabularMappingTypeGenerator(
				xsds, basePackageName, cc, complexContentList, ctName, dif, dif,
				new org.apache.maven.plugin.logging.SystemStreamLog());
		source = directMapper.generateImplContent();
		System.out.println(source);
	}

	/**
	 * @param target
	 *            the {@link ComplexTypeChild} defining the value to set the
	 *            default to.
	 * @param returnValueChild
	 *            {@link ComplexTypeChild} to defining the return value.
	 * @param defaultString
	 *            the default value as string.
	 * @return the constructor string of the default value.
	 */
	public static String getStringConstructor(final ComplexTypeChild target,
			final ComplexTypeChild returnValueChild,
			final String defaultString) {
		StringBuffer sb = new StringBuffer(128);
		if (Objects.nonNull(target) && Objects.nonNull(target.getComplexType())
				&& Objects.nonNull(
						target.getComplexType().getClassNameFullQualified())
				&& Objects.nonNull(returnValueChild)
				&& Objects.nonNull(defaultString)) {
			if (target.getComplexType().getClassNameFullQualified()
					.equals("java.lang.String")) {
				sb.append("\"");
				sb.append(defaultString);
				sb.append("\"");
			} else if (target.getComplexType().getClassNameFullQualified()
					.equals("java.math.BigDecimal")) {
				sb.append("BigDecimal.valueOf(Double.valueOf(");
				sb.append(defaultString.trim());
				sb.append("))");
			} else if (target.getComplexType().getClassNameFullQualified()
					.equals("java.math.BigInteger")) {
				sb.append("BigInteger.valueOf(Long.valueOf(");
				sb.append(defaultString.trim());
				sb.append("))");
			} else if (target.getComplexType().getClassNameFullQualified()
					.equals("boolean")
					|| target.getComplexType().getClassNameFullQualified()
							.equals("java.lang.Boolean")) {
				sb.append("Boolean.valueOf(\"");
				sb.append(defaultString.trim());
				sb.append("\")");
			} else if (target.getComplexType().getClassNameFullQualified()
					.equals("double")
					|| target.getComplexType().getClassNameFullQualified()
							.equals("java.lang.Double")) {
				sb.append("Double.valueOf(");
				sb.append(defaultString.trim());
				sb.append(")");
			} else if (target.getComplexType().getClassNameFullQualified()
					.equals("float")
					|| target.getComplexType().getClassNameFullQualified()
							.equals("java.lang.Float")) {
				sb.append("Float.valueOf(");
				sb.append(defaultString.trim());
				sb.append(")");
			} else if (target.getComplexType().getClassNameFullQualified()
					.equals("int")
					|| target.getComplexType().getClassNameFullQualified()
							.equals("java.lang.Integer")) {
				sb.append("Integer.valueOf(");
				sb.append(defaultString.trim());
				sb.append(")");
			} else if (target.getComplexType().getClassNameFullQualified()
					.equals("short")
					|| target.getComplexType().getClassNameFullQualified()
							.equals("java.lang.Short")) {
				sb.append("Short.valueOf(");
				sb.append(defaultString.trim());
				sb.append(")");
			}
		}
		if (sb.length() == 0) {
			sb.append(target.getJavaDefaultValue());
		}

		return sb.toString();
	}

	/**
	 * @param returnValueChild
	 *            the {@link ComplexTypeChild} defining the default value.
	 * @return the default value as String.
	 */
	public static String getReturnDefaultString(
			final ComplexTypeChild returnValueChild) {
		String value = null;
		if (Objects.nonNull(returnValueChild)
				&& Objects.nonNull(returnValueChild.getDefaultValue())) {
			value = returnValueChild.getDefaultValue().getStringValue();
		}
		return value;
	}

	public static String toJavadocHeader(final String documentation) {
		int lenght = 77;
		String s = documentation.replaceAll("\\t", " ").replaceAll("\\n", " ")
				.replaceAll("( )+", " ");
		StringBuffer sb = new StringBuffer();
		while (s.length() > 0) {
			int index = s.substring(0, Math.min(lenght, s.length()))
					.lastIndexOf(' ');
			if (s.length() < lenght || index < 0) {
				if (sb.length() > 0) {
					sb.append("\n * ");
				}
				sb.append(s.trim());
				s = "";
			} else {
				if (index > 0) {
					sb.append("\n * ");
					sb.append(s.substring(0, index).trim());
					s = s.substring(index + 1, s.length());
				}
			}
		}
		s = sb.toString().trim();
		if (s.length() > 0 && s.charAt(s.length() - 1) != '.') {
			sb.append(".\n");
		} else {
			sb.append("\n");
		}
		s = sb.toString();
		if (s.charAt(0) == '\'') {
			sb.replace(0, 1, "");
		}
		return sb.toString();

	}

	protected ComplexContentList complexContentList;
	protected XsdsUtil config;
	protected Log log;
	protected String packageName;
	protected String packageNameImpl;
	protected File compileableSourceDirectory;
	protected File preparedSourceDirectory;

	AbstractGenerator(final XsdsUtil config,
			final ComplexContentList complexContentList,
			final File compileableSourceDirectory,
			final File preparedSourceDirectory, final Log log) {
		this.config = config;
		this.complexContentList = complexContentList;
		this.log = log;
		this.compileableSourceDirectory = compileableSourceDirectory;
		this.preparedSourceDirectory = preparedSourceDirectory;
	}

	public abstract void generateImpl();

	public abstract void generateInterface();

	protected void getChildrenImports(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree,
			final Set<String> imports, final Set<String> importClasses) {
		childrenTree.stream().filter(
				child -> !"interfaceName".equals(child.getKey().getChildName()))
				.forEach(child -> {
					addImport(
							child.getKey().getComplexType()
									.getClassNameFullQualified(),
							imports, importClasses);
					child.getValue().stream()
							.filter(grandchild -> !"interfaceName"
									.equals(grandchild.getChildName()))
							.forEach(grandchild -> {
								ComplexContent cc = this.getMapperDefinition(
										grandchild.getComplexType());
								if (Objects.nonNull(cc)) {
									AbstractGenerator.addImport(
											cc.getFQInterfaceName(), imports,
											importClasses);
								}
								AbstractGenerator.addImport(
										grandchild.getComplexType()
												.getClassNameFullQualified(),
										imports, importClasses);
							});
				});
	}

	protected abstract List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> getChildrenTree();

	protected List<ComplexTypeChild> getDefaultingChildren(
			final ComplexType ct) {
		List<ComplexTypeChild> list = ct.getChildren().stream()
				.filter(child -> child.getDefaultValue() != null)
				.collect(Collectors.toList());
		return list;
	}

	protected String getFqImplName() {
		return new StringBuffer(
				this.packageNameImpl.length() + 1 + this.getImplName().length())
						.append(this.packageNameImpl).append(".")
						.append(this.getImplName()).toString();
	}

	protected String getFqInterfaceName() {
		return new StringBuffer(this.packageName.length() + 1
				+ this.getInterfaceName().length()).append(this.packageName)
						.append(".").append(this.getInterfaceName()).toString();
	}

	protected Set<String> getImplImports(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree,
			final String... mandatoryImports) {
		Set<String> imports = new TreeSet<String>();
		Set<String> importClasses = new TreeSet<String>();

		Stream.of(mandatoryImports)
				.forEach(mandatoryImport -> addImport(mandatoryImport, imports,
						importClasses));

		addImport("org.springframework.stereotype.Component", imports,
				importClasses);
		if (isListImport(childrenTree)) {
			addImport("java.util.List", imports, importClasses);
		}

		this.getChildrenImports(childrenTree, imports, importClasses);

		return imports;
	}

	protected String getImplName() {
		return new StringBuffer().append(this.getInterfaceName()).append("Impl")
				.toString();
	}

	protected Set<String> getInterfaceImports(
			final List<ComplexTypeChild> children,
			final String... mandatoryImports) {
		Set<String> imports = new TreeSet<String>();
		Set<String> importClasses = new TreeSet<String>();

		Stream.of(mandatoryImports)
				.forEach(mandatoryImport -> addImport(mandatoryImport, imports,
						importClasses));

		children.stream()
				.forEach(child -> addImport(
						child.getComplexType().getClassNameFullQualified(),
						imports, importClasses));

		if (isChildListImport(children)) {
			addImport("java.util.List", imports, importClasses);
		}
		return imports;
	}

	protected abstract String getInterfaceName();

	protected String getLoggerDefinition() {
		StringBuffer sb = new StringBuffer(256);
		sb.append("\t/** The {@link org.slf4j.Logger}. */\n");
		sb.append("\tprivate Logger logger = LoggerFactory.getLogger(");
		sb.append(this.getImplName());
		sb.append(".class);\n");
		return sb.toString();
	}

	protected ComplexContent getMapperDefinition(final ComplexType ct) {
		ComplexContent cc = this.complexContentList.getComplexContent(
				ct.getTargetNamespace(), ct.getQNameLocalPart());
		if (Objects.nonNull(cc) && cc.isInterfaceType) {
			cc = null;
		}
		return cc;
	}

	protected String getMapperDefinitions(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> children,
			final Set<String> importedClasses) {
		StringBuffer sb = new StringBuffer(1024);
		TreeSet<String> usedInterfaces = new TreeSet<String>();
		children.stream().forEach(
				child -> child.getValue().stream().forEach(grandchild -> {
					ComplexContent cc = this
							.getMapperDefinition(grandchild.getComplexType());
					if (cc != null && !usedInterfaces
							.contains(cc.getFQInterfaceName())) {
						usedInterfaces.add(cc.getFQInterfaceName());
						String varName = Util.lowerize(cc.interfaceClassName);
						String className = cc.getFQInterfaceName();
						className = cc.getInterfaceName();
						sb.append("\t/** The ");
						// if (cc.isDirect) {
						// sb.append("{@link DirectMappingType} ");
						// }
						sb.append("{@link ");
						sb.append(className);
						sb.append("}. */\n");
						sb.append("\t@Autowired\n");
						sb.append("\tprivate ");
						sb.append(className);
						sb.append(" ");
						sb.append(varName);
						sb.append(";\n");
					}
				}));
		return sb.toString();
	}

	protected String getMethodArgs(final List<ComplexTypeChild> children) {
		StringBuffer sb = new StringBuffer(128);
		int i = 0;
		children.stream()
				.filter(child -> Objects.isNull(child.getDefaultValue()))
				.forEach(child -> {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(child.getJavaVarDefinitionFullQualified());
				});
		return sb.toString();
	}

	protected String getPackageNameImpl() {
		return new StringBuffer(this.getPackageNameInterface()).append(".impl")
				.toString();
	}

	protected abstract String getPackageNameInterface();
}
