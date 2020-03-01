package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyAcctiSysTab;

public interface InvtyAcctiSysTabService {

	ObjectNode insertInvtyAcctiSysTab(InvtyAcctiSysTab invtyAcctiSysTab);
	
	ObjectNode updateInvtyAcctiSysTabByOrdrNum(InvtyAcctiSysTab invtyAcctiSysTab);
	
	ObjectNode deleteInvtyAcctiSysTabByOrdrNum(Integer ordrNum);
	
	InvtyAcctiSysTab selectInvtyAcctiSysTabByOrdrNum(Integer ordrNum);
	
    String selectInvtyAcctiSysTabList(Map map);
}
