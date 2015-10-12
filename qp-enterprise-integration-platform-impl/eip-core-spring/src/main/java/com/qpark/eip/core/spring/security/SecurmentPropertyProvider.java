/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core.spring.security;

/**
 * @author bhausen
 */
public interface SecurmentPropertyProvider {
    /**
     * @return the securementActions
     */
    String getSecurementActions();

    /**
     * @return the securementUsername
     */
    String getSecurementUsername();

    /**
     * @return the securementPassword
     */
    String getSecurementPassword();

    /**
     * @return the securementPasswordType
     */
    String getSecurementPasswordType();

    /**
     * @return the securementUsernameTokenElements
     */
    String getSecurementUsernameTokenElements();
}