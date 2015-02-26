set pages 0
set feed off
set verify off
set head off
set echo off

select 'exec dbms_wm.disableversioning('''||table_name||''');'
from user_wm_versioned_tables;

spool ../temp/osp_bus_dyn_disable_versioning.sql
prompt spool ../temp/osp_bus_dyn_disable_versioning.log
/
prompt spool off
prompt @../root/osp_bus_disable_versioning.sql
spool off

select decode(count(*), 0, '--                               Finished Disabling Version Enabled Tables', '@../temp/osp_bus_dyn_disable_versioning.sql')
from user_wm_versioned_tables;

spool ../temp/osp_bus_run_dyn_disable_versioning_or_exit.sql
/
spool off

set echo on
@../temp/osp_bus_run_dyn_disable_versioning_or_exit.sql

