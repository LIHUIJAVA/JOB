package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyCapOcupLaytTab;

public interface InvtyCapOcupLaytTabService {

	ObjectNode insertInvtyCapOcupLaytTab(InvtyCapOcupLaytTab invtyCapOcupLaytTab);
	
	ObjectNode updateInvtyCapOcupLaytTabByOrdrNum(InvtyCapOcupLaytTab invtyCapOcupLaytTab);
	
	ObjectNode deleteInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum);
	
	InvtyCapOcupLaytTab selectInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum);
	
    String selectInvtyCapOcupLaytTabList(Map map);
}
