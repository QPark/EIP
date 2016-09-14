-----------------------------------------------------------------------------
-- Create script of persistence unit com.samples.platform.domain.
-- Target database type: Oracle 10.1
-- Generated at 20160914-093031.226.
-----------------------------------------------------------------------------
-- Verify sequence handling at the end of the script

CREATE TABLE CLUSTERTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, DESCRIPTION VARCHAR2(2047) NULL, FILENAME VARCHAR2(511) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, PACKAGENAME VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, VERSION_ VARCHAR2(255) NULL, CLUSTER__DOMAINTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX CLSTRTYPCLSTRTYPCLSTRDMNTYPHJD ON CLUSTERTYPE (CLUSTER__DOMAINTYPE_HJID);
CREATE TABLE DATATYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, DESCRIPTION VARCHAR2(2047) NULL, ID VARCHAR2(36) NULL, JAVACLASSNAME VARCHAR2(511) NULL, JAVAPACKAGENAME VARCHAR2(511) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, SHORTNAME VARCHAR2(511) NULL, BASICDATATYPES_ENTERPRISETYP_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX DTTYPDTTYPBSCDTTYPSNTRPRSETYP0 ON DATATYPE (BASICDATATYPES_ENTERPRISETYP_0);
CREATE TABLE FIELDMAPPINGTYPE (HJID NUMBER(19) NOT NULL, MAPPINGTYPE VARCHAR2(255) NULL, RETURNVALUETYPEID VARCHAR2(36) NULL, PRIMARY KEY (HJID));
CREATE TABLE COMPLEXMAPPINGTYPE (HJID NUMBER(19) NOT NULL, COMPLEXMAPPINGTYPE_CLUSTERTY_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX CMPLXMPPNCMPLXMPPNGTYPCLSTRTY0 ON COMPLEXMAPPINGTYPE (COMPLEXMAPPINGTYPE_CLUSTERTY_0);
CREATE TABLE COMPLEXTYPE (HJID NUMBER(19) NOT NULL, DESCENDEDFROMID VARCHAR2(36) NULL, ISFLOWINPUTTYPE NUMBER(1) default 0 NULL, ISFLOWOUTPUTTYPE NUMBER(1) default 0 NULL, ISMAPPINGREQUESTTYPE NUMBER(1) default 0 NULL, ISMAPPINGRESPONSETYPE NUMBER(1) default 0 NULL, COMPLEXTYPE_CLUSTERTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX CMPCMPLXTYPCMPLXTYPCLSTRTYPHJD ON COMPLEXTYPE (COMPLEXTYPE_CLUSTERTYPE_HJID);
CREATE TABLE COMPLEXUUIDMAPPINGTYPE (HJID NUMBER(19) NOT NULL, CATEGORYNAMEORUUID VARCHAR2(255) NULL, COMPLEXUUIDMAPPINGTYPE_CLUST_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX CMPLXDMPPNGCMPLXDMPPNGTYPCLST0 ON COMPLEXUUIDMAPPINGTYPE (COMPLEXUUIDMAPPINGTYPE_CLUST_0);
CREATE TABLE DEFAULTMAPPINGTYPE (HJID NUMBER(19) NOT NULL, DEFAULTVALUE VARCHAR2(255) NULL, DEFAULTMAPPINGTYPE_CLUSTERTY_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX DFLTMPPNGTDFLTMPPNGTYPCLSTRTY0 ON DEFAULTMAPPINGTYPE (DEFAULTMAPPINGTYPE_CLUSTERTY_0);
CREATE TABLE DIRECTMAPPINGTYPE (HJID NUMBER(19) NOT NULL, ACCESSOR VARCHAR2(511) NULL, ACCESSORFIELDID VARCHAR2(36) NULL, DIRECTMAPPINGTYPE_CLUSTERTYP_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX DRCTMPPNGDRCTMPPNGTYPCLSTRTYP0 ON DIRECTMAPPINGTYPE (DIRECTMAPPINGTYPE_CLUSTERTYP_0);
CREATE TABLE DOMAINTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, DESCRIPTION VARCHAR2(2047) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, SUBNAME VARCHAR2(255) NULL, SUBSUBNAME VARCHAR2(255) NULL, SUBSUBSUBNAME VARCHAR2(255) NULL, DOMAINS_ENTERPRISETYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX DMNTYPDMNTYPDMNSNTRPRSTYPEHJID ON DOMAINTYPE (DOMAINS_ENTERPRISETYPE_HJID);
CREATE TABLE ELEMENTTYPE (HJID NUMBER(19) NOT NULL, COMPLEXTYPEID VARCHAR2(36) NULL, ELEMENTTYPE_CLUSTERTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX LMNTTLMNTTYPLMNTTYPCLSTRTYPHJD ON ELEMENTTYPE (ELEMENTTYPE_CLUSTERTYPE_HJID);
CREATE TABLE ENTERPRISETYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, PRIMARY KEY (HJID));
CREATE TABLE FIELDTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, CARDINALITY_ VARCHAR2(255) NULL, CARDINALITYMAXOCCURS NUMBER(10) NULL, CARDINALITYMINOCCURS NUMBER(10) NULL, DESCRIPTION VARCHAR2(2047) NULL, FIELDTYPEDEFINITIONID VARCHAR2(36) NULL, ID VARCHAR2(36) NULL, LISTFIELD NUMBER(1) default 0 NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, OPTIONALFIELD NUMBER(1) default 0 NULL, PARENTID VARCHAR2(36) NULL, SEQUENCENUMBER NUMBER(10) NULL, FIELD_COMPLEXTYPE_HJID NUMBER(19) NULL, INPUT__FIELDMAPPINGTYPE_HJID NUMBER(19) NULL, FIELDMAPPINGS_INTERFACEMAPPI_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX FLDTYPFLDTYPFLDCOMPLEXTYPEHJID ON FIELDTYPE (FIELD_COMPLEXTYPE_HJID);
CREATE INDEX FLDTYPFLDTYPFLDMPPNGSNTRFCMPP0 ON FIELDTYPE (FIELDMAPPINGS_INTERFACEMAPPI_0);
CREATE INDEX FLDTYPFLDTYPNPTFLDMPPNGTYPHJID ON FIELDTYPE (INPUT__FIELDMAPPINGTYPE_HJID);
CREATE TABLE FLOWFILTERTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, FILTERINFIELDDESCRIPTION VARCHAR2(2047) NULL, FILTEROUTFIELDDESCRIPTION VARCHAR2(2047) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, FILTERINOUT_FLOWFILTERTYPE_H_0 NUMBER(19) NULL, FILTER_FLOWPROCESSTYPE_HJID NUMBER(19) NULL, RULE__FLOWPROCESSTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX FLFLWFLTRTYPFLTRFLWPRCSSTYPHJD ON FLOWFILTERTYPE (FILTER_FLOWPROCESSTYPE_HJID);
CREATE INDEX FLFLWFLTRTYPFLTRNTFLWFLTRTYPH0 ON FLOWFILTERTYPE (FILTERINOUT_FLOWFILTERTYPE_H_0);
CREATE INDEX FLWFFLWFLTRTYPRLFLWPRCSSTYPHJD ON FLOWFILTERTYPE (RULE__FLOWPROCESSTYPE_HJID);
CREATE TABLE FLOWMAPINOUTTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, ID VARCHAR2(36) NULL, MAPINFIELDDESCRIPTION VARCHAR2(2047) NULL, MAPOUTFIELDDESCRIPTION VARCHAR2(2047) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, MAPINOUT_FLOWMAPINOUTTYPE_HJ_0 NUMBER(19) NULL, MAPINOUT_FLOWPROCESSTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX FLFLWMPNTTYPMPNTFLWPRCSSTYPHJD ON FLOWMAPINOUTTYPE (MAPINOUT_FLOWPROCESSTYPE_HJID);
CREATE INDEX FLWFLWMPNTTYPMPNTFLWMPNTTYPHJ0 ON FLOWMAPINOUTTYPE (MAPINOUT_FLOWMAPINOUTTYPE_HJ_0);
CREATE TABLE FLOWPROCESSTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, REQUESTFIELDDESCRIPTION VARCHAR2(2047) NULL, RESPONSEFIELDDESCRIPTION VARCHAR2(2047) NULL, REQUESTRESPONSE_FLOWPROCESST_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX FLWPRCSSTYPRQSTRSPNSFLWPRCSST0 ON FLOWPROCESSTYPE (REQUESTRESPONSE_FLOWPROCESST_0);
CREATE TABLE FLOWRULETYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, RULEINFIELDDESCRIPTION VARCHAR2(2047) NULL, RULEOUTFIELDDESCRIPTION VARCHAR2(2047) NULL, RULEINOUT_FLOWRULETYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX FLWRLTYFLWRLTYPRLNTFLWRLTYPHJD ON FLOWRULETYPE (RULEINOUT_FLOWRULETYPE_HJID);
CREATE TABLE FLOWSUBREQUESTTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, SUBREQUESTFIELDDESCRIPTION VARCHAR2(2047) NULL, SUBRESPONSEFIELDDESCRIPTION VARCHAR2(2047) NULL, SUBREQUESTINOUT_FLOWSUBREQUE_0 NUMBER(19) NULL, SUBREQUEST_FLOWPROCESSTYPE_H_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX FLFLWSBRQSTTYPSBRQSTNTFLWSBRQ0 ON FLOWSUBREQUESTTYPE (SUBREQUESTINOUT_FLOWSUBREQUE_0);
CREATE INDEX FLWSBRQSTTYSBRQSTFLWPRCSSTYPH0 ON FLOWSUBREQUESTTYPE (SUBREQUEST_FLOWPROCESSTYPE_H_0);
CREATE TABLE FLOWTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, CLUSTERID VARCHAR2(36) NULL, DESCRIPTION VARCHAR2(2047) NULL, ID VARCHAR2(36) NULL, INFIELDDESCRIPTION VARCHAR2(2047) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, OUTFIELDDESCRIPTION VARCHAR2(2047) NULL, PARENTID VARCHAR2(36) NULL, SHORTNAME VARCHAR2(511) NULL, EXECUTEREQUEST_FLOWTYPE_HJID NUMBER(19) NULL, INVOKEFLOWDEFINITION_FLOWTYP_0 NUMBER(19) NULL, PROCESSRESPONSE_FLOWTYPE_HJID NUMBER(19) NULL, FLOWS_ENTERPRISETYPE_HJID NUMBER(19) NULL, INVOKES_OPERATIONTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX FLWTYPFLWTYPXCTRQSTFLWTYPEHJID ON FLOWTYPE (EXECUTEREQUEST_FLOWTYPE_HJID);
CREATE INDEX FLWTYPFLWTYPNVKFLWDFNTNFLWTYP0 ON FLOWTYPE (INVOKEFLOWDEFINITION_FLOWTYP_0);
CREATE INDEX FLWTYPFLWTYPFLWSNTRPRSTYPEHJID ON FLOWTYPE (FLOWS_ENTERPRISETYPE_HJID);
CREATE INDEX FLWTYFLWTYPPRCSSRSPNSFLWTYPHJD ON FLOWTYPE (PROCESSRESPONSE_FLOWTYPE_HJID);
CREATE INDEX FLWTYPFLWTYPNVKSPRTIONTYPEHJID ON FLOWTYPE (INVOKES_OPERATIONTYPE_HJID);
CREATE TABLE INTERFACEMAPPINGTYPE (HJID NUMBER(19) NOT NULL, INTERFACEMAPPINGTYPE_CLUSTER_0 NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX NTRFCMPPNGTNTRFCMPPNGTYPCLSTR0 ON INTERFACEMAPPINGTYPE (INTERFACEMAPPINGTYPE_CLUSTER_0);
CREATE TABLE OPERATIONTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, REQUESTFIELDDESCRIPTION VARCHAR2(2047) NULL, RESPONSEFIELDDESCRIPTION VARCHAR2(2047) NULL, SECURITYROLENAME VARCHAR2(511) NULL, SHORTNAME VARCHAR2(511) NULL, REQUESTRESPONSE_OPERATIONTYP_0 NUMBER(19) NULL, OPERATION__SERVICETYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX PRTNTYPRTNTYPRQSTRSPNSPRTNTYP0 ON OPERATIONTYPE (REQUESTRESPONSE_OPERATIONTYP_0);
CREATE INDEX PRTNTYPPRTNTYPPRTNSRVCTYPEHJID ON OPERATIONTYPE (OPERATION__SERVICETYPE_HJID);
CREATE TABLE REQUESTRESPONSEDATATYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, REQUESTDESCRIPTION VARCHAR2(2047) NULL, REQUESTID VARCHAR2(36) NULL, RESPONSEDESCRIPTION VARCHAR2(2047) NULL, RESPONSEID VARCHAR2(36) NULL, PRIMARY KEY (HJID));
CREATE TABLE SERVICETYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, CLUSTERID VARCHAR2(36) NULL, DESCRIPTION VARCHAR2(2047) NULL, ID VARCHAR2(36) NULL, MODELVERSION VARCHAR2(127) NULL, NAME_ VARCHAR2(511) NULL, NAMESPACE VARCHAR2(511) NULL, PACKAGENAME VARCHAR2(511) NULL, PARENTID VARCHAR2(36) NULL, SECURITYROLENAME VARCHAR2(511) NULL, SERVICEID VARCHAR2(511) NULL, SERVICE_DOMAINTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX SRVCTYPSRVCTYPSRVCDMINTYPEHJID ON SERVICETYPE (SERVICE_DOMAINTYPE_HJID);
CREATE TABLE CLUSTERTYPE_WARNING (HJID NUMBER(19) NULL, HJVALUE VARCHAR2(2047) NULL, HJINDEX NUMBER(10) NULL);
CREATE INDEX CLSTRTYPWRNNFKCLSTRTYPWRNNGHJD ON CLUSTERTYPE_WARNING (HJID);
CREATE TABLE FLOWMAPINOUTTYPE_INTERFACEMA_0 (HJID NUMBER(19) NULL, HJVALUE VARCHAR2(36) NULL, HJINDEX NUMBER(10) NULL);
CREATE INDEX FLWMPNTTYPFLWMPNTTYPNTRFCM0HJD ON FLOWMAPINOUTTYPE_INTERFACEMA_0 (HJID);
CREATE TABLE FLOWPROCESSTYPE_EXECUTIONORD_0 (HJID NUMBER(19) NULL, HJVALUE VARCHAR2(36) NULL, HJINDEX NUMBER(10) NULL);
CREATE INDEX FLWPRCSSTFLWPRCSSTYPXCTNRD0HJD ON FLOWPROCESSTYPE_EXECUTIONORD_0 (HJID);
ALTER TABLE CLUSTERTYPE ADD CONSTRAINT CLSTERTYPECLSTERDOMAINTYPEHJID FOREIGN KEY (CLUSTER__DOMAINTYPE_HJID) REFERENCES DOMAINTYPE (HJID);
ALTER TABLE DATATYPE ADD CONSTRAINT DTTYPBSCDTATYPESENTERPRISETYP0 FOREIGN KEY (BASICDATATYPES_ENTERPRISETYP_0) REFERENCES ENTERPRISETYPE (HJID);
ALTER TABLE FIELDMAPPINGTYPE ADD CONSTRAINT FK_FIELDMAPPINGTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE COMPLEXMAPPINGTYPE ADD CONSTRAINT CMPLXMPPNCMPLXMPPNGTYPCLSTRTY0 FOREIGN KEY (COMPLEXMAPPINGTYPE_CLUSTERTY_0) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE COMPLEXMAPPINGTYPE ADD CONSTRAINT FK_COMPLEXMAPPINGTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE COMPLEXTYPE ADD CONSTRAINT CMPLXTYPCMPLXTYPCLSTERTYPEHJID FOREIGN KEY (COMPLEXTYPE_CLUSTERTYPE_HJID) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE COMPLEXTYPE ADD CONSTRAINT FK_COMPLEXTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE COMPLEXUUIDMAPPINGTYPE ADD CONSTRAINT CMPLXDMPPNGCMPLXDMPPNGTYPCLST0 FOREIGN KEY (COMPLEXUUIDMAPPINGTYPE_CLUST_0) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE COMPLEXUUIDMAPPINGTYPE ADD CONSTRAINT FK_COMPLEXUUIDMAPPINGTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE DEFAULTMAPPINGTYPE ADD CONSTRAINT DFLTMPPNGTDFLTMPPNGTYPCLSTRTY0 FOREIGN KEY (DEFAULTMAPPINGTYPE_CLUSTERTY_0) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE DEFAULTMAPPINGTYPE ADD CONSTRAINT FK_DEFAULTMAPPINGTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE DIRECTMAPPINGTYPE ADD CONSTRAINT DRCTMPPNGDRCTMPPNGTYPCLSTRTYP0 FOREIGN KEY (DIRECTMAPPINGTYPE_CLUSTERTYP_0) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE DIRECTMAPPINGTYPE ADD CONSTRAINT FK_DIRECTMAPPINGTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE DOMAINTYPE ADD CONSTRAINT DMNTYPEDMINSENTERPRISETYPEHJID FOREIGN KEY (DOMAINS_ENTERPRISETYPE_HJID) REFERENCES ENTERPRISETYPE (HJID);
ALTER TABLE ELEMENTTYPE ADD CONSTRAINT LMNTTYPLMNTTYPECLUSTERTYPEHJID FOREIGN KEY (ELEMENTTYPE_CLUSTERTYPE_HJID) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE ELEMENTTYPE ADD CONSTRAINT FK_ELEMENTTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE FIELDTYPE ADD CONSTRAINT FIELDTYPEFIELDCOMPLEXTYPE_HJID FOREIGN KEY (FIELD_COMPLEXTYPE_HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE FIELDTYPE ADD CONSTRAINT FLDTYPFLDMPPNGSINTERFACEMAPPI0 FOREIGN KEY (FIELDMAPPINGS_INTERFACEMAPPI_0) REFERENCES DATATYPE (HJID);
ALTER TABLE FIELDTYPE ADD CONSTRAINT FLDTYPENPTFIELDMAPPINGTYPEHJID FOREIGN KEY (INPUT__FIELDMAPPINGTYPE_HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE FLOWFILTERTYPE ADD CONSTRAINT FLWFLTRTYPFLTRFLWPRCSSTYPEHJID FOREIGN KEY (FILTER_FLOWPROCESSTYPE_HJID) REFERENCES FLOWPROCESSTYPE (HJID);
ALTER TABLE FLOWFILTERTYPE ADD CONSTRAINT FLWFLTRTYPFLTRNTFLWFLTERTYPEH0 FOREIGN KEY (FILTERINOUT_FLOWFILTERTYPE_H_0) REFERENCES REQUESTRESPONSEDATATYPE (HJID);
ALTER TABLE FLOWFILTERTYPE ADD CONSTRAINT FLWFLTRTYPERLFLWPRCESSTYPEHJID FOREIGN KEY (RULE__FLOWPROCESSTYPE_HJID) REFERENCES FLOWPROCESSTYPE (HJID);
ALTER TABLE FLOWMAPINOUTTYPE ADD CONSTRAINT FLWMPNTTYPMPNTFLWPRCSSTYPEHJID FOREIGN KEY (MAPINOUT_FLOWPROCESSTYPE_HJID) REFERENCES FLOWPROCESSTYPE (HJID);
ALTER TABLE FLOWMAPINOUTTYPE ADD CONSTRAINT FLWMPNTTYPMPNTFLWMPNOUTTYPEHJ0 FOREIGN KEY (MAPINOUT_FLOWMAPINOUTTYPE_HJ_0) REFERENCES REQUESTRESPONSEDATATYPE (HJID);
ALTER TABLE FLOWPROCESSTYPE ADD CONSTRAINT FLWPRCSSTYPRQSTRSPNSFLWPRCSST0 FOREIGN KEY (REQUESTRESPONSE_FLOWPROCESST_0) REFERENCES REQUESTRESPONSEDATATYPE (HJID);
ALTER TABLE FLOWRULETYPE ADD CONSTRAINT FLWRLTYPERLNUTFLOWRULETYPEHJID FOREIGN KEY (RULEINOUT_FLOWRULETYPE_HJID) REFERENCES REQUESTRESPONSEDATATYPE (HJID);
ALTER TABLE FLOWSUBREQUESTTYPE ADD CONSTRAINT FLWSBRQSTTYPSBRQSTNTFLWSBRQUE0 FOREIGN KEY (SUBREQUESTINOUT_FLOWSUBREQUE_0) REFERENCES REQUESTRESPONSEDATATYPE (HJID);
ALTER TABLE FLOWSUBREQUESTTYPE ADD CONSTRAINT FLWSBRQSTTYSBRQSTFLWPRCSSTYPH0 FOREIGN KEY (SUBREQUEST_FLOWPROCESSTYPE_H_0) REFERENCES FLOWPROCESSTYPE (HJID);
ALTER TABLE FLOWTYPE ADD CONSTRAINT FLWTYPEXCTEREQUESTFLOWTYPEHJID FOREIGN KEY (EXECUTEREQUEST_FLOWTYPE_HJID) REFERENCES FLOWPROCESSTYPE (HJID);
ALTER TABLE FLOWTYPE ADD CONSTRAINT FLWTYPNVKFLWDEFINITIONFLOWTYP0 FOREIGN KEY (INVOKEFLOWDEFINITION_FLOWTYP_0) REFERENCES REQUESTRESPONSEDATATYPE (HJID);
ALTER TABLE FLOWTYPE ADD CONSTRAINT FLWTYPEFLOWSENTERPRISETYPEHJID FOREIGN KEY (FLOWS_ENTERPRISETYPE_HJID) REFERENCES ENTERPRISETYPE (HJID);
ALTER TABLE FLOWTYPE ADD CONSTRAINT FLWTYPPRCSSRSPONSEFLOWTYPEHJID FOREIGN KEY (PROCESSRESPONSE_FLOWTYPE_HJID) REFERENCES FLOWPROCESSTYPE (HJID);
ALTER TABLE FLOWTYPE ADD CONSTRAINT FLWTYPENVOKESOPERATIONTYPEHJID FOREIGN KEY (INVOKES_OPERATIONTYPE_HJID) REFERENCES OPERATIONTYPE (HJID);
ALTER TABLE INTERFACEMAPPINGTYPE ADD CONSTRAINT NTRFCMPPNGTNTRFCMPPNGTYPCLSTR0 FOREIGN KEY (INTERFACEMAPPINGTYPE_CLUSTER_0) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE INTERFACEMAPPINGTYPE ADD CONSTRAINT FK_INTERFACEMAPPINGTYPE_HJID FOREIGN KEY (HJID) REFERENCES DATATYPE (HJID);
ALTER TABLE OPERATIONTYPE ADD CONSTRAINT PRTNTYPRQSTRSPNSEOPERATIONTYP0 FOREIGN KEY (REQUESTRESPONSE_OPERATIONTYP_0) REFERENCES REQUESTRESPONSEDATATYPE (HJID);
ALTER TABLE OPERATIONTYPE ADD CONSTRAINT PRTONTYPEPRTIONSERVICETYPEHJID FOREIGN KEY (OPERATION__SERVICETYPE_HJID) REFERENCES SERVICETYPE (HJID);
ALTER TABLE SERVICETYPE ADD CONSTRAINT SRVICETYPESRVICEDOMAINTYPEHJID FOREIGN KEY (SERVICE_DOMAINTYPE_HJID) REFERENCES DOMAINTYPE (HJID);
ALTER TABLE CLUSTERTYPE_WARNING ADD CONSTRAINT FK_CLUSTERTYPE_WARNING_HJID FOREIGN KEY (HJID) REFERENCES CLUSTERTYPE (HJID);
ALTER TABLE FLOWMAPINOUTTYPE_INTERFACEMA_0 ADD CONSTRAINT FLWMAPINOUTTYPEINTERFACEMA0HJD FOREIGN KEY (HJID) REFERENCES FLOWMAPINOUTTYPE (HJID);
ALTER TABLE FLOWPROCESSTYPE_EXECUTIONORD_0 ADD CONSTRAINT FLWPROCESSTYPEEXECUTIONORD0HJD FOREIGN KEY (HJID) REFERENCES FLOWPROCESSTYPE (HJID);

-----------------------------------------------------------------------------
-- Hibernate sequence
-----------------------------------------------------------------------------
-- CREATE SEQUENCE hibernate_sequence;

-----------------------------------------------------------------------------
-- EclipseLink sequence
-----------------------------------------------------------------------------
-- CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR2(50) NOT NULL, SEQ_COUNT NUMBER(38) NULL, PRIMARY KEY (SEQ_NAME));
-- INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);
