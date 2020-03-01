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
	 * �����˿���״̬������1ʱ���ɺ����˻���
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
	//�������۵��е�tx_id�����̶�����list����ѯ���۵��б�
	public List<SellSngl> selectSellSnglsByOrderId(List<PlatOrder> platOrders);
	
	public List<PlatOrder> selectPlatOrderListByEcOrderId(String ecOrderId);
	//�����˿�Ų�ѯ��Ӧ�˻���
	public RtnGoods selectRtnGoodsByRefId(String refId);
	//���ݶ�Ӧ�˻����Ų�ѯ���۳��ⵥ
	public SellOutWhs selectSellOutWhsByRtnId(String rtnId);
	//ƽ̨�˿�Ų�ѯ
	public RefundOrder selectEcRefId(String ecrefId);
	//����ƽ̨�����Ų�ѯ�˿
	public RefundOrder selectByEcOrderId(@Param("ecOrderId")String ecOrderId,
										 @Param("storeId")String storeId);
	
	//����
	public List<Map> exportList(Map map);
	
	//public int selectCountByEcOrderId(String ecOrderId)
	public int selectCountByEcOrderId(String ecOrderId);

}
