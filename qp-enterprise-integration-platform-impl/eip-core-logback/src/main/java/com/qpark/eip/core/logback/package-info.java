/**
 * Contains the {@link com.qpark.eip.core.logback.LoggingInitializer} using the
 * {@link ch.qos.logback.classic.joran.JoranConfigurator} to initialize the
 * logging.
 * </p>
 * In <code>$CATALINA_BASE</code> the files starting with <i>logback</i>
 * appended with service name and service version will be used to configure
 * logback. If that is succesfully done the cascade of files is stopped and the
 * configuration is used. If no file could be found the eip-logback.xml
 * contained in this maven artefact is used to configure logback.
 *
 * @author bhausen
 */
package com.qpark.eip.core.logback;