package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.SellComnInvSub;

public interface SellComnInvSubDao{
	
	int insertSellComnInvSub(SellComnInvSub sellComnInvSub);
	
	int insertSellComnInvSubList(List<SellComnInvSub> sellComnInvSub);
	
	int updateSellComnInvSub(SellComnInvSub sellComnInvSub);
	
	int deleteSellComnInvSubBySellInvNum(String sellInvNum);
	
	SellComnInvSub selectSellComnInvSubByOrdrNum(Integer ordrNum);

    List<SellComnInvSub> selectSellComnInvSubList(Map map);
    
    int selectSellComnInvSubCount();
}
