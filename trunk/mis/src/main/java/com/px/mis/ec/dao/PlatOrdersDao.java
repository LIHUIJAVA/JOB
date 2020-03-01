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
	 * �������ʱʹ�ã��������������
	 */
	public void updateNumAndMoney(PlatOrders orders);

	public List<PlatOrders> checkInvIdHasNull(String orderId);

	/**
	 * ���ݶ����š�������롢���Ų�ѯ�б�
	 */
	public List<PlatOrders> selectByEcOrderIdAndInvIdAndBatNum(@Param("ecOrderId") String ecOrderId,
			@Param("invId") String invId, @Param("batNum") String batNum);

	/**
	 * �˿���ʱ�޸Ŀ��˽���������
	 */
	public void updateCanRefMoneyAndNum(PlatOrders platOrders);

	/**
	 * ���� ��Ʒid sku
	 */
	public List<PlatOrders> selectByEcOrderAndGood(@Param("ecOrderId") String ecOrderId, @Param("goodId") String goodId,
			@Param("goodSku") String goodSku);
	
	/**
	 * ���ݶ����Ų�ѯ������ϸ�в��ظ���ecGoodId
	 * ����ecgoodId����
	 * ��������������
	 * @param orderId
	 * @return
	 */
	public List<PlatOrders> selectListByOrderIdAndGroupByEcGoodId(String orderId);
	
	
	/**
	 * �����б��ѯ
	 */
	public List<Orderssss> orderssssList(Map map);
	
	public int selectCount(Map map);
	/**
	 * ������ϸ�б���
	 * @param map
	 * @return
	 */
	public List<exportOrders> exportOrderssssList(Map map);
	/**
	 * ������ϸ�б�������ѯ
	 * @param list
	 * @return
	 */
	public List<Orderssss> batchList(@Param("list")List<String> list);
	
	/**
	 * ���ݶ����Ų�ѯ������ϸ�����մ������+���η���
	 */
	public List<PlatOrders> platOrdersByInvIdAndBatch(String orderId);
	
	/**
	 * �޸���Ʒ����Ʒ����ͱ���
	 * 
	 */

	public int updateecGooIdmultiple(List<String> list);
	
	public int selectgoodNum(String orderId);

	/**
	 * ���ݶ�����ŷ���
	 * 
	 */
	public List<PlatOrders> selectOrderByinvId(Map<String, String> map);
	
	public String selectorderIdByinvId(PlatOrders platorders);
	
	public void updateInvIdGooIdsGoodMoney(PlatOrders platOrders);
	
	//public void updatePlatOrdersBatchNoNull(PlatOrders platOrders);
}
