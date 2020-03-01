package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrderExport;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellSngl;

public interface RefundOrderDao  {

	public void insertList(List<RefundOrder> refundOrderList);
	
	public void insert(RefundOrder refundOrder);
	
	public void update(RefundOrder refundOrder);
	/**
	 * 更新退款单审核状态，返回1时生成后续退货单
	 * @param refundOrder
	 */
	public int updateAudit(RefundOrder refundOrder);
	
	
	
	public void delete(List<String> refIds);
	
	public RefundOrder select(String refId);
	
	public List<RefundOrder> selectList(Map map);
	
	public int selectCount(Map map);
	
	public int audit(List<String> refIds);
	
	public int noAudit(List<String> refIds);
	
	public List<RefundOrder> selectListByRefIds(List<String> refIds);
	//根据销售单中的tx_id（电商订单号list）查询销售单列表
	public List<SellSngl> selectSellSnglsByOrderId(List<PlatOrder> platOrders);
	
	public List<PlatOrder> selectPlatOrderListByEcOrderId(String ecOrderId);
	//根据退款单号查询对应退货单
	public RtnGoods selectRtnGoodsByRefId(String refId);
	//根据对应退货单号查询销售出库单
	public SellOutWhs selectSellOutWhsByRtnId(String rtnId);
	//平台退款号查询
	public RefundOrder selectEcRefId(String ecrefId);
	//根据平台订单号查询退款单
	public RefundOrder selectByEcOrderId(@Param("ecOrderId")String ecOrderId,
										 @Param("storeId")String storeId);
	
	//导出
	public List<Map> exportList(Map map);
	
	//public int selectCountByEcOrderId(String ecOrderId)
	public int selectCountByEcOrderId(String ecOrderId);

}
