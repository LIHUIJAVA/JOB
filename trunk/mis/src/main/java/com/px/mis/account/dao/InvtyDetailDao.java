package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyDetails;
import com.px.mis.account.entity.SellOutWhsPool;
import com.px.mis.purc.entity.SellOutWhs;

public interface InvtyDetailDao {
	//��ѯ�ô����Ӧ��ϸ�Ƿ����
	List<InvtyDetail> selectByInvty(Map<String, Object> paramMap);
    //���Ӵ����ϸ
	int insertInvtyDetail(InvtyDetail invty);
	int insertInvtyDetailList(List<InvtyDetail> invty);
	
	//����������ϸ�ӱ�
	int insertInvtyDetailsList(List<InvtyDetails> list);
	//����ɾ����ϸ���ӱ�
	int delectInvtyList(List<InvtyDetails> invtyDetailList);
	//����������ϸ�ӱ�
	int insertInvtyDetails(InvtyDetails invtys);
	//��ѯ�շ������
	List<InvtyDetail> selectSendAndRecePool(Map map);
	//�����շ������
	int countSelectSendAndRecePool(Map map);
	//��ѯ������Ʒ����
	List<SellOutWhsPool> sendProductsPool(Map map);
	//���㷢����Ʒ����
	int countSendProductsPool(Map map);
	//��ѯ������Ʒ
	List<SellOutWhsPool> sendProductList(Map map);
	//���㷢����Ʒ
	List<SellOutWhsPool> countSendProductList(Map map);
	
	//��ѯ�����ϸ
	List<InvtyDetail> selectByInvtyDeatil(Map<String, Object> paramMap);
	
	List<InvtyDetail> selectByInvtyDeatilsList(Map<String, Object> paramMap);
	//������-��ѯ�ڳ�
	List<InvtyDetail> selectBeginDataByMap(Map map);
	//������-��ѯ�ڳ�����
	int selectBeginDataByMapCount(Map map);
	//sum �ڳ�����
	InvtyDetail sumQtyAndAmtByInvtyEncd(Map map);
	
	
}
