package com.px.mis.account.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.PursInvSubTab;

public interface PursInvSubTabService {
	
	ObjectNode insertPursInvSubTab(PursInvSubTab pursInvSubTab);
	
	ObjectNode updatePursInvSubTabById(PursInvSubTab pursInvSubTab);
	
	Integer deletePursInvSubTabByPursInvNum(String pursInvNum);
	
	PursInvSubTab selectPursInvSubTabById(String vouchCateSubTabId);
	
    List<PursInvSubTab> selectPursInvSubTabList();
}
