package com.px.mis.ec.dao;

import java.util.List;

import com.px.mis.ec.entity.CouponDetail;

public interface CouponDetailDao {
	public int insert(List<CouponDetail> couponlist);
	
	public void delete(String orderId);
	/**
	 * 根据平台id和平台订单号查询优惠金额
	 * @param platId 平台id
	 * @param orderid 平台订单号
	 * @param skuid sku编号 
	 * @param type 查询类型 1：查询单品促销 针对单品扣减实付金额  2：查询整单店铺优惠 分摊到明细中
	 * @return 金额count
	 */
	public int selectByOrderId(String platId,String orderid,String skuid,int type);
	
	/**
	 * 根据平台订单号查询优惠明细列表
	 * @param ecOrderId 平台订单号
	 * @return List
	 */
	public List<CouponDetail> couponDetailList(String ecOrderId);

}
