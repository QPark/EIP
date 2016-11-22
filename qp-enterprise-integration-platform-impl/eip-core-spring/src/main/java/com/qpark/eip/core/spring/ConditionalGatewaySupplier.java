package com.qpark.eip.core.spring;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Supplies beans of type Gateway, if the properties could be find to match the
 * bean names.
 *
 * @author bhausen
 * @param <Gateway>
 *            The type of gateway conditionally to call.
 */
public abstract class ConditionalGatewaySupplier<Gateway>
		implements ApplicationContextAware {
	/**
	 * Get the bean id, which is beanName without beanPrefix and beanSuffix.
	 *
	 * @param beanName
	 *            the name of the bean.
	 * @param beanPrefix
	 *            the bean name prefix to eliminate.
	 * @param beanSuffix
	 *            the bean name suffix to eliminate.
	 * @return the bean id.
	 */
	private static Optional<String> getBeanId(final String beanName,
			final String beanPrefix, final String beanSuffix) {
		Optional<String> value = Optional.empty();
		if (Objects.nonNull(beanName)) {
			value = Optional.of(beanName
					.substring(beanPrefix.length(),
							beanName.indexOf(beanSuffix))
					.replace(".", "").toLowerCase());
		}
		return value;
	}

	/**
	 * Get the matching property name for the bean id - matching if the bean id
	 * equals the propertieName without propertyPrefix and propertySuffix.
	 *
	 * @param beanId
	 *            the bean id.
	 * @param properties
	 *            all property names.
	 * @param propertyPrefix
	 *            the property name prefix to eliminate.
	 * @param propertySuffix
	 *            the property name suffix to eliminate.
	 * @param propertyValueExclusion
	 *            the value of a property to exclude.
	 * @return the property name matching the bean id.
	 */
	private static Optional<String> getBeanIdPropertyName(final String beanId,
			final Map<String, String> properties, final String propertyPrefix,
			final String propertySuffix, final String propertyValueExclusion) {
		Optional<String> value = Optional.empty();
		if (Objects.nonNull(properties)) {
			for (final String propertyName : properties.keySet()) {
				if (propertyValueExclusion.trim().equals("") || !properties
						.get(propertyName).matches(propertyValueExclusion)) {
					final String propertyId = propertyName
							.substring(propertyPrefix.length(),
									propertyName.length()
											- propertySuffix.length())
							.replace(".", "").toLowerCase();
					if (propertyId.equals(beanId)) {
						value = Optional.of(propertyName);
						break;
					}
				}
			}
		}
		return value;
	}

	/** The {@link ApplicationContext}. */
	private ApplicationContext applicationContext;
	/** The bean prefix to get the bean id. */
	private String beanPrefix = "";
	/** The bean suffix to get the bean id. */
	private String beanSuffix = "";
	/** The {@link Class} of the generic gateway. */
	private Class<Gateway> gatewayClass;
	/** The list of gateways bound to the application context. */
	@Autowired
	protected List<Gateway> gateways;
	/** The {@link ApplicationPlaceholderConfigurer}. */
	@Autowired
	protected ApplicationPlaceholderConfigurer properties;
	/** The property prefix to match the bean id. */
	private String propertyPrefix = "";
	/** The property suffix to match the bean id. */
	private String propertySuffix = "";
	/**
	 * The regular expression a property value need to match, to be excluded
	 * from the list of gateways.
	 */
	private String propertyValueExclusion = "";

	/**
	 * @return the beanPrefix
	 */
	public String getBeanPrefix() {
		return this.beanPrefix;
	}

	/**
	 * @return the beanSuffix
	 */
	public String getBeanSuffix() {
		return this.beanSuffix;
	}

	/**
	 * Get the {@link Class} of the generic definition.
	 *
	 * @return the generic gateway {@link Class}.
	 */
	@SuppressWarnings("unchecked")
	public Class<Gateway> getGatewayClass() {
		if (Objects.isNull(this.gatewayClass)) {
			try {
				final String className = ((ParameterizedType) this.getClass()
						.getGenericSuperclass()).getActualTypeArguments()[0]
								.getTypeName();
				final Class<?> clazz = Class.forName(className);
				this.gatewayClass = (Class<Gateway>) clazz;
			} catch (final Exception e) {
				this.getLogger().error(e.getMessage(), e);
			}
		}
		return this.gatewayClass;
	}

	/**
	 * Get the list of gateways having name and matching configuration
	 * properties.
	 *
	 * @return the list of gateways.
	 */
	protected List<Gateway> getGateways() {
		this.getLogger().trace("+getGateways");
		final List<Gateway> value = new ArrayList<>();
		final Map<String, Gateway> gatewayBeans = this.applicationContext
				.getBeansOfType(this.getGatewayClass());
		gatewayBeans.entrySet().stream().filter(e -> Objects.nonNull(e.getKey())
				&& Objects.nonNull(e.getValue())).forEach(e -> {
					getBeanId(e.getKey(), this.beanPrefix, this.beanSuffix)
							.ifPresent(beanId -> getBeanIdPropertyName(beanId,
									this.properties.getProperties(),
									this.propertyPrefix, this.propertySuffix,
									this.propertyValueExclusion)
											.ifPresent(propertyName -> {
												this.getLogger().trace(
														" getGateways found property {} for bean {}",
														propertyName,
														e.getKey());
												value.add(e.getValue());
											}));
				});
		this.getLogger().trace("-getGateways #{}", value.size());
		return value;
	}

	/**
	 * @return the logger.
	 */
	protected abstract Logger getLogger();

	/**
	 * @return the propertyPrefix
	 */
	public String getPropertyPrefix() {
		return this.propertyPrefix;
	}

	/**
	 * @return the propertySuffix
	 */
	public String getPropertySuffix() {
		return this.propertySuffix;
	}

	/**
	 * @return the propertyValueExclusion
	 */
	public String getPropertyValueExclusion() {
		return this.propertyValueExclusion;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @param beanPrefix
	 *            the beanPrefix to set
	 */
	public void setBeanPrefix(final String beanPrefix) {
		if (Objects.isNull(beanPrefix)) {
			this.beanPrefix = "";
		} else {
			this.beanPrefix = beanPrefix;
		}
	}

	/**
	 * @param beanSuffix
	 *            the beanSuffix to set
	 */
	public void setBeanSuffix(final String beanSuffix) {
		if (Objects.isNull(beanSuffix)) {
			this.beanSuffix = "";
		} else {
			this.beanSuffix = beanSuffix;
		}
	}

	/**
	 * @param propertyPrefix
	 *            the propertyPrefix to set
	 */
	public void setPropertyPrefix(final String propertyPrefix) {
		if (Objects.isNull(propertyPrefix)) {
			this.propertyPrefix = "";
		} else {
			this.propertyPrefix = propertyPrefix;
		}
	}

	/**
	 * @param propertySuffix
	 *            the propertySuffix to set
	 */
	public void setPropertySuffix(final String propertySuffix) {
		if (Objects.isNull(propertySuffix)) {
			this.propertySuffix = "";
		} else {
			this.propertySuffix = propertySuffix;
		}
	}

	/**
	 * @param propertyValueExclusion
	 *            the propertyValueExclusion to set
	 */
	public void setPropertyValueExclusion(final String propertyValueExclusion) {
		if (Objects.isNull(propertyValueExclusion)) {
			this.propertyValueExclusion = "";
		} else {
			this.propertyValueExclusion = propertyValueExclusion;
		}
	}
}
