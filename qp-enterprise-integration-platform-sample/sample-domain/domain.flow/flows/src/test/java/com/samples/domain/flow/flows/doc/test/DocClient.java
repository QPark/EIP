/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.flow.flows.doc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FieldType;
import com.qpark.eip.model.docmodel.FlowFilterType;
import com.qpark.eip.model.docmodel.FlowMapInOutType;
import com.qpark.eip.model.docmodel.FlowProcessType;
import com.qpark.eip.model.docmodel.FlowRuleType;
import com.qpark.eip.model.docmodel.FlowSubRequestType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.OperationType;
import com.qpark.eip.model.docmodel.RequestResponseDataType;
import com.qpark.eip.model.docmodel.ServiceType;

/**
 * Basic methods to create an Excel sheet with Service Bus information about
 * Services, Flows, Mappings and so on.
 *
 * @author sneuroh
 */

public class DocClient {
  /**
   * Inner class for Flow Step Information
   *
   * @author sneuroh
   */
  private class FlowStepInfo {
    /** UUID */
    String UUID;
    /** inbound or outbound */
    String direction;
    /** name */
    String name;
    /** rule, mapping or subrequest */
    String type;
    /** Input or output types */
    String io;
    /** Description of the Flow Step */
    String descr;
    /** Mapping input types of the InterfaceTypes (if any) */
    String mappingInputTypes = "";

    @Override
    public String toString() {
      final StringBuffer sb = new StringBuffer(256);
      sb.append(this.name).append("(").append(this.direction).append("/").append(this.type)
          .append(")");
      return sb.toString();
    }
  }

  private static String DIR_FLOW_IN = "Inbound";
  private static String DIR_FLOW_OUT = "Outbound";
  private static String STEP_RULE = "Rule";
  private static String STEP_SUB = "Information Request";
  private static String STEP_MAP = "Mapping";
  private HSSFWorkbook wb;
  private HSSFCellStyle headerStyle;
  private HSSFCellStyle bodyStyle;
  private DomainDocProvider provider;
  private Map<String, String> ctIdMap = new ConcurrentHashMap<>(256);
  private CellStyle frequencyStyle;
  private HSSFCellStyle wordWrapStyle;

  /**
   * Constructor
   */
  public DocClient(final DomainDocProvider provider) {
    this.wb = new HSSFWorkbook();
    this.provider = provider;
    this.headerStyle = this.createHeaderStyle();
    this.bodyStyle = this.createBodyStyle();
    this.wordWrapStyle = this.createdWordWrapStyle();
  }

  /**
   * Add a sheet with ComplexType information
   *
   * @param complexTypeUUIDSet
   *            Set of ComplexType UUID
   */
  public void addDataTypeSheet(final Collection<String> complexTypeUUIDSet) {
    final HSSFSheet sheet = this.createSheet("ComplexTypes");
    this.createDataTypeSheet(sheet, complexTypeUUIDSet);
    sheet.createFreezePane(0, 1, 0, 1);
    sheet.setAutoFilter(new CellRangeAddress(0, sheet.getRow(sheet.getLastRowNum()).getRowNum(), 0,
        sheet.getRow(sheet.getFirstRowNum()).getLastCellNum()));
  }

  /**
   * Add information about a Flow Filter to the flowInfo (Filter to be
   * replaced by rule)
   *
   * @param flowInfo
   *            List of FlowStepInfo (UUID, name, type, direction)
   * @param filter
   *            List of Flow Filters
   * @param direction
   *            inbound or outbound
   */
  private void addFlowFilter(final List<FlowStepInfo> flowInfo, final List<FlowFilterType> filter,
      final String direction) {
    FlowStepInfo e = null;
    FlowFilterType ffT = null;
    RequestResponseDataType rrdT = null;
    for (final FlowFilterType element : filter) {
      ffT = element;
      e = new FlowStepInfo();
      e.UUID = ffT.getId();
      e.name = ffT.getName();
      e.type = STEP_RULE;
      e.direction = direction;

      rrdT = ffT.getFilterInOut();
      final ComplexType flowInput = this.getComplexType(rrdT.getRequestId());
      final ComplexType flowOutput = this.getComplexType(rrdT.getResponseId());

      e.io = this.getComplexTypeFieldElements(flowInput) + "\n"
          + this.getComplexTypeFieldElements(flowOutput);
      e.descr =
          ffT.getFilterInFieldDescription() + "\n" + "\n" + ffT.getFilterOutFieldDescription();
      flowInfo.add(e);
    }
  }

  /**
   * Add information about a Flow Mappings to the flowInfo
   *
   * @param flowInfo
   *            List of FlowStepInfo (UUID, name, type, direction)
   * @param map
   *            List of Flow mappings
   * @param direction
   *            inbound or outbound
   */
  private void addFlowMapping(final List<FlowStepInfo> flowInfo, final List<FlowMapInOutType> map,
      final String direction, final List<InterfaceMappingType> interfaceTypes) {
    FlowStepInfo e = null;
    RequestResponseDataType rrdT = null;
    for (final FlowMapInOutType fmioT : map) {
      e = new FlowStepInfo();
      e.UUID = fmioT.getId();
      e.name = fmioT.getName();
      e.type = STEP_MAP;
      e.direction = direction;

      rrdT = fmioT.getMapInOut();
      final ComplexType flowInput = this.getComplexType(rrdT.getRequestId());
      final ComplexType flowOutput = this.getComplexType(rrdT.getResponseId());

      String flowInputInfo = "()";
      String flowOutputInfo = "()";
      if (flowInput != null) {
        flowInputInfo = this.getComplexTypeFieldElements(flowInput);
      }
      if (flowOutput != null) {
        flowOutputInfo = this.getComplexTypeFieldElements(flowOutput);
      }

      e.io = flowInputInfo + "\n" + flowOutputInfo;
      e.descr = fmioT.getMapInFieldDescription() + "\n" + "\n" + fmioT.getMapOutFieldDescription();

      final Set<String> mappingInputTypes = new TreeSet<>();
      interfaceTypes.stream().filter(inf -> fmioT.getInterfaceMappingId().contains(inf.getId()))
          .forEach(inf -> {
            mappingInputTypes.addAll(this.getComplexTypeNames(inf.getFieldMappingInputType()));
          });
      final StringBuffer sb = new StringBuffer(512);
      mappingInputTypes.stream().forEach(ctName -> sb.append(ctName));
      e.mappingInputTypes = sb.toString();

      flowInfo.add(e);
    }
  }

