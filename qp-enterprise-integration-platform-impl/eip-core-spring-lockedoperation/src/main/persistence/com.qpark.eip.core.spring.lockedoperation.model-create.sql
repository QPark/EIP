-----------------------------------------------------------------------------
-- Create script of persistence unit com.qpark.eip.core.spring.lockedoperation.model.
-- Generated at 20160118-080513.586.
-----------------------------------------------------------------------------
-- Verify sequence handling at the end of the script

CREATE TABLE OPERATIONEVENTTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, EVENT VARCHAR2(255) NULL, OPERATIONTIMEITEM TIMESTAMP NULL, OPERATIONUUID VARCHAR2(36) NULL, PRIMARY KEY (HJID));
CREATE TABLE OPERATIONLOCKCONTROLLTYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, LOCKDATEITEM TIMESTAMP NULL, OPERATIONNAME VARCHAR2(255) NULL, SERVERIPADDRESS VARCHAR2(255) NULL, SERVERNAME VARCHAR2(255) NULL, PRIMARY KEY (HJID));
CREATE TABLE OPERATIONSTATETYPE (HJID NUMBER(19) NOT NULL, DTYPE VARCHAR2(31) NULL, OPERATIONUUID VARCHAR2(36) NULL, STATE_ VARCHAR2(255) NULL, PRIMARY KEY (HJID));

-----------------------------------------------------------------------------
-- Hibernate sequence
-----------------------------------------------------------------------------
-- CREATE SEQUENCE hibernate_sequence;

-----------------------------------------------------------------------------
-- EclipseLink sequence
-----------------------------------------------------------------------------
-- CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR2(50) NOT NULL, SEQ_COUNT NUMBER(38) NULL, PRIMARY KEY (SEQ_NAME));
-- INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);
