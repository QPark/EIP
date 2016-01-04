set heading off;
set pagesize 0;
set feedback off;
set echo off;
set termout off
spool drop.sql;
select 'alter table '||uc.table_name||' drop constraint '||uc.constraint_name||';' from user_constraints uc where uc.constraint_type = 'R' union select 'drop table '||ut.table_name||';' from user_tables ut union select 'drop sequence '||us.sequence_name||';' from user_sequences us;
spool off;
