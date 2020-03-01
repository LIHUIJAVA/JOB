package com.px.mis.account.dao;

import com.px.mis.account.entity.InvtyDetails;
import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.service.impl.SellComnInvServiceImpl.zizhu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SellComnInvDao {

	// �������۷�Ʊ
	int insertSellComnInv(SellComnInv sellComnInv);

	// �޸����۷�Ʊ
	int updateSellComnInvBySellInvNum(SellComnInv sellComnInv);

	// ����ɾ�����۷�Ʊ
	int deleteSellComnInvBySellInvNum(String sellSnglNum);

	// ����ɾ�����۷�Ʊ
	int deleteSellComnInvList(List<String> sellInvNum);

	// ���շ�Ʊ�����ѯ��Ʊ���ӱ���Ϣ
	SellComnInv selectSellComnInvBySellInvNum(String sellInvNum);

	// ��ҳ��ѯ���۷�Ʊ����
	List<SellComnInv> selectSellComnInvList(Map map);

	int selectSellComnInvCount(Map map);

	// ��ѯ���۷�Ʊ���״̬
	int selectSellComnInvIsNtChk(String sellInvNum);

	// �޸����״̬
	int updateSellComnInvIsNtChk(SellComnInv sellComnInv);

	// ��ѯ����״̬
	int selectSellComnInvIsNtBookEntry(String sellInvNum);

	// �������۷�Ʊ���ţ���ѯ���۷�Ʊ
	String selectSellSnglNumBySellComnInv(String sellInvNum);

	// ����ʱ�������۷�Ʊ����
	int insertSellComnInvUpload(List<SellComnInv> sellComnInv);

	List<SellComnInvSub> selectSellComnInvToQty(Map map);

	// ���ݴ��/�ֿ�/����/�ͻ� sum�ܿ�Ʊ�������ܳ���ɱ�
	List<SellComnInvSub> selectUnBllgQtyAndAmt(Map<String, Object> dataMap);

	// sum��������/���۷�Ʊ-ȡ��Ʊ
	SellComnInvSub sumSellQtyAndAmt(Map map);

	// ������ͳ�Ʊ�ʱ sum���۳ɱ�
	SellComnInvSub sumSellCost(Map<String, Object> dataMap);

	// ��ѯ-������Ʒ��ϸ��
	List<InvtyDetails> selectSellComnDetailList(Map<String, Object> paramMap);

	// ԭ���ĵ����ӿ�,����ɾ
	List<SellComnInv> printingSellComnInvList(Map map);

	// �����ӿ�
	List<zizhu> printSellComnInvList(Map map);

	BigDecimal selectSellComnInvQty(Map map);

	// ���ݵ���list,������ѯ���ӱ����
	List<SellComnInv> selectComnInvs(List<String> idList);

	// �������
	BigDecimal selectSellQtyByStored(Map map);

	// ����U8�ɹ���д״̬
	int updatePushed(String dscode);

}
