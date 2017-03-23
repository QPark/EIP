/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.xjc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xml.sax.ErrorHandler;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumConstantOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSTerm;

/**
 * Idea taken from
 * org.jvnet.jaxb2_commons.defaultvalueplugin.DefaultValuePlugin.
 *
 * @author bhausen
 */
public class EipXsdDefaultValuePlugin extends Plugin {
	/**
	 * Name of Option to enable this plugin
	 */
	static private final String OPTION_NAME = "Xeip-xsd-default-value";

	/**
	 * Creates a new <code>DefaultValuePlugin</code> instance.
	 */
	public EipXsdDefaultValuePlugin() {
	}

	/**
	 * DefaultValuePlugin uses "-XenhancedDefaultValue" as the command-line
	 * argument
	 */
	@Override
	public String getOptionName() {
		return OPTION_NAME;
	}

	/**
	 * Run the plugin. We perform the following steps:
	 * <ul>
	 * <li>Look for fields that:
	 * <ul>
	 * <li>Were generated from XSD description</li>
	 * <li>The XSD description is of type xsd:element (code level default values
	 * are not necessary for fields generated from attributes)</li>
	 * <li>A default value is specified</li>
	 * <li>Map to one of the supported types</li>
	 * </ul>
	 * </li>
	 * <li>Add a new initializsation expression to every field found</li>
	 * </ul>
	 */
	@Override
	public boolean run(final Outline outline, final Options opt,
			final ErrorHandler errorHandler) {
		// For all Classes generated
		for (final ClassOutline co : outline.getClasses()) {

			// Some conversions may have to add class level code
			JFieldVar dtf = null; // Helper code: DatatypeFactory

			// check all Fields in Class
			for (final FieldOutline f : co.getDeclaredFields()) {
				final CPropertyInfo fieldInfo = f.getPropertyInfo();

				// Do nothing if Field is not created from an xsd particle
				if (!(fieldInfo.getSchemaComponent() instanceof XSParticle)) {
					continue;
				}
				final XSTerm term = ((XSParticle) fieldInfo
						.getSchemaComponent()).getTerm();

				// Default values only necessary for fields derived from an
				// xsd:element
				if (!term.isElementDecl()) {
					continue;
				}
				final XSElementDecl element = term.asElementDecl();

				// Do nothing if no default value
				if (element.getDefaultValue() == null) {
					continue;
				}
				final String defaultValue = element.getDefaultValue().value;

				// Get handle to JModel representing the field
				final Map<String, JFieldVar> fields = co.implClass.fields();
				final JFieldVar var = fields.get(fieldInfo.getName(false));

				// Handle primitive types via boxed representation (treat
				// boolean as java.lang.Boolean)
				JType type = f.getRawType();
				if (type.isPrimitive()) {
					type = type.boxify();
				}
				final String typeFullName = type.fullName();

				// Create an appropriate default expression depending on type
				if ("java.lang.String".equals(typeFullName)) {
					var.init(JExpr.lit(defaultValue));
					if (opt.verbose) {
						System.out
								.println("[INFO] Initializing String variable "
										+ fieldInfo.displayName() + " to \""
										+ defaultValue + "\"");
					}
				}

				else if ("java.math.BigDecimal".equals(typeFullName)) {
					final JExpression bigDecimalExpression = getBigDecimalExpression(
							type, defaultValue);
					if (bigDecimalExpression != null) {
						var.init(bigDecimalExpression);
						if (opt.verbose) {
							System.out.println(
									"[INFO] Initializing BigDecimal variable "
											+ fieldInfo.displayName() + " to \""
											+ defaultValue + "\"");
						}
					}
				}

				else if ("java.math.BigInteger".equals(typeFullName)) {
					final JExpression bigIntegerExpression = getBigIntegerExpression(
							type, defaultValue);
					if (bigIntegerExpression != null) {
						var.init(bigIntegerExpression);
						if (opt.verbose) {
							System.out.println(
									"[INFO] Initializing BigInteger variable "
											+ fieldInfo.displayName() + " to \""
											+ defaultValue + "\"");
						}
					}
				}

				else if ("java.lang.Boolean".equals(typeFullName)) {
					var.init(JExpr.lit(Boolean.valueOf(defaultValue)));
					if (opt.verbose) {
						System.out
								.println("[INFO] Initializing Boolean variable "
										+ fieldInfo.displayName() + " to "
										+ defaultValue + "");
					}
				}

				else if (("java.lang.Byte".equals(typeFullName))
						|| ("java.lang.Short".equals(typeFullName))
						|| ("java.lang.Integer".equals(typeFullName))) {
					// CodeModel does not distinguish between Byte, Short and
					// Integer literals
					var.init(JExpr.lit(Integer.valueOf(defaultValue)));
					if (opt.verbose) {
						System.out
								.println("[INFO] Initializing Integer variable "
										+ fieldInfo.displayName() + " to "
										+ defaultValue + "");
					}
				}

				else if ("java.lang.Long".equals(typeFullName)) {
					var.init(JExpr.lit(Long.valueOf(defaultValue)));
					if (opt.verbose) {
						System.out.println("[INFO] Initializing Long variable "
								+ fieldInfo.displayName() + " to "
								+ defaultValue + "");
					}
				}

				else if ("java.lang.Float".equals(typeFullName)) {
					var.init(JExpr.lit(Float.valueOf(defaultValue)));
					if (opt.verbose) {
						System.out.println("[INFO] Initializing Float variable "
								+ fieldInfo.displayName() + " to "
								+ defaultValue + "");
					}
				}

				else if (("java.lang.Single".equals(typeFullName))
						|| ("java.lang.Double".equals(typeFullName))) {
					// CodeModel does not distinguish between Single and Double
					// literals
					var.init(JExpr.lit(Double.valueOf(defaultValue)));
					if (opt.verbose) {
						System.out
								.println("[INFO] Initializing Double variable "
										+ fieldInfo.displayName() + " to "
										+ defaultValue + "");
					}
				}

				else if ("javax.xml.datatype.XMLGregorianCalendar"
						.equals(typeFullName)) {
					// XMLGregorianCalender is constructed by DatatypeFactory,
					// so we have to have
					// an instance of that once per class
					if (dtf == null) {
						dtf = this.installDtF(co.implClass);
						if (dtf == null) {
							continue;
						}
					}
					// Use our DtF instance to generate the initialization
					// expression
					var.init(JExpr.invoke(dtf, "newXMLGregorianCalendar")
							.arg(defaultValue));
					if (opt.verbose) {
						System.out.println(
								"[INFO] Initializing XMLGregorianCalendar variable "
										+ fieldInfo.displayName()
										+ " with value of " + defaultValue);
					}
				}

				else if ("javax.xml.datatype.Duration".equals(typeFullName)) {
					// Duration is constructed by DatatypeFactory,
					// so we have to have
					// an instance of that once per class
					if (dtf == null) {
						dtf = this.installDtF(co.implClass);
						if (dtf == null) {
							continue;
						}
					}
					// Use our DtF instance to generate the initialization
					// expression
					var.init(
							JExpr.invoke(dtf, "newDuration").arg(defaultValue));
					if (opt.verbose) {
						System.out.println(
								"[INFO] Initializing Duration variable "
										+ fieldInfo.displayName()
										+ " with value of " + defaultValue);
					}
				}

				else if ((type instanceof JDefinedClass)
						&& (((JDefinedClass) type)
								.getClassType() == ClassType.ENUM)) {
					// Find Enum constant
					final JEnumConstant constant = this.findEnumConstant(type,
							defaultValue, outline);
					if (constant != null) {
						var.init(constant);
						if (opt.verbose) {
							System.out.println(
									"[INFO] Initializing enum variable "
											+ fieldInfo.displayName()
											+ " with constant "
											+ constant.getName());
						}
					}
				}

				// Don't know how to create default for this type
				else {
					System.out.println(
							"[WARN] Did not create default value for field "
									+ fieldInfo.displayName()
									+ ". Don't know how to create default value expression for fields of type "
									+ typeFullName + ". Default value of \""
									+ defaultValue + "\" specified in schema");
				}

			} // for FieldOutline

		} // for ClassOutline

		return true;
	}

