package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.SellInvSubTab;

public interface SellInvSubTabDao{
	
	int insertSellInvSubTab(SellInvSubTab sellInvSubTab);
	
	int insertSellInvSubTabList(List<SellInvSubTab> sellInvSubTab);
	
	int updateSellInvSubTab(SellInvSubTab sellInvSubTab);
	
	int deleteSellInvSubTabBySellInvNum(String sellInvNum);
	
	SellInvSubTab selectSellInvSubTabByOrdrNum(Integer ordrNum);

    List<SellInvSubTab> selectSellInvSubTabList(Map map);
    
    int selectSellInvSubTabCount();
}
