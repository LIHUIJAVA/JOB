package com.px.mis.account.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInv;

public interface SellComnInvService {

	ObjectNode addSellComnInv(SellComnInv sellComnInv,String userId,String loginTime);
	
	ObjectNode editSellComnInv(SellComnInv sellComnInv,List<SellComnInvSub> sellComnInvSubList);
	
	ObjectNode deleteSellComnInvBySellInvNum(String sellInvNum);
	
	String deleteSellComnInvList(String sellInvNum);
	
	String selectSellComnInvBySellInvNum(String sellInvNum);
	
    String selectSellComnInvList(Map map);
    
    //审核销售普通发票
    Map<String,Object>  updateSellComnInvIsNtChkList(SellComnInv sellComnInv) throws Exception;
    
    //销售发票新增时，参照销售出库单并带入多张销售出库单
    String selectSellComnInvBingList(List<SellComnInv> sellComnInvList);
    
  //导入销售出库单
  	public String uploadFileAddDb(MultipartFile  file,int i);
  	
 
    String selectSellReturnEntrs(Map map) throws IOException;//参照销售单、退货单、委托代销发货单
    
    String selectSellComnInvBySellRtnEntList(List<SellComnInv> sellComnInvList);//参照时将销售单、退货单、委托代销发货单子表信息返给前端

    //原来的导出接口
    String upLoadSellComnInvList(Map map);
    //导出接口
	String printSellComnInvList(Map map);
	//前端交互,推送
	String pushToU8(String ids) throws IOException;
	//推送到U8
	U8SellComnInv encapsulation(SellComnInv sellComnInv, List<SellComnInvSub> sellComnInvSubList);

    
}
