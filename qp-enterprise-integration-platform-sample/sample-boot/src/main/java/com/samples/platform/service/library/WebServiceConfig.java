package com.samples.platform.service.library;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(
			final ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/services/*");
	}

	@Bean(name = "library") /* This is the name of the service.id! */
	public DefaultWsdl11Definition libraryWsdl11Definition() {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("LibraryServicePort");
		wsdl11Definition.setLocationUri("/services/library");
		wsdl11Definition.setTargetNamespace(
				"http://www.sample.com/Library/LibraryServiceMessages");
		wsdl11Definition.setSchema(this.librarySchema());
		return wsdl11Definition;
	}

	@Bean(name = "librarySchema")
	public XsdSchema librarySchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"ISS/Library/LibraryServiceMessages.xsd"));
	}
}
