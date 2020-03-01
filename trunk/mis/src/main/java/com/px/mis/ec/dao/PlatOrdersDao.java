package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.exportOrders;
import org.apache.commons.collections.map.CompositeMap.MapMutator;
import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.Orderssss;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;

public interface PlatOrdersDao {

	public int insert(@Param("platOrders")List<PlatOrders> platOrders);

	public void delete(String orderId);

	public List<PlatOrders> select(String orderId);

	public PlatOrders selectById(Integer orderItemId);

	public void update(Map map);
	
	public void updateGooId(Map map);
	
	public void updateGooIds(PlatOrders orders);

	public void updatePayMoney(PlatOrders orders);

	public void deleteByOrdersNo(Long no);

	public PlatOrders selectByNo(Long no);

	public List<PlatOrders> selectByEcOrderId(String ecOrderId);

	/**
	 * 订单拆分时使用，更新数量及金额
	 */
	public void updateNumAndMoney(PlatOrders orders);

	public List<PlatOrders> checkInvIdHasNull(String orderId);

	/**
	 * 根据订单号、存货编码、批号查询列表
	 */
	public List<PlatOrders> selectByEcOrderIdAndInvIdAndBatNum(@Param("ecOrderId") String ecOrderId,
			@Param("invId") String invId, @Param("batNum") String batNum);

	/**
	 * 退款单审核时修改可退金额及可退数量
	 */
	public void updateCanRefMoneyAndNum(PlatOrders platOrders);

	/**
	 * 订单 商品id sku
	 */
	public List<PlatOrders> selectByEcOrderAndGood(@Param("ecOrderId") String ecOrderId, @Param("goodId") String goodId,
			@Param("goodSku") String goodSku);
	
	/**
	 * 根据订单号查询订单明细中不重复的ecGoodId
	 * 根据ecgoodId分组
	 * 苏宁订单发货用
	 * @param orderId
	 * @return
	 */
	public List<PlatOrders> selectListByOrderIdAndGroupByEcGoodId(String orderId);
	
	
	/**
	 * 订单列表查询
	 */
	public List<Orderssss> orderssssList(Map map);
	
	public int selectCount(Map map);
	/**
	 * 订单明细列表导出
	 * @param map
	 * @return
	 */
	public List<exportOrders> exportOrderssssList(Map map);
	/**
	 * 订单明细列表批量查询
	 * @param list
	 * @return
	 */
	public List<Orderssss> batchList(@Param("list")List<String> list);
	
	/**
	 * 根据订单号查询订单明细，按照存货编码+批次分组
	 */
	public List<PlatOrders> platOrdersByInvIdAndBatch(String orderId);
	
	/**
	 * 修改商品的商品编码和倍数
	 * 
	 */

	public int updateecGooIdmultiple(List<String> list);
	
	public int selectgoodNum(String orderId);

	/**
	 * 根据订单编号分组
	 * 
	 */
	public List<PlatOrders> selectOrderByinvId(Map<String, String> map);
	
	public String selectorderIdByinvId(PlatOrders platorders);
	
	public void updateInvIdGooIdsGoodMoney(PlatOrders platOrders);
	
	//public void updatePlatOrdersBatchNoNull(PlatOrders platOrders);
}
