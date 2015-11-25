
import java.io.IOException;
import java.util.Properties;

import javax.persistence.Persistence;

public class JpaSchemaExport {

	public static void main(final String[] args) throws IOException {
		execute(args[0], args[1], args[2]);
		System.exit(0);
	}

	public static void execute(final String persistenceUnitName,
			final String destination, final String hibernateDialect) {
		System.out.println("Generating DDL create script to : " + destination);

		final Properties persistenceProperties = new Properties();
		// AvailableSettings x;
		// XXX force persistence properties : remove database target
		persistenceProperties.setProperty("hibernate.hbm2ddl.auto", "create");
		persistenceProperties.setProperty("hibernate.dialect",
				hibernateDialect);

		// persistenceProperties.setProperty(
		// AvailableSettings.SCHEMA_GEN_DATABASE_ACTION, "none");

		// XXX force persistence properties : define create script target from
		// metadata to destination
		// persistenceProperties.setProperty(AvailableSettings.SCHEMA_GEN_CREATE_SCHEMAS,
		// "true");
		persistenceProperties.setProperty("eclipselink.target-database",
				"Oracle");
		persistenceProperties.setProperty(
				"javax.persistence.schema-generation.database.action", "none");
		persistenceProperties.setProperty(
				"javax.persistence.schema-generation.scripts.action", "create");
		persistenceProperties.setProperty(
				"javax.persistence.schema-generation.create-source",
				"metadata");
		persistenceProperties.setProperty(
				"javax.persistence.schema-generation.scripts.create-target",
				"target/jpa/sql/create-schema.sql");

		Persistence.generateSchema(persistenceUnitName, persistenceProperties);
	}

}
