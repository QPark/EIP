//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.15 at 03:50:39 PM CEST 
//


package com.qpark.eip.service.domain.doc.msg;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.qpark.eip.model.docmodel.InterfaceMappingType;


/**
 * <p>Java class for GetFlowInterfaceMappingTypeResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetFlowInterfaceMappingTypeResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="interfaceType" type="{http://www.qpark-consulting.com/EIP/Utility/DocumentationModel}InterfaceMappingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetFlowInterfaceMappingTypeResponseType", propOrder = {
    "interfaceType"
})
public class GetFlowInterfaceMappingTypeResponseType {

    protected List<InterfaceMappingType> interfaceType;

    /**
     * Gets the value of the interfaceType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interfaceType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterfaceType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InterfaceMappingType }
     * 
     * 
     */
    public List<InterfaceMappingType> getInterfaceType() {
        if (interfaceType == null) {
            interfaceType = new ArrayList<InterfaceMappingType>();
        }
        return this.interfaceType;
    }

}
