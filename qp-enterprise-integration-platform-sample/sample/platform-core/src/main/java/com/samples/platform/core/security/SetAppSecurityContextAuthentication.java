package com.samples.platform.core.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.security.EipUserDetailsService;
import com.qpark.eip.core.spring.security.EipUserProvider;
import com.samples.domain.serviceprovider.AppSecurityContextHandler;

/**
 * Setup the security context for authentication.
 *
 * @author bhausen
 */
@Component
public class SetAppSecurityContextAuthentication
		implements AppSecurityContextHandler {
	/**
	 * The {@link ApplicationPlaceholderConfigurer} containing the properties.
	 */
	@Autowired
	private ApplicationPlaceholderConfigurer properties;
	/**
	 * Property name of the webapp system user. Default is
	 * <i>service.endpoint.consumer.self.hosted.services.userName</i>.
	 */
	private String propertyNameUserWebapp = "service.endpoint.consumer.self.hosted.services.userName";
	/**
	 * The authentication manager defined in the
	 * security-authentication-spring-config.xml.
	 */
	@Autowired
	@Qualifier("eipAuthenticationManager")
	private ProviderManager providerManager;

	/** The {@link EipUserProvider} provided in the spring context. */
	@Autowired
	private EipUserProvider userProvider;

	/**
	 * {@inheritDoc}
	 *
	 * @see com.samples.domain.serviceprovider.AppSecurityContextHandler#clearBusSecurityContextAuthentication()
	 */
	@Override
	public void clearBusSecurityContextAuthentication() {
		EipUserDetailsService.clearSecurityContextHolderAuthentication();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.samples.domain.serviceprovider.AppSecurityContextHandler#init()
	 */
	@Override
	@PostConstruct
	public void init() {
		this.providerManager.setEraseCredentialsAfterAuthentication(false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.samples.domain.serviceprovider.AppSecurityContextHandler#setAppSecurityContextAuthentication()
	 */
	@Override
	public boolean setAppSecurityContextAuthentication() {
		return EipUserDetailsService.setSecurityContextHolderAuthentication(
				this.userProvider, this.properties
						.getProperty(this.propertyNameUserWebapp, "bus"));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.samples.domain.serviceprovider.AppSecurityContextHandler#setPropertyNameUserWebapp(java.lang.String)
	 */
	@Override
	public void setPropertyNameUserWebapp(final String propertyNameUserWebapp) {
		this.propertyNameUserWebapp = propertyNameUserWebapp;
	}
}
