package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;
import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.service.impl.PursOrdrServiceImpl.zizhu;

public interface PursOrdrDao {
	
    int deletePursOrdrByPursOrdrId(String pursOrdrId);

    int insertPursOrdr(PursOrdr pursOrdr);
    
    int insertPursOrdrUpload(List<PursOrdr> pursOrdr);//导入时使用的新增采购订单接口
    
    int updatePursOrdrByPursOrdrId(PursOrdr pursOrdr);

    PursOrdr selectPursOrdrByPursOrdrId(String pursOrdrId);
    
    PursOrdr selectPursOrdrById(String pursOrdrId);
    
	List<PursOrdr> selectPursOrdrList(Map map);
	//分页+排序
	List<zizhu> selectPursOrdrListOrderBy(Map map);
	
	int selectPursOrdrCount(Map map);
	
	//批量删除采购订单
	int deletePursOrdrList(List<String> pursOrdrId);
	
	//批量更新审核状态
	int  updatePursOrdrIsNtChkList(List<PursOrdr> pursOrdr);
	//单个修改审核状态
	int  updatePursOrdrIsNtChk(PursOrdr pursOrdr);
	//查询审核状态
    int selectPursOrdrIsNtChk(String pursOrdrId);
		
	List<zizhu> printingPursOrdrList(Map map);
	
	//采购明细表
	List<Map> selectPursOrdrByInvtyEncd(Map map);
	
	int selectPursOrdrByInvtyEncdCount(Map map);
	
	//付款申请单参照时条件查询采购订单主表信息
	List<PursOrdr> selectPursOrdrLists(Map map);
	
	int selectPursOrdrCounts(Map map);
	
	int insertPursOrdrDl(List<String> lists2);

	Map<String, String> selectTableSum(Map map);

	Map selectPursOrdrListSum(Map map);//采购订单列表总计

	Map selectPursOrdrByInvtyEncdSums(Map map);//明细表总计
	
	//到货单参照时条件查询采购订单主表信息
	List<PursOrdr> selectPursOrdrListByToGdsSngl(Map map);
	
	int selectPursOrdrCountByToGdsSngl(Map map);
	//采购明细表--导出
	List<Map> selectPursOrdrByInvtyEncdPrint(Map map);
	
	
	
	
	
	
}