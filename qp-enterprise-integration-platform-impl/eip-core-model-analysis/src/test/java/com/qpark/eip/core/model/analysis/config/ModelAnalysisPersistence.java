package com.qpark.eip.core.model.analysis.config;

import java.util.TimeZone;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.qpark.eip.core.model.analysis.ModelAnalysisDao;
import com.qpark.eip.core.model.analysis.PersistModelAnalysis;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(value = { PersistenceConfig.class })
@ConfigurationProperties
public class ModelAnalysisPersistence implements InitializingBean {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory
			.getLogger(ModelAnalysisPersistence.class);
	/** The JDBC driver of the database {@link DataSource}. */
	@Value("${database.jdbc.driver}")
	private String jdbcDriver;
	/** The JDBC URL of the database {@link DataSource}. */
	@Value("${database.jdbc.url}")
	private String jdbcUrl;
	/** The JDBC user name of the database {@link DataSource}. */
	@Value("${database.jdbc.userName}")
	private String jdbcUserName;
	/** The JDBC password of the database {@link DataSource}. */
	@Value("${database.jdbc.password}")
	private String jdbcPassword;

	/**
	 * The <b>static</b> {@link PropertySourcesPlaceholderConfigurer} to get the
	 * properties.
	 *
	 * @return the {@link PropertySourcesPlaceholderConfigurer}.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * Run the spring boot application.
	 *
	 * @param args
	 *            the arguments.
	 */
	public static void main(final String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("file.encoding", "UTF-8");
		SpringApplication.run(ModelAnalysisPersistence.class, args);
	}

	/**
	 * Get the {@link PersistModelAnalysis}.
	 *
	 * @return the {@link PersistModelAnalysis}.
	 */
	@Bean
	public PersistModelAnalysis getPersistModelAnalysis() {
		PersistModelAnalysis bean = new PersistModelAnalysis();
		return bean;
	}

	/**
	 * Get the {@link ModelAnalysisDao}.
	 *
	 * @return the {@link ModelAnalysisDao}.
	 */
	@Bean
	public ModelAnalysisDao getModelAnalysisDao() {
		ModelAnalysisDao bean = new ModelAnalysisDao();
		return bean;
	}

	/**
	 * Get the {@link DataSource} auto wired in the {@link PersistenceConfig}.
	 *
	 * @return The {@link DataSource}
	 */
	@Bean(name = PersistenceConfig.DATASOURCE_BEAN_NAME)
	public DataSource getDataSource() {
		DriverManagerDataSource bean = new DriverManagerDataSource();
		bean.setDriverClassName(this.jdbcDriver);
		bean.setUrl(this.jdbcUrl);
		bean.setUsername(this.jdbcUserName);
		bean.setPassword(this.jdbcPassword);
		return bean;
	}

	@Autowired
	private PersistModelAnalysis persistModelAnalysis;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.logger.info("+afterPropertiesSet ");
		this.persistModelAnalysis.persist();
		this.logger.info("-afterPropertiesSet");
	}
}