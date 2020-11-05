package com.qpark.eip.core.persistence.test;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

import com.qpark.eip.core.persistence.DDLGenerator;
import com.qpark.eip.core.model.analysis.persistenceconfig.PersistenceConfig;

/**
 * @author bhausen
 */
public class DDLGeneratorTest {
	@Test
	public void prepareDDLGeneration() {
		DDLGenerator generator = new DDLGenerator(
				DDLGenerator.getWhiteListedClassNames());
		generator.setResultingPersistenceUnitName(PersistenceConfig.PERSISTENCE_UNIT_NAME);
		generator.setDatabaseProcuct("PostgreSQL", "9", "4");

		try {
			String persistence20Xml = generator
					.createPersistence20Xml(PersistenceConfig.PERSISTENCE_UNIT_NAME);
			File target20 = new File(new StringBuffer(32).append("target")
					.append(File.separatorChar).append("test-classes")
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
					.createTemporaryPersistenceXml(PersistenceConfig.PERSISTENCE_UNIT_NAME);
			File target = new File(new StringBuffer(32).append("target")
					.append(File.separatorChar).append("test-classes")
					.append(File.separatorChar).append("META-INF")
					.append(File.separatorChar).append(PersistenceConfig.PERSISTENCE_UNIT_NAME)
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
	}
}
