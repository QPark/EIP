
    create table APPLICATIONUSERLOGTYPE (
        HJID number(19,0) not null,
        CLIENTSESSIONID varchar2(2048 char),
        CONTEXT varchar2(2048 char),
        DURATIONMILLIS number(19,0),
        DURATIONSTRING varchar2(2048 char),
        HOSTNAME varchar2(2048 char),
        OPERATIONNAME varchar2(2048 char),
        RETURNEDENTITIES number(10,0),
        RETURNEDFAILURES number(10,0),
        SERVICENAME varchar2(2048 char),
        STARTITEM timestamp,
        STOPITEM timestamp,
        USERNAME varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        primary key (HJID)
    );

    create table AUTHENTICATIONTYPE (
        HJID number(19,0) not null,
        CONTEXT varchar2(2048 char),
        ENABLED number(1,0),
        PASSWORD_ varchar2(2048 char),
        USERNAME varchar2(2048 char),
        AUTHENTICATION_CONTEXTTYPE_H_0 number(19,0),
        primary key (HJID)
    );

    create table CONSUMERSERVICETYPE (
        HJID number(19,0) not null,
        REQUESTVALIDATIONPROPERTYNAME varchar2(2048 char),
        REQUESTVALIDATIONVALUE number(1,0),
        RESPONSEVALIDATIONPROPERTYNA_0 varchar2(2048 char),
        RESPONSEVALIDATIONVALUE number(1,0),
        TIMEOUTPROPERTYNAME varchar2(2048 char),
        TIMEOUTVALUE varchar2(2048 char),
        URLPROPERTYNAME varchar2(2048 char),
        URLVALUE varchar2(2048 char),
        USERNAMEPROPERTYNAME varchar2(2048 char),
        USERNAMEVALUE varchar2(2048 char),
        USERPASSWORDPROPERTYNAME varchar2(2048 char),
        USERPASSWORDVALUE varchar2(2048 char),
        CONSUMER_WEBAPPTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table CONTEXTLISTTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CONTEXTTYPE (
        HJID number(19,0) not null,
        CONTEXT varchar2(2048 char),
        CONTEXT_CONTEXTLISTTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table DATABASECONNECTIONTYPE (
        HJID number(19,0) not null,
        DRIVERCLASSNAME varchar2(2048 char),
        JDBCURL varchar2(2048 char),
        RESOURCENAME varchar2(2048 char),
        TYPE_ varchar2(2048 char),
        USERNAME varchar2(2048 char),
        USERPASSWORD varchar2(2048 char),
        DATABASE__WEBAPPTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table DATABASESERVERTYPE (
        HJID number(19,0) not null,
        OPERATIONSYSTEM_DATABASESERV_0 number(19,0),
        primary key (HJID)
    );

    create table ENVIRONMENTTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        ENVIRONMENT_SYSTEMTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table ESXSERVERTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FLOWLOGMESSAGETYPE (
        HJID number(19,0) not null,
        CLASSIFICATION varchar2(2048 char),
        DATADESCRIPTION varchar2(2048 char),
        FLOWIDENTIFIER varchar2(2048 char),
        FLOWNAME varchar2(2048 char),
        FLOWSESSION varchar2(2048 char),
        FLOWSTEP varchar2(2048 char),
        LOGMESSAGETYPE varchar2(2048 char),
        LOGTIMEITEM timestamp,
        SEVERITY varchar2(2048 char),
        SUBCLASSIFICATION varchar2(2048 char),
        primary key (HJID)
    );

    create table GRANTEDAUTHORITYTYPE (
        HJID number(19,0) not null,
        MAXREQUESTS number(10,0),
        ROLENAME varchar2(2048 char),
        GRANTEDAUTHORITY_AUTHENTICAT_0 number(19,0),
        primary key (HJID)
    );

    create table HTTPSERVERINSTANCETYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        PATH_ varchar2(2048 char),
        SERVERADMINMAILADDRESS varchar2(2048 char),
        SERVERALIAS varchar2(2048 char),
        SERVERNAME varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        WORKERREFERENCEFAILONSTATUS varchar2(2048 char),
        WORKERREFERENCESOCKETTIMEOUT number(10,0),
        INSTANCE__HTTPSERVERTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table HTTPSERVERTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        PATH_ varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        primary key (HJID)
    );

    create table INSIGHTCONNECTIONTYPE (
        HJID number(19,0) not null,
        AGENTCONNECTIONURI varchar2(2048 char),
        AGENTUSERNAME varchar2(2048 char),
        AGENTUSERPASSWORD varchar2(2048 char),
        primary key (HJID)
    );

    create table JAVATYPE (
        HJID number(19,0) not null,
        PATH_ varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        primary key (HJID)
    );

    create table JKMOUNTPOINTTYPE (
        HJID number(19,0) not null,
        BASEURL varchar2(2048 char),
        HOST_ varchar2(2048 char),
        NAME_ varchar2(2048 char),
        PORT number(10,0),
        JKMOUNTPOINT_HTTPSERVERINSTA_0 number(19,0),
        primary key (HJID)
    );

    create table JMXACCESSTYPE (
        HJID number(19,0) not null,
        PORT number(10,0),
        REMOTEACCESSTYPE varchar2(2048 char),
        REMOTEUSERNAME varchar2(2048 char),
        REMOTEUSERPASSWORD varchar2(2048 char),
        primary key (HJID)
    );

    create table JVMOPTIONTYPE (
        HJID number(19,0) not null,
        XXMAXNEWSIZE varchar2(2048 char),
        XXMAXPERMSIZE varchar2(2048 char),
        XXNEWSIZE varchar2(2048 char),
        XXPERMSIZE varchar2(2048 char),
        XMS varchar2(2048 char),
        XMX varchar2(2048 char),
        XRUNJDWPPORT varchar2(2048 char),
        XSS varchar2(2048 char),
        primary key (HJID)
    );

    create table LDAPCONSUMERSERVICETYPE (
        KEYSTOREPASSWORDPROPERTYNAME varchar2(2048 char),
        KEYSTOREPASSWORDVALUE varchar2(2048 char),
        KEYSTOREPATHPROPERTYNAME varchar2(2048 char),
        KEYSTOREPATHVALUE varchar2(2048 char),
        LDAPSERVERBASE varchar2(2048 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table MAVENARTIFACTTYPE (
        HJID number(19,0) not null,
        ARTIFACTID varchar2(2048 char),
        GROUPID varchar2(2048 char),
        PACKING varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        primary key (HJID)
    );

    create table ONESATPLANCONFIGURATIONTYPE (
        HJID number(19,0) not null,
        REPORTDESIGNPATH varchar2(2048 char),
        REPORTSCHEDULERSTART number(1,0),
        REPORTURISCHEME varchar2(2048 char),
        SBCSERVICEBUSSERVERURLROOT varchar2(2048 char),
        SBCSERVICEBUSSERVICECRM varchar2(2048 char),
        SBCSERVICEBUSSERVICEDIRECTORY varchar2(2048 char),
        SBCSERVICEBUSSERVICELEGAL varchar2(2048 char),
        SBCSERVICEBUSSERVICELINKBUDG_0 varchar2(2048 char),
        SBCSERVICEBUSSERVICELOGGING varchar2(2048 char),
        SBCSERVICEBUSSERVICEMONITORI_0 varchar2(2048 char),
        SBCSERVICEBUSUSERNAME varchar2(2048 char),
        SBCSERVICEBUSUSERPASSWORD varchar2(2048 char),
        SBCWEBSERVICEMAXRETRYCOUNT number(10,0),
        SBCWEBSERVICEREADTIMEOUTINMI_0 number(10,0),
        SBCWEBSERVICERETRYTIMEINMILL_0 number(10,0),
        SBCWEBSERVICEVALIDATEINBOUND_0 number(1,0),
        SBCWEBSERVICEVALIDATEOUTBOUN_0 number(1,0),
        SBPSERVICEBUSUSERNAME varchar2(2048 char),
        SBPSERVICEBUSUSERPASSWORD varchar2(2048 char),
        SBPWEBSERVICEEXCEPTIONRESOLV_0 varchar2(2048 char),
        SBPWEBSERVICEVALIDATEINBOUND_0 number(1,0),
        SBPWEBSERVICEVALIDATEOUTBOUN_0 number(1,0),
        TENEOCHECKOUTTIMEOUT number(10,0),
        TENEOHIBERNATECONNECTIONURL varchar2(2048 char),
        TENEOHIBERNATECONNECTIONUSER_0 varchar2(2048 char),
        TENEOHIBERNATECONNECTIONUSER_1 varchar2(2048 char),
        TENEOLIVECHECKOUTTIMEOUT number(10,0),
        UPDATESITEURL varchar2(255 char),
        primary key (HJID)
    );

    create table OPERATINGSYSTEMTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        TYPE_ varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        primary key (HJID)
    );

    create table PHYSICALSERVER (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        SERVER_SYSTEMTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table PROVIDERSERVICETYPE (
        HJID number(19,0) not null,
        AUTHORITYCONFIGCONTEXTNAME varchar2(2048 char),
        SERVICEID varchar2(2048 char),
        PROVIDER_WEBAPPTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table REWRITERULETYPE (
        HJID number(19,0) not null,
        DESTINATION varchar2(2048 char),
        SOURCE_ varchar2(2048 char),
        REWRITERULE_HTTPSERVERINSTAN_0 number(19,0),
        primary key (HJID)
    );

    create table ROLETYPE (
        HJID number(19,0) not null,
        MAXREQUESTS number(10,0),
        NAME_ varchar2(2048 char),
        ROLE__USERTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table SYSTEMTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        primary key (HJID)
    );

    create table SYSTEMUSERLOGTYPE (
        HJID number(19,0) not null,
        CONTEXT varchar2(2048 char),
        LOGDATEITEM date,
        OPERATIONNAME varchar2(2048 char),
        REQUESTSDENIED number(10,0),
        REQUESTSGRANTED number(10,0),
        RESPONSEFAULTS number(10,0),
        SERVICENAME varchar2(2048 char),
        USERNAME varchar2(2048 char),
        VERSION_ varchar2(2048 char),
        primary key (HJID)
    );

    create table TCSERVERSERVICETYPE (
        HJID number(19,0) not null,
        AJPCONNECTORCONNECTIONTIMEOUT number(10,0),
        AJPCONNECTORPORT number(10,0),
        AJPCONNECTORREDIRECTIONPORT number(10,0),
        CATALINASERVICENAME varchar2(2048 char),
        ENGINEJVMROUTE varchar2(2048 char),
        HOSTAPPBASEPATH varchar2(2048 char),
        HTTPCONNECTORCONNECTIONTIMEO_0 number(10,0),
        HTTPCONNECTORPORT number(10,0),
        HTTPCONNECTORREDIRECTIONPORT number(10,0),
        WEBAPP_TCSERVERSERVICETYPE_H_0 number(19,0),
        SERVICE_TCSERVERTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table TCSERVERTYPE (
        HJID number(19,0) not null,
        PATH_ varchar2(2048 char),
        TYPE_ varchar2(2048 char),
        INSIGHT_TCSERVERTYPE_HJID number(19,0),
        JMX_TCSERVERTYPE_HJID number(19,0),
        JVMOPTIONS_TCSERVERTYPE_HJID number(19,0),
        ONESATPLANCONFIG_TCSERVERTYP_0 number(19,0),
        TCSERVER_VMSERVERTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table USERTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        PASSWORD_ varchar2(2048 char),
        USER__PROVIDERSERVICETYPE_HJ_0 number(19,0),
        primary key (HJID)
    );

    create table VMSERVERTYPE (
        HJID number(19,0) not null,
        IPADDRESS varchar2(2048 char),
        NAME_ varchar2(2048 char),
        HTTPSERVER_VMSERVERTYPE_HJID number(19,0),
        JAVA_VMSERVERTYPE_HJID number(19,0),
        OPERATIONSYSTEM_VMSERVERTYPE_0 number(19,0),
        VMSERVER_ESXSERVERTYPE_HJID number(19,0),
        VMSERVER_ENVIRONMENTTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table WEBAPPTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(2048 char),
        PROVIDERREQUESTVALIDATION number(1,0),
        PROVIDERRESPONSEVALIDATION number(1,0),
        ARTIFACT_WEBAPPTYPE_HJID number(19,0),
        primary key (HJID)
    );

    alter table AUTHENTICATIONTYPE 
        add constraint FKCE9C51B293DA6319 
        foreign key (AUTHENTICATION_CONTEXTTYPE_H_0) 
        references CONTEXTTYPE;

    alter table CONSUMERSERVICETYPE 
        add constraint FKC5E21D994A00A14 
        foreign key (CONSUMER_WEBAPPTYPE_HJID) 
        references WEBAPPTYPE;

    alter table CONTEXTTYPE 
        add constraint FK7A40E9499F3B14C0 
        foreign key (CONTEXT_CONTEXTLISTTYPE_HJID) 
        references CONTEXTLISTTYPE;

    alter table DATABASECONNECTIONTYPE 
        add constraint FK74AEA153343E4862 
        foreign key (DATABASE__WEBAPPTYPE_HJID) 
        references WEBAPPTYPE;

    alter table DATABASESERVERTYPE 
        add constraint FKA146F4B8F42C8978 
        foreign key (HJID) 
        references PHYSICALSERVER;

    alter table DATABASESERVERTYPE 
        add constraint FKA146F4B81CD0E674 
        foreign key (OPERATIONSYSTEM_DATABASESERV_0) 
        references OPERATINGSYSTEMTYPE;

    alter table ENVIRONMENTTYPE 
        add constraint FKA6D21BAD31E15B71 
        foreign key (ENVIRONMENT_SYSTEMTYPE_HJID) 
        references SYSTEMTYPE;

    alter table ESXSERVERTYPE 
        add constraint FKA6AA21E7F42C8978 
        foreign key (HJID) 
        references PHYSICALSERVER;

    alter table GRANTEDAUTHORITYTYPE 
        add constraint FK3469262220013668 
        foreign key (GRANTEDAUTHORITY_AUTHENTICAT_0) 
        references AUTHENTICATIONTYPE;

    alter table HTTPSERVERINSTANCETYPE 
        add constraint FKCC9EFE9AF3137168 
        foreign key (INSTANCE__HTTPSERVERTYPE_HJID) 
        references HTTPSERVERTYPE;

    alter table JKMOUNTPOINTTYPE 
        add constraint FK28C214528B31D315 
        foreign key (JKMOUNTPOINT_HTTPSERVERINSTA_0) 
        references HTTPSERVERINSTANCETYPE;

    alter table LDAPCONSUMERSERVICETYPE 
        add constraint FK1D8A8292E804F315 
        foreign key (HJID) 
        references CONSUMERSERVICETYPE;

    alter table PHYSICALSERVER 
        add constraint FKBA06B6DAC141F8A1 
        foreign key (SERVER_SYSTEMTYPE_HJID) 
        references SYSTEMTYPE;

    alter table PROVIDERSERVICETYPE 
        add constraint FK538920BE4506360F 
        foreign key (PROVIDER_WEBAPPTYPE_HJID) 
        references WEBAPPTYPE;

    alter table REWRITERULETYPE 
        add constraint FK1E75C9A21DC238A1 
        foreign key (REWRITERULE_HTTPSERVERINSTAN_0) 
        references HTTPSERVERINSTANCETYPE;

    alter table ROLETYPE 
        add constraint FK1ED0D4B0A4E62447 
        foreign key (ROLE__USERTYPE_HJID) 
        references USERTYPE;

    alter table TCSERVERSERVICETYPE 
        add constraint FK57ED0D9DE6FCF053 
        foreign key (SERVICE_TCSERVERTYPE_HJID) 
        references TCSERVERTYPE;

    alter table TCSERVERSERVICETYPE 
        add constraint FK57ED0D9D5C6C52D 
        foreign key (WEBAPP_TCSERVERSERVICETYPE_H_0) 
        references WEBAPPTYPE;

    alter table TCSERVERTYPE 
        add constraint FK2E485CACA959C43D 
        foreign key (JVMOPTIONS_TCSERVERTYPE_HJID) 
        references JVMOPTIONTYPE;

    alter table TCSERVERTYPE 
        add constraint FK2E485CAC2FF1E958 
        foreign key (INSIGHT_TCSERVERTYPE_HJID) 
        references INSIGHTCONNECTIONTYPE;

    alter table TCSERVERTYPE 
        add constraint FK2E485CACC94CFA58 
        foreign key (JMX_TCSERVERTYPE_HJID) 
        references JMXACCESSTYPE;

    alter table TCSERVERTYPE 
        add constraint FK2E485CAC99F3AFD0 
        foreign key (TCSERVER_VMSERVERTYPE_HJID) 
        references VMSERVERTYPE;

    alter table TCSERVERTYPE 
        add constraint FK2E485CAC8FC2A89C 
        foreign key (ONESATPLANCONFIG_TCSERVERTYP_0) 
        references ONESATPLANCONFIGURATIONTYPE;

    alter table USERTYPE 
        add constraint FK1ED28D8561486B7C 
        foreign key (USER__PROVIDERSERVICETYPE_HJ_0) 
        references PROVIDERSERVICETYPE;

    alter table VMSERVERTYPE 
        add constraint FKE9AA2F41E9B133A 
        foreign key (HTTPSERVER_VMSERVERTYPE_HJID) 
        references HTTPSERVERTYPE;

    alter table VMSERVERTYPE 
        add constraint FKE9AA2F4ADAED948 
        foreign key (JAVA_VMSERVERTYPE_HJID) 
        references JAVATYPE;

    alter table VMSERVERTYPE 
        add constraint FKE9AA2F4EAD0C897 
        foreign key (OPERATIONSYSTEM_VMSERVERTYPE_0) 
        references OPERATINGSYSTEMTYPE;

    alter table VMSERVERTYPE 
        add constraint FKE9AA2F43255FDC0 
        foreign key (VMSERVER_ESXSERVERTYPE_HJID) 
        references ESXSERVERTYPE;

    alter table VMSERVERTYPE 
        add constraint FKE9AA2F48105B460 
        foreign key (VMSERVER_ENVIRONMENTTYPE_HJID) 
        references ENVIRONMENTTYPE;

    alter table WEBAPPTYPE 
        add constraint FKBEFC8B071AB32C3E 
        foreign key (ARTIFACT_WEBAPPTYPE_HJID) 
        references MAVENARTIFACTTYPE;

    create sequence hibernate_sequence;
