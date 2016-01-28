<%@ page import="org.apache.catalina.util.ServerInfo" session="false" %>
<!DOCTYPE HTML>
<html>
<head>
<style>body {
    background-color: #fff;
    z-index: 0
}

body,form {
    margin: 20pt
}

body,p,td,th,option,input,textarea,select {
    color: #3B73AF;
    font-family: arial, sans-serif;
    font-size: 11px;
    padding-left: 6px;
    padding-right: 6px;
}
th, td {
    white-space: nowrap;}

select {
    border: 1px solid gray
}

a:hover {
    color: #3B73AF;
    font-weight: 700;
    text-decoration: underline
}

a.notbold:hover,.dropdown a:hover {
    color: #3B73AF;
    font-weight: 400;
    text-decoration: underline
}

.captiontext {
    color: #000;
    font-size: 10px;
    font-weight: 400
}

.searchtext {
    color: #666;
    font-size: 11px;
    font-weight: 700
}

.textselected {
    color: #630
}

.buttoncaptiontext {
    color: #999;
    font-size: 11px
}

.normaltext {
    color: #000;
    font-weight: 400
}

.boldtext {
    font-weight: 700
}

.button {
    background-repeat: repeat-x;
    font-weight: 700;
    height: 20px
}

.button a:hover {
    color: #C6CFE5;
    text-decoration: none
}

input.compactbutton,input.compactbuttoninactive {
    background-color: #fff;
    color: #366CD9;
    font-family: arial, sans-serif;
    font-size: 9px;
    font-weight: 700;
    padding: 2px 3px 0
}

input.compactbuttoninactive {
    color: #CCC
}

.logo {
    background-color: #b4b4b4
}

.logosm {
    background-color: #d2d2d2
}

.pagetitle,.pagetitlebar,.pagetitlebar td {
    color: #CCC;
    font-family: arial, sans-serif;
    font-size: 12px;
    font-weight: 700
}

.pagetitlebar a,.pagetitlebar td a {
    color: #3179D4;
}

.pagetitleserverbg {
    background-color: #E4F2E4
}

.pagetitleplatformbg {
    background-color: #F2EAE4
}

.pagetitleservicebg {
    background-color: #E1EFF0
}

.pagetitleapplicationbg {
    background-color: #ECE6F7
}

.listtitle,.blocktitle {
    background: transparent url(/images/4.0/backgrounds/hdbg.png) repeat-x scroll 0% 0%;
    color: #fff;
    font-size: 12px;
    font-weight: 700;
    padding: 3px
}

.blocksubtitle {
    font-size: 11px;
    font-weight: 400
}

.blockbottomline {
    height: 1px;
    padding: 0
}

.tablebottomline {
    border-bottom: 1px solid #D5D8DE
}

#escalationslist .tablebottomline {
    border-bottom: 0 solid #D5D8DE
}

.blockcontent {
    background-color: #FFF;
    padding: 3px
}

.blockcontentnopadding {
    background-color: #FFF;
    padding: 0
}

.blocklabel {
    background-color: #FFF;
    font-weight: 700;
    padding: 3px;
    text-align: right
}

.blockcheckboxlabel,.blockleftalignlabel {
    background-color: #FFF;
    font-weight: 700;
    padding: 3px;
    text-align: left
}

.blockbg {
    background-color: #FFF
}

a.listheaderlink:hover {
    color: #004680;
    text-decoration: underline
}

.tablecellheader {
    border-bottom: 1px solid #D5D8DE;
    border-top: 1px solid #ABB1C7;
    border-width: 1px;
    color: #004680;
    font-weight: 700;
    padding: 3px;
    text-align: left
}

.listheader{
	/*background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat-x scroll 0 0;*/
	border-bottom: 1px solid #D5D8DE;
    border-top: 1px solid #ABB1C7;
    border-width: 1px;
    color: #004680;
    font-weight: 700;
    padding: 3px;
    text-align: left;
    background-color: #F5F5F5;
}

.tablerowheader {
    /*background: transparent url(/images/4.0/backgrounds/table_header_large.png) repeat scroll 0 0;*/
    /*transparent url(/images/4.0/backgrounds/hdbg.png) repeat-x scroll 0% 25%;*/
    border-bottom: solid #D5D8DE;
    border-top: solid #ABB1C7;
    border-width: 1px;
    color: #FFF;
    font-weight: 700;
    padding: 3px;
    text-align: left;
    background-color: #F5F5F5;
}

.tablerowheader th {
    color: #000
}

.tableRowHeader td {
    color: #fff
}

.listheadercheckbox {
	background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat scroll 0 0;
    border-bottom: solid #D5D8DE;
    border-top: solid #ABB1C7;
    border-width: 1px;
    font-weight: 700;
    padding: 2px 2px 0 3px;
    text-align: center
}

.listheadersorted{
	background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat scroll 0 0;
}

.listheadersorted,.tablerowsorted {
    border-bottom: solid #C6CFE5;
    border-top: solid #ABB1C7;
    border-width: 1px;
    color: #FFF;
    font-weight: 700;
    padding: 3px
}

.listheaderinactive{
     background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat-x scroll 0 0;
}

.tableHeaderLarge{
    background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat-x scroll 0 0;
    font-weight: 700;
    height:29px
}

.listheaderinactive,.tablerowinactive {
    border-bottom: 1px solid #D5D8DE;
    border-top: 1px solid #ABB1C7;
    border-width: 1px;
    font-weight: 700;
    padding: 3px
}

.tbalerowinactiveblue {
   text-align: left;
   background-color: #DBE3F5;
   border-bottom: 1px solid #D5D8DE;
   whitespace:nowrap;
}

