package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.PursInvMasTab;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.purc.entity.IntoWhs;

public interface PursInvMasTabService {

	//新增采购专用发票
	ObjectNode insertPursInvMasTab(PursInvMasTab pursInvMasTab);
	
	//修改采购专用发票
	ObjectNode updatePursInvMasTabById(PursInvMasTab pursInvMasTab,List<PursInvSubTab> pursInvSubTabList);
	
	//删除采购专用发票
	ObjectNode deletePursInvMasTabByPursInvNum(String pursInvNum);
	
	//查询采购专用发票详细信息
	PursInvMasTab selectPursInvMasTabByPursInvNum(String pursInvNum);
	
	//查询所有采购专用发票
    String selectPursInvMasTabList(Map map);
    
    //批量删除采购专用发票
    String deletePursInvMasTabList(String pursInvNum);
    
   //审核采购专用发票
   String  updatePursInvMasTabIsNtChkList(List<PursInvMasTab> pursInvMasTabList);
   
   //采购发票新增时，参照采购入库单并带入多张采购入库单
   String selectPursInvMasTabBingList(List<IntoWhs> intoWhsList);
   
   //导入销售出库单
   public String uploadFileAddDb(MultipartFile  file);
 	
   //导出时，使用的查询接口
   String upLoadPursInvMasTabList(Map map);
}