	/**
	 * Retrieve the enum constant that correlates to the string value.
	 *
	 * @param enumType
	 *            Type identifying an Enum in the code model
	 * @param enumStringValue
	 *            Lexical value of the constant to search
	 * @param outline
	 *            Outline of the code model
	 * @return The matching Constant from the enum type or NULL if not found
	 */
	private JEnumConstant findEnumConstant(final JType enumType,
			final String enumStringValue, final Outline outline) {
		// Search all Enums generated
		for (final EnumOutline eo : outline.getEnums()) {
			// Is it the type of my variable?
			if (eo.clazz == enumType) {
				// Search all Constants of that enum
				for (final EnumConstantOutline eco : eo.constants) {
					// Is the enum generated from the XML defaut value string?
					if (eco.target.getLexicalValue().equals(enumStringValue)) {
						return eco.constRef;
					}
				} // for Constants
				  // Did not find the constant???
				System.out.println(
						"[WARN] Could not find EnumConstant for value: "
								+ enumStringValue);
				return null;
			}
		}
		// Did not find the type??
		System.out.println("[WARN] Could not find Enum class for type: "
				+ enumType.fullName());
		return null;
	}

	/**
	 * @param parentClass
	 * @param defaultValue
	 * @return the {@link JExpression} creating the {@link BigDecimal}.
	 */
	private static JExpression getBigDecimalExpression(final JType parentClass,
			final String defaultValue) {
		JExpression value = null;
		if (defaultValue != null && defaultValue.trim().length() > 0) {
			try {
				/* Test parsing is working. */
				Double.valueOf(defaultValue);

				final JCodeModel cm = parentClass.owner();
				final JInvocation doubleValue = cm.ref(Double.class)
						.staticInvoke("valueOf").arg(defaultValue);
				value = cm.ref(BigDecimal.class).staticInvoke("valueOf")
						.arg(doubleValue);
			} catch (final Exception e) {
				// We don't want JAXB to break of any plugin error
				System.out.println("[ERROR] Failed to create code");
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * @param parentClass
	 * @param defaultValue
	 * @return the {@link JExpression} creating the {@link BigInteger}.
	 */
	private static JExpression getBigIntegerExpression(final JType parentClass,
			final String defaultValue) {
		JExpression value = null;
		if (defaultValue != null && defaultValue.trim().length() > 0) {
			try {
				/* Test parsing is working. */
				Long.valueOf(defaultValue);

				final JCodeModel cm = parentClass.owner();
				final JInvocation longValue = cm.ref(Long.class)
						.staticInvoke("valueOf").arg(defaultValue);
				value = cm.ref(BigInteger.class).staticInvoke("valueOf")
						.arg(longValue);
			} catch (final Exception e) {
				// We don't want JAXB to break of any plugin error
				System.out.println("[ERROR] Failed to create code");
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * Enhance the CodeModel of a Class to include a {@link DatatypeFactory} as
	 * a static private field. The factory is needed to construct
	 * {@link XMLGregorianCalendar} from String representation.
	 *
	 * @param parentClass
	 *            Class where the DatatypeFactory will be created
	 * @return Reference to the created static field
	 */
	private JFieldVar installDtF(final JDefinedClass parentClass) {
		try {
			final JCodeModel cm = parentClass.owner();
			// Create a static variable of type DatatypeFactory
			final JClass dtfClass = cm.ref(DatatypeFactory.class);
			final JFieldVar dtf = parentClass.field(
					JMod.STATIC | JMod.FINAL | JMod.PRIVATE, dtfClass,
					"DATATYPE_FACTORY");
			// Initialize variable in static block
			final JBlock si = parentClass.init();
			final JTryBlock tryBlock = si._try();
			tryBlock.body().assign(dtf, dtfClass.staticInvoke("newInstance"));
			// Catch exception & rethrow as unchecked Exception
			final JCatchBlock catchBlock = tryBlock
					._catch(cm.ref(DatatypeConfigurationException.class));
			final JVar ex = catchBlock.param("ex");
			final JClass runtimeException = cm.ref(RuntimeException.class);
			catchBlock.body()._throw(JExpr._new(runtimeException)
					.arg("Unable to initialize DatatypeFactory").arg(ex));
			// Return reference to initialized static field
			return dtf;
		} catch (final Exception e) {
			// We don't want JAXB to break of any plugin error
			System.out.println("[ERROR] Failed to create code");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getUsage() {
		return "  -" + OPTION_NAME
				+ "    : enable rewriting of classes to set default values for fields as specified in XML schema";
	}

}
