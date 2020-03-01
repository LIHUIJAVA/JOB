package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.RtnGoodsSub;

public interface RtnGoodsSubDao {

	// ɾ���˻���
	Integer deleteRtnGoodsSubByRtnGoodsId(String rtnGoodsId);

	// ���������˻���
	Integer insertRtnGoodsSub(List<RtnGoodsSub> rtnGoodsSubList);

//    ��������
	Integer insertRtnGoodsSubDl(List<String> lists2);

	// �����˻����Ų�ѯ�˻����ӱ���Ϣ
	List<RtnGoodsSub> selectRtnGoodsSubByRtnGoodsId(String rtnGoodsId);

	// ���ݴ����Ϣ���ӱ���Ų�ѯ�˻�������
	BigDecimal selectRtnGoodsSubQty(RtnGoodsSub rtnGoodsSub);

	// �����ӱ�����޸��˻���δ��Ʊ����
	int updateRtnGoodsUnBllgQtyByOrdrNum(Map map);

	// �����ӱ���Ų�ѯ�˻���δ��Ʊ����
	BigDecimal selectRtnGoodsUnBllgQtyByOrdrNum(Map map);

	// �����˻����ӱ���Ų�ѯ�˻��ӱ���Ϣ,���մ�����ʱʹ��
	RtnGoodsSub selectRtnGoodsSubByRtGoIdAndOrdrNum(Map map);

	// ����ʱ�����˻����ż��ӱ�δ��Ʊ����������ѯ�ӱ���Ϣ
	List<RtnGoodsSub> selectRtnGoodsSubByRtnGoodsIdAndUnBllgQty(String rtnGoodsId);

	// �����ӱ���Ų�ѯ�˻�������Դ�ӱ���Ҫ
	RtnGoodsSub selectRtnGoodsSubToOrdrNum(Long ordrNum);

}