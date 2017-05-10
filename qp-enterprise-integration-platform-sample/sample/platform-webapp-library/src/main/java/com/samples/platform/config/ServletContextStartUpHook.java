package com.samples.platform.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author bhausen
 */
@SuppressWarnings("static-method")
public class ServletContextStartUpHook implements WebApplicationInitializer {
	/**
	 * @return the {@link AnnotationConfigWebApplicationContext} of
	 *         {@link MvcSpringConfig}.
	 */
	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(MvcSpringConfig.class.getName());
		return context;
	}

	/**
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(final ServletContext servletContext)
			throws ServletException {
		@SuppressWarnings("resource")
		WebApplicationContext webApplicationContext = this.getContext();
		/*
		 * Add a new ContextLoaderListener only in case of having no web.xml,
		 * where this has already taken place!
		 */
		// servletContext.addListener(new
		// ContextLoaderListener(webApplicationContext));
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
				"mvc", new DispatcherServlet(webApplicationContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/restservices/*");
	}
}