  /**
   * Add a row to a Flow description sheet for a FlowStepInfo object
   *
   * @param sheet
   *            Sheet
   * @param rownum
   *            row number
   * @param flowName
   *            Flow name
   * @param stepInfo
   *            FlowStepInfo object
   */
  private void addFlowRow(final HSSFSheet sheet, final int rownum, final String flowName,
      final FlowStepInfo stepInfo) {
    final HSSFRow row = sheet.createRow(rownum);
    // Flow
    this.createCell(row, 0, flowName, this.bodyStyle);
    // Direction
    if (Objects.nonNull(stepInfo)) {
      this.createCell(row, 1, stepInfo.direction, this.bodyStyle);
      // Input/Output
      this.createCell(row, 2, stepInfo.io, this.wordWrapStyle);
      // Type
      this.createCell(row, 3, stepInfo.type, this.wordWrapStyle);
      // Processing Step
      this.createCell(row, 4, stepInfo.name, this.bodyStyle);
      // Description
      this.createCell(row, 5, stepInfo.descr, this.wordWrapStyle);
      // Description
      this.createCell(row, 6, stepInfo.mappingInputTypes, this.bodyStyle);
    } else {
      this.createCell(row, 1, "[missing]", this.bodyStyle);
      // Input/Output
      this.createCell(row, 2, "[missing]", this.bodyStyle);
      // Type
      this.createCell(row, 3, "[missing]", this.bodyStyle);
      // Processing Step
      this.createCell(row, 4, "[missing]", this.bodyStyle);
      // Description
      this.createCell(row, 5, "[missing]", this.bodyStyle);
      // Description
      this.createCell(row, 6, "[missing]", this.bodyStyle);
    }

  }

  /**
   * Add information about a Flow Filter to the flowInfo (Filter to be
   * replaced by rule)
   *
   * @param flowInfo
   *            List of FlowStepInfo (UUID, name, type, direction)
   * @param filter
   *            List of Flow Filters
   * @param direction
   *            inbound or outbound
   */
  private void addFlowRule(final List<FlowStepInfo> flowInfo, final List<FlowRuleType> filter,
      final String direction) {
    FlowStepInfo e = null;
    FlowRuleType frT = null;
    RequestResponseDataType rrdT = null;
    for (final FlowRuleType element : filter) {
      frT = element;
      e = new FlowStepInfo();
      e.UUID = frT.getId();
      e.name = frT.getName();
      e.type = STEP_RULE;
      e.direction = direction;

      rrdT = frT.getRuleInOut();
      final ComplexType flowInput = this.getComplexType(rrdT.getRequestId());
      final ComplexType flowOutput = this.getComplexType(rrdT.getResponseId());

      e.io = this.getComplexTypeFieldElements(flowInput) + "\n"
          + this.getComplexTypeFieldElements(flowOutput);
      e.descr = frT.getRuleInFieldDescription() + "\n" + "\n" + frT.getRuleOutFieldDescription();
      flowInfo.add(e);
    }
  }

  /**
   * Add a sheet with Flow information
   *
   * @param flowPattern
   *            String pattern for flow lookup (contains)
   */
  public void addFlowSheet(final String flowPattern) {
    final HSSFSheet sheet = this.createSheet("Flows");
    this.createFlowSheet(sheet, flowPattern);
    sheet.createFreezePane(0, 1, 0, 1);
    sheet.setAutoFilter(new CellRangeAddress(0, sheet.getRow(sheet.getLastRowNum()).getRowNum(), 0,
        sheet.getRow(sheet.getFirstRowNum()).getLastCellNum()));
  }

  /**
   * Add information about a Flow SubRequest to the flowInfo
   *
   * @param flowInfo
   *            List of FlowStepInfo (UUID, name, type, direction)
   * @param sub
   *            List of Flow SubRequests
   * @param direction
   *            inbound or outbound
   */
  private void addFlowSubRequest(final List<FlowStepInfo> flowInfo,
      final List<FlowSubRequestType> sub, final String direction) {
    FlowStepInfo e = null;
    FlowSubRequestType fsrT = null;
    RequestResponseDataType rrdT = null;
    for (final FlowSubRequestType element : sub) {
      fsrT = element;
      e = new FlowStepInfo();
      e.UUID = fsrT.getId();
      e.name = fsrT.getName();
      e.type = STEP_SUB;
      e.direction = direction;

      rrdT = fsrT.getSubRequestInOut();
      final ComplexType flowInput = this.getComplexType(rrdT.getRequestId());
      final ComplexType flowOutput = this.getComplexType(rrdT.getResponseId());

      String flowInputInfo = "()";
      String flowOutputInfo = "()";
      if (flowInput != null) {
        flowInputInfo = this.getComplexTypeFieldElements(flowInput);
      }
      if (flowOutput != null) {
        flowOutputInfo = this.getComplexTypeFieldElements(flowOutput);
      }

      e.io = flowInputInfo + "\n" + flowOutputInfo;
      e.descr = fsrT.getSubRequestFieldDescription() + "\n" + "\n"
          + fsrT.getSubResponseFieldDescription();

      flowInfo.add(e);
    }
  }

  /**
   * Add a sheet with Interface type information for a flow identified by a
   * flow pattern string
   *
   * @param packagePattern
   *            flowPattern String pattern for flow lookup (contains)
   */
  public void addInterfaceMappingSheet(final List<String> packagePattern) {
    final HSSFSheet sheet = this.createSheet("Mappings");
    this.createInterfaceMappingSheet(sheet, packagePattern);
    sheet.createFreezePane(0, 1, 0, 1);
    sheet.setAutoFilter(new CellRangeAddress(0, sheet.getRow(sheet.getLastRowNum()).getRowNum(), 0,
        sheet.getRow(sheet.getFirstRowNum()).getLastCellNum()));
  }

