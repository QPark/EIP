package com.qpark.eip.core.persistence;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DDLGenerator {
	private static final String DEFAULT_PERSISTENCE_UNIT_NAME = "com.qpark.eip.core.persistence.ddl";

	private static void fixGeneratedScriptProblems(final String fileName,
			final String persistenceUnitName, final String databaseProcuctName,
			final String databaseMajorVersion,
			final String databaseMinorVersion, final String scriptType,
			final String generateDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"-----------------------------------------------------------------------------");
		sb.append("\n-- ");
		sb.append(scriptType);
		sb.append(" script of persistence unit ");
		sb.append(persistenceUnitName);
		sb.append(".");
		sb.append("\n-- Target database type: ");
		sb.append(databaseProcuctName);
		sb.append(" ");
		sb.append(databaseMajorVersion);
		sb.append(".");
		sb.append(databaseMinorVersion);
		sb.append("\n-- Generated at ");
		sb.append(generateDate);
		sb.append(".");
		sb.append(
				"\n-----------------------------------------------------------------------------");
		sb.append("\n-- Verify sequence handling at the end of the script");
		sb.append("\n");
		sb.append("\n");
		try (Scanner sc = new Scanner(new File(fileName));) {
			String s;
			while (sc.hasNextLine()) {
				s = sc.nextLine();
				if (s.startsWith("CREATE TABLE SEQUENCE ")) {
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- Hibernate sequence");
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- CREATE SEQUENCE hibernate_sequence;");
					sb.append("\n");
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- EclipseLink sequence");
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- ");
				} else if (s.startsWith("INSERT INTO SEQUENCE")) {
					sb.append("-- ");
				} else if (s.startsWith("DELETE FROM SEQUENCE")) {
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- Hibernate sequence");
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- DROP SEQUENCE hibernate_sequence;");
					sb.append("\n");
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- EclipseLink sequence");
					sb.append(
							"\n-----------------------------------------------------------------------------");
					sb.append("\n-- ");
					sb.append("-- ");
				}
				sb.append(s);
				sb.append(";\n");
			}
			Files.write(Paths.get(fileName), sb.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getPersistenceXmlBegin(
			final String resultingPersistenceUnitName) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		sb.append(
				"<persistence version=\"2.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("\txmlns=\"http://xmlns.jcp.org/xml/ns/persistence\" \n");
		sb.append(
				"\txmlns:orm=\"http://xmlns.jcp.org/xml/ns/persistence/orm\" \n");
		sb.append("\txsi:schemaLocation=\"\n");
		sb.append(
				"\t\thttp://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd\n");
		sb.append(
				"\t\thttp://xmlns.jcp.org/xml/ns/persistence/orm http://xmlns.jcp.org/xml/ns/persistence/orm_2_1.xsd\n");
		sb.append("\t\" \n");
		sb.append(">\n");
		sb.append("\t<persistence-unit name=\"");
		sb.append(resultingPersistenceUnitName);
		sb.append("\" transaction-type=\"RESOURCE_LOCAL\">\n");
		return sb.toString();
	}

	private static String getPersistenceXmlClasses(final InputStream is,
			final List<String> whiteList) {
		StringBuffer sb = new StringBuffer(128);
		String c;
		int numberOfClasses = 0;
		try (Scanner scanner = new Scanner(is);) {
			scanner.useDelimiter("<class>");
			while (scanner.hasNext()) {
				String content = scanner.next();
				int endIndex = content.lastIndexOf("</class>");
				if (endIndex > 0) {
					c = content.substring(0, endIndex);
					if (whiteList != null && whiteList.size() > 0) {
						for (String whiteListed : whiteList) {
							if (c.equals(whiteListed)
								|| c.endsWith(String.format(".%s", whiteListed)) // class name
								|| c.startsWith(String.format("%s.", whiteListed)) // package name
							) {
								sb.append("\t\t<class>").append(c)
										.append("</class>\n");
								numberOfClasses++;
								break;
							}
						}
					} else {
						sb.append("\t\t<class>").append(c).append("</class>\n");
						numberOfClasses++;
					}
				}
			}
		}
		logger.info("Detected {} classes, white listed {}", numberOfClasses, whiteList.size());
		return sb.toString();
	}

	private static String getPersistenceXmlEnd() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t</persistence-unit>\n");
		sb.append("</persistence>\n");
		return sb.toString();
	}

	public static String[] getWhiteListedClassNames() {
		String[] whiteListedClassNames = null;
		try (Scanner scanner = new Scanner(
				DDLGenerator.class.getResourceAsStream(
						"/DDLGeneratorWhiteListedClassNames.txt"));) {
			List<String> whiteList = new ArrayList<String>();
			while (scanner.hasNextLine()) {
				String content = scanner.nextLine();
				if (content != null && content.trim().length() > 0) {
					whiteList.add(content.trim());
				}
			}
			whiteListedClassNames = whiteList.toArray(new String[0]);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return whiteListedClassNames;
	}

	public static void main(final String[] args) {
		// DDLGenerator g = new DDLGenerator();
		// String s = g.createTemporaryPersistenceXml(
		// "com.qpark.eip.core.persistence",
		// "com.qpark.eip.core.spring.lockedoperation.model");
		// System.out.println(s);
		// g.generateDDL();
		// String s = "
		// <class>com.qpark.eip.core.domain.persistencedefinition.ApplicationUserLogType</class>\n
		// <class>com.qpark.eip.core.domain.persistencedefinition.AbstractApplicationUserLogType</class>
		// <class>com.qpark.eip.core.domain.persistencedefinition.ContextListType</class>\n
		// <class>com.qpark.eip.core.domain.persistencedefinition.ContextType</class>\n
		// <class>com.qpark.eip.core.domain.persistencedefinition.FlowLogMessageType</class>\n
		// <class>com.qpark.eip.core.domain.persistencedefinition.GrantedAuthorityType</class>\n
		// <class>com.qpark.eip.core.domain.persistencedefinition.SystemUserLogType</class>\n";
		// List<String> whiteList = new ArrayList<String>();
		// whiteList.add("ApplicationUserLogType");
		// whiteList.add("GrantedAuthorityType");
		// System.out.println(getPersistenceXmlClasses(s, whiteList));
	}

	private static String toPersistenceXmlLocation(final String location) {
		String s = location.trim();
		if (!s.endsWith("persistence.xml")) {
			if (s.endsWith("/")) {
				s = String.format("%spersistence.xml", s);
			} else {
				s = String.format("%s/persistence.xml", s);
			}
		}
		if (!s.startsWith("/")) {
			s = String.format("/%s", s);
		}
		if (!s.startsWith("/META-INF")) {
			s = String.format("/META-INF%s", s);
		}
		return s;
	}

	private String databaseMajorVersion = "10";

	private String databaseMinorVersion = "1";

	private String databaseProcuctName = "Oracle";

	/** The {@link org.slf4j.Logger}. */
	private static Logger logger = LoggerFactory.getLogger(DDLGenerator.class);

	private String resultingPersistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;

	/** A list of classes, which should occur in the generated DDL. */
	private final List<String> whiteList = new ArrayList<String>();

	public DDLGenerator() {
	}

	public DDLGenerator(final String... whiteListedClassNames) {
		if (whiteListedClassNames != null) {
			for (String whiteListed : whiteListedClassNames) {
				if (whiteListed != null) {
					this.whiteList.add(whiteListed);
				}
			}
		}
	}

	public String createTemporaryPersistenceXml(
			final String... persistenceUnitNames) {
		StringBuffer sb = new StringBuffer(1024);
		if (persistenceUnitNames != null) {
			String location;
			InputStream is;
			String classes;
			for (String persistenceXmlLocation : persistenceUnitNames) {
				location = toPersistenceXmlLocation(persistenceXmlLocation);
				logger.info("Search for {} (mapped from {})", location,
						persistenceXmlLocation);
				try {
					is = this.getClass().getResourceAsStream(location);
					classes = DDLGenerator.getPersistenceXmlClasses(is,
							this.whiteList);
					sb.append(classes);
				} catch (Exception e) {
					logger.error("Searched for {}: ", location,
							persistenceXmlLocation);
					e.printStackTrace();
				}
			}
			if (sb.length() > 0) {
				sb.insert(0, DDLGenerator.getPersistenceXmlBegin(
						this.resultingPersistenceUnitName));
				sb.append(DDLGenerator.getPersistenceXmlEnd());
			}
		}
		return sb.toString();
	}

	public void generateDDL() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
		String generateDate = sdf.format(new Date());
		String createFileName = new StringBuffer(64)
				.append(this.resultingPersistenceUnitName).append("-create.sql")
				.toString();
		String dropFileName = new StringBuffer(64)
				.append(this.resultingPersistenceUnitName).append("-drop.sql")
				.toString();

		Properties properties = new Properties();
		properties.setProperty("javax.persistence.database-product-name",
				this.databaseProcuctName);
		properties.setProperty("javax.persistence.database-major-version",
				this.databaseMajorVersion);
		properties.setProperty("javax.persistence.database-minor-version",
				this.databaseMinorVersion);
		properties.setProperty(
				"javax.persistence.schema-generation.scripts.action",
				"drop-and-create");
		properties.setProperty(
				"javax.persistence.schema-generation.scripts.drop-target",
				dropFileName);
		properties.setProperty(
				"javax.persistence.schema-generation.scripts.create-target",
				createFileName);

		// org.eclipse.persistence.config.PersistenceUnitProperties pup;
		properties.put("eclipselink.ddl-generation.index-foreign-keys", "true");

		Persistence.generateSchema(this.resultingPersistenceUnitName,
				properties);

		fixGeneratedScriptProblems(createFileName,
				this.resultingPersistenceUnitName, this.databaseProcuctName,
				this.databaseMajorVersion, this.databaseMinorVersion, "Create",
				generateDate);
		fixGeneratedScriptProblems(dropFileName,
				this.resultingPersistenceUnitName, this.databaseProcuctName,
				this.databaseMajorVersion, this.databaseMinorVersion, "Drop",
				generateDate);
	}

	/**
	 * Set the resulting database product. Alternative value would be
	 * <i>PostgreSQL</i>, <i>9</i>, <i>4</i> or <i>Microsoft SQL Server</i>,
	 * <i>9</i>, <i>0</i>
	 *
	 * @param databaseProcuctName
	 *            the name of the database. Defaults to <i>Oracle</i>
	 * @param databaseMajorVersion
	 *            the major version of the database. Defaults to <i>10</i>
	 * @param databaseMinorVersion
	 *            the minor version of the database. Defaults to <i>1</i>
	 */
	public void setDatabaseProcuct(final String databaseProcuctName,
			final String databaseMajorVersion,
			final String databaseMinorVersion) {
		this.databaseProcuctName = databaseProcuctName;
		this.databaseMajorVersion = databaseMajorVersion;
		this.databaseMinorVersion = databaseMinorVersion;
	}

	/**
	 * @param resultingPersistenceUnitName
	 *            the resultingPersistenceUnitName to set
	 */
	public void setResultingPersistenceUnitName(
			final String resultingPersistenceUnitName) {
		this.resultingPersistenceUnitName = resultingPersistenceUnitName;
	}
}
