/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.inf;

/**
 * The flow context containing the requester user name, service name and version
 * and the operation name.
 *
 * @author bhausen
 */
public interface FlowContext {
  /**
   * Get the operation name of the flow requester.
   *
   * @return the operation name of the flow requester.
   */
  String getRequesterOperationName();

  /**
   * Get the service name of the flow requester.
   *
   * @return the service name of the flow requester.
   */
  String getRequesterServiceName();

  /**
   * Get the service version of the flow requester.
   *
   * @return the service version of the flow requester.
   */
  String getRequesterServiceVersion();

  /**
   * Get the user name of the flow requester.
   *
   * @return the user name of the flow requester.
   */
  String getRequesterUserName();

  /**
   * Get the session id.
   *
   * @return the session id.
   */
  String getSessionId();

  /**
   * Set the operation name of the flow requester.
   *
   * @param requesterOperationName
   *            the operation name of the flow requester.
   */
  void setRequesterOperationName(String requesterOperationName);

  /**
   * Set the service name of the flow requester.
   *
   * @param requesterServiceName
   *            the service name of the flow requester.
   */
  void setRequesterServiceName(String requesterServiceName);

  /**
   * Set the service version of the flow requester.
   *
   * @param requesterServiceVersion
   *            the service version of the flow requester.
   */
  void setRequesterServiceVersion(String requesterServiceVersion);

  /**
   * Set the user name of the flow requester.
   *
   * @param requesterUserName
   *            the user name of the flow requester.
   */
  void setRequesterUserName(String requesterUserName);

  /**
   * Set the session id.
   *
   * @param sessionId
   *            the session id.
   */
  void setSessionId(String sessionId);

}
