/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.jaxb.doc.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.qpark.eip.model.docmodel.ComplexType;
import com.qpark.eip.model.docmodel.DataType;
import com.qpark.eip.model.docmodel.ElementType;
import com.qpark.eip.model.docmodel.EnterpriseType;
import com.qpark.eip.model.docmodel.FieldMappingType;
import com.qpark.eip.model.docmodel.FlowType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;
import com.qpark.eip.model.docmodel.ServiceType;

/**
 * @author bhausen
 */
public interface DomainDocProvider {
  /**
   * @param fileName the name of the file.
   * @return the content.
   */
  public static byte[] readFile(final String fileName) {
    byte[] value = null;
    try {
      value = java.nio.file.Files.readAllBytes(new File(fileName).toPath());
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return value;
  }

  /**
  * @return the object.
  * @throws JAXBException
  */
  public static Optional<EnterpriseType> getEnterpriseType() throws JAXBException {
    try (InputStream inputSteam =
        new FileInputStream(new File(new File(new File("target"), "html-description"),
            "com.samples.bus-ModelAnalysis.xml"));) {
      final Unmarshaller um =
          JAXBContext.newInstance("com.qpark.eip.model.docmodel").createUnmarshaller();
      Object o = um.unmarshal(inputSteam);
      if (o != null && JAXBElement.class.isInstance(o)) {
        o = ((JAXBElement<?>) o).getValue();
      }
      return Optional.ofNullable((EnterpriseType) o);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  /**
   * Get ComplexType by UUID
   *
   * @param complexTypeUUID UUID
   * @return the ComplexType.
   */

  ComplexType getComplexType(String complexTypeUUID);

  /**
   * Get ComplexType by UUID
   *
   * @param complexTypeUUIDs UUID
   * @return the  list of {@link ComplexType}s.
   */

  List<ComplexType> getComplexTypes(Collection<String> complexTypeUUIDs);

  /**
   * Get DataType by UUID
   *
   * @param id uuid
   * @return the {@link DataType}.
   */
  DataType getDataType(String id);

  /**
   * @param ids
   * @return the list of {@link DataType}s.
   */
  List<DataType> getDataTypes(List<String> ids);

  /**
   * Get Element by name
   *
   * @param elementId name
   * @return the {@link ElementType}.
   */

  ElementType getElement(String elementId);

  /**
   * Get FieldMappings by uuid
   * @param fieldDefinitionID uuid
   * @return list of {@link FieldMappingType}s.
   */
  List<FieldMappingType> getFieldMapping(String fieldDefinitionID);

  /**
   * @param pattern
   * @return the list of {@link FlowType}s.
   */
  List<FlowType> getFlow(String pattern);

  /**
   * Get {@link InterfaceMappingType}s by flow UUID
   *
   * @param flowUUID Flow UUID
   * @return the list of {@link InterfaceMappingType}
   */
  List<InterfaceMappingType> getFlowMapping(String flowUUID);

  /**
   * Get Flow by UUID
   *
   * @param interfaceMappingId Flow UUID
   * @return the {@link InterfaceMappingType}.
   */
  InterfaceMappingType getInterfaceMapping(String interfaceMappingId);

  /**
   * Get Flow by UUID
   *
   * @param packagePattern Flow UUID
   * @return the list of {@link InterfaceMappingType}s.
   */
  List<InterfaceMappingType> getInterfaceMappings(String packagePattern);

  /**
   * Get the name of the latest revision.
   * @return the revision name.
   */
  String getRevision();

  /**
   * Get Service by serviceId
   *
   * @param serviceId
   * @return the {@link ServiceType}.
   */
  ServiceType getService(String serviceId);

  /**
   * @return the list of service ids.
   */
  List<String> getServiceIds();
}
