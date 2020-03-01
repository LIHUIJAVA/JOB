package com.px.mis.ec.dao;

import java.util.List;

import com.px.mis.ec.entity.CouponDetail;

public interface CouponDetailDao {
	public int insert(List<CouponDetail> couponlist);
	
	public void delete(String orderId);
	/**
	 * ����ƽ̨id��ƽ̨�����Ų�ѯ�Żݽ��
	 * @param platId ƽ̨id
	 * @param orderid ƽ̨������
	 * @param skuid sku��� 
	 * @param type ��ѯ���� 1����ѯ��Ʒ���� ��Ե�Ʒ�ۼ�ʵ�����  2����ѯ���������Ż� ��̯����ϸ��
	 * @return ���count
	 */
	public int selectByOrderId(String platId,String orderid,String skuid,int type);
	
	/**
	 * ����ƽ̨�����Ų�ѯ�Ż���ϸ�б�
	 * @param ecOrderId ƽ̨������
	 * @return List
	 */
	public List<CouponDetail> couponDetailList(String ecOrderId);

}
