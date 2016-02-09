package com.qpark.eip.core.persistence.test;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

import com.qpark.eip.core.persistence.DDLGenerator;
import com.samples.platform.persistenceconfig.PersistenceConfig;

/**
 * @author bhausen
 */
public class DDLGeneratorTest {
	@Test
	public void prepareDDLGeneration() {
		DDLGenerator generator = new DDLGenerator(
				DDLGenerator.getWhiteListedClassNames());
		try {
			String persistenceXml = generator.createTemporaryPersistenceXml(
					PersistenceConfig.PERSISTENCE_UNIT_NAME);

			System.out.println(persistenceXml);

			File target = new File(new StringBuffer(32).append("target")
					.append(File.separatorChar).append("test-classes")
					.append(File.separatorChar).append("META-INF")
					.append(File.separatorChar).append("persistence.xml")
					.toString());
			if (target.exists()) {
				try (FileOutputStream fos = new FileOutputStream(target)) {
					fos.write(persistenceXml.getBytes());
					fos.flush();
					fos.close();
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
