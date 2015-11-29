/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.logback;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.eip.core.ReInitalizeable;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Searches a list of locations for logback configuration and first readable and
 * valid file is used to reinitialize logging. The search order is built up as
 * follows. If no readable, vallid file is found the default logback
 * configuration will remain in place (typically the logback config found on the
 * class path).
 * <ul>
 * <li>"${servicebus.logback.config}" (IF servicebus.logback.config system
 * property set)</li>
 * <li>"${catalina.base}/conf/logback-&lt;loggerContextName&gt;-${abc.env}.xml"
 * (IF loggerContextName was passed and servicebus.env system property set)</li>
 * <li>"${catalina.base}/conf/logback-${abc.env}.xml" (IF abc.env system
 * property set)</li>
 * <li>"${catalina.base}/conf/logback.xml</li>
 * </ul>
 *
 * @author bhausen
 */
public class LoggingInitializer implements ReInitalizeable {
	private static final String CATALINA_BASE = "catalina.base";
	private static final LoggingInitializer defaulLoggingInitializer = new LoggingInitializer();

	public static LoggingInitializer getInstance() {
		return defaulLoggingInitializer;
	}

	private final Logger logger = LoggerFactory
			.getLogger(LoggingInitializer.class);
	private String baseName;
	private String serviceName;
	private String versionName;

	public void initialize(final String baseName, final String serviceName,
			final String versionName) {
		this.baseName = baseName;
		this.serviceName = serviceName;
		this.versionName = versionName;
		String catalinaBase = System.getProperty(CATALINA_BASE);
		StringBuffer sb = new StringBuffer(256);
		String catalinaConf = sb.append(catalinaBase).append(File.separatorChar)
				.append("conf").append(File.separatorChar).toString();
		sb.setLength(0);

		List<String> configs = new ArrayList<String>(4);

		sb.setLength(0);
		if (baseName != null && baseName.length() > 0 && serviceName != null
				&& serviceName.length() > 0 && versionName != null
				&& versionName.length() > 0) {
			sb.append(catalinaConf).append("logback-").append(baseName)
					.append("-").append(serviceName).append("-")
					.append(versionName).append(".xml");
			configs.add(sb.toString());
		}
		sb.setLength(0);
		if (baseName != null && baseName.length() > 0 && serviceName != null
				&& serviceName.length() > 0) {
			sb.append(catalinaConf).append("logback-").append(baseName)
					.append("-").append(serviceName).append(".xml");
			configs.add(sb.toString());
		}

		sb.setLength(0);
		if (baseName != null && baseName.length() > 0) {
			sb.append(catalinaConf).append("logback-").append(baseName)
					.append(".xml");
			configs.add(sb.toString());
		}

		boolean fromFile = false;
		for (String logbackConfigPath : configs) {
			File f = new File(logbackConfigPath);
			if (f.exists() && f.canRead()) {
				if (this.initializeLogging(f)) {
					this.logger.info("Setup logger with {}",
							f.getAbsolutePath());
					fromFile = true;
					break;
				}
			}
		}
		if (!fromFile) {
			String logbackConfig = "/base-logback.xml";
			InputStream is = LoggingInitializer.class
					.getResourceAsStream(logbackConfig);
			if (is == null) {
				logbackConfig = "/eip-logback.xml";
				is = LoggingInitializer.class
						.getResourceAsStream(logbackConfig);
			}
			this.initializeLogging(is);
			this.logger.info("Setup logger with classpath:{}", logbackConfig);
		}
	}

	/**
	 * Attempts to initialize logging from the specified file if it is readable.
	 *
	 * @param logbackConfigFile
	 * @return true is successfully initializes logging from file, otherwise
	 *         false
	 */
	private boolean initializeLogging(final File config) {
		boolean loggingInitialized = false;
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure(config);
			loggingInitialized = true;
		} catch (JoranException je) {
			this.logger.error(je.getMessage());
		}
		StatusPrinter.print(lc);
		return loggingInitialized;
	}

	/**
	 * Attempts to initialize logging from the specified file if it is readable.
	 *
	 * @param logbackConfigFile
	 * @return true is successfully initializes logging from file, otherwise
	 *         false
	 */
	private boolean initializeLogging(final InputStream config) {
		boolean loggingInitialized = false;
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure(config);
			loggingInitialized = true;
		} catch (JoranException je) {
			this.logger.error(je.getMessage());
		}
		StatusPrinter.print(lc);
		return loggingInitialized;
	}

	/**
	 * @see com.qpark.eip.core.ReInitalizeable#reInitalize()
	 */
	@Override
	public void reInitalize() {
		this.initialize(this.baseName, this.serviceName, this.versionName);
	}
}