.listheaderinactivesorted {
    background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat scroll 0 0;
    border-bottom: solid #C6CFE5;
    border-top: solid #ABB1C7;
    border-width: 1px;
    font-weight: 700;
    padding: 3px
}

.listheaderoff {
    background-color: #DBE3F5;
    border-bottom: solid #C6CFE5;
    border-top: solid #ABB1C7;
    border-width: 1px;
    color: #999;
    font-weight: 700;
    padding: 3px;
    text-align: left
}

.listheaderinactivecenter {
    background-color: #DBE3F5;
    border-bottom: solid #C6CFE5;
    border-top: solid #ABB1C7;
    border-width: 1px;
    font-weight: 700;
    padding: 3px;
    text-align: center
}

.listrow,.lineitem {
    background-color: #EBF2F9;
}

.tableroweven {
    background-color: #FFF
}

.listrowselected,.tablerowaction {
    background-color: #EBEDF2;
    text-align: left
}

.tablerowodd {
    background-color: #F4FAFA
}

.listcell,.listcellselected,.tablecell,.redtablecell {
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    padding: 3px;
    vertical-align: middle
}

.listcellsmall8 {
    border-bottom: 1px solid #D5D8DE;
    font-size: 8px;
    padding: 3px
}

.listcellsmall10 {
    border-bottom: 1px solid #D5D8DE;
    font-size: 10px;
    padding: 3px
}

.listcellsmall12 {
    border-bottom: 1px solid #D5D8DE;
    font-size: 12px;
    padding: 3px
}

.listcellnopadding {
    border-bottom: 1px solid #D5D8DE;
    padding: 1px
}

.listcellcheckbox {
    border-bottom: 1px solid #D5D8DE;
    padding: 0;
    text-align: center;
    vertical-align: middle
}

.listcellcheckboxselected {
    background-color: #E6EEFF;
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    padding: 0
}

.listcellselected,.tablecellaction {
    background-color: #E6EEFF
}

.listcellcheckall {
    font-size: 11px;
    font-weight: 700;
    padding: 3px
}

.listcelllineempty {
    background-color: #FFF;
    padding: 0
}

.listcellprimary {
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    font-weight: 700;
    padding: 3px
}

.linkbox,.dropdown {
    background-color: #EDF0F8;
    border: 1px solid;
    border-color: #BDCEF0 #477DDF #477DDF #BDCEF0;
    color: #039;
    font-weight: 700;
    padding: 3px;
    text-decoration: none
}

.linkbox {
	white-space: nowrap
}
	
.dropdown {
    border-color: #f7faff #99a #99a #f7faff;
    font-size: 11px;
    z-index: 3000
}

.listheaderlight {
    background-color: #DBE3F5
}

.listheaderdark {
    background-color: #C6CFE5
}

.listheadercheckboxleftline {
    border-bottom: solid #D5D8DE;
    border-left: solid #C6CFE5;
    border-top: solid #ABB1C7;
    border-width: 1px;
    font-weight: 700;
    padding: 0;
    text-align: center
}

.listcellheader {
    background-color: #DBE3F5;
    border-bottom: solid #DBE3F5;
    border-width: 1px;
    font-weight: 700;
    padding: 3px
}

.listcellheadersorted {
    background-color: #C6CFE5;
    border-bottom: solid #C6CFE5;
    border-width: 1px;
    font-weight: 700;
    padding: 3px
}

.monitorlistheadercheckbox {
    background-color: #DBE3F5;
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    font-weight: 700;
    padding: 0;
    text-align: center
}

.listcellcheckboxleftline {
    border-bottom: solid #D5D8DE;
    border-left: solid #D5D8DE;
    border-width: 1px;
    padding: 3px;
    text-align: center;
    vertical-align: middle
}

.listcellleftline {
    border-bottom: solid #DBE3F5;
    border-left: solid #C6CFE5;
    border-width: 1px;
    padding: 3px
}

.listcellleftlinenopadding {
    border-bottom: solid #DBE3F5;
    border-left: solid #C6CFE5;
    border-width: 1px
}

.metricred {
    background-color: #F7DEE7;
    text-align: right;
    width: 100%
}

.metricgreen {
    background-color: #CEEFDE;
    text-align: right;
    width: 100%
}

.metricyellow {
    background-color: #FFF1B3;
    text-align: right;
    width: 100%
}

.metricgray {
    background-color: #D5D8DE;
    text-align: right;
    width: 100%
}

.listcellright {
    border-bottom: solid #D5D8DE;
    border-left: solid #D5D8DE;
    border-width: 1px;
    padding: 3px;
    text-align: right
}

.listcellrightselected {
    background-color: #FFEEA1;
    border-bottom: solid #D5D8DE;
    border-left: solid #C6CFE5;
    border-width: 1px;
    padding: 3px;
    text-align: right
}

.listcellrightnoline {
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    padding: 3px;
    text-align: right;
    vertical-align: middle
}

.listcellprimaryinactive {
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    color: #999;
    font-weight: 700;
    padding: 3px
}

.listcellcomparemetrics {
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    padding: 3px 3px 3px 20px
}

.configpropheader {
    background-color: #D5D8DE;
    border-bottom: solid #99A5C0;
    border-top: solid #ABB1C7;
    border-width: 1px;
    font-weight: 700;
    padding: 3px 3px 3px 15px
}

.configpropheaderhelp {
    background-color: #FFF;
    border: solid #ABB1C7;
    border-width: 1px;
    font-size: 14px;
    font-weight: 700;
    padding: 3px 3px 3px 15px
}

.guidetitle {
    background-color: #CCC;
    color: #F35E0C;
    font-size: 18px;
    font-weight: 700;
    padding: 6px
}

a.guidelink:hover {
    color: #039;
    font-size: 16px;
    font-weight: 700;
    text-decoration: underline
}

.guidetext {
    color: #000;
    font-size: 13px
}

