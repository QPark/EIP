-----------------------------------------------------------------------------
-- Drop script of persistence unit com.qpark.eip.core.spring.lockedoperation.model.
-- Generated at 20160118-080513.586.
-----------------------------------------------------------------------------
-- Verify sequence handling at the end of the script

DROP TABLE OPERATIONEVENTTYPE CASCADE CONSTRAINTS;
DROP TABLE OPERATIONLOCKCONTROLLTYPE CASCADE CONSTRAINTS;
DROP TABLE OPERATIONSTATETYPE CASCADE CONSTRAINTS;

-----------------------------------------------------------------------------
-- Hibernate sequence
-----------------------------------------------------------------------------
-- DROP SEQUENCE hibernate_sequence;

-----------------------------------------------------------------------------
-- EclipseLink sequence
-----------------------------------------------------------------------------
-- -- DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN';
