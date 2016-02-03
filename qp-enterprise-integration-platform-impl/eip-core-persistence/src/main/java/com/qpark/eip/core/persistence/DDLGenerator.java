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

public class DDLGenerator {
	private static final String PERSISTENCE_UNIT_NAME = "com.qpark.eip.core.persistence.ddl";

	private static void fixGeneratedScriptProblems(final String fileName,
			final String persistenceUnitName, final String scriptType,
			final String generateDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"-----------------------------------------------------------------------------");
		sb.append("\n-- ");
		sb.append(scriptType);
		sb.append(" script of persistence unit ");
		sb.append(persistenceUnitName);
		sb.append(".");
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

	private static String getPersistenceXmlBegin() {
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
		sb.append(PERSISTENCE_UNIT_NAME);
		sb.append("\" transaction-type=\"RESOURCE_LOCAL\">\n");
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

	private static String getPersistenceXmlClasses(final InputStream is,
			final List<String> whiteList) {
		StringBuffer sb = new StringBuffer(128);
		String c;
		try (Scanner scanner = new Scanner(is);) {
			scanner.useDelimiter("<class>");
			while (scanner.hasNext()) {
				String content = scanner.next();
				int endIndex = content.lastIndexOf("</class>");
				if (endIndex > 0) {
					c = content.substring(0, endIndex);
					if (whiteList != null && whiteList.size() > 0) {
						for (String whiteListed : whiteList) {
							if (c.equals(whiteListed) || c.endsWith(
									String.format(".%s", whiteListed))) {
								sb.append("\t\t<class>").append(c)
										.append("</class>\n");
								break;
							}
						}
					} else {
						sb.append("\t\t<class>").append(c).append("</class>\n");
					}
				}
			}
		}
		return sb.toString();
	}

	private static String getPersistenceXmlEnd() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t</persistence-unit>\n");
		sb.append("</persistence>\n");
		return sb.toString();
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
				try {
					is = this.getClass().getResourceAsStream(location);
					classes = DDLGenerator.getPersistenceXmlClasses(is,
							this.whiteList);
					sb.append(classes);
				} catch (Exception e) {
					System.err.println(String.format("[%s] %s: %s", location,
							e.getClass().getName(), e.getMessage()));
				}
			}
			if (sb.length() > 0) {
				sb.insert(0, DDLGenerator.getPersistenceXmlBegin());
				sb.append(DDLGenerator.getPersistenceXmlEnd());
			}
		}
		return sb.toString();
	}

	public void generateDDL() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
		String generateDate = sdf.format(new Date());
		String createFileName = new StringBuffer(64)
				.append(PERSISTENCE_UNIT_NAME).append("-create.sql").toString();
		String dropFileName = new StringBuffer(64).append(PERSISTENCE_UNIT_NAME)
				.append("-drop.sql").toString();

		Properties properties = new Properties();
		properties.setProperty("javax.persistence.database-product-name",
				"Oracle");
		properties.setProperty("javax.persistence.database-major-version",
				"10");
		properties.setProperty("javax.persistence.database-minor-version", "1");
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

		Persistence.generateSchema(PERSISTENCE_UNIT_NAME, properties);

		fixGeneratedScriptProblems(createFileName, PERSISTENCE_UNIT_NAME,
				"Create", generateDate);
		fixGeneratedScriptProblems(dropFileName, PERSISTENCE_UNIT_NAME, "Drop",
				generateDate);
	}
}
