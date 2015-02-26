set pages 0
set feed off
set verify off
set head off
set echo off
set lines 200

select 'alter table '||uc.table_name||' drop constraint '||uc.constraint_name||';'
from user_constraints uc
where uc.constraint_type = 'R';

spool /xnb/osp_bus_dyn_drop_fks.sql
prompt spool /xnb/osp_bus_dyn_drop_fks.log
/
prompt spool off
spool off


select 'drop table '||ut.table_name||';'
from user_tables ut;

spool /xnb/osp_bus_dyn_drop_tables.sql
prompt spool /xnb/osp_bus_dyn_drop_tables.log
/
prompt spool off
spool off

select 'drop sequence '||us.sequence_name||';'
from user_sequences us;

spool /xnb/osp_bus_dyn_drop_seqs.sql
prompt spool /xnb/osp_bus_dyn_drop_seqs.log
/
prompt spool off
spool off

set feed on
set head on
set verify on
set pages 2000
set echo on

@/xnb/osp_bus_dyn_drop_fks.sql
@/xnb/osp_bus_dyn_drop_tables.sql
@/xnb/osp_bus_dyn_drop_seqs.sql




