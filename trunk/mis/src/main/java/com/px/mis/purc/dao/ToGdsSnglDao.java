package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.service.impl.ToGdsSnglServiceImpl;
import com.px.mis.purc.service.impl.ToGdsSnglServiceImpl.zizhu;


public interface ToGdsSnglDao {
	
	//删除单个到货单信息
    int deleteToGdsSnglByToGdsSnglId(String toGdsSnglId);
    
    //新增到货单信息
    int insertToGdsSngl(ToGdsSngl toGdsSngl);
    
    //删除时候备份到货单信息
    int insertToGdsSnglDl(List<String> lists2);
    
    //导入导出时新增到货单信息
    int insertToGdsSnglUpload(List<ToGdsSngl> toGdsSngl);
    
    //修改到货单信息
    int updateToGdsSnglByToGdsSnglId(ToGdsSngl toGdsSngl);
    
    //根据到货单编码查询到货单主子关联信息
    ToGdsSngl selectToGdsSnglByToGdsSnglId(String toGdsSnglId);
    
    //根据到货单编码查询到货单主表信息
    ToGdsSngl selectToGdsSnglById(String toGdsSnglId);

    //分页查询到货单列表
	 List<ToGdsSngl> selectToGdsSnglList(Map map); 
    //分页带排序
    List<ToGdsSnglServiceImpl.zizhu> selectToGdsSnglListOrderBy(Map map);
    
	int selectToGdsSnglCount(Map map);
	
	//批量删除到货单列表
	int deleteToGdsSnglList(List<String> toGdsSnglId);
	
	//批量修改审核状态
	int updateToGdsSnglIsNtChkList(List<ToGdsSngl> toGdsSngl);
	
	//单个修改审核状态
	Integer updateToGdsSnglIsNtChk(ToGdsSngl toGdsSngl);
	
	//查询审核状态
	Integer selectToGdsSnglIsNtChk(String toGdsSnglId);
	
	//查询退货状态
	Integer selectToGdsSnglIsNtRtnGood(String toGdsSnglId);
	
	List<zizhu> printingToGdsSnglList(Map map);
	
	//根据采购订单号查询到货单信息
	List<ToGdsSngl> selectToGdsSnglByPursOrdrId(String pursOrdrId);
	
	//Map selectToGdsSnglsByPursOrdrId(ToGdsSngl toGdsSngl);
	
	//到货明细表分页查询功能
	List<Map> selectToGdsSnglByInvtyEncd(Map map);
	
	int selectToGdsSnglByInvtyEncdCount(Map map);
	
	//采购入库单参照时查询所有到货单主表信息
	List<ToGdsSngl> selectToGdsSnglLists(Map map);
	
	int selectToGdsSnglCounts(Map map);

	Map selectToGdsSnglListSums(Map map);
	
	//修改到货单处理状态
	int updateToGdsSnglDealStatOK(String toGdsSnglId);
	int updateToGdsSnglDealStatNO(String toGdsSnglId);

}