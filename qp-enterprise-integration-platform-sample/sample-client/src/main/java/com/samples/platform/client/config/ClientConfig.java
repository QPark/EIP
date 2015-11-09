package com.samples.platform.client.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

import com.samples.platform.client.LibraryServiceClient;
import com.samples.platform.client.config.util.ClientCallWss4jSecurityInterceptor;
import com.samples.platform.client.impl.LibraryServiceClientImpl;

/**
 * Spring configuration to enable the server calls of the client.
 *
 * @author bhausen
 */
@Configuration
@PropertySource(value = "classpath:bus.client.properties")
public class ClientConfig {
	/** The name of the service library. */
	private static final String SERVICE_LIBRARY = "library";

	/**
	 * Generate the endpoint URL of the service.
	 *
	 * @param server
	 *            the servicebus server to access.
	 * @param version
	 *            the version of the service bus.
	 * @param service
	 *            the service name.
	 * @return the endpoint URL.
	 */
	private static String getServicebusEndpointUrl(final String server,
			final String version, final String service) {
		StringBuffer sb = new StringBuffer(64);
		sb.append(server.trim());
		if (!server.trim().endsWith("/")) {
			sb.append("/");
		}
		// http://10.8.128.33:8080/platform-library-2.0.0/services/library.wsdl
		sb.append("platform-");
		sb.append(service);
		if (version != null && version.trim().length() > 0) {
			sb.append("-");
			sb.append(version.trim());
		}
		sb.append("/services/");
		sb.append(service);
		return sb.toString();
	}

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
	@Value("${com.samples.platform.client.endpoint.servicebus.server}")
	private String servicebusServer;
	/** The endpoint URL of the directory service. */
	@Value("${com.samples.platform.client.endpoint.servicebus.version:}")
	private String servicebusVersion;

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

	/**
	 * Get the {@link Jaxb2Marshaller} to marshall and unmarshall the calls.
	 *
	 * @return the {@link Jaxb2Marshaller}.
	 */
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller bean = new Jaxb2Marshaller();
		StringBuffer path = new StringBuffer(1024);
		path.append(" com.qpark.eip.service.common.msg");
		path.append(":");
		path.append("com.samples.platform.service.library.msg");
		bean.setContextPath(path.toString());
		return bean;
	}

	/**
	 * Get the message factory supporting SOAP version 1.2.
	 *
	 * @return the {@link SaajSoapMessageFactory}.
	 */
	@Bean
	public SaajSoapMessageFactory messageFactory() {
		SaajSoapMessageFactory bean = new SaajSoapMessageFactory();
		bean.setSoapVersion(SoapVersion.SOAP_12);
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
	public LibraryServiceClient libraryClient(final Jaxb2Marshaller marshaller,
			final SaajSoapMessageFactory messageFactory,
			final ClientCallWss4jSecurityInterceptor securityInterceptor) {
		LibraryServiceClientImpl bean = new LibraryServiceClientImpl();
		bean.setInterceptors(new ClientInterceptor[] { securityInterceptor });
		bean.setDefaultUri(getServicebusEndpointUrl(this.servicebusServer,
				this.servicebusVersion, SERVICE_LIBRARY));
		bean.setMarshaller(marshaller);
		bean.setUnmarshaller(marshaller);
		bean.setMessageFactory(messageFactory);
		return bean;
	}

	/**
	 * The clients {@link Wss4jSecurityInterceptor} providing the web service
	 * security.
	 *
	 * @return the {@link Wss4jSecurityInterceptor} implementation.
	 */
	@Bean
	public ClientCallWss4jSecurityInterceptor securityInterceptor() {
		ClientCallWss4jSecurityInterceptor bean = new ClientCallWss4jSecurityInterceptor();
		bean.setSecurementUsername(this.clientSystemUserName);
		bean.setSecurementPassword(this.clientSystemUserPassword);
		return bean;
	}

}