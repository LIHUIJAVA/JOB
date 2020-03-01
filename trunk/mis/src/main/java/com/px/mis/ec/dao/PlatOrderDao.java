package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.Near15DaysOrder;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellSngl;

public interface PlatOrderDao {

	public int insert(PlatOrder platOrder);
	
	public void update(PlatOrder platOrder);
	/**
	 * 更新订单审核状态并返回成功条数，成功条数等于1时生成销售单，否则不生成
	 * @param platOrder
	 * @return
	 */
	public int updateAudit(PlatOrder platOrder);
	public void updateGoodNum(PlatOrder platOrder);
	
	public void delete(String orderId);
	
	public PlatOrder select(String orderId);
	public PlatOrder selectecByorderId(String orderId);
	public  List<PlatOrders> selectecByorderIds(String orderId);
	
	public String selectecGooId(List<String> list);
	
	public int checkExsits(String ecOrderId,String storeId);
	
	public int checkExsits1(String ecOrderId);
	
	public List<PlatOrder> selectList(Map map);
	
	public int selectCount(Map map);

	public void insertList(List<PlatOrder> platOrderList);
	
	public Map<String, String> selectNumByOrderAndSKu(Map<String, String> map);

	public PlatOrder selectByEcOrderId(String ecOrderId);
	
	public List<PlatOrder> selectPlatOrdersByEcOrderId(String ecOrderId);
	
	public int selectNoAuditCountByOrderIdAndStoreId(@Param("storeId")String storeId,@Param("ecOrderId")String ecOrderId);
	
	//订单联查对应销售单 传orderId
	public SellSngl selectSellSnglByOrderId(String orderId);
	
	public List<RefundOrder> selectRefundOrderByOrderId(String orderId);
	//订单联查对应退款单 传ecorderid
	public List<RefundOrder> selectRefundOrderByEcOrderId(String ecOrderId);
	//订单联查对应的物流单
	public List<LogisticsTab> selectLogisticsTabByOrderId(String orderId);
	//订单联查对应销售出库单 传销售单编号
	public List<SellOutWhs> selectSellOutWhsByOrderId(String orderId);
	//跟据平台订单号count订单未审核数量
	public int selectNoAuditOrderByEcOrderId(String ecOrderId);
	//
	public List<PlatOrder> exportPlatOrder(Map map);
	public List<Map> exportList1(Map map);//导出新
	/**
	 * 根据订单号和sku查询订单明细，京东自主售后用
	 * @param map
	 * @return
	 */
	public List<PlatOrders> selectNumAndPayMoneyByOrderAndSKu(Map map);
	
	/**
	 * 根据订单号和ecGoodId查询订单明细，苏宁退款用
	 * @param map
	 * @return
	 */
	public List<PlatOrders> selectNumAndPayMoneyByOrderAndSKuSN(Map map);
	
	public List<PlatOrder> selectByOrderId(String orderId);
	
	/**
	 * 最近15天订单数据统计
	 * @return list
	 */
	public List<Near15DaysOrder> Near15DaysOrder();
	
	/**
	 * 批量修改快递公司编码
	 */
	
	public void updateExpress(@Param("list") List list,@Param("expressEncd") String expressEncd);
	
	/**
	 * 根据订单号批量查询订单列表
	 * @param OrderIds 平台订单好列表
	 * @return
	 */
	public List<PlatOrder> batchList(@Param("list")List<String> list,@Param("isAudit")String isAudit);
	
	
	public int closeOrder(String orderId);

	public int openOrder(String orderId);
	
	public int selectInvNumCountByOrderId(String orderId);
}
