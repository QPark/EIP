-----------------------------------------------------------------------------
-- Drop script of persistence unit com.samples.platform.domain.
-- Target database type: Oracle 10.1
-- Generated at 20160413-205220.241.
-----------------------------------------------------------------------------
-- Verify sequence handling at the end of the script

ALTER TABLE ASSETTYPE DROP CONSTRAINT FK_ASSETTYPE_HJID;
ALTER TABLE BOOKTYPE DROP CONSTRAINT BOOKTYPEBOOKS_LIBRARYTYPE_HJID;
ALTER TABLE BOOKTYPE DROP CONSTRAINT FK_BOOKTYPE_HJID;
ALTER TABLE LIBRARYTYPE DROP CONSTRAINT FK_LIBRARYTYPE_HJID;
DROP TABLE ENTITYTYPE CASCADE CONSTRAINTS;
DROP TABLE ASSETTYPE CASCADE CONSTRAINTS;
DROP INDEX BKTYPBKTYPBOOKSLIBRARYTYPEHJID;
DROP TABLE BOOKTYPE CASCADE CONSTRAINTS;
DROP TABLE LIBRARYTYPE CASCADE CONSTRAINTS;

-----------------------------------------------------------------------------
-- Hibernate sequence
-----------------------------------------------------------------------------
-- DROP SEQUENCE hibernate_sequence;

-----------------------------------------------------------------------------
-- EclipseLink sequence
-----------------------------------------------------------------------------
-- -- DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN';
