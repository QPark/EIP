package com.samples.platform.client.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import com.samples.platform.client.IssTechSupportServiceClient;
import com.samples.platform.client.IssTechSupportServiceClientExtension;
import com.samples.platform.client.LibraryServiceClient;
import com.samples.platform.client.LibraryServiceClientExtension;

/**
 * Spring configuration to enable the server calls of the client.
 *
 * @author bhausen
 */
@Configuration
@PropertySources({ @PropertySource("classpath:bus.client.properties"),
		@PropertySource(
				value = "file:${catalina.base}/conf/bus.client.properties",
				ignoreResourceNotFound = true), })
public class ClientConfig extends AbstractClientConfig {
	/**
	 * This needs to stay static!
	 *
	 * @return the {@link PropertySourcesPlaceholderConfigurer}.
	 */
	@Bean(name = "bus.client.properties")
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/** The system user name the client should use. */
	@Value("${com.samples.platform.client.systemUser.name}")
	private String clientSystemUserName;
	/** The system user name password. */
	@Value("${com.samples.platform.client.systemUser.password}")
	private String clientSystemUserPassword;
	/** The service bus server to access. */
	@Value("${com.samples.platform.client.endpoint.servicebus.server:http://localhost:8080}")
	private String servicebusServer;
	/** The web application name deployed. */
	@Value("${com.samples.platform.client.endpoint.servicebus.webapp.name:platform-library-2.0.0}")
	private String servicebusWebappName;

	/**
	 * @see com.samples.platform.client.config.AbstractClientConfig#getClientEndPointUrl(java.lang.String)
	 */
	@Override
	public String getClientEndPointUrl(final String serviceId) {
		StringBuffer sb = new StringBuffer(64);
		sb.append(this.servicebusServer.trim());
		if (!this.servicebusServer.trim().endsWith("/")) {
			sb.append("/");
		}
		// http://localhost:8080/platform-library-2.0.0/services/library.wsdl
		sb.append(this.servicebusWebappName.trim());
		sb.append("/services/");
		sb.append(serviceId.trim());
		return sb.toString();
	}

	/**
	 * @see com.samples.platform.client.config.AbstractClientConfig#getClientSystemUserName()
	 */
	@Override
	public String getClientSystemUserName() {
		return this.clientSystemUserName;
	}

	/**
	 * @see com.samples.platform.client.config.AbstractClientConfig#getClientSystemUserPassword()
	 */
	@Override
	public String getClientSystemUserPassword() {
		return this.clientSystemUserPassword;
	}

	/**
	 * Setup the {@link TimeZone} to UTC and the
	 * <code>CommonsClientHttpRequestFactory.defaultTimeout</code> to 30
	 * seconds.
	 */
	@PostConstruct
	private void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("CommonsClientHttpRequestFactory.defaultTimeout",
				"30000");
	}

	@Bean
	public IssTechSupportServiceClientExtension issTechSupportClient() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller
				.setContextPath(IssTechSupportServiceClient.CONTEXT_PATH_NAME);
		IssTechSupportServiceClientExtension bean = new IssTechSupportServiceClientExtension();
		bean.setInterceptors(
				new ClientInterceptor[] { this.securityInterceptor() });
		bean.setDefaultUri(this
				.getClientEndPointUrl(IssTechSupportServiceClient.SERVICE_ID));
		bean.setMarshaller(marshaller);
		bean.setUnmarshaller(marshaller);
		bean.setMessageFactory(this.messageFactory());
		return bean;
	}

	/**
	 * Get the {@link LibraryServiceClient}.
	 *
	 * @param marshaller
	 *            the {@link Jaxb2Marshaller}.
	 * @param messageFactory
	 *            the SOAP 1.2 message factory.
	 * @param securityInterceptor
	 *            the security interceptor.
	 * @return the {@link LibraryServiceClient}.
	 */
	@Bean
	public LibraryServiceClientExtension libraryClient() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(LibraryServiceClient.CONTEXT_PATH_NAME);

		LibraryServiceClientExtension bean = new LibraryServiceClientExtension();
		bean.setInterceptors(
				new ClientInterceptor[] { this.securityInterceptor() });
		bean.setDefaultUri(
				this.getClientEndPointUrl(LibraryServiceClient.SERVICE_ID));
		bean.setMarshaller(marshaller);
		bean.setUnmarshaller(marshaller);
		bean.setMessageFactory(this.messageFactory());
		return bean;
	}
}
