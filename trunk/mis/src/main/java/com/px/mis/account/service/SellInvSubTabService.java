package com.px.mis.account.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.SellInvSubTab;

public interface SellInvSubTabService {
	
	ObjectNode insertSellInvSubTab(SellInvSubTab sellInvSubTab);
	
	ObjectNode updateSellInvSubTabById(SellInvSubTab sellInvSubTab);
	
	Integer deleteSellInvSubTabBySellInvNumv(String sellInvNum);
	
	SellInvSubTab selectSellInvSubTabById(Integer ordrNum);
	
    List<SellInvSubTab> selectSellInvSubTabList();
}
