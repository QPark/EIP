package com.samples.platform.service;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.model.common.PropertyType;
import com.qpark.eip.service.common.msg.GetServiceStatusRequestType;
import com.qpark.eip.service.common.msg.GetServiceStatusResponseType;
import com.qpark.eip.service.common.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get service status on service <code>common</code>.
 *
 * @author bhausen
 */
@Component("operationProviderCommonGetServiceStatus")
public class GetServiceStatusOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetServiceStatusOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/** The {@link ApplicationPlaceholderConfigurer} loaded properties. */
	@Autowired
	private ApplicationPlaceholderConfigurer properties;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetServiceStatusRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetServiceStatusResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetServiceStatusResponseType> getServiceStatus(
			final JAXBElement<GetServiceStatusRequestType> message) {
		this.logger.debug("+getServiceStatus");
		GetServiceStatusResponseType response = this.of
				.createGetServiceStatusResponseType();
		try {
			PropertyType p;
			ClassLoader cl;
			URL[] urls;
			ClassLoader sysCl = ClassLoader.getSystemClassLoader();

			response.setStatus("Service is available");

			/* System properties */
			p = new PropertyType();
			p.setName("System Properties");
			response.getDetails().add(p);
			TreeSet<String> propertyNames = new TreeSet<String>();
			propertyNames.addAll(System.getProperties().stringPropertyNames());
			for (String propertyName : propertyNames) {
				p.getValue().add(new StringBuffer(64).append(propertyName)
						.append("=").append(System.getProperty(propertyName))
						.toString());
			}

			/* Application properties. */
			p = new PropertyType();
			p.setName("Application loaded properties");
			response.getDetails().add(p);
			propertyNames.clear();
			propertyNames.addAll(this.properties.stringPropertyNames());
			for (String propertyName : propertyNames) {
				p.getValue().add(new StringBuffer(64).append(propertyName)
						.append("=")
						.append(this.properties.getProperty(propertyName))
						.toString());
			}

			/* Current lass loader */
			cl = this.getClass().getClassLoader();
			p = new PropertyType();
			p.setName("This ClassLoader");
			response.getDetails().add(p);
			p.getValue().add(cl.getClass().getName());
			if (URLClassLoader.class.isInstance(cl)) {
				urls = ((URLClassLoader) cl).getURLs();
				p.getValue().add(new StringBuffer("Url: ").append(urls.length)
						.toString());
				for (URL url : urls) {
					p.getValue().add(url.toString());
				}
			}
			cl = cl.getParent();
			while (cl != sysCl) {
				p = new PropertyType();
				p.setName("Parent Classloader");
				response.getDetails().add(p);
				p.getValue().add(cl.getClass().getName());
				if (URLClassLoader.class.isInstance(cl)) {
					urls = ((URLClassLoader) cl).getURLs();
					p.getValue().add(new StringBuffer("Url: ")
							.append(urls.length).toString());
					for (URL url : urls) {
						p.getValue().add(url.toString());
					}
				}
				cl = cl.getParent();
			}

			/* System class loader */
			cl = sysCl;
			p = new PropertyType();
			p.setName("SystemClassLoader");
			response.getDetails().add(p);
			p.getValue().add(cl.getClass().getName());
			if (URLClassLoader.class.isInstance(cl)) {
				urls = ((URLClassLoader) cl).getURLs();
				p.getValue().add(new StringBuffer("Url: ").append(urls.length)
						.toString());
				for (URL url : urls) {
					p.getValue().add(url.toString());
				}
			}
		} catch (Throwable e) {
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug("-getServiceStatus #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetServiceStatusResponse(response);
	}
}