  /**
   * Add a sheet with Interface type information for a flow identified by a
   * flow pattern string
   *
   * @param flowPattern
   *            flowPattern String pattern for flow lookup (contains)
   */
  public void addInterfaceMappingSheet(final String flowPattern) {
    final HSSFSheet sheet = this.createSheet("Mappings");
    this.createInterfaceMappingSheet(sheet, flowPattern);
    sheet.createFreezePane(0, 1, 0, 1);
    sheet.setAutoFilter(new CellRangeAddress(0, sheet.getRow(sheet.getLastRowNum()).getRowNum(), 0,
        sheet.getRow(sheet.getFirstRowNum()).getLastCellNum()));
  }

  /**
   * Add a sheet with Service information
   *
   * @param serviceIds
   *            List of Service UUIDs
   */
  public void addServiceSheet(final List<String> serviceIds, final Set<String> ctIds) {
    this.addServiceSheet(serviceIds, ".*", ctIds);
  }

  /**
   * Add a sheet with Service information
   *
   * @param serviceIds
   *            List of Service UUIDs
   */
  public void addServiceSheet(final List<String> serviceIds, final String operationPattern) {
    this.addServiceSheet(serviceIds, operationPattern, new HashSet<String>());
  }

  /**
   * Add a sheet with Service information
   *
   * @param serviceIds
   *            List of ComplexType UUIDs
   */
  public void addServiceSheet(final List<String> serviceIds, final String operationPattern,
      final Set<String> ctIds) {
    final HSSFSheet sheet = this.createSheet("Service");
    this.createServiceSheet(sheet, serviceIds, operationPattern, ctIds);
    sheet.createFreezePane(0, 1, 0, 1);
    sheet.setAutoFilter(new CellRangeAddress(0, sheet.getRow(sheet.getLastRowNum()).getRowNum(), 0,
        sheet.getRow(sheet.getFirstRowNum()).getLastCellNum()));

  }

