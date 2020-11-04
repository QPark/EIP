/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.StrongTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.qpark.eip.core.ReInitalizeable;
import com.qpark.eip.core.spring.ApplicationPlaceholderConfigurer;
import com.qpark.eip.core.spring.security.EipJasyptEncryptionProvider;
import com.qpark.eip.core.spring.security.EipUserProvider;

/**
 * The {@link PropertyUserProvider} read the user name and roles assignments out
 * of the applications properties. These are normally stored in the templates
 * <i>bus.app.xxx-user.properties</i> file and loaded into the
 * {@link ApplicationPlaceholderConfigurer}.
 * <p/>
 * Properties starting with <i>bus.user.definition.xxx</i> defining the user
 * with key <code>xxx</code>. The user name property ends with <i>userName</i>,
 * the password with <i>password</i>. The properties containing the roles
 * assignments contain a <i>.role.</i> in their name. The following example
 * shows the user definition of user with key <code>xxx</code> having the name
 * <i>nameOfUserA</i>.
 *
 * <pre>
 * bus.user.definition.xxx.userName=nameOfUserA
 * bus.user.definition.xxx.password=password
 * bus.user.definition.xxx.role.0=ROLE_GET_USER
 * bus.user.definition.xxx.role.1=ROLE_COMMON
 * bus.user.definition.xxx.role.2=ROLE_READ
 * </pre>
 *
 * The user <code>nameOfUserA</code> has to authenticate with password
 * <code>password</code> and get the authority to get user, do all things in
 * service common and to access all read operations of all services.
 *
 * @author bhausen
 */
public class PropertyUserProvider implements EipUserProvider, ReInitalizeable {
  /** The container having the user data out of the parsed properties. */
  static class UserDefinition {
    /** The key of the user. */
    private final String key;
    /** The name of the user. */
    private String name;
    /** The password of the user. */
    private String password = "";
    /** The set containing the roles of the user. */
    private final TreeSet<String> rolenames = new TreeSet<>();

    /**
     * Create a {@link UserDefinition} with the key.
     *
     * @param key
     */
    UserDefinition(final String key) {
      this.key = key;
    }

    /**
     * Add the role to the set of role names the user is assigned to .
     *
     * @param rolename the role name to add.
     */
    public void addRolename(final String rolename) {
      if (rolename.startsWith("ROLE_")) {
        this.rolenames.add(rolename);
      }
    }

    /**
     * Get the key of the user.
     *
     * @return the key.
     */
    public String getKey() {
      return this.key;
    }

    /**
     * Get the name of the user.
     *
     * @return the name.
     */
    public String getName() {
      return this.name;
    }

    /**
     * Get the password of the user.
     *
     * @return the password.
     */
    public String getPassword() {
      return this.password;
    }

    /**
     * Get the list of {@link GrantedAuthority} containing the roles of the
     * user.
     *
     * @return the list of {@link GrantedAuthority}.
     */
    public List<GrantedAuthority> getRoles() {
      final List<GrantedAuthority> roles = new ArrayList<>();
      for (final String rolename : this.rolenames) {
        roles.add(new SimpleGrantedAuthority(rolename));
      }
      return roles;
    }

    /**
     * Set the name of the user.
     *
     * @param name the name of the user.
     */
    public void setName(final String name) {
      this.name = name;
    }

    /**
     * Set the password of the user.
     *
     * @param password the password of the user.
     */
    public void setPassword(final String password) {
      this.password = password;
    }

    /**
     * Get the size of the list of roles of the user.
     *
     * @return the number of roles of the user.
     */
    public int size() {
      return this.rolenames.size();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      final StringBuffer sb = new StringBuffer(512);
      sb.append("User \"").append(this.name).append("\" with roles ");
      int i = 0;
      for (final String rolename : this.rolenames) {
        if (i > 0) {
          sb.append(", ");
        }
        sb.append(rolename);
        i++;
      }
      return sb.toString();
    }
  }

  /**
   * Get the user definition key out of the property name (<code>xxx</code> in
   * the example).
   *
   * @param propertyName The property name.
   * @return the user name key part.
   */
  private static String extractUser(final String propertyName) {
    String userName = "";
    userName = propertyName.substring(BUS_PROPERTY_NAME_START.length(), propertyName.length());
    userName = userName.substring(0, userName.indexOf('.'));
    return userName;
  }

  /** The {@link Logger}. */
  private final Logger logger = LoggerFactory.getLogger(PropertyUserProvider.class);
  /** The {@link ApplicationPlaceholderConfigurer}. */
  @Autowired
  private ApplicationPlaceholderConfigurer properties;
  /** The start of all user definition property names. */
  private static final String BUS_PROPERTY_NAME_START = "bus.user.definition.";
  /** The name of the anonymous role. */
  public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
  /** The name of the common role. */
  public static final String ROLE_COMMON = "ROLE_COMMON";
  /** The map containing the {@link UserDefinition} objects of the application. */
  private final Map<String, UserDefinition> userDefinitionMap = new HashMap<>();

