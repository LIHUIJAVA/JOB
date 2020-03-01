package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyAcctiMakDocInvtySubj;

public interface InvtyAcctiMakDocInvtySubjService {

	ObjectNode insertInvtyAcctiMakDocInvtySubj(InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj);
	
	ObjectNode updateInvtyAcctiMakDocInvtySubjByOrdrNum(InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj);
	
	ObjectNode deleteInvtyAcctiMakDocInvtySubjByOrdrNum(Integer ordrNum);
	
	InvtyAcctiMakDocInvtySubj selectInvtyAcctiMakDocInvtySubjByOrdrNum(Integer ordrNum);
	
    String selectInvtyAcctiMakDocInvtySubjList(Map map);
}
