/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.qpark.eip.core.ReInitalizeable;
import com.qpark.eip.core.domain.persistencedefinition.AuthenticationType;
import com.qpark.eip.core.domain.persistencedefinition.GrantedAuthorityType;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.ContextNameProvider;
import com.qpark.eip.core.spring.auth.dao.AuthorityDao;
import com.qpark.eip.core.spring.security.EipJasyptEncryptionProvider;
import com.qpark.eip.core.spring.security.EipUserProvider;

/**
 * Provides {@link User} objects read out of the database.
 *
 * @author bhausen
 */
public class DatabaseUserProvider implements EipUserProvider, ReInitalizeable {
  /** The {@link AuthorityDao}. */
  @Autowired
  private AuthorityDao authorityDao;
  /** The eip {@link ContextNameProvider}. */
  @Autowired
  @Qualifier("ComQparkEipCoreSpringAuthContextNameProvider")
  private ContextNameProvider contextNameProvider;
  /** The {@link org.slf4j.Logger}. */
  private final Logger logger = LoggerFactory.getLogger(DatabaseUserProvider.class);
  /** The map containing the User objects of the application. */
  private final Map<String, User> userMap = new HashMap<>();
  /** The {@link ApplicationPlaceholderConfigurer}. */
  @Autowired
  private ApplicationPlaceholderConfigurer properties;

  /**
   * Get a clone of the {@link User}.
   *
   * @param user the {@link User} to clone.
   * @return the clone.
   */
  private static User clone(final User user) {
    User c = null;
    if (user != null) {
      c = new User(user.getUsername(), user.getPassword(), user.isEnabled(),
          user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
          user.getAuthorities());
    }
    return c;
  }

  /**
   * Map the {@link AuthenticationType} to a {@link User}.
   *
   * @param auth the {@link AuthenticationType}.
   * @return the {@link User}.
   */
  private User getUser(final AuthenticationType auth) {
    final List<GrantedAuthority> authorities =
        new ArrayList<>(auth.getGrantedAuthority().size() + 1);
    authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
    for (final GrantedAuthorityType grantedAuthority : auth.getGrantedAuthority()) {
      authorities.add(new SimpleGrantedAuthority(grantedAuthority.getRoleName()));
    }
    final User u = new User(auth.getUserName(), Optional.ofNullable(auth.getPassword()).map(pwd -> {
      if (pwd.trim().startsWith("ENC(") && pwd.trim().endsWith(")")) {
        return EipJasyptEncryptionProvider.getEncryptor(this.properties).decrypt(pwd);
      }
      return null;
    }).orElse(auth.getPassword()), authorities);
    return u;
  }

  /**
   * @see com.qpark.eip.core.spring.security.EipUserProvider#getUser(java.lang.String)
   */
  @Override
  public User getUser(final String username) {
    if (this.userMap.isEmpty()) {
      this.setupUserMap();
    }
    return clone(this.userMap.get(username));
  }

  /**
   * Each 15 minutes this method is called.
   *
   * @see com.qpark.eip.core.ReInitalizeable#reInitalize()
   */
  @Override
  @Scheduled(fixedDelay = 900000)
  public void reInitalize() {
    this.setupUserMap();
  }

  /**
   * Read the {@link AuthenticationType}s out of the database and put the
   * mapped {@link User} into the user map.
   */
  private void setupUserMap() {
    this.logger.trace("+setupUserMap");
    final List<AuthenticationType> auths = this.authorityDao.getAuthenticationTypes(Boolean.TRUE);
    this.logger.trace(" setupUserMap found {} AuthenticationTypes", auths.size());
    /* Add all defined users. */
    for (final AuthenticationType auth : auths) {
      this.userMap.put(auth.getUserName(), this.getUser(auth));
    }
    /* Remove not existing users out of the user map. */
    final List<String> userNames = new ArrayList<>(this.userMap.size());
    Collections.addAll(userNames,
        this.userMap.keySet().toArray(new String[this.userMap.keySet().size()]));
    boolean foundUserNameInDatabase;
    for (final String userName : userNames) {
      foundUserNameInDatabase = false;
      for (final AuthenticationType auth : auths) {
        if (auth.getUserName().equals(userName)) {
          foundUserNameInDatabase = true;
          break;
        }
      }
      if (!foundUserNameInDatabase) {
        this.userMap.remove(userName);
      }
    }
    this.logger.trace("-setupUserMap");
  }
}