.guidetextsmall {
    color: #000;
    font-size: 11px
}

.toolbarline {
    background-color: #F35E0C
}

.errorfield {
    background-color: #FFFD99;
    padding: 3px
}

.errorfieldcontent {
    font-size: 10px;
    font-style: italic
}

.errorblock {
    background-color: #FFFD99;
    border-top: solid #FF9C15;
    border-width: 1px;
    color: #000;
    padding: 2px
}

.confirmationblock {
    background-color: #BFF1B5;
    border-top: solid #00AC3D;
    border-width: 1px;
    color: #000;
    padding: 2px
}

.tabcell {
    border-bottom: 1px solid gray
}

.subtabcell {
    background-color: #FFF;
    border-left: 1px solid gray
}

#SubTabTarget.subtabcell {
    border-left: 0px solid gray;
    border-right: 1px solid gray
}

.dashtabs {
    margin-bottom: 4px
}

.headrightwrapper {
    height: 60px;
    margin-left: 225px;
    position: relative;
    white-space: nowrap
}

.headtopnav {
    height: 41px;
    width: 100%
}

#headusrname {
    background-color: #f1f1f1;
    border-bottom: 1px solid #999;
    border-left: 1px solid #999;
    color: #333;
    font-weight: 700;
    padding: 5px 10px;
    position: absolute;
    right: 0;
    text-align: center;
    top: 0;
    white-space: nowrap;
    z-index: 2
}

#headusrname a {
    color: #039;
    font-size: 10px;
    font-weight: 700;
    text-decoration: none
}

#namehead a {
    font-size: 11px;
    font-style: oblique
}

.headalertwrapper {
    color: #FFF;
    font-weight: 700;
    height: 38px;
    overflow: hidden;
    position: absolute;
    top: 0;
    width: 360px;
    padding-left: 72px;
    z-index: 1
}

.headbotnav {
    background-color: #369
}

#loading {
    bottom: 0;
    position: absolute;
    right: 5px
}

#hb {
    bottom: 0;
    right: 26px
}

.mainnavtext {
    color: #062b7a;
    font-family: Helvetica, sans-serif;
    font-size: 11px;
    font-weight: 700;
    white-space: nowrap
}

.recenttext {
    display: inline;
    float: left;
    padding-top: 5px;
    width: 80px;
}

#recentalerts {
    color: #FFF;
    font-size: 11px;
    font-weight: 700;
    padding: 4px 0 0 75px;
    position: absolute;
    top: 0;
    white-space: nowrap
}

#recent a {
    color: #fff;
    font-weight: 700
}

#recent li {
    display: block;
    list-style: none;
    margin: 0;
    padding: 0;
    white-space: nowrap
}

.headerwrapper {
    background-repeat: repeat-x;
    border-top: 1px solid #eee;
    height: 60px
}

.mastheadcontent a {
    font-size: 11px
}

.footersmall {
    font-size: 8px;
    font-weight: 400;
    padding: 3px
}

.dashboardcontrolactionscontainer {
    background-color: #EBEDF2;
    border-bottom: 1px solid #D5D8DE;
    border-left: 1px solid #D5D8DE;
    border-right: 1px solid #D5D8DE;
    padding: 10px
}

.formlabel {
    font-weight: 700;
    text-align: left
}

.formlabelright {
    font-weight: 700;
    text-align: right
}

.subhead {
    background-color: #CCC;
    color: #F35E0C;
    font-weight: 700;
    padding: 3px;
    text-align: left
}

a.dashlink:hover {
    color: #039;
    font-size: 11px;
    font-weight: 700;
    text-decoration: underline
}

.filterlabeltext {
    background-color: #D5D8DE;
    font-size: 11px;
    font-weight: 700;
    padding: 5px
}

.filternonboldtext {
    background-color: #D5D8DE;
    font-size: 11px;
    padding: 5px
}

.filterformtext {
    color: #000;
    font-size: 11px;
    font-weight: 400
}

.filterline {
    background-color: #ABB1C7
}

.filteremptycellright {
    background-color: #D5D8DE;
    border-left: solid #FFF;
    border-width: 5px
}

.filterlineleft {
    background-color: #ABB1C7;
    border-right: solid #FFF;
    border-right-width: 5px
}

.filterlineright {
    background-color: #ABB1C7;
    border-left: solid #FFF;
    border-left-width: 5px
}

.filterimage {
    padding-left: 1px;
    padding-right: 1px
}

.displaylabel {
    background-color: #EBEDF2;
    color: #000;
    font-size: 11px;
    font-weight: 700;
    padding: 3px
}

.displaysubhead {
    background-color: #EBEDF2;
    color: #F35E0C;
    font-weight: 700;
    padding: 3px
}

a.displaylink:hover {
    color: #039;
    font-size: 11px;
    text-decoration: underline
}

.monitorblockcontainer {
    background-color: #FFF;
    border-left: 1px solid gray;
    border-right: 1px solid gray;
    border-bottom: 1px solid gray;
    padding-top: 10px;
}

.monitorblocktopline {
    border-top: solid #949CBD;
    border-width: 1px
}

.monitortoolbar {
    background-color: #D5D8DE;
    border-top: solid #ABB1C7;
    border-width: 1px 0 0
}

.monitormetricsbaseline {
    color: #039;
    font-weight: 700
}

.monitormetricsvalue {
    color: #000;
    font-weight: 700
}

.monitormetricsselected {
    background-color: #FFEEA1;
    color: #000;
    font-weight: 700;
    width: 100%
}

.monitorcurrenthealthchartheader {
    background-color: #EFEFF7;
    font-weight: 700;
    padding: 3px
}

.monitorcurrenthealthchartcell {
    background-color: #FFF;
    padding: 3px
}

