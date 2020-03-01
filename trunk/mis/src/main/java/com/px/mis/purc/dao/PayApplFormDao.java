package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.SellComnInv;
import com.px.mis.purc.entity.PayApplForm;
import com.px.mis.purc.service.impl.PayApplFormServiceImpl;
import com.px.mis.purc.service.impl.PayApplFormServiceImpl.zizhu;

public interface PayApplFormDao {
	/** ɾ�����ӱ� */
	int deleteByPrimaryKeyList(List<String> list);

	/** ���� */
	int insertPayApplForm(PayApplForm record);

// ɾ��֮ǰ�����뱸��
	int insertPayApplFormDl(List<String> lists);

	/** ������������ѯ������Ϣ */
	PayApplForm selectByPrimaryKey(String payApplId);

	/** ���ն����������ѯ������Ϣ */
	List<PayApplForm> selectByPrimaryKeyLsit(List<String> list);

	/** �������� */
	int updateByPrimaryKeySelective(PayApplForm record);

	/** �����ӱ����� */
	PayApplForm selectByPrimaryList(String payApplId);

	/** ��ҳ�� */
	List<PayApplForm> selectPayApplFormList(Map map);

// ��ҳ+����
	List<zizhu> selectPayApplFormListOrderBy(Map map);

	/** ��ҳʱ���� */
	int selectPayApplFormCount(Map map);

	/** ��˸������뵥 */
	int updatePayApplFormIsNtChk(PayApplForm payApplForm);

	/** ��ѯ�������뵥���״̬ */
	int selectPayApplIsNtChk(String payApplId);

	/** ��ѯ�������뵥����״̬ */
	int selectPayApplIsNtPay(String payApplId);

	Map selectPayApplFormListSums(Map map);

	List<zizhu> printPayApplFormList(Map map);

	// ����idlist������ѯ
	List<PayApplForm> selectPayApplForms(List<String> idList);

}