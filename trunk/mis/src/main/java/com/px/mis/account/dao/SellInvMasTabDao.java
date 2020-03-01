package com.px.mis.account.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellInvMasTab;

public interface SellInvMasTabDao {
	
	int insertSellInvMasTab(SellInvMasTab sellInvMasTab);
	
	int updateSellInvMasTabBySellInvNum(SellInvMasTab sellInvMasTab);
	
	int deleteSellInvMasTabBySellInvNum(String sellInvNum);
	
	int deleteSellInvMasTabList(List<String> sellInvNum);
	
	SellInvMasTab selectSellInvMasTabBySellInvNum(String sellInvNum);

    List<SellInvMasTab> selectSellInvMasTabList(Map map);
    
    int selectSellInvMasTabCount(Map map);
    
    int selectSellInvMasTabIsNtChk(String sellInvNum);
    //按照销售单号计算总金额
	BigDecimal countSellNoTaxAmt(Map<String, String> dataMap);
	
	//修改审核状态
    int updateSellInvMasTabIsNtChk(SellInvMasTab sellInvMasTab);
    
    //查询记账状态
    int selectSellInvMasTabIsNtBookEntry(String sellInvNum);
    
    String selectSellSnglNumBySellInvMasTab(String sellInvNum);
    
    //导入时新增销售发票主表
    int insertSellInvMasTabUpload(SellInvMasTab sellInvMasTab);
    
    //导出时查询接口
    List<SellInvMasTab> printingSellInvMasTabList(Map map);
}
