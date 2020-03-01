package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyDetails;
import com.px.mis.account.entity.SellOutWhsPool;
import com.px.mis.purc.entity.SellOutWhs;

public interface InvtyDetailDao {
	//查询该存货对应明细是否存在
	List<InvtyDetail> selectByInvty(Map<String, Object> paramMap);
    //增加存货明细
	int insertInvtyDetail(InvtyDetail invty);
	int insertInvtyDetailList(List<InvtyDetail> invty);
	
	//批量新增明细子表
	int insertInvtyDetailsList(List<InvtyDetails> list);
	//批量删除明细帐子表
	int delectInvtyList(List<InvtyDetails> invtyDetailList);
	//批量新增明细子表
	int insertInvtyDetails(InvtyDetails invtys);
	//查询收发存汇总
	List<InvtyDetail> selectSendAndRecePool(Map map);
	//计算收发存汇总
	int countSelectSendAndRecePool(Map map);
	//查询发出商品汇总
	List<SellOutWhsPool> sendProductsPool(Map map);
	//计算发出商品汇总
	int countSendProductsPool(Map map);
	//查询发出商品
	List<SellOutWhsPool> sendProductList(Map map);
	//计算发出商品
	List<SellOutWhsPool> countSendProductList(Map map);
	
	//查询存货明细
	List<InvtyDetail> selectByInvtyDeatil(Map<String, Object> paramMap);
	
	List<InvtyDetail> selectByInvtyDeatilsList(Map<String, Object> paramMap);
	//进销存-查询期初
	List<InvtyDetail> selectBeginDataByMap(Map map);
	//进销存-查询期初计数
	int selectBeginDataByMapCount(Map map);
	//sum 期初数据
	InvtyDetail sumQtyAndAmtByInvtyEncd(Map map);
	
	
}
