-- OSP service bus test
create tablespace ospbust logging datafile size 512M
autoextend on next 128M maxsize 2048M
extent management local uniform size 1M
segment space management auto;

-- OSP service bus integration
--create tablespace ospbusi logging datafile size 512M
--autoextend on next 128M maxsize 2048M
--extent management local uniform size 1M
--segment space management auto;



