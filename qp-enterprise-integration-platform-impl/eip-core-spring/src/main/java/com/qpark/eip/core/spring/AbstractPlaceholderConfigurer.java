/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 ******************************************************************************/
package com.qpark.eip.core.spring;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.AbstractEnvironment;

import com.qpark.eip.core.ReInitalizeable;

/**
 * Provides properties loaded by the {@link PropertyPlaceholderConfigurer} routines.
 * @author bhausen
 */
public abstract class AbstractPlaceholderConfigurer extends PropertyPlaceholderConfigurer
		implements Map<String, String>, ReInitalizeable, ApplicationContextAware {
	/**
	 * @param properties The loaded properties
	 * @return the {@link TreeMap} containing the translations.
	 */
	public static TreeMap<String, String> setupTranslationMap(final Map<String, String> properties) {
		TreeMap<String, String> translationMap = new TreeMap<>();
		String number;
		String source;
		String translated;
		for (String s0 : properties.keySet()) {
			if (s0.trim().length() > 0 && !s0.trim().startsWith("#") && s0.indexOf('.') > 0) {
				number = s0.substring(0, s0.indexOf('.'));
				for (String s1 : properties.keySet()) {
					source = null;
					translated = null;
					if (s1.trim().length() > 0 && !s1.trim().startsWith("#") && s1.startsWith(number)
							&& !s1.equals(s0)) {
						if (s0.contains("source")) {
							source = s0;
						} else if (s1.contains("source")) {
							source = s1;
						}
						if (s0.contains("translated")) {
							translated = s0;
						} else if (s1.contains("translated")) {
							translated = s1;
						}
						if (source != null && translated != null) {
							translationMap.put(properties.get(source), properties.get(translated));
						}
					}
				}
			}
		}
		return translationMap;
	}

	/** The properties. */
	private final Properties properties = new Properties();
	/** The application context. */
	protected ApplicationContext applicationContext;

	/**
	 * Keep the value here too, since it is private at {@link PropertyPlaceholderConfigurer}.
	 */
	private int systemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

	/**
	 * Keep the value here too, since it is private at {@link PropertyPlaceholderConfigurer}.
	 */
	private boolean searchSystemEnvironment = !SpringProperties
			.getFlag(AbstractEnvironment.IGNORE_GETENV_PROPERTY_NAME);

	/**
	 * Not supported!
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		// No.
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key) {
		return this.getMap().containsKey(key);
	}

	/**
	 * Does not check System properties or Env!
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value) {
		return this.getMap().containsValue(value);
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return Collections.unmodifiableSet(new TreeMap<>(this.getMap()).entrySet());
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public String get(final Object key) {
		return this.getMap().get(key);
	}

	private Map<String, String> getMap() {
		Map<String, String> m = new HashMap<>();
		if (this.systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
			System.getProperties().entrySet().stream()
					.forEach(e -> m.put(String.valueOf(e.getKey()), String.valueOf(e.getValue())));
			if (this.searchSystemEnvironment) {
				m.putAll(System.getenv());
			}
		}
		this.properties.entrySet().stream()
				.forEach(e -> m.put(String.valueOf(e.getKey()), String.valueOf(e.getValue())));
		if (this.systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE) {
			System.getProperties().entrySet().stream()
					.forEach(e -> m.put(String.valueOf(e.getKey()), String.valueOf(e.getValue())));
			if (this.searchSystemEnvironment) {
				m.putAll(System.getenv());
			}
		}
		return m;
	}

	/**
	 * @return All loaded properties
	 */
	public Map<String, String> getProperties() {
		return Collections.unmodifiableMap(new TreeMap<>(this.getMap()));
	}

	private String getProperty(final Map<String, String> m, final String key, final String defaultValue) {
		String value = defaultValue;
		if (m.containsKey(key)) {
			value = m.get(key);
		}
		return value;
	}

	/**
	 * @param key
	 * @return Get the property
	 */
	public String getProperty(final String key) {
		return this.getMap().get(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return Get the property
	 */
	public double getProperty(final String key, final double defaultValue) {
		double value = defaultValue;
		Map<String, String> m = this.getMap();
		if (m.containsKey(key)) {
			try {
				value = Double.parseDouble(this.getProperty(m, key, String.valueOf(defaultValue)));
			} catch (final NumberFormatException e) {
				value = defaultValue;
			}
		}
		return value;
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return Get the property
	 */
	public int getProperty(final String key, final int defaultValue) {
		int value = defaultValue;
		Map<String, String> m = this.getMap();
		if (m.containsKey(key)) {
			try {
				value = Integer.parseInt(this.getProperty(m, key, String.valueOf(defaultValue)));
			} catch (final NumberFormatException e) {
				value = defaultValue;
			}
		}
		return value;
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return Get the property
	 */
	public long getProperty(final String key, final long defaultValue) {
		long value = defaultValue;
		Map<String, String> m = this.getMap();
		if (m.containsKey(key)) {
			try {
				value = Long.parseLong(this.getProperty(m, key, String.valueOf(defaultValue)));
			} catch (final NumberFormatException e) {
				value = defaultValue;
			}
		}
		return value;
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return Get the property
	 */
	public String getProperty(final String key, final String defaultValue) {
		return this.getProperty(this.getMap(), key, defaultValue);
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.getMap().isEmpty();
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return new TreeMap<>(this.getMap()).keySet();
	}

	/**
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#processProperties(org.springframework.beans.factory.config.ConfigurableListableBeanFactory,
	 *      java.util.Properties)
	 */
	@Override
	protected void processProperties(final ConfigurableListableBeanFactory beanFactoryToProcess, final Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		for (final String k : props.stringPropertyNames()) {
			this.properties.put(k, props.getProperty(k));
		}
	}

	/**
	 * We do not update properties!
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public String put(final String key, final String value) {
		if (!this.properties.containsKey(key)) {
			return String.valueOf(this.properties.put(key, value));
		}
		return this.getMap().get(key);
	}

	/**
	 * We do not update properties!
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends String, ? extends String> m) {
		for (final Map.Entry<? extends String, ? extends String> e : m.entrySet()) {
			this.put(e.getKey(), e.getValue());
		}
	}

	/**
	 * @see com.qpark.eip.core.ReInitalizeable#reInitalize()
	 */
	@Override
	public void reInitalize() {
		if (ConfigurableApplicationContext.class.isInstance(this.applicationContext)) {
			this.properties.clear();
			this.postProcessBeanFactory(((ConfigurableApplicationContext) this.applicationContext).getBeanFactory());
		}
	}

	/**
	 * We do not remove properties!
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public String remove(final Object key) {
		// We do not remove properties
		return null;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Set whether to search for a matching system environment variable if no matching system property has been found.
	 * Only applied when "systemPropertyMode" is active (i.e. "fallback" or "override"), right after checking JVM system
	 * properties.
	 * <p>
	 * Default is "true". Switch this setting off to never resolve placeholders against system environment variables.
	 * Note that it is generally recommended to pass external values in as JVM system properties: This can easily be
	 * achieved in a startup script, even for existing environment variables.
	 * <p>
	 * <b>NOTE:</b> Access to environment variables does not work on the Sun VM 1.4, where the corresponding
	 * {@link System#getenv} support was disabled - before it eventually got re-enabled for the Sun VM 1.5. Please
	 * upgrade to 1.5 (or higher) if you intend to rely on the environment variable support.
	 * @see #setSystemPropertiesMode
	 * @see java.lang.System#getProperty(String)
	 * @see java.lang.System#getenv(String)
	 */
	@Override
	public void setSearchSystemEnvironment(final boolean searchSystemEnvironment) {
		super.setSearchSystemEnvironment(searchSystemEnvironment);
		this.searchSystemEnvironment = searchSystemEnvironment;
	}

	/**
	 * Set how to check system properties: as fallback, as override, or never. For example, will resolve ${user.dir} to
	 * the "user.dir" system property.
	 * <p>
	 * The default is "fallback": If not being able to resolve a placeholder with the specified properties, a system
	 * property will be tried. "override" will check for a system property first, before trying the specified
	 * properties. "never" will not check system properties at all.
	 * @see #SYSTEM_PROPERTIES_MODE_NEVER
	 * @see #SYSTEM_PROPERTIES_MODE_FALLBACK
	 * @see #SYSTEM_PROPERTIES_MODE_OVERRIDE
	 * @see #setSystemPropertiesModeName
	 */
	@Override
	public void setSystemPropertiesMode(final int systemPropertiesMode) {
		super.setSystemPropertiesMode(systemPropertiesMode);
		this.systemPropertiesMode = systemPropertiesMode;
	}

	/**
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return this.getMap().size();
	}

	/**
	 * Get the property names as {@link Set}.
	 * @return the property names.
	 */
	public Set<String> stringPropertyNames() {
		return new TreeMap<>(this.getMap()).keySet();
	}

	/** @return filled {@link Properties}. */
	public Properties toProperties() {
		final Properties p = new Properties();
		p.putAll(new TreeMap<>(this.getMap()));
		return p;
	}

	/**
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<String> values() {
		return Collections.unmodifiableCollection(new TreeMap<>(this.getMap()).values());
	}
}