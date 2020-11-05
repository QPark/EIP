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
 * Basic flow interface.
 *
 * @author bhausen
 * @param <Request>
 * @param <Response>
 */
public interface Flow<Request, Response> {
  /**
   * Invoke the flow. This calls executeRequest and processResponse.
   *
   * @param request the Request.
   * @param flowContext the {@link FlowContext}
   * @return the Response.
   */
  Response invokeFlow(Request request, FlowContext flowContext);
}

