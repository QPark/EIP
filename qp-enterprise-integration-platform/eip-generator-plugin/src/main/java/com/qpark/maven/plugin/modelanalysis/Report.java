/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.plugin.modelanalysis;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qpark.eip.model.docmodelreport.DataTypeReportRow;
import com.qpark.eip.model.docmodelreport.FlowReportRow;
import com.qpark.eip.model.docmodelreport.MappingReportRow;
import com.qpark.eip.model.docmodelreport.ServiceReportRow;

/**
 * Service, flow, mapping and data type report generator.
 *
 * @author bhausen
 */
public class Report {
	public static String getCss() {
		StringBuffer sb = new StringBuffer(512);
		sb.append("body {background-color: #fff;z-index: 0;}\n");
		sb.append("body,form {margin: 20pt;}\n");
		sb.append(
				"body,p,td,th,option,input,textarea,select {color: #3B73AF;font-family: arial, sans-serif;font-size: 11px;padding-left: 6px;padding-right: 6px;}\n");
		sb.append("th, td {white-space: nowrap;}\n");
		sb.append("select {border: 1px solid gray;}\n");
		sb.append("a:hover {color: #3B73AF;font-weight: 700;text-decoration: underline;}\n");
		sb.append("a.notbold:hover,.dropdown a:hover {color: #3B73AF;font-weight: 400;text-decoration: underline;}\n");
		sb.append(".captiontext {color: #000;font-size: 10px;font-weight: 400;}\n");
		sb.append(".searchtext {color: #666;font-size: 11px;font-weight: 700;}\n");
		sb.append(".textselected {color: #630;}ö\n");
		sb.append(".buttoncaptiontext {color: #999;font-size: 11px;}\n");
		sb.append(".normaltext {color: #000;font-weight: 400;}\n");
		sb.append(".boldtext {font-weight: 700;}\n");
		sb.append(".button {background-repeat: repeat-x;font-weight: 700;height: 20px;}\n");
		sb.append(".button a:hover {color: #C6CFE5;text-decoration: none;}\n");
		sb.append(
				"input.compactbutton,input.compactbuttoninactive {background-color: #fff;color: #366CD9;font-family: arial, sans-serif;font-size: 9px;font-weight: 700;padding: 2px 3px 0;}\n");
		sb.append("input.compactbuttoninactive {color: #CCC;}\n");
		sb.append(".logo {background-color: #b4b4b4;}\n");
		sb.append(".logosm {background-color: #d2d2d2;}\n");
		sb.append(
				".pagetitle,.pagetitlebar,.pagetitlebar td {color: #CCC;font-family: arial, sans-serif;font-size: 12px;font-weight: 700;}\n");
		sb.append(".pagetitlebar a,.pagetitlebar td a {color: #3179D4;}\n");
		sb.append(".pagetitleserverbg {background-color: #E4F2E4}\n");
		sb.append(".pagetitleplatformbg {background-color: #F2EAE4}\n");
		sb.append(".pagetitleservicebg {background-color: #E1EFF0}\n");
		sb.append(".pagetitleapplicationbg {background-color: #ECE6F7}\n");
		sb.append(
				".listtitle,.blocktitle {background: transparent url(/images/4.0/backgrounds/hdbg.png) repeat-x scroll 0% 0%;color: #fff;\n");
		sb.append("font-size: 12px;font-weight: 700;padding: 3px}\n");
		sb.append(".blocksubtitle {font-size: 11px;font-weight: 400}\n");
		sb.append(".blockbottomline {height: 1px;padding: 0}\n");
		sb.append(".tablebottomline {border-bottom: 1px solid #D5D8DE}\n");
		sb.append("#escalationslist .tablebottomline {border-bottom: 0 solid #D5D8DE}\n");
		sb.append(".blockcontent {background-color: #FFF;padding: 3px}\n");
		sb.append(".blockcontentnopadding {background-color: #FFF;padding: 0}\n");
		sb.append(".blocklabel {background-color: #FFF;font-weight: 700;padding: 3px;text-align: right}\n");
		sb.append(
				".blockcheckboxlabel,.blockleftalignlabel {background-color: #FFF;font-weight: 700;padding: 3px;text-align: left}\n");
		sb.append(".blockbg {background-color: #FFF}\n");
		sb.append("a.listheaderlink:hover {color: #004680;text-decoration: underline}\n");
		sb.append(
				".tablecellheader {border-bottom: 1px solid #D5D8DE;border-top: 1px solid #ABB1C7;border-width: 1px;color: #004680;font-weight: 700;padding: 3px;text-align: left}\n");
		sb.append(
				".listheader{border-bottom: 1px solid #D5D8DE;border-top: 1px solid #ABB1C7;border-width: 1px;color: #004680;font-weight: 700;padding: 3px;text-align: left;background-color: #F5F5F5;}\n");
		sb.append(
				".tablerowheader {border-bottom: solid #D5D8DE;border-top: solid #ABB1C7;border-width: 1px;color: #FFF;font-weight: 700;padding: 3px;text-align: left;background-color: #F5F5F5;}\n");
		sb.append(".tablerowheader th {color: #000}\n");
		sb.append(".tableRowHeader td {color: #fff}\n");
		sb.append(
				".listheadercheckbox {background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat scroll 0 0;border-bottom: solid #D5D8DE;border-top: solid #ABB1C7;border-width: 1px;font-weight: 700;padding: 2px 2px 0 3px;text-align: center}\n");
		sb.append(
				".listheadersorted{background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat scroll 0 0;}\n");
		sb.append(
				".listheadersorted,.tablerowsorted {border-bottom: solid #C6CFE5;border-top: solid #ABB1C7;border-width: 1px;color: #FFF;font-weight: 700;padding: 3px}\n");
		sb.append(
				".listheaderinactive{background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat-x scroll 0 0;}\n");
		sb.append(
				".tableHeaderLarge{background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat-x scroll 0 0;font-weight: 700;height:29px}\n");
		sb.append(
				".listheaderinactive,.tablerowinactive {border-bottom: 1px solid #D5D8DE;border-top: 1px solid #ABB1C7;border-width: 1px;font-weight: 700;padding: 3px}\n");
		sb.append(
				".tbalerowinactiveblue { text-align: left; background-color: #DBE3F5; border-bottom: 1px solid #D5D8DE; whitespace:nowrap;}\n");
		sb.append(
				".listheaderinactivesorted { background:transparent url(/images/4.0/backgrounds/table_header_large.png) repeat scroll 0 0; border-bottom: solid #C6CFE5; border-top: solid #ABB1C7; border-width: 1px; font-weight: 700; padding: 3px }\n");
		sb.append(
				".listheaderoff { background-color: #DBE3F5; border-bottom: solid #C6CFE5; border-top: solid #ABB1C7; border-width: 1px; color: #999; font-weight: 700; padding: 3px; text-align: left }\n");
		sb.append(
				".listheaderinactivecenter { background-color: #DBE3F5; border-bottom: solid #C6CFE5; border-top: solid #ABB1C7; border-width: 1px; font-weight: 700; padding: 3px; text-align: center }\n");
		sb.append(".listrow,.lineitem { background-color: #EBF2F9; }\n");
		sb.append(".tableroweven { background-color: #FFF }\n");
		sb.append(".listrowselected,.tablerowaction { background-color: #EBEDF2; text-align: left }\n");
		sb.append(".tablerowodd { background-color: #F4FAFA }\n");
		sb.append(
				".listcell,.listcellselected,.tablecell,.redtablecell { border-bottom: solid #D5D8DE; border-width: 1px; padding: 3px; vertical-align: middle }\n");
		sb.append(".listcellsmall8 { border-bottom: 1px solid #D5D8DE; font-size: 8px; padding: 3px }\n");
		sb.append(".listcellsmall10 { border-bottom: 1px solid #D5D8DE; font-size: 10px; padding: 3px }\n");
		sb.append(".listcellsmall12 { border-bottom: 1px solid #D5D8DE; font-size: 12px; padding: 3px }\n");
		sb.append(".listcellnopadding { border-bottom: 1px solid #D5D8DE; padding: 1px }\n");
		sb.append(
				".listcellcheckbox { border-bottom: 1px solid #D5D8DE; padding: 0; text-align: center; vertical-align: middle }\n");
		sb.append(
				".listcellcheckboxselected { background-color: #E6EEFF; border-bottom: solid #D5D8DE; border-width: 1px; padding: 0 }\n");
		sb.append(".listcellselected,.tablecellaction { background-color: #E6EEFF }\n");
		sb.append(".listcellcheckall { font-size: 11px; font-weight: 700; padding: 3px }\n");
		sb.append(".listcelllineempty { background-color: #FFF; padding: 0 }\n");
		sb.append(
				".listcellprimary { border-bottom: solid #D5D8DE; border-width: 1px; font-weight: 700; padding: 3px }\n");
		sb.append(
				".linkbox,.dropdown { background-color: #EDF0F8; border: 1px solid; border-color: #BDCEF0 #477DDF #477DDF #BDCEF0; color: #039; font-weight: 700; padding: 3px; text-decoration: none }\n");
		sb.append(".linkbox { white-space: nowrap }\n");
		sb.append(".dropdown { border-color: #f7faff #99a #99a #f7faff; font-size: 11px; z-index: 3000 }\n");
		sb.append(".listheaderlight { background-color: #DBE3F5 }\n");
		sb.append(".listheaderdark { background-color: #C6CFE5 }\n");
		sb.append(
				".listheadercheckboxleftline { border-bottom: solid #D5D8DE; border-left: solid #C6CFE5; border-top: solid #ABB1C7; border-width: 1px; font-weight: 700; padding: 0; text-align: center }\n");
		sb.append(
				".listcellheader { background-color: #DBE3F5; border-bottom: solid #DBE3F5; border-width: 1px; font-weight: 700; padding: 3px }\n");
		sb.append(
				".listcellheadersorted { background-color: #C6CFE5; border-bottom: solid #C6CFE5; border-width: 1px; font-weight: 700; padding: 3px }\n");
		sb.append(
				".monitorlistheadercheckbox { background-color: #DBE3F5; border-bottom: solid #D5D8DE; border-width: 1px; font-weight: 700; padding: 0; text-align: center }\n");
		sb.append(
				".listcellcheckboxleftline { border-bottom: solid #D5D8DE; border-left: solid #D5D8DE; border-width: 1px; padding: 3px; text-align: center; vertical-align: middle }\n");
		sb.append(
				".listcellleftline { border-bottom: solid #DBE3F5; border-left: solid #C6CFE5; border-width: 1px; padding: 3px }\n");
		sb.append(
				".listcellleftlinenopadding { border-bottom: solid #DBE3F5; border-left: solid #C6CFE5; border-width: 1px }\n");
		sb.append(".metricred { background-color: #F7DEE7; text-align: right; width: 100% }\n");
		sb.append(".metricgreen { background-color: #CEEFDE; text-align: right; width: 100% }\n");
		sb.append(".metricyellow { background-color: #FFF1B3; text-align: right; width: 100% }\n");
		sb.append(".metricgray { background-color: #D5D8DE; text-align: right; width: 100% }\n");
		sb.append(
				".listcellright { border-bottom: solid #D5D8DE; border-left: solid #D5D8DE; border-width: 1px; padding: 3px; text-align: right }\n");
		sb.append(
				".listcellrightselected { background-color: #FFEEA1; border-bottom: solid #D5D8DE; border-left: solid #C6CFE5; border-width: 1px; padding: 3px; text-align: right }\n");
		sb.append(
				".listcellrightnoline { border-bottom: solid #D5D8DE; border-width: 1px; padding: 3px; text-align: right; vertical-align: middle }\n");
		sb.append(
				".listcellprimaryinactive { border-bottom: solid #D5D8DE; border-width: 1px; color: #999; font-weight: 700; padding: 3px }\n");
		sb.append(
				".listcellcomparemetrics { border-bottom: solid #D5D8DE; border-width: 1px; padding: 3px 3px 3px 20px }\n");
		sb.append(
				".configpropheader { background-color: #D5D8DE; border-bottom: solid #99A5C0; border-top: solid #ABB1C7; border-width: 1px; font-weight: 700; padding: 3px 3px 3px 15px }\n");
		sb.append(
				".configpropheaderhelp { background-color: #FFF; border: solid #ABB1C7; border-width: 1px; font-size: 14px; font-weight: 700; padding: 3px 3px 3px 15px }\n");
		sb.append(
				".guidetitle { background-color: #CCC; color: #F35E0C; font-size: 18px; font-weight: 700; padding: 6px }\n");
		sb.append("a.guidelink:hover { color: #039; font-size: 16px; font-weight: 700; text-decoration: underline }\n");
		sb.append(".guidetext { color: #000; font-size: 13px }\n");
		sb.append(".guidetextsmall { color: #000; font-size: 11px }\n");
		sb.append(".toolbarline { background-color: #F35E0C }\n");
		sb.append(".errorfield { background-color: #FFFD99; padding: 3px }\n");
		sb.append(".errorfieldcontent { font-size: 10px; font-style: italic }\n");
		sb.append(
				".errorblock { background-color: #FFFD99; border-top: solid #FF9C15; border-width: 1px; color: #000; padding: 2px }\n");
		sb.append(
				".confirmationblock { background-color: #BFF1B5; border-top: solid #00AC3D; border-width: 1px; color: #000; padding: 2px }\n");
		sb.append(".tabcell { border-bottom: 1px solid gray }\n");
		sb.append(".subtabcell { background-color: #FFF; border-left: 1px solid gray }\n");
		sb.append("#SubTabTarget.subtabcell { border-left: 0px solid gray; border-right: 1px solid gray }\n");
		sb.append(".dashtabs { margin-bottom: 4px }\n");
		sb.append(".headrightwrapper { height: 60px; margin-left: 225px; position: relative; white-space: nowrap }\n");
		sb.append(".headtopnav { height: 41px; width: 100% }\n");
		sb.append(
				"#headusrname { background-color: #f1f1f1; border-bottom: 1px solid #999; border-left: 1px solid #999; color: #333; font-weight: 700; padding: 5px 10px; position: absolute; right: 0; text-align: center; top: 0; white-space: nowrap; z-index: 2 }\n");
		sb.append("#headusrname a { color: #039; font-size: 10px; font-weight: 700; text-decoration: none }\n");
		sb.append("#namehead a { font-size: 11px; font-style: oblique }\n");
		sb.append(
				".headalertwrapper { color: #FFF; font-weight: 700; height: 38px; overflow: hidden; position: absolute; top: 0; width: 360px; padding-left: 72px; z-index: 1 }\n");
		sb.append(".headbotnav { background-color: #369 }\n");
		sb.append("#loading { bottom: 0; position: absolute; right: 5px }\n");
		sb.append("#hb { bottom: 0; right: 26px }\n");
		sb.append(
				".mainnavtext { color: #062b7a; font-family: Helvetica, sans-serif; font-size: 11px; font-weight: 700; white-space: nowrap }\n");
		sb.append(".recenttext { display: inline; float: left; padding-top: 5px; width: 80px; }\n");
		sb.append(
				"#recentalerts { color: #FFF; font-size: 11px; font-weight: 700; padding: 4px 0 0 75px; position: absolute; top: 0; white-space: nowrap }\n");
		sb.append("#recent a { color: #fff; font-weight: 700 }\n");
		sb.append("#recent li { display: block; list-style: none; margin: 0; padding: 0; white-space: nowrap }\n");
		sb.append(".headerwrapper { background-repeat: repeat-x; border-top: 1px solid #eee; height: 60px }\n");
		sb.append(".mastheadcontent a { font-size: 11px }\n");
		sb.append(".footersmall { font-size: 8px; font-weight: 400; padding: 3px }\n");
		sb.append(
				".dashboardcontrolactionscontainer { background-color: #EBEDF2; border-bottom: 1px solid #D5D8DE; border-left: 1px solid #D5D8DE; border-right: 1px solid #D5D8DE; padding: 10px }\n");
		sb.append(".formlabel { font-weight: 700; text-align: left }\n");
		sb.append(".formlabelright { font-weight: 700; text-align: right }\n");
		sb.append(
				".subhead { background-color: #CCC; color: #F35E0C; font-weight: 700; padding: 3px; text-align: left }\n");
		sb.append("a.dashlink:hover { color: #039; font-size: 11px; font-weight: 700; text-decoration: underline }\n");
		sb.append(".filterlabeltext { background-color: #D5D8DE; font-size: 11px; font-weight: 700; padding: 5px }\n");
		sb.append(".filternonboldtext { background-color: #D5D8DE; font-size: 11px; padding: 5px }\n");
		sb.append(".filterformtext { color: #000; font-size: 11px; font-weight: 400 }\n");
		sb.append(".filterline { background-color: #ABB1C7 }\n");
		sb.append(".filteremptycellright { background-color: #D5D8DE; border-left: solid #FFF; border-width: 5px }\n");
		sb.append(".filterlineleft { background-color: #ABB1C7; border-right: solid #FFF; border-right-width: 5px }\n");
		sb.append(".filterlineright { background-color: #ABB1C7; border-left: solid #FFF; border-left-width: 5px }\n");
		sb.append(".filterimage { padding-left: 1px; padding-right: 1px }\n");
		sb.append(
				".displaylabel { background-color: #EBEDF2; color: #000; font-size: 11px; font-weight: 700; padding: 3px }\n");
		sb.append(".displaysubhead { background-color: #EBEDF2; color: #F35E0C; font-weight: 700; padding: 3px }\n");
		sb.append("a.displaylink:hover { color: #039; font-size: 11px; text-decoration: underline }\n");
		sb.append(
				".monitorblockcontainer { background-color: #FFF; border-left: 1px solid gray; border-right: 1px solid gray; border-bottom: 1px solid gray; padding-top: 10px; }\n");
		sb.append(".monitorblocktopline { border-top: solid #949CBD; border-width: 1px }\n");
		sb.append(".monitortoolbar { background-color: #D5D8DE; border-top: solid #ABB1C7; border-width: 1px 0 0 }\n");
		sb.append(".monitormetricsbaseline { color: #039; font-weight: 700 }\n");
		sb.append(".monitormetricsvalue { color: #000; font-weight: 700 }\n");
		sb.append(
				".monitormetricsselected { background-color: #FFEEA1; color: #000; font-weight: 700; width: 100% }\n");
		sb.append(".monitorcurrenthealthchartheader { background-color: #EFEFF7; font-weight: 700; padding: 3px }\n");
		sb.append(".monitorcurrenthealthchartcell { background-color: #FFF; padding: 3px }\n");
		sb.append(".monitorservicelist { font-weight: 700; padding: 3px 3px 3px 10px }\n");
		sb.append(".monitorchartcell { background-color: #FFF; padding: 10px }\n");
		sb.append(".monitorchartblockpadding { background-color: #FFF; padding: 5px }\n");
		sb.append(".selectwid { width: 225px }\n");
		sb.append(
				".minitabon { background-color: #D9D9D9; border-top: solid #F35E0C; border-width: 1px; color: #F35E0C; font-size: 12px; font-weight: 700; padding: 0 }\n");
		sb.append(
				".minitaboff { background-color: #C3CEE5; border-bottom: solid #F35E0C; border-width: 1px; color: #039; font-size: 12px; font-weight: 700; padding: 0; text-decoration: none }\n");
		sb.append(".minitabempty { border-bottom: 1px solid #F35E0C; border-width: 1px; padding: 0 }\n");
		sb.append(".subminitab { background-color: #D9D9D9; color: #F35E0C; font-weight: 700; padding: 3px }\n");
		sb.append(".controlblockcontainer { background-color: #EFEFF7; padding: 20px }\n");
		sb.append(".calbody { padding: 5px 10px 0 }\n");
		sb.append(
				".calheader { background-color: #EBEDF2; border-bottom: solid #D5D8DE; border-top: solid #F35E0C; border-width: 2px 0 1px; padding: 3px }\n");
		sb.append(".caldays { color: #F35E0C }\n");
		sb.append(".autodiscrowignored { background-color: #F5F5F5 }\n");
		sb.append(".autodiscrownew { background-color: #F5FFF9 }\n");
		sb.append(".autodiscrowmodified { background-color: #FCF9E3 }\n");
		sb.append(".multiselect { width: 250px }\n");
		sb.append(
				".resourcehubblocktitle { border-bottom: 1px solid gray; color: #F35E0C; font-size: 12px; font-weight: 700; padding: 3px }\n");
		sb.append(
				".addremovelistcellleft { border-bottom: solid #D5D8DE 1px; border-right: solid #D5D8DE 5px; padding: 3px }\n");
		sb.append(
				".addremovelistcellright { border-bottom: solid #D5D8DE 1px; border-left: solid #D5D8DE 5px; padding: 3px }\n");
		sb.append(
				".addremovelistcellcheckboxright { border-bottom: solid #D5D8DE 1px; border-left: solid #D5D8DE 5px; padding: 0; text-align: center }\n");
		sb.append(
				".addremovelistheaderleft { background-color: #DBE3F5; border-bottom: solid #C6CFE5 1px; border-right: solid #D5D8DE 5px; border-top: solid #ABB1C7 1px; font-weight: 700; padding: 3px }\n");
		sb.append(
				".addremovelistheadercheckboxright { background-color: #DBE3F5; border-bottom: solid #D5D8DE 1px; border-left: solid #D5D8DE 5px; border-top: solid #ABB1C7 1px; font-weight: 700; padding: 0; text-align: center }\n");
		sb.append(
				".addremovefilterlabeltextleft { background-color: #D5D8DE; border-right: solid #FFF 5px; font-size: 11px; font-weight: 700; padding: 5px }\n");
		sb.append(
				".addremovefilterlabeltextright { background-color: #D5D8DE; border-left: solid #FFF 5px; font-size: 11px; font-weight: 700; padding: 5px }\n");
		sb.append(
				".minichartheader { color: #004680; font-size: 11px; font-weight: 700; padding: 2px; text-align: left; vertical-align: middle }\n");
		sb.append(".minicharttitle { background-color: #EBEDF2; color: #004680; font-size: 10px; padding: 3px }\n");
		sb.append(".metricresourcetype {\n");
		sb.append("font-size: 9px;\n");
		sb.append("font-weight: 400;\n");
		sb.append("padding-left: 2em\n");
		sb.append("}\n");
		sb.append("#diagramdiv {\n");
		sb.append("position: absolute;\n");
		sb.append("visibility: hidden;\n");
		sb.append("z-index: 100\n");
		sb.append("}\n");
		sb.append("#inventoryprops,.menu {\n");
		sb.append("background-color: #F2F4F7;\n");
		sb.append("border: 2px solid #99a;\n");
		sb.append("filter: alpha(opacity=90);\n");
		sb.append("opacity: 0.9;\n");
		sb.append("position: absolute\n");
		sb.append("}\n");
		sb.append("#inventoryprops {\n");
		sb.append("padding: 5px;\n");
		sb.append("width: 95%\n");
		sb.append("}\n");
		sb.append(".menu {\n");
		sb.append("color: #000;\n");
		sb.append("font-size: 10px;\n");
		sb.append("left: 0;\n");
		sb.append("top: 0;\n");
		sb.append("visibility: hidden;\n");
		sb.append("width: 24em;\n");
		sb.append("z-index: 500;\n");
		sb.append("}\n");
		sb.append(".menu ul,.dropdown ul {\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 0\n");
		sb.append("}\n");
		sb.append(".menu ul li {\n");
		sb.append("list-style-type: none;\n");
		sb.append("margin: 0;\n");
		sb.append("padding: .3em .4em\n");
		sb.append("}\n");
		sb.append(".dropdown li {\n");
		sb.append("list-style-type: none;\n");
		sb.append("margin: 2px\n");
		sb.append("}\n");
		sb.append(".menu a,div.eventssummary a {\n");
		sb.append("background-color: transparent;\n");
		sb.append("cursor: default;\n");
		sb.append("display: block;\n");
		sb.append("position: relative;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".menu a:link {\n");
		sb.append("color: #3B73AF\n");
		sb.append("}\n");
		sb.append(".menu a:visited {\n");
		sb.append("color: #3B73AF\n");
		sb.append("}\n");
		sb.append(".menu a:hover {\n");
		sb.append("background-color: #338;\n");
		sb.append("color: #fff\n");
		sb.append("}\n");
		sb.append("a.listcellpopup2:hover span {\n");
		sb.append("background-color: #DBE3F5;\n");
		sb.append("border: 2px solid;\n");
		sb.append("border-color: #f7faff #99a #99a #f7faff;\n");
		sb.append("color: #000;\n");
		sb.append("display: block;\n");
		sb.append("left: -13em;\n");
		sb.append("padding: 4px;\n");
		sb.append("position: absolute;\n");
		sb.append("text-align: left;\n");
		sb.append("top: .2em;\n");
		sb.append("width: 48em\n");
		sb.append("}\n");
		sb.append("a.listcellpopup3:hover,span.spanpopup1:hover {\n");
		sb.append("color: #039;\n");
		sb.append("font-weight: 700;\n");
		sb.append("position: relative;\n");
		sb.append("text-decoration: underline;\n");
		sb.append("z-index: 25\n");
		sb.append("}\n");
		sb.append("a.listcellpopup4:link,a.listcellpopup5:link {\n");
		sb.append("color: #000;\n");
		sb.append("font-weight: 400;\n");
		sb.append("text-decoration: none;\n");
		sb.append("z-index: 24\n");
		sb.append("}\n");
		sb.append("a.listcellpopup4:visited,a.listcellpopup5:visited {\n");
		sb.append("color: #000;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: underline;\n");
		sb.append("z-index: 24\n");
		sb.append("}\n");
		sb.append("a.listcellpopup4:hover,a.listcellpopup5:hover {\n");
		sb.append("position: relative;\n");
		sb.append("z-index: 25\n");
		sb.append("}\n");
		sb.append("a.listcellpopup4:hover span,a.listcellpopup5:hover span,span.spanpopup1:hover span {\n");
		sb.append("background-color: #E6E6E6;\n");
		sb.append("border: 1px solid;\n");
		sb.append("border-color: #000;\n");
		sb.append("color: #000;\n");
		sb.append("display: block;\n");
		sb.append("left: -.3em;\n");
		sb.append("padding: 2px;\n");
		sb.append("position: absolute;\n");
		sb.append("text-align: left;\n");
		sb.append("top: -.3em;\n");
		sb.append("width: 60em\n");
		sb.append("}\n");
		sb.append("a.listcellpopup5:hover span {\n");
		sb.append("height: auto;\n");
		sb.append("width: 30em\n");
		sb.append("}\n");
		sb.append("span.spanpopup1:hover span {\n");
		sb.append("height: auto;\n");
		sb.append("left: .7em;\n");
		sb.append("top: 1em;\n");
		sb.append("width: auto\n");
		sb.append("}\n");
		sb.append("div.overlay {\n");
		sb.append("-moz-opacity: 0.3;\n");
		sb.append("background-color: #777;\n");
		sb.append("border: 0;\n");
		sb.append("color: #000;\n");
		sb.append("filter: alpha(opacity=30);\n");
		sb.append("font-size: 10px;\n");
		sb.append("height: 0;\n");
		sb.append("left: 0;\n");
		sb.append("opacity: 0.3;\n");
		sb.append("position: absolute;\n");
		sb.append("top: 200px;\n");
		sb.append("visibility: hidden;\n");
		sb.append("width: 9px;\n");
		sb.append("}\n");
		sb.append("div.timepopup {\n");
		sb.append("background-color: #e6e6e6;\n");
		sb.append("border: 1px solid;\n");
		sb.append("color: #000;\n");
		sb.append("font-size: 10px;\n");
		sb.append("left: 0;\n");
		sb.append("opacity: 0.99;\n");
		sb.append("padding: 3px;\n");
		sb.append("position: absolute;\n");
		sb.append("text-align: center;\n");
		sb.append("top: -20px;\n");
		sb.append("width: 70px;\n");
		sb.append("z-index: 210\n");
		sb.append("}\n");
		sb.append("div.scrollable {\n");
		sb.append("height: 165px;\n");
		sb.append("left: 400px;\n");
		sb.append("margin-right: 0;\n");
		sb.append("overflow: auto;\n");
		sb.append("top: 100px;\n");
		sb.append("width: auto\n");
		sb.append("}\n");
		sb.append("div.scrollable #resourcetable {\n");
		sb.append("width: 98%\n");
		sb.append("}\n");
		sb.append("div.eventdetails {\n");
		sb.append("background-color: #CCE0FF;\n");
		sb.append("border: 1px solid;\n");
		sb.append("font-size: 10px;\n");
		sb.append("margin-top: 2px;\n");
		sb.append("opacity: 0.99;\n");
		sb.append("position: absolute;\n");
		sb.append("width: 672px\n");
		sb.append("}\n");
		sb.append("div.eventssummary {\n");
		sb.append("height: 200px;\n");
		sb.append("overflow: auto;\n");
		sb.append("padding: 6px;\n");
		sb.append("position: relative;\n");
		sb.append("z-index: 500\n");
		sb.append("}\n");
		sb.append("td.eventstab,td.eventstabon {\n");
		sb.append("border-bottom: solid;\n");
		sb.append("border-color: #000;\n");
		sb.append("border-right: solid;\n");
		sb.append("border-width: 1px;\n");
		sb.append("padding-left: 2px;\n");
		sb.append("padding-right: 2px;\n");
		sb.append("text-align: center\n");
		sb.append("}\n");
		sb.append("td.eventstabon {\n");
		sb.append("border-bottom: none\n");
		sb.append("}\n");
		sb.append(".red,.redtablecell {\n");
		sb.append("color: #900\n");
		sb.append("}\n");
		sb.append("a.red:hover {\n");
		sb.append("color: red;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".green {\n");
		sb.append("color: #090\n");
		sb.append("}\n");
		sb.append("a.green:hover {\n");
		sb.append("color: #0C0;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".yellow {\n");
		sb.append("color: #BAA301\n");
		sb.append("}\n");
		sb.append("a.yellow:hover {\n");
		sb.append("color: #D1B801;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".navy {\n");
		sb.append("color: navy\n");
		sb.append("}\n");
		sb.append("a.navy:hover {\n");
		sb.append("color: #00F;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("a.black:hover {\n");
		sb.append("color: gray;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("b.rtop,b.rbottom {\n");
		sb.append("background: #FFF;\n");
		sb.append("display: block\n");
		sb.append("}\n");
		sb.append("b.rtop b,b.rbottom b {\n");
		sb.append("background: #CCC;\n");
		sb.append("display: block;\n");
		sb.append("height: 1px;\n");
		sb.append("overflow: hidden\n");
		sb.append("}\n");
		sb.append("b.r1 {\n");
		sb.append("margin-left: 4px\n");
		sb.append("}\n");
		sb.append("b.r2 {\n");
		sb.append("margin-left: 2px\n");
		sb.append("}\n");
		sb.append("b.r3 {\n");
		sb.append("margin-left: 1px\n");
		sb.append("}\n");
		sb.append("div.effectscontainer div.effectsportlet {\n");
		sb.append("height: 100%;\n");
		sb.append("position: relative;\n");
		sb.append("}\n");
		sb.append("div.eventblock {\n");
		sb.append("background-color: #039;\n");
		sb.append("font-size: 8px;\n");
		sb.append("height: 8px;\n");
		sb.append("position: relative;\n");
		sb.append("width: 8px;\n");
		sb.append("z-index: 201\n");
		sb.append("}\n");
		sb.append("ul.boxy {\n");
		sb.append("list-style-type: none;\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 0;\n");
		sb.append("z-index: 0\n");
		sb.append("}\n");
		sb.append("ul.boxy li {\n");
		sb.append("margin: 0;\n");
		sb.append("padding-left: 4px\n");
		sb.append("}\n");
		sb.append("ul.eventDetails li {\n");
		sb.append("margin-left: -2em;\n");
		sb.append("font-size: 10px;\n");
		sb.append("}\n");
		sb.append(".bigEventDetails {\n");
		sb.append("height: 300px;\n");
		sb.append("width: 100%;\n");
		sb.append("overflow: auto;\n");
		sb.append("}\n");
		sb.append(".overlay_dialog {\n");
		sb.append("-moz-opacity: 0.6;\n");
		sb.append("background-color: #666;\n");
		sb.append("filter: alpha(opacity=60);\n");
		sb.append("opacity: 0.6\n");
		sb.append("}\n");
		sb.append(".overlay___invisible__ {\n");
		sb.append("-moz-opacity: 0;\n");
		sb.append("background-color: #666;\n");
		sb.append("filter: alpha(opacity=0);\n");
		sb.append("opacity: 0\n");
		sb.append("}\n");
		sb.append(".dialog_n {\n");
		sb.append("height: 1px\n");
		sb.append("}\n");
		sb.append(".dialog_s {\n");
		sb.append("height: 19px\n");
		sb.append("}\n");
		sb.append(".dialog_sizer {\n");
		sb.append("background: transparent url(default/sizer.gif) no-repeat 0 0;\n");
		sb.append("cursor: se-resize;\n");
		sb.append("height: 19px;\n");
		sb.append("width: 9px\n");
		sb.append("}\n");
		sb.append(".dialog_close {\n");
		sb.append("background: transparent url(default/close.gif) no-repeat 0 0;\n");
		sb.append("cursor: pointer;\n");
		sb.append("height: 14px;\n");
		sb.append("left: 8px;\n");
		sb.append("position: absolute;\n");
		sb.append("top: 5px;\n");
		sb.append("width: 14px;\n");
		sb.append("z-index: 2000\n");
		sb.append("}\n");
		sb.append(".dialog_minimize {\n");
		sb.append("background: transparent url(default/minimize.gif) no-repeat 0 0;\n");
		sb.append("cursor: pointer;\n");
		sb.append("height: 15px;\n");
		sb.append("left: 28px;\n");
		sb.append("position: absolute;\n");
		sb.append("top: 5px;\n");
		sb.append("width: 14px;\n");
		sb.append("z-index: 2000\n");
		sb.append("}\n");
		sb.append(".dialog_maximize {\n");
		sb.append("background: transparent url(default/maximize.gif) no-repeat 0 0;\n");
		sb.append("cursor: pointer;\n");
		sb.append("height: 15px;\n");
		sb.append("left: 49px;\n");
		sb.append("position: absolute;\n");
		sb.append("top: 5px;\n");
		sb.append("width: 14px;\n");
		sb.append("z-index: 2000\n");
		sb.append("}\n");
		sb.append(".dialog_title {\n");
		sb.append("color: #000;\n");
		sb.append("float: left;\n");
		sb.append("font-size: 12px;\n");
		sb.append("height: 14px;\n");
		sb.append("text-align: center;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append(".dialog_content {\n");
		sb.append("background-color: #FFF;\n");
		sb.append("color: #DDD;\n");
		sb.append("font-family: Tahoma, Arial, sans-serif;\n");
		sb.append("font-size: 10px;\n");
		sb.append("overflow: auto\n");
		sb.append("}\n");
		sb.append(".dialog_buttons {\n");
		sb.append("padding: 4px\n");
		sb.append("}\n");
		sb.append(".top_draggable,.bottom_draggable {\n");
		sb.append("cursor: move\n");
		sb.append("}\n");
		sb.append(".dialog {\n");
		sb.append("background-color: #fff;\n");
		sb.append("border-color: #F35E0C;\n");
		sb.append("border-style: solid;\n");
		sb.append("border-width: 1px;\n");
		sb.append("display: block;\n");
		sb.append("position: absolute;\n");
		sb.append("z-index: 300\n");
		sb.append("}\n");
		sb.append(".dialog table .PageTitle {\n");
		sb.append("background-color: #fff;\n");
		sb.append("}\n");
		sb.append(".dialog table .displaylabel {\n");
		sb.append("background-color: #fff\n");
		sb.append("}\n");
		sb.append(".dialog table .displaysubhead {\n");
		sb.append("background-color: #fff\n");
		sb.append("}\n");
		sb.append(".dialog table .displaycontent {\n");
		sb.append("background-color: #fff\n");
		sb.append("}\n");
		sb.append(".dialog table.table_window {\n");
		sb.append("border-collapse: collapse;\n");
		sb.append("border-spacing: 0;\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 0;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append(".dialog table.table_window td,.dialog table.table_window th {\n");
		sb.append("padding: 0\n");
		sb.append("}\n");
		sb.append(".dialog .title_window {\n");
		sb.append("-moz-user-select: none\n");
		sb.append("}\n");
		sb.append(".accordiontabtitlebar {\n");
		sb.append("background: #fff url(/images/4.0/backgrounds/panel_gray.jpg) repeat-x scroll 0% 50%;\n");
		sb.append("border-bottom-color: #666;\n");
		sb.append("border-style: solid none;\n");
		sb.append("border-top-color: #DDD;\n");
		sb.append("border-width: 1px 0;\n");
		sb.append("font-size: 13px;\n");
		sb.append("padding: 0 6px;\n");
		sb.append("color: fff;\n");
		sb.append("font-weight: bold\n");
		sb.append("}\n");
		sb.append("#panelContent {\n");
		sb.append("}\n");
		sb.append("#panelHeader {\n");
		sb.append("}\n");
		sb.append("#propertiesAccordion {\n");
		sb.append("border-left: 1px solid gray;\n");
		sb.append("border-right: 1px solid gray;\n");
		sb.append("}\n");
		sb.append(".resource {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-left: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("width: 60%\n");
		sb.append("}\n");
		sb.append(".availability {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("text-align: center;\n");
		sb.append("width: 8%\n");
		sb.append("}\n");
		sb.append(".alerts {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("text-align: center;\n");
		sb.append("width: 7%\n");
		sb.append("}\n");
		sb.append(".oob {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-right: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("text-align: center;\n");
		sb.append("width: 5%\n");
		sb.append("}\n");
		sb.append(".latest {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-right: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("text-align: center;\n");
		sb.append("width: 20%\n");
		sb.append("}\n");
		sb.append(".resourcenamealertleft {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding: 3px;\n");
		sb.append("text-align: left;\n");
		sb.append("width: 49%\n");
		sb.append("}\n");
		sb.append(".alerttype {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding: 3px;\n");
		sb.append("text-align: left;\n");
		sb.append("width: 20%\n");
		sb.append("}\n");
		sb.append(".resourcetypename {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-left: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("width: 20%\n");
		sb.append("}\n");
		sb.append(".throughput {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("text-align: center;\n");
		sb.append("width: 10%\n");
		sb.append("}\n");
		sb.append(".metricname {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-right: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("text-align: center;\n");
		sb.append("white-space: nowrap;\n");
		sb.append("width: 20%\n");
		sb.append("}\n");
		sb.append(".availresourcestatus {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding-bottom: 3px;\n");
		sb.append("padding-top: 3px;\n");
		sb.append("white-space: nowrap\n");
		sb.append("}\n");
		sb.append("#roworder {\n");
		sb.append("list-style-type: none;\n");
		sb.append("margin: 0 5px 0 0;\n");
		sb.append("padding: 0\n");
		sb.append("}\n");
		sb.append("#roworder li {\n");
		sb.append("border-bottom: 1px solid gray;\n");
		sb.append("font: 13px arial;\n");
		sb.append("list-style: none;\n");
		sb.append("list-style-type: none;\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 4px;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append("#viewescalationul {\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 0;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append("#viewEscalationUL li {\n");
		sb.append("background-color: #fff\n");
		sb.append("}\n");
		sb.append("#addescalationul {\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 0;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append("#addescalationul .esctbl {\n");
		sb.append("padding-left: 10px\n");
		sb.append("}\n");
		sb.append(".esclistarrow {\n");
		sb.append("border-bottom: solid #D5D8DE;\n");
		sb.append("border-width: 1px;\n");
		sb.append("display: none;\n");
		sb.append("padding: 3px\n");
		sb.append("}\n");
		sb.append(".selectedhighlight {\n");
		sb.append("background-color: #dbe3f5;\n");
		sb.append("border-bottom: solid #D5D8DE;\n");
		sb.append("border-width: 1px;\n");
		sb.append("padding: 3px\n");
		sb.append("}\n");
		sb.append(".esctbl {\n");
		sb.append("margin-top: 10px;\n");
		sb.append("padding-bottom: 0;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append("#viewescalationul .esctbl {\n");
		sb.append("margin-top: 0;\n");
		sb.append("padding-left: 10px;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append(".remove {\n");
		sb.append("margin-left: 5px;\n");
		sb.append("margin-right: 5px;\n");
		sb.append("margin-top: 5px\n");
		sb.append("}\n");
		sb.append(".escinput {\n");
		sb.append("font: 10px arial;\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 0;\n");
		sb.append("text-align: right\n");
		sb.append("}\n");
		sb.append(".escconfirmation {\n");
		sb.append("background-color: #D5D8DE;\n");
		sb.append("border: 1px solid gray;\n");
		sb.append("margin: auto;\n");
		sb.append("padding: 10px;\n");
		sb.append("width: 200px\n");
		sb.append("}\n");
		sb.append(".td2 {\n");
		sb.append("padding: 5px 0 10px 5px;\n");
		sb.append("width: 10%\n");
		sb.append("}\n");
		sb.append(".td3 {\n");
		sb.append("padding-right: 5px;\n");
		sb.append("padding-top: 5px;\n");
		sb.append("width: 10%\n");
		sb.append("}\n");
		sb.append(".portletlrbotborder {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("border-left: 1px solid #D5D8DE;\n");
		sb.append("border-right: 1px solid #D5D8DE\n");
		sb.append("}\n");
		sb.append(".portletlrborder {\n");
		sb.append("border-top: 1px solid #D5D8DE;\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("border-left: 1px solid #D5D8DE;\n");
		sb.append("border-right: 1px solid #D5D8DE;\n");
		sb.append("}\n");
		sb.append("#narrowlist_false .blocktitle {\n");
		sb.append(
				"//    background: transparent url('/images/4.0/backgrounds/panel_gray.jpg') repeat-x scroll 0% 50%;\n");
		sb.append("}\n");
		sb.append("#narrowlist_true .blockcontent {\n");
		sb.append("background-color: #fff\n");
		sb.append("}\n");
		sb.append("#narrowlist_false li, #narrowlist_true li {\n");
		sb.append("padding: 5px 0px;\n");
		sb.append("}\n");
		sb.append("#displayselaction {\n");
		sb.append("border: 1px dotted #D5D8DE;\n");
		sb.append("margin-left: 5px;\n");
		sb.append("width: 35%\n");
		sb.append("}\n");
		sb.append(".emaildiv {\n");
		sb.append("text-align: left\n");
		sb.append("}\n");
		sb.append(".modifieddate {\n");
		sb.append("background-color: #fff;\n");
		sb.append("padding: 3px;\n");
		sb.append("text-align: right;\n");
		sb.append("border-bottom:1px solid #D5D8DE;\n");
		sb.append("border-left:1px solid #D5D8DE;\n");
		sb.append("border-right:1px solid #D5D8DE;\n");
		sb.append("}\n");
		sb.append(".esctbl .blocktitle {\n");
		sb.append("background-color: #f2f4f7;\n");
		sb.append("color: #333;\n");
		sb.append("font-weight: 700\n");
		sb.append("}\n");
		sb.append("#fixedsection {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("border-top: 1px solid #D5D8DE;\n");
		sb.append("margin-bottom: 10px\n");
		sb.append("}\n");
		sb.append(".esclognotified {\n");
		sb.append("border-bottom: 1px solid #333\n");
		sb.append("}\n");
		sb.append(".italicinfo {\n");
		sb.append("color: #333;\n");
		sb.append("font-size: 11px;\n");
		sb.append("font-style: italic\n");
		sb.append("}\n");
		sb.append(".pagetitlebar,.pagetitlebar td {\n");
		sb.append("/*b\n");
		sb.append("ackground-color: #ff7214;*/\n");
		sb.append("/*b\n");
		sb.append("ackground-image: url(/images/bg_PageTitle2.gif);*/\n");
		sb.append("background-repeat: repeat-x;\n");
		sb.append("color: #444444;\n");
		sb.append("font-size: 17px;\n");
		sb.append("}\n");
		sb.append("blk,.black,#usersconfigwindow .blockcontent,#rolesconfigwindow .blockcontent {\n");
		sb.append("color: #000\n");
		sb.append("}\n");
		sb.append("a:link,a:visited {\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none;\n");
		sb.append("color: rgba(21, 70, 122, 1);\n");
		sb.append("}\n");
		sb.append("a.notbold:link,.dropdown a:link,a.notbold:visited,.dropdown a:visited {\n");
		sb.append("color: #039;\n");
		sb.append("font-weight: 400;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".inactivetext,.calinactiveday {\n");
		sb.append("color: #999\n");
		sb.append("}\n");
		sb.append(".pagetitlegroupbg,.pagetitlerolebg,.pagetitleuserbg {\n");
		sb.append("background-color: #E5E5E5\n");
		sb.append("}\n");
		sb.append(".toolbarcontent {\n");
		sb.append("height: 30px;\n");
		sb.append("z-index: 1;\n");
		sb.append("position: relative;\n");
		sb.append("}\n");
		sb.append(".pagetitlesmalltext,a.listcellpopup5:link,a.listcellpopup5:visited {\n");
		sb.append("color: #555;\n");
		sb.append("font-size: 11px;\n");
		sb.append("line-height: 14px\n");
		sb.append("}\n");
		sb.append(".blockcontentsmalltext,.displaycontent {\n");
		sb.append("background-color: #EBEDF2;\n");
		sb.append("color: #000;\n");
		sb.append("font-size: 11px;\n");
		sb.append("padding: 3px\n");
		sb.append("}\n");
		sb.append(".smokeylabel,.autodisclabel {\n");
		sb.append("font-weight: 700;\n");
		sb.append("padding: 3px;\n");
		sb.append("text-align: right\n");
		sb.append("}\n");
		sb.append(".smokeycontent,.autodisccontent {\n");
		sb.append("padding: 3px\n");
		sb.append("}\n");
		sb.append(".SmokeyContent input[type=\"text\"]{\n");
		sb.append("width:30px;\n");
		sb.append("}\n");
		sb.append("a.listheaderlink:link,a.listheaderlink:visited {\n");
		sb.append("color: #004680;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(
				"tr.tableroweven:hover,tr.tablerowodd:hover,#narrowlist_true tr.listrow:hover,#narrowlist_false tr.listrow:hover {\n");
		sb.append("background-color: #d9dfe7\n");
		sb.append("}\n");
		sb.append(".listcellline,.monitorblock {\n");
		sb.append("background-color: #D5D8DE\n");
		sb.append("}\n");
		sb.append("a.guidelink:link,a.guidelink:visited {\n");
		sb.append("color: #039;\n");
		sb.append("font-size: 16px;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".footerbold,.searchbold {\n");
		sb.append("font-weight: 700;\n");
		sb.append("padding: 3px\n");
		sb.append("}\n");
		sb.append(".footerregular,.searchregular {\n");
		sb.append("font-weight: 400;\n");
		sb.append("padding: 3px\n");
		sb.append("}\n");
		sb.append("a.dashlink:link,a.dashlink:visited {\n");
		sb.append("color: #039;\n");
		sb.append("font-size: 11px;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("a.displaylink:link,a.displaylink:visited {\n");
		sb.append("color: #039;\n");
		sb.append("font-size: 11px;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".monitorlistrow,.monitorchartblock {\n");
		sb.append("background-color: #FFF\n");
		sb.append("}\n");
		sb.append("a.listcellpopup1,a.listcellpopup2 {\n");
		sb.append("color: #000;\n");
		sb.append("font-weight: 400;\n");
		sb.append("position: relative;\n");
		sb.append("text-decoration: underline;\n");
		sb.append("z-index: 24\n");
		sb.append("}\n");
		sb.append("a.listcellpopup1:hover,a.listcellpopup2:hover {\n");
		sb.append("z-index: 25\n");
		sb.append("}\n");
		sb.append(
				"a.listcellpopup1 span,a.listcellpopup2 span,a.listcellpopup3 span,a.listcellpopup4 span,a.listcellpopup5 span,span.spanpopup1 span {\n");
		sb.append("display: none\n");
		sb.append("}\n");
		sb.append("a.listcellpopup1:hover span,a.listcellpopup3:hover span {\n");
		sb.append("background-color: #DBE3F5;\n");
		sb.append("border: 2px solid;\n");
		sb.append("border-color: #f7faff #99a #99a #f7faff;\n");
		sb.append("color: #000;\n");
		sb.append("display: block;\n");
		sb.append("left: -19em;\n");
		sb.append("padding: 4px;\n");
		sb.append("position: absolute;\n");
		sb.append("text-align: left;\n");
		sb.append("top: .2em;\n");
		sb.append("width: 40em\n");
		sb.append("}\n");
		sb.append("a.listcellpopup3:link,a.listcellpopup3:visited {\n");
		sb.append("color: #039;\n");
		sb.append("font-weight: 700;\n");
		sb.append("position: relative;\n");
		sb.append("text-decoration: underline;\n");
		sb.append("z-index: 24\n");
		sb.append("}\n");
		sb.append("span.spanpopup1:link,span.spanpopup1:visited {\n");
		sb.append("color: #039;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none;\n");
		sb.append("z-index: 24\n");
		sb.append("}\n");
		sb.append("span.spanpopup1:hover,#deletebtn a:hover,a:link,a:visited {\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("a.red:link,a.red:visited {\n");
		sb.append("color: #900;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("a.green:link,a.green:visited {\n");
		sb.append("color: #090;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("a.yellow:link,a.yellow:visited {\n");
		sb.append("color: #BAA301;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("a.navy:link,a.navy:visited {\n");
		sb.append("color: navy;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append("a.black:link,a.black:visited {\n");
		sb.append("color: #000;\n");
		sb.append("font-weight: 700;\n");
		sb.append("text-decoration: none\n");
		sb.append("}\n");
		sb.append(".dialog_nw,.dialog_ne {\n");
		sb.append("height: 1px;\n");
		sb.append("width: 2px\n");
		sb.append("}\n");
		sb.append(".dialog_e,.dialog_w {\n");
		sb.append("width: 2px\n");
		sb.append("}\n");
		sb.append(".dialog_sw,.dialog_se {\n");
		sb.append("height: 19px;\n");
		sb.append("width: 9px\n");
		sb.append("}\n");
		sb.append(".status_bar,.status_bar input {\n");
		sb.append("font-size: 12px\n");
		sb.append("}\n");
		sb.append(".resourcename,.resourcenamealert {\n");
		sb.append("border-bottom: 1px solid #D5D8DE;\n");
		sb.append("padding: 3px;\n");
		sb.append("width: 49%\n");
		sb.append("}\n");
		sb.append("#viewescalationul li,#addescalationul li {\n");
		sb.append("background-color: #F2F4F7;\n");
		sb.append("border-bottom: 1px solid gray;\n");
		sb.append("font: 13px arial;\n");
		sb.append("list-style: none;\n");
		sb.append("margin: 0;\n");
		sb.append("padding: 0;\n");
		sb.append("width: 100%\n");
		sb.append("}\n");
		sb.append("#narrowlist_true .blocktitle,#displayselaction .blocktitle {\n");
		sb.append("background: transparent url(/images/4.0/backgrounds/panel_gray.jpg) repeat-x scroll 0%;\n");
		sb.append("color: #fff;\n");
		sb.append("font-weight: 700\n");
		sb.append("}\n");
		sb.append("#alertcenter #alertGroupSelect {\n");
		sb.append("width: 150;\n");
		sb.append("}\n");
		sb.append("/*IE 6 OVERRIDES*/\n");
		sb.append("#recentAlerts {\n");
		sb.append("float:left;\n");
		sb.append("_padding-left: 0px;\n");
		sb.append("}\n");
		sb.append(".recentText {\n");
		sb.append("float:left;\n");
		sb.append("margin-right:6px;\n");
		sb.append("}\n");
		sb.append(".headTopNav {\n");
		sb.append("_height: 42px;\n");
		sb.append("}\n");
		sb.append(".dojoMenuBar2 {\n");
		sb.append("}\n");
		sb.append(".dojoMenuBar2 .dojoMenuItem2 {\n");
		sb.append("_padding: 3px 15px 3px;\n");
		sb.append("}\n");
		sb.append(".headUsrName {\n");
		sb.append("_font-size: 10px;\n");
		sb.append("_z-index: 10;\n");
		sb.append("}\n");
		sb.append("#loading {\n");
		sb.append("_margin-bottom: -1px;\n");
		sb.append("}\n");
		sb.append("#hb {\n");
		sb.append("_margin-bottom: -1px;\n");
		sb.append("}\n");
		sb.append(".messagePanel {\n");
		sb.append("padding: 5px 2px 5px 5px;\n");
		sb.append("margin-bottom: 9px;\n");
		sb.append("background-color: #FFFD99;\n");
		sb.append("}\n");
		sb.append(".messageWarning {\n");
		sb.append("border: 1px solid #FFCC33;\n");
		sb.append("background-color: #FFFFCC;\n");
		sb.append("padding: 5px;\n");
		sb.append("margin: 10px;\n");
		sb.append("font-size: 13px;\n");
		sb.append("}\n");
		sb.append(".messageInfo {\n");
		sb.append("border: 1px solid #004BD6;\n");
		sb.append("background-color: #DCF9FF;\n");
		sb.append("padding: 9px;\n");
		sb.append("margin: 10px 0px 10px 10px;\n");
		sb.append("font-size: 13px;\n");
		sb.append("}\n");
		sb.append(".infoIcon {\n");
		sb.append("height: 32px;\n");
		sb.append("width: 32px;\n");
		sb.append("background: url(\"/images/icon_info_small.gif\") no-repeat;\n");
		sb.append("float: left;\n");
		sb.append("}\n");
		sb.append(".dashboard {\n");
		sb.append("background:transparent url(/images/4.0/backgrounds/titlebar_bg.png) repeat-x scroll 0 0;\n");
		sb.append("border:1px solid gray;\n");
		sb.append("font-size:12px;\n");
		sb.append("margin:0 0 6px;\n");
		sb.append("padding:4px;\n");
		sb.append("height:22px;\n");
		sb.append("}\n");
		sb.append("#DashboardForm select{\n");
		sb.append("padding:1px;\n");
		sb.append("border:1px solid #ddd;\n");
		sb.append("}\n");
		sb.append(".info { border: 1px solid red; background-color: #FFFD99; } .warning {}\n");
		sb.append(
				".error { border: 1px solid red; } .hidden { display: none; } .dojoDialog { -moz-border-radius: 6px; -webkit-border-radius: 6px; background-color: #FFFFFF; border: 1px solid #AAAAAA; height: 240px; width: 300px; } .dojoDialogTitle { background: transparent url(/images/4.0/backgrounds/titlebar_bg.png) repeat-x scroll 0 0; padding: 4px; font-size:1.2em; font-weight:bold } .dojoDialogBody { padding-left: 15px; } .DashboardSelectBoxLabel { vertical-align: top; display: block; padding: 3px; } .dojoDialogFooter { height: 26px; bottom: 0; position: absolute; width: 294px; padding: 5px 3px 0; }\n");
		sb.append(
				".dojoDialogHeader { background-color: #60A5EA; color: #FFF; font-size: 12px; font-weight: 700; padding: 3px; } .dojoDialogMessage { background: #EFEFEF none repeat scroll 0; color: red; padding: 7px; } .exception table { border: 1px solid #2D6EBE; background-color: #FFF; } .errorTitle { background: transparent url(/images/4.0/backgrounds/hdbg.png) repeat-x scroll 0% 25%; color: #FFF; height: 21px } .exception table p { padding: 4px; } .errorTitle .exception td { color: #FFF; } .exception td { color: #000; } .exception a:visited,.exception a:hover,.exception a { color: #88C7FC; text-decoration: none } .editResourceOwner .BlockTitle { background: transparent url(/images/4.0/backgrounds/panel_gray.jpg) repeat-x scroll 0% 50%; }\n");
		sb.append(".select { }\n");
		sb.append(
				".pagetitle .pagetitle, .PortletTitle { color: #444444; font-family: arial,sans-serif; font-size: 1.1em; font-weight: 700; }\n");
		sb.append(".buttonTable { background-color: #fff; margin-top: -4px; }\n");
		sb.append(
				".wait { background: #000 url('/images/widget_bg.jpg') repeat-x; color: #fff; padding: 5px; position: absolute; line-height: 12pt; }\n");
		sb.append("th, td {white-space: normal;}\n");
		sb.append("td {vertical-align: top;}\n");
		return sb.toString();
	}

	public static String getDataTypeReport(final List<DataTypeReportRow> rows, final String htmlHeader) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<style>\n");
		sb.append(Report.getCss());
		sb.append("\n</style>\n</head>\n<body>\n");

		sb.append("<h2>Build environment overview</h2>\n");
		sb.append(htmlHeader);
		sb.append("</p>");

		sb.append("<h2>DataType overview</h2>\n");

		sb.append("<table class=\"portletlrborder\">\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th width=\"20%\">Type Name</th>\n");
		sb.append("<th width=\"11%\">Type Name space</th>\n");
		sb.append("<th width=\"20%\">Type Description</th>\n");
		sb.append("<th width=\"12%\">Inherited From</th>\n");
		sb.append("<th width=\"12%\">Field Name</th>\n");
		sb.append("<th width=\"5%\">Field Cardinality</th>\n");
		sb.append("<th width=\"20%\">Field Description</th>\n");
		sb.append("</tr>\n");
		rows.stream().forEach(r -> {
			sb.append("<tr class=\"ListRow\">\n");
			sb.append("<td>\n");
			sb.append(r.getName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getTargetNamespace());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(getString(r.getDescription()));
			sb.append("</td>\n");
			sb.append("<td>\n");
			if (r.getInheritedFrom().isEmpty()) {
				sb.append("[]");
			} else {
				r.getInheritedFrom().stream().forEach(fl -> sb.append(fl).append("<br/>"));
			}
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getFieldName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getFieldCardinality());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(getString(r.getFieldDescription()));
			sb.append("</td>\n");
			sb.append("</tr>\n");
		});
		sb.append("</table>\n");

		sb.append("\n</body>\n</html>\n");
		return sb.toString();
	}

	public static String getFlowReport(final List<FlowReportRow> rows, final String htmlHeader) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<style>\n");
		sb.append(Report.getCss());
		sb.append("\n</style>\n</head>\n<body>\n");

		sb.append("<h2>Build environment overview</h2>\n");
		sb.append(htmlHeader);
		sb.append("</p>");

		sb.append("<h2>Flow overview</h2>\n");

		sb.append("<table class=\"portletlrborder\">\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th width=\"15%\">Flow</th>\n");
		sb.append("<th width=\"4%\">Direction</th>\n");
		sb.append("<th width=\"19%\">Input/Output</th>\n");
		sb.append("<th width=\"4%\">Type</th>\n");
		sb.append("<th width=\"11%\">Processing Step</th>\n");
		sb.append("<th width=\"17%\">Description</th>\n");
		sb.append("<th width=\"30%\">Mapping Input Types</th>\n");
		sb.append("</tr>\n");
		rows.stream().forEach(r -> {
			sb.append("<tr class=\"ListRow\">\n");
			sb.append("<td>\n");
			sb.append(r.getFlowName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.isInbound() ? "Inpound" : "Outbound");
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getInputOutput());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getProcessingStepType());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getProcessingStepName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(getString(r.getStepDescription()));
			sb.append("</td>\n");
			sb.append("<td>\n");
			if (r.getMappingInputType().isEmpty()) {
				sb.append("[]");
			} else {
				r.getMappingInputType().stream().forEach(fl -> sb.append(fl).append("<br/>"));
			}
			sb.append("</td>\n");
			sb.append("</tr>\n");
		});
		sb.append("</table>\n");

		sb.append("\n</body>\n</html>\n");
		return sb.toString();
	}

	public static String getJson(final List<?> rows, final ObjectMapper mapper) {
		StringBuffer sb = new StringBuffer(1024);
		rows.stream().forEach(r -> {
			try {
				sb.append(mapper.writeValueAsString(r)).append("\n");
			} catch (Exception e) {
			}
		});
		return sb.toString();
	}

	public static String getMappingReport(final List<MappingReportRow> rows, final String htmlHeader) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<style>\n");
		sb.append(Report.getCss());
		sb.append("\n</style>\n</head>\n<body>\n");

		sb.append("<h2>Build environment overview</h2>\n");
		sb.append(htmlHeader);
		sb.append("</p>");

		sb.append("<h2>Mapping overview</h2>\n");

		sb.append("<table class=\"portletlrborder\">\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th width=\"10%\">Flow</th>\n");
		sb.append("<th width=\"10%\">Interface Type</th>\n");
		sb.append("<th width=\"6%\">Field</th>\n");
		sb.append("<th width=\"3%\">Cardinality</th>\n");
		sb.append("<th width=\"15%\">Field Description</th>\n");
		sb.append("<th width=\"10%\">Mapping</th>\n");
		sb.append("<th width=\"3%\">Mapping Type</th>\n");
		sb.append("<th width=\"3%\">Description</th>\n");
		sb.append("<th width=\"15%\">Mapping Input Types</th>\n");
		sb.append("</tr>\n");
		rows.stream().forEach(r -> {
			sb.append("<tr class=\"ListRow\">\n");
			sb.append("<td>\n");
			sb.append(r.getFlowName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getInterfaceName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getInterfaceFieldName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getInterfaceFieldCardinality());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(getString(r.getInterfaceFieldDescription()));
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getMappingTypeName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getMappingTypeType());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(getString(r.getMappingTypeDescription()));
			sb.append("</td>\n");
			sb.append("<td>\n");
			if (r.getMappingTypeInputTypes().isEmpty()) {
				sb.append("[]");
			} else {
				r.getMappingTypeInputTypes().stream().forEach(fl -> sb.append(fl).append("<br/>"));
			}
			sb.append("</td>\n");
			sb.append("</tr>\n");
		});
		sb.append("</table>\n");

		sb.append("\n</body>\n</html>\n");
		return sb.toString();
	}

	public static String getServiceReport(final List<ServiceReportRow> rows, final String htmlHeader) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<style>\n");
		sb.append(Report.getCss());
		sb.append("\n</style>\n</head>\n<body>\n");

		sb.append("<h2>Build environment overview</h2>\n");
		sb.append(htmlHeader);
		sb.append("</p>");

		sb.append("<h2>Service overview</h2>\n");

		sb.append("<table class=\"portletlrborder\">\n");
		sb.append("<tr class=\"tablerowheader\">\n");
		sb.append("<th width=\"12%\">Service</th>\n");
		sb.append("<th width=\"20%\">Operation</th>\n");
		sb.append("<th width=\"5%\">Request/Response</th>\n");
		sb.append("<th width=\"28%\">Description</th>\n");
		sb.append("<th width=\"20%\">Elements</th>\n");
		sb.append("<th width=\"15%\">Flow link</th>\n");
		sb.append("</tr>\n");
		rows.stream().forEach(r -> {
			sb.append("<tr class=\"ListRow\">\n");
			sb.append("<td>\n");
			sb.append(r.getServiceId());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.getOperationName());
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(r.isRequest() ? "Request" : "Response");
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(getString(r.getDescription()));
			sb.append("</td>\n");
			sb.append("<td>\n");
			sb.append(getString(r.getFields()));
			sb.append("</td>\n");
			sb.append("<td>\n");
			if (r.getFlowLinks().isEmpty()) {
				sb.append("[]");
			} else {
				r.getFlowLinks().stream().forEach(fl -> sb.append(fl).append("<br/>"));
			}
			sb.append("</td>\n");
			sb.append("</tr>\n");
		});
		sb.append("</table>\n");

		sb.append("\n</body>\n</html>\n");
		return sb.toString();
	}

	private static String getString(final String s) {
		String value = s;
		if (Objects.isNull(s)) {
			value = "missing";
		} else {
			value = value.replaceAll("\\n", "<br/>");
		}
		return value;
	}
}
