package com.qpark.eip.core.persistence.ddl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	public static void main(final String[] args) {
		DDLGenerator g = new DDLGenerator();
		String s = g.getPersistenceXml(
				"/META-INF/com.qpark.eip.core.persistence",
				"/META-INF/com.qpark.eip.core.spring.lockedoperation.model");
		System.out.println(s);
		g.generateDDL();
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

	public String getPersistenceXml(final String... persistenceXmlLocations) {
		StringBuffer sb = new StringBuffer(1024);
		if (persistenceXmlLocations != null) {
			for (String persistenceXmlLocation : persistenceXmlLocations) {
				try {
					if (!persistenceXmlLocation.endsWith("persistence.xml")) {
						if (persistenceXmlLocation.endsWith("/")) {
							persistenceXmlLocation = new StringBuffer(
									persistenceXmlLocation)
											.append("persistence.xml")
											.toString();
						} else {
							persistenceXmlLocation = new StringBuffer(
									persistenceXmlLocation)
											.append("/persistence.xml")
											.toString();
						}
					}
					sb.append(this.getPersistenceXmlClasses(
							this.getClass().getResourceAsStream(
									persistenceXmlLocation.trim())));
				} catch (Exception e) {
					System.err.println(String.format("%s: %s",
							persistenceXmlLocation, e.getMessage()));
				}
			}
			if (sb.length() > 0) {
				sb.insert(0, this.getPersistenceXmlBegin());
				sb.append(this.getPersistenceXmlEnd());
			}
		}
		return sb.toString();
	}

	private String getPersistenceXmlBegin() {
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

	private String getPersistenceXmlClasses(final InputStream is) {
		String s = "";
		try (Scanner scanner = new Scanner(is);) {
			String content = scanner.useDelimiter("\\A").next();
			int beginIndex = content.indexOf("<class>");
			if (beginIndex > 0) {
				int endIndex = content.lastIndexOf("</class>");
				if (endIndex > 0) {
					s = new StringBuffer(1024).append("        ")
							.append(content.substring(beginIndex,
									endIndex + "</class>".length()))
							.append("\n").toString();

				}
			}
		}
		return s;
	}

	private String getPersistenceXmlEnd() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\t</persistence-unit>\n");
		sb.append("</persistence>\n");
		return sb.toString();
	}
}
