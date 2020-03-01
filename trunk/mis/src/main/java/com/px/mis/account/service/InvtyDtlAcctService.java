package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyDtlAcct;

public interface InvtyDtlAcctService {

	ObjectNode insertInvtyDtlAcct(InvtyDtlAcct invtyDtlAcct);
	
	ObjectNode updateInvtyDtlAcctByOrdrNum(InvtyDtlAcct invtyDtlAcct);
	
	ObjectNode deleteInvtyDtlAcctByOrdrNum(Integer ordrNum);
	
	InvtyDtlAcct selectInvtyDtlAcctByOrdrNum(Integer ordrNum);
	
    String selectInvtyDtlAcctList(Map map);
}
