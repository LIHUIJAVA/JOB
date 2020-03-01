package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursInvMasTab;
import com.px.mis.account.entity.PursInvSubTab;

public interface PursInvMasTabDao{
	
	//新增采购专用发票
	int insertPursInvMasTab(PursInvMasTab pursInvMasTab);
	
	//修改采购专用发票
	int updatePursInvMasTabById(PursInvMasTab pursInvMasTab);
	
	//删除采购专用发票
	int deletePursInvMasTabById(String pursInvNum);
	
	//按照发票号查询采购专用发票详情
	PursInvMasTab selectPursInvMasTabById(String pursInvNum);

	//分页查询采购专用发票
    List<PursInvMasTab> selectPursInvMasTabList(Map map);
    
    //查询采购专用发票总条数
    int selectPursInvMasTabCount(Map map);
    
    //批量删除采购专用发票
    int deletePursInvMasTabList(List<String> pursInvNum);
    
    //查询采购专用发票是否审核
    int selectPursInvMasTabIsNtChk(String pursInvNum);
    
    //按照采购发票和其他入库单
    PursInvSubTab countPursNoTaxAmt(Map map);
    //按照采购入库单和其他入库单
    PursInvSubTab countIntoNoTaxAmt(Map<String, Object> dataMap);
    //计算采购发票数量和金额 -成本
   	PursInvSubTab countPursNoTaxAmtAndQty(Map<String, Object> dataMap);
    //计算销售和其他单据数量和金额-计算成本
   	PursInvSubTab countSellAndOthAmt(Map<String, Object> dataMap);
   	
   	List<PursInvSubTab> countPursNoTaxAmtAndQtyList(Map<String, Object> dataMap);
   	List<PursInvSubTab> countSellAndOthAmtList(Map<String, Object> dataMap);
   	
    //修改审核状态
    int updatePursInvMasTabIsNtChk(PursInvMasTab pursInvMasTab);
    
    //查询采购发票记账状态
    int selectPursInvMasTabIsNtBookEntry(String pursInvNum);
    
    String selectIntoWhsSnglIdByPursInvMasTab(String pursInvNum);
    
    //导入时新增采购专用发票
    int insertPursInvMasTabUpload(PursInvMasTab pursInvMasTab);
    
    //导出时查询接口
    List<PursInvMasTab> printingPursInvMasTabList(Map map);

	

	

	
   

	
}