.monitorservicelist {
    font-weight: 700;
    padding: 3px 3px 3px 10px
}

.monitorchartcell {
    background-color: #FFF;
    padding: 10px
}

.monitorchartblockpadding {
    background-color: #FFF;
    padding: 5px
}

.selectwid {
    width: 225px
}

.minitabon {
    background-color: #D9D9D9;
    border-top: solid #F35E0C;
    border-width: 1px;
    color: #F35E0C;
    font-size: 12px;
    font-weight: 700;
    padding: 0
}

.minitaboff {
    background-color: #C3CEE5;
    border-bottom: solid #F35E0C;
    border-width: 1px;
    color: #039;
    font-size: 12px;
    font-weight: 700;
    padding: 0;
    text-decoration: none
}

.minitabempty {
    border-bottom: 1px solid #F35E0C;
    border-width: 1px;
    padding: 0
}

.subminitab {
    background-color: #D9D9D9;
    color: #F35E0C;
    font-weight: 700;
    padding: 3px
}

.controlblockcontainer {
    background-color: #EFEFF7;
    padding: 20px
}

.calbody {
    padding: 5px 10px 0
}

.calheader {
    background-color: #EBEDF2;
    border-bottom: solid #D5D8DE;
    border-top: solid #F35E0C;
    border-width: 2px 0 1px;
    padding: 3px
}

.caldays {
    color: #F35E0C
}

.autodiscrowignored {
    background-color: #F5F5F5
}

.autodiscrownew {
    background-color: #F5FFF9
}

.autodiscrowmodified {
    background-color: #FCF9E3
}

.multiselect {
    width: 250px
}

.resourcehubblocktitle {
    border-bottom: 1px solid gray;
    color: #F35E0C;
    font-size: 12px;
    font-weight: 700;
    padding: 3px
}

.addremovelistcellleft {
    border-bottom: solid #D5D8DE 1px;
    border-right: solid #D5D8DE 5px;
    padding: 3px
}

.addremovelistcellright {
    border-bottom: solid #D5D8DE 1px;
    border-left: solid #D5D8DE 5px;
    padding: 3px
}

.addremovelistcellcheckboxright {
    border-bottom: solid #D5D8DE 1px;
    border-left: solid #D5D8DE 5px;
    padding: 0;
    text-align: center
}

.addremovelistheaderleft {
    background-color: #DBE3F5;
    border-bottom: solid #C6CFE5 1px;
    border-right: solid #D5D8DE 5px;
    border-top: solid #ABB1C7 1px;
    font-weight: 700;
    padding: 3px
}

.addremovelistheadercheckboxright {
    background-color: #DBE3F5;
    border-bottom: solid #D5D8DE 1px;
    border-left: solid #D5D8DE 5px;
    border-top: solid #ABB1C7 1px;
    font-weight: 700;
    padding: 0;
    text-align: center
}

.addremovefilterlabeltextleft {
    background-color: #D5D8DE;
    border-right: solid #FFF 5px;
    font-size: 11px;
    font-weight: 700;
    padding: 5px
}

.addremovefilterlabeltextright {
    background-color: #D5D8DE;
    border-left: solid #FFF 5px;
    font-size: 11px;
    font-weight: 700;
    padding: 5px
}

.minichartheader {
    color: #004680;
    font-size: 11px;
    font-weight: 700;
    padding: 2px;
    text-align: left;
    vertical-align: middle
}

.minicharttitle {
    background-color: #EBEDF2;
    color: #004680;
    font-size: 10px;
    padding: 3px
}

.metricresourcetype {
    font-size: 9px;
    font-weight: 400;
    padding-left: 2em
}

#diagramdiv {
    position: absolute;
    visibility: hidden;
    z-index: 100
}

#inventoryprops,.menu {
    background-color: #F2F4F7;
    border: 2px solid #99a;
    filter: alpha(opacity=90);
    opacity: 0.9;
    position: absolute
}

#inventoryprops {
    padding: 5px;
    width: 95%
}

.menu {
    color: #000;
    font-size: 10px;
    left: 0;
    top: 0;
    visibility: hidden;
    width: 24em;
    z-index: 500;
}

.menu ul,.dropdown ul {
    margin: 0;
    padding: 0
}

.menu ul li {
    list-style-type: none;
    margin: 0;
    padding: .3em .4em
}

.dropdown li {
    list-style-type: none;
    margin: 2px
}

.menu a,div.eventssummary a {
    background-color: transparent;
    cursor: default;
    display: block;
    position: relative;
    text-decoration: none
}

.menu a:link {
    color: #3B73AF
}

.menu a:visited {
    color: #3B73AF
}

.menu a:hover {
    background-color: #338;
    color: #fff
}

a.listcellpopup2:hover span {
    background-color: #DBE3F5;
    border: 2px solid;
    border-color: #f7faff #99a #99a #f7faff;
    color: #000;
    display: block;
    left: -13em;
    padding: 4px;
    position: absolute;
    text-align: left;
    top: .2em;
    width: 48em
}

a.listcellpopup3:hover,span.spanpopup1:hover {
    color: #039;
    font-weight: 700;
    position: relative;
    text-decoration: underline;
    z-index: 25
}

a.listcellpopup4:link,a.listcellpopup5:link {
    color: #000;
    font-weight: 400;
    text-decoration: none;
    z-index: 24
}

a.listcellpopup4:visited,a.listcellpopup5:visited {
    color: #000;
    font-weight: 700;
    text-decoration: underline;
    z-index: 24
}

a.listcellpopup4:hover,a.listcellpopup5:hover {
    position: relative;
    z-index: 25
}

