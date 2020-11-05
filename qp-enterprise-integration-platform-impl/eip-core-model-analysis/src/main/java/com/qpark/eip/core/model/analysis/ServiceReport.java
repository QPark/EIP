/*******************************************************************************
 * Copyright (c) 2013 - 2016 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.model.analysis;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.core.reporting.Report;
import com.qpark.eip.core.reporting.ReportUtil;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.RequestResponseDataType;
import com.qpark.eip.model.reporting.ReportContentType;
import com.qpark.eip.model.reporting.ReportHeaderDataType;
import com.qpark.eip.model.reporting.ReportInfoType;
import com.qpark.eip.model.reporting.ReportMetaDataType;
import com.qpark.eip.model.reporting.ReportType;
import com.qpark.eip.service.domain.doc.report.DataProviderModelAnalysis;

/**
 * @author bhausen
 */
public class ServiceReport extends Report<DataProviderModelAnalysis> {
	/** @return ReportHeaderDataType */
	public static ReportHeaderDataType createHeaderRow() {
		ReportHeaderDataType rh = new ReportHeaderDataType();
		rh.setRowNumber(1);
		List<ReportMetaDataType> md = rh.getReportMetaData();
		md.add(ReportUtil.getColumnHeader("Package name"));
		md.add(ReportUtil.getColumnHeader("ServiceId"));
		md.add(ReportUtil.getColumnHeader("Operation name"));
		md.add(ReportUtil.getColumnHeader("Request/Response"));
		md.add(ReportUtil.getColumnHeader("Operation description"));
		md.add(ReportUtil.getColumnHeader("Operation elements"));
		md.add(ReportUtil.getColumnHeader("TargetNamespace"));
		return rh;
	}

	/** @return the {@link ReportType}. */
	private static ReportType createReportDefinition() {
		ReportType report = ofr.createReportType();
		report.setCreated(DateUtil.get(Instant.now()));
		ReportHeaderDataType headerRow = createHeaderRow();
		report.getReportHeaderData().add(headerRow);
		report.setReportInfo(new ReportInfoType());
		report.getReportInfo().setDelimiter(ReportUtil.DELIMITER);
		report.getReportInfo().setNumberOfColumns(headerRow.getReportMetaData().size());
		return report;
	}

	/**
	 * @param report
	 * @param reportDate
	 */
	public ServiceReport(final Date reportDate) {
		super(createReportDefinition(), reportDate);
		this.report.setReportName(this.getReportName());
		this.report.setReportUUID(UUID.randomUUID().toString());
		this.report.setArtefact(ServiceReport.class.getPackage().getName());
		this.report.setArtefactVersion("1.0");
		this.report.setEnvironment("EIP Sample");
	}

	/**
	 * @see com.qpark.eip.core.reporting.Report#createReportContent(java.lang.Object)
	 */
	@Override
	public Report<DataProviderModelAnalysis> createReportContent(final DataProviderModelAnalysis data) {
		List<ReportContentType> rows = new ArrayList<>();
		data.getServiceIds().stream().map(sid -> data.getService(sid).orElse(null)).filter(Objects::nonNull)
				.forEach(service -> service.getOperation().stream().sorted(Comparator.comparing(OperationType::getName))
						.forEach(operation -> {
							RequestResponseDataType requestResponse = operation.getRequestResponse();
							Optional.ofNullable(requestResponse).map(rr -> rr.getRequestId())
									.map(rid -> data.getElement(rid)).ifPresent(element -> {
										element.ifPresent(e -> data.getComplexType(e.getComplexTypeId())
												.ifPresent(complexType -> {
													rows.add(ReportUtil.getRow(1, ReportUtil.DELIMITER,
															new String[] { complexType.getJavaPackageName(),
																	service.getName(), operation.getShortName(),
																	"Request", operation.getRequestFieldDescription(),
																	data.getFieldList(complexType),
																	complexType.getNamespace() }));
												}));
									});
							Optional.ofNullable(requestResponse).map(rr -> rr.getResponseId())
									.map(rid -> data.getElement(rid)).ifPresent(element -> {
										element.ifPresent(e -> data.getComplexType(e.getComplexTypeId())
												.ifPresent(complexType -> {
													rows.add(ReportUtil.getRow(1, ReportUtil.DELIMITER,
															new String[] { complexType.getJavaPackageName(),
																	service.getName(), operation.getShortName(),
																	"Response", operation.getResponseFieldDescription(),
																	data.getFieldList(complexType),
																	complexType.getNamespace() }));
												}));
									});

						}));
		int i = this.report.getReportHeaderData().size();
		this.report.getReportContent().addAll(rows);
		for (ReportContentType c : this.report.getReportContent()) {
			c.setRow(i++);
		}
		int numRows = this.report.getReportHeaderData().size() + this.report.getReportContent().size();
		this.report.getReportInfo().setNumberOfRows(numRows);
		return this;

	}

	/**
	 * @see com.qpark.eip.core.reporting.Report#getReportName()
	 */
	@Override
	public String getReportName() {
		return this.getClass().getSimpleName();
	}
}
