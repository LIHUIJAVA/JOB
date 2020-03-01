package com.px.mis.ec.service;

import com.px.mis.ec.entity.CouponDetail;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PlatOrderService {

	public String add(String userId, PlatOrder platOrder, List<PlatOrders> platOrdersList);

	public String edit(PlatOrder platOrder, List<PlatOrders> platOrdersList,String userId);
	//批量修改发货仓库 逗号分隔的订单主键字段
	public String batchEditWhs(String orderIds,String userId,String whsEncd);
	//批量修改发货仓库 逗号分隔的订单主键字段
	public String batchEditExpress(String orderIds,String userId,String expressCode);
	public String delete(String orderId,String accNum);

	public String query(String orderId);

	public String queryList(Map map);

	public String download(String userId,String startDate,String endDate,int pageNo,int pageSize,String storeId);
	//京东单条订单下载
	public String jdDownloadByOrderId(String userId,Map map);
	//单条订单下载入口
	public String downloadByOrderId(String json);
	//根据订单号计算每条明细的实付金额
	public List<PlatOrders> jisuanPayPrice(String orderid);
	
	public String unionQuery(String orderId);
	
	/**
	 * 根据订单里面的sku和spu拆解订单明细
	 */
	public List<PlatOrders> checkInvty(PlatOrder platOrder,List<PlatOrders> platOrdersList,List<CouponDetail> couponDetails);
	
	
	public String exportPlatOrder(Map map);
	
	public String autoMatch(String orderId,String accNum);
	/**
	 * 单条订单拆分
	 * @param orderId 被拆分的订单号
	 * @param platOrdersIds 被拆分的订单明细id，逗号分隔
	 * @param splitNum 拆分数量，逗号分隔
	 * @param 操作人id
	 * @return
	 */
	public String splitOrder(String orderId,String platOrdersIds,String splitNum,String accNum);
	/**
	 * 为订单明细分配批次及扣对应可用量
	 * @param platOrders 订单明细
	 * @return map 
	 * flag：true时分配成功 false时分配失败，可用量不足  
	 * platOrders： 分配成功时返回的订单明细列表
	 * message：分配失败原因
	 */
	public Map checkCanUseNumAndBatch(PlatOrders platOrders);
	
	public String importPlatOrder(MultipartFile  file,String userId);
	
	public String Near15DaysOrder();
	/**
	 * 批量查询
	 * @param list
	 * @return
	 */
	public String batchSelect(List<String> list,String isAudit);
	/**
	 * 订单明细列表查询
	 * @param map
	 * @return
	 */
	public String orderssssList(Map map);
	/**
	 * 订单明细列表导出
	 * @param map
	 * @return
	 */
	public String exportOrderssssList(Map map);
	/**
	 * 订单明细列表批量查询
	 * @param list
	 * @return
	 */
	public String batchList(List<String> list);
	/**
	 * 调整商品
	 */
	public String updateGooId(PlatOrder platOrder,String invId,String invIdLast,String multiple,List<PlatOrders> platOrders);
	
	public String selectecGooId(List<String> list);
	
	public PlatOrder selectecByorderId(String orderId);
	
	public List<PlatOrders> selectecByorderIds(String orderId);
	
	/**
	 * 订单关闭
	 * @param orderId 以逗号分隔的订单号
	 * @param accNum
	 * @return
	 */
	public String closeOrder(String orderId,String accNum);
	

	/**
	 * 订单打开
	 * @param orderId 以逗号分隔的订单号
	 * @param accNum
	 * @return
	 */
	public String openOrder(String orderId,String accNum);


}