a.listcellpopup4:hover span,a.listcellpopup5:hover span,span.spanpopup1:hover span {
    background-color: #E6E6E6;
    border: 1px solid;
    border-color: #000;
    color: #000;
    display: block;
    left: -.3em;
    padding: 2px;
    position: absolute;
    text-align: left;
    top: -.3em;
    width: 60em
}

a.listcellpopup5:hover span {
    height: auto;
    width: 30em
}

span.spanpopup1:hover span {
    height: auto;
    left: .7em;
    top: 1em;
    width: auto
}

div.overlay {
    -moz-opacity: 0.3;
    background-color: #777;
    border: 0;
    color: #000;
    filter: alpha(opacity=30);
    font-size: 10px;
    height: 0;
    left: 0;
    opacity: 0.3;
    position: absolute;
    top: 200px;
    visibility: hidden;
    width: 9px;
}

div.timepopup {
    background-color: #e6e6e6;
    border: 1px solid;
    color: #000;
    font-size: 10px;
    left: 0;
    opacity: 0.99;
    padding: 3px;
    position: absolute;
    text-align: center;
    top: -20px;
    width: 70px;
    z-index: 210
}

div.scrollable {
    height: 165px;
    left: 400px;
    margin-right: 0;
    overflow: auto;
    top: 100px;
    width: auto
}

div.scrollable #resourcetable {
    width: 98%
}

div.eventdetails {
    background-color: #CCE0FF;
    border: 1px solid;
    font-size: 10px;
    margin-top: 2px;
    opacity: 0.99;
    position: absolute;
    width: 672px
}

div.eventssummary {
    height: 200px;
    overflow: auto;
    padding: 6px;
    position: relative;
    z-index: 500
}

td.eventstab,td.eventstabon {
    border-bottom: solid;
    border-color: #000;
    border-right: solid;
    border-width: 1px;
    padding-left: 2px;
    padding-right: 2px;
    text-align: center
}

td.eventstabon {
    border-bottom: none
}

.red,.redtablecell {
    color: #900
}

a.red:hover {
    color: red;
    font-weight: 700;
    text-decoration: none
}

.green {
    color: #090
}

a.green:hover {
    color: #0C0;
    font-weight: 700;
    text-decoration: none
}

.yellow {
    color: #BAA301
}

a.yellow:hover {
    color: #D1B801;
    font-weight: 700;
    text-decoration: none
}

.navy {
    color: navy
}

a.navy:hover {
    color: #00F;
    font-weight: 700;
    text-decoration: none
}

a.black:hover {
    color: gray;
    font-weight: 700;
    text-decoration: none
}

b.rtop,b.rbottom {
    background: #FFF;
    display: block
}

b.rtop b,b.rbottom b {
    background: #CCC;
    display: block;
    height: 1px;
    overflow: hidden
}

b.r1 {
    margin-left: 4px
}

b.r2 {
    margin-left: 2px
}

b.r3 {
    margin-left: 1px
}

div.effectscontainer div.effectsportlet {
    height: 100%;
    position: relative;
}

div.eventblock {
    background-color: #039;
    font-size: 8px;
    height: 8px;
    position: relative;
    width: 8px;
    z-index: 201
}

ul.boxy {
    list-style-type: none;
    margin: 0;
    padding: 0;
    z-index: 0
}

ul.boxy li {
    margin: 0;
    padding-left: 4px
}

ul.eventDetails li {
    margin-left: -2em;
    font-size: 10px;
}

.bigEventDetails {
    height: 300px;
    width: 100%;
    overflow: auto;
}

.overlay_dialog {
    -moz-opacity: 0.6;
    background-color: #666;
    filter: alpha(opacity=60);
    opacity: 0.6
}

.overlay___invisible__ {
    -moz-opacity: 0;
    background-color: #666;
    filter: alpha(opacity=0);
    opacity: 0
}

.dialog_n {
    height: 1px
}

.dialog_s {
    height: 19px
}

.dialog_sizer {
    background: transparent url(default/sizer.gif) no-repeat 0 0;
    cursor: se-resize;
    height: 19px;
    width: 9px
}

.dialog_close {
    background: transparent url(default/close.gif) no-repeat 0 0;
    cursor: pointer;
    height: 14px;
    left: 8px;
    position: absolute;
    top: 5px;
    width: 14px;
    z-index: 2000
}

.dialog_minimize {
    background: transparent url(default/minimize.gif) no-repeat 0 0;
    cursor: pointer;
    height: 15px;
    left: 28px;
    position: absolute;
    top: 5px;
    width: 14px;
    z-index: 2000
}

.dialog_maximize {
    background: transparent url(default/maximize.gif) no-repeat 0 0;
    cursor: pointer;
    height: 15px;
    left: 49px;
    position: absolute;
    top: 5px;
    width: 14px;
    z-index: 2000
}

.dialog_title {
    color: #000;
    float: left;
    font-size: 12px;
    height: 14px;
    text-align: center;
    width: 100%
}

.dialog_content {
    background-color: #FFF;
    color: #DDD;
    font-family: Tahoma, Arial, sans-serif;
    font-size: 10px;
    overflow: auto
}

.dialog_buttons {
    padding: 4px
}

.top_draggable,.bottom_draggable {
    cursor: move
}

.dialog {
    background-color: #fff;
    border-color: #F35E0C;
    border-style: solid;
    border-width: 1px;
    display: block;
    position: absolute;
    z-index: 300
}

.dialog table .PageTitle {
    background-color: #fff;
}

.dialog table .displaylabel {
    background-color: #fff
}

.dialog table .displaysubhead {
    background-color: #fff
}

.dialog table .displaycontent {
    background-color: #fff
}

.dialog table.table_window {
    border-collapse: collapse;
    border-spacing: 0;
    margin: 0;
    padding: 0;
    width: 100%
}

.dialog table.table_window td,.dialog table.table_window th {
    padding: 0
}

