/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.flow.flows.doc.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qpark.eip.model.docmodel.EnterpriseType;

/**
 * @author bhausen
 */
public class DomainDocReport {
  /** The {@link org.slf4j.Logger}. */
  private static Logger logger = LoggerFactory.getLogger(DomainDocReport.class);


  /**
   * @param doc
   *            the {@link DocClient}.
   */
  public static void generateOrchestrationReport(final DocClient doc) {
    final String flowPattern = "com.samples.platform.inf.iss.tech.support.flow";
    final List<String> interfacePackagePattern = new ArrayList<>();
    interfacePackagePattern.add("com.samples.platform.inf.iss.tech.support.type");
    final List<String> services = new ArrayList<>();
    services.add("iss.tech.support");
    final String operationPattern = ".*";
    final String baseFileName = "Orchestration";
    generateReport(doc, flowPattern, interfacePackagePattern, services, operationPattern,
        baseFileName);
  }
  /**
   * @param doc
   * @param flowPattern
   * @param interfacePackagePattern
   * @param services
   * @param operationPattern
   * @param baseFileName
   */
  private static void generateReport(final DocClient doc, final String flowPattern,
      final List<String> interfacePackagePattern, final List<String> services,
      final String operationPattern, final String baseFileName) {
    logger.info("+generateReport {}", baseFileName);
    final Instant start = Instant.now();

    doc.addServiceSheet(services, operationPattern);
    doc.addFlowSheet(flowPattern);
    doc.addInterfaceMappingSheet(interfacePackagePattern);
    final Collection<String> ctIds = doc.getComplexTypeIdsUsed();
    doc.addDataTypeSheet(ctIds);
    doc.writeWorkbook(baseFileName);
    logger.info("-generateReport {} {}", baseFileName, Duration.between(start, Instant.now()));
  }


  /**
   * @param jaxb
   * @return the XML string of the value.
   * @throws Exception
   */
  public static <T> String getXmlString(final JAXBElement<T> jaxb) throws Exception {
    final JAXBContext jaxbContext =
        JAXBContext.newInstance(jaxb.getValue().getClass().getPackage().getName());
    final Marshaller m = jaxbContext.createMarshaller();
    m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final PrintWriter pw = new PrintWriter(baos, true);
    m.marshal(jaxb, pw);
    final String xml = new String(baos.toByteArray(), "UTF-8");
    // this.getLogger().debug(String.format("\n%s", xml));
    return xml;
  }

  /**
   * @param args
   */
  public static void main(final String[] args) {
    try {
      new DomainDocReport().run();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param file
   *            the of the file
   * @param bytes
   *            content to write.
   * @return the {@link Path} of the created file.
   */
  public static Path writeFile(final Path file, final byte[] bytes) {
    Path value = null;
    try {
      final Path directory = file.getParent();
      if (!directory.toFile().exists()) {
        directory.toFile().mkdirs();
      }
      value = java.nio.file.Files.write(file, bytes, StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return value;
  }

  /**
  * @throws Exception
  */
  void run() throws Exception {
    logger.info("+run");
    final Instant start = Instant.now();
    DomainDocProvider.getEnterpriseType().ifPresent(enterprise -> {
    	generateOrchestrationReport(new DocClient(new DomainDocProviderImpl(enterprise)));
    });
    logger.info("-run {}", Duration.between(start, Instant.now()));
  }
}