  /**
   * Auto size the columns of the given sheet.
   *
   * @param sheet
   *            the sheet.
   */
  private void autoSizeColumn(final HSSFSheet sheet) {
    if (sheet != null && sheet.getRow(0) != null) {
      for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
        sheet.autoSizeColumn(j);
      }
    }
  }

  public void autoSizeColumns() {
    for (int i = 0; i < this.wb.getNumberOfSheets(); i++) {
      this.autoSizeColumn(this.wb.getSheetAt(i));
    }
  }

  /**
   * @return body style
   */
  private HSSFCellStyle createBodyStyle() {
    final HSSFCellStyle style = this.wb.createCellStyle();
    // style.setWrapText(true);
    return style;
  }

  /**
   * Create a cell in the sheet row at index and set the value and apply style
   *
   * @param row
   *            row
   * @param index
   *            index in row
   * @param value
   *            value
   * @param style
   *            cell style
   * @return cell created
   */
  private HSSFCell createCell(final HSSFRow row, final int index, final String value,
      final HSSFCellStyle style) {
    final HSSFCell cell = row.createCell(index);
    if (Objects.isNull(value)) {
      cell.setCellValue("[missing]");
    } else {
      cell.setCellValue(value.trim());
    }
    cell.setCellStyle(style);
    return cell;
  }

  /**
   * Create a sheet with Complex Type information
   *
   * @param sheet
   * @param ctIds
   *            List
   */
  private void createDataTypeSheet(final HSSFSheet sheet, final Collection<String> ctIds) {

    int rownum = 0;
    final HSSFRow rowHeader = sheet.createRow(rownum);

    this.createCell(rowHeader, 0, "Package name", this.headerStyle);
    this.createCell(rowHeader, 1, "Type name", this.headerStyle);
    this.createCell(rowHeader, 2, "Inherited from", this.headerStyle);
    this.createCell(rowHeader, 3, "Cardinality", this.headerStyle);
    this.createCell(rowHeader, 4, "Field name", this.headerStyle);
    this.createCell(rowHeader, 5, "Field type", this.headerStyle);
    this.createCell(rowHeader, 6, "Field description", this.headerStyle);
    this.createCell(rowHeader, 7, "Type description", this.headerStyle);
    this.createCell(rowHeader, 8, "Type namespace", this.headerStyle);

    this.setColumnWidth(sheet, 0, 10);
    this.setColumnWidth(sheet, 1, 50);
    this.setColumnWidth(sheet, 2, 10);
    this.setColumnWidth(sheet, 5, 30);
    this.setColumnWidth(sheet, 3, 30);
    this.setColumnWidth(sheet, 4, 15);
    this.setColumnWidth(sheet, 6, 50);
    this.setColumnWidth(sheet, 7, 50);
    this.setColumnWidth(sheet, 8, 30);

    rownum++;
    final AtomicInteger rowNum = new AtomicInteger(rownum);
    for (final String ctId : ctIds) {
      final ComplexType ct = this.getComplexType(ctId);
      if (ct != null) {
        final List<FieldType> fields = ct.getField();
        if (Objects.nonNull(fields)) {
          fields.stream().filter(f -> Objects.nonNull(f))
              .sorted((f1, f2) -> Integer.compare(f1.getSequenceNumber(), f2.getSequenceNumber()))
              .forEach(f -> {
                final HSSFRow row = sheet.createRow(rowNum.getAndIncrement());

                final List<String> descendends = new ArrayList<>();
                this.getComplexTypeNamesParents(ct, descendends);

                this.createCell(row, 0, ct.getJavaPackageName(), this.wordWrapStyle);
                this.createCell(row, 1, this.getRealShortName(ct), this.bodyStyle);
                this.createCell(row, 2,
                    descendends.stream()
                        .filter(d -> !d.equals("anyType {http://www.w3.org/2001/XMLSchema}"))
                        .collect(Collectors.joining("\n")).trim(),
                    this.bodyStyle);
                this.createCell(row, 3, f.getCardinality(), this.bodyStyle);
                this.createCell(row, 4, f.getName(), this.bodyStyle);
                this.createCell(row, 5,
                    Optional.ofNullable(this.getDataType(f.getFieldTypeDefinitionId()))
                        .map(dt -> dt.getName()).orElse("unknown"),
                    this.bodyStyle);
                this.createCell(row, 6, f.getDescription(), this.wordWrapStyle);
                this.createCell(row, 7, ct.getDescription(), this.wordWrapStyle);
                this.createCell(row, 8, ct.getNamespace(), this.bodyStyle);
              });
        }
      }
    }
  }

  /**
   * @return the {@link CellStyle} supporting 4 digit frequencies.
   */
  public HSSFCellStyle createdWordWrapStyle() {
    final HSSFCellStyle style = this.wb.createCellStyle();
    style.setWrapText(true);
    return style;
  }

  /**
   * Setup a the sheet with Flow information
   *
   * @param sheet
   *            Sheet
   * @param flowPattern
   *            String pattern for flow lookup (contains)
   */
  private void createFlowSheet(final HSSFSheet sheet, final String flowPattern) {
    final List<FlowType> flowList = this.provider.getFlow(flowPattern);

    int rownum = 0;
    HSSFRow row = sheet.createRow(rownum);

    this.createCell(row, 0, "Flow", this.headerStyle);
    this.createCell(row, 1, "Direction", this.headerStyle);
    this.createCell(row, 2, "Input/Output", this.headerStyle);
    this.createCell(row, 3, "Type", this.headerStyle);
    this.createCell(row, 4, "Processing Step", this.headerStyle);
    this.createCell(row, 5, "Description", this.headerStyle);
    this.createCell(row, 6, "Mapping Input Types", this.headerStyle);
    rownum++;

    this.setColumnWidth(sheet, 0, 40);
    this.setColumnWidth(sheet, 1, 9);
    this.setColumnWidth(sheet, 2, 50);
    this.setColumnWidth(sheet, 3, 10);
    this.setColumnWidth(sheet, 4, 30);
    this.setColumnWidth(sheet, 5, 45);
    this.setColumnWidth(sheet, 6, 80);

    // sheet.getcolumn
    for (final FlowType flow : flowList) {
      final List<InterfaceMappingType> interfaceTypes = this.provider.getFlowMapping(flow.getId());
      final String flowName = flow.getShortName();
      row = sheet.createRow(rownum);
      this.createCell(row, 0, flow.getShortName(), this.bodyStyle);

      // Combination of the Flow messages
      flow.getInvokeFlowDefinition();

      final FlowProcessType exReq = flow.getExecuteRequest();
      if (Objects.nonNull(exReq)) {
        // Request response in
        // Flow in type, flow out type UUID
        final List<FlowStepInfo> exReqFlowInfo = new ArrayList<>();
        // Rule
        final List<FlowFilterType> exReqfilter = exReq.getFilter();
        this.addFlowFilter(exReqFlowInfo, exReqfilter, DIR_FLOW_IN);
        final List<FlowRuleType> exReqRule = exReq.getRule();
        this.addFlowRule(exReqFlowInfo, exReqRule, DIR_FLOW_IN);
        final List<FlowMapInOutType> exReqmap = exReq.getMapInOut();
        this.addFlowMapping(exReqFlowInfo, exReqmap, DIR_FLOW_IN, interfaceTypes);
        final List<FlowSubRequestType> exReqsub = exReq.getSubRequest();
        this.addFlowSubRequest(exReqFlowInfo, exReqsub, DIR_FLOW_IN);

        rownum = this.sortAndAddFlowStepInfo(exReqFlowInfo, exReq.getExecutionOrder(), sheet,
            flowName, rownum);
      }

      final FlowProcessType procRes = flow.getProcessResponse();
      if (Objects.nonNull(procRes)) {
        // Request response out
        // Flow in type, flow out type UUID
        final List<FlowStepInfo> procResFlowInfo = new ArrayList<>();
        // Rule
        final List<FlowFilterType> procResfilter = procRes.getFilter();
        this.addFlowFilter(procResFlowInfo, procResfilter, DIR_FLOW_OUT);
        final List<FlowRuleType> procResRule = procRes.getRule();
        this.addFlowRule(procResFlowInfo, procResRule, DIR_FLOW_OUT);
        final List<FlowMapInOutType> procResmap = procRes.getMapInOut();
        this.addFlowMapping(procResFlowInfo, procResmap, DIR_FLOW_OUT, interfaceTypes);
        final List<FlowSubRequestType> procRessub = procRes.getSubRequest();
        this.addFlowSubRequest(procResFlowInfo, procRessub, DIR_FLOW_OUT);

        rownum = this.sortAndAddFlowStepInfo(procResFlowInfo, procRes.getExecutionOrder(), sheet,
            flowName, rownum);
      }
    }
  }

  /**
   * @return header style
   */
  private HSSFCellStyle createHeaderStyle() {
    final HSSFCellStyle style = this.wb.createCellStyle();
    final Font boldFont = this.wb.createFont();
    boldFont.setFontHeightInPoints((short) 10);
    boldFont.setFontName("Arial");
    boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    boldFont.setItalic(false);
    style.setFont(boldFont);
    style.setWrapText(true);
    return style;
  }

  /**
   * @param sheet
   * @param flowPattern
   */
  private void createInterfaceMappingSheet(final HSSFSheet sheet,
      final List<String> interfaceMappingPackagePatterns) {
    final List<InterfaceMappingType> interfaceTypes = new ArrayList<>();
    interfaceMappingPackagePatterns.stream()
        .forEach(p -> interfaceTypes.addAll(this.provider.getInterfaceMappings(p)));
    int rownum = 0;
    HSSFRow row = sheet.createRow(rownum);

    this.createCell(row, 0, "InterfaceType", this.headerStyle);
    this.createCell(row, 1, "Package", this.bodyStyle);
    this.createCell(row, 2, "Field", this.headerStyle);
    this.createCell(row, 3, "Field Cardinality", this.headerStyle);
    this.createCell(row, 4, "Field Description", this.headerStyle);
    this.createCell(row, 5, "Mapping", this.headerStyle);
    this.createCell(row, 6, "Mapping Type", this.headerStyle);
    this.createCell(row, 7, "Description", this.headerStyle);
    this.createCell(row, 8, "Mapping Input Types", this.headerStyle);
    this.createCell(row, 9, "FQN", this.bodyStyle);

    this.setColumnWidth(sheet, 0, 30);
    this.setColumnWidth(sheet, 1, 3);
    this.setColumnWidth(sheet, 2, 15);
    this.setColumnWidth(sheet, 3, 5);
    this.setColumnWidth(sheet, 4, 45);
    this.setColumnWidth(sheet, 5, 30);
    this.setColumnWidth(sheet, 6, 8);
    this.setColumnWidth(sheet, 7, 45);
    this.setColumnWidth(sheet, 8, 80);
    this.setColumnWidth(sheet, 9, 3);

    final TreeSet<String> interfaceTypeNames = new TreeSet<>();
    rownum++;

    for (final InterfaceMappingType interfaceMapping : interfaceTypes) {
      if (!interfaceTypeNames.contains(interfaceMapping.getJavaClassName())) {
        final List<FieldType> fields = interfaceMapping.getFieldMappings();
        for (final FieldType field : fields) {
          row = sheet.createRow(rownum);
          this.createCell(row, 0, interfaceMapping.getShortName(), this.bodyStyle);
          this.createCell(row, 1, interfaceMapping.getJavaPackageName(), this.bodyStyle);
          this.createCell(row, 2, field.getName(), this.bodyStyle);
          this.createCell(row, 3, field.getCardinality(), this.bodyStyle);
          this.createCell(row, 4, field.getDescription(), this.wordWrapStyle);

          final String fieldDefinitionID = field.getFieldTypeDefinitionId();
          final List<FieldMappingType> fieldDefinitionList =
              this.provider.getFieldMapping(fieldDefinitionID);
          if (fieldDefinitionList.size() > 0) {
            final FieldMappingType mapping = fieldDefinitionList.get(0);
            this.createCell(row, 5, mapping.getShortName(), this.bodyStyle);
            this.createCell(row, 6, mapping.getMappingType(), this.wordWrapStyle);
            this.createCell(row, 7, mapping.getDescription(), this.wordWrapStyle);
            final StringBuffer mappingInputTypes = new StringBuffer(512);
            this.getComplexTypeNames(mapping.getFieldMappingInputType()).stream()
                .forEach(s -> mappingInputTypes.append(s));
            this.createCell(row, 8, mappingInputTypes.toString().trim(), this.wordWrapStyle);
          } else {
            final InterfaceMappingType fieldInterface =
                this.provider.getInterfaceMapping(fieldDefinitionID);
            if (Objects.nonNull(fieldInterface)) {
              this.createCell(row, 5, fieldInterface.getShortName(), this.bodyStyle);
              this.createCell(row, 6, "Interface", this.bodyStyle);
              this.createCell(row, 7, fieldInterface.getDescription(), this.wordWrapStyle);
            }
          }
          this.createCell(row, 9, interfaceMapping.getJavaClassName(), this.bodyStyle);
          rownum++;
        }
      }
    }

  }

  /**
   * @param sheet
   * @param flowPattern
   */
  private void createInterfaceMappingSheet(final HSSFSheet sheet, final String flowPattern) {
    final List<FlowType> flowList = this.provider.getFlow(flowPattern);
    int rownum = 0;
    HSSFRow row = sheet.createRow(rownum);

    this.createCell(row, 0, "Flow", this.headerStyle);
    this.createCell(row, 1, "Interface Type", this.headerStyle);
    this.createCell(row, 2, "Field", this.headerStyle);
    this.createCell(row, 3, "Field Cardinality", this.headerStyle);
    this.createCell(row, 4, "Field Description", this.headerStyle);
    this.createCell(row, 5, "Mapping", this.headerStyle);
    this.createCell(row, 6, "Mapping Type", this.headerStyle);
    this.createCell(row, 7, "Description", this.headerStyle);
    this.createCell(row, 8, "Mapping Input Types", this.headerStyle);

    this.setColumnWidth(sheet, 0, 30);
    this.setColumnWidth(sheet, 1, 30);
    this.setColumnWidth(sheet, 2, 15);
    this.setColumnWidth(sheet, 3, 5);
    this.setColumnWidth(sheet, 4, 45);
    this.setColumnWidth(sheet, 5, 30);
    this.setColumnWidth(sheet, 6, 8);
    this.setColumnWidth(sheet, 7, 45);
    this.setColumnWidth(sheet, 8, 80);

    final TreeSet<String> interfaceTypeNames = new TreeSet<>();
    rownum++;
    for (final FlowType flow : flowList) {
      final List<InterfaceMappingType> interfaceTypes = this.provider.getFlowMapping(flow.getId());

      for (final InterfaceMappingType interfaceMapping : interfaceTypes) {
        if (!interfaceTypeNames.contains(interfaceMapping.getJavaClassName())) {
          final List<FieldType> fields = interfaceMapping.getFieldMappings();
          for (final FieldType field : fields) {
            row = sheet.createRow(rownum);
            this.createCell(row, 0, flow.getShortName(), this.bodyStyle);
            this.createCell(row, 1, interfaceMapping.getShortName(), this.bodyStyle);
            this.createCell(row, 2, field.getName(), this.bodyStyle);
            this.createCell(row, 3, field.getCardinality(), this.bodyStyle);
            this.createCell(row, 4, field.getDescription(), this.bodyStyle);

            final String fieldDefinitionID = field.getFieldTypeDefinitionId();
            final List<FieldMappingType> fieldDefinitionList =
                this.provider.getFieldMapping(fieldDefinitionID);
            if (fieldDefinitionList.size() > 0) {
              final FieldMappingType mapping = fieldDefinitionList.get(0);
              this.createCell(row, 5, mapping.getShortName(), this.bodyStyle);
              this.createCell(row, 6, mapping.getMappingType(), this.bodyStyle);
              this.createCell(row, 7, mapping.getDescription(), this.bodyStyle);
              final StringBuffer mappingInputTypes = new StringBuffer(512);
              this.getComplexTypeNames(mapping.getFieldMappingInputType()).stream()
                  .forEach(s -> mappingInputTypes.append(s));
              this.createCell(row, 8, mappingInputTypes.toString().trim(), this.bodyStyle);
            }
            rownum++;
          }
        }
      }
    }
  }

  /**
   * Create a Sheet for a Service Service - Service name Operation - Operation
   * name Request/Response - Message type; in request or out response
   * Description - Message description Elements - Elements of the Message Flow
   * link - a flow invoked by the Service operation
   *
   * @param sheet
   * @param services
   *            List of Service UUIDs
   * @return Set of UUIDS of the service used complex types
   */
  private void createServiceSheet(final HSSFSheet sheet, final List<String> services,
      final String operationPattern, final Set<String> ctIds) {
    int rownum = 0;
    HSSFRow row = sheet.createRow(rownum);

    this.createCell(row, 0, "Package name", this.headerStyle);
    this.createCell(row, 1, "Service", this.headerStyle);
    this.createCell(row, 2, "Operation", this.headerStyle);
    this.createCell(row, 3, "Request/Response", this.headerStyle);
    this.createCell(row, 4, "Description", this.headerStyle);
    this.createCell(row, 5, "Elements", this.headerStyle);
    this.createCell(row, 6, "TargetNamespace", this.headerStyle);
    rownum++;

    this.setColumnWidth(sheet, 0, 10);
    this.setColumnWidth(sheet, 1, 25);
    this.setColumnWidth(sheet, 2, 40);
    this.setColumnWidth(sheet, 3, 10);
    this.setColumnWidth(sheet, 4, 55);
    this.setColumnWidth(sheet, 5, 65);
    this.setColumnWidth(sheet, 6, 30);

    for (final String service : services) {
      final ServiceType serviceT = this.provider.getService(service);
      final List<OperationType> operationTL = serviceT.getOperation();
      final String serviceName = serviceT.getName();
      String operation = "";
      String description = "";
      String fields = "";
      for (final OperationType operationT : operationTL) {
        if (Objects.isNull(operationPattern) || operationPattern.trim().equals("")
            || operationT.getShortName().matches(operationPattern)) {
          operation = operationT.getShortName();
          description = operationT.getRequestFieldDescription();

          final RequestResponseDataType requestResponseT = operationT.getRequestResponse();

          final ElementType elementTREQ = this.provider.getElement(requestResponseT.getRequestId());
          if (Objects.nonNull(elementTREQ)) {
            final ComplexType complexTREQ = this.getComplexType(elementTREQ.getComplexTypeId());
            if (Objects.nonNull(complexTREQ)) {
              fields = this.getFieldList(complexTREQ);
              ctIds.addAll(this.getElements(complexTREQ));

              row = sheet.createRow(rownum);
              this.createCell(row, 0, complexTREQ.getJavaPackageName(), this.bodyStyle);
              this.createCell(row, 1, serviceName, this.bodyStyle);
              this.createCell(row, 2, operation, this.bodyStyle);
              this.createCell(row, 3, "Request", this.bodyStyle);
              this.createCell(row, 4, description, this.wordWrapStyle);
              this.createCell(row, 5, fields, this.wordWrapStyle);
              this.createCell(row, 6, complexTREQ.getNamespace(), this.bodyStyle);
              rownum++;
            }
          }
          final ElementType elementTRES =
              this.provider.getElement(requestResponseT.getResponseId());
          if (Objects.nonNull(elementTRES)) {
            final ComplexType complexTRES = this.getComplexType(elementTRES.getComplexTypeId());
            if (Objects.nonNull(complexTRES)) {
              fields = this.getFieldList(complexTRES);
              ctIds.addAll(this.getElements(complexTRES));
              description = operationT.getResponseFieldDescription();

              row = sheet.createRow(rownum);
              this.createCell(row, 0, complexTRES.getJavaPackageName(), this.bodyStyle);
              this.createCell(row, 1, serviceName, this.bodyStyle);
              this.createCell(row, 2, operation, this.bodyStyle);
              this.createCell(row, 3, "Response", this.bodyStyle);
              this.createCell(row, 4, description, this.bodyStyle);
              this.createCell(row, 5, fields, this.bodyStyle);
              this.createCell(row, 6, complexTRES.getNamespace(), this.bodyStyle);
              rownum++;
            }
          }
        }
      }
    }
  }

  /**
   * Create sheet
   *
   * @param name
   * @return
   */
  private HSSFSheet createSheet(final String name) {
    return this.wb.createSheet(name);
  }

  private ComplexType getComplexType(final String ctId) {
    final ComplexType ct = this.provider.getComplexType(ctId);
    if (Objects.nonNull(ct) && Objects.nonNull(ct.getId()) && ComplexType.class.isInstance(ct)) {
      this.ctIdMap.put(String.format("%s%s", this.getRealShortName(ct), ct.getName()), ct.getId());
    }
    return ct;
  }

  private String getComplexTypeFieldElements(final ComplexType ct) {
    String value = null;
    if (ct != null) {
      final StringBuffer sb = new StringBuffer(256);
      sb.append(this.getRealShortName(ct)).append("\n");
      ct.getField().stream().forEach(f -> {
        final DataType dt = this.getDataType(f.getFieldTypeDefinitionId());
        if (Objects.nonNull(dt)) {
          sb.append(String.format("\t(%s:%s%s)\n", f.getName(), this.getRealShortName(dt),
              f.getCardinality()));
        } else {
          sb.append(String.format("\t(%s:Type?%s)\n", f.getName(), f.getCardinality()));
        }
      });
      value = sb.toString();
    }
    return value;
  }

  private void getComplexTypeIdsChildren(final Collection<String> ctIds) {
    ctIds.remove(null);
    final Set<String> childrenIds = new HashSet<>();
    this.getComplexTypes(ctIds).stream().filter(ct -> Objects.nonNull(ct))
        .forEach(ct -> ct.getField().stream().filter(ft -> Objects.nonNull(ft))
            .filter(ft -> Objects.nonNull(ft.getFieldTypeDefinitionId()))
            .filter(ft -> !ctIds.contains(ft) && !ctIds.contains(ft.getFieldTypeDefinitionId()))
            .forEach(ft -> childrenIds.add(ft.getFieldTypeDefinitionId())));
    if (!ctIds.isEmpty()) {
      childrenIds.removeAll(ctIds);
    }
    if (!childrenIds.isEmpty()) {
      this.getComplexTypeIdsChildren(childrenIds);
    }
  }

  private void getComplexTypeIdsParents(final Collection<String> ctIds) {
    final List<String> descendendIds = this.getComplexTypes(ctIds).stream()
        .filter(ct -> Objects.nonNull(ct) && Objects.nonNull(ct.getDescendedFromId()))
        .map(ct -> ct.getDescendedFromId()).collect(Collectors.toList());
    descendendIds.removeAll(this.ctIdMap.values());
    if (!descendendIds.isEmpty()) {
      this.getComplexTypeIdsParents(descendendIds);
      final Collection<String> childrenIds = new HashSet<>();
      this.getComplexTypes(ctIds).stream().filter(ct -> Objects.nonNull(ct))
          .forEach(ct -> ct.getField().stream()
              .filter(ft -> Objects.nonNull(ft.getFieldTypeDefinitionId()))
              .forEach(ft -> childrenIds.add(ft.getFieldTypeDefinitionId())));
      childrenIds.removeAll(this.ctIdMap.values());
      if (childrenIds.size() > 0) {
        this.getComplexTypeIdsChildren(childrenIds);
      }
    }
  }

  /**
   * Get the Ids of the ComplexTypes used.
   *
   * @return
   */
  public Collection<String> getComplexTypeIdsUsed() {
    this.getComplexTypeIdsChildren(this.ctIdMap.values());
    this.getComplexTypeIdsParents(this.ctIdMap.values());
    final TreeMap<String, String> sortedMap = new TreeMap<>();
    sortedMap.putAll(this.ctIdMap);
    return sortedMap.values();
  }

  private Set<String> getComplexTypeNames(final List<String> ctIds) {
    final Set<String> value = new TreeSet<>();
    this.getComplexTypes(ctIds).stream().forEach(ct -> value
        .add(String.format("%s{%s}\n", this.getRealShortName(ct), ct.getNamespace()).toString()));
    return value;
  }

  private void getComplexTypeNamesParents(final ComplexType ct, final List<String> descendends) {
    if (Objects.nonNull(ct) && Objects.nonNull(ct.getDescendedFromId())) {
      final DataType dt = this.getDataType(ct.getDescendedFromId());
      if (Objects.nonNull(dt)) {
        descendends.add(0, String.format("%s {%s}", this.getRealShortName(dt), dt.getNamespace()));
        if (ComplexType.class.isInstance(dt)) {
          this.getComplexTypeNamesParents((ComplexType) dt, descendends);
        }
      }
    }
  }

  private List<ComplexType> getComplexTypes(final Collection<String> ids) {
    final List<ComplexType> cts = this.provider.getComplexTypes(ids);
    cts.stream().filter(ct -> Objects.nonNull(ct) && Objects.nonNull(ct.getId())).forEach(ct -> {
      final String key = String.format("%s%s", this.getRealShortName(ct), ct.getName());
      this.ctIdMap.put(key, ct.getId());
    });
    return cts;
  }

  private DataType getDataType(final String id) {
    final DataType dt = this.provider.getDataType(id);
    if (Objects.nonNull(dt) && Objects.nonNull(dt.getId()) && ComplexType.class.isInstance(dt)) {
      this.ctIdMap.put(String.format("%s%s", this.getRealShortName(dt), dt.getName()), dt.getId());
    }
    return dt;
  }

  private List<DataType> getDataTypes(final List<String> ids) {
    final List<DataType> dts = this.provider.getDataTypes(ids);
    dts.stream()
        .filter(dt -> Objects.nonNull(dt) && Objects.nonNull(dt.getId())
            && ComplexType.class.isInstance(dt))
        .forEach(ct -> this.ctIdMap
            .put(String.format("%s%s", this.getRealShortName(ct), ct.getName()), ct.getId()));

    return dts;
  }

  /**
   * Get a List of elements of a Complex Type
   *
   * @param ct
   *            Complex Type
   * @return List of element types
   */
  private List<String> getElements(final ComplexType ct) {
    final List<String> value = new ArrayList<>();
    final List<FieldType> fields = ct.getField();
    this.getDataTypes(
        fields.stream().map(f -> f.getFieldTypeDefinitionId()).collect(Collectors.toList()));
    fields.stream().forEach(f -> {
      final DataType dt = this.getDataType(f.getFieldTypeDefinitionId());
      if (Objects.nonNull(dt)) {
        value.add(dt.getId());
      }
    });
    return value;
  }

  /**
   * Get a description of all field of a ComplexType
   *
   * @param ct
   *            ComplexType
   * @return (name: type cardinality)
   */
  private String getFieldList(final ComplexType ct) {
    final List<String> value = new ArrayList<>();
    final List<FieldType> fields = ct.getField();
    this.getDataTypes(
        fields.stream().map(f -> f.getFieldTypeDefinitionId()).collect(Collectors.toList()));
    fields.stream().forEach(f -> {
      final DataType dt = this.getDataType(f.getFieldTypeDefinitionId());
      if (Objects.nonNull(dt)) {
        value.add(String.format("(%s:%s%s{%s})", f.getName(), f.getCardinality(),
            this.getRealShortName(dt), dt.getNamespace()));
      } else {
        value.add(String.format("(%s:Type?%s)", f.getName(), f.getCardinality()));
      }
    });
    return value.stream().collect(Collectors.joining("\n"));
  }

  /**
   * Get the file name of the report.
   *
   * @param baseName
   * @return <datetime>-<baseName>-rev<revision>.xls
   */
  public String getFileName(final String baseName) {
    return String.format("target%s%s-rev%s.xls", File.separator, baseName,
        this.provider.getRevision());
  }

  /**
   * @return the {@link CellStyle} supporting 4 digit frequencies.
   */
  public CellStyle getFrequencyCellStyle() {
    final CellStyle value = this.wb.createCellStyle();
    value.setBorderBottom(CellStyle.BORDER_THIN);
    value.setBorderTop(CellStyle.BORDER_THIN);
    value.setBorderRight(CellStyle.BORDER_THIN);
    value.setBorderLeft(CellStyle.BORDER_THIN);
    value.setDataFormat(this.wb.createDataFormat().getFormat("0.0000"));
    return value;
  }

  private String getRealShortName(final DataType dt) {
    return dt.getName().replace(dt.getNamespace(), "").replace('{', ' ').replace('}', ' ')
        .replaceAll(" ", "");
  }

  /**
   * @param serviceNames
   * @return
   */

  private List<ServiceType> getService(final List<String> serviceNames) {
    final List<ServiceType> services = new ArrayList<>();
    for (final String s : serviceNames) {
      services.add(this.provider.getService(s));
    }
    return services;
  }

  /**
   * @return the webserviceClient
   */
  public DomainDocProvider getWebserviceClient() {
    return this.provider;
  }

  protected void setColumnWidth(final HSSFSheet sheet, final int columnIndex,
      final int excelColumnWidth) {
    if (excelColumnWidth > 0) {
      sheet.setColumnWidth(columnIndex, excelColumnWidth * 256 + 200);
    }
  }

  /**
   * Use the Flow execution order information to sort the FlowStepInfo objects
   * and add a row to the sheet
   *
   * @param procResFlowInfo
   *            Flow Steps (unsorted)
   * @param executionOrder
   *            List of UUIDs of Flow Steps in order of execution
   * @param sheet
   *            Excel Sheet
   * @param flowName
   *            Name of the Flow
   * @param rownum
   *            Start row number in the sheet
   * @return row number after the insertion of the Flow information in the
   *         sheet
   */
  private int sortAndAddFlowStepInfo(final List<FlowStepInfo> procResFlowInfo,
      final List<String> executionOrder, final HSSFSheet sheet, final String flowName, int rownum) {
    final HashMap<String, FlowStepInfo> steps = new HashMap<>();

    for (final FlowStepInfo stepInfo : procResFlowInfo) {
      steps.put(stepInfo.UUID, stepInfo);
    }

    for (final String key : executionOrder) {
      this.addFlowRow(sheet, rownum, flowName, steps.get(key));
      rownum++;
    }
    return rownum;
  }

  /**
   * Check out a Flow Definition
   *
   * @param flow
   *            name of the Flow
   */
  private void walkFlow(final String flow) {
    final List<FlowType> flowList = this.provider.getFlow(flow);

    // Flows
    flowList.forEach(f -> {

      System.out.println("FLOW  " + f.getShortName() + " " + f.getDescription());
      final String flowUUID = f.getId();

      final List<InterfaceMappingType> interfaceTypes = this.provider.getFlowMapping(flowUUID);

      // Interface Types in the Flow
      interfaceTypes.forEach(interfaceType -> {
        System.out.println(
            "INTF     " + interfaceType.getShortName() + " " + interfaceType.getDescription());

        final List<FieldType> fields = interfaceType.getFieldMappings();

        // Interface Type Fields
        fields.forEach(field -> {
          System.out.println("FIELD       " + field.getName() + " " + field.getDescription());

          final String fieldDefinitionID = field.getFieldTypeDefinitionId();

          final List<FieldMappingType> fieldDefinitionList =
              this.provider.getFieldMapping(fieldDefinitionID);

          // Field Mapping
          fieldDefinitionList.forEach(fieldDefinition -> {
            System.out.println(
                "FDEF       " + fieldDefinition.getName() + " " + fieldDefinition.getDescription());

            // Field Mapping return value
            System.out.println("FDEF RET  " + fieldDefinition.getReturnValueTypeId());

            final List<FieldType> mapperInputFields = fieldDefinition.getInput();

            // Field Mapping Input
            mapperInputFields.forEach(mapperInputFieled -> {
              System.out.println("MAPP         " + mapperInputFieled.getName() + " "
                  + mapperInputFieled.getDescription());
            });

          });

        });

      });

    });

  }

  /**
   * Check out a Service
   *
   * @param serviceName
   *            name
   */
  private void walkService(final String serviceName) {
    final ServiceType serviceT = this.provider.getService(serviceName);
    System.out.println("SRV " + serviceT.getName() + " " + serviceT.getDescription());
    final List<OperationType> operationTL = serviceT.getOperation();

    for (final OperationType operationT : operationTL) {

      System.out.println("OP    " + operationT.getShortName());

      final RequestResponseDataType requestResponseT = operationT.getRequestResponse();
      final ElementType elementTREQ = this.provider.getElement(requestResponseT.getRequestId());
      final ComplexType complexTREQ = this.getComplexType(elementTREQ.getComplexTypeId());
      System.out.println("OPREQ      " + operationT.getRequestFieldDescription());

      final List<FieldType> reqFields = complexTREQ.getField();

      for (final FieldType freq : reqFields) {
        final DataType freqD = this.getDataType(freq.getFieldTypeDefinitionId());
        String freqDShortName = "Type?";
        if (Objects.nonNull(freqD)) {
          freqDShortName = this.getRealShortName(freqD);
        }
        System.out.println("OPREQFIELD      " + freq.getName() + " " + freq.getCardinality() + " "
            + freqDShortName);
      }

      final ElementType elementTRES = this.provider.getElement(requestResponseT.getResponseId());
      final ComplexType complexTRES = this.getComplexType(elementTRES.getComplexTypeId());
      System.out.println("OPRES      " + operationT.getResponseFieldDescription());

      final List<FieldType> resFields = complexTRES.getField();

      for (final FieldType fres : resFields) {
        final DataType fresD = this.getDataType(fres.getFieldTypeDefinitionId());
        String fresDShortName = "Type?";
        if (Objects.nonNull(fresD)) {
          fresDShortName = this.getRealShortName(fresD);
        }
        System.out.println("OPRESFIELD      " + fres.getName() + " " + fres.getCardinality() + " "
            + fresDShortName);
      }

    }

  }

  /**
   * Write workbook
   *
   * @param baseName
   */
  public void writeWorkbook(final String baseName) {
    final String fileName = this.getFileName(baseName);
    // Write the output to a file
    FileOutputStream fileOut = null;
    try {
      fileOut = new FileOutputStream(fileName);
    } catch (final FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try {
      this.wb.write(fileOut);
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      fileOut.close();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
