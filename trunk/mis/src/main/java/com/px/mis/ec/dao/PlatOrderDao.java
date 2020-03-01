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
	 * ���¶������״̬�����سɹ��������ɹ���������1ʱ�������۵�����������
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
	
	//���������Ӧ���۵� ��orderId
	public SellSngl selectSellSnglByOrderId(String orderId);
	
	public List<RefundOrder> selectRefundOrderByOrderId(String orderId);
	//���������Ӧ�˿ ��ecorderid
	public List<RefundOrder> selectRefundOrderByEcOrderId(String ecOrderId);
	//���������Ӧ��������
	public List<LogisticsTab> selectLogisticsTabByOrderId(String orderId);
	//���������Ӧ���۳��ⵥ �����۵����
	public List<SellOutWhs> selectSellOutWhsByOrderId(String orderId);
	//����ƽ̨������count����δ�������
	public int selectNoAuditOrderByEcOrderId(String ecOrderId);
	//
	public List<PlatOrder> exportPlatOrder(Map map);
	public List<Map> exportList1(Map map);//������
	/**
	 * ���ݶ����ź�sku��ѯ������ϸ�����������ۺ���
	 * @param map
	 * @return
	 */
	public List<PlatOrders> selectNumAndPayMoneyByOrderAndSKu(Map map);
	
	/**
	 * ���ݶ����ź�ecGoodId��ѯ������ϸ�������˿���
	 * @param map
	 * @return
	 */
	public List<PlatOrders> selectNumAndPayMoneyByOrderAndSKuSN(Map map);
	
	public List<PlatOrder> selectByOrderId(String orderId);
	
	/**
	 * ���15�충������ͳ��
	 * @return list
	 */
	public List<Near15DaysOrder> Near15DaysOrder();
	
	/**
	 * �����޸Ŀ�ݹ�˾����
	 */
	
	public void updateExpress(@Param("list") List list,@Param("expressEncd") String expressEncd);
	
	/**
	 * ���ݶ�����������ѯ�����б�
	 * @param OrderIds ƽ̨�������б�
	 * @return
	 */
	public List<PlatOrder> batchList(@Param("list")List<String> list,@Param("isAudit")String isAudit);
	
	
	public int closeOrder(String orderId);

	public int openOrder(String orderId);
	
	public int selectInvNumCountByOrderId(String orderId);
}
