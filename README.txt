/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/

The EIP (Enterprise Integration Platform) is a consulting tool that allows us to efficiently set up SOA (Service Oriented Architecture) and  MDM (Master Data Management) projects. 
The software is build on our experience that is transformed into design pattern on top of a stable and reliable software stack including VMWare vFabric and Spring Source projects.

Excute the following steps to build the QPark EIP:
1. mvn install -P bom
2. mvn install -P platform,impl,sample

Maven min version 3.5.4
JDK min version JDK 11 (LTS)
EIP is now compatible with the java versions up to 15. Please be aware, future EIP versions will drop the support of java versions prior to 11. 
Java 1.8 to 10 are deprecated now.

From time to time it happens, that the following is reported:
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-dependency-plugin:2.10:unpack (unpack-sources) on project platform-domain-model-service: Unable to create Marker: C:\xnb\dev\git\EIP\qp-enterprise-integration-platform-sample\sample\platform-domain-model-service\target\dependency-maven-plugin-markers\com.samples.domain-model-jar-2.1.0.marker: Zugriff verweigert -> [Help 1]
In this case just go on further as proposed applying the maven -rf option.

About QPark:

QPark Consulting helps companies make distinctive, lasting and sustainable improvements in performance by using IT as an efficient tool.

We are organized in functional practices.

QPark is Luxembourg based and we are in just a few minutes on site. 