.dialog .title_window {
    -moz-user-select: none
}

.accordiontabtitlebar {
    background: #fff url(/images/4.0/backgrounds/panel_gray.jpg) repeat-x scroll 0% 50%;
    border-bottom-color: #666;
    border-style: solid none;
    border-top-color: #DDD;
    border-width: 1px 0;
    font-size: 13px;
    padding: 0 6px;
    color: fff;
    font-weight: bold
}

#panelContent {
    
}

#panelHeader {
    
}

#propertiesAccordion {
    border-left: 1px solid gray;
    border-right: 1px solid gray;
}

.resource {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-left: 3px;
    padding-top: 3px;
    width: 60%
}

.availability {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-top: 3px;
    text-align: center;
    width: 8%
}

.alerts {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-top: 3px;
    text-align: center;
    width: 7%
}

.oob {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-right: 3px;
    padding-top: 3px;
    text-align: center;
    width: 5%
}

.latest {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-right: 3px;
    padding-top: 3px;
    text-align: center;
    width: 20%
}

.resourcenamealertleft {
    border-bottom: 1px solid #D5D8DE;
    padding: 3px;
    text-align: left;
    width: 49%
}

.alerttype {
    border-bottom: 1px solid #D5D8DE;
    padding: 3px;
    text-align: left;
    width: 20%
}

.resourcetypename {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-left: 3px;
    padding-top: 3px;
    width: 20%
}

.throughput {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-top: 3px;
    text-align: center;
    width: 10%
}

.metricname {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-right: 3px;
    padding-top: 3px;
    text-align: center;
    white-space: nowrap;
    width: 20%
}

.availresourcestatus {
    border-bottom: 1px solid #D5D8DE;
    padding-bottom: 3px;
    padding-top: 3px;
    white-space: nowrap
}

#roworder {
    list-style-type: none;
    margin: 0 5px 0 0;
    padding: 0
}

#roworder li {
    border-bottom: 1px solid gray;
    font: 13px arial;
    list-style: none;
    list-style-type: none;
    margin: 0;
    padding: 4px;
    width: 100%
}

#viewescalationul {
    margin: 0;
    padding: 0;
    width: 100%
}

#viewEscalationUL li {
    background-color: #fff
}

#addescalationul {
    margin: 0;
    padding: 0;
    width: 100%
}

#addescalationul .esctbl {
    padding-left: 10px
}

.esclistarrow {
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    display: none;
    padding: 3px
}

.selectedhighlight {
    background-color: #dbe3f5;
    border-bottom: solid #D5D8DE;
    border-width: 1px;
    padding: 3px
}

.esctbl {
    margin-top: 10px;
    padding-bottom: 0;
    width: 100%
}

#viewescalationul .esctbl {
    margin-top: 0;
    padding-left: 10px;
    width: 100%
}

.remove {
    margin-left: 5px;
    margin-right: 5px;
    margin-top: 5px
}

.escinput {
    font: 10px arial;
    margin: 0;
    padding: 0;
    text-align: right
}

.escconfirmation {
    background-color: #D5D8DE;
    border: 1px solid gray;
    margin: auto;
    padding: 10px;
    width: 200px
}

.td2 {
    padding: 5px 0 10px 5px;
    width: 10%
}

.td3 {
    padding-right: 5px;
    padding-top: 5px;
    width: 10%
}

.portletlrbotborder {
    border-bottom: 1px solid #D5D8DE;
    border-left: 1px solid #D5D8DE;
    border-right: 1px solid #D5D8DE
}

.portletlrborder {
    border-top: 1px solid #D5D8DE;
    border-bottom: 1px solid #D5D8DE;
    border-left: 1px solid #D5D8DE;
    border-right: 1px solid #D5D8DE;
}

#narrowlist_false .blocktitle {
//    background: transparent url('/images/4.0/backgrounds/panel_gray.jpg') repeat-x scroll 0% 50%;
}

#narrowlist_true .blockcontent {
    background-color: #fff
}

#narrowlist_false li, #narrowlist_true li {
    padding: 5px 0px;
}

#displayselaction {
    border: 1px dotted #D5D8DE;
    margin-left: 5px;
    width: 35%
}

.emaildiv {
    text-align: left
}

.modifieddate {
    background-color: #fff;
    padding: 3px;
    text-align: right;
    border-bottom:1px solid #D5D8DE;
    border-left:1px solid #D5D8DE;
    border-right:1px solid #D5D8DE;
}

.esctbl .blocktitle {
    background-color: #f2f4f7;
    color: #333;
    font-weight: 700
}

#fixedsection {
    border-bottom: 1px solid #D5D8DE;
    border-top: 1px solid #D5D8DE;
    margin-bottom: 10px
}

.esclognotified {
    border-bottom: 1px solid #333
}

.italicinfo {
    color: #333;
    font-size: 11px;
    font-style: italic
}

.pagetitlebar,.pagetitlebar td {
    /*b

ackground-color: #ff7214;*/
    /*b

ackground-image: url(/images/bg_PageTitle2.gif);*/
    background-repeat: repeat-x;
    color: #444444;
    font-size: 17px;
}

blk,.black,#usersconfigwindow .blockcontent,#rolesconfigwindow .blockcontent {
    color: #000
}

a:link,a:visited {
    font-weight: 700;
    text-decoration: none;
    color: rgba(21, 70, 122, 1);
}

a.notbold:link,.dropdown a:link,a.notbold:visited,.dropdown a:visited {
    color: #039;
    font-weight: 400;
    text-decoration: none
}

.inactivetext,.calinactiveday {
    color: #999
}

.pagetitlegroupbg,.pagetitlerolebg,.pagetitleuserbg {
    background-color: #E5E5E5
}

.toolbarcontent {
    height: 30px;
    z-index: 1;
    position: relative;
}

