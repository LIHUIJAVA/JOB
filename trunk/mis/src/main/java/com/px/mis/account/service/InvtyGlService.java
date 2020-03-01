package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyGl;

public interface InvtyGlService {

	ObjectNode insertInvtyGl(InvtyGl invtyGl);
	
	ObjectNode updateInvtyGlByOrdrNum(InvtyGl invtyGl);
	
	ObjectNode deleteInvtyGlByOrdrNum(Integer ordrNum);
	
	InvtyGl selectInvtyGlByOrdrNum(Integer ordrNum);
	
    String selectInvtyGlList(Map map);
}
