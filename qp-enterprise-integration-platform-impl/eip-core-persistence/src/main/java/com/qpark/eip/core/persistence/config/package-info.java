/**
 * Contains the spring configuration class of the persistence. To be able to use
 * it the {@link com.qpark.eip.core.persistence.config.EipPersistenceConfig}
 * need to get two values set:
 * <dl>
 * <dt>jpaVendorAdapterClassName</dt>
 * <dd>The class name of the
 * {@link org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter}
 * implementation. Currently supported are
 * {@link org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter},
 * {@link org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter} and
 * {@link org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter}.
 * </p>
 * To set {@link org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter}
 * the value needs to be
 * <ul>
 * <li>org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter or</li>
 * <li>Hibernate (ignore case)</li>
 * </ul>
 * </p>
 * To set {@link org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter}
 * the value needs to be
 * <ul>
 * <li>org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter or</li>
 * <li>EclipseLink (ignore case)</li>
 * </ul>
 * </p>
 * To set {@link org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter} the
 * value needs to be
 * <ul>
 * <li>org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter or</li>
 * <li>OpenJpa (ignore case)</li>
 * </ul>
 * </dd>
 * <dt>jpaVendorAdpaterDatabasePlatform</dt>
 * <dd>This is the DatabasePlatform parameter to set into the
 * {@link org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter}. If
 * hibernate is the choice, the full qualified class names of
 * {@link org.hibernate.dialect.Dialect} would be the value.</dd>
 * </dl>
 *
 * @author bhausen
 */
package com.qpark.eip.core.persistence.config;