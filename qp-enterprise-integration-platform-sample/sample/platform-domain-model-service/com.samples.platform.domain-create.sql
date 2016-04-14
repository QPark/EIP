-----------------------------------------------------------------------------
-- Create script of persistence unit com.samples.platform.domain.
-- Target database type: Oracle 10.1
-- Generated at 20160413-205220.241.
-----------------------------------------------------------------------------
-- Verify sequence handling at the end of the script

CREATE TABLE ENTITYTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, UUID VARCHAR2(36) NULL, PRIMARY KEY (HJID));
CREATE TABLE ASSETTYPE (HJID NUMBER(19) NOT NULL, NAME_ VARCHAR2(255) NULL, PRIMARY KEY (HJID));
CREATE TABLE BOOKTYPE (HJID NUMBER(19) NOT NULL, ISBN VARCHAR2(255) NULL, CATEGORY VARCHAR2(36) NULL, LANGUAGE_ VARCHAR2(36) NULL, PRICE NUMBER(20,10) NULL, TITLE VARCHAR2(255) NULL, BOOKS_LIBRARYTYPE_HJID NUMBER(19) NULL, PRIMARY KEY (HJID));
CREATE INDEX BKTYPBKTYPBOOKSLIBRARYTYPEHJID ON BOOKTYPE (BOOKS_LIBRARYTYPE_HJID);
CREATE TABLE LIBRARYTYPE (HJID NUMBER(19) NOT NULL, LOCATION_ VARCHAR2(255) NULL, PRIMARY KEY (HJID));
ALTER TABLE ASSETTYPE ADD CONSTRAINT FK_ASSETTYPE_HJID FOREIGN KEY (HJID) REFERENCES ENTITYTYPE (HJID);
ALTER TABLE BOOKTYPE ADD CONSTRAINT BOOKTYPEBOOKS_LIBRARYTYPE_HJID FOREIGN KEY (BOOKS_LIBRARYTYPE_HJID) REFERENCES ENTITYTYPE (HJID);
ALTER TABLE BOOKTYPE ADD CONSTRAINT FK_BOOKTYPE_HJID FOREIGN KEY (HJID) REFERENCES ENTITYTYPE (HJID);
ALTER TABLE LIBRARYTYPE ADD CONSTRAINT FK_LIBRARYTYPE_HJID FOREIGN KEY (HJID) REFERENCES ENTITYTYPE (HJID);

-----------------------------------------------------------------------------
-- Hibernate sequence
-----------------------------------------------------------------------------
-- CREATE SEQUENCE hibernate_sequence;

-----------------------------------------------------------------------------
-- EclipseLink sequence
-----------------------------------------------------------------------------
-- CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR2(50) NOT NULL, SEQ_COUNT NUMBER(38) NULL, PRIMARY KEY (SEQ_NAME));
-- INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);