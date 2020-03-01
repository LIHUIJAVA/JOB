package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyTermBgnTab;

public interface InvtyTermBgnTabService {

	ObjectNode insertInvtyTermBgnTab(InvtyTermBgnTab invtyTermBgnTab);
	
	ObjectNode updateInvtyTermBgnTabByOrdrNum(InvtyTermBgnTab invtyTermBgnTab);
	
	ObjectNode deleteInvtyTermBgnTabByOrdrNum(Integer ordrNum);
	
	InvtyTermBgnTab selectInvtyTermBgnTabByOrdrNum(Integer ordrNum);
	
    String selectInvtyTermBgnTabList(Map map);
}
