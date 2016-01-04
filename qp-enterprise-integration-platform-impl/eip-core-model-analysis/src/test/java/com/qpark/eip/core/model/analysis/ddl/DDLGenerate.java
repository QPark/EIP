package com.qpark.eip.core.model.analysis.ddl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.persistence.Persistence;

public class DDLGenerate {
    public static void main(final String[] args) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
	String generateDate = sdf.format(new Date());
	String persistenceUnitName = "com.qpark.eip.model.docmodel";
	String createFileName = new StringBuffer(64).append(persistenceUnitName).append("-create.sql").toString();
	String dropFileName = new StringBuffer(64).append(persistenceUnitName).append("-drop.sql").toString();

	Properties properties = new Properties();
	properties.setProperty("javax.persistence.database-product-name", "Oracle");
	properties.setProperty("javax.persistence.database-major-version", "10");
	properties.setProperty("javax.persistence.database-minor-version", "1");
	properties.setProperty("javax.persistence.schema-generation.scripts.action", "drop-and-create");
	properties.setProperty("javax.persistence.schema-generation.scripts.drop-target", dropFileName);
	properties.setProperty("javax.persistence.schema-generation.scripts.create-target", createFileName);

	// org.eclipse.persistence.config.PersistenceUnitProperties pup;
	properties.put("eclipselink.ddl-generation.index-foreign-keys", "true");

	Persistence.generateSchema(persistenceUnitName, properties);

	fixGeneratedScriptProblems(createFileName, persistenceUnitName, "Create", generateDate);
	fixGeneratedScriptProblems(dropFileName, persistenceUnitName, "Drop", generateDate);
    }

    private static void fixGeneratedScriptProblems(final String fileName, final String persistenceUnitName,
	    final String scriptType, final String generateDate) {
	StringBuffer sb = new StringBuffer();
	sb.append("-----------------------------------------------------------------------------");
	sb.append("\n-- ");
	sb.append(scriptType);
	sb.append(" script of persistence unit ");
	sb.append(persistenceUnitName);
	sb.append(".");
	sb.append("\n-- Generated at ");
	sb.append(generateDate);
	sb.append(".");
	sb.append("\n-----------------------------------------------------------------------------");
	sb.append("\n-- Verify sequence handling at the end of the script");
	sb.append("\n");
	sb.append("\n");
	try (Scanner sc = new Scanner(new File(fileName));) {
	    String s;
	    while (sc.hasNextLine()) {
		s = sc.nextLine();
		if (s.startsWith("CREATE TABLE SEQUENCE ")) {
		    sb.append("\n-----------------------------------------------------------------------------");
		    sb.append("\n-- Hibernate sequence");
		    sb.append("\n-----------------------------------------------------------------------------");
		    sb.append("\n-- CREATE SEQUENCE hibernate_sequence;");
		    sb.append("\n");
		    sb.append("\n-----------------------------------------------------------------------------");
		    sb.append("\n-- EclipseLink sequence");
		    sb.append("\n-----------------------------------------------------------------------------");
		    sb.append("\n-- ");
		} else if (s.startsWith("INSERT INTO SEQUENCE")) {
		    sb.append("-- ");
		} else if (s.startsWith("DELETE FROM SEQUENCE")) {
		    sb.append("\n-----------------------------------------------------------------------------");
		    sb.append("\n-- Hibernate sequence");
		    sb.append("\n-----------------------------------------------------------------------------");
		    sb.append("\n-- DROP SEQUENCE hibernate_sequence;");
		    sb.append("\n");
		    sb.append("\n-----------------------------------------------------------------------------");
		    sb.append("\n-- EclipseLink sequence");
		    sb.append("\n-----------------------------------------------------------------------------");
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
}
