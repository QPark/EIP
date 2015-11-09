
    create table ADDITIONALATTRIBUTECRITERIAT_0 (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ADDITIONALATTRIBUTECRITERIAT_1 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        ASSETUUIDITEMS_ADDITIONALATT_0 number(19,0),
        primary key (HJID)
    );

    create table ADDITIONALATTRIBUTETYPE (
        HJID number(19,0) not null,
        ASSETUUID varchar2(36 char),
        DEFINITIONNAME varchar2(255 char),
        DEFINITIONUUID varchar2(36 char),
        VALUE_ varchar2(255 char),
        primary key (HJID)
    );

    create table AMPLIFIERCONFIGURATIONTYPE (
        HJID number(19,0) not null,
        ALCSTATE number(1,0),
        LIMITERSTATUS number(1,0),
        STATUS varchar2(255 char),
        primary key (HJID)
    );

    create table AMPLIFIERTYPE (
        ALCADJUSTMENTRANGE number(6,2),
        ALCAVAILABLE number(1,0),
        HPAMODEL varchar2(36 char),
        GAINSTEP number(6,2),
        HASLIMITER number(1,0),
        LINEARIZER number(1,0),
        MAXHPAPOWER number(6,1),
        MAXIBO number(6,2),
        MINIBO number(6,2),
        HJID number(19,0) not null,
        AMPLIFIER_SATELLITETYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table ASPECTCORRECTIONRESULTTYPE (
        HJID number(19,0) not null,
        DOWNLINKASPECTCORRECTION number(6,2),
        DOWNLINKFREESPACELOSS number(6,2),
        DOWNLINKPATHLOSS number(6,2),
        EARTHSTATIONUUID varchar2(36 char),
        GRIDFILEUSED varchar2(255 char),
        TRANSPONDERINTERMODULATION number(6,2),
        primary key (HJID)
    );

    create table ASSETCRITERIATYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ASSETTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table AUTHENTICATEDUSERTYPE (
        HJID number(19,0) not null,
        PASSWORDOBJECT varchar2(255 char),
        USERNAMEOBJECT varchar2(255 char),
        primary key (HJID)
    );

    create table BANDWIDTHSEGMENTCRITERIATYPE (
        SERVICEREQUEST varchar2(36 char),
        TRANSPONDER varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table BANDWIDTHSEGMENTMEASUREMENTT_0 (
        ASSUMEDDOWNLINKASPECTCORRECT_0 number(6,2),
        ASSUMEDDOWNLINKFREESPACELOSS number(6,2),
        MEASUREDDOWNLINKATMOSPHERICL_0 number(6,2),
        MEASUREDDOWNLINKEIRP number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table BANDWIDTHSEGMENTTYPE (
        BANDWIDTH number(11,4),
        DOWNLINKCENTREFREQUENCY number(11,4),
        LINEITEMID varchar2(255 char),
        RESERVEDDOWNLINKEIRP number(6,2),
        SAPTRANSACTIONID varchar2(255 char),
        SERVICEREQUEST varchar2(36 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        TRANSPONDER varchar2(36 char),
        TYPE_ varchar2(36 char),
        UPLINKCENTREFREQUENCY number(11,4),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table BASEBANDRESULTTYPE (
        HJID number(19,0) not null,
        CODINGTYPE varchar2(255 char),
        DESIREDTHRESHOLDEBOVERNO number(20,10),
        FRAMELENGTH varchar2(255 char),
        INFORMATIONRATE number(7,3),
        INNERCODEFECRATE number(20,10),
        MODULATIONSCHEME varchar2(36 char),
        OVERHEADRATE number(6,2),
        PILOTINSERTION number(1,0),
        REEDSOLOMONOUTERCODING number(20,10),
        TRANSMISSIONRATE number(20,10),
        primary key (HJID)
    );

    create table BEACONCONFIGURATIONTYPE (
        HJID number(19,0) not null,
        ANALOGMODULATION varchar2(36 char),
        STATUS varchar2(36 char),
        USAGE_ varchar2(255 char),
        primary key (HJID)
    );

    create table BEACONMEASUREMENTTYPE (
        ASSUMEDDOWNLINKASPECTCORRECT_0 number(6,2),
        ASSUMEDDOWNLINKPATHLOSS number(6,2),
        BEAMPEAKEIRP number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table BEACONTYPE (
        EIRP number(6,2),
        BANDWIDTH number(11,4),
        DOWNLINKFREQUENCY number(11,4),
        MODE_ varchar2(36 char),
        POLARISATION varchar2(36 char),
        HJID number(19,0) not null,
        BEAM_BEACONTYPE_HJID number(19,0),
        CONFIGURATION_BEACONTYPE_HJID number(19,0),
        BEACON_SATELLITETYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table BEAMCONFIGURATIONTYPE (
        AZIMUTH number(5,2),
        ELEVATION number(5,2),
        REGION varchar2(36 char),
        YAW number(5,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table BEAMCONTOURTYPE (
        HJID number(19,0) not null,
        GXT blob,
        NAME_ varchar2(255 char),
        TRANSPONDERINTERMODULATION number(6,2),
        primary key (HJID)
    );

    create table BEAMCRITERIATYPE (
        DIRECTION varchar2(255 char),
        SATELLITE varchar2(255 char),
        TRANSPONDER varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table BEAMTYPE (
        DIRECTION varchar2(255 char),
        FREQUENCYBAND varchar2(255 char),
        POLARISATION varchar2(36 char),
        HJID number(19,0) not null,
        BEAMPOINTING_BEAMTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table BUSINESSPARTNERCRITERIATYPE (
        HJID number(19,0) not null,
        CITY varchar2(255 char),
        COUNTRY varchar2(255 char),
        MAXELEMENTS number(20,0),
        WEBSITE varchar2(255 char),
        primary key (HJID)
    );

    create table BUSINESSPARTNERCRITERIATYPEI_0 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        IDITEMS_BUSINESSPARTNERCRITE_0 number(19,0),
        primary key (HJID)
    );

    create table BUSINESSPARTNERCRITERIATYPEN_0 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        NAMEITEMS_BUSINESSPARTNERCRI_0 number(19,0),
        primary key (HJID)
    );

    create table BUSINESSPARTNERTYPE (
        HJID number(19,0) not null,
        SESGS varchar2(255 char),
        CITY varchar2(255 char),
        COUNTRY varchar2(255 char),
        DELETED number(1,0),
        ID varchar2(255 char),
        NAME_ varchar2(255 char),
        POSTALCODE varchar2(255 char),
        POSTALCODELETTER varchar2(255 char),
        SALESREP varchar2(255 char),
        SHORTNAME varchar2(255 char),
        STATE_ varchar2(255 char),
        STREET varchar2(255 char),
        USEMBARGO number(1,0),
        WEBSITE varchar2(255 char),
        LEGALREGULATION_BUSINESSPART_0 number(19,0),
        primary key (HJID)
    );

    create table CAPACITYLINEITEMTYPE (
        CAPACITY number(20,10),
        DOWNLINKREGION varchar2(36 char),
        FLEET varchar2(36 char),
        FREQUENCYBAND varchar2(255 char),
        FULLTRANSPONDERREQUIRED number(1,0),
        ORBITALCHANNEL varchar2(36 char),
        ORBITALLOCATION number(20,10),
        PROTECTIONLEVEL varchar2(36 char),
        SATELLITE varchar2(36 char),
        UNIT varchar2(36 char),
        UPLINKREGION varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CARRIERCRITERIATYPE (
        BANDWIDTHSEGMENT varchar2(255 char),
        TRANSPONDER varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CARRIERMEASUREMENTTYPE (
        ASSUMEDDOWNLINKASPECTCORRECT_0 number(6,2),
        ASSUMEDDOWNLINKFREESPACELOSS number(6,2),
        ASSUMEDUPLINKASPECTCORRECTION number(6,2),
        ASSUMEDUPLINKFREESPACELOSS number(6,2),
        MEASUREDBER double precision,
        MEASUREDCOVERN number(6,2),
        MEASUREDDOWNLINKATMOSPHERICL_0 number(6,2),
        MEASUREDDOWNLINKEIRP number(6,2),
        MEASUREDEBNO number(6,2),
        MEASUREDNUMBEROFBITSPERSYMBOL float,
        MEASUREDOCCUPIEDBANDWIDTH number(11,4),
        MEASUREDSMALLSIGNALGAIN number(6,2),
        MEASUREDSYMBOLRATE number(8,3),
        MEASUREDUPLINKATMOSPHERICLOSS number(6,2),
        MEASUREDUPLINKEIRP number(6,2),
        NOMINALCOVERN number(6,2),
        NOMINALDOWNLINKEIRP number(6,2),
        NOMINALUPLINKEIRP number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CARRIERRECEIVEEARTHSTATIONTY_0 (
        PLANNEDCN number(6,2),
        PLANNEDDOWNLINKASPECTCORRECT_0 number(6,2),
        PLANNEDDOWNLINKAVAILABILITY number(6,2),
        PLANNEDDOWNLINKCOVERNPLUSONE number(6,2),
        PLANNEDDOWNLINKPATHLOSS number(6,2),
        PLANNEDEBNO number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CARRIERRESOURCEREQUIREMENTST_0 (
        HJID number(19,0) not null,
        ALLOCATEDBANDWIDTH number(11,4),
        DOWNLINKEIRP number(6,2),
        PEB number(11,4),
        PEBTOACTUALBANDWIDTHRATIO number(20,10),
        REQUIREDBANDWIDTH number(11,4),
        SYMBOLRATE number(8,3),
        primary key (HJID)
    );

    create table CARRIERRESULTTYPE (
        HJID number(19,0) not null,
        CARRIERRESOURCEREQUIREMENT_C_0 number(19,0),
        CARRIERRESULTS_LINKBUDGETRES_0 number(19,0),
        primary key (HJID)
    );

    create table CARRIERTRANSMITEARTHSTATIONT_0 (
        HPAADDITIONALMARGIN number(6,2),
        HPABACKOFF number(6,2),
        HPAPOSTLOSSES number(6,2),
        ISPRIMARY number(1,0),
        PLANNEDASPECTCORRECTION number(6,2),
        PLANNEDUPLINKAVAILABILITY number(6,2),
        PLANNEDUPLINKEIRP number(6,2),
        UPLINKCOVERNPLUSI number(6,2),
        UPLINKPOWERCONTROLENABLED number(1,0),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CARRIERTYPE (
        ACCESS_ varchar2(36 char),
        ALLOCATEDBANDWIDTH number(11,4),
        BANDWIDTHSEGMENT varchar2(36 char),
        CALCULATEDDOWNLINKEIRP number(6,2),
        DATARATE number(7,3),
        DESIGNATOR varchar2(255 char),
        DOWNLINKCENTREFREQUENCY number(11,4),
        DOWNLINKTARGETAVAILABILITY number(6,2),
        DVBSMODE varchar2(36 char),
        FRAMELENGTH varchar2(36 char),
        IMPLEMENTATIONMARGIN number(6,2),
        INNERCODEFECRATE number(20,10),
        INNERCODEFECTYPE varchar2(36 char),
        INPUTBACKOFF number(6,2),
        ISPCMA number(1,0),
        LINEITEMID varchar2(255 char),
        MODULATION varchar2(36 char),
        MONITORED number(1,0),
        OCCASIONALUSE number(1,0),
        OCCUPIEDBANDWIDTH number(11,4),
        OUTPUTBACKOFF number(6,2),
        OVERHEADDATARATE number(7,3),
        PREDICTEDBEAMPEAKDOWNLINKEIRP number(6,2),
        REEDSALOMON number(1,0),
        REEDSALOMONK number(10,0),
        REEDSALOMONN number(10,0),
        ROLLOFFFACTOR number(20,10),
        SAPTRANSACTIONID varchar2(255 char),
        SERVICEREQUEST varchar2(36 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        SPREADSPECTRUMFLAG number(1,0),
        SPREADINGFACTOR number(20,10),
        SYMBOLRATE number(8,3),
        TARGETPEB number(11,4),
        THRESHOLDEBNO number(6,2),
        TRANSPONDER varchar2(36 char),
        TYPE_ varchar2(36 char),
        UPLINKCENTREFREQUENCY number(11,4),
        UPLINKTARGETAVAILABILITY number(6,2),
        USEMBARGO number(1,0),
        USEPILOTS number(1,0),
        HJID number(19,0) not null,
        LINKBUDGETMODEM_CARRIERTYPE__0 number(19,0),
        primary key (HJID)
    );

    create table CARRIERTYPERECEIVEEARTHSTATI_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        RECEIVEEARTHSTATIONITEMS_CAR_0 number(19,0),
        primary key (HJID)
    );

    create table CARRIERTYPETRANSMITEARTHSTAT_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        TRANSMITEARTHSTATIONITEMS_CA_0 number(19,0),
        primary key (HJID)
    );

    create table CHANNELTYPE (
        BANDWIDTH number(11,4),
        CENTREFREQUENCY number(11,4),
        HJID number(19,0) not null,
        BEAM_CHANNELTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table CITYLOCATIONTYPE (
        HJID number(19,0) not null,
        ALTITUDE number(20,10),
        CAPITAL number(1,0),
        COUNTRY varchar2(255 char),
        LATITUDE number(20,10),
        LATITUDEDEGREE number(20,10),
        LATITUDEDIRECTION varchar2(255 char),
        LATITUDEMINUTES number(10,0),
        LATITUDESECONDS number(10,0),
        LONGITUDE number(20,10),
        LONGITUDEDEGREE number(20,10),
        LONGITUDEDIRECTION varchar2(255 char),
        LONGITUDEMINUTES number(10,0),
        LONGITUDESECONDS number(10,0),
        NAME_ varchar2(255 char),
        primary key (HJID)
    );

    create table CLBTSSDBCARRIERTYPE (
        BWSEGID varchar2(255 char),
        CONTRACTID varchar2(255 char),
        CONTRACTLINE varchar2(255 char),
        CXRAUPCFLG number(10,0),
        CXRBANDACTIVITYFAC double precision,
        CXRBANDFLG number(10,0),
        CXRBANDNUMCXRS number(10,0),
        CXRBANDWIDTHALLOC double precision,
        CXRBANDWIDTHOCPD double precision,
        CXRBPEIRPDLKPREDICTED double precision,
        CXRCENTERFREQDLK double precision,
        CXRCENTERFREQULK double precision,
        CXRCURRENTLFC varchar2(255 char),
        CXRDESIGNATOR varchar2(255 char),
        CXREIRPDLKHIGH double precision,
        CXREIRPDLKLOW double precision,
        CXREMBARGOFLG number(10,0),
        CXRENDDATEITEM timestamp,
        CXRID varchar2(255 char),
        CXRINPUTPWRBACKOFF double precision,
        CXRLINKBUDGETDATASOURCE varchar2(255 char),
        CXRMAXOUTAGE double precision,
        CXRMAXOUTAGEDLK double precision,
        CXRMAXOUTAGEULK double precision,
        CXRMODEMID varchar2(255 char),
        CXRMONITOREDFLG number(10,0),
        CXRMULTIACCESSTYPE varchar2(255 char),
        CXROCCUSEFLG number(10,0),
        CXROUTPUTPWRBACKOFF double precision,
        CXRREQAVAILABILITY double precision,
        CXRSTARTDATEITEM timestamp,
        CXRSTDID varchar2(255 char),
        CXRTHRESHOLDCN double precision,
        CXRTXEIRP double precision,
        CXRTYPE varchar2(255 char),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATGROUPNAME varchar2(255 char),
        SATID varchar2(255 char),
        SSDBENTITYID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        TXESACODE varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        XPDRID varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBCONFIGPLANTYPE (
        ACTUALSTARTDATEITEM timestamp,
        CONFIGPLANID varchar2(255 char),
        CURRENTLIFECYCLESTATE varchar2(255 char),
        EARLIESTSTARTDATEITEM timestamp,
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        LATESTSTARTDATEITEM timestamp,
        PLANDESC varchar2(255 char),
        SATID varchar2(255 char),
        SSDBENTITYID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBCONFIGSATTYPE (
        CONFIGPLANID varchar2(255 char),
        DEGOFINCLINATION double precision,
        INCLINATIONDATEITEM timestamp,
        INCLINEDORBFLG number(10,0),
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATID varchar2(255 char),
        SATLONGACTUAL double precision,
        SATLONGBETAF double precision,
        SATLONGNOM double precision,
        SATREGIONNAME varchar2(255 char),
        SATREGIONNAMECONTOUR varchar2(255 char),
        STATIONKEEPINGEW double precision,
        STATIONKEEPINGNS double precision,
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBCXRDIGITALTYPE (
        CODECCODERATE double precision,
        CXRDIGCODINGTYPE varchar2(255 char),
        CXRDIGDECODINGTYPE varchar2(255 char),
        CXRDIGINFORATE double precision,
        CXRDIGOVERHEAD double precision,
        CXRID varchar2(255 char),
        LASTUPDATETIMESTAMPITEM timestamp,
        MODULATIONTYPE varchar2(255 char),
        REQBER double precision,
        RSCODERATEK double precision,
        RSCODERATEN double precision,
        RSCODINGFLG number(10,0),
        SATID varchar2(255 char),
        SPREADINGFACTOR number(10,0),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBCXRRXESATYPE (
        CXRASPECTCORRDLK double precision,
        CXRID varchar2(255 char),
        CXRPATHLOSSDLK double precision,
        CXRRXESAPOL varchar2(255 char),
        CXRRXPREDICTEDBER double precision,
        CXRRXPREDICTEDCN double precision,
        CXRRXPREDICTEDCNO double precision,
        CXRRXPREDICTEDEBNO double precision,
        LASTUPDATETIMESTAMPITEM timestamp,
        RXESACODE varchar2(255 char),
        SATID varchar2(255 char),
        SSDBENTITYID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBLIFECYCLETYPE (
        LASTUPDATETIMESTAMPITEM timestamp,
        LIFECYCLECURRENTFLG number(10,0),
        LIFECYCLEENTITYID varchar2(255 char),
        LIFECYCLESTATECURRENT varchar2(255 char),
        LIFECYCLETRANSITIONDATEITEM timestamp,
        SSDBENTITYID varchar2(255 char),
        SSDBENTITYTYPE varchar2(255 char),
        TRANSITIONEDBY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBSATBIASTYPE (
        CONFIGPLANID varchar2(255 char),
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATID varchar2(255 char),
        SATPLATFBIASAZ double precision,
        SATPLATFBIASEL double precision,
        SATPLATFBIASYAW double precision,
        SYSTEMNAME varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBSATBMPOINTTYPE (
        BEAMDIRECTION varchar2(255 char),
        BEAMNAME varchar2(255 char),
        BEAMPOINTANGAZ double precision,
        BEAMPOINTANGEL double precision,
        BEAMPOINTANGYAW double precision,
        CONFIGPLANID varchar2(255 char),
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATGROUPNAME varchar2(255 char),
        SATID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBSATBMPOLTYPE (
        BEAMDIRECTION varchar2(255 char),
        BEAMNAME varchar2(255 char),
        BEAMPOL varchar2(255 char),
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATGROUPNAME varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBSATBMTYPE (
        BEAMCOVNAME varchar2(255 char),
        BEAMCOVNAMEINV varchar2(255 char),
        BEAMCOVTYPE varchar2(255 char),
        BEAMDESC varchar2(255 char),
        BEAMDIRECTION varchar2(255 char),
        BEAMDISPLAYPRIORITY number(10,0),
        BEAMFREQBAND varchar2(255 char),
        BEAMHASCONTOURFLG number(10,0),
        BEAMINVERTABLEFLG number(10,0),
        BEAMNAME varchar2(255 char),
        BEAMNAMEGAINGRID varchar2(255 char),
        BEAMNAMEPARENT varchar2(255 char),
        BEAMPOINTAZFLG number(10,0),
        BEAMPOINTELFLG number(10,0),
        BEAMPOINTYAWFLG number(10,0),
        BEAMREGIONINDEPFLG number(10,0),
        CLBTCONVFACT double precision,
        GAINGRIDFILEBETAF varchar2(255 char),
        GAINGRIDFILECONTOUR varchar2(255 char),
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATGROUPNAME varchar2(255 char),
        SSDBENTITYID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBSATFLIGHTTYPE (
        BEAMGROUPNAME varchar2(255 char),
        ENDOFLIFEDATEITEM timestamp,
        FILLFACTORBW double precision,
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        LAUNCHDATEITEM timestamp,
        ORGNAMEOWNER varchar2(255 char),
        SATDESC varchar2(255 char),
        SATDPLYSTATUS varchar2(255 char),
        SATID varchar2(255 char),
        SATIDGAINGRID varchar2(255 char),
        SATTYPENAME varchar2(255 char),
        SSDBENTITYID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBXPDRBACKOFFSCLBTTYPE (
        BEAMDIRECTION varchar2(255 char),
        BEAMNAME varchar2(255 char),
        INSTID number(20,0),
        SATGROUPNAME varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        XPDRIBO double precision,
        XPDROBO double precision,
        XPDROPMODENAME varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBXPDRBMCONNECTTYPE (
        BEAMDIRECTIONDLK varchar2(255 char),
        BEAMDIRECTIONULK varchar2(255 char),
        BEAMNAMEDLK varchar2(255 char),
        BEAMNAMEULK varchar2(255 char),
        CXRTOTALCOCHANINTFWORST double precision,
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATGROUPNAME varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        XPDRBEAMBANDWIDTHDLK double precision,
        XPDRBEAMBANDWIDTHULK double precision,
        XPDRBEAMBEGINFREQDLK double precision,
        XPDRBEAMBEGINFREQULK double precision,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBXPDRBMRESRCTYPE (
        BEAMDIRECTION varchar2(255 char),
        BEAMNAME varchar2(255 char),
        BEAMREGIONINDEPFLG number(10,0),
        CLBTCONVFACT double precision,
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATGROUPNAME varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        XPDRBEAMBANDWIDTH double precision,
        XPDRBEAMBANK varchar2(255 char),
        XPDRBEAMBEGINFREQ double precision,
        XPDRBEAMCENTERFREQ double precision,
        XPDRBEAMFREQBAND varchar2(255 char),
        XPDRBEAMFREQSLOT varchar2(255 char),
        XPDRBEAMNAME varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBXPDRDLKBMTYPE (
        BEAMDIRECTIONDLK varchar2(255 char),
        BEAMNAMEDLK varchar2(255 char),
        BEAMPOLDLK varchar2(255 char),
        CONFIGPLANID varchar2(255 char),
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATGROUPNAME varchar2(255 char),
        SATID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        XPDRBEAMBANDWIDTHDLK double precision,
        XPDRBEAMBEGINFREQDLK double precision,
        XPDRID varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBXPDRTYPE (
        AMPID varchar2(255 char),
        AMPOPERPOINT double precision,
        CONFIGPLANID varchar2(255 char),
        FILLFACTORBW double precision,
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        SATAMPID varchar2(255 char),
        SATID varchar2(255 char),
        SSDBENTITYID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        USERSTATUS varchar2(255 char),
        XPDRBANDWIDTH double precision,
        XPDRBEGINFREQDLK double precision,
        XPDRBPEIRPPREDICTED double precision,
        XPDRCHANNELBAND varchar2(255 char),
        XPDRDCPOWERWATTS double precision,
        XPDRDESC varchar2(255 char),
        XPDREIRPDLKHIGH double precision,
        XPDREIRPDLKLOW double precision,
        XPDRID varchar2(255 char),
        XPDRMONITOREDFLG number(10,0),
        XPDROPMODE varchar2(255 char),
        XPDROUTPUTBACKOFF double precision,
        XPDRSATROUTPUTPWR double precision,
        XPDRSHEDORDER double precision,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CLBTSSDBXPDRULKBMTYPE (
        BEAMDIRECTIONULK varchar2(255 char),
        BEAMNAMEULK varchar2(255 char),
        BEAMPOLULK varchar2(255 char),
        CONFIGPLANID varchar2(255 char),
        INSTID number(20,0),
        LASTUPDATETIMESTAMPITEM timestamp,
        RECEIVERID varchar2(255 char),
        SATGROUPNAME varchar2(255 char),
        SATID varchar2(255 char),
        SYSTEMNAME varchar2(255 char),
        UPDATEDBY varchar2(255 char),
        XPDRBEAMBANDWIDTHULK double precision,
        XPDRBEAMBEGINFREQULK double precision,
        XPDRGAINSTEPMODECURRENT varchar2(255 char),
        XPDRID varchar2(255 char),
        XPDROFFSETFREQ double precision,
        XPDRSFDCURRENT double precision,
        XPDRTRANSLATIONFREQ double precision,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table COMMANDCARRIERCONFIGURATIONT_0 (
        HJID number(19,0) not null,
        STATUS varchar2(36 char),
        primary key (HJID)
    );

    create table COMMANDCARRIERTYPE (
        BANDWIDTH number(11,4),
        POLARISATIONUUID varchar2(36 char),
        UPLINKCENTREFREQUENCY number(11,4),
        HJID number(19,0) not null,
        COMMANDCARRIERCONFIGURATION__0 number(19,0),
        COMMANDCARRIER_SATELLITETYPE_0 number(19,0),
        primary key (HJID)
    );

    create table CONFIGURABLEASSETCRITERIATYPE (
        VALIDFROMITEM date,
        VALIDTOITEM date,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CONFIGURABLEASSETCRITERIATYP_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        STATUSITEMS_CONFIGURABLEASSE_0 number(19,0),
        primary key (HJID)
    );

    create table CONFIGURABLEASSETTYPE (
        STATUS varchar2(36 char),
        STATUSDESCRIPTION varchar2(255 char),
        VALIDFROMITEM date,
        VALIDTOITEM date,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table CONTACTCRITERIATYPE (
        HJID number(19,0) not null,
        CONTACTROLE varchar2(255 char),
        EMAIL varchar2(255 char),
        FIRSTNAME varchar2(255 char),
        LASTNAME varchar2(255 char),
        MAXELEMENTS number(20,0),
        primary key (HJID)
    );

    create table CONTACTCRITERIATYPEBUSINESSP_0 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        BUSINESSPARTNERIDITEMS_CONTA_0 number(19,0),
        primary key (HJID)
    );

    create table CONTACTCRITERIATYPEIDITEM (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        IDITEMS_CONTACTCRITERIATYPE__0 number(19,0),
        primary key (HJID)
    );

    create table CONTACTROLETYPE (
        HJID number(19,0) not null,
        CONTACT varchar2(255 char),
        ROLE_ varchar2(36 char),
        CONTACTS_DATAPLATFORMTYPE_HJ_0 number(19,0),
        CONTACTS_EARTHSTATIONTYPE_HJ_0 number(19,0),
        primary key (HJID)
    );

    create table CONTACTTYPE (
        HJID number(19,0) not null,
        VCARD varchar2(255 char),
        BUSINESSPARTNERID varchar2(255 char),
        BUSINESSUNITUUID varchar2(36 char),
        ID varchar2(255 char),
        INTERNAL number(1,0),
        INTERNALES number(1,0),
        USEMBARGO number(1,0),
        LEGALREGULATION_CONTACTTYPE__0 number(19,0),
        primary key (HJID)
    );

    create table DATAPLATFORMCRITERIATYPE (
        DATARATETYPE varchar2(255 char),
        SATELLITE varchar2(255 char),
        SERVICEREGION varchar2(255 char),
        TELEPORT varchar2(255 char),
        TYPE_ varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table DATAPLATFORMTYPE (
        ALLOCATEDBANDWIDTH number(11,4),
        BAND varchar2(36 char),
        BANDWIDTHFACTOR number(20,10),
        DATARATE number(6,2),
        DATARATETYPE varchar2(36 char),
        DOWNLINKBEAM varchar2(36 char),
        RATECARDFACTOR number(20,10),
        SATELLITE varchar2(36 char),
        SERVICEREGION varchar2(36 char),
        TELEPORT varchar2(36 char),
        TRANSPONDER varchar2(36 char),
        TYPE_ varchar2(36 char),
        UPLINKBEAM varchar2(36 char),
        USEMBARGO number(1,0),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table DOWNLINKBUDGETRESULTTYPE (
        HJID number(19,0) not null,
        ASICOVERI number(20,10),
        COVERI number(6,2),
        COVERN number(6,2),
        COVERNPLUSI number(6,2),
        ASPECTCORRECTION number(20,10),
        ATMOSPHERICLOSS number(20,10),
        AVAILABILITY number(6,2),
        AVAILABILITYPERIOD varchar2(255 char),
        INTERFERENCECOVERT number(20,10),
        PATHLOSS number(20,10),
        POINTINGLOSS number(20,10),
        RAINMARGIN number(20,10),
        RECEIVEEARTHSTATIONCLEARSKYG_0 number(20,10),
        TARGETAVAILABILITY number(20,10),
        THERMALCOVERT number(20,10),
        TOTALCOVERNPLUSI number(20,10),
        TOTALCLEARSKYEBOVERNO number(20,10),
        TOTALPRIORASICOVERNPLUSI number(20,10),
        primary key (HJID)
    );

    create table DOWNLINKCHANNELTYPE (
        BEAMPEAKSATURATEDEIRP number(6,2),
        FCA number(6,2),
        FCACOMMAND number(10,0),
        FREQUENCYCONVERTERSTATUS varchar2(36 char),
        GUARDBAND number(11,4),
        LINEARIZERSTATUS varchar2(36 char),
        RFMUTE varchar2(36 char),
        HJID number(19,0) not null,
        AMPLIFIER_DOWNLINKCHANNELTYP_0 number(19,0),
        AMPLIFIERCONFIGURATION_DOWNL_0 number(19,0),
        FREQUENCYCONVERTER_DOWNLINKC_0 number(19,0),
        primary key (HJID)
    );

    create table DOWNLINKCONSTRAINTTYPE (
        EARTHSURFACEPSD number(20,10),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table DUMMYCAPACITYTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(255 char),
        primary key (HJID)
    );

    create table DVBMONITORINGANALYSISCONTROL_0 (
        HJID number(19,0) not null,
        CREATIONTIMEITEM timestamp,
        DONETIMEITEM timestamp,
        ERRORTIMEITEM timestamp,
        LOCKTIMEITEM timestamp,
        MONITORINGDATEITEM date,
        SERVICEID number(20,0),
        primary key (HJID)
    );

    create table DVBMONITORINGSTATUSCRITERIAT_0 (
        HJID number(19,0) not null,
        MONITORINGDATEITEM date,
        primary key (HJID)
    );

    create table DVBMONITORINGSTATUSCRITERIAT_1 (
        HJID number(19,0) not null,
        ITEM number(10,0),
        SERVICEIDITEMS_DVBMONITORING_0 number(19,0),
        primary key (HJID)
    );

    create table EARTHSTATIONANTENNAMODELCRIT_0 (
        MANUFACTURER varchar2(255 char),
        MAXPOWER number(6,2),
        MAXSIZE number(20,10),
        MINPOWER number(6,2),
        MINSIZE number(20,10),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table EARTHSTATIONANTENNAMODELTYPE (
        DESCRIPTION varchar2(255 char),
        MANUFACTURER varchar2(255 char),
        MAXDIMENSION number(8,2),
        MINDIMENSION number(8,2),
        MOUNT varchar2(36 char),
        OPTICALCONFIGURATION varchar2(36 char),
        RECEIVENUMBEROFPORTS number(10,0),
        SHAPE varchar2(36 char),
        SIDELOBEMASK varchar2(255 char),
        TRANSMITNUMBEROFPORTS number(10,0),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table EARTHSTATIONCRITERIATYPE (
        ANTENNAMODEL varchar2(255 char),
        APPLICANT varchar2(255 char),
        CUSTOMEREARTHSTATIONCODE varchar2(255 char),
        EARTHSTATIONCOMPANY varchar2(255 char),
        NETWORK varchar2(255 char),
        SESEARTHSTATIONCODE varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table EARTHSTATIONLOCATIONTYPE (
        HJID number(19,0) not null,
        ALTITUDE number(8,2),
        NEARESTCITY varchar2(255 char),
        LATITUDE_EARTHSTATIONLOCATIO_0 number(19,0),
        LONGITUDE_EARTHSTATIONLOCATI_0 number(19,0),
        primary key (HJID)
    );

    create table EARTHSTATIONNETWORKCRITERIAT_0 (
        OWNEROPERATOR varchar2(255 char),
        SUBNETWORK varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table EARTHSTATIONNETWORKTYPE (
        CONTROLCENTER varchar2(255 char),
        MANAGEMENTSYSTEM varchar2(36 char),
        NETWORKSYSTEMINTEGRATOR varchar2(36 char),
        OPERATOR_ varchar2(255 char),
        OWNER_ varchar2(255 char),
        REMOTECONFIGURATION number(1,0),
        REMOTEPOWEROFF number(1,0),
        TOPOLOGY varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table EARTHSTATIONPOINTINGTYPE (
        HJID number(19,0) not null,
        ELEVATIONANGLE number(5,2),
        SATELLITE varchar2(36 char),
        POINTING_EARTHSTATIONTYPE_HJ_0 number(19,0),
        primary key (HJID)
    );

    create table EARTHSTATIONRESULTTYPE (
        HJID number(19,0) not null,
        AZIMUTH number(20,10),
        CITY varchar2(255 char),
        CODE varchar2(255 char),
        DIAMETER number(8,2),
        ELEVATIONANGLE number(5,2),
        HPARATEDOUTPUTPOWER number(6,2),
        HPAREQUIREDOUTPUTPOWER number(6,2),
        LATITUDE number(20,10),
        LOCATIONNAME varchar2(255 char),
        LONGITUDE number(20,10),
        primary key (HJID)
    );

    create table EARTHSTATIONTYPE (
        IFDISTRIBUTIONTYPE varchar2(36 char),
        ANTENNAMODEL varchar2(36 char),
        APPLICANT varchar2(255 char),
        ASSOCIATEDSESBUSINESSUNIT varchar2(36 char),
        AUTOMATICULCONTROL number(1,0),
        COUNTRYOFREGISTRATION varchar2(36 char),
        CUSTOMEREARTHSTATIONCODE varchar2(255 char),
        DEMODULATORSTEPSIZE number(11,4),
        DEPLOYMENT varchar2(36 char),
        DOWNCONVERTERSTEPSIZE number(11,4),
        EARTHSTATIONCOMPANY varchar2(255 char),
        HPAPHASECOMBINED number(1,0),
        MANNING varchar2(36 char),
        MAXPOWERATFEED number(6,2),
        MAXPOWERAVAILABLE number(6,2),
        MOBILITY varchar2(36 char),
        MODULATORSTEPSIZE number(11,4),
        NUMBEROFUPCONVERTERS varchar2(36 char),
        OCCASIONALUSE number(1,0),
        OWNEROPERATOR varchar2(255 char),
        POLARIZATIONANTENNAFEEDSYSTEM varchar2(36 char),
        RECEPTIONTYPE varchar2(36 char),
        REMOTECONTROLPOSSIBLE number(1,0),
        REMOTEPOWEROFF number(1,0),
        SESEARTHSTATIONCODE varchar2(255 char),
        UPCONVERTERINTRANSMISSIONCHA_0 number(1,0),
        UPCONVERTERSTEPSIZE number(11,4),
        UPLINKPOWERCONTROLEXISTING number(1,0),
        UPLINKPOWERCONTROLRANGE number(6,2),
        USEMBARGO number(1,0),
        VSATNETWORK varchar2(36 char),
        VSATSUBNETWORK varchar2(255 char),
        HJID number(19,0) not null,
        HPA_EARTHSTATIONTYPE_HJID number(19,0),
        ANTENNACHARACTERISTICS_EARTH_0 number(19,0),
        LOCATION__EARTHSTATIONTYPE_H_0 number(19,0),
        primary key (HJID)
    );

    create table EARTHSTATIONTYPETRACKINGSYST_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        TRACKINGSYSTEMITEMS_EARTHSTA_0 number(19,0),
        primary key (HJID)
    );

    create table ELEMENTARYSTREAMCRITERIATYPE (
        SERVICE varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ELEMENTARYSTREAMTYPE (
        BITRATE number(7,3),
        ISHDTV number(1,0),
        ISTRANSRATED number(1,0),
        MAXBITRATE number(7,3),
        MINBITRATE number(7,3),
        PACKETID number(10,0),
        SERVICE varchar2(36 char),
        TYPE_ varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ELEMENTARYSTREAMTYPEENCRYPTI_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        ENCRYPTIONITEMS_ELEMENTARYST_0 number(19,0),
        primary key (HJID)
    );

    create table ENDUSERCRITERIATYPE (
        SAPTRANSACTIONID varchar2(255 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        CITY varchar2(255 char),
        CUSTOMER varchar2(255 char),
        INBOUNDSATELLITE varchar2(255 char),
        LINEITEMID varchar2(255 char),
        OUTBOUNDSATELLITE varchar2(255 char),
        PLATFORM varchar2(255 char),
        TELEPORT varchar2(255 char),
        VASDESIGNATOR varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ENDUSERTYPE (
        SAPTRANSACTIONID varchar2(255 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        CITY varchar2(255 char),
        CUSTOMER varchar2(255 char),
        INBOUNDSATELLITE varchar2(36 char),
        LINEITEMID varchar2(255 char),
        OUTBOUNDSATELLITE varchar2(36 char),
        PLATFORM varchar2(36 char),
        SERVICETYPE varchar2(36 char),
        TELEPORT varchar2(36 char),
        USEMBARGO number(1,0),
        VASDESIGNATOR varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ENTITYCRITERIATYPE (
        HJID number(19,0) not null,
        MAXELEMENTS number(20,0),
        primary key (HJID)
    );

    create table ENTITYCRITERIATYPEIDITEM (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        IDITEMS_ENTITYCRITERIATYPE_H_0 number(19,0),
        primary key (HJID)
    );

    create table ENTITYTYPE (
        HJID number(19,0) not null,
        UUID varchar2(36 char),
        NAME_ varchar2(255 char),
        primary key (HJID)
    );

    create table FAILUREMESSAGELISTTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FAILUREMESSAGEPHRASEABLETYPE (
        HJID number(19,0) not null,
        TEXT varchar2(255 char),
        primary key (HJID)
    );

    create table FAILUREMESSAGEPHRASEABLETYPE_0 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        PHRASEKEYITEMS_FAILUREMESSAG_0 number(19,0),
        primary key (HJID)
    );

    create table FAILUREMESSAGEPHRASETYPE (
        HJID number(19,0) not null,
        KEY_ varchar2(255 char),
        PHRASE varchar2(255 char),
        MESSAGEPHRASE_FAILUREMESSAGE_0 number(19,0),
        primary key (HJID)
    );

    create table FAILUREMESSAGETYPE (
        HJID number(19,0) not null,
        CODE varchar2(255 char),
        SEVERITY varchar2(255 char),
        SUPPORTINFORMATION_FAILUREME_0 number(19,0),
        WHATHAPPENED_FAILUREMESSAGET_0 number(19,0),
        WHATTODO_FAILUREMESSAGETYPE__0 number(19,0),
        WHYHAPPENED_FAILUREMESSAGETY_0 number(19,0),
        MESSAGE_FAILUREMESSAGELISTTY_0 number(19,0),
        primary key (HJID)
    );

    create table FDBEAMDOWNLINKMONITOREDTYPE (
        EIRPCONVERSIONFACTOR number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FDBEAMMONITOREDTYPE (
        HJID number(19,0) not null,
        AZIMUTH number(5,2),
        BEAMID varchar2(36 char),
        BEAMNAME varchar2(255 char),
        CENTREFREQUENCY number(20,10),
        ELEVATION number(5,2),
        POLARIZATION varchar2(255 char),
        PATTERNFILE_FDBEAMMONITOREDT_0 number(19,0),
        primary key (HJID)
    );

    create table FDBEAMUPLINKMONITOREDTYPE (
        GOVERTCONVERSIONFACTOR number(6,2),
        FLUXCONTROLLOSS number(6,2),
        FLUXDENSITYSATURATION number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FDCHANNELMONITOREDTYPE (
        HJID number(19,0) not null,
        BANDWIDTH number(11,4),
        CHANNELID varchar2(255 char),
        FREQUENCYTRANSLATION number(20,10),
        TRANSPONDERLINEARIZED number(1,0),
        DOWNLINKBEAM_FDCHANNELMONITO_0 number(19,0),
        UPLINKBEAM_FDCHANNELMONITORE_0 number(19,0),
        CHANNEL_FDSATELLITEMONITORED_0 number(19,0),
        primary key (HJID)
    );

    create table FDCHANNELPLANNEDPATTERNINFOT_0 (
        HJID number(19,0) not null,
        DLANT varchar2(255 char),
        DLFILE varchar2(255 char),
        DLFILETYPE varchar2(255 char),
        DLFREQ varchar2(255 char),
        DLPE varchar2(255 char),
        DLPITCH varchar2(255 char),
        DLPOL varchar2(255 char),
        DLROLL varchar2(255 char),
        DLYAW varchar2(255 char),
        EFACT varchar2(255 char),
        FFACT varchar2(255 char),
        GFACT varchar2(255 char),
        ULANT varchar2(255 char),
        ULFILE varchar2(255 char),
        ULFILETYPE varchar2(255 char),
        ULFREQ varchar2(255 char),
        ULPE varchar2(255 char),
        ULPITCH varchar2(255 char),
        ULPOL varchar2(255 char),
        ULROLL varchar2(255 char),
        ULYAW varchar2(255 char),
        AMP varchar2(255 char),
        CH varchar2(255 char),
        RCVR varchar2(255 char),
        CHANNELPATTERNINFO_FDSATELLI_0 number(19,0),
        primary key (HJID)
    );

    create table FDFLEETPATTERNINFOTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FDPATTERNFILETYPE (
        HJID number(19,0) not null,
        DIRECTION varchar2(255 char),
        FILEFORMAT varchar2(255 char),
        FILENAME varchar2(255 char),
        PATTERN blob,
        primary key (HJID)
    );

    create table FDSATELLITEMONITOREDTYPE (
        HJID number(19,0) not null,
        CHANGINGTIMEITEM timestamp,
        DOWNLINKBEAMPOINTINGERROR number(5,2),
        NUMBEROFCHANNELS number(10,0),
        ORBITALLOCATION number(5,2),
        PARAMETERFILENAME varchar2(255 char),
        SATELLITEID varchar2(36 char),
        SATELLITENAME varchar2(255 char),
        UPLINKBEAMPOINTINGERROR number(5,2),
        YAW number(5,2),
        MONITOREDPATTERN_FDFLEETPATT_0 number(19,0),
        primary key (HJID)
    );

    create table FDSATELLITEPLANNEDPATTERNINF_0 (
        HJID number(19,0) not null,
        NUMBEROFCHANNELS number(10,0),
        SATELLITENAME varchar2(255 char),
        SATELLITEUUID varchar2(36 char),
        PLANNEDPATTERN_FDFLEETPATTER_0 number(19,0),
        primary key (HJID)
    );

    create table FDSATELLITEPLANNEDPATTERNINF_1 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        PARAMETERFILENAMEITEMS_FDSAT_0 number(19,0),
        primary key (HJID)
    );

    create table FLEETCRITERIATYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FLEETDRIVEINCOMINGTRANSFERCO_0 (
        HJID number(19,0) not null,
        CREATIONTIMEITEM timestamp,
        DONETIMEITEM timestamp,
        ERRORTIMEITEM timestamp,
        LOCKTIMEITEM timestamp,
        SATELLITENAME varchar2(255 char),
        primary key (HJID)
    );

    create table FLEETTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FREQUENCYBANDCRITERIATYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table FREQUENCYBANDCRITERIATYPEORB_0 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        ORBITALLOCATIONITEMS_FREQUEN_0 number(19,0),
        primary key (HJID)
    );

    create table FREQUENCYBANDDEFINITIONTYPE (
        HJID number(19,0) not null,
        BANDWIDTH number(11,4),
        CENTREFREQUENCY number(11,4),
        ENDFREQUENCY number(11,4),
        NAME_ varchar2(255 char),
        STARTFREQUENCY number(11,4),
        primary key (HJID)
    );

    create table FREQUENCYBANDTYPE (
        ITURADARBAND varchar2(255 char),
        BANDWIDTH number(11,4),
        CENTREFREQUENCY number(11,4),
        DESCRIPTION varchar2(255 char),
        DIRECTIONUUID varchar2(36 char),
        HJID number(19,0) not null,
        SUBBAND_ORBITALLOCATIONTYPE__0 number(19,0),
        REGULATEDBAND_ORBITALLOCATIO_0 number(19,0),
        primary key (HJID)
    );

    create table FREQUENCYCONVERTERTYPE (
        FREQUENCYCONVERSIONSTAGE varchar2(36 char),
        HJID number(19,0) not null,
        FREQUENCYCONVERTER_SATELLITE_0 number(19,0),
        primary key (HJID)
    );

    create table HPATYPE (
        HJID number(19,0) not null,
        MODELTYPE varchar2(36 char),
        NUMBEROFHPA number(10,0),
        POWER number(6,2),
        primary key (HJID)
    );

    create table INTERFERENCETYPE (
        HJID number(19,0) not null,
        ADJACENTCARRIERINTERFERENCE number(20,10),
        ADJACENTCHANNELINTERFERENCE number(20,10),
        ADJACENTSATELLITEDOWNLINKINT_0 number(20,10),
        ADJACENTSATELLITEUPLINKINTER_0 number(20,10),
        COCHANNELINTERFERENCE number(20,10),
        TERRESTRIALDOWNLINKINTERFERE_0 number(20,10),
        TERRESTRIALUPLINKINTERFERENCE number(20,10),
        primary key (HJID)
    );

    create table LATITUDETYPE (
        HJID number(19,0) not null,
        DEGREE number(5,2),
        MINUTES number(10,0),
        SECONDS number(10,0),
        WITH_ varchar2(255 char),
        primary key (HJID)
    );

    create table LDANTENNABANDTYPE (
        HJID number(19,0) not null,
        AMBIENTTEMPERATURE number(20,10),
        FEEDLOSS number(20,10),
        FREQUENCYDOWNLINKFROM number(20,10),
        FREQUENCYDOWNLINKTO number(20,10),
        FREQUENCYUPLINKFROM number(20,10),
        FREQUENCYUPLINKTO number(20,10),
        LNANOISETEMPERATURE number(20,10),
        MISMATCHLOSSDIAMETER2MORGREA_0 number(20,10),
        MISMATCHLOSSDIAMETERLESSTHAN_0 number(20,10),
        NAME_ varchar2(255 char),
        primary key (HJID)
    );

    create table LDANTENNAGOVERTTYPE (
        HJID number(19,0) not null,
        FREQUENCY number(20,10),
        VALUE_ varchar2(255 char),
        primary key (HJID)
    );

    create table LDANTENNATYPE (
        HJID number(19,0) not null,
        ANTENNANOISETEMPERATURE number(20,10),
        ANTENNATEMPERATUREELEVATIONA_0 number(20,10),
        DIAMETER number(20,10),
        RECEIVEEFFICIENCY number(20,10),
        RECEIVEFREQUENCY number(20,10),
        RECEIVEGAIN number(20,10),
        TRANSMITEFFICIENCY number(20,10),
        TRANSMITFREQUENCY number(20,10),
        TRANSMITGAIN number(20,10),
        RECEIVEGOVERT_LDANTENNATYPE__0 number(19,0),
        ANTENNAS_LDANTENNABANDTYPE_H_0 number(19,0),
        primary key (HJID)
    );

    create table LDHPABACKOFFTYPE (
        HJID number(19,0) not null,
        DECIBEL number(20,10),
        OPERATIONALMODE varchar2(255 char),
        BACKOFF_LDHPATYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table LDHPABANDOUTPUTPOWERTYPE (
        HJID number(19,0) not null,
        FREQUENCYBANDNAME varchar2(255 char),
        OUTPUTPOWER number(20,10),
        FREQUENCYBANDOUTPUTPOWER_LDH_0 number(19,0),
        primary key (HJID)
    );

    create table LDHPATYPE (
        HJID number(19,0) not null,
        TYPE_ varchar2(255 char),
        primary key (HJID)
    );

    create table LDINTERFERENCEBEAMTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(255 char),
        TYPE_ varchar2(255 char),
        BEAM_LDINTERFERENCESPACECRAF_0 number(19,0),
        primary key (HJID)
    );

    create table LDINTERFERENCEEARTHSTATIONOP_0 (
        HJID number(19,0) not null,
        INTERMODULATION number(20,10),
        OPERATIONALMODE varchar2(255 char),
        EARTHSTATIONOPERATIONALMODE__0 number(19,0),
        primary key (HJID)
    );

    create table LDINTERFERENCEINTERFERENCEMA_0 (
        HJID number(19,0) not null,
        ADJACENTCARRIER number(20,10),
        ADJACENTCHANNEL number(20,10),
        COCHANNEL number(20,10),
        DOWNLINKBEAMTYPE varchar2(255 char),
        UPLINKBEAMTYPE varchar2(255 char),
        INTERFERENCEMAPPING_LDINTERF_0 number(19,0),
        primary key (HJID)
    );

    create table LDINTERFERENCEINTERMODULATIO_0 (
        HJID number(19,0) not null,
        FREQUENCYBANDNAME varchar2(255 char),
        OPERATIONALMODE varchar2(255 char),
        TRANSPONDERINTERMODULATION number(20,10),
        INTERMODULATIONBAND_LDINTERF_0 number(19,0),
        primary key (HJID)
    );

    create table LDINTERFERENCEOPERATIONALBAN_0 (
        HJID number(19,0) not null,
        ADJACENTSPACECRAFTDOWNLINK number(20,10),
        ADJACENTSPACECRAFTUPLINK number(20,10),
        FREQUENCYBANDNAME varchar2(255 char),
        TERRESTRIALDOWNLINK number(20,10),
        TERRESTRIALUPLINK number(20,10),
        OPERATIONALBAND_LDINTERFEREN_0 number(19,0),
        primary key (HJID)
    );

    create table LDINTERFERENCESPACECRAFTTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(255 char),
        SPACECRAFT_LDINTERFERENCETYP_0 number(19,0),
        primary key (HJID)
    );

    create table LDINTERFERENCETYPE (
        HJID number(19,0) not null,
        TYPE_ varchar2(255 char),
        primary key (HJID)
    );

    create table LEGALREGULATIONTYPE (
        HJID number(19,0) not null,
        BLOCKSTATUS varchar2(255 char),
        LEGALCOMMENTS varchar2(255 char),
        LEGALREGULATION varchar2(255 char),
        LEGALREGULATIONDESCRIPTION varchar2(255 char),
        RELEASEREASON varchar2(255 char),
        RELEASEREASONDESCRIPTION varchar2(255 char),
        primary key (HJID)
    );

    create table LIFECYCLEASSETCRITERIATYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table LIFECYCLEASSETCRITERIATYPELI_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        LIFECYCLESTATEITEMS_LIFECYCL_0 number(19,0),
        primary key (HJID)
    );

    create table LIFECYCLEASSETTYPE (
        LIFECYCLE varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table LIFECYCLESTATEDEFINITIONTYPE (
        HJID number(19,0) not null,
        UUID varchar2(36 char),
        NAME_ varchar2(255 char),
        primary key (HJID)
    );

    create table LIFECYCLESTATEENTRYDEFINITIO_0 (
        HJID number(19,0) not null,
        UUID varchar2(36 char),
        DESCRIPTION varchar2(255 char),
        IMAGEURL varchar2(255 char),
        INITIAL_ number(1,0),
        NAME_ varchar2(255 char),
        STATES_LIFECYCLESTATEDEFINIT_0 number(19,0),
        primary key (HJID)
    );

    create table LIFECYCLETRANSITIONTYPE (
        HJID number(19,0) not null,
        LIFECYCLEFROM varchar2(36 char),
        LIFECYCLETO varchar2(36 char),
        TRANSITIONDATEITEM date,
        LIFECYCLETRANSITION_LIFECYCL_0 number(19,0),
        primary key (HJID)
    );

    create table LINEITEMTYPE (
        HJID number(19,0) not null,
        CRMTBILLINGBLOCKSTATUS varchar2(255 char),
        ZZNI number(1,0),
        ZZOSP number(1,0),
        BILLINGBLOCKSTATUSREASON varchar2(255 char),
        COMMERCIALSTATUS varchar2(36 char),
        EMPLOYEE varchar2(255 char),
        ENDOFSERVICEITEM date,
        ID varchar2(255 char),
        PRODUCT varchar2(255 char),
        STARTOFSERVICEITEM date,
        USEMBARGO number(1,0),
        LEGALREGULATION_LINEITEMTYPE_0 number(19,0),
        CONTRACTLINES_QUOTATIONCONTR_0 number(19,0),
        primary key (HJID)
    );

    create table LINEITEMUPDATETYPE (
        HJID number(19,0) not null,
        COMMERCIALLIFECYCLESTATE varchar2(36 char),
        LINEITEMID varchar2(255 char),
        SAPTRANSACTIONID varchar2(255 char),
        SERVICEREQUEST varchar2(36 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        primary key (HJID)
    );

    create table LINKBUDGETCARRIERRESULTTYPE (
        HJID number(19,0) not null,
        OVERALLAVAILABILITY number(6,2),
        TRANSPONDERINTERMODULATION number(6,2),
        BANDWIDTHRESULT_LINKBUDGETCA_0 number(19,0),
        BASEBANDRESULT_LINKBUDGETCAR_0 number(19,0),
        DOWNLINKBUDGET_LINKBUDGETCAR_0 number(19,0),
        DOWNLINKCONSTRAINTS_LINKBUDG_0 number(19,0),
        DOWNLINKINTERFERENCES_LINKBU_0 number(19,0),
        RECEIVEEARTHSTATIONRESULT_LI_0 number(19,0),
        TRANSMITEARTHSTATIONRESULT_L_0 number(19,0),
        TRANSPONDERINTERFERENCES_LIN_0 number(19,0),
        UPLINKBUDGET_LINKBUDGETCARRI_0 number(19,0),
        UPLINKCONSTRAINTS_LINKBUDGET_0 number(19,0),
        UPLINKINTERFERENCES_LINKBUDG_0 number(19,0),
        primary key (HJID)
    );

    create table LINKBUDGETCONSTRAINTTYPE (
        HJID number(19,0) not null,
        ITULIMIT number(6,2),
        MARGINTOITULIMIT number(6,2),
        ONAXISPSD number(6,2),
        primary key (HJID)
    );

    create table LINKBUDGETGRIDFILETYPE (
        HJID number(19,0) not null,
        BEAMNAME varchar2(255 char),
        BEAMPOLARIZATION varchar2(255 char),
        CENTERFREQUENCY number(20,10),
        CONTENT blob,
        FILENAME varchar2(255 char),
        FILETYPE varchar2(255 char),
        FLEET varchar2(255 char),
        MAXIMUMGAIN number(20,10),
        MEASUREMENTTYPE varchar2(255 char),
        SATELLITE varchar2(255 char),
        primary key (HJID)
    );

    create table LINKBUDGETRESULTTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table LINKBUDGETRESULTTYPEWARNINGI_0 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        WARNINGITEMS_LINKBUDGETRESUL_0 number(19,0),
        primary key (HJID)
    );

    create table LNATYPE (
        ATTENUATION number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table LOGASSETTYPE (
        DESCRIPTION varchar2(255 char),
        HJID number(19,0) not null,
        ASSETCONTEXT_LOGENTRYTYPE_HJ_0 number(19,0),
        primary key (HJID)
    );

    create table LOGENTRYTYPE (
        HJID number(19,0) not null,
        BUSINESSRELATIONID varchar2(255 char),
        LOGSYSTEM varchar2(36 char),
        ASSET_LOGENTRYTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table LONGITUDETYPE (
        HJID number(19,0) not null,
        DEGREE number(5,2),
        LENGTH_ varchar2(255 char),
        MINUTES number(10,0),
        SECONDS number(10,0),
        primary key (HJID)
    );

    create table MEASUREMENTCRITERIATYPE (
        ENDTIMEITEM timestamp,
        MEASUREDASSETID varchar2(36 char),
        MEASUREDASSETTYPE varchar2(255 char),
        STARTTIMEITEM timestamp,
        HJID number(19,0) not null,
        MONITORINGPATH_MEASUREMENTCR_0 number(19,0),
        primary key (HJID)
    );

    create table MEASUREMENTTYPE (
        HJID number(19,0) not null,
        ASSETID varchar2(36 char),
        ID varchar2(36 char),
        SOURCE_ varchar2(255 char),
        TIMEITEM timestamp,
        MONITORINGPATHNAME_MEASUREME_0 number(19,0),
        SPECTRUMPLOT_MEASUREMENTTYPE_0 number(19,0),
        primary key (HJID)
    );

    create table MODEMTYPE (
        HJID number(19,0) not null,
        AIRINTERFACETYPE varchar2(255 char),
        CARRIERINFORATE number(7,3),
        CODECCODERATE number(20,10),
        FRAMELENGTH varchar2(255 char),
        NAME_ varchar2(255 char),
        REEDSALOMONCODERATEK number(20,10),
        REEDSALOMONCODERATEN number(20,10),
        REEDSALOMONCODINGFLAG number(1,0),
        ROLLOFFFACTOR number(20,10),
        USEPILOTS number(1,0),
        primary key (HJID)
    );

    create table MONICSINCOMINGTRANSFERCONTRO_0 (
        HJID number(19,0) not null,
        CREATIONTIMEITEM timestamp,
        DONETIMEITEM timestamp,
        ERRORTIMEITEM timestamp,
        LOCKTIMEITEM timestamp,
        RECINDEX number(20,0),
        TARGETMONICS varchar2(255 char),
        TYPE_ varchar2(255 char),
        primary key (HJID)
    );

    create table MONICSOUTGOINGTRANSFERCONTRO_0 (
        TARGETMONICS varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table MONICSSCDBBEACON (
        ACTIVE varchar2(255 char),
        ALLOCBW double precision,
        BEACONID varchar2(255 char),
        DLASPECTCORRECTION double precision,
        DLATMOSPHERICLOSS double precision,
        DLCF double precision,
        DLFREESPACELOSS double precision,
        ESID varchar2(255 char),
        MAXDLEIRP double precision,
        MINDLEIRP double precision,
        NOMDLEIRP double precision,
        POL varchar2(255 char),
        POLCODE varchar2(255 char),
        SATELLITEID varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table MONICSSCDBCARRIER (
        ACTIVE varchar2(255 char),
        ALLOCBW double precision,
        BITSSYMBOL number(20,0),
        CARRIERID varchar2(255 char),
        CARRIERSTANDARD varchar2(255 char),
        CUSTOMERNAME varchar2(255 char),
        DATARATE double precision,
        DELETEFROMMONICS number(20,0),
        DLCF double precision,
        ESID varchar2(255 char),
        FEC double precision,
        FECTYPE varchar2(255 char),
        MAXDLEIRP double precision,
        MINDLEIRP double precision,
        MODTYPE varchar2(255 char),
        MONBER varchar2(255 char),
        MONDIRECTION varchar2(255 char),
        MONPLAN varchar2(255 char),
        NOISEBW double precision,
        NOMDLEIRP double precision,
        NOMMONBER double precision,
        NOMMONCN double precision,
        NOMMONCNO double precision,
        NOMMONEBNO double precision,
        NOMULEIRP double precision,
        OCCBW double precision,
        OCCASIONALUSE varchar2(255 char),
        POLCODE varchar2(255 char),
        REEDSOLOMONK number(20,0),
        REEDSOLOMONN number(20,0),
        SATELLITEID varchar2(255 char),
        SPREADINGFACTOR number(20,0),
        STATUS varchar2(255 char),
        TRANSPONDERID varchar2(255 char),
        TYPE_ varchar2(255 char),
        ULCF double precision,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table MONICSSCDBTRANSPONDER (
        ASPECTCORRECTION double precision,
        ATMOSPHERICLOSS double precision,
        BEAMID varchar2(255 char),
        CUSTOMERNAME varchar2(255 char),
        DLENDFREQ double precision,
        DLPOLARIZATION varchar2(255 char),
        DLSTARTFREQ double precision,
        ESID varchar2(255 char),
        FREESPACELOSS double precision,
        MAXEIRP double precision,
        MINEIRP double precision,
        MONDIRECTION varchar2(255 char),
        MONPLAN varchar2(255 char),
        POLCODE varchar2(255 char),
        SATELLITEID varchar2(255 char),
        SATUREIRP double precision,
        STATUS varchar2(255 char),
        TRANSPONDERID varchar2(255 char),
        ULENDFREQ double precision,
        ULSTARTFREQ double precision,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table MONITORINGEARTHSTATIONDEFINI_0 (
        HJID number(19,0) not null,
        COVERN number(6,2),
        COVERNO number(6,2),
        ACTIVE number(1,0),
        ASPECTCORRECTION number(6,2),
        ATMOSPHERICLOSS number(6,2),
        EBNO number(6,2),
        FREESPACELOSS number(6,2),
        MONITORINGPATHNAME_MONITORIN_0 number(19,0),
        primary key (HJID)
    );

    create table MONITORINGPATHCRITERIATYPE (
        HJID number(19,0) not null,
        CSMSITE varchar2(36 char),
        MONITORINGPATH varchar2(255 char),
        primary key (HJID)
    );

    create table MONITORINGPATHTYPE (
        HJID number(19,0) not null,
        CSMSITE varchar2(36 char),
        MONITORINGPATH varchar2(255 char),
        primary key (HJID)
    );

    create table MULTIPLEXCRITERIATYPE (
        CARRIER varchar2(255 char),
        TRANSPONDER varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table MULTIPLEXTYPE (
        CARRIER varchar2(36 char),
        MULTIPLEXMARGIN number(7,3),
        NETWORKIDENTIFIER number(10,0),
        ORIGINALNETWORKIDENTIFIER number(10,0),
        TRANSPONDER varchar2(36 char),
        TRANSPORTSTREAMIDENTIFIER number(10,0),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table MULTIPLEXTYPEMULTIPLEXPOOLID_0 (
        HJID number(19,0) not null,
        ITEM number(10,0),
        MULTIPLEXPOOLIDITEMS_MULTIPL_0 number(19,0),
        primary key (HJID)
    );

    create table NAMEDENTITYCRITERIATYPE (
        NAME_ varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ORBITALCHANNELCRITERIATYPE (
        ORBITALLOCATION varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ORBITALCHANNELTYPE (
        BANDWIDTH number(11,4),
        CENTREFREQUENCY number(11,4),
        DOWNLINKREGION varchar2(36 char),
        ORBITALPOSITION varchar2(36 char),
        POLARISATION varchar2(36 char),
        HJID number(19,0) not null,
        CHANNEL_ORBITALLOCATIONTYPE__0 number(19,0),
        primary key (HJID)
    );

    create table ORBITALLOCATIONCRITERIATYPE (
        FLEET varchar2(255 char),
        LONGITUDE varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ORBITALLOCATIONTYPE (
        ORBITALLOCATION number(5,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ORGANIZATION (
        HJID number(19,0) not null,
        DESCRIPTION varchar2(255 char),
        ID varchar2(255 char),
        primary key (HJID)
    );

    create table PAYLOADTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table PERSON (
        HJID number(19,0) not null,
        ACCOUNTNAME varchar2(255 char),
        COMPANYNAME varchar2(255 char),
        ID varchar2(255 char),
        NAME_ varchar2(255 char),
        OFFICECOUNTRY varchar2(255 char),
        OFFICELOCATION varchar2(255 char),
        OFFICENUMBER varchar2(255 char),
        OFFICEPHONENUMBER varchar2(255 char),
        TITLE varchar2(255 char),
        USCITIZEN number(1,0),
        primary key (HJID)
    );

    create table PLANNABLEASSETCRITERIATYPE (
        PLANNEDENDDATEITEM date,
        PLANNEDSTARTDATEITEM date,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table PLANNABLEASSETTYPE (
        PLANNEDSTARTDATEITEM date,
        PLANNEDSTOPDATEITEM date,
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table PLATFORMLINEITEMTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table PROPERTYTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(255 char),
        primary key (HJID)
    );

    create table PROPERTYTYPEVALUEITEM (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        VALUEITEMS_PROPERTYTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table QUOTATIONCONTRACTCRITERIATYPE (
        HJID number(19,0) not null,
        CUSTOMER varchar2(255 char),
        EARLIESTEXPIRYDATEITEM date,
        LATESTCUSTOMERSTARTDATEITEM date,
        MAXELEMENTS number(20,0),
        ORBITALLOCATION varchar2(255 char),
        SAPTRANSACTIONID varchar2(255 char),
        SATELLITE varchar2(255 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        primary key (HJID)
    );

    create table QUOTATIONCONTRACTCRITERIATYP_0 (
        HJID number(19,0) not null,
        ITEM varchar2(255 char),
        LINEITEMIDITEMS_QUOTATIONCON_0 number(19,0),
        primary key (HJID)
    );

    create table QUOTATIONCONTRACTTYPE (
        HJID number(19,0) not null,
        CRMTBILLINGBLOCKSTATUS varchar2(255 char),
        SESGS number(1,0),
        ZZNI number(1,0),
        ZZOSP number(1,0),
        BILLINGBLOCKSTATUSREASON varchar2(255 char),
        CONTRACTDOCUMENT varchar2(255 char),
        CRMWEBUITRANSACTION varchar2(255 char),
        CUSTOMER varchar2(255 char),
        CUSTOMERSTARTDATEITEM date,
        EARLYTERMINATIONDATEITEM date,
        EMPLOYEE varchar2(255 char),
        EXPIRYDATEITEM date,
        SALESOFFICE varchar2(255 char),
        SALESSTAGE varchar2(255 char),
        SAPTRANSACTIONID varchar2(255 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        SIGNATUREDATEITEM date,
        USEMBARGO number(1,0),
        LEGALREGULATION_QUOTATIONCON_0 number(19,0),
        primary key (HJID)
    );

    create table RECEIVEBANDPARAMETERTYPE (
        HJID number(19,0) not null,
        GOVERT number(6,2),
        GOVERTATELEVATION number(5,2),
        GOVERTATFREQUENCY number(20,10),
        LNALNBTEMPERATURE number(4,1),
        BANDWIDTH number(11,4),
        CENTERFREQUENCY number(20,10),
        NOISEFIGURE float,
        NOISETEMPERATURE number(4,1),
        ONAXISCROSSPOLEISOLATION number(6,2),
        OPERATINGBANDID varchar2(36 char),
        RECEIVEANTENNAGAIN number(6,2),
        RECEIVEANTENNAGAINAT number(11,4),
        RECEIVEBANDPERFORMANCE_EARTH_0 number(19,0),
        primary key (HJID)
    );

    create table RECEIVEEARTHSTATIONRESULTTYPE (
        CLEARSKYRECEIVEGOVERT number(20,10),
        DEGRADEDGOVERT number(20,10),
        DOWNLINKASPECTCORRECTION number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table REFERENCEDATACRITERIATYPE (
        ACTIVE number(1,0),
        CATEGORY varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table REFERENCEDATATYPE (
        ACTIVE number(1,0),
        CATEGORY varchar2(255 char),
        DESCRIPTION varchar2(255 char),
        DISPLAYVALUE varchar2(255 char),
        ID varchar2(36 char),
        VALUE_ varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table ROLERIGHTITEM (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        RIGHTITEMS_ROLE__HJID number(19,0),
        primary key (HJID)
    );

    create table ROLE_ (
        HJID number(19,0) not null,
        DESCRIPTION varchar2(255 char),
        ID varchar2(36 char),
        primary key (HJID)
    );

    create table SAPBEAMTYPE (
        DIRECTION varchar2(255 char),
        NAME_ varchar2(255 char),
        POINTINGREGION varchar2(36 char),
        POLARIZATION varchar2(36 char),
        SATELLITE varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SAPDATAPLATFORMTYPE (
        ALLOCATEDBANDWIDTH number(11,4),
        BANDENUM varchar2(36 char),
        DATARATEKBPS number(6,2),
        DOWNLINKBEAM varchar2(36 char),
        NAME_ varchar2(255 char),
        SATELLITE varchar2(36 char),
        TELEPORT varchar2(36 char),
        TRANSPONDER varchar2(36 char),
        UPLINKBEAM varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SAPENUMERATIONTYPE (
        CATEGORY varchar2(255 char),
        VALUE_ varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SAPORBITALCHANNELTYPE (
        BANDENUM varchar2(36 char),
        BANDWIDTH number(20,10),
        CENTREFREQUENCY number(20,10),
        ITURADARBANDENUM varchar2(36 char),
        NAME_ varchar2(255 char),
        ORBITALLOCATION number(20,10),
        POLARIZATION varchar2(36 char),
        REGION varchar2(36 char),
        SATELLITE varchar2(36 char),
        TRANSPONDER varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SAPOUTGOINGTRANSFERCONTROLLI_0 (
        HJID number(19,0) not null,
        ACTIONTYPE varchar2(255 char),
        CREATIONTIMEITEM timestamp,
        DONETIMEITEM timestamp,
        ERRORTIMEITEM timestamp,
        LATESTRETURNCODE varchar2(255 char),
        LATESTTRANSFERTRIALTIMEITEM timestamp,
        LOCKTIMEITEM timestamp,
        NUMBEROFTRIALS number(10,0),
        TYPEORDER number(10,0),
        USERNAME varchar2(255 char),
        OUTGOING_SAPOUTGOINGTRANSFER_0 number(19,0),
        primary key (HJID)
    );

    create table SAPSATELLITETYPE (
        FLEETNAME varchar2(255 char),
        NAME_ varchar2(255 char),
        OPERATIONALSERVICEDATEITEM timestamp,
        ORBITALLOCATION number(20,10),
        ORBITALTOLERANCE number(20,10),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SAPTELEPORTTYPE (
        NAME_ varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SAPTRANSFERASSETTYPE (
        HJID number(19,0) not null,
        DELETED number(1,0),
        STATUS varchar2(36 char),
        UUID varchar2(36 char),
        primary key (HJID)
    );

    create table SAPTRANSPONDERBEAMTYPE (
        BEAM varchar2(36 char),
        TRANSPONDER varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SAPTRANSPONDERTYPE (
        DOWNLINKFREQUENCYBANDNAME varchar2(255 char),
        NAME_ varchar2(255 char),
        ORBITALCHANNEL varchar2(36 char),
        SATELLITE varchar2(36 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SATELLITECRITERIATYPE (
        EASTERNLOCATIONLIMIT number(5,2),
        FLEET varchar2(255 char),
        WESTERNLOCATIONLIMIT number(5,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SATELLITETYPE (
        ENDOFLIFEITEM date,
        FLEETUUID varchar2(36 char),
        INCLINATION number(5,2),
        INCLINATIONDATEITEM date,
        INCLINEDORBITOPERATIONS number(1,0),
        LAUNCHDATEITEM date,
        LAUNCHMASS number(6,1),
        LAUNCHSITEUUID varchar2(36 char),
        LAUNCHVEHICLEUUID varchar2(36 char),
        LONGITUDE number(5,2),
        NOMINALEWSTATIONKEEPINGLIMIT number(5,2),
        NOMINALNSSTATIONKEEPINGLIMIT number(5,2),
        ORBIT varchar2(36 char),
        ORIGINALNAME varchar2(255 char),
        PITCH number(5,2),
        ROLL number(5,2),
        YAW number(5,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SERVICEBUSDATAPLATFORMTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SERVICEBUSENDUSERTYPE (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SERVICECRITERIATYPE (
        MULTIPLEX varchar2(255 char),
        SERVICEREQUEST varchar2(36 char),
        TRANSPONDER varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SERVICEREQUESTCRITERIATYPE (
        BUSINESSUNIT varchar2(36 char),
        LINEITEMID varchar2(255 char),
        PROTECTIONLEVEL varchar2(36 char),
        SAPTRANSACTIONID varchar2(255 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        USEMBARGO number(1,0),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SERVICEREQUESTTYPE (
        BANDWIDTHREQUIRED number(11,4),
        BITRATEREQUIRED number(7,3),
        BUSINESSUNIT varchar2(36 char),
        CUSTOMER varchar2(255 char),
        DOWNLINKREGION varchar2(36 char),
        ENDOFSERVICEITEM date,
        FLEET varchar2(36 char),
        FREQUENCYBAND varchar2(36 char),
        FULLTRANSPONDERREQUIRED number(1,0),
        LINEITEMEMPLOYEE varchar2(255 char),
        LINEITEMID varchar2(255 char),
        MAXREACTIVATIONDAYS number(10,0),
        ORBITALCHANNEL varchar2(36 char),
        ORBITALLOCATION varchar2(36 char),
        PREEMPTIONDAYS number(10,0),
        PRODUCT varchar2(36 char),
        PROTECTIONLEVEL varchar2(36 char),
        QUOTATIONCONTRACTEMPLOYEE varchar2(255 char),
        SALESOFFICE varchar2(255 char),
        SALESSTAGE varchar2(255 char),
        SAPTRANSACTIONID varchar2(255 char),
        SATELLITE varchar2(36 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        STARTOFSERVICEITEM date,
        UPLINKREGION varchar2(36 char),
        USEMBARGO number(1,0),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SERVICEREQUESTTYPEBANDWIDTHS_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        BANDWIDTHSEGMENTITEMS_SERVIC_0 number(19,0),
        primary key (HJID)
    );

    create table SERVICEREQUESTTYPECARRIERITEM (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        CARRIERITEMS_SERVICEREQUESTT_0 number(19,0),
        primary key (HJID)
    );

    create table SERVICEREQUESTTYPESERVICEINM_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        SERVICEINMULTIPLEXITEMS_SERV_0 number(19,0),
        primary key (HJID)
    );

    create table SERVICETASKTYPE (
        CREATEDBY varchar2(255 char),
        CREATEDITEM date,
        DUEDATEITEM date,
        LIFECYCLESTATE varchar2(36 char),
        LINEITEMID varchar2(255 char),
        SAPTRANSACTIONID varchar2(255 char),
        SERVICEREQUESTID varchar2(36 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        TASKTYPE varchar2(36 char),
        HJID number(19,0) not null,
        SERVICETASK_SERVICEREQUESTTY_0 number(19,0),
        primary key (HJID)
    );

    create table SERVICETYPE (
        AGREEMENTREFERENCE varchar2(255 char),
        AGREEMENTTYPE varchar2(255 char),
        AVERAGEVIDEOSTREAMBITRATE number(7,3),
        BITRATE number(7,3),
        GENRE varchar2(36 char),
        INTERACTIVETV number(1,0),
        INVOICETAGREQUIRED number(1,0),
        LINEITEMID varchar2(255 char),
        MILLIONSERVICENUMBER varchar2(255 char),
        MULTIPLEX varchar2(36 char),
        MULTIPLEXPOOL number(10,0),
        PREVIOUSNAME varchar2(255 char),
        PROVIDERNAME varchar2(255 char),
        QUALITY number(6,2),
        SAPTRANSACTIONID varchar2(255 char),
        SERVICEID number(10,0),
        SERVICEREQUEST varchar2(36 char),
        SESTRANSACTIONNUMBER varchar2(255 char),
        STATISTICALLYMULTIPLEXED number(1,0),
        SUBJECTTOMONTHLYREPORTING number(1,0),
        TIMESHAREDGROUP varchar2(255 char),
        TRANSMISSIONHOURS varchar2(255 char),
        TYPE_ varchar2(36 char),
        WEBSITE varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table SPECTRUMPLOTCRITERIATYPE (
        ASSETID varchar2(36 char),
        ENDTIMEITEM timestamp,
        STARTTIMEITEM timestamp,
        HJID number(19,0) not null,
        MONITORINGPATH_SPECTRUMPLOTC_0 number(19,0),
        primary key (HJID)
    );

    create table SPECTRUMPLOTTYPE (
        HJID number(19,0) not null,
        CENTERFREQUENCY number(11,4),
        ID varchar2(36 char),
        MAXVALUE_ number(6,2),
        MINVALUE_ number(6,2),
        NUMPOINTS number(20,0),
        SPAN number(11,4),
        primary key (HJID)
    );

    create table SPECTRUMPLOTTYPETRACEDATAITEM (
        HJID number(19,0) not null,
        ITEM number(6,2),
        TRACEDATAITEMS_SPECTRUMPLOTT_0 number(19,0),
        primary key (HJID)
    );

    create table SUBNETWORKTYPE (
        HJID number(19,0) not null,
        NAME_ varchar2(255 char),
        SUBNETWORK_EARTHSTATIONNETWO_0 number(19,0),
        primary key (HJID)
    );

    create table SUBNETWORKTYPEEARTHSTATIONIT_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        EARTHSTATIONITEMS_SUBNETWORK_0 number(19,0),
        primary key (HJID)
    );

    create table SUMMARYLINKBUDGETRESULTTYPE (
        HJID number(19,0) not null,
        COVERT number(20,10),
        AVAILABILITY number(20,10),
        EBNO number(20,10),
        primary key (HJID)
    );

    create table TICKETASSETTYPE (
        DESCRIPTION varchar2(255 char),
        HJID number(19,0) not null,
        ASSETCONTEXT_TICKETENTRYTYPE_0 number(19,0),
        primary key (HJID)
    );

    create table TICKETCRITERIATYPE (
        HJID number(19,0) not null,
        MAXELEMENTS number(20,0),
        TICKETID varchar2(255 char),
        primary key (HJID)
    );

    create table TICKETCRITERIATYPEASSETUUIDI_0 (
        HJID number(19,0) not null,
        ITEM varchar2(36 char),
        ASSETUUIDITEMS_TICKETCRITERI_0 number(19,0),
        primary key (HJID)
    );

    create table TICKETENTRYTYPE (
        HJID number(19,0) not null,
        SEVERITY number(10,0),
        TICKETID varchar2(255 char),
        TICKETSYSTEM varchar2(36 char),
        ASSET_TICKETENTRYTYPE_HJID number(19,0),
        primary key (HJID)
    );

    create table TICKETINFOTYPE (
        HJID number(19,0) not null,
        ASSETUUID varchar2(36 char),
        CLOSED number(1,0),
        CREATEDBY varchar2(255 char),
        CREATIONDATEITEM timestamp,
        DESCRIPTION varchar2(255 char),
        SEVERITY number(10,0),
        TICKETID varchar2(255 char),
        primary key (HJID)
    );

    create table TRANSFERCONTROLLINGTRANSFERA_0 (
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table TRANSFERCONTROLLINGTYPE (
        HJID number(19,0) not null,
        CREATIONTIMEITEM timestamp,
        DONETIMEITEM timestamp,
        ERRORTIMEITEM timestamp,
        LASTESTTRANSFERTRIALTIMEITEM timestamp,
        LOCKTIMEITEM timestamp,
        NUMBEROFTRIALS number(10,0),
        USERNAME varchar2(255 char),
        OUTGOING_TRANSFERCONTROLLING_0 number(19,0),
        primary key (HJID)
    );

    create table TRANSMISSIONPATHRESULTTYPE (
        HJID number(19,0) not null,
        OVERALLAVAILABILITY number(6,2),
        BASEBANDRESULT_TRANSMISSIONP_0 number(19,0),
        DOWNLINKBUDGET_TRANSMISSIONP_0 number(19,0),
        DOWNLINKCONSTRAINTS_TRANSMIS_0 number(19,0),
        DOWNLINKINTERFERENCES_TRANSM_0 number(19,0),
        RECEIVEEARTHSTATIONRESULT_TR_0 number(19,0),
        TRANSMITEARTHSTATIONRESULT_T_0 number(19,0),
        TRANSPONDERINTERFERENCES_TRA_0 number(19,0),
        UPLINKBUDGET_TRANSMISSIONPAT_0 number(19,0),
        UPLINKCONSTRAINTS_TRANSMISSI_0 number(19,0),
        UPLINKINTERFERENCES_TRANSMIS_0 number(19,0),
        TRANSMISSIONPATHRESULT_CARRI_0 number(19,0),
        primary key (HJID)
    );

    create table TRANSMISSIONPATHTYPE (
        HJID number(19,0) not null,
        RECEIVEEARTHSTATION_TRANSMIS_0 number(19,0),
        TRANSMITEARTHSTATION_TRANSMI_0 number(19,0),
        primary key (HJID)
    );

    create table TRANSMITBANDPARAMETERTYPE (
        HJID number(19,0) not null,
        BANDWIDTH number(11,4),
        CENTERFREQUENCY number(20,10),
        MAXPERMITTEDEIRP number(6,2),
        ONAXISCROSSPOLEISOLATION number(6,2),
        OPERATINGBANDID varchar2(36 char),
        TRANSMITANTENNAGAIN number(6,2),
        TRANSMITANTENNAGAINAT number(11,4),
        TRANSMITBANDPERFORMANCE_EART_0 number(19,0),
        primary key (HJID)
    );

    create table TRANSMITEARTHSTATIONRESULTTY_0 (
        EARTHSTATIONINTERMODULATION number(20,10),
        UPLINKASPECTCORRECTION number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table TRANSPONDERCRITERIATYPE (
        PAYLOAD varchar2(255 char),
        SATELLITE varchar2(255 char),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table TRANSPONDERMEASUREMENTTYPE (
        ASSUMEDDOWNLINKASPECTCORRECT_0 number(6,2),
        ASSUMEDDOWNLINKFREESPACELOSS number(6,2),
        MEASUREDBEAMPEAKEIRP number(6,2),
        MEASUREDDOWNLINKATMOSPHERICL_0 number(6,2),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table TRANSPONDERRESULTTYPE (
        HJID number(19,0) not null,
        TRANSPONDERINTERMODULATION number(20,10),
        primary key (HJID)
    );

    create table TRANSPONDERTYPE (
        ALCENABLED varchar2(36 char),
        AMPLIFIERGAINSTEP number(6,2),
        AMPLIFIERPOWERSETTING number(6,1),
        INPUTBACKOFF number(6,2),
        LIMITER number(1,0),
        OPERATIONALMODE varchar2(36 char),
        OUTPUTBACKOFF number(6,2),
        PATHUPLINKCHANNEL varchar2(36 char),
        PAYLOAD varchar2(36 char),
        SATELLITE varchar2(36 char),
        HJID number(19,0) not null,
        DOWNLINKCHANNEL_TRANSPONDERT_0 number(19,0),
        primary key (HJID)
    );

    create table UPLINKBUDGETRESULTTYPE (
        HJID number(19,0) not null,
        COVERI number(6,2),
        COVERN number(6,2),
        COVERNPLUSI number(6,2),
        PEB number(20,10),
        ASPECTCORRECTION number(20,10),
        ATMOSPHERICLOSS number(20,10),
        AVAILABILITY number(6,2),
        AVAILABILITYPERIOD varchar2(255 char),
        CARRIERBEAMCENTREDOWNLINKEIRP number(20,10),
        CARRIERIBO number(20,10),
        CARRIEROBO number(20,10),
        EARTHSTATIONTRANSMITEIRPPERC_0 number(20,10),
        INTERFERENCECOVERT number(20,10),
        PATHLOSS number(20,10),
        PERCARRIERFLUXDENSITY number(20,10),
        POINTINGLOSS number(20,10),
        RAINMARGIN number(20,10),
        THERMALCOVERT number(20,10),
        primary key (HJID)
    );

    create table UPLINKCHANNELTYPE (
        GOVERT number(6,2),
        FREQUENCYCONVERTERSTATUS varchar2(36 char),
        LNASTATUS varchar2(36 char),
        SATURATEDFLUXDENSITY number(6,2),
        TRANSLATIONFREQUENCY number(11,4),
        HJID number(19,0) not null,
        LNA_UPLINKCHANNELTYPE_HJID number(19,0),
        FREQUENCYCONVERTER_UPLINKCHA_0 number(19,0),
        UPLINKCHANNEL_TRANSPONDERTYP_0 number(19,0),
        primary key (HJID)
    );

    create table UPLINKCONSTRAINTTYPE (
        OFFAXISEIRPDENSITY number(20,10),
        HJID number(19,0) not null,
        primary key (HJID)
    );

    create table USEMBARGOENHANCEDTYPE (
        HJID number(19,0) not null,
        ENTITYTYPE varchar2(255 char),
        USEMBARGO number(1,0),
        UUID varchar2(36 char),
        primary key (HJID)
    );

    create table UUIDTRANSLATIONTYPE (
        HJID number(19,0) not null,
        SYSTEM_ varchar2(255 char),
        UNIQUENAME varchar2(255 char),
        UUID varchar2(36 char),
        primary key (HJID)
    );

    alter table ADDITIONALATTRIBUTECRITERIAT_1 
        add constraint FKE198B1F252EB530C 
        foreign key (ASSETUUIDITEMS_ADDITIONALATT_0) 
        references ADDITIONALATTRIBUTECRITERIAT_0;

    alter table AMPLIFIERTYPE 
        add constraint FK2C1F190B34A3A597 
        foreign key (AMPLIFIER_SATELLITETYPE_HJID) 
        references SATELLITETYPE;

    alter table AMPLIFIERTYPE 
        add constraint FK2C1F190B8CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table ASSETCRITERIATYPE 
        add constraint FK5937C9E95ECF2F6F 
        foreign key (HJID) 
        references NAMEDENTITYCRITERIATYPE;

    alter table ASSETTYPE 
        add constraint FKF48C920A3180C83D 
        foreign key (HJID) 
        references ENTITYTYPE;

    alter table BANDWIDTHSEGMENTCRITERIATYPE 
        add constraint FKC48F517B3D24FD65 
        foreign key (HJID) 
        references PLANNABLEASSETCRITERIATYPE;

    alter table BANDWIDTHSEGMENTMEASUREMENTT_0 
        add constraint FKC73D13CBB5D0800D 
        foreign key (HJID) 
        references MEASUREMENTTYPE;

    alter table BANDWIDTHSEGMENTTYPE 
        add constraint FK3D25CB9C77DB7066 
        foreign key (HJID) 
        references PLANNABLEASSETTYPE;

    alter table BEACONMEASUREMENTTYPE 
        add constraint FK9D90F812B5D0800D 
        foreign key (HJID) 
        references MEASUREMENTTYPE;

    alter table BEACONTYPE 
        add constraint FKB510857E729D454A 
        foreign key (BEAM_BEACONTYPE_HJID) 
        references BEAMTYPE;

    alter table BEACONTYPE 
        add constraint FKB510857E800A64EE 
        foreign key (CONFIGURATION_BEACONTYPE_HJID) 
        references BEACONCONFIGURATIONTYPE;

    alter table BEACONTYPE 
        add constraint FKB510857EEA45FCE4 
        foreign key (BEACON_SATELLITETYPE_HJID) 
        references SATELLITETYPE;

    alter table BEACONTYPE 
        add constraint FKB510857E8CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table BEAMCONFIGURATIONTYPE 
        add constraint FK136CA1418CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table BEAMCRITERIATYPE 
        add constraint FKEC5A8C48EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table BEAMTYPE 
        add constraint FK7D6B23698CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table BEAMTYPE 
        add constraint FK7D6B2369C5062BA3 
        foreign key (BEAMPOINTING_BEAMTYPE_HJID) 
        references BEAMCONFIGURATIONTYPE;

    alter table BUSINESSPARTNERCRITERIATYPEI_0 
        add constraint FK79001E196BAFE39F 
        foreign key (IDITEMS_BUSINESSPARTNERCRITE_0) 
        references BUSINESSPARTNERCRITERIATYPE;

    alter table BUSINESSPARTNERCRITERIATYPEN_0 
        add constraint FK790030DEC32AF1DE 
        foreign key (NAMEITEMS_BUSINESSPARTNERCRI_0) 
        references BUSINESSPARTNERCRITERIATYPE;

    alter table BUSINESSPARTNERTYPE 
        add constraint FK93395422D867BEDA 
        foreign key (LEGALREGULATION_BUSINESSPART_0) 
        references LEGALREGULATIONTYPE;

    alter table CAPACITYLINEITEMTYPE 
        add constraint FK9126A7BFF4E02F3 
        foreign key (HJID) 
        references LINEITEMTYPE;

    alter table CARRIERCRITERIATYPE 
        add constraint FK2FF775F13D24FD65 
        foreign key (HJID) 
        references PLANNABLEASSETCRITERIATYPE;

    alter table CARRIERMEASUREMENTTYPE 
        add constraint FK2C9052FEB5D0800D 
        foreign key (HJID) 
        references MEASUREMENTTYPE;

    alter table CARRIERRECEIVEEARTHSTATIONTY_0 
        add constraint FK6449688BF1B27245 
        foreign key (HJID) 
        references EARTHSTATIONTYPE;

    alter table CARRIERRESULTTYPE 
        add constraint FK4633A9AF9E6C91B6 
        foreign key (CARRIERRESULTS_LINKBUDGETRES_0) 
        references LINKBUDGETRESULTTYPE;

    alter table CARRIERRESULTTYPE 
        add constraint FK4633A9AF62EBCD93 
        foreign key (CARRIERRESOURCEREQUIREMENT_C_0) 
        references CARRIERRESOURCEREQUIREMENTST_0;

    alter table CARRIERTRANSMITEARTHSTATIONT_0 
        add constraint FK86A48E93F1B27245 
        foreign key (HJID) 
        references EARTHSTATIONTYPE;

    alter table CARRIERTYPE 
        add constraint FK529CC61219FF69E7 
        foreign key (LINKBUDGETMODEM_CARRIERTYPE__0) 
        references MODEMTYPE;

    alter table CARRIERTYPE 
        add constraint FK529CC61277DB7066 
        foreign key (HJID) 
        references PLANNABLEASSETTYPE;

    alter table CARRIERTYPERECEIVEEARTHSTATI_0 
        add constraint FKCB1B4A6DDBECB5F8 
        foreign key (RECEIVEEARTHSTATIONITEMS_CAR_0) 
        references CARRIERTYPE;

    alter table CARRIERTYPETRANSMITEARTHSTAT_0 
        add constraint FKF801D9AD1A7C2C67 
        foreign key (TRANSMITEARTHSTATIONITEMS_CA_0) 
        references CARRIERTYPE;

    alter table CHANNELTYPE 
        add constraint FK90B9669D64B0128B 
        foreign key (BEAM_CHANNELTYPE_HJID) 
        references BEAMTYPE;

    alter table CHANNELTYPE 
        add constraint FK90B9669D8CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table CLBTSSDBCARRIERTYPE 
        add constraint FK4D1B49942088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBCONFIGPLANTYPE 
        add constraint FK49DF133E42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBCONFIGSATTYPE 
        add constraint FK9FE2174542088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBCXRDIGITALTYPE 
        add constraint FK7164EEAE42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBCXRRXESATYPE 
        add constraint FKCC9868342088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBLIFECYCLETYPE 
        add constraint FKD0AD324B42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBSATBIASTYPE 
        add constraint FKF6CDA5C042088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBSATBMPOINTTYPE 
        add constraint FK4EA5C15242088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBSATBMPOLTYPE 
        add constraint FK94A2FD8F42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBSATBMTYPE 
        add constraint FKF8C9299242088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBSATFLIGHTTYPE 
        add constraint FKB76E70B742088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBXPDRBACKOFFSCLBTTYPE 
        add constraint FKD849FE5F42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBXPDRBMCONNECTTYPE 
        add constraint FK505AA35A42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBXPDRBMRESRCTYPE 
        add constraint FKF500974142088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBXPDRDLKBMTYPE 
        add constraint FK5705A68942088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBXPDRTYPE 
        add constraint FKCC70809942088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table CLBTSSDBXPDRULKBMTYPE 
        add constraint FK2F91059A42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table COMMANDCARRIERTYPE 
        add constraint FK998142075FECEED6 
        foreign key (COMMANDCARRIER_SATELLITETYPE_0) 
        references SATELLITETYPE;

    alter table COMMANDCARRIERTYPE 
        add constraint FK99814207C066C297 
        foreign key (COMMANDCARRIERCONFIGURATION__0) 
        references COMMANDCARRIERCONFIGURATIONT_0;

    alter table COMMANDCARRIERTYPE 
        add constraint FK998142073180C83D 
        foreign key (HJID) 
        references ENTITYTYPE;

    alter table CONFIGURABLEASSETCRITERIATYPE 
        add constraint FKB5BA83D0EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table CONFIGURABLEASSETCRITERIATYP_0 
        add constraint FK195F98628764C51 
        foreign key (STATUSITEMS_CONFIGURABLEASSE_0) 
        references CONFIGURABLEASSETCRITERIATYPE;

    alter table CONFIGURABLEASSETTYPE 
        add constraint FK7ADF22F18CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table CONTACTCRITERIATYPEBUSINESSP_0 
        add constraint FKA69F788FB4CCAC7 
        foreign key (BUSINESSPARTNERIDITEMS_CONTA_0) 
        references CONTACTCRITERIATYPE;

    alter table CONTACTCRITERIATYPEIDITEM 
        add constraint FKB4A03F07966BDC9A 
        foreign key (IDITEMS_CONTACTCRITERIATYPE__0) 
        references CONTACTCRITERIATYPE;

    alter table CONTACTROLETYPE 
        add constraint FKECA384B0E2776A9C 
        foreign key (CONTACTS_DATAPLATFORMTYPE_HJ_0) 
        references DATAPLATFORMTYPE;

    alter table CONTACTROLETYPE 
        add constraint FKECA384B07EA2BD6A 
        foreign key (CONTACTS_EARTHSTATIONTYPE_HJ_0) 
        references EARTHSTATIONTYPE;

    alter table CONTACTTYPE 
        add constraint FK82D2373A7F3B66EC 
        foreign key (LEGALREGULATION_CONTACTTYPE__0) 
        references LEGALREGULATIONTYPE;

    alter table DATAPLATFORMCRITERIATYPE 
        add constraint FKE49741B61131D3EF 
        foreign key (HJID) 
        references CONFIGURABLEASSETCRITERIATYPE;

    alter table DATAPLATFORMTYPE 
        add constraint FKFAF326D7980470F0 
        foreign key (HJID) 
        references CONFIGURABLEASSETTYPE;

    alter table DOWNLINKCHANNELTYPE 
        add constraint FK185A98132C3D9B 
        foreign key (AMPLIFIER_DOWNLINKCHANNELTYP_0) 
        references AMPLIFIERTYPE;

    alter table DOWNLINKCHANNELTYPE 
        add constraint FK185A981AE8413D3 
        foreign key (AMPLIFIERCONFIGURATION_DOWNL_0) 
        references AMPLIFIERCONFIGURATIONTYPE;

    alter table DOWNLINKCHANNELTYPE 
        add constraint FK185A981F9A68907 
        foreign key (HJID) 
        references CHANNELTYPE;

    alter table DOWNLINKCHANNELTYPE 
        add constraint FK185A981989FAC8E 
        foreign key (FREQUENCYCONVERTER_DOWNLINKC_0) 
        references FREQUENCYCONVERTERTYPE;

    alter table DOWNLINKCONSTRAINTTYPE 
        add constraint FK65967533E95C5A62 
        foreign key (HJID) 
        references LINKBUDGETCONSTRAINTTYPE;

    alter table DVBMONITORINGSTATUSCRITERIAT_1 
        add constraint FK66AF40DDA67F5337 
        foreign key (SERVICEIDITEMS_DVBMONITORING_0) 
        references DVBMONITORINGSTATUSCRITERIAT_0;

    alter table EARTHSTATIONANTENNAMODELCRIT_0 
        add constraint FK48ABE03BEC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table EARTHSTATIONANTENNAMODELTYPE 
        add constraint FK8DD3446A3180C83D 
        foreign key (HJID) 
        references ENTITYTYPE;

    alter table EARTHSTATIONCRITERIATYPE 
        add constraint FK437929C376FAB2BA 
        foreign key (HJID) 
        references LIFECYCLEASSETCRITERIATYPE;

    alter table EARTHSTATIONLOCATIONTYPE 
        add constraint FK544ACF19FB179F87 
        foreign key (LONGITUDE_EARTHSTATIONLOCATI_0) 
        references LONGITUDETYPE;

    alter table EARTHSTATIONLOCATIONTYPE 
        add constraint FK544ACF19C268DD36 
        foreign key (LATITUDE_EARTHSTATIONLOCATIO_0) 
        references LATITUDETYPE;

    alter table EARTHSTATIONNETWORKCRITERIAT_0 
        add constraint FK56A31FA2EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table EARTHSTATIONNETWORKTYPE 
        add constraint FK65ED1E5E3180C83D 
        foreign key (HJID) 
        references ENTITYTYPE;

    alter table EARTHSTATIONPOINTINGTYPE 
        add constraint FKB22C313655525C29 
        foreign key (POINTING_EARTHSTATIONTYPE_HJ_0) 
        references EARTHSTATIONTYPE;

    alter table EARTHSTATIONTYPE 
        add constraint FK2D706BE46AEC4510 
        foreign key (LOCATION__EARTHSTATIONTYPE_H_0) 
        references EARTHSTATIONLOCATIONTYPE;

    alter table EARTHSTATIONTYPE 
        add constraint FK2D706BE4A3C23A1 
        foreign key (HPA_EARTHSTATIONTYPE_HJID) 
        references HPATYPE;

    alter table EARTHSTATIONTYPE 
        add constraint FK2D706BE4CF7B03BF 
        foreign key (ANTENNACHARACTERISTICS_EARTH_0) 
        references EARTHSTATIONANTENNAMODELTYPE;

    alter table EARTHSTATIONTYPE 
        add constraint FK2D706BE426724ABB 
        foreign key (HJID) 
        references LIFECYCLEASSETTYPE;

    alter table EARTHSTATIONTYPETRACKINGSYST_0 
        add constraint FKD3251F93D4074DF4 
        foreign key (TRACKINGSYSTEMITEMS_EARTHSTA_0) 
        references EARTHSTATIONTYPE;

    alter table ELEMENTARYSTREAMCRITERIATYPE 
        add constraint FKF75685853D24FD65 
        foreign key (HJID) 
        references PLANNABLEASSETCRITERIATYPE;

    alter table ELEMENTARYSTREAMTYPE 
        add constraint FKA4FEA9A68CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table ELEMENTARYSTREAMTYPEENCRYPTI_0 
        add constraint FK488C4F7B959AA819 
        foreign key (ENCRYPTIONITEMS_ELEMENTARYST_0) 
        references ELEMENTARYSTREAMTYPE;

    alter table ENDUSERCRITERIATYPE 
        add constraint FK84B2511F3D24FD65 
        foreign key (HJID) 
        references PLANNABLEASSETCRITERIATYPE;

    alter table ENDUSERTYPE 
        add constraint FK1104AF4077DB7066 
        foreign key (HJID) 
        references PLANNABLEASSETTYPE;

    alter table ENTITYCRITERIATYPEIDITEM 
        add constraint FKEC7542AB5F1846F 
        foreign key (IDITEMS_ENTITYCRITERIATYPE_H_0) 
        references ENTITYCRITERIATYPE;

    alter table FAILUREMESSAGEPHRASEABLETYPE_0 
        add constraint FKB32C36FB1401C057 
        foreign key (PHRASEKEYITEMS_FAILUREMESSAG_0) 
        references FAILUREMESSAGEPHRASEABLETYPE;

    alter table FAILUREMESSAGEPHRASETYPE 
        add constraint FK2D31B070FDC94DB 
        foreign key (MESSAGEPHRASE_FAILUREMESSAGE_0) 
        references FAILUREMESSAGELISTTYPE;

    alter table FAILUREMESSAGETYPE 
        add constraint FKFE9663F71F265CD4 
        foreign key (SUPPORTINFORMATION_FAILUREME_0) 
        references FAILUREMESSAGEPHRASEABLETYPE;

    alter table FAILUREMESSAGETYPE 
        add constraint FKFE9663F7906A8857 
        foreign key (MESSAGE_FAILUREMESSAGELISTTY_0) 
        references FAILUREMESSAGELISTTYPE;

    alter table FAILUREMESSAGETYPE 
        add constraint FKFE9663F7BD1BB3C7 
        foreign key (WHATTODO_FAILUREMESSAGETYPE__0) 
        references FAILUREMESSAGEPHRASEABLETYPE;

    alter table FAILUREMESSAGETYPE 
        add constraint FKFE9663F7D909D751 
        foreign key (WHATHAPPENED_FAILUREMESSAGET_0) 
        references FAILUREMESSAGEPHRASEABLETYPE;

    alter table FAILUREMESSAGETYPE 
        add constraint FKFE9663F7B68EF6EC 
        foreign key (WHYHAPPENED_FAILUREMESSAGETY_0) 
        references FAILUREMESSAGEPHRASEABLETYPE;

    alter table FDBEAMDOWNLINKMONITOREDTYPE 
        add constraint FK2A8B48AA9F8625D4 
        foreign key (HJID) 
        references FDBEAMMONITOREDTYPE;

    alter table FDBEAMMONITOREDTYPE 
        add constraint FK100D91C6E4DEC1A5 
        foreign key (PATTERNFILE_FDBEAMMONITOREDT_0) 
        references FDPATTERNFILETYPE;

    alter table FDBEAMUPLINKMONITOREDTYPE 
        add constraint FK242A7C919F8625D4 
        foreign key (HJID) 
        references FDBEAMMONITOREDTYPE;

    alter table FDCHANNELMONITOREDTYPE 
        add constraint FK59E11BCE8FF81555 
        foreign key (DOWNLINKBEAM_FDCHANNELMONITO_0) 
        references FDBEAMDOWNLINKMONITOREDTYPE;

    alter table FDCHANNELMONITOREDTYPE 
        add constraint FK59E11BCE409A1728 
        foreign key (UPLINKBEAM_FDCHANNELMONITORE_0) 
        references FDBEAMUPLINKMONITOREDTYPE;

    alter table FDCHANNELMONITOREDTYPE 
        add constraint FK59E11BCE1CB82CAE 
        foreign key (CHANNEL_FDSATELLITEMONITORED_0) 
        references FDSATELLITEMONITOREDTYPE;

    alter table FDCHANNELPLANNEDPATTERNINFOT_0 
        add constraint FKF4469F8659CC221B 
        foreign key (CHANNELPATTERNINFO_FDSATELLI_0) 
        references FDSATELLITEPLANNEDPATTERNINF_0;

    alter table FDSATELLITEMONITOREDTYPE 
        add constraint FK1F1CEF16C4201601 
        foreign key (MONITOREDPATTERN_FDFLEETPATT_0) 
        references FDFLEETPATTERNINFOTYPE;

    alter table FDSATELLITEPLANNEDPATTERNINF_0 
        add constraint FKBBA5C42989ABE683 
        foreign key (PLANNEDPATTERN_FDFLEETPATTER_0) 
        references FDFLEETPATTERNINFOTYPE;

    alter table FDSATELLITEPLANNEDPATTERNINF_1 
        add constraint FKBBA5C42A13EA96C 
        foreign key (PARAMETERFILENAMEITEMS_FDSAT_0) 
        references FDSATELLITEPLANNEDPATTERNINF_0;

    alter table FLEETCRITERIATYPE 
        add constraint FKD78C3F47EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table FLEETTYPE 
        add constraint FK1E2345688CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table FREQUENCYBANDCRITERIATYPE 
        add constraint FKCC86830AEC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table FREQUENCYBANDCRITERIATYPEORB_0 
        add constraint FKB7A35DE6ACC95645 
        foreign key (ORBITALLOCATIONITEMS_FREQUEN_0) 
        references FREQUENCYBANDCRITERIATYPE;

    alter table FREQUENCYBANDTYPE 
        add constraint FKDB12FC2B55BB81D3 
        foreign key (REGULATEDBAND_ORBITALLOCATIO_0) 
        references ORBITALLOCATIONTYPE;

    alter table FREQUENCYBANDTYPE 
        add constraint FKDB12FC2B3A6AA8A7 
        foreign key (SUBBAND_ORBITALLOCATIONTYPE__0) 
        references ORBITALLOCATIONTYPE;

    alter table FREQUENCYBANDTYPE 
        add constraint FKDB12FC2B8CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table FREQUENCYCONVERTERTYPE 
        add constraint FK67482CFE899ABE93 
        foreign key (FREQUENCYCONVERTER_SATELLITE_0) 
        references SATELLITETYPE;

    alter table FREQUENCYCONVERTERTYPE 
        add constraint FK67482CFE3180C83D 
        foreign key (HJID) 
        references ENTITYTYPE;

    alter table LDANTENNATYPE 
        add constraint FK63A8C7A5CDF33927 
        foreign key (RECEIVEGOVERT_LDANTENNATYPE__0) 
        references LDANTENNAGOVERTTYPE;

    alter table LDANTENNATYPE 
        add constraint FK63A8C7A5447254FA 
        foreign key (ANTENNAS_LDANTENNABANDTYPE_H_0) 
        references LDANTENNABANDTYPE;

    alter table LDHPABACKOFFTYPE 
        add constraint FK4E024BE177E4B98E 
        foreign key (BACKOFF_LDHPATYPE_HJID) 
        references LDHPATYPE;

    alter table LDHPABANDOUTPUTPOWERTYPE 
        add constraint FK5D07158837553BAB 
        foreign key (FREQUENCYBANDOUTPUTPOWER_LDH_0) 
        references LDHPATYPE;

    alter table LDINTERFERENCEBEAMTYPE 
        add constraint FK2A28CF639FF75514 
        foreign key (BEAM_LDINTERFERENCESPACECRAF_0) 
        references LDINTERFERENCESPACECRAFTTYPE;

    alter table LDINTERFERENCEEARTHSTATIONOP_0 
        add constraint FK4E2517F6F01E197E 
        foreign key (EARTHSTATIONOPERATIONALMODE__0) 
        references LDINTERFERENCETYPE;

    alter table LDINTERFERENCEINTERFERENCEMA_0 
        add constraint FK5183B1C155D03D99 
        foreign key (INTERFERENCEMAPPING_LDINTERF_0) 
        references LDINTERFERENCESPACECRAFTTYPE;

    alter table LDINTERFERENCEINTERMODULATIO_0 
        add constraint FKE4935801CD392B78 
        foreign key (INTERMODULATIONBAND_LDINTERF_0) 
        references LDINTERFERENCESPACECRAFTTYPE;

    alter table LDINTERFERENCEOPERATIONALBAN_0 
        add constraint FKEBC22568AA99354 
        foreign key (OPERATIONALBAND_LDINTERFEREN_0) 
        references LDINTERFERENCESPACECRAFTTYPE;

    alter table LDINTERFERENCESPACECRAFTTYPE 
        add constraint FK63BBE38E677576 
        foreign key (SPACECRAFT_LDINTERFERENCETYP_0) 
        references LDINTERFERENCETYPE;

    alter table LIFECYCLEASSETCRITERIATYPE 
        add constraint FKBEBD0FDFEC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table LIFECYCLEASSETCRITERIATYPELI_0 
        add constraint FKF807B24D26262DAF 
        foreign key (LIFECYCLESTATEITEMS_LIFECYCL_0) 
        references LIFECYCLEASSETCRITERIATYPE;

    alter table LIFECYCLEASSETTYPE 
        add constraint FK4E1A2E008CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table LIFECYCLESTATEENTRYDEFINITIO_0 
        add constraint FKC1E192176AEFFF 
        foreign key (STATES_LIFECYCLESTATEDEFINIT_0) 
        references LIFECYCLESTATEDEFINITIONTYPE;

    alter table LIFECYCLETRANSITIONTYPE 
        add constraint FKC0299BD91ED6DE6A 
        foreign key (LIFECYCLETRANSITION_LIFECYCL_0) 
        references LIFECYCLEASSETTYPE;

    alter table LINEITEMTYPE 
        add constraint FK97AAACC19E43468 
        foreign key (LEGALREGULATION_LINEITEMTYPE_0) 
        references LEGALREGULATIONTYPE;

    alter table LINEITEMTYPE 
        add constraint FK97AAACC1F62C9F5A 
        foreign key (CONTRACTLINES_QUOTATIONCONTR_0) 
        references QUOTATIONCONTRACTTYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D059F363D5 
        foreign key (TRANSMITEARTHSTATIONRESULT_L_0) 
        references TRANSMITEARTHSTATIONRESULTTY_0;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D05C78EE55 
        foreign key (TRANSPONDERINTERFERENCES_LIN_0) 
        references INTERFERENCETYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D09069FBB6 
        foreign key (BASEBANDRESULT_LINKBUDGETCAR_0) 
        references BASEBANDRESULTTYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D0B7D3E753 
        foreign key (DOWNLINKINTERFERENCES_LINKBU_0) 
        references INTERFERENCETYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D0D72E5DEF 
        foreign key (DOWNLINKBUDGET_LINKBUDGETCAR_0) 
        references DOWNLINKBUDGETRESULTTYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D070571C29 
        foreign key (UPLINKCONSTRAINTS_LINKBUDGET_0) 
        references UPLINKCONSTRAINTTYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D062DE0120 
        foreign key (BANDWIDTHRESULT_LINKBUDGETCA_0) 
        references CARRIERRESOURCEREQUIREMENTST_0;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D0CF9E088 
        foreign key (DOWNLINKCONSTRAINTS_LINKBUDG_0) 
        references DOWNLINKCONSTRAINTTYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D06DCEB658 
        foreign key (UPLINKBUDGET_LINKBUDGETCARRI_0) 
        references UPLINKBUDGETRESULTTYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D0AF7FA498 
        foreign key (RECEIVEEARTHSTATIONRESULT_LI_0) 
        references RECEIVEEARTHSTATIONRESULTTYPE;

    alter table LINKBUDGETCARRIERRESULTTYPE 
        add constraint FK3CE857D0474407CF 
        foreign key (UPLINKINTERFERENCES_LINKBUDG_0) 
        references INTERFERENCETYPE;

    alter table LINKBUDGETRESULTTYPEWARNINGI_0 
        add constraint FK24CBFF14E0967CB3 
        foreign key (WARNINGITEMS_LINKBUDGETRESUL_0) 
        references LINKBUDGETRESULTTYPE;

    alter table LNATYPE 
        add constraint FK3D2F16B93180C83D 
        foreign key (HJID) 
        references ENTITYTYPE;

    alter table LOGASSETTYPE 
        add constraint FKF03B336615876401 
        foreign key (ASSETCONTEXT_LOGENTRYTYPE_HJ_0) 
        references LOGENTRYTYPE;

    alter table LOGASSETTYPE 
        add constraint FKF03B33668CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table LOGENTRYTYPE 
        add constraint FK8542BFE8DFC8DCDA 
        foreign key (ASSET_LOGENTRYTYPE_HJID) 
        references LOGASSETTYPE;

    alter table MEASUREMENTCRITERIATYPE 
        add constraint FK78D274B57F08F916 
        foreign key (MONITORINGPATH_MEASUREMENTCR_0) 
        references MONITORINGPATHCRITERIATYPE;

    alter table MEASUREMENTCRITERIATYPE 
        add constraint FK78D274B548FC8E3C 
        foreign key (HJID) 
        references ENTITYCRITERIATYPE;

    alter table MEASUREMENTTYPE 
        add constraint FKA634C8D6BB62398B 
        foreign key (MONITORINGPATHNAME_MEASUREME_0) 
        references MONITORINGPATHTYPE;

    alter table MEASUREMENTTYPE 
        add constraint FKA634C8D6BDF728EC 
        foreign key (SPECTRUMPLOT_MEASUREMENTTYPE_0) 
        references SPECTRUMPLOTTYPE;

    alter table MONICSOUTGOINGTRANSFERCONTRO_0 
        add constraint FKD95A7244E0F4C4E6 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTYPE;

    alter table MONICSSCDBBEACON 
        add constraint FK6398B85F42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table MONICSSCDBCARRIER 
        add constraint FK3E87761D42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table MONICSSCDBTRANSPONDER 
        add constraint FKEB3344AF42088FCB 
        foreign key (HJID) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table MONITORINGEARTHSTATIONDEFINI_0 
        add constraint FKA27881A2859F43D4 
        foreign key (MONITORINGPATHNAME_MONITORIN_0) 
        references MONITORINGPATHTYPE;

    alter table MULTIPLEXCRITERIATYPE 
        add constraint FKC00E8F61EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table MULTIPLEXTYPE 
        add constraint FK945C4F828CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table MULTIPLEXTYPEMULTIPLEXPOOLID_0 
        add constraint FKBB12252E2CF7C37 
        foreign key (MULTIPLEXPOOLIDITEMS_MULTIPL_0) 
        references MULTIPLEXTYPE;

    alter table NAMEDENTITYCRITERIATYPE 
        add constraint FK4DAB555548FC8E3C 
        foreign key (HJID) 
        references ENTITYCRITERIATYPE;

    alter table ORBITALCHANNELCRITERIATYPE 
        add constraint FKFD746B87EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table ORBITALCHANNELTYPE 
        add constraint FKCC1BB1A827F01059 
        foreign key (CHANNEL_ORBITALLOCATIONTYPE__0) 
        references ORBITALLOCATIONTYPE;

    alter table ORBITALCHANNELTYPE 
        add constraint FKCC1BB1A88CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table ORBITALLOCATIONCRITERIATYPE 
        add constraint FK17C728A3EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table ORBITALLOCATIONTYPE 
        add constraint FKB79B4AC48CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table PAYLOADTYPE 
        add constraint FKB69E8DA88CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table PLANNABLEASSETCRITERIATYPE 
        add constraint FKB43006AA76FAB2BA 
        foreign key (HJID) 
        references LIFECYCLEASSETCRITERIATYPE;

    alter table PLANNABLEASSETTYPE 
        add constraint FK63CA1FCB26724ABB 
        foreign key (HJID) 
        references LIFECYCLEASSETTYPE;

    alter table PLATFORMLINEITEMTYPE 
        add constraint FKE8A5F0B4FF4E02F3 
        foreign key (HJID) 
        references LINEITEMTYPE;

    alter table PROPERTYTYPEVALUEITEM 
        add constraint FKCEEC0A357B7B0CCF 
        foreign key (VALUEITEMS_PROPERTYTYPE_HJID) 
        references PROPERTYTYPE;

    alter table QUOTATIONCONTRACTCRITERIATYP_0 
        add constraint FK98A0599FBAFE49CA 
        foreign key (LINEITEMIDITEMS_QUOTATIONCON_0) 
        references QUOTATIONCONTRACTCRITERIATYPE;

    alter table QUOTATIONCONTRACTTYPE 
        add constraint FK911892B8BE07C6DD 
        foreign key (LEGALREGULATION_QUOTATIONCON_0) 
        references LEGALREGULATIONTYPE;

    alter table RECEIVEBANDPARAMETERTYPE 
        add constraint FK8694DDCBA49EA7C 
        foreign key (RECEIVEBANDPERFORMANCE_EARTH_0) 
        references EARTHSTATIONTYPE;

    alter table RECEIVEEARTHSTATIONRESULTTYPE 
        add constraint FK3D376484A06023CD 
        foreign key (HJID) 
        references EARTHSTATIONRESULTTYPE;

    alter table REFERENCEDATACRITERIATYPE 
        add constraint FKFE5FE0EE48FC8E3C 
        foreign key (HJID) 
        references ENTITYCRITERIATYPE;

    alter table REFERENCEDATATYPE 
        add constraint FK6B0A7E0F3180C83D 
        foreign key (HJID) 
        references ENTITYTYPE;

    alter table ROLERIGHTITEM 
        add constraint FK1A158B59FD9DFC05 
        foreign key (RIGHTITEMS_ROLE__HJID) 
        references ROLE_;

    alter table SAPBEAMTYPE 
        add constraint FK1F99812B90A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPDATAPLATFORMTYPE 
        add constraint FKC750A29990A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPENUMERATIONTYPE 
        add constraint FKFCFFBDDF90A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPORBITALCHANNELTYPE 
        add constraint FKF70944EA90A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPOUTGOINGTRANSFERCONTROLLI_0 
        add constraint FK4550DFF26E9B4796 
        foreign key (OUTGOING_SAPOUTGOINGTRANSFER_0) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPSATELLITETYPE 
        add constraint FK7D8465390A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPTELEPORTTYPE 
        add constraint FK82B28DA790A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPTRANSPONDERBEAMTYPE 
        add constraint FKE4DDD25190A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SAPTRANSPONDERTYPE 
        add constraint FKFCD5EA2290A7B51C 
        foreign key (HJID) 
        references SAPTRANSFERASSETTYPE;

    alter table SATELLITECRITERIATYPE 
        add constraint FKC22F97B41131D3EF 
        foreign key (HJID) 
        references CONFIGURABLEASSETCRITERIATYPE;

    alter table SATELLITETYPE 
        add constraint FK7F885AD5980470F0 
        foreign key (HJID) 
        references CONFIGURABLEASSETTYPE;

    alter table SERVICEBUSDATAPLATFORMTYPE 
        add constraint FK62D09C82FDBBFA8A 
        foreign key (HJID) 
        references DATAPLATFORMTYPE;

    alter table SERVICEBUSENDUSERTYPE 
        add constraint FKABD91AB56617DA7 
        foreign key (HJID) 
        references ENDUSERTYPE;

    alter table SERVICECRITERIATYPE 
        add constraint FKC2C9AA2E3D24FD65 
        foreign key (HJID) 
        references PLANNABLEASSETCRITERIATYPE;

    alter table SERVICEREQUESTCRITERIATYPE 
        add constraint FK696D1253EC52C528 
        foreign key (HJID) 
        references ASSETCRITERIATYPE;

    alter table SERVICEREQUESTTYPE 
        add constraint FK95DDE47426724ABB 
        foreign key (HJID) 
        references LIFECYCLEASSETTYPE;

    alter table SERVICEREQUESTTYPEBANDWIDTHS_0 
        add constraint FKEDEB33872F92920A 
        foreign key (BANDWIDTHSEGMENTITEMS_SERVIC_0) 
        references SERVICEREQUESTTYPE;

    alter table SERVICEREQUESTTYPECARRIERITEM 
        add constraint FK14623CF77965111C 
        foreign key (CARRIERITEMS_SERVICEREQUESTT_0) 
        references SERVICEREQUESTTYPE;

    alter table SERVICEREQUESTTYPESERVICEINM_0 
        add constraint FK17A62E781BC204DC 
        foreign key (SERVICEINMULTIPLEXITEMS_SERV_0) 
        references SERVICEREQUESTTYPE;

    alter table SERVICETASKTYPE 
        add constraint FK6FB457D4906783FD 
        foreign key (SERVICETASK_SERVICEREQUESTTY_0) 
        references SERVICEREQUESTTYPE;

    alter table SERVICETASKTYPE 
        add constraint FK6FB457D48CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table SERVICETYPE 
        add constraint FKD71874F77DB7066 
        foreign key (HJID) 
        references PLANNABLEASSETTYPE;

    alter table SPECTRUMPLOTCRITERIATYPE 
        add constraint FK7255104B7F4508DC 
        foreign key (MONITORINGPATH_SPECTRUMPLOTC_0) 
        references MONITORINGPATHCRITERIATYPE;

    alter table SPECTRUMPLOTCRITERIATYPE 
        add constraint FK7255104B48FC8E3C 
        foreign key (HJID) 
        references ENTITYCRITERIATYPE;

    alter table SPECTRUMPLOTTYPETRACEDATAITEM 
        add constraint FK51076EF6E49E58B7 
        foreign key (TRACEDATAITEMS_SPECTRUMPLOTT_0) 
        references SPECTRUMPLOTTYPE;

    alter table SUBNETWORKTYPE 
        add constraint FK74ECE028F0AB3E45 
        foreign key (SUBNETWORK_EARTHSTATIONNETWO_0) 
        references EARTHSTATIONNETWORKTYPE;

    alter table SUBNETWORKTYPEEARTHSTATIONIT_0 
        add constraint FK8722EAEEB55E354 
        foreign key (EARTHSTATIONITEMS_SUBNETWORK_0) 
        references SUBNETWORKTYPE;

    alter table TICKETASSETTYPE 
        add constraint FKCFDD607E4E34FB21 
        foreign key (ASSETCONTEXT_TICKETENTRYTYPE_0) 
        references TICKETENTRYTYPE;

    alter table TICKETASSETTYPE 
        add constraint FKCFDD607E8CC2AB29 
        foreign key (HJID) 
        references ASSETTYPE;

    alter table TICKETCRITERIATYPEASSETUUIDI_0 
        add constraint FK7FA943F4B5C42C52 
        foreign key (ASSETUUIDITEMS_TICKETCRITERI_0) 
        references TICKETCRITERIATYPE;

    alter table TICKETENTRYTYPE 
        add constraint FK64E4ED0085834899 
        foreign key (ASSET_TICKETENTRYTYPE_HJID) 
        references TICKETASSETTYPE;

    alter table TRANSFERCONTROLLINGTYPE 
        add constraint FK99DFCD2272FC0954 
        foreign key (OUTGOING_TRANSFERCONTROLLING_0) 
        references TRANSFERCONTROLLINGTRANSFERA_0;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A021C8854D 
        foreign key (BASEBANDRESULT_TRANSMISSIONP_0) 
        references BASEBANDRESULTTYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A0BCF8464B 
        foreign key (DOWNLINKINTERFERENCES_TRANSM_0) 
        references INTERFERENCETYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A05A84D467 
        foreign key (DOWNLINKCONSTRAINTS_TRANSMIS_0) 
        references DOWNLINKCONSTRAINTTYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A086FAC82F 
        foreign key (UPLINKCONSTRAINTS_TRANSMISSI_0) 
        references UPLINKCONSTRAINTTYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A059F381DD 
        foreign key (TRANSMITEARTHSTATIONRESULT_T_0) 
        references TRANSMITEARTHSTATIONRESULTTY_0;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A0688CE786 
        foreign key (DOWNLINKBUDGET_TRANSMISSIONP_0) 
        references DOWNLINKBUDGETRESULTTYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A094CEFBAE 
        foreign key (UPLINKINTERFERENCES_TRANSMIS_0) 
        references INTERFERENCETYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A0AF836959 
        foreign key (RECEIVEEARTHSTATIONRESULT_TR_0) 
        references RECEIVEEARTHSTATIONRESULTTYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A021A9A52B 
        foreign key (UPLINKBUDGET_TRANSMISSIONPAT_0) 
        references UPLINKBUDGETRESULTTYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A05CED90E7 
        foreign key (TRANSPONDERINTERFERENCES_TRA_0) 
        references INTERFERENCETYPE;

    alter table TRANSMISSIONPATHRESULTTYPE 
        add constraint FKEBB8A9A09CBE20C3 
        foreign key (TRANSMISSIONPATHRESULT_CARRI_0) 
        references CARRIERRESULTTYPE;

    alter table TRANSMISSIONPATHTYPE 
        add constraint FK4018CEC3F3E96FD8 
        foreign key (TRANSMITEARTHSTATION_TRANSMI_0) 
        references CARRIERTRANSMITEARTHSTATIONT_0;

    alter table TRANSMISSIONPATHTYPE 
        add constraint FK4018CEC344422BA 
        foreign key (RECEIVEEARTHSTATION_TRANSMIS_0) 
        references EARTHSTATIONTYPE;

    alter table TRANSMITBANDPARAMETERTYPE 
        add constraint FKE730D17E4822494B 
        foreign key (TRANSMITBANDPERFORMANCE_EART_0) 
        references EARTHSTATIONTYPE;

    alter table TRANSMITEARTHSTATIONRESULTTY_0 
        add constraint FKE08ECA6DA06023CD 
        foreign key (HJID) 
        references EARTHSTATIONRESULTTYPE;

    alter table TRANSPONDERCRITERIATYPE 
        add constraint FK8ADD64031131D3EF 
        foreign key (HJID) 
        references CONFIGURABLEASSETCRITERIATYPE;

    alter table TRANSPONDERMEASUREMENTTYPE 
        add constraint FK17C3D3ACB5D0800D 
        foreign key (HJID) 
        references MEASUREMENTTYPE;

    alter table TRANSPONDERTYPE 
        add constraint FK48D2E6249115E161 
        foreign key (DOWNLINKCHANNEL_TRANSPONDERT_0) 
        references DOWNLINKCHANNELTYPE;

    alter table TRANSPONDERTYPE 
        add constraint FK48D2E624980470F0 
        foreign key (HJID) 
        references CONFIGURABLEASSETTYPE;

    alter table UPLINKCHANNELTYPE 
        add constraint FKD90EEA689BFAF81A 
        foreign key (LNA_UPLINKCHANNELTYPE_HJID) 
        references LNATYPE;

    alter table UPLINKCHANNELTYPE 
        add constraint FKD90EEA68765C17AE 
        foreign key (FREQUENCYCONVERTER_UPLINKCHA_0) 
        references FREQUENCYCONVERTERTYPE;

    alter table UPLINKCHANNELTYPE 
        add constraint FKD90EEA68F9A68907 
        foreign key (HJID) 
        references CHANNELTYPE;

    alter table UPLINKCHANNELTYPE 
        add constraint FKD90EEA684D6883F4 
        foreign key (UPLINKCHANNEL_TRANSPONDERTYP_0) 
        references TRANSPONDERTYPE;

    alter table UPLINKCONSTRAINTTYPE 
        add constraint FK92EE36ECE95C5A62 
        foreign key (HJID) 
        references LINKBUDGETCONSTRAINTTYPE;

    create sequence hibernate_sequence;