.pagetitlesmalltext,a.listcellpopup5:link,a.listcellpopup5:visited {
    color: #555;
    font-size: 11px;
    line-height: 14px
}

.blockcontentsmalltext,.displaycontent {
    background-color: #EBEDF2;
    color: #000;
    font-size: 11px;
    padding: 3px
}

.smokeylabel,.autodisclabel {
    font-weight: 700;
    padding: 3px;
    text-align: right
}

.smokeycontent,.autodisccontent {
    padding: 3px
}

.SmokeyContent input[type="text"]{
	width:30px;
}

a.listheaderlink:link,a.listheaderlink:visited {
    color: #004680;
    text-decoration: none
}

tr.tableroweven:hover,tr.tablerowodd:hover,#narrowlist_true tr.listrow:hover,#narrowlist_false tr.listrow:hover {
    background-color: #d9dfe7
}

.listcellline,.monitorblock {
    background-color: #D5D8DE
}

a.guidelink:link,a.guidelink:visited {
    color: #039;
    font-size: 16px;
    font-weight: 700;
    text-decoration: none
}

.footerbold,.searchbold {
    font-weight: 700;
    padding: 3px
}

.footerregular,.searchregular {
    font-weight: 400;
    padding: 3px
}

a.dashlink:link,a.dashlink:visited {
    color: #039;
    font-size: 11px;
    font-weight: 700;
    text-decoration: none
}

a.displaylink:link,a.displaylink:visited {
    color: #039;
    font-size: 11px;
    text-decoration: none
}

.monitorlistrow,.monitorchartblock {
    background-color: #FFF
}

a.listcellpopup1,a.listcellpopup2 {
    color: #000;
    font-weight: 400;
    position: relative;
    text-decoration: underline;
    z-index: 24
}

a.listcellpopup1:hover,a.listcellpopup2:hover {
    z-index: 25
}

a.listcellpopup1 span,a.listcellpopup2 span,a.listcellpopup3 span,a.listcellpopup4 span,a.listcellpopup5 span,span.spanpopup1 span {
    display: none
}

a.listcellpopup1:hover span,a.listcellpopup3:hover span {
    background-color: #DBE3F5;
    border: 2px solid;
    border-color: #f7faff #99a #99a #f7faff;
    color: #000;
    display: block;
    left: -19em;
    padding: 4px;
    position: absolute;
    text-align: left;
    top: .2em;
    width: 40em
}

a.listcellpopup3:link,a.listcellpopup3:visited {
    color: #039;
    font-weight: 700;
    position: relative;
    text-decoration: underline;
    z-index: 24
}

span.spanpopup1:link,span.spanpopup1:visited {
    color: #039;
    font-weight: 700;
    text-decoration: none;
    z-index: 24
}

span.spanpopup1:hover,#deletebtn a:hover,a:link,a:visited {
    text-decoration: none
}

a.red:link,a.red:visited {
    color: #900;
    font-weight: 700;
    text-decoration: none
}

a.green:link,a.green:visited {
    color: #090;
    font-weight: 700;
    text-decoration: none
}

a.yellow:link,a.yellow:visited {
    color: #BAA301;
    font-weight: 700;
    text-decoration: none
}

a.navy:link,a.navy:visited {
    color: navy;
    font-weight: 700;
    text-decoration: none
}

a.black:link,a.black:visited {
    color: #000;
    font-weight: 700;
    text-decoration: none
}

.dialog_nw,.dialog_ne {
    height: 1px;
    width: 2px
}

.dialog_e,.dialog_w {
    width: 2px
}

.dialog_sw,.dialog_se {
    height: 19px;
    width: 9px
}

.status_bar,.status_bar input {
    font-size: 12px
}

.resourcename,.resourcenamealert {
    border-bottom: 1px solid #D5D8DE;
    padding: 3px;
    width: 49%
}

#viewescalationul li,#addescalationul li {
    background-color: #F2F4F7;
    border-bottom: 1px solid gray;
    font: 13px arial;
    list-style: none;
    margin: 0;
    padding: 0;
    width: 100%
}

#narrowlist_true .blocktitle,#displayselaction .blocktitle {
    background: transparent url(/images/4.0/backgrounds/panel_gray.jpg) repeat-x scroll 0%;
    color: #fff;
    font-weight: 700
}

#alertcenter #alertGroupSelect {
    width: 150;
}

/*IE 6 OVERRIDES*/

#recentAlerts {
    float:left;
    _padding-left: 0px;
}
.recentText {
    float:left;
    margin-right:6px;
}

.headTopNav {
    _height: 42px;
}

.dojoMenuBar2 {
    
}

.dojoMenuBar2 .dojoMenuItem2 {
    _padding: 3px 15px 3px;
}

.headUsrName {
    _font-size: 10px;
    _z-index: 10;
}

#loading {
    _margin-bottom: -1px;
}

#hb {
    _margin-bottom: -1px;
}

/*MIG*/

.messagePanel {
    padding: 5px 2px 5px 5px;
    margin-bottom: 9px;
    background-color: #FFFD99;
}

.messageWarning {
    border: 1px solid #FFCC33;
    background-color: #FFFFCC;
    padding: 5px;
    margin: 10px;
    font-size: 13px;
}

.messageInfo {
    border: 1px solid #004BD6;
    background-color: #DCF9FF;
    padding: 9px;
    margin: 10px 0px 10px 10px;
    font-size: 13px;
}

.infoIcon {
    height: 32px;
    width: 32px;
    background: url("/images/icon_info_small.gif") no-repeat;
    float: left;
}

