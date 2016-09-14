-----------------------------------------------------------------------------
-- Drop script of persistence unit com.samples.platform.domain.
-- Target database type: Oracle 10.1
-- Generated at 20160914-060147.826.
-----------------------------------------------------------------------------
-- Verify sequence handling at the end of the script

ALTER TABLE CLUSTERTYPE DROP CONSTRAINT CLSTERTYPECLSTERDOMAINTYPEHJID;
ALTER TABLE DATATYPE DROP CONSTRAINT DTTYPBSCDTATYPESENTERPRISETYP0;
ALTER TABLE FIELDMAPPINGTYPE DROP CONSTRAINT FK_FIELDMAPPINGTYPE_HJID;
ALTER TABLE COMPLEXMAPPINGTYPE DROP CONSTRAINT CMPLXMPPNCMPLXMPPNGTYPCLSTRTY0;
ALTER TABLE COMPLEXMAPPINGTYPE DROP CONSTRAINT FK_COMPLEXMAPPINGTYPE_HJID;
ALTER TABLE COMPLEXTYPE DROP CONSTRAINT CMPLXTYPCMPLXTYPCLSTERTYPEHJID;
ALTER TABLE COMPLEXTYPE DROP CONSTRAINT FK_COMPLEXTYPE_HJID;
ALTER TABLE COMPLEXUUIDMAPPINGTYPE DROP CONSTRAINT CMPLXDMPPNGCMPLXDMPPNGTYPCLST0;
ALTER TABLE COMPLEXUUIDMAPPINGTYPE DROP CONSTRAINT FK_COMPLEXUUIDMAPPINGTYPE_HJID;
ALTER TABLE DEFAULTMAPPINGTYPE DROP CONSTRAINT DFLTMPPNGTDFLTMPPNGTYPCLSTRTY0;
ALTER TABLE DEFAULTMAPPINGTYPE DROP CONSTRAINT FK_DEFAULTMAPPINGTYPE_HJID;
ALTER TABLE DIRECTMAPPINGTYPE DROP CONSTRAINT DRCTMPPNGDRCTMPPNGTYPCLSTRTYP0;
ALTER TABLE DIRECTMAPPINGTYPE DROP CONSTRAINT FK_DIRECTMAPPINGTYPE_HJID;
ALTER TABLE DOMAINTYPE DROP CONSTRAINT DMNTYPEDMINSENTERPRISETYPEHJID;
ALTER TABLE ELEMENTTYPE DROP CONSTRAINT LMNTTYPLMNTTYPECLUSTERTYPEHJID;
ALTER TABLE ELEMENTTYPE DROP CONSTRAINT FK_ELEMENTTYPE_HJID;
ALTER TABLE FIELDTYPE DROP CONSTRAINT FIELDTYPEFIELDCOMPLEXTYPE_HJID;
ALTER TABLE FIELDTYPE DROP CONSTRAINT FLDTYPFLDMPPNGSINTERFACEMAPPI0;
ALTER TABLE FIELDTYPE DROP CONSTRAINT FLDTYPENPTFIELDMAPPINGTYPEHJID;
ALTER TABLE FLOWFILTERTYPE DROP CONSTRAINT FLWFLTRTYPFLTRFLWPRCSSTYPEHJID;
ALTER TABLE FLOWFILTERTYPE DROP CONSTRAINT FLWFLTRTYPFLTRNTFLWFLTERTYPEH0;
ALTER TABLE FLOWMAPINOUTTYPE DROP CONSTRAINT FLWMPNTTYPMPNTFLWPRCSSTYPEHJID;
ALTER TABLE FLOWMAPINOUTTYPE DROP CONSTRAINT FLWMPNTTYPMPNTFLWMPNOUTTYPEHJ0;
ALTER TABLE FLOWPROCESSTYPE DROP CONSTRAINT FLWPRCSSTYPRQSTRSPNSFLWPRCSST0;
ALTER TABLE FLOWSUBREQUESTTYPE DROP CONSTRAINT FLWSBRQSTTYPSBRQSTNTFLWSBRQUE0;
ALTER TABLE FLOWSUBREQUESTTYPE DROP CONSTRAINT FLWSBRQSTTYSBRQSTFLWPRCSSTYPH0;
ALTER TABLE FLOWTYPE DROP CONSTRAINT FLWTYPEXCTEREQUESTFLOWTYPEHJID;
ALTER TABLE FLOWTYPE DROP CONSTRAINT FLWTYPNVKFLWDEFINITIONFLOWTYP0;
ALTER TABLE FLOWTYPE DROP CONSTRAINT FLWTYPEFLOWSENTERPRISETYPEHJID;
ALTER TABLE FLOWTYPE DROP CONSTRAINT FLWTYPPRCSSRSPONSEFLOWTYPEHJID;
ALTER TABLE FLOWTYPE DROP CONSTRAINT FLWTYPENVOKESOPERATIONTYPEHJID;
ALTER TABLE INTERFACEMAPPINGTYPE DROP CONSTRAINT NTRFCMPPNGTNTRFCMPPNGTYPCLSTR0;
ALTER TABLE INTERFACEMAPPINGTYPE DROP CONSTRAINT FK_INTERFACEMAPPINGTYPE_HJID;
ALTER TABLE OPERATIONTYPE DROP CONSTRAINT PRTNTYPRQSTRSPNSEOPERATIONTYP0;
ALTER TABLE OPERATIONTYPE DROP CONSTRAINT PRTONTYPEPRTIONSERVICETYPEHJID;
ALTER TABLE SERVICETYPE DROP CONSTRAINT SRVICETYPESRVICEDOMAINTYPEHJID;
ALTER TABLE CLUSTERTYPE_WARNING DROP CONSTRAINT FK_CLUSTERTYPE_WARNING_HJID;
ALTER TABLE FLOWMAPINOUTTYPE_INTERFACEMA_0 DROP CONSTRAINT FLWMAPINOUTTYPEINTERFACEMA0HJD;
DROP INDEX CLSTRTYPCLSTRTYPCLSTRDMNTYPHJD;
DROP TABLE CLUSTERTYPE CASCADE CONSTRAINTS;
DROP INDEX DTTYPDTTYPBSCDTTYPSNTRPRSETYP0;
DROP TABLE DATATYPE CASCADE CONSTRAINTS;
DROP TABLE FIELDMAPPINGTYPE CASCADE CONSTRAINTS;
DROP INDEX CMPLXMPPNCMPLXMPPNGTYPCLSTRTY0;
DROP TABLE COMPLEXMAPPINGTYPE CASCADE CONSTRAINTS;
DROP INDEX CMPCMPLXTYPCMPLXTYPCLSTRTYPHJD;
DROP TABLE COMPLEXTYPE CASCADE CONSTRAINTS;
DROP INDEX CMPLXDMPPNGCMPLXDMPPNGTYPCLST0;
DROP TABLE COMPLEXUUIDMAPPINGTYPE CASCADE CONSTRAINTS;
DROP INDEX DFLTMPPNGTDFLTMPPNGTYPCLSTRTY0;
DROP TABLE DEFAULTMAPPINGTYPE CASCADE CONSTRAINTS;
DROP INDEX DRCTMPPNGDRCTMPPNGTYPCLSTRTYP0;
DROP TABLE DIRECTMAPPINGTYPE CASCADE CONSTRAINTS;
DROP INDEX DMNTYPDMNTYPDMNSNTRPRSTYPEHJID;
DROP TABLE DOMAINTYPE CASCADE CONSTRAINTS;
DROP INDEX LMNTTLMNTTYPLMNTTYPCLSTRTYPHJD;
DROP TABLE ELEMENTTYPE CASCADE CONSTRAINTS;
DROP TABLE ENTERPRISETYPE CASCADE CONSTRAINTS;
DROP INDEX FLDTYPFLDTYPFLDCOMPLEXTYPEHJID;
DROP INDEX FLDTYPFLDTYPFLDMPPNGSNTRFCMPP0;
DROP INDEX FLDTYPFLDTYPNPTFLDMPPNGTYPHJID;
DROP TABLE FIELDTYPE CASCADE CONSTRAINTS;
DROP INDEX FLFLWFLTRTYPFLTRFLWPRCSSTYPHJD;
DROP INDEX FLFLWFLTRTYPFLTRNTFLWFLTRTYPH0;
DROP TABLE FLOWFILTERTYPE CASCADE CONSTRAINTS;
DROP INDEX FLFLWMPNTTYPMPNTFLWPRCSSTYPHJD;
DROP INDEX FLWFLWMPNTTYPMPNTFLWMPNTTYPHJ0;
DROP TABLE FLOWMAPINOUTTYPE CASCADE CONSTRAINTS;
DROP INDEX FLWPRCSSTYPRQSTRSPNSFLWPRCSST0;
DROP TABLE FLOWPROCESSTYPE CASCADE CONSTRAINTS;
DROP INDEX FLFLWSBRQSTTYPSBRQSTNTFLWSBRQ0;
DROP INDEX FLWSBRQSTTYSBRQSTFLWPRCSSTYPH0;
DROP TABLE FLOWSUBREQUESTTYPE CASCADE CONSTRAINTS;
DROP INDEX FLWTYPFLWTYPXCTRQSTFLWTYPEHJID;
DROP INDEX FLWTYPFLWTYPNVKFLWDFNTNFLWTYP0;
DROP INDEX FLWTYPFLWTYPFLWSNTRPRSTYPEHJID;
DROP INDEX FLWTYFLWTYPPRCSSRSPNSFLWTYPHJD;
DROP INDEX FLWTYPFLWTYPNVKSPRTIONTYPEHJID;
DROP TABLE FLOWTYPE CASCADE CONSTRAINTS;
DROP INDEX NTRFCMPPNGTNTRFCMPPNGTYPCLSTR0;
DROP TABLE INTERFACEMAPPINGTYPE CASCADE CONSTRAINTS;
DROP INDEX PRTNTYPRTNTYPRQSTRSPNSPRTNTYP0;
DROP INDEX PRTNTYPPRTNTYPPRTNSRVCTYPEHJID;
DROP TABLE OPERATIONTYPE CASCADE CONSTRAINTS;
DROP TABLE REQUESTRESPONSEDATATYPE CASCADE CONSTRAINTS;
DROP INDEX SRVCTYPSRVCTYPSRVCDMINTYPEHJID;
DROP TABLE SERVICETYPE CASCADE CONSTRAINTS;
DROP INDEX CLSTRTYPWRNNFKCLSTRTYPWRNNGHJD;
DROP TABLE CLUSTERTYPE_WARNING CASCADE CONSTRAINTS;
DROP INDEX FLWMPNTTYPFLWMPNTTYPNTRFCM0HJD;
DROP TABLE FLOWMAPINOUTTYPE_INTERFACEMA_0 CASCADE CONSTRAINTS;

-----------------------------------------------------------------------------
-- Hibernate sequence
-----------------------------------------------------------------------------
-- DROP SEQUENCE hibernate_sequence;

-----------------------------------------------------------------------------
-- EclipseLink sequence
-----------------------------------------------------------------------------
-- -- DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN';
