package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.CustCls;

public interface CustClsDao {
	
	//新增客户分类
	int insertCustCls(CustCls custCls);
	
	//修改客户分类
	int updateCustClsByClsId(CustCls custCls);
	
	//删除客户分类
	int deleteCustClsByClsId(String clsId);
	
	//查询客户分类明细
	CustCls selectCustClsByClsId(String clsId);
	
	//查询所有客户分类信息
	List<CustCls> selectCustCls();
	
}