/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.domaindoc;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.model.analysis.ServiceReport;
import com.qpark.eip.core.reporting.Report;
import com.qpark.eip.core.reporting.ReportRenderer;
import com.qpark.eip.core.spring.lockedoperation.AbstractAsyncLockableOperation;
import com.qpark.eip.core.spring.lockedoperation.LockableOperation;
import com.qpark.eip.core.spring.lockedoperation.LockableOperationContext;
import com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis;

/**
 * @author bhausen
 */
@Component
public class CreateServiceReport extends AbstractAsyncLockableOperation {
	/** The UUID of the {@link LockableOperation}. */
	public static final String OPERATION_UUID = "3668b727-b3a8-496b-99b7-d77b517a738a";
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreateServiceReport.class);
	/** The {@link EnterpriseDao}. */
	@Autowired
	private EnterpriseDao enterprise;

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return this.logger;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.LockableOperation#getUUID()
	 */
	@Override
	public String getUUID() {
		return OPERATION_UUID;
	}

	/**
	 * @see com.qpark.eip.core.spring.lockedoperation.AbstractAsyncLockableOperation#invokeOperationAsync(com.qpark.eip.core.spring.lockedoperation.LockableOperationContext)
	 */
	@Override
	protected void invokeOperationAsync(final LockableOperationContext context) {
		this.logger.debug("+invokeOperationAsync {} {}", this.getName(), this.getUUID());
		final ReportRenderer renderer = new ReportRenderer();
		StringBuffer sb = new StringBuffer();
		Report<DataProviderModelAnalysis> report = new ServiceReport(new Date()).createReportContent(this.enterprise);
		try {
			renderer.renderReportCSV(report.getReport(), sb);
			this.logger.info(sb.toString());
		} catch (final Exception e) {
			this.logger.error(e.getMessage(), e);
		}
		this.logger.debug("-invokeOperationAsync {} {}", this.getName(), this.getUUID());
	}
}
