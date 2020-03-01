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
	//�����޸ķ����ֿ� ���ŷָ��Ķ��������ֶ�
	public String batchEditWhs(String orderIds,String userId,String whsEncd);
	//�����޸ķ����ֿ� ���ŷָ��Ķ��������ֶ�
	public String batchEditExpress(String orderIds,String userId,String expressCode);
	public String delete(String orderId,String accNum);

	public String query(String orderId);

	public String queryList(Map map);

	public String download(String userId,String startDate,String endDate,int pageNo,int pageSize,String storeId);
	//����������������
	public String jdDownloadByOrderId(String userId,Map map);
	//���������������
	public String downloadByOrderId(String json);
	//���ݶ����ż���ÿ����ϸ��ʵ�����
	public List<PlatOrders> jisuanPayPrice(String orderid);
	
	public String unionQuery(String orderId);
	
	/**
	 * ���ݶ��������sku��spu��ⶩ����ϸ
	 */
	public List<PlatOrders> checkInvty(PlatOrder platOrder,List<PlatOrders> platOrdersList,List<CouponDetail> couponDetails);
	
	
	public String exportPlatOrder(Map map);
	
	public String autoMatch(String orderId,String accNum);
	/**
	 * �����������
	 * @param orderId ����ֵĶ�����
	 * @param platOrdersIds ����ֵĶ�����ϸid�����ŷָ�
	 * @param splitNum ������������ŷָ�
	 * @param ������id
	 * @return
	 */
	public String splitOrder(String orderId,String platOrdersIds,String splitNum,String accNum);
	/**
	 * Ϊ������ϸ�������μ��۶�Ӧ������
	 * @param platOrders ������ϸ
	 * @return map 
	 * flag��trueʱ����ɹ� falseʱ����ʧ�ܣ�����������  
	 * platOrders�� ����ɹ�ʱ���صĶ�����ϸ�б�
	 * message������ʧ��ԭ��
	 */
	public Map checkCanUseNumAndBatch(PlatOrders platOrders);
	
	public String importPlatOrder(MultipartFile  file,String userId);
	
	public String Near15DaysOrder();
	/**
	 * ������ѯ
	 * @param list
	 * @return
	 */
	public String batchSelect(List<String> list,String isAudit);
	/**
	 * ������ϸ�б��ѯ
	 * @param map
	 * @return
	 */
	public String orderssssList(Map map);
	/**
	 * ������ϸ�б���
	 * @param map
	 * @return
	 */
	public String exportOrderssssList(Map map);
	/**
	 * ������ϸ�б�������ѯ
	 * @param list
	 * @return
	 */
	public String batchList(List<String> list);
	/**
	 * ������Ʒ
	 */
	public String updateGooId(PlatOrder platOrder,String invId,String invIdLast,String multiple,List<PlatOrders> platOrders);
	
	public String selectecGooId(List<String> list);
	
	public PlatOrder selectecByorderId(String orderId);
	
	public List<PlatOrders> selectecByorderIds(String orderId);
	
	/**
	 * �����ر�
	 * @param orderId �Զ��ŷָ��Ķ�����
	 * @param accNum
	 * @return
	 */
	public String closeOrder(String orderId,String accNum);
	

	/**
	 * ������
	 * @param orderId �Զ��ŷָ��Ķ�����
	 * @param accNum
	 * @return
	 */
	public String openOrder(String orderId,String accNum);


}
