package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellInvMasTab;
import com.px.mis.account.entity.SellInvSubTab;
import com.px.mis.purc.entity.SellSngl;

public interface SellInvMasTabService {

	ObjectNode insertSellInvMasTab(SellInvMasTab sellInvMasTab,String userId);
	
	ObjectNode updateSellInvMasTab(SellInvMasTab sellInvMasTab,List<SellInvSubTab> sellInvSubTabList);
	
	ObjectNode deleteSellInvMasTabBySellInvNum(String sellInvNum);
	
	String deleteSellInvMasTabList(String sellInvNum);
	
	SellInvMasTab selectSellInvMasTabBySellInvNum(String sellInvNum);
	
    String selectSellInvMasTabList(Map map);
    
    //审核销售专用发票
    String  updateSellInvMasTabIsNtChkList(List<SellInvMasTab> sellInvMasTabList);
    
    //销售发票新增时，参照销售出库单并带入多张销售出库单
    String selectSellInvMasTabBingList(List<SellComnInv> sellSnglList);
    
  //导入销售出库单
  	public String uploadFileAddDb(MultipartFile  file);
  	
    //导出时，使用的查询接口
    String upLoadSellInvMasTabList(Map map);
}
