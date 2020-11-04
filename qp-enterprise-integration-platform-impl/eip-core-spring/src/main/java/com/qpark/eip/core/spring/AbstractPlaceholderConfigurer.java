/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring;

import java.util.Collection;
import java.util.Collections;
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
import com.qpark.eip.core.ReInitalizeable;

/**
 * Provides properties loaded by the {@link PropertyPlaceholderConfigurer}
 * routines.
 *
 * @author bhausen
 */
public abstract class AbstractPlaceholderConfigurer extends PropertyPlaceholderConfigurer
    implements Map<String, String>, ReInitalizeable, ApplicationContextAware {
  /**
   * @param properties
   *            The loaded properties
   * @return the {@link TreeMap} containing the translations.
   */
  public static TreeMap<String, String> setupTranslationMap(final Map<String, String> properties) {
    final TreeMap<String, String> translationMap = new TreeMap<String, String>();
    String number;
    String source;
    String translated;
    for (final String s0 : properties.keySet()) {
      if (s0.trim().length() > 0 && !s0.trim().startsWith("#") && s0.indexOf('.') > 0) {
        number = s0.substring(0, s0.indexOf('.'));
        for (final String s1 : properties.keySet()) {
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

  /** The map containing all the properties. */
  private final TreeMap<String, String> properties = new TreeMap<String, String>();
  /** The application context. */
  protected ApplicationContext applicationContext;

  /**
   * Not supported!
   *
   * @see java.util.Map#clear()
   */
  @Override
  public void clear() {}

  /**
   * @see java.util.Map#containsKey(java.lang.Object)
   */
  @Override
  public boolean containsKey(final Object key) {
    return this.properties.containsKey(key);
  }

  /**
   * @see java.util.Map#containsValue(java.lang.Object)
   */
  @Override
  public boolean containsValue(final Object value) {
    return this.properties.containsValue(value);
  }

  /**@return filled {@link Properties}.   */
  public Properties toProperties() {
    final Properties p = new Properties();
    this.properties.entrySet().stream().forEach(e -> p.put(e.getKey(), e.getValue()));
    return p;
  }

  /**
   * @see java.util.Map#entrySet()
   */
  @Override
  public Set<java.util.Map.Entry<String, String>> entrySet() {
    return Collections.unmodifiableSet(this.properties.entrySet());
  }

  /**
   * @see java.util.Map#get(java.lang.Object)
   */
  @Override
  public String get(final Object key) {
    return this.properties.get(key);
  }

  /**
   * @return All loaded properties
   */
  public Map<String, String> getProperties() {
    return Collections.unmodifiableMap(this.properties);
  }

  /**
   * @return Get the property
   */
  public String getProperty(final String key) {
    return this.properties.get(key);
  }

  /**
   * @return Get the property
   */
  public String getProperty(final String key, final String defaultValue) {
    String value = defaultValue;
    if (this.properties.containsKey(key)) {
      value = this.properties.get(key);
    }
    return value;
  }

  /**
   * @return Get the property
   */
  public int getProperty(final String key, final int defaultValue) {
    int value = defaultValue;
    if (this.properties.containsKey(key)) {
      try {
        value = Integer.parseInt(this.getProperty(key, String.valueOf(defaultValue)));
      } catch (final NumberFormatException e) {
        value = defaultValue;
      }
    }
    return value;
  }

  /**
   * @return Get the property
   */
  public long getProperty(final String key, final long defaultValue) {
    long value = defaultValue;
    if (this.properties.containsKey(key)) {
      try {
        value = Long.parseLong(this.getProperty(key, String.valueOf(defaultValue)));
      } catch (final NumberFormatException e) {
        value = defaultValue;
      }
    }
    return value;
  }

  /**
   * Get the property names as {@link Set}.
   *
   * @return the property names.
   */
  public Set<String> stringPropertyNames() {
    return this.properties.keySet();
  }

  /**
   * @return Get the property
   */
  public double getProperty(final String key, final double defaultValue) {
    double value = defaultValue;
    if (this.properties.containsKey(key)) {
      try {
        value = Double.parseDouble(this.getProperty(key, String.valueOf(defaultValue)));
      } catch (final NumberFormatException e) {
        value = defaultValue;
      }
    }
    return value;
  }

  /**
   * @see java.util.Map#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return this.properties.isEmpty();
  }

  /**
   * @see java.util.Map#keySet()
   */
  @Override
  public Set<String> keySet() {
    return this.properties.keySet();
  }

  /**
   * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#processProperties(org.springframework.beans.factory.config.ConfigurableListableBeanFactory,
   *      java.util.Properties)
   */
  @Override
  protected void processProperties(final ConfigurableListableBeanFactory beanFactoryToProcess,
      final Properties props) throws BeansException {
    super.processProperties(beanFactoryToProcess, props);
    for (final String k : props.stringPropertyNames()) {
      this.properties.put(k, props.getProperty(k));
    }
  }

  /**
   * We do not update properties!
   *
   * @see java.util.Map#put(java.lang.Object, java.lang.Object)
   */
  @Override
  public String put(final String key, final String value) {
    if (!this.properties.containsKey(key)) {
      return this.properties.put(key, value);
    } else {
      return this.properties.get(key);
    }
  }

  /**
   * We do not update properties!
   *
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
      this.postProcessBeanFactory(
          ((ConfigurableApplicationContext) this.applicationContext).getBeanFactory());
    }
  }

  /**
   * We do not remove properties!
   *
   * @see java.util.Map#remove(java.lang.Object)
   */
  @Override
  public String remove(final Object key) {
    // We do not remove properties
    return this.properties.get(key);
  }

  /**
   * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
   */
  @Override
  public void setApplicationContext(final ApplicationContext applicationContext)
      throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * @see java.util.Map#size()
   */
  @Override
  public int size() {
    return this.properties.size();
  }

  /**
   * @see java.util.Map#values()
   */
  @Override
  public Collection<String> values() {
    return Collections.unmodifiableCollection(this.properties.values());
  }
}