.dashboard {
	background:transparent url(/images/4.0/backgrounds/titlebar_bg.png) repeat-x scroll 0 0;
	border:1px solid gray;
	font-size:12px;
	margin:0 0 6px;
	padding:4px;
	height:22px;
}
#DashboardForm select{
	padding:1px;
	border:1px solid #ddd;
}
.info {
    border: 1px solid red;
    background-color: #FFFD99;
}

.warning {
    
}

.error {
    border: 1px solid red;
}

.hidden {
    display: none;
}

.dojoDialog {
    -moz-border-radius: 6px;
    -webkit-border-radius: 6px;
    background-color: #FFFFFF;
    border: 1px solid #AAAAAA;
    height: 240px;
    width: 300px;
}

.dojoDialogTitle {
    background: transparent url(/images/4.0/backgrounds/titlebar_bg.png) repeat-x scroll 0 0;
    padding: 4px;
    font-size:1.2em;
    font-weight:bold
}

.dojoDialogBody {
    padding-left: 15px;
}

.DashboardSelectBoxLabel {
    vertical-align: top;
    display: block;
    padding: 3px;
}

.dojoDialogFooter {
    height: 26px;
    bottom: 0;
    position: absolute;
    width: 294px;
    padding: 5px 3px 0;
}

.dojoDialogHeader {
    background-color: #60A5EA;
    color: #FFF;
    font-size: 12px;
    font-weight: 700;
    padding: 3px;
}

.dojoDialogMessage {
    background: #EFEFEF none repeat scroll 0;
    color: red;
    padding: 7px;
}

/*Exception Page*/

.exception table {
    border: 1px solid #2D6EBE;
    background-color: #FFF;
}

.errorTitle {
    background: transparent url(/images/4.0/backgrounds/hdbg.png) repeat-x scroll 0% 25%;
    color: #FFF;
    height: 21px
}

.exception table p {
    padding: 4px;
}
.errorTitle .exception td {
    color: #FFF;
}

.exception td {
    color: #000;
}

.exception a:visited,.exception a:hover,.exception a {
    color: #88C7FC;
    text-decoration: none
}

/*Change Resource Owner tile*/

.editResourceOwner .BlockTitle {
    background: transparent url(/images/4.0/backgrounds/panel_gray.jpg) repeat-x scroll 0% 50%;
}

.select {
    
}

.pagetitle .pagetitle, .PortletTitle {
    color: #444444;
    font-family: arial,sans-serif;
    font-size: 1.1em;
    font-weight: 700;
}

.buttonTable {
    background-color: #fff;
    margin-top: -4px;
}

.wait {
    background: #000 url('/images/widget_bg.jpg') repeat-x;
    color: #fff;
    padding: 5px;
    position: absolute;
    line-height: 12pt;
}</style>
</head>
  <body>
    <p style="text-align: right; margin-bottom: 0px;" id="index-page-loaded-date">nix neues</p>
    <h1>Settings at <% out.println(ServerInfo.getServerInfo()); %></h1>

<%
		ClassLoader cl;
		java.net.URL[] urls;
		ClassLoader sysCl = ClassLoader.getSystemClassLoader();

		StringBuffer sb = new StringBuffer();
		sb.append("<h2>Service is available</h2>");

		/* System properties */
		sb.append("<h3>System Properties</h3>");
		java.util.TreeSet<String> propertyNames = new java.util.TreeSet<String>();
		propertyNames.addAll(System.getProperties().stringPropertyNames());
		sb.append("<table class=\"portletlrborder\">\n");
		sb.append("<tr class=\"tablerowheader\">");
		sb.append("<th>Name</th><th>Value</th></tr>\n");
		for (String propertyName : propertyNames) {
			sb.append("<tr class=\"ListRow\"><td>");
			sb.append(propertyName);
			sb.append("</td><td>");
			sb.append(System.getProperty(propertyName));
			sb.append("</td></tr>\n");
		}
		sb.append("</table>\n");

		/* Current lass loader */
		cl = this.getClass().getClassLoader();
		sb.append("<h3>This ClassLoader</h3>");
		if (java.net.URLClassLoader.class.isInstance(cl)) {
			urls = ((java.net.URLClassLoader) cl).getURLs();
			sb.append("<table class=\"portletlrborder\">\n");
			sb.append("<tr class=\"tablerowheader\">");
			sb.append("<th>Url</th></tr>\n");
			for (java.net.URL url : urls) {
				sb.append("<tr class=\"ListRow\"><td>");
				sb.append(url.toString());
				sb.append("</td></tr>\n");
			}
			sb.append("</table>\n");
		}

		cl = cl.getParent();
		while (cl != sysCl) {
			sb.append("<h3>Parent Classloader</h3>");
			if (java.net.URLClassLoader.class.isInstance(cl)) {
				urls = ((java.net.URLClassLoader) cl).getURLs();
				sb.append("<table class=\"portletlrborder\">\n");
				sb.append("<tr class=\"tablerowheader\">");
				sb.append("<th>Url</th></tr>\n");
				for (java.net.URL url : urls) {
					sb.append("<tr class=\"ListRow\"><td>");
					sb.append(url.toString());
					sb.append("</td></tr>\n");
				}
				sb.append("</table>\n");
			}
			cl = cl.getParent();
		}

		/* System class loader */
		cl = sysCl;
		sb.append("<h3>SystemClassLoader</h3>");
		if (java.net.URLClassLoader.class.isInstance(cl)) {
			urls = ((java.net.URLClassLoader) cl).getURLs();
			sb.append("<table class=\"portletlrborder\">\n");
			sb.append("<tr class=\"tablerowheader\">");
			sb.append("<th>Url</th></tr>\n");
			for (java.net.URL url : urls) {
				sb.append("<tr class=\"ListRow\"><td>");
				sb.append(url.toString());
				sb.append("</td></tr>\n");
			}
			sb.append("</table>\n");
		}
%>
<%= sb.toString() %>



<body>