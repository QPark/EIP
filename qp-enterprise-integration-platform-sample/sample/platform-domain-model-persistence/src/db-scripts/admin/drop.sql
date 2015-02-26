select 'alter table '||uc.table_name||' drop constraint '||uc.constraint_name||';'
from user_constraints uc
where uc.constraint_type = 'R';
select 'drop table '||ut.table_name||';'
from user_tables ut;
select 'drop sequence '||us.sequence_name||';'
from user_sequences us;
