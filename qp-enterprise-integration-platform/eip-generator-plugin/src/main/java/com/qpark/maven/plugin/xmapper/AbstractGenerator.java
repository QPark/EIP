package com.qpark.maven.plugin.xmapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;

import com.qpark.maven.Util;
import com.qpark.maven.plugin.xmapper.GeneratorMapper.ComplexContent;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

/**
 * @author bhausen
 */
public abstract class AbstractGenerator {
	public static final void addImport(final String fqName,
			final Set<String> imports, final Set<String> importClasses) {
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

	public static final boolean isChildListImport(
			final List<ComplexTypeChild> children) {
		boolean value = false;
		for (ComplexTypeChild child : children) {
			if (child.isList()) {
				value = true;
				break;
			}
		}
		return value;
	}

	public static final boolean isListImport(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree) {
		boolean value = false;
		for (Entry<ComplexTypeChild, List<ComplexTypeChild>> child : childrenTree) {
			if (child.getKey().isList()) {
				value = true;
			} else {
				value = isChildListImport(child.getValue());
			}
			if (value) {
				break;
			}
		}
		return value;
	}

	protected final List<ComplexContent> complexMappings;
	protected final XsdsUtil config;
	protected final List<ComplexContent> defaultMappings;
	protected final List<ComplexContent> directMappings;
	protected final List<ComplexContent> interfaceTypes;
	protected final Log log;
	protected String packageName;
	protected String packageNameImpl;

	AbstractGenerator(final XsdsUtil config,
			final List<ComplexContent> directMappings,
			final List<ComplexContent> defaultMappings,
			final List<ComplexContent> complexMappings,
			final List<ComplexContent> interfaceTypes, final Log log) {
		this.config = config;
		this.directMappings = directMappings;
		this.defaultMappings = defaultMappings;
		this.complexMappings = complexMappings;
		this.interfaceTypes = interfaceTypes;
		this.log = log;
	}

	protected final void getChildrenImports(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree,
			final Set<String> imports, final Set<String> importClasses) {
		ComplexContent cc = null;
		for (Entry<ComplexTypeChild, List<ComplexTypeChild>> child : childrenTree) {
			if (!"interfaceName".equals(child.getKey().getChildName())) {
				AbstractGenerator.addImport(child.getKey().getComplexType()
						.getClassNameFullQualified(), imports, importClasses);
			}
			for (ComplexTypeChild grandchild : child.getValue()) {
				cc = this.getMapperDefinition(grandchild.getComplexType());
				if (cc != null) {
					AbstractGenerator.addImport(cc.getFQInterfaceName(),
							imports, importClasses);
				}
				if (!"interfaceName".equals(grandchild.getChildName())) {
					AbstractGenerator.addImport(grandchild.getComplexType()
							.getClassNameFullQualified(), imports,
							importClasses);
				}
			}
		}
	}

	protected abstract List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> getChildrenTree();

	protected List<ComplexTypeChild> getDefaultingChildren(final ComplexType ct) {
		List<ComplexTypeChild> list = new ArrayList<ComplexTypeChild>(ct
				.getChildren().size());
		for (ComplexTypeChild child : ct.getChildren()) {
			if (child.getDefaultValue() != null) {
				list.add(child);
			}
		}
		return list;
	}

	protected final String getFqImplName() {
		return new StringBuffer(this.packageNameImpl.length() + 1
				+ this.getImplName().length()).append(this.packageNameImpl)
				.append(".").append(this.getImplName()).toString();
	}

	protected final String getFqInterfaceName() {
		return new StringBuffer(this.packageName.length() + 1
				+ this.getInterfaceName().length()).append(this.packageName)
				.append(".").append(this.getInterfaceName()).toString();
	}

	protected final Set<String> getImplImports(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> childrenTree,
			final String... mandatoryImports) {
		Set<String> imports = new TreeSet<String>();
		Set<String> importClasses = new TreeSet<String>();

		for (String mandatoryImport : mandatoryImports) {
			AbstractGenerator
					.addImport(mandatoryImport, imports, importClasses);
		}

		AbstractGenerator.addImport("org.springframework.stereotype.Component",
				imports, importClasses);
		if (AbstractGenerator.isListImport(childrenTree)) {
			AbstractGenerator.addImport("java.util.List", imports,
					importClasses);
		}

		this.getChildrenImports(childrenTree, imports, importClasses);

		return imports;
	}

	protected final String getImplName() {
		return new StringBuffer().append(this.getInterfaceName())
				.append("Impl").toString();
	}

	protected final Set<String> getInterfaceImports(
			final List<ComplexTypeChild> children,
			final String... mandatoryImports) {
		Set<String> imports = new TreeSet<String>();
		Set<String> importClasses = new TreeSet<String>();

		for (String importClass : mandatoryImports) {
			AbstractGenerator.addImport(importClass, imports, importClasses);
		}
		for (ComplexTypeChild child : children) {
			AbstractGenerator.addImport(child.getComplexType()
					.getClassNameFullQualified(), imports, importClasses);
		}

		if (AbstractGenerator.isChildListImport(children)) {
			AbstractGenerator.addImport("java.util.List", imports,
					importClasses);
		}
		return imports;
	}

	protected abstract String getInterfaceName();

	protected final String getLoggerDefinition() {
		StringBuffer sb = new StringBuffer(256);
		sb.append("\t/** The {@link org.slf4j.Logger}. */\n");
		sb.append("\tprivate Logger logger = LoggerFactory.getLogger(");
		sb.append(this.getImplName());
		sb.append(".class);\n");
		return sb.toString();
	}

	protected final ComplexContent getMapperDefinition(final ComplexType ct) {
		ComplexContent cc = null;
		for (ComplexContent complexContent : this.directMappings) {
			if (complexContent.ct.getClassNameFullQualified().equals(
					ct.getClassNameFullQualified())) {
				cc = complexContent;
				break;
			}
		}
		if (cc == null) {
			for (ComplexContent complexContent : this.defaultMappings) {
				if (complexContent.ct.getClassNameFullQualified().equals(
						ct.getClassNameFullQualified())) {
					cc = complexContent;
					break;
				}
			}
		}
		if (cc == null) {
			for (ComplexContent complexContent : this.complexMappings) {
				if (complexContent.ct.getClassNameFullQualified().equals(
						ct.getClassNameFullQualified())) {
					cc = complexContent;
					break;
				}
			}
		}
		if (cc == null) {
			// for (ComplexContent complexContent : this.interfaceTypes) {
			// if (complexContent.ct.getClassNameFullQualified().equals(
			// ct.getClassNameFullQualified())) {
			// cc = complexContent;
			// break;
			// }
			// }
		}
		return cc;
	}

	protected String getMapperDefinitions(
			final List<Entry<ComplexTypeChild, List<ComplexTypeChild>>> children,
			final Set<String> importedClasses) {
		StringBuffer sb = new StringBuffer(1024);
		ComplexContent cc = null;
		TreeSet<String> usedInterfaces = new TreeSet<String>();
		for (Entry<ComplexTypeChild, List<ComplexTypeChild>> child : children) {
			for (ComplexTypeChild grandchild : child.getValue()) {
				cc = this.getMapperDefinition(grandchild.getComplexType());
				if (cc != null
						&& !usedInterfaces.contains(cc.getFQInterfaceName())) {
					usedInterfaces.add(cc.getFQInterfaceName());
					String varName = Util.lowerize(cc.interfaceName);
					String className = cc.interfaceName;
					if (!importedClasses.contains(cc.getFQInterfaceName())) {
						className = cc.getFQInterfaceName();
					}
					sb.append("\t/** The ");
					if (cc.isDirect) {
						sb.append("{@link DirectMappingType} ");
					}
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

			}
		}
		return sb.toString();
	}

	protected String getMethodArgs(final List<ComplexTypeChild> children) {
		StringBuffer sb = new StringBuffer(128);
		int i = 0;
		for (ComplexTypeChild child : children) {
			if (child.getDefaultValue() == null) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(child.getJavaVarDefinition());
				i++;
			}
		}
		return sb.toString();
	}

	protected final String getPackageNameImpl() {
		return new StringBuffer(this.getPackageNameInterface()).append(".impl")
				.toString();
	}

	protected abstract String getPackageNameInterface();
}
