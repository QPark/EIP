/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015, 2016 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis.report.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * One row in the service report.
 *
 * @author bhausen
 */
public class ServiceReportRow {
	/** The description of the request or response. */
	private String description;
	/** The fields of the request or response. */
	private String fields;
	/** The list of flows called in the request or response. */
	private List<String> flowLinks;
	/** The described row is a request. */
	private boolean isRequest;
	/** The name of the operation. */
	private String operationName;
	/** The name of the service. */
	private String serviceName;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the fields
	 */
	public String getFields() {
		return this.fields;
	}

	/**
	 * @return the flowLinks
	 */
	public List<String> getFlowLinks() {
		if (Objects.isNull(this.flowLinks)) {
			this.flowLinks = new ArrayList<>();
		}
		return this.flowLinks;
	}

	/**
	 * @return the operationName
	 */
	public String getOperationName() {
		return this.operationName;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return this.serviceName;
	}

	/**
	 * @return the isRequest
	 */
	public boolean isRequest() {
		return this.isRequest;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(final String fields) {
		this.fields = fields;
	}

	/**
	 * @param flowLinks
	 *            the flowLinks to set
	 */
	public void setFlowLinks(final List<String> flowLinks) {
		this.flowLinks = flowLinks;
	}

	/**
	 * @param operationName
	 *            the operationName to set
	 */
	public void setOperationName(final String operationName) {
		this.operationName = operationName;
	}

	/**
	 * @param isRequest
	 *            the isRequest to set
	 */
	public void setRequest(final boolean isRequest) {
		this.isRequest = isRequest;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(final String serviceName) {
		this.serviceName = serviceName;
	}
}
