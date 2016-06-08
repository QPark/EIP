package com.qpark.maven.plugin.flowmapper;

import java.io.File;

import org.apache.xmlbeans.SchemaType;

import com.qpark.maven.Util;
import com.qpark.maven.xmlbeans.ComplexType;
import com.qpark.maven.xmlbeans.ComplexTypeChild;
import com.qpark.maven.xmlbeans.XsdsUtil;

public class XsdDirectMapperGenerator {
	public static String toPropertyName(final String name) {
		String s = name;
		if (s != null && s.length() > 0) {
			StringBuffer sb = new StringBuffer(s.length());
			sb.append(s.substring(0, 1).toLowerCase());
			if (s.length() > 1) {
				sb.append(s.substring(1, s.length()));
			}
			s = sb.toString();
		}
		return s;
	}

	public static void main(final String[] args) {

		String basePackageName = "com.qpark.eip.inf";
		String xsdPath = "C:\\xnb\\dev\\xxxx\\domain-gen-flow\\target\\model";
		xsdPath = "C:\\xnb\\dev\\38\\com.ses.domain\\com.ses.domain.gen\\domain-gen-flow\\target\\model";
		File dif = new File(xsdPath);
		String messagePackageNameSuffix = "mapping flow";
		long start = System.currentTimeMillis();
		XsdsUtil xsds = XsdsUtil.getInstance(dif, "a.b.c.bus", messagePackageNameSuffix, "delta");
		ComplexContentList complexContentList = new ComplexContentList();
		complexContentList.setupComplexContentLists(xsds);
		System.out.println(Util.getDuration(System.currentTimeMillis() - start));

		String source = null;
		ComplexContent cc;
		SchemaType type;
		String ctNamespace;
		String ctName;
		String prefix;

		ctNamespace = "http://www.qp.com/CommonTypes-1.0";
		prefix = "OSPcty10";
		StringBuffer sb = new StringBuffer(512);
		for (ComplexType ct : xsds.getComplexTypes()) {
			if (ct.getTargetNamespace().equals(ctNamespace)) {
				ctName = ct.getClassName();
				if (ctName.endsWith("Type")) {
					ctName = ctName.substring(0, ctName.length() - 4);
				}
				for (ComplexTypeChild ctc : ct.getChildren()) {
					sb.append("\t<complexType name=\"");
					sb.append(ctName);
					sb.append(".");
					sb.append(ctc.getChildName());
					sb.append("MappingType\">\n");

					sb.append("\t\t<annotation><documentation>Direct mapping of ");
					sb.append(ct.getClassName());
					sb.append(" ");
					sb.append(ctc.getChildName());
					sb.append(".</documentation></annotation>\n");

					sb.append(
							"\t\t<complexContent>\n\t\t\t<extension base=\"MPmp:DirectMappingType\">\n\t\t\t\t<sequence>\n");
					sb.append("\t\t\t\t\t<element name=\"");
					sb.append(toPropertyName(ctName));
					sb.append("\" type=\"");
					sb.append(prefix);
					sb.append(":");
					sb.append(ct.getQNameLocalPart());
					sb.append("\" />\n");
					sb.append("\t\t\t\t\t<element name=\"return\" type=\"");
					if (ctc.getComplexType().getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
					} else {
						sb.append("OSPcty10:");
					}
					sb.append(ctc.getComplexType().getQNameLocalPart());
					sb.append("\" />\n");

					sb.append("\t\t\t\t</sequence>\n\t\t\t</extension>\n\t\t</complexContent>\n\t</complexType>\n");
				}
			}
		}
		System.out.println(sb.toString());
		// xsds.getComplexTypes().stream().filter(ComplexType::)
	}

}
