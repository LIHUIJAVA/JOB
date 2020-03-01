package com.px.mis.purc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.entity.PursOrdrSub;

public interface PursOrdrService {

	String addPursOrdr(String userId,PursOrdr pursOrdr,List<PursOrdrSub> pursOrdrSubList,String loginTime) throws Exception;
	
	String editPursOrdr(PursOrdr pursOrdr,List<PursOrdrSub> pursOrdrSubList) throws Exception;
	
	String deletePursOrdr(String pursOrdrId);
	
	String queryPursOrdr(String pursOrdrId);
	
	String queryPursOrdrList(Map map);

	String deletePursOrdrList(String pursOrdrId);
	
	String updatePursOrdrIsNtChkList(List<PursOrdr> pursOrdr);
	
	String printingPursOrdrList(Map map);
	
	public String uploadFileAddDb(MultipartFile  file,int i,HashMap map);
	
	//采购明细
	String queryPursOrdrByInvtyEncd(Map map);
	
	//参照时查询所有未申请付款金额的采购订单子表信息
	public String selectUnApplPayQtyByPursOrdrId(String pursOrdrId);
	
	//参照时查询所有未到货数量的采购订单子表信息
    public String selectUnToGdsQtyByPursOrdrId(String pursOrdrId);
    
     //到货单、付款申请单参照时条件查询采购订单主表信息
    String queryPursOrdrLists(Map map);

	String queryPursOrdrListOrderBy(Map map);

	String queryPursOrdrByInvtyEncdPrint(Map map);
}
