/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.service.iss.tech.support;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.spring.lockedoperation.LockableOperation;
import com.qpark.eip.core.spring.lockedoperation.LockableOperationContext;
import com.qpark.eip.core.spring.lockedoperation.model.OperationEventEnumType;
import com.qpark.eip.core.spring.lockedoperation.model.OperationStateEnumType;
import com.samples.platform.model.iss.tech.support.OperationStateType;
import com.samples.platform.service.iss.tech.support.msg.AppOperationEventRequestType;
import com.samples.platform.service.iss.tech.support.msg.AppOperationEventResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation app operation event on service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportAppOperationEvent")
public class AppOperationEventOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(AppOperationEventOperation.class);

	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The list of all available {@link AppOperationEventListener}s. */
	@Autowired(required = false)
	private List<LockableOperation> lockedOperations;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link AppOperationEventRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link AppOperationEventResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<AppOperationEventResponseType> appOperationEvent(
			final JAXBElement<AppOperationEventRequestType> message) {
		this.logger.debug("+appOperationEvent");
		AppOperationEventResponseType response = this.of
				.createAppOperationEventResponseType();
		long start = System.currentTimeMillis();
		try {
			if (this.lockedOperations != null
					&& message.getValue().getOperationEvent() != null) {
				LockableOperationContext context = new LockableOperationContext();
				context.put("key", "what ever value");
				OperationEventEnumType event = OperationEventEnumType.START;
				try {
					event = OperationEventEnumType.fromValue(
							message.getValue().getOperationEvent().getEvent());
				} catch (IllegalArgumentException e) {
					event = OperationEventEnumType.START;
				}
				/*
				 * For each AppOperationEventListener out of the spring context.
				 */
				OperationStateEnumType status;
				for (LockableOperation operation : this.lockedOperations) {
					/* Send the event to the operation. */
					status = operation
							.runOperation(
									message.getValue().getOperationEvent()
											.getOperationUUID(),
									event, context);
					if (status != null) {
						response.setReport(new OperationStateType());
						response.getReport()
								.setOperationUUID(operation.getUUID());
						response.getReport().setState(status.value());
					}
				}
			}
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug(" appOperationEvent duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-appOperationEvent #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createAppOperationEventResponse(response);
	}
}