  /**
   * Read the user definitions out of the properties and put the into the map
   * {@link PropertyUserProvider#userDefinitionMap}.
   */
  private synchronized void createUsers() {
    this.logger.trace("+createUsers");
    /* Create an _sorted_ map containing all user definition properties. */
    final TreeMap<String, String> userDefinitionProperties = new TreeMap<>();
    for (final Map.Entry<String, String> entry : this.properties.getProperties().entrySet()) {
      if (entry.getKey() != null && entry.getKey().startsWith(BUS_PROPERTY_NAME_START)) {
        userDefinitionProperties.put(entry.getKey(), entry.getValue());
      }
    }
    final StrongTextEncryptor encryptor = EipJasyptEncryptionProvider.getEncryptor(this.properties);

    /* Create a map of UserDefinitions parsed out of the properties. */
    final HashMap<String, UserDefinition> parsedUserDefinitions = new HashMap<>();
    for (final Map.Entry<String, String> userDefinitionProperty : userDefinitionProperties
        .entrySet()) {
      /* Get the user key out of the property name. */
      final String userKey = extractUser(userDefinitionProperty.getKey());
      parsedUserDefinitions.putIfAbsent(userKey, new UserDefinition(userKey));
      final UserDefinition userDefinition = parsedUserDefinitions.get(userKey);
      /* Setup the content of the property into the UserDefinition. */
      if (userDefinitionProperty.getKey().endsWith("userName")) {
        userDefinition.setName(userDefinitionProperty.getValue());
      } else if (userDefinitionProperty.getKey().contains(".role.")) {
        if (userDefinitionProperty.getValue() != null
            && !userDefinitionProperty.getValue().equals(ROLE_ANONYMOUS)) {
          userDefinition.addRolename(userDefinitionProperty.getValue());
        }
      }
    }
    /* Password setup */
    for (final Map.Entry<String, String> userDefinitionProperty : userDefinitionProperties
        .entrySet()) {
      if (userDefinitionProperty.getKey().endsWith("password")) {
        /* Get the user key out of the property name. */
        final String userKey = extractUser(userDefinitionProperty.getKey());
        final UserDefinition userDefinition = parsedUserDefinitions.get(userKey);
        if (parsedUserDefinitions.containsKey(userKey)) {
          userDefinition
              .setPassword(Optional.ofNullable(userDefinitionProperty.getValue()).map(pwd -> {
                String pwdEncrypted = null;
                if (pwd.trim().startsWith("ENC(") && pwd.trim().endsWith(")")) {
                  this.logger.debug(
                      "createUsers: Try password decryption '{}' password '{}' failed.",
                      userDefinition.getName(), pwd);
                  try {
                    pwdEncrypted =
                        encryptor.decrypt(pwd.substring(0, pwd.length() - 1).replace("ENC(", ""));
                  } catch (final EncryptionOperationNotPossibleException e) {
                    this.logger.error(
                        "createUsers: Password decryption of user '{}' password '{}' failed.",
                        userDefinition.getName(), pwd);
                    this.logger.error(e.getMessage(), e);
                  }
                }
                return pwdEncrypted;
              }).orElse(userDefinitionProperty.getValue()));
        } else {
          this.logger.error("No user setup for password property {}",
              userDefinitionProperty.getKey());
        }
      }
    }

    /*
     * Update the userMap. If the userName of the userMap is not part of the parsedUserDefinitions
     * any more, the user is removed out of the userMap.
     */
    final List<String> validUserNames = parsedUserDefinitions.values().stream()
        .map(UserDefinition::getName).collect(Collectors.toList());
    final List<String> userNamesToRemove = this.userDefinitionMap.keySet().stream()
        .filter(key -> !validUserNames.contains(key)).collect(Collectors.toList());
    userNamesToRemove.stream().forEach(u -> this.userDefinitionMap.remove(u));
    parsedUserDefinitions.values().stream()
        .forEach(ud -> this.userDefinitionMap.put(ud.getName(), ud));
    this.logger.trace("-createUsers");
  }

  /**
   * @see com.qpark.eip.core.spring.security.EipUserProvider#getUser(java.lang.String)
   */
  @Override
  public User getUser(final String username) {
    if (this.userDefinitionMap.size() == 0) {
      this.createUsers();
    }
    return this.getUser(this.userDefinitionMap.get(username));
  }

  /**
   * Add the user from the userDefinition to the map.
   *
   * @param userDefinition
   */
  private User getUser(final UserDefinition userDefinition) {
    return Optional.ofNullable(userDefinition).map(ud -> {
      userDefinition.addRolename(ROLE_ANONYMOUS);
      final boolean enabled = true;
      final boolean accountNonExpired = true;
      final boolean credentialsNonExpired = true;
      final boolean accountNonLocked = true;
      final User user = new User(userDefinition.getName(), userDefinition.getPassword(), enabled,
          accountNonExpired, credentialsNonExpired, accountNonLocked, userDefinition.getRoles());
      if (this.logger.isTraceEnabled()) {
        this.logger.trace("Created: {} {}", userDefinition.toString(), userDefinition.getRoles()
            .stream().map(r -> r.getAuthority()).collect(Collectors.joining(", ")));
      }
      return user;
    }).orElse(null);
  }

  /**
   * @see com.qpark.eip.core.ReInitalizeable#reInitalize()
   */
  @Override
  public void reInitalize() {
    this.logger.debug("+reInitalize: Setup the userMap ({})", this.userDefinitionMap.size());
    this.createUsers();
    this.logger.debug("-reInitalize: Setup the userMap ({})", this.userDefinitionMap.size());
  }
}
