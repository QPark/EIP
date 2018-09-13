/*******************************************************************************
 * Copyright (c) 2013 - 2017 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.persistence.ddlexecution;

import java.io.File;
import java.io.FileOutputStream;

import com.qpark.eip.core.persistence.DDLGenerator;

/**
 * @author bhausen
 */
@SuppressWarnings("static-method")
public class DDLGeneratorTest {
	/** The persistence unit name. */
	private static final String PERSISTENCE_UNIT_NAME = "com.qpark.eip.core.persistence.test.domain";

	/**
	 * Test implementation to be implemented in the using packages.
	 */
	// @Test
	public void prepareDDLGeneration() {
		DDLGenerator generator = new DDLGenerator(
				DDLGenerator.getWhiteListedClassNames());
		generator.setResultingPersistenceUnitName(PERSISTENCE_UNIT_NAME);
		// generator.setDatabaseProcuct("PostgreSQL", "9", "4");
		try {
			String persistence20Xml = generator
					.createPersistence20Xml(PERSISTENCE_UNIT_NAME);
			File target20 = new File(new StringBuffer(32).append("target")
					.append(File.separatorChar).append("classes")
					.append(File.separatorChar).append("META-INF")
					.append(File.separatorChar).append("persistence.xml")
					.toString());
			if (target20.exists()) {
				try (FileOutputStream fos = new FileOutputStream(target20)) {
					fos.write(persistence20Xml.getBytes());
					fos.flush();
					fos.close();
					System.out
							.println("Wrote to:" + target20.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String persistenceXml = generator
					.createTemporaryPersistenceXml(PERSISTENCE_UNIT_NAME);
			File target = new File(new StringBuffer(32).append("target")
					.append(File.separatorChar).append("classes")
					.append(File.separatorChar).append("META-INF")
					.append(File.separatorChar).append(PERSISTENCE_UNIT_NAME)
					.append(File.separatorChar).append("persistence.xml")
					.toString());
			if (target.exists()) {
				try (FileOutputStream fos = new FileOutputStream(target)) {
					fos.write(persistenceXml.getBytes());
					fos.flush();
					fos.close();
					System.out.println("+TemporaryPersistenceXml");
					System.out.println(persistenceXml);
					System.out.println("-TemporaryPersistenceXml");
					System.out.println("Wrote to:" + target.getAbsolutePath());
					// File targetDDL = new File(
					// new StringBuffer(32).append("target").toString());
					// generator.generateDDL(targetDDL, target);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				System.out.println("Generate:");
				File targetDDL = new File(
						new StringBuffer(32).append("target").toString());
				generator.generateDDL(targetDDL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		generator = new DDLGenerator(DDLGenerator.getWhiteListedClassNames());
		generator.setResultingPersistenceUnitName(PERSISTENCE_UNIT_NAME);
		// generator.setDatabaseProcuct("PostgreSQL", "9", "4");
		try {
			String persistenceXml = generator
					.createTemporaryPersistenceXml(PERSISTENCE_UNIT_NAME);

			File target = new File(new StringBuffer(32).append("target")
					.append(File.separatorChar).append("test-classes")
					.append(File.separatorChar).append("META-INF")
					.append(File.separatorChar).append(PERSISTENCE_UNIT_NAME)
					.append(File.separatorChar).append("persistence.xml")
					.toString());
			if (target.exists()) {
				try (FileOutputStream fos = new FileOutputStream(target)) {
					fos.write(persistenceXml.getBytes());
					fos.flush();
					fos.close();
					System.out.println("+TemporaryPersistenceXml");
					System.out.println(persistenceXml);
					System.out.println("-TemporaryPersistenceXml");
					System.out.println("Wrote to:" + target.getAbsolutePath());
					generator.generateDDL();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
