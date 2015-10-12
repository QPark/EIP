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
 * Get the information about the number of calls allowed and how many calls
 * already made.
 * 
 * @author bhausen
 */
public interface EipLimitedAccessDataProvider {
    /**
     * Get the number of calls the user is allowed to do.
     * 
     * @param userName
     *            the name of the user.
     * @param serviceName
     *            the service name.
     * @param operationName
     *            the name of the operation.
     * @return the number of calls the user is allowed to do.
     */
    int getAllowedRequestNumber(String userName, String serviceName, String operationName);

    /**
     * Get the number of calls the user already made.
     * 
     * @param userName
     *            the name of the user.
     * @param serviceName
     *            the service name.
     * @param operationName
     *            the name of the operation.
     * @return the number of calls the user already made.
     */
    int getCurrentRequestNumber(String userName, String serviceName, String operationName);
}
